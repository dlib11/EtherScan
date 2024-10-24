package it.librone.okipo.task.repositories;

import it.librone.okipo.task.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    Boolean existsByEthAddress(String ethAddress);
    Optional<Address> findByEthAddress(String ethAddress);
}
