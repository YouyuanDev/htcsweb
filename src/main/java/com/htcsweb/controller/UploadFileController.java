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
            String saveDirectory = request.getSession().getServletContext().getRealPath("/upload")+"/pictures";
            String saveDirectory1 = request.getSession().getServletContext().getRealPath("upload/pictures");
           // String saveDirectory1 = request.getSession().getServletContext().getRealPath("\\");
            System.out.println("相对路径="+saveDirectory1);
            //String fpath=request.getp
            File f=new File(saveDirectory);
            if(f.isDirectory()){
                System.out.println("是目录");
            }else{
                System.out.println("不是目录");
            }
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
                    //response.getWriter().write(fileName +"("+new Date()+")");    //可以返回一个JSON字符串, 在客户端做更多处理
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
