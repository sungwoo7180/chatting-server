package com.example.chatserver.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
// import org.springframework.data.annotation.CreatedBy;
// import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@Table(name = "users")
public class User {

    // 사용자 엔티티 – 여러 채팅방에 참여할 수 있음


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // IDENTITY 전략을 사용하여 자동 생성
    private Long id;


    @Column(nullable = false, unique = true)
    private String loginId;


    @Column(nullable = false)
    private String nickName;


    @Column(nullable = false, unique = true)
    private String email;


    @Column(nullable = false)
    private String password;


    @Column
    private String profileImageUrl;


    @Column(nullable = false)
    // @CreatedDate        // Entity 가 생성되어 저장될 때 시간이 자동 저장, Spring Data JPA 의 Auditing 기능을 활성화
    private LocalDateTime createdAt;


    // 삭제된 시점을 저장하기 위한 필드 -> 1달 유예
    @Column
    private LocalDateTime deletedAt;


    // 엔티티가 생성될 때 -> 현재 날짜를 값으로 설정
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        deletedAt = null;
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

