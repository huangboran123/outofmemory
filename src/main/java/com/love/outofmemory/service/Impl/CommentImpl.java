package com.love.outofmemory.service.Impl;

import com.love.outofmemory.domain.Comment;
import com.love.outofmemory.mapper.CommentMapper;
import com.love.outofmemory.service.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentImpl implements ICommentService {
    @Autowired
    private CommentMapper commentMapper;

    @Override
    public int commitcomment(Comment comment1) {
        return commentMapper.commitcomment(comment1);
    }
}
