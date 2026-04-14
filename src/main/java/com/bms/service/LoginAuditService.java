package com.bms.service;

import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Service;

/**
 * LoginAuditService uses Spring Security events to log authentication attempts
 * via the AryanUrs-authored AuditService.
 */
@Service
public class LoginAuditService {

    private final AuditService auditService;

    public LoginAuditService(AuditService auditService) {
        this.auditService = auditService;
    }

    @EventListener
    public void onSuccess(AuthenticationSuccessEvent event) {
        String email = event.getAuthentication().getName();
        // Since we don't have the full Account object in the event easily,
        // we simulate the polymorphic logging using a placeholder or by fetching the user.
        // For simplicity in this demo of LSP, we log the string directly.
        auditService.logAction(() -> email, "SUCCESSFUL_LOGIN");
    }

    @EventListener
    public void onFailure(AuthenticationFailureBadCredentialsEvent event) {
        String email = event.getAuthentication().getName();
        auditService.logAction(() -> email, "FAILED_LOGIN_ATTEMPT");
    }

    // Helper interface for LSP demonstration in this specific listener
    @FunctionalInterface
    public interface SimpleAccount extends com.bms.entity.Account {
        @Override
        default String getName() { return "System User"; }
        @Override
        default String getRoleName() { return "N/A"; }
    }
}
