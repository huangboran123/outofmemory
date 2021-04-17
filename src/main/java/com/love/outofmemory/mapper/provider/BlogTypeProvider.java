package com.love.outofmemory.mapper.provider;

import org.apache.ibatis.jdbc.SQL;

public class BlogTypeProvider {

    public String dynamicTypeSql(Integer id){
        String sql=new SQL(){


        }.toString();

        return sql;


    }
}
