package com.example.websocketdemo.config;



import org.apache.coyote.Response;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.naming.Context;
import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

@Configuration
public class GetHttpSessionConfigurator extends ServerEndpointConfig.Configurator {

    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        HttpSession session = (HttpSession) request.getHttpSession();
        if (session != null) {
            sec.getUserProperties().put(HttpSession.class.getName(),session);
        }
        super.modifyHandshake(sec, request, response);
    }
}
