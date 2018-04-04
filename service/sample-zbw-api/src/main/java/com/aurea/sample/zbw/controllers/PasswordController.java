package com.aurea.sample.zbw.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

@Api(tags = "api-reset-password")
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/oauth")
public class PasswordController {

    private static final String ERROR_RESET_KEY = "invalid-token";

    @ApiOperation(value = "Validate the reset password token")
    @GetMapping(path = "/check_reset_token")
    @ResponseStatus(OK)
    public ResponseEntity<?> checkToken(@RequestParam("token") String token) {
        if (ERROR_RESET_KEY.equals(token)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
