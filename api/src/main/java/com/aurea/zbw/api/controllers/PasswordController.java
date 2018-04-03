package com.aurea.zbw.api.controllers;

import com.aurea.zbw.api.exceptions.BadRequestException;
import com.aurea.zbw.api.exceptions.NotFoundException;
import com.aurea.zbw.api.model.TokenPassword;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

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

    @ApiOperation(value = "Check Validity of Token")
    @GetMapping(path = "/check-token/{token}")
    @ResponseStatus(OK)
    public ResponseEntity<?> checkToken(@PathVariable("token") String token) {
        if (ERROR_RESET_KEY.equals(token)) {
            return new ResponseEntity<>("Token not found or expired.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
