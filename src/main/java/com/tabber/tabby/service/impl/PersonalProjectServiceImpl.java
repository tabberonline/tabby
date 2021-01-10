package com.tabber.tabby.service.impl;

import com.tabber.tabby.constants.TabbyConstants;
import com.tabber.tabby.dto.PersonalProjectRequest;
import com.tabber.tabby.entity.PersonalProjectEntity;
import com.tabber.tabby.entity.UserEntity;
import com.tabber.tabby.exceptions.PersonalProjectNotExistsException;
import com.tabber.tabby.respository.PersonalProjectRepository;
import com.tabber.tabby.service.PersonalProjectService;
import com.tabber.tabby.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonalProjectServiceImpl implements PersonalProjectService {
    @Autowired
    UserService userService;

    @Autowired
    PersonalProjectRepository personalProjectRepository;

    @Override
    public PersonalProjectEntity createPersonalProject(PersonalProjectRequest personalProjectRequest , Long userId)
    throws Exception{
        UserEntity userEntity = userService.getUserFromUserId(userId);
        if(userEntity.getPersonalProjects().size() >= TabbyConstants.PERSONAL_PROJECT_SIZE_LIMIT)
            throw new Exception("Personal project size limit is reached");
        PersonalProjectEntity personalProjectEntity = new PersonalProjectEntity()
                .toBuilder()
                .title(personalProjectRequest.getTitle())
                .link(personalProjectRequest.getLink())
                .userId(userId)
                .build();
        personalProjectRepository.saveAndFlush(personalProjectEntity);
        userEntity.getPersonalProjects().add(personalProjectEntity);
        userEntity = userService.setResumePresent(userEntity);
        userService.updateCache(userEntity);
        return personalProjectEntity;
    }

    @Override
    public PersonalProjectEntity updatePersonalProject(PersonalProjectRequest personalProjectRequest, Long projectId, Long userId) throws PersonalProjectNotExistsException {
        if(projectId==null){
            throw new PersonalProjectNotExistsException("Project id not specified");
        }
        UserEntity userEntity = userService.getUserFromUserId(userId);
        PersonalProjectEntity personalProject = getPersonalProjectFromProjectId(userEntity,projectId);
        if(personalProject == null)
            throw new PersonalProjectNotExistsException("Project doesn't exist exception");
        userEntity.getPersonalProjects().remove(personalProject);
        personalProject = personalProject.toBuilder()
                .title(personalProjectRequest.getTitle())
                .link(personalProjectRequest.getLink())
                .invisible(personalProjectRequest.getInvisible())
                .build();
        personalProjectRepository.saveAndFlush(personalProject);
        userEntity.getPersonalProjects().add(personalProject);
        userService.updateCache(userEntity);
        return personalProject;
    }

    @Override
    public PersonalProjectEntity deletePersonalProject(Long projectId, Long userId) throws PersonalProjectNotExistsException{
        UserEntity userEntity = userService.getUserFromUserId(userId);
        if(projectId == null){
            throw new PersonalProjectNotExistsException("Project id not specified");
        }
        PersonalProjectEntity personalProjectEntity = getPersonalProjectFromProjectId(userEntity,projectId);
        if(personalProjectEntity==null){
            throw new PersonalProjectNotExistsException("Doesn't exist for user ");
        }
        personalProjectRepository.delete(personalProjectEntity);
        userEntity.getPersonalProjects().remove(personalProjectEntity);
        userEntity = userService.setResumePresent(userEntity);
        userService.updateCache(userEntity);
        return personalProjectEntity;
    }

    private PersonalProjectEntity getPersonalProjectFromProjectId(UserEntity userEntity,Long projectId){
        for(PersonalProjectEntity personalProject:userEntity.getPersonalProjects()){
            if(personalProject.getPersonalProjectId().equals(projectId))
                return personalProject;
        }
        return null;
    }

}

