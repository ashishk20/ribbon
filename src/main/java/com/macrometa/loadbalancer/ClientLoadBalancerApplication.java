package com.macrometa.loadbalancer;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class ClientLoadBalancerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClientLoadBalancerApplication.class, args);
    }

}
