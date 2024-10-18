package com.spring.tradeflow.model.repositories;

import com.spring.tradeflow.model.entities.Telephone;
import org.springframework.data.repository.CrudRepository;

public interface TelephoneRepository extends CrudRepository<Telephone, Long> {
}
