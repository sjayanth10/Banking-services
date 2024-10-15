package com.natwest.Cashwave.AccountService.Repository;

import com.natwest.Cashwave.AccountService.Entity.Account;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends MongoRepository<Account,String> {
    List<Account> findByUser_Id(String Id);

    Account findByAccountNo(String accountNo);

    Boolean existsByAccountNo(String accountNo);
}
