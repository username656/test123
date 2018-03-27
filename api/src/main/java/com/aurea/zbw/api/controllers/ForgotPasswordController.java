package com.aurea.zbw.api.controllers;

import static com.aurea.zbw.api.controllers.ForgotPasswordController.ENDPOINT;
import static org.springframework.http.HttpStatus.OK;

import com.aurea.zbw.api.exceptions.BadRequestException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "Login")
@RestController
@RequestMapping(path = ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
public class ForgotPasswordController {

    public static final String ENDPOINT = "/auth/forgot-password";
    private static final String ERROR_EMAIL = "error@example.org";

    @ApiOperation(value = "Forgot Password")
    @PostMapping
    @ResponseStatus(OK)
    final void doPost(@ApiParam(required = true) @RequestParam final String email) {
        if (ERROR_EMAIL.equals(email)) {
            throw new BadRequestException("The email provided does not appear on our records.");
        }
    }

}
