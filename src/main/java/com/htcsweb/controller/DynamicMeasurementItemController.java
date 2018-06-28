package com.htcsweb.controller;


import com.alibaba.fastjson.JSONObject;
import com.htcsweb.dao.DynamicMeasurementItemDao;
import com.htcsweb.entity.AcceptanceCriteria;
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

@Controller
@RequestMapping("/DynamicItemOperation")
public class DynamicMeasurementItemController {

    @Autowired
    DynamicMeasurementItemDao dynamicMeasurementItemDao;


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
