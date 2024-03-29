package com.love.outofmemory.service.Impl;

import com.love.outofmemory.domain.User;
import com.love.outofmemory.domain.view.BlogPageUser;
import com.love.outofmemory.domain.view.ProfilePageUser;
import com.love.outofmemory.mapper.UserMapper;
import com.love.outofmemory.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author huang
 */
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    //注册
    @Override
    public int newUser(User user) {
        return userMapper.newUser(user);
    }

    //登录
    @Override
    public User getUserByloginname(String loginname) {

        User user=userMapper.getUserByEmail(loginname);
        if(user!=null){

            return user;
        }
        else {
            return userMapper.getUserByPhone(loginname);
        }

    }

    @Override
    public ProfilePageUser getProfileUserById(Integer userId) {
        return userMapper.getProfileUserById(userId);
    }

    @Override
    public int updateUserById(User muser) {
        return userMapper.updateUserById(muser);
    }

    @Override
    public int updateUserpasswdById(Integer userId, String newpasswdMd5) {
        return userMapper.updateUserpasswdById(userId,newpasswdMd5);
    }

    @Override
    public int updateUserphoneById(Integer userId, String phonenumber) {
        return userMapper.updateUserphoneById(userId,phonenumber);
    }

    @Override
    public int updateUseremailById(Integer userId, String email) {
        return userMapper.updateUseremailById(userId, email);
    }

    @Override
    public int followblogerByid(Integer userId, Integer blogauthorId, Integer type) {
        if(type==1){
            if(userMapper.isalreadyfollowed(userId,blogauthorId)==1){
                return 2;
            }else {
                return userMapper.followblogerByid(userId,blogauthorId);
            }
        }else {

            return userMapper.unfollowblogerByid(userId,blogauthorId);
        }

    }

    @Override
    public BlogPageUser getProfileMoreUserById(Integer userId) {
        return userMapper.getProfileMoreUserById(userId);
    }

    @Override
    public List<User> getPagingFollowsByUserId(Integer page, Integer pageSize, Integer userId) {
        return userMapper.getPagingFollowsByUserId((page-1)*pageSize,pageSize,userId);
    }

    @Override
    public Integer getFollowTotalcountByUserId(Integer userId) {
        return userMapper.getFollowTotalcountByUserId(userId);
    }

    @Override
    public User getUserById(Integer followId) {
        return userMapper.getUserById(followId);
    }
}
