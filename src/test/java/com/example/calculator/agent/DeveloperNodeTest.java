package com.example.calculator.agent;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

import static org.assertj.core.api.Assertions.assertThat;

class DeveloperNodeTest {

    @Test
    @EnabledOnOs({OS.MAC, OS.LINUX})
    void testDeveloperNodeWithMockModel() {
        // Using /bin/cat as a mock binary to verify the hand-off logic
        LocalCodexModel mockModel = new LocalCodexModel("/bin/cat");
        DeveloperNode node = new DeveloperNode(mockModel);
        
        AgenticScope scope = new AgenticScope();
        scope.setBlueprint("Blueprint: Implement a Square Root operation.");
        
        node.process(scope);
        
        // Verify that the DeveloperNode correctly passed the blueprint to the model
        // and updated the scope with the response (which cat echoed back)
        assertThat(scope.getSourceCode()).contains("Square Root");
    }

    @Test
    void testDeveloperNodeFallbackLogic() {
        // Using an invalid path to trigger the resilience fallback in DeveloperNode
        LocalCodexModel mockModel = new LocalCodexModel("/tmp/invalid_binary_path");
        DeveloperNode node = new DeveloperNode(mockModel);
        
        AgenticScope scope = new AgenticScope();
        scope.setBlueprint("Generic Requirement");
        
        node.process(scope);
        
        // DeveloperNode should detect the model failure and generate the mock CalculatorService code
        assertThat(scope.getSourceCode()).contains("package com.example.calculator.service");
        assertThat(scope.getSourceCode()).contains("public class CalculatorService");
    }
}
