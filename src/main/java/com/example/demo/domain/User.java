package com.example.demo.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Document
public class User implements UserDetails{
    @Id
    private String id;

    @Indexed(unique = true)
    private String username;

    @JsonProperty( access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private List<Role> authorities;

    private String firstname;

    private String lastname;

    private String email;

//    private String photo;

    private boolean status;

    private String phone;

    private String validateCode;

    private Date registerTime;

    private String device;

    private String accessCode;

    private long accessCodeStartTime;

    public User(){
    }

    public User (User user){

    }

    public User(Builder builder){

        this.firstname = builder.firstname;
        this.lastname = builder.lastname;
        this.username = builder.username;
        this.password = builder.password;
        this.status = builder.status;
        this.email = builder.email;
        this.phone = builder.phones;
        this.authorities = builder.authorities;
//        this.photo = builder.photo;
        this.validateCode = builder.validateCode;
        this.registerTime = builder.registerTime;
        this.accessCodeStartTime = builder.accessCodeStartTime;

    }

    public long getAccessCodeStartTime() {
        return accessCodeStartTime;
    }

    public void setAccessCodeStartTime(long accessCodeStartTime) {
        this.accessCodeStartTime = accessCodeStartTime;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getAccessCode() {
        return accessCode;
    }

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }

    public String getValidateCode() {
        return validateCode;
    }

    public void setValidateCode(String validateCode) {
        this.validateCode = validateCode;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAuthorities(List<Role> authorities) {
        this.authorities = authorities;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

//    public String getPhoto() {
//        return photo;
//    }
//
//    public void setPhoto(String photo) {
//        this.photo = photo;
//    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phones) {
        this.phone = phones;
    }

    public Date getLastActivateTime() {
        Calendar cl = Calendar.getInstance();
        cl.setTime(registerTime);
        cl.add(Calendar.DATE , 2);

        return cl.getTime();
    }

    public static class Builder{
        private String username;

        private String password;

        private List<Role> authorities;

        private String firstname;

        private String lastname;

        private String email;

//        private String photo;

        private boolean status;

        private String phones;

        private String validateCode;

        private Date registerTime;

        private String device;

        private String accessCode;

        private long accessCodeStartTime;
        public Builder(){

        }

        public Builder accessCodeStartTime(long accessCodeStartTime) {
            this.accessCodeStartTime = accessCodeStartTime;
            return this;
        }

        public Builder device(String device) {
            this.device = device;
            return this;
        }



        public Builder accessCode(String accessCode) {
            this.accessCode = accessCode;
            return this;
        }



        public Builder validateCode(String code){
            this.validateCode = code;
            return this;
        }

        public Builder registerDate(Date time){
            this.registerTime = time;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

//        public Builder photo(String photopath) {
//            this.photo = photopath;
//            return this;
//        }

        public Builder phones(String phones) {
            this.phones = phones;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder authorities(List<Role> authorities) {
            this.authorities = authorities;
            return this;
        }

        public Builder firstname(String firstname) {
            this.firstname = firstname;
            return this;
        }

        public Builder lastname(String lastname) {
            this.lastname = lastname;
            return this;
        }

        public Builder status(boolean status) {
            this.status = status;
            return this;
        }

        public User build(){
            return new User(this);
        }
    }



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.status;
    }
}
