package com.natwest.Cashwave.CreditcardService.DTO;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
public class CreditCardRequest {
    @Id
    @Field(name="_id")
    public String creditCardNo;
    public String bankName;
    public String cardHolderName;
    public String expiryDate;
}
