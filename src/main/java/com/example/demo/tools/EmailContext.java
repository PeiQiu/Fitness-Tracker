package com.example.demo.tools;

import com.example.demo.domain.User;

import java.net.InetAddress;
import java.net.UnknownHostException;


public class EmailContext {
    public static String setEmaiContext(User user, String email){
        InetAddress ips;
        String netWorkAddress;
        try {
            ips = InetAddress.getLocalHost();
            netWorkAddress = ips.getHostAddress();
            System.out.println("ips  :: " + ips + "  net work address :: " + netWorkAddress);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return null;
        }

        StringBuffer sb=new StringBuffer("Click the this link to activate the account, " +
                                            "effective time in the 48 hours, or re-register account, the link can only be used once, please activate as soon as possible!</br>");
        sb.append("<a href=\"http://"+netWorkAddress+":8080/active?email=");
        sb.append(email);
        sb.append("&validateCode=");
        sb.append(user.getValidateCode());
        sb.append("\">http://FitnessTracker/active?email=");
        sb.append(email);
        sb.append("&validateCode=");
        sb.append(user.getValidateCode());
        sb.append("</a>");
        return sb.toString();
    }
}
