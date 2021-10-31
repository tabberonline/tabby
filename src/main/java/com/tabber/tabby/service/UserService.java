package com.tabber.tabby.service;

import com.tabber.tabby.entity.UserEntity;
import liquibase.pro.packaged.L;

public interface UserService {
    Long save(UserEntity userEntity);
    void updateCache(UserEntity userEntity);
    UserEntity getUserFromUserId(Long userId);
    UserEntity getUserFromSub(String sub);
    UserEntity setResumePresent(UserEntity userEntity);
    void updateUserName(Long userId, String userName);
     Object getEnrichedUserData(Long userId);
     void deleteUser(Long userId, Long deleteUserId);
    void updateUserCookiePermission(Long userId);
}
