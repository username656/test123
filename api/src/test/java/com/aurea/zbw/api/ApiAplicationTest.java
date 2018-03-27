package com.aurea.zbw.api;

import com.aurea.zbw.api.ApiApplication;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class ApiAplicationTest {

    @Test
    public void theClassShouldHaveAnEmptyConstructorAndBeInstantiable() throws Exception {
        assertThat(new ApiApplication(), notNullValue());
    }

}
