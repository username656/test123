package com.aurea.zbw.api.controllers;

import com.google.common.annotations.VisibleForTesting;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    public static final String ENDPOINT = "/";

    @VisibleForTesting
    static final String SWAGGER_UI_REDIRECT = "redirect:swagger-ui.html";

    @RequestMapping(value = ENDPOINT)
    final String doGet() {
        return SWAGGER_UI_REDIRECT;
    }

}

