package com.caogen.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping(value = "/")
    public String goIndex() {
        return "index";
    }

    @GetMapping(value = "/404")
    public String go404() {
        return "error/404";
    }

}
