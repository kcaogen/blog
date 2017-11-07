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
import java.util.HashMap;
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

    @GetMapping(value = "/blog")
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

    @GetMapping(value = "/blog/add")
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

    @PostMapping(value = "/delBlog", produces = { "application/json;charset=UTF-8" })
    @ResponseBody
    public BlogResult delBlog(@RequestParam(value="blogId")long blogId,
                              @RequestParam(value="blogType")String blogType) {
        BlogResult result = null;
        try {
            adminService.delBlog(blogId);
            redisCache.delCacheByAddBlog(blogType);
            result = new BlogResult(true, blogId);
        }catch (Exception e) {
            result = new BlogResult(false, e.getMessage());
            logger.error("delBlog: " + e);
            e.printStackTrace();
        }finally {
            return result;
        }
    }

    @GetMapping(value = "/blog/update/{blogId}")
    public String goUpdateBlog(Model model, @PathVariable("blogId") long blogId) {
        try {
            List<BlogType> blogTypeList = redisCache.getBlogType();
            List<BlogTag> blogTagList = redisCache.getBlogTag();
            HashMap<String, Object> blogInfo = adminService.getBlog(blogId);

            model.addAttribute("blogTypeList", blogTypeList);
            model.addAttribute("blogTagList", blogTagList);
            model.addAttribute("info", blogInfo);
        }catch (Exception e) {
            logger.error("goUpdateBlog: " + e);
            e.printStackTrace();
        }finally {
            return "admin/updateBlog";
        }
    }

    @PostMapping(value = "/updateBlog", produces = { "application/json;charset=UTF-8" })
    @ResponseBody
    public BlogResult updateBlog(Blog blog, String blogTag) {
        BlogResult result = null;
        try {
            adminService.updateBlog(blog, blogTag);
            redisCache.delCacheByUpdateBlog(blog.getBlogType(), String.valueOf(blog.getBlogId()));
            result = new BlogResult(true, blog.getBlogId());
        }catch (Exception e) {
            result = new BlogResult(false, e.getMessage());
            logger.error("updateBlog: " + e);
            e.printStackTrace();
        }finally {
            return result;
        }
    }

    @GetMapping(value = "/blogType")
    public String goBlogTypeList(@RequestParam(value="pageNum",defaultValue="1",required=false)int pageNum,
                             Model model) {
        try {
            Page page = adminService.getBlogTypeList(pageNum);
            model.addAttribute("page", page);
        }catch (Exception e) {
            logger.error("goBlogTypeList: " + e);
            e.printStackTrace();
        }finally {
            return "admin/blogTypeList";
        }
    }

    @PostMapping(value = "/addBlogType", produces = { "application/json;charset=UTF-8" })
    @ResponseBody
    public BlogResult addBlogType(String typeName) {
        BlogResult result = null;
        try {
            adminService.addBlogType(typeName);
            redisCache.delCacheByAddBlogType();
            result = new BlogResult(true, true);
        }catch (Exception e) {
            result = new BlogResult(false, e.getMessage());
            logger.error("addBlogType: " + e);
            e.printStackTrace();
        }finally {
            return result;
        }
    }

    @GetMapping(value = "/blogTag")
    public String goBlogTagList(@RequestParam(value="pageNum",defaultValue="1",required=false)int pageNum,
                                 Model model) {
        try {
            Page page = adminService.getBlogTagList(pageNum);
            model.addAttribute("page", page);
        }catch (Exception e) {
            logger.error("goBlogTagList: " + e);
            e.printStackTrace();
        }finally {
            return "admin/blogTagList";
        }
    }

    @PostMapping(value = "/addBlogTag", produces = { "application/json;charset=UTF-8" })
    @ResponseBody
    public BlogResult addBlogTag(String tagName) {
        BlogResult result = null;
        try {
            adminService.addBlogTag(tagName);
            redisCache.delCacheByAddBlogTag();
            result = new BlogResult(true, true);
        }catch (Exception e) {
            result = new BlogResult(false, e.getMessage());
            logger.error("addBlogTag: " + e);
            e.printStackTrace();
        }finally {
            return result;
        }
    }

}
