package com.jawbone.upplatformsdk.utils;


public class ActivityType {
    public static String chooseType(int typeNumber){
        String type = "walk";
        switch (typeNumber) {
            case 1:
                type = "walk";
                break;
            case 2:
                type = "run";
                break;
            case 3:
                type = "lift weights";
                break;
            case 4:
                type = "cross train";
                break;
            case 5:
                type = "nike training";
                break;
            case 6:
                type = "yoga";
                break;
            case 7:
                type = "pilates";
                break;
            case 8:
                type = "body weight exercise";
                break;
            case 9:
                type = "crossfit";
                break;
            case 10:
                type = "p90x";
                break;
            case 11:
                type = "zumba";
                break;
            case 12:
                type = "trx";
                break;
            case 13:
                type = "swim";
                break;
            case 14:
                type = "bike";
                break;
            case 15:
                type = "elliptical";
                break;
            case 16:
                type = "bar method";
                break;
            case 17:
                type = "kinect exercises";
                break;
            case 18:
                type = "tennis";
                break;
            case 19:
                type = "basketball";
                break;
            case 20:
                type = "golf";
                break;
            case 21:
                type = "soccer";
                break;
            case 22:
                type = "ski snowboard";
                break;
            case 23:
                type = "dance";
                break;
            case 24:
                type = "hike";
                break;
            case 25:
                type = "cross country skiing";
                break;
            case 26:
                type = "stationary bike";
                break;
            case 27:
                type = "cardio";
                break;
            case 28:
                type = "game";
                break;
            default:
                type = "other";
                break;
        }
        return type;
    }
}
