package com.aurea.boot.autoconfigure.api;

import static org.junit.Assert.assertEquals;

import com.aurea.boot.autoconfigure.api.json.ServerResponseJson;
import org.junit.Test;

public class ApiBaseEndpointTest {

    private static final String ERROR_OCCURRED = "Error occurred";
    private static final String ERROR_OCCURRED_MESSAGE = "Error occurred message";

    @Test
    public void testHandleBadRequestException() {
        ServerResponseJson serverResponseJson = new ApiBaseEndpoint()
                .buildServerResponse(ERROR_OCCURRED, ERROR_OCCURRED_MESSAGE);
        assertEquals(serverResponseJson.getError(), ERROR_OCCURRED);
        assertEquals(serverResponseJson.getMessage(), ERROR_OCCURRED_MESSAGE);
    }

}
