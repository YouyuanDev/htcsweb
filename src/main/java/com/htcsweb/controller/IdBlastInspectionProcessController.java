package com.htcsweb.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.htcsweb.dao.IdBlastInspectionProcessDao;
import com.htcsweb.dao.InspectionTimeRecordDao;
import com.htcsweb.dao.PipeBasicInfoDao;
import com.htcsweb.entity.IdBlastInspectionProcess;
import com.htcsweb.entity.IdBlastProcess;
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
@RequestMapping("/IdBlastInspectionOperation")
public class IdBlastInspectionProcessController {

    @Autowired
    private PipeBasicInfoDao pipeBasicInfoDao;
    @Autowired
    private IdBlastInspectionProcessDao idBlastInspectionProcessDao;

    @Autowired
    private InspectionTimeRecordDao inspectionTimeRecordDao;

    @RequestMapping(value = "/getIdBlastInspectionByLike")
    @ResponseBody
    public String getIdBlastInspectionByLike(@RequestParam(value = "pipe_no",required = false)String pipe_no, @RequestParam(value = "operator_no",required = false)String operator_no, @RequestParam(value = "begin_time",required = false)String begin_time, @RequestParam(value = "end_time",required = false)String end_time, @RequestParam(value = "mill_no",required = false)String mill_no, HttpServletRequest request){
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
        List<HashMap<String,Object>>list=idBlastInspectionProcessDao.getAllByLike(pipe_no,operator_no,beginTime,endTime,mill_no,start,Integer.parseInt(rows));
        int count=idBlastInspectionProcessDao.getCountAllByLike(pipe_no,operator_no,beginTime,endTime,mill_no);
        Map<String,Object> maps=new HashMap<String,Object>();
        maps.put("total",count);
        maps.put("rows",list);
        String mmp= JSONArray.toJSONString(maps);
        //System.out.print("mmp:"+mmp);
        return mmp;
    }



