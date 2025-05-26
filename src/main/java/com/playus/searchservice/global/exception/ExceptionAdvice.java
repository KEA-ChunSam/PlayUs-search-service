package com.playus.searchservice.global.exception;

import com.playus.searchservice.domain.post.controller.PostSearchController;
import com.playus.searchservice.global.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@RestControllerAdvice(assignableTypes = {
        PostSearchController.class
})
public class ExceptionAdvice {

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ErrorResponse handleBindException(BindException e) {
        String errorMessage = e.getAllErrors().get(0).getDefaultMessage();
        log.warn("Validation Error: {}", errorMessage);
        return ErrorResponse.badRequestError(errorMessage);
    }
}
