package org.certifit.application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Configuration
public class RestConfig {

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();

//        restTemplate.setInterceptors(List.of((request, body, execution) -> {
//            request.getHeaders().set("User-Agent",
//                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
//                            "AppleWebKit/537.36 (KHTML, like Gecko) " +
//                            "Chrome/120.0.0.0 Safari/537.36");
//            request.getHeaders().set("Accept", "application/json");
//            return execution.execute(request, body);
//        }));

        return restTemplate;
    }
}
