package it.librone.okipo.task.Services;

import feign.Response;
import it.librone.okipo.task.DTO.Result;
import it.librone.okipo.task.DTO.ethScanBalanceDTO;
import it.librone.okipo.task.DTO.ethScanResponseDTOv2;
import it.librone.okipo.task.Exceptions.ApiKeyNotValidException;
import it.librone.okipo.task.Exceptions.GenericEthScanException;
import it.librone.okipo.task.Services.Client.EthScanFeign;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

/**
 * Classe per gestire le chiamate all'API di Etherscan
 */
@Service
public class EthereumScanService {
    @Autowired
    private Logger log;

    @Autowired
    WebClient webClient;

    @Value("${etherscan.apikey}")
    private String apiKey;

    @Autowired
    private EthScanFeign ethScanFeign;

    /**
     * Metodo per ottenere le transazioni di un indirizzo
     * @param hash
     * @param startBlock
     * @return
     */
//    public ethScanResponseDTOv2 getTransactions(String hash, Long startBlock) {
//        return webClient.get()
//                .uri(uriBuilder -> uriBuilder
//                        .path("/api")
//                        .queryParam("module", "account")
//                        .queryParam("action", "txlist")
//                        .queryParam("address", hash)
//                        .queryParam("startblock", startBlock)
//                        .queryParam("endblock", 99999999)
//                        .queryParam("sort", "asc")
//                        .queryParam("apikey", apiKey)
//                        .build()
//                )
//                .header("Content-Type", "application/json")
//                .retrieve()
//                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
//                        clientResponse -> clientResponse.bodyToMono(String.class).map(body -> new GenericEthScanException(body)))
//                .bodyToMono(ethScanResponseDTOv2.class)
//                .block();
//    }

//    Metodo aggiornato usando FEIGN CLIENT
    public ethScanResponseDTOv2 getTransactions(String hash, Long startBlock) {
        ResponseEntity<ethScanResponseDTOv2> response = ethScanFeign.getTransactions("account",
                "txlist",
                hash,
                startBlock.toString(),
                "99999999",
                "asc",
                apiKey);
                return response.getBody();
    }

    public List<Result> getTransactionList(String hash) {
        return getTransactionList(hash, 0L);
    }

    /**
     * Metodo per ottenere le transazioni di un indirizzo a partire da un blocco specifico
     * In questo metodo verifico anche se l'API key è valida ed in caso negativo lancio un'eccezione
     * Chiamata di tipo Sincrona
     * @param hash
     * @param startBlock
     * @return
     */
    public List<Result> getTransactionList(String hash, Long startBlock){
        ethScanResponseDTOv2 response= getTransactions(hash, startBlock);

        if(response.getMessage().equals("NOTOK")){
            throw new ApiKeyNotValidException("EtherScan "+(String)response.getResult());
        }
        log.info("EtherScan getTransaction: OK");
        return (List<Result>) response.getResult();//response;
    }

    /**
     * Metodo per ottenere il saldo di un indirizzo
     * Chiamata di tipo Sincrona
     * @param hash
     * @return
     */

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
        log.info("EtherScan getBalance: OK");
        return Double.parseDouble(response.getResult());
    }
}
