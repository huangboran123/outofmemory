package com.love.outofmemory.mapper.provider;
import com.mysql.cj.util.StringUtils;
import org.apache.ibatis.jdbc.SQL;
public class BlogTypeProvider {

    public String dynamicTypeSql(Integer id){
        String sql=new SQL(){{
            SELECT("count(id)");
            FROM("o_blog");
            if(!StringUtils.isNullOrEmpty(String.valueOf(id))){
                if(id==0){

                }
                else{
                    WHERE("classification=#{id}");
                }

            }

        }



        }.toString();

        return sql;

    }
}
