package com.spring.game.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    @NotNull(message = "Username is required.")
    @Size(min = 1, message = "Username is required.")
    private String username;

    @NotNull(message = "Password is required.")
    @Size(min = 6, message = "Password should be atleast 6 characters.")
    private String password;
}
