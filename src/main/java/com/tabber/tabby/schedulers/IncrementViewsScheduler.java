package com.tabber.tabby.schedulers;

import com.tabber.tabby.entity.PortfolioEntity;
import com.tabber.tabby.entity.UserEntity;
import com.tabber.tabby.manager.RedisServiceManager;
import com.tabber.tabby.manager.UserResumeManager;
import com.tabber.tabby.respository.PortfolioRepository;
import com.tabber.tabby.respository.UserRepository;
import com.tabber.tabby.service.UserService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Tuple;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class IncrementViewsScheduler implements Job {

    @Autowired
    RedisServiceManager redisServiceManager;

    @Autowired
    UserResumeManager userResumeManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PortfolioRepository portfolioRepository;

    @Autowired
    UserService userService;

    private static final Logger logger = Logger.getLogger(IncrementViewsScheduler.class.getName());

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        incrementViewsScheduler();
    }

    public void incrementViewsScheduler() {
        // for tracked views
        try {
            Set<Tuple> set = redisServiceManager.zrevrangeWithScores("viewsSet", 0, -1);
            String[] keysToRemove = new String[set.size()];
            int index = 0;
            for(Tuple tuple : set) {
                String key = tuple.getElement();
                UserEntity userEntity = userResumeManager.findUserById(Long.valueOf(key));
                PortfolioEntity portfolioEntity = portfolioRepository.getTopByPortfolioUserId(Long.valueOf(key));
                Long currentViews = userEntity.getPortfolio().getViews();
                portfolioEntity.setViews(currentViews==null ? Double.valueOf(tuple.getScore()).intValue() : currentViews + Double.valueOf(tuple.getScore()).intValue());
                portfolioRepository.saveAndFlush(portfolioEntity);
                userEntity.setPortfolio(portfolioEntity);
                userService.updateCache(userEntity);
                keysToRemove[index] = key;
                index++;
            }
            redisServiceManager.zrem("viewsSet", keysToRemove);

        } catch (Exception ex) {
            logger.log(Level.WARNING,"Failed to increment views in DB due to exception:"+ ex.toString());
        }

        // for untracked views
        try {
            Set<Tuple> set = redisServiceManager.zrevrangeWithScores("untrackedViewsSet", 0, -1);
            String[] keysToRemove = new String[set.size()];
            int index = 0;
            for(Tuple tuple : set) {
                String key = tuple.getElement();
                UserEntity userEntity = userResumeManager.findUserById(Long.valueOf(key));
                PortfolioEntity portfolioEntity = portfolioRepository.getTopByPortfolioUserId(Long.valueOf(key));
                Long currentUntrackedViews = userEntity.getPortfolio().getUntrackedViews();
                portfolioEntity.setUntrackedViews(currentUntrackedViews==null ? Double.valueOf(tuple.getScore()).intValue() : currentUntrackedViews + Double.valueOf(tuple.getScore()).intValue());
                portfolioRepository.saveAndFlush(portfolioEntity);
                userEntity.setPortfolio(portfolioEntity);
                userService.updateCache(userEntity);
                keysToRemove[index] = key;
                index++;
            }
            redisServiceManager.zrem("untrackedViewsSet", keysToRemove);
        } catch (Exception ex) {
            logger.log(Level.WARNING,"Failed to increment untracked views in DB due to exception:"+ ex.toString());
        }

    }

}
