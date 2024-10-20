package com.spring.tradeflow.model.repositories.client;

import com.spring.tradeflow.model.entities.client.Telephone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TelephoneRepository extends JpaRepository<Telephone, Long> {
}
