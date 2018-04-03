package com.aurea.boot.autoconfigure.api.user;

import com.aurea.boot.autoconfigure.api.annotation.ApiEndpointMapping;
import com.aurea.boot.autoconfigure.api.user.ApiConsts.User.Mapping;
import com.aurea.boot.autoconfigure.api.user.json.TokenPasswordJson;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@ApiEndpointMapping(
        value = Mapping.API_USER,
        tags = "User API"
)
public class ApiUserEndpoint {

    @ApiOperation("Forgot Password")
    @PostMapping(Mapping.FORGOT_PASSWORD)
    public ResponseEntity<String> forgotPassword(
            @ApiParam(required = true)
            @RequestParam final String email) {
        if ("error@example.org".equals(email)) {
            return new ResponseEntity<>(
                    "The email provided does not appear on our records.", HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>("Forgot password mail sent.", HttpStatus.OK);
        }
    }

    @ApiOperation("Reset Password")
    @PostMapping(Mapping.RESET_PASSWORD)
    public ResponseEntity<String> resetPassword(TokenPasswordJson tokenPasswordJson) {
        if ("invalid-token".equals(tokenPasswordJson.getToken())) {
            return new ResponseEntity<>("Invalid reset key", HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>("Password has been reset.", HttpStatus.OK);
        }
    }
}
