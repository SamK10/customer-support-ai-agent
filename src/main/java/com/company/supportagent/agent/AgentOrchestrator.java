package com.company.supportagent.agent;

import com.company.supportagent.intent.Intent;
import com.company.supportagent.intent.IntentClassifier;
import com.company.supportagent.memory.AgentMemory;
import com.company.supportagent.memory.AgentMemoryService;
import com.company.supportagent.planner.LLMPlanner;
import com.company.supportagent.planner.PlanValidator;
import com.company.supportagent.planner.PlannerResponse;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AgentOrchestrator {

    private final IntentClassifier intentClassifier;
    private final AgentExecutor executor;
    private final AgentMemoryService memoryService;
    private final LLMPlanner llmPlanner;
    private final PlanValidator planValidator;

    public AgentOrchestrator(IntentClassifier intentClassifier,
                             AgentExecutor executor,
                             AgentMemoryService memoryService,
                             LLMPlanner llmPlanner,
                             PlanValidator planValidator) {
        this.intentClassifier = intentClassifier;
        this.executor = executor;
        this.memoryService = memoryService;
        this.llmPlanner = llmPlanner;
        this.planValidator = planValidator;
    }

    public AgentContext handleTicket(Long ticketId,
                                     String customerId,
                                     String message) {

        /* 1️⃣ Load memory (Week-2) */
        AgentMemory memory = memoryService.loadMemory(customerId);

        /* 2️⃣ Create request context */
        AgentContext context = new AgentContext();
        context.setTicketId(ticketId);

        /* 3️⃣ Classify intent (Week-1) */
        Intent intent = intentClassifier.classify(message);

        /* 4️⃣ Context-aware fallback for vague follow-ups (Week-2) */
        if (intent == Intent.UNKNOWN && !memory.getPreviousIntents().isEmpty()) {
            String lastIntent =
                    memory.getPreviousIntents()
                            .get(memory.getPreviousIntents().size() - 1);
            intent = Intent.valueOf(lastIntent);
        }

        context.setIntent(intent);

        /* 5️⃣ Update memory (Week-2) */
        memory.getPreviousIntents().add(intent.name());
        memory.incrementInteraction();

        /* 6️⃣ LLM-based planning (Week-3) */
        PlannerResponse plan = llmPlanner.plan(intent, memory);

        /* 7️⃣ Validate AI output (MANDATORY SAFETY GATE) */
        planValidator.validate(plan);

        /* 8️⃣ Convert validated plan into executable steps */
        List<String> steps =
                plan.getSteps()
                        .stream()
                        .map(Enum::name)
                        .toList();

        context.setPlannerSteps(steps);

        /* 9️⃣ Execute deterministically (NO AI AUTHORITY) */
        AgentContext resultContext =
                executor.execute(customerId, context);

        /* 🔟 Persist updated memory */
        memoryService.saveMemory(memory);

        return resultContext;
    }
}
