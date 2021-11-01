package com.tabber.tabby.manager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tabber.tabby.constants.TabbyConstants;
import com.tabber.tabby.entity.CustomLinkEntity;
import com.tabber.tabby.entity.UserEntity;
import com.tabber.tabby.respository.UserRepository;
import com.tabber.tabby.service.CustomLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserResumeManager {

    @Autowired
    UserRepository userRepository;

    @Autowired
    CustomLinkService customLinkService;

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

    public void deleteUserCache(Long userId){
        String resumeCacheKey = getResumeCacheKey(userId);

        try{
            redisServiceManager.delKey(resumeCacheKey);
        }catch (Exception ex){}
    }

    public Long getCustomLinkUserId(String groupId, Long id) {
        if(groupId == null)
            return id;
        String customLinkCacheKey = TabbyConstants.CUSTOM_LINK + "::" + groupId + "_" + id.toString();
        String cacheUserId = redisServiceManager.getValueForKey(customLinkCacheKey);
        if(cacheUserId == null){
           CustomLinkEntity customLinkEntity = customLinkService.getCustomLinkEntity(groupId,id,"GROUP");
           if(customLinkEntity == null)
               return null;
           redisServiceManager.setWithExpiry(customLinkCacheKey,String.valueOf(customLinkEntity.getUser().getUserId()),3600);
           return customLinkEntity.getUser().getUserId();
        }
        else {
            return Long.parseLong(cacheUserId);
        }
    }

    private String getResumeCacheKey(Long userId){
        return TabbyConstants.RESUME+"::"+userId.toString();
    }
}
