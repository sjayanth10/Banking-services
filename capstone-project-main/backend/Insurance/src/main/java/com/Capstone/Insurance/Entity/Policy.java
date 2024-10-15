package com.Capstone.Insurance.Entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
@Data
@Document(collection = "insurance_policies")
public class Policy {
    @Id
    private String id;

    private String policyProviderName;

    private String policynumber;

    private String policytype;

    private Double policyamount;

    private Double premium;

    private String emailid;

    private String startdate;

    private String enddate;

    private String AccountNumber;

    // getters and setters


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPolicyProviderName() {
        return policyProviderName;
    }

    public void setPolicyProviderName(String policyProviderName) {
        this.policyProviderName = policyProviderName;
    }

    public String getpolicynumber() {
        return policynumber;
    }

    public void setpolicynumber(String policynumber) {
        this.policynumber = policynumber;
    }

    public String getPolicytype() {
        return policytype;
    }

    public void setPolicytype(String policytype) {
        this.policytype = policytype;
    }

    public Double getPolicyamount() {
        return policyamount;
    }

    public void setPolicyamount(Double policyamount) {
        this.policyamount = policyamount;
    }

    public Double getPremium() {
        return premium;
    }

    public void setPremium(Double premium) {
        this.premium = premium;
    }

    public String getEmailid() {
        return emailid;
    }

    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getAccountNumber() {
        return AccountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        AccountNumber = accountNumber;
    }
}

