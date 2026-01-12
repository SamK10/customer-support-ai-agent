package com.company.supportagent.agent;

import com.company.supportagent.intent.Intent;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class AgentPlanner {

    public List<String> planSteps(Intent intent) {

        return switch (intent) {
            case CARD_DISPUTE -> List.of(
                    "FETCH_TRANSACTIONS",
                    "CHECK_DUPLICATE",
                    "APPLY_POLICY",
                    "RESPOND_USER"
            );
            case FAILED_TRANSACTION -> List.of(
                    "FETCH_TRANSACTIONS",
                    "VERIFY_STATUS",
                    "RESPOND_USER"
            );
            default -> List.of("ESCALATE");
        };
    }
}
