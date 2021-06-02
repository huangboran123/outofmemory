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
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    public BlogPageUser getUserMoreById(Integer userId, Integer id) {
        return blogMapper.getUserMoreById(userId,id);
    }

    @Override
    public List<Blog> getSideRecommandblogs() {
        return blogMapper.getSideRecommandblogs();
    }

    @Override
    public List<Blog> getIndexRecommandblogs(Integer page, Integer pageSize,Integer tag,String keyword) {


       /* 博客查询redis优化*/
        List<Integer> blogids=getRecommandBlogIds(page,pageSize,tag,keyword);
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
    public List<Integer> getRecommandBlogIds(Integer page, Integer pageSize,Integer tag,String keyword) {
        return blogMapper.getRecommandBlogIds((page-1)*pageSize,pageSize,tag,keyword);
    }

    @Override
    public int likesblogbyid(Integer blogId) {
        return blogMapper.likesblogbyid(blogId);
    }

    @Override
    public Integer getTotalcountbyclassanduser(Integer classId, Integer userId,Integer tag,String keyword) {
        return blogMapper.getTotalcountbyclassanduser(classId,userId,tag,keyword);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveredisViews(Map<Object, Object> viewmap) {
        Long k= 0L;

        for(Map.Entry<Object, Object> entry:viewmap.entrySet()){

            String id= (String) entry.getKey();
            Integer views= (Integer) entry.getValue();
            int i=blogMapper.updateblogviewByid(id,views);
            k++;
        }
        return k;
    }

    @Override
    public List<Blog> getAllmyPagingblogs(Integer classification, Integer page, Integer pageSize, Integer userId, Integer sort,Integer tag) {
        /* 博客查询redis优化*/
        List<Integer> blogids=getmyPagingBlogIds(classification,page,pageSize, userId,sort,tag);
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

    private List<Integer> getmyPagingBlogIds(Integer classification, Integer page, Integer pageSize, Integer id,Integer sort,Integer tag) {
        return blogMapper.getmyPagingBlogIds(classification,(page-1)*pageSize,pageSize,id,sort,tag);
    }
}
