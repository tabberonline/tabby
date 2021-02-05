package com.tabber.tabby.controllers;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
public class HealthController {

    @Value("${test.env}")
    public String username;

    @GetMapping(value = "ping")
    public ResponseEntity<String> ping(){
        return new ResponseEntity<>("Pong"+username, HttpStatus.OK);
    }

}
