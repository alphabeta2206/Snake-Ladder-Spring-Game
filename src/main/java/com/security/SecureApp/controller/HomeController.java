package com.security.SecureApp.controller;

import com.security.SecureApp.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import com.security.SecureApp.Service.MyUserDetailsService;
import com.security.SecureApp.modal.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@Controller
public class HomeController {
    @Autowired
    MyUserDetailsService myUserDetailsService;

    @Autowired
    UserRepository userRepository;


    @RequestMapping(value = "/home")
    public String Home(Model model,  Principal principal) {
        String username = principal.getName();
        model.addAttribute("user_details", myUserDetailsService.findUserByUsername(username));

        return "mainhome";
    }

    @RequestMapping(value = "/home/error")
    public String error() {
        return "redirect:/home";
    }

    @RequestMapping(value ="/register")
    public String Register() {
        return "register";

    }
    @RequestMapping(value = "/processRegister")
    @ResponseBody
    public ModelAndView processRegister(@RequestParam String username, @RequestParam String password, @RequestParam long wallet_amt) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setWallet_amt(wallet_amt);
        user.setIn_game(false);
        myUserDetailsService.saveUserDetails(user);
        return new ModelAndView("redirect:/login");
    }
    @RequestMapping(value ="/login")
    public String userLogin() {
            return "login";
//        return "redirect:/processLogin";
    }

    @RequestMapping(value ="/updateWallet/{id}")
    public String updateWallet(Model model,  Principal principal, @PathVariable long id) {
        String username = principal.getName();
        model.addAttribute("user_details", myUserDetailsService.findUserByUsername(username));
        return "updateWallet";
    }
    @PostMapping(value ="/processUpdateWallet/{id}")
    public String processUpdateWallet(@RequestParam long id,
                                      Model model,
                                      Principal principal,
                                      @ModelAttribute User user) {
        User user1 = myUserDetailsService.findUserByUsername(principal.getName());
        user1.setWallet_amt(user.getWallet_amt());
        myUserDetailsService.updateWalletAmount(user1);
        return "redirect:/home";
    }

    @RequestMapping(value ="/deleteUser")
    public String deleteUser() {
        return "deleteConfirmation";
    }

    @RequestMapping(value ="/processDeleteUser")
    public String processDeleteUser() {

        myUserDetailsService.deleteUserDetails();
        return "redirect:/logout";

    }



    @RequestMapping(value ="/showProfile")
    public String showProfile() {
        return "showProfile";
    }

}
