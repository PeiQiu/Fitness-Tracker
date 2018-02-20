package com.example.demo.controller;


import com.example.demo.domain.User;
import com.example.demo.services.UserService;
import com.example.demo.tools.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

@RestController
@RequestMapping("/fitness/api/v1")

public class CommonApiController {

    public static final String FILE_PATH = "/avatar";
    @Autowired
    UserService userService;

    @PreAuthorize("hasPermission(#uid, 'Role', 'ROLE_USER') || hasPermission(#uid, 'Role', 'ROLE_ADMIN')")
    @ResponseBody
    @RequestMapping(value = "/user/{uid}/profile",method = RequestMethod.PUT)
    public User updateProfile(@PathVariable("uid") String uid,
                              @RequestBody User u){
        try {
            User user=userService.loadUserByUserId(uid);
//            System.out.println("#####################"+user.getUsername());
            if(user!=null){
                if(u.getFirstname()!=null){
                    user.setFirstname(u.getFirstname());
                }
                if(u.getLastname()!=null){
                    user.setLastname(u.getLastname());
                }
                if(u.getEmail()!=null){
                    user.setEmail(u.getEmail());
                }
                if(u.getPassword()!=null){
                    user.setPassword(u.getPassword());
                }
                if(u.getPhone()!=null){
                    user.setPhone(u.getPhone());
                }
                user = userService.Save(user);
                user.setPassword("");
                return user;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new User();
    }

    @PreAuthorize("hasPermission(#uid, 'Role', 'ROLE_USER') || hasPermission(#uid, 'Role', 'ROLE_ADMIN')")
    @RequestMapping(value = "/user/{uid}/avatars", method = RequestMethod.POST)
    @ResponseBody
    public Boolean uploadAvatar(@RequestParam("file") MultipartFile file, @PathVariable("uid") String uid, HttpSession session) throws IOException {
//        System.out.print("post picture !!");
        if (file.isEmpty()) return false;
        String fileNameOriginal = file.getOriginalFilename();
        String mimeType = session.getServletContext().getMimeType(fileNameOriginal);
        if (!mimeType.startsWith("image/")) {
            return false;
        }
        String type = ImageUtils.getImageType(file.getBytes());
        if (type.equals(ImageUtils.IMAGE_NOT_IMAGE)) {
            return false;
        }

        File tempFile = new File(System.getProperty("user.home") + FILE_PATH, uid + type);
        if (!tempFile.getParentFile().exists()) {
            tempFile.getParentFile().mkdir();
        }
        File file1 = new File(System.getProperty("user.home") + FILE_PATH, uid + ImageUtils.IMAGE_JPEG);
        File file2 = new File(System.getProperty("user.home") + FILE_PATH, uid + ImageUtils.IMAGE_PNG);

        if (file1.exists()) {
            file1.delete();
        }
        if (file2.exists()) {
            file2.delete();
        }
        try {
                tempFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

        try {
            file.transferTo(tempFile);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "user/{uid}/avatars", method = RequestMethod.GET)
    public void getAvatar(@PathVariable("uid") String uid, HttpServletResponse res) {
        File file = null;
        FileInputStream fis = null;
        File file1 = new File(System.getProperty("user.home") + FILE_PATH, uid + ImageUtils.IMAGE_JPEG);
        File file2 = new File(System.getProperty("user.home") + FILE_PATH, uid + ImageUtils.IMAGE_PNG);
        if (file1.exists()) {
            file = file1;
        }
        if (file2.exists()) {
            file = file2;
        }
        if (file != null) {
            try {
                fis = new FileInputStream(file);
                byte[] b = new byte[fis.available()];
                fis.read(b);
                String fileType = new MimetypesFileTypeMap().getContentType(file);
                res.setContentType(fileType);
                OutputStream out = res.getOutputStream();
                out.write(b);
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            try {
                res.getWriter().println("Avatar does not exists");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
