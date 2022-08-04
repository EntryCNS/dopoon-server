package com.swcns.dopoonserver.domain.auth.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class SignInResponse {
    @ApiModelProperty("액세스 토큰")
    @JsonProperty("access_token")
    private final String accessToken;
}
