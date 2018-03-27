package com.aurea.zbw.api.config;

import com.aurea.zbw.api.config.WebConfig;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class WebConfigTest {

    @Test
    public void theClassShouldHaveAnEmptyConstructorAndBeInstantiable() throws Exception {
        assertThat(new WebConfig(), notNullValue());
    }

}
