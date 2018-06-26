package com.htcsweb.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.htcsweb.dao.PipeBasicInfoDao;
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
import javax.servlet.http.HttpSession;
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
    @Autowired
    private PipeBasicInfoDao pipeBasicInfoDao;

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
            System.out.println(begin_time+":"+end_time);
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
    @RequestMapping("/genQRCodeByContractNo")
    public String genQRCodeByContractNo(@RequestParam(value = "hlparam")String hlparam,HttpServletRequest request,HttpServletResponse response)throws Exception{
        String[]contractNoArr=hlparam.split(",");
        List<String>list=new ArrayList<String>();
        list=getPipenoByContractNo(contractNoArr);
        String[]pipeNoArr=new String[list.size()];
        String[] pipeNos=list.toArray(pipeNoArr);
        List<PipeQrCodePrintRecord>list1=new ArrayList<PipeQrCodePrintRecord>();
        HttpSession session = request.getSession();
        String employee_no=(String)session.getAttribute("userSession");
        for (int i=0;i<pipeNos.length;i++){
            PipeQrCodePrintRecord record=new PipeQrCodePrintRecord();
            record.setPipe_no(pipeNos[i]);
            //把用户数据保存在session域对象中
            if(employee_no!=null) {
                record.setOperator_no(employee_no);
            }else{
                record.setOperator_no("0000000");
            }
            record.setOperation_time(new Date());
            record.setRemark("合同信息管理页面下载");
            list1.add(record);
        }
        if(list1.size()>0)
            pipeQrCodePrintRecordDao.addQrCode(list1);
        ResponseUtil.writeQRCodeZipFile(pipeNos,request,response);
        return "";
    }

    @RequestMapping("/genQRCode")
    public String genQRCode(@RequestParam(value = "hlparam")String hlparam,HttpServletRequest request,HttpServletResponse response)throws Exception{
        ResponseUtil.writeQRCodeZipFile(hlparam,request,response);
        List<PipeQrCodePrintRecord>list1=new ArrayList<PipeQrCodePrintRecord>();
        String[]arr=hlparam.split(",");
        String remark=request.getParameter("remark");
        if(remark==null)
            remark="钢管信息管理页面下载";

        for (int i=0;i<arr.length;i++){
            PipeQrCodePrintRecord record=new PipeQrCodePrintRecord();
            record.setPipe_no(arr[i]);
            //session 读取用户id
            HttpSession session = request.getSession();
            //把用户数据保存在session域对象中
            String employee_no=(String)session.getAttribute("userSession");
            if(employee_no!=null) {
                record.setOperator_no(employee_no);
            }else{
                record.setOperator_no("0000000");
            }
            record.setOperation_time(new Date());
            record.setRemark(remark);
            list1.add(record);
        }
        if(list1.size()>0)
            pipeQrCodePrintRecordDao.addQrCode(list1);
        return  null;
    }
    //根据合同编号查询所有的钢管编号
    public List<String>getPipenoByContractNo(String[]contractno){
        return pipeBasicInfoDao.getPipeNoByContractNo(contractno);
    }

}
