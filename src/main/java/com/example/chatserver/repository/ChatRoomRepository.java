package com.example.chatserver.repository;

import com.example.chatserver.domain.ChatRoom;
import com.example.chatserver.domain.GroupChatRoom;
import com.example.chatserver.domain.OneToOneChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    // 바텀 네비게이션 2번째 아이템을 클릭했을 때, 개인 채팅방 목록을 조회하는 메소드
    // user_id 를 기반으로 OneToOneChatRoom 을 조회하는 메소드
    Optional<OneToOneChatRoom> findOneToOneChatRoomByUserId(Long userId);


    // 바텀 네비게이션 3번째 아이템을 클릭했을 때, 그룹 채팅방 목록을 조회하는 메소드
    // user_id 를 기반으로 GroupChatRoom 을 조회하는 메소드
    Optional<GroupChatRoom> findGroupChatRoomByUserId(Long userId);
}
