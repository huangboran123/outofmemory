package com.love.outofmemory.service.Impl;

import com.love.outofmemory.Utills.RedisUtil;
import com.love.outofmemory.domain.Blog;
import com.love.outofmemory.domain.view.Classify;
import com.love.outofmemory.domain.view.BlogPageUser;
import com.love.outofmemory.mapper.BlogMapper;
import com.love.outofmemory.mapper.BlogTagMapper;
import com.love.outofmemory.mapper.ClassificationMapper;
import com.love.outofmemory.service.IBlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author huang
 */
@Service
public class BlogServiceImpl implements IBlogService {
    @Autowired
    private BlogMapper blogMapper;
    @Autowired
    private BlogTagMapper blogTagMapper;
    @Autowired
    private ClassificationMapper classificationMapper;
    @Autowired
    private RedisUtil redisUtil;



    @Override
    public int addBlog(Blog blog) {

        return blogMapper.addBlog(blog);

    }

    @Override
    public List<Classify> getClassifyBlogCount(Integer userId) {
        return blogMapper.getClassifyBlogCount(userId);
    }

    @Override
    public List<Blog> getblogsByuseridandclassify(Integer userId, Integer classificationId) {

        return blogMapper.getblogsByuseridandclassify(userId,classificationId);
    }

    @Override
    public List<Blog> gettopnblogsorderBydate(Integer id, Integer n) {
        return blogMapper.gettopnblogsorderBydate(id,n);
    }

    @Override
    public int removeblogByIid(Integer delblogId) {
        return blogMapper.removeblogByIid(delblogId);
    }

    @Override
    public int moveBlogtoDefaultclass(Integer id, Integer[] classDeleteId) {
        int i=0;
        for(Integer c:classDeleteId){
            if(blogMapper.moveBlogtoDefaultclass(id,c)==1){
                i++;
            }
        }

        return i;
    }

    @Override
    public int moveBlogtootherclass(Integer blogId, Integer movchoice) {
        return blogMapper.moveBlogtootherclass(blogId,movchoice);
    }

    @Override
    public Blog getblogById(Integer blogId) {
        return blogMapper.getblogById(blogId);
    }

    @Override
    public int updateBlogByid(Integer userId, Integer blogId, String title, String content, String tag, String classify,Integer isorignal) {
        Integer tagId=blogTagMapper.getTagByName(tag).getId();
        Integer classId=classificationMapper.getIdByname(userId,classify).getId();


        return blogMapper.updateblogByid(blogId,title,content,tagId,classId,isorignal);
    }

    @Override
    public BlogPageUser getUserMoreById(Integer userId) {
        return blogMapper.getUserMoreById(userId);
    }

    @Override
    public List<Blog> getRecommandblogs() {
        return blogMapper.getRecommandblogs();
    }

    @Override
    public List<Blog> getIndexRecommandblogs(Integer page, Integer pagesize) {


       /* 博客查询redis优化*/
        List<Integer> blogids=getRecommandBlogIds(page,pagesize);
        List<Blog> bloglistbetter=new ArrayList<>();

        for (Integer i:blogids)
        {
            if(redisUtil.HashGet("Views",i.toString())==null){
                Blog b=getblogById(i);
                bloglistbetter.add(b);
                redisUtil.HashPut("Views",i.toString(),b.getViews());
            }
            else{
                Blog b=getblogByIdNoviews(i);
                b.setViews((Integer)redisUtil.HashGet("Views",i.toString()));
                bloglistbetter.add(b);
            }
        }

        return bloglistbetter;
    }

    @Override
    public Blog getblogByIdNoviews(Integer blogId) {
        return blogMapper.getblogByIdNoviews(blogId);
    }

    @Override
    public List<Integer> getRecommandBlogIds(Integer page, Integer pagesize) {
        return blogMapper.getRecommandBlogIds(page-1,pagesize);
    }

    @Override
    public int likesblogbyid(Integer blogId) {
        return blogMapper.likesblogbyid(blogId);
    }
}
