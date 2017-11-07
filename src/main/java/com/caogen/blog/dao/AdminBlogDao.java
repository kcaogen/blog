package com.caogen.blog.dao;

import com.caogen.blog.entity.Blog;
import com.caogen.blog.entity.BlogTag;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface AdminBlogDao {

    long getBlogCount(@Param("name") String name);

    List<Blog> getBlog(@Param("offset") int offset, @Param("pageSize") int pageSize, @Param("name") String name);

    /**
     * 插入博客
     * @param blog
     * @param blog
     * @return
     */
    int insertBlog(Blog blog);

    /**
     * 插入博客标签
     * @param tagList
     */
    void insertBlogTag(List<HashMap<String, Integer>> tagList);

    /**
     * 删除博客
     * @param blogId
     */
    void delBlog(@Param("blogId") long blogId);

    /**
     * 删除博客标签
     * @param blogId
     */
    void delBlogTag(@Param("blogId") long blogId);

    /**
     * 获取博客信息
     * @param blogId
     * @return
     */
    Blog getBlogById(@Param("blogId") long blogId);

    /**
     * 获取博客标签
     * @param blogId
     * @return
     */
    List<BlogTag> getBlogTagById(@Param("blogId") long blogId);

    /**
     * 修改博客
     * @param blog
     */
    void updateBlog(Blog blog);
}
