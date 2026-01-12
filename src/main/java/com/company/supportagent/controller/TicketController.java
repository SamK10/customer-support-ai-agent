package com.company.supportagent.controller;

import com.company.supportagent.agent.AgentContext;
import com.company.supportagent.agent.AgentOrchestrator;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    private final AgentOrchestrator orchestrator;

    public TicketController(AgentOrchestrator orchestrator) {
        this.orchestrator = orchestrator;
    }

    @PostMapping("/process")
    public AgentContext processTicket(
            @RequestParam Long ticketId,
            @RequestParam String customerId,
            @RequestBody String message) {

        return orchestrator.handleTicket(ticketId, customerId, message);
    }
}
