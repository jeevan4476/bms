package com.bms.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.bms.entity.Account;

/**
 * AuditService demonstrates the Liskov Substitution Principle (LSP).
 * It can process any object that implements the Account interface (User, etc.)
 * without needing to know the specific concrete class.
 */
@Service
public class AuditService {
    private static final Logger log = LoggerFactory.getLogger(AuditService.class);

    public void logAction(Account account, String action) {
        log.info("AUDIT LOG: [User: {}] [Role: {}] [Action: {}]", 
            account.getEmail(), account.getRoleName(), action);
    }
}
