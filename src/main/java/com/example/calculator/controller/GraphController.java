package com.example.calculator.controller;

import com.example.calculator.agent.AgenticScope;
import com.example.calculator.agent.WorkflowOrchestrator;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/graph")
public class GraphController {

    private final WorkflowOrchestrator workflowOrchestrator;

    public GraphController(WorkflowOrchestrator workflowOrchestrator) {
        this.workflowOrchestrator = workflowOrchestrator;
    }

    @PostMapping("/execute")
    public AgenticScope executeGraph(@RequestBody String requirement) {
        // Regex sanitization of requirement input
        String sanitized = requirement.replaceAll("[^a-zA-Z0-9 .,_-]", "");
        return workflowOrchestrator.executeWorkflow(sanitized);
    }
}
