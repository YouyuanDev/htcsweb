package com.htcsweb.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.htcsweb.dao.IdFinalInspectionProcessDao;
import com.htcsweb.dao.InspectionTimeRecordDao;
import com.htcsweb.dao.PipeBasicInfoDao;
import com.htcsweb.entity.IdFinalInspectionProcess;
import com.htcsweb.entity.InspectionTimeRecord;
import com.htcsweb.entity.PipeBasicInfo;
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
@RequestMapping("/IdFinalInOperation")
public class IdFinalInspectionProcessController {
    @Autowired
    private IdFinalInspectionProcessDao idFinalInspectionProcessDao;
    @Autowired
    private PipeBasicInfoDao pipeBasicInfoDao;
    @Autowired
    private InspectionTimeRecordDao inspectionTimeRecordDao;

    //查询
    @RequestMapping(value = "/getIdFinalInByLike")
    @ResponseBody
    public String getIdFinalInByLike(@RequestParam(value = "pipe_no",required = false)String pipe_no, @RequestParam(value = "operator_no",required = false)String operator_no, @RequestParam(value = "begin_time",required = false)String begin_time, @RequestParam(value = "end_time",required = false)String end_time,  @RequestParam(value = "mill_no",required = false)String mill_no, HttpServletRequest request){
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
        List<HashMap<String,Object>> list=idFinalInspectionProcessDao.getAllByLike(pipe_no,operator_no,beginTime,endTime,mill_no,start,Integer.parseInt(rows));
        int count=idFinalInspectionProcessDao.getCountAllByLike(pipe_no,operator_no,beginTime,endTime,mill_no);
        Map<String,Object> maps=new HashMap<String,Object>();
        maps.put("total",count);
        maps.put("rows",list);
        String mmp= JSONArray.toJSONString(maps);
        return mmp;
    }

