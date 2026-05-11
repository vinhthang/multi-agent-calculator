package com.example.calculator.agent;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ArchitectNode {
    private static final Logger log = LoggerFactory.getLogger(ArchitectNode.class);

    private final ChatLanguageModel chatModel;

    public ArchitectNode(@Value("${google.api.key}") String apiKey) {
        this.chatModel = GoogleAiGeminiChatModel.builder()
                .apiKey(apiKey)
                .modelName("gemini-1.5-flash")
                .build();
    }

    public void process(AgenticScope scope) {
        log.info("Architect Node: Generating blueprint for requirement: {}", scope.getRequirement());
        String prompt = "You are a Software Architect. Generate a technical blueprint for a Spring Boot Java 21 Calculator Web App.\n"
                + "Requirement: " + scope.getRequirement() + "\n"
                + "Define the REST endpoints, the BigDecimal math strategy, and the regex sanitization rules. Output plain text.";
        
        try {
            String blueprint = chatModel.generate(prompt);
            scope.setBlueprint(blueprint);
        } catch (Exception e) {
            scope.setBlueprint("Fallback Blueprint: Implement basic calculator with BigDecimal and strict regex validation.");
        }
    }
}
