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
            System.out.println("执行到此处1。。。。。。。。。");
           // String uploadPath="\\WebContent\\images\\";
            String uploadPath="/webapp/upload/pictures/";
            File f=new File(uploadPath);
            if(f.isDirectory()){
                System.out.println("是路径");
            }else{
                System.out.println("bu是路径");
            }
            //String temp=Thread.currentThread().getContextClassLoader().getResource("").getPath();
           // int num=temp.indexOf(".metadata");
           // String saveDirectory=temp.substring(1,num).replace('/', '\\')+request.getContextPath().replaceAll("/", "")+"\\WebContent\\images\\";

            String saveDirectory = request.getSession().getServletContext().getRealPath(uploadPath);
            System.out.println("保存路径"+saveDirectory);
//            MultipartRequest request1=new MultipartRequest("","","","");
            MultipartRequest multi = new MultipartRequest(request,saveDirectory,1000 * 1024 * 1024, "UTF-8");

            //如果有上传文件, 则保存到数据内
            Enumeration files = multi.getFileNames();
            System.out.println("执行到此处2。。。。。。。。。");
            while (files.hasMoreElements()) {
                System.out.println("执行到此处3。。。。。。。。。");
                String name = (String)files.nextElement();
                System.out.println("文件名＝"+name);
                File fs = multi.getFile(name);
                if(f!=null){
                    //读取上传后的项目文件, 导入保存到数据中
                    String fileName = multi.getFilesystemName(name);
                    response.getWriter().write(fileName +"("+new Date()+")");    //可以返回一个JSON字符串, 在客户端做更多处理
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
//            String saveDirectory = request.getSession().getServletContext().getRealPath(uploadPath);
//            MultipartResolver resolver=null;
//            System.out.println("文件保存路径＝"+saveDirectory);
//            try{
//                resolver=new CommonsMultipartResolver(request.getSession().getServletContext());
//            }catch (Exception e){
//                System.out.println("执行到此处2222。。。。。。。。。");
//                e.printStackTrace();
//            }
//
//            System.out.println("执行到此处1。。。。。。。。。");
//            MultipartHttpServletRequest multi=(MultipartHttpServletRequest)request;
//            System.out.println("执行到此处2。。。。。。。。。");
//           // MultipartHttpServletRequest multi=(MultipartHttpServletRequest)request;
//            Iterator files=multi.getFileNames();
//            System.out.println("执行到此处3。。。。。。。。。");
//            while (files.hasNext()) {
//                System.out.println("执行到此处4。。。。。。。。。");
//                String element = (String)files.next();
//                MultipartFile file = multi.getFile(element);
//                if(file!=null){
//                    //读取上传后的项目文件, 导入保存到数据中
//                    String fileName =file.getOriginalFilename();
//                    System.out.println("执行到此处5。。。。。。。。。");
//                    //获取文件扩展名
//                    String extName=fileName.substring(fileName.lastIndexOf('.')+1);
//                    fileName=String.valueOf(System.currentTimeMillis())+"."+extName;
//                    System.out.println("文件全路径＝"+fileName);
//                    File dir=new File(saveDirectory,fileName);
//                    if(!dir.exists()){
//                        dir.mkdirs();
//                    }
//                    file.transferTo(dir);
//
//                }
//            }
//            res=true;
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
