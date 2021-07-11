package com.tabber.tabby.service.impl;

import com.tabber.tabby.entity.UserEntity;
import com.tabber.tabby.manager.UserResumeManager;
import com.tabber.tabby.respository.UserRepository;
import com.tabber.tabby.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserResumeManager userResumeManager;

    @Override
    public Long save(UserEntity userEntity){
        if(userEntity==null) return null;
        userRepository.saveAndFlush(userEntity);
        return userEntity.getUserId();
    }

    @Override
    public void updateCache(UserEntity userEntity){
        userResumeManager.updateUserResumeCache(userEntity);
    }

    @Override
    public UserEntity getUserFromUserId(Long userId){
        UserEntity userEntity= userResumeManager.findUserById(userId);
        return userEntity;
    }

    @Override
    public UserEntity getUserFromSub(String sub){
        return userRepository.getTopBySub(sub);
    }

    @Override
    public UserEntity setResumePresent(UserEntity userEntity){
        if(userEntity.getPortfolio() == null && userEntity.getContestWidgets().size() == 0
        && userEntity.getRankWidgets().size()==0 && userEntity.getPersonalProjects().size()==0
        && userEntity.getResumePresent()){
            userEntity.setResumePresent(false);
            userRepository.saveAndFlush(userEntity);
            return userEntity;
        }
        else if(!(userEntity.getPortfolio() == null && userEntity.getContestWidgets().size() == 0
                && userEntity.getRankWidgets().size()==0 && userEntity.getPersonalProjects().size()==0)
                && !userEntity.getResumePresent()){
            userEntity.setResumePresent(true);
            userRepository.saveAndFlush(userEntity);
            return userEntity;
        }
        return userEntity;
    }

    @Override
    public void updateUserName(UserEntity userEntity){
        userRepository.saveAndFlush(userEntity);
        updateCache(userEntity);
    }
}
