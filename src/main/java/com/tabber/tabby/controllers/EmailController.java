package com.tabber.tabby.controllers;

import com.tabber.tabby.constants.URIEndpoints;
import com.tabber.tabby.dto.EmailRequest;
import com.tabber.tabby.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {

    @Autowired
    EmailService emailService;
    @PostMapping(value = URIEndpoints.EMAIL_TABBER,produces = "application/json")
    public ResponseEntity<String> sendEmail(
            @RequestBody @Validated EmailRequest emailRequest) throws Exception {

        try{
            emailService.sendMail(emailRequest.getEmailTo(), emailRequest.getSubject(), emailRequest.getText());
        }
        catch (Exception e){
            throw new Exception("Unable to send email for request "+emailRequest+" due to exception "+e.toString());
        }
        return new ResponseEntity<>("Email Sent Successfully", HttpStatus.OK);
    }
}
