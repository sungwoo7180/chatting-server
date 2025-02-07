package com.example.chatserver.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/* ================================
   공통 추상 클래스: ChatRoom
   SINGLE_TABLE 상속 전략을 사용하여 그룹 채팅과 1대1 채팅을 구분합니다.
   Discriminator 컬럼("chat_room_type")을 통해 타입을 식별합니다.
   ================================ */

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "chat_room_type", discriminatorType = DiscriminatorType.STRING)
@Getter
@Setter
public abstract class ChatRoom {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 채팅방 생성 시점
    @Column(nullable = false)
    private LocalDateTime createdAt;

    // 채팅방에 속한 메시지들 ( 정렬 기준은 전송 시각 ASC )
    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("sentAt ASC")
    private List<ChatMessage> messages = new ArrayList<>();

    // 채팅방에 참여한 사용자(중계 엔티티를 통한 1:N 관계)
    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ChatRoomUser> chatRoomUsers = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }


}
