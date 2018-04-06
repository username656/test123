package com.aurea.boot.autoconfigure.api.user;

import static com.aurea.boot.autoconfigure.api.user.ApiConsts.User.Mapping.RESET_PASSWORD;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@ApiEndpointMapping(
        value = Mapping.API_USER,
        tags = "User API"
)
@RequiredArgsConstructor
public class ApiUserEndpoint {

    private final UserRepository userRepository;

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
        if ("invalid-token".equals(tokenPasswordJson.getToken())) {
            throw new BadRequestException("Invalid reset key");
        }
    }

    @ApiOperation("Get current user")
    @GetMapping(Mapping.CURRENT_USER)
    public User getCurrentUser(Principal principal) {
        return userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new BadRequestException("User not found"));
    }
}
