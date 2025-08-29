package com.healthrx.webhooksqlsolver.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.net.InetSocketAddress;
import java.net.Proxy;

@Configuration
public class RestTemplateConfig {

    @Value("${rest.connection.timeout:5000}")
    private int connectionTimeout;

    @Value("${rest.read.timeout:5000}")
    private int readTimeout;

    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        
        // Configure timeouts
        requestFactory.setConnectTimeout(connectionTimeout);
        requestFactory.setReadTimeout(readTimeout);
        
        // Uncomment and configure proxy if needed
        // requestFactory.setProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxy-host", 8080)));
        
        return new RestTemplate(requestFactory);
    }
}
