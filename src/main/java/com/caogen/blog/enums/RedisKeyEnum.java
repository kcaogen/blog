package com.caogen.blog.enums;

public enum RedisKeyEnum {

    BLOGVIEWS("blogViews"),             //博客访问量
    BLOGSCORE("blogScore"),             //博客得分
    BLOGTYPE("blogType"),               //博客类型
    BLOGTAG("blogTag"),                 //博客标签
    BLOGINFO("blogInfo"),               //博客详情
    BLOGTAGGROUP("blogTagGroup"),       //博客标签根据博客ID分组
    BLOGIDLIST("blogIdList"),           //博客ID集合
    BLOGLISTBYNEW("blogListByNew");     //最新的博客集合

    private String redisKey;

    private RedisKeyEnum(String redisKey) {
        this.redisKey = redisKey;
    }

    public String getRedisKey() {
        return redisKey;
    }

    public void setRedisKey(String redisKey) {
        this.redisKey = redisKey;
    }

    @Override
    public String toString() {
       return redisKey;
    }
}
