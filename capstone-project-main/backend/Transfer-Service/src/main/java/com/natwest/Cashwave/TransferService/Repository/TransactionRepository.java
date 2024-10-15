package com.natwest.Cashwave.TransferService.Repository;

import com.natwest.Cashwave.TransferService.Entity.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends MongoRepository<Transaction,String> {
    List<Transaction> findByUser_Id(String user_id);
}
