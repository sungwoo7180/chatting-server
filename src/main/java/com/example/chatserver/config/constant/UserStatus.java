package com.example.chatserver.config.constant;

public enum UserStatus {
    ACTIVE,         // 활성 계정
    SUSPENDED,      // 정지된 계정
    BANNED,         // 영구 정지 계정
    DELETED,        // 삭제된 계정
    DELETING        // 삭제 진행중
}
