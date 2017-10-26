package com.caogen.blog.controller;

import com.caogen.blog.cache.RedisCache;
import com.caogen.blog.entity.Blog;
import com.caogen.blog.entity.BlogTag;
import com.caogen.blog.entity.BlogType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.Resource;
import java.util.List;

@Controller
public class BlogController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private RedisCache redisCache;

    /**
     * right页面的内容
     * @param model
     */
    public void rightContent(Model model){
        List<BlogType> blogTypeList = redisCache.getBlogType();
        List<BlogTag> blogTagList = redisCache.getBlogTag();
        List<Blog> newBlogList = redisCache.getNewBlog();
        List<Blog> readBlogList = redisCache.getBlogByRead();

        model.addAttribute("newBlogList", newBlogList);
        model.addAttribute("blogTypeList", blogTypeList);
        model.addAttribute("blogTagList", blogTagList);
        model.addAttribute("readBlogList", readBlogList);
    }

    @GetMapping(value = "/blog")
    public String goBlog(Model model) {
        try {
            long blogNum = redisCache.getBlogNum();
            long page = redisCache.getBlogPage(null);

            model.addAttribute("blogNum", blogNum);
            model.addAttribute("page", page);
            rightContent(model);
        }catch (Exception e) {
            logger.error("goBlog:" + e);
            e.printStackTrace();
        }finally {
            return "/blog/blogList";
        }
    }

    @GetMapping(value = "/blog/{blogType}")
    public String goBlog(Model model,
                         @PathVariable("blogType") String blogType) {
        try {
            long blogNum = redisCache.getBlogNum();
            long page = redisCache.getBlogPage(blogType);

            model.addAttribute("blogType", blogType);
            model.addAttribute("blogNum", blogNum);
            model.addAttribute("page", page);
            rightContent(model);
        }catch (Exception e) {
            logger.error("goBlog:" + e);
            e.printStackTrace();
        }finally {
            return "/blog/blogList";
        }
    }

}
