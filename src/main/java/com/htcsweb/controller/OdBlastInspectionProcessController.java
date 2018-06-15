package com.htcsweb.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.htcsweb.dao.*;
import com.htcsweb.entity.*;

import com.htcsweb.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/OdBlastInspectionOperation")
public class OdBlastInspectionProcessController {

    @Autowired
    private PipeBasicInfoDao pipeBasicInfoDao;
    @Autowired
    private OdBlastInspectionProcessDao odBlastInspectionProcessDao;

    @Autowired
    private InspectionTimeRecordDao inspectionTimeRecordDao;

    @Autowired
    private OdCoatingProcessDao odCoatingProcessDao;

    @Autowired
    private OdCoating3LpeProcessDao odCoating3LpeProcessDao;

    @RequestMapping(value = "/getOdBlastInspectionByLike")
    @ResponseBody
    public String getOdBlastInspectionByLike(@RequestParam(value = "pipe_no",required = false)String pipe_no, @RequestParam(value = "operator_no",required = false)String operator_no, @RequestParam(value = "begin_time",required = false)String begin_time, @RequestParam(value = "end_time",required = false)String end_time, @RequestParam(value = "mill_no",required = false)String mill_no, HttpServletRequest request){
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
        List<HashMap<String,Object>>list=odBlastInspectionProcessDao.getAllByLike(pipe_no,operator_no,beginTime,endTime,mill_no,start,Integer.parseInt(rows));
        int count=odBlastInspectionProcessDao.getCountAllByLike(pipe_no,operator_no,beginTime,endTime,mill_no);
        Map<String,Object> maps=new HashMap<String,Object>();
        maps.put("total",count);
        maps.put("rows",list);
        String mmp= JSONArray.toJSONString(maps);
        //System.out.print("mmp:"+mmp);
        return mmp;
    }



