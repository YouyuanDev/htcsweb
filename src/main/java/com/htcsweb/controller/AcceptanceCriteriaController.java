package com.htcsweb.controller;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


import com.htcsweb.dao.*;
import com.htcsweb.entity.*;
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
@RequestMapping("/AcceptanceCriteriaOperation")
public class AcceptanceCriteriaController {
    @Autowired
    private ODCoatingAcceptanceCriteriaDao odcoatingacceptancecriteriaDao;
    @Autowired
    private IDCoatingAcceptanceCriteriaDao idcoatingacceptancecriteriaDao;

    @Autowired
    private PipeBodyAcceptanceCriteriaDao pipeBodyAcceptanceCriteriaDao;

    @Autowired
    private PipeBasicInfoDao pipeBasicInfoDao;








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
        if(page==null||page==""){
            page="0";
        }if(rows==null||rows==""){
            rows="20";
        }
        int start=(Integer.parseInt(page)-1)*Integer.parseInt(rows);
        List<HashMap<String,Object>>list=odcoatingacceptancecriteriaDao.getAllByLike(coating_acceptance_criteria_no,start,Integer.parseInt(rows));
        int count=odcoatingacceptancecriteriaDao.getCount(coating_acceptance_criteria_no);
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
                System.out.println("执行修改到此。。。。");
                System.out.println(odCoatingAcceptanceCriteria.getId());
                System.out.println(odCoatingAcceptanceCriteria.getBlast_finish_sa25_max());
                System.out.println(odCoatingAcceptanceCriteria.getCutback_min());
                resTotal=odcoatingacceptancecriteriaDao.updateOdCoatingCriterProcess(odCoatingAcceptanceCriteria);
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

    //删除外防腐标准
    @RequestMapping("/delAllODAcceptanceCriteria")
    public String delAllODAcceptanceCriteria(@RequestParam(value = "hlparam")String hlparam,HttpServletResponse response)throws Exception{
        String[]idArr=hlparam.split(",");
        int resTotal=0;
        resTotal=odcoatingacceptancecriteriaDao.delOdCoatingCriterProcess(idArr);
        JSONObject json=new JSONObject();
        StringBuilder sbmessage = new StringBuilder();
        sbmessage.append("总共");
        sbmessage.append(Integer.toString(resTotal));
        sbmessage.append("项外防接收标准删除成功\n");
        if(resTotal>0){
            //System.out.print("删除成功");
            json.put("success",true);
        }else{
            //System.out.print("删除失败");
            json.put("success",false);
        }
        json.put("message",sbmessage.toString());
        ResponseUtil.write(response,json);
        return null;
    }

    //获取所有内防接收标准下拉
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
        if(page==null||page==""){
            page="0";
        }if(rows==null||rows==""){
            rows="20";
        }
        System.out.println("rows="+rows);
        int start=(Integer.parseInt(page)-1)*Integer.parseInt(rows);
        List<HashMap<String,Object>>list=idcoatingacceptancecriteriaDao.getAllByLike(coating_acceptance_criteria_no,start,Integer.parseInt(rows));
        int count=idcoatingacceptancecriteriaDao.getCount(coating_acceptance_criteria_no);
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
    //删除内防腐标准
    @RequestMapping("/delAllIDAcceptanceCriteria")
    public String delAllIDAcceptanceCriteria(@RequestParam(value = "hlparam")String hlparam,HttpServletResponse response)throws Exception{
        String[]idArr=hlparam.split(",");
        int resTotal=0;
        resTotal=idcoatingacceptancecriteriaDao.delIdCoatingCriterProcess(idArr);
        JSONObject json=new JSONObject();
        StringBuilder sbmessage = new StringBuilder();
        sbmessage.append("总共");
        sbmessage.append(Integer.toString(resTotal));
        sbmessage.append("项内防接收标准删除成功\n");
        if(resTotal>0){
            //System.out.print("删除成功");
            json.put("success",true);
        }else{
            //System.out.print("删除失败");
            json.put("success",false);
        }
        json.put("message",sbmessage.toString());
        ResponseUtil.write(response,json);
        return null;
    }







