package com.example.demo.repositories.Friend;


import com.example.demo.domain.Friends.Friend;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface FriendRepositoryMongo extends MongoRepository<Friend, String>{

    public List<Friend> findByUserIdOrFriendId(String userId, String friendId, Sort sort);

    public Friend findByUserIdAndFriendId(String uid, String friendId);

}
