package com.company.supportagent.controller;

import com.company.supportagent.agent.AgentContext;
import com.company.supportagent.agent.AgentOrchestrator;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class DashboardController {

    private final AgentOrchestrator orchestrator;

    public DashboardController(AgentOrchestrator orchestrator) {
        this.orchestrator = orchestrator;
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @PostMapping("/process")
    public String processTicket(
            @RequestParam Long ticketId,
            @RequestParam String customerId,
            @RequestParam String message,
            Model model) {

        AgentContext result =
                orchestrator.handleTicket(ticketId, customerId, message);

        model.addAttribute("result", result);

        return "result";
    }
}
