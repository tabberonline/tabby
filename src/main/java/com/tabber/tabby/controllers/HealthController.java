package com.tabber.tabby.controllers;

import com.tabber.tabby.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @Autowired
    EmailService emailService;
    @GetMapping(value = "ping")
    public ResponseEntity<String> ping(){
        emailService.sendMail("mandeep.sidhu2@gmail.com","test","lo lol lo l");
        return new ResponseEntity<>("Pong", HttpStatus.OK);
    }
}
