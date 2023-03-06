package com.spring.game.Controller;

import com.spring.game.model.CurrencyExchangeRate;
import com.spring.game.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/currency")
public class CurrencyController {

    @Autowired
    private CurrencyService currencyService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping("/add")
    @ResponseBody
    public CurrencyExchangeRate add(@RequestBody CurrencyExchangeRate currencyExchangeRate){
        return currencyService.saveCurrency(currencyExchangeRate);
    }

    @RequestMapping("/all")
    @ResponseBody
    public List<CurrencyExchangeRate> findAll(){
        return currencyService.getCurrencyExchanges();
    }
}
//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
