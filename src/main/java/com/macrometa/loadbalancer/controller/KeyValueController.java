package com.macrometa.loadbalancer.controller;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class KeyValueController {

    private AtomicInteger selector = new AtomicInteger(0);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private EurekaClient eurekaClient;

    @GetMapping(path = "/set")
    public ResponseEntity set(@RequestParam(name = "k") String key, @RequestParam(name = "v") String value){
        return getResponse("set?k="+ key+ "&v=" + value);
    }

    @GetMapping(path = "/get")
    public ResponseEntity get(@RequestParam(name = "k") String key){
        return getResponse("get?k="+ key);
    }

    @GetMapping(path = "/is")
    public ResponseEntity is(@RequestParam(name = "k") String key){
        return getResponse("is?k="+ key);
    }

    @GetMapping(path = "/getKeys")
    public ResponseEntity getKeys(@RequestParam(required = false) Integer page, @RequestParam(required = false)  Integer size){
        return getResponse("getKeys" + getPageParams(page, size));
    }

    @GetMapping(path = "/getValues")
    public ResponseEntity getValues(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size){
        return getResponse("getValues" + getPageParams(page, size));
    }

    @GetMapping(path = "/getAll")
    public ResponseEntity getAll(@RequestParam(required = false)Integer page, @RequestParam(required = false) Integer size){
        return getResponse("getAll" + getPageParams(page, size));
    }

    @GetMapping(path = "/rm")
    public ResponseEntity remove(@RequestParam(name = "k") String key){
        return getResponse("rm?k="+ key);
    }

    @GetMapping(path = "/clear")
    public ResponseEntity clear(){
        return getResponse("clear");
    }

    private ResponseEntity getResponse(String apiEndPoint) {

        String instanceUrl = getNextInstanceUrl();
        String url = null;

        if(instanceUrl != null){
            url = instanceUrl + apiEndPoint;
        }
        if (null != url) {
            return restTemplate.getForEntity(url, Object.class);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
        }
    }

    private String getPageParams(Integer page, Integer size){
        String pageParams = "";
        if(null != page && null != size){
            pageParams = "?page=" + page + "&size=" + size;
        }
        return pageParams;
    }

    private String getNextInstanceUrl() {
        Application application = this.eurekaClient.getApplication("key-value");
        List<InstanceInfo> instanceInfoList = application.getInstances();
        InstanceInfo instanceInfo;

        if (null == instanceInfoList || instanceInfoList.isEmpty()) {
            return null;
        } else if (instanceInfoList.size() > selector.get()) {
            instanceInfo = instanceInfoList.get(selector.get());
        } else {
            selector.set(0);
            instanceInfo = instanceInfoList.get(selector.get());
        }
        selector.addAndGet(1);
        return instanceInfo.getHomePageUrl();
    }

}
