package com.bms.entity;

/**
 * Liskov Substitution Principle (LSP): This interface allows any account type 
 * (User, Admin, etc.) to be used interchangeably in auditing and logging services.
 */
public interface Account {
    String getEmail();
    String getName();
    String getRoleName();
}
