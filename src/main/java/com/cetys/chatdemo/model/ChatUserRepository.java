package com.cetys.chatdemo.model;

import org.springframework.data.repository.CrudRepository;

public interface ChatUserRepository extends CrudRepository<ChatUser, Integer> {
    ChatUser findByUsername(String username);

    boolean existsByUsername(String username);
}
