package com.tabber.tabby.service.impl;

import com.tabber.tabby.dto.CourseWidgetRequest;
import com.tabber.tabby.dto.ExperienceWidgetRequest;
import com.tabber.tabby.entity.CourseWidgetEntity;
import com.tabber.tabby.entity.ExperienceWidgetEntity;
import com.tabber.tabby.entity.PlanEntity;
import com.tabber.tabby.entity.UserEntity;
import com.tabber.tabby.exceptions.CourseWidgetNotExistsException;
import com.tabber.tabby.exceptions.ExperienceWidgetNotExistsException;
import com.tabber.tabby.manager.PlansManager;
import com.tabber.tabby.respository.CourseWidgetRepository;
import com.tabber.tabby.respository.ExperienceWidgetRepository;
import com.tabber.tabby.service.CourseWidgetService;
import com.tabber.tabby.service.ExperienceWidgetService;
import com.tabber.tabby.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExperienceWidgetServiceImpl implements ExperienceWidgetService {

    @Autowired
    UserService userService;

    @Autowired
    ExperienceWidgetRepository experienceWidgetRepository;

    @Autowired
    PlansManager plansManager;

    @Override
    public ExperienceWidgetEntity createExperienceWidget(ExperienceWidgetRequest experienceWidgetRequest, Long userId) throws Exception {
        UserEntity userEntity = userService.getUserFromUserId(userId);
        PlanEntity planEntity = plansManager.findPlanById(userEntity.getPlanId());
        if(userEntity.getExperienceWidgets().size() >= planEntity.getExperienceLimit())
            throw new Exception("Experience widget size limit is reached");
        ExperienceWidgetEntity experienceWidgetEntity = new ExperienceWidgetEntity()
                .toBuilder()
                .companyName(experienceWidgetRequest.getCompanyName())
                .description(experienceWidgetRequest.getDescription())
                .type(experienceWidgetRequest.getType())
                .startDate(experienceWidgetRequest.getStartDate())
                .endDate(experienceWidgetRequest.getEndDate())
                .cloudCertificationLink(experienceWidgetRequest.getCloudCertificationLink())
                .experienceUserId(userId)
                .build();
        experienceWidgetRepository.saveAndFlush(experienceWidgetEntity);
        userEntity.getExperienceWidgets().add(experienceWidgetEntity);
        userEntity = userService.setResumePresent(userEntity);
        userService.updateCache(userEntity);
        return experienceWidgetEntity;
    }

    @Override
    public ExperienceWidgetEntity updateExperienceWidget(ExperienceWidgetRequest experienceWidgetRequest, Integer experienceId, Long userId) throws ExperienceWidgetNotExistsException {
        if(experienceId == null){
            throw new ExperienceWidgetNotExistsException("Experience Id not specified");
        }
        UserEntity userEntity = userService.getUserFromUserId(userId);
        ExperienceWidgetEntity experienceWidget = getExperienceById(experienceId, userEntity);
        if(experienceWidget==null){
            throw new ExperienceWidgetNotExistsException("Doesn't exist for user "+userId+" for experience id "+experienceId);
        }
        userEntity.getExperienceWidgets().remove(experienceWidget);
        ExperienceWidgetEntity experienceWidgetEntity = new ExperienceWidgetEntity()
                .toBuilder()
                .companyName(experienceWidgetRequest.getCompanyName())
                .description(experienceWidgetRequest.getDescription())
                .type(experienceWidgetRequest.getType())
                .startDate(experienceWidgetRequest.getStartDate())
                .endDate(experienceWidgetRequest.getEndDate())
                .cloudCertificationLink(experienceWidgetRequest.getCloudCertificationLink())
                .experienceUserId(userId)
                .build();
        experienceWidgetRepository.saveAndFlush(experienceWidgetEntity);
        userEntity.getExperienceWidgets().add(experienceWidgetEntity);
        userEntity = userService.setResumePresent(userEntity);
        return experienceWidgetEntity;
    }

    @Override
    public ExperienceWidgetEntity deleteExperienceWidget(Integer experienceId, Long userId) throws ExperienceWidgetNotExistsException {
        UserEntity userEntity = userService.getUserFromUserId(userId);
        if(experienceId == null){
            throw new ExperienceWidgetNotExistsException("Widget Id not specified");
        }
        ExperienceWidgetEntity experienceWidgetEntity = getExperienceById(experienceId, userEntity);
        if(experienceWidgetEntity==null){
            throw new ExperienceWidgetNotExistsException("Doesn't exist for user "+userId+" for course id "+ experienceId);
        }
        experienceWidgetRepository.delete(experienceWidgetEntity);
        userEntity.getExperienceWidgets().remove(experienceWidgetEntity);
        userEntity = userService.setResumePresent(userEntity);
        userService.updateCache(userEntity);
        return experienceWidgetEntity;
    }

    private ExperienceWidgetEntity getExperienceById(Integer experienceId,UserEntity userEntity){
        List<ExperienceWidgetEntity> experienceWidgetEntities = userEntity.getExperienceWidgets();
        for(ExperienceWidgetEntity experienceWidget:experienceWidgetEntities){
            if(experienceWidget.getExperienceId().equals(experienceId)){
                return experienceWidget;
            }
        }
        return null;
    }
}
