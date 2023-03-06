package com.spring.game.Controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import java.security.Principal;


@Controller
public class MainController {
    @RequestMapping("/login")
    public String showLogin() {
        return "login";
    }
    @RequestMapping("/lobby")
    public String showLobby(Model model, Principal principal) {
        model.addAttribute("name", principal.getName());
        return "lobby";
    }

}
