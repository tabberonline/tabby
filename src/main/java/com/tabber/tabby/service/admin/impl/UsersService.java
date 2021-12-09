package com.tabber.tabby.service.admin.impl;

import com.tabber.tabby.entity.UserEntity;
import java.util.List;

public interface UsersService {
    public List<UserEntity> getUsersForAdmin(Integer pageSize, Integer pageNo);
}
