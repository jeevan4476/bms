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
        auditService.logAction(new com.bms.entity.Account() {
            @Override public String getEmail() { return email; }
            @Override public String getName() { return "System User"; }
            @Override public String getRoleName() { return "N/A"; }
        }, "SUCCESSFUL_LOGIN");
    }

    @EventListener
    public void onFailure(AuthenticationFailureBadCredentialsEvent event) {
        String email = event.getAuthentication().getName();
        auditService.logAction(new com.bms.entity.Account() {
            @Override public String getEmail() { return email; }
            @Override public String getName() { return "System User"; }
            @Override public String getRoleName() { return "N/A"; }
        }, "FAILED_LOGIN_ATTEMPT");
    }
}
