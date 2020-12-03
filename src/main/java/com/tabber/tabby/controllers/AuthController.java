package com.tabber.tabby.controllers;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.tabber.tabby.entity.UserEntity;
import com.tabber.tabby.exceptions.UnauthorisedException;
import com.tabber.tabby.security.JWTService;
import com.tabber.tabby.service.AuthService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class AuthController {

    private static final Logger logger = Logger.getLogger(AuthController.class.getName());

    @Autowired
    AuthService authService;

    @PostMapping("login")
    public ResponseEntity<JSONObject> login(@RequestParam String idTokenString) throws UnauthorisedException {
        String accessToken = null;
        JSONObject jsonObject = new JSONObject();
        try {
            accessToken = authService.login(idTokenString);
        }
        catch (Exception ex){
            logger.log(Level.WARNING,"Unable to verify user for identity token :{}",idTokenString.concat("for token ").concat(ex.toString()));
            jsonObject.put("response","Forbidden");
            return new ResponseEntity<JSONObject>(jsonObject, HttpStatus.FORBIDDEN);
        }
        jsonObject.put("access_token",accessToken);
        return new ResponseEntity<>(jsonObject, HttpStatus.OK);
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
