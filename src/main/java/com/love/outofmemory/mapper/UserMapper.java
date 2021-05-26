package com.love.outofmemory.mapper;


import com.love.outofmemory.domain.User;
import com.love.outofmemory.domain.view.BlogPageUser;
import com.love.outofmemory.domain.view.ProfilePageUser;
import com.love.outofmemory.mapper.provider.DynamicSQLProvider;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {

    //用户注册
    @Insert("insert into o_user values(null,#{username},#{password},#{phone},#{sex},#{birthday},#{reputation},#{email},#{qq_number},#{address},#{create_time},#{image},#{fans},#{follow},#{level}) ")
    int newUser(User user);

    @Select("select * from o_user where phone=#{phone}")
    User getUserByPhone(String phone);

    @Select("select * from o_user where email=#{email}")
    User getUserByEmail(String email);

    @Select("select id,username,sex,birthday,reputation,qq_number,address,image,level,TIMESTAMPDIFF(YEAR,create_time,NOW()) AS codeage from o_user where id=#{userId}")
    ProfilePageUser getProfileUserById(Integer userId);

    @Update("update o_user set username=#{username},birthday=#{birthday},qq_number=#{qq_number},sex=#{sex},reputation=#{reputation} where id=#{id}")
    int updateUserById(User muser);

    @Update("update o_user set password=#{newpasswdMd5} where id=#{userId}")
    int updateUserpasswdById(Integer userId, String newpasswdMd5);

    @Update("update o_user set phone=#{phonenumber} where id=#{userId}")
    int updateUserphoneById(Integer userId, String phonenumber);

    @Update("update o_user set email=#{email} where id=#{userId}")
    int updateUseremailById(Integer userId, String email);

    @Insert("insert into o_follow values(null,#{userId},#{blogauthorId})")
    int followblogerByid(Integer userId, Integer blogauthorId);

    @Select("select count(id) from o_follow where user_id=#{userId} and follow_id=#{blogauthorId}")
    int isalreadyfollowed(Integer userId, Integer blogauthorId);

    @Delete("delete from o_follow where user_id=#{userId} and follow_id=#{blogauthorId}")
    int unfollowblogerByid(Integer userId, Integer blogauthorId);

    @SelectProvider(value = DynamicSQLProvider.class,method ="dynamicblogfollowexitsSql" )
    @Results(id="usermore",value = {

            //(可省略)
            @Result(column ="userviews" ,property = "userviews"),
            @Result(column ="originals" ,property = "originals"),
            @Result(column ="usercomments" ,property = "usercomments"),
            @Result(column ="usergoodcounts" ,property = "usergoodcounts"),
            @Result(column ="usercollections" ,property = "usercollections"),
            @Result(column ="codeage" ,property = "codeage"),
            @Result(column = "rownum",property = "rank"),
            @Result(column = "befollowed",property = "befollowed")

    })
    BlogPageUser getProfileMoreUserById(Integer userId);
}
