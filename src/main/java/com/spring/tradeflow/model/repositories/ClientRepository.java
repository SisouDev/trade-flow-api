package com.spring.tradeflow.model.repositories;

import com.spring.tradeflow.model.entities.Client;
import org.springframework.data.repository.CrudRepository;

public interface ClientRepository extends CrudRepository<Client, Long> {
}
