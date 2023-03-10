package com.Task1.Task.controller;

import com.Task1.Task.dto.UserRegistrationDTO;
import com.Task1.Task.model.Role;
import com.Task1.Task.model.User;
import com.Task1.Task.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.swing.*;
import java.security.Principal;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    UserService userService;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @RequestMapping("/home")
    @ResponseBody
    public String home() {
        return "Welcome please login or register";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new User());
        return "loginForm";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "registerForm";
    }

    @PostMapping("/process_register")
    public String processRegister(@RequestParam String username, @RequestParam String name, @RequestParam String password, @RequestParam Double walletAmt, @RequestParam String currencyCode) {
        User user = new User();
        user.setUserName(username);
        user.setName(name);
        user.setPassword(passwordEncoder.encode(password));
        user.setWalletAmt(walletAmt);
        user.setCurrencyCode(currencyCode);
        Role role = new Role("ROLE_USER");
        user.setRoles(List.of(role));
        userService.saveUser(user);
        return "redirect:/login";
    }




    @GetMapping("/logout")
    public String logout() {
        return "logoutForm";
    }
}
