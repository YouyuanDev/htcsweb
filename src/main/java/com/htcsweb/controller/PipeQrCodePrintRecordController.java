package com.htcsweb.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.htcsweb.dao.PipeQrCodePrintRecordDao;
import com.htcsweb.entity.PipeQrCodePrintRecord;
import com.htcsweb.util.ResponseUtil;
import com.htcsweb.entity.PipeQrCodePrintRecord;
import com.htcsweb.util.QRCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


        String logoDirectory = request.getSession().getServletContext().getRealPath("/images");
        StringBuilder sb = new StringBuilder();
        sb.append(logoDirectory);
        sb.append("/");
        sb.append("logo1.jpg");
        System.out.println("logoDirectory="+sb.toString());
        String logofullname=sb.toString();


        for(int i=0;i<pipeNoArr.length;i++){
            StringBuilder sb2 = new StringBuilder();
            sb2.append("qrcode_");
            sb2.append(pipeNoArr[i]);
            StringBuilder sbbot = new StringBuilder();
            sbbot.append("P/N:");
            sbbot.append(pipeNoArr[i]);
            QRCodeUtil.GenerateQRCode(pipeNoArr[i],logofullname,sb2.toString(),sbbot.toString(),"QRtmp");
        }



        JSONObject json=new JSONObject();
        StringBuilder sbmessage = new StringBuilder();
        sbmessage.append("总共");
        sbmessage.append(Integer.toString(pipeNoArr.length));
        sbmessage.append("项二维码生成成功\n");
        if(resTotal>0){
            json.put("success",true);
        }else{
            json.put("success",false);
        }
        json.put("message",sbmessage.toString());
        ResponseUtil.write(response,json);
        return null;
    }


}
