package com.htcsweb.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.htcsweb.dao.*;
import com.htcsweb.entity.*;
import com.htcsweb.util.DateTimeUtil;
import com.htcsweb.util.GroupEntity;
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
@RequestMapping("/DailyProductionReportOperation")
public class DailyProductionReportController {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat timeformat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Autowired
    private DailyProductionReportDao dailyProductionReportDao;
    @Autowired
    private ContractInfoDao contractInfoDao;
    @Autowired
    private OdCoatingProcessDao odCoatingProcessDao;
    @Autowired
    private OdCoating3LpeProcessDao odCoating3LpeProcessDao;
    @Autowired
    private OdFinalInspectionProcessDao odFinalInspectionProcessDao;
    //模糊查询DailyProductionReport信息列表
    @RequestMapping(value = "/getDailProductionReportByLike")
    @ResponseBody
    public String getDailProductionReportByLike(@RequestParam(value = "project_no",required = false)String project_no, @RequestParam(value = "begin_time",required = false)String begin_time, @RequestParam(value = "end_time",required = false)String end_time,HttpServletRequest request){
        String page= request.getParameter("page");
        String rows= request.getParameter("rows");
        if(page==null){
            page="1";
        }
        if(rows==null){
            rows="20";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date beginTime=null;
        Date endTime=null;
        try{
            if(begin_time!=null&&begin_time!=""){
                beginTime=sdf.parse(begin_time);
                System.out.println(beginTime.toString());
            }
            if(end_time!=null&&end_time!=""){
                endTime=sdf.parse(end_time);
                System.out.println(endTime.toString());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        int start=(Integer.parseInt(page)-1)*Integer.parseInt(rows);
        List<HashMap<String,Object>> list=dailyProductionReportDao.getAllByLike(project_no,beginTime,endTime,start,Integer.parseInt(rows));
        int count=dailyProductionReportDao.getCountAllByLike(project_no,beginTime,endTime);
        Map<String,Object> maps=new HashMap<String,Object>();
        maps.put("total",count);
        maps.put("rows",list);
        String mmp= JSONArray.toJSONString(maps);
        return mmp;
    }
    @RequestMapping(value = "/saveDailyProductionReport")
    @ResponseBody
    public String saveDailyProductionReport(DailyProductionReport dailyProductionReport, HttpServletRequest request, HttpServletResponse response){
        JSONObject json=new JSONObject();
        try{
            String productiondate= request.getParameter("production-date");
            int resTotal=0;
            if(productiondate!=null&&productiondate!=""){
                SimpleDateFormat simFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date new_productiondate = simFormat.parse(productiondate);
                dailyProductionReport.setProduction_date(new_productiondate);
            }else{
                dailyProductionReport.setProduction_date(new Date());
            }

            if(dailyProductionReport.getId()==0){
                //添加
                resTotal=dailyProductionReportDao.addDailyProductionReport(dailyProductionReport);
            }else{
                //修改！
                resTotal=dailyProductionReportDao.updateDailyProductionReport(dailyProductionReport);
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

    @RequestMapping("/delDailyProductionReport")
    public String delDailyProductionReport(@RequestParam(value = "hlparam")String hlparam,HttpServletResponse response)throws Exception{
        String[]idArr=hlparam.split(",");
        int resTotal=0;
        resTotal=dailyProductionReportDao.delDailyProductionReport(idArr);
        JSONObject json=new JSONObject();
        StringBuilder sbmessage = new StringBuilder();
        sbmessage.append("总共");
        sbmessage.append(Integer.toString(resTotal));
        sbmessage.append("项日报删除成功\n");
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
    @RequestMapping(value = "/addDialyReportAndCreateExcel")
    @ResponseBody
    public String addDialyReportAndCreateExcel(HttpServletRequest request){
        String flag="fail";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try{
          String project_no=request.getParameter("project_no");
          String beginTime=request.getParameter("begin_time");
          String endTime=request.getParameter("end_time");
          Date begin_time=null,end_time=null;
            List<String>dateList=new ArrayList<>();
            //判断是否获取到项目编号
            if(project_no!=null&&!project_no.equals("")){
               //判断是否获取到日期，并且开始日期小于结束日期
                if(beginTime!=null&&!beginTime.equals("")&&endTime!=null&&!endTime.equals("")){
                    begin_time=sdf.parse(beginTime);
                    end_time=sdf.parse(endTime);
                    dateList= DateTimeUtil.getBetweenDates(begin_time,end_time);
                    //遍历日期
                    List<HashMap<String,Object>>hashMapList=getAllContractInfo(project_no);
                    List<GroupEntity>list=getTabGroup(hashMapList);
                    //遍历排列组合，生成组合对应的tab
                    float od=0f,wt=0f;
                    String external_coating="",internal_coating="";
                    for (GroupEntity entity:list){
                        od=entity.getOd(); wt=entity.getWt();
                        external_coating=entity.getExternal_coating();internal_coating=entity.getInternal_coating();
                        for (String item:dateList){
                            //根据日期填充对应的tab
                            int res=getTotalOd2FBECoating(item,project_no,external_coating,od,wt);
                            int res1=getTotalOd3LPECoating(item,project_no,external_coating,od,wt);
                            int res2=getTotalQualifiedOdCoating(item,project_no,external_coating,od,wt);
                            System.out.println(res+"+++++"+res1+"++++++"+res2);
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return JSONArray.toJSONString(flag);
    }
    //

    //根据项目编号查找该项目中所有的合同
    private List<HashMap<String,Object>> getAllContractInfo(String project_no){
          List<HashMap<String,Object>>list=contractInfoDao.getAllContractInfoByProjectNo(project_no);
          return list;
    }
    //生成tab的排列组合,根据od-wt,external_coating,internal_coating进行组合
    private List<GroupEntity>getTabGroup(List<HashMap<String,Object>>contractInfoList){
        List<GroupEntity>tabGroupList=new ArrayList<>();//od-wt,external_coating,internal_coating组合
        List<String>odWtStrList=new ArrayList<>();//od-wt组合
        List<HashMap<String,String>>odwtNewList=new ArrayList<>();
        List<String>externalList=new ArrayList<>();//外防组合
        List<String>internalList=new ArrayList<>();//内防组合
        String odwtKey="",odwtValue="";
        for (HashMap<String,Object>item:contractInfoList){
           //获取od-wt的组合数
            odwtKey =String.valueOf(item.get("od"));
            odwtValue=String.valueOf(item.get("wt"));
            if(!odWtStrList.contains(odwtKey+"*"+odwtValue)){
               HashMap<String,String>tempMap=new HashMap<>();
               tempMap.put("od",String.valueOf(item.get("od")));
               tempMap.put("wt",String.valueOf(item.get("wt")));
               odwtNewList.add(tempMap);
               odWtStrList.add(String.valueOf(item.get("od"))+"*"+String.valueOf(item.get("wt")));
            }
            //获取外防组合数
            if(!externalList.contains(String.valueOf(item.get("external_coating")))){
                   externalList.add(String.valueOf(item.get("external_coating")));
            }

            //获取内防组合数
            if(!internalList.contains(String.valueOf(item.get("internal_coating")))){
                internalList.add(String.valueOf(item.get("internal_coating")));
            }

        }
        for (HashMap<String,String>item:odwtNewList){
                odwtKey = item.get("od");
                odwtValue=item.get("wt");
                for (String item1:externalList){
                    for (String item2:internalList){
                        GroupEntity entity=new GroupEntity();
                        entity.setOd(Float.parseFloat(odwtKey));
                        entity.setWt(Float.parseFloat(odwtValue));
                        entity.setExternal_coating(item1);
                        entity.setInternal_coating(item2);
                        tabGroupList.add(entity);
                    }
                }
        }
        return tabGroupList;
    }

    //------------获取各个数据-------
    //1.获取当天外防腐2FBE总数
    private int getTotalOd2FBECoating(String now,String project_no,String external_coating,float od,float wt){
        String nextday=DateTimeUtil.getNextDay(now);
        int total=0;
        try{
            Date beginTime=timeformat.parse(now+" 08:00:00");
            Date endTime=timeformat.parse(nextday+" 08:00:00");
            total=odCoatingProcessDao.getTotalOd2FBEOfDay(project_no,external_coating,od,wt,beginTime,endTime);
        }catch (Exception e){

        }
        return total;
    }
    //2.获取当天外防腐3LPE合格数
    private int getTotalOd3LPECoating(String now,String project_no,String external_coating,float od,float wt){
        String nextday=DateTimeUtil.getNextDay(now);
        int total=0;
        try{
            Date beginTime=timeformat.parse(now+" 08:00:00");
            Date endTime=timeformat.parse(nextday+" 08:00:00");
            total=odCoating3LpeProcessDao.getTotalOd3LPEOfDay(project_no,external_coating,od,wt,beginTime,endTime);
        }catch (Exception e){

        }
        return total;
    }
    //3.获取当天外防腐合格数
    private int getTotalQualifiedOdCoating(String now,String project_no,String external_coating,float od,float wt){
        String nextday=DateTimeUtil.getNextDay(now);
        int total=0;
        try{
            Date beginTime=timeformat.parse(now+" 08:00:00");
            Date endTime=timeformat.parse(nextday+" 08:00:00");
            total=odFinalInspectionProcessDao.getTotalQualifiedOfDay(project_no,external_coating,od,wt,beginTime,endTime,"1");
        }catch (Exception e){

        }
        return total;
    }

}
