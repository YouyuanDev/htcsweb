package com.htcsweb.controller;



import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.htcsweb.dao.IdCoatingInspectionProcessDao;
import com.htcsweb.dao.InspectionTimeRecordDao;
import com.htcsweb.dao.PipeBasicInfoDao;
import com.htcsweb.entity.IdCoatingInspectionProcess;
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
@RequestMapping("/IdCoatInOperation")
public class IdCoatingInspectionProcessController {
    @Autowired
    private IdCoatingInspectionProcessDao idCoatingInspectionProcessDao;
    @Autowired
    private PipeBasicInfoDao pipeBasicInfoDao;
    @Autowired
    private InspectionTimeRecordDao inspectionTimeRecordDao;

    //查询
    @RequestMapping(value = "/getIdCoatingInByLike")
    @ResponseBody
    public String getIdCoatingInByLike(@RequestParam(value = "pipe_no",required = false)String pipe_no, @RequestParam(value = "operator_no",required = false)String operator_no, @RequestParam(value = "begin_time",required = false)String begin_time, @RequestParam(value = "end_time",required = false)String end_time, @RequestParam(value = "mill_no",required = false)String mill_no, HttpServletRequest request){
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
        List<HashMap<String,Object>> list=idCoatingInspectionProcessDao.getAllByLike(pipe_no,operator_no,beginTime,endTime,mill_no,start,Integer.parseInt(rows));
        int count=idCoatingInspectionProcessDao.getCountAllByLike(pipe_no,operator_no,beginTime,endTime,mill_no);
        Map<String,Object> maps=new HashMap<String,Object>();
        maps.put("total",count);
        maps.put("rows",list);
        String mmp= JSONArray.toJSONString(maps);
        return mmp;
    }

    //添加、修改
    @RequestMapping("/saveIdCoatingInProcess")
    @ResponseBody
    public String saveIdCoatingInProcess(IdCoatingInspectionProcess idCoatingInspectionProcess, HttpServletRequest request, HttpServletResponse response){
        JSONObject json=new JSONObject();
        try{
            int resTotal=0;
            if(idCoatingInspectionProcess.getOperation_time()==null){
                idCoatingInspectionProcess.setOperation_time(new Date());
            }
            String pipeno=idCoatingInspectionProcess.getPipe_no();
            String msg="";
            if(idCoatingInspectionProcess.getId()==0){
                //添加
                List<HashMap<String,Object>>list=pipeBasicInfoDao.getPipeInfoByNo(idCoatingInspectionProcess.getPipe_no());
                if(list.size()>0){
                    String pipestatus=(String)list.get(0).get("status");
                    if(pipestatus.equals("id3")){
                        IdCoatingInspectionProcess oldrecord=idCoatingInspectionProcessDao.getRecentRecordByPipeNo(idCoatingInspectionProcess.getPipe_no());
                        if(oldrecord!=null&&oldrecord.getResult().equals("10")){
                            //存在一条pending数据，不给予insert处理
                            msg="已存在待定记录,不能新增记录";
                        }else{
                            resTotal=idCoatingInspectionProcessDao.addIdCoatingInProcess(idCoatingInspectionProcess);
                        }
                    }
                }
                String project_no="";
                String mill_no=idCoatingInspectionProcess.getMill_no();
                if(list.size()>0){
                    project_no=(String)list.get(0).get("project_no");
                }
                System.out.println("project_no="+project_no);

                //更新增量 inspectionTimeMap
                if (!idCoatingInspectionProcess.getWet_film_thickness_list().equals("")) {

                    List<InspectionTimeRecord> lt=inspectionTimeRecordDao.getRecordByProjectNoMillNo(project_no,mill_no,"id_wet_film_thickness_freq");
                    if(lt.size()>0) {
                        InspectionTimeRecord itr=lt.get(0);
                        itr.setInspction_time(idCoatingInspectionProcess.getOperation_time());
                        inspectionTimeRecordDao.updateInspectionTimeRecord(itr);
                    }else{
                        InspectionTimeRecord itr=new InspectionTimeRecord();
                        itr.setProject_no(project_no);
                        itr.setMill_no(mill_no);
                        itr.setInspection_item("id_wet_film_thickness_freq");
                        itr.setInspction_time(idCoatingInspectionProcess.getOperation_time());
                        inspectionTimeRecordDao.addInspectionTimeRecord(itr);
                    }

                }


            }else{
                //修改！
                resTotal=idCoatingInspectionProcessDao.updateIdCoatingInProcess(idCoatingInspectionProcess);
            }
            if(resTotal>0){
                //更新管子的状态
                List<PipeBasicInfo> list=pipeBasicInfoDao.getPipeNumber(pipeno);
                if(list.size()>0){
                    PipeBasicInfo p=list.get(0);
                    if(p.getStatus().equals("id3")) {
                        //验证钢管状态为内涂完成管    id3  内涂检验工序  0:idrepair1  1:id4   2:idstrip1  10:id3  4: onhold
                        if(idCoatingInspectionProcess.getResult().equals("1")) {//当合格时才更新钢管状态
                            p.setStatus("id4");
                            p.setLast_accepted_status(p.getStatus());
                            //设置idsampling mark
                            if(idCoatingInspectionProcess.getIs_sample().equals("1")){
                                p.setIdsampling_mark("1");
                            }
                            if(idCoatingInspectionProcess.getIs_glass_sample().equals("1")){
                                p.setId_glass_sample_mark("1");
                            }

                        }else if(idCoatingInspectionProcess.getResult().equals("0")){
                            p.setStatus("idrepair1");
                        }else if(idCoatingInspectionProcess.getResult().equals("2")){
                            p.setStatus("idstrip1");
                            p.setLast_accepted_status(p.getStatus());
                        }else if(idCoatingInspectionProcess.getResult().equals("4")){
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
    //删除
    @RequestMapping("/delIdCoatingInProcess")
    public String delIdCoatingInProcess(@RequestParam(value = "hlparam")String hlparam,HttpServletResponse response)throws Exception{
        String[]idArr=hlparam.split(",");
        int resTotal=0;
        resTotal=idCoatingInspectionProcessDao.delIdCoatingInProcess(idArr);
        JSONObject json=new JSONObject();
        StringBuilder sbmessage = new StringBuilder();
        sbmessage.append("总共");
        sbmessage.append(Integer.toString(resTotal));
        sbmessage.append("项内涂层检验信息删除成功\n");
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

    //得到可以钢管最新的待定的内防喷涂检验记录  最后一条记录且result为待定 10
    @RequestMapping(value = "/getPendingRecordByPipeNo")
    @ResponseBody
    public String getPendingRecordByPipeNo(@RequestParam(value = "pipe_no",required = false)String pipe_no, HttpServletRequest request) {

        IdCoatingInspectionProcess record=idCoatingInspectionProcessDao.getRecentRecordByPipeNo(pipe_no);
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
