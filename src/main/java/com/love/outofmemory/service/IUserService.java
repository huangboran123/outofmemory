package com.love.outofmemory.service;

import com.love.outofmemory.domain.User;

/**
 * @author huang
 */
public interface IUserService {

    int newUser(User user);

    User getUserByloginname(String loginname);
}
