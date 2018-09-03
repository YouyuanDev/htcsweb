package com.htcsweb.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.htcsweb.dao.ProjectInfoDao;
import com.htcsweb.entity.PipeStatus;
import com.htcsweb.entity.ProjectInfo;
import com.htcsweb.util.ComboxItem;
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
import java.util.*;


@Controller
@RequestMapping("/ProjectOperation")
public class ProjectController {


    @Autowired
    private ProjectInfoDao projectInfoDao;
    /**
     * 获取项目编号和名称
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/getProjectNoAndName",produces="text/plain;charset=UTF-8")
    @ResponseBody
    public String getProjectNoAndName(HttpServletRequest request,HttpServletResponse response)
    {
        List<HashMap<String,Object>>list=projectInfoDao.getProjectNoAndName();
        JSONArray jsonArray=new JSONArray();
        jsonArray.add(list);
        return jsonArray.toString();
    }
    /**
     * 分页模糊查询项目信息
     * @param project_no(项目编号)
     * @param project_name(项目名称)
     * @param client_name(客户名称)
     * @param begin_time(开始日期)
     * @param end_time(结束日期)
     * @param request
     * @return
     */
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
                beginTime=sdf.parse(begin_time);
            }
            if(end_time!=null&&end_time!=""){
                endTime=sdf.parse(end_time);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        int start=(Integer.parseInt(page)-1)*Integer.parseInt(rows);
        List<HashMap<String,Object>>list=projectInfoDao.getAllByLike(project_no,project_name,client_name,beginTime,endTime,start,Integer.parseInt(rows));
        int count=projectInfoDao.getCountAllByLike(project_no,project_name,client_name,beginTime,endTime);
        Map<String,Object> maps=new HashMap<String,Object>();
        maps.put("total",count);
        maps.put("rows",list);
        String mmp= JSONArray.toJSONString(maps);
        return mmp;
    }
    /**
     * 添加或修改项目信息
     * @param projectInfo(项目信息)
     * @param response
     * @return
     */
    @RequestMapping(value = "/saveProject")
    @ResponseBody
    public String saveProject(ProjectInfo projectInfo,HttpServletResponse response){
        JSONObject json=new JSONObject();
        try{
            int resTotal=0;
            if(projectInfo.getId()==0){
                //添加
                resTotal=projectInfoDao.addProjectInfo(projectInfo);
            }else{
                //修改！
                resTotal=projectInfoDao.updateProjectInfo(projectInfo);
            }
            if(resTotal>0){
                json.put("success",true);
                json.put("message","保存成功");
            }else{
                json.put("success",false);
                json.put("message","保存失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            json.put("success",false);
            json.put("message",e.getMessage());
        }finally {
            try {
                ResponseUtil.write(response, json);
            }catch  (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    /**
     * 删除项目信息
     * @param hlparam(项目信息id集合,","分割)
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/delProject")
    public String delProject(@RequestParam(value = "hlparam")String hlparam,HttpServletResponse response)throws Exception{
        String[]idArr=hlparam.split(",");
        int resTotal=0;
        resTotal=projectInfoDao.delProjectInfo(idArr);
        JSONObject json=new JSONObject();
        StringBuilder sbmessage = new StringBuilder();
        sbmessage.append("总共");
        sbmessage.append(Integer.toString(resTotal));
        sbmessage.append("项项目信息删除成功\n");
        if(resTotal>0){
            json.put("success",true);
        }else{
            json.put("success",false);
        }
        json.put("message",sbmessage.toString());
        ResponseUtil.write(response,json);
        return null;
    }
    /**
     * 检查项目编号是否已经存在
     * @param project_no
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/checkProjectNoAvailable")
    @ResponseBody
    public String checkProjectNoAvailable(@RequestParam(value = "project_no",required = false)String project_no, HttpServletResponse response)throws Exception{
        int resTotal=0;
        resTotal=projectInfoDao.hasProjectNo(project_no);
        JSONObject json=new JSONObject();
        if(resTotal>0){
            json.put("success",false);
        }else{
            json.put("success",true);
            json.put("message","查询失败");
        }
        ResponseUtil.write(response,json);
        return null;
    }
    /**
     * 根据钢管编号异步查询钢管信息-名字
     * @param response
     * @param request
     * @return
     */
    @RequestMapping(value ="/getProjectNameByNo")
    public String getProjectNameByNo(HttpServletResponse response,HttpServletRequest request){
        String project_no=request.getParameter("project_no");
        String project_name=projectInfoDao.getProjectNameByNo(project_no);
        String mmp= JSON.toJSONString(project_name);
        try{
            ResponseUtil.write(response,mmp);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 根据合同编号异步查询项目名称和编号
     * @param response
     * @param request
     * @return
     */
    @RequestMapping(value = "/getProjectInfoByContract",produces = "text/plain;charset=utf-8")
    @ResponseBody
    public String getProjectInfoByContract(HttpServletResponse response,HttpServletRequest request){
        String contract_no=request.getParameter("contract_no");
        List<ProjectInfo>list=projectInfoDao.getProjectInfoByContract(contract_no);
        String map= JSONObject.toJSONString(list);
        try{
            ResponseUtil.write(response,map);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 根据项目编号获取项目信息
     * @param request
     * @return
     */
    @RequestMapping(value = "/getProjectInfo",produces = "text/plain;charset=utf-8")
    @ResponseBody
    public String getProjectInfo(HttpServletRequest request){
        String project_no=request.getParameter("project_no");
        List<ProjectInfo>list=projectInfoDao.getProjectInfo(project_no);
        String map= JSONObject.toJSONString(list);
        return map;
    }
    /**
     * 根据项目编号和项目名字异步查询钢管信息
     * @param response
     * @param request
     * @return
     */
    @RequestMapping(value ="/getProjectInfoByNoOrName")
    public String getProjectInfoByNoOrName(HttpServletResponse response,HttpServletRequest request){
        String project_no=request.getParameter("project_no");
        String project_name=request.getParameter("project_name");
        List<HashMap<String,Object>>list=projectInfoDao.getProjectInfoByNoOrName(project_no,project_name);
        String mmp= JSON.toJSONString(list);
        try{
            ResponseUtil.write(response, mmp);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


}
