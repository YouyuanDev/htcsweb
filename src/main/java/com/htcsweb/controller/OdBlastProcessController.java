package com.htcsweb.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.htcsweb.dao.OdBlastProcessDao;
import com.htcsweb.entity.OdBlastProcess;
import com.htcsweb.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/OdOperation")
public class OdBlastProcessController {
   
    @Autowired
    private OdBlastProcessDao odblastprocessDao;

    @RequestMapping("/saveOdBlastProcess")
    @ResponseBody
    public String saveOdBlastProcess(OdBlastProcess odblastprocess, HttpServletResponse response){
        JSONObject json=new JSONObject();
        try{
            int resTotal=0;
            odblastprocess.setOperation_time(new Date());
            if(odblastprocess.getId()==0){
                //添加
                resTotal=odblastprocessDao.addOdBlastProcess(odblastprocess);
            }else{
                //修改！
                System.out.print("操作工编号是:"+odblastprocess.getOperator_no());
                resTotal=odblastprocessDao.updateOdBlastProcess(odblastprocess);
            }
            if(resTotal>0){
                json.put("success",true);
            }else{
                json.put("success",false);
            }
            ResponseUtil.write(response,json);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    @RequestMapping("/delOdBlastProcess")
    public String delOdBlastProcess(@RequestParam(value = "hlparam")String hlparam,HttpServletResponse response)throws Exception{
        String[]idArr=hlparam.split(",");
        int resTotal=0;
        resTotal=odblastprocessDao.delOdBlastProcess(idArr);
        JSONObject json=new JSONObject();
        if(resTotal>0){
            json.put("success",true);
        }else{
            json.put("success",false);
        }
        ResponseUtil.write(response,json);
        return null;
    }
    @ResponseBody
    @RequestMapping("/getOdBlastProcess")
    public  Map<String,Object> getOdBlastProcess(){
        List<OdBlastProcess> odblastprocesses= odblastprocessDao.getOdBlastProcess();
        Map<String,Object>  map= new HashMap<String, Object>();
        map.put("rows",odblastprocesses);
        return  map;
    }



    @RequestMapping("/editOdBlastProcess")
    public  String editOdBlastProcess(OdBlastProcess odblastprocess){
        System.out.println(odblastprocess);
        odblastprocessDao.updateOdBlastProcess(odblastprocess);
        return "od/odblastprocess";
    }

    @RequestMapping(value = "/getOdBlastByPage")
    @ResponseBody
    public String getOdBlastByPage(HttpServletRequest request){
        String page= request.getParameter("page");
        String rows= request.getParameter("rows");
        int start=(Integer.parseInt(page)-1)*Integer.parseInt(rows);
        List<OdBlastProcess> odblastprocesses= odblastprocessDao.feny(start,Integer.parseInt(rows));
        int count=odblastprocessDao.getCount();
        Map<String,Object> maps=new HashMap<String,Object>();
        maps.put("total",count);
        maps.put("rows",odblastprocesses);
        String mmp= JSONArray.toJSONString(maps);
        return mmp;
    }
    @RequestMapping(value = "/getOdBlastByLike")
    @ResponseBody
    public String getOdBlastByLike(@RequestParam(value = "pipe_no",required = false)String pipe_no, @RequestParam(value = "operator_no",required = false)String operator_no, @RequestParam(value = "begin_time",required = false)String begin_time, @RequestParam(value = "end_time",required = false)String end_time, HttpServletRequest request){
        String page= request.getParameter("page");
        String rows= request.getParameter("rows");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date beginTime=null;
        Date endTime=null;
        try{
            if(begin_time!=null&&begin_time!=""){
                beginTime=new Date(begin_time);
            }else{
                beginTime=new Date();
                beginTime.setTime(0);//设置时间最开始(1970-01-01)
            }
            if(end_time!=null&&end_time!=""){
                endTime=new Date(end_time);
            }else{
                endTime=new Date();
            }
        }catch (Exception e){
                e.printStackTrace();
        }
        int start=(Integer.parseInt(page)-1)*Integer.parseInt(rows);
        List<OdBlastProcess> odblastprocesses= odblastprocessDao.getAllByLike(pipe_no,operator_no,beginTime,endTime,start,Integer.parseInt(rows));
        int count=odblastprocessDao.getCount();
        Map<String,Object> maps=new HashMap<String,Object>();
        maps.put("total",count);
        maps.put("rows",odblastprocesses);
        String mmp= JSONArray.toJSONString(maps);
        return mmp;
    }
    @RequestMapping(value = "/getNewOdBlastByLike")
    @ResponseBody
    public String getNewOdBlastByLike(@RequestParam(value = "pipe_no",required = false)String pipe_no, @RequestParam(value = "operator_no",required = false)String operator_no, @RequestParam(value = "begin_time",required = false)String begin_time, @RequestParam(value = "end_time",required = false)String end_time, HttpServletRequest request){
        String page= request.getParameter("page");
        String rows= request.getParameter("rows");
        if(page==null){
            page="1";
            System.out.println("why page is null");
        }
        if(rows==null){
            rows="20";
            System.out.println("why rows is null");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date beginTime=null;
        Date endTime=null;
        try{
            if(begin_time!=null&&begin_time!=""){
                beginTime=new Date(begin_time);
            }else{
                beginTime=new Date();
                beginTime.setTime(0);//设置时间最开始(1970-01-01)
            }
            if(end_time!=null&&end_time!=""){
                endTime=new Date(end_time);
            }else{
                endTime=new Date();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        int start=(Integer.parseInt(page)-1)*Integer.parseInt(rows);
        String odblastprocesses= odblastprocessDao.getNewAllByLike(pipe_no,operator_no,beginTime,endTime,start,Integer.parseInt(rows));
        System.out.println(odblastprocesses);
        int count=odblastprocessDao.getCount();
        Map<String,Object> maps=new HashMap<String,Object>();
        maps.put("total",count);
        maps.put("rows",odblastprocesses);
        String mmp= JSONArray.toJSONString(maps);
        System.out.println(mmp);
        return mmp;
    }
    @RequestMapping("getOdBlastProcessById")
    public  String getodblastprocessById(int id,HttpServletRequest request){
        OdBlastProcess odblastprocess= odblastprocessDao.getOdBlastProcessById(id);
        request.getSession().setAttribute("sb",odblastprocess);
        System.out.println(odblastprocess);
        return  "odblastprocessedit";
    }


}
