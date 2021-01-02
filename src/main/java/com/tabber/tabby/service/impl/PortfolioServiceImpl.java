package com.tabber.tabby.service.impl;

import com.tabber.tabby.dto.PortfolioRequest;
import com.tabber.tabby.entity.PortfolioEntity;
import com.tabber.tabby.entity.UserEntity;
import com.tabber.tabby.exceptions.PortfolioExistsException;
import com.tabber.tabby.exceptions.PortfolioNotExistsException;
import com.tabber.tabby.respository.PortfolioRepository;
import com.tabber.tabby.service.PortfolioService;
import com.tabber.tabby.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PortfolioServiceImpl implements PortfolioService {
    @Autowired
    PortfolioRepository portfolioRepository;

    @Autowired
    UserService userService;

    @Override
    public PortfolioEntity createPortfolio(PortfolioRequest portfolioRequest,Long userId){
        UserEntity user = userService.getUserFromUserId(userId);
        if( user.getPortfolio()!=null){
            throw new PortfolioExistsException("Portfolio exists for user with id: "+userId);
        }
        PortfolioEntity portfolioEntity = new PortfolioEntity().toBuilder()
                .title(portfolioRequest.getTitle())
                .description(portfolioRequest.getDescription())
                .user(user)
                .build();
        portfolioRepository.saveAndFlush(portfolioEntity);
        user.setPortfolio(portfolioEntity);
        user = userService.setResumePresent(user);
        userService.updateCache(user);
        return portfolioEntity;
    }

    @Override
    public PortfolioEntity updatePortfolio(PortfolioRequest portfolioRequest,Long userId){
        UserEntity user = userService.getUserFromUserId(userId);
        if(user.getPortfolio()==null) {
            throw new PortfolioNotExistsException("Portfolio does not exist for user with id: " + userId);
        }
        PortfolioEntity portfolioEntity = user.getPortfolio();
        portfolioEntity = portfolioEntity.toBuilder()
                .title(portfolioRequest.getTitle())
                .description(portfolioRequest.getDescription())
                .build();
        portfolioRepository.saveAndFlush(portfolioEntity);
        user.setPortfolio(portfolioEntity);
        userService.updateCache(user);
        return portfolioEntity;
    }

}
