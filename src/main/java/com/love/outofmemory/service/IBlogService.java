package com.love.outofmemory.service;

import com.love.outofmemory.domain.Blog;
import com.love.outofmemory.domain.User;
import com.love.outofmemory.domain.view.Classify;
import com.love.outofmemory.domain.view.BlogPageUser;

import java.util.List;
import java.util.Map;

/**
 * @author huang
 */
public interface IBlogService {

    /*新建博客*/
    int addBlog(Blog blog);

    /*获取用户的博客分类以及对应数量*/
    List<Classify> getClassifyBlogCount(Integer userId);

   /* 获取用户特定分类的所有博客*/
    List<Blog> getblogsByuseridandclassify(Integer userId, Integer classificationId);

    /*获取用户最新发布的指定数量的博客*/
    List<Blog> gettopnblogsorderBydate(Integer userId, Integer n);

    /*根据博客ID删除博客*/
    int removeblogByIid(Integer delblogId);

    /*移动博客到默认分类*/
    int moveBlogtoDefaultclass(Integer id, Integer[] classDeleteId);

    /*移动博客到其他分类*/
    int moveBlogtootherclass(Integer blogId, Integer movchoice);

    /*根据博客id获取博客*/
    Blog getblogById(Integer blogId);

    /*根据博客ID修改博客某些内容*/
    int updateBlogByid(Integer userId, Integer blogId, String title, String content, String tag, String classify,Integer isorignal);

    /*博客页面的视图，查询博客作者的相关信息userId为博客作者，id为用户自己*/
    BlogPageUser getUserMoreById(Integer userId, Integer id);

  /*  获取推荐的博客，依据浏览量，点赞数和评论数之和*/
    List<Blog> getSideRecommandblogs();

    /*获取首页推荐博客,分页*/
    List<Blog> getIndexRecommandblogs(Integer page, Integer pageSize,Integer tag,String keyword);

    //查询博客不包含views
    Blog getblogByIdNoviews(Integer blogId);

    //获取首页博客id
    List<Integer> getRecommandBlogIds(Integer page, Integer pageSize,Integer tag,String keyword);

    /*点赞*/
    int likesblogbyid(Integer blogId);

    /*获取所有博客工具博客类别*/
    Integer getTotalcountbyclassanduser(Integer classId, Integer userId,Integer tag,String keyword);

   /* 保持浏览量一致*/
    Long saveredisViews(Map<Object, Object> viewmap);

    /*根据用户和博客类型获取博客*/
    List<Blog> getAllmyPagingblogs(Integer classification, Integer page, Integer pageSize, Integer userId, Integer sort,Integer tag);


}
