package com.example.demo.domain;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by peiqiutian on 16/09/2017.
 */
@Document
public class Name {
    private String fistname;
    private String lastname;

    public Name(){

    }

    public String getFistname() {
        return fistname;
    }

    public void setFistname(String fistname) {
        this.fistname = fistname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
}
