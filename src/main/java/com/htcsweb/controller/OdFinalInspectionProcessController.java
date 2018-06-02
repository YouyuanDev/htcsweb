package com.htcsweb.controller;



import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.htcsweb.dao.InspectionTimeRecordDao;
import com.htcsweb.dao.OdFinalInspectionProcessDao;

import com.htcsweb.dao.PipeBasicInfoDao;
import com.htcsweb.entity.InspectionTimeRecord;
import com.htcsweb.entity.PipeBasicInfo;
import com.htcsweb.entity.OdFinalInspectionProcess;
import com.htcsweb.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/OdFinalInOperation")
public class OdFinalInspectionProcessController {
    @Autowired
    private OdFinalInspectionProcessDao odFinalInspectionProcessDao;
    @Autowired
    private PipeBasicInfoDao pipeBasicInfoDao;
    @Autowired
    private InspectionTimeRecordDao inspectionTimeRecordDao;

    //查询
    @RequestMapping(value = "/getOdFinalInByLike")
    @ResponseBody
    public String getOdFinalInByLike(@RequestParam(value = "pipe_no",required = false)String pipe_no, @RequestParam(value = "operator_no",required = false)String operator_no, @RequestParam(value = "begin_time",required = false)String begin_time, @RequestParam(value = "end_time",required = false)String end_time, @RequestParam(value = "mill_no",required = false)String mill_no, HttpServletRequest request){
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
        List<HashMap<String,Object>> list=odFinalInspectionProcessDao.getAllByLike(pipe_no,operator_no,beginTime,endTime,mill_no,start,Integer.parseInt(rows));
        int count=odFinalInspectionProcessDao.getCountAllByLike(pipe_no,operator_no,beginTime,endTime,mill_no);
        Map<String,Object> maps=new HashMap<String,Object>();
        maps.put("total",count);
        maps.put("rows",list);
        String mmp= JSONArray.toJSONString(maps);
        return mmp;
    }

