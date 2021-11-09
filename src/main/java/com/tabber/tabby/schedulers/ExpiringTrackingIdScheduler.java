package com.tabber.tabby.schedulers;

import com.tabber.tabby.manager.RedisServiceManager;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ExpiringTrackingIdScheduler implements Job {

    @Autowired
    RedisServiceManager redisServiceManager;

    private static final Logger logger = Logger.getLogger(ExpiringTrackingIdScheduler.class.getName());

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        expiringTrackingIdScheduler();
    }

    public void expiringTrackingIdScheduler() {
        try {
            Set<String> setOfKeysToRemove = redisServiceManager
                    .zrangebyscore("trackingIdSet", "0", String.valueOf(System.currentTimeMillis()/1000));
            String[] keysToRemove = setOfKeysToRemove.toArray(new String[0]);
            redisServiceManager.zrem("trackingIdSet", keysToRemove);
        } catch(Exception ex) {
            logger.log(Level.WARNING,"Failed to remove tracking ids due to exception:"+ ex.toString());
        }
    }
}