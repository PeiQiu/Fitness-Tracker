package com.example.demo.repositories.ChatMessage;


import com.example.demo.domain.ChatMessage.BaseMessage;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BaseMessageRepositoryMongo extends MongoRepository<BaseMessage, String> {

    public List<BaseMessage> findBySenderUsernameAndReceiverUsername(String senderUsername, String receiverUsername, Sort sort);

    public List<BaseMessage> findByRoomId(String roomId, Sort sort);

    public List<BaseMessage> findByReceiverUsernameAndRoomId(String receiverUsername, String roomId, Sort sort);
}
