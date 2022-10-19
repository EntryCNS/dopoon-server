package com.swcns.dopoonserver.domain.auth.presentation.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@Getter
@AllArgsConstructor
public class SignupRequest {

    @ApiModelProperty("이메일 주소")
    @Email(message = "올바른 이메일을 입력해야 합니다")
    private final String email;

    @ApiModelProperty("이름 (실명)")
    @Length(min=2, message = "이름은 2자리 이상이여야 합니다")
    private final String name;

    @ApiModelProperty("비밀번호")
    @Length(min = 8, message = "비밀번호는 8자리 이상이여야 합니다")
    private final String password;

    @ApiModelProperty("생년월일 (yyyyMMdd 형식)")
    @Length(min = 8, max = 8, message = "생년월일은 8자리로 구성됩니다")
    @Pattern(regexp = "\\d+", message = "올바르지 않은 생년월일 형식입니다")
    @JsonProperty("birthday")
    private final String birthDay;
}
