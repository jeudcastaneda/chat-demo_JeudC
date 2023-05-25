package com.cetys.chatdemo.config;

import com.cetys.chatdemo.model.ChatUser;
import com.cetys.chatdemo.model.ChatUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class ChatUserDetailsService implements UserDetailsService {

    @Autowired
    ChatUserRepository chatUserRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ChatUser chatUser = chatUserRepository.findByUsername(username);
        GrantedAuthority[] roles = {new SimpleGrantedAuthority("USER")};
        if(chatUser != null) {
            return new User(
                    chatUser.getUsername(),
                    chatUser.getPassword(),
                    Arrays.asList(roles));
        } else {
            throw new UsernameNotFoundException("Usuario incorrecto");
        }
    }
}
