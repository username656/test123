package com.aurea.boot.autoconfigure.api.user;

import static com.aurea.boot.autoconfigure.api.user.ApiConsts.User.Mapping.RESET_PASSWORD;

import com.aurea.boot.autoconfigure.api.annotation.ApiEndpointMapping;
import com.aurea.boot.autoconfigure.api.error.BadRequestException;
import com.aurea.boot.autoconfigure.api.user.ApiConsts.User.Mapping;
import com.aurea.boot.autoconfigure.api.user.json.TokenPasswordJson;
import com.aurea.boot.autoconfigure.data.user.User;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.security.Principal;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@ApiEndpointMapping(
        value = Mapping.API_USER,
        tags = "User API"
)
@RequiredArgsConstructor
public class ApiUserEndpoint {

    @NonNull
    private final UserService userService;

    @ApiOperation("Forgot Password")
    @PostMapping(Mapping.FORGOT_PASSWORD)
    public void forgotPassword(@ApiParam(required = true) @RequestBody String email) {
        if ("error@example.org".equals(email)) {
            throw new BadRequestException("The email provided does not appear on our records");
        }
    }

    @ApiOperation("Reset Password")
    @PostMapping(RESET_PASSWORD)
    public void resetPassword(@RequestBody TokenPasswordJson tokenPasswordJson) {
        if (StringUtils.isEmpty(tokenPasswordJson.getToken())) {
            throw new IllegalArgumentException("Reset token empty");
        }
        if (StringUtils.isEmpty(tokenPasswordJson.getPassword())) {
            throw new IllegalArgumentException("New password empty");
        }
        userService.resetPassword(tokenPasswordJson);
    }

    @ApiOperation("Get current user")
    @GetMapping(Mapping.CURRENT_USER)
    public User getCurrentUser(Principal principal) {
        return userService.getCurrentUser(principal.getName());
    }

    @ApiOperation("Validate the reset password token")
    @GetMapping(Mapping.CHECK_RESET_PASSWORD_TOKEN)
    public void checkResetPasswordToken(@ApiParam(required = true) @RequestParam String token) {
        if ("invalid-token".equals(token)) {
            throw new BadRequestException("Invalid token");
        }
    }
}
