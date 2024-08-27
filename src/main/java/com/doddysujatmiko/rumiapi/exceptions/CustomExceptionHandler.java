package com.doddysujatmiko.rumiapi.exceptions;

import com.doddysujatmiko.rumiapi.common.utils.Responser;
import com.doddysujatmiko.rumiapi.log.LogService;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.AbstractAuditable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {
    @Autowired
    LogService logService;

    @Autowired
    Responser responser;

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(Exception exception) {
        logService.logError(exception.getMessage());
        return responser.response(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handleBadRequestException(Exception exception) {
        logService.logError(exception.getMessage());
        return responser.response(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<?> handleUnauthorizedException(Exception exception) {
        logService.logError(exception.getMessage());
        return responser.response(HttpStatus.UNAUTHORIZED, exception.getMessage());
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<?> handleForbiddenException(Exception exception) {
        logService.logError(exception.getMessage());
        return responser.response(HttpStatus.FORBIDDEN, exception.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException(Exception exception) {
        logService.logError(exception.getMessage());
        return responser.response(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<?> handleInternalServerErrorException(Exception exception) {
        logService.logError(exception.getMessage());
        return responser.response(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<?> handleException(Exception exception) {
//        logService.logError(exception.getMessage());
//        return responser.response(HttpStatus.INTERNAL_SERVER_ERROR, "[UNHANDLED EXCEPTION] " + exception.getMessage());
//    }
//
//    @ExceptionHandler(Error.class)
//    public ResponseEntity<?> handleError(Error error) {
//        logService.logError(error.getMessage());
//        return responser.response(HttpStatus.INTERNAL_SERVER_ERROR, "[UNHANDLED ERROR] " + error.getMessage());
//    }
}