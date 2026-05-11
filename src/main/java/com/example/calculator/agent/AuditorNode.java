package com.example.calculator.agent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class AuditorNode {
    private static final Logger log = LoggerFactory.getLogger(AuditorNode.class);

    public void process(AgenticScope scope) {
        scope.getCompilerErrors().clear();
        scope.getTestFailures().clear();

        String code = scope.getSourceCode();
        if (code == null || code.isEmpty()) {
            scope.getCompilerErrors().add("Source code is empty.");
            scope.setSecurityAuditPassed(false);
            return;
        }

        // Static Security Audit
        if (!code.contains("BigDecimal")) {
            scope.getCompilerErrors().add("Security Audit Failed: Must use BigDecimal.");
            scope.setSecurityAuditPassed(false);
            return;
        } else {
            scope.setSecurityAuditPassed(true);
        }

        // Write the code to a physical file (mock path for demo purposes)
        try {
            Path servicePath = Paths.get("src/main/java/com/example/calculator/service/CalculatorService.java");
            Files.createDirectories(servicePath.getParent());
            Files.writeString(servicePath, code);
            
            // Execute dynamic compilation and tests via Maven
            ProcessBuilder pb = new ProcessBuilder("mvn", "clean", "test");
            pb.redirectErrorStream(true);
            Process process = pb.start();
            
            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }
            int exitCode = process.waitFor();
            
            if (exitCode != 0) {
                scope.setTestsPassed(false);
                scope.getTestFailures().add(output.toString());
                log.warn("Auditor Node: Tests failed. Exit code: {}", exitCode);
            } else {
                scope.setTestsPassed(true);
                log.info("Auditor Node: All tests passed!");
            }
        } catch (Exception e) {
            scope.getCompilerErrors().add("Compilation/Execution Exception: " + e.getMessage());
            scope.setTestsPassed(false);
        }
    }
}
