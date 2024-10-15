package com.example.Natwest.cashwavebackend.Service;

import com.example.Natwest.cashwavebackend.Model.Loan;
import com.example.Natwest.cashwavebackend.Repo.Repo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanService {

    private final Repo loanRepo;

    @Autowired
    public LoanService(Repo loanRepo){
        this.loanRepo=loanRepo;
    }

    public Loan saveLoan(Loan loan){
        return loanRepo.save(loan);
    }

    public List<Loan> getAllLoans(){
        return loanRepo.findAll();
    }

    public Loan findLoan(String lender, String loanNumber) {
        List<Loan> loanList = loanRepo.findAll();
        for (Loan loan : loanList) {
            if (loan.getLoanNumber().equals(loanNumber)) {
                return loan;
            }
        }
        // If no matching loan is found, you can return null or handle it as needed
        return null;
    }
}
