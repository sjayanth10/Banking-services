package com.natwest.Cashwave.AccountService.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Document(collection = "Accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    @Id
    @Field(name = "_id")
    private String accountNo;
    private String accountBankName;
    private double accountBalance;

    @DBRef
    private User user;
}
