package com.example.calculator.agent;

import java.util.ArrayList;
import java.util.List;

public class AgenticScope {
    private String requirement;
    private String blueprint;
    private String sourceCode;
    private String testCode;
    private List<String> compilerErrors = new ArrayList<>();
    private List<String> testFailures = new ArrayList<>();
    private int iterationCount = 0;
    private boolean securityAuditPassed = false;
    private boolean testsPassed = false;

    public String getRequirement() { return requirement; }
    public void setRequirement(String requirement) { this.requirement = requirement; }

    public String getBlueprint() { return blueprint; }
    public void setBlueprint(String blueprint) { this.blueprint = blueprint; }

    public String getSourceCode() { return sourceCode; }
    public void setSourceCode(String sourceCode) { this.sourceCode = sourceCode; }

    public String getTestCode() { return testCode; }
    public void setTestCode(String testCode) { this.testCode = testCode; }

    public List<String> getCompilerErrors() { return compilerErrors; }
    public void setCompilerErrors(List<String> compilerErrors) { this.compilerErrors = compilerErrors; }

    public List<String> getTestFailures() { return testFailures; }
    public void setTestFailures(List<String> testFailures) { this.testFailures = testFailures; }

    public int getIterationCount() { return iterationCount; }
    public void incrementIterationCount() { this.iterationCount++; }

    public boolean isSecurityAuditPassed() { return securityAuditPassed; }
    public void setSecurityAuditPassed(boolean securityAuditPassed) { this.securityAuditPassed = securityAuditPassed; }

    public boolean isTestsPassed() { return testsPassed; }
    public void setTestsPassed(boolean testsPassed) { this.testsPassed = testsPassed; }

    public boolean isSuccessful() {
        return securityAuditPassed && testsPassed && compilerErrors.isEmpty() && testFailures.isEmpty();
    }
}
