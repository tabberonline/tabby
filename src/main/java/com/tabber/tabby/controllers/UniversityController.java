package com.tabber.tabby.controllers;

import com.tabber.tabby.constants.URIEndpoints;
import com.tabber.tabby.constants.UniversityListConstants;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(value = URIEndpoints.UNIVERSITY)
public class UniversityController {

    @GetMapping(value = URIEndpoints.UNIVERSITY_LIST,produces = "application/json")
    public ResponseEntity<Map> getUniversityList() throws Exception {

        return new ResponseEntity<>(UniversityListConstants.universityList, HttpStatus.OK);
    }
}
