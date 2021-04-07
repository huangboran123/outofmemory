package com.love.outofmemory.mapper;


import com.love.outofmemory.domain.User;
import com.love.outofmemory.domain.view.ProfilePageUser;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
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
}
