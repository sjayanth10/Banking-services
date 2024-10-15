package com.natwest.Cashwave.CreditcardService.Service;

import com.natwest.Cashwave.CreditcardService.DTO.CreditCardRequest;
import com.natwest.Cashwave.CreditcardService.Entity.CreditCard;
import com.natwest.Cashwave.CreditcardService.Entity.User;
import com.natwest.Cashwave.CreditcardService.Repository.CreditCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

@Service
public class CreditcardService {
    @Autowired
    CreditCardRepository creditCardRepository;

    @Autowired
    RestTemplate restTemplate;

    public ResponseEntity<?> addCreditCard(String userId, CreditCardRequest creditCardRequest)
    {
        // Find the user by ID


        ResponseEntity<User> userResponse = restTemplate.getForEntity(
                "http://localhost:8080/users/getUser/" + userId,
                User.class
        );
        if(userResponse.getStatusCode()==HttpStatus.OK)
        {
            User user=userResponse.getBody();
            if(creditCardRepository.existsByCreditCardNo(creditCardRequest.getCreditCardNo()))
                ResponseEntity.status(HttpStatus.CONFLICT).body("Credit card number already exists.");
            CreditCard newCreditCard = new CreditCard();
            newCreditCard.setCreditCardNo(creditCardRequest.getCreditCardNo());
            newCreditCard.setBankName(creditCardRequest.getBankName());
            newCreditCard.setCardHolderName(creditCardRequest.getCardHolderName());
            newCreditCard.setExpiryDate(creditCardRequest.getExpiryDate());
            newCreditCard.setUser(user)  ;
            return new ResponseEntity<>(creditCardRepository.save(newCreditCard),HttpStatus.OK);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with ID: " + userId);
        }
    }

    public ResponseEntity<?> listCreditCards(String userId)
    {
        return new ResponseEntity<>(creditCardRepository.findByUser_Id(userId),HttpStatus.OK);
    }
}
