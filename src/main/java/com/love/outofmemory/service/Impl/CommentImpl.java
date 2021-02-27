package com.love.outofmemory.service.Impl;

import com.love.outofmemory.domain.Comment;
import com.love.outofmemory.domain.ReplyComment;
import com.love.outofmemory.mapper.CommentMapper;
import com.love.outofmemory.service.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentImpl implements ICommentService {
    @Autowired
    private CommentMapper commentMapper;

    @Override
    public int commitcomment(Comment comment1) {
        return commentMapper.commitcomment(comment1);
    }

    @Override
    public List<Comment> getAllCommentsPageByBlogId(Integer blogId, Integer start, Integer counts) {
        return commentMapper.getAllCommentsPageByBlogId(blogId,start,counts);
    }

    @Override
    public int commitreply(ReplyComment replyComment) {
        return commentMapper.commitreply(replyComment);
    }
}
