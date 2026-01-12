package com.company.supportagent.agent;

import com.company.supportagent.intent.Intent;

import java.util.ArrayList;
import java.util.List;

public class AgentContext {

    private Long ticketId;
    private Intent intent;
    private List<String> plannerSteps = new ArrayList<>();
    private String finalDecision;
    private boolean escalated;

    public Long getTicketId() { return ticketId; }
    public void setTicketId(Long ticketId) { this.ticketId = ticketId; }

    public Intent getIntent() { return intent; }
    public void setIntent(Intent intent) { this.intent = intent; }

    public List<String> getPlannerSteps() { return plannerSteps; }
    public void setPlannerSteps(List<String> plannerSteps) {
        this.plannerSteps = plannerSteps;
    }

    public String getFinalDecision() { return finalDecision; }
    public void setFinalDecision(String finalDecision) {
        this.finalDecision = finalDecision;
    }

    public boolean isEscalated() { return escalated; }
    public void setEscalated(boolean escalated) {
        this.escalated = escalated;
    }
}
