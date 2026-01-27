package com.company.supportagent.memory;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Primary   // 👈 used unless explicitly overridden
public class InMemoryMemoryService implements AgentMemoryService {

    private final Map<String, AgentMemory> store = new ConcurrentHashMap<>();

    @Override
    public AgentMemory loadMemory(String customerId) {
        return store.computeIfAbsent(customerId, id -> {
            AgentMemory memory = new AgentMemory();
            memory.setCustomerId(id);
            return memory;
        });
    }

    @Override
    public void saveMemory(AgentMemory memory) {
        store.put(memory.getCustomerId(), memory);
    }
}
