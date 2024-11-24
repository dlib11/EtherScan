package it.librone.okipo.task.Utility;

import it.librone.okipo.task.DTO.TransactionDTO;
import it.librone.okipo.task.entities.Address;
import it.librone.okipo.task.entities.Transaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class MapperTransactionDTOTest {

    @Autowired
    private MapperTransactionDTO mapperTransactionDTO;


    @Test
    public void testSameMapper(){
        Address add= new Address();
        add.setEthAddress("0x123");


        Transaction transaction = Transaction.builder()
                .address(add)
                .blockNumber(1L)
                .timeStamp(2L)
                .transactionHash("0x123")
                .from("0x123")
                .to("0x123")
                .value(1.00)
                .gas(1.00)
                .build();

        TransactionDTO transactionToDTO =TransactionDTO.builder()
                .Address("0x123")
                .blockNumber(1L)
                .timeStamp(2L)
                .transactionHash("0x123")
                .from("0x123")
                .to("0x123")
                .value(1.00)
                .gas(1.00)
                .build();

        TransactionDTO convertedManually = TransactionToDTO.toDTO(transaction);
        TransactionDTO convertedWithMapper = this.mapperTransactionDTO.toDTO(transaction);

        assertAll(
                ()->assertEquals(convertedWithMapper.getAddress(), convertedManually.getAddress()),
                ()->assertEquals(convertedWithMapper.getBlockNumber(), convertedManually.getBlockNumber()),
                ()->assertEquals(convertedWithMapper.getTimeStamp(), convertedManually.getTimeStamp()),
                ()->assertEquals(convertedWithMapper.getTransactionHash(), convertedManually.getTransactionHash()),
                ()->assertEquals(convertedWithMapper.getFrom(), convertedManually.getFrom()),
                ()->assertEquals(convertedWithMapper.getTo(), convertedManually.getTo()),
                ()->assertEquals(convertedWithMapper.getValue(), convertedManually.getValue()),
                ()->assertEquals(convertedWithMapper.getGas(), convertedManually.getGas())
        );
    }
}