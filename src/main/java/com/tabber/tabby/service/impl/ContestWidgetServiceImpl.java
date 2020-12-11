package com.tabber.tabby.service.impl;

import com.tabber.tabby.dto.ContestWidgetRequest;
import com.tabber.tabby.entity.ContestWidgetEntity;
import com.tabber.tabby.entity.UserEntity;
import com.tabber.tabby.exceptions.ContestWidgetExistsException;
import com.tabber.tabby.exceptions.ContestWidgetNotExistsException;
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
        if(contestWidgetExistsForWebsite(userId,contestWidgetRequest)!=null){
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
        return contestWidgetEntity;
    }
    @Override
    public ContestWidgetEntity updateContestWidget(ContestWidgetRequest contestWidgetRequest, Long userId) throws ContestWidgetNotExistsException{
        ContestWidgetEntity contestWidget = contestWidgetExistsForWebsite(userId,contestWidgetRequest);
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
    private ContestWidgetEntity contestWidgetExistsForWebsite(Long userId, ContestWidgetRequest contestWidgetRequest){
        UserEntity userEntity = userService.getUserFromUserId(userId);
        List<ContestWidgetEntity> contestWidgetEntities = userEntity.getContestWidgets();
        for(ContestWidgetEntity contestWidget:contestWidgetEntities){
            if(contestWidget.getWebsiteId().equals(contestWidgetRequest.getWebsiteId())){
                return contestWidget;
            }
        }
        return null;
    }
}
