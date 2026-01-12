package com.company.supportagent.agent;

import com.company.supportagent.intent.Intent;
import com.company.supportagent.intent.IntentClassifier;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class AgentOrchestrator {

    private final IntentClassifier intentClassifier;
    private final AgentPlanner planner;
    private final AgentExecutor executor;

    public AgentOrchestrator(IntentClassifier intentClassifier,
                             AgentPlanner planner,
                             AgentExecutor executor) {
        this.intentClassifier = intentClassifier;
        this.planner = planner;
        this.executor = executor;
    }

    public AgentContext handleTicket(Long ticketId,
                                     String customerId,
                                     String message) {

        AgentContext context = new AgentContext();
        context.setTicketId(ticketId);

        Intent intent = intentClassifier.classify(message);
        context.setIntent(intent);

        List<String> steps = planner.planSteps(intent);
        context.setPlannerSteps(steps);

        return executor.execute(customerId, context);
    }
}
