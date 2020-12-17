package com.tabber.tabby.controllers;

import com.tabber.tabby.constants.URIEndpoints;
import com.tabber.tabby.dto.UserBasicRespone;
import com.tabber.tabby.entity.UserEntity;
import com.tabber.tabby.exceptions.RankWidgetExistsException;
import com.tabber.tabby.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping(value = URIEndpoints.USER)
public class UserDetailsController {

    private static final Logger logger = Logger.getLogger(UserDetailsController.class.getName());

    @Autowired
    UserService userService;

    @GetMapping(value = URIEndpoints.RESUME,produces = "application/json")
    public ResponseEntity<UserEntity> getUserResume() throws Exception {
        UserEntity userEntity = null;
        try {
            Long userId= Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
            userEntity = userService.getUserFromUserId(userId);
        }
        catch (Exception ex){
            logger.log(Level.SEVERE,"Cannot get user due to exception: {}",ex.toString());
            throw ex;
        }
        return new ResponseEntity<>(userEntity, HttpStatus.OK);
    }

    @GetMapping(value = URIEndpoints.USER_INFO,produces = "application/json")
    public ResponseEntity<UserBasicRespone> getUserInfo() throws Exception {
        UserBasicRespone userBasicRespone = null;
        UserEntity userEntity;
        try {
            Long userId= Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
            userEntity = userService.getUserFromUserId(userId);
            userBasicRespone=new UserBasicRespone().toBuilder()
                    .email(userEntity.getEmail())
                    .locale(userEntity.getLocale())
                    .pictureUrl(userEntity.getPictureUrl())
                    .name(userEntity.getName())
                    .userId(userEntity.getUserId())
                    .resumePresent(userEntity.getResumePresent())
                    .sub(userEntity.getSub())
                    .build();

        }
        catch (Exception ex){
            logger.log(Level.SEVERE,"Cannot get user due to exception: {}",ex.toString());
            throw ex;
        }
        return new ResponseEntity<>(userBasicRespone, HttpStatus.OK);
    }
}
