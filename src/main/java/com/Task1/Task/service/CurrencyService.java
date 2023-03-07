package com.Task1.Task.service;

import com.Task1.Task.model.CurrencyExchangeRate;
import com.Task1.Task.repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CurrencyService {

    @Autowired
    CurrencyRepository currencyRepository;

    public double convertToEuro(String code, double amount){
        return amount;
    }

    public double convertFromEuro(String code, double amount){
        return amount;
    }

    public void setCurrency(CurrencyExchangeRate currency) {
        currencyRepository.save(currency);
    }

    public double getMultiplier(String code){
        CurrencyExchangeRate currency = currencyRepository.findByCode(code);
        return currency.getMultiplier();
    }
}
