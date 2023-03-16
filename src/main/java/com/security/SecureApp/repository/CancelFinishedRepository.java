package com.security.SecureApp.repository;

import com.security.SecureApp.modal.CancelFinishedGame;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CancelFinishedRepository extends JpaRepository<CancelFinishedGame, Long> {
}
