package com.macrometa.loadbalancer.controller;

import com.macrometa.loadbalancer.config.RibbonConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RibbonClient(name = "key-value", configuration = RibbonConfig.class)
public class KeyValueController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping(path = "/set")
    public Object set(@RequestParam(name = "k") String key, @RequestParam(name = "v") String value){
        return restTemplate.getForObject("http://key-value/set?k="+ key+ "&v=" + value, String.class);
    }

}
