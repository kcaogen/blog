package com.caogen.blog.dao;

import com.caogen.blog.entity.Blog;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminBlogDao {

    long getBlogCount(@Param("name") String name);

    List<Blog> getBlog(@Param("offset") int offset, @Param("pageSize") int pageSize, @Param("name") String name);
}
