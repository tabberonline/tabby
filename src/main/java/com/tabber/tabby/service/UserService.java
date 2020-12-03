package com.tabber.tabby.service;

import com.tabber.tabby.entity.UserEntity;

public interface UserService {
    void save(UserEntity userEntity);
    UserEntity getUserFromUserId(Long userId);
    UserEntity getUserFromEmail(String email);
}
