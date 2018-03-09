package com.htcsweb.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.htcsweb.dao.BarePipeGrindingCutoffRecordDao;
import com.htcsweb.dao.PipeBasicInfoDao;
import com.htcsweb.entity.BarePipeGrindingCutoffRecord;
import com.htcsweb.entity.OdBlastProcess;
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
@RequestMapping("/BarePipeGrindingOperation")
public class BarePipeGrindingController {

    @Autowired
    BarePipeGrindingCutoffRecordDao  barePipeGrindingCutoffRecordDao;
    @Autowired
    private PipeBasicInfoDao pipeBasicInfoDao;

    @RequestMapping("/saveBarePipeGrindingProcess")
    @ResponseBody
    public String saveBarePipeGrindingProcess(BarePipeGrindingCutoffRecord barePipeGrindingCutoffRecord, HttpServletRequest request, HttpServletResponse response){
        JSONObject json=new JSONObject();
        try{
            String operationtime= request.getParameter("operation-time");
            int resTotal=0;
            if(operationtime!=null&&operationtime!=""){
                SimpleDateFormat simFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date new_optime = simFormat.parse(operationtime);
                barePipeGrindingCutoffRecord.setOperation_time(new_optime);
            }else{
                barePipeGrindingCutoffRecord.setOperation_time(new Date());
            }
            String pipeno=barePipeGrindingCutoffRecord.getPipe_no();
            if(barePipeGrindingCutoffRecord.getId()==0){
                //添加
                resTotal=barePipeGrindingCutoffRecordDao.addBarePipeGrindingCutoffRecord(barePipeGrindingCutoffRecord);
            }else{
                //修改！
                resTotal=barePipeGrindingCutoffRecordDao.updateBarePipeGrindingCutoffRecord(barePipeGrindingCutoffRecord);
            }
            if(resTotal>0){
                //更新管子的状态
                List<PipeBasicInfo> list=pipeBasicInfoDao.getPipeNumber(pipeno);
                if(list.size()>0){
                    PipeBasicInfo p=list.get(0);
                    if(p.getStatus().equals("onhold")) {
                        //验证钢管状态为隔离管
                        if(barePipeGrindingCutoffRecord.getResult()!=null&&barePipeGrindingCutoffRecord.getResult().equals("1")) {//当合格时才更新钢管状态
                            if(barePipeGrindingCutoffRecord.getOdid().equals("OD")) {
                                p.setStatus("bare1");
                            }
                            else {
                                p.setStatus("bare2");
                            }
                            int statusRes = pipeBasicInfoDao.updatePipeBasicInfo(p);
                        }
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
    @RequestMapping("/delBarePipeGrindingProcess")
    public String delBarePipeGrindingProcess(@RequestParam(value = "hlparam")String hlparam, HttpServletResponse response)throws Exception{
        String[]idArr=hlparam.split(",");
        int resTotal=0;
        resTotal=barePipeGrindingCutoffRecordDao.delBarePipeGrindingCutoffRecord(idArr);
        JSONObject json=new JSONObject();
        StringBuilder sbmessage = new StringBuilder();
        sbmessage.append("总共");
        sbmessage.append(Integer.toString(resTotal));
        sbmessage.append("项修磨信息删除成功\n");
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



    @RequestMapping("/editBarePipeGrindingProcess")
    public  String editBarePipeGrindingProcess(BarePipeGrindingCutoffRecord barePipeGrindingCutoffRecord){
        System.out.println(barePipeGrindingCutoffRecord);
        barePipeGrindingCutoffRecordDao.updateBarePipeGrindingCutoffRecord(barePipeGrindingCutoffRecord);
        return "grinding/barePipeGrindingCutoffRecord";
    }



    @RequestMapping(value = "/getAllByLike")
    @ResponseBody
    public String getAllByLike(@RequestParam(value = "pipe_no",required = false)String pipe_no, @RequestParam(value = "operator_no",required = false)String operator_no, @RequestParam(value = "begin_time",required = false)String begin_time, @RequestParam(value = "end_time",required = false)String end_time, @RequestParam(value = "mill_no",required = false)String mill_no, HttpServletRequest request){
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
        List<HashMap<String,Object>>list=barePipeGrindingCutoffRecordDao.getAllByLike(pipe_no,operator_no,beginTime,endTime,mill_no,start,Integer.parseInt(rows));
        int count=barePipeGrindingCutoffRecordDao.getCountAllByLike(pipe_no,operator_no,beginTime,endTime,mill_no);
        Map<String,Object> maps=new HashMap<String,Object>();
        maps.put("total",count);
        maps.put("rows",list);
        String mmp= JSONArray.toJSONString(maps);
        //System.out.print("mmp:"+mmp);
        return mmp;
    }



}
