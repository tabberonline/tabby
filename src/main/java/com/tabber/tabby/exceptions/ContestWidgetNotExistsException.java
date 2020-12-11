package com.tabber.tabby.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value =  HttpStatus.ALREADY_REPORTED)
public class ContestWidgetNotExistsException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ContestWidgetNotExistsException(String message){ super(message);};
}