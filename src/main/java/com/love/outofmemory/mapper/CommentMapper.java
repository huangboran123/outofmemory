package com.love.outofmemory.mapper;

import com.love.outofmemory.domain.Comment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentMapper {



    @Insert("insert into o_comment values(null,#{critic.id},#{comment_time},#{comment_content},#{blog.id})")
    int commitcomment(Comment comment1);

    @Select("select * from o_comment")
    List<Comment> getAllCommentsPageByBlogId(Integer blogId, Integer counts);
}
