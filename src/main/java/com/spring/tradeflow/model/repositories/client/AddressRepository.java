package com.spring.tradeflow.model.repositories.client;

import com.spring.tradeflow.model.entities.client.Address;
import org.springframework.data.repository.CrudRepository;

public interface AddressRepository extends CrudRepository<Address, Long> {
}
