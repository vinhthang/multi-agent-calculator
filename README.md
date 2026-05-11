# LangChain4j Multi-Agent Graph Calculator

A production-grade Java Calculator Web App demonstrating an autonomous "Self-Correction Loop" using a Multi-Agent Graph Pattern. This project uses LangChain4j to orchestrate specialized agents that collaborate to ensure code quality, security, and functional correctness.

## 🚀 Core Purpose
The project functions as an **autonomous development engine**. You provide a high-level natural language requirement, and the agents collaborate to implement, verify, and audit the code.

## 🛠 Technology Stack
- **Java 21**: Utilizing Records and Virtual Threads.
- **Spring Boot 3.4.x**: Main application framework.
- **LangChain4j**: Agent orchestration.
- **Models**:
  - **Google Gemini (Architect)**: Planning and blueprint generation.
  - **Local Codex Binary (Developer)**: Logic implementation.
- **Maven**: Build and dynamic test execution.

## 🤖 Agent Roles & Graph Nodes
The application is architected as a state machine with a shared `AgenticScope`:

1.  **Architect (Gemini)**: Interprets requirements and generates a technical blueprint (REST endpoints, BigDecimal strategy, sanitization rules).
2.  **Developer (Codex)**: Implements the Java logic, including controllers, services, and DTOs.
3.  **Auditor (Security/QA)**: 
    - Performs static security audits (e.g., ensuring `BigDecimal` usage).
    - Dynamically writes source code to disk and executes `mvn test`.
    - Routes back to the Developer with feedback if failures occur.

## 🔒 Security & Resilience (NFRs)
- **Input Sanitization**: Strict Regex validation in the API layer to prevent malicious inputs.
- **Precision**: Mandatory use of `BigDecimal` for all mathematical operations.
- **Audit Logging**: Comprehensive logging of all operations and agent-to-agent handoffs in a dedicated `AUDIT_LOGGER`.
- **Self-Correction Loop**: The workflow iterates up to 3 times to resolve compiler errors or test failures before termination.

## 🚦 Getting Started

### Prerequisites
- Java 21+
- Maven
- `GOOGLE_API_KEY` (or fallback provided)
- `codex` binary available in your system PATH

### Running the Application
```bash
mvn spring-boot:run
```

### Triggering the Agent Workflow
Send a POST request to the Graph Orchestrator with a new requirement:

```bash
curl -X POST http://localhost:8080/api/graph/execute \
     -H "Content-Type: text/plain" \
     -d "Add a square root operation with input validation"
```

The system will then start the Architect -> Developer -> Auditor cycle to implement the new functionality.

## 🧪 Testing
The project includes an integration test suite verifying the core calculator logic and security constraints:
```bash
mvn test
```
