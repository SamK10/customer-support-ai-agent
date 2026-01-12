package com.company.supportagent.policy;

import org.springframework.stereotype.Component;

@Component
public class PolicyEngine {

    public PolicyDecision evaluateDuplicate(boolean duplicate,
                                            double amount) {

        if (duplicate && amount <= 10000) {
            return PolicyDecision.REFUND;
        }
        if (duplicate) {
            return PolicyDecision.ESCALATE;
        }
        return PolicyDecision.NO_ACTION;
    }
}
