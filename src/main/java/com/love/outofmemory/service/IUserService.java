package com.love.outofmemory.service;

import com.love.outofmemory.domain.User;
import com.love.outofmemory.domain.view.ProfilePageUser;

/**
 * @author huang
 */
public interface IUserService {

    int newUser(User user);

    User getUserByloginname(String loginname);

    ProfilePageUser getProfileUserById(Integer userId);

    int updateUserById(User muser);

    int updateUserpasswdById(Integer userId, String newpasswdMd5);

    int updateUserphoneById(Integer userId, String phonenumber);

    int updateUseremailById(Integer id, String email);

    int followblogerByid(Integer userId, Integer blogauthorId);
}
