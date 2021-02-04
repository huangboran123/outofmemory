package com.love.outofmemory.mapper;

import com.love.outofmemory.domain.Comment;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentMapper {



    @Insert("")
    int commitcomment(Comment comment1);
}
