package com.tabber.tabby.controllers;

import com.tabber.tabby.entity.RankWidgetEntity;
import com.tabber.tabby.entity.UserEntity;
import com.tabber.tabby.respository.RankWidgetRepository;
import com.tabber.tabby.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;


@RestController
public class HealthController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RankWidgetRepository rankWidgetRepository;

    @RequestMapping(value = "/ping", method =RequestMethod.GET)
    public ResponseEntity<String> ping(){
//        UserEntity userEntity=userRepository.getTopByUserId(3l);
//        Set<RankWidgetEntity> rankWidgetEntities1 = userEntity.getRankWidgets();
//        RankWidgetEntity rankWidgetEntity = new RankWidgetEntity()
//                .toBuilder()
//                .rank(2)
//                .userId(3l)
//                .websiteId(2)
//                .websiteUsername("fewf")
//                .build();
//        rankWidgetRepository.saveAndFlush(rankWidgetEntity);
//        Set<RankWidgetEntity> rankWidgetEntities2 = userEntity.getRankWidgets();
        return new ResponseEntity<>("Pong", HttpStatus.OK);
    }

}
