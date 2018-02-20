package com.misfit.cloudapisdk.api;

import com.misfit.cloudapisdk.api.response.AutheticationCode;

import com.misfit.cloudapisdk.datamodel.*;
import retrofit.http.*;
import retrofit.Callback;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by peiqiutian on 11/07/2017.
 */

public interface RestApiInterface {

    //accessToken
    @POST("/auth/tokens/exchange")
    AutheticationCode getAccessToken(
            @Body AccessTokenParameters atp
            //Callback<AutheticationCode> response
    );

    //profile
    @GET("/move/resource/v1/user/me/profile")
    Object getProfile(
        //Callback<Object> response
    );

    //device
    @GET("/move/resource/v1/user/me/device")
    Object getDevice(
            //Callback<Object> response
    );

    //goal
    @GET("/move/resource/v1/user/me/activity/goals")
    Object getGoal(
            @QueryMap HashMap<String, Object> hashMap
            //Callback<Object> response
    );

    //sleep
    @GET("/move/resource/v1/user/me/activity/summary")
    Object getSummary(
            @QueryMap HashMap<String, Object> hashMap
            //Callback<Object> response
    );

    //session
    @GET("/move/resource/v1/user/me/activity/sessions")
    MisfitSessions getSession(
            @QueryMap HashMap<String, Object> hashMap
            //Callback<Object> response
    );

    //sleep
    @GET("/move/resource/v1/user/me/activity/sleeps")
    MisfitSleeps getSleep(
            @QueryMap HashMap<String, Object> hashMap
            //Callback<Object> response
    );

}
