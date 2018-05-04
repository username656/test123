package com.aurea.boot.autoconfigure.api.user;

import static com.aurea.boot.autoconfigure.api.user.ApiConsts.User.Mapping.FORGOT_PASSWORD;
import static com.aurea.boot.autoconfigure.api.user.ApiConsts.User.Mapping.RESET_PASSWORD;

import com.aurea.boot.autoconfigure.data.user.User;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApiUserLinks {

    @NonNull
    private final EntityLinks entityLinks;

    Link getForgotPasswordLink() {
        return entityLinks.linkToCollectionResource(User.class)
                .withRel("action:forgot-password")
                .withHref(entityLinks.linkFor(User.class) + FORGOT_PASSWORD);
    }

    Link getResetPasswordLink() {
        return entityLinks.linkToCollectionResource(User.class)
                .withRel("action:reset-password")
                .withHref(entityLinks.linkFor(User.class) + RESET_PASSWORD);
    }
}
