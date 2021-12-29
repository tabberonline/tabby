package com.tabber.tabby.service.admin;

import com.tabber.tabby.constants.TabbyConstants;
import com.tabber.tabby.entity.UserEntity;
import com.tabber.tabby.exceptions.UnauthorisedException;
import com.tabber.tabby.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AdminCommonService {

    @Autowired
    UserService userService;

    public void verifyAdmin(Long adminUserId) throws Exception {
        UserEntity userEntity = null;
        try {
            userEntity = userService.getUserFromUserId(adminUserId);
        } catch(Exception ex) {
            throw new Exception("Exception Occur while fetching admin entity from admin id in " + AdminCommonService.class);
        }
        String adminEmail = userEntity.getEmail();
        List<String> approvedAdmins = TabbyConstants.admins;
        if(!approvedAdmins.contains(adminEmail)) {
            throw new UnauthorisedException("Invalid Admin");
        }
    }
}