    //根据项目编号查找外防腐标准   stencil_content 不做动态替换
    @RequestMapping("/getODAcceptanceCriteriaByContractNo")
    @ResponseBody
    public String getODAcceptanceCriteriaByContractNo(HttpServletRequest request){
        String contract_no=request.getParameter("contract_no");
        if(contract_no!=null&&contract_no!=""){
             ODCoatingAcceptanceCriteria criteria=odcoatingacceptancecriteriaDao.getODAcceptanceCriteriaByContractNo(contract_no);
             String map= JSONObject.toJSONString(criteria);
             return map;
        }else{
            return  null;
        }
    }
    //根据项目编号查找内防腐标准
    @RequestMapping("/getIDAcceptanceCriteriaByContractNo")
    @ResponseBody
    public String getIDAcceptanceCriteriaByContractNo(HttpServletRequest request){
        String contract_no=request.getParameter("contract_no");
        if(contract_no!=null&&contract_no!=""){
             IDCoatingAcceptanceCriteria criteria=idcoatingacceptancecriteriaDao.getIDAcceptanceCriteriaByContractNo(contract_no);
             String map= JSONObject.toJSONString(criteria);
             return map;
        }else{
            return  null;
        }
    }

    //根据钢管编号查找内防腐标准 APP使用
    @RequestMapping("/getODAcceptanceCriteriaByPipeNo")
    @ResponseBody
    public String getODAcceptanceCriteriaByPipeNo(HttpServletRequest request){
        //AcceptanceCriteriaOperation/getIDAcceptanceCriteriaByPipeNo.action?pipe_no=1524540
        String pipe_no=request.getParameter("pipe_no");
        if(pipe_no!=null&&pipe_no!=""){
            ODCoatingAcceptanceCriteria criteria=odcoatingacceptancecriteriaDao.getODAcceptanceCriteriaByPipeNo(pipe_no);
            List<HashMap<String,Object>> pipelist= pipeBasicInfoDao.getPipeInfoByNo(pipe_no);
            if(pipelist.size()>0&&criteria!=null){
                float od=(float)pipelist.get(0).get("od");
                float wt=(float)pipelist.get(0).get("wt");
                String grade=(String)pipelist.get(0).get("grade");
                String contract_no=(String)pipelist.get(0).get("contract_no");
                String coating_standard=(String)pipelist.get(0).get("coating_standard");
                String client_spec=(String)pipelist.get(0).get("client_spec");
                String project_name=(String)pipelist.get(0).get("project_name");
                float p_length=(float)pipelist.get(0).get("p_length");
                float halflength=p_length*0.5f;
                String heat_no=(String)pipelist.get(0).get("heat_no");
                String pipe_making_lot_no=(String)pipelist.get(0).get("pipe_making_lot_no");
                float kg=(float)pipelist.get(0).get("weight")*1000;
                Date od_coating_date=(Date)pipelist.get(0).get("od_coating_date");
                String od_coating_dateString="";
                if(od_coating_date!=null) {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    od_coating_dateString = formatter.format(od_coating_date);
                }

                //替换
                String stencil_content=(String)criteria.getStencil_content();
                stencil_content = stencil_content.replace("[OD]", String.valueOf(od));
                stencil_content = stencil_content.replace("[WT]", String.valueOf(wt));
                stencil_content = stencil_content.replace("[GRADE]", grade);
                stencil_content = stencil_content.replace("[CONTRACTNO]", contract_no);
                stencil_content = stencil_content.replace("[COATINGSPEC]", coating_standard);
                stencil_content = stencil_content.replace("[CLIENTSPEC]", client_spec);
                stencil_content = stencil_content.replace("[PROJECTNAME]", project_name);
                stencil_content = stencil_content.replace("[PIPENO]", pipe_no);
                stencil_content = stencil_content.replace("[PIPELENGTH]", String.valueOf(p_length));
                stencil_content = stencil_content.replace("[HALFLENGTH]", String.valueOf(halflength));
                stencil_content = stencil_content.replace("[HEATNO]",heat_no);
                stencil_content = stencil_content.replace("[BATCHNO]",pipe_making_lot_no);
                stencil_content = stencil_content.replace("[WEIGHT]",String.valueOf(kg));
                stencil_content = stencil_content.replace("[COATINGDATE]",od_coating_dateString);
                criteria.setStencil_content(stencil_content);
            }

            String map= JSONObject.toJSONString(criteria);
            return map;
        }else{
            return  null;
        }
    }

