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
                    List<ContractInfo>hashMapList=getAllContractInfo(project_no);
                    List<GroupEntity>list=getTabGroup(hashMapList);
                    //遍历排列组合，生成组合对应的tab
                    float od=0f,wt=0f;
                    String external_coating="",internal_coating="";
                    String od_wt="";
                    for (GroupEntity entity:list){
                        od=entity.getOd(); wt=entity.getWt();
                        od_wt=String.valueOf(od)+String.valueOf(wt);
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
                            List<Object>odlist=getTotalQualifiedOdCoating(item,project_no,external_coating,internal_coating,od,wt);
                            int res5=0;float res6=0;
                            if(odlist!=null&&odlist.size()>0){
                                res5=((Integer)odlist.get(0)).intValue();
                                //合格长度
                                res6=((Float)odlist.get(1)).floatValue();
                            }
                            BigDecimal b=new  BigDecimal(res6);
                            res6=  b.setScale(2,  BigDecimal.ROUND_HALF_UP).floatValue();
                            //外防目标合格数
                            int res7=0;
                            //外防目标合格长度
                            float res8=0f;
                            //外防修补数
                            int res9=getTotalCoatingRepair(item,project_no,external_coating,internal_coating,"od",od,wt);
                            //外防光管隔离数

                            //List<String>bareList=getTotalBarePipeGrindingCutoff(item,project_no,external_coating,internal_coating,"od",od,wt);
                            int res10=getTotalBarePipeGrindCutoffOfTime(item,project_no,external_coating,internal_coating,"od",od,wt);
                            //外防光管隔离管修磨
                            int res11=getTotalBarePipeGrindOfTime(item,project_no,external_coating,internal_coating,"od",od,wt);
                            //外防光管隔离管切管
                            int res12=getTotalBarePipeCutoffOfTime(item,project_no,external_coating,internal_coating,"od",od,wt);
                            //外防涂层管废管数
                            //List<String>wasteList=getTotalOdWastePipe(item,project_no,external_coating,internal_coating,od,wt);
                            int res13=getTotalWastePipe(item,project_no,external_coating,internal_coating,"od",od,wt);
                            //外防涂层管废管处理
                            int res14=getTotalHandleWastePipe(item,project_no,external_coating,internal_coating,"od",od,wt);

                            //获取内防腐总数
                            int res15=getTotalIdCoating(item,project_no,external_coating,internal_coating,od,wt);
                            List<Object>idlist= getTotalQualifiedIdCoating(item,project_no,external_coating,internal_coating,od,wt);
                            int res16=0;float res17=0;
                            if(idlist!=null&&idlist.size()>0){
                                 res16=((Integer)odlist.get(0)).intValue();
                                //合格长度
                                 res17=((Float)odlist.get(1)).floatValue();
                            }
                            BigDecimal b1=new  BigDecimal(res17);

                            res17=  b1.setScale(2,  BigDecimal.ROUND_HALF_UP).floatValue();
                            //内防目标合格数
                            int res18=0;
                            //内防目标合格长度
                            float res19=0f;
                            //内防修补数
                            int res20=getTotalCoatingRepair(item,project_no,external_coating,internal_coating,"id",od,wt);
                            //内防光管隔离数
                            int res21=getTotalBarePipeGrindCutoffOfTime(item,project_no,external_coating,internal_coating,"id",od,wt);
                            //内防光管隔离管修磨
                            int res22=getTotalBarePipeGrindOfTime(item,project_no,external_coating,internal_coating,"id",od,wt);
                            //内防光管隔离管切管
                            int res23=getTotalBarePipeCutoffOfTime(item,project_no,external_coating,internal_coating,"id",od,wt);
                            //内防涂层管废管数
                            //List<String>wasteIdList=getTotalIdWastePipe(item,project_no,external_coating,internal_coating,od,wt);
                            int res24=getTotalWastePipe(item,project_no,external_coating,internal_coating,"id",od,wt);
                            //内防涂层管废管处理
                            int res25=getTotalHandleWastePipe(item,project_no,external_coating,internal_coating,"id",od,wt);
                            //试验管白班
                            List<PipeSamplingRecord>sampleDayList=getPipeSamplingInfo(item,0,project_no,external_coating,internal_coating,od,wt);
                            String res26=" ";float  res27=0f,res28=0f;
                            if(sampleDayList!=null){
                                PipeSamplingRecord record=sampleDayList.get(0);
                                res26=record.getPipe_no();
                                res27=record.getOriginal_pipe_length();
                                BigDecimal b2=new  BigDecimal(res27);
                                res27=b2.setScale(2,  BigDecimal.ROUND_HALF_UP).floatValue();
                                res28=record.getCut_off_length();
                                BigDecimal b3=new  BigDecimal(res28);
                                res28=b3.setScale(2,  BigDecimal.ROUND_HALF_UP).floatValue();
                            }
                            //试验管夜班
                            List<PipeSamplingRecord>sampleNightList=getPipeSamplingInfo(item,1,project_no,external_coating,internal_coating,od,wt);
                            String res29=" ";float  res30=0f,res31=0f;
                            if(sampleDayList!=null){
                                PipeSamplingRecord record=sampleDayList.get(0);
                                res29=record.getPipe_no();
                                res30=record.getOriginal_pipe_length();
                                BigDecimal b4=new  BigDecimal(res30);
                                res30=b4.setScale(2,  BigDecimal.ROUND_HALF_UP).floatValue();
                                res31=record.getCut_off_length();
                                BigDecimal b5=new  BigDecimal(res31);
                                res31=b5.setScale(2,  BigDecimal.ROUND_HALF_UP).floatValue();
                            }
                            //管段重切
                            //List<String>cutList=getPipeBevelOfDay(item,project_no,external_coating,internal_coating,od,wt);

                            int res32=getTotalOfSampleCutoff(item,project_no,external_coating,internal_coating,od,wt);
                            int res33=getTotalOfBarePipeGrindCutOff(item,project_no,external_coating,internal_coating,od,wt);
                            //int res32=Integer.parseInt(cutList.get(1));
                            //发运成品管
                            int res34=0;
                            float res35=0f;
                            //向日报中填充数据
                            //首先判断日报表中是否有此数据，如果没有则添加，否则进行更新,参数(time,project_no,od_wt,外防类型)
                            List<DailyProductionReport>list1=dailyProductionReportDao.getDailyReportByParams(project_no,external_coating,od_wt,timeformat.parse(item));
                            if(list1.size()>0){
                                //更新
                                int id=list1.get(0).getId();
                                DailyProductionReport report=list1.get(0);
                                report.setBare_pipe_count(0);
                                report.setBare_pipe_length(0);
                                report.setOd_total_coated_count(res4);
                                report.setOd_total_accepted_count(res5);
                                report.setOd_aiming_accepted_count(res7);
                                report.setOd_total_accepted_length(res6);
                                report.setOd_repair_pipe_count(res9);
                                report.setOd_bare_pipe_onhold_count(res10);
                                report.setOd_bare_pipe_grinded_count(res11);
                                report.setOd_bare_pipe_cut_count(res12);
                                report.setOd_coated_pipe_rejected_count(res13);
                                report.setOd_coated_pipe_strip_count(res14);
                                report.setId_total_accepted_count(res15);
                                report.setId_aiming_accepted_count(0);
                                report.setId_total_accepted_length(res17);
                                report.setId_aiming_total_accepted_length(0);
                                report.setId_repair_pipe_count(res20);
                                report.setId_bare_pipe_onhold_count(res21);
                                report.setId_bare_pipe_grinded_count(res22);
                                report.setId_bare_pipe_cut_count(res23);
                                report.setId_coated_pipe_rejected_count(res24);
                                report.setId_coated_pipe_strip_count(res25);
                                report.setOd_test_pipe_no_dayshift(res26);
                                report.setOd_test_pipe_length_before_cut_dayshift(res27);
                                report.setOd_test_pipe_cutting_length_dayshift(res28);
                                report.setOd_test_pipe_no_nightshift(res29);
                                report.setOd_test_pipe_length_before_cut_nightshift(res30);
                                report.setOd_test_pipe_cutting_length_nightshift(res31);
                                report.setOd_test_pipe_count(res32);
                                report.setRebevel_pipe_count(res33);
                                report.setPipe_accepted_count_after_rebevel(res33);
                                report.setPipe_delivered_count(0);
                                report.setPipe_delivered_length(0);
                                dailyProductionReportDao.updateDailyProductionReport(report);
                            }else{
                                //添加
                                DailyProductionReport report=new DailyProductionReport();
                                report.setBare_pipe_count(0);
                                report.setBare_pipe_length(0);
                                report.setOd_total_coated_count(res4);
                                report.setOd_total_accepted_count(res5);
                                report.setOd_aiming_accepted_count(res7);
                                report.setOd_total_accepted_length(res6);
                                report.setOd_repair_pipe_count(res9);
                                report.setOd_bare_pipe_onhold_count(res10);
                                report.setOd_bare_pipe_grinded_count(res11);
                                report.setOd_bare_pipe_cut_count(res12);
                                report.setOd_coated_pipe_rejected_count(res13);
                                report.setOd_coated_pipe_strip_count(res14);
                                report.setId_total_accepted_count(res15);
                                report.setId_aiming_accepted_count(0);
                                report.setId_total_accepted_length(res17);
                                report.setId_aiming_total_accepted_length(0);
                                report.setId_repair_pipe_count(res20);
                                report.setId_bare_pipe_onhold_count(res21);
                                report.setId_bare_pipe_grinded_count(res22);
                                report.setId_bare_pipe_cut_count(res23);
                                report.setId_coated_pipe_rejected_count(res24);
                                report.setId_coated_pipe_strip_count(res25);
                                report.setOd_test_pipe_no_dayshift(res26);
                                report.setOd_test_pipe_length_before_cut_dayshift(res27);
                                report.setOd_test_pipe_cutting_length_dayshift(res28);
                                report.setOd_test_pipe_no_nightshift(res29);
                                report.setOd_test_pipe_length_before_cut_nightshift(res30);
                                report.setOd_test_pipe_cutting_length_nightshift(res31);
                                report.setOd_test_pipe_count(res32);
                                report.setRebevel_pipe_count(res33);
                                report.setPipe_accepted_count_after_rebevel(res33);
                                report.setPipe_delivered_count(0);
                                report.setPipe_delivered_length(0);
                                dailyProductionReportDao.addDailyProductionReport(report);
                            }
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
    private List<ContractInfo> getAllContractInfo(String project_no){
          List<ContractInfo>list=contractInfoDao.getAllContractInfoByProjectNo(project_no);
          return list;
    }
    //生成tab的排列组合,根据od-wt,external_coating,internal_coating进行组合
    private List<GroupEntity>getTabGroup(List<ContractInfo>contractInfoList){
        List<GroupEntity>tabGroupList=new ArrayList<>();//od-wt,external_coating,internal_coating组合
        List<String>odWtStrList=new ArrayList<>();//od-wt组合
        List<HashMap<String,String>>odwtNewList=new ArrayList<>();
        List<String>externalList=new ArrayList<>();//外防组合
        List<String>internalList=new ArrayList<>();//内防组合
        String odwtKey="",odwtValue="";
        for (ContractInfo item:contractInfoList){
           //获取od-wt的组合数
            odwtKey =String.valueOf(item.getOd());
            odwtValue=String.valueOf(item.getWt());
            if(!odWtStrList.contains(odwtKey+"*"+odwtValue)){
               HashMap<String,String>tempMap=new HashMap<>();
               tempMap.put("od",String.valueOf(item.getOd()));
               tempMap.put("wt",String.valueOf(item.getWt()));
               odwtNewList.add(tempMap);
               odWtStrList.add(String.valueOf(item.getOd())+"*"+String.valueOf(item.getWt()));
            }
            //获取外防组合数
            if(!externalList.contains(String.valueOf(item.getExternal_coating()))){
                   externalList.add(String.valueOf(item.getExternal_coating()));
            }

            //获取内防组合数
            if(!internalList.contains(String.valueOf(item.getInternal_coating()))){
                internalList.add(String.valueOf(item.getInternal_coating()));
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
    private List<Object> getTotalQualifiedOdCoating(String now,String project_no,String external_coating,String internal_coating,float od,float wt){
        String nextday=DateTimeUtil.getNextDay(now);
        List<Object>list=new ArrayList<>();
        try{
            Date beginTime=timeformat.parse(now+" 08:00:00");
            Date endTime=timeformat.parse(nextday+" 08:00:00");
            List<HashMap<String,Object>>list1=odFinalInspectionProcessDao.getTotalQualifiedOfDay(project_no,external_coating,internal_coating,od,wt,beginTime,endTime,"1");
            if(list1!=null){
                HashMap<String,Object>hs=list1.get(0);
                if(hs!=null){
                    list.add(hs.get("count1"));
                    list.add(hs.get("count2"));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
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
            total=coatingRepairDao.getTotalCoatingRepairOfTime(project_no,null,external_coating,internal_coating,odid,od,wt,beginTime,endTime);
        }catch (Exception e){
            e.printStackTrace();
        }
        return total;
    }
    //4.获取当天的防腐光管隔离数
    private int getTotalBarePipeGrindCutoffOfTime(String now,String project_no,String external_coating,String internal_coating,String odid,float od,float wt){
        String nextday=DateTimeUtil.getNextDay(now);
        int total=0;
        try{
            Date beginTime=timeformat.parse(now+" 08:00:00");
            Date endTime=timeformat.parse(nextday+" 08:00:00");
            barePipeGrindingCutoffRecordDao.getTotalBarePipeGrindCutoffOfTime(project_no,null,external_coating,internal_coating,odid,od,wt,beginTime,endTime);
        }catch (Exception e){
            e.printStackTrace();
        }finally {

        }
        return total;
    }
    //5.光管隔离修磨数量
    private int getTotalBarePipeGrindOfTime(String now,String project_no,String external_coating,String internal_coating,String odid,float od,float wt){
        String nextday=DateTimeUtil.getNextDay(now);
        int total=0;
        try{
            Date beginTime=timeformat.parse(now+" 08:00:00");
            Date endTime=timeformat.parse(nextday+" 08:00:00");
            barePipeGrindingCutoffRecordDao.getTotalBarePipeGrindOfTime(project_no,null,external_coating,internal_coating,odid,od,wt,beginTime,endTime);
        }catch (Exception e){
            e.printStackTrace();
        }finally {

        }
        return total;
    }
    //6.光管隔离切管数量
    private int getTotalBarePipeCutoffOfTime(String now,String project_no,String external_coating,String internal_coating,String odid,float od,float wt){
        String nextday=DateTimeUtil.getNextDay(now);
        int total=0;
        try{
            Date beginTime=timeformat.parse(now+" 08:00:00");
            Date endTime=timeformat.parse(nextday+" 08:00:00");
            barePipeGrindingCutoffRecordDao.getTotalBarePipeCutoffOfTime(project_no,null,external_coating,internal_coating,odid,od,wt,beginTime,endTime);
        }catch (Exception e){
            e.printStackTrace();
        }finally {

        }
        return total;
    }
    //7.获取废管数量
    private int getTotalWastePipe(String now,String project_no,String external_coating,String internal_coating,String odid,float od,float wt){
        String nextday=DateTimeUtil.getNextDay(now);
        int total=0;
        try{
            Date beginTime=timeformat.parse(now+" 08:00:00");
            Date endTime=timeformat.parse(nextday+" 08:00:00");
            coatingStripDao.getStripOfTime(project_no,null,external_coating,internal_coating,odid,od,wt,beginTime,endTime);
        }catch (Exception e){
            e.printStackTrace();
        }finally {

        }
        return total;
    }
    //8.获取废管处理数量
    private int getTotalHandleWastePipe(String now,String project_no,String external_coating,String internal_coating,String odid,float od,float wt){
        String nextday=DateTimeUtil.getNextDay(now);
        int total=0;
        try{
            Date beginTime=timeformat.parse(now+" 08:00:00");
            Date endTime=timeformat.parse(nextday+" 08:00:00");
            coatingStripDao.getTotalStripQuailfiedOfTime(project_no,null,external_coating,internal_coating,odid,od,wt,beginTime,endTime);
        }catch (Exception e){
            e.printStackTrace();
        }finally {

        }
        return total;
    }

    //7.获取外防腐涂层管废管数量和废管处理数量(当天符合参数的待扒皮数量和扒皮完成的和是废管数量)
//    private List<String> getTotalOdWastePipe(String now,String project_no,String external_coating,String internal_coating,float od,float wt){
//        List<String>list=new ArrayList<>();
//        int total0=0,total1=0,total2=0;
//        try{
//            String nextday=DateTimeUtil.getNextDay(now);
//            Date beginTime=timeformat.parse(now+" 08:00:00");
//            Date endTime=timeformat.parse(nextday+" 08:00:00");
//            if(external_coating.equals("2FBE")){
//                total0=odCoatingInspectionProcessDao.getTotalOdWastePipe(project_no,external_coating,internal_coating,od,wt,beginTime,endTime);//待扒皮数量
//            }else if(external_coating.equals("3LPE")){
//                total0=odCoating3LpeInspectionProcessDao.getTotalOdWastePipe(project_no,external_coating,internal_coating,od,wt,beginTime,endTime);//待扒皮数量
//            }
//            total1=getTotalBarePipeGrindingInfo(now,project_no,external_coating,internal_coating,"od",od,wt);//废管处理数量
//            total2=total0+total1;//废管数量
//        }catch (Exception e){
//            e.printStackTrace();
//        }finally {
//            list.add(String.valueOf(total2));
//            list.add(String.valueOf(total1));
//        }
//        return list;
//    }
    //8.获取废管处理数量
    private int getTotalBarePipeGrindingInfo(String now,String project_no,String external_coating,String internal_coating,String odid,float od,float wt){
        String nextday=DateTimeUtil.getNextDay(now);
        int total=0;
        try{
            Date beginTime=timeformat.parse(now+" 08:00:00");
            Date endTime=timeformat.parse(nextday+" 08:00:00");
            total=coatingStripDao.getTotalStripOfTime(project_no,null,external_coating,internal_coating,odid,od,wt,beginTime,endTime);
        }catch (Exception e){
            e.printStackTrace();
        }
        return total;
    }
    //---------------内防腐------------
    //9.获取当天内防腐总数
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
    //10.获取当天的内防腐合格数和长度
    private List<Object> getTotalQualifiedIdCoating(String now,String project_no,String external_coating,String internal_coating,float od,float wt){
        String nextday=DateTimeUtil.getNextDay(now);
        List<Object>list=new ArrayList<>();
        //String total0="0",total1="0";
        try{
            Date beginTime=timeformat.parse(now+" 08:00:00");
            Date endTime=timeformat.parse(nextday+" 08:00:00");
            List<HashMap<String,Object>>list1=idFinalInspectionProcessDao.getTotalQualifiedOfDay(project_no,external_coating,internal_coating,od,wt,beginTime,endTime,"1");
            if(list1!=null){
                HashMap<String,Object>hs=list1.get(0);
                if(hs!=null){
                    list.add(hs.get("count1"));
                    list.add(hs.get("count2"));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    //11.获取内防腐涂层管废管数量和废管处理数量(当天符合参数的待扒皮数量和扒皮完成的和是废管数量)
//    private List<String> getTotalIdWastePipe(String now,String project_no,String external_coating,String internal_coating,float od,float wt){
//        List<String>list=new ArrayList<>();
//        int total0=0,total1=0,total2=0;
//        try{
//            String nextday=DateTimeUtil.getNextDay(now);
//            Date beginTime=timeformat.parse(now+" 08:00:00");
//            Date endTime=timeformat.parse(nextday+" 08:00:00");
//            total0=idCoatingInspectionProcessDao.getTotalIdWastePipe(project_no,external_coating,internal_coating,od,wt,beginTime,endTime);
//            total1=getTotalBarePipeGrindingInfo(now,project_no,external_coating,internal_coating,"id",od,wt);//废管处理数量
//            total2=total0+total1;//废管数量
//        }catch (Exception e){
//            e.printStackTrace();
//        }finally {
//            list.add(String.valueOf(total2));
//            list.add(String.valueOf(total1));
//        }
//        return list;
//    }
    //12.白班、夜班样管信息
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
    //13.管段切斜信息(切割分为两种，一种是样管切割，一种是瑕疵切割)
    //13.1获取样管切割个数
    private int getTotalOfSampleCutoff(String now,String project_no,String external_coating,String internal_coating,float od,float wt){
        String nextday=DateTimeUtil.getNextDay(now);
        int total=0;
        try{
            Date beginTime=timeformat.parse(now+" 08:00:00");
            Date endTime=timeformat.parse(nextday+" 08:00:00");
            total=pipeSamplingRecordDao.getTotalCutOffOfSample(project_no,null,external_coating,internal_coating,null,od,wt,beginTime,endTime);
        }catch (Exception e){
            e.printStackTrace();
        }
        return total;
    }
    //13.2获取瑕疵切割个数
    private int getTotalOfBarePipeGrindCutOff(String now,String project_no,String external_coating,String internal_coating,float od,float wt){
        String nextday=DateTimeUtil.getNextDay(now);
        int total=0;
        try{
            Date beginTime=timeformat.parse(now+" 08:00:00");
            Date endTime=timeformat.parse(nextday+" 08:00:00");
            total=barePipeGrindingCutoffRecordDao.getTotalCutoffOfBare(project_no,null,external_coating,internal_coating,null,od,wt,beginTime,endTime);
        }catch (Exception e){
            e.printStackTrace();
        }
        return total;
    }
    //13.3获取切割合格数
    private int getTotalQualifiedCutOff(String now,int type,String project_no,String external_coating,String internal_coating,float od,float wt){
        String nextday=DateTimeUtil.getNextDay(now);
        int total=0;
        try{
            Date beginTime=timeformat.parse(now+" 08:00:00");
            Date endTime=timeformat.parse(nextday+" 08:00:00");
            total=pipeRebevelRecordDao.getQualifiedOfCutoff(project_no,external_coating,internal_coating,od,wt,beginTime,endTime);
        }catch (Exception e){
            e.printStackTrace();
        }
        return total;
    }

//    private List<String>getPipeBevelOfDay(String now,String project_no,String external_coating,String internal_coating,float od,float wt){
//        List<String>list=new ArrayList<>();
//        int total0=0,total1=0;
//        try{
//            String nextday=DateTimeUtil.getNextDay(now);
//            Date beginTime=timeformat.parse(now+" 08:00:00");
//            Date endTime=timeformat.parse(nextday+" 08:00:00");
//            List<HashMap<String,Object>>list1=pipeRebevelRecordDao.getPipeBevelOfDay(project_no,external_coating,internal_coating,"1",od,wt,beginTime,endTime);
//            if(list1!=null){
//                HashMap<String,Object>hs=list1.get(0);
//                if(hs!=null){
//                    total0=Integer.parseInt(String.valueOf(hs.get("count1")));
//                    total1=Integer.parseInt(String.valueOf(hs.get("count2")));
//                }
//            }
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }finally {
//            list.add(String.valueOf(total0));
//            list.add(String.valueOf(total1));
//        }
//        return list;
//    }
}
