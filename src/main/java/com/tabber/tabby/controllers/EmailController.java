package com.tabber.tabby.controllers;

import com.tabber.tabby.constants.URIEndpoints;
import com.tabber.tabby.dto.EmailRequest;
import com.tabber.tabby.email.EmailService;
import com.tabber.tabby.service.EmailTabberProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class EmailController {

    @Autowired
    EmailService emailService;

    @Autowired
    EmailTabberProfileService emailTabberProfileService;

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

    @PostMapping(value = URIEndpoints.EMAIL_TO,produces = "application/json")
    public ResponseEntity<String> sendEmailToUser(
            @RequestParam("email_to") String emailTo,@RequestParam("file") MultipartFile file) throws Exception {

        try{
            Long userId= Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
            emailTabberProfileService.sendTabbyProfileInEmail(userId, emailTo, file);
        }
        catch (Exception e){
            throw new Exception("Unable to send email to user "+emailTo+" due to exception "+e.toString());
        }
        return new ResponseEntity<>("Email Sent Successfully to user"+emailTo, HttpStatus.OK);
    }
}
