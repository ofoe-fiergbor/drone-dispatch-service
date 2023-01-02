package io.iamofoe.dronedispatchservice.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public @ResponseBody HttpErrorInfo handleNotFoundException(NotFoundException ex, HttpServletRequest request) {
        return createHttpErrorInfo(NOT_FOUND, ex, request);
    }

    @ResponseStatus(UNPROCESSABLE_ENTITY)
    @ExceptionHandler(InvalidInputException.class)
    public @ResponseBody HttpErrorInfo handleInvalidInputException(InvalidInputException ex, HttpServletRequest request) {
        return createHttpErrorInfo(UNPROCESSABLE_ENTITY, ex, request);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public @ResponseBody HttpErrorInfo handleBadRequestException(BadRequestException ex, HttpServletRequest request) {
        return createHttpErrorInfo(BAD_REQUEST, ex, request);
    }

    private HttpErrorInfo createHttpErrorInfo(HttpStatus httpStatus, RuntimeException ex, HttpServletRequest request) {
        String message = ex.getMessage();
        String path = request.getRequestURI();
        LOG.debug("Returning HTTP status: {}, message: {}, path: {}", httpStatus, message, path);
        return new HttpErrorInfo(httpStatus, message, path);
    }

}
