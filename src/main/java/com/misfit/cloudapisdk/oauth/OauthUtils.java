package com.misfit.cloudapisdk.oauth;

import com.misfit.cloudapisdk.utils.CloudApi;
import org.apache.http.client.utils.URIBuilder;

import java.util.List;

/**
 * Created by peiqiutian on 11/07/2017.
 */
public class OauthUtils {

    public static URIBuilder setMisfitOauthParameters(String clientId, String callBackUrl, List<CloudApi.CloudAuthScope> scope){
        URIBuilder builder = setBaseParameterForMisFit();
        builder.setPath("/auth/dialog/authorize");
        builder.setParameter("response_type", "code");
        builder.setParameter("client_id", clientId);
        builder = setMisfitScopePArameter(scope, builder);
        builder.setParameter("redirect_uri", callBackUrl);
        return builder;
    }

    private static URIBuilder setMisfitScopePArameter(List<CloudApi.CloudAuthScope> scopeArrayList, URIBuilder builder) {
        StringBuilder scopeValues = new StringBuilder();

        for (CloudApi.CloudAuthScope  scope: scopeArrayList) {
            switch (scope) {
                case PROFILE:
                    scopeValues.append("public ");
                    break;
                case DEVICE:
                    scopeValues.append("email ");
                    break;
                case GOAL:
                    scopeValues.append("birthday ");
                    break;
                case SUMMARY:
                    scopeValues.append("tracking ");
                    break;
                case SESSION:
                    scopeValues.append("sessions ");
                    break;
                case SLEEP:
                    scopeValues.append("sleeps ");
                    break;
                case ALL:
                    scopeValues.append("public ");
                    scopeValues.append("email ");
                    scopeValues.append("birthday ");
                    scopeValues.append("tracking ");
                    scopeValues.append("sessions ");
                    scopeValues.append("sleeps ");
                    break;
                default:
                    scopeValues = null;
                    System.out.println("unknown scope:" + scope + ", setting it to null");
                    break;
            }
        }
        if (scopeValues != null && scopeValues.length() > 0) {
            scopeValues.setLength(scopeValues.length() - 1);
            builder.setParameter("scope", scopeValues.toString());
            return builder;
        } else {
            return builder;
        }
    }



    public static URIBuilder setBaseParameterForMisFit(){
        URIBuilder uriBuilder = new URIBuilder();
        uriBuilder.setScheme(CloudApi.URI_SCHEME);
        uriBuilder.setHost(CloudApi.AUTHORITY);
        return uriBuilder;
    }
}
