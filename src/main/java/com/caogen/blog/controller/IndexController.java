package com.caogen.blog.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping(value = "/")
    public String goIndex() {
        return "index";
    }

    @GetMapping(value = "/login")
    public String goLogin() {
        return "admin/login";
    }

    @GetMapping(value = "/error/404")
    public String go404() {
        return "error/404";
    }

    @GetMapping(value = "/error/500")
    public String go500() {
        return "error/500";
    }

}
