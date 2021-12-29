package com.tabber.tabby.controllers.admin;

import com.tabber.tabby.constants.URIEndpoints;
import com.tabber.tabby.controllers.AuthController;
import com.tabber.tabby.entity.UserEntity;
import com.tabber.tabby.service.admin.AdminCommonService;
import com.tabber.tabby.service.admin.UsersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Slf4j
@RequestMapping(URIEndpoints.ADMIN_USER)
@RestController
public class UsersController {

    @Autowired
    UsersService usersService;

    @Autowired
    AdminCommonService commonService;

    private static final Logger logger = Logger.getLogger(AuthController.class.getName());

    @GetMapping("/get_users_from_limit_and_offset")
    private ResponseEntity<List<UserEntity>> getUsersFromLimitAndOffset(@RequestParam Integer pageSize, @RequestParam Integer pageNo) throws Exception {
        logger.log(Level.WARNING, "Inside Admin User Controller :: getUsersFromLimitAndOffset :: Page No : " + pageNo + " :: Page Size : " + pageSize);
        try {
            Long adminUserId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
            commonService.verifyAdmin(adminUserId);
            return new ResponseEntity<>(usersService.getUsersFromLimitAndOffset(pageSize, pageNo), HttpStatus.OK);
        }
        catch (Exception ex){
            logger.log(Level.SEVERE,"Cannot fetch users from limit and offset due to exception: {} : " + ex.toString());
            throw ex;
        }
    }

    @GetMapping("/get_user_from_id")
    private ResponseEntity<UserEntity> getUserFromId(@RequestParam Long userId) throws Exception {
        logger.log(Level.WARNING, "Inside Admin User Controller :: getUserFromId :: User Id : " + userId);
        try {
            Long adminUserId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
            commonService.verifyAdmin(adminUserId);
            return new ResponseEntity<>(usersService.getUserFromId(userId), HttpStatus.OK);
        }
        catch (Exception ex){
            logger.log(Level.SEVERE,"Cannot fetch user from user id due to exception: {} : " + ex.toString());
            throw ex;
        }
    }

    @GetMapping("/get_similar_name_users")
    private ResponseEntity<List<UserEntity>> getSimilarNameUsers(@RequestParam String name, @RequestParam Integer pageSize, @RequestParam Integer pageNo) throws Exception {
        logger.log(Level.WARNING, "Inside Admin User Controller :: getSimilarNameUsers :: Name : " + name +  " Page No : " + pageNo + " :: Page Size : " + pageSize);
        try {
            Long adminUserId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
            commonService.verifyAdmin(adminUserId);
            return new ResponseEntity<>(usersService.getSimilarNameUsers(name, pageSize, pageNo), HttpStatus.OK);
        }
        catch (Exception ex){
            logger.log(Level.SEVERE,"Cannot fetch users of similar names due to exception: {} : " + ex.toString());
            throw ex;
        }
    }

    @GetMapping("/get_similar_plan_users")
    private ResponseEntity<List<UserEntity>> getSimilarPlanUsers(@RequestParam Integer planId, @RequestParam Integer pageSize, @RequestParam Integer pageNo) throws Exception {
        logger.log(Level.WARNING, "Inside Admin User Controller :: getSimilarPlanUsers :: Plan Id : " + planId + " Page No : " + pageNo + " :: Page Size : " + pageSize);
        try {
            Long adminUserId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
            commonService.verifyAdmin(adminUserId);
            return new ResponseEntity<>(usersService.getSimilarPlanUsers(planId, pageSize, pageNo), HttpStatus.OK);
        }
        catch (Exception ex){
            logger.log(Level.SEVERE,"Cannot fetch users of similar plans due to exception: {} : " + ex.toString());
            throw ex;
        }
    }

    @GetMapping("/get_user_from_email")
    private ResponseEntity<UserEntity> getUserFromEmail(@RequestParam String email) throws Exception {
        logger.log(Level.WARNING, "Inside Admin User Controller :: getUserFromEmail :: Email : " + email);
        try {
            Long adminUserId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
            commonService.verifyAdmin(adminUserId);
            return new ResponseEntity<>(usersService.getUserFromEmail(email), HttpStatus.OK);
        }
        catch (Exception ex){
            logger.log(Level.SEVERE,"Cannot fetch user from email due to exception: {} : " + ex.toString());
            throw ex;
        }
    }

    @PostMapping("/set_views_manually")
    private ResponseEntity<Map<String, Map<Long, Long>>> setViewsManually(@RequestParam Long userId, @RequestParam Long views) throws Exception {
        logger.log(Level.WARNING, "Inside Admin User Controller :: setViewsManually :: UserId : " + userId + " :: Views : " + views);
        String response = null;
        HashMap<String, Map<Long, Long>> map1 = new HashMap<>();
        HashMap<Long, Long> map2 = new HashMap<>();
        map2.put(userId, views);
        try {
            Long adminUserId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
            commonService.verifyAdmin(adminUserId);
            response = usersService.setViewsManually(userId, views);
            map1.put(response, map2);
        }
        catch (Exception ex){
            logger.log(Level.SEVERE,"Cannot set views manually due to exception: {} : " + ex.toString());
            throw ex;
        }
        return new ResponseEntity<>(map1, HttpStatus.OK);
    }

}
