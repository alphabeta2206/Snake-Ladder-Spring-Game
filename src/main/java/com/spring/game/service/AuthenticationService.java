package com.spring.game.service;

import com.spring.game.dto.UserRegistrationDTO;
import com.spring.game.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    @Autowired
    private UserRepo userRepo;

    public boolean validate(UserRegistrationDTO userRegistrationDetails){
        if(userRegistrationDetails.getUsername().length()<3 ){
            return false;
        }else if(userRepo.findByUsername(userRegistrationDetails.getUsername()) != null){
            return false;
        }
        boolean hasDigit = false;
        boolean hasAlphabet = false;

        for (int i = 0; i < userRegistrationDetails.getUsername().length(); i++) {
            char f = userRegistrationDetails.getUsername().charAt(i);

            if(Character.isDigit(f)) hasDigit = true;
            if(Character.isAlphabetic(f)) hasAlphabet = true;
        }
        if (!hasDigit || !hasAlphabet){
            return false; // Please Include a minimum of 1 Digit 1 Alphabet in the Username
        }
        if (userRegistrationDetails.getPassword().length() <= 8) {
            return false; // Passwords should have a minimum of 8 characters
        }else
            return userRegistrationDetails.getPassword().equals(userRegistrationDetails.getConfirm_password()); // Passwords do not match
    }
}
