package com.swcns.dopoonserver.global.infra.kakao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

import java.util.List;

public interface KakaoApi{
    @Setter
    @Getter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    class KakaoMapResponse {
        private List<Document> documents;

    }

    @NoArgsConstructor
    @Setter
    @Getter
    @JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Document {
        private String categoryName;

    }

    @GET("/v2/local/search/keyword.json")
    Call<KakaoMapResponse> getCategory(
            @Header("Authorization") String token,
            @Query("query") String query
    );
}
