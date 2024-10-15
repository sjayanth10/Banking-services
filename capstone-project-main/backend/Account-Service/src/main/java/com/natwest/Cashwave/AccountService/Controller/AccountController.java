package com.natwest.Cashwave.AccountService.Controller;

import com.natwest.Cashwave.AccountService.DTO.AccountRequest;
import com.natwest.Cashwave.AccountService.Service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
@CrossOrigin(origins="*")
public class AccountController {
    @Autowired
    AccountService accountService;

    @PostMapping("/{user_id}/addAccount")
    public ResponseEntity<?> addAccount(@PathVariable String user_id, @RequestBody AccountRequest accountRequest)
    {
        return accountService.addAccount(user_id,accountRequest);
    }

    @GetMapping("/{user_id}/listAccounts")
    public ResponseEntity<?> listAccounts(@PathVariable String user_id)
    {
        return accountService.listAccount(user_id);
    }

    @PutMapping("/{accountNo}/updateBalance")
    public ResponseEntity<?> updateBalance(@PathVariable String accountNo,String balance)
    {
        double newbalance=Double.parseDouble(balance);
        return accountService.updateAccountBalance(accountNo,newbalance);
    }

    @GetMapping("/getAccount/{accountNo}")
    public ResponseEntity<?> getAccount(@PathVariable String accountNo)
    {
        return accountService.findByaccountNo(accountNo);
    }
}
