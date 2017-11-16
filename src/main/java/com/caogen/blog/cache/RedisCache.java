package com.caogen.blog.cache;

import com.caogen.blog.entity.Blog;
import com.caogen.blog.entity.BlogTag;
import com.caogen.blog.entity.BlogType;
import com.caogen.blog.enums.RedisKeyEnum;
import com.caogen.blog.service.BlogService;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Tuple;


import javax.annotation.Resource;
import java.util.*;


@Component
public class RedisCache{

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private JedisPool jedisPool;

    @Resource
    private BlogService blogService;

    @Value("${qiniuyun.url}")
    private String url;

    private final int pageCount = 5;

    /**
     * 释放redis连接
     * @param jedis
     */
    public void jedisClose(final Jedis jedis) {
        if(jedis != null)jedis.close();
    }

    /**
     * 获取博客访问量
     * @return
     */
    public long getBlogNum() {
        String blogViewsKey = RedisKeyEnum.BLOGVIEWS.toString();
        String blogScoreKey = RedisKeyEnum.BLOGSCORE.toString();
        Jedis jedis = jedisPool.getResource();
        long blogNum = 0;
        try {
            blogNum = jedis.incr(blogViewsKey);
            Set<Tuple> blogSet = jedis.zrangeWithScores(blogScoreKey, 0, -1);

            if(blogSet == null || blogSet.isEmpty()){

                List<String> allBlogId = blogService.getAllBlogId(null);
                if(allBlogId == null || allBlogId.isEmpty())return blogNum;
                int size = allBlogId.size();
                int num = 0;
                for(int i = 0; i < size; i++){
                    Blog blog = blogService.getBlogInfoById(Integer.parseInt(allBlogId.get(i)));
                    num = blog.getBrowse();
                    jedis.zadd(blogScoreKey, num, allBlogId.get(i));
                    blogNum += num;
                }

            }else {
                for(Tuple tuple: blogSet){
                    blogNum += (int)tuple.getScore();
                }
            }

        }catch (Exception e) {
            logger.error("getBlogNum:" + e);
            e.printStackTrace();
        }finally {
            jedisClose(jedis);
            return blogNum;
        }

    }

