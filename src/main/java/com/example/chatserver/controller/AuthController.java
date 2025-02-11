package com.example.chatserver.controller;

import com.example.chatserver.domain.User;
import com.example.chatserver.domain.UserCreateForm;
import com.example.chatserver.dto.AccessTokenDTO;
import com.example.chatserver.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
//import static com.example.chatserver.domain.QUser.user;
// todo : 관리자 페이지
// Spring Security 에서 유저 Role 정보를 읽기 위한 import
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    // 회원가입 페이지 표시
    @GetMapping("/signup")
    public String signup(
            UserCreateForm userCreateForm
    ) {
        return "signup_form";
    }

    // 회원가입: 간단하게 User 엔티티를 입력받아 저장 (실제 구현 시 DTO, 검증, 이메일 전송 등을 추가)
    @PostMapping("/signup")
    public String signup(@ModelAttribute @Valid UserCreateForm userCreateForm, BindingResult bindingResult) {
        // 1. 입력값 검증
        if (bindingResult.hasErrors()) {
            return "signup_form";
        }
        // 2. 비밀번호 확인 검사
        if (!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())) {
            bindingResult.rejectValue(
                    "password2",
                    "passwordDoubleCheckError",
                    "패스워드 확인 값이 일치하지 않습니다."
            );
            return "signup_form";
        }
        // 3. 회원가입 처리 - 백엔드 validation
        try {
            authService.create(
                    userCreateForm.getLoginId(),
                    userCreateForm.getPassword1(),
                    userCreateForm.getEmail(),
                    userCreateForm.getNickName());
        } catch (IllegalStateException e) {
            bindingResult.reject(
                    "signupFailed",
                    "이미 등록된 사용자 입니다."
            );
            return "signup_form";
        } catch (Exception e) {
            bindingResult.reject(
                    "signupFailed",
                    e.getMessage()
            );
            return "signup_form";
        }
        // 4. 회원 가입 성공
        return "redirect:/";
    }

    /**
     * 로그인 엔드포인트
     * 클라이언트는 로그인 ID와 비밀번호를 파라미터로 전송합니다.
     *
     * @param loginId  로그인 ID
     * @param password 비밀번호 (원시 값)
     * @return JWT 토큰을 포함한 AccessTokenDTO, 로그인 실패 시 에러 메시지
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String loginId, @RequestParam String password) {
        User loginUser = new User();
        loginUser.setLoginId(loginId);
        // 전달받은 원시 비밀번호를 사용 (AuthService 에서 passwordEncoder.matches 로 검증)
        loginUser.setPassword(password);

        AccessTokenDTO tokenDTO = authService.getAccessToken(loginUser);
        if (tokenDTO == null) {
            return ResponseEntity.badRequest().body("로그인 실패: 로그인 정보가 올바르지 않습니다.");
        }
        return ResponseEntity.ok(tokenDTO);
    }

    @GetMapping("/login")
    public String login() {
        return "login_form";
    }

    // 추가: 이메일 인증, 비밀번호 재설정 등 엔드포인트 구현 가능


}
