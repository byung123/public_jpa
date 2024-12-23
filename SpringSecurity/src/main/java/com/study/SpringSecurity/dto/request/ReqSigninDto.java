package com.study.SpringSecurity.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class ReqSigninDto {
//    @Pattern(regexp = "^[a-z0-9]{6,}$", message = "사용자이름은 6자이상의 영소문자, 숫자 조합이어야합니다.")
    @NotBlank(message = "사용자 이름을 입력하세요") // 빈 값만 아니면 요청은 되게끔 해주는 어노테이션, message 안하면 기본 오류메세지 응답
    private String username;
//    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[~!@#$%^&*?])[A-Za-z\\d~!@#$%^&*?]{8,16}$"
//    , message = "비밀번호는 8자이상의 16자 이하의 영대소문, 숫자, 특수문자(~!@#$%^&*?)를 포함해야합니다.")
    @NotBlank(message = "비밀번호를 입력하세요")
    private String password;
}
