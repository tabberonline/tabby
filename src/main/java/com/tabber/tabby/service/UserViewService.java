package com.tabber.tabby.service;

import com.tabber.tabby.entity.UserEntity;
import com.tabber.tabby.manager.RedisServiceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class UserViewService {

    @Autowired
    RedisServiceManager redisServiceManager;

    private static final Logger logger = Logger.getLogger(UserViewService.class.getName());

    public Long getRecentViews(Long userId) {
         Double views = redisServiceManager.zscore("viewsSet",userId.toString());
        return views == null? 0:views.longValue();
    }

    public void incrementViewsRedis(UserEntity userEntity) {
        try {
            Long userId = userEntity.getUserId();
            Double currentViews = redisServiceManager.zscore("viewsSet", String.valueOf(userId));
            Long views = currentViews==null ? 0 : currentViews.longValue();
            redisServiceManager.zadd("viewsSet", String.valueOf(userId), views+1);
        } catch(Exception ex) {
            logger.log(Level.INFO,"Error in incrementing views in redis : "+ex);
        }
    }

    public void setTrackingId(UserEntity userEntity, String trackingId) {
        try {
            String trackingIdAndUserIdCombination = trackingId + "_" + userEntity.getUserId();
            Double prevTrackingIdScore = redisServiceManager.zscore("trackingIdSet", trackingIdAndUserIdCombination);
            if(prevTrackingIdScore==null) {
                Long currentTimestamp = (System.currentTimeMillis()/1000) + 3600;
                redisServiceManager.zadd("trackingIdSet", trackingIdAndUserIdCombination, currentTimestamp);
                incrementViewsRedis(userEntity);
            }
        } catch (Exception ex) {
            logger.log(Level.INFO,"Error in setting tracking id in redis : "+ex);
        }
    }

    public void setUntrackedViews(UserEntity userEntity) {
        try {
            Long userId = userEntity.getUserId();
            Double userIdViewsPresent = redisServiceManager.zscore("untrackedViewsSet", String.valueOf(userId));
            if(userIdViewsPresent!=null) {
                redisServiceManager.zadd("untrackedViewsSet", String.valueOf(userId), (long)userIdViewsPresent.doubleValue() + 1);
            }
            else {
                redisServiceManager.zadd("untrackedViewsSet", String.valueOf(userId), (long)1);
            }
        } catch(Exception ex) {
            logger.log(Level.INFO,"Error in setting untracked views in redis : "+ex);
        }
    }
}
