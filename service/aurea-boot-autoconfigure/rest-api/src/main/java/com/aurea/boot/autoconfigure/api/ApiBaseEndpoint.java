package com.aurea.boot.autoconfigure.api;

import com.aurea.boot.autoconfigure.api.json.ServerResponseJson;
import javax.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public class ApiBaseEndpoint {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ServerResponseJson handleBadRequestException(RuntimeException exception) {
        return buildServerResponse(exception.getClass().getSimpleName(), exception.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ServerResponseJson handleNotFoundException(RuntimeException exception) {
        return buildServerResponse(exception.getClass().getSimpleName(), exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ServerResponseJson handleInternalErrorException(Exception exception) {
        return buildServerResponse(exception.getClass().getSimpleName(), exception.getMessage());
    }

    protected ServerResponseJson buildServerResponse(String error, String message) {
        return ServerResponseJson.builder()
                .error(error)
                .message(message)
                .build();
    }
}
