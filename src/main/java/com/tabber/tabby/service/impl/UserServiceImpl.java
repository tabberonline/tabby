package com.tabber.tabby.service.impl;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.tabber.tabby.entity.UserEntity;
import com.tabber.tabby.manager.UserResumeManager;
import com.tabber.tabby.respository.UserRepository;
import com.tabber.tabby.service.UniversityService;
import com.tabber.tabby.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserResumeManager userResumeManager;

    @Autowired
    UniversityService universityService;

    private static final Logger logger = Logger.getLogger(UserServiceImpl.class.getName());


    @Override
    public Long save(UserEntity userEntity){
        if(userEntity==null) return null;
        userRepository.saveAndFlush(userEntity);
        return userEntity.getUserId();
    }

    @Override
    public void updateCache(UserEntity userEntity){
        userResumeManager.updateUserResumeCache(userEntity);
    }

    @Override
    public UserEntity getUserFromUserId(Long userId){
        UserEntity userEntity= userResumeManager.findUserById(userId);
        return userEntity;
    }

    @Override
    public UserEntity getUserFromSub(String sub){
        return userRepository.getTopBySub(sub);
    }

    @Override
    public UserEntity setResumePresent(UserEntity userEntity){
        if(userEntity.getPortfolio() == null && userEntity.getContestWidgets().size() == 0
        && userEntity.getRankWidgets().size()==0 && userEntity.getPersonalProjects().size()==0
        && userEntity.getResumePresent()){
            userEntity.setResumePresent(false);
            userRepository.saveAndFlush(userEntity);
            return userEntity;
        }
        else if(!(userEntity.getPortfolio() == null && userEntity.getContestWidgets().size() == 0
                && userEntity.getRankWidgets().size()==0 && userEntity.getPersonalProjects().size()==0)
                && !userEntity.getResumePresent()){
            userEntity.setResumePresent(true);
            userRepository.saveAndFlush(userEntity);
            return userEntity;
        }
        return userEntity;
    }

    @Override
    public void updateUserName(UserEntity userEntity){
        userRepository.saveAndFlush(userEntity);
        updateCache(userEntity);
    }

    public Object getEnrichedUserData(Long userId){
        UserEntity userEntity= userResumeManager.findUserById(userId);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
        Object userEnrichedData;
        try {
            userEnrichedData = objectMapper.readValue(objectMapper.writeValueAsString(userEntity), Object.class);
            LinkedHashMap<String , String> portfolioObject= (LinkedHashMap)((LinkedHashMap) userEnrichedData).get("portfolio");
            if(((LinkedHashMap) userEnrichedData).get("portfolio")!=null) {
                Object collegeIndex = ((LinkedHashMap) ((LinkedHashMap) userEnrichedData).get("portfolio")).get("college");
                String collegeName;
                if ((Integer) collegeIndex == -1) {
                    collegeName = portfolioObject.get("college_others");
                } else {
                    collegeName = universityService.getAllUniversityMap().get(collegeIndex);
                }
                ((LinkedHashMap) ((LinkedHashMap) userEnrichedData).get("portfolio")).put("college", collegeName);
                ((LinkedHashMap) ((LinkedHashMap) userEnrichedData).get("portfolio")).remove("college_others");
            }
            return userEnrichedData;
        }catch(Exception e){
            logger.log(Level.INFO,"Error in college enriching  "+e);
        }
        return null;
    }
}
