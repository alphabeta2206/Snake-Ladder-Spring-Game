package com.security.SecureApp.repository;

import com.security.SecureApp.modal.Games;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GameRepository extends JpaRepository<Games, Long> {

    @Query(value = "SELECT * FROM games WHERE status =?1", nativeQuery = true)
    public List<Games> findAllByStatus(String status);






}
