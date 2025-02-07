package com.example.chatserver.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * ChatUserPrincipal 은 Spring Security 의 UserDetails 를 구현하는 클래스입니다.
 * 이 클래스는 Spring Security 에서 인증 시 사용하는 User 객체의 역할을 합니다.
 * 현재 도메인에 User 엔티티가 존재하므로, 의도적으로 Spring Security 의 User 클래스를 상속받기 위해
 * 전체 패키지명을 명시합니다.
 */
public class ChatUserPrincipal extends org.springframework.security.core.userdetails.User {

    public ChatUserPrincipal(String loginId, String password, Collection<? extends GrantedAuthority> authorities) {
        super(loginId, password, authorities);
    }

    public ChatUserPrincipal(String loginId, String password, boolean enabled, boolean accountNonExpired,
                             boolean credentialsNonExpired, boolean accountNonLocked,
                             Collection<? extends GrantedAuthority> authorities) {
        super(loginId, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }

    /**
     * 도메인 User 엔티티로부터 UserDetails 객체를 생성하는 정적 팩토리 메서드.
     * 여기서는 appUser 의 loginId를 username 으로 사용합니다.
     *
     * @param appUser 도메인 User 엔티티
     * @return UserDetails 객체
     */
    public static UserDetails fromUserEntity(com.example.chatserver.domain.User appUser) {
        // 실제 권한 변환 로직이 필요하면 appUser.getRole()를 기반으로 GrantedAuthority 목록을 생성합니다.
        // 여기서는 단순화를 위해 빈 리스트를 사용합니다.
        return new ChatUserPrincipal(
                appUser.getLoginId(),
                appUser.getPassword(),
                Collections.emptyList()
        );
    }
}
