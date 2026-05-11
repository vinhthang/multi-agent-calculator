package com.example.calculator;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CalculatorIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testAddition() throws Exception {
        mockMvc.perform(get("/api/calculator/add?a=10.5&b=20.5"))
                .andExpect(status().isOk())
                .andExpect(content().string("31.0"));
    }

    @Test
    public void testDivisionByZero() throws Exception {
        mockMvc.perform(get("/api/calculator/divide?a=10&b=0"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Division by zero is not allowed."));
    }

    @Test
    public void testInputSanitization() throws Exception {
        mockMvc.perform(get("/api/calculator/add?a=10;drop table&b=20"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid input: Parameters must be valid numbers."));
    }
}
