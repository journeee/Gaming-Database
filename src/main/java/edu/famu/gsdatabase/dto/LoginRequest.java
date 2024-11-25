package edu.famu.gsdatabase.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LoginRequest {
    // Getters and Setters
    @NotBlank(message = "Identifier (username or email) is required")
    private String identifier;

    @NotBlank(message = "Password is required")
    private String password;

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
