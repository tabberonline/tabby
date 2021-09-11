package com.tabber.tabby.manager;

import com.tabber.tabby.entity.UniversityEntity;
import com.tabber.tabby.respository.UniversityRepository;
import com.tabber.tabby.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class UniversityManager {

    @Autowired
    UniversityRepository universityRepository;

    @Autowired
    RedisServiceManager redisServiceManager;

    private static final Logger logger = Logger.getLogger(UniversityManager.class.getName());


    public void setUniversityList(){
        Integer universityCount = universityRepository.getUniversityCount();
        Integer offset = 0, limit = 50;
        Integer keysCount = Integer.valueOf((int) Math.ceil(Double.valueOf(universityCount)/limit));
        for(int i=0;i<keysCount;i++){
            List<UniversityEntity> universityEntities = universityRepository.getUniversities(offset,limit);
            logger.log(Level.INFO,"Getting list with offset:"+ offset +" limit:"+limit+" university list size:"+universityEntities.size());
            String redisKey = "UNIVERSITY_LIST_" + i;
            for(UniversityEntity universityEntity:universityEntities){
                redisServiceManager.rPush(redisKey,universityEntity.getName());
            }
            offset+=limit;
        }
        redisServiceManager.setWithExpiry("UNIVERSITY_LIST_COUNT",String.valueOf(universityCount),24*60*60);
    }
    public void clearUniversityListCache(){
        String uniCount = redisServiceManager.getValueForKey("UNIVERSITY_LIST_COUNT");
        Integer universityCount;
        if(uniCount == null)  {
            return;
        }
        universityCount = Integer.parseInt(uniCount);
        Integer limit = 50;
        Integer keysCount = Integer.valueOf((int) Math.ceil(Double.valueOf(universityCount)/limit));
        for(int i=0;i<keysCount;i++) {
            String redisKey = "UNIVERSITY_LIST_" + i;
            redisServiceManager.delKey(redisKey);
        }
        redisServiceManager.delKey("UNIVERSITY_LIST_COUNT");
    }

    public Map<Integer,String> getUniversityList(){
        String uniCount = redisServiceManager.getValueForKey("UNIVERSITY_LIST_COUNT");
        Integer universityCount;
        Map<Integer,String> map = new HashMap<>();
        if(uniCount == null)  {
            setUniversityList();
            uniCount = redisServiceManager.getValueForKey("UNIVERSITY_LIST_COUNT");
        }
        universityCount = Integer.parseInt(uniCount);
        Integer limit = 50;
        Integer keysCount = Integer.valueOf((int) Math.ceil(Double.valueOf(universityCount)/limit));
        int j =1;
        for(int i=0;i<keysCount;i++){
            String redisKey = "UNIVERSITY_LIST_" + i;
            List<String> uniList = redisServiceManager.lrange(redisKey,0,-1);
            for(String uni:uniList){
                map.put(j++,uni);
            }
        }
        return map;
    }
}
