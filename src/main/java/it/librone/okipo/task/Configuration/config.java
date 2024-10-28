package it.librone.okipo.task.Configuration;

import it.librone.okipo.task.Controllers.Rest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.regex.Pattern;

@Configuration
public class config {
    @Bean
    public WebClient getWebClientBuilder() {

        return WebClient
                .builder()
                .baseUrl("https://api.etherscan.io")
                // maxInMemorySize impostata a 10MB per evitare problemi in caso di risposte molto grandi
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024)) //
                .build();
    }

    @Bean
    public Pattern getMatcher() {
        return Pattern.compile("^0x[a-fA-F0-9]{40}$");
    }

    @Bean
    public Logger getLogger() {
        return LoggerFactory.getLogger(Rest.class);
    }

}
