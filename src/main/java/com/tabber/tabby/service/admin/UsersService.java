package com.tabber.tabby.service.admin;

import com.tabber.tabby.entity.UserEntity;
import java.util.List;

public interface UsersService {
    public List<UserEntity> getUsersFromLimitAndOffset(Integer pageSize, Integer pageNo);
    public List<UserEntity> getSimilarNameUsers(String name, Integer pageSize, Integer pageNo);
    public List<UserEntity> getSimilarPlanUsers(Integer planId, Integer pageSize, Integer pageNo);
    public UserEntity getUserFromEmail(String email);
    public String setViewsManually(Long userId, Long views);
    public UserEntity getUserFromId(Long id);
}
