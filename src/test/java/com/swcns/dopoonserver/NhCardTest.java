package com.swcns.dopoonserver;

import com.swcns.dopoonserver.global.infra.nh.service.NhCardService;
import com.swcns.dopoonserver.global.infra.nh.service.NhRegisterService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
public class NhCardTest {
    @Autowired
    private NhRegisterService nhRegisterService;

    @Autowired
    private NhCardService nhCardService;

    private final String BIRTHDAY = "20050128";
    private final String CARD_NUMBER = "9411123456782718";
    private String registerNumber;
    private String cardNumber;

    @Order(0)
    @DisplayName("카드 발급 요청 API")
    @Test
    void requestCardNumber() {
        String confirmCode = nhRegisterService.requestRegisterCard(BIRTHDAY, CARD_NUMBER);
        System.out.println(confirmCode);

        this.registerNumber = confirmCode;
    }

    @Order(1)
    @DisplayName("카드 발급 확인 API")
    @Test
    void confirmCardNumber() {
        String response = nhRegisterService.confirmRegisterCard(BIRTHDAY, this.registerNumber);
        System.out.println(response);

        this.cardNumber = response;
    }

    @Order(2)
    @DisplayName("카드 사용기록 조회 API")
    @Test
    void confirmCardHistory() {
        nhCardService.getBills(1, this.cardNumber).forEach(System.out::println);
    }
}
