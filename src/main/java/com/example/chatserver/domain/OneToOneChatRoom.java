package com.example.chatserver.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@DiscriminatorValue("ONE_TO_ONE")
public class OneToOneChatRoom extends ChatRoom {

    // 1대1 채팅의 경우 추가 필드는 추후 생각, 참여자 수가 2명임을 전제로 함

}
