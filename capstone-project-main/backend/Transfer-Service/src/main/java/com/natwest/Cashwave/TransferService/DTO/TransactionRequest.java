package com.natwest.Cashwave.TransferService.DTO;

import lombok.Data;

@Data
public class TransactionRequest {

    public String receiverName;
    public String receiverNo;
    public String amount;
    public String description;
    public String transactionDate;
}
