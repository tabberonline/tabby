package com.tabber.tabby.controllers;


import com.tabber.tabby.schedulers.EmailTabberProfileReceiverScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @Value("${test.env}")
    public String username;

    @Autowired
    EmailTabberProfileReceiverScheduler emailTabberProfileReceiverScheduler;
    @GetMapping(value = "ping")
    public ResponseEntity<String> ping(){
        emailTabberProfileReceiverScheduler.aws();
        return new ResponseEntity<>("Pong"+username, HttpStatus.OK);
    }

}
