package com.tabber.tabby.utils;

import com.tabber.tabby.constants.TabbyConstants;
import com.tabber.tabby.manager.RedisServiceManager;
import org.springframework.beans.factory.annotation.Autowired;

public class CacheClearUtil {

    @Autowired
    RedisServiceManager redisServiceManager;

    public void clearFrontendCache(String key,String pageType){
        redisServiceManager.delKey(TabbyConstants.FRONTEND_CONFIG+"::"+pageType+"_"+key);
    }

    public void clearAllFrontendCache(){
        redisServiceManager.delKey(TabbyConstants.FRONTEND_CONFIG+"::all");
    }

}
