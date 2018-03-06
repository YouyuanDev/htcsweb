package com.htcsweb.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.htcsweb.dao.PipeQrCodePrintRecordDao;
import com.htcsweb.entity.PipeQrCodePrintRecord;
import com.htcsweb.util.ResponseUtil;
import com.htcsweb.entity.PipeQrCodePrintRecord;
import com.htcsweb.util.QRCodeUtil;
import com.htcsweb.util.ZipUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.htcsweb.util.FileBean;

@Controller
@RequestMapping("/QrCodeOperation")
public class PipeQrCodePrintRecordController {
   
    @Autowired
    private PipeQrCodePrintRecordDao pipeQrCodePrintRecordDao;


    @RequestMapping("/delQrCodePrintRecord")
    public String delQrCodePrintRecord(@RequestParam(value = "hlparam")String hlparam,HttpServletResponse response)throws Exception{
        String[]idArr=hlparam.split(",");
        int resTotal=0;
        resTotal=pipeQrCodePrintRecordDao.delQrCode(idArr);
        JSONObject json=new JSONObject();
        StringBuilder sbmessage = new StringBuilder();
        sbmessage.append("总共");
        sbmessage.append(Integer.toString(resTotal));
        sbmessage.append("项二维码打印记录删除成功\n");
        if(resTotal>0){
            json.put("success",true);
        }else{
            json.put("success",false);
        }
        json.put("message",sbmessage.toString());
        ResponseUtil.write(response,json);
        return null;
    }


    @RequestMapping(value = "/getQrCodePrintRecord")
    @ResponseBody
    public String getQrCodePrintRecord(@RequestParam(value = "pipe_no",required = false)String pipe_no, @RequestParam(value = "operator_no",required = false)String operator_no, @RequestParam(value = "begin_time",required = false)String begin_time, @RequestParam(value = "end_time",required = false)String end_time,HttpServletRequest request){
        String page= request.getParameter("page");
        String rows= request.getParameter("rows");
        if(page==null){
            page="1";
        }
        if(rows==null){
            rows="20";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date beginTime=null;
        Date endTime=null;
        try{
            if(begin_time!=null&&begin_time!=""){
                beginTime=sdf.parse(begin_time);
                System.out.println(beginTime.toString());
            }
            if(end_time!=null&&end_time!=""){
                endTime=sdf.parse(end_time);
                System.out.println(endTime.toString());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        int start=(Integer.parseInt(page)-1)*Integer.parseInt(rows);
        List<PipeQrCodePrintRecord>list=pipeQrCodePrintRecordDao.getQrCodeInfoByLike(pipe_no,operator_no,beginTime,endTime,start,Integer.parseInt(rows));
        int count=pipeQrCodePrintRecordDao.getCountQrCodeInfoByLike(pipe_no,operator_no,beginTime,endTime);
        Map<String,Object> maps=new HashMap<String,Object>();
        maps.put("total",count);
        maps.put("rows",list);
        String mmp= JSONArray.toJSONString(maps);
        return mmp;
    }


    @RequestMapping("/genQRCode")
    public String genQRCode(@RequestParam(value = "hlparam")String hlparam,HttpServletRequest request,HttpServletResponse response)throws Exception{
        String[]pipeNoArr=hlparam.split(",");
        int resTotal=0;

        String tmpfolderPath=request.getSession().getServletContext().getRealPath("/tmp");
        String logoDirectory = request.getSession().getServletContext().getRealPath("/images");
        StringBuilder sb = new StringBuilder();
        sb.append(logoDirectory);
        sb.append("/");
        sb.append("logo1.jpg");
        //System.out.println("logoDirectory="+sb.toString());
        String logofullname=sb.toString();
        //下载文件列表初始化
        ArrayList<FileBean> fileList = new ArrayList<FileBean>();
        for(int i=0;i<pipeNoArr.length;i++){
            StringBuilder sb2 = new StringBuilder();
            sb2.append("qrcode_");
            sb2.append(pipeNoArr[i]);
            String pictureName=sb2.toString();
            StringBuilder sbbot = new StringBuilder();
            sbbot.append("P/N:");
            sbbot.append(pipeNoArr[i]);
            String bottomDes=sbbot.toString();
            String QRContent=pipeNoArr[i];
            QRCodeUtil.GenerateQRCode(QRContent,logofullname,pictureName,bottomDes,tmpfolderPath);
            //保存该文件
            FileBean file = new FileBean();
            file.setFileName(pictureName+".jpg");
            //System.out.println("tmpfolderPath="+tmpfolderPath);
            file.setFilePath(tmpfolderPath);
            fileList.add(file);
        }
        //以上代码可以正常生成所有单个QR码图片

        //以下代码下载zip文件

        //设置压缩包的名字
        //解决不同浏览器压缩包名字含有中文时乱码的问题
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");//可以方便地修改日期格式
        String timenow = dateFormat.format( now );
        StringBuilder sbzip = new StringBuilder();
        sbzip.append("downloadQR");
        sbzip.append(timenow);
        sbzip.append(".zip");
        String zipName = sbzip.toString();
        response.setContentType("APPLICATION/OCTET-STREAM");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(zipName, "UTF-8"));
        //设置压缩流：直接写入response，实现边压缩边下载

        //循环将文件写入压缩流
        ZipOutputStream out = new ZipOutputStream(response.getOutputStream());
        try {
            for(Iterator<FileBean> it = fileList.iterator(); it.hasNext();){
                FileBean file = it.next();
                //System.out.println("zip="+file.getFilePath()+"/"+file.getFileName());
                ZipUtils.doCompress(file.getFilePath()+"/"+file.getFileName(), out);
                response.flushBuffer();

            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());

        }finally{
            out.close();
        }



        return null;
    }


}
