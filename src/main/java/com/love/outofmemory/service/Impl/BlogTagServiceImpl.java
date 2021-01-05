package com.love.outofmemory.service.Impl;

import com.love.outofmemory.domain.BlogTag;
import com.love.outofmemory.mapper.BlogTagMapper;
import com.love.outofmemory.service.IBlogTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author huang
 */
@Service
public class BlogTagServiceImpl implements IBlogTagService {

    @Autowired
    private BlogTagMapper blogTagMapper;

    @Override
    public List<BlogTag> getBlogTags() {

      List<BlogTag> tags= blogTagMapper.getBlogTags();
      if (tags.size()==0){
          blogTagMapper.addTag("默认标签");
      }
        return blogTagMapper.getBlogTags();
    }

    @Override
    public BlogTag getTagByName(String tag) {
        return blogTagMapper.getTagByName(tag);
    }

}
