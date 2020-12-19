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
    public ContestWidgetEntity createContestWidget(ContestWidgetRequest contestWidgetRequest, Long userId) {
        UserEntity userEntity = userService.getUserFromUserId(userId);
        ContestWidgetEntity contestWidgetEntity = new ContestWidgetEntity()
                .toBuilder()
                .websiteId(contestWidgetRequest.getWebsiteId())
                .websiteUsername(contestWidgetRequest.getUsername())
                .rank(contestWidgetRequest.getRank())
                .userId(userId)
                .contestName(contestWidgetRequest.getContestName())
                .build();
        contestWidgetRepository.saveAndFlush(contestWidgetEntity);
        userEntity.getContestWidgets().add(contestWidgetEntity);
        userService.setResumePresent(userEntity);
        return contestWidgetEntity;
    }
    @Override
    public ContestWidgetEntity updateContestWidget(ContestWidgetRequest contestWidgetRequest, Long userId) throws ContestWidgetNotExistsException{
        ContestWidgetEntity contestWidget = contestWidgetRepository.getTopByWidgetId(contestWidgetRequest.getId());
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
    public ContestWidgetEntity deleteContestWidget(ContestWidgetRequest contestWidgetRequest, Long userId) throws ContestWidgetNotExistsException {
        UserEntity userEntity = userService.getUserFromUserId(userId);
        ContestWidgetEntity contestWidget = contestWidgetRepository.getTopByWidgetId(contestWidgetRequest.getId());
        if(contestWidget==null){
            throw new ContestWidgetNotExistsException("Doesn't exist for user "+userId+" for website id "+contestWidgetRequest.getWebsiteId());
        }
        contestWidgetRepository.delete(contestWidget);
        userEntity.getContestWidgets().remove(contestWidget);
        userService.setResumePresent(userEntity);
        return contestWidget;
    }


}
