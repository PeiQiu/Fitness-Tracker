package com.misfit.cloudapisdk.utils;

import com.jawbone.upplatformsdk.utils.UpPlatformSdkConstants;

/**
 * Created by peiqiutian on 11/07/2017.
 */
public class CloudApi {
    public static final String URI_SCHEME = "https";
    public static final String AUTHORITY = "api.misfitwearables.com";

    public static final String API_VERSION = "version";
    public static final String API_VERSION_STRING = "v1";
    //public static final String uid = "uid";

    public static final String UP_PLATFORM_ACCESS_TOKEN = "access_token";
    public static final String UP_PLATFORM_REFRESH_TOKEN = "refresh_token";

    public static final String AUTH_URI = "auth_uri";
    public static final String ACCESS_CODE = "code";
    public static final String CLIENT_SECRET = "client_secret";
    //public static final int JAWBONE_AUTHORIZE_REQUEST_CODE = 120501;
    public static final String API_URL = "https://api.misfitwearables.com";

    /**
     * Different types of API permissions that can be requested, defined as an enum
     */
    public enum CloudAuthScope {
        PROFILE,
        DEVICE,
        GOAL,
        SUMMARY,
        SESSION,
        SLEEP,
        ALL;
    }

    /**
     * Different API calls, defined in an enum, to facilitate reuse.
     */
    public static enum RestApiRequestType {
        GET_PROFILE("Get profile of a user"), //0
        GET_DEVICE("Get device’s information"), //1
        GET_GOAL("Get goals of a user in the period"), //2
        GET_SUMMARY("Get sleep of a user’s activity in the period"),//3
        GET_SESSION("Get sessions of a user in the time range"),//4
        GET_SLEEP("Get sleep sessions of a user in the time range");//5


        private String displayTitle;

        RestApiRequestType(String s) {
            this.displayTitle = s;
        }

        @Override
        public String toString() {
            return displayTitle;
        }

        public static final int size = CloudApi.RestApiRequestType.values().length;
    }
}


