package com.Task1.Task.controller;

import com.Task1.Task.dto.UserRegistrationDTO;
import com.Task1.Task.exceptions.SessionActiveException;
import com.Task1.Task.model.Role;
import com.Task1.Task.model.User;
import com.Task1.Task.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Operation(summary = "Home Page")
    @RequestMapping("/home")
    @ResponseBody
    public String home() {
        return "Welcome please login or register";
    }

    @Operation(summary = "Displays Login Form")
    @GetMapping("/login")
    public String showLoginForm(HttpSession session) {
        if(session.getAttribute("sessionVar") == null) return "loginForm";
        return "redirect:/lobby";
    }

    @Operation(summary = "Displays Register Form")
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "registerForm";
    }

    @Operation(summary = "Process User Register")
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

    @Operation(summary = "Logged in User Data")
    @GetMapping("/profile")
    public ResponseEntity<User> getProfile(Principal principal ) {
        return new ResponseEntity<User>( userService.getByUsername( principal.getName() ) , HttpStatus.OK ) ;
    }

    @Operation(summary = "Process User Logout")
    @GetMapping("/logout")
    public String logout() {
        return "logoutForm";
    }
}
