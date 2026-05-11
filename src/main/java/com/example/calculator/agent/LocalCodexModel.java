package com.example.calculator.agent;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.output.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.List;

public class LocalCodexModel implements ChatLanguageModel {
    private static final Logger log = LoggerFactory.getLogger(LocalCodexModel.class);

    private final String binaryPath;

    public LocalCodexModel(String binaryPath) {
        this.binaryPath = binaryPath;
    }

    @Override
    public Response<AiMessage> generate(List<ChatMessage> messages) {
        StringBuilder promptBuilder = new StringBuilder();
        for (ChatMessage message : messages) {
            promptBuilder.append(message.text()).append("\n");
        }
        String prompt = promptBuilder.toString();

        try {
            log.info("LocalCodexModel: Executing binary at {}", binaryPath);
            ProcessBuilder pb = new ProcessBuilder(binaryPath);
            pb.redirectErrorStream(true);
            Process process = pb.start();

            try (OutputStream os = process.getOutputStream()) {
                os.write(prompt.getBytes());
                os.flush();
            }

            StringBuilder outputBuilder = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    outputBuilder.append(line).append("\n");
                }
            }

            process.waitFor();
            
            return Response.from(AiMessage.from(outputBuilder.toString().trim()));
        } catch (Exception e) {
            // For resilience, if the binary fails, we return a fallback response
            return Response.from(AiMessage.from("// Codex evaluation failed: " + e.getMessage()));
        }
    }
}
