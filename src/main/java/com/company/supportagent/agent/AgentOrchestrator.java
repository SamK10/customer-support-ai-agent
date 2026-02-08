package com.company.supportagent.agent;

import com.company.supportagent.audit.AuditLogger;
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
    private final AuditLogger auditLogger;

    public AgentOrchestrator(
            IntentClassifier intentClassifier,
            AgentExecutor executor,
            AgentMemoryService memoryService,
            LLMPlanner llmPlanner,
            PlanValidator planValidator,
            KnowledgeRetriever knowledgeRetriever,
            AuditLogger auditLogger) {

        this.intentClassifier = intentClassifier;
        this.executor = executor;
        this.memoryService = memoryService;
        this.llmPlanner = llmPlanner;
        this.planValidator = planValidator;
        this.knowledgeRetriever = knowledgeRetriever;
        this.auditLogger = auditLogger;
    }

    public AgentContext handleTicket(
            Long ticketId,
            String customerId,
            String message) {

        /* =========================
           1️⃣ Load memory (Week-2)
           ========================= */
        AgentMemory memory = memoryService.loadMemory(customerId);

        AgentContext context = new AgentContext();
        context.setTicketId(ticketId);

        /* =========================
           2️⃣ Classify intent (Week-1)
           ========================= */
        Intent intent = intentClassifier.classify(message);

        /* Contextual fallback for vague follow-ups (Week-2) */
        if (intent == Intent.UNKNOWN && !memory.getPreviousIntents().isEmpty()) {
            intent = Intent.valueOf(
                    memory.getPreviousIntents()
                            .get(memory.getPreviousIntents().size() - 1)
            );
        }

        context.setIntent(intent);

        /* =========================
           3️⃣ Update memory (Week-2)
           ========================= */
        memory.getPreviousIntents().add(intent.name());
        memory.incrementInteraction();

        /* =========================
           4️⃣ Retrieve policy knowledge (Week-4 RAG)
           ========================= */
        String query = IntentQueryMapper.toQuery(intent);

        List<KnowledgeDocument> knowledge =
                knowledgeRetriever.retrieve(query);

        /* =========================
           5️⃣ AI planning (Week-3)
           ========================= */
        PlannerResponse plan =
                llmPlanner.plan(intent, memory, knowledge);

        /* =========================
           6️⃣ Validate plan (Safety gate)
           ========================= */
        planValidator.validate(plan);

        context.setPlannerSteps(
                plan.getSteps().stream()
                        .map(Enum::name)
                        .toList()
        );

        /* =========================
           7️⃣ Execute deterministically (Week-1)
           ========================= */
        AgentContext result =
                executor.execute(customerId, context);

        /* =========================
           8️⃣ Persist memory
           ========================= */
        memoryService.saveMemory(memory);

        /* =========================
           9️⃣ Audit decision (Week-5)
           ========================= */
        boolean fallbackUsed =
                plan.getSteps().size() == 1 &&
                        plan.getSteps().get(0).name().equals("ESCALATE");

        List<String> policyIds =
                knowledge.stream()
                        .map(KnowledgeDocument::getId)
                        .toList();

        auditLogger.logDecision(
                ticketId,
                customerId,
                intent.name(),
                context.getPlannerSteps(),
                fallbackUsed,
                policyIds
        );

        return result;
    }
}
