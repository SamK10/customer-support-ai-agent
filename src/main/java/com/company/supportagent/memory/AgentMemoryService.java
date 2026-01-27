package com.company.supportagent.memory;

public interface AgentMemoryService {

    AgentMemory loadMemory(String customerId);

    void saveMemory(AgentMemory memory);
}
