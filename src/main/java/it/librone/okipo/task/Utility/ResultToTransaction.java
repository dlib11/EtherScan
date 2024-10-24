package it.librone.okipo.task.Utility;

import it.librone.okipo.task.DTO.Result;
import it.librone.okipo.task.entities.Address;
import it.librone.okipo.task.entities.Transaction;
import org.springframework.stereotype.Service;

@Service
public class ResultToTransaction {

    // TODO verificare se gli indirizzi TO e FROM sono corretti
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
        //return transaction;
    }
}
