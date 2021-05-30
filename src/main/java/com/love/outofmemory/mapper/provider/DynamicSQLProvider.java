package com.love.outofmemory.mapper.provider;
import com.mysql.cj.util.StringUtils;
import org.apache.ibatis.jdbc.SQL;
public class DynamicSQLProvider {

   /* 查询某类博客数量*/
    public String dynamicblogTypeSql(Integer classId,Integer userId,Integer tag){
        String sql=new SQL(){{
            SELECT("count(id)");
            FROM("o_blog");
            if(classId!=null){
                WHERE("classification=#{classId}");
            }
            if(userId!=null){
                WHERE("user_id=#{userId}");
            }
            if(tag!=null){
                WHERE("tag=#{tag}");
            }


        }
        }.toString();

        return sql;

    }

   /* 查询是否关注*/
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
                SELECT("(SELECT COUNT(o_blog.id) FROM o_blog WHERE isoriginal=1 AND user_id=#{userId}) AS originals","(select count(*) from o_collection where user_id=#{userId}) as usercollections","COALESCE(SUM(views),0) AS userviews","COALESCE(SUM(comments),0) AS usercomments","COALESCE(level,0) AS level"," COALESCE(SUM(good_count),0) AS usergoodcounts","COALESCE(TIMESTAMPDIFF(YEAR,o_user.create_time,NOW()),0) AS codeage","COALESCE((SELECT rownum FROM(SELECT a.*,@rownum:=@rownum+1 AS rownum FROM (SELECT SUM(good_count) AS rankbase ,user_id FROM o_blog GROUP BY user_id ORDER BY rankbase DESC)AS a,(SELECT @rownum := 0) r) AS b WHERE b.user_id=#{userId}),0) AS rownum");
                FROM("o_blog");
                INNER_JOIN("o_user on o_blog.user_id=o_user.id");
                WHERE("o_blog.user_id=#{userId}");

            }
            }.toString();
        }
        else{
              sql=new SQL(){{
                SELECT("(SELECT COUNT(o_blog.id) FROM o_blog WHERE isoriginal=1 AND user_id=#{userId}) AS originals","(select count(*) from o_collection where user_id=#{userId}) as usercollections","COALESCE(SUM(views),0) AS userviews","COALESCE(SUM(comments),0) AS usercomments","COALESCE(level,0) AS level"," COALESCE(SUM(good_count),0) AS usergoodcounts","COALESCE(TIMESTAMPDIFF(YEAR,o_user.create_time,NOW()),0) AS codeage","COALESCE((SELECT rownum FROM(SELECT a.*,@rownum:=@rownum+1 AS rownum FROM (SELECT SUM(good_count) AS rankbase ,user_id FROM o_blog GROUP BY user_id ORDER BY rankbase DESC)AS a,(SELECT @rownum := 0) r) AS b WHERE b.user_id=#{userId}),0) AS rownum", finalBefollowed);
                FROM("o_blog");
                INNER_JOIN("o_user on o_blog.user_id=o_user.id");
                WHERE("o_blog.user_id=#{userId}");

            }
            }.toString();
        }



        return sql;

    }

    /*博客id查询（偏向于个人）*/
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

    /*博客id查询（偏向于个人）*/
    public String dynamicindexblogSql(Integer page,Integer pageSize,Integer tag){

        String sql=new SQL(){{

            SELECT("id");
            FROM("o_blog");

            if(tag!=null){
                WHERE("tag=#{tag}");
            }

            ORDER_BY("good_count+views+comments desc");
            if(page!=null&&pageSize!=null){
                LIMIT("#{page} , #{pageSize}");
            }

        }}.toString();
        return sql;
    }
}
