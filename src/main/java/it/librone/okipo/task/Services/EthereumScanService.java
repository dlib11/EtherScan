package it.librone.okipo.task.Services;

import it.librone.okipo.task.DTO.ethScanResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class EthereumScanService {
    @Autowired
    WebClient webClient;

    @Value("${etherscan.apikey}")
    private String apiKey;

    public ethScanResponseDTO getTransactions(String hash) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api")
                        .queryParam("module", "account")
                        .queryParam("action", "txlist")
                        .queryParam("address", hash)
                        .queryParam("startblock", 0)
                        .queryParam("endblock", 99999999)
                        .queryParam("sort", "asc")
                        .queryParam("apikey", apiKey)
                        .build()
                )
                .header("Content-Type", "application/json")
                .retrieve()
                .bodyToMono(ethScanResponseDTO.class)
                .block();
    }
}
