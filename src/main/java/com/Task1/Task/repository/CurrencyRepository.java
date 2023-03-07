package com.Task1.Task.repository;

import com.Task1.Task.model.CurrencyExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyRepository extends JpaRepository<CurrencyExchangeRate, Long> {
    public CurrencyExchangeRate findByCode(String code);
}
