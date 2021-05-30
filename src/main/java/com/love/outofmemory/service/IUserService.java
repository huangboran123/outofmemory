package com.love.outofmemory.service;

import com.love.outofmemory.domain.User;
import com.love.outofmemory.domain.view.BlogPageUser;
import com.love.outofmemory.domain.view.ProfilePageUser;

import java.util.List;

/**
 * @author huang
 */
public interface IUserService {

    //用户注册
    int newUser(User user);

    //根据用户名获取用户
    User getUserByloginname(String loginname);

    //获取用户加详细信息
    ProfilePageUser getProfileUserById(Integer userId);

    //用户更新
    int updateUserById(User muser);

    //更新密码
    int updateUserpasswdById(Integer userId, String newpasswdMd5);

    //更新手机号码
    int updateUserphoneById(Integer userId, String phonenumber);

    //更新邮箱
    int updateUseremailById(Integer id, String email);

    //关注与取消关注
    int followblogerByid(Integer userId, Integer blogauthorId, Integer type);

    //个人关注页个人信息
    BlogPageUser getProfileMoreUserById(Integer userId);

    /* 获取用户关注*/
    List<User> getPagingFollowsByUserId(Integer page, Integer pageSize, Integer userId);

    Integer getFollowTotalcountByUserId(Integer userId);
}
