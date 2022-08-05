package com.swcns.dopoonserver;

import com.swcns.dopoonserver.global.infra.finance.service.OpenFinanceService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
public class AccountTest {
    @Autowired
    private OpenFinanceService openFinanceService;

    private final String CODE = "c0TCdsUn8fDKMt0UEYeCwoW6iEeO98";
    private OpenFinanceService.TokenAndUserNumber token;
    private OpenFinanceService.UserFinanceDetail financeDetail;

    @Order(0)
    @DisplayName("사용자 토큰 가져오기")
    @Test
    void getUserToken() {
        OpenFinanceService.TokenAndUserNumber response = openFinanceService.getTokenAndUserNumberByCode(CODE)
                .orElseThrow();

        System.out.println(response);
        this.token = response;
    }

    @Order(1)
    @DisplayName("계좌목록 조회")
    @Test
    void confirmCardNumber() {
        OpenFinanceService.UserFinanceDetail response = openFinanceService.getUserFinanceDetail(token)
                .orElseThrow();

        System.out.println(response);
        this.financeDetail = response;
    }
}
