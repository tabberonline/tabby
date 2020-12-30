package com.tabber.tabby.service;

import com.tabber.tabby.entity.UserEntity;
import liquibase.pro.packaged.L;

public interface UserService {
    Long save(UserEntity userEntity);
    void updateCache(UserEntity userEntity);
    UserEntity getUserFromUserId(Long userId);
    UserEntity getUserFromSub(String sub);
    UserEntity setResumePresent(UserEntity userEntity);
}
