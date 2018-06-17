package com.htcsweb.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.htcsweb.dao.PipeBasicInfoDao;
import com.htcsweb.entity.CoatingStrip;
import com.htcsweb.dao.CoatingStripDao;
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
@RequestMapping("/coatingStripOperation")
public class CoatingStripController {

    @Autowired
    private CoatingStripDao coatingStripDao;
    @Autowired
    private PipeBasicInfoDao pipeBasicInfoDao;

    @RequestMapping(value = "/getCoatingStripInfoByLike")
    @ResponseBody
    public String getCoatingStripInfoByLike(@RequestParam(value = "pipe_no",required = false)String pipe_no, @RequestParam(value = "operator_no",required = false)String operator_no, @RequestParam(value = "project_no",required = false)String project_no,@RequestParam(value = "contract_no",required = false)String contract_no, @RequestParam(value = "begin_time",required = false)String begin_time, @RequestParam(value = "end_time",required = false)String end_time, @RequestParam(value = "mill_no",required = false)String mill_no,HttpServletRequest request){
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
        List<HashMap<String,Object>>list=coatingStripDao.getCoatingStripInfoByLike(pipe_no,operator_no,project_no,contract_no,beginTime,endTime,mill_no,start,Integer.parseInt(rows));
        int count=coatingStripDao.getCountCoatingStripInfoByLike(pipe_no,operator_no,project_no,contract_no,beginTime,endTime,mill_no);
        Map<String,Object> maps=new HashMap<String,Object>();
        maps.put("total",count);
        maps.put("rows",list);
        String mmp= JSONArray.toJSONString(maps);
        //System.out.print("mmp:"+mmp);
        return mmp;
    }


    @RequestMapping("/saveCoatingStrip")
    @ResponseBody
    public String saveCoatingStrip(CoatingStrip coatingStrip, HttpServletRequest request, HttpServletResponse response){
        JSONObject json=new JSONObject();
        try{
            int resTotal=0;
            if(coatingStrip.getOperation_time()==null){
                coatingStrip.setOperation_time(new Date());
            }
            String pipeno=coatingStrip.getPipe_no();
            String msg="";
            if(coatingStrip.getId()==0){
                //添加
                List<HashMap<String,Object>>list =pipeBasicInfoDao.getPipeInfoByNo(coatingStrip.getPipe_no());
                if(list.size()>0) {
                    String pipestatus = (String) list.get(0).get("status");
                    if (pipestatus.equals("odstrip1")||pipestatus.equals("idstrip1")) {
                        List<CoatingStrip> oldlist = coatingStripDao.getRecentRecordByPipeNo(coatingStrip.getPipe_no());
                        if (oldlist != null && oldlist.size() > 0 && oldlist.get(0).getResult().equals("10")) {
                            //存在一条pending数据，不给予insert处理
                            msg="已存在待定记录,不能新增记录";
                        } else {
                            resTotal=coatingStripDao.addCoatingStrip(coatingStrip);
                        }
                    }
                }

            }else{
                //修改！
                resTotal=coatingStripDao.updateCoatingStrip(coatingStrip);
            }
            if(resTotal>0){
                //更新管子的状态
                List<PipeBasicInfo> list=pipeBasicInfoDao.getPipeNumber(pipeno);
                if(list.size()>0){
                    PipeBasicInfo p=list.get(0);
                    if(p.getStatus().equals("odstrip1")) {
                        //验证钢管状态为od 扒皮管
                        if(coatingStrip.getResult().equals("1")) {//当合格时才更新钢管状态
                            //p.setStatus("odstrip2");
                            p.setStatus("bare1");
                            p.setLast_accepted_status(p.getStatus());
                            //删除钢管外涂时间
                            p.setOd_coating_date(null);
                            //删除钢管内涂日期，因为外扒皮会废掉内涂层
                            p.setId_coating_date(null);
                            int statusRes = pipeBasicInfoDao.updatePipeBasicInfo(p);
                        }

                    }else if(p.getStatus().equals("idstrip1")) {
                        //验证钢管状态为id 扒皮管
                        if(coatingStrip.getResult().equals("1")) {//当合格时才更新钢管状态
                            //p.setStatus("idstrip2");
                            p.setStatus("bare2");
                            p.setLast_accepted_status(p.getStatus());
                            //删除钢管内涂时间
                            p.setId_coating_date(null);
                            int statusRes = pipeBasicInfoDao.updatePipeBasicInfo(p);
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
    //删除扒皮记录
    @RequestMapping("/delCoatingStrip")
    public String delCoatingRepair(@RequestParam(value = "hlparam")String hlparam,HttpServletResponse response)throws Exception{
        String[]idArr=hlparam.split(",");
        int resTotal=0;
        resTotal=coatingStripDao.delCoatingStrip(idArr);
        JSONObject json=new JSONObject();
        StringBuilder sbmessage = new StringBuilder();
        sbmessage.append("总共");
        sbmessage.append(Integer.toString(resTotal));
        sbmessage.append("项扒皮记录删除成功\n");
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

    //得到可以钢管最新的待定的涂层扒皮记录  最后一条记录且result为待定 10
    @RequestMapping(value = "/getPendingRecordByPipeNo")
    @ResponseBody
    public String getPendingRecordByPipeNo(@RequestParam(value = "pipe_no",required = false)String pipe_no, HttpServletRequest request) {

        List<CoatingStrip> list=coatingStripDao.getRecentRecordByPipeNo(pipe_no);
        Map<String, Object> maps = new HashMap<String, Object>();
        if(list.size()>0) {
            CoatingStrip record=list.get(0);
            if (record!=null&&record.getResult().equals("10")) {
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
