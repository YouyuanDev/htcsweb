package com.htcsweb.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.htcsweb.dao.*;
import com.htcsweb.entity.*;
import com.htcsweb.util.DateTimeUtil;
import com.htcsweb.util.GroupEntity;
import com.htcsweb.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.incrementer.HsqlMaxValueIncrementer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
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
    @Autowired
    private CoatingRepairDao coatingRepairDao;
    @Autowired
    private BarePipeGrindingCutoffRecordDao barePipeGrindingCutoffRecordDao;
    @Autowired
    private IdCoatingProcessDao idCoatingProcessDao;
    @Autowired
    private CoatingStripDao coatingStripDao;
    @Autowired
    private OdCoatingInspectionProcessDao odCoatingInspectionProcessDao;
    @Autowired
    private OdCoating3LpeInspectionProcessDao odCoating3LpeInspectionProcessDao;
    @Autowired
    private IdFinalInspectionProcessDao idFinalInspectionProcessDao;
    @Autowired
    private IdCoatingInspectionProcessDao idCoatingInspectionProcessDao;
    @Autowired
    private PipeSamplingRecordDao pipeSamplingRecordDao;
    @Autowired
    private PipeRebevelRecordDao pipeRebevelRecordDao;
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
                            // DailyProductionReport dailyReport=new DailyProductionReport();
                            //获取外防腐总数
                            int res1=getTotalOdCoating(item,project_no,external_coating,internal_coating,od,wt);
                            //接收光管数量，长度
                            int res2=0,res3=0;
                            //外涂总防腐数
                            int res4=getTotalOdCoating(item,project_no,external_coating,internal_coating,od,wt);
                            //外防腐合格数
                            List<String>odlist=getTotalQualifiedOdCoating(item,project_no,external_coating,internal_coating,od,wt);
                            int res5=Integer.parseInt(odlist.get(0));
                            //合格长度
                            float res6=Float.parseFloat(odlist.get(1));
                            BigDecimal b=new  BigDecimal(res6);
                            res6=  b.setScale(2,  BigDecimal.ROUND_HALF_UP).floatValue();
                            //外防目标合格数
                            int res7=0;
                            //外防目标合格长度
                            float res8=0f;
                            //外防修补数
                            int res9=getTotalCoatingRepair(item,project_no,external_coating,internal_coating,"od",od,wt);
                            //外防光管隔离数
                            List<String>bareList=getTotalBarePipeGrindingCutoff(item,project_no,external_coating,internal_coating,"od",od,wt);
                            int res10=Integer.parseInt(bareList.get(0));
                            //外防光管隔离管修磨
                            int res11=Integer.parseInt(bareList.get(1));
                            //外防光管隔离管切管
                            int res12=Integer.parseInt(bareList.get(2));
                            //外防涂层管废管数
                            List<String>wasteList=getTotalOdWastePipe(item,project_no,external_coating,internal_coating,od,wt);
                            int res13=Integer.parseInt(wasteList.get(0));
                            //外防涂层管废管处理
                            int res14=Integer.parseInt(wasteList.get(1));

                            //获取内防腐总数
                            int res15=getTotalIdCoating(item,project_no,external_coating,internal_coating,od,wt);
                            //内防目标合格数
                            int res16=0;
                            //内防目标合格长度
                            float res17=0f;
                            //内防修补数
                            int res18=getTotalCoatingRepair(item,project_no,external_coating,internal_coating,"id",od,wt);
                            //内防光管隔离数
                            List<String>bareIdList=getTotalBarePipeGrindingCutoff(item,project_no,external_coating,internal_coating,"id",od,wt);
                            int res19=Integer.parseInt(bareIdList.get(0));
                            //内防光管隔离管修磨
                            int res20=Integer.parseInt(bareIdList.get(1));
                            //内防光管隔离管切管
                            int res21=Integer.parseInt(bareIdList.get(2));
                            //内防涂层管废管数
                            List<String>wasteIdList=getTotalIdWastePipe(item,project_no,external_coating,internal_coating,od,wt);
                            int res22=Integer.parseInt(wasteIdList.get(0));
                            //内防涂层管废管处理
                            int res23=Integer.parseInt(wasteIdList.get(1));
                            //试验管白班
                            List<PipeSamplingRecord>sampleDayList=getPipeSamplingInfo(item,0,project_no,external_coating,internal_coating,od,wt);
                            String res24=" ";float  res25=0f,res26=0f;
                            if(sampleDayList!=null){
                                PipeSamplingRecord record=sampleDayList.get(0);
                                res24=record.getPipe_no();
                                res25=record.getOriginal_pipe_length();
                                BigDecimal b1=new  BigDecimal(res25);
                                res25=b1.setScale(2,  BigDecimal.ROUND_HALF_UP).floatValue();
                                res26=record.getCut_off_length();
                                BigDecimal b2=new  BigDecimal(res26);
                                res26=b2.setScale(2,  BigDecimal.ROUND_HALF_UP).floatValue();
                            }
                            //试验管夜班
                            List<PipeSamplingRecord>sampleNightList=getPipeSamplingInfo(item,1,project_no,external_coating,internal_coating,od,wt);
                            String res27=" ";float  res28=0f,res29=0f;
                            if(sampleDayList!=null){
                                PipeSamplingRecord record=sampleDayList.get(0);
                                res27=record.getPipe_no();
                                res28=record.getOriginal_pipe_length();
                                BigDecimal b1=new  BigDecimal(res28);
                                res28=b1.setScale(2,  BigDecimal.ROUND_HALF_UP).floatValue();
                                res29=record.getCut_off_length();
                                BigDecimal b2=new  BigDecimal(res29);
                                res29=b2.setScale(2,  BigDecimal.ROUND_HALF_UP).floatValue();
                            }
                            //管段重切
                            List<String>cutList=getPipeBevelOfDay(item,project_no,external_coating,internal_coating,od,wt);
                            int res30=Integer.parseInt(cutList.get(0));
                            int res31=Integer.parseInt(cutList.get(1));
                            //发运成品管
                            int res32=0;
                            float res33=0f;
                            //向日报中填充数据

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
    //1.获取当天外防腐总数(分为2FBE和3LPE两种)
    private int getTotalOdCoating(String now,String project_no,String external_coating,String internal_coating,float od,float wt){
        String nextday=DateTimeUtil.getNextDay(now);
        int total=0;
        try{
            Date beginTime=timeformat.parse(now+" 08:00:00");
            Date endTime=timeformat.parse(nextday+" 08:00:00");
            if(external_coating.equals("2FBE")){
                total=odCoatingProcessDao.getTotalOd2FBEOfDay(project_no,external_coating,internal_coating,od,wt,beginTime,endTime);
            }else if(external_coating.equals("3LPE")){
                total=odCoating3LpeProcessDao.getTotalOd3LPEOfDay(project_no,external_coating,internal_coating,od,wt,beginTime,endTime);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return total;
    }
    //2.获取当天外防腐合格数
    private List<String> getTotalQualifiedOdCoating(String now,String project_no,String external_coating,String internal_coating,float od,float wt){
        String nextday=DateTimeUtil.getNextDay(now);
        List<String>list=new ArrayList<>();
        String total0="0",total1="0";
        try{
            Date beginTime=timeformat.parse(now+" 08:00:00");
            Date endTime=timeformat.parse(nextday+" 08:00:00");
            List<HashMap<String,Object>>list1=odFinalInspectionProcessDao.getTotalQualifiedOfDay(project_no,external_coating,internal_coating,od,wt,beginTime,endTime,"1");
            if(list1!=null){
                HashMap<String,Object>hs=list1.get(0);
                if(hs!=null){
                    total0=String.valueOf("count1");
                    total1=String.valueOf("count2");

                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            list.add(total0);
            list.add(total1);
        }
        return list;
    }
    //3.获取当天的修补支数(根据od或者id划分)
    private int getTotalCoatingRepair(String now,String project_no,String external_coating,String internal_coating,String odid,float od,float wt){
        String nextday=DateTimeUtil.getNextDay(now);
        int total=0;
        try{
            Date beginTime=timeformat.parse(now+" 08:00:00");
            Date endTime=timeformat.parse(nextday+" 08:00:00");
            total=coatingRepairDao.getTotalCoatingRepairOfDay(project_no,external_coating,internal_coating,odid,od,wt,beginTime,endTime);
        }catch (Exception e){
            e.printStackTrace();
        }
        return total;
    }
    //4.获取当天的外防腐光管隔离数,光管隔离修磨数量,光管隔离切管数量(根据od或者id划分)
    private List<String>getTotalBarePipeGrindingCutoff(String now,String project_no,String external_coating,String internal_coating,String odid,float od,float wt){
        String nextday=DateTimeUtil.getNextDay(now);
        List<String> list=new ArrayList<>();
        String total0="0",total1="0",total2="0";
        try{
            Date beginTime=timeformat.parse(now+" 08:00:00");
            Date endTime=timeformat.parse(nextday+" 08:00:00");
            List<HashMap<String,Object>> list1=barePipeGrindingCutoffRecordDao.getTotalBarePipeGrindingCutoff(project_no,external_coating,internal_coating,odid,od,wt,beginTime,endTime);
            if(list1!=null){
                HashMap<String,Object>hs=list1.get(0);
                if(hs!=null){
                    total0=String.valueOf(hs.get("count1"));
                    total1=String.valueOf(hs.get("count2"));
                    total2=String.valueOf(hs.get("count3"));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            list.add(total0);
            list.add(total1);
            list.add(total2);
        }
        return list;
    }
    //5.获取外防腐涂层管废管数量和废管处理数量(当天符合参数的待扒皮数量和扒皮完成的和是废管数量)
    private List<String> getTotalOdWastePipe(String now,String project_no,String external_coating,String internal_coating,float od,float wt){
        List<String>list=new ArrayList<>();
        int total0=0,total1=0,total2=0;
        try{
            String nextday=DateTimeUtil.getNextDay(now);
            Date beginTime=timeformat.parse(now+" 08:00:00");
            Date endTime=timeformat.parse(nextday+" 08:00:00");
            if(external_coating.equals("2FBE")){
                total0=odCoatingInspectionProcessDao.getTotalOdWastePipe(project_no,external_coating,internal_coating,od,wt,beginTime,endTime);//待扒皮数量
            }else if(external_coating.equals("3LPE")){
                total0=odCoating3LpeInspectionProcessDao.getTotalOdWastePipe(project_no,external_coating,internal_coating,od,wt,beginTime,endTime);//待扒皮数量
            }
            total1=getTotalBarePipeGrindingInfo(now,project_no,external_coating,internal_coating,"od",od,wt);//废管处理数量
            total2=total0+total1;//废管数量
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            list.add(String.valueOf(total2));
            list.add(String.valueOf(total1));
        }
        return list;
    }
    //6.获取废管处理数量
    private int getTotalBarePipeGrindingInfo(String now,String project_no,String external_coating,String internal_coating,String odid,float od,float wt){
        String nextday=DateTimeUtil.getNextDay(now);
        int total=0;
        try{
            Date beginTime=timeformat.parse(now+" 08:00:00");
            Date endTime=timeformat.parse(nextday+" 08:00:00");
            total=coatingStripDao.getTotalBarePipeGrindingInfo(project_no,external_coating,internal_coating,odid,od,wt,beginTime,endTime);
        }catch (Exception e){
            e.printStackTrace();
        }
        return total;
    }
    //---------------内防腐------------
    //7.获取当天内防腐总数
    private int getTotalIdCoating(String now,String project_no,String external_coating,String internal_coating,float od,float wt){
        String nextday=DateTimeUtil.getNextDay(now);
        int total=0;
        try{
            Date beginTime=timeformat.parse(now+" 08:00:00");
            Date endTime=timeformat.parse(nextday+" 08:00:00");
            total=idCoatingProcessDao.getTotalIdOfDay(project_no,external_coating,internal_coating,od,wt,beginTime,endTime);
        }catch (Exception e){
            e.printStackTrace();
        }
        return total;
    }
    //8.获取当天的内防腐合格数
    private List<String> getTotalQualifiedIdCoating(String now,String project_no,String external_coating,String internal_coating,float od,float wt){
        String nextday=DateTimeUtil.getNextDay(now);
        List<String>list=new ArrayList<>();
        String total0="0",total1="0";
        try{
            Date beginTime=timeformat.parse(now+" 08:00:00");
            Date endTime=timeformat.parse(nextday+" 08:00:00");
            List<HashMap<String,Object>>list1=idFinalInspectionProcessDao.getTotalQualifiedOfDay(project_no,external_coating,internal_coating,od,wt,beginTime,endTime,"1");
            if(list1!=null){
                HashMap<String,Object>hs=list1.get(0);
                if(hs!=null){
                    total0=String.valueOf("count1");
                    total1=String.valueOf("count2");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            list.add(total0);
            list.add(total1);
        }
        return list;
    }

    //10.获取内防腐涂层管废管数量和废管处理数量(当天符合参数的待扒皮数量和扒皮完成的和是废管数量)
    private List<String> getTotalIdWastePipe(String now,String project_no,String external_coating,String internal_coating,float od,float wt){
        List<String>list=new ArrayList<>();
        int total0=0,total1=0,total2=0;
        try{
            String nextday=DateTimeUtil.getNextDay(now);
            Date beginTime=timeformat.parse(now+" 08:00:00");
            Date endTime=timeformat.parse(nextday+" 08:00:00");
            total0=idCoatingInspectionProcessDao.getTotalIdWastePipe(project_no,external_coating,internal_coating,od,wt,beginTime,endTime);
            total1=getTotalBarePipeGrindingInfo(now,project_no,external_coating,internal_coating,"id",od,wt);//废管处理数量
            total2=total0+total1;//废管数量
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            list.add(String.valueOf(total2));
            list.add(String.valueOf(total1));
        }
        return list;
    }
    //11.白班、夜班样管信息
    private List<PipeSamplingRecord>getPipeSamplingInfo(String now,int type,String project_no,String external_coating,String internal_coating,float od,float wt){
        List<PipeSamplingRecord>list=new ArrayList<>();
        Date begin_time=null,end_time=null;
        try{
            //白班
            if(type==0){
                String beginTime=now+" 08:00:00";
                String endTime=now+" 20:00:00";
                begin_time=timeformat.parse(beginTime);
                end_time=timeformat.parse(endTime);
            }else if(type==1){//夜班
                String beginTime=now+" 20:00:00";
                String nextday=DateTimeUtil.getNextDay(now);
                String endTime=nextday+" 08:00:00";
                begin_time=timeformat.parse(beginTime);
                end_time=timeformat.parse(endTime);
            }
            list=pipeSamplingRecordDao.getPipeSamplingInfo(project_no,external_coating,internal_coating,od,wt,begin_time,end_time);
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }
    //12.管段切斜信息(管切数量，合格数量)
    private List<String>getPipeBevelOfDay(String now,String project_no,String external_coating,String internal_coating,float od,float wt){
        List<String>list=new ArrayList<>();
        int total0=0,total1=0;
        try{
            String nextday=DateTimeUtil.getNextDay(now);
            Date beginTime=timeformat.parse(now+" 08:00:00");
            Date endTime=timeformat.parse(nextday+" 08:00:00");
            List<HashMap<String,Object>>list1=pipeRebevelRecordDao.getPipeBevelOfDay(project_no,external_coating,internal_coating,"1",od,wt,beginTime,endTime);
            if(list1!=null){
                HashMap<String,Object>hs=list1.get(0);
                if(hs!=null){
                    total0=Integer.parseInt(String.valueOf(hs.get("count1")));
                    total1=Integer.parseInt(String.valueOf(hs.get("count2")));
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            list.add(String.valueOf(total0));
            list.add(String.valueOf(total1));
        }
        return list;
    }
}
