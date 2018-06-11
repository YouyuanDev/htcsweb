package com.htcsweb.controller;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.htcsweb.dao.PipeBasicInfoDao;
import com.htcsweb.entity.CoatingRepair;
import com.htcsweb.dao.CoatingRepairDao;
import com.htcsweb.util.ComboxItem;
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
import java.text.ParseException;
import java.util.*;


@Controller
@RequestMapping("/coatingRepairOperation")
public class CoatingRepairController {

    @Autowired
    private CoatingRepairDao coatingRepairDao;
    @Autowired
    private PipeBasicInfoDao pipeBasicInfoDao;

    @RequestMapping(value = "/getCoatingRepairInfoByLike")
    @ResponseBody
    public String getCoatingRepairInfoByLike(@RequestParam(value = "pipe_no",required = false)String pipe_no, @RequestParam(value = "operator_no",required = false)String operator_no, @RequestParam(value = "project_no",required = false)String project_no,@RequestParam(value = "mill_no",required = false)String mill_no,@RequestParam(value = "contract_no",required = false)String contract_no,@RequestParam(value = "status",required = false)String status, @RequestParam(value = "begin_time",required = false)String begin_time, @RequestParam(value = "end_time",required = false)String end_time, HttpServletRequest request){
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
//                System.out.println(beginTime.toString());
            }
            if(end_time!=null&&end_time!=""){
                endTime=sdf.parse(end_time);
//                System.out.println(endTime.toString());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        int start=(Integer.parseInt(page)-1)*Integer.parseInt(rows);
        List<HashMap<String,Object>>list=coatingRepairDao.getCoatingRepairInfoByLike(pipe_no,operator_no,project_no,contract_no,mill_no,status,beginTime,endTime,start,Integer.parseInt(rows));
        int count=coatingRepairDao.getCountCoatingRepairInfoByLike(pipe_no,operator_no,project_no,contract_no,mill_no,status,beginTime,endTime);
        Map<String,Object> maps=new HashMap<String,Object>();
        maps.put("total",count);
        maps.put("rows",list);
        String mmp= JSONArray.toJSONString(maps);
        //System.out.print("mmp:"+mmp);
        return mmp;
    }



    @RequestMapping("/saveCoatingRepair")
    @ResponseBody
    public String saveCoatingRepair(CoatingRepair coatingRepair, HttpServletRequest request, HttpServletResponse response){
        JSONObject json=new JSONObject();
        try{
            String operationtime= request.getParameter("operation-time");
            String inspectiontime=request.getParameter("inspection-time");
            int resTotal=0;
            if(operationtime!=null&&operationtime!=""){
                SimpleDateFormat simFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date new_operationtime = simFormat.parse(operationtime);
                coatingRepair.setOperation_time(new_operationtime);
            }else{
                coatingRepair.setOperation_time(new Date());
            }

            if(inspectiontime!=null&&inspectiontime!=""){
                SimpleDateFormat simFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date new_inspectiontime = simFormat.parse(inspectiontime);
                coatingRepair.setInspection_time(new_inspectiontime);
            }else{
                coatingRepair.setInspection_time(new Date());
            }

            String pipeno=coatingRepair.getPipe_no();
            if(coatingRepair.getId()==0){
                //添加
                resTotal=coatingRepairDao.addCoatingRepair(coatingRepair);
            }else{
                //修改！
                resTotal=coatingRepairDao.updateCoatingRepair(coatingRepair);
            }
            if(resTotal>0){
                //更新管子的状态
                List<PipeBasicInfo> list=pipeBasicInfoDao.getPipeNumber(pipeno);
                if(list.size()>0){
                    PipeBasicInfo p=list.get(0);
                    if(p.getStatus().equals("odrepair1")||p.getStatus().equals("odrepair2")) {
                        //验证钢管状态为外防工序修补管  0:odrepair1 1：上一个工序状态 od3 或者 od5  2:odrepair2  3:odstrip1  4:idstrip1 10:odrepair1 or odrepair2
                        if(coatingRepair.getResult().equals("0")) {//修补不合格重修
                            p.setStatus("odrepair1");
                        }
                        else if(coatingRepair.getResult().equals("1")) {//当合格时才更新钢管状态 上一个工序状态
                            p.setStatus(p.getLast_accepted_status());
                        }
                        else if(coatingRepair.getResult().equals("2")) {//修补完成，待检验
                            p.setStatus("odrepair2");
                        }
                        else if(coatingRepair.getResult().equals("3")) {//当不合格需要外扒皮时 更新钢管状态
                            p.setStatus("odstrip1");
                        }
                        else if(coatingRepair.getResult().equals("4")) {//当不合格需要内扒皮时 更新钢管状态
                            p.setStatus("idstrip1");
                        }
                    }else if(p.getStatus().equals("idrepair1")||p.getStatus().equals("idrepair2")) {
                        //验证钢管状态为id 修补管  0:idrepair1 1：上一个工序状态 id3 或者 id5  2:idrepair2  3:odstrip1  4:idstrip1 10:idrepair1 or idrepair2
                        if(coatingRepair.getResult().equals("0")) {//修补不合格重修
                            p.setStatus("idrepair1");
                        }
                        else if(coatingRepair.getResult().equals("1")) {//当合格时才更新钢管状态
                            p.setStatus(p.getLast_accepted_status());
                        }
                        else if(coatingRepair.getResult().equals("2")) {//当不合格需要扒皮时 更新钢管状态
                            p.setStatus("idrepair2");
                        }
                        else if(coatingRepair.getResult().equals("3")) {//当不合格需要外扒皮时 更新钢管状态
                            p.setStatus("odstrip1");
                        }
                        else if(coatingRepair.getResult().equals("4")) {//当不合格需要内扒皮时 更新钢管状态
                            p.setStatus("idstrip1");
                        }
                    }
                    int statusRes = pipeBasicInfoDao.updatePipeBasicInfo(p);

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
    //删除外防腐修补记录
    @RequestMapping("/delCoatingRepair")
    public String delCoatingRepair(@RequestParam(value = "hlparam")String hlparam,HttpServletResponse response)throws Exception{
        String[]idArr=hlparam.split(",");
        int resTotal=0;
        resTotal=coatingRepairDao.delCoatingRepair(idArr);
        JSONObject json=new JSONObject();
        StringBuilder sbmessage = new StringBuilder();
        sbmessage.append("总共");
        sbmessage.append(Integer.toString(resTotal));
        sbmessage.append("项外防修补记录删除成功\n");
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

    //得到可以钢管最新的待定的涂层修补记录  最后一条记录且result为待定 10
    @RequestMapping(value = "/getPendingRecordByPipeNo")
    @ResponseBody
    public String getPendingRecordByPipeNo(@RequestParam(value = "pipe_no",required = false)String pipe_no, HttpServletRequest request) {

        List<CoatingRepair> list=coatingRepairDao.getRecentRecordByPipeNo(pipe_no);
        Map<String, Object> maps = new HashMap<String, Object>();
        if(list.size()>0) {
            CoatingRepair record=list.get(0);
            if (record.getResult().equals("10")) {
                //是待定状态
                maps.put("success", true);
                maps.put("record", record);
            } else {
                maps.put("success", false);
            }
        }
        else {
            maps.put("success", false);
        }
        String mmp= JSONArray.toJSONString(maps);
        return mmp;
    }
}
