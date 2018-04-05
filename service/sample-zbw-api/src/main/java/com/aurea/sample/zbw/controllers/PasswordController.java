package com.aurea.sample.zbw.controllers;

import static org.springframework.http.HttpStatus.OK;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "api-reset-password")
@RequiredArgsConstructor
@RestController
@RequestMapping("/oauth")
public class PasswordController {

    private static final String ERROR_RESET_KEY = "invalid-token";

    @ApiOperation("Validate the reset password token")
    @GetMapping(path = "/check-reset-token")
    @ResponseStatus(OK)
    public ResponseEntity checkToken(@RequestParam("token") String token) {
        if (ERROR_RESET_KEY.equals(token)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
