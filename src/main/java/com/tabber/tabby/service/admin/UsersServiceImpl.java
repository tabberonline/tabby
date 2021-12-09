package com.tabber.tabby.service.admin;

import com.tabber.tabby.controllers.AuthController;
import com.tabber.tabby.entity.UserEntity;
import com.tabber.tabby.respository.UserRepository;
import com.tabber.tabby.service.admin.impl.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class UsersServiceImpl implements UsersService {

    @Autowired
    UserRepository userRepository;

    private static final Logger logger = Logger.getLogger(AuthController.class.getName());

    @Override
    public List<UserEntity> getUsersForAdmin(Integer pageSize, Integer pageNo) {
        try {
            List<UserEntity> users = this.userRepository.getUsersWithOffset(pageSize, (--pageNo)*pageSize);
            return users;
        }
         catch (Exception ex) {
            logger.log(Level.WARNING, "Inside getUserForAdmin :: " + ex);
         }
        return null;
    }
}
