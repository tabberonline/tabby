package com.tabber.tabby.service.impl;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.tabber.tabby.constants.TabbyConstants;
import com.tabber.tabby.entity.UserEntity;
import com.tabber.tabby.exceptions.UnauthorisedException;
import com.tabber.tabby.security.JWTService;
import com.tabber.tabby.service.AuthService;
import com.tabber.tabby.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    JWTService jwtService;

    @Autowired
    UserService userService;

    private static final Logger logger = Logger.getLogger(AuthServiceImpl.class.getName());

    public String login(String idTokenString) throws UnauthorisedException{
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
                .setAudience(Collections.singletonList("954820964588-l8ba8pbqa49riqctv9fa6ckrt1dbul77.apps.googleusercontent.com"))
                .build();
        GoogleIdToken idToken;
        try {
            idToken = verifier.verify(idTokenString);
        }
        catch (Exception ex){
            logger.log(Level.WARNING,"Unable to verify user for identity token :{}",idTokenString);
           throw new UnauthorisedException(ex.toString());
        }
        if(idToken == null )
            throw new UnauthorisedException("Id token is null");

        GoogleIdToken.Payload payload = idToken.getPayload();

        if(!Boolean.valueOf(payload.getEmailVerified()))
            throw new UnauthorisedException("Id token is null");

        logger.log(Level.INFO,"User Info :{}",payload);
        UserEntity userEntity = userService.getUserFromSub(payload.getSubject());
        Long userId ;
        Date presentDate = new Date();
        if(userEntity == null) {
            userEntity = new UserEntity()
                    .toBuilder()
                    .sub(payload.getSubject())
                    .email(payload.getEmail())
                    .pictureUrl((String) payload.get("picture"))
                    .locale((String) payload.get("locale"))
                    .name((String) payload.get("name"))
                    .resumePresent(false)
                    .cookieAccepted(false)
                    .lastLoggedIn(presentDate)
                    .planId(TabbyConstants.LITE_PLAN_ID)
                    .userType("user")
                    .build();
        }
        else {
            userEntity.setLastLoggedIn(presentDate);
        }
        userId = userService.save(userEntity);
        String token = jwtService.getJWTToken(userEntity.getSub(),userId,payload.getEmail());
        return token;
    }
}
