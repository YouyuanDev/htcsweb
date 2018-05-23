package com.htcsweb.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.htcsweb.dao.InspectionFrequencyDao;
import com.htcsweb.entity.IdStencilProcess;
import com.htcsweb.entity.InspectionFrequency;
import com.htcsweb.entity.PipeBasicInfo;
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
@RequestMapping("/InspectionFrequencyOperation")
public class InspectionFrequencyController {


    @Autowired
    private InspectionFrequencyDao inspectionFrequencyDao;



    //查找检验频率标准
    @RequestMapping("/getAllInspectionFrequencyByLike")
    @ResponseBody
    public String getAllInspectionFrequencyByLike(@RequestParam(value = "inspection_frequency_no",required = false)String inspection_frequency_no,HttpServletRequest request){
        String page= request.getParameter("page");
        String rows= request.getParameter("rows");
        if(page==null||page==""){
            page="0";
        }if(rows==null||rows==""){
            rows="20";
        }
        int start=(Integer.parseInt(page)-1)*Integer.parseInt(rows);
        List<HashMap<String,Object>>list=inspectionFrequencyDao.getAllByLike(inspection_frequency_no,start,Integer.parseInt(rows));
        int count=inspectionFrequencyDao.getCount(inspection_frequency_no);
        Map<String,Object> maps=new HashMap<String,Object>();
        maps.put("total",count);
        maps.put("rows",list);
        String mmp= JSONArray.toJSONString(maps);
        return mmp;
    }





    //查找所有检验频率,下拉框使用
    @RequestMapping("/getAllInspectionFrequency")
    @ResponseBody
    public String getAllInspectionFrequency(@RequestParam(value = "coating_acceptance_criteria_no",required = false)String coating_acceptance_criteria_no, HttpServletRequest request){
        List<InspectionFrequency> list=inspectionFrequencyDao.getAllInspectionFrequency();
        List<ComboxItem> colist=new ArrayList<ComboxItem>();
        for(int i=0;i<list.size();i++){
            ComboxItem citem= new ComboxItem();
            InspectionFrequency ps=((InspectionFrequency)list.get(i));
            citem.id=ps.getInspection_frequency_no();
            citem.text=ps.getInspection_frequency_no();
            colist.add(citem);
        }
        String map= JSONObject.toJSONString(colist);
        return map;
    }


    //添加、修改
    @RequestMapping("/saveInspectionFrequency")
    @ResponseBody
    public String saveInspectionFrequency(InspectionFrequency inspectionFrequency, HttpServletRequest request, HttpServletResponse response){
        JSONObject json=new JSONObject();
        try{
            int resTotal=0;


            if(inspectionFrequency.getId()==0){
                //添加
                resTotal=inspectionFrequencyDao.addInspectionFrequency(inspectionFrequency);
            }else{
                //修改！
                resTotal=inspectionFrequencyDao.updateInspectionFrequency(inspectionFrequency);
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
    @RequestMapping("/delInspectionFrequency")
    public String delInspectionFrequency(@RequestParam(value = "hlparam")String hlparam,HttpServletResponse response)throws Exception{
        String[]idArr=hlparam.split(",");
        int resTotal=0;
        resTotal=inspectionFrequencyDao.delInspectionFrequency(idArr);
        JSONObject json=new JSONObject();
        StringBuilder sbmessage = new StringBuilder();
        sbmessage.append("总共");
        sbmessage.append(Integer.toString(resTotal));
        sbmessage.append("项检验频率信息删除成功\n");
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
