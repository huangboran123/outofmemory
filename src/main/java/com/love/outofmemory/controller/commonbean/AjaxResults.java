package com.love.outofmemory.controller.commonbean;

import com.love.outofmemory.domain.Blog;
import lombok.Data;

import java.util.List;

/**
 * 模拟Ajax返回数据
 */
@Data
public class AjaxResults {
   private String data;
   private List<Blog> blogresults;
   private Integer totalcount;

}
