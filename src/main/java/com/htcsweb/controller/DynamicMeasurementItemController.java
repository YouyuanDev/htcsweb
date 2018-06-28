package com.htcsweb.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.htcsweb.dao.DynamicMeasurementItemDao;
import com.htcsweb.entity.AcceptanceCriteria;
import com.htcsweb.entity.CoatingRepair;
import com.htcsweb.entity.DynamicMeasurementItem;
import com.htcsweb.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/DynamicItemOperation")
public class DynamicMeasurementItemController {

    @Autowired
    DynamicMeasurementItemDao dynamicMeasurementItemDao;


    //获得动态检测项列表，根据ACNo
    @RequestMapping("/getDynamicItemByACNo")
    @ResponseBody
    public String getDynamicItemByACNo(HttpServletRequest request){
        JSONObject json=new JSONObject();

        String acceptance_criteria_no=request.getParameter("acceptance_criteria_no");
        List<DynamicMeasurementItem> list=dynamicMeasurementItemDao.getDynamicMeasurementItemByAcceptanceCriteriaNo(acceptance_criteria_no);

        String mmp= JSONArray.toJSONString(list);
        return mmp;
    }


    @RequestMapping("/importDynamicItem")
    @ResponseBody
    public String importDynamicItem(HttpServletRequest request) {
        JSONObject json = new JSONObject();
        String src_acceptance_criteria_no=request.getParameter("src_acceptance_criteria_no");
        String des_acceptance_criteria_no=request.getParameter("des_acceptance_criteria_no");
        List<DynamicMeasurementItem> list=dynamicMeasurementItemDao.getDynamicMeasurementItemByAcceptanceCriteriaNo(src_acceptance_criteria_no);

        int totalcount=0;
        for(int i=0;i<list.size();i++) {
            DynamicMeasurementItem item=list.get(i);
            //初始化自己的参数
            item.setId(0);
            item.setAcceptance_criteria_no(des_acceptance_criteria_no);
            item.setItem_code("IT"+System.currentTimeMillis());
            int count=dynamicMeasurementItemDao.addDynamicMeasurementItem(item);
            totalcount+=count;
        }
        if(totalcount>0) {
            json.put("success", true);
            json.put("message", "成功导入检测项"+String.valueOf(totalcount)+"项");
        }
        else{
            json.put("success", false);
            json.put("message", "没有检测项可导入");
        }

        String mmp= JSONArray.toJSONString(json);
        return mmp;

    }





            //添加、修改
    @RequestMapping("/saveDynamicItem")
    @ResponseBody
    public String saveDynamicItem(DynamicMeasurementItem dynamicMeasurementItem, HttpServletRequest request, HttpServletResponse response){
        JSONObject json=new JSONObject();
        try{
            int resTotal=0;
            if(dynamicMeasurementItem.getId()==0){
                //添加
                resTotal=dynamicMeasurementItemDao.addDynamicMeasurementItem(dynamicMeasurementItem);
            }else{
                //修改！
                resTotal=dynamicMeasurementItemDao.updateDynamicMeasurementItem(dynamicMeasurementItem);

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
    @RequestMapping("/delDynamicItem")
    public String delDynamicItem(@RequestParam(value = "hlparam")String hlparam, HttpServletResponse response)throws Exception{
        String[]idArr=hlparam.split(",");
        int resTotal=0;
        resTotal=dynamicMeasurementItemDao.delDynamicMeasurementItem(idArr);
        JSONObject json=new JSONObject();
        StringBuilder sbmessage = new StringBuilder();
        sbmessage.append("总共");
        sbmessage.append(Integer.toString(resTotal));
        sbmessage.append("项检验项删除成功\n");
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
