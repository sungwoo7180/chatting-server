package com.example.chatserver.config.constant;

public enum UserRole {
    SUPER_ADMIN,    // 최고 관리자 - 모든 유저 CRUD 권한 보유, 중관 관리자 관리
    ADMIN,          // 중관 관리자 - 채팅방 메세지 신고 내역 확인 및 제재
    USER            // 일반 사용자
}
