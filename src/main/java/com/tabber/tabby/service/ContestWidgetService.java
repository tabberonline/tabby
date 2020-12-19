package com.tabber.tabby.service;

import com.tabber.tabby.dto.ContestWidgetRequest;
import com.tabber.tabby.entity.ContestWidgetEntity;
import com.tabber.tabby.exceptions.ContestWidgetExistsException;
import com.tabber.tabby.exceptions.ContestWidgetNotExistsException;

public interface ContestWidgetService {
    ContestWidgetEntity createContestWidget(ContestWidgetRequest rankWidgetRequest, Long userId) throws ContestWidgetExistsException;

    ContestWidgetEntity updateContestWidget(ContestWidgetRequest rankWidgetRequest, Long contestId,Long userId) throws ContestWidgetNotExistsException;

    ContestWidgetEntity deleteContestWidget(Long contestId, Long userId) throws ContestWidgetNotExistsException;
}
