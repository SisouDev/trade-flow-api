package com.spring.tradeflow.model.repositories;

import com.spring.tradeflow.model.entities.Address;
import org.springframework.data.repository.CrudRepository;

public interface AddressRepository extends CrudRepository<Address, Long> {
}
