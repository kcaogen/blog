package com.caogen.blog.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import com.caogen.blog.service.BlogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.caogen.blog.dao.BlogDao;
import com.caogen.blog.entity.Blog;
import com.caogen.blog.entity.BlogTag;
import com.caogen.blog.entity.BlogType;

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
	public List<BlogType> getAllBlogType() {
		return blogDao.getAllBlogType();
	}

	/**
	 * 获取所有博客标签
	 * @return
	 */
	@Override
	public List<BlogTag> getAllBlogTag() {
		return blogDao.getAllBlogTag();
	}

	/**
	 * 插入博客
	 * @param blog
	 * @param blogTag
	 * @return
	 */
	@Override
	public int insertBlog(Blog blog, String blogTag) {
		blogDao.insertBlog(blog);

		String[] tags = blogTag.split(",");
		List<HashMap<String, Integer>> tagList = new ArrayList<>();
		HashMap<String, Integer> map;
		for(int i = 0; i < tags.length; i++){
			map = new HashMap<>();
			map.put("blogId", blog.getBlogId());
			map.put("tagId", Integer.parseInt(tags[i]));
			tagList.add(map);
		}

		blogDao.insertBlogTag(tagList);

		return blog.getBlogId();
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
