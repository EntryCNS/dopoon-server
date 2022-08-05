package com.swcns.dopoonserver.global.infra.finance;

import com.swcns.dopoonserver.global.infra.finance.http.FinanceAccountHttpApi;
import com.swcns.dopoonserver.global.infra.finance.http.FinanceTokenHttpApi;
import com.swcns.dopoonserver.global.infra.finance.http.FinanceUserHttpApi;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Configuration
public class FinanceConfiguration {
    @Bean
    public JacksonConverterFactory jacksonConverterFactory() {
        return JacksonConverterFactory.create();
    }

    @Bean
    public Retrofit financeRetrofit() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        return new Retrofit.Builder()
                .client(client)
                .baseUrl("https://testapi.openbanking.or.kr")
                .addConverterFactory(jacksonConverterFactory())
                .build();
    }

    @Bean
    public FinanceTokenHttpApi financeTokenHttpApi() {
        Retrofit retrofit = financeRetrofit();
        return retrofit.create(FinanceTokenHttpApi.class);
    }

    @Bean
    public FinanceUserHttpApi financeUserHttpApi() {
        Retrofit retrofit = financeRetrofit();
        return retrofit.create(FinanceUserHttpApi.class);
    }

    @Bean
    public FinanceAccountHttpApi financeAccountHttpApi() {
        Retrofit retrofit = financeRetrofit();
        return retrofit.create(FinanceAccountHttpApi.class);
    }
}
