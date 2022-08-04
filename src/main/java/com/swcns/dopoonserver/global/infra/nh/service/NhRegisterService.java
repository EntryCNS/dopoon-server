package com.swcns.dopoonserver.global.infra.nh.service;

import com.swcns.dopoonserver.global.infra.nh.NhHeaderService;
import com.swcns.dopoonserver.global.infra.nh.exception.CardRegisterFailedException;
import com.swcns.dopoonserver.global.infra.nh.http.NhRegisterHttpApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import retrofit2.Response;

@RequiredArgsConstructor
@Service
public class NhRegisterService {
    private final NhHeaderService nhHeaderService;
    private final NhRegisterHttpApi nhRegisterHttpApi;

    /* 카드 등록번호를 반환 */
    public String requestRegisterCard(String birthDay, String cardNumber) {
        NhHeaderService.NhHeader header = nhHeaderService.getNhHeader("OpenFinCardDirect");
        NhRegisterHttpApi.IssueCardRequest req = new NhRegisterHttpApi.IssueCardRequest(header, birthDay, cardNumber);
        try {
            Response<NhRegisterHttpApi.IssueCardResponse> res = nhRegisterHttpApi.issueCard(req).execute();
            System.out.println(res.body());
            if(!res.isSuccessful()) {
                System.out.println(new String(res.errorBody().bytes()));
                throw new CardRegisterFailedException();
            }

            return res.body().getRegisterNumber();
        }catch (Exception ex) {
            ex.printStackTrace();
            throw new CardRegisterFailedException();
        }
    }

    public String confirmRegisterCard(String birthDay, String registerNumber) {
        NhHeaderService.NhHeader header = nhHeaderService.getNhHeader("CheckOpenFinCardDirect");
        NhRegisterHttpApi.ConfirmCardRequest req = new NhRegisterHttpApi.ConfirmCardRequest(header, registerNumber, birthDay);
        try {
            Response<NhRegisterHttpApi.ConfirmCardResponse> res = nhRegisterHttpApi.confirmCard(req).execute();
            System.out.println(res.body());
            if(!res.isSuccessful()) {
                System.out.println(new String(res.errorBody().bytes()));
                throw new CardRegisterFailedException();
            }

            return res.body().getFinCard();
        }catch (Exception ex) {
            ex.printStackTrace();
            throw new CardRegisterFailedException();
        }
    }
}
