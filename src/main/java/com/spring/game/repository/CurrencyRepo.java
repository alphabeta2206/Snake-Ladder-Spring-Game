package com.spring.game.repository;

import com.spring.game.model.CurrencyExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRepo extends JpaRepository<CurrencyExchangeRate, Long> {
    CurrencyExchangeRate findByCode(String code);
}
