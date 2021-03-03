package com.love.outofmemory.mapper;

import com.love.outofmemory.domain.Comment;
import com.love.outofmemory.domain.ReplyComment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentMapper {



    @Insert("insert into o_comment values(null,#{critic.id},#{comment_time},#{comment_content},#{blog.id})")
    int commitcomment(Comment comment1);

    @Select("select o_comment.id cid,critic_id,comment_time,comment_content,image,username from o_comment inner join o_user on o_comment.critic_id=o_user.id   where blog_id=#{blogId} order by comment_time desc limit #{start},#{counts} ")
    @Results(id="comment",value = {

            @Result(column = "critic_id",property = "critic.id"),
            @Result(column = "cid",property = "id"),
            @Result(column = "image",property = "critic.image"),
            @Result(column = "username",property = "critic.username")


    })
    List<Comment> getAllCommentsPageByBlogId(Integer blogId,Integer start, Integer counts);

    @Insert("insert into o_reply_comment values(null,#{comment.id},#{reply_time},#{reply_content},#{from.id},#{to.id})")
    int commitreply(ReplyComment replyComment);

//    注意:若某表中含有相同类型的外键需要连表查询，可以使用子查询命名为不同别名再连接
    @Select("select o_reply_comment.id rid, comment_id,reply_time,reply_content,from_uid,to_uid,o_user.id fromid,toid,o_user.username fromusername,tousername,o_user.image fromimage,toimage from ((o_reply_comment inner join o_user on o_reply_comment.from_uid=o_user.id)inner join (select id toid,username tousername,image toimage from o_user)fromuser on o_reply_comment.to_uid=fromuser.toid ) where comment_id=#{id} order by reply_time asc"
           )
    /*回复结果集映射*/
    @Results(id="reply",value = {
            @Result(column ="rid",property = "id"),
            @Result(column ="comment_id",property = "comment.id"),
            @Result(column ="reply_time",property = "reply_time"),
            @Result(column ="reply_content",property = "reply_content"),
            @Result(column ="fromid",property = "from.id"),
            @Result(column ="toid",property = "to.id"),
            @Result(column ="fromusername",property = "from.username"),
            @Result(column ="tousername",property = "to.username"),
            @Result(column ="fromimage",property = "from.image"),
            @Result(column ="toimage",property = "to.image"),

    })
    List<ReplyComment> getAllReplycommentsById(Integer id);
}
