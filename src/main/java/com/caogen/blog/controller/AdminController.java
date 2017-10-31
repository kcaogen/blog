package com.caogen.blog.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping(value = "")
    public String goIndex() {
        return "admin/index";
    }

}
