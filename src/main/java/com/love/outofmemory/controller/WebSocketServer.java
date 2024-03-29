package com.love.outofmemory.controller;

import com.love.outofmemory.Utills.DateUtil;
import com.love.outofmemory.domain.Message;
import com.love.outofmemory.domain.User;
import com.love.outofmemory.service.IMessageService;
import lombok.extern.slf4j.Slf4j;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

/*原型模式*/
@ServerEndpoint("/chat/{userId}")
@Component
@Slf4j
public class WebSocketServer {


    public static IMessageService iMessageService;



    /**concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。*/
    public static ConcurrentHashMap<String,WebSocketServer> webSocketMap = new ConcurrentHashMap<>();
    /**与某个客户端的连接会话，需要通过它来给客户端发送数据*/
    private Session session;
    /**接收userId*/
    private String userId="";
    /**
     * 连接建立成功调用的方法*/
    @OnOpen
    public void onOpen(Session session,@PathParam("userId") String userId) {
        this.session = session;
        this.userId=userId;
        if(webSocketMap.containsKey(userId)){
            webSocketMap.remove(userId);
            //将当前用户id作为key值，将对应id创建的websocket实例加入到线程安全的map中
            webSocketMap.put(userId,this);

        }else{
            webSocketMap.put(userId,this);
            //加入map中
        }

        try {
            sendMessage("{"+"msg:"+"'连接成功'}");
        } catch (IOException e) {
            log.error("用户:"+userId+",网络异常!!!!!!");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        if(webSocketMap.containsKey(userId)){
            /*map中删除*/
            webSocketMap.remove(userId);

        }

    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息*/
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("用户消息:"+userId+",报文:"+message);
        //可以群发消息
        //消息保存到数据库、redis
        if(StringUtils.isNotBlank(message)){
            try {
                //解析发送的报文
                JSONObject jsonObject = JSON.parseObject(message);

                if(jsonObject.containsKey("msg")){
                    log.info(jsonObject.get("msg").toString());
                }else{
                    //追加发送人(防止串改)
                    jsonObject.put("fromUserId",this.userId);
                    String toUserId=jsonObject.getString("toUserId");
                    //传送给对应toUserId用户的websocket
                    if(StringUtils.isNotBlank(toUserId)&&webSocketMap.containsKey(toUserId)){
                        webSocketMap.get(toUserId).sendMessage(jsonObject.toJSONString());
                        log.info("请求的userId:"+toUserId+"存在该服务器上");
                        log.info("消息存入MySQL数据库");
                        saveMessage(toUserId,this.userId,jsonObject.getDate("sendtime"),jsonObject.getString("content"));

                    }else{
                        log.info("请求的userId:"+toUserId+"不在该服务器上");
                        log.info("消息存入MySQL数据库");
                        //否则不在这个服务器上，发送到mysql或者redis
                        saveMessage(toUserId,this.userId, DateUtil.strToDateLong(jsonObject.getString("sendtime")),jsonObject.getString("content"));
                    }
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("用户错误:"+this.userId+",原因:"+error.getMessage());
        error.printStackTrace();
    }
    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    /**
     * 发送自定义消息
     * */
    public static void sendInfo(String message,@PathParam("userId") String userId) throws IOException {
        log.info("发送消息到:"+userId+"，报文:"+message);
        if(StringUtils.isNotBlank(userId)&&webSocketMap.containsKey(userId)){
            webSocketMap.get(userId).sendMessage(message);
        }else{
            log.error("用户"+userId+",不在线！");
        }
    }

    /*保存消息*/
    public void saveMessage(String toUserId, String fromUserId, Date sendtime, String content){
        Message localmessage=new Message();
        User user=new User();
        user.setId(Integer.valueOf(toUserId));
        User touser=new User();
        touser.setId(Integer.valueOf(fromUserId));
        localmessage.setUser(user);
        localmessage.setContent(content);
        localmessage.setSendto(touser);
        localmessage.setSend_time(sendtime);
        try {
            Integer i=iMessageService.saveMessage(localmessage);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
