package com.natwest.Cashwave.AccountService.DTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountRequest {
    @Id
    @Field(name="id")
    private String accountNo;
    private String accountBankName;
    private String accountBalance;
}
