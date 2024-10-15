package com.example.Natwest.cashwavebackend.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@Document(collection = "Loans")
@NoArgsConstructor
@AllArgsConstructor
public class Loan {

    private String id;
    private String loanNumber;
    private String lender;
    private double amountPayable;
}