    //根据钢管编号查找内防腐标准 APP使用
    @RequestMapping("/getIDAcceptanceCriteriaByPipeNo")
    @ResponseBody
    public String getIDAcceptanceCriteriaByPipeNo(HttpServletRequest request){
        //AcceptanceCriteriaOperation/getIDAcceptanceCriteriaByPipeNo.action?pipe_no=1524540
        String pipe_no=request.getParameter("pipe_no");
        if(pipe_no!=null&&pipe_no!=""){
            IDCoatingAcceptanceCriteria criteria=idcoatingacceptancecriteriaDao.getIDAcceptanceCriteriaByPipeNo(pipe_no);
            List<HashMap<String,Object>> pipelist= pipeBasicInfoDao.getPipeInfoByNo(pipe_no);
            if(pipelist.size()>0&&criteria!=null){
                float od=(float)pipelist.get(0).get("od");
                float wt=(float)pipelist.get(0).get("wt");
                float p_length=(float)pipelist.get(0).get("p_length");
                String pipe_making_lot_no=(String)pipelist.get(0).get("pipe_making_lot_no");
                float kg=(float)pipelist.get(0).get("weight")*1000;
                Date id_coating_date=(Date)pipelist.get(0).get("id_coating_date");
                String id_coating_dateString="";
                if(id_coating_date!=null) {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    id_coating_dateString = formatter.format(id_coating_date);
                }

                //替换
                String stencil_content=(String)criteria.getStencil_content();
                stencil_content = stencil_content.replace("[OD]", String.valueOf(od));
                stencil_content = stencil_content.replace("[WT]", String.valueOf(wt));
                stencil_content = stencil_content.replace("[PIPENO]", pipe_no);
                stencil_content = stencil_content.replace("[PIPELENGTH]", String.valueOf(p_length));
                stencil_content = stencil_content.replace("[BATCHNO]",pipe_making_lot_no);
                stencil_content = stencil_content.replace("[WEIGHT]",String.valueOf(kg));
                stencil_content = stencil_content.replace("[COATINGDATE]",id_coating_dateString);
                criteria.setStencil_content(stencil_content);
            }

            String map= JSONObject.toJSONString(criteria);
            return map;
        }else{
            return  null;
        }
    }



    //查找钢管管体标准
    @RequestMapping("/getAllPipeBodyAcceptanceCriteriaByLike")
    @ResponseBody
    public String getAllPipeBodyAcceptanceCriteriaByLike(@RequestParam(value = "pipe_body_acceptance_criteria_no",required = false)String pipe_body_acceptance_criteria_no,HttpServletRequest request){
        String page= request.getParameter("page");
        String rows= request.getParameter("rows");
        if(page==null||page==""){
            page="0";
        }if(rows==null||rows==""){
            rows="20";
        }
        System.out.println("rows="+rows);
        int start=(Integer.parseInt(page)-1)*Integer.parseInt(rows);
        List<HashMap<String,Object>>list=pipeBodyAcceptanceCriteriaDao.getAllByLike(pipe_body_acceptance_criteria_no,start,Integer.parseInt(rows));
        int count=pipeBodyAcceptanceCriteriaDao.getCount(pipe_body_acceptance_criteria_no);
        Map<String,Object> maps=new HashMap<String,Object>();
        maps.put("total",count);
        maps.put("rows",list);
        String mmp= JSONArray.toJSONString(maps);
        return mmp;
    }

