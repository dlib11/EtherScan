package it.librone.okipo.task.Services;

import it.librone.okipo.task.DTO.Result;
import it.librone.okipo.task.Utility.ResultToTransaction;
import it.librone.okipo.task.entities.Address;
import it.librone.okipo.task.entities.Transaction;
import it.librone.okipo.task.repositories.TransactionRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

/**
 * Classe per gestire le transazioni
 */
@Service
public class TransactionServicesV2 {
    private static final Logger log = LoggerFactory.getLogger(TransactionServicesV2.class);
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AddressService addressService;

    @Autowired
    private EthereumScanService ethereumScanService;

    public void saveAll(List<Transaction> list) {
        transactionRepository.saveAll(list);
    }

    /**
     * Metodo per ottenere l'ultimo blocco delle transazioni di una indirizzo
     * @param address
     * @return
     */
    public Long findLastTransactionBlocknum(String address) {
        return transactionRepository.findLastTransaction(address).orElse(0L);
    }

    /**
     * Metodo per salvare le transazioni di un indirizzo
     * Se l'indirizzo non esiste lo crea
     * Se l'indiriizo esiste aggiunge le transazioni mancanti
     * NB: Questo metodo è ricorsivo in caso ci siano più di 10000 transazioni, poichè l'endpoint di Etherscan restituisce al massimo 10000 transazioni
     * @param dto
     * @return
     */
    @Transactional
    public Integer saveTransaction(String dto) {
        Address address= addressService.getByEthAddress(dto);

        if(address==null)
            address= addressService.saveAddress(dto);

        Long transactionLastUpdate=0L;
        if(findLastTransactionBlocknum(address.getEthAddress())>0)
           transactionLastUpdate=findLastTransactionBlocknum(address.getEthAddress())+1;

           // log.info("transactionLastUpdate: "+transactionLastUpdate);

        List<Result> response = ethereumScanService.getTransactionList(address.getEthAddress(),transactionLastUpdate);

        Address finalAddress = address;
        List<Transaction> transactions = ((List<Result>)response).stream().map(Result-> ResultToTransaction.convertToTransaction(Result, finalAddress)).toList();

        if(!transactions.isEmpty())
           saveAll(transactions);

        address.setLastUpdatedAt(Instant.now());
        addressService.saveAddress(address);

        if(transactions.size()==10000)
           return transactions.size()+saveTransaction(dto);
        return transactions.size();

    }


    /*
            NON PAGEABLE
        public List<Transaction> getAllByAddress(String address, String order){
            if(order.equals("asc"))
                return transactionRepository.findByAddress_EthAddressOrderByTimeStampDesc(address);
            else if(order.equals("desc"))
                return transactionRepository.findByAddress_EthAddressOrderByTimeStampAsc(address);
            else
                return transactionRepository.findByAddress_EthAddressOrderByTimeStampDesc(address);
        }

     */

        // pageable

    /**
     * Metodo per ottenere le transazioni di un indirizzo, questa versione è pageable
     * @param address
     * @param order
     * @param page
     * @param size
     * @return
     */
        public Page<Transaction> getAllByAddressPageable(String address, String order, int page, int size) {
            Pageable pageable = PageRequest.of(page, size);
            if(order.equals("asc"))
                return transactionRepository.findByAddress_EthAddressOrderByTimeStampAsc(address, pageable);
            else
                return transactionRepository.findByAddress_EthAddressOrderByTimeStampDesc(address, pageable);
        }




}
