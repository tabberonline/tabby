package com.tabber.tabby.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.tabber.tabby.dto.PortfolioRequest;
import com.tabber.tabby.dto.SocialWebsiteDto;
import com.tabber.tabby.entity.PortfolioEntity;
import com.tabber.tabby.entity.UserEntity;
import com.tabber.tabby.enums.WebsiteType;
import com.tabber.tabby.exceptions.PortfolioExistsException;
import com.tabber.tabby.exceptions.PortfolioNotExistsException;
import com.tabber.tabby.respository.PortfolioRepository;
import com.tabber.tabby.service.PortfolioService;
import com.tabber.tabby.service.UserService;
import com.tabber.tabby.service.WebsiteService;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PortfolioServiceImpl implements PortfolioService {
    @Autowired
    PortfolioRepository portfolioRepository;

    @Autowired
    UserService userService;

    @Autowired
    WebsiteService websiteService;

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

    public ArrayList<SocialWebsiteDto> addSocialWeblink(String websiteName, String link, Long userId) throws Exception {
        UserEntity user = userService.getUserFromUserId(userId);
        if(user.getPortfolio()==null) {
            throw new PortfolioNotExistsException("Portfolio does not exist for user with id: " + userId);
        }
        if(websiteService.getWebsiteByNameAndType(websiteName, WebsiteType.SOCIAL.name()) == null){
            throw new Exception("Requested website doesn't exist");
        }
        PortfolioEntity portfolioEntity = user.getPortfolio();
        SocialWebsiteDto socialWebsiteDto = new SocialWebsiteDto().toBuilder()
                .website_name(websiteName)
                .link(link)
                .build();
        ArrayList<SocialWebsiteDto> socialWebsiteDtoArrayList = portfolioEntity.getSocialProfiles();
        if(socialWebsiteDtoArrayList == null || socialWebsiteDtoArrayList.size()==0){
            socialWebsiteDtoArrayList = new ArrayList<>();
        }
        if(checkIfWebsiteAlreadyPresent(socialWebsiteDtoArrayList,websiteName)){
            throw new Exception("this profile already exists");
        }
        socialWebsiteDtoArrayList.add(socialWebsiteDto);
        portfolioEntity.setSocialProfiles(socialWebsiteDtoArrayList);
        portfolioRepository.saveAndFlush(portfolioEntity);
        user.setPortfolio(portfolioEntity);
        userService.updateCache(user);
        return socialWebsiteDtoArrayList;
    }

    public ArrayList<SocialWebsiteDto> updateSocialWeblink(String websiteName, String link, Long userId) throws Exception {
        UserEntity user = userService.getUserFromUserId(userId);
        if(user.getPortfolio()==null) {
            throw new PortfolioNotExistsException("Portfolio does not exist for user with id: " + userId);
        }
        if(websiteService.getWebsiteByNameAndType(websiteName, WebsiteType.SOCIAL.name()) == null){
            throw new Exception("Requested website doesn't exist");
        }
        PortfolioEntity portfolioEntity = user.getPortfolio();
        SocialWebsiteDto socialWebsiteDto = new SocialWebsiteDto().toBuilder()
                .website_name(websiteName)
                .link(link)
                .build();
        ArrayList<SocialWebsiteDto> socialWebsiteDtoArrayList = portfolioEntity.getSocialProfiles();
        if(socialWebsiteDtoArrayList == null || socialWebsiteDtoArrayList.size()==0){
            throw new Exception("No profile present for this id");
        }

        boolean updated = checkIfWebsiteAlreadyPresent(socialWebsiteDtoArrayList,websiteName);
        if(!updated){
            throw new Exception("No profile present for this website");
        }

        socialWebsiteDtoArrayList.add(socialWebsiteDto);
        portfolioEntity.setSocialProfiles(socialWebsiteDtoArrayList);
        portfolioRepository.saveAndFlush(portfolioEntity);
        user.setPortfolio(portfolioEntity);
        userService.updateCache(user);
        return socialWebsiteDtoArrayList;
    }

    private Boolean checkIfWebsiteAlreadyPresent(List<SocialWebsiteDto> socialWebsiteDtoArrayList,String  websiteName){
        for(SocialWebsiteDto socialWebsite:socialWebsiteDtoArrayList){
            if(socialWebsite.getWebsite_name().equals(websiteName)){
                socialWebsiteDtoArrayList.remove(socialWebsite);
                return true;
            }
        }
        return false;
    }

    @Override
    public void updateResumeLink(String cloudLink,Long userId){
        UserEntity user = userService.getUserFromUserId(userId);
        if(user.getPortfolio()==null) {
            throw new PortfolioNotExistsException("Portfolio does not exist for user with id: " + userId);
        }
        PortfolioEntity portfolioEntity = user.getPortfolio();
        portfolioEntity = portfolioEntity.toBuilder()
                .cloudResumeLink(cloudLink)
                .build();
        portfolioRepository.saveAndFlush(portfolioEntity);
        user.setPortfolio(portfolioEntity);
        userService.updateCache(user);
    }



}
