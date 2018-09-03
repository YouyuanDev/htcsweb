package com.htcsweb.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.htcsweb.dao.CoatingPowderInfoDao;
import com.htcsweb.entity.CoatingPowderInfo;
import com.htcsweb.util.ComboxItem;
import com.htcsweb.util.ResponseUtil;
import org.apache.ibatis.jdbc.Null;
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
    /**
     * 分页查询涂层材料信息
     * @param coating_powder_name
     * @param powder_type
     * @param request
     * @return
     */
    @RequestMapping(value = "/getCoatingPowderInfoByLike")
    @ResponseBody
    public String getCoatingPowderInfoByLike(@RequestParam(value = "coating_powder_name",required = false)String coating_powder_name,@RequestParam(value = "powder_type",required = false)String powder_type, HttpServletRequest request){
        String page= request.getParameter("page");
        String rows= request.getParameter("rows");
        if(page==null){
            page="1";
        }
        if(rows==null){
            rows="20";
        }
        int start=(Integer.parseInt(page)-1)*Integer.parseInt(rows);
        List<CoatingPowderInfo>list=coatingPowderInfoDao.getAllByLike(coating_powder_name,powder_type,start,Integer.parseInt(rows));
        int count=coatingPowderInfoDao.getCountAllByLike(coating_powder_name,powder_type);
        Map<String,Object> maps=new HashMap<String,Object>();
        maps.put("total",count);
        maps.put("rows",list);
        String mmp= JSONArray.toJSONString(maps);
        return mmp;
    }
    /**
     * 添加或修改涂层材料信息
     * @param coatingPowderInfo(涂层材料信息)
     * @param response
     * @return
     */
    @RequestMapping(value = "/saveCoatingPowder")
    @ResponseBody
    public String saveCoatingPowder(CoatingPowderInfo coatingPowderInfo, HttpServletResponse response){
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
    /**
     * 删除涂层材料信息
     * @param hlparam
     * @param response
     * @return
     * @throws Exception
     */
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
            json.put("success",true);
        }else{
            json.put("success",false);
        }
        json.put("message",sbmessage.toString());
        ResponseUtil.write(response,json);
        return null;
    }
    /**
     * 获取所有原材料类型
     * @param request
     * @return
     */
    @RequestMapping("/getAllCoatingPowderType")
    @ResponseBody
    public String getAllCoatingPowderType(HttpServletRequest request) {
        List<CoatingPowderInfo> list=coatingPowderInfoDao.getAllCoatingPowderType();
        String map= JSONObject.toJSONString(list);
        return map;
    }
    /**
     * 获取所有FBE涂层粉末型号名称
     * @param request
     * @return
     */
    @RequestMapping("/getAllFBECoatingPowderInfo")
    @ResponseBody
    public String getAllFBECoatingPowderInfo(HttpServletRequest request) {
        String map= JSONObject.toJSONString(getAllCoatingPowderInfoByType("FBE"));
        return map;
    }
    /**
     * 获取所有PE涂层粉末型号名称
     * @param request
     * @return
     */
    @RequestMapping("/getAllPECoatingPowderInfo")
    @ResponseBody
    public String getAllPECoatingPowderInfo(HttpServletRequest request) {
        //CoatingPowderOperation/getAllCoatingPowderInfo.action
        //return getAllCoatingPowderInfoByType("PE");
        String map= JSONObject.toJSONString(getAllCoatingPowderInfoByType("PE"));
        return map;

    }
    /**
     * 获取所有AD型号名称
     * @param request
     * @return
     */
    @RequestMapping("/getAllADCoatingPowderInfo")
    @ResponseBody
    public String getAllADCoatingPowderInfo(HttpServletRequest request) {
        String map= JSONObject.toJSONString(getAllCoatingPowderInfoByType("AD"));
        return map;
    }
    /**
     * 获取所有PARTICLE型号名称
     * @param request
     * @return
     */
    @RequestMapping("/getAllPARTICLECoatingPowderInfo")
    @ResponseBody
    public String getAllPARTICLECoatingPowderInfo(HttpServletRequest request) {
        String map= JSONObject.toJSONString(getAllCoatingPowderInfoByType("PARTICLE"));
        return map;
    }
    /**
     * 获取所有PP型号名称
     * @param request
     * @return
     */
    @RequestMapping("/getAllPPCoatingPowderInfo")
    @ResponseBody
    public String getAllPPCoatingPowderInfo(HttpServletRequest request) {
        String map= JSONObject.toJSONString(getAllCoatingPowderInfoByType("PP"));
        return map;
    }
    /**
     * 获取所有EPOXY型号名称
     * @param request
     * @return
     */
    @RequestMapping("/getAllEPOXYCoatingPowderInfo")
    @ResponseBody
    public String getAllEPOXYCoatingPowderInfo(HttpServletRequest request) {
        String map= JSONObject.toJSONString(getAllCoatingPowderInfoByType("EPOXY"));
        return map;
    }
    /**
     * 获取所有REPAIR型号名称
     * @param request
     * @return
     */
    @RequestMapping("/getAllREPAIRCoatingPowderInfo")
    @ResponseBody
    public String getAllREPAIRCoatingPowderInfo(HttpServletRequest request) {
        String map= JSONObject.toJSONString(getAllCoatingPowderInfoByType("REPAIR"));
        return map;
    }
    /**
     * 获取所有CURING型号名称
     * @param request
     * @return
     */
    @RequestMapping("/getAllCURINGCoatingPowderInfo")
    @ResponseBody
    public String getAllCURINGCoatingPowderInfo(HttpServletRequest request) {
        String map= JSONObject.toJSONString(getAllCoatingPowderInfoByType("CURING"));
        return map;
    }
    /**
     *获取所有型号名称
     * @param request
     * @return
     */
    @RequestMapping(value = "/getAllCoatingPowderInfo",produces = "text/plain;charset=utf-8")
    @ResponseBody
    public String getAllCoatingPowderInfo(HttpServletRequest request) {
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
    /**
     * 根据原材料类型获取原材料型号
     * @param request
     * @return
     */
    @RequestMapping(value = "/getAllCoatingPowderInfoByPowderType")
    @ResponseBody
    private String getAllCoatingPowderInfoByPowderType(HttpServletRequest request){
        String powderType=request.getParameter("powderType");
        List<CoatingPowderInfo>list=new ArrayList<>();
        if(powderType!=null&&!powderType.equals(""))
          list=coatingPowderInfoDao.getAllCoatingPowderInfoByType(powderType);
        String map= JSONObject.toJSONString(list);
        return map;
    }
    /**
     * 根据原材料类型获取原材料型号(小页面使用)
     * @param coatingType
     * @return
     */
    private List<ComboxItem> getAllCoatingPowderInfoByType(String coatingType) {
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
