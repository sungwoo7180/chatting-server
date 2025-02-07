package com.example.chatserver.service;

import com.example.chatserver.config.constant.UserRole;
import com.example.chatserver.config.jwt.TokenProvider;
import com.example.chatserver.domain.ChatUserPrincipal;
import com.example.chatserver.domain.User;
import com.example.chatserver.dto.AccessTokenDTO;
import com.example.chatserver.repository.AuthRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional // All or Nothing -> 실패 시 발생하는 예외 처리를 위해 사용
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    /**
     * 로그인 시, loginId를 기반으로 사용자 정보를 로드합니다.
     *
     * @param loginId 로그인 ID
     * @return UserDetails 객체 (ChatUserPrincipal)
     * @throws UsernameNotFoundException 사용자 미발견 시 예외 발생
     */
    // 로그인 전용 메서드 Override
    @Override
    public UserDetails loadUserByUsername(
            String loginId  // 로그인 ID 를 말함
    ) throws UsernameNotFoundException {
        Optional<User> registeredUser = authRepository.findByLoginId(loginId);
        if (registeredUser.isEmpty()) {
            throw new UsernameNotFoundException(loginId + " 사용자를 찾을 수 없습니다.");
        }
        User user = registeredUser.get();
        return ChatUserPrincipal.fromUserEntity(user);
    }

    // CRUD 기능 구현
    public void create(
            String loginId,
            String password1,
            String email
    ) {
        User newUser = new User();
        newUser.setLoginId(loginId);
        newUser.setPassword(
                passwordEncoder.encode(password1)
        );
        newUser.setEmail(email);
        newUser.setRole(UserRole.USER);  // Enum default validation 필수 체크됨

        // 중복 유저 체크
        validateDuplicateUser(newUser);
        User savedUser = authRepository.save(newUser);
    }

    // 중복 유저 검사 메서드 선언
    public void validateDuplicateUser(User user) {
        // login id 중복 검사
        if (existsByLoginId(user.getLoginId())) {
            throw new IllegalStateException("이미 존재하는 ID 입니다.");
        }
        // email 중복 검사
        if (existsByEmail(user.getEmail())) {
            throw new IllegalStateException("이미 존재하는 Email 입니다.");
        }
    }

    public boolean existsByLoginId(String loginId) {
        return authRepository.existsByLoginId(loginId);
    }

    public boolean existsByEmail(String username) {
        return authRepository.existsByEmail(username);
    }

    public AccessTokenDTO getAccessToken(User user) {
        // 1) Spring Security 로그인 전용 메서드 loadUserByUsername 사용해 인증
        UserDetails userDetails;
        try {
            userDetails = loadUserByUsername(user.getEmail());
        } catch (Exception e) {
            return null;
        }
        // 2) UserService 에 TokenProvider 주입 -> Done
        // 3) TokenProvider 에서 Token String 을 생성
        //    - 비밀번호 체크
        if (passwordEncoder.matches(user.getPassword(), userDetails.getPassword())) {
            // 4) AccessTokenDTO 로 Wrapping 및 리턴
            String accessToken = tokenProvider.generateToken(user, Duration.ofHours(1L));
            String tokenType = "Bearer";
            return new AccessTokenDTO(
                    accessToken, tokenType
            );
        }
        return null;
    }

    public List<User> makeDummyData(int count) {
        // 1) 기존에 유저 수가 10명 이상 이미 생성 되어 있으면
        //    skip 하고 기존 리스트 반환
        List<User> users = authRepository.findAll();
        if (users.size() >= 10) {
            return users;
        }
        // 2) 유저 타입별 count 개수 만큼 생성
        for (UserRole role : UserRole.values()) {
            for (int i= 1; i <= count; i++) {
                User newUser = new User();
                newUser.setLoginId(role.name() + i);
                newUser.setPassword(passwordEncoder.encode("1234"));
                newUser.setEmail(role.name() + i + "@tt.cc");
                newUser.setRole(role);
                users.add(newUser);
            }
        }
        return authRepository.saveAll(users);
    }

    public List<User> getUsersByRole(UserRole role) {
        // 아래 validation 은 불필요
//        if (role == null || role.equals(UserRole.USER)) {
//            return null;
//        }
        List<UserRole> roles = new ArrayList<>();
        for (int i = role.ordinal(); i < UserRole.values().length; i++) {
            roles.add(UserRole.values()[i]);
        }
        List<User> users = new ArrayList<>();
        for (UserRole targetRole : roles) {
            users.addAll(authRepository.findByRole(targetRole));
        }
        return users;
    }

    public Page<User> getUsersPageByRole(UserRole role, int page, int size) {
        // 아래 validation 은 불필요
//        if (role == null || role.equals(UserRole.USER)) {
//            return null;
//        }
        List<UserRole> roles = new ArrayList<>();
        for (int i = role.ordinal(); i < UserRole.values().length; i++) {
            roles.add(UserRole.values()[i]);
        }
        List<User> users = new ArrayList<>();
        for (UserRole targetRole : roles) {
            users.addAll(authRepository.findByRole(targetRole));
        }
        // List -> Page 변환
        Pageable pageable = PageRequest.of(page, size);
        int start = Math.min((int) pageable.getOffset(), users.size());  // 시작 인덱스를 잡기 위한 기준점 설정
        int end = Math.min((start + pageable.getPageSize()), users.size());  // 종료 인덱스를 잡기 위한 기준점 설정
        // DB 상의 인덱스가 아니라, List 의 인덱스를 기준으로 자르기
        List<User> usersPageContent = users.subList(start, end);

        // Page 객체 생성 및 반환
        return new PageImpl<>(usersPageContent, pageable, users.size());
    }


    @Transactional
    public List<User> getUserListWithFetchedOrders() {
        // 1) user 리스트 조회, Lazy 필드 후속 조회
        List<User> users = authRepository.findAll();
        assert !users.isEmpty();
        for (User user : users) {
            // Lazy Loading 방식의 데이터 Fetch 수행
            // 메인 엔티티 로드 후, + N 추가 쿼리를 수동 실행
//            Hibernate.initialize(user.getOrders());
        }
        return users;
    }
}
