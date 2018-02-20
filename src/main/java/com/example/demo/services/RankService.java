package com.example.demo.services;

import com.example.demo.domain.*;
import com.example.demo.domain.Friends.Friend;
import com.example.demo.domain.Rank.Rank;
import com.example.demo.repositories.Rank.RankRepositoryMongo;
import com.jawbone.upplatformsdk.datamodel.JawboneCommonParameter;
import com.misfit.cloudapisdk.datamodel.MisfitCommitParam;
import com.misfit.cloudapisdk.datamodel.MisfitSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class RankService {
    @Autowired
    private RankRepositoryMongo rankRepositoryMongo;

    @Autowired
    private FriendService friendService;

    @Autowired
    private UserService userService;

    @Autowired
    private ConnectToJawboneApiService jawboneApiService;

    @Autowired
    private ConnectToMisfitApiService misfitApiService;

    private Long geCurrentTime() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String currentDate = sdf.format(date);
        return sdf.parse(currentDate).getTime();
    }

    private FitnessSummary getFitnessSummary(User user, Long datetime){
        FitnessSummary summary = null;
        if(user.getDevice().equals("jawbone")){
            JawboneCommonParameter jawboneCommonParameter = new JawboneCommonParameter.Builder().Start(new Date(datetime)).End(new Date(datetime)).Details(false).build();
            summary = jawboneApiService.getJawboneSummary(user.getAccessCode(), jawboneCommonParameter);
        }

        if(user.getDevice().equals("misfit")){
            MisfitCommitParam misfitCommitParam = new MisfitCommitParam.Builder().start(new Date(datetime)).end(new Date(datetime)).detail(false).build();
            summary = misfitApiService.getMisfitSummary(user.getAccessCode(), misfitCommitParam);
        }
        return summary;
    }


    private void updateRankData(String uid) throws Exception {
        Long currentTime = geCurrentTime();
//        System.out.println("current time long :: " + currentTime);
        User user = userService.loadUserByUserId(uid);
        if(user == null){
            return;
        }else{
            user.setPassword(null);
        }
        Rank rank = rankRepositoryMongo.findByAuthorIdAndDateTime(uid, currentTime);
        FitnessSummary fitnessSummary = getFitnessSummary(user, currentTime);
        if(rank == null){
            rank = new Rank.Builder()
                    .authorId(uid).dateTime(currentTime).fitnessSummary(fitnessSummary).followers(new ArrayList<String>()).steps(fitnessSummary.getSteps()).build();
        }else {
            rank.setFitnessSummary(fitnessSummary);
        }
        rankRepositoryMongo.save(rank);
    }

    private void configRankParams(Rank rank, String uid){
        User user = null;
        try {
            user = userService.loadUserByUserId(rank.getAuthorId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        CustomerUser customerUser = new CustomerUser(user);
        rank.setAuthor(customerUser);
        if(rank.getFollowers().contains(uid)){
            rank.setLike(true);
        }else{
            rank.setLike(false);
        }
    }

    public List<Rank> getFriendsRank(String uid) throws Exception { // , int numPage
        updateRankData(uid);
        Long currentDate = geCurrentTime();
//        Pageable pageable = new PageRequest(numPage, 2, new Sort(new Sort.Order(Sort.Direction.DESC, "steps")));
        List<Friend> friends = friendService.getFriends(uid);
        List<String> authorIds = new ArrayList<>();
        authorIds.add(uid);
        for(Friend friend : friends){
            CustomerUser customerUser = friend.getFriend();
            authorIds.add(customerUser.getId());
        }
        List<Rank> ranks = rankRepositoryMongo.findByAuthorIdInAndDateTime(authorIds, currentDate);//.getContent();
        for(Rank rank : ranks){
            configRankParams(rank, uid);
        }
        return ranks;
    }

    public Rank updateOneRankToLike(String uid, String id){
        Rank rank = rankRepositoryMongo.findOne(id);
        if(rank.getFollowers().contains(uid)){
            rank.getFollowers().remove(uid);
        }else{
            rank.getFollowers().add(uid);
        }
        return rankRepositoryMongo.save(rank);
    }

    private Date transferPastDates(Date current, int num){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(current);
        calendar.add(Calendar.DATE, num);
        return calendar.getTime();
    }

    private void configRankSummarys(User user, Rank rank){
        Date currentTime = new Date(rank.getDateTime());
        Date endTime = transferPastDates(currentTime, -1);
        Date startTime = transferPastDates(currentTime, -7);
        FitnessSummarys summarys = null;
//        System.out.println("-------++++++======= start time : " + startTime.toString() + " end time : " + endTime.toString());
        if(user.getDevice().equals("jawbone")){
            JawboneCommonParameter jawboneCommonParameter = new JawboneCommonParameter.Builder().Start(startTime).End(endTime).Details(true).build();
            summarys = jawboneApiService.getJawboneSummaries(user.getAccessCode(), jawboneCommonParameter);
        }
        if(user.getDevice().equals("misfit")){
            MisfitCommitParam misfitCommitParam = new MisfitCommitParam.Builder().start(startTime).end(endTime).detail(true).build();
            summarys = misfitApiService.getMisfitSummaries(user.getAccessCode(), misfitCommitParam);
        }
        rank.setSummarys(summarys);
    }


    public Rank getRankSummaryDetails(String uid, String id) throws Exception {
        Rank rank = rankRepositoryMongo.findOne(id);
        String authorId = rank.getAuthorId();
        User user = userService.loadUserByUserId(authorId);
        configRankSummarys(user, rank);
        rank.setAuthor(new CustomerUser(user));
        return rank;
    }
}
