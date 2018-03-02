package com.htcsweb.controller;



import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.htcsweb.dao.LabTesting3LpeDao;
import com.htcsweb.dao.PipeBasicInfoDao;
import com.htcsweb.entity.LabTesting3Lpe;
import com.htcsweb.util.ResponseUtil;
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
@RequestMapping("/LabTest3LpeOperation")
public class LabTesting3LpeController {
    @Autowired
    private LabTesting3LpeDao labTesting3LpeDao;
    @Autowired
    private PipeBasicInfoDao pipeBasicInfoDao;

    //查询
    @RequestMapping(value = "/getLabTest3LpeByLike")
    @ResponseBody
    public String getLabTest3LpeByLike(@RequestParam(value = "pipe_no",required = false)String pipe_no, @RequestParam(value = "operator_no",required = false)String operator_no, @RequestParam(value = "begin_time",required = false)String begin_time, @RequestParam(value = "end_time",required = false)String end_time, HttpServletRequest request){
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
            }
            if(end_time!=null&&end_time!=""){
                endTime=sdf.parse(end_time);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        int start=(Integer.parseInt(page)-1)*Integer.parseInt(rows);
        List<HashMap<String,Object>> list=labTesting3LpeDao.getNewAllByLike(pipe_no,operator_no,beginTime,endTime,start,Integer.parseInt(rows));
        int count=labTesting3LpeDao.getCountNewAllByLike(pipe_no,operator_no,beginTime,endTime);
        Map<String,Object> maps=new HashMap<String,Object>();
        maps.put("total",count);
        maps.put("rows",list);
        String mmp= JSONArray.toJSONString(maps);
        return mmp;
    }

    //添加、修改
    @RequestMapping("/saveLabTest3Lpe")
    @ResponseBody
    public String saveLabTest3Lpe(LabTesting3Lpe labTesting3Lpe, HttpServletRequest request, HttpServletResponse response){
        JSONObject json=new JSONObject();
        try{
            String odbptime= request.getParameter("odbptime");
            String coatingdate= request.getParameter("coatingdate");
            int resTotal=0;
            if(odbptime!=null&&odbptime!=""){
                SimpleDateFormat simFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date new_odbptime = simFormat.parse(odbptime);
                labTesting3Lpe.setOperation_time(new_odbptime);
            }else{
                labTesting3Lpe.setOperation_time(new Date());
            }
            if(coatingdate!=null&&coatingdate!=""){
                SimpleDateFormat simFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date new_coatingdate = simFormat.parse(coatingdate);
                labTesting3Lpe.setCoating_date(new_coatingdate);
            }else{
                labTesting3Lpe.setCoating_date(new Date());
            }
            if(labTesting3Lpe.getId()==0){
                //添加
                resTotal=labTesting3LpeDao.addLabTest3Lpe(labTesting3Lpe);
            }else{
                //修改！
                resTotal=labTesting3LpeDao.updateLabTest3Lpe(labTesting3Lpe);
            }
            if(resTotal>0){
               json.put("success",true);
            }else{
                json.put("success",false);
            }

        }catch (Exception e){
            json.put("success",false);
            e.printStackTrace();
        }finally {
            try{
                ResponseUtil.write(response,json);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }
    //删除
    @RequestMapping("/delLabTest3Lpe")
    public String delLabTest3Lpe(@RequestParam(value = "hlparam")String hlparam,HttpServletResponse response)throws Exception{
        String[]idArr=hlparam.split(",");
        int resTotal=0;
        resTotal=labTesting3LpeDao.delLabTest3Lpe(idArr);
        JSONObject json=new JSONObject();
        if(resTotal>0){
            json.put("success",true);
        }else{
            json.put("success",false);
        }
        ResponseUtil.write(response,json);
        return null;
    }
}
