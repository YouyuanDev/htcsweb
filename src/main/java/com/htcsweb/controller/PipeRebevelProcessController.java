package com.htcsweb.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.htcsweb.dao.PipeRebevelRecordDao;
import com.htcsweb.dao.PipeBasicInfoDao;
import com.htcsweb.entity.BarePipeGrindingCutoffRecord;
import com.htcsweb.entity.PipeBasicInfo;
import com.htcsweb.entity.PipeRebevelRecord;
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
@RequestMapping("/PipeRebevelOperation")
public class PipeRebevelProcessController {

    @Autowired
    PipeRebevelRecordDao pipeRebevelRecordDao;
    @Autowired
    private PipeBasicInfoDao pipeBasicInfoDao;

    @RequestMapping("/savePipeRebevelProcess")
    @ResponseBody
    public String savePipeRebevelProcess(PipeRebevelRecord pipeRebevelRecord, HttpServletRequest request, HttpServletResponse response){
        JSONObject json=new JSONObject();
        System.out.println("savePipeRebevelProcess");
        try{
            String operationtime= request.getParameter("operation-time");
            int resTotal=0;
            if(operationtime!=null&&operationtime!=""){
                SimpleDateFormat simFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date new_optime = simFormat.parse(operationtime);
                pipeRebevelRecord.setOperation_time(new_optime);
            }else{
                pipeRebevelRecord.setOperation_time(new Date());
            }
            String pipeno=pipeRebevelRecord.getPipe_no();
            if(pipeRebevelRecord.getId()==0){
                //添加
                //System.out.println("11111111");
                List<HashMap<String,Object>>list =pipeBasicInfoDao.getPipeInfoByNo(pipeRebevelRecord.getPipe_no());
                if(list.size()>0) {
                    String rebevelmark = (String) list.get(0).get("rebevel_mark");
                    if (rebevelmark!=null&&rebevelmark.equals("1")) {
                        List<PipeRebevelRecord> oldlist = pipeRebevelRecordDao.getRecentRecordByPipeNo(pipeRebevelRecord.getPipe_no());
                        if (oldlist != null && oldlist.size() > 0 && oldlist.get(0).getResult().equals("10")) {
                            //存在一条pending数据，不给予insert处理
                        } else {
                            resTotal=pipeRebevelRecordDao.addPipeRebevelRecord(pipeRebevelRecord);
                        }
                    }
                }

            }else{
                //修改！
                resTotal=pipeRebevelRecordDao.updatePipeRebevelRecord(pipeRebevelRecord);
            }
            if(resTotal>0){
                //更新管子的状态
                List<PipeBasicInfo> list=pipeBasicInfoDao.getPipeNumber(pipeno);
                if(list.size()>0) {
                    PipeBasicInfo p = list.get(0);
                    //倒棱成功，清楚倒棱标志位
                    if (pipeRebevelRecord.getResult() != null && pipeRebevelRecord.getResult().equals("1")) {
                        p.setRebevel_mark("0");
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
    @RequestMapping("/delPipeRebevelProcess")
    public String delPipeRebevelProcess(@RequestParam(value = "hlparam")String hlparam, HttpServletResponse response)throws Exception{
        String[]idArr=hlparam.split(",");
        int resTotal=0;
        resTotal=pipeRebevelRecordDao.delPipeRebevelRecord(idArr);
        JSONObject json=new JSONObject();
        StringBuilder sbmessage = new StringBuilder();
        sbmessage.append("总共");
        sbmessage.append(Integer.toString(resTotal));
        sbmessage.append("项倒棱信息删除成功\n");
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



//    @RequestMapping("/editPipeRebevelProcess")
//    public  String editBarePipeGrindingProcess(PipeRebevelRecord pipeRebevelRecord){
//        System.out.println(pipeRebevelRecord);
//        pipeRebevelRecordDao.updatePipeRebevelRecord(pipeRebevelRecord);
//        return "grinding/barePipeGrindingCutoffRecord";
//    }



    @RequestMapping(value = "/getAllPipeRebevelByLike")
    @ResponseBody
    public String getAllPipeRebevelByLike(@RequestParam(value = "pipe_no",required = false)String pipe_no, @RequestParam(value = "operator_no",required = false)String operator_no, @RequestParam(value = "begin_time",required = false)String begin_time, @RequestParam(value = "end_time",required = false)String end_time, HttpServletRequest request){
        String page= request.getParameter("page");
        String rows= request.getParameter("rows");
        System.out.print("getAllPipeRebevelByLike:");
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
        List<HashMap<String,Object>>list=pipeRebevelRecordDao.getAllByLike(pipe_no,operator_no,beginTime,endTime,start,Integer.parseInt(rows));
        int count=pipeRebevelRecordDao.getCountAllByLike(pipe_no,operator_no,beginTime,endTime);
        Map<String,Object> maps=new HashMap<String,Object>();
        maps.put("total",count);
        maps.put("rows",list);
        String mmp= JSONArray.toJSONString(maps);
        System.out.print("mmp:"+mmp);
        return mmp;
    }

    //得到可以钢管最新的待定的倒棱记录  最后一条记录且result为待定 10
    @RequestMapping(value = "/getPendingRecordByPipeNo")
    @ResponseBody
    public String getPendingRecordByPipeNo(@RequestParam(value = "pipe_no",required = false)String pipe_no, HttpServletRequest request) {

        List<PipeRebevelRecord> list=pipeRebevelRecordDao.getRecentRecordByPipeNo(pipe_no);
        Map<String, Object> maps = new HashMap<String, Object>();
        if(list.size()>0) {
            PipeRebevelRecord record=list.get(0);
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
