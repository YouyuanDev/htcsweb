package com.htcsweb.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.htcsweb.dao.IdFinalInspectionProcessDao;
import com.htcsweb.dao.PipeBasicInfoDao;
import com.htcsweb.entity.IdFinalInspectionProcess;
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
            }else{
                //修改！
                resTotal=idFinalInspectionProcessDao.updateIdFinalInProcess(idFinalInspectionProcess);
            }
            if(resTotal>0){
                //更新管子的状态
                List<PipeBasicInfo> list=pipeBasicInfoDao.getPipeNumber(pipeno);
                if(list.size()>0){
                    PipeBasicInfo p=list.get(0);
                    //验证钢管状态是否为内喷标完成或者外防入库
                    if(p.getStatus().equals("id5")||p.getStatus().equals("odstockin")||p.getStatus().equals("idrepair2")){
                        //如果为合格
                        if(idFinalInspectionProcess.getResult().equals("1")) {
                            p.setStatus("id6");
                            int statusRes=pipeBasicInfoDao.updatePipeBasicInfo(p);
                        }else if(idFinalInspectionProcess.getResult().equals("0")){
                            p.setStatus("odrepair1");
                            int statusRes = pipeBasicInfoDao.updatePipeBasicInfo(p);
                        }else if(idFinalInspectionProcess.getResult().equals("2")){
                            p.setStatus("odstrip1");
                            int statusRes = pipeBasicInfoDao.updatePipeBasicInfo(p);
                        }else if(idFinalInspectionProcess.getResult().equals("3")){
                            p.setStatus("od4");
                            int statusRes = pipeBasicInfoDao.updatePipeBasicInfo(p);
                        }else if(idFinalInspectionProcess.getResult().equals("4")){
                            p.setStatus("idrepair1");
                            int statusRes = pipeBasicInfoDao.updatePipeBasicInfo(p);
                        }else if(idFinalInspectionProcess.getResult().equals("5")){
                            p.setStatus("idstrip1");
                            int statusRes = pipeBasicInfoDao.updatePipeBasicInfo(p);
                        }else if(idFinalInspectionProcess.getResult().equals("6")){
                            p.setStatus("id4");
                            int statusRes = pipeBasicInfoDao.updatePipeBasicInfo(p);
                        }
                    }
                }
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
    @RequestMapping("/delIdFinalInProcess")
    public String delIdFinalInProcess(@RequestParam(value = "hlparam")String hlparam,HttpServletResponse response)throws Exception{
        String[]idArr=hlparam.split(",");
        int resTotal=0;
        resTotal=idFinalInspectionProcessDao.delIdFinalInProcess(idArr);
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
