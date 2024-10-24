package it.librone.okipo.task.Services;

import it.librone.okipo.task.entities.Transaction;
import it.librone.okipo.task.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionServices {
    @Autowired
    private TransactionRepository transactionRepository;
    public void saveAll(List<Transaction> list) {
        transactionRepository.saveAll(list);
    }
}
