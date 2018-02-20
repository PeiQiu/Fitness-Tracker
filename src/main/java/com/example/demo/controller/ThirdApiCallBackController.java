package com.example.demo.controller;

import com.example.demo.services.ConnectToJawboneApiService;
import com.example.demo.services.ConnectToMisfitApiService;
import com.example.demo.services.UserService;
import com.jawbone.upplatformsdk.utils.UpPlatformSdkConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@PreAuthorize("hasRole('ROLE_USER')")
@RequestMapping("/fitnesstracker/api/v1")
public class ThirdApiCallBackController {


    @Autowired
    private ConnectToJawboneApiService jawboneAPI;

    @Autowired
    private ConnectToMisfitApiService misfitAPI;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/jawbone/access", method = RequestMethod.GET)
    public ModelAndView getJawboneAccessCode(@RequestParam(value = UpPlatformSdkConstants.ACCESS_CODE, required = false) String code, ModelMap model) throws Exception {
//        System.out.println("jawbone access call-back error");
        if(code != null){
            String accessCode = jawboneAPI.getAccessToken(code);
            if(accessCode != null){
                return new ModelAndView("redirect:/#/selectdevice?success=true&device=jawbone&accessCode="+accessCode, model);
            }
        }
        return new ModelAndView("redirect:/#/selectdevice?success=false", model);
    }

    @RequestMapping(value = "/misfit/access", method = RequestMethod.GET)
    public ModelAndView getMisfitAccessCode(@RequestParam(value = "code", required = false) String code, ModelMap model) throws Exception {
        if(code != null){
            String accessCode = misfitAPI.getMisfitAccessToken(code);
            if( accessCode != null) {
                return new ModelAndView("redirect:/#/selectdevice?success=true&device=misfit&accessCode=" + accessCode, model);
            }
        }
        return new ModelAndView("redirect:/#/selectdevice?success=false", model);
    }
}
