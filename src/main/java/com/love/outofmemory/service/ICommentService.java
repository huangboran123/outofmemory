package com.love.outofmemory.service;

import com.love.outofmemory.domain.Comment;

import java.util.List;

public interface ICommentService {
    int commitcomment(Comment comment1);

    List<Comment> getAllCommentsPageByBlogId(Integer blogId, Integer counts);
}
