package com.natwest.psuedo.queue.repository;

import com.natwest.psuedo.queue.entity.TransactionEntity;
import org.springframework.data.repository.CrudRepository;

public interface TransactionRepository extends CrudRepository<TransactionEntity,Integer> {
}
