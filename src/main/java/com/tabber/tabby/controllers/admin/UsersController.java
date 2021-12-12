package com.tabber.tabby.controllers.admin;

import com.tabber.tabby.constants.URIEndpoints;
import com.tabber.tabby.controllers.AuthController;
import com.tabber.tabby.entity.UserEntity;
import com.tabber.tabby.service.admin.UsersServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Slf4j
@RequestMapping(URIEndpoints.ADMIN_USER)
@RestController
public class UsersController {

    @Autowired
    UsersServiceImpl usersService;

    private static final Logger logger = Logger.getLogger(AuthController.class.getName());

    @GetMapping("/get_users_from_limit_and_offset")
    private ResponseEntity<List<UserEntity>> getUsersFromLimitAndOffset(@RequestParam Integer pageSize, @RequestParam Integer pageNo) {
        logger.log(Level.WARNING, "Inside Admin User Controller :: getUsersFromLimitAndOffset :: Page No : " + pageNo + " :: Page Size : " + pageSize);
        return new ResponseEntity<>(this.usersService.getUsersFromLimitAndOffset(pageSize, pageNo), HttpStatus.OK);
    }

    @GetMapping("/get_similar_name_users")
    private ResponseEntity<List<UserEntity>> getSimilarNameUsers(@RequestParam String name, @RequestParam Integer pageSize, @RequestParam Integer pageNo) {
        logger.log(Level.WARNING, "Inside Admin User Controller :: getSimilarNameUsers :: Name : " + name +  " Page No : " + pageNo + " :: Page Size : " + pageSize);
        return new ResponseEntity<>(this.usersService.getSimilarNameUsers(name, pageSize, pageNo), HttpStatus.OK);
    }

    @GetMapping("/get_similar_plan_users")
    private ResponseEntity<List<UserEntity>> getSimilarPlanUsers(@RequestParam Integer planId, @RequestParam Integer pageSize, @RequestParam Integer pageNo) {
        logger.log(Level.WARNING, "Inside Admin User Controller :: getSimilarPlanUsers :: Plan Id : " + planId + " Page No : " + pageNo + " :: Page Size : " + pageSize);
        return new ResponseEntity<>(this.usersService.getSimilarPlanUsers(planId, pageSize, pageNo), HttpStatus.OK);
    }

    @GetMapping("/get_user_from_email")
    private ResponseEntity<List<UserEntity>> getUserFromEmail(@RequestParam String email) {
        logger.log(Level.WARNING, "Inside Admin User Controller :: getUserFromEmail :: Email : " + email);
        return new ResponseEntity<>(this.usersService.getUserFromEmail(email), HttpStatus.OK);
    }

    @PostMapping("/set_views_manually")
    private ResponseEntity<String> setViewsManually(@RequestParam String email, @RequestParam Long userId, @RequestParam Long views) {
        logger.log(Level.WARNING, "Inside Admin User Controller :: setViewsManually :: Email : " + email + " :: Views : " + views);
        return new ResponseEntity<>(this.usersService.setViewsManually(email, userId, views), HttpStatus.OK);
    }

}
