package it.librone.okipo.task.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class config {
    @Bean
    public WebClient getWebClientBuilder() {
        return WebClient.builder().baseUrl("https://api.etherscan.io").build();
    }
}
