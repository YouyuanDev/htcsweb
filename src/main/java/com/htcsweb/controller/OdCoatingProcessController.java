package com.htcsweb.controller;



import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.htcsweb.dao.InspectionTimeRecordDao;
import com.htcsweb.dao.OdBlastInspectionProcessDao;
import com.htcsweb.dao.OdCoatingProcessDao;
import com.htcsweb.dao.PipeBasicInfoDao;
import com.htcsweb.entity.InspectionTimeRecord;
import com.htcsweb.entity.OdCoatingProcess;
import com.htcsweb.entity.PipeBasicInfo;
import com.htcsweb.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.text.html.MinimalHTMLWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/OdCoatOperation")
public class OdCoatingProcessController {
    @Autowired
    private OdCoatingProcessDao odCoatingProcessDao;
    @Autowired
    private PipeBasicInfoDao pipeBasicInfoDao;
    @Autowired
    private OdBlastInspectionProcessDao odBlastInspectionProcessDao;
    @Autowired
    private InspectionTimeRecordDao inspectionTimeRecordDao;
    //查询
    @RequestMapping(value = "/getOdCoatingByLike")
    @ResponseBody
    public String getOdCoatingByLike(@RequestParam(value = "pipe_no",required = false)String pipe_no, @RequestParam(value = "operator_no",required = false)String operator_no, @RequestParam(value = "begin_time",required = false)String begin_time, @RequestParam(value = "end_time",required = false)String end_time, @RequestParam(value = "mill_no",required = false)String mill_no, HttpServletRequest request){
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
        List<HashMap<String,Object>> list=odCoatingProcessDao.getAllByLike(pipe_no,operator_no,beginTime,endTime,mill_no,start,Integer.parseInt(rows));
        int count=odCoatingProcessDao.getCountAllByLike(pipe_no,operator_no,beginTime,endTime,mill_no);
        Map<String,Object> maps=new HashMap<String,Object>();
        maps.put("total",count);
        maps.put("rows",list);
        String mmp= JSONArray.toJSONString(maps);
        return mmp;
    }

