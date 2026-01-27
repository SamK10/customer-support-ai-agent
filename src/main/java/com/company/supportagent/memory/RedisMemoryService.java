package com.company.supportagent.memory;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisMemoryService implements AgentMemoryService {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisMemoryService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public AgentMemory loadMemory(String customerId) {
        String key = "agent:memory:" + customerId;
        AgentMemory memory =
                (AgentMemory) redisTemplate.opsForValue().get(key);

        if (memory == null) {
            memory = new AgentMemory();
            memory.setCustomerId(customerId);
        }
        return memory;
    }

    @Override
    public void saveMemory(AgentMemory memory) {
        String key = "agent:memory:" + memory.getCustomerId();
        redisTemplate.opsForValue().set(key, memory);
    }
}
