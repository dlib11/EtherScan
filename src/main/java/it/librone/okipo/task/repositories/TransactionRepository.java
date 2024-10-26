package it.librone.okipo.task.repositories;

import it.librone.okipo.task.entities.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t WHERE t.address.ethAddress = :address")
    Optional<List<Transaction>> findByDerivedQuery(String address);


    @Query("SELECT MAX(t.blockNumber) FROM Transaction t")
    Long findLastTransaction();

    // NON PAGEABLE
  //  List<Transaction> findByAddress_EthAddressOrderByTimeStampDesc(String address);
  //  List<Transaction> findByAddress_EthAddressOrderByTimeStampAsc(String address);

    Page<Transaction> findByAddress_EthAddressOrderByTimeStampDesc(String address, Pageable pageable);
    Page<Transaction> findByAddress_EthAddressOrderByTimeStampAsc(String address, Pageable pageable);
}
