package com.caogen.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Controller
@RequestMapping(value = "/show")
public class ShowController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 博客访问量可视化
     * @return
     */
    @GetMapping(value = "/blog")
    public String blogAccessShow() {
        try {

        }catch (Exception e) {
            logger.error("blogAccessShow:" + e);
            e.printStackTrace();
        }finally {
            return "/show/blogAccessShow";
        }
    }

}
