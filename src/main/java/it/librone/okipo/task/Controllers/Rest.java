package it.librone.okipo.task.Controllers;

import it.librone.okipo.task.DTO.AddressDTO;
import it.librone.okipo.task.DTO.TransactionDTO;
import it.librone.okipo.task.Services.AddressService;
import it.librone.okipo.task.Services.EthereumScanService;
import it.librone.okipo.task.Services.TransactionServicesV2;
import it.librone.okipo.task.Utility.TransactionToDTO;
import it.librone.okipo.task.entities.Address;
import it.librone.okipo.task.entities.Transaction;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "http://localhost:4200")
public class Rest {

    @Autowired
    private Logger log;

    @Autowired
    private AddressService addressService;

    @Autowired
    private TransactionServicesV2 transactionService;

    @Autowired
    private EthereumScanService ethereumScanService;

    @Autowired
    Pattern ethAddressPattern;


    /**
     * Endpoint per aggiungere un address e scaricare le transactions.
     *
     * @param dto AddressDTO con l'address da aggiungere
     * @return ResponseEntity con il numero di transazioni aggiunte
     */

     @RequestMapping(value="/addAddress", method = RequestMethod.POST)
    public ResponseEntity<?> get(@RequestBody(required = true) @Valid AddressDTO dto) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        log.info("POST /add, address: "+dto.getAddress());

        Integer newTransactions= transactionService.saveTransaction(dto.getAddress());
        response.put("Result", "Success");
        response.put("Transaction", newTransactions);
        return new ResponseEntity<>(response, org.springframework.http.HttpStatus.OK);
    }


    /**
     * Endpoint per ottenere le transactions di un address.
     *
     * @param addressDto l'address di cui ottenere le transactions
     * @param order l'ordine delle transactions (asc o desc)
     * @param page il numero di pagine per la paginazione
     * @param size il numero di transactions per pagina
     * @return ResponseEntity con le transactions
     */

        @RequestMapping(value="/transactions", method = RequestMethod.GET)
    public ResponseEntity<?> getAll(@RequestParam(required = true, name = "address") String addressDto,
                                    @RequestParam(required = false, defaultValue ="asc") String order,
                                    @RequestParam(required = false, defaultValue = "0") int page,
                                    @RequestParam(required = false, defaultValue = "10") int size)
        {

        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        Matcher matcher= ethAddressPattern.matcher(addressDto);
        if(!matcher.matches()){
            response.put("Error", "Invalid address");
            log.error("GET /getAll, Invalid address "+addressDto);
            return new ResponseEntity<>(response, org.springframework.http.HttpStatus.BAD_REQUEST);
        }

        Address address= addressService.getByEthAddress(addressDto);
        if(address==null){
            response.put("Error", "Address not found");
            log.error("GET /getAll, Address not found "+addressDto);
            return new ResponseEntity<>(response, org.springframework.http.HttpStatus.NOT_FOUND);
        }

        if(!order.equals("asc") && !order.equals("desc")) {
            response.put("Error", "Invalid order "+order);
            log.error("GET /getAll, Invalid order "+order);
            return new ResponseEntity<>(response, org.springframework.http.HttpStatus.BAD_REQUEST);
        }

            // NON pageable
        //List<Transaction> transactions= transactionService.getAllByAddress(address.getEthAddress(), order);
        //List<TransactionDTO> transactionDTOS= transactions.stream().map(TransactionToDTO::toDTO).toList();
            // Pageable version
        Page<Transaction> transactionsPage = transactionService.getAllByAddressPageable(addressDto, order, page, size);
        List<TransactionDTO> transactionDTOS = transactionsPage.getContent().stream().map(TransactionToDTO::toDTO).toList();


        // response.put("Result", transactionDTOS.size());  // NON pageable
        response.put("Balance", address.getBalance().toString());
        response.put("TotalPages", transactionsPage.getTotalPages());
        response.put("TotalElements", transactionsPage.getTotalElements());
        response.put("Response", transactionDTOS);
        log.info("GET /getAll, address: "+addressDto+", transactions: "+transactionDTOS.size());
        return new ResponseEntity<>(response, org.springframework.http.HttpStatus.OK);
    }


}
