
package com.project.back_end.dto;

public class Login {
    private String identifier; // User's email or username
    private String password;    // User's password

    // Default constructor
    public Login() {
    }

    // Constructor to initialize both fields
    public Login(String identifier, String password) {
        this.identifier = identifier;
        this.password = password;
    }

    // Getter for identifier
    public String getIdentifier() {
        return identifier;
    }

    // Setter for identifier
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    // Getter for password
    public String getPassword() {
        return password;
    }

    // Setter for password
    public void setPassword(String password) {
        this.password = password;
    }
}
