package com.love.outofmemory.service;

import com.love.outofmemory.domain.BlogTag;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * @author huang
 */
public interface IBlogTagService {

    List<BlogTag> getBlogTags();

    BlogTag getTagByName(String tag);
}
