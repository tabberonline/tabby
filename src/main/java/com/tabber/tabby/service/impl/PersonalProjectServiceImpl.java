package com.tabber.tabby.service.impl;

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
    public PersonalProjectEntity createPersonalProject(PersonalProjectRequest personalProjectRequest , Long userId) {
        UserEntity userEntity = userService.getUserFromUserId(userId);
        PersonalProjectEntity personalProjectEntity = new PersonalProjectEntity()
                .toBuilder()
                .title(personalProjectRequest.getTitle())
                .link(personalProjectRequest.getLink())
                .userId(userId)
                .build();
        personalProjectRepository.saveAndFlush(personalProjectEntity);
        userEntity.getPersonalProjects().add(personalProjectEntity);
        userService.setResumePresent(userEntity);
        return personalProjectEntity;
    }

    @Override
    public PersonalProjectEntity updatePersonalProject(PersonalProjectRequest personalProjectRequest, Long projectId, Long userId) throws PersonalProjectNotExistsException {
        if(projectId==null){
            throw new PersonalProjectNotExistsException("Project id not specified");
        }
        PersonalProjectEntity personalProject = personalProjectRepository.getTopByProjectId(projectId);
        if(personalProject == null)
            throw new PersonalProjectNotExistsException("Project doesn't exist exception");
        personalProject = personalProject.toBuilder()
                .title(personalProjectRequest.getTitle())
                .link(personalProjectRequest.getLink())
                .invisible(personalProjectRequest.getInvisible())
                .build();
        personalProjectRepository.saveAndFlush(personalProject);
        return personalProject;
    }

    @Override
    public PersonalProjectEntity deletePersonalProject(Long projectId, Long userId) throws PersonalProjectNotExistsException{
        UserEntity userEntity = userService.getUserFromUserId(userId);
        if(projectId == null){
            throw new PersonalProjectNotExistsException("Project id not specified");
        }
        PersonalProjectEntity personalProjectEntity = personalProjectRepository.getTopByProjectId(projectId);
        if(personalProjectEntity==null){
            throw new PersonalProjectNotExistsException("Doesn't exist for user ");
        }
        personalProjectRepository.delete(personalProjectEntity);
        userEntity.getRankWidgets().remove(personalProjectEntity);
        userService.setResumePresent(userEntity);
        return personalProjectEntity;
    }

}

