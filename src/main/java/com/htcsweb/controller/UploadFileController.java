package com.htcsweb.controller;


import com.alibaba.fastjson.JSONObject;
import com.htcsweb.dao.PipeBasicInfoDao;
import com.htcsweb.entity.ContractInfo;
import com.htcsweb.util.FileRenameUtil;
import com.htcsweb.util.ResponseUtil;
import com.htcsweb.util.ExcelUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.oreilly.servlet.multipart.FileRenamePolicy;
import com.oreilly.servlet.MultipartRequest;
import org.springframework.web.bind.annotation.ResponseBody;
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
import com.htcsweb.dao.ContractInfoDao;
import java.util.HashMap;




@Controller
@RequestMapping("/UploadFile")
public class UploadFileController {

    @Autowired
    private PipeBasicInfoDao pipeBasicInfoDao;

    @Autowired
    private ContractInfoDao contractInfoDao;

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
    @ResponseBody
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
    public String uploadPipeList(HttpServletRequest request, HttpServletResponse response) throws Exception{
        try {
            String saveDirectory = request.getSession().getServletContext().getRealPath("/upload/pipes");
            File uploadPath = new File(saveDirectory);
            if (!uploadPath.exists()) {
                uploadPath.mkdirs();
            }
            //FileRenameUtil util = new FileRenameUtil();
            MultipartRequest multi = new MultipartRequest(request, saveDirectory, 100* 1024 * 1024, "UTF-8");
            Enumeration files = multi.getFileNames();
            String newName = "";
            File file=null;
            int TotalUploadedPipes=0;
            int TotalSkippedPipes=0;
            if (files.hasMoreElements()) {
                String name = (String) files.nextElement();
                  file = multi.getFile(name);
                if (file != null) {
                    newName = file.getName();
                    //处理excel文件
                    HashMap retMap =importExcelInfo(saveDirectory+"/"+newName);
                    TotalUploadedPipes=Integer.parseInt(retMap.get("uploaded").toString());
                    TotalSkippedPipes=Integer.parseInt(retMap.get("skipped").toString());
                }
            }

            JSONObject json = new JSONObject();
            json.put("fileUrl", newName);
            json.put("totaluploaded",TotalUploadedPipes);
            json.put("totalskipped",TotalSkippedPipes);
            json.put("success",true);
            ResponseUtil.write(response, json);
            System.out.print("uploadPipeList成功");
            System.out.println("saveDirectory File="+saveDirectory+"/"+newName);
            System.out.println("file.length()="+file.length());

        } catch (Exception e) {
            System.err.println("Exception=" + e.getMessage().toString());
            e.printStackTrace();
            JSONObject json = new JSONObject();
            json.put("success",false);
            ResponseUtil.write(response, json);
        }
        return null;
    }






    public HashMap importExcelInfo( String fullfilename) throws Exception{

            HashMap retMap = new HashMap();//返回值
            int TotalUploaded=0;//成功插入数据库的钢管数量
            int TotalSkipped=0; //无合同号存在跳过的钢管数量


            List<List<Object>> listob =ExcelUtil.readFromFiletoList(fullfilename);
            //遍历listob数据，把数据放到List中


            for (int i = 0; i < listob.size(); i++) {
                List<Object> ob = listob.get(i);
                PipeBasicInfo pipe = new PipeBasicInfo();
                //设置编号
                System.out.println(String.valueOf(ob.get(ExcelUtil.PIPE_NO_INDEX)));
                //通过遍历实现把每一列封装成一个model中，再把所有的model用List集合装载
                System.out.println("row:"+String.valueOf(i));
                System.out.println("listob.size():"+String.valueOf(listob.size()));
                if(!ExcelUtil.isNumeric00(String.valueOf(ob.get(ExcelUtil.PIPE_NO_INDEX)))){
                    //若管号为空或不为数字，则跳过
                    continue;
                }
                if(!ExcelUtil.isNumeric00(String.valueOf(ob.get(ExcelUtil.OD_INDEX)))){
                    //若od为空或不为数字，则跳过
                    continue;
                }
                if(!ExcelUtil.isNumeric00(String.valueOf(ob.get(ExcelUtil.WT_INDEX)))){
                    //若wt为空或不为数字，则跳过
                    continue;
                }
                if(!ExcelUtil.isNumeric00(String.valueOf(ob.get(ExcelUtil.WEIGHT_INDEX)))){
                    //若weight为空或不为数字，则跳过
                    continue;
                }
                if(!ExcelUtil.isNumeric00(String.valueOf(ob.get(ExcelUtil.P_LENGTH_INDEX)))){
                    //若length为空或不为数字，则跳过
                    continue;
                }

                pipe.setId(0);
                pipe.setPipe_no(String.valueOf(ob.get(ExcelUtil.PIPE_NO_INDEX)));
                pipe.setContract_no(String.valueOf(ob.get(ExcelUtil.CONTRACT_NO_INDEX)));
                pipe.setOd(Float.parseFloat(ob.get(ExcelUtil.OD_INDEX).toString()));
                pipe.setWt(Float.parseFloat(ob.get(ExcelUtil.WT_INDEX).toString()));
                pipe.setHeat_no(String.valueOf(ob.get(ExcelUtil.HEAT_NO_INDEX)));
                pipe.setPipe_making_lot_no(String.valueOf(ob.get(ExcelUtil.PIPE_MAKING_LOT_NO_INDEX)));
                pipe.setWeight(Float.parseFloat(ob.get(ExcelUtil.WEIGHT_INDEX).toString()));
                pipe.setP_length(Float.parseFloat(ob.get(ExcelUtil.P_LENGTH_INDEX).toString()));
                pipe.setStatus("bare1");


                //批量插入
                int res=0;

                //查找该contract是否存在
                List<ContractInfo>conlist=contractInfoDao.getContractInfoByContractNo(pipe.getContract_no());
                if(conlist.size()==0){
                    TotalSkipped=TotalSkipped+1;
                    continue;//不存在则此钢管不予以录入系统
                }

                //查找该pipebasicinfo是否存在
                List<PipeBasicInfo>pipelist=pipeBasicInfoDao.getPipeNumber(pipe.getPipe_no());
                if(pipelist.size()==0){
                    //新钢管入库
                    res = pipeBasicInfoDao.addPipeBasicInfo(pipe);
                    System.out.println("Insert res: " + res);
                }else{
                    //更新数据库旧记录
                    PipeBasicInfo oldpipeinfo=pipelist.get(0);
                    pipe.setId(oldpipeinfo.getId());
                    res = pipeBasicInfoDao.updatePipeBasicInfo(pipe);
                    System.out.println("Update res: " + res);

                }


                TotalUploaded=TotalUploaded+res;
            }
            System.out.println("Total pipes: "+TotalUploaded);
        retMap.put("uploaded",TotalUploaded);
        retMap.put("skipped",TotalSkipped);

        return retMap;
    }



}

