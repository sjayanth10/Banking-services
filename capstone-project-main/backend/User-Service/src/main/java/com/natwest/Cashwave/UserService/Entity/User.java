package com.natwest.Cashwave.UserService.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "Users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private String id;
    private String mobilenumber;
    private String emailid;
    private String name;
    private String dateofbirth;
    private String aadharcardnumber;
    private String security_PIN;
    private String  salt;
    private String upi_ID;
    private String block;
    private String resetToken;
    private Date resetTokenExpiryDate;
}