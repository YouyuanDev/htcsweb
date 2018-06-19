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

    //获取所有FBE涂层粉末型号名称
    @RequestMapping("/getAllFBECoatingPowderInfo")
    @ResponseBody
    public String getAllFBECoatingPowderInfo(HttpServletRequest request) {
        //CoatingPowderOperation/getAllCoatingPowderInfo.action
        //return getAllCoatingPowderInfoByType("FBE");
        String map= JSONObject.toJSONString(getAllCoatingPowderInfoByType("FBE"));
        return map;
    }

    //获取所有PE涂层粉末型号名称
    @RequestMapping("/getAllPECoatingPowderInfo")
    @ResponseBody
    public String getAllPECoatingPowderInfo(HttpServletRequest request) {
        //CoatingPowderOperation/getAllCoatingPowderInfo.action
        //return getAllCoatingPowderInfoByType("PE");
        String map= JSONObject.toJSONString(getAllCoatingPowderInfoByType("PE"));
        return map;

    }

    //获取所有AD型号名称
    @RequestMapping("/getAllADCoatingPowderInfo")
    @ResponseBody
    public String getAllADCoatingPowderInfo(HttpServletRequest request) {
        //CoatingPowderOperation/getAllCoatingPowderInfo.action
        //return getAllCoatingPowderInfoByType("AD");
        String map= JSONObject.toJSONString(getAllCoatingPowderInfoByType("AD"));
        return map;
    }
    //获取所有PARTICLE型号名称
    @RequestMapping("/getAllPARTICLECoatingPowderInfo")
    @ResponseBody
    public String getAllPARTICLECoatingPowderInfo(HttpServletRequest request) {
        //CoatingPowderOperation/getAllCoatingPowderInfo.action
        //return getAllCoatingPowderInfoByType("PARTICLE");
        String map= JSONObject.toJSONString(getAllCoatingPowderInfoByType("PARTICLE"));
        return map;
    }

    //获取所有PP型号名称
    @RequestMapping("/getAllPPCoatingPowderInfo")
    @ResponseBody
    public String getAllPPCoatingPowderInfo(HttpServletRequest request) {
        //CoatingPowderOperation/getAllCoatingPowderInfo.action
        String map= JSONObject.toJSONString(getAllCoatingPowderInfoByType("PP"));
        return map;
    }
    //获取所有EPOXY型号名称
    @RequestMapping("/getAllEPOXYCoatingPowderInfo")
    @ResponseBody
    public String getAllEPOXYCoatingPowderInfo(HttpServletRequest request) {
        //CoatingPowderOperation/getAllCoatingPowderInfo.action
        String map= JSONObject.toJSONString(getAllCoatingPowderInfoByType("EPOXY"));
        return map;
    }

    //获取所有REPAIR型号名称
    @RequestMapping("/getAllREPAIRCoatingPowderInfo")
    @ResponseBody
    public String getAllREPAIRCoatingPowderInfo(HttpServletRequest request) {
        //CoatingPowderOperation/getAllCoatingPowderInfo.action
        String map= JSONObject.toJSONString(getAllCoatingPowderInfoByType("REPAIR"));
        return map;
    }

    //获取所有CURING型号名称
    @RequestMapping("/getAllCURINGCoatingPowderInfo")
    @ResponseBody
    public String getAllCURINGCoatingPowderInfo(HttpServletRequest request) {
        //CoatingPowderOperation/getAllCoatingPowderInfo.action
        String map= JSONObject.toJSONString(getAllCoatingPowderInfoByType("CURING"));
        return map;
    }


    //获取所有CURING型号名称
    @RequestMapping(value = "/getAllCoatingPowderInfo",produces = "text/plain;charset=utf-8")
    @ResponseBody
    public String getAllCoatingPowderInfo(HttpServletRequest request) {
        //CoatingPowderOperation/getAllCoatingPowderInfo.action
        Map<String,Object> maps=new HashMap<String,Object>();
        List<ComboxItem> FBE=getAllCoatingPowderInfoByType("FBE");
        List<ComboxItem> PE=getAllCoatingPowderInfoByType("PE");
        List<ComboxItem> PP=getAllCoatingPowderInfoByType("PP");
        List<ComboxItem> AD=getAllCoatingPowderInfoByType("AD");
        List<ComboxItem> EPOXY=getAllCoatingPowderInfoByType("EPOXY");
        List<ComboxItem> CURING= getAllCoatingPowderInfoByType("CURING");
        List<ComboxItem> REPAIR = getAllCoatingPowderInfoByType("REPAIR");

        maps.put("FBE",FBE);
        maps.put("PE",PE);
        maps.put("PP",PP);
        maps.put("AD",AD);
        maps.put("EPOXY",EPOXY);
        maps.put("CURING",CURING);
        maps.put("REPAIR",REPAIR);

        String map=JSONObject.toJSONString(maps);
        return map;
    }

    private List<ComboxItem> getAllCoatingPowderInfoByType(String coatingType) {
        //APPRequestTransfer/getAllCoatingPowderInfo.action
        List<CoatingPowderInfo> list=coatingPowderInfoDao.getAllCoatingPowderInfoByType(coatingType);
        List<ComboxItem> colist=new ArrayList<ComboxItem>();
        for(int i=0;i<list.size();i++){
            ComboxItem citem= new ComboxItem();
            CoatingPowderInfo cp=((CoatingPowderInfo)list.get(i));
            citem.id=String.valueOf(cp.getId());
            citem.text= cp.getCoating_powder_name();
            colist.add(citem);
        }

        return colist;
    }
}
