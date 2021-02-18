package com.love.outofmemory.mapper;

import com.love.outofmemory.domain.Blog;
import com.love.outofmemory.domain.view.Classify;

import com.love.outofmemory.domain.view.BlogPageUser;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author huang
 */
@Repository
public interface BlogMapper {

    @Insert("insert into o_blog values(null,#{user.id},#{publish_time},#{classification.id}," +
            "#{good_count},#{content},#{title},#{views},#{tag.id},#{comments},#{isoriginal})")
    int addBlog(Blog blog);

    /*左外连接,查询对应用户博客分类中的所有各自数量*/
    @Select("SELECT COUNT(o_blog.classification) counts ,blog_classification.id,NAME " +
            "FROM blog_classification LEFT JOIN  o_blog " +
            "ON blog_classification.id=o_blog.classification" +
            " AND blog_classification.user_id=o_blog.user_id " +
            " WHERE blog_classification.user_id=#{userId} GROUP BY blog_classification.id")
    List<Classify> getClassifyBlogCount(Integer userId);

    /*多表连接*/
    @Select("SELECT * , blog_classification.name classificationName,blog_tag.name tagName, blog_classification.id classificationId,blog_tag.id tagId\n" +
            " FROM (o_blog INNER JOIN blog_classification ON o_blog.classification=blog_classification.id AND blog_classification.user_id=#{userId})  INNER JOIN blog_tag ON blog_tag.id=o_blog.tag\n" +
            "WHERE o_blog.user_id=#{userId} AND o_blog.classification=#{classificationId} ORDER BY publish_time DESC")
    @Results(id="blog",value = {
            //classification映射
            @Result(column ="classificationId" ,property = "classification.id"),
            @Result(column ="classificationName" ,property = "classification.name"),
            //映射tag
            @Result(column ="tagId" ,property = "tag.id"),
            @Result(column ="tagName" ,property = "tag.name"),
            //user不必映射所有
            @Result(column ="user_id" ,property = "user.id")

    })
    List<Blog> getblogsByuseridandclassify(Integer userId, Integer classificationId);

    /*查询用户博客按时间降序取前几个*/
    @Select("SELECT * , blog_classification.name classificationName,blog_tag.name tagName, blog_classification.id classificationId,blog_tag.id tagId \n" +
            "FROM (o_blog INNER JOIN blog_classification ON o_blog.classification=blog_classification.id AND blog_classification.user_id=#{id}) INNER JOIN blog_tag ON blog_tag.id=o_blog.tag \n" +
            "WHERE o_blog.user_id=#{id} ORDER BY publish_time DESC LIMIT 0,#{n}")
    @ResultMap("blog")
    List<Blog> gettopnblogsorderBydate(Integer id, Integer n);

    @Delete("delete from o_blog where id=#{delblogId}")
    int removeblogByIid(Integer delblogId);

    @Update("update o_blog set classification=1 where user_id=#{id} and classification=#{classDeleteId}")
    int moveBlogtoDefaultclass(Integer id, Integer classDeleteId);

    @Update("update o_blog set classification=#{movchoice} where id=#{blogId}")
    int moveBlogtootherclass(Integer blogId, Integer movchoice);

    @Select("SELECT * , blog_classification.name classificationName,blog_tag.name tagName, blog_classification.id classificationId,blog_tag.id tagId \n" +
            "FROM ((o_blog INNER JOIN blog_classification ON o_blog.classification=blog_classification.id)INNER JOIN o_user on o_user.id=o_blog.user_id) INNER JOIN blog_tag ON blog_tag.id=o_blog.tag " +
            "where o_blog.id=#{blogId}")
    @Results(id="blogmore",value = {
            //classification映射
            @Result(column ="classificationId" ,property = "classification.id"),
            @Result(column ="classificationName" ,property = "classification.name"),
            //映射tag
            @Result(column ="tagId" ,property = "tag.id"),
            @Result(column ="tagName" ,property = "tag.name"),
            //user不必映射所有
            @Result(column ="user_id" ,property = "user.id"),
            @Result(column ="username" ,property = "user.username"),
            @Result(column ="image" ,property = "user.image"),
            @Result(column ="fans" ,property = "user.fans"),
            @Result(column ="follow" ,property = "user.follow"),
            @Result(column ="reputation" ,property = "user.reputation"),
            @Result(column ="reputation" ,property = "user.reputation"),
    })
    Blog getblogById(Integer blogId);

    @Select("SELECT COUNT(o_blog.id) AS userblogcount ,(select count(*) from o_collection where user_id=#{userId}) AS usercollections, SUM(views) AS userviews ,SUM(comments) AS usercomments ,SUM(good_count) AS usergoodcounts,TIMESTAMPDIFF(YEAR,o_user.create_time,NOW()) AS codeage  FROM o_blog INNER JOIN o_user ON o_blog.user_id=o_user.id WHERE o_blog.user_id=#{userId}")
    @Results(id="usermore",value = {

            //(可省略)
            @Result(column ="userviews" ,property = "userviews"),
            @Result(column ="userblogcount" ,property = "userblogcount"),
            @Result(column ="usercomments" ,property = "usercomments"),
            @Result(column ="usergoodcounts" ,property = "usergoodcounts"),
            @Result(column ="usercollections" ,property = "usercollections"),
            @Result(column ="codeage" ,property = "codeage"),

    })
    BlogPageUser getUserMoreById(Integer userId);



    @Update("update o_blog set title=#{title},content=#{content},tag=#{tagId},classification=#{classId},isoriginal=#{isoriginal} where id=#{blogId}")
    int updateblogByid(Integer blogId, String title, String content, Integer tagId, Integer classId,Integer isoriginal);

    @Select("select id,user_id,title,good_count+views+comments AS recommendation from o_blog ORDER BY recommendation DESC LIMIT 0,6")
    List<Blog> getRecommandblogs();


    @Select("select *,good_count+views+comments AS recommendation  , blog_classification.name classificationName,blog_tag.name tagName, blog_classification.id classificationId,blog_tag.id tagId \n" +
            "from ((o_blog INNER JOIN blog_classification ON o_blog.classification=blog_classification.id)INNER JOIN o_user on o_user.id=o_blog.user_id) INNER JOIN blog_tag ON blog_tag.id=o_blog.tag  ORDER BY recommendation DESC LIMIT 0,5")
    @ResultMap("blogmore")
    List<Blog> getIndexRecommandblogs();
}
