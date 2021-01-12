package com.love.outofmemory.service;

import com.love.outofmemory.domain.Blog;
import com.love.outofmemory.domain.view.Classify;
import com.love.outofmemory.domain.view.BlogPageUser;

import java.util.List;

/**
 * @author huang
 */
public interface IBlogService {
    int addBlog(Blog blog);
    List<Classify> getClassifyBlogCount(Integer userId);

    List<Blog> getblogsByuseridandclassify(Integer userId, Integer classificationId);

    List<Blog> gettopnblogsorderBydate(Integer userId, Integer n);

    int removeblogByIid(Integer delblogId);

    int moveBlogtoDefaultclass(Integer id, Integer[] classDeleteId);

    int moveBlogtootherclass(Integer blogId, Integer movchoice);

    Blog getblogById(Integer blogId);

    int updateBlogByid(Integer userId, Integer blogId, String title, String content, String tag, String classify,Integer isorignal);

    BlogPageUser getUserMoreById(Integer userId);
}
