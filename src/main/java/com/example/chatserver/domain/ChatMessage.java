package com.example.chatserver.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter 
@Setter
@Table(name = "chat_messages")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 채팅 메시지 내용
    @Column(nullable = false, length = 1000)
    private String content;

    // 메시지 전송 시각
    @Column(nullable = false)
    private LocalDateTime sentAt;

    // 발신자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    // 메시지가 속한 채팅방 (그룹/1대1 모두 해당)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    // M대N 관계 해소를 위한 중계 테이블로 대체함.
/*    // 해당 메시지를 읽은 사용자 목록 (읽음 상태를 파악) ->
//    @ManyToMany
//    @JoinTable(
//        name = "chat_message_reads",
//        joinColumns = @JoinColumn(name = "chat_message_id"),
//        inverseJoinColumns = @JoinColumn(name = "user_id")
//    )
//    private Set<User> readBy = new HashSet<>(); */

    @PrePersist
    protected void onCreate() {
        sentAt = LocalDateTime.now();
    }
}