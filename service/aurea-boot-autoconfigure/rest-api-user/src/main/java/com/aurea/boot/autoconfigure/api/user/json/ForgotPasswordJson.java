package com.aurea.boot.autoconfigure.api.user.json;

import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForgotPasswordJson {

    @NotEmpty
    private String email;
}
