package com.example.demo.services;

import com.example.demo.domain.FitnessSummary;
import com.example.demo.domain.sessions.FitnessSession;
import com.example.demo.domain.sleeps.FitnessSleep;
import com.jawbone.upplatformsdk.api.ApiManager;
import com.jawbone.upplatformsdk.api.response.OauthAccessTokenResponse;
import com.jawbone.upplatformsdk.datamodel.*;
import com.jawbone.upplatformsdk.datamodel.SleepsData.*;
import com.jawbone.upplatformsdk.datamodel.Summary.JawboneMove;
import com.jawbone.upplatformsdk.datamodel.Summary.JawboneMoveItem;
import com.jawbone.upplatformsdk.datamodel.Summary.JawboneMoveItemDetails;
import com.jawbone.upplatformsdk.datamodel.WorkoutsData.WorkoutDataItem;
import com.jawbone.upplatformsdk.datamodel.WorkoutsData.WorkoutDataItemDetails;
import com.jawbone.upplatformsdk.datamodel.WorkoutsData.WorkoutDatas;
import com.jawbone.upplatformsdk.oauth.OauthUtils;
import com.jawbone.upplatformsdk.utils.UpPlatformSdkConstants;

import org.apache.http.client.utils.URIBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class ConnectToJawboneApiService {
    private static final String CLIENT_ID = "2gMRzsHVJlM";
    private static final String CLIENT_SECRET = "aa3c71600da94350ddd206da1bbdc9dfd9156083";
    private static final String OAUTH_CALLBACK_URL = "http://localhost:8080/fitnesstracker/api/v1/jawbone/access?";
    private List<UpPlatformSdkConstants.UpPlatformAuthScope> authScope;

    public ConnectToJawboneApiService(){
        authScope = new ArrayList<>();
        authScope.add(UpPlatformSdkConstants.UpPlatformAuthScope.ALL);
    }

    public URIBuilder getJawboneAPI(){
        URIBuilder  builder = OauthUtils.setOauthParameters(CLIENT_ID, OAUTH_CALLBACK_URL, authScope);
        return builder;
    }

    public String getAccessToken(String code){
        ApiManager.getRequestInterceptor().clearAccessToken();
        OauthAccessTokenResponse  oauthAccessTokenResponse =
                ApiManager.getRestApiInterface().getAccessToken(CLIENT_ID, CLIENT_SECRET, code);
        if(oauthAccessTokenResponse == null){
//            System.out.println("accessToken not returned by Oauth call, exiting...");
            return null;
        }
        return oauthAccessTokenResponse.access_token;
    }

    private void setAccessToken( String accessCode){
        ApiManager.getRequestInterceptor().setAccessToken(accessCode);
    }

    public JawboneGoals getJawboneGoals(String accessToken){
        setAccessToken(accessToken);
        return (JawboneGoals) executeRestApi(UpPlatformSdkConstants.RestApiRequestType.GET_GOALS, new JawboneCommonParameter());
    }

    public JawboneSleepDataOutputFormat getJawboneSleeps(String accessToken, JawboneCommonParameter jawboneCommonParameter){
        setAccessToken(accessToken);
        List<FitnessSleep> ans = new ArrayList<>();
        JawboneSleeps jawboneSleeps =  (JawboneSleeps) executeRestApi(UpPlatformSdkConstants.RestApiRequestType.GET_SLEEP_EVENTS_LIST, jawboneCommonParameter);
        SleepsResponseBodyData sleepsResponseBodyData = jawboneSleeps.getData();
        Links links = sleepsResponseBodyData.getLinks();
//        if(links != null){
//            System.out.println( "========jawbone links : " + links.getNext());
//        }
        List<SleepsItem> list = sleepsResponseBodyData.getItems();
//        System.out.println("--------------dates size " + list.size() + " size : " + sleepsResponseBodyData.getSize());
        for(int i =0; i < list.size(); i ++){
            // sleep item ----------------
            SleepsItem sleepsItem = list.get(i);
//            System.out.println("------------xid : " + sleepsItem.getXid() + "  :  date-- " + sleepsItem.getDate() + " duration : : " + sleepsItem.getDetails().getDuration());
            List<SleepPhasesItem> jawboneSleepTotalPhase = new ArrayList<>();
            if(jawboneCommonParameter.isDetails()) {
                jawboneCommonParameter.setXid(sleepsItem.getXid());
                //sleep phases ------------
                SleepPhases sleepPhases = (SleepPhases) executeRestApi(UpPlatformSdkConstants.RestApiRequestType.GET_SLEEP_TICKS, jawboneCommonParameter);
                SleepPhasesResponseBody sleepPhasesResponseBody = sleepPhases.getData();
                List<SleepPhasesItem> phases = sleepPhasesResponseBody.getItems();
                jawboneSleepTotalPhase.addAll(phases);
            }
            JawboneSleepDataItemOutputFormat jawboneSleepDataItemOutputFormat
                    = new JawboneSleepDataItemOutputFormat.Builder()
                            .SleepDetails(jawboneSleepTotalPhase)
                            .Id(sleepsItem.getXid()).Duration(sleepsItem.getDetails().getDuration())
                            .StartTime(new Date(sleepsItem.getTime_created()*1000)).build();
            ans.add(jawboneSleepDataItemOutputFormat);
        }
        JawboneSleepDataOutputFormat jawboneSleepDataOutputFormat = new JawboneSleepDataOutputFormat();
        jawboneSleepDataOutputFormat.setSleeps(ans);
        return jawboneSleepDataOutputFormat;
    }





    public JawboneOutputActivesData getJawboneSession(String accessToken, JawboneCommonParameter jawboneCommonParameter){
        setAccessToken(accessToken);
        WorkoutDatas workoutDatas = (WorkoutDatas) executeRestApi(UpPlatformSdkConstants.RestApiRequestType.GET_WORKOUTS_EVENTS_LIST, jawboneCommonParameter);
//        System.out.println("items size : " + workoutDatas.getData().getSize());
        List<WorkoutDataItem> listItems = workoutDatas.getData().getItems();
        List<FitnessSession> sessions = new ArrayList<>();
        for(WorkoutDataItem item : listItems){
//            System.out.println("---- item id : " + item.getXid() + " start time : " + item.getTime_created() + " date : " + new Date(item.getTime_created()*1000).toString() );

            WorkoutDataItemDetails workoutDataItemDetails = item.getDetails();

            JawboneOutputActiveData jawboneOutputActiveData =
                    new JawboneOutputActiveData.Builder()
                            .activityType(item.getSub_type())
                            .id(item.getXid())
                            .calories(workoutDataItemDetails.getCalories())
                            .distance(workoutDataItemDetails.getMeters())
                            .duration(workoutDataItemDetails.getTime())
                            .startTime(new Date(item.getTime_created()*1000))
                            .steps(workoutDataItemDetails.getSteps()).build();

            sessions.add(jawboneOutputActiveData);
        }
        JawboneOutputActivesData jawboneOutputActivesData = new JawboneOutputActivesData();
        jawboneOutputActivesData.setSessions(sessions);
        return jawboneOutputActivesData;
    }



    public JawboneOutputSummaryData getJawboneSummary(String accessCode , JawboneCommonParameter jawboneCommonParameter){
        setAccessToken(accessCode);
        JawboneMove jawboneMove = (JawboneMove) executeRestApi(UpPlatformSdkConstants.RestApiRequestType.GET_MOVES_EVENTS_LIST, jawboneCommonParameter);
        Links links = jawboneMove.getData().getLinks();
//        if(links != null){
//            System.out.println( "========jawbone links : " + links.getNext());
//        }

        List<JawboneMoveItem> moveItems = jawboneMove.getData().getItems();
        Float defaultValue = new Float(0);
        JawboneOutputSummaryData jawboneOutputSummaryData = new JawboneOutputSummaryData.Builder()
                .activityCalories(defaultValue)
                .calories(defaultValue)
                .distance(defaultValue)
                .steps((long) 0).build();
//        System.out.println(" >>>>>>>> size number : " + moveItems.size());
        for(JawboneMoveItem jawboneMoveItem : moveItems){
//            System.out.println("------- start time : " + jawboneMoveItem.getDate() + "time create : " + new Date(jawboneMoveItem.getTime_created()*1000).toString());
            JawboneMoveItemDetails jawboneMoveItemDetails = jawboneMoveItem.getDetails();
            jawboneOutputSummaryData.setActivityCalories(jawboneOutputSummaryData.getActivityCalories() + jawboneMoveItemDetails.getWo_calories());
            jawboneOutputSummaryData.setCalories(jawboneOutputSummaryData.getCalories() + jawboneMoveItemDetails.getCalories());
            jawboneOutputSummaryData.setDistance(jawboneOutputSummaryData.getDistance() + jawboneMoveItemDetails.getDistance());
            jawboneOutputSummaryData.setSteps(jawboneOutputSummaryData.getSteps() + jawboneMoveItemDetails.getSteps());
    }
        return jawboneOutputSummaryData;
    }

    public JawboneOutputSummarysData getJawboneSummaries(String accessCode, JawboneCommonParameter jawboneCommonParameter){
        setAccessToken(accessCode);
        JawboneMove jawboneMove = (JawboneMove) executeRestApi(UpPlatformSdkConstants.RestApiRequestType.GET_MOVES_EVENTS_LIST, jawboneCommonParameter);
        List<JawboneMoveItem> moveItems = jawboneMove.getData().getItems();
        List<FitnessSummary> summaries = new ArrayList<>();
        for(JawboneMoveItem jawboneMoveItem : moveItems){
            JawboneMoveItemDetails jawboneMoveItemDetails = jawboneMoveItem.getDetails();
            JawboneOutputSummaryData jawboneOutputSummaryData =
                    new JawboneOutputSummaryData.Builder()
                            .activityCalories(jawboneMoveItemDetails.getWo_calories())
                            .calories(jawboneMoveItemDetails.getCalories())
                            .date(new Date(jawboneMoveItem.getTime_created()*1000))
                            .distance(jawboneMoveItemDetails.getDistance())
                            .steps(jawboneMoveItemDetails.getSteps()).build();
            summaries.add(jawboneOutputSummaryData);
        }
        JawboneOutputSummarysData jawboneOutputSummarysData = new JawboneOutputSummarysData();
        jawboneOutputSummarysData.setSummary(summaries);
        return jawboneOutputSummarysData;
    }











    public Object executeRestApi(UpPlatformSdkConstants.RestApiRequestType apiRequestType, JawboneCommonParameter jawboneCommonParameter){

        Object obj = null;
        switch (apiRequestType) {
            case GET_MEALS_EVENTS_LIST:
                System.out.println("making Get Meal Events List api call ...");
                ApiManager.getRestApiInterface().getMealEventsList(
                        UpPlatformSdkConstants.API_VERSION_STRING,
                        getMealEventsListRequestParams()
                );
                break;
            case GET_MEALS_EVENT:
                System.out.println("making Get Meal Event api call ...");
                ApiManager.getRestApiInterface().getMealEvent(
                        UpPlatformSdkConstants.API_VERSION_STRING,
                        "JtN269m6S_xmX72fwD63cg" //hardcoded value, should be dynamic
                );
                break;
            case DELETE_MEAL:
                System.out.println( "making Delete Meal api call ...");
                // note, you can only delete meals that you created
                // so first create it then delete
                ApiManager.getRestApiInterface().deleteMealEvent(
                        UpPlatformSdkConstants.API_VERSION_STRING,
                        "O-IjYEIQFz6dU0FWTA0U-w" //hardcoded value, should be dynamic
                        //
                );
                break;
            case CREATE_MEAL:
                System.out.println( "making Create Meal api call ...");
                ApiManager.getRestApiInterface().createMealEvent(
                        UpPlatformSdkConstants.API_VERSION_STRING,
                        getCreateOrUpdateMealEventRequestParams()
                        //
                );
                break;
            case UPDATE_MEAL:
                System.out.println( "making Update Meal api call ...");
                ApiManager.getRestApiInterface().updateMealEvent(
                        UpPlatformSdkConstants.API_VERSION_STRING,
                        "JtN269m6S_xmX72fwD63cg", //hardcoded value, should be dynamic
                        getCreateOrUpdateMealEventRequestParams()
                        //
                );
                break;
            case GET_MOVES_EVENTS_LIST:
                System.out.println( "making Get Move Events List api call ...");
                obj = ApiManager.getRestApiInterface().getMoveEventsList(
                        UpPlatformSdkConstants.API_VERSION_STRING,
                        getMoveEventsListRequestParams(jawboneCommonParameter)
                        //
                );
                break;
            case GET_MOVES_EVENT:
                System.out.println( "making Get Move Event api call ...");
                ApiManager.getRestApiInterface().getMoveEvent(
                        UpPlatformSdkConstants.API_VERSION_STRING,
                        "JtN269m6S_xR5T-blbnJBA" //hardcoded value, should be dynamic
                       // 
                );
                break;
            case GET_MOVES_GRAPH:
                System.out.println( "making Get Move Graph api call ...");
                ApiManager.getRestApiInterface().getMoveGraph(
                        UpPlatformSdkConstants.API_VERSION_STRING,
                        "wZ3pxuSAHA9mnOxjz3yw5w" //hardcoded value, should be dynamic
                       // 
                );
                break;
            case GET_MOVES_TICKS:
                System.out.println( "making Get Move Ticks api call ...");
                ApiManager.getRestApiInterface().getMoveTicks(
                        UpPlatformSdkConstants.API_VERSION_STRING,
                        "wZ3pxuSAHA9mnOxjz3yw5w" //hardcoded value, should be dynamic
                       // 
                );
                break;
            case GET_CUSTOM_EVENTS_LIST:
                System.out.println( "making Get Custom Events List api call ...");
                ApiManager.getRestApiInterface().getCustomEventsList(
                        UpPlatformSdkConstants.API_VERSION_STRING,
                        getCustomEventsListRequestParams()
                        //
                );
                break;
            case CREATE_CUSTOM_EVENT:
                System.out.println( "making Create Custom Event api call ...");
                ApiManager.getRestApiInterface().createCustomEvent(
                        UpPlatformSdkConstants.API_VERSION_STRING,
                        getCreateOrUpdateCustomEventRequestParams()
                        );
                break;
            case UPDATE_CUSTOM_EVENT:
                System.out.println( "making Update Custom Event api call ...");
                ApiManager.getRestApiInterface().updateCustomEvent(
                        UpPlatformSdkConstants.API_VERSION_STRING,
                        getCreateOrUpdateCustomEventRequestParams()
                        );
                break;
            case DELETE_CUSTOM_EVENT:
                System.out.println( "making Delete Custom Event api call ...");
                ApiManager.getRestApiInterface().deleteCustomEvent(
                        UpPlatformSdkConstants.API_VERSION_STRING,
                        "O-IjYEIQFz6dU0FWTA0U-w" //hardcoded value, should be dynamic
                        );
                break;
            case GET_WORKOUTS_EVENTS_LIST:
                System.out.println( "making Get Workout Events List api call ...");
                obj = ApiManager.getRestApiInterface().getWorkoutEventList(
                        UpPlatformSdkConstants.API_VERSION_STRING,
                        getWorkoutEventsListRequestParams(jawboneCommonParameter)
                        );
                break;
            case GET_WORKOUTS_EVENT:
                System.out.println( "making Get Workout Event api call ...");
                ApiManager.getRestApiInterface().getWorkoutEvent(
                        UpPlatformSdkConstants.API_VERSION_STRING,
                        "JtN269m6S_xYwnSu7WQdfA" //hardcoded value, should be dynamic
                        );
                break;
            case GET_WORKOUTS_GRAPH:
                System.out.println( "making Get Workout Graph api call ...");
                ApiManager.getRestApiInterface().getWorkoutGraph(
                        UpPlatformSdkConstants.API_VERSION_STRING,
                        "O-IjYEIQFz6dU0FWTA0U-w" //hardcoded value, should be dynamic
                        );
                break;
            case GET_WORKOUTS_TICKS:
                System.out.println( "making Get Workout Ticks api call ...");
                ApiManager.getRestApiInterface().getWorkoutTicks(
                        UpPlatformSdkConstants.API_VERSION_STRING,
                        "O-IjYEIQFz6dU0FWTA0U-w" //hardcoded value, should be dynamic
                        );
                break;
            case CREATE_WORKOUT_EVENT:
                System.out.println( "making Create Workout Event api call ...");
                ApiManager.getRestApiInterface().createWorkoutEvent(
                        UpPlatformSdkConstants.API_VERSION_STRING,
                        getCreateOrUpdateWorkoutEventRequestParams()
                        );
                break;
            case UPDATE_WORKOUT_EVENT:
                System.out.println( "making Update Workout Event api call ...");
                ApiManager.getRestApiInterface().updateWorkoutEvent(
                        UpPlatformSdkConstants.API_VERSION_STRING,
                        getCreateOrUpdateWorkoutEventRequestParams()
                        );
                break;
            case DELETE_WORKOUT_EVENT:
                System.out.println( "making Delete Workout Event api call ...");
                ApiManager.getRestApiInterface().deleteWorkoutEvent(
                        UpPlatformSdkConstants.API_VERSION_STRING,
                        "O-IjYEIQFz6dU0FWTA0U-w" //hardcoded value, should be dynamic
                        );
                break;
            case GET_SLEEP_EVENTS_LIST:
                System.out.println( "making Get Sleep Events List api call ...");
                obj = ApiManager.getRestApiInterface().getSleepEventsList(
                        UpPlatformSdkConstants.API_VERSION_STRING,
                        getSleepEventsListRequestParams( jawboneCommonParameter )
                        );
                break;
            case GET_SLEEP_EVENT:
                System.out.println( "making Get Sleep Event api call ...");
                ApiManager.getRestApiInterface().getSleepEvent(
                        UpPlatformSdkConstants.API_VERSION_STRING,
                        "Zmz9yIE9kk0zi4-n8juddg" //hardcoded value, should be dynamic
                        );
                break;
            case GET_SLEEP_GRAPH:
                System.out.println( "making Get Sleep Graph api call ...");
                ApiManager.getRestApiInterface().getSleepGraph(
                        UpPlatformSdkConstants.API_VERSION_STRING,
                        //"O-IjYEIQFz6dU0FWTA0U-w" //hardcoded value, should be dynamic
                        jawboneCommonParameter.getXid()
                        );
                break;
            case GET_SLEEP_TICKS:
                System.out.println( "making Get Sleep Ticks api call ...");
                obj = ApiManager.getRestApiInterface().getSleepPhases(
                        UpPlatformSdkConstants.API_VERSION_STRING,
                        //"O-IjYEIQFz6dU0FWTA0U-w" //hardcoded value, should be dynamic
                        jawboneCommonParameter.getXid()
                        );
                break;
            case CREATE_SLEEP_EVENT:
                System.out.println( "making Create Sleep Event api call ...");
                ApiManager.getRestApiInterface().createSleepEvent(
                        UpPlatformSdkConstants.API_VERSION_STRING,
                        getCreateSleepEventRequestParams()
                        );
                break;
            case DELETE_SLEEP_EVENT:
                System.out.println( "making Delete Sleep Event api call ...");
                ApiManager.getRestApiInterface().deleteSleepEvent(
                        UpPlatformSdkConstants.API_VERSION_STRING,
                        "O-IjYEIQFz6dU0FWTA0U-w" //hardcoded value, should be dynamic
                        );
                break;
            case GET_BODY_EVENTS_LIST:
                System.out.println( "making Get Body Events List api call ...");
                ApiManager.getRestApiInterface().getBodyEventsList(
                        UpPlatformSdkConstants.API_VERSION_STRING,
                        getBodyEventsListRequestParams()
                        );
                break;
            case GET_BODY_EVENT:
                System.out.println( "making Get Body Event api call ...");
                ApiManager.getRestApiInterface().getBodyEvent(
                        UpPlatformSdkConstants.API_VERSION_STRING,
                        "JtN269m6S_yjZabtdYhsJQ" //hardcoded value, should be dynamic
                        );
                break;
            case CREATE_BODY_EVENT:
                System.out.println( "making Create Body Event api call ...");
                ApiManager.getRestApiInterface().createBodyEvent(
                        UpPlatformSdkConstants.API_VERSION_STRING,
                        getCreateBodyEventRequestParams()
                        );
                break;
            case DELETE_BODY_EVENT:
                System.out.println( "making Delete Body Event api call ...");
                ApiManager.getRestApiInterface().deleteBodyEvent(
                        UpPlatformSdkConstants.API_VERSION_STRING,
                        "O-IjYEIQFz6dU0FWTA0U-w" //hardcoded value, should be dynamic
                        );
                break;
            case GET_BAND_EVENTS:
                System.out.println( "making Get Band Events api call ...");
                ApiManager.getRestApiInterface().getBandEvents(
                        UpPlatformSdkConstants.API_VERSION_STRING,
                        getBandEventsRequestParams()
                        );
                break;
            case GET_GOALS:
                System.out.println( "making Get Users Goals api call ...");
                obj =  ApiManager.getRestApiInterface().getUsersGoals(UpPlatformSdkConstants.API_VERSION_STRING);
                break;
            case CREATE_OR_UPDATE_GOALS:
                System.out.println( "making Create or Update Users Goals api call ...");
                ApiManager.getRestApiInterface().createOrUpdateUsersGoals(
                        UpPlatformSdkConstants.API_VERSION_STRING,
                        getCreateOrUpdateUsersGoalsRequestParams()
                        );
                break;
            case GET_MOOD_EVENTS_LIST:
                System.out.println( "making Get Mood Events List api call ...");
                ApiManager.getRestApiInterface().getMoodEventsList(
                        UpPlatformSdkConstants.API_VERSION_STRING,
                        "20140908"
                        );
                break;
            case GET_MOOD_EVENT:
                System.out.println( "making Get Mood Event api call ...");
                ApiManager.getRestApiInterface().getMoodEvent(
                        UpPlatformSdkConstants.API_VERSION_STRING,
                        "O-IjYEIQFz6dU0FWTA0U-w" //hardcoded value, should be dynamic
                        );
                break;
            case CREATE_MOOD_EVENT:
                System.out.println( "making Create Mood Event api call ...");
                ApiManager.getRestApiInterface().createMoodEvent(
                        UpPlatformSdkConstants.API_VERSION_STRING,
                        getCreateMoodEventRequestParams()
                        );
                break;
            case DELETE_MOOD_EVENT:
                System.out.println( "making Delete Mood Event api call ...");
                ApiManager.getRestApiInterface().deleteMoodEvent(
                        UpPlatformSdkConstants.API_VERSION_STRING,
                        "O-IjYEIQFz6dU0FWTA0U-w" //hardcoded value, should be dynamic
                        );
                break;
            case GET_REFRESH_TOKEN:
                System.out.println( "making Get Refresh Token api call ...");
                if (CLIENT_SECRET != null) {
                    ApiManager.getRestApiInterface().getRefreshToken(
                            UpPlatformSdkConstants.API_VERSION_STRING,
                            CLIENT_SECRET
                            );
                } else {
                    System.out.println( "client secret is null, so api call is aborted..");
                }
                break;
            case GET_SETTINGS:
                System.out.println( "making Get User Settings api call ...");
                ApiManager.getRestApiInterface().getUserSettings(
                        UpPlatformSdkConstants.API_VERSION_STRING
                        );
                break;
            case GET_TIME_ZONE:
                System.out.println( "making Get Time Zone api call ...");
                ApiManager.getRestApiInterface().getTimeZone(
                        UpPlatformSdkConstants.API_VERSION_STRING,
                        getTimeZoneRequestParams()
                        );
                break;
            case GET_TRENDS:
                System.out.println(  "making Get Trends api call ...");
                ApiManager.getRestApiInterface().getTrends(
                        UpPlatformSdkConstants.API_VERSION_STRING,
                        getTrendsRequestParams()
                        );
                break;
            case GET_USER:
                System.out.println( "making Get User api call ...");
                ApiManager.getRestApiInterface().getUser(
                        UpPlatformSdkConstants.API_VERSION_STRING
                        );
                break;
            case GET_USERS_FRIENDS:
                System.out.println( "making Get Users Friends api call ...");
                ApiManager.getRestApiInterface().getUsersFriends(
                        UpPlatformSdkConstants.API_VERSION_STRING
                        );
                break;
            default:
                System.out.println( "api endpoint not yet defined!!" );
                break;
        }
        return obj;
    }



    private static HashMap<String, Object> getTrendsRequestParams() {
        HashMap<String, Object> queryHashMap = new HashMap<String, Object>();

//        //uncomment to add as needed parameters
//        queryHashMap.put("end_date", "<insert-date>");
//        queryHashMap.put("bucket_size", 50);
//        queryHashMap.put("num_buckets", 10);

        return queryHashMap;
    }

    private static HashMap<String, Integer> getTimeZoneRequestParams() {
        HashMap<String, Integer> queryHashMap = new HashMap<String, Integer>();

        //uncomment to add as needed parameters
//        queryHashMap.put("date", "<insert-date>");
//        queryHashMap.put("start_time", "<insert-time>");
//        queryHashMap.put("end_time", "<insert-time>");
//        queryHashMap.put("timestamp", "<insert-time>");

        return queryHashMap;
    }

    private static HashMap<String, Object> getCreateMoodEventRequestParams() {
        HashMap<String, Object> queryHashMap = new HashMap<String, Object>();

//        //uncomment to add as needed parameters
//        queryHashMap.put("title", "<insert-title>");
//        queryHashMap.put("sub_tye", 1);
//        queryHashMap.put("time_created", 1409967653);
//        queryHashMap.put("tz", null);
//        queryHashMap.put("share", false);

        return queryHashMap;
    }

    private static HashMap<String, Object> getCreateOrUpdateUsersGoalsRequestParams() {
        HashMap<String, Object> queryHashMap = new HashMap<String, Object>();

//        //uncomment to add as needed parameters
//        float placeHolder = new Float(0);
//        queryHashMap.put("move_steps", "<insert-steps>");
//        queryHashMap.put("sleep_total", "<insert-sleep>");
//        queryHashMap.put("body_weight", placeHolder);
//        queryHashMap.put("body_weight_intent", "<insert-intent>");

        return queryHashMap;
    }

    private static HashMap<String, Integer> getBandEventsRequestParams() {
        HashMap<String, Integer> queryHashMap = new HashMap<String, Integer>();

        //uncomment to add as needed parameters
//        queryHashMap.put("date", "<insert-date>");
//        queryHashMap.put("start_time", "<insert-time>");
//        queryHashMap.put("end_time", "<insert-time>");
//        queryHashMap.put("created_after", "<insert-time>");

        return queryHashMap;
    }

    private static HashMap<String, Object> getCreateBodyEventRequestParams() {
        HashMap<String, Object> queryHashMap = new HashMap<String, Object>();

//        //uncomment to add as needed parameters
//        float placeHolder = new Float(0);
//        queryHashMap.put("title", "<insert-title>");
//        queryHashMap.put("weight", placeHolder);
//        queryHashMap.put("body_fat", placeHolder);
//        queryHashMap.put("lean_mass", placeHolder);
//        queryHashMap.put("bmi", placeHolder);
//        queryHashMap.put("note", "<insert-note>");
//        queryHashMap.put("time_created", 1409967653);
//        queryHashMap.put("tz", null);
//        queryHashMap.put("share", false);

        return queryHashMap;
    }

    private static HashMap<String, Integer> getBodyEventsListRequestParams() {
        HashMap<String, Integer> queryHashMap = new HashMap<String, Integer>();

        //uncomment to add as needed parameters
//        queryHashMap.put("date", "<insert-date>");
//        queryHashMap.put("page_token", "<insert-page-token>");
//        queryHashMap.put("start_time", "<insert-time>");
//        queryHashMap.put("end_time", "<insert-time>");
//        queryHashMap.put("updated_after", "<insert-time>");
//        queryHashMap.put("limit", "<insert-limit>");

        return queryHashMap;
    }

    private static HashMap<String, Object> getCreateSleepEventRequestParams() {
        HashMap<String, Object> queryHashMap = new HashMap<String, Object>();

//        //uncomment to add as needed parameters
//        queryHashMap.put("time_created", 1);
//        queryHashMap.put("time_completed", 1);
//        queryHashMap.put("tz", null);
//        queryHashMap.put("share", false);

        queryHashMap.put("time_created", 1409967653);
        queryHashMap.put("time_completed", 1409967655);
        return queryHashMap;
    }

    private static HashMap<String, Long> getSleepEventsListRequestParams(JawboneCommonParameter jawboneCommonParameter) {
        HashMap<String, Long> queryHashMap = new HashMap<>();
//        System.out.println("integer time : " + jawboneCommonParameter.getStart().getTime() + "; time/1000 : " + jawboneCommonParameter.getStart().getTime()/1000 + "; int: " + (int)jawboneCommonParameter.getStart().getTime());
        queryHashMap.put("start_time", jawboneCommonParameter.getStart().getTime()/1000 );
        queryHashMap.put("end_time", jawboneCommonParameter.getEnd().getTime()/1000);
        queryHashMap.put("limit", (long) 30);
//      queryHashMap.put("updated_after", "<insert-time>");
        return queryHashMap;
    }

    private static HashMap<String, Object> getCreateOrUpdateWorkoutEventRequestParams() {
        HashMap<String, Object> queryHashMap = new HashMap<String, Object>();

//        //uncomment to add as needed parameters
//        File photo = new File(""); //path to image file
//        TypedFile typedFile = new TypedFile("application/octet-stream", photo);
//        float placeHolder = new Float(0);
//        queryHashMap.put("sub_type", 1);
//        queryHashMap.put("image_url", URI.create("<insert-uri>"));
//        queryHashMap.put("time_created", 1);
//        queryHashMap.put("time_completed", 1);
//        queryHashMap.put("place_lat", placeHolder);
//        queryHashMap.put("place_lon", placeHolder);
//        queryHashMap.put("place_acc", placeHolder);
//        queryHashMap.put("place_name", null);
//        queryHashMap.put("tz", null);
//        queryHashMap.put("share", false);
//        queryHashMap.put("calories", 100);
//        queryHashMap.put("distance", 700);
//        queryHashMap.put("intensity", 5);

        queryHashMap.put("sub_type", 1);
        return queryHashMap;
    }

    private static HashMap<String, Long> getWorkoutEventsListRequestParams(JawboneCommonParameter jawboneCommonParameter) {
        HashMap<String, Long> queryHashMap = new HashMap<>();

        //uncomment to add as needed parameters
//        queryHashMap.put("date", "<insert-date>");
//        queryHashMap.put("page_token", "<insert-page-token>");
        queryHashMap.put("start_time", jawboneCommonParameter.getStart().getTime()/1000);
        queryHashMap.put("end_time", jawboneCommonParameter.getEnd().getTime()/1000);
//        queryHashMap.put("updated_after", "<insert-time>");
//        queryHashMap.put("limit", "<insert-limit>");

        return queryHashMap;
    }

    private HashMap<String, Object> getCreateOrUpdateCustomEventRequestParams() {
        HashMap<String, Object> queryHashMap = new HashMap<String, Object>();

        //uncomment to add as needed parameters
//        Object jsonObject = null;
//        float placeHolder = new Float(0);
//        queryHashMap.put("title", "<insert-title>");
//        queryHashMap.put("verb", "<insert-verb>");
//        queryHashMap.put("attributes", jsonObject);
//        queryHashMap.put("image_url", URI.create("<insert-uri>"));
//        queryHashMap.put("place_lat", placeHolder);
//        queryHashMap.put("place_lon", placeHolder);
//        queryHashMap.put("place_acc", placeHolder);
//        queryHashMap.put("place_name", null);
//        queryHashMap.put("time_created", 1);
//        queryHashMap.put("tz", null);
//        queryHashMap.put("share", false);

        return queryHashMap;
    }

    private static HashMap<String, Integer> getCustomEventsListRequestParams() {
        HashMap<String, Integer> queryHashMap = new HashMap<String, Integer>();

        //uncomment to add as needed parameters
//        queryHashMap.put("date", "<insert-date>");
//        queryHashMap.put("page_token", "<insert-page-token>");
//        queryHashMap.put("start_time", "<insert-time>");
//        queryHashMap.put("end_time", "<insert-time>");
//        queryHashMap.put("updated_after", "<insert-time>");
//        queryHashMap.put("limit", "<insert-limit>");

        return queryHashMap;
    }

    private static HashMap<String, Long> getMoveEventsListRequestParams(JawboneCommonParameter jawboneCommonParameter) {
        HashMap<String, Long> queryHashMap = new HashMap<>();
        //uncomment to add as needed parameters
//        queryHashMap.put("date", "<insert-date>");
//        queryHashMap.put("page_token", "<insert-page-token>");
        queryHashMap.put("start_time", jawboneCommonParameter.getStart().getTime()/1000);
        queryHashMap.put("end_time", jawboneCommonParameter.getEnd().getTime()/1000);
        System.out.println("----- limit number : " + (long) 30);
        queryHashMap.put("limit", (long) 30);
//        queryHashMap.put("updated_after", "<insert-time>");
        return queryHashMap;
    }

    private static HashMap<String, Object> getCreateOrUpdateMealEventRequestParams() {
        HashMap<String, Object> queryHashMap = new HashMap<String, Object>();

//        //uncomment to add as needed parameters
//        File photo = new File(""); //path to image file
//        TypedFile typedFile = new TypedFile("application/octet-stream", photo);
//        float placeHolder = new Float(0);
//        queryHashMap.put("note", "<insert-title>");
//        queryHashMap.put("sub_type", 1);
//        queryHashMap.put("image_url", URI.create("<insert-uri>"));
//        queryHashMap.put("photo", typedFile);
//        queryHashMap.put("place_lat", placeHolder);
//        queryHashMap.put("place_lon", placeHolder);
//        queryHashMap.put("place_acc", placeHolder);
//        queryHashMap.put("time_created", 1);
//        queryHashMap.put("place_name", null);
//        queryHashMap.put("tz", null);
//        queryHashMap.put("share", false);

        queryHashMap.put("note", "Create Meal 1");
        queryHashMap.put("sub_type", 3);
        return queryHashMap;
    }

    private static HashMap<String, Integer> getMealEventsListRequestParams() {
        HashMap<String, Integer> queryHashMap = new HashMap<String, Integer>();

        //uncomment to add as needed parameters
//        queryHashMap.put("date", "<insert-date>");
//        queryHashMap.put("page_token", "<insert-page-token>");
//        queryHashMap.put("start_time", "<insert-time>");
//        queryHashMap.put("end_time", "<insert-time>");
//        queryHashMap.put("updated_after", "<insert-time>");

        return queryHashMap;
    }


}
