package it.librone.okipo.task.Services.Client;

import it.librone.okipo.task.DTO.ethScanResponseDTOv2;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ethScan",
        url = "https://api.etherscan.io",
        path = "/api",
        configuration = FeignConfig.class)
public interface EthScanFeign {

    @GetMapping("/?module={module}&action={action}&address={address}&startblock={startblock}&endblock={endblock}&sort={sort}&apikey={apikey}")
    public ResponseEntity<ethScanResponseDTOv2> getTransactions(@PathVariable("module") String module,
                                                                @PathVariable("action") String action,
                                                                @PathVariable("address") String address,
                                                                @PathVariable("startblock") String startblock,
                                                                @PathVariable("endblock") String endblock,
                                                                @PathVariable("sort") String sort,
                                                                @PathVariable("apikey") String apikey);
}
