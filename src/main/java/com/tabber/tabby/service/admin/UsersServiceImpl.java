package com.tabber.tabby.service.admin;

import com.tabber.tabby.constants.TabbyConstants;
import com.tabber.tabby.controllers.AuthController;
import com.tabber.tabby.entity.UserEntity;
import com.tabber.tabby.manager.RedisServiceManager;
import com.tabber.tabby.respository.PortfolioRepository;
import com.tabber.tabby.respository.UserRepository;
import com.tabber.tabby.service.admin.impl.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class UsersServiceImpl implements UsersService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserEntity userEntity;

    @Autowired
    PortfolioRepository portfolioRepository;

    @Autowired
    RedisServiceManager redisServiceManager;

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
    public List<UserEntity> getUserFromEmail(String email) {
        try {
            List<UserEntity> users = this.userRepository.getUserFromEmail(email);
            return users;
        }
        catch (Exception ex) {
            logger.log(Level.WARNING, "Exception occur Inside Admin User Service :: getUserFromEmail :: " + ex);
        }
        return null;
    }

    public String setViewsManually(String email, Long userId, Long views) {
        List<String> admins = TabbyConstants.admins;
        int i;
        for(i=0; i<admins.size(); i++) {
            if(admins.get(i) == email) {
                i--;
                break;
            }
        }
        if(i==admins.size()-1) {
            return "You cannot update views";
        }
        UserEntity userEntity = userRepository.getTopByUserId(userId);
        userEntity.getPortfolio().setViews(views);
//        Double currentViews = redisServiceManager.zscore("viewsSet", String.valueOf(userId));
//        Long addViews = currentViews==null ? 0 : currentViews.longValue();
        redisServiceManager.zadd("viewsSet", String.valueOf(userId), views);
        return "Successfully Updated Views in Cache and Views";
    }
}
