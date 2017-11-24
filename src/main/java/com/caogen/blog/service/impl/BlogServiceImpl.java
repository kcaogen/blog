package com.caogen.blog.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import com.caogen.blog.entity.Tag;
import com.caogen.blog.service.BlogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.caogen.blog.dao.BlogDao;
import com.caogen.blog.entity.Blog;
import com.caogen.blog.entity.Type;

@Service
@Transactional
public class BlogServiceImpl implements BlogService {

	@Resource
	private BlogDao blogDao;


	/**
	 * 获取所有博客信息
	 * @return
	 */
	@Override
	public List<Blog> getAllBlog() {
		return blogDao.getAllBlog();
	}

	/**
	 * 获取所有博客类型
	 * @return
	 */
	@Override
	public List<Type> getAllBlogType() {
		return blogDao.getAllBlogType();
	}

	/**
	 * 获取所有博客标签
	 * @return
	 */
	@Override
	public List<Tag> getAllBlogTag() {
		return blogDao.getAllBlogTag();
	}

	/**
	 * 通过blogId获取博客信息
	 * @param blogId
	 * @return
	 */
	@Override
	public Blog getBlogInfoById(int blogId) {
		return blogDao.getBlogInfoById(blogId);
	}

	/**
	 * 通过blogId获取博客标签
	 * @param blogId
	 * @return
	 */
	@Override
	public List<String> getTagListById(int blogId) {
		return blogDao.getTagListById(blogId);
	}

	/**
	 * 获取所有博客Id
	 * @return
	 */
	@Override
	public List<String> getAllBlogId(String blogType) {
		return blogDao.getAllBlogId(blogType);
	}

	/**
	 * 获取最新博客
	 * @return
	 */
	@Override
	public List<Blog> getNewBlog() {
		return blogDao.getNewBlog();
	}

	@Override
	public void updateBlogNum(List<HashMap<String, Object>> list) {
		blogDao.updateBlogNum(list);
	}

	@Override
	public List<HashMap<String, Object>> getTagList() {
		return blogDao.getTagList();
	}

}
