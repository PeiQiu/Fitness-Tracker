package com.example.demo.repositories.Sharing;

import com.example.demo.domain.Sharings.Sharing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class SharingRepositoryImpl implements SharingRepository {

    @Autowired
    private MongoOperations mongo;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd");

    private Long getBeginDate(Long dateTime){
        Date date = new Date(dateTime);
        String date_string = simpleDateFormat.format(date);
        System.out.println("get date String value : " + date_string);
        try {
            return simpleDateFormat.parse(date_string).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Long getEndDate(Long datetime){
        long time =  datetime + (1000*60*60*24);
        System.out.println("time date : " + new Date(time).toString());
        return time;
    }

    private Pattern makeOnePattern(String regex){
        return Pattern.compile(queryParams(regex), Pattern.CASE_INSENSITIVE);
    }

    private String queryParams(String regex){
        return "^.*" + regex + ".*$";
    }

    @Override
    public List<Sharing> findFilter(String autherId, Long dateTime, String content, Pageable pageable) {
        Query query = new Query();
        query.addCriteria(Criteria.where("autherId").is(autherId));
        if(dateTime != null){
            long startDateTime = getBeginDate(dateTime);
            long endDateTime = getEndDate(startDateTime);
            query.addCriteria(Criteria.where("sharingDate").gte(startDateTime).lte(endDateTime));
        }
        query.addCriteria(Criteria.where("content").regex(makeOnePattern(content)));
        query.with(pageable);
        return mongo.find(query, Sharing.class);
    }

    @Override
    public List<Sharing> findByFriendsIdsAndState(List<String> friends, int state, Pageable pageable) {
        Query query = new Query();
        query.addCriteria(Criteria.where("state").is(state));

        Criteria[] criterias = new Criteria[friends.size()];
        for(int i =0; i < criterias.length; i ++){
            criterias[i] = Criteria.where("autherId").is(friends.get(i));
        }
        Criteria allCrits = new Criteria().orOperator(criterias);

        query.addCriteria(allCrits);
        query.with(pageable);
        return mongo.find(query, Sharing.class);
    }


}
