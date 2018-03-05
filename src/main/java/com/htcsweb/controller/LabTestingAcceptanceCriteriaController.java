package com.htcsweb.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.htcsweb.dao.LabTestingAcceptanceCriteria2FbeDao;
import com.htcsweb.dao.LabTestingAcceptanceCriteria3LpeDao;
import com.htcsweb.util.ComboxItem;
import com.htcsweb.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.htcsweb.entity.LabTestingAcceptanceCriteria2Fbe;
import com.htcsweb.entity.LabTestingAcceptanceCriteria3Lpe;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
@RequestMapping("/LabTestingAcceptanceCriteriaOperation")
public class LabTestingAcceptanceCriteriaController {

    @Autowired
    LabTestingAcceptanceCriteria2FbeDao labTestingAcceptanceCriteria2FbeDao;

    @Autowired
    LabTestingAcceptanceCriteria3LpeDao labTestingAcceptanceCriteria3LpeDao;

    //获取2fbe实验标准
    @RequestMapping("/getAllLabTestingAcceptanceCriteria2fbe")
    @ResponseBody
    public String getAllLabTestingAcceptanceCriteria2fbe(@RequestParam(value = "lab_testing_acceptance_criteria_no",required = false)String lab_testing_acceptance_criteria_no, HttpServletRequest request){
        List<LabTestingAcceptanceCriteria2Fbe> list=labTestingAcceptanceCriteria2FbeDao.getAllLabTestingAcceptanceCriteria2Fbe();
        List<ComboxItem> colist=new ArrayList<ComboxItem>();
        for(int i=0;i<list.size();i++){
            ComboxItem citem= new ComboxItem();
            LabTestingAcceptanceCriteria2Fbe ps=((LabTestingAcceptanceCriteria2Fbe)list.get(i));
            citem.id=ps.getLab_testing_acceptance_criteria_no();
            citem.text=ps.getLab_testing_acceptance_criteria_no();
            colist.add(citem);
        }
        String map= JSONObject.toJSONString(colist);
        return map;
    }
    //获取3Lpe实验标准
    @RequestMapping("/getAllLabTestingAcceptanceCriteria3lpe")
    @ResponseBody
    public String getAllLabTestingAcceptanceCriteria3lpe(@RequestParam(value = "lab_testing_acceptance_criteria_no",required = false)String coating_acceptance_criteria_no, HttpServletRequest request){
        List<LabTestingAcceptanceCriteria3Lpe> list=labTestingAcceptanceCriteria3LpeDao.getAllLabTestingAcceptanceCriteria3Lpe();
        List<ComboxItem> colist=new ArrayList<ComboxItem>();
        for(int i=0;i<list.size();i++){
            ComboxItem citem= new ComboxItem();
            LabTestingAcceptanceCriteria3Lpe ps=((LabTestingAcceptanceCriteria3Lpe)list.get(i));
            citem.id=ps.getLab_testing_acceptance_criteria_no();
            citem.text=ps.getLab_testing_acceptance_criteria_no();
            colist.add(citem);
        }
        String map= JSONObject.toJSONString(colist);
        return map;

    }
    //查找2fbe实验标准
    @RequestMapping("/getAllODAcceptanceCriteria2FbeByLike")
    @ResponseBody
    public String getAllODAcceptanceCriteria2FbeByLike(@RequestParam(value = "lab_testing_acceptance_criteria_no",required = false)String lab_testing_acceptance_criteria_no,HttpServletRequest request){
        String page= request.getParameter("page");
        String rows= request.getParameter("rows");
        if(page==null||page==""){
            page="0";
        }if(rows==null||rows==""){
            rows="20";
        }
        int start=(Integer.parseInt(page)-1)*Integer.parseInt(rows);
        List<HashMap<String,Object>>list=labTestingAcceptanceCriteria2FbeDao.getNewAllByLike(lab_testing_acceptance_criteria_no,start,Integer.parseInt(rows));
        int count=labTestingAcceptanceCriteria2FbeDao.getCountNewAllByLike(lab_testing_acceptance_criteria_no);
        Map<String,Object> maps=new HashMap<String,Object>();
        maps.put("total",count);
        maps.put("rows",list);
        String mmp= JSONArray.toJSONString(maps);
        return mmp;
    }
    //查找3lpe实验标准
    @RequestMapping("/getAllODAcceptanceCriteria3LpeByLike")
    @ResponseBody
    public String getAllODAcceptanceCriteria3LpeByLike(@RequestParam(value = "lab_testing_acceptance_criteria_no",required = false)String lab_testing_acceptance_criteria_no,HttpServletRequest request){
        String page= request.getParameter("page");
        String rows= request.getParameter("rows");
        if(page==null||page==""){
            page="0";
        }if(rows==null||rows==""){
            rows="20";
        }
        int start=(Integer.parseInt(page)-1)*Integer.parseInt(rows);
        List<HashMap<String,Object>>list=labTestingAcceptanceCriteria3LpeDao.getNewAllByLike(lab_testing_acceptance_criteria_no,start,Integer.parseInt(rows));
        int count=labTestingAcceptanceCriteria3LpeDao.getCountNewAllByLike(lab_testing_acceptance_criteria_no);
        Map<String,Object> maps=new HashMap<String, Object>();
        maps.put("total",count);
        maps.put("rows",list);
        String mmp= JSONArray.toJSONString(maps);
        return mmp;
    }
    //保存、修改2fbe实验标准
    @RequestMapping("/saveLabTestingAcceptanceCriteria2Fbe")
    @ResponseBody
    public String saveLabTestingAcceptanceCriteria2Fbe(LabTestingAcceptanceCriteria2Fbe labTestingAcceptanceCriteria2Fbe, HttpServletRequest request, HttpServletResponse response){
        JSONObject json=new JSONObject();
        try{
            int resTotal=0;
            labTestingAcceptanceCriteria2Fbe.setLast_update_time(new Date());
            if(labTestingAcceptanceCriteria2Fbe.getId()==0){
                //添加
                resTotal=labTestingAcceptanceCriteria2FbeDao.addLabTestCriteria2Fbe(labTestingAcceptanceCriteria2Fbe);
            }else{
                //修改！
                resTotal=labTestingAcceptanceCriteria2FbeDao.updateLabTestCriteria2Fbe(labTestingAcceptanceCriteria2Fbe);
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
    //保存、修改3Lpe实验标准
    @RequestMapping("/saveLabTestingAcceptanceCriteria3lpe")
    @ResponseBody
    public String saveLabTestingAcceptanceCriteria3lpe(LabTestingAcceptanceCriteria3Lpe labTestingAcceptanceCriteria3Lpe, HttpServletRequest request, HttpServletResponse response){
        JSONObject json=new JSONObject();
        try{
            int resTotal=0;
            labTestingAcceptanceCriteria3Lpe.setLast_update_time(new Date());
            if(labTestingAcceptanceCriteria3Lpe.getId()==0){
                //添加
                System.out.println("tinajai");
                resTotal=labTestingAcceptanceCriteria3LpeDao.addLabTestCriteria3Lpe(labTestingAcceptanceCriteria3Lpe);
            }else{
                //修改！
                resTotal=labTestingAcceptanceCriteria3LpeDao.updateLabTestCriteria3Lpe(labTestingAcceptanceCriteria3Lpe);
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

    //删除2fbe实验标准
    @RequestMapping("/delLabTestingAcceptanceCriteria2fbe")
    public String delLabTestingAcceptanceCriteria2fbe(@RequestParam(value = "hlparam")String hlparam,HttpServletResponse response)throws Exception{
        String[]idArr=hlparam.split(",");
        int resTotal=0;
        resTotal=labTestingAcceptanceCriteria2FbeDao.delLabTestCriteria2Fbe(idArr);
        JSONObject json=new JSONObject();
        StringBuilder sbmessage = new StringBuilder();
        sbmessage.append("总共");
        sbmessage.append(Integer.toString(resTotal));
        sbmessage.append("条2Fbe实验接收标准删除成功\n");
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
    //删除3lpe实验标准
    @RequestMapping("/delLabTestingAcceptanceCriteria3lpe")
    public String delLabTestingAcceptanceCriteria3lpe(@RequestParam(value = "hlparam")String hlparam,HttpServletResponse response)throws Exception{
        String[]idArr=hlparam.split(",");
        int resTotal=0;
        resTotal=labTestingAcceptanceCriteria3LpeDao.delLabTestCriteria3Lpe(idArr);
        JSONObject json=new JSONObject();
        StringBuilder sbmessage = new StringBuilder();
        sbmessage.append("总共");
        sbmessage.append(Integer.toString(resTotal));
        sbmessage.append("条3Lpe实验接收标准删除成功\n");
        if(resTotal>0){
            json.put("success",true);
        }else{
            json.put("success",false);
        }
        json.put("message",sbmessage.toString());
        ResponseUtil.write(response,json);
        return null;
    }
    //根据项目编号查找外防2fbe实验标准
    @RequestMapping("/getAcceptanceCriteria2FbeByContractNo")
    @ResponseBody
    public String getAcceptanceCriteria2FbeByContractNo(HttpServletRequest request){
        String contract_no=request.getParameter("contract_no");
        if(contract_no!=null&&contract_no!=""){
            LabTestingAcceptanceCriteria2Fbe criteria=labTestingAcceptanceCriteria2FbeDao.getLabTestCriteria2FbeByContractNo(contract_no);
            String map= JSONObject.toJSONString(criteria);
            return map;
        }else{
            return  null;
        }
    }
    //根据项目编号查找外防3lpe实验标准
    @RequestMapping("/getAcceptanceCriteria3LpeByContractNo")
    @ResponseBody
    public String getAcceptanceCriteria3LpeByContractNo(HttpServletRequest request){
        String contract_no=request.getParameter("contract_no");
        if(contract_no!=null&&contract_no!=""){
            LabTestingAcceptanceCriteria3Lpe criteria=labTestingAcceptanceCriteria3LpeDao.getLabTestCriteria3LpeByContractNo(contract_no);
            String map= JSONObject.toJSONString(criteria);
            System.out.println("查到标准＝"+map);
            return map;
        }else{
            return  null;
        }
    }
}
