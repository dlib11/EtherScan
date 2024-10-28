package it.librone.okipo.task.Utility;

import it.librone.okipo.task.DTO.TransactionDTO;
import it.librone.okipo.task.entities.Transaction;

public class TransactionToDTO {
    /**
     * Metodo per convertire un oggetto Transaction in un oggetto TransactionDTO
     * @param transaction
     * @return
     */
    public static TransactionDTO toDTO(Transaction transaction) {
        return TransactionDTO.builder()
                .Address(transaction.getAddress().getEthAddress())
                .blockNumber(transaction.getBlockNumber())
                .timeStamp(transaction.getTimeStamp())
                .transactionHash(transaction.getTransactionHash())
                .from(transaction.getFrom())
                .to(transaction.getTo())
                .value(transaction.getValue())
                .gas(transaction.getGas())
                .build();
    }
}
