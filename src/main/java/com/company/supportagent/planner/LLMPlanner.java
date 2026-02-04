package com.company.supportagent.planner;

import com.company.supportagent.intent.Intent;
import com.company.supportagent.memory.AgentMemory;
import com.company.supportagent.rag.KnowledgeDocument;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Component
public class LLMPlanner {

    private final WebClient openAIWebClient;

    public LLMPlanner(WebClient openAIWebClient) {
        this.openAIWebClient = openAIWebClient;
    }

    public PlannerResponse plan(
            Intent intent,
            AgentMemory memory,
            List<KnowledgeDocument> knowledge) {

        String prompt =
                PlannerPromptBuilder.buildPrompt(intent, memory, knowledge);

        Map<String, Object> requestBody = Map.of(
                "model", "gpt-3.5-turbo",
                "temperature", 0,
                "messages", List.of(
                        Map.of("role", "system", "content", "You are a planning engine."),
                        Map.of("role", "user", "content", prompt)
                )
        );

        try {
            Map response =
                    openAIWebClient.post()
                            .uri("/chat/completions")
                            .bodyValue(requestBody)
                            .retrieve()
                            .bodyToMono(Map.class)
                            .block();

            String json =
                    (String) ((Map) ((Map)
                            ((List) response.get("choices"))
                                    .get(0))
                            .get("message"))
                            .get("content");

            return JsonUtil.fromJson(json, PlannerResponse.class);

        } catch (Exception ex) {
            return fallbackPlan();
        }
    }

    private PlannerResponse fallbackPlan() {
        PlannerResponse fallback = new PlannerResponse();
        fallback.setSteps(List.of(PlanStep.ESCALATE));
        fallback.setConfidence(1.0);
        return fallback;
    }
}
