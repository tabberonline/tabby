package com.tabber.tabby.controllers;

import com.tabber.tabby.constants.URIEndpoints;
import com.tabber.tabby.dto.RankWidgetRequest;
import com.tabber.tabby.entity.RankWidgetEntity;
import com.tabber.tabby.exceptions.RankWidgetExistsException;
import com.tabber.tabby.exceptions.RankWidgetNotExistsException;
import com.tabber.tabby.service.RankWidgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping(value = URIEndpoints.RANK_WIDGET)
public class RankWidgetController {

    private static final Logger logger = Logger.getLogger(RankWidgetController.class.getName());

    @Autowired
    RankWidgetService rankWidgetService;

    @PostMapping(value = URIEndpoints.CREATE_WIDGET,produces = "application/json")
    public ResponseEntity<RankWidgetEntity> createRankWidget(
            @RequestBody @Validated RankWidgetRequest rankWidgetRequest) throws RankWidgetExistsException {
        logger.log(Level.INFO,"Create widget request for rank widget",rankWidgetRequest);
        RankWidgetEntity rankWidgetEntity = null;
        try {
            Long userId= Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
            rankWidgetEntity = rankWidgetService.createRankWidget(rankWidgetRequest,userId);
        }
        catch (Exception ex){
            logger.log(Level.SEVERE,"Cannot create widget due to exception: {}",ex.toString());
           throw ex;
        }
        return new ResponseEntity<>(rankWidgetEntity, HttpStatus.OK);
    }

    @PutMapping(value = URIEndpoints.UPDATE_WIDGET,produces = "application/json")
    public ResponseEntity<RankWidgetEntity>  updateRankWidget(
            @RequestBody RankWidgetRequest rankWidgetRequest) throws RankWidgetNotExistsException {
        logger.log(Level.INFO,"Update widget request for rank widget",rankWidgetRequest);
        RankWidgetEntity rankWidgetEntity = null;
        try {
            Long userId= Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
            rankWidgetEntity = rankWidgetService.updateRankWidget(rankWidgetRequest,userId);
        }
        catch (Exception ex){
            logger.log(Level.SEVERE,"Cannot update widget due to exception: {}",ex.toString());
            throw ex;
        }
        return new ResponseEntity<>(rankWidgetEntity, HttpStatus.OK);
    }
}
