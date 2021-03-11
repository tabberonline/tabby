package com.tabber.tabby.controllers;

import com.tabber.tabby.constants.URIEndpoints;
import com.tabber.tabby.dto.EmailHistoryResponse;
import com.tabber.tabby.dto.EmailRequest;
import com.tabber.tabby.dto.StatusWiseResponse;
import com.tabber.tabby.email.EmailService;
import com.tabber.tabby.enums.ResponseStatus;
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
            emailService.sendMail(emailRequest.getEmailTo(), emailRequest.getSubject(), emailRequest.getText(), emailRequest.getEmail());
        }
        catch (Exception e){
            throw new Exception("Unable to send email for request "+emailRequest+" due to exception "+e.toString());
        }
        return new ResponseEntity<>("Email Sent Successfully", HttpStatus.OK);
    }

    @PostMapping(value = URIEndpoints.EMAIL_TO,produces = "application/json")
    public ResponseEntity<StatusWiseResponse> sendEmailToUser(
            @RequestParam("email_to") String emailTo,@RequestParam(value = "file",required = false) MultipartFile file) throws Exception {
        StatusWiseResponse statusWiseResponse;
        try{
            Long userId= Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
            statusWiseResponse=emailTabberProfileService.sendTabbyProfileInEmail(userId, emailTo, file);
        }
        catch (Exception e){
            statusWiseResponse= new StatusWiseResponse().toBuilder()
                    .status(ResponseStatus.failure.name())
                    .message("Error in sending email")
                    .detailed_message(e.toString())
                    .build();
        }
        return new ResponseEntity<>(statusWiseResponse, HttpStatus.OK);
    }

    @GetMapping(value = URIEndpoints.EMAIL_HISTORY,produces = "application/json")
    public ResponseEntity<EmailHistoryResponse> emailHistory (
            @RequestParam("page_no") Integer pageNo,@RequestParam( "items_per_page") Integer itemsPerPage) throws Exception {

        try{
            Long userId= Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
            EmailHistoryResponse emailHistoryResponse = emailTabberProfileService.getPaginatedEmailHistory(pageNo,itemsPerPage,userId);
            return new ResponseEntity<>(emailHistoryResponse, HttpStatus.OK);
        }
        catch (Exception e){
            throw e;
        } }


}
