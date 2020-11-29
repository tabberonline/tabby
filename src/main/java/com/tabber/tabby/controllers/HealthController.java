package com.tabber.tabby.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HealthController {

    @RequestMapping(value = "/ping", method =RequestMethod.GET)
    public ResponseEntity<String> ping(){
        return new ResponseEntity<>("Pong", HttpStatus.OK);
    }

}
