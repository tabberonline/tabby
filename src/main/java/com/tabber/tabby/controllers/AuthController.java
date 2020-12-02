package com.tabber.tabby.controllers;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.tabber.tabby.security.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;

import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class AuthController {

    private static final Logger logger = Logger.getLogger(AuthController.class.getName());

    @Autowired
    JWTService jwtService;

    @PostMapping("login")
    public ResponseEntity<String> login(@RequestParam String idTokenString){
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
                .setAudience(Collections.singletonList("148434873376-a1k8ubdj3g3oqkh53an00v8angbj2itd.apps.googleusercontent.com"))
                .build();
        GoogleIdToken idToken = null;
        try {
            idToken = verifier.verify(idTokenString);
        }
        catch (Exception ex){
            logger.log(Level.WARNING,"Unable to verify user for identity token :{}",idTokenString);
            return new ResponseEntity<>("FORBIDDEN", HttpStatus.FORBIDDEN);
        }
        if (idToken != null) {
            Payload payload = idToken.getPayload();
            logger.log(Level.INFO,"User Info :{}",payload);
            // Print user identifier
            String userId = payload.getSubject();
            System.out.println("User ID: " + userId);

            // Get profile information from payload
            String email = payload.getEmail();
            boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
            String name = (String) payload.get("name");
            String pictureUrl = (String) payload.get("picture");
            String locale = (String) payload.get("locale");
            String familyName = (String) payload.get("family_name");
            String givenName = (String) payload.get("given_name");
            String token = jwtService.getJWTToken("lol");
            return new ResponseEntity<>(token, HttpStatus.OK);
        }
        logger.log(Level.WARNING,"User id token is null");
        return new ResponseEntity<>("FORBIDDEN", HttpStatus.FORBIDDEN);
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
