package com.tabber.tabby.manager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tabber.tabby.constants.TabbyConstants;
import com.tabber.tabby.entity.UserEntity;
import com.tabber.tabby.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserResumeManager {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RedisServiceManager redisServiceManager;

    public UserEntity findUserById(Long userId){
        String resumeCacheKey = getResumeCacheKey(userId);
        ObjectMapper objectMapper = new ObjectMapper();

        UserEntity userEntity = null;
        try{
            userEntity=objectMapper.readValue(redisServiceManager.getValueForKey(resumeCacheKey),UserEntity.class) ;
        }catch (Exception ex){}


        if(userEntity == null){
            String userResumeString;
            userEntity = userRepository.getTopByUserId(userId);
            try{
                userResumeString=objectMapper.writeValueAsString(userEntity);
                redisServiceManager.setWithExpiry(resumeCacheKey,userResumeString,3600);
            }catch (Exception ex){}
        }

       return userEntity;
    }

    public void updateUserResumeCache(UserEntity userEntity){
        String resumeCacheKey = getResumeCacheKey(userEntity.getUserId());
        ObjectMapper objectMapper = new ObjectMapper();
        String userResumeString;
        try{
            userResumeString=objectMapper.writeValueAsString(userEntity);
            redisServiceManager.setWithExpiry(resumeCacheKey,userResumeString,3600);
        }catch (Exception ex){}
    }

    private String getResumeCacheKey(Long userId){
        return TabbyConstants.RESUME+"::"+userId.toString();
    }
}
