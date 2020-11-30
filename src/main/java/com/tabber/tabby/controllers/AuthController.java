package com.tabber.tabby.controllers;

import com.tabber.tabby.security.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {
    @Autowired
    JWTService jwtService;

    @PostMapping("login")
    public ResponseEntity<String> login(String idToken){
        String token = jwtService.getJWTToken("lol");
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @PostMapping("hello")
    public String helloWorld(@RequestParam(value="name", defaultValue="World") String name) {
        return "Hello "+name+"!!";
    }

    @RequestMapping("test")
    public String test() {
        return "Hello ";
    }
}
