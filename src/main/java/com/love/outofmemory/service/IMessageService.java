package com.love.outofmemory.service;

import com.love.outofmemory.domain.Message;
import com.love.outofmemory.domain.User;

import java.util.List;

public interface IMessageService {
    /*获取当前用户最近的聊天对象*/

    List<User> getRecentContactByUserId(Integer page,Integer pageSize,Integer userId);

    /*获取交流双方聊天记录聊天*/
    List<Message> getMessageContactByUserIdAndFollowId(Integer page, Integer pageSize, Integer userId, Integer followId);
}
