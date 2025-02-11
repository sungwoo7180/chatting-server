package com.example.chatserver.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UserCreateRequestDTO {

    @Size(min = 3, max = 25)
    @NotEmpty(message = "로그인 ID는 필수항목입니다.")
    private String loginId;

    @NotEmpty(message = "닉네임은 필수항목입니다.")
    private String nickName;

    @NotEmpty(message = "비밀번호는 필수항목입니다.")
    private String password1;

    @NotEmpty(message = "비밀번호 확인은 필수항목입니다.")
    private String password2;

    @NotEmpty(message = "이메일은 필수항목입니다.")
    @Email
    private String email;

    public boolean isPasswordMatch() {
        return password1.equals(password2);
    }
}
