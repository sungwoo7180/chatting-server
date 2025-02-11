package com.example.chatserver.controller;

import com.example.chatserver.repository.ChatRoomRepository;
import com.example.chatserver.service.ChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/chatroom")
public class ChatRoomController {


    private final ChatRoomService chatRoomService;

    @Autowired
    private final ChatRoomRepository chatRoomRepository;

    public ChatRoomController(ChatRoomService chatRoomService, ChatRoomRepository chatRoomRepository) {
        this.chatRoomService = chatRoomService;
        this.chatRoomRepository = chatRoomRepository;
    }


    // user_id 를 기반으로 OneToOneChatRoom 을 조회하는 메소드
    public void findOneToOneChatRoomByUserId(Long userId) {

    }

}
