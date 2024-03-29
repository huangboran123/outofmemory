package com.love.outofmemory.mapper;

import com.love.outofmemory.domain.Message;
import com.love.outofmemory.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageMapper {

    @Select("SELECT distinct a.sendto_id id ,b.image,b.username FROM o_message a INNER JOIN o_user b ON a.sendto_id=b.id WHERE a.user_id=#{userId} limit #{page},#{pageSize}")
    List<User> getRecentContactByUserId(Integer page,Integer pageSize,Integer userId);

    @Select("SELECT a.user_id,a.send_time,a.content,b.username,b.image FROM o_message a INNER JOIN o_user b ON a.user_id=b.id WHERE (a.user_id=#{userId} AND a.sendto_id=#{followId}) OR (a.user_id=#{followId} AND a.sendto_id=#{userId}) LIMIT #{page},#{pageSize}")
    @Results(id="messagemore",value = {

            //(可省略)
            @Result(column ="image" ,property = "user.image"),
            @Result(column ="username" ,property = "user.username"),


    })
    List<Message> getMessageContactByUserIdAndFollowId(Integer page, Integer pageSize, Integer userId, Integer followId);

    @Insert("insert into o_message values(null,#{user.id},#{sendto.id},#{send_time},#{context})")
    Integer saveMessage(Message localmessage);
}
