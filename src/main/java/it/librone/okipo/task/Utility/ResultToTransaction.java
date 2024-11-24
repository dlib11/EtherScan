package it.librone.okipo.task.Utility;

import it.librone.okipo.task.DTO.Result;
import it.librone.okipo.task.entities.Address;
import it.librone.okipo.task.entities.Transaction;
import org.springframework.stereotype.Service;

@Service
public class ResultToTransaction {
    /**
     * Metodo per convertire un oggetto dto Result in un oggetto Transaction
     * @param result
     * @param address
     * @return
     */

    public static Transaction convertToTransaction(Result result, Address address) {
        return  new Transaction().builder()
                .transactionHash(result.getHash())
                .blockNumber(result.getBlockNumber())
                .timeStamp(result.getTimeStamp())
                .to(result.getTo())
                .from(result.getFrom())
                .value(result.getValue())
                .gas(result.getGas())
                .address(address)
                .build();
    }


}
