package com.misfit.cloudapisdk.datamodel;


/**
 * Created by peiqiutian on 13/07/2017.
 */
public class AccessTokenParameters {
    private String grant_type;
    private String code;
    private String client_id;
    private String client_secret;
    private String redirect_uri;

    public AccessTokenParameters(Builder builder) {
        this.grant_type = builder.grant_type;
        this.code = builder.code;
        this.client_id = builder.client_id;
        this.client_secret = builder.client_secret;
        this.redirect_uri = builder.redirect_uri;
    }

    public String getGrant_type() {
        return grant_type;
    }

    public void setGrant_type(String grant_type) {
        this.grant_type = grant_type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getClient_secret() {
        return client_secret;
    }

    public void setClient_secret(String client_secret) {
        this.client_secret = client_secret;
    }

    public String getRedirect_uri() {
        return redirect_uri;
    }

    public void setRedirect_uri(String redirect_uri) {
        this.redirect_uri = redirect_uri;
    }

    public static class Builder{
        private String grant_type;
        private String code;
        private String client_id;
        private String client_secret;
        private String redirect_uri;

        public Builder Grant_type(String grant_type) {
            this.grant_type = grant_type;
            return  this;
        }

        public Builder Code(String code) {
            this.code = code;
            return this;
        }

        public Builder Client_id(String client_id) {
            this.client_id = client_id;
            return this;
        }

        public Builder Client_secret(String client_secret) {
            this.client_secret = client_secret;
            return this;
        }

        public Builder Redirect_uri(String redirect_uri) {
            this.redirect_uri = redirect_uri;
            return this;
        }

        public AccessTokenParameters buid(){
            return new AccessTokenParameters(this);
        }
    }
}
