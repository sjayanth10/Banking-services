package com.natwest.Cashwave.AccountService.Service;

import com.mongodb.client.result.UpdateResult;
import com.natwest.Cashwave.AccountService.DTO.AccountRequest;
import com.natwest.Cashwave.AccountService.Entity.Account;
import com.natwest.Cashwave.AccountService.Entity.User;
import com.natwest.Cashwave.AccountService.Repository.AccountRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    MongoTemplate mongoTemplate;

    public ResponseEntity<?> addAccount(String user_id, AccountRequest newAccountdetails)
    {
        ResponseEntity<User> userResponse = restTemplate.getForEntity(
                "http://localhost:8081/users/getUser/" + user_id,
                User.class
        );
        if(userResponse.getStatusCode() == HttpStatus.OK){
            User user=userResponse.getBody();
            if (newAccountdetails.getAccountNo() == null || newAccountdetails.getAccountNo().isEmpty()) {
                return ResponseEntity.badRequest().body("Account number is required.");
            }

            if (newAccountdetails.getAccountBalance() == null) {
                return ResponseEntity.badRequest().body("Account balance is required.");
            }

            boolean accountExists = accountRepository.existsByAccountNo(newAccountdetails.getAccountNo());

            if (accountExists) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Account number already exists.");
            }

            Account newAccount = new Account();
            newAccount.setAccountBalance(Double.parseDouble(newAccountdetails.getAccountBalance()));
            newAccount.setAccountNo(newAccountdetails.getAccountNo());
            newAccount.setAccountBankName(newAccountdetails.getAccountBankName());

            // Set the user reference in the new account
            newAccount.setUser(user);

            // Save the new account to the Accounts collection
            accountRepository.save(newAccount);

            return ResponseEntity.status(HttpStatus.CREATED).body(newAccount);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with ID: " + user_id);
        }
    }


    public ResponseEntity<?> listAccount(String user_id)
    {
        List<Account> accounts=accountRepository.findByUser_Id(user_id);
        return ResponseEntity.ok(accounts);
    }

    public ResponseEntity<String> updateAccountBalance(String accountNo, double newBalance) {
        Query query = new Query(Criteria.where("accountNo").is(accountNo));
        Update update = new Update().set("accountBalance", newBalance);

        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, Account.class);

        if (updateResult.getModifiedCount() > 0) {
            // The account balance was updated successfully
            return ResponseEntity.status(HttpStatus.OK).body("Account balance updated successfully.");
        } else {
            // No matching account was found, or the balance was not modified
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found or balance not modified.");
        }
    }

    public ResponseEntity<?> findByaccountNo(String accountNo)
    {
        // Find all accounts for the specified user_id
        Account account=accountRepository.findByAccountNo(accountNo);
        if(account==null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account Not found");
        else
            return ResponseEntity.ok(account);
    }
}
