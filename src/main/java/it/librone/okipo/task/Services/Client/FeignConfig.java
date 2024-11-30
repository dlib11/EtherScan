package it.librone.okipo.task.Services.Client;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomErrorDecoder();
    }


    public class CustomErrorDecoder implements ErrorDecoder {

        @Override
        public Exception decode(String methodKey, Response response) {
            switch (response.status()) {
                case 400:
                    return new CustomFeignException("Bad Request");
                case 404:
                    return new CustomFeignException("Not Found");
                default:
                    return new Exception("Generic error");
            }
        }
    }
}