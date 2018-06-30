package com.htcsweb.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.htcsweb.dao.InspectionProcessRecordHeaderDao;
import com.htcsweb.dao.InspectionProcessRecordItemDao;
import com.htcsweb.dao.PipeBasicInfoDao;
import com.htcsweb.dao.ProcessInfoDao;
import com.htcsweb.entity.*;
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
@RequestMapping("/InspectionProcessOperation")
public class InspectionProcess {


    //岗位检验通用表单operation

    @Autowired
    private PipeBasicInfoDao pipeBasicInfoDao;

    @Autowired
    private InspectionProcessRecordHeaderDao inspectionProcessRecordHeaderDao;

    @Autowired
    private ProcessInfoDao processInfoDao;

    @Autowired
    private InspectionProcessRecordItemDao inspectionProcessRecordItemDao;



    @RequestMapping("/saveProcess")
    @ResponseBody
    public String saveProcess(InspectionProcessRecordHeader inspectionProcessRecordHeader, HttpServletRequest request, HttpServletResponse response){
        JSONObject json=new JSONObject();

        String dynamicJson=request.getParameter("dynamicJson");
        System.out.println("dynamicJson="+dynamicJson);
        JSONObject dynamicMap = JSONObject.parseObject(dynamicJson);

        String msg="";
        if(inspectionProcessRecordHeader.getInspection_process_record_header_code()==null||inspectionProcessRecordHeader.getInspection_process_record_header_code()==""){
            inspectionProcessRecordHeader.setInspection_process_record_header_code("IPRH"+System.currentTimeMillis());
        }
        System.out.println("id="+inspectionProcessRecordHeader.getId());
        for (String key:dynamicMap.keySet()) {
            if(inspectionProcessRecordHeader.getId()==0) {
                //新增动态检测项
                System.out.println("新增动态检测项="+inspectionProcessRecordHeader.getInspection_process_record_header_code());
                InspectionProcessRecordItem recorditem=new InspectionProcessRecordItem();
                recorditem.setId(0);
                recorditem.setInspection_process_record_header_code(inspectionProcessRecordHeader.getInspection_process_record_header_code());
                recorditem.setItem_code(key);
                recorditem.setItem_value((String)dynamicMap.get(key));
                inspectionProcessRecordItemDao.addInspectionProcessRecordItem(recorditem);
            }else{
                //更新原有动态检测项
                InspectionProcessRecordItem recorditem=inspectionProcessRecordItemDao.getInspectionProcessRecordItemByHeaderCodeAndItemCode(inspectionProcessRecordHeader.getInspection_process_record_header_code(),key);
                if(recorditem!=null) {
                    recorditem.setItem_value((String) dynamicMap.get(key));
                    inspectionProcessRecordItemDao.updateInspectionProcessRecordItem(recorditem);
                }else{
                    InspectionProcessRecordItem item=new InspectionProcessRecordItem();
                    item.setId(0);
                    item.setInspection_process_record_header_code(inspectionProcessRecordHeader.getInspection_process_record_header_code());
                    item.setItem_code(key);
                    item.setItem_value((String)dynamicMap.get(key));
                    inspectionProcessRecordItemDao.addInspectionProcessRecordItem(item);
                }

            }
        }

        try{
            int resTotal=0;
            if(inspectionProcessRecordHeader.getOperation_time()==null){
                inspectionProcessRecordHeader.setOperation_time(new Date());
            }



            String pipeno=inspectionProcessRecordHeader.getPipe_no();
            String mill_no=inspectionProcessRecordHeader.getMill_no();

            if(inspectionProcessRecordHeader.getId()==0){
                //添加
                String project_no="";
                List<HashMap<String,Object>> list=pipeBasicInfoDao.getPipeInfoByNo(pipeno);
                if(list.size()>0){
                    String pipestatus=(String)list.get(0).get("status");
                    if(pipestatus.equals("bare1")){
                        InspectionProcessRecordHeader oldrecord=inspectionProcessRecordHeaderDao.getRecentRecordByPipeNo("",pipeno);
                        if(oldrecord!=null&&oldrecord.getResult().equals("10")){
                            //存在一条pending数据，不给予insert处理
                            msg="已存在待定记录,不能新增记录";
                        }else{
                            inspectionProcessRecordHeader.setInspection_process_record_header_code(inspectionProcessRecordHeader.getInspection_process_record_header_code());
                            resTotal=inspectionProcessRecordHeaderDao.addInspectionProcessRecordHeader(inspectionProcessRecordHeader);

                        }
                    }
                    project_no=(String)list.get(0).get("project_no");
                    System.out.println("project_no="+project_no);
                }
                if(resTotal>0){
                    //更新检验时间
                    //更新增量 inspectionTimeMap
//                    if (odblastprocess.getRinse_water_conductivity() != -99) {
////
////                        List<InspectionTimeRecord> lt=inspectionTimeRecordDao.getRecordByProjectNoMillNo(project_no,mill_no,"od_rinse_water_conductivity_freq");
////                        if(lt.size()>0) {
////                            InspectionTimeRecord itr=lt.get(0);
////                            itr.setInspction_time(odblastprocess.getOperation_time());
////                            inspectionTimeRecordDao.updateInspectionTimeRecord(itr);
////                        }else{
////                            InspectionTimeRecord itr=new InspectionTimeRecord();
////                            itr.setProject_no(project_no);
////                            itr.setMill_no(mill_no);
////                            itr.setInspection_item("od_rinse_water_conductivity_freq");
////                            itr.setInspction_time(odblastprocess.getOperation_time());
////                            inspectionTimeRecordDao.addInspectionTimeRecord(itr);
////                        }
////
////                    }

                }

            }else{
                //修改！
                resTotal=inspectionProcessRecordHeaderDao.updateInspectionProcessRecordHeader(inspectionProcessRecordHeader);

            }
            if(resTotal>0){
                //更新管子的状态
                List<PipeBasicInfo> list=pipeBasicInfoDao.getPipeNumber(pipeno);
                if(list.size()>0){
                    PipeBasicInfo p=list.get(0);
                    if(p.getStatus().equals("bare1")) {
                        //验证钢管状态为光管  bare1     外喷砂工序   0:bare1     1:od1   10:bare1 3： onhold
                        if(inspectionProcessRecordHeader.getResult().equals("1")) {//当合格时才更新钢管状态
                            p.setStatus("od1");
                            p.setLast_accepted_status(p.getStatus());
                        }
                        else if(inspectionProcessRecordHeader.getResult().equals("3")) {//当需要修磨或切除时，设置为onhold状态
                            p.setStatus("onhold");
                        }
                        int statusRes = pipeBasicInfoDao.updatePipeBasicInfo(p);
                    }

                }
                json.put("success",true);
                json.put("message","保存成功");
            }else{
                json.put("success",false);
                json.put("message","保存失败，"+msg);
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
    @RequestMapping("/delProcess")
    public String delProcess(@RequestParam(value = "hlparam")String hlparam, HttpServletResponse response)throws Exception{
        String[]idArr=hlparam.split(",");
        int resTotal=0;
        resTotal=inspectionProcessRecordHeaderDao.delInspectionProcessRecordHeader(idArr);
        JSONObject json=new JSONObject();
        StringBuilder sbmessage = new StringBuilder();
        sbmessage.append("总共");
        sbmessage.append(Integer.toString(resTotal));
        sbmessage.append("项工序检验信息删除成功\n");
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



    ///搜索岗位工序检验信息

    @RequestMapping(value = "/getProcessByLike")
    @ResponseBody
    public String getProcessByLike(@RequestParam(value = "process_code",required = false)String process_code,@RequestParam(value = "pipe_no",required = false)String pipe_no, @RequestParam(value = "operator_no",required = false)String operator_no, @RequestParam(value = "begin_time",required = false)String begin_time, @RequestParam(value = "end_time",required = false)String end_time, @RequestParam(value = "mill_no",required = false)String mill_no, HttpServletRequest request){
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
        List<HashMap<String,Object>>list=inspectionProcessRecordHeaderDao.getAllByLike(process_code,pipe_no,operator_no,beginTime,endTime,mill_no,start,Integer.parseInt(rows));
        int count=inspectionProcessRecordHeaderDao.getCountAllByLike(process_code,pipe_no,operator_no,beginTime,endTime,mill_no);
        Map<String,Object> maps=new HashMap<String,Object>();
        maps.put("total",count);
        maps.put("rows",list);
        String mmp= JSONArray.toJSONString(maps);
        //System.out.print("mmp:"+mmp);
        return mmp;
    }

}
