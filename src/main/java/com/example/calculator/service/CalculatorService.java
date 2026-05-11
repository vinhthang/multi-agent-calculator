package com.example.calculator.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class CalculatorService {

    private static final Logger auditLogger = LoggerFactory.getLogger("AUDIT_LOGGER");

    public BigDecimal add(BigDecimal a, BigDecimal b) {
        BigDecimal result = a.add(b);
        auditLogger.info("Operation: ADD | Operands: {}, {} | Result: {}", a, b, result);
        return result;
    }

    public BigDecimal subtract(BigDecimal a, BigDecimal b) {
        BigDecimal result = a.subtract(b);
        auditLogger.info("Operation: SUBTRACT | Operands: {}, {} | Result: {}", a, b, result);
        return result;
    }

    public BigDecimal multiply(BigDecimal a, BigDecimal b) {
        BigDecimal result = a.multiply(b);
        auditLogger.info("Operation: MULTIPLY | Operands: {}, {} | Result: {}", a, b, result);
        return result;
    }

    public BigDecimal divide(BigDecimal a, BigDecimal b) {
        if (b.compareTo(BigDecimal.ZERO) == 0) {
            auditLogger.warn("Operation: DIVIDE | Operands: {}, {} | Result: ERROR (Division by Zero)", a, b);
            throw new IllegalArgumentException("Division by zero is not allowed.");
        }
        BigDecimal result = a.divide(b, 10, RoundingMode.HALF_UP).stripTrailingZeros();
        auditLogger.info("Operation: DIVIDE | Operands: {}, {} | Result: {}", a, b, result);
        return result;
    }
}