    //保存外打砂检验数据
    @RequestMapping("/saveIdBlastInspectionProcess")
    @ResponseBody
    public String saveIdBlastInspectionProcess(IdBlastInspectionProcess idBlastInspectionProcess, HttpServletRequest request, HttpServletResponse response){
        JSONObject json=new JSONObject();
        try{

            int resTotal=0;
            //System.out.println("odbptime="+odbptime);

            if(idBlastInspectionProcess.getOperation_time()==null){
                idBlastInspectionProcess.setOperation_time(new Date());
            }
            String pipeno=idBlastInspectionProcess.getPipe_no();
            String msg="";
            if(idBlastInspectionProcess.getId()==0){
                //添加
                List<HashMap<String,Object>>list=pipeBasicInfoDao.getPipeInfoByNo(idBlastInspectionProcess.getPipe_no());
                if(list.size()>0){
                    String pipestatus=(String)list.get(0).get("status");
                    if(pipestatus.equals("id1")){
                        IdBlastInspectionProcess oldrecord=idBlastInspectionProcessDao.getRecentRecordByPipeNo(idBlastInspectionProcess.getPipe_no());
                        if(oldrecord!=null&&oldrecord.getResult().equals("10")){
                            //存在一条pending数据，不给予insert处理
                            msg="已存在待定记录,不能新增记录";
                        }else{
                            resTotal=idBlastInspectionProcessDao.addIdBlastInProcess(idBlastInspectionProcess);
                        }
                    }
                }
                String project_no="";
                String mill_no=idBlastInspectionProcess.getMill_no();
                if(list.size()>0){
                    project_no=(String)list.get(0).get("project_no");
                }
                System.out.println("project_no="+project_no);

                //更新增量 inspectionTimeMap
                if (idBlastInspectionProcess.getRelative_humidity()!=-99) {
                    List<InspectionTimeRecord> lt=inspectionTimeRecordDao.getRecordByProjectNoMillNo(project_no,mill_no,"id_relative_humidity_freq");
                    if(lt.size()>0) {
                        InspectionTimeRecord itr=lt.get(0);
                        itr.setInspction_time(idBlastInspectionProcess.getOperation_time());
                        inspectionTimeRecordDao.updateInspectionTimeRecord(itr);
                    }else{
                        InspectionTimeRecord itr=new InspectionTimeRecord();
                        itr.setProject_no(project_no);
                        itr.setMill_no(mill_no);
                        itr.setInspection_item("id_relative_humidity_freq");
                        itr.setInspction_time(idBlastInspectionProcess.getOperation_time());
                        inspectionTimeRecordDao.addInspectionTimeRecord(itr);
                    }
                }
                if (idBlastInspectionProcess.getPipe_temp()!=-99) {
                    List<InspectionTimeRecord> lt=inspectionTimeRecordDao.getRecordByProjectNoMillNo(project_no,mill_no,"id_pipe_temp_freq");
                    if(lt.size()>0) {
                        InspectionTimeRecord itr=lt.get(0);
                        itr.setInspction_time(idBlastInspectionProcess.getOperation_time());
                        inspectionTimeRecordDao.updateInspectionTimeRecord(itr);
                    }else{
                        InspectionTimeRecord itr=new InspectionTimeRecord();
                        itr.setProject_no(project_no);
                        itr.setMill_no(mill_no);
                        itr.setInspection_item("id_pipe_temp_freq");
                        itr.setInspction_time(idBlastInspectionProcess.getOperation_time());
                        inspectionTimeRecordDao.addInspectionTimeRecord(itr);
                    }
                }
                if (idBlastInspectionProcess.getAir_temp()!=-99) {
                    List<InspectionTimeRecord> lt=inspectionTimeRecordDao.getRecordByProjectNoMillNo(project_no,mill_no,"id_room_temp_freq");
                    if(lt.size()>0) {
                        InspectionTimeRecord itr=lt.get(0);
                        itr.setInspction_time(idBlastInspectionProcess.getOperation_time());
                        inspectionTimeRecordDao.updateInspectionTimeRecord(itr);
                    }else{
                        InspectionTimeRecord itr=new InspectionTimeRecord();
                        itr.setProject_no(project_no);
                        itr.setMill_no(mill_no);
                        itr.setInspection_item("id_room_temp_freq");
                        itr.setInspction_time(idBlastInspectionProcess.getOperation_time());
                        inspectionTimeRecordDao.addInspectionTimeRecord(itr);
                    }
                }
                if (!idBlastInspectionProcess.getBlast_finish_sa25().equals("")) {
                    List<InspectionTimeRecord> lt=inspectionTimeRecordDao.getRecordByProjectNoMillNo(project_no,mill_no,"id_blast_finish_sa25_freq");
                    if(lt.size()>0) {
                        InspectionTimeRecord itr=lt.get(0);
                        itr.setInspction_time(idBlastInspectionProcess.getOperation_time());
                        inspectionTimeRecordDao.updateInspectionTimeRecord(itr);
                    }else{
                        InspectionTimeRecord itr=new InspectionTimeRecord();
                        itr.setProject_no(project_no);
                        itr.setMill_no(mill_no);
                        itr.setInspection_item("id_blast_finish_sa25_freq");
                        itr.setInspction_time(idBlastInspectionProcess.getOperation_time());
                        inspectionTimeRecordDao.addInspectionTimeRecord(itr);
                    }
                }
                if (idBlastInspectionProcess.getSurface_dust_rating()!=-99) {
                    List<InspectionTimeRecord> lt=inspectionTimeRecordDao.getRecordByProjectNoMillNo(project_no,mill_no,"id_surface_dust_rating_freq");
                    if(lt.size()>0) {
                        InspectionTimeRecord itr=lt.get(0);
                        itr.setInspction_time(idBlastInspectionProcess.getOperation_time());
                        inspectionTimeRecordDao.updateInspectionTimeRecord(itr);
                    }else{
                        InspectionTimeRecord itr=new InspectionTimeRecord();
                        itr.setProject_no(project_no);
                        itr.setMill_no(mill_no);
                        itr.setInspection_item("id_surface_dust_rating_freq");
                        itr.setInspction_time(idBlastInspectionProcess.getOperation_time());
                        inspectionTimeRecordDao.addInspectionTimeRecord(itr);
                    }
                }
                if (idBlastInspectionProcess.getSalt_contamination_after_blasting()!=-99) {
                    List<InspectionTimeRecord> lt=inspectionTimeRecordDao.getRecordByProjectNoMillNo(project_no,mill_no,"id_salt_contamination_after_blasting_freq");
                    if(lt.size()>0) {
                        InspectionTimeRecord itr=lt.get(0);
                        itr.setInspction_time(idBlastInspectionProcess.getOperation_time());
                        inspectionTimeRecordDao.updateInspectionTimeRecord(itr);
                    }else{
                        InspectionTimeRecord itr=new InspectionTimeRecord();
                        itr.setProject_no(project_no);
                        itr.setMill_no(mill_no);
                        itr.setInspection_item("id_salt_contamination_after_blasting_freq");
                        itr.setInspction_time(idBlastInspectionProcess.getOperation_time());
                        inspectionTimeRecordDao.addInspectionTimeRecord(itr);
                    }
                }
                if (idBlastInspectionProcess.getProfile()!=-99) {
                    List<InspectionTimeRecord> lt=inspectionTimeRecordDao.getRecordByProjectNoMillNo(project_no,mill_no,"id_profile_freq");
                    if(lt.size()>0) {
                        InspectionTimeRecord itr=lt.get(0);
                        itr.setInspction_time(idBlastInspectionProcess.getOperation_time());
                        inspectionTimeRecordDao.updateInspectionTimeRecord(itr);
                    }else{
                        InspectionTimeRecord itr=new InspectionTimeRecord();
                        itr.setProject_no(project_no);
                        itr.setMill_no(mill_no);
                        itr.setInspection_item("id_profile_freq");
                        itr.setInspction_time(idBlastInspectionProcess.getOperation_time());
                        inspectionTimeRecordDao.addInspectionTimeRecord(itr);
                    }
                }


            }else{
                //修改！
                System.out.println("ID"+idBlastInspectionProcess.getId());
                resTotal=idBlastInspectionProcessDao.updateIdBlastInProcess(idBlastInspectionProcess);
            }
            if(resTotal>0){
                //更新管子的状态
                List<PipeBasicInfo> list=pipeBasicInfoDao.getPipeNumber(pipeno);
                if(list.size()>0){
                    PipeBasicInfo p=list.get(0);
                    if(p.getStatus().equals("id1")) {
                        //验证钢管状态为内打砂完成  id1   内喷砂检验工序 0:bare2     1:id2   10:id1  3： onhold
                        if(idBlastInspectionProcess.getResult().equals("1")) {//当打砂检验合格时才更新钢管状态
                            p.setStatus("id2");
                            p.setLast_accepted_status(p.getStatus());
                        }else if(idBlastInspectionProcess.getResult().equals("0")) {
                            p.setStatus("bare2");
                            p.setLast_accepted_status(p.getStatus());
                        }
                        else if(idBlastInspectionProcess.getResult().equals("3")) {//当需要修磨或切除时，设置为onhold状态
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


    @RequestMapping("/delIdBlastInspectionProcess")
    public String delIdBlastInspectionProcess(@RequestParam(value = "hlparam")String hlparam,HttpServletResponse response)throws Exception{
        String[]idArr=hlparam.split(",");
        int resTotal=0;
        resTotal=idBlastInspectionProcessDao.delIdBlastInspectionProcess(idArr);
        JSONObject json=new JSONObject();
        StringBuilder sbmessage = new StringBuilder();
        sbmessage.append("总共");
        sbmessage.append(Integer.toString(resTotal));
        sbmessage.append("项内喷砂检验信息删除成功\n");
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


    //得到可以钢管最新的待定的内防喷砂检验记录  最后一条记录且result为待定 10
    @RequestMapping(value = "/getPendingRecordByPipeNo")
    @ResponseBody
    public String getPendingRecordByPipeNo(@RequestParam(value = "pipe_no",required = false)String pipe_no, HttpServletRequest request) {

        IdBlastInspectionProcess record=idBlastInspectionProcessDao.getRecentRecordByPipeNo(pipe_no);
        Map<String,Object> maps=new HashMap<String,Object>();
        if(record!=null&&record.getResult().equals("10")){
            //是待定状态
            maps.put("success",true);
            maps.put("record",record);
        }else{
            maps.put("success",false);
        }

        String mmp= JSONArray.toJSONString(maps);
        return mmp;

    }

}
