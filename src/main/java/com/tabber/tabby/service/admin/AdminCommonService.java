package com.tabber.tabby.service.admin;

import com.tabber.tabby.constants.TabbyConstants;
import com.tabber.tabby.entity.UserEntity;
import com.tabber.tabby.exceptions.UnauthorisedException;
import com.tabber.tabby.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CommonService {

    @Autowired
    UserService userService;

    public void verifyAdmin(Long adminUserId) {
        UserEntity userEntity = userService.getUserFromUserId(adminUserId);
        String adminEmail = userEntity.getEmail();
        List<String> approvedAdmins = TabbyConstants.admins;
        if(!approvedAdmins.contains(adminEmail)) {
            throw new UnauthorisedException("Invalid Admin");
        }
    }
}
