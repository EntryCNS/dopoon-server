package com.swcns.dopoonserver.domain.fintech.presentation;

import com.swcns.dopoonserver.domain.fintech.service.FintechService;
import com.swcns.dopoonserver.global.exception.defaults.RedirectFailedException;
import com.swcns.dopoonserver.global.utils.csrf.CsrfToken;
import com.swcns.dopoonserver.global.utils.csrf.ValidCsrf;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@Api("핀테크 API")
@RequiredArgsConstructor
@RequestMapping("/fintech")
@RestController
public class FintechController {
    private final FintechService fintechService;

    @ApiOperation("핀테크 O-Auth 인증 URL로 리다이렉트합니다")
    @GetMapping("/oauth")
    public void redirectOAuthUrl(HttpServletResponse response) {
        try {
            response.sendRedirect(fintechService.getOAuthUrl());
        }catch (Exception ex) {
            throw new RedirectFailedException();
        }
    }

    @ApiOperation("핀테크 계좌를 등록합니다")
    @ResponseStatus(HttpStatus.CREATED)
    @ValidCsrf
    @GetMapping("/register")
    public void registerFintech(@RequestParam("code") String code, @RequestParam("client_info") String jwtString, @RequestParam("state") @CsrfToken String csrf) {
        fintechService.registerFintech(code, jwtString);
    }
}
