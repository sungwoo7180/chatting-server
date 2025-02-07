package com.example.chatserver.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter @Setter
@DiscriminatorValue("GROUP")
public class GroupChatRoom extends ChatRoom {

    // 채팅방 이름
    @Column(nullable = false)
    private String roomName;

    // 선택적 비밀번호 ( 입장시 검증 용도 )
    @Column
    private String Password;


    // 그룹 채팅방의 방장(채팅방 생성자 혹은 지정된 사용자)
    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;




}
