package com.love.outofmemory.service.Impl;

import com.love.outofmemory.domain.Message;
import com.love.outofmemory.domain.User;
import com.love.outofmemory.mapper.MessageMapper;
import com.love.outofmemory.service.IMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements IMessageService {
    @Autowired
    private MessageMapper messageMapper;
    @Override
    public List<User> getRecentContactByUserId(Integer page,Integer pageSize,Integer userId) {
        return messageMapper.getRecentContactByUserId((page-1)*pageSize,pageSize,userId);
    }

    @Override
    public List<Message> getMessageContactByUserIdAndFollowId(Integer page, Integer pageSize, Integer userId, Integer followId) {
        return messageMapper.getMessageContactByUserIdAndFollowId(page,pageSize,userId,followId);
    }

    @Override
    public Integer saveMessage(Message localmessage) {
        return messageMapper.saveMessage(localmessage);
    }
}
