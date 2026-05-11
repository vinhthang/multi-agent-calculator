# LangChain4j Multi-Agent Graph Calculator

Generate a production-grade Java Calculator Web App using a Multi-Agent Graph Pattern with Spring Boot, LangChain4j, Gemini, and a local Codex binary.

## User Review Required

> [!IMPORTANT]
> The plan has been updated according to your feedback.
> - **Codex Binary**: We will wrap your local `codex` binary in a custom LangChain4j model so the Developer node can use it.
> - **Dynamic Compilation**: The Auditor node will physically compile the generated code and run tests using Java's Compiler API and JUnit Platform.
> - Please confirm if this updated approach looks good, and I will begin the implementation immediately.

## Proposed Changes

### Project Setup

#### [NEW] multi-agent-calculator/pom.xml
Set up the Maven project with Java 21, Spring Boot Starter Web, Langchain4j (core, gemini/vertex-ai), and JUnit 5. We will use Spring Boot 3.4.x and the latest open-source Langchain4j.

### Multi-Agent Graph Architecture

#### [NEW] multi-agent-calculator/src/main/java/com/example/calculator/agent/AgenticScope.java
A shared state container holding the technical blueprint, source code, test results, security reports, and iteration counts.

#### [NEW] multi-agent-calculator/src/main/java/com/example/calculator/agent/LocalCodexModel.java
A custom LangChain4j `ChatLanguageModel` implementation that interfaces with your local `codex` binary using Java's `ProcessBuilder` by passing the prompt and capturing the output.

#### [NEW] multi-agent-calculator/src/main/java/com/example/calculator/agent/ArchitectNode.java
Gemini-powered node that interprets requirements and generates a technical blueprint. It will use the provided fallback API key (`gen-lang-client-0228536785`) if `GOOGLE_API_KEY` is unavailable.

#### [NEW] multi-agent-calculator/src/main/java/com/example/calculator/agent/DeveloperNode.java
Codex-powered node that implements Java logic (using Java 21 Records, BigDecimal, controllers). It will use the `LocalCodexModel` behind the scenes.

#### [NEW] multi-agent-calculator/src/main/java/com/example/calculator/agent/AuditorNode.java
Security/QA node. It will write the Developer's generated source code and tests to a temporary directory, compile them dynamically using `javax.tools.JavaCompiler`, and run tests via the JUnit Platform Launcher API. If tests fail, it will extract compiler errors or test failures and feed them back to the DeveloperNode.

#### [NEW] multi-agent-calculator/src/main/java/com/example/calculator/agent/WorkflowOrchestrator.java
State machine logic managing transitions, feedback loops from Auditor to Developer, and the max 3 iteration termination condition.

### Application Logic

#### [NEW] multi-agent-calculator/src/main/java/com/example/calculator/CalculatorApplication.java
Spring Boot main class.

#### [NEW] multi-agent-calculator/src/main/java/com/example/calculator/controller/CalculatorController.java
REST endpoints with strict input sanitization to ensure mathematical operation safety.

#### [NEW] multi-agent-calculator/src/main/java/com/example/calculator/service/CalculatorService.java
Mathematical operations with comprehensive audit logging.

## Verification Plan

### Automated Tests
- Run `mvn clean test` to ensure standard JUnit tests pass.
- Write tests that simulate the WorkflowOrchestrator loop to verify the state machine terminates correctly.
- Verify the AuditorNode correctly executes dynamically compiled tests and extracts error logs on failure.

### Manual Verification
- Execute API requests via `curl` to confirm math operations and test security edge cases.
