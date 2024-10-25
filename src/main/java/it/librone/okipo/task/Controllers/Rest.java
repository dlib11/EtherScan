package it.librone.okipo.task.Controllers;

import it.librone.okipo.task.DTO.AddressDTO;
import it.librone.okipo.task.DTO.Result;
import it.librone.okipo.task.DTO.ethScanResponseDTO;
import it.librone.okipo.task.Services.AddressService;
import it.librone.okipo.task.Services.EthereumScanService;
import it.librone.okipo.task.Services.TransactionServices;
import it.librone.okipo.task.Utility.ResultToTransaction;
import it.librone.okipo.task.entities.Address;
import it.librone.okipo.task.entities.Transaction;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class Rest {

    private static final Logger log = LoggerFactory.getLogger(Rest.class);
    @Autowired
    private AddressService addressService;

    @Autowired
    private TransactionServices transactionService;

    @Autowired
    private EthereumScanService ethereumScanService;

    @RequestMapping(value="/get", method = RequestMethod.GET)
    @Transactional
    public ResponseEntity<?> get(@RequestBody(required = true) @Valid AddressDTO dto) {
        log.info("AddressDTO: "+dto.getAddress());
        Address address= addressService.getByEthAddress(dto.getAddress());
        if(address==null){
            address= new Address();
            address.setEthAddress(dto.getAddress());
            address=addressService.saveAddress(address);
            List<Result> response = ethereumScanService.getTransactions(address.getEthAddress()).getResult();

            //va dichiarata final sennò non va bene per la lambda expression
            Address finalAddress = address;
            List<Transaction> transactions = response.stream().map(Result-> ResultToTransaction.convertToTransaction(Result, finalAddress)).toList();
            //List<Transaction> transactions = response.stream().map(ResultToTransaction::convertToTransaction).toList();

            transactionService.saveAll(transactions);

            //address.setTransactions(transactions);

            address.setLastUpdatedAt(Instant.now());
            addressService.saveAddress(address);
            return new ResponseEntity<>("Success!" + Instant.now(), org.springframework.http.HttpStatus.OK);

        }
        else {
            log.info("Address: "+address.getEthAddress());
            Instant addressLastUpdate=address.getLastUpdatedAt();

            Long transactionLastUpdate=transactionService.findLastTransactiontimeStamp();
            log.info("transactionLastUpdate: "+transactionLastUpdate);

            List<Result> response = ethereumScanService.getTransactions(address.getEthAddress(),transactionLastUpdate).getResult();

            // aggiungo relazione address alle transaction
            Address finalAddress = address;
            List<Transaction> transactions = response.stream().map(Result-> ResultToTransaction.convertToTransaction(Result, finalAddress)).toList();

            Integer transactionSize=transactions.size();
            if(transactionSize>0)
                transactionService.saveAll(transactions);

            //in alternativa avrei potuto inserirlo in un metodo @PostSave di Transaction
            // però facendo così lo modifico una volta sola invece di aggiornarlo una volta per ogni transazione
            // inoltre se non ci sono transazioni lo aggiorno ugualmente
            address.setLastUpdatedAt(Instant.now());
            addressService.saveAddress(address);

            return new ResponseEntity<>("Address already exist, updated "+transactionSize+" transactions!", org.springframework.http.HttpStatus.OK);
        }

    }
}
