package com.love.outofmemory.service;

import com.love.outofmemory.domain.Comment;
import com.love.outofmemory.domain.ReplyComment;

import java.util.List;

public interface ICommentService {
    int commitcomment(Comment comment1);

    List<Comment> getAllCommentsPageByBlogId(Integer blogId, Integer start, Integer counts);

    int commitreply(ReplyComment replyComment);
}
