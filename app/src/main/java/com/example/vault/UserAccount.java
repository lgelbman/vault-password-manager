package com.example.vault;

public class UserAccount {

    private String username;
    private String passwordHash;
    private String accountType;

    public UserAccount(String username, String passwordHash, String accountType) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.accountType = accountType;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getAccountType() { return this.accountType; }
}
