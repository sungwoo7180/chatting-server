package com.example.chatserver.repository;

import com.example.chatserver.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // login_id 를 기반으로 User 를 조회하는 메소드
    Optional<User> findByLoginId(String loginId);

    // 이메일을 기반으로 User 를 조회하는 메소드
    Optional<User> findByEmail(String email);


    Optional<User> findFirstByOrderByIdDesc();

}
