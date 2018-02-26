package com.htcsweb.controller;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


import com.htcsweb.entity.ODCoatingAcceptanceCriteria;
import com.htcsweb.entity.IDCoatingAcceptanceCriteria;
import com.htcsweb.util.ComboxItem;
import com.htcsweb.dao.ODCoatingAcceptanceCriteriaDao;
import com.htcsweb.dao.IDCoatingAcceptanceCriteriaDao;
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
@RequestMapping("/AcceptanceCriteriaOperation")
public class AcceptanceCriteriaController {
    @Autowired
    private ODCoatingAcceptanceCriteriaDao odcoatingacceptancecriteriaDao;
    @Autowired
    private IDCoatingAcceptanceCriteriaDao idcoatingacceptancecriteriaDao;

    @RequestMapping("/getAllODAcceptanceCriteria")
    @ResponseBody
    public String getAllODAcceptanceCriteria(@RequestParam(value = "coating_acceptance_criteria_no",required = false)String coating_acceptance_criteria_no,HttpServletRequest request){
        List<ODCoatingAcceptanceCriteria>list=odcoatingacceptancecriteriaDao.getAllODAcceptanceCriteria();
        List<ComboxItem> colist=new ArrayList<ComboxItem>();
        for(int i=0;i<list.size();i++){
            ComboxItem citem= new ComboxItem();
            ODCoatingAcceptanceCriteria ps=((ODCoatingAcceptanceCriteria)list.get(i));
            citem.id=ps.getCoating_acceptance_criteria_no();
            citem.text=ps.getCoating_acceptance_criteria_no();
            colist.add(citem);
        }
        String map= JSONObject.toJSONString(colist);
        return map;
    }
    //查找外防腐标准
    @RequestMapping("/getAllODAcceptanceCriteriaByLike")
    @ResponseBody
    public String getAllODAcceptanceCriteriaByLike(@RequestParam(value = "coating_acceptance_criteria_no",required = false)String coating_acceptance_criteria_no,HttpServletRequest request){
        String page= request.getParameter("page");
        String rows= request.getParameter("rows");
        int start=(Integer.parseInt(page)-1)*Integer.parseInt(rows);
        List<HashMap<String,Object>>list=odcoatingacceptancecriteriaDao.getAllByLike(coating_acceptance_criteria_no,start,Integer.parseInt(rows));
        int count=odcoatingacceptancecriteriaDao.getCount();
        Map<String,Object> maps=new HashMap<String,Object>();
        maps.put("total",count);
        maps.put("rows",list);
        String mmp= JSONArray.toJSONString(maps);
        return mmp;
    }
    //添加、修改外防腐标准
    @RequestMapping("/saveAllODAcceptanceCriteria")
    @ResponseBody
    public String saveAllODAcceptanceCriteria(ODCoatingAcceptanceCriteria odCoatingAcceptanceCriteria, HttpServletRequest request, HttpServletResponse response){
        JSONObject json=new JSONObject();
        try{
            int resTotal=0;
            odCoatingAcceptanceCriteria.setLast_update_time(new Date());
            if(odCoatingAcceptanceCriteria.getId()==0){
                //添加
                resTotal=odcoatingacceptancecriteriaDao.addOdCoatingCriterProcess(odCoatingAcceptanceCriteria);
            }else{
                //修改！
                resTotal=odcoatingacceptancecriteriaDao.updateOdCoatingCriterProcess(odCoatingAcceptanceCriteria);
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
    //删除外防腐标准
    @RequestMapping("/delAllODAcceptanceCriteria")
    public String delAllODAcceptanceCriteria(@RequestParam(value = "hlparam")String hlparam,HttpServletResponse response)throws Exception{
        String[]idArr=hlparam.split(",");
        int resTotal=0;
        resTotal=odcoatingacceptancecriteriaDao.delOdCoatingCriterProcess(idArr);
        JSONObject json=new JSONObject();
        if(resTotal>0){
            json.put("success",true);
        }else{
            json.put("success",false);
        }
        ResponseUtil.write(response,json);
        return null;
    }


    @RequestMapping("/getAllIDAcceptanceCriteria")
    @ResponseBody
    public String getAllIDAcceptanceCriteria(@RequestParam(value = "coating_acceptance_criteria_no",required = false)String coating_acceptance_criteria_no,HttpServletRequest request){
        List<IDCoatingAcceptanceCriteria>list=idcoatingacceptancecriteriaDao.getAllIDAcceptanceCriteria();
        List<ComboxItem> colist=new ArrayList<ComboxItem>();
        for(int i=0;i<list.size();i++){
            ComboxItem citem= new ComboxItem();
            IDCoatingAcceptanceCriteria ps=((IDCoatingAcceptanceCriteria)list.get(i));
            citem.id=ps.getCoating_acceptance_criteria_no();
            citem.text=ps.getCoating_acceptance_criteria_no();
            colist.add(citem);
        }
        String map= JSONObject.toJSONString(colist);
        return map;
    }
    //查找内防腐标准
    @RequestMapping("/getAllIDAcceptanceCriteriaByLike")
    @ResponseBody
    public String getAllIDAcceptanceCriteriaByLike(@RequestParam(value = "coating_acceptance_criteria_no",required = false)String coating_acceptance_criteria_no,HttpServletRequest request){
        String page= request.getParameter("page");
        String rows= request.getParameter("rows");
        int start=(Integer.parseInt(page)-1)*Integer.parseInt(rows);
        List<HashMap<String,Object>>list=idcoatingacceptancecriteriaDao.getAllByLike(coating_acceptance_criteria_no,start,Integer.parseInt(rows));
        int count=idcoatingacceptancecriteriaDao.getCount();
        Map<String,Object> maps=new HashMap<String,Object>();
        maps.put("total",count);
        maps.put("rows",list);
        String mmp= JSONArray.toJSONString(maps);
        return mmp;
    }
    //添加、修改内防腐标准
    @RequestMapping("/saveAllIDAcceptanceCriteria")
    @ResponseBody
    public String saveAllIDAcceptanceCriteria(IDCoatingAcceptanceCriteria  idCoatingAcceptanceCriteria, HttpServletRequest request, HttpServletResponse response){
        JSONObject json=new JSONObject();
        try{
            int resTotal=0;
            idCoatingAcceptanceCriteria.setLast_update_time(new Date());
            if(idCoatingAcceptanceCriteria.getId()==0){
                //添加
                resTotal=idcoatingacceptancecriteriaDao.addIdCoatingCriterProcess(idCoatingAcceptanceCriteria);
            }else{
                //修改！
                resTotal=idcoatingacceptancecriteriaDao.updateIdCoatingCriterProcess(idCoatingAcceptanceCriteria);
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
    //删除内防腐标准
    @RequestMapping("/delAllIDAcceptanceCriteria")
    public String delAllIDAcceptanceCriteria(@RequestParam(value = "hlparam")String hlparam,HttpServletResponse response)throws Exception{
        String[]idArr=hlparam.split(",");
        int resTotal=0;
        resTotal=idcoatingacceptancecriteriaDao.delIdCoatingCriterProcess(idArr);
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
