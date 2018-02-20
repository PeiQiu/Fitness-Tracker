package com.example.demo.services;


import com.example.demo.domain.ChatMessage.ChatMessage;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Component
public class MyWebSocketHandler implements WebSocketHandler{
    public static final String USER_Id = "uid";

    @Autowired
    private MessageService messageService;

    public static final Map<String , WebSocketSession> userSocketSessionMap;

    static {
        userSocketSessionMap = new HashMap<>();
    }



    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {
//        System.out.println("redirect to wrong page !! ");
        String uid = (String) webSocketSession.getAttributes().get("uid");
        if(userSocketSessionMap.get(uid) == null){
            userSocketSessionMap.put(uid, webSocketSession);
        }
    }

    @Override
    public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) throws Exception {
        if(webSocketMessage.getPayloadLength() == 0) return;
        ChatMessage msg = new Gson().fromJson(webSocketMessage.getPayload().toString(), ChatMessage.class);
        Long timeStamp = new Date().getTime();
//        msg.setMessageDate(timeStamp);
//        ChatMessage chatmessage = messageService.addNewChaeMessage(msg);

//        sendMessageToUser(msg.getToName(), new TextMessage(new Gson().toJson(chatmessage)));
    }

    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {

    }

    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
//        System.out.println("WebSocket:"+webSocketSession.getAttributes().get("uid")+"close connection");
        Iterator<Map.Entry<String, WebSocketSession>> iterator = userSocketSessionMap.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<String, WebSocketSession> entry = iterator.next();
            if(entry.getValue().getAttributes().get(USER_Id) == webSocketSession.getAttributes().get(USER_Id)){
                userSocketSessionMap.remove(webSocketSession.getAttributes().get(USER_Id));
//                System.out.println("WebSocket in staticMap:" + webSocketSession.getAttributes().get("uid") + "removed");
            }
        }
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    public void sendMessageToUser(String uid, TextMessage message)
            throws IOException {
        WebSocketSession session = userSocketSessionMap.get(uid);
        if (session != null && session.isOpen()) {
            session.sendMessage(message);
        }
    }
}
