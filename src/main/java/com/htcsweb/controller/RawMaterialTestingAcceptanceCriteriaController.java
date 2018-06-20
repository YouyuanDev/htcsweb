package com.htcsweb.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.htcsweb.entity.RawMaterialTestingAcceptanceCriteria2Fbe;
import com.htcsweb.dao.RawMaterialTestingAcceptanceCriteria2FbeDao;
import com.htcsweb.entity.RawMaterialTestingAcceptanceCriteria3Lpe;
import com.htcsweb.dao.RawMaterialTestingAcceptanceCriteria3LpeDao;
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
import java.util.*;


@Controller
@RequestMapping("/rawMaterialACOperation")
public class RawMaterialTestingAcceptanceCriteriaController {

    @Autowired
    RawMaterialTestingAcceptanceCriteria2FbeDao rawMaterialTestingAcceptanceCriteria2FbeDao;

    @Autowired
    RawMaterialTestingAcceptanceCriteria3LpeDao rawMaterialTestingAcceptanceCriteria3LpeDao;


    @RequestMapping("/getAllRawMaterialTestingAcceptanceCriteria2fbe")
    @ResponseBody
    public String getAllRawMaterialTestingAcceptanceCriteria2fbe(HttpServletRequest request){

        List<RawMaterialTestingAcceptanceCriteria2Fbe> list=rawMaterialTestingAcceptanceCriteria2FbeDao.getAllRawMaterialTestingAcceptanceCriteria2Fbe();
        List<ComboxItem> colist=new ArrayList<ComboxItem>();
        for(int i=0;i<list.size();i++){
            ComboxItem citem= new ComboxItem();
            RawMaterialTestingAcceptanceCriteria2Fbe ps=((RawMaterialTestingAcceptanceCriteria2Fbe)list.get(i));
            citem.id=ps.getRaw_material_testing_acceptance_criteria_no();
            citem.text=ps.getRaw_material_testing_acceptance_criteria_no();
            colist.add(citem);
        }
        String map= JSONObject.toJSONString(colist);
        return map;
    }

