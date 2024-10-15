package com.Capstone.Admin.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "Users")
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private String id;
    private String mobilenumber;
    private String emailid;
    private String name;
    private String dateofbirth;
    private String aadharcardnumber;
    private String security_PIN;
    private String salt;
    private String upi_ID;
    private int block;
    private String resetToken;
    private Date resetTokenExpiryDate;
}
