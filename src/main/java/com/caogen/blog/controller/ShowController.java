package com.caogen.blog.controller;

import com.caogen.blog.service.ShowService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.HashMap;


@Controller
@RequestMapping(value = "/show")
public class ShowController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private ShowService showService;

    /**
     * 博客访问量可视化
     * @return
     */
    @GetMapping(value = "/blog")
    public String blogAccessShow(Model model) {
        try {
            HashMap<String, Object> cityList = showService.getAllCity();
            ObjectMapper mapper = new ObjectMapper();
            model.addAttribute("cityListJson",mapper.writeValueAsString(cityList.get("cityList")));
            model.addAttribute("geoCoordMap",mapper.writeValueAsString(cityList.get("cityMap")));
        }catch (Exception e) {
            logger.error("blogAccessShow:" + e);
            e.printStackTrace();
        }finally {
            return "show/blogAccessShow";
        }
    }

}
