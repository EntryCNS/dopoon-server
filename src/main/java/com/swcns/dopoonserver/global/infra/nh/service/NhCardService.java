package com.swcns.dopoonserver.global.infra.nh.service;

import com.swcns.dopoonserver.domain.card.entity.Bill;
import com.swcns.dopoonserver.domain.card.type.BillCategory;
import com.swcns.dopoonserver.global.infra.nh.NhHeaderService;
import com.swcns.dopoonserver.global.infra.nh.exception.CardHistoryQueryFailedException;
import com.swcns.dopoonserver.global.infra.nh.exception.CardRegisterFailedException;
import com.swcns.dopoonserver.global.infra.nh.http.NhCardHttpApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import retrofit2.Response;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class NhCardService {
    private final NhHeaderService nhHeaderService;
    private final NhCardHttpApi nhCardHttpApi;

    public List<Bill> getBills(int page, String cardNumber) {
        NhHeaderService.NhHeader header = nhHeaderService.getNhHeader("InquireCreditCardAuthorizationHistory");
        try {
            Response<NhCardHttpApi.CardHistoryResponse> response = nhCardHttpApi.getCardHistory(
                    new NhCardHttpApi.CardHistoryRequest(
                            header, "1", "20191105", "20191109", String.valueOf(page), "15", cardNumber
                    )
            ).execute();
            System.out.println(response.message());
            if(!response.isSuccessful()) {
                System.out.println(new String(response.errorBody().bytes()));
            }
            System.out.println(response.body());

            List<NhCardHttpApi.CardHistoryResponse.HistoryDetail> details = response.body().getRecords();
            final SimpleDateFormat parseFormat = new SimpleDateFormat("yyyyMMddhhmmss");

            return details.stream().map(detail -> {
                try {
                    long mills = parseFormat.parse(detail.getBillYmd() + detail.getBillHms()).getTime();
                    return Bill.builder()
                            .billCategory(BillCategory.ETC)
                            .storeName(detail.getStoreName())
                            .price(Integer.parseInt(detail.getCost()))
                            .billedAt(LocalDateTime.ofInstant(Instant.ofEpochMilli(mills), ZoneId.systemDefault()))
                            .build();
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }).collect(Collectors.toList());

        }catch (Exception ex) {
            ex.printStackTrace();
            throw new CardHistoryQueryFailedException();
        }
    }
}
