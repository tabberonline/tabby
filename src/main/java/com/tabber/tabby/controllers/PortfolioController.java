package com.tabber.tabby.controllers;

import com.tabber.tabby.constants.URIEndpoints;
import com.tabber.tabby.dto.PortfolioRequest;
import com.tabber.tabby.entity.PortfolioEntity;
import com.tabber.tabby.exceptions.RankWidgetExistsException;
import com.tabber.tabby.exceptions.RankWidgetNotExistsException;
import com.tabber.tabby.service.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping(value = URIEndpoints.PORTFOLIO)
public class PortfolioController {

    private static final Logger logger = Logger.getLogger(PortfolioController.class.getName());

    @Autowired
    PortfolioService portfolioService;

    @PostMapping(value = URIEndpoints.CREATE,produces = "application/json")
    public ResponseEntity<PortfolioEntity> createPortfolio(
            @RequestBody @Validated PortfolioRequest portfolioRequest) throws RankWidgetExistsException {
        logger.log(Level.INFO,"Create widget request for rank widget",portfolioRequest);
        PortfolioEntity portfolioEntity = null;
        try {
            Long userId= Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
            portfolioEntity = portfolioService.createPortfolio(portfolioRequest,userId);
        }
        catch (Exception ex){
            logger.log(Level.SEVERE,"Cannot create widget due to exception: {}",ex.toString());
            throw ex;
        }
        return new ResponseEntity<>(portfolioEntity, HttpStatus.OK);
    }

    @PutMapping(value = URIEndpoints.UPDATE,produces = "application/json")
    public ResponseEntity<PortfolioEntity>  updatePortfolio(
            @RequestBody PortfolioRequest portfolioRequest) throws RankWidgetNotExistsException {
        logger.log(Level.INFO,"Update widget request for rank widget:{}",portfolioRequest);
        PortfolioEntity portfolioEntity = null;
        try {
            Long userId= Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
             portfolioEntity = portfolioService.updatePortfolio(portfolioRequest,userId);
        }
        catch (Exception ex){
            logger.log(Level.SEVERE,"Cannot update widget due to exception: {}",ex.toString());
            throw ex;
        }
        return new ResponseEntity<>(portfolioEntity, HttpStatus.OK);
    }

}
