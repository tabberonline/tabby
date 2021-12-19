package com.tabber.tabby.service.admin;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.tabber.tabby.constants.TabbyConstants;
import com.tabber.tabby.controllers.AuthController;
import com.tabber.tabby.entity.UserEntity;
import com.tabber.tabby.exceptions.UnauthorisedException;
import com.tabber.tabby.manager.RedisServiceManager;
import com.tabber.tabby.respository.PortfolioRepository;
import com.tabber.tabby.respository.UserRepository;
import com.tabber.tabby.service.UserService;
import com.tabber.tabby.service.admin.impl.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class UsersServiceImpl implements UsersService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PortfolioRepository portfolioRepository;

    @Autowired
    RedisServiceManager redisServiceManager;

    @Autowired
    UserService userService;


    private static final Logger logger = Logger.getLogger(AuthController.class.getName());

    @Override
    public List<UserEntity> getUsersFromLimitAndOffset(Integer pageSize, Integer pageNo) {
        try {
            List<UserEntity> users = this.userRepository.getUsersFromLimitAndOffset(pageSize, (--pageNo)*pageSize);
            return users;
        }
         catch (Exception ex) {
            logger.log(Level.WARNING, "Exception occur Inside Admin User Service :: getUsersFromLimitAndOffset :: " + ex);
         }
        return null;
    }

    @Override
    public List<UserEntity> getSimilarNameUsers(String name, Integer pageSize, Integer pageNo) {
        try {
            List<UserEntity> users = this.userRepository.getSimilarNameUsers(name, pageSize, (--pageNo)*pageSize);
            return users;
        }
        catch (Exception ex) {
            logger.log(Level.WARNING, "Exception occur Inside Admin User Service :: getSimilarNameUsers :: " + ex);
        }
        return null;
    }

    @Override
    public List<UserEntity> getSimilarPlanUsers(Integer planId, Integer pageSize, Integer pageNo) {
        try {
            List<UserEntity> users = this.userRepository.getSimilarPlanUsers(planId, pageSize, (--pageNo)*pageSize);
            return users;
        }
        catch (Exception ex) {
            logger.log(Level.WARNING, "Exception occur Inside Admin User Service :: getSimilarPlanUsers :: " + ex);
        }
        return null;
    }

    @Override
    public UserEntity getUserFromEmail(String email) {
        try {
            UserEntity users = this.userRepository.getUserFromEmail(email);
            return users;
        }
        catch (Exception ex) {
            logger.log(Level.WARNING, "Exception occur Inside Admin User Service :: getUserFromEmail :: " + ex);
        }
        return null;
    }

    public String setViewsManually(Long adminUserId, Long userId, Long views) {
//        UserEntity adminUserEntity = userService.getUserFromSub(payload.getSubject());
        String adminEmail = adminUserEntity.getEmail();
        List<String> admins = TabbyConstants.admins;
        String response = null;

        if(admins.contains(adminEmail)) {
            UserEntity userEntity = userRepository.getTopByUserId(userId);
            userEntity.getPortfolio().setViews(views);
            redisServiceManager.zadd("viewsSet", String.valueOf(userId), views);
            response = "Updated Views";
        }
        else {
            response = "Cannot Update Views due to unauthorised views";
        }
        return response;
    }

    @Override
    public UserEntity getUserFromId(Long id) {
        try {
            UserEntity users = this.userRepository.getUserFromId(id);
            return users;
        }
        catch (Exception ex) {
            logger.log(Level.WARNING, "Exception occur Inside Admin User Service :: getUserFromId :: " + ex);
        }
        return null;
    }

    @Override
    public Boolean verifyAdmin(Long id) throws Exception {
        UserEntity adminEntity = userService.getUserFromUserId(id);
        List<String> approvedAdmins = TabbyConstants.admins;
        if(approvedAdmins.contains(adminEntity.getEmail())) {
            return Boolean.TRUE;
        }
        throw new Exception("Unauthorized Exception");
    }


}
