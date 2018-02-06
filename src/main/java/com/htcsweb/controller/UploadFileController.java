package com.htcsweb.controller;


import com.alibaba.fastjson.JSONObject;
import com.htcsweb.util.ResponseUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
//import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
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

@Controller
@RequestMapping("/UploadFile")
public class UploadFileController {
    @RequestMapping(value = "/uploadPicture")
    public String uploadPicture(HttpServletRequest request,HttpServletResponse response){
        try{

            String saveDirectory = request.getSession().getServletContext().getRealPath("/upload/pictures");
            MultipartRequest multi = new MultipartRequest(request,saveDirectory,5* 1024 * 1024, "UTF-8");

            //如果有上传文件, 则保存到数据内

            Enumeration files = multi.getFileNames();

            while (files.hasMoreElements()) {
                String name = (String)files.nextElement();
                File file = multi.getFile(name);
                if(file!=null){
                    String fileName = multi.getFilesystemName(name);
                    String extName=fileName.substring(fileName.lastIndexOf('.'));
                    fileName=System.currentTimeMillis()+extName;

                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
//    public String uploadPicture(HttpServletRequest request,HttpServletResponse response){
//        System.out.println("zhixing.....................");
//        JSONObject json=new JSONObject();
//        boolean res=false;
//        try {
//            String uploadPath="/upload/pictures";
//            CommonsMultipartResolver multipartResolver=new CommonsMultipartResolver(request.getSession().getServletContext());
//            if(multipartResolver.isMultipart(request)){
//                MultipartHttpServletRequest multi=(MultipartHttpServletRequest)request;
//                Iterator iterator=multi.getFileNames();
//                while (iterator.hasNext()){
//                    MultipartFile file=multi.getFile(iterator.next().toString());
//                    if(file!=null){
//                        String path=request.getSession().getServletContext().getRealPath("/")+"upload/"+file.getOriginalFilename();
//                        file.transferTo(new File(path));
//                    }
//                }
//            }
//
//        }catch (Exception e){
//           res=false;
//           e.printStackTrace();
//        }
//        if(res){
//            System.out.println("执行到此处6。。。。。。。。。");
//            json.put("success",true);
//        }else{
//            System.out.println("执行到此处7。。。。。。。。。");
//            json.put("success",false);
//        }
//        try{
//            ResponseUtil.write(response,json);
//        }catch(Exception e){}
//
//        return  null;
//    }
}
