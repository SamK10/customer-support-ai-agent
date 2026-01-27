package com.company.supportagent.memory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AgentMemory implements Serializable {

    private String customerId;
    private List<String> previousIntents = new ArrayList<>();
    private int interactionCount;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public List<String> getPreviousIntents() {
        return previousIntents;
    }

    public int getInteractionCount() {
        return interactionCount;
    }

    public void incrementInteraction() {
        this.interactionCount++;
    }
}