    //查找原材料标准(2FBE)
    @RequestMapping("/getRawMaterialStandard2FbeByLike")
    @ResponseBody
    public String getRawMaterialStandard2FbeByLike(@RequestParam(value = "raw_material_testing_acceptance_criteria_no",required = false)String raw_material_testing_acceptance_criteria_no,HttpServletRequest request){
        String page= request.getParameter("page");
        String rows= request.getParameter("rows");
        if(page==null||page==""){
            page="0";
        }if(rows==null||rows==""){
            rows="20";
        }
        int start=(Integer.parseInt(page)-1)*Integer.parseInt(rows);
        List<HashMap<String,Object>>list=rawMaterialTestingAcceptanceCriteria2FbeDao.getAllByLike(raw_material_testing_acceptance_criteria_no,start,Integer.parseInt(rows));
        int count=rawMaterialTestingAcceptanceCriteria2FbeDao.getCountAllByLike(raw_material_testing_acceptance_criteria_no);
        Map<String,Object> maps=new HashMap<String,Object>();
        maps.put("total",count);
        maps.put("rows",list);
        String mmp= JSONArray.toJSONString(maps);
        return mmp;
    }
    //添加、修改原材料标准(2FBE)
    @RequestMapping("/saveRawMaterialStandard2Fbe")
    @ResponseBody
    public String saveRawMaterialStandard2Fbe(RawMaterialTestingAcceptanceCriteria2Fbe rawMaterialTestingAcceptanceCriteria2Fbe, HttpServletRequest request, HttpServletResponse response){
        JSONObject json=new JSONObject();
        try{
            int resTotal=0;
            rawMaterialTestingAcceptanceCriteria2Fbe.setLast_update_time(new Date());
            if(rawMaterialTestingAcceptanceCriteria2Fbe.getId()==0){
                //添加
                resTotal=rawMaterialTestingAcceptanceCriteria2FbeDao.addRawMaterialTestingAcceptanceCriteria2Fbe(rawMaterialTestingAcceptanceCriteria2Fbe);
            }else{
                //修改！

                resTotal=rawMaterialTestingAcceptanceCriteria2FbeDao.updateRawMaterialTestingAcceptanceCriteria2Fbe(rawMaterialTestingAcceptanceCriteria2Fbe);
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

    //删除原材料标准(2FBE)
    @RequestMapping("/delRawMaterialStandard2Fbe")
    public String delRawMaterialStandard2Fbe(@RequestParam(value = "hlparam")String hlparam,HttpServletResponse response)throws Exception{
        String[]idArr=hlparam.split(",");
        int resTotal=0;
        resTotal=rawMaterialTestingAcceptanceCriteria2FbeDao.delRawMaterialTestingAcceptanceCriteria2Fbe(idArr);
        JSONObject json=new JSONObject();
        StringBuilder sbmessage = new StringBuilder();
        sbmessage.append("总共");
        sbmessage.append(Integer.toString(resTotal));
        sbmessage.append("项原材料(2FBE)标准删除成功\n");
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



    @RequestMapping("/getAllRawMaterialTestingAcceptanceCriteria3lpe")
    @ResponseBody
    public String getAllRawMaterialTestingAcceptanceCriteria3lpe(HttpServletRequest request){
        List<RawMaterialTestingAcceptanceCriteria3Lpe> list=rawMaterialTestingAcceptanceCriteria3LpeDao.getAllRawMaterialTestingAcceptanceCriteria3Lpe();
        List<ComboxItem> colist=new ArrayList<ComboxItem>();
        for(int i=0;i<list.size();i++){
            ComboxItem citem= new ComboxItem();
            RawMaterialTestingAcceptanceCriteria3Lpe ps=((RawMaterialTestingAcceptanceCriteria3Lpe)list.get(i));
            citem.id=ps.getRaw_material_testing_acceptance_criteria_no();
            citem.text=ps.getRaw_material_testing_acceptance_criteria_no();
            colist.add(citem);
        }
        String map= JSONObject.toJSONString(colist);
        System.out.println("3map="+map);
        return map;
    }
    //查找原材料标准(3LPE)
    @RequestMapping("/getRawMaterialStandard3LpeByLike")
    @ResponseBody
    public String getRawMaterialStandard3LpeByLike(@RequestParam(value = "raw_material_testing_acceptance_criteria_no",required = false)String raw_material_testing_acceptance_criteria_no,HttpServletRequest request){
        String page= request.getParameter("page");
        String rows= request.getParameter("rows");
        if(page==null||page==""){
            page="0";
        }if(rows==null||rows==""){
            rows="20";
        }
        System.out.println("rows="+rows);
        int start=(Integer.parseInt(page)-1)*Integer.parseInt(rows);
        List<HashMap<String,Object>>list=rawMaterialTestingAcceptanceCriteria3LpeDao.getAllByLike(raw_material_testing_acceptance_criteria_no,start,Integer.parseInt(rows));
        int count=rawMaterialTestingAcceptanceCriteria3LpeDao.getCountAllByLike(raw_material_testing_acceptance_criteria_no);
        Map<String,Object> maps=new HashMap<String,Object>();
        maps.put("total",count);
        maps.put("rows",list);
        String mmp= JSONArray.toJSONString(maps);
        return mmp;
    }
    //添加、修改原材料标准(3LPE)
    @RequestMapping("/saveRawMaterialStandard3Lpe")
    @ResponseBody
    public String saveRawMaterialStandard3Lpe(RawMaterialTestingAcceptanceCriteria3Lpe  rawMaterialTestingAcceptanceCriteria3Lpe, HttpServletRequest request, HttpServletResponse response){
        JSONObject json=new JSONObject();
        try{
            int resTotal=0;
            rawMaterialTestingAcceptanceCriteria3Lpe.setLast_update_time(new Date());
            if(rawMaterialTestingAcceptanceCriteria3Lpe.getId()==0){
                //添加
                resTotal=rawMaterialTestingAcceptanceCriteria3LpeDao.addRawMaterialTestingAcceptanceCriteria3Lpe(rawMaterialTestingAcceptanceCriteria3Lpe);
            }else{
                //修改！
                resTotal=rawMaterialTestingAcceptanceCriteria3LpeDao.updateRawMaterialTestingAcceptanceCriteria3Lpe(rawMaterialTestingAcceptanceCriteria3Lpe);
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
    //删除原材料标准(3LPE)
    @RequestMapping("/delRawMaterialStandard3Lpe")
    public String delRawMaterialStandard3Lpe(@RequestParam(value = "hlparam")String hlparam,HttpServletResponse response)throws Exception{
        String[]idArr=hlparam.split(",");
        int resTotal=0;
        resTotal=rawMaterialTestingAcceptanceCriteria3LpeDao.delRawMaterialTestingAcceptanceCriteria3Lpe(idArr);
        JSONObject json=new JSONObject();
        StringBuilder sbmessage = new StringBuilder();
        sbmessage.append("总共");
        sbmessage.append(Integer.toString(resTotal));
        sbmessage.append("项原材料(3LPE)标准删除成功\n");
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


    //根据项目编号查找原材料(2FBE)标准
    @RequestMapping("/getRawMaterialTestingAcceptanceCriteria2FbeByProjectNo")
    @ResponseBody
    public String getRawMaterialTestingAcceptanceCriteria2FbeByProjectNo(HttpServletRequest request){
        String project_no=request.getParameter("project_no");
        if(project_no!=null&&project_no!=""){
            RawMaterialTestingAcceptanceCriteria2Fbe criteria=rawMaterialTestingAcceptanceCriteria2FbeDao.getRawMaterialTestingAcceptanceCriteria2FbeByProjectNo(project_no);
            String map= JSONObject.toJSONString(criteria);
            return map;
        }else{
            return  null;
        }
    }


    //根据钢管编号查找原材料(2FBE)标准
    @RequestMapping("/getRawMaterialStandard2FbeByPipeNo")
    @ResponseBody
    public String getRawMaterialStandard2FbeByPipeNo(HttpServletRequest request){
        String pipe_no=request.getParameter("pipe_no");
        if(pipe_no!=null&&pipe_no!=""){
            RawMaterialTestingAcceptanceCriteria2Fbe criteria=rawMaterialTestingAcceptanceCriteria2FbeDao.getRawMaterialStandard2FbeByPipeNo(pipe_no);
            String map= JSONObject.toJSONString(criteria);
            return map;
        }else{
            return  null;
        }
    }


    //根据项目编号查找原材料(3LPE)标准
    @RequestMapping("/getRawMaterialTestingAcceptanceCriteria3LpeByProjectNo")
    @ResponseBody
    public String getRawMaterialTestingAcceptanceCriteria3LpeByProjectNo(HttpServletRequest request){
        String project_no=request.getParameter("project_no");
        if(project_no!=null&&project_no!=""){
            RawMaterialTestingAcceptanceCriteria3Lpe criteria=rawMaterialTestingAcceptanceCriteria3LpeDao.getRawMaterialTestingAcceptanceCriteria3LpeByProjectNo(project_no);
            String map= JSONObject.toJSONString(criteria);
            return map;
        }else{
            return  null;
        }
    }

    //根据钢管编号查找原材料(3LPE)标准
    @RequestMapping("/getRawMaterialStandard3LpeByPipeNo")
    @ResponseBody
    public String getRawMaterialStandard3LpeByPipeNo(HttpServletRequest request){
        String pipe_no=request.getParameter("pipe_no");
        if(pipe_no!=null&&pipe_no!=""){
            RawMaterialTestingAcceptanceCriteria3Lpe criteria=rawMaterialTestingAcceptanceCriteria3LpeDao.getRawMaterialStandard3LpeByPipeNo(pipe_no);
            String map= JSONObject.toJSONString(criteria);
            return map;
        }else{
            return  null;
        }
    }
}
