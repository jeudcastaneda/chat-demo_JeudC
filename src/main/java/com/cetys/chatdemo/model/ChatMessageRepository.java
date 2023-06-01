package com.cetys.chatdemo.model;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ChatMessageRepository extends CrudRepository<ChatMessage,Integer>{
    // CRUD = Create Read Update Delete
    // ABC = Altas Bajas Cambios

    List<ChatMessage> findAll();
}
