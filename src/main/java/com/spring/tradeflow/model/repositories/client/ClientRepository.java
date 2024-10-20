package com.spring.tradeflow.model.repositories.client;

import com.spring.tradeflow.model.entities.client.Client;
import com.spring.tradeflow.model.entities.client.Telephone;
import com.spring.tradeflow.utils.enums.client.States;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    List<Client> findByLastName(String lastName);
    List<Client> findByFirstName(String firstName);
    Optional<Client> findByEmail(String email);
    List<Client> findByAddress_State(States state);
    List<Client> findByFirstNameStartingWith(String start);
    List<Client> findByAddress_City(String city);

    @Modifying
    @Transactional
    @Query("DELETE FROM Telephone t WHERE t.telephoneId = :telephoneId AND t.client.id = :clientId")
    void removeTelephone(Long clientId, Long telephoneId);

    @Modifying
    @Transactional
    @Query("UPDATE Client c SET c.address.city = :city WHERE c.id = :id")
    int updateAddressCity(Long id, String city);

    @Modifying
    @Transactional
    @Query("UPDATE Client c SET c.firstName = :firstName WHERE c.id = :id")
    int updateFirstName(Long id, String firstName);

    @Modifying
    @Transactional
    @Query("UPDATE Client c SET c.lastName = :lastName WHERE c.id = :id")
    int updateLastName(Long id, String lastName);


}
