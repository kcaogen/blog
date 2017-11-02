package com.caogen.blog.service;

import com.caogen.blog.dto.Page;

public interface AdminService {

    Page getBlogList(int pageNum, String name);

}
