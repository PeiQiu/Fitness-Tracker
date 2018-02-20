package com.example.demo.controller;

import com.example.demo.domain.*;
import com.example.demo.services.ConnectToMisfitApiService;
import com.example.demo.services.ConnectToJawboneApiService;
import com.example.demo.services.UserService;
import com.jawbone.upplatformsdk.datamodel.JawboneCommonParameter;
import com.jawbone.upplatformsdk.datamodel.JawboneGoals;
import com.jawbone.upplatformsdk.utils.UpPlatformSdkConstants;
import com.misfit.cloudapisdk.datamodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.net.URISyntaxException;
import java.util.Date;
import java.util.HashMap;

@RestController
@RequestMapping("/fitnesstracker/api/v1")
@PreAuthorize("hasPermission(#uid, 'Role', 'ROLE_USER')")
public class ApiController {

    static String MISFIT = "misfit";
    static String JABONE = "jawbone";


    @Autowired
    private ConnectToJawboneApiService jawboneAPI;

    @Autowired
    private ConnectToMisfitApiService misfitAPI;

    @Autowired
    private UserService userService;


    @RequestMapping(value = "/user/{uid}/jawbone", method = RequestMethod.GET)
    public String accessJawboneAPI(@PathVariable String uid){
        String url = null;
        try {
            url = jawboneAPI.getJawboneAPI().build().toString();
            System.out.println(url);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return "\""+url+"\"";
    }

    @RequestMapping(value = "/user/{uid}/misfit", method = RequestMethod.GET)
    public String accessMisfitAPI(@PathVariable String uid){
        String url = null;
        try{
            url = misfitAPI.getMisfitApi().build().toString();
        }catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return "\""+url+"\"";
    }

    @RequestMapping(value = "/user/{uid}/device", method = RequestMethod.DELETE)
    public User disconnectDivice(@PathVariable String uid) throws Exception {
        User user = userService.loadUserByUserId(uid);
        user.setDevice(null);
        user.setAccessCode(null);
        user.setAccessCodeStartTime(0);
        return userService.Save(user);
    }

    @RequestMapping(value = "/user/{uid}/device", method = RequestMethod.PUT)
    public User updateUserWithDeviceAndAccessCode(@PathVariable String uid, @RequestBody DeviceParam deviceParam) throws Exception {
        User user = userService.device( uid, deviceParam.getDevice(), deviceParam.getAccessCode(), new Date().getTime());
        System.out.println("update username : " + user.getUsername());
        return user;
    }

    @RequestMapping(value = "/user/{uid}/misfit/profile", method = RequestMethod.GET)
    public MisfitProfile getMisfitProfile(@PathVariable String uid) throws Exception {
        User user = userService.loadUserByUserId(uid);
        return misfitAPI.getMisfitProfile(user.getAccessCode(), new HashMap<>());
    }

    @RequestMapping(value = "/user/{uid}/misfit/device", method = RequestMethod.GET)
    public MisfitDevice getMisfitDevice(@PathVariable String uid) throws Exception {
        User user = userService.loadUserByUserId(uid);
        System.out.println(user.getAccessCode());
        return misfitAPI.getMisfitDevice( user.getAccessCode(), new HashMap<>());
    }


    @RequestMapping(value = "/user/{uid}/goals", method = RequestMethod.GET)
    public FitnessGoals getGoals(@PathVariable String uid, @RequestParam("device") String device, @RequestParam("start") @DateTimeFormat(pattern = "yyyy-MM-dd") Date start, @RequestParam("end") @DateTimeFormat(pattern = "yyyy-MM-dd") Date end) throws Exception {
        User user = userService.loadUserByUserId(uid);
        MisfitCommitParam misfitCommitParam = new MisfitCommitParam.Builder().start(start).end(end).build();
        System.out.println("device : " + device);
        if(device.equals(MISFIT)){
            System.out.println("----------------  enter misfit");
            MisfitGoals misfitGoals = misfitAPI.getMisfitGoal(user.getAccessCode(), misfitCommitParam);
            return new FitnessGoals(misfitGoals);
        }else{
            System.out.println("----------------- enter jawbone");
            JawboneGoals jawboneGoals = jawboneAPI.getJawboneGoals(user.getAccessCode());
            return new FitnessGoals(jawboneGoals);
        }
    }


    @RequestMapping(value = "/user/{uid}/sleeps", method = RequestMethod.GET)
    public FitnessSleeps getSleep(@PathVariable String uid, @RequestParam("device") String device,@RequestParam("start") @DateTimeFormat(pattern = "yyyy-MM-dd") Date start, @RequestParam("end") @DateTimeFormat(pattern = "yyyy-MM-dd") Date end) throws Exception {
        FitnessSleeps fitnessSleeps = null;
        User user = userService.loadUserByUserId(uid);
        if(device.equals(MISFIT)){
            MisfitCommitParam misfitCommitParam = new MisfitCommitParam.Builder().start(start).end(end).build();
            fitnessSleeps = misfitAPI.getMisfitSleep(user.getAccessCode(), misfitCommitParam);
        }else{
            JawboneCommonParameter jawboneCommonParameter = new JawboneCommonParameter.Builder().Start(start).End(end).Details(true).build();
            fitnessSleeps = jawboneAPI.getJawboneSleeps(user.getAccessCode(), jawboneCommonParameter);
        }
        return fitnessSleeps;
    }

    @RequestMapping(value = "/user/{uid}/session", method = RequestMethod.GET)
    public FitnessSessions getSession(@PathVariable String uid, @RequestParam("device") String device, @RequestParam("start") @DateTimeFormat(pattern = "yyyy-MM-dd") Date start, @RequestParam("end") @DateTimeFormat(pattern = "yyyy-MM-dd") Date end) throws Exception {
        User user = userService.loadUserByUserId(uid);
        FitnessSessions fitnessSessions = null;
        System.out.println("start" + start.toString() + "end" + end.getTime());
        if(device.equals(MISFIT)){
            MisfitCommitParam misfitCommitParam = new MisfitCommitParam.Builder().start(start).end(end).device(device).build();
            fitnessSessions = misfitAPI.getMisfitSession(user.getAccessCode(), misfitCommitParam);
        }else{
            JawboneCommonParameter jawboneCommonParameter = new JawboneCommonParameter.Builder().Start(start).End(end).build();
            fitnessSessions = jawboneAPI.getJawboneSession(user.getAccessCode(), jawboneCommonParameter);
        }
        return fitnessSessions;
    }
    @RequestMapping(value = "/user/{uid}/summary", method = RequestMethod.GET)
    public FitnessSummary getSummary(@PathVariable String uid, @RequestParam("device") String device, @RequestParam("start") @DateTimeFormat(pattern = "yyyy-MM-dd") Date start, @RequestParam("end") @DateTimeFormat(pattern = "yyyy-MM-dd") Date end) throws Exception {
        User user = userService.loadUserByUserId(uid);
        FitnessSummary fitnessSummary = null;
        if(device.equals(MISFIT)){
            MisfitCommitParam misfitCommitParam = new MisfitCommitParam.Builder().start(start).end(end).build();
            fitnessSummary =  misfitAPI.getMisfitSummary(user.getAccessCode(), misfitCommitParam);
        }else{
            JawboneCommonParameter jawboneCommonParameter = new JawboneCommonParameter.Builder().Start(start).End(end).build();
            fitnessSummary = jawboneAPI.getJawboneSummary(user.getAccessCode(), jawboneCommonParameter);
        }
        return fitnessSummary;
    }

    @RequestMapping(value = "/user/{uid}/summary/detail", method = RequestMethod.GET)
    public FitnessSummarys getSummarys(@PathVariable String uid, @RequestParam("device") String device, @RequestParam("start") @DateTimeFormat(pattern = "yyyy-MM-dd") Date start, @RequestParam("end") @DateTimeFormat(pattern = "yyyy-MM-dd") Date end) throws Exception {
        User user = userService.loadUserByUserId(uid);
        FitnessSummarys fitnessSummarys = null;
        if(device.equals(MISFIT)){
            MisfitCommitParam misfitCommitParam = new MisfitCommitParam.Builder().start(start).end(end).detail(true).build();
            fitnessSummarys =  misfitAPI.getMisfitSummaries(user.getAccessCode(), misfitCommitParam);
        }else{
            JawboneCommonParameter jawboneCommonParameter = new JawboneCommonParameter.Builder().Start(start).End(end).build();
            fitnessSummarys = jawboneAPI.getJawboneSummaries(user.getAccessCode(), jawboneCommonParameter);
        }
        return fitnessSummarys;
    }
}