    //保存外打砂检验数据
    @RequestMapping("/saveOdBlastInspectionProcess")
    @ResponseBody
    public String saveOdBlastInspectionProcess(OdBlastInspectionProcess odblastinspectionprocess, HttpServletRequest request, HttpServletResponse response){
        JSONObject json=new JSONObject();
        try{
            String odbptime= request.getParameter("odbptime");
            int resTotal=0;
            //System.out.println("odbptime="+odbptime);

            if(odbptime!=null&&odbptime!=""){
                SimpleDateFormat simFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date new_odbptime = simFormat.parse(odbptime);
                odblastinspectionprocess.setOperation_time(new_odbptime);
            }else{
                odblastinspectionprocess.setOperation_time(new Date());
            }
            String pipeno=odblastinspectionprocess.getPipe_no();
            String msg="";
            if(odblastinspectionprocess.getId()==0){
                //添加
                List<HashMap<String,Object>>list=pipeBasicInfoDao.getPipeInfoByNo(odblastinspectionprocess.getPipe_no());
                if(list.size()>0){
                    String pipestatus=(String)list.get(0).get("status");
                    if(pipestatus.equals("od1")){
                        OdBlastInspectionProcess oldrecord=odBlastInspectionProcessDao.getRecentRecordByPipeNo(odblastinspectionprocess.getPipe_no());
                        if(oldrecord!=null&&oldrecord.getResult().equals("10")){
                            //存在一条pending数据，不给予insert处理
                            msg="已存在待定记录,不能新增记录";
                        }else{
                            resTotal=odBlastInspectionProcessDao.addOdBlastInProcess(odblastinspectionprocess);
                        }
                    }
                }


                String project_no="";
                String mill_no=odblastinspectionprocess.getMill_no();
                if(list.size()>0){
                    project_no=(String)list.get(0).get("project_no");
                }
                System.out.println("project_no="+project_no);

                //更新增量 inspectionTimeMap
                if (odblastinspectionprocess.getRelative_humidity() != -99) {
                    List<InspectionTimeRecord> lt=inspectionTimeRecordDao.getRecordByProjectNoMillNo(project_no,mill_no,"od_relative_humidity_freq");
                    if(lt.size()>0) {
                        InspectionTimeRecord itr=lt.get(0);
                        itr.setInspction_time(odblastinspectionprocess.getOperation_time());
                        inspectionTimeRecordDao.updateInspectionTimeRecord(itr);
                    }else{
                        InspectionTimeRecord itr=new InspectionTimeRecord();
                        itr.setProject_no(project_no);
                        itr.setMill_no(mill_no);
                        itr.setInspection_item("od_relative_humidity_freq");
                        itr.setInspction_time(odblastinspectionprocess.getOperation_time());
                        inspectionTimeRecordDao.addInspectionTimeRecord(itr);
                    }

                }
                if (!odblastinspectionprocess.getBlast_finish_sa25().equals("")) {
                    List<InspectionTimeRecord> lt=inspectionTimeRecordDao.getRecordByProjectNoMillNo(project_no,mill_no,"od_blast_finish_sa25_freq");
                    if(lt.size()>0) {
                        InspectionTimeRecord itr=lt.get(0);
                        itr.setInspction_time(odblastinspectionprocess.getOperation_time());
                        inspectionTimeRecordDao.updateInspectionTimeRecord(itr);
                    }else{
                        InspectionTimeRecord itr=new InspectionTimeRecord();
                        itr.setProject_no(project_no);
                        itr.setMill_no(mill_no);
                        itr.setInspection_item("od_blast_finish_sa25_freq");
                        itr.setInspction_time(odblastinspectionprocess.getOperation_time());
                        inspectionTimeRecordDao.addInspectionTimeRecord(itr);
                    }

                }
                if (odblastinspectionprocess.getSurface_dust_rating() != -99) {
                    List<InspectionTimeRecord> lt=inspectionTimeRecordDao.getRecordByProjectNoMillNo(project_no,mill_no,"od_surface_dust_rating_freq");
                    if(lt.size()>0) {
                        InspectionTimeRecord itr=lt.get(0);
                        itr.setInspction_time(odblastinspectionprocess.getOperation_time());
                        inspectionTimeRecordDao.updateInspectionTimeRecord(itr);
                    }else{
                        InspectionTimeRecord itr=new InspectionTimeRecord();
                        itr.setProject_no(project_no);
                        itr.setMill_no(mill_no);
                        itr.setInspection_item("od_surface_dust_rating_freq");
                        itr.setInspction_time(odblastinspectionprocess.getOperation_time());
                        inspectionTimeRecordDao.addInspectionTimeRecord(itr);
                    }

                }
                if (odblastinspectionprocess.getProfile() != -99) {
                    List<InspectionTimeRecord> lt=inspectionTimeRecordDao.getRecordByProjectNoMillNo(project_no,mill_no,"od_profile_freq");
                    if(lt.size()>0) {
                        InspectionTimeRecord itr=lt.get(0);
                        itr.setInspction_time(odblastinspectionprocess.getOperation_time());
                        inspectionTimeRecordDao.updateInspectionTimeRecord(itr);
                    }else{
                        InspectionTimeRecord itr=new InspectionTimeRecord();
                        itr.setProject_no(project_no);
                        itr.setMill_no(mill_no);
                        itr.setInspection_item("od_profile_freq");
                        itr.setInspction_time(odblastinspectionprocess.getOperation_time());
                        inspectionTimeRecordDao.addInspectionTimeRecord(itr);
                    }

                }
                if (odblastinspectionprocess.getSalt_contamination_after_blasting() != -99) {
                    List<InspectionTimeRecord> lt=inspectionTimeRecordDao.getRecordByProjectNoMillNo(project_no,mill_no,"od_salt_contamination_after_blasting_freq");
                    if(lt.size()>0) {
                        InspectionTimeRecord itr=lt.get(0);
                        itr.setInspction_time(odblastinspectionprocess.getOperation_time());
                        inspectionTimeRecordDao.updateInspectionTimeRecord(itr);
                    }else{
                        InspectionTimeRecord itr=new InspectionTimeRecord();
                        itr.setProject_no(project_no);
                        itr.setMill_no(mill_no);
                        itr.setInspection_item("od_salt_contamination_after_blasting_freq");
                        itr.setInspction_time(odblastinspectionprocess.getOperation_time());
                        inspectionTimeRecordDao.addInspectionTimeRecord(itr);
                    }

                }
                if (odblastinspectionprocess.getPipe_temp() != -99) {
                    List<InspectionTimeRecord> lt=inspectionTimeRecordDao.getRecordByProjectNoMillNo(project_no,mill_no,"od_pipe_temp_after_blast_freq");
                    if(lt.size()>0) {
                        InspectionTimeRecord itr=lt.get(0);
                        itr.setInspction_time(odblastinspectionprocess.getOperation_time());
                        inspectionTimeRecordDao.updateInspectionTimeRecord(itr);
                    }else{
                        InspectionTimeRecord itr=new InspectionTimeRecord();
                        itr.setProject_no(project_no);
                        itr.setMill_no(mill_no);
                        itr.setInspection_item("od_pipe_temp_after_blast_freq");
                        itr.setInspction_time(odblastinspectionprocess.getOperation_time());
                        inspectionTimeRecordDao.addInspectionTimeRecord(itr);
                    }

                }

            }else{
                //修改！
                resTotal=odBlastInspectionProcessDao.updateOdBlastInProcess(odblastinspectionprocess);
            }
            if(resTotal>0){
                //更新管子的状态
                List<PipeBasicInfo> list=pipeBasicInfoDao.getPipeNumber(pipeno);
                if(list.size()>0){
                    PipeBasicInfo p=list.get(0);
                    int statusRes=0;
                    if(p.getStatus().equals("od1")) {
                        //验证钢管状态为光管  od1      外喷砂检验工序   0:bare1     1:od2   10:od1  3： onhold
                        if(odblastinspectionprocess.getResult().equals("1")) {//当打砂检验合格时才更新钢管状态
                            p.setStatus("od2");
                            p.setLast_accepted_status(p.getStatus());
                        }
                        else if(odblastinspectionprocess.getResult().equals("0")){//打砂检验不合格，改变状态为bare1，重新打砂处理
                            p.setStatus("bare1");
                            p.setLast_accepted_status(p.getStatus());
                        }
                        else if(odblastinspectionprocess.getResult().equals("3")) {//当需要修磨或切除时，设置为onhold状态
                            p.setStatus("onhold");
                        }
                        statusRes = pipeBasicInfoDao.updatePipeBasicInfo(p);
                    }
                    //当数据合格后 且状态跳转后
                    if(statusRes>0&&odblastinspectionprocess.getResult().equals("1")) {
                        //APP使用，外喷砂检验合格后的管子，自动添加odcoating记录
                        HttpSession session = request.getSession();
                        //把用户数据保存在session域对象中
                        String mill_no = (String) session.getAttribute("millno");
                        System.out.println("112123");
                        if (mill_no != null) {
                            //此处为APP应用
                            List<HashMap<String, Object>> lt = pipeBasicInfoDao.getPipeInfoByNo(pipeno);
                            if (lt.size() > 0) {
                                String external_coating = (String) lt.get(0).get("external_coating");
                                if (external_coating != null && external_coating.equals("2FBE")) {
                                    OdCoatingProcess odCoatingProcess=new OdCoatingProcess();
                                    odCoatingProcess.setId(0);
                                    odCoatingProcess.setPipe_no(pipeno);
                                    odCoatingProcess.setMill_no(mill_no);
                                    odCoatingProcess.setOperation_time(new Date());
                                    odCoatingProcess.setOperator_no(odblastinspectionprocess.getOperator_no());
                                    odCoatingProcess.setResult("10");
                                    odCoatingProcess.setRemark("INIT");
                                    odCoatingProcess.setBase_coat_gun_count(-99);
                                    odCoatingProcess.setAir_pressure(-99);
                                    odCoatingProcess.setApplication_temp(-99);
                                    odCoatingProcess.setApplication_voltage(-99);
                                    odCoatingProcess.setCoating_line_speed(-99);
                                    odCoatingProcess.setCoating_voltage(-99);
                                    odCoatingProcess.setGun_distance(-99);
                                    odCoatingProcess.setSpray_speed(-99);
                                    odCoatingProcess.setTo_first_touch_duration(-99);
                                    odCoatingProcess.setTo_quench_duration(-99);
                                    odCoatingProcess.setTop_coat_gun_count(-99);
                                    odCoatingProcess.setBase_coat_lot_no("");
                                    odCoatingProcess.setBase_coat_used("");
                                    odCoatingProcess.setTop_coat_used("");
                                    odCoatingProcess.setTop_coat_lot_no("");
                                    odCoatingProcess.setUpload_files("");
                                    odCoatingProcessDao.addOdCoatingProcess(odCoatingProcess);
                                } else if (external_coating != null && external_coating.equals("3LPE")) {
                                    OdCoating3LpeProcess odCoating3LpeProcess = new OdCoating3LpeProcess();
                                    odCoating3LpeProcess.setId(0);
                                    odCoating3LpeProcess.setPipe_no(pipeno);
                                    odCoating3LpeProcess.setMill_no(mill_no);
                                    odCoating3LpeProcess.setOperation_time(new Date());
                                    odCoating3LpeProcess.setOperator_no(odblastinspectionprocess.getOperator_no());
                                    odCoating3LpeProcess.setResult("10");
                                    odCoating3LpeProcess.setRemark("INIT");
                                    odCoating3LpeProcess.setBase_coat_gun_count(-99);
                                    odCoating3LpeProcess.setAir_pressure(-99);
                                    odCoating3LpeProcess.setApplication_temp(-99);
                                    odCoating3LpeProcess.setApplication_voltage(-99);
                                    odCoating3LpeProcess.setCoating_line_speed(-99);
                                    odCoating3LpeProcess.setCoating_voltage(-99);
                                    odCoating3LpeProcess.setGun_distance(-99);
                                    odCoating3LpeProcess.setSpray_speed(-99);
                                    odCoating3LpeProcess.setTo_first_touch_duration(-99);
                                    odCoating3LpeProcess.setTo_quench_duration(-99);
                                    odCoating3LpeProcess.setBase_coat_used("");
                                    odCoating3LpeProcess.setBase_coat_lot_no("");
                                    odCoating3LpeProcess.setMiddle_coat_lot_no("");
                                    odCoating3LpeProcess.setMiddle_coat_used("");
                                    odCoating3LpeProcess.setTop_coat_lot_no("");
                                    odCoating3LpeProcess.setTop_coat_used("");
                                    odCoating3LpeProcess.setUpload_files("");
                                    odCoating3LpeProcessDao.addOdCoating3LpeProcess(odCoating3LpeProcess);
                                }
                            }
                        }
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
            json.put("message","保存失败");
        }finally {
            try {
                ResponseUtil.write(response, json);
            }catch  (Exception e) {
                e.printStackTrace();
            }


        }
        return null;
    }


    @RequestMapping("/delOdBlastInspectionProcess")
    public String delOdBlastInspectionProcess(@RequestParam(value = "hlparam")String hlparam,HttpServletResponse response)throws Exception{
        String[]idArr=hlparam.split(",");
        int resTotal=0;
        resTotal=odBlastInspectionProcessDao.delOdBlastInspectionProcess(idArr);
        JSONObject json=new JSONObject();
        StringBuilder sbmessage = new StringBuilder();
        sbmessage.append("总共");
        sbmessage.append(Integer.toString(resTotal));
        sbmessage.append("项外喷砂检验信息删除成功\n");
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


    //得到可以钢管最新的待定的打砂检验记录  最后一条记录且result为待定 10
    @RequestMapping(value = "/getPendingRecordByPipeNo")
    @ResponseBody
    public String getPendingRecordByPipeNo(@RequestParam(value = "pipe_no",required = false)String pipe_no, HttpServletRequest request) {

        OdBlastInspectionProcess odbi=odBlastInspectionProcessDao.getRecentRecordByPipeNo(pipe_no);
        Map<String,Object> maps=new HashMap<String,Object>();
        if(odbi!=null&&odbi.getResult().equals("10")){
            //是待定状态
            maps.put("success",true);
            maps.put("record",odbi);
        }else{
            maps.put("success",false);
        }

        String mmp= JSONArray.toJSONString(maps);
        return mmp;

    }

}
