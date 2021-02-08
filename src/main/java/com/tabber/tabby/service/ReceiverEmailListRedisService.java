package com.tabber.tabby.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tabber.tabby.constants.TabbyConstants;
import com.tabber.tabby.manager.RedisServiceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReceiverEmailListRedisService {

    @Autowired
    RedisServiceManager redisServiceManager;

    @Autowired
    ObjectMapper mapper;

   public Boolean addEmailToRedisCachedList(String email){
        try {
            String countString = redisServiceManager.getValueForKey(TabbyConstants.COUNT_EMAIL_KEY);
            Integer count;
            Long currentListSize;
            if (countString == null) {
                // new list and count to be instantiated
                count = 1;
                redisServiceManager.setWithExpiry(TabbyConstants.COUNT_EMAIL_KEY, count.toString(), 7 * 24 * 60 * 60);
            } else {
                count = Integer.parseInt(countString);
            }

            // add to email list
            String emailKey = TabbyConstants.EMAIL_KEY + "_" + count;
            currentListSize = redisServiceManager.rPush(emailKey, email);

            // prepare count for next entry
            if (currentListSize >= TabbyConstants.MAX_EMAILS_PER_KEY) {
                count = count + 1;
                redisServiceManager.setWithExpiry(TabbyConstants.COUNT_EMAIL_KEY, count.toString(), 7 * 24 * 60 * 60);
            }
        }
        catch (Exception ex){
            return false;
        }
        return true;
    }
}
