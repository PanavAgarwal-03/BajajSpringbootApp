package com.healthrx.webhooksqlsolver.service;

import com.healthrx.webhooksqlsolver.model.WebhookResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class WebhookService {
    
    private static final Logger log = LoggerFactory.getLogger(WebhookService.class);

    private final RestTemplate restTemplate;
    private final SqlSolutionService sqlSolutionService;
    private final String name;
    private final String regNo;
    private final String email;
    private final String generateWebhookUrl;
    private final String submitSolutionUrl;
    private final int connectionTimeout;
    private final int readTimeout;

    public WebhookService(
            RestTemplate restTemplate,
            SqlSolutionService sqlSolutionService,
            @Value("${student.name}") String name,
            @Value("${student.regNo}") String regNo,
            @Value("${student.email}") String email,
            @Value("${webhook.generate.url}") String generateWebhookUrl,
            @Value("${webhook.submit.url}") String submitSolutionUrl,
            @Value("${rest.connection.timeout:5000}") int connectionTimeout,
            @Value("${rest.read.timeout:5000}") int readTimeout) {

        this.restTemplate = restTemplate;
        this.sqlSolutionService = sqlSolutionService;
        this.name = name.trim();
        this.regNo = regNo.trim();
        this.email = email.trim();
        this.generateWebhookUrl = generateWebhookUrl;
        this.submitSolutionUrl = submitSolutionUrl;
        this.connectionTimeout = connectionTimeout;
        this.readTimeout = readTimeout;
    }

    public void executeProcess() {
        log.info("Starting webhook execution process...");
        try {
            // Step 1: Generate webhook
            log.info("Generating webhook...");
            WebhookResponse webhookResponse = generateWebhook();
            
            if (webhookResponse == null || webhookResponse.getWebhook() == null || webhookResponse.getAccessToken() == null) {
                throw new RuntimeException("Failed to generate webhook or invalid response received");
            }
            
            log.info("Webhook generated successfully");
            
            // Step 2: Determine question based on registration number
            boolean isEven = isRegistrationNumberEven();
            String questionUrl = isEven ? 
                "https://drive.google.com/file/d/143MR5cLFrlNEuHzzWJ5RHnEWuijuM9X/view" :
                "https://drive.google.com/file/d/1IeSI6l6KoSQAFFRihIT9tEDICtoz-G/view";
            
            log.info("Solving problem from URL: {}", questionUrl);
            
            // Step 3: Solve the problem
            String solution = solveProblem(questionUrl);
            
            // Step 4: Submit solution
            log.info("Submitting solution...");
            submitSolution(webhookResponse.getWebhook(), webhookResponse.getAccessToken(), solution);
            
            log.info("Process completed successfully!");
            
        } catch (Exception e) {
            log.error("Error in webhook process", e);
            throw new RuntimeException("Failed to complete webhook process", e);
        }
    }

    private boolean isRegistrationNumberEven() {
        try {
            String lastTwoDigits = regNo.replaceAll("[^0-9]", "");
            if (lastTwoDigits.length() < 2) {
                throw new IllegalArgumentException("Registration number must contain at least 2 digits");
            }
            int lastDigit = Character.getNumericValue(lastTwoDigits.charAt(lastTwoDigits.length() - 1));
            return lastDigit % 2 == 0;
        } catch (Exception e) {
            log.error("Error determining if registration number is even/odd. Defaulting to odd.", e);
            return false; // Default to odd if there's an error
        }
    }

    private WebhookResponse generateWebhook() {
        log.debug("Sending request to generate webhook");
        
        Map<String, String> request = new HashMap<>();
        request.put("name", name);
        request.put("regNo", regNo);
        request.put("email", email);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(request, headers);
        
        try {
            return restTemplate.postForObject(generateWebhookUrl, entity, WebhookResponse.class);
        } catch (Exception e) {
            log.error("Error generating webhook", e);
            throw new RuntimeException("Failed to generate webhook", e);
        }
    }

    private String solveProblem(String questionUrl) {
        log.info("Determining problem based on registration number...");
        
        // Check if the last digit of registration number is even or odd
        boolean isEven = isRegistrationNumberEven();
        
        if (isEven) {
            log.info("Solving Question 2 (Even registration number)");
            return sqlSolutionService.solveQuestion2();
        } else {
            log.info("Solving Question 1 (Odd registration number)");
            return sqlSolutionService.solveQuestion1();
        }
    }

    private void submitSolution(String webhookUrl, String accessToken, String solution) {
        log.debug("Submitting solution to webhook: {}", webhookUrl);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        Map<String, String> request = new HashMap<>();
        request.put("finalQuery", solution);
        
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(request, headers);
        
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                webhookUrl,
                HttpMethod.POST,
                entity,
                String.class
            );
            
            if (response.getStatusCode() == HttpStatus.OK) {
                log.info("Solution submitted successfully. Response: {}", response.getBody());
            } else {
                log.warn("Unexpected response status: {} - {}", response.getStatusCodeValue(), response.getBody());
            }
        } catch (Exception e) {
            log.error("Error submitting solution", e);
            throw new RuntimeException("Failed to submit solution", e);
        }
    }
}
