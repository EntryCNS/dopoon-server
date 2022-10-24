package com.swcns.dopoonserver.domain.fintech.service;

import com.swcns.dopoonserver.domain.card.entity.Card;
import com.swcns.dopoonserver.domain.card.repository.CardRepository;
import com.swcns.dopoonserver.domain.user.entity.FintechToken;
import com.swcns.dopoonserver.domain.user.entity.User;
import com.swcns.dopoonserver.domain.user.exception.UserNotFoundException;
import com.swcns.dopoonserver.domain.user.facade.UserFacade;
import com.swcns.dopoonserver.domain.user.repository.FintechTokenRepository;
import com.swcns.dopoonserver.domain.user.repository.UserRepository;
import com.swcns.dopoonserver.global.infra.finance.FinanceProperties;
import com.swcns.dopoonserver.global.infra.finance.service.OpenFinanceService;
import com.swcns.dopoonserver.global.security.JwtTokenProvider;
import com.swcns.dopoonserver.global.utils.csrf.CsrfService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.swcns.dopoonserver.global.infra.finance.service.OpenFinanceService.*;

@Log4j2
@RequiredArgsConstructor
@Service
public class FintechService {
    private final FinanceProperties properties;
    private final OpenFinanceService openFinanceService;
    private final CsrfService csrfService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserFacade userFacade;
    private final UserRepository userRepository;
    private final FintechTokenRepository fintechTokenRepository;
    private final CardRepository cardRepository;

    public String getOAuthUrl() {
        User user = userFacade.queryCurrentUser()
                .orElseThrow(UserNotFoundException::new);

        StringBuilder builder = new StringBuilder("https://testapi.openbanking.or.kr/oauth/2.0/authorize?");
        builder.append("response_type=code&");
        builder.append(String.format("client_id=%s&", properties.getClientId()));
        builder.append(String.format("redirect_uri=%s&", properties.getRedirectUri()));
        builder.append("scope=login inquiry&");
        builder.append(String.format("state=%s&", csrfService.generateToken()));
        builder.append("auth_type=0&");
        builder.append(String.format("client_info=%s&", jwtTokenProvider.generateExternalApiToken(user.getEmail())));
        builder.append("cellphone_cert_yn=Y&");
        builder.append("authorized_cert_yn=N&");
        builder.append("account_hold_auth_yn=N");

        return builder.toString();
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public void registerFintech(String code, String jwtString) {
        log.info("Card registration");

        User user = userRepository.findById(((User) jwtTokenProvider.authentication(jwtString).getPrincipal()).getId())
                .orElseThrow(UserNotFoundException::new);
        if(user.getFintechToken() != null) {
            user.clearFintechToken();
            fintechTokenRepository.delete(user.getFintechToken());
        }

        TokenAndUserNumber tokenInfo = openFinanceService.getTokenAndUserNumberByCode(code)
                .orElseThrow();
        FintechToken fintechToken = FintechToken.builder()
                .id(user.getId())
                .user(user)
                .userNumber(tokenInfo.getUserNumber())
                .accessToken(tokenInfo.getAccessToken())
                .refreshToken(tokenInfo.getRefreshToken())
                .build();
        user.setFintechToken(fintechTokenRepository.save(fintechToken));

        UserFinanceDetail financeInfo = openFinanceService.getUserFinanceDetail(tokenInfo)
                .orElseThrow();

        List<String> originalCardList = user.getCards().stream().map(Card::getCardNumber).collect(Collectors.toList());
        List<Card> cards = financeInfo.getAccounts().stream()
                .map(account -> Card.builder()
                        .cardNumber(account.getFintechUseNumber())
                        .billList(new ArrayList<>())
                        .companyName(account.getBankName())
                        .nickname(account.getNickname())
                        .owner(user)
                        .build())
                .filter(it -> !originalCardList.contains(it.getCardNumber())) // 새로운 카드만 등록
                .collect(Collectors.toList());

        user.addCards(cardRepository.saveAll(cards));
    }

}
