package com.tabber.tabby.manager;

import com.tabber.tabby.constants.TabbyConstants;
import com.tabber.tabby.entity.EmailEntity;
import com.tabber.tabby.entity.FrontendConfigurationEntity;
import com.tabber.tabby.respository.EmailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class EmailsManager {

    @Autowired
    EmailsRepository emailsRepository;

    @Autowired
    RedisServiceManager redisServiceManager;

    @Cacheable(value = TabbyConstants.EMAIL_BY,key="#profileId",unless = "#result == null")
    public EmailEntity getEmailByProfileId(Long profileId){
        return emailsRepository.getEmailDataByProfileId(profileId);
    }

    @CacheEvict(value = TabbyConstants.EMAIL_BY,key="#profileId")
    public void evictEmailCacheValue(Long profileId) {}


}
