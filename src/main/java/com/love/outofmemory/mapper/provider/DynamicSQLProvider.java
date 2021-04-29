package com.love.outofmemory.mapper.provider;
import com.mysql.cj.util.StringUtils;
import org.apache.ibatis.jdbc.SQL;
public class DynamicSQLProvider {

    public String dynamicblogTypeSql(Integer id){
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

    public String dynamicblogfollowexitsSql(Integer userId,Integer id){

        String befollowed= new SQL(){{
            if(null!=id){
                SELECT("COUNT(*)");
                FROM("o_follow");
                WHERE("user_id=#{id}","follow_id=#{userId}");
            }
        }}.toString();
        if(null!=befollowed){
            befollowed="("+befollowed+") as befollowed";
        }else {
            befollowed="";
        }


        String finalBefollowed = befollowed;
        String sql=new SQL(){{
            SELECT("COUNT(o_blog.id) AS userblogcount","(select count(*) from o_collection where user_id=#{userId}) as usercollections","SUM(views) AS userviews","SUM(comments) AS usercomments"," SUM(good_count) AS usergoodcounts","TIMESTAMPDIFF(YEAR,o_user.create_time,NOW()) AS codeage", finalBefollowed);
            FROM("o_blog");
            INNER_JOIN("o_user on o_blog.user_id=o_user.id");
            WHERE("o_blog.user_id=#{userId}");

        }

        }.toString();


        return sql;


    }
}
