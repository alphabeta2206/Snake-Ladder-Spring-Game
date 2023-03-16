package com.security.SecureApp.repository;

import com.security.SecureApp.modal.Bet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BetRepository extends JpaRepository<Bet, Long> {
}
