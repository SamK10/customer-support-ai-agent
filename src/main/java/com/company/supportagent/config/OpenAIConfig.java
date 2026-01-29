package com.company.supportagent.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class OpenAIConfig {

    @Bean
    public WebClient openAIWebClient() {
        System.out.println("OPENAI_API_KEY = " + System.getenv("OPENAI_API_KEY"));
        return WebClient.builder()
                .baseUrl("https://api.openai.com/v1")
                .defaultHeader("Authorization",
                        "Bearer " + System.getenv("OPENAI_API_KEY"))
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}
