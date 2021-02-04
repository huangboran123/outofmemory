package com.love.outofmemory.mapper;

import com.love.outofmemory.domain.Comment;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentMapper {



    @Insert("insert into o_comment values(null,#{critic.id},#{comment_time},#{comment_content},#{blog.id})")
    int commitcomment(Comment comment1);
}
