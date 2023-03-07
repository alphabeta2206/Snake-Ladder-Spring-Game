package com.Task1.Task.controller;

import com.Task1.Task.model.CurrencyExchangeRate;
import com.Task1.Task.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CurrencyController {

    @Autowired
    CurrencyService currencyService;

    @RequestMapping("/add")
    public void setCurrency(@RequestBody CurrencyExchangeRate currency) {
        currencyService.setCurrency(currency);
    }
}
