package com.example.chatserver.domain;

import com.example.chatserver.config.constant.UserRole;
import com.example.chatserver.config.constant.UserStatus;
import com.example.chatserver.dto.UserCreateRequestDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
// import org.springframework.data.annotation.CreatedBy;
// import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.Collection;

@Entity
@Getter @Setter
@Table(name = "users")      // 테이블명은 예약어를 피하기 위해 단순 user 로 사용하지 않기
public class User {

    // 사용자 엔티티 – 여러 채팅방에 참여할 수 있음


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // IDENTITY 전략을 사용하여 자동 생성
    private Long id;


    @Column(nullable = false, unique = true)
    private String loginId;


    @Column(nullable = false, unique = true)
    private String nickName;


    @Column(nullable = false, unique = true)
    private String email;


    @Column(nullable = false)
    private String password;


    @Column
    private String profileImageUrl;


    @Column(nullable = false, updatable = false)
    // @CreatedDate        // Entity 가 생성되어 저장될 때 시간이 자동 저장, Spring Data JPA 의 Auditing 기능을 활성화
    private LocalDateTime createdAt;


    // 삭제된 시점을 저장하기 위한 필드 -> 1달 유예
    @Column
    private LocalDateTime deletedAt;

    // 권한
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    // 유저 상태
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;

    public User(String loginId, String password, Collection<? extends GrantedAuthority> authorities) {
    }

    public User() {

    }

    public User(UserCreateRequestDTO dto, String encodedPassword) {
        this.loginId = dto.getLoginId();
        this.nickName = dto.getNickName();
        this.email = dto.getEmail();
        this.password = encodedPassword;
        this.createdAt = LocalDateTime.now();
        this.role = UserRole.USER;
        this.status = UserStatus.ACTIVE;
    }

    // 엔티티가 생성될 때 -> 현재 날짜를 값으로 설정
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        deletedAt = null;

        if ( role == null ) {
            role = UserRole.USER; // 기본 역할 설정
        }
        if ( status == null ) {
            status = UserStatus.ACTIVE; // 기본 상태 설정
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", loginId='" + loginId + '\'' +
                ", nickName='" + nickName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", profileImageUrl='" + profileImageUrl + '\'' +
                ", createdAt=" + createdAt +
                ", deletedAt=" + deletedAt +
                '}';
    }
}

