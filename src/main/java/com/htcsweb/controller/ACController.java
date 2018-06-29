package com.htcsweb.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.htcsweb.dao.AcceptanceCriteriaDao;
import com.htcsweb.entity.AcceptanceCriteria;
import com.htcsweb.entity.InspectionTimeRecord;
import com.htcsweb.entity.OdCoating3LpeProcess;
import com.htcsweb.entity.PipeBasicInfo;
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
@RequestMapping("/ACOperation")
public class ACController {


    @Autowired
    private AcceptanceCriteriaDao acceptanceCriteriaDao;

    @RequestMapping(value = "/getACByLike")
    @ResponseBody
    public String getACByLike(@RequestParam(value = "acceptance_criteria_no",required = false)String acceptance_criteria_no,@RequestParam(value = "acceptance_criteria_name",required = false)String acceptance_criteria_name, @RequestParam(value = "external_coating_type",required = false)String external_coating_type, @RequestParam(value = "internal_coating_type",required = false)String internal_coating_type, HttpServletRequest request){
        String page= request.getParameter("page");
        String rows= request.getParameter("rows");
        if(page==null){
            page="1";
        }
        if(rows==null){
            rows="20";
        }

        try{

        }catch (Exception e){
            e.printStackTrace();
        }
        int start=(Integer.parseInt(page)-1)*Integer.parseInt(rows);
        List<HashMap<String,Object>> list=acceptanceCriteriaDao.getAllByLike(acceptance_criteria_no,acceptance_criteria_name,external_coating_type,internal_coating_type,start,Integer.parseInt(rows));
        int count=acceptanceCriteriaDao.getCountAllByLike(acceptance_criteria_no,acceptance_criteria_name,external_coating_type,internal_coating_type);
        Map<String,Object> maps=new HashMap<String,Object>();
        maps.put("total",count);
        maps.put("rows",list);
        String mmp= JSONArray.toJSONString(maps);
        //System.out.print("mmp:"+mmp);
        return mmp;
    }


    @RequestMapping(value = "/getACs",produces = "text/plain;charset=utf-8")
    @ResponseBody
    public String getACs(HttpServletRequest request){
        String map="";
        try{
            String acceptance_criteria_no=request.getParameter("acceptance_criteria_no");
            String acceptance_criteria_name=request.getParameter("acceptance_criteria_name");
            List<AcceptanceCriteria>list=acceptanceCriteriaDao.getACs(acceptance_criteria_no,acceptance_criteria_name);
            map= JSONObject.toJSONString(list);
        }catch (Exception e){
            e.printStackTrace();
        }
        return map;
    }


    //添加、修改
    @RequestMapping("/saveAC")
    @ResponseBody
    public String saveAC(AcceptanceCriteria acceptanceCriteria, HttpServletRequest request, HttpServletResponse response){
        JSONObject json=new JSONObject();
        try{
            int resTotal=0;
            acceptanceCriteria.setLast_update_time(new Date());
            if(acceptanceCriteria.getId()==0){
                //添加
                resTotal=acceptanceCriteriaDao.addAcceptanceCriteria(acceptanceCriteria);
            }else{
                //修改！
                resTotal=acceptanceCriteriaDao.updateAcceptanceCriteria(acceptanceCriteria);

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


    //删除
    @RequestMapping("/delAC")
    public String delAC(@RequestParam(value = "hlparam")String hlparam,HttpServletResponse response)throws Exception{
        String[]idArr=hlparam.split(",");
        int resTotal=0;
        resTotal=acceptanceCriteriaDao.delAcceptanceCriteria(idArr);
        JSONObject json=new JSONObject();
        StringBuilder sbmessage = new StringBuilder();
        sbmessage.append("总共");
        sbmessage.append(Integer.toString(resTotal));
        sbmessage.append("项检验标准删除成功\n");
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


}
