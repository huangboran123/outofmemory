package com.love.outofmemory.configuration;


import com.love.outofmemory.controller.WebSocketServer;
import com.love.outofmemory.service.IMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
public class WebsocketConfiguration {

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
    @Autowired
    public void setIMessageService(IMessageService iMessageService){
        WebSocketServer.iMessageService=iMessageService;
    }
}
