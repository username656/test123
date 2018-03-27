package com.aurea.kayako.api.controllers;

import static com.aurea.kayako.api.controllers.HomeController.SWAGGER_UI_REDIRECT;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class HomeControllerTest {

    @Test
    public void doGetReturnsTheRedirectToTheExpectedPlace() throws Exception {
        HomeController controller = new HomeController();
        assertThat(controller.doGet(), is(SWAGGER_UI_REDIRECT));
    }

}
