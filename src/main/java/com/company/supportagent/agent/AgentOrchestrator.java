package com.company.supportagent.agent;

import com.company.supportagent.intent.Intent;
import com.company.supportagent.intent.IntentClassifier;
import com.company.supportagent.memory.AgentMemory;
import com.company.supportagent.memory.AgentMemoryService;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class AgentOrchestrator {

    private final IntentClassifier intentClassifier;
    private final AgentPlanner planner;
    private final AgentExecutor executor;
    private final AgentMemoryService memoryService;

    public AgentOrchestrator(IntentClassifier intentClassifier,
                             AgentPlanner planner,
                             AgentExecutor executor,
                             AgentMemoryService memoryService) {
        this.intentClassifier = intentClassifier;
        this.planner = planner;
        this.executor = executor;
        this.memoryService = memoryService;
    }

    public AgentContext handleTicket(Long ticketId,
                                     String customerId,
                                     String message) {

        // 1️⃣ Load memory
        AgentMemory memory = memoryService.loadMemory(customerId);

        // 2️⃣ Create context
        AgentContext context = new AgentContext();
        context.setTicketId(ticketId);

        // 3️⃣ Detect intent
        Intent intent = intentClassifier.classify(message);
        context.setIntent(intent);

        // 4️⃣ Update memory
        memory.getPreviousIntents().add(intent.name());
        memory.incrementInteraction();

        // 5️⃣ Plan
        List<String> steps = planner.planSteps(intent);
        context.setPlannerSteps(steps);

        // 6️⃣ Execute
        AgentContext result =
                executor.execute(customerId, context);

        // 7️⃣ Save memory
        memoryService.saveMemory(memory);

        return result;
    }
}
