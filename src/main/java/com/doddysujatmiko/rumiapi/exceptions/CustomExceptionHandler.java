package com.doddysujatmiko.rumiapi.exceptions;

import com.doddysujatmiko.rumiapi.common.utils.Responser;
import com.doddysujatmiko.rumiapi.log.LogService;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
public class CustomExceptionHandler {
    private final LogService logService;
    private final Responser responser;
    private final Environment environment;

    @Autowired
    public CustomExceptionHandler(LogService logService, Responser responser, Environment environment) {
        this.logService = logService;
        this.responser = responser;
        this.environment = environment;
    }

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

    private String constructConciseStackTrace(Throwable t, Integer maxIndex) {
        var stackTrace = t.getStackTrace();
        if(t.getCause() != null) stackTrace = t.getCause().getStackTrace();
        return  "StackTrace" + Arrays
                .stream(stackTrace)
                .limit(maxIndex)
                .map(element -> "," + element.getClassName() +
                        "/" + element.getMethodName() +
                        "/" + element.getLineNumber())
                .collect(Collectors.joining());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception exception) {
        var conciseStackTrace = constructConciseStackTrace(exception, 3);
        logService.logError(conciseStackTrace);
        if(Objects.equals(environment.getActiveProfiles()[0], "dev"))
            return responser.response(HttpStatus.INTERNAL_SERVER_ERROR, "[UNHANDLED EXCEPTION] " +
                    exception.getMessage() + conciseStackTrace);

        return responser.response(HttpStatus.INTERNAL_SERVER_ERROR, "[UNHANDLED EXCEPTION] " + exception.getMessage());
    }

    @ExceptionHandler(Error.class)
    public ResponseEntity<?> handleError(Error error) {
        var conciseStackTrace = constructConciseStackTrace(error, 3);
        logService.logError(conciseStackTrace);
        if(Objects.equals(environment.getActiveProfiles()[0], "dev"))
            return responser.response(HttpStatus.INTERNAL_SERVER_ERROR, "[UNHANDLED ERROR] " +
                    error.getMessage() + conciseStackTrace);

        return responser.response(HttpStatus.INTERNAL_SERVER_ERROR, "[UNHANDLED ERROR] " + error.getMessage());
    }
}