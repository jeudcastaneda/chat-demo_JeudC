package com.cetys.chatdemo.controller;

import com.cetys.chatdemo.model.ChatUser;
import com.cetys.chatdemo.model.ChatUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    @Autowired
    ChatUserRepository chatUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/register")
    public String getRegister(Model model) {
        ChatUser user = new ChatUser();
        model.addAttribute("newUser", user);
        return "register";
    }

    @PostMapping("/register")
    public String postRegister(ChatUser user, Model model) {
        boolean userExists = chatUserRepository.existsByUsername(user.getUsername());

        if (userExists) {
            model.addAttribute("newUser", user);
            model.addAttribute("error", "Username ocupado");
            return "register";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        chatUserRepository.save(user);
        return "index";
    }
}
