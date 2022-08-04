package com.swcns.dopoonserver.domain.auth.presentation.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SignInRequest {
    @ApiModelProperty("이메일 또는 로그인 아이디")
    @JsonProperty("email_or_id")
    private final String emailOrId;

    @ApiModelProperty("비밀번호")
    private final String password;
}
