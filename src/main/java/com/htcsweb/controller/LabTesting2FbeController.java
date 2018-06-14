package com.htcsweb.controller;



import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.htcsweb.dao.LabTesting2FbeDao;
import com.htcsweb.dao.PipeBasicInfoDao;
import com.htcsweb.entity.LabTesting2Fbe;
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
@RequestMapping("/LabTest2FbeOperation")
public class LabTesting2FbeController {
    @Autowired
    private LabTesting2FbeDao labTesting2FbeDao;
    @Autowired
    private PipeBasicInfoDao pipeBasicInfoDao;

    //查询
    @RequestMapping(value = "/getLabTest2FbeByLike")
    @ResponseBody
    public String getLabTest2FbeByLike(@RequestParam(value = "pipe_no",required = false)String pipe_no, @RequestParam(value = "operator_no",required = false)String operator_no, @RequestParam(value = "begin_time",required = false)String begin_time, @RequestParam(value = "end_time",required = false)String end_time, HttpServletRequest request){
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
            }
            if(end_time!=null&&end_time!=""){
                endTime=sdf.parse(end_time);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        int start=(Integer.parseInt(page)-1)*Integer.parseInt(rows);
        List<HashMap<String,Object>> list=labTesting2FbeDao.getNewAllByLike(pipe_no,operator_no,beginTime,endTime,start,Integer.parseInt(rows));
        int count=labTesting2FbeDao.getCountNewAllByLike(pipe_no,operator_no,beginTime,endTime);
        Map<String,Object> maps=new HashMap<String,Object>();
        maps.put("total",count);
        maps.put("rows",list);
        String mmp= JSONArray.toJSONString(maps);
        return mmp;
    }

    //添加、修改
    @RequestMapping("/saveLabTest2Fbe")
    @ResponseBody
    public String saveLabTest2Fbe(LabTesting2Fbe labTesting2Fbe, HttpServletRequest request, HttpServletResponse response){
        JSONObject json=new JSONObject();
        try{
            String odbptime= request.getParameter("odbptime");
            String coatingdate= request.getParameter("coatingdate");
            int resTotal=0;
            if(odbptime!=null&&odbptime!=""){
                SimpleDateFormat simFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date new_odbptime = simFormat.parse(odbptime);
                labTesting2Fbe.setOperation_time(new_odbptime);
            }else{
                labTesting2Fbe.setOperation_time(new Date());
            }
            if(coatingdate!=null&&coatingdate!=""){
                SimpleDateFormat simFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date new_coatingdate = simFormat.parse(coatingdate);
                labTesting2Fbe.setCoating_date(new_coatingdate);
            }else{
                labTesting2Fbe.setCoating_date(new Date());
            }
            String msg="";
            if(labTesting2Fbe.getId()==0){
                //添加

                LabTesting2Fbe  oldrecord = labTesting2FbeDao.getRecentRecordByPipeNo(labTesting2Fbe.getPipe_no());
                if (oldrecord != null && oldrecord.getResult().equals("10")) {
                    //存在一条pending数据，不给予insert处理
                    msg="已存在待定记录,不能新增记录";
                } else {
                    resTotal=labTesting2FbeDao.addLabTest2Fbe(labTesting2Fbe);
                }


            }else{
                //修改！
                resTotal=labTesting2FbeDao.updateLabTest2Fbe(labTesting2Fbe);
            }
            if(resTotal>0){
                json.put("success",true);
                json.put("message","保存成功");
            }else{
                json.put("success",false);
                json.put("message","保存失败，"+msg);
            }

        }catch (Exception e){
            json.put("success",false);
            e.printStackTrace();
        }finally {
            try{
                ResponseUtil.write(response,json);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }
    //删除
    @RequestMapping("/delLabTest2Fbe")
    public String delLabTest2Fbe(@RequestParam(value = "hlparam")String hlparam,HttpServletResponse response)throws Exception{
        String[]idArr=hlparam.split(",");
        int resTotal=0;
        resTotal=labTesting2FbeDao.delLabTest2Fbe(idArr);
        JSONObject json=new JSONObject();
        StringBuilder sbmessage = new StringBuilder();
        sbmessage.append("总共");
        sbmessage.append(Integer.toString(resTotal));
        sbmessage.append("项2FBE实验信息删除成功\n");
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

    //得到可以钢管最新的待定的Lab 2fbe记录  最后一条记录且result为待定 10
    @RequestMapping(value = "/getPendingRecordByPipeNo")
    @ResponseBody
    public String getPendingRecordByPipeNo(@RequestParam(value = "pipe_no",required = false)String pipe_no, HttpServletRequest request) {

        LabTesting2Fbe record=labTesting2FbeDao.getRecentRecordByPipeNo(pipe_no);
        Map<String, Object> maps = new HashMap<String, Object>();

        if (record!=null&&record.getResult().equals("10")) {
            //是待定状态
            maps.put("success", true);
            maps.put("record", record);
        } else {
            maps.put("success", false);
        }

        String mmp= JSONArray.toJSONString(maps);
        return mmp;

    }
}
