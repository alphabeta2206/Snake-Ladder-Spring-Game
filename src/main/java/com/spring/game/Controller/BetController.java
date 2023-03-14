package com.spring.game.Controller;

import com.spring.game.model.Bet;
import com.spring.game.model.User;
import com.spring.game.service.BetService;
import com.spring.game.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Controller
public class BetController {
    @Autowired
    private BetService betService;
    @Autowired
    private UserService userService;



    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping("/all-bets")
    @ResponseBody
    public List<Bet> getAllBets() {
        return betService.getBets();
    }
    @RequestMapping("/user-bets")
    @ResponseBody
    public List<Bet> getUserBetHistory(Principal principal) {
        User user = userService.findByUsername(principal.getName());
        return betService.getBetHistory(user.getId());
    }
}
