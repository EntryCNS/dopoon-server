package com.swcns.dopoonserver.global.infra.kakao;

import com.swcns.dopoonserver.domain.card.type.BillCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class KakaoService {
    private final KakaoApiProperties kakaoApiProperties;

    private final KakaoApi kakaoAPI;

    public BillCategory getCategory(String placeName) {
        try {
            List<KakaoApi.Document> documents = kakaoAPI.getCategory(String.format("KakaoAK %s", kakaoApiProperties.getApiKey()),
                    placeName)
                    .execute()
                    .body()
                    .getDocuments();

            String document = documents.get(0).getCategoryName();
            String[] categoryResult = document.split(">");

            return BillCategory.parseFromString(categoryResult[0]);
        } catch (Exception exception) {
            exception.printStackTrace();
            return BillCategory.ETC;
        }
    }

}
