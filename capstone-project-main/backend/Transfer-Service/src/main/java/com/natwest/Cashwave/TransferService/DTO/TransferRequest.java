package com.natwest.Cashwave.TransferService.DTO;
import lombok.Data;

@Data
public class TransferRequest {
    public TransactionRequest transactionRequest;
    public String fromAccountNo;
}
