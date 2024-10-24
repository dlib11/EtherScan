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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class Rest {

    @Autowired
    private AddressService addressService;

    @Autowired
    private TransactionServices transactionService;

    @Autowired
    private EthereumScanService ethereumScanService;

    @RequestMapping(value="/get", method = RequestMethod.GET)
    @Transactional
    public ResponseEntity<?> get(@RequestBody(required = true) @Valid AddressDTO dto) {
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

            addressService.saveAddress(address);
            return new ResponseEntity<>("Success!", org.springframework.http.HttpStatus.OK);

        }

        return new ResponseEntity<>("Hello World", org.springframework.http.HttpStatus.OK);
    }
}
