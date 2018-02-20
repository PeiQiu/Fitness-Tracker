package com.example.demo.repositories.Friend;


import com.example.demo.domain.Friends.FriendRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FriendRequestRepositoryMongo extends MongoRepository<FriendRequest, String>{

    public List<FriendRequest> findByToCustomerUserIdAndStatus(String toCustomerId, boolean status, Sort sort);

    public List<FriendRequest> findByFromCustomerUserIdOrToCustomerUserId( String fromCustomerId, String toCustomerId);

    public FriendRequest findByFromCustomerUserIdAndToCustomerUserId(String fromCustomerId, String toCustomerId);

}
