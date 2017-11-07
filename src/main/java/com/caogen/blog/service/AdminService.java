package com.caogen.blog.service;

import com.caogen.blog.dto.Page;
import com.caogen.blog.entity.Blog;

import java.util.HashMap;

public interface AdminService {

    Page getBlogList(int pageNum, String name);

    /**
     * 插入博客
     * @param blog
     * @param blogTag
     * @return
     */
    int insertBlog(Blog blog, String blogTag);

    /**
     * 删除博客
     * @param blogId
     */
    void delBlog(long blogId);

    /**
     * 获取博客信息
     * @param blogId
     * @return
     */
    HashMap<String, Object> getBlog(long blogId);

    /**
     * 修改博客
     * @param blog
     * @param blogTag
     */
    void updateBlog(Blog blog, String blogTag);

    Page getBlogTypeList(int pageNum);

    void addBlogType(String typeName);

    Page getBlogTagList(int pageNum);

    void addBlogTag(String tagName);
}
