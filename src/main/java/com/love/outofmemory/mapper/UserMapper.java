package com.love.outofmemory.mapper;


import com.love.outofmemory.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {

    //用户注册
    @Insert("insert into o_user values(null,#{username},#{password},#{phone},#{sex},#{birthday},#{reputation},#{email},#{qq_number},#{address},#{create_time},#{image},#{fans},#{follow}) ")
    int newUser(User user);

    @Select("select * from o_user where phone=#{phone}")
    User getUserByPhone(String phone);

    @Select("select * from o_user where email=#{email}")
    User getUserByEmail(String email);


}
