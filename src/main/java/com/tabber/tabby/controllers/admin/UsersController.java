package com.tabber.tabby.controllers.admin;

import com.tabber.tabby.constants.URIEndpoints;
import com.tabber.tabby.controllers.AuthController;
import com.tabber.tabby.entity.UserEntity;
import com.tabber.tabby.service.admin.UsersServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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

    @GetMapping("/get_users")
    private ResponseEntity<List<UserEntity>> getUsers(@RequestParam Integer pageSize, @RequestParam Integer pageNo) {
        logger.log(Level.WARNING, "Inside Admin User Controller :: Page No : " + pageNo + " :: Page Size : " + pageSize);
        return new ResponseEntity<>(this.usersService.getUsersForAdmin(pageSize, pageNo), HttpStatus.OK);
    }
}