    /**
     * 获取博客类型
     * @return
     */
    public List<BlogType> getBlogType(){
        String blogTypeKey = RedisKeyEnum.BLOGTYPE.toString();
        Jedis jedis = jedisPool.getResource();
        ObjectMapper mapper = new ObjectMapper();
        List<BlogType> blogTypeList = null;
        try {
            String blogTypeJson = jedis.get(blogTypeKey);

            if(!StringUtils.isEmpty(blogTypeJson)){
                JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, BlogType.class);
                blogTypeList = mapper.readValue(blogTypeJson, javaType);
            }else {
                blogTypeList = blogService.getAllBlogType();
                if(blogTypeList != null && !blogTypeList.isEmpty()){
                    jedis.set(blogTypeKey, mapper.writeValueAsString(blogTypeList));
                }
            }

        }catch (Exception e) {
            logger.error("getBlogType:" + e);
            e.printStackTrace();
        }finally {
            jedisClose(jedis);
            return blogTypeList;
        }

    }

    /**
     * 获取博客标签
     * @return
     */
    public List<BlogTag> getBlogTag(){
        String blogTagKey = RedisKeyEnum.BLOGTAG.toString();
        Jedis jedis = jedisPool.getResource();
        ObjectMapper mapper = new ObjectMapper();
        List<BlogTag> blogTagList = null;
        try {
            String blogTagJson = jedis.get(blogTagKey);
            if(!StringUtils.isEmpty(blogTagJson)){
                JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, BlogTag.class);
                blogTagList = mapper.readValue(blogTagJson, javaType);
            }else {
                blogTagList = blogService.getAllBlogTag();
                if(blogTagList != null && !blogTagList.isEmpty()){
                    jedis.set(blogTagKey, mapper.writeValueAsString(blogTagList));
                }
            }
        }catch (Exception e) {
            logger.error("getBlogTag:" + e);
            e.printStackTrace();
        }finally {
            jedisClose(jedis);
            return blogTagList;
        }

    }

    /**
     * 通过博客主键ID获取博客详情
     * @param blogId
     * @return
     */
    public HashMap<String, Object> getBlogInfo(String blogId){
        if(Integer.parseInt(blogId) <= 0) return null;
        String blogInfoKey = RedisKeyEnum.BLOGINFO.toString();
        String blogTagGroupKey = RedisKeyEnum.BLOGTAGGROUP.toString();
        String blogScoreKey = RedisKeyEnum.BLOGSCORE.toString();
        Jedis jedis = jedisPool.getResource();
        ObjectMapper mapper = new ObjectMapper();
        HashMap<String, Object> param = new HashMap<>();
        try {
            String blogInfo = jedis.hget(blogInfoKey, blogId);
            Blog blog;
            if(StringUtils.isEmpty(blogInfo)){
                blog = blogService.getBlogInfoById(Integer.parseInt(blogId));
                if(blog != null){
                    blog.setBlogImg(url + blog.getBlogImg());
                    jedis.hset(blogInfoKey, blogId, mapper.writeValueAsString(blog));
                }
            }else{
                blog = mapper.readValue(blogInfo, Blog.class);
            }

            String tagInfo = jedis.hget(blogTagGroupKey, blogId);
            List<String> tagList;
            if(StringUtils.isEmpty(tagInfo)){
                tagList = blogService.getTagListById(Integer.parseInt(blogId));
                jedis.hset(blogTagGroupKey, blogId, mapper.writeValueAsString(tagList));
            }else{
                JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, String.class);
                tagList = mapper.readValue(tagInfo, javaType);
            }

            if(blog != null)blog.setBrowse((int) new Double(jedis.zscore(blogScoreKey, blogId)==null ? 0 : jedis.zscore(blogScoreKey, blogId)).longValue());
            param.put("blog", blog);
            param.put("tagList", tagList);
        }catch (Exception e) {
            logger.error("getBlogInfo:" + e);
            e.printStackTrace();
        }finally {
            jedisClose(jedis);
            return param;
        }

    }

    /**
     * 批量获取博客详情
     * @param blogIdList
     * @return
     */
    public List<HashMap<String, Object>> getBlogInfoList(List<String> blogIdList){
        String blogInfoKey = RedisKeyEnum.BLOGINFO.toString();
        String blogTagGroupKey = RedisKeyEnum.BLOGTAGGROUP.toString();
        String blogScoreKey = RedisKeyEnum.BLOGSCORE.toString();
        Jedis jedis = jedisPool.getResource();
        ObjectMapper mapper = new ObjectMapper();
        List<HashMap<String, Object>> list = new ArrayList<>();
        try {
            String[] blogIdArray = (String[])blogIdList.toArray(new String[blogIdList.size()]);
            List<String> blogList = jedis.hmget(blogInfoKey, blogIdArray);
            List<String> blogTagList = jedis.hmget(blogTagGroupKey, blogIdArray);

            int size = blogIdList.size();
            HashMap<String, Object> param;
            String blogId;
            for(int i = 0; i < size; i++){
                blogId = blogIdList.get(i);
                param = new HashMap<>();

                String blogInfo = blogList.get(i);
                Blog blog;
                if(StringUtils.isEmpty(blogInfo)){
                    blog = blogService.getBlogInfoById(Integer.parseInt(blogId));
                    if(blog != null)blog.setBlogImg(url + blog.getBlogImg());
                    jedis.hset(blogInfoKey, blogId, mapper.writeValueAsString(blog));
                }else{
                    blog = mapper.readValue(blogInfo, Blog.class);
                }

                String tagInfo = blogTagList.get(i);
                List<String> tagList;
                if(StringUtils.isEmpty(tagInfo)){
                    tagList = blogService.getTagListById(Integer.parseInt(blogId));
                    jedis.hset(blogTagGroupKey, blogId, mapper.writeValueAsString(tagList));
                }else{
                    JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, String.class);
                    tagList = mapper.readValue(tagInfo, javaType);
                }

                if(blog != null)blog.setBrowse((int) new Double(jedis.zscore(blogScoreKey, blogId)==null ? 0 : jedis.zscore(blogScoreKey, blogId)).longValue());
                param.put("blog", blog);
                param.put("tagList", tagList);
                list.add(param);
            }
        }catch (Exception e) {
            logger.error("getBlogInfoList:" + e);
            e.printStackTrace();
        }finally {
            jedisClose(jedis);
            return list;
        }

    }

    /**
     * 获取博客详情页访问量
     * @param blogId
     * @return
     */
    public long getBlogNumById(String blogId){
        if(Integer.parseInt(blogId) <= 0) return 0;
        Jedis jedis = jedisPool.getResource();
        String blogScoreKey = RedisKeyEnum.BLOGSCORE.toString();
        Double blogNum = 0.0;
        try {
            blogNum = jedis.zincrby(blogScoreKey, 1, blogId);
        }catch (Exception e) {
            logger.error("getBlogNumById:" + e);
            e.printStackTrace();
        }finally {
            jedisClose(jedis);
            return new Double(blogNum).longValue();
        }

    }

    /**
     * 获取博客列表的页数
     * @return
     */
    public long getBlogPage(String blogType){
        String blogIdListKey = RedisKeyEnum.BLOGIDLIST.toString();
        Jedis jedis = jedisPool.getResource();
        long page = 1;
        try {
            String key = blogIdListKey;
            if(!StringUtils.isEmpty(blogType)){
                key = blogType;
            }

            long count = jedis.llen(key);
            page = (count-1) / pageCount + 1;//这样就计算好了页码数量，逢1进1
        }catch (Exception e) {
            logger.error("getBlogPage:" + e);
            e.printStackTrace();
        }finally {
            jedisClose(jedis);
            return page;
        }

    }

    /**
     * 通过页码获取博客信息
     * @param page
     * @return
     */
    public List<String> getBlogIdList(int page, String blogType){
        String blogIdListKey = RedisKeyEnum.BLOGIDLIST.toString();
        Jedis jedis = jedisPool.getResource();
        List<String> blogIdList = null;
        try {
            String key = blogIdListKey;
            if(!StringUtils.isEmpty(blogType)){
                key = blogType;
            }
            int start = (page - 1) * pageCount;
            int end = page * pageCount - 1;

            blogIdList = jedis.lrange(key, start, end);
            if(blogIdList == null || blogIdList.isEmpty()){
                List<String> allBlogId = blogService.getAllBlogId(blogType);
                if(allBlogId == null || allBlogId.isEmpty())return new ArrayList<>();
                String[] allBlogArray = (String[])allBlogId.toArray(new String[allBlogId.size()]);
                jedis.rpush(key, allBlogArray);

                blogIdList = jedis.lrange(key, start, end);
            }
        }catch (Exception e) {
            logger.error("getBlogIdList:" + e);
            e.printStackTrace();
        }finally {
            jedisClose(jedis);
            return blogIdList;
        }

    }

    /**
     * 得到最新博客
     * @return
     */
    public List<Blog> getNewBlog(){
        String blogListByNew = RedisKeyEnum.BLOGLISTBYNEW.toString();
        Jedis jedis = jedisPool.getResource();
        ObjectMapper mapper = new ObjectMapper();
        List<Blog> blogList = null;
        try {
            String blogJson = jedis.get(blogListByNew);

            if(StringUtils.isEmpty(blogJson)){
                blogList = blogService.getNewBlog();
                jedis.set(blogListByNew, mapper.writeValueAsString(blogList));
            }else{
                JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, Blog.class);
                blogList = mapper.readValue(blogJson, javaType);
            }
        }catch (Exception e) {
            logger.error("getNewBlog:" + e);
            e.printStackTrace();
        }finally {
            jedisClose(jedis);
            return blogList;
        }

    }

    /**
     * 获取阅读最多的博客
     * @return
     */
    public List<Blog> getBlogByRead(){
        String blogScoreKey = RedisKeyEnum.BLOGSCORE.toString();
        String blogInfoKey = RedisKeyEnum.BLOGINFO.toString();
        Jedis jedis = jedisPool.getResource();
        ObjectMapper mapper = new ObjectMapper();
        List<Blog> blogList = new ArrayList<>();
        try {
            Set<Tuple> blogSet = jedis.zrevrangeWithScores(blogScoreKey, 0, 4);
            Blog blog;
            for(Tuple tuple: blogSet){
                String blogId = tuple.getElement();
                String blogInfo = jedis.hget(blogInfoKey, blogId);
                if(StringUtils.isEmpty(blogInfo)){
                    blog = blogService.getBlogInfoById(Integer.parseInt(blogId));
                    if(blog != null)blog.setBlogImg(url + blog.getBlogImg());
                    jedis.hset(blogInfoKey, blogId, mapper.writeValueAsString(blog));
                }else{
                    blog = mapper.readValue(blogInfo, Blog.class);
                }
                if(blog == null)blog = new Blog();
                blog.setBrowse((int)tuple.getScore());
                blog.setContent(null);
                blogList.add(blog);
            }
        }catch (Exception e) {
            logger.error("getBlogByRead:" + e);
            e.printStackTrace();
        }finally {
            jedisClose(jedis);
            return blogList;
        }

    }

    /**
     * 新增博客之后有些缓存需要清除以便重新生成
     */
    public void delCacheByAddBlog(String blogType){
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.del(blogType);
            jedis.del(RedisKeyEnum.BLOGIDLIST.toString());
            jedis.del(RedisKeyEnum.BLOGLISTBYNEW.toString());
        }catch (Exception e) {
            logger.error("delCacheByAddBlog:" + e);
            e.printStackTrace();
        }finally {
            jedisClose(jedis);
        }

    }

    /**
     * 修改博客之后有些缓存需要清除以便重新生成
     */
    public void delCacheByUpdateBlog(String blogType, String blogId) {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.del(blogType);
            jedis.del(RedisKeyEnum.BLOGIDLIST.toString());
            jedis.del(RedisKeyEnum.BLOGLISTBYNEW.toString());
            jedis.hdel(RedisKeyEnum.BLOGINFO.toString(), blogId);
            jedis.hdel(RedisKeyEnum.BLOGTAGGROUP.toString(), blogId);
        }catch (Exception e) {
            logger.error("delCacheByUpdateBlog:" + e);
            e.printStackTrace();
        }finally {
            jedisClose(jedis);
        }
    }

    /**
     * 修改博客图片之后有些缓存需要清除以便重新生成
     */
    public void delCacheByUpdateBlogImg(String blogId) {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.hdel(RedisKeyEnum.BLOGINFO.toString(), blogId);
        }catch (Exception e) {
            logger.error("delCacheByUpdateBlog:" + e);
            e.printStackTrace();
        }finally {
            jedisClose(jedis);
        }
    }

    /**
     * 新增博客类型之后有些缓存需要清除以便重新生成
     */
    public void delCacheByAddBlogType() {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.del(RedisKeyEnum.BLOGTYPE.toString());
        }catch (Exception e) {
            logger.error("delCacheByAddBlogType:" + e);
            e.printStackTrace();
        }finally {
            jedisClose(jedis);
        }
    }

    /**
     * 新增博客标签之后有些缓存需要清除以便重新生成
     */
    public void delCacheByAddBlogTag() {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.del(RedisKeyEnum.BLOGTAG.toString());
        }catch (Exception e) {
            logger.error("delCacheByAddBlogTag:" + e);
            e.printStackTrace();
        }finally {
            jedisClose(jedis);
        }
    }

    /**
     * 更改博客访问量
     */
    @Scheduled(cron="0 0/10 * * * ?")
    public void updateBlogNum(){
        Jedis jedis = jedisPool.getResource();
        try {
            List<HashMap<String, Object>> list = new ArrayList<>();
            Set<Tuple> blogSet = jedis.zrangeWithScores(RedisKeyEnum.BLOGSCORE.toString(), 0, -1);
            HashMap<String, Object> map;
            for(Tuple tuple: blogSet){
                map = new HashMap<>();
                map.put("blogId", Integer.valueOf(tuple.getElement()));
                map.put("browse", (int)tuple.getScore());
                list.add(map);
            }
            blogService.updateBlogNum(list);
        }catch (Exception e) {
            logger.error("updateBlogNum:" + e);
            e.printStackTrace();
        }finally {
            jedisClose(jedis);
        }

    }

}
