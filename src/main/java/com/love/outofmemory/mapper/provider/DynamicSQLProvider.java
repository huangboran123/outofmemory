package com.love.outofmemory.mapper.provider;
import com.mysql.cj.util.StringUtils;
import org.apache.ibatis.jdbc.SQL;
public class DynamicSQLProvider {

    public String dynamicblogTypeSql(Integer classId,Integer userId){
        String sql=new SQL(){{
            SELECT("count(id)");
            FROM("o_blog");
            if(classId!=null&&userId==null){
                WHERE("classification=#{classId}");
            }
            if(classId==null&&userId!=null){
                WHERE("user_id=#{userId}");
            }
            if(classId!=null&&userId!=null){
                WHERE("user_id=#{userId} and classification=#{classId}");
            }



        }
        }.toString();

        return sql;

    }

   /* 是否关注*/
    public String dynamicblogfollowexitsSql(Integer userId,Integer id){

        String befollowed= new SQL(){{
            if(null!=id){
                SELECT("COUNT(*)");
                FROM("o_follow");
                WHERE("user_id=#{id}","follow_id=#{userId}");
            }
        }}.toString();
        if(null!=befollowed&&!"".equals(befollowed)){
            befollowed="("+befollowed+") as befollowed";
        }else {
            befollowed="";
        }


        String sql="";
        String finalBefollowed = befollowed;
        if("".equals(finalBefollowed)){
            sql=new SQL(){{
                SELECT("COUNT(o_blog.id) AS userblogcount","(select count(*) from o_collection where user_id=#{userId}) as usercollections","SUM(views) AS userviews","SUM(comments) AS usercomments"," SUM(good_count) AS usergoodcounts","TIMESTAMPDIFF(YEAR,o_user.create_time,NOW()) AS codeage ");
                FROM("o_blog");
                INNER_JOIN("o_user on o_blog.user_id=o_user.id");
                WHERE("o_blog.user_id=#{userId}");

            }
            }.toString();
        }
        else{
              sql=new SQL(){{
                SELECT("COUNT(o_blog.id) AS userblogcount","(select count(*) from o_collection where user_id=#{userId}) as usercollections","SUM(views) AS userviews","SUM(comments) AS usercomments"," SUM(good_count) AS usergoodcounts","TIMESTAMPDIFF(YEAR,o_user.create_time,NOW()) AS codeage", finalBefollowed);
                FROM("o_blog");
                INNER_JOIN("o_user on o_blog.user_id=o_user.id");
                WHERE("o_blog.user_id=#{userId}");

            }
            }.toString();
        }



        return sql;

    }

    /*博客id查询*/
    public String dynamicmyblogSql(Integer classification,Integer page,Integer pageSize,Integer id,Integer sort,Integer tag){

        String sql=new SQL(){{

                SELECT("id");
                FROM("o_blog");
                if(classification!=null){
                WHERE("classification=#{classification}");
                }
                if(tag!=null){
                    WHERE("tag=#{tag}");
                }
                if(id!=null){
                    WHERE("user_id=#{id}");
                }
                if(page!=null&&pageSize!=null){
                    LIMIT("#{page} , #{pageSize}");
                }
                if(sort==0){
                    ORDER_BY("publish_time desc");
                }else{

                    ORDER_BY("views desc");
                }
        }}.toString();
        return sql;
    }
}
