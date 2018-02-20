package com.example.demo.domain.Rank;

import com.example.demo.domain.CustomerUser;
import com.example.demo.domain.FitnessSummary;
import com.example.demo.domain.FitnessSummarys;
import com.example.demo.domain.Friends.Friend;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
public class Rank {

    @Id
    private String id;

    private String authorId;

    private List<String> followers;

    private long dateTime;

    private long steps;

    private FitnessSummary fitnessSummary;

    @Transient
    private CustomerUser author;

    @Transient
    private boolean like;

    @Transient
    private FitnessSummarys summarys;

    public Rank(){

    }

    public Rank(Builder builder){
        this.authorId = builder.authorId;
        this.dateTime = builder.dateTime;
        this.fitnessSummary = builder.fitnessSummary;
        this.followers = builder.followers;
        this.steps = builder.steps;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public long getSteps() {
        return steps;
    }

    public void setSteps(long steps) {
        this.steps = steps;
    }

    public List<String> getFollowers() {
        return followers;
    }

    public void setFollowers(List<String> followers) {
        this.followers = followers;
    }

    public boolean isLike() {
        return like;
    }

    public void setLike(boolean like) {
        this.like = like;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public FitnessSummary getFitnessSummary() {
        return fitnessSummary;
    }

    public void setFitnessSummary(FitnessSummary fitnessSummary) {
        this.fitnessSummary = fitnessSummary;
    }

    public CustomerUser getAuthor() {
        return author;
    }

    public void setAuthor(CustomerUser author) {
        this.author = author;
    }

    public FitnessSummarys getSummarys() {
        return summarys;
    }

    public void setSummarys(FitnessSummarys summarys) {
        this.summarys = summarys;
    }

    public static class Builder {
        private String authorId;

        private List<String> followers;

        private long dateTime;

        private long steps;

        private FitnessSummary fitnessSummary;

        public Builder authorId(String authorId) {
            this.authorId = authorId;
            return this;
        }

        public Builder steps(long steps){
            this.steps = steps;
            return this;
        }

        public Builder followers(List<String> followers) {
            this.followers = followers;
            return this;
        }

        public Builder dateTime(long dateTime) {
            this.dateTime = dateTime;
            return this;
        }

        public Builder fitnessSummary(FitnessSummary fitnessSummary) {
            this.fitnessSummary = fitnessSummary;
            return this;
        }

        public Rank build(){
            return new Rank(this);
        }
    }
}
