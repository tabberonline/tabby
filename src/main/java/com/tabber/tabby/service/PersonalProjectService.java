package com.tabber.tabby.service;

import com.tabber.tabby.dto.PersonalProjectRequest;
import com.tabber.tabby.entity.PersonalProjectEntity;

public interface PersonalProjectService {
    PersonalProjectEntity createPersonalProject(PersonalProjectRequest personalProjectRequest , Long userId);
    PersonalProjectEntity updatePersonalProject(PersonalProjectRequest personalProjectRequest, Long userId);
    PersonalProjectEntity deletePersonalProject(Long projectId, Long userId);
}
