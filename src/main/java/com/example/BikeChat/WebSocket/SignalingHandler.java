package com.example.BikeChat.WebSocket;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

    public class SignalingHandler extends TextWebSocketHandler {
        private final ConcurrentHashMap<String, Set<WebSocketSession>> rooms = new ConcurrentHashMap<>();


        @Override
        public void afterConnectionEstablished(WebSocketSession session) throws Exception {
            Map<String, String> params = getQueryParams(session.getUri().getQuery());
            String roomId = params.get("roomId");
            String userId = params.get("userId");

            rooms.computeIfAbsent(roomId, k -> ConcurrentHashMap.newKeySet()).add(session);

            System.out.println("User " + userId + " joined room " + roomId);
        }

        @Override
        protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
            Map<String, String> params = getQueryParams(session.getUri().getQuery());
            String roomId = params.get("roomId");

            Set<WebSocketSession> participants = rooms.getOrDefault(roomId, Collections.emptySet());
            for (WebSocketSession participant : participants) {
                if (!participant.equals(session) && participant.isOpen()) {
                    participant.sendMessage(message);
                }
            }
        }

        @Override
        public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
            for (Set<WebSocketSession> participants : rooms.values()) {
                participants.remove(session);
            }
            System.out.println("Connection closed: " + session.getId());
        }


        private Map<String, String> getQueryParams(String query) {
            Map<String, String> params = new HashMap<>();
            if (query != null) {
                String[] pairs = query.split("&");
                for (String pair : pairs) {
                    String[] keyValue = pair.split("=");
                    if (keyValue.length == 2) {
                        params.put(keyValue[0], keyValue[1]);
                    }
                }
            }
            return params;
        }
    }
