package com.spring.game.Controller;


import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;


@Controller
public class MainController {
    @RequestMapping("/login")
    public String showLogin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            /* The user is logged in :) */
            return "redirect:/lobby";
        }
        return "login";
    }
    @RequestMapping("/lobby")
    public String showLobby(Model model, Principal principal) {
        model.addAttribute("name", principal.getName());
        return "lobby";
    }

}
