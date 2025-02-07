package com.example.chatserver.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class AccessTokenDTO {
    private String accessToken;
    private String tokenType;
}