package com.example.chatserver.controller;

import com.example.chatserver.service.MainPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainPageController {
    private final MainPageService mainPageService;

    @GetMapping("/")
    public String mainPage() {
        return mainPageService.getMainPage(); // index 뷰 네임 리턴
    }
}