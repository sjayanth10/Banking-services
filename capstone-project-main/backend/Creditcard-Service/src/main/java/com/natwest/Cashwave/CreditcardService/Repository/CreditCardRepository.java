package com.natwest.Cashwave.CreditcardService.Repository;
import com.natwest.Cashwave.CreditcardService.Entity.CreditCard;
import org.springframework.data.mongodb.core.MongoAdminOperations;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CreditCardRepository extends MongoRepository<CreditCard,String> {
    boolean existsByCreditCardNo(String creditCardNo);

    List<CreditCard> findByUser_Id(String userId);
}