package com.example.calculator.agent;

import org.springframework.stereotype.Component;

@Component
public class DeveloperNode {

    private final LocalCodexModel codexModel;

    public DeveloperNode() {
        this.codexModel = new LocalCodexModel("codex"); // Assumes codex is in PATH
    }

    public void process(AgenticScope scope) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("You are a Java Developer. Implement the logic for: ").append(scope.getBlueprint()).append("\n");
        
        if (!scope.getCompilerErrors().isEmpty() || !scope.getTestFailures().isEmpty()) {
            prompt.append("Fix the following issues:\n");
            prompt.append("Errors: ").append(scope.getCompilerErrors()).append("\n");
            prompt.append("Test Failures: ").append(scope.getTestFailures()).append("\n");
        }

        String response = codexModel.generate(prompt.toString());
        
        // Mock logic for demo if codex binary isn't available
        if (response.contains("failed") || response.trim().isEmpty()) {
            response = generateMockSourceCode();
        }

        scope.setSourceCode(response);
        // We'll write this source code to a file in the Auditor for actual execution
    }
    
    private String generateMockSourceCode() {
        return "package com.example.calculator.service;\n\n" +
               "import org.springframework.stereotype.Service;\n" +
               "import java.math.BigDecimal;\n\n" +
               "@Service\n" +
               "public class CalculatorService {\n" +
               "    public BigDecimal add(BigDecimal a, BigDecimal b) { return a.add(b); }\n" +
               "    public BigDecimal divide(BigDecimal a, BigDecimal b) {\n" +
               "        if (b.compareTo(BigDecimal.ZERO) == 0) throw new IllegalArgumentException(\"Div by zero\");\n" +
               "        return a.divide(b);\n" +
               "    }\n" +
               "}\n";
    }
}
