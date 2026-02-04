package com.company.supportagent.agent;

import com.company.supportagent.intent.Intent;
import com.company.supportagent.intent.IntentClassifier;
import com.company.supportagent.memory.AgentMemory;
import com.company.supportagent.memory.AgentMemoryService;
import com.company.supportagent.planner.LLMPlanner;
import com.company.supportagent.planner.PlanValidator;
import com.company.supportagent.planner.PlannerResponse;
import com.company.supportagent.rag.IntentQueryMapper;
import com.company.supportagent.rag.KnowledgeDocument;
import com.company.supportagent.rag.KnowledgeRetriever;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AgentOrchestrator {

    private final IntentClassifier intentClassifier;
    private final AgentExecutor executor;
    private final AgentMemoryService memoryService;
    private final LLMPlanner llmPlanner;
    private final PlanValidator planValidator;
    private final KnowledgeRetriever knowledgeRetriever;

    public AgentOrchestrator(
            IntentClassifier intentClassifier,
            AgentExecutor executor,
            AgentMemoryService memoryService,
            LLMPlanner llmPlanner,
            PlanValidator planValidator,
            KnowledgeRetriever knowledgeRetriever) {

        this.intentClassifier = intentClassifier;
        this.executor = executor;
        this.memoryService = memoryService;
        this.llmPlanner = llmPlanner;
        this.planValidator = planValidator;
        this.knowledgeRetriever = knowledgeRetriever;
    }

    public AgentContext handleTicket(
            Long ticketId,
            String customerId,
            String message) {

        AgentMemory memory = memoryService.loadMemory(customerId);

        AgentContext context = new AgentContext();
        context.setTicketId(ticketId);

        Intent intent = intentClassifier.classify(message);

        if (intent == Intent.UNKNOWN && !memory.getPreviousIntents().isEmpty()) {
            intent = Intent.valueOf(
                    memory.getPreviousIntents()
                            .get(memory.getPreviousIntents().size() - 1));
        }

        context.setIntent(intent);

        memory.getPreviousIntents().add(intent.name());
        memory.incrementInteraction();

        // 🔹 WEEK-4: Retrieve knowledge
        String query = IntentQueryMapper.toQuery(intent);

        List<KnowledgeDocument> knowledge =
                knowledgeRetriever.retrieve(query);

        System.out.println(
                "Retrieved policies: " +
                        knowledge.stream().map(KnowledgeDocument::getId).toList()
        );

        PlannerResponse plan =
                llmPlanner.plan(intent, memory, knowledge);

        planValidator.validate(plan);

        context.setPlannerSteps(
                plan.getSteps().stream()
                        .map(Enum::name)
                        .toList()
        );

        AgentContext result =
                executor.execute(customerId, context);

        memoryService.saveMemory(memory);

        return result;
    }
}
