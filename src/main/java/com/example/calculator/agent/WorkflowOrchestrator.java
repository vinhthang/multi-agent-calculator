package com.example.calculator.agent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class WorkflowOrchestrator {
    private static final Logger log = LoggerFactory.getLogger(WorkflowOrchestrator.class);

    private final ArchitectNode architectNode;
    private final DeveloperNode developerNode;
    private final AuditorNode auditorNode;

    public WorkflowOrchestrator(ArchitectNode architectNode, DeveloperNode developerNode, AuditorNode auditorNode) {
        this.architectNode = architectNode;
        this.developerNode = developerNode;
        this.auditorNode = auditorNode;
    }

    public AgenticScope executeWorkflow(String requirement) {
        AgenticScope scope = new AgenticScope();
        scope.setRequirement(requirement);

        log.info("Starting Workflow...");
        architectNode.process(scope);

        while (scope.getIterationCount() < 3) {
            scope.incrementIterationCount();
            log.info("Executing Developer Node (Iteration {})...", scope.getIterationCount());
            developerNode.process(scope);

            log.info("Executing Auditor Node...");
            auditorNode.process(scope);

            if (scope.isSuccessful()) {
                log.info("Workflow completed successfully!");
                break;
            }
        }

        return scope;
    }
}
