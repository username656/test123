package com.aurea.boot.autoconfigure.api.user.json;

import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TokenPasswordJson {

    @NotEmpty
    private String token;
    @NotEmpty
    private String password;
}
