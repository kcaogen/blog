package com.caogen.blog.service.impl;

import com.caogen.blog.dao.AdminBlogDao;
import com.caogen.blog.dto.Page;
import com.caogen.blog.entity.Blog;
import com.caogen.blog.entity.BlogTag;
import com.caogen.blog.entity.BlogType;
import com.caogen.blog.enums.PageEnum;
import com.caogen.blog.service.AdminService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {

    @Resource
    private AdminBlogDao adminBlogDao;

    @Value("${qiniuyun.url}")
    private String url;

    @Override
    public Page getBlogList(int pageNum, String name) {
        name = name.trim();
        long count = adminBlogDao.getBlogCount(name);
        Page page = new Page(PageEnum.PageSize.getPageSize(), count,pageNum);
        List<Blog> blogList = adminBlogDao.getBlog(page.getOffSet(), page.getPageSize(), name);
        for (int i = 0; i < blogList.size(); i++) {
            blogList.get(i).setBlogImg(url + blogList.get(i).getBlogImg());
        }
        page.setResult(blogList);
        return page;
    }

    /**
     * 插入博客
     * @param blog
     * @param blogTag
     * @return
     */
    @Override
    public int insertBlog(Blog blog, String blogTag) {
        adminBlogDao.insertBlog(blog);

        String[] tags = blogTag.split(",");
        List<HashMap<String, Integer>> tagList = new ArrayList<>();
        HashMap<String, Integer> map;
        for(int i = 0; i < tags.length; i++){
            map = new HashMap<>();
            map.put("blogId", blog.getBlogId());
            map.put("tagId", Integer.parseInt(tags[i]));
            tagList.add(map);
        }

        adminBlogDao.insertBlogTagByBlogId(tagList);

        return blog.getBlogId();
    }

    /**
     * 删除博客
     * @param blogId
     */
    @Override
    public void delBlog(long blogId) {
        adminBlogDao.delBlog(blogId);
        adminBlogDao.delBlogTag(blogId);
    }

    /**
     * 获取博客信息
     * @param blogId
     * @return
     */
    @Override
    public HashMap<String, Object> getBlog(long blogId) {
        HashMap<String, Object> map = new HashMap<>();
        Blog blog = adminBlogDao.getBlogById(blogId);
        List<BlogTag> tagList = adminBlogDao.getBlogTagById(blogId);
        map.put("blog", blog);
        map.put("tagList", tagList);
        return map;
    }

    /**
     * 修改博客
     * @param blog
     * @param blogTag
     * @return
     */
    @Override
    public void updateBlog(Blog blog, String blogTag) {
        adminBlogDao.updateBlog(blog);
        adminBlogDao.delBlogTag(blog.getBlogId());
        String[] tags = blogTag.split(",");
        List<HashMap<String, Integer>> tagList = new ArrayList<>();
        HashMap<String, Integer> map;
        for(int i = 0; i < tags.length; i++){
            map = new HashMap<>();
            map.put("blogId", blog.getBlogId());
            map.put("tagId", Integer.parseInt(tags[i]));
            tagList.add(map);
        }

        adminBlogDao.insertBlogTagByBlogId(tagList);

    }

    @Override
    public Page getBlogTypeList(int pageNum) {
        long count = adminBlogDao.getBlogTypeCount();
        Page page = new Page(PageEnum.PageSize.getPageSize(), count,pageNum);
        List<BlogType> blogTypeList = adminBlogDao.getBlogType(page.getOffSet(), page.getPageSize());
        page.setResult(blogTypeList);
        return page;
    }

    @Override
    public void addBlogType(String typeName) {
        adminBlogDao.insertBlogType(typeName);
    }

    @Override
    public Page getBlogTagList(int pageNum) {
        long count = adminBlogDao.getBlogTagCount();
        Page page = new Page(PageEnum.PageSize.getPageSize(), count,pageNum);
        List<BlogTag> blogTagList = adminBlogDao.getBlogTag(page.getOffSet(), page.getPageSize());
        page.setResult(blogTagList);
        return page;
    }

    @Override
    public void addBlogTag(String tagName) {
        adminBlogDao.insertBlogTag(tagName);
    }

    @Override
    public void updateBlogImg(String blogImg, long blogId){
        adminBlogDao.updateBlogImg(blogImg, blogId);
    }

}
