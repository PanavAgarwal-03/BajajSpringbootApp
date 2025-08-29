package com.healthrx.webhooksqlsolver.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WebhookResponse {
    @JsonProperty("webhook")
    private String webhook;
    
    @JsonProperty("accessToken")
    private String accessToken;
    
    // Default constructor
    public WebhookResponse() {}
    
    // Constructor with parameters
    public WebhookResponse(String webhook, String accessToken) {
        this.webhook = webhook;
        this.accessToken = accessToken;
    }
    
    // Getter methods
    public String getWebhook() {
        return webhook;
    }
    
    public void setWebhook(String webhook) {
        this.webhook = webhook;
    }
    
    public String getAccessToken() {
        return accessToken;
    }
    
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
    
    @Override
    public String toString() {
        return "WebhookResponse{" +
                "webhook='" + webhook + '\'' +
                ", accessToken='" + accessToken + '\'' +
                '}';
    }
}
