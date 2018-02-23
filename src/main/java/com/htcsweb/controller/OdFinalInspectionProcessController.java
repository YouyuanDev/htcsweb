package com.htcsweb.controller;



import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.htcsweb.dao.OdFinalInspectionProcessDao;

import com.htcsweb.dao.PipeBasicInfoDao;

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

    //查询
    @RequestMapping(value = "/getOdFinalInByLike")
    @ResponseBody
    public String getOdFinalInByLike(@RequestParam(value = "pipe_no",required = false)String pipe_no, @RequestParam(value = "operator_no",required = false)String operator_no, @RequestParam(value = "begin_time",required = false)String begin_time, @RequestParam(value = "end_time",required = false)String end_time, HttpServletRequest request){
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
        List<HashMap<String,Object>> list=odFinalInspectionProcessDao.getAllByLike(pipe_no,operator_no,beginTime,endTime,start,Integer.parseInt(rows));
        int count=odFinalInspectionProcessDao.getCount();
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
            }else{
                //修改！
                resTotal=odFinalInspectionProcessDao.updateOdFinalInProcess(odFinalInspectionProcess);
            }
            if(resTotal>0){
                //更新管子的状态
//                List<PipeBasicInfo> list=pipeBasicInfoDao.getPipeNumber(pipeno);
//                if(list.size()>0){
//                    PipeBasicInfo p=list.get(0);
//                    if(p.getStatus().equals("od2")) {
//                        //验证钢管状态为光管
//                        if(odStencilProcess.getResult().equals("1")) {//当合格时才更新钢管状态
//                            p.setStatus("od3");
//                            int statusRes = pipeBasicInfoDao.updatePipeBasicInfo(p);
//                        }else if(odStencilProcess.getResult().equals("0")){
//                            p.setStatus("odstrip1");
//                            int statusRes = pipeBasicInfoDao.updatePipeBasicInfo(p);
//                        }
//                    }
//                }
               json.put("success",true);
            }else{
                json.put("success",false);
            }
            ResponseUtil.write(response,json);
        }catch (Exception e){
            e.printStackTrace();
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
        if(resTotal>0){
            json.put("success",true);
        }else{
            json.put("success",false);
        }
        ResponseUtil.write(response,json);
        return null;
    }
}
