package com.example.demo.domain;


import org.springframework.security.core.GrantedAuthority;

public class Role implements GrantedAuthority{

    private String authority;

    public Role() {

    }

    public Role(String name){
        this.authority = name;
    }
    public String getName(){
        return this.authority;
    }

    public void setName(String name){
        this.authority = name;
    }

    @Override
    public String getAuthority() {
        return this.authority;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((authority == null) ? 0 : authority.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Role other = (Role) obj;
        if (authority == null) {
            if (other.authority != null)
                return false;
        } else if (!authority.equals(other.authority))
            return false;
        return true;
    }



}
