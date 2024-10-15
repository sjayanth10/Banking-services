package com.natwest.Cashwave.UserService.Controller;

import com.natwest.Cashwave.UserService.Dto.logincred;
import com.natwest.Cashwave.UserService.Entity.User;
import com.natwest.Cashwave.UserService.Repository.UserRepository;
import com.natwest.Cashwave.UserService.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins="http://localhost:3000")
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        try {
            User registeredUser = userService.registerUser(user);
            return new ResponseEntity<>(registeredUser, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/checkEmail")
    public ResponseEntity<Boolean> checkEmail(@RequestParam String emailid) {
        try {
            // Check if the email already exists in the database
            boolean isEmailUnique = userService.isUserEmailUnique(emailid);
            return ResponseEntity.ok(isEmailUnique);
        } catch (Exception e) {
            // Handle exceptions appropriately and return an error response if needed
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }
    @PostMapping("/login")
    public ResponseEntity<User> loginUser(@RequestBody logincred loginreq) {
        String emailid = loginreq.getEmailid();
        String security_PIN = loginreq.getSecurity_PIN();
        return userService.loginUser(emailid, security_PIN);
    }
    @PutMapping("/update/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable String userId, @RequestBody User updatedUserData) {
        try {
            // Call your UserService method to update the user data in the database
            User updatedUser = userService.updateUser(userId, updatedUserData);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping("/update_password/{userId}")
    public ResponseEntity<User> updateUser_Password(@PathVariable String userId, @RequestBody User updatedUserData) {
        try {
            // Call your UserService method to update the user data in the database
            User updatedUser = userService.updateUser_Password(userId, updatedUserData);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/forgot-password")
    public ResponseEntity<?> initiatePasswordReset(@RequestBody Map<String, String> request) {
        String email = request.get("emailid");

        // Check if the email exists in the database using UserRepository
        User user = userRepository.findByEmailid(email);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        // Initiate the password reset process using UserService
        userService.initiatePasswordReset(email);

        return ResponseEntity.status(HttpStatus.OK).body("Password reset initiated. Check your email.");
    }
    @GetMapping("/validate-reset-token")
    public ResponseEntity<String> validateResetToken(@RequestParam String token) {
        Optional<User> optionalUser = userRepository.findByResetToken(token);
        User user = optionalUser.orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token");
        }

        Date resetTokenExpiryDate = user.getResetTokenExpiryDate();
        Date now = new Date();

        if (resetTokenExpiryDate == null || resetTokenExpiryDate.before(now)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Expired token");
        }

        return ResponseEntity.ok("Token is valid");
    }
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        // Check if the token is valid and not expired
        Optional<User> optionalUser = userRepository.findByResetToken(token);
        User user = optionalUser.orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token");
        }

        Date resetTokenExpiryDate = user.getResetTokenExpiryDate();
        Date now = new Date();

        if (resetTokenExpiryDate == null || resetTokenExpiryDate.before(now)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Expired token");
        }

        // Update the user's password
        user.setSecurity_PIN(newPassword); // Use password hashing
        user.setResetToken(null);
        user.setResetTokenExpiryDate(null);
        userRepository.save(user);

        return ResponseEntity.ok("Password reset successful");
    }

    @GetMapping("/getUser/{emailID}")
    public ResponseEntity<User> getUser(@PathVariable String emailID)
    {
        return userService.getUser(emailID);
    }

}
