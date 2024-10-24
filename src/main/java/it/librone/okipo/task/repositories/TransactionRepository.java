package it.librone.okipo.task.repositories;

import it.librone.okipo.task.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t WHERE t.address.ethAddress = :address")
    Optional<List<Transaction>> findByDerivedQuery(String address);
}
