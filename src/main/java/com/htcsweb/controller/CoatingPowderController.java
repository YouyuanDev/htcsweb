package com.htcsweb.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.htcsweb.dao.CoatingPowderInfoDao;
import com.htcsweb.entity.CoatingPowderInfo;
import com.htcsweb.util.ComboxItem;
import com.htcsweb.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/CoatingPowderOperation")
public class CoatingPowderController {

    @Autowired
    CoatingPowderInfoDao coatingPowderInfoDao;


    //CoatingPowderOperation/getCoatingPowderInfoByLike

    //模糊查询CoatingPowder信息列表
    @RequestMapping(value = "/getCoatingPowderInfoByLike")
    @ResponseBody
    public String getCoatingPowderInfoByLike(@RequestParam(value = "coating_powder_name",required = false)String coating_powder_name, HttpServletRequest request){
        String page= request.getParameter("page");
        String rows= request.getParameter("rows");
        if(page==null){
            page="1";
        }
        if(rows==null){
            rows="20";
        }

        int start=(Integer.parseInt(page)-1)*Integer.parseInt(rows);
        List<CoatingPowderInfo>list=coatingPowderInfoDao.getAllByLike(coating_powder_name,start,Integer.parseInt(rows));
        int count=coatingPowderInfoDao.getCountAllByLike(coating_powder_name);

        Map<String,Object> maps=new HashMap<String,Object>();
        maps.put("total",count);
        maps.put("rows",list);
        String mmp= JSONArray.toJSONString(maps);
//        System.out.print("mmp:"+mmp);
        return mmp;
    }



    //增加或修改CoatingPowder信息
    @RequestMapping(value = "/saveCoatingPowder")
    @ResponseBody
    public String saveCoatingPowder(CoatingPowderInfo coatingPowderInfo, HttpServletResponse response){
//        System.out.print("saveCoatingPowder");

        JSONObject json=new JSONObject();
        try{
            int resTotal=0;


            if(coatingPowderInfo.getId()==0){
                //添加
                resTotal=coatingPowderInfoDao.addCoatingPowder(coatingPowderInfo);

            }else{
                //修改！

                resTotal=coatingPowderInfoDao.updateCoatingPowder(coatingPowderInfo);
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


    //删除原材料记录
    @RequestMapping("/delCoatingPowder")
    public String delCoatingPowder(@RequestParam(value = "hlparam")String hlparam, HttpServletResponse response)throws Exception{
        String[]idArr=hlparam.split(",");
        int resTotal=0;
        resTotal=coatingPowderInfoDao.delCoatingPowder(idArr);
        JSONObject json=new JSONObject();
        StringBuilder sbmessage = new StringBuilder();
        sbmessage.append("总共");
        sbmessage.append(Integer.toString(resTotal));
        sbmessage.append("项原材料型号信息删除成功\n");
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
