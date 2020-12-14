package com.tabber.tabby.controllers;

import com.tabber.tabby.constants.URIEndpoints;
import com.tabber.tabby.dto.ContestWidgetRequest;
import com.tabber.tabby.entity.ContestWidgetEntity;
import com.tabber.tabby.exceptions.ContestWidgetExistsException;
import com.tabber.tabby.exceptions.ContestWidgetNotExistsException;
import com.tabber.tabby.service.ContestWidgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping(value = URIEndpoints.CONTEST_WIDGET)
public class ContestWidgetController {

    private static final Logger logger = Logger.getLogger(RankWidgetController.class.getName());

    @Autowired
    ContestWidgetService contestWidgetService;

    @PostMapping(value = URIEndpoints.CREATE_WIDGET,produces = "application/json")
    public ResponseEntity<ContestWidgetEntity> createRankWidget(
            @RequestBody @Validated ContestWidgetRequest contestWidgetRequest) throws ContestWidgetExistsException {
        logger.log(Level.INFO,"Create widget request for contest widget",contestWidgetRequest);
        ContestWidgetEntity contestWidgetEntity = null;
        try {
            Long userId= Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
            contestWidgetEntity = contestWidgetService.createContestWidget(contestWidgetRequest,userId);
        }
        catch (Exception ex){
            logger.log(Level.SEVERE,"Cannot create widget due to exception: {}",ex.toString());
            throw ex;
        }
        return new ResponseEntity<>(contestWidgetEntity, HttpStatus.OK);
    }

    @PutMapping(value = URIEndpoints.UPDATE_WIDGET,produces = "application/json")
    public ResponseEntity<ContestWidgetEntity>  updateRankWidget(
            @RequestBody ContestWidgetRequest contestWidgetRequest) throws ContestWidgetNotExistsException {
        logger.log(Level.INFO,"Update widget request for rank widget",contestWidgetRequest);
        ContestWidgetEntity contestWidgetEntity = null;
        try {
            Long userId= Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
            contestWidgetEntity = contestWidgetService.updateContestWidget(contestWidgetRequest,userId);
        }
        catch (Exception ex){
            logger.log(Level.SEVERE,"Cannot update widget due to exception: {}",ex.toString());
            throw ex;
        }
        return new ResponseEntity<>(contestWidgetEntity, HttpStatus.OK);
    }
}