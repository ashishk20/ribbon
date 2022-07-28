package com.macrometa.loadbalancer.config;

import com.netflix.loadbalancer.BestAvailableRule;
import com.netflix.loadbalancer.IRule;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RibbonConfig {

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public IRule ribbonRule() {
        return new BestAvailableRule();
    }
}
