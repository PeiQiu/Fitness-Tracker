package com.example.demo.repositories.GroupFriends;


import com.example.demo.domain.ChatMessage.GroupFriends;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface GroupFriendsRepository extends MongoRepository<GroupFriends, String> {

    public List<GroupFriends> findByGroupIdsContaining(String groupId);

}
