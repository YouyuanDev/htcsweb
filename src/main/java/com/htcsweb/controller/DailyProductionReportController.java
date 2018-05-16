package com.htcsweb.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.htcsweb.dao.*;
import com.htcsweb.entity.*;
import com.htcsweb.util.DateTimeUtil;
import com.htcsweb.util.GenerateExcelToPDFUtil;
import com.htcsweb.util.GroupEntity;
import com.htcsweb.util.ResponseUtil;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.*;
import org.apache.ibatis.jdbc.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.incrementer.HsqlMaxValueIncrementer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
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
    @Autowired
    private ProjectInfoDao projectInfoDao;
    //统计数量和长度
    private class CountSum{
        public int count=0;
        public float sum=0;
    }

    //生产日报的excel并下载
    @RequestMapping(value="getDailyRecordReportExcel",produces="application/json;charset=UTF-8")
    @ResponseBody
    public  String getDailyRecordReportExcel(HttpServletRequest request, HttpServletResponse response) {
        String basePath = request.getSession().getServletContext().getRealPath("/");
        Date start_time = null;
        Date finish_time = null;
        //是否成功标识
        String zipName = "fail";
        //先获取选择的日期区间之间的所有日期
        String project_no = request.getParameter("project_no");
        String beginTimeStr = request.getParameter("beginTime");
        String endTimeStr = request.getParameter("endTime");
       // String project_name=request.getParameter("project_name");
        try{
            //先清理.zip垃圾文件
            File fileZip=new File(basePath+"upload/pdf/");
            if(fileZip.exists()&&fileZip.isDirectory()){
                String zipList[]=fileZip.list();
                for (String zippath:zipList){
                    File file=new File(basePath+"/upload/pdf/"+zippath);
                    if(file.isFile()&&file.getName().endsWith(".zip")){
                        file.delete();
                    }
                }
            }
            WritableCellFormat wcf=cellFormat3();
            String excelTemplateFullName=request.getSession().getServletContext().getRealPath("/")
                    +"template/daliy_production_report_template.xls";
            String newExcelFileName=null;
             //获取数据库中的数据
             if(project_no!=null&&!project_no.equals("")&&beginTimeStr!=null&&!beginTimeStr.equals("")&&endTimeStr!=null&&!endTimeStr.equals("")){
                 String project_name=" ",client_name=" ";
                 List<ProjectInfo>projectInfoList=projectInfoDao.getProjectInfoByProjectNo(project_no);
                 if(projectInfoList!=null&&projectInfoList.size()>0){
                     ProjectInfo projectInfo=projectInfoList.get(0);
                     if(projectInfo.getProject_name()!=null)
                         project_name=projectInfo.getProject_name();
                     if(projectInfo.getClient_name()!=null)
                         client_name=projectInfo.getClient_name();
                 }
                 start_time=timeformat.parse(beginTimeStr+" 08:00:00");
                 finish_time=timeformat.parse(endTimeStr+" 08:00:00");
                 List<DailyProductionReport>dailyProductionReportList=null;
//                 File file0=new File(newExcelFileName);
//                 if(!file0.exists()){
//                     file0.createNewFile();
//                 }
                 String tabName="",od_wt="",external_coating="",internal_coating="";

                // Date begin_time=sdf.parse(beginTimeStr);
                 //Date end_time=sdf.parse(endTimeStr);
                 System.out.println(beginTimeStr+":"+endTimeStr+":"+project_no);
                 List<GroupEntity>grouplist=getTabGroup(project_no);
                 List<String> dateList= DateTimeUtil.getBetweenDates(start_time,finish_time);
                 float od=0,wt=0;
                 System.out.println(grouplist.size()+":"+dateList.size()+"~~~~~~");
                 for (GroupEntity entity:grouplist){
                     //模版第8行开始
                     int row=8;
                     ArrayList<Label> datalist=new ArrayList<Label>();

                     od=entity.getOd(); wt=entity.getWt();
                     od_wt=String.valueOf(od)+"x"+String.valueOf(wt);

                     external_coating=entity.getExternal_coating();
                     internal_coating=entity.getInternal_coating();

                     tabName=od_wt+external_coating+internal_coating;

                     //定义统计total字段
                     int total_odCoatingCount=0;
                     int total_bareCount=0;
                     float total_bareLength=0;
                     //外防腐合格数
                     int total_odAcceptedCount=0;
                     //合格长度
                     float total_odAcceptedLength=0;
                     //外防目标合格数
                     int total_odTargetAcceptedCount=0;
                     //外防目标合格长度
                     float total_odTargetAcceptedLength=0;
                     //外防修补数
                     int total_odCoatingRepairCount=0;
                     //外防光管隔离数
                     int total_odPipeOnholdCount=0;
                     //外防光管隔离管修磨
                     int total_odBarePipeGrindingCount=0;
                     //外防光管隔离管切管
                     int total_odBareCutoffCount=0;
                     //外防涂层管废管数
                     int total_odCoatingRejectedPipe=0;
                     //外防涂层管废管扒皮处理合格数量
                     int total_odCoatingStripAcceptedCount=0;

                     //获取内防腐总数
                     int total_idCoatingCount=0;
                     //内防腐合格数和长度
                     //合格数
                     int total_idAcceptedCount=0;
                     //合格长度
                     float total_idAcceptedLength=0;
                     //内防目标合格数
                     int total_idTargetAcceptedTotal=0;
                     //内防目标合格长度
                     float total_idTargetAcceptedLength=0f;
                     //内防修补数
                     int total_idCoatingRepairCount=0;
                     //内防光管隔离数
                     int total_idPipeOnholdCount=0;
                     //内防光管隔离管修磨
                     int total_idBarePipeGrindingCount=0;
                     //内防光管隔离管切管
                     int total_idBareCutoffCount=0;
                     //内防涂层管废管数
                     int total_idCoatingRejectCount=0;
                     //内防涂层管废管扒皮处理数量
                     int total_idCoatingStripAcceptedCount=0;

                     //白班试验管原始总长度
                     float total_samplePipeOriginalLengthDayShift=0;
                     //白班试验管切割总长度
                     float total_samplePipeCutLengthDayShift=0;

                     //夜班试验管原始总长度
                     float total_samplePipeOriginalLengthNightShift=0;
                     //夜班试验管切割总长度
                     float total_samplePipeCutLengthNightShift=0;
                     //白夜班实验管数量
                     int total_samplePipeCount=0;

                     //需重新倒棱管数量(切长处理管+样管切样)
                     int total_NeedRebevelCount=0;
                     //重新倒棱合格管数量
                     int total_acceptedRebevelCount=0;
                     //发运成品管数量
                     int total_shippedPipeCount=0;
                     //发运成品管长度
                     float total_shippedPipeLength=0f;

                     for (String item:dateList){
                         //根据笛卡尔集生成对应的tab
                         dailyProductionReportList=dailyProductionReportDao.getDailyReportByParams(project_no,external_coating,internal_coating,od_wt,sdf.parse(item));
                         System.out.println("数据："+dailyProductionReportList.size());
                         if(dailyProductionReportList!=null&&dailyProductionReportList.size()>0){
                             for (DailyProductionReport report:dailyProductionReportList){
                                //od外防统计
                                 total_odCoatingCount+=report.getOd_total_coated_count();
                                 total_bareCount+=report.getBare_pipe_count();
                                 total_bareLength+=report.getBare_pipe_length();
                                 total_odAcceptedCount+=report.getOd_total_accepted_count();

                                 total_odTargetAcceptedCount+=report.getOd_aiming_accepted_count();
                                 total_odTargetAcceptedLength+=report.getOd_aiming_total_accepted_length();

                                 total_odTargetAcceptedLength+=report.getOd_aiming_total_accepted_length();
                                 total_odCoatingRepairCount+=report.getOd_repair_pipe_count();
                                 total_odPipeOnholdCount+=report.getOd_bare_pipe_onhold_count();
                                 total_odBarePipeGrindingCount+=report.getOd_bare_pipe_grinded_count();
                                 total_odBareCutoffCount+=report.getOd_bare_pipe_cut_count();
                                 total_odCoatingRejectedPipe+=report.getOd_coated_pipe_rejected_count();
                                 total_odCoatingStripAcceptedCount+=report.getOd_coated_pipe_strip_count();
                                //id内防统计
                                 total_idCoatingCount+=report.getId_total_coated_count();
                                 total_idAcceptedCount+=report.getId_total_accepted_count();
                                 total_idAcceptedLength+=report.getId_total_accepted_length();
                                 total_idTargetAcceptedTotal+=report.getId_aiming_accepted_count();
                                 total_idTargetAcceptedLength+=report.getId_aiming_total_accepted_length();
                                 total_idCoatingRepairCount+=report.getId_repair_pipe_count();
                                 total_idPipeOnholdCount+=report.getId_bare_pipe_onhold_count();
                                 total_idBarePipeGrindingCount+=report.getId_bare_pipe_grinded_count();
                                 total_idBareCutoffCount+=report.getId_bare_pipe_cut_count();
                                 total_idCoatingRejectCount+=report.getId_coated_pipe_rejected_count();
                                 total_idCoatingStripAcceptedCount+=report.getId_coated_pipe_strip_count();

                                 total_samplePipeOriginalLengthDayShift+=report.getOd_test_pipe_length_before_cut_dayshift();
                                 total_samplePipeCutLengthDayShift+=report.getOd_test_pipe_cutting_length_dayshift();

                                 total_samplePipeOriginalLengthNightShift+=report.getOd_test_pipe_length_before_cut_nightshift();
                                 total_samplePipeCutLengthNightShift+=report.getOd_test_pipe_cutting_length_nightshift();
                                 total_samplePipeCount+=report.getOd_test_pipe_count();


                                 total_NeedRebevelCount+=report.getRebevel_pipe_count();
                                 total_acceptedRebevelCount+=report.getPipe_accepted_count_after_rebevel();
                                 total_shippedPipeCount+=report.getPipe_delivered_count();
                                 total_shippedPipeLength+=report.getPipe_delivered_length();

                                 FillExcelData(datalist,dailyProductionReportList,row,od_wt,project_name,client_name,wcf);
                                 row++;
                             }

                         }

                     }


                     //设置最后一行统计数据行
                     datalist.add(new Label(0,row,"Total：", wcf));
                     datalist.add(new Label(1,row,String.valueOf(total_bareCount), wcf));
                     datalist.add(new Label(2,row,String.valueOf(total_bareLength), wcf));
                     datalist.add(new Label(3,row,String.valueOf(total_odCoatingCount), wcf));
                     datalist.add(new Label(4,row,String.valueOf(total_odAcceptedCount), wcf));
                     datalist.add(new Label(5,row,String.valueOf(total_odTargetAcceptedCount), wcf));
                     datalist.add(new Label(6,row,String.valueOf(total_odAcceptedLength), wcf));
                     datalist.add(new Label(7,row,String.valueOf(total_odTargetAcceptedLength), wcf));
                     datalist.add(new Label(8,row,String.valueOf(total_odCoatingRepairCount), wcf));
                     datalist.add(new Label(9,row,String.valueOf(total_odPipeOnholdCount), wcf));
                     datalist.add(new Label(10,row,String.valueOf(total_odBarePipeGrindingCount), wcf));
                     datalist.add(new Label(11,row,String.valueOf(total_odBareCutoffCount), wcf));
                     datalist.add(new Label(12,row,String.valueOf(total_odCoatingRejectedPipe), wcf));
                     datalist.add(new Label(13,row,String.valueOf(total_odCoatingStripAcceptedCount), wcf));

                     datalist.add(new Label(14,row,String.valueOf(total_idCoatingCount), wcf));
                     datalist.add(new Label(15,row,String.valueOf(total_idAcceptedCount), wcf));
                     datalist.add(new Label(16,row,String.valueOf(total_idTargetAcceptedTotal), wcf));
                     datalist.add(new Label(17,row,String.valueOf(total_idAcceptedLength), wcf));
                     datalist.add(new Label(18,row,String.valueOf(total_idTargetAcceptedLength), wcf));
                     datalist.add(new Label(19,row,String.valueOf(total_idCoatingRepairCount), wcf));
                     datalist.add(new Label(20,row,String.valueOf(total_idPipeOnholdCount), wcf));
                     datalist.add(new Label(21,row,String.valueOf(total_idBarePipeGrindingCount), wcf));
                     datalist.add(new Label(22,row,String.valueOf(total_idBareCutoffCount), wcf));
                     datalist.add(new Label(23,row,String.valueOf(total_idCoatingRejectCount), wcf));
                     datalist.add(new Label(24,row,String.valueOf(total_idCoatingStripAcceptedCount), wcf));

                     datalist.add(new Label(25,row,String.valueOf("NA"), wcf));
                     datalist.add(new Label(26,row,String.valueOf(total_samplePipeOriginalLengthDayShift), wcf));
                     datalist.add(new Label(27,row,String.valueOf(total_samplePipeCutLengthDayShift), wcf));
                     datalist.add(new Label(28,row,String.valueOf("NA"), wcf));
                     datalist.add(new Label(29,row,String.valueOf(total_samplePipeOriginalLengthNightShift), wcf));
                     datalist.add(new Label(30,row,String.valueOf(total_samplePipeCutLengthNightShift), wcf));
                     datalist.add(new Label(31,row,String.valueOf(total_samplePipeCount), wcf));

                     datalist.add(new Label(32,row,String.valueOf(total_NeedRebevelCount), wcf));
                     datalist.add(new Label(33,row,String.valueOf(total_acceptedRebevelCount), wcf));
                     datalist.add(new Label(34,row,String.valueOf(total_shippedPipeCount), wcf));
                     datalist.add(new Label(35,row,String.valueOf(total_shippedPipeLength), wcf));
                     row++;

                     if(datalist.size()>=0){
                         newExcelFileName=GenerateExcelToPDFUtil.FillExcelTemplate(excelTemplateFullName,newExcelFileName,datalist,tabName,"template/img/image004.png");
                     }
                 }
                 if(dailyProductionReportList!=null&&dailyProductionReportList.size()>0) {
                     ArrayList list = new ArrayList();
                     list.add(newExcelFileName);
                     zipName = "upload/pdf/" + ResponseUtil.downLoadPdf(list, request, response);
                 }
                 //删除多余文件
//                 if(file0.exists()){
//                    file0.delete();
//                 }
             }
        }catch (Exception e){
            e.printStackTrace();
        }
        return JSONObject.toJSONString(zipName);
    }

    private  void  FillExcelData(ArrayList<Label> datalist,List<DailyProductionReport>dailyProductionReportList,int row,String tabName,String project_name,String client_name,WritableCellFormat wcf){
        int i =0;
        try{
            for (DailyProductionReport report:dailyProductionReportList){

                //根据循环的个数创建sheet
                tabName=getFormatString(report.getOd_wt());
                if(i==0) {
                    i++;
                    datalist.add(new Label(1, 3,client_name , wcf));
                    datalist.add(new Label(3, 3, project_name, cellFormat2()));
                    datalist.add(new Label(34, 3, tabName + "mm", wcf));
                    datalist.add(new Label(34, 4, getFormatString(report.getOd_coating_type()), wcf));
                    datalist.add(new Label(3, 5, getFormatString(report.getOd_coating_type()) + " External coated pipe", cellFormat1()));
                    datalist.add(new Label(14, 5, getFormatString(report.getId_coating_type()) + " Internal coated pipe", cellFormat1()));
                }
                datalist.add(new Label(0,row,sdf.format(report.getProduction_date()), wcf));
                datalist.add(new Label(1,row,String.valueOf(report.getBare_pipe_count()), wcf));
                datalist.add(new Label(2,row,String.valueOf(report.getBare_pipe_length()), wcf));
                datalist.add(new Label(3,row,String.valueOf(report.getOd_total_coated_count()), wcf));
                datalist.add(new Label(4,row,String.valueOf(report.getOd_total_accepted_count()), wcf));
                datalist.add(new Label(5,row,String.valueOf(report.getOd_aiming_accepted_count()), wcf));
                datalist.add(new Label(6,row,String.valueOf(report.getOd_total_accepted_length()), wcf));
                datalist.add(new Label(7,row,String.valueOf(report.getOd_aiming_total_accepted_length()), wcf));
                datalist.add(new Label(8,row,String.valueOf(report.getOd_repair_pipe_count()), wcf));
                datalist.add(new Label(9,row,String.valueOf(report.getOd_bare_pipe_onhold_count()), wcf));
                datalist.add(new Label(10,row,String.valueOf(report.getOd_bare_pipe_grinded_count()), wcf));
                datalist.add(new Label(11,row,String.valueOf(report.getOd_bare_pipe_cut_count()), wcf));
                datalist.add(new Label(12,row,String.valueOf(report.getOd_coated_pipe_rejected_count()), wcf));
                datalist.add(new Label(13,row,String.valueOf(report.getOd_coated_pipe_strip_count()), wcf));

                datalist.add(new Label(14,row,String.valueOf(report.getId_total_coated_count()), wcf));
                datalist.add(new Label(15,row,String.valueOf(report.getId_total_accepted_count()), wcf));
                datalist.add(new Label(16,row,String.valueOf(report.getId_aiming_accepted_count()), wcf));
                datalist.add(new Label(17,row,String.valueOf(report.getId_total_accepted_length()), wcf));
                datalist.add(new Label(18,row,String.valueOf(report.getId_aiming_total_accepted_length()), wcf));
                datalist.add(new Label(19,row,String.valueOf(report.getId_repair_pipe_count()), wcf));
                datalist.add(new Label(20,row,String.valueOf(report.getId_bare_pipe_onhold_count()), wcf));
                datalist.add(new Label(21,row,String.valueOf(report.getId_bare_pipe_grinded_count()), wcf));
                datalist.add(new Label(22,row,String.valueOf(report.getId_bare_pipe_cut_count()), wcf));
                datalist.add(new Label(23,row,String.valueOf(report.getId_coated_pipe_rejected_count()), wcf));
                datalist.add(new Label(24,row,String.valueOf(report.getId_coated_pipe_strip_count()), wcf));

                datalist.add(new Label(25,row,String.valueOf(report.getOd_test_pipe_no_dayshift()), wcf));
                datalist.add(new Label(26,row,String.valueOf(report.getOd_test_pipe_length_before_cut_dayshift()), wcf));
                datalist.add(new Label(27,row,String.valueOf(report.getOd_test_pipe_cutting_length_dayshift()), wcf));
                datalist.add(new Label(28,row,String.valueOf(report.getOd_test_pipe_no_nightshift()), wcf));
                datalist.add(new Label(29,row,String.valueOf(report.getOd_test_pipe_length_before_cut_nightshift()), wcf));
                datalist.add(new Label(30,row,String.valueOf(report.getOd_test_pipe_cutting_length_nightshift()), wcf));
                datalist.add(new Label(31,row,String.valueOf(report.getOd_test_pipe_count()), wcf));

                datalist.add(new Label(32,row,String.valueOf(report.getRebevel_pipe_count()), wcf));
                datalist.add(new Label(33,row,String.valueOf(report.getPipe_accepted_count_after_rebevel()), wcf));
                datalist.add(new Label(34,row,String.valueOf(report.getPipe_delivered_count()), wcf));
                datalist.add(new Label(35,row,String.valueOf(report.getPipe_delivered_length()), wcf));
                //工程累计

            }
        }catch (Exception e){

        }


    }

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
                //System.out.println(beginTime.toString());
            }
            if(end_time!=null&&end_time!=""){
                endTime=sdf.parse(end_time);
                //System.out.println(endTime.toString());
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
        int result=-1;
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
                    List<GroupEntity>grouplist=getTabGroup(project_no);
                    //遍历排列组合，生成组合对应的tab
                    float od=0f,wt=0f;
                    String external_coating="",internal_coating="";
                    String od_wt="";
                    // System.out.println("排列组合数量："+grouplist.size());
//                    for (GroupEntity entity:grouplist){
//                        for (String item:dateList){
//                            System.out.println(item+":"+entity.getOd()+":"+entity.getWt()+":"+entity.getExternal_coating()+":"+entity.getInternal_coating());
//                        }
//                    }
                    for (GroupEntity entity:grouplist){
                        od=entity.getOd();
                        wt=entity.getWt();
                        od_wt=String.valueOf(od)+"x"+String.valueOf(wt);
                        external_coating=entity.getExternal_coating();
                        internal_coating=entity.getInternal_coating();

                        for (String item:dateList){
                            //根据日期填充对应的tab
                            //获取外防腐总数
                            int odCoatingCount=getOdCoatingCount(item,project_no,external_coating,internal_coating,od,wt);
                            //接收光管数量，长度
                            int bareCount=0;
                            float bareLength=0;
                            //外防腐合格数和长度
                            CountSum countSum=getOdCoatingAcceptedCount(item,project_no,external_coating,internal_coating,od,wt);
                            int odAcceptedCount=0;float odAcceptedLength=0;
                            //外防腐合格数
                            odAcceptedCount=countSum.count;
                            //合格长度
                            odAcceptedLength=countSum.sum;
                            //外防目标合格数
                            int odTargetAcceptedCount=0;
                            //外防目标合格长度
                            float odTargetAcceptedLength=0;
                            //外防修补数
                            int odCoatingRepairCount=getDailyCoatingRepairCount(item,project_no,external_coating,internal_coating,"od",od,wt);
                            //外防光管隔离数
                            int odPipeOnholdCount=getBarePipeOnholdCount(item,project_no,external_coating,internal_coating,"od",od,wt);
                            //外防光管隔离管修磨
                            int odBarePipeGrindingCount=getBarePipeGrindingCount(item,project_no,external_coating,internal_coating,"od",od,wt);
                            //外防光管隔离管切管
                            int odBareCutoffCount=getBarePipeCutoffCount(item,project_no,external_coating,internal_coating,"od",od,wt);
                            //外防涂层管废管数
                            int odCoatingRejectedPipe=getCoatingRejectedPipe(item,project_no,external_coating,internal_coating,"od",od,wt);
                            //外防涂层管废管扒皮处理合格数量
                            int odCoatingStripAcceptedCount=getStripPipeAcceptedCount(item,project_no,external_coating,internal_coating,"od",od,wt);
                            //获取内防腐总数
                            int idCoatingCount=getIdCoatingCount(item,project_no,external_coating,internal_coating,od,wt);
                            CountSum countSum1= getIdCoatingAcceptedCount(item,project_no,external_coating,internal_coating,od,wt);
                            //内防腐合格数和长度
                            int idAcceptedCount=0;float idAcceptedLength=0;
                            //合格数
                            idAcceptedCount=countSum1.count;
                            //合格长度
                            idAcceptedLength=countSum1.sum;
                            //内防目标合格数
                            int idTargetAcceptedTotal=0;
                            //内防目标合格长度
                            float idTargetAcceptedLength=0f;
                            //内防修补数
                            int idCoatingRepairCount=getDailyCoatingRepairCount(item,project_no,external_coating,internal_coating,"id",od,wt);
                            //内防光管隔离数
                            int idPipeOnholdCount=getBarePipeOnholdCount(item,project_no,external_coating,internal_coating,"id",od,wt);
                            //内防光管隔离管修磨
                            int idBarePipeGrindingCount=getBarePipeGrindingCount(item,project_no,external_coating,internal_coating,"id",od,wt);
                            //内防光管隔离管切管
                            int idBareCutoffCount=getBarePipeCutoffCount(item,project_no,external_coating,internal_coating,"id",od,wt);
                            //内防涂层管废管数
                            int idCoatingRejectCount=getCoatingRejectedPipe(item,project_no,external_coating,internal_coating,"id",od,wt);
                            //内防涂层管废管扒皮处理数量
                            int idCoatingStripAcceptedCount=getStripPipeAcceptedCount(item,project_no,external_coating,internal_coating,"id",od,wt);

                            //试验管白班
                            List<PipeSamplingRecord>sampleDayList=getPipeSamplingInfo(item,0,project_no,external_coating,internal_coating,od,wt);
                            //白班试验管编号、原始长度、切样长度
                            String samplePipeNoDayShift=" ";
                            float samplePipeOriginalLengthDayShift=0f,samplePipeCutLengthDayShift=0f;
                            //试验管数量
                            int samplePipeCount=0;
                            if(sampleDayList!=null&&sampleDayList.size()>0){
                                    PipeSamplingRecord record=sampleDayList.get(0);
                                    samplePipeNoDayShift=record.getPipe_no();
                                    samplePipeOriginalLengthDayShift=record.getOriginal_pipe_length();
                                    BigDecimal b2=new  BigDecimal(samplePipeOriginalLengthDayShift);
                                    samplePipeOriginalLengthDayShift=b2.setScale(2,  BigDecimal.ROUND_HALF_UP).floatValue();
                                    samplePipeCutLengthDayShift=record.getCut_off_length();
                                    BigDecimal b3=new  BigDecimal(samplePipeCutLengthDayShift);
                                    samplePipeCutLengthDayShift=b3.setScale(2,  BigDecimal.ROUND_HALF_UP).floatValue();
                                    samplePipeCount++;
                            }
                            List<PipeSamplingRecord>sampleNightList=getPipeSamplingInfo(item,1,project_no,external_coating,internal_coating,od,wt);
                            //夜班试验管编号、原始长度、切样长度
                            String samplePipeNoNightShift=" ";float  samplePipeOriginalLengthNightShift=0f,samplePipeCutLengthNightShift=0f;
                            if(sampleNightList!=null&&sampleNightList.size()>0){
                                PipeSamplingRecord record=sampleNightList.get(0);
                                samplePipeNoNightShift=record.getPipe_no();
                                samplePipeOriginalLengthNightShift=record.getOriginal_pipe_length();
                                BigDecimal b4=new  BigDecimal(samplePipeOriginalLengthNightShift);
                                samplePipeOriginalLengthNightShift=b4.setScale(2,  BigDecimal.ROUND_HALF_UP).floatValue();
                                samplePipeCutLengthNightShift=record.getCut_off_length();
                                BigDecimal b5=new  BigDecimal(samplePipeCutLengthNightShift);
                                samplePipeCutLengthNightShift=b5.setScale(2,  BigDecimal.ROUND_HALF_UP).floatValue();
                                samplePipeCount++;
                            }

                            //需重新倒棱管数量(切长处理管+样管切样)
                            int onholdCutOffCount=getBarePipeCutoffCount(item,project_no,external_coating,internal_coating,od,wt);

                            int sampleCutoffCount=getSampleCutoffCount(item,project_no,external_coating,internal_coating,od,wt);
                            //System.out.println(sampleCutoffTotal+":"+grindCutOffTotal+"----------------");
                            int cutOffTotalCount=sampleCutoffCount+onholdCutOffCount;
                            //重新倒棱合格管数量
                            int acceptedRebevelCount=getAcceptedRebevelCount(item,project_no,external_coating,internal_coating,od,wt);
                            //发运成品管数量
                            int shippedPipeCount=0;
                            //发运成品管长度
                            float shippedPipeLength=0f;
                            //向日报中填充数据
                            //首先判断日报表中是否有此数据，如果没有则添加，否则进行更新,参数(time,project_no,od_wt,外防类型)
                            List<DailyProductionReport>list1=dailyProductionReportDao.getDailyReportByParams(project_no,external_coating,internal_coating,od_wt,sdf.parse(item));
                            //System.out.println("odCoatingStripAcceptedCount："+odCoatingStripAcceptedCount);
                            DailyProductionReport report=new DailyProductionReport();
                            if(list1!=null&&list1.size()>0){
                                report=list1.get(0);
                                System.out.println("-----------更新"+samplePipeCount);
                            }
                            //设置数据字段
                            report.setBare_pipe_count(bareCount);
                            report.setBare_pipe_length(bareLength);
                            report.setOd_total_coated_count(odCoatingCount);
                            report.setOd_total_accepted_count(odAcceptedCount);
                            report.setOd_total_accepted_length(odAcceptedLength);
                            report.setOd_aiming_accepted_count(odTargetAcceptedCount);
                            report.setOd_aiming_total_accepted_length(odTargetAcceptedLength);
                            report.setOd_repair_pipe_count(odCoatingRepairCount);
                            report.setOd_bare_pipe_onhold_count(odPipeOnholdCount);
                            report.setOd_bare_pipe_grinded_count(odBarePipeGrindingCount);
                            report.setOd_bare_pipe_cut_count(odBareCutoffCount);
                            report.setOd_coated_pipe_rejected_count(odCoatingRejectedPipe);
                            report.setOd_coated_pipe_strip_count(odCoatingStripAcceptedCount);
                            report.setId_total_coated_count(idCoatingCount);
                            report.setId_total_accepted_count(idAcceptedCount);
                            report.setId_total_accepted_length(idAcceptedLength);
                            report.setId_aiming_accepted_count(idTargetAcceptedTotal);
                            report.setId_aiming_total_accepted_length(idTargetAcceptedLength);
                            report.setId_repair_pipe_count(idCoatingRepairCount);
                            report.setId_bare_pipe_onhold_count(idPipeOnholdCount);
                            report.setId_bare_pipe_grinded_count(idBarePipeGrindingCount);
                            report.setId_bare_pipe_cut_count(idBareCutoffCount);
                            report.setId_coated_pipe_rejected_count(idCoatingRejectCount);
                            report.setId_coated_pipe_strip_count(idCoatingStripAcceptedCount);

                            report.setOd_test_pipe_no_dayshift(samplePipeNoDayShift);
                            report.setOd_test_pipe_length_before_cut_dayshift(samplePipeOriginalLengthDayShift);
                            report.setOd_test_pipe_cutting_length_dayshift(samplePipeCutLengthDayShift);
                            report.setOd_test_pipe_no_nightshift(samplePipeNoNightShift);
                            report.setOd_test_pipe_length_before_cut_nightshift(samplePipeOriginalLengthNightShift);
                            report.setOd_test_pipe_cutting_length_nightshift(samplePipeCutLengthNightShift);

                            report.setOd_test_pipe_count(samplePipeCount);
                            report.setRebevel_pipe_count(cutOffTotalCount);
                            report.setPipe_accepted_count_after_rebevel(acceptedRebevelCount);
                            report.setPipe_delivered_count(shippedPipeCount);
                            report.setPipe_delivered_length(shippedPipeLength);

                            report.setProduction_date(timeformat.parse(item+" 08:00:00"));
                            report.setProject_no(project_no);
                            report.setOd_coating_type(external_coating);
                            report.setId_coating_type(internal_coating);
                            report.setOd_wt(od_wt);
                            if(list1.size()>0){
                                //更新
                                 result=dailyProductionReportDao.updateDailyProductionReport(report);
                            }else{
                                //添加
                                //System.out.println("getOd_total_coated_count："+report.getOd_total_coated_count());
                                  result=dailyProductionReportDao.addDailyProductionReport(report);
                                //System.out.println("执行结果："+result);
                            }
                        }
                    }
                }
            }

            if(result>0){
                flag="success";
            }
        }catch (Exception e){
            e.printStackTrace();
            flag="error";
        }


        return JSONArray.toJSONString(flag);
    }
    //


    //生成tab的排列组合,根据od-wt,external_coating,internal_coating进行组合
    private List<GroupEntity>getTabGroup(String project_no){
        List<GroupEntity>tabGroupList=new ArrayList<>();
        List<ContractInfo>list=contractInfoDao.getAllContractInfoByProjectNo(project_no);
        List<String> GroupList=new ArrayList<>();
        for (ContractInfo item:list){
            if(!GroupList.contains(item.getOd()+item.getWt()+item.getExternal_coating()+item.getInternal_coating())){
                GroupList.add(item.getOd()+item.getWt()+item.getExternal_coating()+item.getInternal_coating());
                GroupEntity entity=new GroupEntity();
                entity.setOd(item.getOd());
                entity.setWt(item.getWt());
                entity.setExternal_coating(item.getExternal_coating());
                entity.setInternal_coating(item.getInternal_coating());
                tabGroupList.add(entity);
            }
        }

        return tabGroupList;
    }

    //------------获取各个数据-------
    //1.获取当天外防腐总数(获取外防终检表中数)
    private int getOdCoatingCount(String now,String project_no,String external_coating,String internal_coating,float od,float wt){
        String nextday=DateTimeUtil.getNextDay(now);
        int count=0;
        try{
            Date beginTime=timeformat.parse(now+" 08:00:00");
            Date endTime=timeformat.parse(nextday+" 08:00:00");
            count=odFinalInspectionProcessDao.getODCoatingCount(project_no,null,external_coating,internal_coating,od,wt,beginTime,endTime);
        }catch (Exception e){
            e.printStackTrace();
        }
        return count;
    }
    //2.获取当天外防腐合格支数与合格长度
    private CountSum getOdCoatingAcceptedCount(String now,String project_no,String external_coating,String internal_coating,float od,float wt){
        String nextday=DateTimeUtil.getNextDay(now);
        CountSum countSum=new CountSum();
        try{
            Date beginTime=timeformat.parse(now+" 08:00:00");
            Date endTime=timeformat.parse(nextday+" 08:00:00");
            List<HashMap<String,Object>>list1=odFinalInspectionProcessDao.getODCoatingAcceptedInfo(project_no,external_coating,internal_coating,od,wt,beginTime,endTime,"1");
            if(list1!=null&&list1.size()>0){
                HashMap<String,Object>hs=list1.get(0);
                if(hs!=null){
                    countSum.count=((Long) hs.get("odtotalcount")).intValue();
                    if(hs.get("odtotallength")!=null){
                        countSum.sum=((Double) (hs.get("odtotallength"))).floatValue();
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return countSum;
    }
    //3.获取当天的修补支数(根据od或者id划分)
    private int getDailyCoatingRepairCount(String now,String project_no,String external_coating,String internal_coating,String odid,float od,float wt){
        String nextday=DateTimeUtil.getNextDay(now);
        int count=0;
        try{
            Date beginTime=timeformat.parse(now+" 08:00:00");
            Date endTime=timeformat.parse(nextday+" 08:00:00");
            count=coatingRepairDao.getCoatingRepairCount(project_no,null,external_coating,internal_coating,odid,od,wt,beginTime,endTime);
        }catch (Exception e){
            e.printStackTrace();
        }
        return count;
    }
    //4.获取当天的防腐光管隔离数（外打砂和外打砂检验数量和）
    private int getBarePipeOnholdCount(String now,String project_no,String external_coating,String internal_coating,String odid,float od,float wt){
        String nextday=DateTimeUtil.getNextDay(now);
        int count=0;
        try{
            Date beginTime=timeformat.parse(now+" 08:00:00");
            Date endTime=timeformat.parse(nextday+" 08:00:00");
            if(odid!=null&&odid.equals("od")){
                count=barePipeGrindingCutoffRecordDao.getODBarePipeOnholdCount(project_no,null,external_coating,internal_coating,od,wt,beginTime,endTime);
            }
            else if(odid!=null&&odid.equals("id")){
                count=barePipeGrindingCutoffRecordDao.getIDBarePipeOnholdCount(project_no,null,external_coating,internal_coating,od,wt,beginTime,endTime);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return count;
    }
    //5.光管隔离修磨数量
    private int getBarePipeGrindingCount(String now,String project_no,String external_coating,String internal_coating,String odid,float od,float wt){
        String nextday=DateTimeUtil.getNextDay(now);
        int count=0;
        try{
            Date beginTime=timeformat.parse(now+" 08:00:00");
            Date endTime=timeformat.parse(nextday+" 08:00:00");
            count=barePipeGrindingCutoffRecordDao.getBarePipeGrindingCount(project_no,null,external_coating,internal_coating,odid,od,wt,beginTime,endTime);
        }catch (Exception e){
            e.printStackTrace();
        }
        return count;
    }
    //6.光管隔离切管数量
    private int getBarePipeCutoffCount(String now,String project_no,String external_coating,String internal_coating,String odid,float od,float wt){
        String nextday=DateTimeUtil.getNextDay(now);
        int count=0;
        try{
            Date beginTime=timeformat.parse(now+" 08:00:00");
            Date endTime=timeformat.parse(nextday+" 08:00:00");
            count=barePipeGrindingCutoffRecordDao.getBarePipeCutoffCount(project_no,null,external_coating,internal_coating,odid,od,wt,beginTime,endTime);
        }catch (Exception e){
            e.printStackTrace();
        }
        return count;
    }
    //7.获取涂层废管数量
    private int getCoatingRejectedPipe(String now,String project_no,String external_coating,String internal_coating,String odid,float od,float wt){
        String nextday=DateTimeUtil.getNextDay(now);
        int count=0;
        try{
            Date beginTime=timeformat.parse(now+" 08:00:00");
            Date endTime=timeformat.parse(nextday+" 08:00:00");
            if(odid!=null&&odid.equals("od")){
                count=coatingStripDao.getODCoatingRejectedPipeCount(project_no,null,external_coating,internal_coating,od,wt,beginTime,endTime);
            }
            else if(odid!=null&&odid.equals("id")){
                count=coatingStripDao.getIDCoatingRejectedPipeCount(project_no,null,external_coating,internal_coating,od,wt,beginTime,endTime);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println(project_no+"-"+external_coating+"-"+internal_coating+"-"+odid+"-"+od+"-"+wt+"-"+"+++++++"+count);
        return count;
    }
    //8.涂层废管扒皮处理合格数量
    private int getStripPipeAcceptedCount(String now,String project_no,String external_coating,String internal_coating,String odid,float od,float wt){
        String nextday=DateTimeUtil.getNextDay(now);
        //System.out.println(now+":"+nextday+":"+project_no+":"+external_coating+":"+internal_coating+":"+od+":"+wt+":"+odid);
        int count=0;
        try{
            Date beginTime=timeformat.parse(now+" 08:00:00");
            Date endTime=timeformat.parse(nextday+" 08:00:00");
            count=coatingStripDao.getStripPipeAcceptedCount(project_no,null,external_coating,internal_coating,odid,od,wt,beginTime,endTime);
        }catch (Exception e){
            e.printStackTrace();
        }finally {

        }
        return count;
    }

    //---------------内防腐------------
    //9.获取当天内防腐总数
    private int getIdCoatingCount(String now,String project_no,String external_coating,String internal_coating,float od,float wt){
        String nextday=DateTimeUtil.getNextDay(now);
        int count=0;
        try{
            Date beginTime=timeformat.parse(now+" 08:00:00");
            Date endTime=timeformat.parse(nextday+" 08:00:00");
            count=idFinalInspectionProcessDao.getIDCoatingCount(project_no,null,external_coating,internal_coating,od,wt,beginTime,endTime);
        }catch (Exception e){
            e.printStackTrace();
        }
        return count;
    }
    //10.获取当天的内防腐合格数和长度
    private CountSum getIdCoatingAcceptedCount(String now,String project_no,String external_coating,String internal_coating,float od,float wt){
        String nextday=DateTimeUtil.getNextDay(now);
        CountSum countSum=new CountSum();
        try{
            Date beginTime=timeformat.parse(now+" 08:00:00");
            Date endTime=timeformat.parse(nextday+" 08:00:00");
            List<HashMap<String,Object>>list1=idFinalInspectionProcessDao.getIDCoatingAcceptedInfo(project_no,external_coating,internal_coating,od,wt,beginTime,endTime,"1");
            if(list1!=null&&list1.size()>0){
                HashMap<String,Object>hs=list1.get(0);
                if(hs!=null){
                    countSum.count=((Long)hs.get("idtotalcount")).intValue();
                    if(hs.get("idtotallength")!=null){
                        countSum.sum=((Double) hs.get("idtotallength")).floatValue();
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return countSum;
    }
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
    private int getSampleCutoffCount(String now,String project_no,String external_coating,String internal_coating,float od,float wt){
        String nextday=DateTimeUtil.getNextDay(now);
        int count=0;
        try{
            Date beginTime=timeformat.parse(now+" 08:00:00");
            Date endTime=timeformat.parse(nextday+" 08:00:00");
            count=pipeSamplingRecordDao.getSampleCutoffCount(project_no,null,external_coating,internal_coating,null,od,wt,beginTime,endTime);
        }catch (Exception e){
            e.printStackTrace();
        }
        return count;
    }
    //13.2获取隔离管切割管数量
    private int getBarePipeCutoffCount(String now,String project_no,String external_coating,String internal_coating,float od,float wt){
        String nextday=DateTimeUtil.getNextDay(now);
        int count=0;
        try{
            Date beginTime=timeformat.parse(now+" 08:00:00");
            Date endTime=timeformat.parse(nextday+" 08:00:00");
            count=barePipeGrindingCutoffRecordDao.getBarePipeCutoffCount(project_no,null,external_coating,internal_coating,null,od,wt,beginTime,endTime);
        }catch (Exception e){
            e.printStackTrace();
        }
        return count;
    }
    //13.3获取重倒棱合格管数量
    private int getAcceptedRebevelCount(String now,String project_no,String external_coating,String internal_coating,float od,float wt){
        String nextday=DateTimeUtil.getNextDay(now);
        int count=0;
        try{
            Date beginTime=timeformat.parse(now+" 08:00:00");
            Date endTime=timeformat.parse(nextday+" 08:00:00");
            count=pipeRebevelRecordDao.getAcceptedRebevelCount(project_no,external_coating,internal_coating,od,wt,beginTime,endTime);
        }catch (Exception e){
            e.printStackTrace();
        }
        return count;
    }

    public  String getFormatString(String param){
        if(param!=null&&!param.equals("")){
            return  param;
        }else{
            return  " ";
        }
    }
    private WritableCellFormat cellFormat1(){
        WritableCellFormat cellFormat=null;
        try{
            WritableFont font=new WritableFont(WritableFont.ARIAL,28,WritableFont.BOLD,false, UnderlineStyle.NO_UNDERLINE);
            cellFormat=new WritableCellFormat(font);
            cellFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
            cellFormat.setAlignment(Alignment.CENTRE);
            cellFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
        }catch (Exception e){

        }
        return cellFormat;
    }
    private WritableCellFormat cellFormat2(){
        WritableCellFormat cellFormat=null;
        try{
            WritableFont font=new WritableFont(WritableFont.ARIAL,9,WritableFont.BOLD,false, UnderlineStyle.NO_UNDERLINE);
            cellFormat=new WritableCellFormat(font);
            cellFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
            cellFormat.setAlignment(Alignment.CENTRE);
            cellFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
        }catch (Exception e){
        }
        return cellFormat;
    }
    private WritableCellFormat cellFormat3(){
        WritableCellFormat wcf=null;
        try{
             wcf=new WritableCellFormat();
            wcf.setBorder(Border.ALL, BorderLineStyle.THIN);
            wcf.setAlignment(Alignment.CENTRE);
        }catch (Exception e){
        }
        return wcf;
    }
}
