package com.swcns.dopoonserver.domain.auth.presentation;

import com.swcns.dopoonserver.domain.auth.presentation.dto.request.SignInRequest;
import com.swcns.dopoonserver.domain.auth.presentation.dto.request.SignupRequest;
import com.swcns.dopoonserver.domain.auth.presentation.dto.response.SignInResponse;
import com.swcns.dopoonserver.domain.auth.presentation.service.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api("인증 관련 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @ApiOperation(value = "회원가입")
    @PostMapping("/sign-up")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void signUp(@RequestBody @Valid SignupRequest signupRequest) {
        authService.signUp(signupRequest);
    }

    @ApiOperation(value = "로그인 및 액세스 토큰 발급")
    @PostMapping("/sign-in")
    public SignInResponse signIn(@RequestBody SignInRequest signInRequest) {
        return authService.signIn(signInRequest);
    }
}
