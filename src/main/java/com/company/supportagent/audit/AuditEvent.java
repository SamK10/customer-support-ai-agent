package com.company.supportagent.audit;

import java.time.Instant;
import java.util.List;

public class AuditEvent {

    private final Instant timestamp;
    private final Long ticketId;
    private final String customerId;
    private final String intent;
    private final List<String> plannerSteps;
    private final boolean fallbackUsed;
    private final List<String> policyIds;

    public AuditEvent(
            Long ticketId,
            String customerId,
            String intent,
            List<String> plannerSteps,
            boolean fallbackUsed,
            List<String> policyIds) {

        this.timestamp = Instant.now();
        this.ticketId = ticketId;
        this.customerId = customerId;
        this.intent = intent;
        this.plannerSteps = plannerSteps;
        this.fallbackUsed = fallbackUsed;
        this.policyIds = policyIds;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public Long getTicketId() {
        return ticketId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getIntent() {
        return intent;
    }

    public List<String> getPlannerSteps() {
        return plannerSteps;
    }

    public boolean isFallbackUsed() {
        return fallbackUsed;
    }

    public List<String> getPolicyIds() {
        return policyIds;
    }
}
