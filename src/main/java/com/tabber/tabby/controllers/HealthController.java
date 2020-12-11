package com.tabber.tabby.controllers;

import com.tabber.tabby.entity.PortfolioEntity;
import com.tabber.tabby.entity.RankWidgetEntity;
import com.tabber.tabby.entity.UserEntity;
import com.tabber.tabby.entity.WebsiteEntity;
import com.tabber.tabby.manager.RedisServiceManager;
import com.tabber.tabby.manager.WebsiteManager;
import com.tabber.tabby.respository.PortfolioRepository;
import com.tabber.tabby.respository.RankWidgetRepository;
import com.tabber.tabby.respository.UserRepository;
import com.tabber.tabby.respository.WebsiteRepository;
import com.tabber.tabby.service.UserService;
import com.tabber.tabby.service.WebsiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
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
    RankWidgetRepository rankWidgetRepository;
    @Autowired
    WebsiteService websiteService;

    @Autowired
    UserService userService;

    @Autowired
    WebsiteRepository websiteRepository;

    @Autowired
    PortfolioRepository portfolioRepository;

    @Autowired
    UserRepository userRepository;
    @GetMapping(value = "ping")
    public ResponseEntity<String> ping(){
        String sub=SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();
        UserEntity userEntity=userRepository.getTopBySub(sub);
//        UserEntity userEntity=userService.getUserFromEmail("mandeep.sidhu2@gmail.com");
//        List<RankWidgetEntity> rankWidgetEntities = userEntity.getRankWidgets();
//        RankWidgetEntity rankWidgetEntity=new RankWidgetEntity().toBuilder()
//                .rank(2)
//                .websiteId(1)
//                .userId(userEntity.getUserId())
//                .build();
//        rankWidgetRepository.save(rankWidgetEntity);
//        UserEntity userEntity1=userService.getUserFromEmail("mandeep.sidhu2@gmail.com");
////            PortfolioEntity portfolioEntity = new PortfolioEntity().toBuilder().title("we").description("efef").build();
//       PortfolioEntity portfolioEntity=
//               new PortfolioEntity().toBuilder().title("we").
//                       user(userEntity)
//                       .description("efef").build();
//       portfolioRepository.saveAndFlush(portfolioEntity);
//    //    userEntity1.setPortfolio(portfolioEntity);
//       // userService.save(userEntity1);
//         userEntity1=userService.getUserFromEmail("mandeep.sidhu2@gmail.com");
        return new ResponseEntity<>("Pong", HttpStatus.OK);
    }

}
