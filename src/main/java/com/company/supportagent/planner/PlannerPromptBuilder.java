package com.company.supportagent.planner;

import com.company.supportagent.intent.Intent;
import com.company.supportagent.memory.AgentMemory;

public class PlannerPromptBuilder {

    public static String buildPrompt(Intent intent, AgentMemory memory) {

        return """
        You are an AI planning assistant for a banking customer support system.

        You must ONLY return valid JSON.
        You must ONLY use allowed steps.

        Allowed steps:
        - FETCH_TRANSACTIONS
        - CHECK_DUPLICATE
        - APPLY_POLICY
        - RESPOND_USER
        - ESCALATE

        Customer intent: %s
        Previous intents: %s
        Interaction count: %d

        Output JSON format:
        {
          "steps": ["FETCH_TRANSACTIONS", "CHECK_DUPLICATE"],
          "confidence": 0.85
        }
        """.formatted(
                intent.name(),
                memory.getPreviousIntents(),
                memory.getInteractionCount()
        );
    }
}
