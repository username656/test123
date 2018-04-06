package com.aurea.boot.autoconfigure.api.user;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import com.aurea.boot.autoconfigure.api.annotation.ApiEndpointMapping;
import com.aurea.boot.autoconfigure.api.error.BadRequestException;
import com.aurea.boot.autoconfigure.api.user.ApiConsts.User.Mapping;
import com.aurea.boot.autoconfigure.api.user.json.TokenPasswordJson;
import com.aurea.boot.autoconfigure.data.user.User;
import com.aurea.boot.autoconfigure.data.user.UserRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@ApiEndpointMapping(
        value = Mapping.API_USER,
        tags = "User API"
)
@RequiredArgsConstructor
public class ApiUserEndpoint {

    private static final String ERROR_RESET_KEY = "invalid-token";

    private final UserRepository userRepository;

    @ApiOperation("Forgot Password")
    @PostMapping(Mapping.FORGOT_PASSWORD)
    public ResponseEntity<String> forgotPassword(
            @ApiParam(required = true)
            @RequestParam final String email) {
        if ("error@example.org".equals(email)) {
            return new ResponseEntity<>(
                    "The email provided does not appear on our records.", BAD_REQUEST);
        } else {
            return new ResponseEntity<>("Forgot password mail sent.", HttpStatus.OK);
        }
    }

    @ApiOperation("Reset Password")
    @PostMapping(Mapping.RESET_PASSWORD)
    public ResponseEntity<String> resetPassword(TokenPasswordJson tokenPasswordJson) {
        if ("invalid-token".equals(tokenPasswordJson.getToken())) {
            return new ResponseEntity<>("Invalid reset key", BAD_REQUEST);
        } else {
            return new ResponseEntity<>("Password has been reset.", HttpStatus.OK);
        }
    }

    @ApiOperation("Get current user")
    @GetMapping(Mapping.CURRENT_USER)
    public User getCurrentUser(Principal principal) {
        return userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new BadRequestException("User not found"));
    }

    @ApiOperation("Validate the reset password token")
    @GetMapping(path = "/check-reset-password-token")
    public ResponseEntity checkToken(@ApiParam(required = true) @RequestParam String token) {
        if (ERROR_RESET_KEY.equals(token)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
