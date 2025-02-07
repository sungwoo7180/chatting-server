package com.example.chatserver.repository;

public interface UserRepository {

    // 유저 정보를 조회하는 메소드
    // user_id 를 기반으로 User 를 조회하는 메소드
    public void findUserByUserId(Long userId);


}
