package com.misfit.cloudapisdk.api;


import com.google.gson.GsonBuilder;
import com.misfit.cloudapisdk.utils.CloudApi;
import com.misfit.cloudapisdk.utils.DotNetDateConverter;
import retrofit.ErrorHandler;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.converter.GsonConverter;

import java.util.Date;

/**
 * Created by peiqiutian on 11/07/2017.
 */
public class ApiManager {
    private static RestAdapter restAdapter;
    private static RestApiInterface restApiInterface;
    private static ApiHeaders apiHeaders;

    private static RestAdapter getRestAdapter(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Date.class, new DotNetDateConverter());
        if(restAdapter == null){
            restAdapter = new RestAdapter.Builder()
                    .setConverter(new GsonConverter(gsonBuilder.create()))
                    .setRequestInterceptor(getRestInterceptor())
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .setErrorHandler(new CustomerErrorHandler())
                    .setEndpoint(CloudApi.API_URL)
                    .build();
        }
        return restAdapter;
    }

    private static class CustomerErrorHandler implements ErrorHandler {

        @Override
        public Throwable handleError(RetrofitError error) {
            return error.getCause();
        }
    }


    public static RestApiInterface getRestApiInterface(){
        restAdapter = getRestAdapter();
        if(restApiInterface == null){
            restApiInterface = restAdapter.create(RestApiInterface.class);
        }
        return restApiInterface;
    }

    public static ApiHeaders getRestInterceptor(){
        if(apiHeaders == null){
            apiHeaders = new ApiHeaders();
        }
        return apiHeaders;
    }

}
