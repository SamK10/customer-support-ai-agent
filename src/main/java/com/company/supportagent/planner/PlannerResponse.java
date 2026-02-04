package com.company.supportagent.planner;

import java.util.List;

public class PlannerResponse {

    private List<PlanStep> steps;
    private double confidence;

    public List<PlanStep> getSteps() {
        return steps;
    }

    public void setSteps(List<PlanStep> steps) {
        this.steps = steps;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }
}
