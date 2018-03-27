package com.aurea.kayako.api.config;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class WebConfigTest {

    @Test
    public void theClassShouldHaveAnEmptyConstructorAndBeInstantiable() throws Exception {
        assertThat(new WebConfig(), notNullValue());
    }

}
