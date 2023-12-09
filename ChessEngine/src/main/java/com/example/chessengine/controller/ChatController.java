package com.example.chessengine.controller;

import com.example.chessengine.entity.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @MessageMapping("/chat.send.{idMatch}")
    @SendTo("/topic/match.{idMatch}")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        String username = (String) headerAccessor.getSessionAttributes().get("username");

        // Additional logic if needed
System.out.println(username);
        // Set the username in the chat message
        chatMessage.setSender(username);

        return chatMessage;
    }
}

