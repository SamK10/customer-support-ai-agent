package com.company.supportagent.audit;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuditLogger {

    private final AuditService auditService;

    public AuditLogger(AuditService auditService) {
        this.auditService = auditService;
    }

    public void logDecision(
            Long ticketId,
            String customerId,
            String intent,
            List<String> steps,
            boolean fallbackUsed,
            List<String> policyIds) {

        AuditEvent event = new AuditEvent(
                ticketId,
                customerId,
                intent,
                steps,
                fallbackUsed,
                policyIds
        );

        auditService.record(event);
    }
}
