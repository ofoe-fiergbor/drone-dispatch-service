package io.iamofoe.dronedispatchservice.exception;

import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

public class HttpErrorInfo {
    private final ZonedDateTime timestamp;
    private final HttpStatus httpStatus;
    private final String message;
    private final String path;

    public HttpErrorInfo() {
        this.timestamp = null;
        this.httpStatus = null;
        this.message = null;
        this.path = null;
    }

    public HttpErrorInfo(HttpStatus httpStatus, String message, String path) {
        this.timestamp = ZonedDateTime.now();
        this.httpStatus = httpStatus;
        this.message = message;
        this.path = path;
    }

    public ZonedDateTime getTimestamp() {
        return this.timestamp;
    }

    public int getStatus() {
        return httpStatus.value();
    }

    public String getMessage() {
        return this.message;
    }

    public String getPath() {
        return this.path;
    }
    public String getError() {
        return httpStatus.getReasonPhrase();
    }

}
