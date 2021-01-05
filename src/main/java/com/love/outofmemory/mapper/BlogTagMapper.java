package com.love.outofmemory.mapper;

import com.love.outofmemory.domain.BlogTag;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogTagMapper {

    @Select("select * from blog_tag")
    List<BlogTag> getBlogTags();

    @Insert("insert into blog_tag values(null,#{tagname})")
    int addTag(String tagname);

    @Select("select * from blog_tag where name=#{tag}")
    BlogTag getTagByName(String tag);
}
