package com.htcsweb.controller;


import com.alibaba.fastjson.JSONObject;
import com.htcsweb.util.FileRenameUtil;
import com.htcsweb.util.ResponseUtil;
import com.htcsweb.util.ExcelUtil;
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
import java.io.InputStream;
import java.io.FileInputStream;
import java.util.List;
import com.htcsweb.entity.PipeBasicInfo;
import java.util.ArrayList;





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



    @RequestMapping(value = "/uploadFile")
    public String uploadFile(HttpServletRequest request, HttpServletResponse response) {
        try {
            String saveDirectory = request.getSession().getServletContext().getRealPath("/upload/files");
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
            json.put("fileUrl", newName);
            ResponseUtil.write(response, json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/uploadPipeList")
    public String uploadPipeList(HttpServletRequest request, HttpServletResponse response) {
        try {
            String saveDirectory = request.getSession().getServletContext().getRealPath("/upload/files");
            File uploadPath = new File(saveDirectory);
            if (!uploadPath.exists()) {
                uploadPath.mkdirs();
            }
            FileRenameUtil util = new FileRenameUtil();
            MultipartRequest multi = new MultipartRequest(request, saveDirectory, 100* 1024 * 1024, "UTF-8", util);
            Enumeration files = multi.getFileNames();
            String newName = "";
            if (files.hasMoreElements()) {
                String name = (String) files.nextElement();
                File file = multi.getFile(name);
                if (file != null) {
                    newName = file.getName();
                    //处理excel文件
                    InputStream in = new FileInputStream(file);
                    System.out.println("saveDirectory="+saveDirectory);
                    System.out.println("newName="+newName);
                    //importExcelInfo(in,saveDirectory+"/"+newName);
                }
            }

            JSONObject json = new JSONObject();
            json.put("fileUrl", newName);
            json.put("success",true);
            ResponseUtil.write(response, json);
            System.out.print("uploadPipeList成功");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void importExcelInfo(InputStream in, String filename){
        try {
            System.out.println("11111=" + filename);
            List<List<Object>> listob = ExcelUtil.getBankListByExcel(in, filename);
            System.out.println("2222222=" + filename);
            List<PipeBasicInfo> pipeList = new ArrayList<PipeBasicInfo>();
            //遍历listob数据，把数据放到List中
            for (int i = 0; i < listob.size(); i++) {
                List<Object> ob = listob.get(i);
                PipeBasicInfo pipe = new PipeBasicInfo();
                //设置编号
                System.out.println(String.valueOf(ob.get(1)));
                //通过遍历实现把每一列封装成一个model中，再把所有的model用List集合装载
                pipe.setPipe_no(String.valueOf(ob.get(1)));
//            salarymanage.setCompany(String.valueOf(ob.get(1)));
//            salarymanage.setNumber(String.valueOf(ob.get(2)));
//            salarymanage.setName(String.valueOf(ob.get(3)));
//            salarymanage.setSex(String.valueOf(ob.get(4)));
//            salarymanage.setCardName(String.valueOf(ob.get(5)));
//            salarymanage.setBankCard(String.valueOf(ob.get(6)));
//            salarymanage.setBank(String.valueOf(ob.get(7)));
//            //object类型转Double类型
//            salarymanage.setMoney(Double.parseDouble(ob.get(8).toString()));
//            salarymanage.setRemark(String.valueOf(ob.get(9)));
//            salarymanage.setSalaryDate(salaryDate);
                pipeList.add(pipe);
            }
            //批量插入
            //pipeBasicInfoDAO.insertInfoPipe(pipeList);
        }
        catch (Exception e)
        {
            System.out.println("Exception=" + e.getMessage().toString());
        }
    }



}