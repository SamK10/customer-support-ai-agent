package com.company.supportagent.planner;

import org.springframework.stereotype.Component;

@Component
public class PlanValidator {

    public void validate(PlannerResponse response) {

        if (response.getSteps() == null || response.getSteps().isEmpty()) {
            throw new IllegalStateException("Planner returned empty steps");
        }

        for (PlanStep step : response.getSteps()) {
            if (step == null) {
                throw new IllegalStateException("Invalid plan step");
            }
        }

        if (response.getConfidence() < 0.3) {
            throw new IllegalStateException("Low confidence plan");
        }
    }
}
