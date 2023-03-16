package com.security.SecureApp.repository;
import com.security.SecureApp.modal.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);





}
