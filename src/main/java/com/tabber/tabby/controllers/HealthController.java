package com.tabber.tabby.controllers;

import com.tabber.tabby.entity.RankWidgetEntity;
import com.tabber.tabby.entity.UserEntity;
import com.tabber.tabby.entity.WebsiteEntity;
import com.tabber.tabby.manager.RedisServiceManager;
import com.tabber.tabby.manager.WebsiteManager;
import com.tabber.tabby.respository.RankWidgetRepository;
import com.tabber.tabby.respository.UserRepository;
import com.tabber.tabby.respository.WebsiteRepository;
import com.tabber.tabby.service.WebsiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;


@RestController
public class HealthController {

//    @Autowired
//    UserRepository userRepository;
//
//    @Autowired
//    RankWidgetRepository rankWidgetRepository;

    @Autowired
    RedisServiceManager redisServiceManager;
    @Autowired
    WebsiteService websiteService;

    @Autowired
    WebsiteManager websiteManager;

    @Autowired
    WebsiteRepository websiteRepository;
    @RequestMapping(value = "/ping", method =RequestMethod.GET)
    public ResponseEntity<String> ping(){
        redisServiceManager.setWithExpiry("q1","q2",600);
        String a=redisServiceManager.getValueForKey("q1");
//        UserEntity userEntity=userRepository.getTopByUserId(1l);
//        List<RankWidgetEntity> rankWidgetEntities1 = userEntity.getRankWidgets();
//        RankWidgetEntity rankWidgetEntity = new RankWidgetEntity()
//                .toBuilder()
//                .rank(2)
//                .userId(1l)
//                .websiteId(2)
//                .websiteUsername("fewf")
//                .build();
//        rankWidgetRepository.saveAndFlush(rankWidgetEntity);
//        userRepository.saveAndFlush(userEntity);
//        userEntity=userRepository.getTopByUserId(1l);
        WebsiteEntity websiteEntity = websiteService.getWebsiteById(1);
        WebsiteEntity websiteEntity1=websiteManager.findWebsiteById(1);
        return new ResponseEntity<>("Pong", HttpStatus.OK);
    }

}
