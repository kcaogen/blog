package com.caogen.blog.controller;

import com.caogen.blog.cache.RedisCache;
import com.caogen.blog.dto.BlogResult;
import com.caogen.blog.dto.Page;
import com.caogen.blog.entity.Blog;
import com.caogen.blog.entity.BlogTag;
import com.caogen.blog.entity.BlogType;
import com.caogen.blog.service.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private AdminService adminService;

    @Resource
    private RedisCache redisCache;

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

    @GetMapping(value = "/addBlog")
    public String goAddBlog(Model model) {
        try {
            List<BlogType> blogTypeList = redisCache.getBlogType();
            List<BlogTag> blogTagList = redisCache.getBlogTag();

            model.addAttribute("blogTypeList", blogTypeList);
            model.addAttribute("blogTagList", blogTagList);
        }catch (Exception e) {
            logger.error("goAddBlog: " + e);
            e.printStackTrace();
        }finally {
            return "admin/addBlog";
        }
    }

    @PostMapping(value = "/addBlog", produces = { "application/json;charset=UTF-8" })
    @ResponseBody
    public BlogResult addBlog(Blog blog, String blogTag) {
        BlogResult result = null;
        try {
            int id = adminService.insertBlog(blog, blogTag);
            redisCache.delCacheByAddBlog(blog.getBlogType());
            result = new BlogResult(true, id);
        }catch (Exception e) {
            result = new BlogResult(false, e.getMessage());
            logger.error("goAddBlog: " + e);
            e.printStackTrace();
        }finally {
            return result;
        }
    }

}
