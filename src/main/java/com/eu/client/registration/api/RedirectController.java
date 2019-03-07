package com.eu.client.registration.api;

import lombok.SneakyThrows;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;

@Controller
public class RedirectController {

    @SneakyThrows
    @RequestMapping("/")
    public void handleFoo(HttpServletResponse response) {
        response.sendRedirect("swagger-ui.html");
    }

}