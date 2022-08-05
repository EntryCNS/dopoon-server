package com.swcns.dopoonserver.global.infra.finance.service;

import com.swcns.dopoonserver.domain.card.entity.Bill;
import com.swcns.dopoonserver.domain.card.entity.Card;
import com.swcns.dopoonserver.domain.card.type.BillCategory;
import com.swcns.dopoonserver.domain.user.entity.FintechToken;
import com.swcns.dopoonserver.domain.user.entity.User;
import com.swcns.dopoonserver.domain.user.exception.UserNotFoundException;
import com.swcns.dopoonserver.domain.user.facade.UserFacade;
import com.swcns.dopoonserver.global.infra.finance.FinanceProperties;
import com.swcns.dopoonserver.global.infra.finance.http.FinanceAccountHttpApi;
import com.swcns.dopoonserver.global.infra.finance.http.FinanceTokenHttpApi;
import com.swcns.dopoonserver.global.infra.finance.http.FinanceUserHttpApi;
import com.swcns.dopoonserver.global.utils.RandomUtil;
import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import retrofit2.Response;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.swcns.dopoonserver.global.infra.finance.http.FinanceTokenHttpApi.*;
import static com.swcns.dopoonserver.global.infra.finance.http.FinanceUserHttpApi.*;
import static com.swcns.dopoonserver.global.infra.finance.http.FinanceAccountHttpApi.*;

@Log4j2
@RequiredArgsConstructor
@Service
public class OpenFinanceService {
    private final FinanceProperties financeProperties;
    private final FinanceTokenHttpApi financeTokenHttpApi;
    private final FinanceUserHttpApi financeUserHttpApi;
    private final FinanceAccountHttpApi financeAccountHttpApi;
    private final UserFacade userFacade;
    private final RandomUtil randomUtil;

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

    @Transactional(rollbackFor = RuntimeException.class)
    public FintechToken updateToken() {
        User user = userFacade.queryCurrentUser(true)
                .orElseThrow(UserNotFoundException::new);

        try {
            Response<FinanceTokenResponse> response = financeTokenHttpApi.getTokenByRefreshToken(
                    financeProperties.getClientId(), financeProperties.getClientSecret(),user.getFintechToken().getRefreshToken(),
                    "login inquiry", "refresh_token"
            ).execute();

            user.getFintechToken().updateTokens(response.body().getAccessToken(), response.body().getRefreshToken());
        }catch (Exception ignored) { }

        return user.getFintechToken();
    }

    public List<Bill> getBillHistoryOf(Card card, Date from, Date to) {
        final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyyMMdd");
        final SimpleDateFormat REQUEST_TIME_FORMATTER = new SimpleDateFormat("yyyyMMddhhmmss");

        FintechToken fintechToken = updateToken();

        String bankTranId = String.format("%sU%s", financeProperties.getUseOrganizationCode(), randomUtil.generateRandomString(9));

        try {
            Response<FinanceBillResponse> list = financeAccountHttpApi.getBillsByFintechNumber(
                    String.format("Bearer %s", fintechToken.getAccessToken()),
                    bankTranId, card.getCardNumber(), "O", "D", DATE_FORMATTER.format(from), DATE_FORMATTER.format(to),
                    "D", REQUEST_TIME_FORMATTER.format(new Date())
            ).execute();

            return list.body().getResList().stream()
                    .map(it ->
                            Bill.builder()
                                    .billedAt(LocalDateTime.of(
                                        Integer.parseInt(it.getTranDate().substring(0, 3)),
                                                    Integer.parseInt(it.getTranDate().substring(4, 5)),
                                            Integer.parseInt(it.getTranDate().substring(6, 7)),

                                            Integer.parseInt(it.getTranTime().substring(0, 1)),
                                            Integer.parseInt(it.getTranTime().substring(2, 3))
                                    ))
                                    .storeName(it.getBranchName())
                                    .price(Integer.parseInt(it.getTranAmt()))
                                    .billCategory(BillCategory.ETC)
                                    .build())
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            return new ArrayList<>();
        }
    }
}
