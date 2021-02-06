package com.love.outofmemory.mapper;

import com.love.outofmemory.domain.Comment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentMapper {



    @Insert("insert into o_comment values(null,#{critic.id},#{comment_time},#{comment_content},#{blog.id})")
    int commitcomment(Comment comment1);

    @Select("select o_comment.id,critic_id,comment_time,comment_content,image from o_comment inner join o_user on o_comment.critic_id=o_user.id  where blog_id=#{blogId} limit #{start},#{counts}")
    @Results(id="comment",value = {

            @Result(column = "critic_id",property = "critic.id"),
            @Result(column = "o_comment.id",property = "id"),
            @Result(column = "image",property = "critic.image")

    })
    List<Comment> getAllCommentsPageByBlogId(Integer blogId,Integer start, Integer counts);
}
