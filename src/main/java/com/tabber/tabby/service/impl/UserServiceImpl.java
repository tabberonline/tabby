package com.tabber.tabby.service.impl;

import com.tabber.tabby.entity.UserEntity;
import com.tabber.tabby.respository.UserRepository;
import com.tabber.tabby.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Override
    public Long save(UserEntity userEntity){
        if(userEntity==null) return null;
        userRepository.saveAndFlush(userEntity);
        return userEntity.getUserId();
    }

    @Override
    public UserEntity getUserFromUserId(Long userId){
        return userRepository.getTopByUserId(userId);
    }

    @Override
    public UserEntity getUserFromSub(String sub){
        return userRepository.getTopBySub(sub);
    }

    @Override
    public UserEntity getUserFromEmail(String email){
        return userRepository.getTopByEmailId(email);
    }
}
