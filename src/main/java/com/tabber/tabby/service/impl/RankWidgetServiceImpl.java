package com.tabber.tabby.service.impl;

import com.tabber.tabby.dto.RankWidgetRequest;
import com.tabber.tabby.entity.RankWidgetEntity;
import com.tabber.tabby.entity.UserEntity;
import com.tabber.tabby.exceptions.RankWidgetExistsException;
import com.tabber.tabby.exceptions.RankWidgetNotExistsException;
import com.tabber.tabby.respository.RankWidgetRepository;
import com.tabber.tabby.service.RankWidgetService;
import com.tabber.tabby.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RankWidgetServiceImpl implements RankWidgetService {

    @Autowired
    RankWidgetRepository rankWidgetRepository;

    @Autowired
    UserService userService;

    @Override
    public RankWidgetEntity createRankWidget(RankWidgetRequest rankWidgetRequest, Long userId) throws RankWidgetExistsException {
        if(rankWidgetExistsForWebsite(userId,rankWidgetRequest)!=null){
            throw new RankWidgetExistsException("Exists for user "+userId+" for website id "+rankWidgetRequest.getWebsiteId());
        }
        RankWidgetEntity rankWidgetEntity = new RankWidgetEntity()
                .toBuilder()
                .websiteId(rankWidgetRequest.getWebsiteId())
                .websiteUsername(rankWidgetRequest.getUsername())
                .rank(rankWidgetRequest.getRank())
                .userId(userId)
                .build();
        rankWidgetRepository.saveAndFlush(rankWidgetEntity);
        return rankWidgetEntity;
    }

    @Override
    public RankWidgetEntity updateRankWidget(RankWidgetRequest rankWidgetRequest, Long userId) throws RankWidgetNotExistsException{
        RankWidgetEntity rankWidget = rankWidgetExistsForWebsite(userId,rankWidgetRequest);
        if(rankWidget==null){
            throw new RankWidgetNotExistsException("Doesn't exist for user "+userId+" for website id "+rankWidgetRequest.getWebsiteId());
        }
        rankWidget = rankWidget.toBuilder()
                .rank(rankWidgetRequest.getRank())
                .websiteId(rankWidgetRequest.getWebsiteId())
                .websiteUsername(rankWidgetRequest.getUsername())
                .build();
        rankWidgetRepository.saveAndFlush(rankWidget);
        return rankWidget;
    }

    @Override
    public RankWidgetEntity deleteRankWidget(RankWidgetRequest rankWidgetRequest, Long userId) throws RankWidgetNotExistsException{
        RankWidgetEntity rankWidget = rankWidgetExistsForWebsite(userId,rankWidgetRequest);
        if(rankWidget==null){
            throw new RankWidgetNotExistsException("Doesn't exist for user "+userId+" for website id "+rankWidgetRequest.getWebsiteId());
        }
        rankWidgetRepository.delete(rankWidget);
        return rankWidget;
    }


    private RankWidgetEntity rankWidgetExistsForWebsite(Long userId,RankWidgetRequest rankWidgetRequest){
        UserEntity userEntity = userService.getUserFromUserId(userId);
        List<RankWidgetEntity> rankWidgetEntities = userEntity.getRankWidgets();
        for(RankWidgetEntity rankWidget:rankWidgetEntities){
            if(rankWidget.getWebsiteId().equals(rankWidgetRequest.getWebsiteId())){
                return rankWidget;
            }
        }
        return null;
    }

}
