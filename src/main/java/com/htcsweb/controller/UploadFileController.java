package com.htcsweb.controller;


import com.alibaba.fastjson.JSONObject;
import com.htcsweb.util.FileRenameUtil;
import com.htcsweb.util.ResponseUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.oreilly.servlet.multipart.FileRenamePolicy;
import com.oreilly.servlet.MultipartRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Random;

@Controller
@RequestMapping("/UploadFile")
public class UploadFileController {
    @RequestMapping(value = "/uploadPicture")
    public String uploadPicture(HttpServletRequest request, HttpServletResponse response) {
        try {
            String saveDirectory = request.getSession().getServletContext().getRealPath("/upload/pictures");
            File uploadPath = new File(saveDirectory);
            if (!uploadPath.exists()) {
                uploadPath.mkdirs();
            }
            FileRenameUtil util = new FileRenameUtil();
            MultipartRequest multi = new MultipartRequest(request, saveDirectory, 100* 1024 * 1024, "UTF-8", util);
            Enumeration files = multi.getFileNames();
            String newName = "";
            while (files.hasMoreElements()) {
                String name = (String) files.nextElement();
                File file = multi.getFile(name);
                if (file != null) {
                    newName = file.getName();
                }
            }
            JSONObject json = new JSONObject();
            json.put("imgUrl", newName);
            ResponseUtil.write(response, json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @RequestMapping("/delUploadPicture")
    public String delUploadPicture(HttpServletRequest request,HttpServletResponse response){
        JSONObject json=new JSONObject();
        try {
            String imgList=request.getParameter("imgList");
            if(imgList!=null&&imgList!=""){
                String []listArr=imgList.split(";");
                String saveDirectory = request.getSession().getServletContext().getRealPath("/upload/pictures");
                String imgPath="";
                for(int i=0;i<listArr.length;i++){
                    imgPath=saveDirectory+"/"+listArr[i];
                    File file=new File(imgPath);
                    if(file.isFile()&&file.exists()){
                        file.delete();
                    }
                }
                json.put("success",true);
            }else{
                json.put("success",false);
            }
            ResponseUtil.write(response,json);
        }catch (Exception e){
            json.put("success",false);
        }
        return null;
    }
}