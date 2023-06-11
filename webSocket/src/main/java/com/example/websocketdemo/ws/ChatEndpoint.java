package com.example.websocketdemo.ws;

import com.example.websocketdemo.config.GetHttpSessionConfigurator;
import com.example.websocketdemo.entity.Message;
import com.example.websocketdemo.util.MessageUtils;
import com.example.websocketdemo.util.OpenAIAPI;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

@ServerEndpoint(value = "/chat",configurator = GetHttpSessionConfigurator.class)
@Component
public class ChatEndpoint {

    private static int onlineCount=0;
    private static CopyOnWriteArrayList<ChatEndpoint> webSocketSet=new CopyOnWriteArrayList<ChatEndpoint>();
    private Session session;

    private HttpSession httpsession;

    @OnOpen
    public void onOpen(Session session,EndpointConfig config){
        this.session=session;
        webSocketSet.add(this);//加入set中

        HttpSession request = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        this.httpsession = request;

        String username = (String) request.getAttribute("user");

        addOnlineCount();
        System.out.println("有新连接加入！当前在线人数为"+getOnlineCount());
    }

    @OnClose
    public void onClose(){
        webSocketSet.remove(this);
        subOnlineCount();
        System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    @OnMessage
    public void onMessage(String message,Session session) throws IOException{
        System.out.println("来自客户端的消息："+message);
        this.session.getBasicRemote().sendText(OpenAIAPI.chat(message));
    }

    @OnError
    public void onError(Session session,Throwable throwable){
        System.out.println("发生错误！");
        throwable.printStackTrace();
    }
    //   下面是自定义的一些方法
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    public static synchronized int getOnlineCount(){
        return onlineCount;
    }
    public static synchronized void addOnlineCount(){
        ChatEndpoint.onlineCount++;
    }
    public static synchronized void subOnlineCount(){
        ChatEndpoint.onlineCount--;
    }
}
