package com.swcns.dopoonserver.global.infra.finance.service;

import com.swcns.dopoonserver.global.infra.finance.FinanceProperties;
import com.swcns.dopoonserver.global.infra.finance.http.FinanceTokenHttpApi;
import com.swcns.dopoonserver.global.infra.finance.http.FinanceUserHttpApi;
import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import retrofit2.Response;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.swcns.dopoonserver.global.infra.finance.http.FinanceTokenHttpApi.*;
import static com.swcns.dopoonserver.global.infra.finance.http.FinanceUserHttpApi.*;

@Log4j2
@RequiredArgsConstructor
@Service
public class OpenFinanceService {
    private final FinanceProperties financeProperties;
    private final FinanceTokenHttpApi financeTokenHttpApi;
    private final FinanceUserHttpApi financeUserHttpApi;

    @ToString
    @AllArgsConstructor @Builder @Getter
    public static class TokenAndUserNumber {
        private String accessToken;
        private String refreshToken;
        private String userNumber;
    }

    public Optional<TokenAndUserNumber> getTokenAndUserNumberByCode(String code) {
        try {
            Response<FinanceTokenResponse> response = financeTokenHttpApi.getTokenByCode(code,
                    financeProperties.getClientId(),
                    financeProperties.getClientSecret(),
                    financeProperties.getRedirectUri(),
                    "authorization_code").execute();

            return Optional.of(TokenAndUserNumber.builder()
                    .accessToken(response.body().getAccessToken())
                    .refreshToken(response.body().getRefreshToken())
                    .userNumber(response.body().getUserSeqNo())
                    .build());
        }catch (Exception ex) {
            ex.printStackTrace();
            return Optional.empty();
        }
    }

    @ToString
    @AllArgsConstructor @Builder @Getter
    public static class UserAccountDetail {
        private String fintechUseNumber;
        private String nickname;
        private String bankName;
    }

    @ToString
    @AllArgsConstructor @Builder @Getter
    public static class UserFinanceDetail {
        private String userName;
        private List<UserAccountDetail> accounts;
    }

    public Optional<UserFinanceDetail> getUserFinanceDetail(TokenAndUserNumber tokenAndUserNumber) {
        try {
            Response<FinanceUserResponse> response = financeUserHttpApi.getUserInfoByNumber(
                    String.format("Bearer %s", tokenAndUserNumber.accessToken),
                    tokenAndUserNumber.userNumber
            ).execute();

            return Optional.of(UserFinanceDetail.builder()
                    .userName(response.body().getUserName())
                    .accounts(response.body().getResList().stream().map(it ->
                            UserAccountDetail.builder()
                                    .bankName(it.getBankName())
                                    .fintechUseNumber(it.getFintechUseNum())
                                    .nickname(it.getAccountAlias())
                                    .build()
                            )
                            .collect(Collectors.toList())
                    )
                    .build());
        }catch (Exception ex) {
            ex.printStackTrace();
            return Optional.empty();
        }
    }
}
