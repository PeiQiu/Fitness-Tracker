package com.misfit.cloudapisdk.api;

import retrofit.RequestInterceptor;

/**
 * Created by peiqiutian on 11/07/2017.
 */

public class ApiHeaders implements RequestInterceptor{


    private String accessToken;

    public void setAccessToken(String at){
        this.accessToken = at;
    }

    public void clearAccessToken(){
        this.accessToken = null;
    }
    @Override
    public void intercept(RequestFacade requestFacade) {
        if(accessToken != null){
            requestFacade.addHeader("access_token", this.accessToken);
        }
        requestFacade.addHeader("Content-Type", "application/json");
    }
}
