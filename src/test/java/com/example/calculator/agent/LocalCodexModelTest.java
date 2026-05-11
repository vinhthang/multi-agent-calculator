package com.example.calculator.agent;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.output.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class LocalCodexModelTest {

    @Test
    @EnabledOnOs({OS.MAC, OS.LINUX})
    void testGenerateUsingCatBinary() {
        // We use /bin/cat as a mock binary because it echoes back everything from stdin to stdout
        LocalCodexModel model = new LocalCodexModel("/bin/cat");
        
        UserMessage message = UserMessage.from("Test Prompt Content");
        Response<AiMessage> response = model.generate(Collections.singletonList(message));
        
        // Assert that the model correctly captured the output from the process
        assertThat(response.content().text()).contains("Test Prompt Content");
    }

    @Test
    void testFailureHandling() {
        // Pointing to a non-existent binary to trigger the catch block
        LocalCodexModel model = new LocalCodexModel("/tmp/non_existent_binary_12345");
        
        UserMessage message = UserMessage.from("Should Fail");
        Response<AiMessage> response = model.generate(Collections.singletonList(message));
        
        // Assert that the model returns a resilient fallback message instead of crashing
        assertThat(response.content().text()).contains("Codex evaluation failed");
    }
}
