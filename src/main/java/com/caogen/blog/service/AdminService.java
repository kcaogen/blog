package com.caogen.blog.service;

import com.caogen.blog.dto.Page;
import com.caogen.blog.entity.Blog;

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

}
