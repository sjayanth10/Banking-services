package com.example.Natwest.cashwavebackend.Repo;

import com.example.Natwest.cashwavebackend.Model.Loan;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Repo extends MongoRepository<Loan, String> {
    List<Loan> findByLender(String lender);
}
