package com.spring.game.service;

import com.spring.game.model.CurrencyExchangeRate;
import com.spring.game.repository.CurrencyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CurrencyService {

    @Autowired
    private CurrencyRepo currencyRepo;

    public CurrencyExchangeRate saveCurrency(CurrencyExchangeRate currency) {
        return currencyRepo.save(currency);
    }

    public List<CurrencyExchangeRate> getCurrencyExchanges() {
        return currencyRepo.findAll();
    }

    public double convertToEuro(double amount, String currency){
        CurrencyExchangeRate currencyExchangeRate = currencyRepo.findByCode(currency);
        return currencyExchangeRate.getRate()*amount;
    }

    public double convertFromEuro(double amount, String currency){
        CurrencyExchangeRate currencyExchangeRate = currencyRepo.findByCode(currency);
        return amount/currencyExchangeRate.getRate();
    }

}
