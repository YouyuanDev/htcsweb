package com.htcsweb.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.htcsweb.dao.OdBlastProcessDao;
import com.htcsweb.dao.ProjectInfoDao;
import com.htcsweb.entity.OdBlastProcess;
import com.htcsweb.entity.ProjectInfo;
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
@RequestMapping("/ProjectOperation")
public class ProjectController {


    @Autowired
    private ProjectInfoDao projectInfoDao;

    @RequestMapping(value = "/getProjectInfoByLike")
    @ResponseBody
    public String getProjectInfoByLike(@RequestParam(value = "project_no",required = false)String project_no, @RequestParam(value = "project_name",required = false)String project_name,@RequestParam(value = "client_name",required = false)String client_name, @RequestParam(value = "begin_time",required = false)String begin_time, @RequestParam(value = "end_time",required = false)String end_time, HttpServletRequest request){
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
        List<HashMap<String,Object>>list=projectInfoDao.getAllByLike(project_no,project_name,client_name,beginTime,endTime,start,Integer.parseInt(rows));
        int count=projectInfoDao.getCount();
        Map<String,Object> maps=new HashMap<String,Object>();
        maps.put("total",count);
        maps.put("rows",list);
        String mmp= JSONArray.toJSONString(maps);
        return mmp;
    }

    @RequestMapping("/saveProject")
    @ResponseBody
    public String saveProject(ProjectInfo projectInfo, HttpServletResponse response){

        System.err.println("cccc:"+projectInfo.getProject_name());
        JSONObject json=new JSONObject();
        try{
            int resTotal=0;
            projectInfo.setProject_time(new Date());

            System.out.print("cccc:"+projectInfo.getProject_name());
            if(projectInfo.getId()==0){
                //添加
                resTotal=projectInfoDao.addProjectInfo(projectInfo);
            }else{
                //修改！
                System.out.print("ss是:"+projectInfo.getProject_name());
                resTotal=projectInfoDao.updateProjectInfo(projectInfo);
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



}
