package com.love.outofmemory.mapper;

import com.love.outofmemory.domain.Classification;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassificationMapper {
    @Select("select * from blog_classification where user_id=#{id}")
    @Results( id="classification",value = {
            @Result(column ="user_id",property ="user.id" )
    })
    List<Classification> getClassificationByUserID(Integer id);

    @Insert("insert into blog_classification values(null,#{id},#{name})")
    int addClassification(Integer id, String name);

    @Select("select * from blog_classification where user_id=#{userId} and name=#{name}")
    @ResultMap(value = "classification")
    Classification getIdByname(Integer userId,String name);

    @Select("select * from blog_classification where user_id=#{id} and id>1")
    @ResultMap(value = "classification")
    List<Classification> getDiyClassificationByUserID(Integer id);

    @Delete("delete from blog_classification where user_id=#{uid} and id=#{c}")
    int deleteClassByuidAndclassid(Integer uid, Integer c);

    @Select("select * from blog_classification where user_id=#{uid}  and id!=#{currentclass}")
    @ResultMap(value = "classification")
    List<Classification> getMovclass(Integer uid, Integer currentclass);
}
