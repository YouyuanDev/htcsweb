package com.htcsweb.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.htcsweb.dao.PipeBasicInfoDao;
import com.htcsweb.dao.RawMaterialTesting3LpeDao;
import com.htcsweb.entity.RawMaterialTesting3Lpe;
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
@RequestMapping("/RawMaterialTesting3LpeOperation")
public class RawMaterialTesting3LpeController {
    @Autowired
    private RawMaterialTesting3LpeDao rawMaterialTesting3LpeDao;
    @Autowired
    private PipeBasicInfoDao pipeBasicInfoDao;

    //查询
    @RequestMapping(value = "/getRawMaterialTest3LpeByLike")
    @ResponseBody
    public String getRawMaterialTest3LpeByLike(@RequestParam(value = "project_no",required = false)String project_no, @RequestParam(value = "operator_no",required = false)String operator_no, @RequestParam(value = "begin_time",required = false)String begin_time, @RequestParam(value = "end_time",required = false)String end_time, HttpServletRequest request){
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
        List<HashMap<String,Object>> list=rawMaterialTesting3LpeDao.getNewAllByLike(project_no,operator_no,beginTime,endTime,start,Integer.parseInt(rows));
        int count=rawMaterialTesting3LpeDao.getCountNewAllByLike(project_no,operator_no,beginTime,endTime);
        Map<String,Object> maps=new HashMap<String,Object>();
        maps.put("total",count);
        maps.put("rows",list);
        String mmp= JSONArray.toJSONString(maps);
        return mmp;
    }

    //添加、修改
    @RequestMapping("/saveRawMaterialTest3Lpe")
    @ResponseBody
    public String saveRawMaterialTest3Lpe(RawMaterialTesting3Lpe rawMaterialTesting3Lpe, HttpServletRequest request, HttpServletResponse response){
        JSONObject json=new JSONObject();
        try{
            int resTotal=0;
            if(rawMaterialTesting3Lpe.getOperation_time()==null){
                rawMaterialTesting3Lpe.setOperation_time(new Date());
            }
            String msg="";
            if(rawMaterialTesting3Lpe.getId()==0){
                //添加
                RawMaterialTesting3Lpe  oldrecord = rawMaterialTesting3LpeDao.getRecentRecordBySampleNo(rawMaterialTesting3Lpe.getSample_no());
                if (oldrecord != null && oldrecord.getResult().equals("10")) {
                    //存在一条pending数据，不给予insert处理
                    msg="已存在待定记录,不能新增记录";
                } else {
                    resTotal=rawMaterialTesting3LpeDao.addRawMaterialTest3Lpe(rawMaterialTesting3Lpe);
                }

            }else{
                //修改！
                resTotal=rawMaterialTesting3LpeDao.updateRawMaterialTest3Lpe(rawMaterialTesting3Lpe);
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
    @RequestMapping("/delRawMaterialTest3Lpe")
    public String delRawMaterialTest3Lpe(@RequestParam(value = "hlparam")String hlparam,HttpServletResponse response)throws Exception{
        String[]idArr=hlparam.split(",");
        int resTotal=0;
        resTotal=rawMaterialTesting3LpeDao.delRawMaterialTest3Lpe(idArr);
        JSONObject json=new JSONObject();
        StringBuilder sbmessage = new StringBuilder();
        sbmessage.append("总共");
        sbmessage.append(Integer.toString(resTotal));
        sbmessage.append("项3LPE原材料信息删除成功\n");
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

    //最新的待定的Raw material 3LPE记录  最后一条记录且result为待定 10
//    @RequestMapping(value = "/getPendingRecordBySampleNo")
//    @ResponseBody
//    public String getPendingRecordBySampleNo(@RequestParam(value = "sample_no",required = false)String sample_no, HttpServletRequest request) {
//
//        RawMaterialTesting3Lpe record=rawMaterialTesting3LpeDao.getRecentRecordBySampleNo(sample_no);
//        Map<String, Object> maps = new HashMap<String, Object>();
//
//        if (record!=null&&record.getResult().equals("10")) {
//            //是待定状态
//            maps.put("success", true);
//            maps.put("record", record);
//        } else {
//            maps.put("success", false);
//        }
//
//        String mmp= JSONArray.toJSONString(maps);
//        return mmp;
//
//    }
    //根据id取出数据
    @RequestMapping(value = "/getRawMaterialTesting3LpeById")
    @ResponseBody
    public String getRawMaterialTesting3LpeById(HttpServletRequest request) {
        String idStr=request.getParameter("id");
        Map<String, Object> maps = new HashMap<String, Object>();
        if(idStr!=null&&!idStr.equals("")){
            RawMaterialTesting3Lpe record=rawMaterialTesting3LpeDao.getRawMaterialTesting3LpeById(Integer.parseInt(idStr));
            if (record!=null) {
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
