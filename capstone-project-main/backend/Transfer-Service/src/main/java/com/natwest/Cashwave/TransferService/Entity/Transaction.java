package com.natwest.Cashwave.TransferService.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "Transaction")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id

    public String id;
    public String receiverName;
    public String receiverNo;
    public double amount;
    public String description;
    public String fromAccount;
    public String transactionDate;
    @DBRef
    User user;
}

