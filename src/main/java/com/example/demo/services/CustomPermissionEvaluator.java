package com.example.demo.services;

import com.example.demo.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.security.Principal;

/**
 * Created by peiqiutian on 15/11/2017.
 */
@Component
public class CustomPermissionEvaluator implements PermissionEvaluator{
    @Autowired
    private UserService userService;

    @Override
    public boolean hasPermission(Authentication auth, Object targetDomainObject, Object permission) {
        if ((auth == null) || (targetDomainObject == null) || !(permission instanceof String)){
            return false;
        }
        String targetType = targetDomainObject.getClass().getSimpleName().toUpperCase();
        return hasPrivilege(auth, targetType, permission.toString().toUpperCase());
    }

    @Override
    public boolean hasPermission(Authentication auth, Serializable targetId, String targetType, Object permission) {
//        System.out.println("authetication : " +  auth.getName() + "target id :" + targetId.toString() + " targetType : " + targetType + " permission : ");
        if((auth == null) || (targetType == null) || !(permission instanceof String) || targetId == null) {
                return false;
        }
        return varifyAthenticationUserId(auth, targetId.toString(), targetType.toUpperCase(), permission.toString().toUpperCase());
    }

    private boolean varifyAthenticationUserId(Authentication auth, String targetId, String targetType, String permission){
        for(GrantedAuthority grantedAuth : auth.getAuthorities()){
            if(grantedAuth.getAuthority().startsWith(targetType)){
                if(grantedAuth.getAuthority().contains(permission)){
//                    System.out.println("--------- user " + auth.getName());
                    User user = userService.loadUserByUsername(auth.getName());
//                    System.out.println("contains permission in authorities : : " + user.getId() + " <> " + user.getUsername());
                    return user.getId().equals(targetId);
                }
            }
        }
        return false;
    }

    private boolean hasPrivilege(Authentication auth, String targetType, String permission){
        for(GrantedAuthority grantedAuth : auth.getAuthorities()){
//            for(int i =0; i < grantedAuth.getAuthority().length(); i ++){
//                System.out.println(">>>>>>>>>>>>>>> grantedAuth : " + grantedAuth.getAuthority().toString() + " ; permission --- " + permission);
//            }
            if(grantedAuth.getAuthority().startsWith(targetType)){
                if(grantedAuth.getAuthority().contains(permission)){
                    return true;
                }
            }
        }
        return false;
    }
}
