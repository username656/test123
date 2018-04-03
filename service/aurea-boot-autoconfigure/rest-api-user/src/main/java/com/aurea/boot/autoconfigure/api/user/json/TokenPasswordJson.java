package com.aurea.boot.autoconfigure.api.user.json;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenPasswordJson {
    
    private String token;
    private String password;
}
