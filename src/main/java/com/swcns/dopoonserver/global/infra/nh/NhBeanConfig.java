package com.swcns.dopoonserver.global.infra.nh;

import com.swcns.dopoonserver.global.infra.nh.http.NhCardHttpApi;
import com.swcns.dopoonserver.global.infra.nh.http.NhRegisterHttpApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Configuration
public class NhBeanConfig {

    @Bean
    public NhProperties nhProperties() {
        return new NhProperties();
    }

    @Bean
    public JacksonConverterFactory jacksonConverterFactory() {
        return JacksonConverterFactory.create();
    }

    @Bean
    public Retrofit nhRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(nhProperties().getEndpoint())
                .addConverterFactory(jacksonConverterFactory())
                .build();
    }

    @Bean
    public NhRegisterHttpApi nhRegisterHttpApi() {
        Retrofit retrofit = nhRetrofit();
        return retrofit.create(NhRegisterHttpApi.class);
    }

    @Bean
    public NhCardHttpApi nhCardHttpApi() {
        Retrofit retrofit = nhRetrofit();
        return retrofit.create(NhCardHttpApi.class);
    }
}
