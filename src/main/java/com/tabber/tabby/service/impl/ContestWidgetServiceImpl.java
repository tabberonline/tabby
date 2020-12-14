package com.tabber.tabby.service.impl;

import com.tabber.tabby.dto.ContestWidgetRequest;
import com.tabber.tabby.entity.ContestWidgetEntity;
import com.tabber.tabby.entity.UserEntity;
import com.tabber.tabby.exceptions.ContestWidgetExistsException;
import com.tabber.tabby.exceptions.ContestWidgetNotExistsException;
import com.tabber.tabby.exceptions.RankWidgetNotExistsException;
import com.tabber.tabby.respository.ContestWidgetRepository;
import com.tabber.tabby.service.ContestWidgetService;
import com.tabber.tabby.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContestWidgetServiceImpl implements ContestWidgetService {

    @Autowired
    UserService userService;

    @Autowired
    ContestWidgetRepository contestWidgetRepository;

    @Override
    public ContestWidgetEntity createContestWidget(ContestWidgetRequest contestWidgetRequest, Long userId) throws ContestWidgetExistsException{
        UserEntity userEntity = userService.getUserFromUserId(userId);
        if(contestWidgetExistsForWebsite(userEntity,contestWidgetRequest)!=null){
            throw new ContestWidgetExistsException("Exists for user "+userId+" for website id "+contestWidgetRequest.getWebsiteId());
        }
        ContestWidgetEntity contestWidgetEntity = new ContestWidgetEntity()
                .toBuilder()
                .websiteId(contestWidgetRequest.getWebsiteId())
                .websiteUsername(contestWidgetRequest.getUsername())
                .rank(contestWidgetRequest.getRank())
                .userId(userId)
                .contestName(contestWidgetRequest.getContestName())
                .build();
        contestWidgetRepository.saveAndFlush(contestWidgetEntity);
        userService.setResumePresent(userEntity);
        return contestWidgetEntity;
    }
    @Override
    public ContestWidgetEntity updateContestWidget(ContestWidgetRequest contestWidgetRequest, Long userId) throws ContestWidgetNotExistsException{
        UserEntity userEntity = userService.getUserFromUserId(userId);
        ContestWidgetEntity contestWidget = contestWidgetExistsForWebsite(userEntity,contestWidgetRequest);
        if(contestWidget==null){
            throw new ContestWidgetNotExistsException("Doesn't exist for user "+userId+" for website id "+contestWidgetRequest.getWebsiteId());
        }
        contestWidget = contestWidget.toBuilder()
                .rank(contestWidgetRequest.getRank())
                .websiteId(contestWidgetRequest.getWebsiteId())
                .websiteUsername(contestWidgetRequest.getUsername())
                .contestName(contestWidgetRequest.getContestName())
                .build();
        contestWidgetRepository.saveAndFlush(contestWidget);
        return contestWidget;
    }

    @Override
    public ContestWidgetEntity deleteRankWidget(ContestWidgetRequest contestWidgetRequest, Long userId) throws ContestWidgetNotExistsException {
        UserEntity userEntity = userService.getUserFromUserId(userId);
        ContestWidgetEntity contestWidget = contestWidgetExistsForWebsite(userEntity,contestWidgetRequest);
        if(contestWidget==null){
            throw new RankWidgetNotExistsException("Doesn't exist for user "+userId+" for website id "+contestWidgetRequest.getWebsiteId());
        }
        contestWidgetRepository.delete(contestWidget);
        userService.setResumePresent(userEntity);
        return contestWidget;
    }

    private ContestWidgetEntity contestWidgetExistsForWebsite(UserEntity userEntity, ContestWidgetRequest contestWidgetRequest){
        List<ContestWidgetEntity> contestWidgetEntities = userEntity.getContestWidgets();
        for(ContestWidgetEntity contestWidget:contestWidgetEntities){
            if(contestWidget.getWebsiteId().equals(contestWidgetRequest.getWebsiteId())){
                return contestWidget;
            }
        }
        return null;
    }
}
