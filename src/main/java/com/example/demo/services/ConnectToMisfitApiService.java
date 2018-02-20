package com.example.demo.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.misfit.cloudapisdk.api.ApiManager;
import com.misfit.cloudapisdk.api.response.AutheticationCode;
import com.misfit.cloudapisdk.datamodel.*;
import com.misfit.cloudapisdk.oauth.OauthUtils;
import com.misfit.cloudapisdk.utils.CloudApi;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.stereotype.Service;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class ConnectToMisfitApiService {
    private static final String APP_KEY = "WUW4LGKRwSBgTV5A";
    private static final String APP_SECRET = "jCZvzotgAoEF9IZ0oo2W4p8litJIQkNV";
    private static final String CALL_BACK_URI = "http://localhost:8080/fitnesstracker/api/v1/misfit/access?";
    private static final String BACK_URL = "http://localhost:8080";
    private List<CloudApi.CloudAuthScope> authScopes;
    private AutheticationCode autheticationCode;
    private Gson gson;

    public ConnectToMisfitApiService() {
        authScopes = new ArrayList<>();
        authScopes.add(CloudApi.CloudAuthScope.ALL);
        gson = new Gson();
    }

    public URIBuilder getMisfitApi() {
        URIBuilder builder = OauthUtils.setMisfitOauthParameters(APP_KEY, CALL_BACK_URI, authScopes);
        return builder;
    }

    public String getMisfitAccessToken(String code) {
//        System.out.println("try to get access token ..... ");
        AccessTokenParameters atp = new AccessTokenParameters.Builder()
                .Client_id(APP_KEY).Client_secret(APP_SECRET).Code(code)
                .Grant_type("authorization_code").Redirect_uri(CALL_BACK_URI).buid();
        ApiManager.getRestInterceptor().clearAccessToken();
        this.autheticationCode = ApiManager.getRestApiInterface().getAccessToken(
                atp
                //misfitAccessToken
        );
        if (autheticationCode != null) {
            //String w = executeRestApi(CloudApi.RestApiRequestType.GET_PROFILE);
            //System.out.println("String answer : " + w);
            return autheticationCode.access_token;
        } else {
            return null;
        }
    }

    public Object executeRestApi(CloudApi.RestApiRequestType apiRequestType, HashMap<String,Object> hashMap) {
        Object ans = null;
        switch (apiRequestType) {
            case GET_PROFILE:
                System.out.println("making Get Meal Events List api call ...");
                ans = ApiManager.getRestApiInterface().getProfile(
                        //genericCallbackListener
                );
                break;
            case GET_DEVICE:
                System.out.println("making Get Meal Event api call ...");
                ans = ApiManager.getRestApiInterface().getDevice(
                        //genericCallbackListener
                );
                break;
            case GET_GOAL:
                System.out.println("making Delete Meal api call ...");
                // note, you can only delete meals that you created
                // so first create it then delete
                ans = ApiManager.getRestApiInterface().getGoal(
                        hashMap
                        //getGoalParameters()
                       // genericCallbackListener
                );
                break;

            case GET_SESSION:
                System.out.println("making get session call ...");
                ans = ApiManager.getRestApiInterface().getSession(
                        hashMap
                        //getSessionParameters()
                        //genericCallbackListener
                );
                break;

            case GET_SLEEP:
                System.out.println("making get sleeps call ...");
                ans = ApiManager.getRestApiInterface().getSleep(
                        hashMap
                        //getSleepParameters()
                        //genericCallbackListener
                );
                break;

            case GET_SUMMARY:
                ans = ApiManager.getRestApiInterface().getSummary(
                        hashMap
                        //getSummaryParameters()
                        //genericCallbackListener
                );
                break;
            default:
                System.out.println(" have na such api call ...");
                break;
        }
        return ans;
    }

    private String transferDate(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }
    private HashMap<String,Object> getParameters(MisfitCommitParam misfitCommitParam) {
        HashMap<String, Object> queryHashMap = new HashMap<>();
        //uncomment to add as needed parameters
        queryHashMap.put("start_date", transferDate(misfitCommitParam.getStart()));
        queryHashMap.put("end_date", transferDate(misfitCommitParam.getEnd()));
        if(misfitCommitParam.isDetail()){
            queryHashMap.put("detail", true);
        }
        return queryHashMap;
    }

    public void setHeader(String access_token){
        ApiManager.getRestInterceptor().setAccessToken(access_token);
    }
    public MisfitProfile getMisfitProfile(String access_token, HashMap<String,Object> hashMap){
        setHeader( access_token );
        return (MisfitProfile)executeRestApi(CloudApi.RestApiRequestType.GET_PROFILE, hashMap);
    }

    public MisfitDevice getMisfitDevice(String access_token, HashMap<String,Object> hashMap){
        setHeader( access_token );
        Object obj = executeRestApi(CloudApi.RestApiRequestType.GET_DEVICE, hashMap);
        Type MisfitDeviceType = new TypeToken<MisfitDevice>() {}.getType();
        MisfitDevice device = gson.fromJson(obj.toString(), MisfitDeviceType);
//        System.out.println("object :: " + obj.toString() + "class :  " + obj.getClass());
        return device;
    }



    public MisfitGoals getMisfitGoal(String access_token, MisfitCommitParam misfitCommitParam){
        setHeader( access_token );
        Object obj = executeRestApi(CloudApi.RestApiRequestType.GET_GOAL, getParameters(misfitCommitParam));
        Type type = new TypeToken<MisfitGoals>() {}.getType();
        return gson.fromJson(obj.toString(), type);
    }

    public MisfitSleeps getMisfitSleep(String access_token, MisfitCommitParam misfitCommitParam){
        setHeader( access_token );
        Object obj = executeRestApi(CloudApi.RestApiRequestType.GET_SLEEP, getParameters(misfitCommitParam));
        return (MisfitSleeps)obj;
    }

    public MisfitSessions getMisfitSession(String access_token, MisfitCommitParam misfitCommitParam){
        setHeader( access_token );
        Object obj = executeRestApi(CloudApi.RestApiRequestType.GET_SESSION, getParameters(misfitCommitParam));
        return (MisfitSessions)obj;
    }

    public MisfitSummarys getMisfitSummaries(String access_token, MisfitCommitParam misfitCommitParam){
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        setHeader( access_token );
        Object obj = executeRestApi(CloudApi.RestApiRequestType.GET_SUMMARY, getParameters(misfitCommitParam));
//        System.out.println(" >>  " + obj.toString() + "  class :: " + obj.getClass());
        Type Type = new TypeToken<MisfitSummarys>() {}.getType();
        return gson.fromJson(obj.toString(), Type);
    }

    public MisfitSummary getMisfitSummary(String access_token, MisfitCommitParam misfitCommitParam){
        setHeader( access_token );
        Object obj = executeRestApi(CloudApi.RestApiRequestType.GET_SUMMARY, getParameters(misfitCommitParam));
//        System.out.println(" >>  " + obj.toString() + "  class :: " + obj.getClass());
        Type Type = new TypeToken<MisfitSummary>() {}.getType();
        return gson.fromJson(obj.toString(), Type);
    }







    Callback genericCallbackListener = new Callback<Object>() {
        @Override
        public void success(Object o, Response response) {
            System.out.println( "success response" + o.toString());
        }

        @Override
        public void failure(RetrofitError retrofitError) {
            System.out.println("error " + retrofitError.getMessage());
        }
    };
}