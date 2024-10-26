package it.librone.okipo.task.Services;

import it.librone.okipo.task.DTO.Result;
import it.librone.okipo.task.Utility.ResultToTransaction;
import it.librone.okipo.task.entities.Address;
import it.librone.okipo.task.entities.Transaction;
import it.librone.okipo.task.repositories.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class TransactionServices {
    private static final Logger log = LoggerFactory.getLogger(TransactionServices.class);
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AddressService addressService;

    @Autowired
    private EthereumScanService ethereumScanService;

    public void saveAll(List<Transaction> list) {
        transactionRepository.saveAll(list);
    }
/*
    public Transaction findLastTransaction() {
        return transactionRepository.findLastTransaction().orElse(null);
    }
*/
    public Long findLastTransactionBlocknum() {
        return transactionRepository.findLastTransaction();
    }
    public Integer saveTransaction(String dto) {
        Address address= addressService.getByEthAddress(dto);
        if(address==null){
            address= new Address();
            address.setEthAddress(dto);
            address=addressService.saveAddress(address);
            List<Result> response = ethereumScanService.getTransactions(address.getEthAddress()).getResult();

            //va dichiarata final sennò non va bene per la lambda expression
            Address finalAddress = address;
            List<Transaction> transactions = response.stream().map(Result-> ResultToTransaction.convertToTransaction(Result, finalAddress)).toList();
            //List<Transaction> transactions = response.stream().map(ResultToTransaction::convertToTransaction).toList();

            saveAll(transactions);

            //address.setTransactions(transactions);

            address.setLastUpdatedAt(Instant.now());
            addressService.saveAddress(address);
            return transactions.size();

        }
        else {
            log.info("Address: "+address.getEthAddress());
            Instant addressLastUpdate=address.getLastUpdatedAt();

            Long transactionLastUpdate=0L;
            // DEVO PRIMA CONTROLLARE SE CI SONO TRANSAZIONI SALVATE SENNO OTTENGO NULL
            // CHE SUCCEDE SE NON CI SONO TRANSAZIONI SALVATE?
            // SE NON CI SONO TRANSAZIONI SALVATE DEVO PARTIRE DA 0
            // SE CI SONO TRANSAZIONI SALVATE DEVO PARTIRE DAL BLOCCO SUCCESSIVO
            if(findLastTransactionBlocknum()>0)
                // NEL CASO NON CI SIANO MI RESTITUISCE 0??
                transactionLastUpdate=findLastTransactionBlocknum()+1;
            //devo fare transactionLastUpdate +1 per evitare di riprendere la stessa transazione ??

            // IMPORTANTE, DEVO RIPRENDERE LO STESSO BLOCK INVECE DI +1
            // METTI CHE L'ULTIMA TRANSAZIONE SALVATA E' AL BLOCK 100
            // SE RIPRENDI DA 101 MAGARI NON PRENDI UN'ALTRA TRANSAZIONE DEL BLOCCO 100 CHE E' STATA FATTA DOPO
            log.info("transactionLastUpdate: "+transactionLastUpdate);

            List<Result> response = ethereumScanService.getTransactions(address.getEthAddress(),transactionLastUpdate).getResult();

            // aggiungo relazione address alle transaction
            Address finalAddress = address;
            List<Transaction> transactions = response.stream().map(Result-> ResultToTransaction.convertToTransaction(Result, finalAddress)).toList();

            if(!transactions.isEmpty())
                saveAll(transactions);

            //in alternativa avrei potuto inserirlo in un metodo @PostSave di Transaction
            // però facendo così lo modifico una volta sola invece di aggiornarlo una volta per ogni transazione
            // inoltre se non ci sono transazioni lo aggiorno ugualmente
            address.setLastUpdatedAt(Instant.now());
            addressService.saveAddress(address);

            return transactions.size();
        }


    }
}
