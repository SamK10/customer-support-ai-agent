package com.company.supportagent.planner;

import com.company.supportagent.intent.Intent;
import com.company.supportagent.memory.AgentMemory;
import com.company.supportagent.rag.KnowledgeDocument;

import java.util.List;

public class PlannerPromptBuilder {

    public static String buildPrompt(
            Intent intent,
            AgentMemory memory,
            List<KnowledgeDocument> knowledge) {

        String knowledgeText =
                knowledge.isEmpty()
                        ? "No relevant policy found."
                        : knowledge.stream()
                        .map(d -> d.getTitle() + ": " + d.getContent())
                        .reduce("", (a, b) -> a + "\n" + b);

        return """
        You are an AI planning assistant for a banking system.

        You MUST base your plan ONLY on the provided policies.
        If policy is unclear, you MUST ESCALATE.

        Customer intent: %s
        Previous intents: %s

        Relevant policies:
        %s

        Allowed steps:
        FETCH_TRANSACTIONS
        CHECK_DUPLICATE
        APPLY_POLICY
        RESPOND_USER
        ESCALATE

        Return ONLY valid JSON:
        {
          "steps": ["FETCH_TRANSACTIONS"],
          "confidence": 0.0
        }
        """.formatted(
                intent.name(),
                memory.getPreviousIntents(),
                knowledgeText
        );
    }
}