    //添加、修改
    @RequestMapping("/saveOdFinalInProcess")
    @ResponseBody
    public String saveOdFinalInProcess(OdFinalInspectionProcess odFinalInspectionProcess, HttpServletRequest request, HttpServletResponse response){
        JSONObject json=new JSONObject();
        try{
            String odstencilprotime= request.getParameter("odFinalInprotime");
            int resTotal=0;
            if(odstencilprotime!=null&&odstencilprotime!=""){
                SimpleDateFormat simFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date new_odbptime = simFormat.parse(odstencilprotime);
                odFinalInspectionProcess.setOperation_time(new_odbptime);
            }else{
                odFinalInspectionProcess.setOperation_time(new Date());
            }
            String pipeno=odFinalInspectionProcess.getPipe_no();
            if(odFinalInspectionProcess.getId()==0){
                //添加
                resTotal=odFinalInspectionProcessDao.addOdFinalInProcess(odFinalInspectionProcess);
                List<HashMap<String,Object>>list=pipeBasicInfoDao.getPipeInfoByNo(odFinalInspectionProcess.getPipe_no());
                String project_no="";
                String mill_no=odFinalInspectionProcess.getMill_no();
                if(list.size()>0){
                    project_no=(String)list.get(0).get("project_no");
                }
                System.out.println("project_no="+project_no);

                //更新增量 inspectionTimeMap
                if (!odFinalInspectionProcess.getCutback_length().equals("")) {
                    List<InspectionTimeRecord> lt=inspectionTimeRecordDao.getRecordByProjectNoMillNo(project_no,mill_no,"od_cutback_freq");
                    if(lt.size()>0) {
                        InspectionTimeRecord itr=lt.get(0);
                        itr.setInspction_time(odFinalInspectionProcess.getOperation_time());
                        inspectionTimeRecordDao.updateInspectionTimeRecord(itr);
                    }else{
                        InspectionTimeRecord itr=new InspectionTimeRecord();
                        itr.setProject_no(project_no);
                        itr.setMill_no(mill_no);
                        itr.setInspection_item("od_cutback_freq");
                        itr.setInspction_time(odFinalInspectionProcess.getOperation_time());
                        inspectionTimeRecordDao.addInspectionTimeRecord(itr);
                    }
                }
                if (!odFinalInspectionProcess.getEpoxy_cutback_list().equals("")) {
                    List<InspectionTimeRecord> lt=inspectionTimeRecordDao.getRecordByProjectNoMillNo(project_no,mill_no,"od_epoxy_cutback_freq");
                    if(lt.size()>0) {
                        InspectionTimeRecord itr=lt.get(0);
                        itr.setInspction_time(odFinalInspectionProcess.getOperation_time());
                        inspectionTimeRecordDao.updateInspectionTimeRecord(itr);
                    }else{
                        InspectionTimeRecord itr=new InspectionTimeRecord();
                        itr.setProject_no(project_no);
                        itr.setMill_no(mill_no);
                        itr.setInspection_item("od_epoxy_cutback_freq");
                        itr.setInspction_time(odFinalInspectionProcess.getOperation_time());
                        inspectionTimeRecordDao.addInspectionTimeRecord(itr);
                    }
                }
                if (!odFinalInspectionProcess.getMagnetism_list().equals("")) {
                    List<InspectionTimeRecord> lt=inspectionTimeRecordDao.getRecordByProjectNoMillNo(project_no,mill_no,"od_magnetism_freq");
                    if(lt.size()>0) {
                        InspectionTimeRecord itr=lt.get(0);
                        itr.setInspction_time(odFinalInspectionProcess.getOperation_time());
                        inspectionTimeRecordDao.updateInspectionTimeRecord(itr);
                    }else{
                        InspectionTimeRecord itr=new InspectionTimeRecord();
                        itr.setProject_no(project_no);
                        itr.setMill_no(mill_no);
                        itr.setInspection_item("od_magnetism_freq");
                        itr.setInspction_time(odFinalInspectionProcess.getOperation_time());
                        inspectionTimeRecordDao.addInspectionTimeRecord(itr);
                    }
                }
                if (!odFinalInspectionProcess.getCoating_bevel_angle_list().equals("")) {
                    List<InspectionTimeRecord> lt=inspectionTimeRecordDao.getRecordByProjectNoMillNo(project_no,mill_no,"od_coating_bevel_angle_freq");
                    if(lt.size()>0) {
                        InspectionTimeRecord itr=lt.get(0);
                        itr.setInspction_time(odFinalInspectionProcess.getOperation_time());
                        inspectionTimeRecordDao.updateInspectionTimeRecord(itr);
                    }else{
                        InspectionTimeRecord itr=new InspectionTimeRecord();
                        itr.setProject_no(project_no);
                        itr.setMill_no(mill_no);
                        itr.setInspection_item("od_coating_bevel_angle_freq");
                        itr.setInspction_time(odFinalInspectionProcess.getOperation_time());
                        inspectionTimeRecordDao.addInspectionTimeRecord(itr);
                    }
                }

            }else{
                //修改！
                resTotal=odFinalInspectionProcessDao.updateOdFinalInProcess(odFinalInspectionProcess);
            }
            if(resTotal>0){
                //更新管子的状态
                List<PipeBasicInfo> list=pipeBasicInfoDao.getPipeNumber(pipeno);
                if(list.size()>0){
                    PipeBasicInfo p=list.get(0);
                    if(p.getStatus().equals("od5")) {
                        //验证钢管状态为喷标完成管   od5 外防终检工序  0:odrepair1  1:od6  2:odstrip1   3:od5   4: od4 5:onhold
                        if (odFinalInspectionProcess.getResult().equals("1")) {//当合格时才更新钢管状态
                            p.setStatus("od6");
                            p.setLast_accepted_status(p.getStatus());
                        } else if (odFinalInspectionProcess.getResult().equals("0")) {
                            p.setStatus("odrepair1");
                        } else if (odFinalInspectionProcess.getResult().equals("2")) {
                            p.setStatus("odstrip1");
                            p.setLast_accepted_status(p.getStatus());
                        } else if (odFinalInspectionProcess.getResult().equals("4")) {
                            p.setStatus("od4");
                            p.setLast_accepted_status(p.getStatus());
                        } else if (odFinalInspectionProcess.getResult().equals("5")) {
                            p.setStatus("onhold");
                        }
                        int statusRes = pipeBasicInfoDao.updatePipeBasicInfo(p);
                    }
//                    }else if() {
//                        //外防修补检验合格状态
//                        if(odFinalInspectionProcess.getResult().equals("1")) {
//                            p.setStatus("od6");
//                            int statusRes = pipeBasicInfoDao.updatePipeBasicInfo(p);
//                        }else if(odFinalInspectionProcess.getResult().equals("0")){
//                            p.setStatus("odrepair1");
//                            int statusRes = pipeBasicInfoDao.updatePipeBasicInfo(p);
//                        }else if(odFinalInspectionProcess.getResult().equals("2")){
//                            p.setStatus("odstrip1");
//                            int statusRes = pipeBasicInfoDao.updatePipeBasicInfo(p);
//                        }else if(odFinalInspectionProcess.getResult().equals("4")){
//                            p.setStatus("od4");
//                            int statusRes = pipeBasicInfoDao.updatePipeBasicInfo(p);
//                        }
//                    }
                }
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
    @RequestMapping("/delOdFinalInProcess")
    public String delOdFinalInProcess(@RequestParam(value = "hlparam")String hlparam,HttpServletResponse response)throws Exception{
        String[]idArr=hlparam.split(",");
        int resTotal=0;
        resTotal=odFinalInspectionProcessDao.delOdFinalInProcess(idArr);
        JSONObject json=new JSONObject();
        StringBuilder sbmessage = new StringBuilder();
        sbmessage.append("总共");
        sbmessage.append(Integer.toString(resTotal));
        sbmessage.append("项外终检信息删除成功\n");
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
