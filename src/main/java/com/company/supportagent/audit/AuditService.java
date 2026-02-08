package com.company.supportagent.audit;

import org.springframework.stereotype.Service;

@Service
public class AuditService {

    public void record(AuditEvent event) {

        // For now: structured console log
        System.out.println("""
            [AUDIT]
            timestamp=%s
            ticketId=%d
            customerId=%s
            intent=%s
            plannerSteps=%s
            fallbackUsed=%s
            policies=%s
            """
                .formatted(
                        event.getTimestamp(),
                        event.getTicketId(),
                        event.getCustomerId(),
                        event.getIntent(),
                        event.getPlannerSteps(),
                        event.isFallbackUsed(),
                        event.getPolicyIds()
                )
        );
    }
}
