package com.example.demo.repositories.User;


import com.example.demo.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

@Service
public class UserRepositoryMongoImpl implements UserRepository{
    @Autowired
    private MongoOperations mongo;


    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private boolean validate(String email){
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        return pattern.matcher(email).matches();
    }

    private Pattern makeOnePattern(String regex){
        return Pattern.compile(queryParams(regex), Pattern.CASE_INSENSITIVE);
    }

    private String queryParams(String regex){
        return "^.*" + regex + ".*$";
    }

    @Override
    public List<User> findFiltered(String keyWord) {
        System.out.println("keyword " + keyWord);
        Query query = new Query();
        if(validate(keyWord)){
            query.addCriteria(Criteria.where("email").regex(Pattern.compile(keyWord)));
        }else{
            Criteria c1 = Criteria.where("username").regex(makeOnePattern(keyWord));
            Criteria c2 = Criteria.where("firstname").regex(makeOnePattern(keyWord));
            Criteria c3 = Criteria.where("lastname").regex(makeOnePattern(keyWord));
            Criteria c = new Criteria();
            c.orOperator(c1,c2,c3);
            query.addCriteria(c);
        }
        query.addCriteria(Criteria.where("status").is(true));
        return mongo.find(query, User.class);
    }
}