    //添加、修改
    @RequestMapping("/saveOdCoatingProcess")
    @ResponseBody
    public String saveOdCoatingProcess(OdCoatingProcess odCoatingProcess, HttpServletRequest request, HttpServletResponse response){
        JSONObject json=new JSONObject();
        try{
            String odcoatprotime= request.getParameter("odcoatprotime");
            int resTotal=0;
            if(odCoatingProcess.getOperation_time()==null){
                SimpleDateFormat simFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date new_odbptime = simFormat.parse(odcoatprotime);
                odCoatingProcess.setOperation_time(new_odbptime);
            }
            String pipeno=odCoatingProcess.getPipe_no();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String msg="";
            if(odCoatingProcess.getId()==0){
                //添加
                List<HashMap<String,Object>>list=pipeBasicInfoDao.getPipeInfoByNo(odCoatingProcess.getPipe_no());
                if(list.size()>0){
                    String pipestatus=(String)list.get(0).get("status");
                    if(pipestatus.equals("od2")){
                        OdCoatingProcess oldrecord=odCoatingProcessDao.getRecentRecordByPipeNo(odCoatingProcess.getPipe_no());
                        if(oldrecord!=null&&oldrecord.getResult().equals("10")){
                            //存在一条pending数据，不给予insert处理
                             msg="已存在待定记录,不能新增记录";
                        }else{
                            resTotal=odCoatingProcessDao.addOdCoatingProcess(odCoatingProcess);
                        }
                    }
                }

                String project_no="";
                String mill_no=odCoatingProcess.getMill_no();
                if(list.size()>0){
                    project_no=(String)list.get(0).get("project_no");
                }
                System.out.println("project_no="+project_no);

                //更新增量 inspectionTimeMap
                if (odCoatingProcess.getApplication_temp() != -99) {

                    List<InspectionTimeRecord> lt=inspectionTimeRecordDao.getRecordByProjectNoMillNo(project_no,mill_no,"od_application_temp_freq");
                    if(lt.size()>0) {
                        InspectionTimeRecord itr=lt.get(0);
                        itr.setInspction_time(odCoatingProcess.getOperation_time());
                        inspectionTimeRecordDao.updateInspectionTimeRecord(itr);
                    }else{
                        InspectionTimeRecord itr=new InspectionTimeRecord();
                        itr.setProject_no(project_no);
                        itr.setMill_no(mill_no);
                        itr.setInspection_item("od_application_temp_freq");
                        itr.setInspction_time(odCoatingProcess.getOperation_time());
                        inspectionTimeRecordDao.addInspectionTimeRecord(itr);
                    }

                }
            }else{
                //修改！
                resTotal=odCoatingProcessDao.updateOdCoatingProcess(odCoatingProcess);

            }

            if(resTotal>0){
                //此时的resTotal为新增厚的记录的id，更新odBlastInsepction的等待时间
                //－先根据新增id查询外打砂检验的id,然后更新
                int id=odCoatingProcess.getId();
                List<HashMap<String,Object>>list=odBlastInspectionProcessDao.getOdBlastInfoBy2fbeCoatingInfo(pipeno,id);
                if(list!=null&&list.size()>0){
                    HashMap<String,Object>hs=list.get(0);
                    int odBlastId=Integer.parseInt(String.valueOf(hs.get("id")));
                    long begin_time=format.parse(String.valueOf(hs.get("odcoatingtime"))).getTime();
                    long end_time=format.parse(String.valueOf(hs.get("odblasttime"))).getTime();
                    float minute=((begin_time-end_time)/(1000));
                    minute=minute/60;
                    minute=(float)(Math.round(minute*100))/100;
                    odBlastInspectionProcessDao.updateElapsedTime(minute,odBlastId);
                }
                //更新管子的状态
                List<PipeBasicInfo> pipelist=pipeBasicInfoDao.getPipeNumber(pipeno);
                if(pipelist.size()>0){
                    PipeBasicInfo p=pipelist.get(0);
                    if(p.getStatus().equals("od2")) {
                        //验证钢管状态为光管  od2      外涂工序   0:bare1     1:od3    10:od2
                        if(odCoatingProcess.getResult().equals("1")) {//当合格时才更新钢管状态
                            p.setStatus("od3");
                            p.setLast_accepted_status(p.getStatus());
                            //同时更新钢管基础信息的od_coating_date信息，外涂日期
                            p.setOd_coating_date(odCoatingProcess.getOperation_time());
                        }else if(odCoatingProcess.getResult().equals("0")){
                            p.setStatus("bare1");
                            p.setLast_accepted_status(p.getStatus());
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
            ResponseUtil.write(response,json);
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
    @RequestMapping("/delOdCoatingProcess")
    public String delOdCoatingProcess(@RequestParam(value = "hlparam")String hlparam,HttpServletResponse response)throws Exception{
        String[]idArr=hlparam.split(",");
        int resTotal=0;
        resTotal=odCoatingProcessDao.delOdCoatingProcess(idArr);
        JSONObject json=new JSONObject();
        StringBuilder sbmessage = new StringBuilder();
        sbmessage.append("总共");
        sbmessage.append(Integer.toString(resTotal));
        sbmessage.append("项外涂信息删除成功\n");
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

    //得到可以钢管最新的待定的2fbe涂层记录  最后一条记录且result为待定 10
    @RequestMapping(value = "/getPendingRecordByPipeNo")
    @ResponseBody
    public String getPendingRecordByPipeNo(@RequestParam(value = "pipe_no",required = false)String pipe_no, HttpServletRequest request) {

        OdCoatingProcess record=odCoatingProcessDao.getRecentRecordByPipeNo(pipe_no);
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


    //得到钢管上一根最新的合格的3lpe涂层记录  最后一条记录且result为合格 1
    @RequestMapping(value = "/getLastAcceptedRecordBeforePipeNo")
    @ResponseBody
    public String getLastAcceptedRecordBeforePipeNo(@RequestParam(value = "pipe_no",required = false)String pipe_no, HttpServletRequest request) {
        HttpSession session = request.getSession();
        //OdCoatOperation/getLastAcceptedRecordBeforePipeNo.action?pipe_no=121212
        //把用户数据保存在session域对象中
        String mill_no = (String) session.getAttribute("millno");
        Map<String,Object> maps=new HashMap<String,Object>();
        if(mill_no!=null){
            OdCoatingProcess record=odCoatingProcessDao.getLastAcceptedRecordBeforePipeNo(pipe_no,mill_no);
            if(record!=null){
                //是合格状态
                maps.put("success",true);
                maps.put("record",record);
            }else{
                maps.put("success",false);
            }

        }else{
            maps.put("success",false);
        }
        String mmp= JSONArray.toJSONString(maps);
        return mmp;

    }


    //得到钢管后10根涂层记录管号，并且记录为待定状态 10
    @RequestMapping(value = "/getNextTenPipesBeforePipeNo")
    @ResponseBody
    public String getNextTenPipesBeforePipeNo(@RequestParam(value = "pipe_no",required = false)String pipe_no, HttpServletRequest request) {
        HttpSession session = request.getSession();
        //OdCoatOperation/getLastAcceptedRecordBeforePipeNo.action?pipe_no=121212
        //把用户数据保存在session域对象中
        String mill_no = (String) session.getAttribute("millno");
        Map<String,Object> maps=new HashMap<String,Object>();
        if(mill_no!=null){
            List<HashMap<String,Object>> list=odCoatingProcessDao.getNextTenPipesBeforePipeNo(pipe_no,mill_no);
            if(list.size()>0){
                //是合格状态
                maps.put("success",true);
                maps.put("data",list);
            }else{
                maps.put("success",false);
            }

        }else{
            maps.put("success",false);
        }
        String mmp= JSONArray.toJSONString(maps);
        return mmp;

    }


}
