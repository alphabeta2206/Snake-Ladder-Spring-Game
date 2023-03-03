package com.Task1.Task.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class BetController {
    @RequestMapping("/placebet")
    @ResponseBody
    public String placeBet(){
        return "Bet Placed";
    }

    @RequestMapping("/payout")
    @ResponseBody
    public void payOut(){

    }
}
