package com.natwest.Cashwave.UserService.Service;

import com.natwest.Cashwave.UserService.Entity.User;
import com.natwest.Cashwave.UserService.Repository.UserRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.data.mongodb.core.MongoTemplate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;


@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    private final MongoTemplate mongoTemplate;
    @Autowired
    public UserService(UserRepository userRepository, MongoTemplate mongoTemplate) {
        this.userRepository = userRepository;
        this.mongoTemplate = mongoTemplate;
    }
    @Autowired
    private JavaMailSender javaMailSender;

    public User registerUser(User user) {
        // Perform validation and other business logic if needed
        return userRepository.save(user);
    }
    public void updateResetToken(String email, String resetToken, Date resetTokenExpiration) {
        Query query = new Query(Criteria.where("emailid").is(email));
        Update update = new Update()
                .set("resetToken", resetToken)
                .set("resetTokenExpiration", resetTokenExpiration);

        mongoTemplate.updateFirst(query, update, User.class);
    }
//    private void sendPasswordResetEmail(User user, String resetToken) {
//        try {
//            // Create a SimpleMailMessage
//            SimpleMailMessage mailMessage = new SimpleMailMessage();
//
//            // Set the recipient email address
//            mailMessage.setTo(user.getEmailid());
//
//            // Set the subject of the email
//            mailMessage.setSubject("Password Reset Request");
//
//            // Construct the email message body with the reset token
//            String resetLink = "http://your-reset-link.com?token=" + resetToken; // Replace with your actual reset link
//            mailMessage.setText("To reset your password, click on the following link:\n" + resetLink);
//
//            // Send the email
//            javaMailSender.send(mailMessage);
//        } catch (MailException e) {
//            // Handle email sending errors
//            e.printStackTrace();
//            // You can log the error or take appropriate action here
//        }
//    }
    public boolean isUserEmailUnique(String emailid) {
        // Use your UserRepository to check if the email exists in the database
        User existingUser = userRepository.findByEmailid(emailid);

        // If no user with the given email is found, it's unique
        return existingUser==null;
    }

    private String hashPassword(String password, String salt) {
        // You can use a secure hashing algorithm here (e.g., bcrypt)
        // For simplicity, let's assume you're using SHA-256
        String saltedPassword = password + salt;
        // Perform the hashing (you may want to use a more secure algorithm)
        return DigestUtils.sha256Hex(saltedPassword);
    }

    public ResponseEntity<User> loginUser(String emailid, String password) {
        User user = userRepository.findByEmailid(emailid);
        if (user != null) {
            String storedSalt = user.getSalt(); // Retrieve the salt from the user's record
            String storedHashedPIN = user.getSecurity_PIN();
            if (storedSalt != null && storedHashedPIN != null) {
                // Calculate the hash of the entered security PIN using the stored salt
                String calculatedHashedPIN = hashPassword(password, storedSalt);

                // Check if the calculated hash matches the stored hash
                if (calculatedHashedPIN.equals(storedHashedPIN)) {
                    return new ResponseEntity<>(user, HttpStatus.OK); // PIN is correct
                }
                else {
                    return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
                }
            }
            else {
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            }
        }
        return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
    }
    public User updateUser(String userId, User updatedUserData) {
        // Find the user by ID in the database
        User existingUser = userRepository.findById(userId).orElse(null);

        if (existingUser == null) {
            // User not found, return null or throw an exception
            return null;
        }

        // Update user information based on the provided updatedUserData
        // You can choose which fields are updatable and apply the changes accordingly
        // For example, if only the name and email are updatable:
        if (updatedUserData.getName() != null) {
            existingUser.setName(updatedUserData.getName());
        }
        if (updatedUserData.getEmailid() != null) {
            existingUser.setEmailid(updatedUserData.getEmailid());
        }
        if (updatedUserData.getAadharcardnumber() != null) {
            existingUser.setAadharcardnumber(updatedUserData.getAadharcardnumber());
        }
        if (updatedUserData.getMobilenumber() != null) {
            existingUser.setMobilenumber(updatedUserData.getMobilenumber());
        }
        if (updatedUserData.getSecurity_PIN() != null) {
            existingUser.setSecurity_PIN(updatedUserData.getSecurity_PIN());
        }
        if (updatedUserData.getSalt() != null) {
            existingUser.setSalt(updatedUserData.getSalt());
        }
        // Update other fields as needed

        // Save the updated user object back to the database
        User updatedUser = userRepository.save(existingUser);

        return updatedUser;
    }
    public User updateUser_Password(String userId, User updatedUserData) {
        // Find the user by ID in the database
        User existingUser = userRepository.findById(userId).orElse(null);

        if (existingUser == null) {
            // User not found, return null or throw an exception
            return null;
        }

        // Update user information based on the provided updatedUserData
        // You can choose which fields are updatable and apply the changes accordingly
        // For example, if only the name and email are updatable:
        if (updatedUserData.getName() != null) {
            existingUser.setName(updatedUserData.getName());
        }
        if (updatedUserData.getEmailid() != null) {
            existingUser.setEmailid(updatedUserData.getEmailid());
        }
        if (updatedUserData.getAadharcardnumber() != null) {
            existingUser.setAadharcardnumber(updatedUserData.getAadharcardnumber());
        }
        if (updatedUserData.getMobilenumber() != null) {
            existingUser.setMobilenumber(updatedUserData.getMobilenumber());
        }
        if (updatedUserData.getSecurity_PIN() != null) {
            existingUser.setSecurity_PIN(updatedUserData.getSecurity_PIN());
        }
        // Update other fields as needed

        // Save the updated user object back to the database
        User updatedUser = userRepository.save(existingUser);

        return updatedUser;
    }
    public void initiatePasswordReset(String email) {
        User user = userRepository.findByEmailid(email);

        if (user != null) {
            // Generate a unique reset token (you should implement this logic)
            String resetToken = generateResetToken(user);

            // Set the reset token and its expiration date in the user entity
            user.setResetToken(resetToken);
            user.setResetTokenExpiryDate(calculateExpiryDate());

            // Save the updated user entity back to the database
            userRepository.save(user);

//            // Send a password reset email to the user's email address
//            sendPasswordResetEmail(user, resetToken);
        }
    }
    public Date calculateExpiryDate() {
        // Create a Calendar instance to work with dates and times
        Calendar calendar = Calendar.getInstance();

        // Set the expiration time to one hour from the current time
        calendar.setTime(new Date()); // Set the calendar to the current time
        calendar.add(Calendar.HOUR_OF_DAY, 1); // Add one hour to the current time

        // Get the resulting date and time, which will be one hour from the current time
        return calendar.getTime();
    }


    public String generateResetToken(User user) {
        String resetToken = UUID.randomUUID().toString();

        // Calculate expiration time as a java.util.Date (one hour from now)
        Date expirationTime = calculateExpiryDate();

        // Store resetToken and expirationTime in the user's document in MongoDB
        updateResetToken(user.getEmailid(), resetToken, expirationTime);

        return resetToken;
    }



    public ResponseEntity<User> getUser(String emailID) {
        try {
            User user = userRepository.findByEmailid(emailID);

            if (user != null) {
                return ResponseEntity.ok(user);
            } else {
                // User not found, return an appropriate response, e.g., HttpStatus.NOT_FOUND
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            // Handle any other exceptions that might occur, e.g., database errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


}


