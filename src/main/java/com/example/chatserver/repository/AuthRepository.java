package com.example.chatserver.repository;

import com.example.chatserver.config.constant.UserRole;
import com.example.chatserver.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthRepository extends JpaRepository<User, Long> {

    // 1. 로그인 ID로 사용자 조회 ( 로그인 또는 중복 체크 시 사용 )
    Optional<User> findByLoginId(String loginId);


    // 2. 회원가입시 중복 체크용 : 로그인 ID, 닉네임 이메일
    boolean existsByLoginId(String loginId);
    boolean existsByNickName(String nickName);
    boolean existsByEmail(String email);

    // 이메일로 사용자 조회 ( 로그인 또는 중복 체크 시 사용 )
    Optional<User> findByEmail(String email);

    // 역할별 사용자 목록 조회 (예: 관리자, 일반 사용자 등)
    List<User> findByRole(UserRole role);

//    // 역할별 사용자 목록 조회 (예: 관리자, 일반 사용자 등)
//    Collection<? extends User> findByRole(UserRole targetRole) {
//        return null;
//    }

}
