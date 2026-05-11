package com.example.calculator.controller;

import com.example.calculator.service.CalculatorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/calculator")
public class CalculatorController {

    private final CalculatorService calculatorService;
    private static final Pattern NUMBER_PATTERN = Pattern.compile("^-?\\d+(\\.\\d+)?$");

    public CalculatorController(CalculatorService calculatorService) {
        this.calculatorService = calculatorService;
    }

    @GetMapping("/add")
    public ResponseEntity<?> add(@RequestParam String a, @RequestParam String b) {
        return processOperation(a, b, calculatorService::add);
    }

    @GetMapping("/subtract")
    public ResponseEntity<?> subtract(@RequestParam String a, @RequestParam String b) {
        return processOperation(a, b, calculatorService::subtract);
    }

    @GetMapping("/multiply")
    public ResponseEntity<?> multiply(@RequestParam String a, @RequestParam String b) {
        return processOperation(a, b, calculatorService::multiply);
    }

    @GetMapping("/divide")
    public ResponseEntity<?> divide(@RequestParam String a, @RequestParam String b) {
        return processOperation(a, b, calculatorService::divide);
    }

    private ResponseEntity<?> processOperation(String a, String b, MathOperation operation) {
        if (!NUMBER_PATTERN.matcher(a).matches() || !NUMBER_PATTERN.matcher(b).matches()) {
            return ResponseEntity.badRequest().body("Invalid input: Parameters must be valid numbers.");
        }
        try {
            BigDecimal valA = new BigDecimal(a);
            BigDecimal valB = new BigDecimal(b);
            BigDecimal result = operation.apply(valA, valB);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @FunctionalInterface
    interface MathOperation {
        BigDecimal apply(BigDecimal a, BigDecimal b);
    }
}
