package com.cetys.chatdemo.controller;

import com.cetys.chatdemo.model.ChatMessage;
import com.cetys.chatdemo.model.ChatMessageRepository;
import com.cetys.chatdemo.model.ChatUser;
import com.cetys.chatdemo.model.ChatUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
public class ChatController {

    @Autowired
    ChatMessageRepository chatMessageRepository;
    @Autowired
    ChatUserRepository chatUserRepository;
    @Autowired
    SessionRegistry sessionRegistry;

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        chatMessageRepository.save(chatMessage);
        return chatMessage;
    }

    @GetMapping("/chat")
    public String getChat(Principal principal, Model model) {
        List<String> users = sessionRegistry.getAllPrincipals().stream()
                .filter((user) -> !sessionRegistry.getAllSessions(user, false).isEmpty())
                .map((user) -> ((User)user).getUsername())
                .toList();
        model.addAttribute("members", users);
        model.addAttribute("username", principal.getName());
        return "chat";
    }

    @GetMapping("/chat/messages")
    public ResponseEntity<List<ChatMessage>> getMessages() {
        return ResponseEntity.ok().body(chatMessageRepository.findAll());
    }

    @GetMapping("/chat/user")
    public ResponseEntity<ChatUser> getUser(@RequestParam(value = "username") String username) {
        ChatUser user =  chatUserRepository.findByUsername(username);
        System.out.println("USER -> " + user.getUsername());
        return ResponseEntity.ok().body(user);
    }

    @PostMapping(path="/chat/user", consumes={"application/json"})
    public ResponseEntity<ChatUser> postUser(@RequestBody ChatUser user) {
        System.out.println("USERNAME -> " + user.getUsername());
        chatUserRepository.save(user);
        return ResponseEntity.ok().body(user);
    }

    @PostMapping(path="/chat/user", consumes={"application/x-www-form-urlencoded"})
    public ResponseEntity<ChatUser> postUserForm(ChatUser user) {
        System.out.println("USERNAME -> " + user.getUsername());
        chatUserRepository.save(user);
        return ResponseEntity.ok().body(user);
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage,
                               SimpMessageHeaderAccessor headerAccessor) {
        // Add username in web socket session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }
}
