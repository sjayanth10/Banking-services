package com.example.Natwest.cashwavebackend.Controller;

import com.example.Natwest.cashwavebackend.Model.Loan;
import com.example.Natwest.cashwavebackend.Service.LoanService;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/loans")
@Configuration
@CrossOrigin(origins = "http://localhost:3000")
public class Controller {

    @Autowired
    LoanService loanService;

    @Autowired
    public Controller(LoanService loanService){
        this.loanService = loanService;
    }

    @PostMapping
    public Loan addLoan(@RequestBody Loan loan){
        return loanService.saveLoan(loan);
    }

    @GetMapping("/loans")
    public List<Loan> getAllLoans(){
        return loanService.getAllLoans();
    }

    @GetMapping("/loans/{loanNumber}")
    public Loan getLoan(@RequestBody String lender, @RequestParam String loanNumber){
        return loanService.findLoan(lender, loanNumber);
    }

}
