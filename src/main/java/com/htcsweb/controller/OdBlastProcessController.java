package com.htcsweb.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.htcsweb.dao.InspectionTimeRecordDao;
import com.htcsweb.dao.OdBlastProcessDao;
import com.htcsweb.dao.PipeBasicInfoDao;
import com.htcsweb.dao.RoleDao;
import com.htcsweb.entity.InspectionTimeRecord;
import com.htcsweb.entity.OdBlastProcess;
import com.htcsweb.entity.PipeBasicInfo;

import com.htcsweb.util.APICloudPushService;
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
@RequestMapping("/OdOperation")
public class OdBlastProcessController {
   
    @Autowired
    private OdBlastProcessDao odblastprocessDao;

    @Autowired
    private PipeBasicInfoDao pipeBasicInfoDao;

    @Autowired
    private InspectionTimeRecordDao inspectionTimeRecordDao;


    @RequestMapping("/saveOdBlastProcess")
    @ResponseBody
    public String saveOdBlastProcess(OdBlastProcess odblastprocess, HttpServletRequest request, HttpServletResponse response){
        JSONObject json=new JSONObject();
        try{
            String odbptime= request.getParameter("odbptime");
            int resTotal=0;
            if(odbptime!=null&&odbptime!=""){
                SimpleDateFormat simFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date new_odbptime = simFormat.parse(odbptime);
                odblastprocess.setOperation_time(new_odbptime);
            }else{
                odblastprocess.setOperation_time(new Date());
            }
            String pipeno=odblastprocess.getPipe_no();
            if(odblastprocess.getId()==0){
                //添加
                List<HashMap<String,Object>>list=pipeBasicInfoDao.getPipeInfoByNo(odblastprocess.getPipe_no());
                if(list.size()>0){
                    String pipestatus=(String)list.get(0).get("status");
                    if(pipestatus.equals("bare1")){
                        OdBlastProcess oldrecord=odblastprocessDao.getRecentRecordByPipeNo(odblastprocess.getPipe_no());
                        if(oldrecord!=null&&oldrecord.getResult().equals("10")){
                            //存在一条pending数据，不给予insert处理
                        }else{
                            resTotal=odblastprocessDao.addOdBlastProcess(odblastprocess);
                        }
                    }
                }
                if(resTotal>0){
                    //更新检验时间

                    String project_no="";
                    String mill_no=odblastprocess.getMill_no();
                    if(list.size()>0){
                        project_no=(String)list.get(0).get("project_no");
                    }
                    System.out.println("project_no="+project_no);

                    //更新增量 inspectionTimeMap
                    if (odblastprocess.getRinse_water_conductivity() != -99) {

                        List<InspectionTimeRecord> lt=inspectionTimeRecordDao.getRecordByProjectNoMillNo(project_no,mill_no,"od_rinse_water_conductivity_freq");
                        if(lt.size()>0) {
                            InspectionTimeRecord itr=lt.get(0);
                            itr.setInspction_time(odblastprocess.getOperation_time());
                            inspectionTimeRecordDao.updateInspectionTimeRecord(itr);
                        }else{
                            InspectionTimeRecord itr=new InspectionTimeRecord();
                            itr.setProject_no(project_no);
                            itr.setMill_no(mill_no);
                            itr.setInspection_item("od_rinse_water_conductivity_freq");
                            itr.setInspction_time(odblastprocess.getOperation_time());
                            inspectionTimeRecordDao.addInspectionTimeRecord(itr);
                        }

                    }
                    if (odblastprocess.getAbrasive_conductivity() != -99) {
                        List<InspectionTimeRecord> lt=inspectionTimeRecordDao.getRecordByProjectNoMillNo(project_no,mill_no,"od_abrasive_conductivity_freq");
                        if(lt.size()>0) {
                            InspectionTimeRecord itr=lt.get(0);
                            itr.setInspction_time(odblastprocess.getOperation_time());
                            inspectionTimeRecordDao.updateInspectionTimeRecord(itr);

                        }else{
                            InspectionTimeRecord itr=new InspectionTimeRecord();
                            itr.setProject_no(project_no);
                            itr.setMill_no(mill_no);
                            itr.setInspection_item("od_abrasive_conductivity_freq");
                            itr.setInspction_time(odblastprocess.getOperation_time());
                            inspectionTimeRecordDao.addInspectionTimeRecord(itr);
                        }
                    }
                    if (odblastprocess.getSalt_contamination_before_blasting() != -99) {
                        List<InspectionTimeRecord> lt=inspectionTimeRecordDao.getRecordByProjectNoMillNo(project_no,mill_no,"od_salt_contamination_before_blast_freq");
                        if(lt.size()>0) {
                            InspectionTimeRecord itr=lt.get(0);
                            itr.setInspction_time(odblastprocess.getOperation_time());
                            inspectionTimeRecordDao.updateInspectionTimeRecord(itr);
                        }else{
                            InspectionTimeRecord itr=new InspectionTimeRecord();
                            itr.setProject_no(project_no);
                            itr.setMill_no(mill_no);
                            itr.setInspection_item("od_salt_contamination_before_blast_freq");
                            itr.setInspction_time(odblastprocess.getOperation_time());
                            inspectionTimeRecordDao.addInspectionTimeRecord(itr);
                        }

                    }
                    if (odblastprocess.getPreheat_temp() != -99) {
                        List<InspectionTimeRecord> lt=inspectionTimeRecordDao.getRecordByProjectNoMillNo(project_no,mill_no,"od_preheat_temp_freq");
                        if(lt.size()>0) {
                            InspectionTimeRecord itr=lt.get(0);
                            itr.setInspction_time(odblastprocess.getOperation_time());
                            inspectionTimeRecordDao.updateInspectionTimeRecord(itr);
                        }else{
                            InspectionTimeRecord itr=new InspectionTimeRecord();
                            itr.setProject_no(project_no);
                            itr.setMill_no(mill_no);
                            itr.setInspection_item("od_preheat_temp_freq");
                            itr.setInspction_time(odblastprocess.getOperation_time());
                            inspectionTimeRecordDao.addInspectionTimeRecord(itr);

                        }

                    }

                }

            }else{
                //修改！
                resTotal=odblastprocessDao.updateOdBlastProcess(odblastprocess);
            }
            if(resTotal>0){
                //更新管子的状态
                List<PipeBasicInfo> list=pipeBasicInfoDao.getPipeNumber(pipeno);
                if(list.size()>0){
                    PipeBasicInfo p=list.get(0);
                    if(p.getStatus().equals("bare1")) {
                        //验证钢管状态为光管  bare1     外喷砂工序   0:bare1     1:od1   10:bare1 3： onhold
                        if(odblastprocess.getResult().equals("1")) {//当合格时才更新钢管状态
                            p.setStatus("od1");
                            p.setLast_accepted_status(p.getStatus());
                        }
                        else if(odblastprocess.getResult().equals("3")) {//当需要修磨或切除时，设置为onhold状态
                            p.setStatus("onhold");
                        }
                        int statusRes = pipeBasicInfoDao.updatePipeBasicInfo(p);
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
    @RequestMapping("/delOdBlastProcess")
    public String delOdBlastProcess(@RequestParam(value = "hlparam")String hlparam,HttpServletResponse response)throws Exception{
        String[]idArr=hlparam.split(",");
        int resTotal=0;
        resTotal=odblastprocessDao.delOdBlastProcess(idArr);
        JSONObject json=new JSONObject();
        StringBuilder sbmessage = new StringBuilder();
        sbmessage.append("总共");
        sbmessage.append(Integer.toString(resTotal));
        sbmessage.append("项外喷砂信息删除成功\n");
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
    @ResponseBody
    @RequestMapping("/getOdBlastProcess")
    public  Map<String,Object> getOdBlastProcess(){
        List<OdBlastProcess> odblastprocesses= odblastprocessDao.getOdBlastProcess();
        Map<String,Object>  map= new HashMap<String, Object>();
        map.put("rows",odblastprocesses);
        return  map;
    }

    @RequestMapping("/editOdBlastProcess")
    public  String editOdBlastProcess(OdBlastProcess odblastprocess){
        System.out.println(odblastprocess);
        odblastprocessDao.updateOdBlastProcess(odblastprocess);
        return "od/odblastprocess";
    }


//    @RequestMapping(value = "/getOdBlastByLike")
//    @ResponseBody
//    public String getOdBlastByLike(@RequestParam(value = "pipe_no",required = false)String pipe_no, @RequestParam(value = "operator_no",required = false)String operator_no, @RequestParam(value = "begin_time",required = false)String begin_time, @RequestParam(value = "end_time",required = false)String end_time, HttpServletRequest request){
//        String page= request.getParameter("page");
//        String rows= request.getParameter("rows");
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        Date beginTime=null;
//        Date endTime=null;
//        try{
//            if(begin_time!=null&&begin_time!=""){
//                beginTime=new Date(begin_time);
//            }else{
//                beginTime=new Date();
//                beginTime.setTime(0);//设置时间最开始(1970-01-01)
//            }
//            if(end_time!=null&&end_time!=""){
//                endTime=new Date(end_time);
//            }else{
//                endTime=new Date();
//            }
//        }catch (Exception e){
//                e.printStackTrace();
//        }
//        int start=(Integer.parseInt(page)-1)*Integer.parseInt(rows);
//        List<OdBlastProcess> odblastprocesses= odblastprocessDao.getAllByLike(pipe_no,operator_no,beginTime,endTime,start,Integer.parseInt(rows));
//        int count=odblastprocessDao.getCount();
//        Map<String,Object> maps=new HashMap<String,Object>();
//        maps.put("total",count);
//        maps.put("rows",odblastprocesses);
//        String mmp= JSONArray.toJSONString(maps);
//        return mmp;
//    }
    @RequestMapping(value = "/getNewOdBlastByLike")
    @ResponseBody
    public String getNewOdBlastByLike(@RequestParam(value = "pipe_no",required = false)String pipe_no, @RequestParam(value = "operator_no",required = false)String operator_no, @RequestParam(value = "begin_time",required = false)String begin_time, @RequestParam(value = "end_time",required = false)String end_time, @RequestParam(value = "mill_no",required = false)String mill_no, HttpServletRequest request){
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
//        try{
//            if(begin_time!=null&&begin_time!=""){
//                beginTime=new Date(begin_time);
//            }else{
//                beginTime=new Date();
//                beginTime.setTime(0);//设置时间最开始(1970-01-01)
//            }
//            if(end_time!=null&&end_time!=""){
//                endTime=new Date(end_time);
//            }else{
//                endTime=new Date();
//            }
//        }
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
        List<HashMap<String,Object>>list=odblastprocessDao.getNewAllByLike(pipe_no,operator_no,beginTime,endTime,mill_no,start,Integer.parseInt(rows));
        int count=odblastprocessDao.getCountNewAllByLike(pipe_no,operator_no,beginTime,endTime,mill_no);
        Map<String,Object> maps=new HashMap<String,Object>();
        maps.put("total",count);
        maps.put("rows",list);
        String mmp= JSONArray.toJSONString(maps);
        //System.out.print("mmp:"+mmp);
        return mmp;
    }
    @RequestMapping("getOdBlastProcessById")
    public  String getodblastprocessById(int id,HttpServletRequest request){
        OdBlastProcess odblastprocess= odblastprocessDao.getOdBlastProcessById(id);
        request.getSession().setAttribute("sb",odblastprocess);
        System.out.println(odblastprocess);
        return  "odblastprocessedit";
    }

    //得到可以钢管最新的待定的打砂记录  最后一条记录且result为待定 10
    @RequestMapping(value = "/getPendingRecordByPipeNo")
    @ResponseBody
    public String getPendingRecordByPipeNo(@RequestParam(value = "pipe_no",required = false)String pipe_no, HttpServletRequest request) {

        OdBlastProcess odb=odblastprocessDao.getRecentRecordByPipeNo(pipe_no);
        Map<String,Object> maps=new HashMap<String,Object>();
        if(odb!=null&&odb.getResult().equals("10")){
            //是待定状态
            maps.put("success",true);
            maps.put("record",odb);
        }else{
            maps.put("success",false);
        }

        String mmp= JSONArray.toJSONString(maps);
        return mmp;

    }

}