    //获取所有钢管管体接收标准下拉
    @RequestMapping("/getAllPipeBodyAcceptanceCriteria")
    @ResponseBody
    public String getAllPipeBodyAcceptanceCriteria(@RequestParam(value = "pipe_body_acceptance_criteria_no",required = false)String pipe_body_acceptance_criteria_no,HttpServletRequest request){
        List<PipeBodyAcceptanceCriteria>list=pipeBodyAcceptanceCriteriaDao.getAllPipeBodyAcceptanceCriteria();
        List<ComboxItem> colist=new ArrayList<ComboxItem>();
        for(int i=0;i<list.size();i++){
            ComboxItem citem= new ComboxItem();
            PipeBodyAcceptanceCriteria ps=((PipeBodyAcceptanceCriteria)list.get(i));
            citem.id=ps.getPipe_body_acceptance_criteria_no();
            citem.text=ps.getPipe_body_acceptance_criteria_no();
            colist.add(citem);
        }
        String map= JSONObject.toJSONString(colist);
        return map;
    }

    //添加、修改钢管管体标准
    @RequestMapping("/saveAllPipeBodyAcceptanceCriteria")
    @ResponseBody
    public String saveAllPipeBodyAcceptanceCriteria(PipeBodyAcceptanceCriteria pipeBodyAcceptanceCriteria, HttpServletRequest request, HttpServletResponse response){
        JSONObject json=new JSONObject();
        try{
            int resTotal=0;
            pipeBodyAcceptanceCriteria.setLast_update_time(new Date());
            if(pipeBodyAcceptanceCriteria.getId()==0){
                //添加
                resTotal=pipeBodyAcceptanceCriteriaDao.addPipeBodyAcceptanceCriteria(pipeBodyAcceptanceCriteria);
            }else{
                //修改！
                resTotal=pipeBodyAcceptanceCriteriaDao.updatePipeBodyAcceptanceCriteria(pipeBodyAcceptanceCriteria);
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


    //删除钢管管体标准
    @RequestMapping("/delAllPipeBodyAcceptanceCriteria")
    public String delAllPipeBodyAcceptanceCriteria(@RequestParam(value = "hlparam")String hlparam,HttpServletResponse response)throws Exception{
        String[]idArr=hlparam.split(",");
        int resTotal=0;
        resTotal=pipeBodyAcceptanceCriteriaDao.delPipeBodyAcceptanceCriteria(idArr);
        JSONObject json=new JSONObject();
        StringBuilder sbmessage = new StringBuilder();
        sbmessage.append("总共");
        sbmessage.append(Integer.toString(resTotal));
        sbmessage.append("项钢管管体接收标准删除成功\n");
        if(resTotal>0){
            //System.out.print("删除成功");
            json.put("success",true);
        }else{
            //System.out.print("删除失败");
            json.put("success",false);
        }
        json.put("message",sbmessage.toString());
        ResponseUtil.write(response,json);
        return null;
    }

    //根据项目编号查找钢管管体标准
    @RequestMapping("/getPipeBodyAcceptanceCriteriaByContractNo")
    @ResponseBody
    public String getPipeBodyAcceptanceCriteriaByContractNo(HttpServletRequest request){
        String contract_no=request.getParameter("contract_no");
        if(contract_no!=null&&contract_no!=""){
            PipeBodyAcceptanceCriteria criteria=pipeBodyAcceptanceCriteriaDao.getPipeBodyAcceptanceCriteriaByContractNo(contract_no);
            String map= JSONObject.toJSONString(criteria);
            return map;
        }else{
            return  null;
        }
    }

    //根据钢管编号查找钢管管体标准
    @RequestMapping("/getPipeBodyAcceptanceCriteriaByPipeNo")
    @ResponseBody
    public String getPipeBodyAcceptanceCriteriaByPipeNo(HttpServletRequest request){
        //AcceptanceCriteriaOperation/getPipeBodyAcceptanceCriteriaByPipeNo?pipe_no='1525310'
        String pipe_no=request.getParameter("pipe_no");
        if(pipe_no!=null&&pipe_no!=""){
            PipeBodyAcceptanceCriteria criteria=pipeBodyAcceptanceCriteriaDao.getPipeBodyAcceptanceCriteriaByPipeNo(pipe_no);
            String map= JSONObject.toJSONString(criteria);
            return map;
        }else{
            return  null;
        }
    }

}
