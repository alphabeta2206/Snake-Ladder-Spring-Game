package com.spring.game.Controller;

import com.spring.game.dto.UserRegistrationDTO;
import com.spring.game.model.User;
import com.spring.game.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserRegistrationController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserRegistrationController.class);
    @Autowired
    private UserService userService;
    @RequestMapping("/register")
    @ResponseBody
    public User registerUser(@RequestBody UserRegistrationDTO user_details){
        return userService.saveUser(user_details);
    }
}
