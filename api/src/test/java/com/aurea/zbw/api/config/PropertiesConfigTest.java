package com.aurea.zbw.api.config;

import com.aurea.zbw.api.config.PropertiesConfig;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class PropertiesConfigTest {

    @Test
    public void theClassShouldHaveAnEmptyConstructorAndBeInstantiable() throws Exception {
        assertThat(new PropertiesConfig(), notNullValue());
    }

}
