package com.caogen.blog.controller;

import com.caogen.blog.dto.Page;
import com.caogen.blog.service.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private AdminService adminService;

    @GetMapping(value = "")
    public String goIndex() {
        return "admin/index";
    }

    @GetMapping(value = "/blogList")
    public String goBlogList(@RequestParam(value="pageNum",defaultValue="1",required=false)int pageNum,
                             @RequestParam(value="blogCondition",defaultValue="",required=false)String blogCondition,
                             Model model) {
       try {
           Page page = adminService.getBlogList(pageNum, blogCondition);
           model.addAttribute("page", page);
           model.addAttribute("blogCondition", blogCondition);
       }catch (Exception e) {
           logger.error("goBlogList: " + e);
           e.printStackTrace();
       }finally {
           return "admin/blogList";
       }
    }

}
