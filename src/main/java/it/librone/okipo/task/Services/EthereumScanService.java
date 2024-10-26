package it.librone.okipo.task.Services;

import it.librone.okipo.task.DTO.ethScanBalanceDTO;
import it.librone.okipo.task.DTO.ethScanResponseDTOv2;
import it.librone.okipo.task.Exceptions.ApiKeyNotValidException;
import it.librone.okipo.task.Exceptions.GenericEthScanException;
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

    public ethScanResponseDTOv2 getTransactions(String hash) {
    return getTransactions(hash, 0L);
    }
    public ethScanResponseDTOv2 getTransactions(String hash, Long startBlock) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api")
                        .queryParam("module", "account")
                        .queryParam("action", "txlist")
                        .queryParam("address", hash)
                        .queryParam("startblock", startBlock)
                        .queryParam("endblock", 99999999)
                        .queryParam("sort", "asc")
                        .queryParam("apikey", apiKey)
                        .build()
                )
                .header("Content-Type", "application/json")
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        clientResponse -> clientResponse.bodyToMono(String.class).map(body -> new GenericEthScanException(body)))
                .bodyToMono(ethScanResponseDTOv2.class)
                .block();
    }

    public ethScanResponseDTOv2 getTransactionList(String hash) {
        return getTransactionList(hash, 0L);
    }

    public ethScanResponseDTOv2 getTransactionList(String hash, Long startBlock){
        ethScanResponseDTOv2 response= getTransactions(hash, startBlock);
        //if(response.getStatus().equals("0")){
        if(response.getMessage().equals("NOTOK")){
            throw new ApiKeyNotValidException("EtherScan "+(String)response.getResult());
        }
        return response;
    }

    public Double getBalance(String hash) {
        ethScanBalanceDTO response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api")
                        .queryParam("module", "account")
                        .queryParam("action", "balance")
                        .queryParam("address", hash)
                        .queryParam("tag", "latest")
                        .queryParam("apikey", apiKey)
                        .build()
                )
                .header("Content-Type", "application/json")
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        clientResponse -> clientResponse.bodyToMono(String.class).map(body -> new GenericEthScanException(body)))
                .bodyToMono(ethScanBalanceDTO.class)
                .block();

        if(response.getMessage().equals("NOTOK")){
            throw new ApiKeyNotValidException("EtherScan "+response.getResult());
        }
        return Double.parseDouble(response.getResult());
    }
}
