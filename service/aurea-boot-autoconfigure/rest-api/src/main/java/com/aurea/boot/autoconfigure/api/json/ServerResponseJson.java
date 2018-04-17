package com.aurea.boot.autoconfigure.api.json;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ServerResponseJson {

    private final String error;
    private final String message;

}
