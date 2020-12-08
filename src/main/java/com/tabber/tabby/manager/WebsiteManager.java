package com.tabber.tabby.manager;

import com.tabber.tabby.constants.TabbyConstants;
import com.tabber.tabby.entity.WebsiteEntity;
import com.tabber.tabby.respository.WebsiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class WebsiteManager {

    @Autowired
    WebsiteRepository websiteRepository;

    @Cacheable(value = TabbyConstants.WEBSITE,key="#websiteId.toString()")
    public WebsiteEntity findWebsiteById(Integer websiteId){
        if(websiteId==null)
            return null;
        return websiteRepository.getTopById(websiteId);
    }
}