    //添加、修改
    @RequestMapping("/saveIdFinalInProcess")
    @ResponseBody
    public String saveIdFinalInProcess(IdFinalInspectionProcess idFinalInspectionProcess, HttpServletRequest request, HttpServletResponse response){
        JSONObject json=new JSONObject();
        try{
            String odstencilprotime= request.getParameter("idFinalInprotime");
            int resTotal=0;
            if(odstencilprotime!=null&&odstencilprotime!=""){
                SimpleDateFormat simFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date new_odbptime = simFormat.parse(odstencilprotime);
                idFinalInspectionProcess.setOperation_time(new_odbptime);
            }else{
                idFinalInspectionProcess.setOperation_time(new Date());
            }
            String pipeno=idFinalInspectionProcess.getPipe_no();
            if(idFinalInspectionProcess.getId()==0){
                //添加
                resTotal=idFinalInspectionProcessDao.addIdFinalInProcess(idFinalInspectionProcess);
                List<HashMap<String,Object>>list=pipeBasicInfoDao.getPipeInfoByNo(idFinalInspectionProcess.getPipe_no());
                String project_no="";
                String mill_no=idFinalInspectionProcess.getMill_no();
                if(list.size()>0){
                    project_no=(String)list.get(0).get("project_no");
                }
                System.out.println("project_no="+project_no);

                //更新增量 inspectionTimeMap
                if (!idFinalInspectionProcess.getDry_film_thickness_list().equals("")) {

                    List<InspectionTimeRecord> lt=inspectionTimeRecordDao.getRecordByProjectNoMillNo(project_no,mill_no,"id_dry_film_thickness_freq");
                    if(lt.size()>0) {
                        InspectionTimeRecord itr=lt.get(0);
                        itr.setInspction_time(idFinalInspectionProcess.getOperation_time());
                        inspectionTimeRecordDao.updateInspectionTimeRecord(itr);
                    }else{
                        InspectionTimeRecord itr=new InspectionTimeRecord();
                        itr.setProject_no(project_no);
                        itr.setMill_no(mill_no);
                        itr.setInspection_item("id_dry_film_thickness_freq");
                        itr.setInspction_time(idFinalInspectionProcess.getOperation_time());
                        inspectionTimeRecordDao.addInspectionTimeRecord(itr);
                    }

                }
                if (!idFinalInspectionProcess.getRoughness_list().equals("")) {

                    List<InspectionTimeRecord> lt=inspectionTimeRecordDao.getRecordByProjectNoMillNo(project_no,mill_no,"id_roughness_freq");
                    if(lt.size()>0) {
                        InspectionTimeRecord itr=lt.get(0);
                        itr.setInspction_time(idFinalInspectionProcess.getOperation_time());
                        inspectionTimeRecordDao.updateInspectionTimeRecord(itr);
                    }else{
                        InspectionTimeRecord itr=new InspectionTimeRecord();
                        itr.setProject_no(project_no);
                        itr.setMill_no(mill_no);
                        itr.setInspection_item("id_roughness_freq");
                        itr.setInspction_time(idFinalInspectionProcess.getOperation_time());
                        inspectionTimeRecordDao.addInspectionTimeRecord(itr);
                    }

                }
                if (!idFinalInspectionProcess.getCutback_length().equals("")) {

                    List<InspectionTimeRecord> lt=inspectionTimeRecordDao.getRecordByProjectNoMillNo(project_no,mill_no,"id_cutback_freq");
                    if(lt.size()>0) {
                        InspectionTimeRecord itr=lt.get(0);
                        itr.setInspction_time(idFinalInspectionProcess.getOperation_time());
                        inspectionTimeRecordDao.updateInspectionTimeRecord(itr);
                    }else{
                        InspectionTimeRecord itr=new InspectionTimeRecord();
                        itr.setProject_no(project_no);
                        itr.setMill_no(mill_no);
                        itr.setInspection_item("id_cutback_freq");
                        itr.setInspction_time(idFinalInspectionProcess.getOperation_time());
                        inspectionTimeRecordDao.addInspectionTimeRecord(itr);
                    }

                }
                if (!idFinalInspectionProcess.getMagnetism_list().equals("")) {

                    List<InspectionTimeRecord> lt=inspectionTimeRecordDao.getRecordByProjectNoMillNo(project_no,mill_no,"id_residual_magnetism_freq");
                    if(lt.size()>0) {
                        InspectionTimeRecord itr=lt.get(0);
                        itr.setInspction_time(idFinalInspectionProcess.getOperation_time());
                        inspectionTimeRecordDao.updateInspectionTimeRecord(itr);
                    }else{
                        InspectionTimeRecord itr=new InspectionTimeRecord();
                        itr.setProject_no(project_no);
                        itr.setMill_no(mill_no);
                        itr.setInspection_item("id_residual_magnetism_freq");
                        itr.setInspction_time(idFinalInspectionProcess.getOperation_time());
                        inspectionTimeRecordDao.addInspectionTimeRecord(itr);
                    }

                }
                if (idFinalInspectionProcess.getHolidays()!=-99) {
                    List<InspectionTimeRecord> lt=inspectionTimeRecordDao.getRecordByProjectNoMillNo(project_no,mill_no,"id_holiday_freq");
                    if(lt.size()>0) {
                        InspectionTimeRecord itr=lt.get(0);
                        itr.setInspction_time(idFinalInspectionProcess.getOperation_time());
                        inspectionTimeRecordDao.updateInspectionTimeRecord(itr);
                    }else{
                        InspectionTimeRecord itr=new InspectionTimeRecord();
                        itr.setProject_no(project_no);
                        itr.setMill_no(mill_no);
                        itr.setInspection_item("id_holiday_freq");
                        itr.setInspction_time(idFinalInspectionProcess.getOperation_time());
                        inspectionTimeRecordDao.addInspectionTimeRecord(itr);
                    }

                }

            }else{
                //修改！
                resTotal=idFinalInspectionProcessDao.updateIdFinalInProcess(idFinalInspectionProcess);
            }
            if(resTotal>0){
                //更新管子的状态
                List<PipeBasicInfo> list=pipeBasicInfoDao.getPipeNumber(pipeno);
                if(list.size()>0){
                    PipeBasicInfo p=list.get(0);
                    //验证钢管状态是否为内喷标完成或者外防入库  id5  内防终检工序     0:idrepair1   1:id6   2:odstrip1   3:idstrip1  4: id4   10:id5   6:  onhold
                    if(p.getStatus().equals("id5")){
                        //如果为合格
                        if(idFinalInspectionProcess.getResult().equals("1")) {
                            p.setStatus("id6");
                            p.setLast_accepted_status(p.getStatus());
                        }else if(idFinalInspectionProcess.getResult().equals("0")){
                            p.setStatus("idrepair1");
                        }else if(idFinalInspectionProcess.getResult().equals("2")){
                            p.setStatus("odstrip1");
                            p.setLast_accepted_status(p.getStatus());
                        }else if(idFinalInspectionProcess.getResult().equals("3")){
                            p.setStatus("idstrip1");
                            p.setLast_accepted_status(p.getStatus());
                        }else if(idFinalInspectionProcess.getResult().equals("4")){
                            p.setStatus("id4");
                            p.setLast_accepted_status(p.getStatus());
                        }else if(idFinalInspectionProcess.getResult().equals("6")){
                            p.setStatus("onhold");
                        }
                        int statusRes=pipeBasicInfoDao.updatePipeBasicInfo(p);
                    }
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
    @RequestMapping("/delIdFinalInProcess")
    public String delIdFinalInProcess(@RequestParam(value = "hlparam")String hlparam,HttpServletResponse response)throws Exception{
        String[]idArr=hlparam.split(",");
        int resTotal=0;
        resTotal=idFinalInspectionProcessDao.delIdFinalInProcess(idArr);
        JSONObject json=new JSONObject();
        StringBuilder sbmessage = new StringBuilder();
        sbmessage.append("总共");
        sbmessage.append(Integer.toString(resTotal));
        sbmessage.append("项内防终检信息删除成功\n");
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
