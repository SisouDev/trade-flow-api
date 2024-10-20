package com.spring.tradeflow.model.repositories.client;

import com.spring.tradeflow.model.entities.client.Client;
import org.springframework.data.repository.CrudRepository;

public interface ClientRepository extends CrudRepository<Client, Long> {
}
