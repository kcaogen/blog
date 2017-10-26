package com.caogen.blog.dao;

import java.util.HashMap;
import java.util.List;

import com.caogen.blog.entity.Blog;
import com.caogen.blog.entity.BlogTag;
import com.caogen.blog.entity.BlogType;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogDao {
	
	/**
	 * 获取所有博客信息
	 * @return
	 */
	public List<Blog> getAllBlog();

	/**
	 * 获取所有博客类型
	 * @return
	 */
	public List<BlogType> getAllBlogType();
	
	/**
	 * 获取所有博客标签
	 * @return
	 */
	public List<BlogTag> getAllBlogTag();
	
	/**
	 * 插入博客
	 * @param blog
	 * @param blog
	 * @return
	 */
	public int insertBlog(Blog blog);
	
	/**
	 * 插入博客标签
	 * @param tagList
	 */
	public void insertBlogTag(List<HashMap<String, Integer>> tagList);
	
	/**
	 * 通过blogId获取博客信息
	 * @param blogId
	 * @return
	 */
	public Blog getBlogInfoById(int blogId);
	
	/**
	 * 通过blogId获取博客标签
	 * @param blogId
	 * @return
	 */
	public List<String> getTagListById(int blogId);
	
	/**
	 * 获取所有博客Id
	 * @return
	 */
	public List<String> getAllBlogId(String blogType);
	
	/**
	 * 获取最新博客
	 * @return
	 */
	public List<Blog> getNewBlog();
	
	/**
	 * 修改博客访问量
	 * @param list
	 */
	public void updateBlogNum(List<HashMap<String, Object>> list);
	
	/**
	 * 获取博客标签
	 * @return
	 */
	public List<HashMap<String, Object>> getTagList();
}
