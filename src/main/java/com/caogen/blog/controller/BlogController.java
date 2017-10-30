package com.caogen.blog.controller;

import com.caogen.blog.cache.RedisCache;
import com.caogen.blog.dto.BlogResult;
import com.caogen.blog.entity.Blog;
import com.caogen.blog.entity.BlogTag;
import com.caogen.blog.entity.BlogType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
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

    /**
     * 跳转到博客列表
     * @param model
     * @return
     */
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
            return "blog/blogList";
        }
    }

    /**
     * 跳转到博客列表（带类型）
     * @param model
     * @return
     */
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
            return "blog/blogList";
        }
    }

    /**
     * 获取博客列表数据
     * @param page
     * @param blogType
     * @return
     */
    @PostMapping(value = "/blogList", produces = { "application/json;charset=UTF-8" })
    @ResponseBody
    public BlogResult getBlogList(@RequestParam(value="page",defaultValue="1",required=false)int page,
                                  @RequestParam(value="blogType",defaultValue="",required=false) String blogType) {
        BlogResult result = null;
        try {
            List<String> blogIdList = redisCache.getBlogIdList(page, blogType);
            if(blogIdList == null || blogIdList.isEmpty()){
                result = new BlogResult(false, "没有博客数据！");
                return result;
            }

            List<HashMap<String, Object>> blogInfoList = redisCache.getBlogInfoList(blogIdList);
            result = new BlogResult(true, blogInfoList);
        }catch (Exception e) {
            result = new BlogResult(false, e.getMessage());
            logger.error("getBlogList:" + e);
            e.printStackTrace();
        }finally {
            return  result;
        }

    }

    /**
     * 获取博客详情
     * @param model
     * @param blogId
     * @return
     */
    @GetMapping(value = "/blogInfo/{blogId}")
    public String goBlogInfo(Model model, @PathVariable("blogId") String blogId) {
        try {
            long blogNum = redisCache.getBlogNumById(blogId);
            HashMap<String, Object> blogInfo = redisCache.getBlogInfo(blogId);
            model.addAttribute("blogInfo", blogInfo);
            model.addAttribute("blogNum", blogNum);
            rightContent(model);
        } catch (Exception e) {
            logger.error("goBlogInfo:" + e);
        }finally {
            return "blog/blogInfo";
        }

    }

}
