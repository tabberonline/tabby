package com.tabber.tabby.utils;

import com.tabber.tabby.constants.TabbyConstants;
import com.tabber.tabby.manager.RedisServiceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class CacheClearUtil {

    @Autowired
    RedisServiceManager redisServiceManager;
    public void clearFrontendCache(String key,String pageType){
        redisServiceManager.delKey(TabbyConstants.FRONTEND_CONFIG+"::"+pageType+"_"+key);
    }

}
