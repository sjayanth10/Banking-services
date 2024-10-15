package com.natwest.Cashwave.TransferService.Service;

import com.natwest.Cashwave.TransferService.DTO.TransactionRequest;
import com.natwest.Cashwave.TransferService.Entity.Account;
import com.natwest.Cashwave.TransferService.Entity.Transaction;
import com.natwest.Cashwave.TransferService.Entity.User;
import com.natwest.Cashwave.TransferService.Repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class TransactionService {
    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    RestTemplate restTemplate;

    public ResponseEntity<?> addTransaction(String user_Id, TransactionRequest transactiondetails,String fromAccount)
    {
        ResponseEntity<User> userResponse = restTemplate.getForEntity(
                "http://localhost:8081/users/getUser/" + user_Id,
                User.class
        );
        if(userResponse.getStatusCode()== HttpStatus.OK)
        {
            ResponseEntity<Account> accountResponse=restTemplate.getForEntity("http://localhost:8082/accounts/getAccount/"+fromAccount, Account.class);
            if(accountResponse.getStatusCode()==HttpStatus.OK)
            {
                User user=userResponse.getBody();
                Transaction transaction=new Transaction();
                transaction.setUser(user);
                transaction.setAmount(Double.parseDouble(transactiondetails.getAmount()));
                transaction.setDescription(transactiondetails.getDescription());
                transaction.setReceiverName(transactiondetails.getReceiverName());
                transaction.setReceiverNo(transactiondetails.getReceiverNo());
                transaction.setFromAccount(fromAccount);
                transaction.setTransactionDate(transactiondetails.getTransactionDate());
                transactionRepository.save(transaction);
                return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
            }
            else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found");
            }
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with ID: " + user_Id);
        }

    }

    public List<Transaction> listTransactions(String user_id)
    {
        return transactionRepository.findByUser_Id(user_id);
    }
    public ResponseEntity<?> transferAmount(String user_id, TransactionRequest transactiondetails, String fromAccount) {
        try {
            double amount = Double.parseDouble(transactiondetails.getAmount());
            ResponseEntity<Account> fromaccountResponse=restTemplate.getForEntity("http://localhost:8082/accounts/getAccount/"+fromAccount,
                    Account.class);
            String description=transactiondetails.getDescription();
            if(description.equalsIgnoreCase("Creditcard bill")||description.equalsIgnoreCase("Insurance amount")||description.equalsIgnoreCase("Loan emi"))
            {
                if(fromaccountResponse.getStatusCode()!=HttpStatus.OK)
                {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("From account was not found");
                }
                Account fromAccount1=fromaccountResponse.getBody();

                if (amount > fromAccount1.getAccountBalance()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Insufficient balance for the transfer");
                }
                double newFromAccountBalance = fromAccount1.getAccountBalance() - amount;
                ResponseEntity<String> updateFromAccountResponse = restTemplate.exchange(
                        "http://localhost:8082/accounts/" + fromAccount + "/updateBalance?balance=" + newFromAccountBalance,
                        HttpMethod.PUT,
                        null,
                        String.class);
                if(updateFromAccountResponse.getStatusCode()!=HttpStatus.OK)
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("An error occurred while updating account balances");

                ResponseEntity<?> transaction = addTransaction(user_id, transactiondetails, fromAccount);
                if (transaction.getStatusCode() == HttpStatus.CREATED) {
                    // Transaction was created successfully
                    return ResponseEntity.status(HttpStatus.OK).body("Transaction completed successfully");
                } else {
                    // Handle transaction creation errors here
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("An error occurred while processing the transaction");
                }
            }
            else {
                ResponseEntity<Account> toaccountResponse=restTemplate.getForEntity("http://localhost:8082/accounts/getAccount/"+transactiondetails.getReceiverNo(),
                        Account.class);

                if (fromaccountResponse.getStatusCode()!=HttpStatus.OK || toaccountResponse.getStatusCode()!=HttpStatus.OK) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body("One or both accounts not found");
                }
                Account fromAccount1=fromaccountResponse.getBody();
                Account toAccount1=toaccountResponse.getBody();
                if (amount > fromAccount1.getAccountBalance()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Insufficient balance for the transfer");
                }

                // Update the account balances
                double newFromAccountBalance = fromAccount1.getAccountBalance() - amount;
                double newToAccountBalance = toAccount1.getAccountBalance() + amount;

                //add updateBalance code
                ResponseEntity<String> updateFromAccountResponse = restTemplate.exchange(
                        "http://localhost:8082/accounts/" + fromAccount + "/updateBalance?balance=" + newFromAccountBalance,
                        HttpMethod.PUT,
                        null,
                        String.class);

                ResponseEntity<String> updateToAccountResponse = restTemplate.exchange(
                        "http://localhost:8082/accounts/" + toAccount1.getAccountNo() + "/updateBalance?balance=" + newToAccountBalance,
                        HttpMethod.PUT,
                        null,
                        String.class);
                if(updateToAccountResponse.getStatusCode()!=HttpStatus.OK||updateFromAccountResponse.getStatusCode()!=HttpStatus.OK)
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("An error occurred while updating account balances");

                ResponseEntity<?> transaction = addTransaction(user_id, transactiondetails, fromAccount);
                if (transaction.getStatusCode() == HttpStatus.CREATED) {
                    // Transaction was created successfully
                    return ResponseEntity.status(HttpStatus.OK).body("Transaction completed successfully");
                } else {
                    // Handle transaction creation errors here
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("An error occurred while processing the transaction");
                }
            }
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid amount format");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }
}
