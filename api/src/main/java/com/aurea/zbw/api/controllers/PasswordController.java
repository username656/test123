package com.aurea.zbw.api.controllers;

import com.aurea.zbw.api.exceptions.BadRequestException;
import com.aurea.zbw.api.model.TokenPassword;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@Api(tags = "Login")
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/auth")
public class PasswordController {

    private static final String ERROR_EMAIL = "error@example.org";
    private static final String ERROR_RESET_KEY = "invalid-token";

    @ApiOperation(value = "Forgot Password")
    @PostMapping(path = "/forgot-password")
    @ResponseStatus(OK)
    final void doPost(@ApiParam(required = true) @RequestParam final String email) {
        if (ERROR_EMAIL.equals(email)) {
            throw new BadRequestException("The email provided does not appear on our records.");
        }
    }

    @ApiOperation(value = "Reset Password")
    @PostMapping(path = "/reset-password")
    @ResponseStatus(OK)
    public void resetPassword(@RequestBody TokenPassword tokenPassword) {
        if (ERROR_RESET_KEY.equals(tokenPassword.getToken())) {
            throw new BadRequestException("Invalid reset key");
        }
    }
}
