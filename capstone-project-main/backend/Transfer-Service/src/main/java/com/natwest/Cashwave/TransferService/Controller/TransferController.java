package com.natwest.Cashwave.TransferService.Controller;

import com.natwest.Cashwave.TransferService.DTO.TransactionRequest;
import com.natwest.Cashwave.TransferService.DTO.TransferRequest;
import com.natwest.Cashwave.TransferService.Entity.Transaction;
import com.natwest.Cashwave.TransferService.Service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transfer")
@CrossOrigin(origins = "*")
public class TransferController {

    @Autowired
    TransactionService transactionService;
    @PostMapping("/{user_id}/addTrans")
    public ResponseEntity<?> addTransaction(@PathVariable String user_id, @RequestBody TransferRequest transferRequest)
    {
        TransactionRequest transaction=transferRequest.getTransactionRequest();
        String fromAccountNo=transferRequest.getFromAccountNo();
        return transactionService.addTransaction(user_id,transaction,fromAccountNo);
    }

    @GetMapping("/{user_id}/listTrans")
    public ResponseEntity<List<Transaction>> listTransactions(@PathVariable String user_id)
    {
        return new ResponseEntity<>(transactionService.listTransactions(user_id),HttpStatus.OK);
    }

    @PutMapping("/{user_id}/makeTransfer")
    public ResponseEntity<?> makeTransfer(@PathVariable String user_id, @RequestBody TransferRequest transferRequest)
    {
        try {
            TransactionRequest transactionRequest = transferRequest.getTransactionRequest();
            String fromAccount = transferRequest.getFromAccountNo();
            return transactionService.transferAmount(user_id, transactionRequest, fromAccount);
        } catch (Exception e) {
            // Handle any unexpected exceptions here and return an appropriate response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while processing the request");
        }
    }
}
