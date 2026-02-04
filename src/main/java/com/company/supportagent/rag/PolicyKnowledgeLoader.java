package com.company.supportagent.rag;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class PolicyKnowledgeLoader {

    private final KnowledgeStore store;

    public PolicyKnowledgeLoader(KnowledgeStore store) {
        this.store = store;
    }

    @PostConstruct
    public void loadPolicies() {

        store.add(new KnowledgeDocument(
                "POLICY-001",
                "Duplicate Transaction Policy",
                """
                If a card transaction is duplicated and posted more than once,
                the customer is eligible for a refund after verification.
                """
        ));

        store.add(new KnowledgeDocument(
                "POLICY-002",
                "Escalation Policy",
                """
                If transaction verification fails or data is unavailable,
                the case must be escalated to a human agent.
                """
        ));
    }
}
