package com.example.demo.repositories.Friend;


import com.example.demo.domain.Friends.Friend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class FriendRepositoryImpl implements FriendRepository{

    @Autowired
    private MongoOperations mongo;

//    @Override
//    public List<Friend> findByFromCustomerIdOrToCustomerIdAndStateAndType(String fromCustomerId, String toCustomerId, Boolean state, Boolean type, Sort sort) {
//        System.out.println("fromid : " + fromCustomerId + "toid : " + toCustomerId);
//        Query query = new Query();
//        Criteria c1 = Criteria.where("fromCustomerId").is(fromCustomerId);
//        Criteria c2 = Criteria.where("toCustomerId").is(toCustomerId);
//        Criteria c = new Criteria();
//        c.orOperator(c1,c2);
//        query.addCriteria(c);
//        query.addCriteria(Criteria.where("state").is(true));
//        query.addCriteria(Criteria.where("type").is(true));
//        query.with(sort);
//        return mongo.find(query, Friend.class);
//    }
}
