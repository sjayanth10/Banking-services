package com.natwest.Cashwave.CreditcardService.Controller;

import com.natwest.Cashwave.CreditcardService.DTO.CreditCardRequest;
import com.natwest.Cashwave.CreditcardService.Service.CreditcardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/creditcard")
@CrossOrigin(origins = "*")
public class CreditcardController {
    @Autowired
    CreditcardService creditCardService;

    @PostMapping("/{user_id}/addCreditCard")
    public ResponseEntity<?> addCreditCard(@PathVariable String user_id, @RequestBody CreditCardRequest creditCardRequest)
    {
        return creditCardService.addCreditCard(user_id,creditCardRequest);
    }

    @GetMapping("/{user_id}/listCreditCard")
    public ResponseEntity<?> listCreditCard(@PathVariable String user_id)
    {
        return creditCardService.listCreditCards(user_id);
    }
}
