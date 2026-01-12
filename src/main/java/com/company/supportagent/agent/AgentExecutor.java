package com.company.supportagent.agent;

import com.company.supportagent.domain.Transaction;
import com.company.supportagent.policy.PolicyDecision;
import com.company.supportagent.policy.PolicyEngine;
import com.company.supportagent.service.TransactionService;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class AgentExecutor {

    private final TransactionService transactionService;
    private final PolicyEngine policyEngine;

    public AgentExecutor(TransactionService transactionService,
                         PolicyEngine policyEngine) {
        this.transactionService = transactionService;
        this.policyEngine = policyEngine;
    }

    public AgentContext execute(String customerId, AgentContext context) {

        List<Transaction> txns =
                transactionService.fetchTransactions(customerId);

        boolean duplicate =
                transactionService.hasDuplicateTransaction(txns);

        PolicyDecision decision =
                policyEngine.evaluateDuplicate(duplicate, 5000);

        context.setFinalDecision(decision.name());
        context.setEscalated(decision == PolicyDecision.ESCALATE);

        return context;
    }
}
