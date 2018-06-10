package com.htcsweb.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.htcsweb.dao.InspectionFrequencyDao;
import com.htcsweb.dao.InspectionTimeRecordDao;
import com.htcsweb.entity.IdStencilProcess;
import com.htcsweb.entity.InspectionFrequency;
import com.htcsweb.entity.InspectionTimeRecord;
import com.htcsweb.entity.PipeBasicInfo;
import com.htcsweb.util.ComboxItem;
import com.htcsweb.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;


@Controller
@RequestMapping("/InspectionFrequencyOperation")
public class InspectionFrequencyController {


    @Autowired
    private InspectionFrequencyDao inspectionFrequencyDao;
    @Autowired
    private InspectionTimeRecordDao inspectionTimeRecordDao;


    //查找检验频率标准
    @RequestMapping("/getAllInspectionFrequencyByLike")
    @ResponseBody
    public String getAllInspectionFrequencyByLike(@RequestParam(value = "inspection_frequency_no",required = false)String inspection_frequency_no,HttpServletRequest request){
        String page= request.getParameter("page");
        String rows= request.getParameter("rows");
        if(page==null||page==""){
            page="0";
        }if(rows==null||rows==""){
            rows="20";
        }
        int start=(Integer.parseInt(page)-1)*Integer.parseInt(rows);
        List<HashMap<String,Object>>list=inspectionFrequencyDao.getAllByLike(inspection_frequency_no,start,Integer.parseInt(rows));
        int count=inspectionFrequencyDao.getCount(inspection_frequency_no);
        Map<String,Object> maps=new HashMap<String,Object>();
        maps.put("total",count);
        maps.put("rows",list);
        String mmp= JSONArray.toJSONString(maps);
        return mmp;
    }





    //查找所有检验频率,下拉框使用
    @RequestMapping("/getAllInspectionFrequency")
    @ResponseBody
    public String getAllInspectionFrequency(@RequestParam(value = "coating_acceptance_criteria_no",required = false)String coating_acceptance_criteria_no, HttpServletRequest request){
        List<InspectionFrequency> list=inspectionFrequencyDao.getAllInspectionFrequency();
        List<ComboxItem> colist=new ArrayList<ComboxItem>();
        for(int i=0;i<list.size();i++){
            ComboxItem citem= new ComboxItem();
            InspectionFrequency ps=((InspectionFrequency)list.get(i));
            citem.id=ps.getInspection_frequency_no();
            citem.text=ps.getInspection_frequency_no();
            colist.add(citem);
        }
        String map= JSONObject.toJSONString(colist);
        return map;
    }


    //添加、修改
    @RequestMapping("/saveInspectionFrequency")
    @ResponseBody
    public String saveInspectionFrequency(InspectionFrequency inspectionFrequency, HttpServletRequest request, HttpServletResponse response){
        JSONObject json=new JSONObject();
        try{
            int resTotal=0;


            if(inspectionFrequency.getId()==0){
                //添加
                resTotal=inspectionFrequencyDao.addInspectionFrequency(inspectionFrequency);
            }else{
                //修改！
                resTotal=inspectionFrequencyDao.updateInspectionFrequency(inspectionFrequency);
            }
            if(resTotal>0){
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
    //删除
    @RequestMapping("/delInspectionFrequency")
    public String delInspectionFrequency(@RequestParam(value = "hlparam")String hlparam,HttpServletResponse response)throws Exception{
        String[]idArr=hlparam.split(",");
        int resTotal=0;
        resTotal=inspectionFrequencyDao.delInspectionFrequency(idArr);
        JSONObject json=new JSONObject();
        StringBuilder sbmessage = new StringBuilder();
        sbmessage.append("总共");
        sbmessage.append(Integer.toString(resTotal));
        sbmessage.append("项检验频率信息删除成功\n");
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


    //检测某检测项此刻是否需要检测
    @RequestMapping("/getAllInspectionTimeMapByPipeNoMillNo")
    @ResponseBody
    public String getAllInspectionTimeMapByPipeNoMillNo( HttpServletRequest request){

        //InspectionFrequencyOperation/getAllInspectionTimeMapByPipeNoMillNo.action?pipe_no=1524540&mill_no=mill_1
        String pipe_no=request.getParameter("pipe_no");
        String mill_no=request.getParameter("mill_no");
        System.out.println("11111111111pipenono"+pipe_no);
        System.out.println("11111111111mill_no"+mill_no);
        List<InspectionTimeRecord> lt=inspectionTimeRecordDao.getRecordByPipeNoMillNo(pipe_no,mill_no,null);
        List<HashMap<String,Object>> ltif= inspectionFrequencyDao.getFrequencyInfoByPipeNo(pipe_no);
        Map<String,HashMap<String,Object>> maps=new HashMap<String,HashMap<String,Object>>();
        Date now=new Date();

        System.out.println("11111111111ltif.size()"+ltif.size());
        System.out.println("222222lt.size()"+lt.size());//这里出错了 可能没记录

        if(ltif.size()>0){
            HashMap<String,Object> insmap=new HashMap<String,Object>();
            insmap=ltif.get(0);

            for(int i=0;i<lt.size();i++){
                InspectionTimeRecord timeRecord=lt.get(i);
                float freq=(float)insmap.get(timeRecord.getInspection_item());
                //检验频率 秒
                float freqSec=freq*60*60;
                //间隔秒
                long interval = (now.getTime() - timeRecord.getInspction_time().getTime())/1000;

                boolean needInspectNow=false;
                if(interval>=freqSec){
                    //间隔已大于检验频率，需要检验
                    needInspectNow=true;
                }
                HashMap<String,Object> m=new HashMap<String,Object>();
                m.put("lastInspectionTime",timeRecord.getInspction_time());
                m.put("needInspectNow",needInspectNow);
                m.put("InspectionItem",timeRecord.getInspection_item());


                maps.put(timeRecord.getInspection_item(),m);
            }
        }



        String mmp= JSONArray.toJSONString(maps);
        //System.out.println(mmp);
        return mmp;
    }

    //检测某检测项此刻是否需要检测
    @RequestMapping("/getAllInspectionTimeMapByProjectNoMillNo")
    @ResponseBody
    public String getAllInspectionTimeMapByProjectNoMillNo(@RequestParam(value = "project_no",required = false)String project_no, @RequestParam(value = "mill_no",required = false)String mill_no, HttpServletRequest request){

        //InspectionFrequencyOperation/getAllInspectionTimeMapByProjectNoMillNo.action?project_no=20180208&mill_no=mill_1
        List<InspectionTimeRecord> lt=inspectionTimeRecordDao.getRecordByProjectNoMillNo(project_no,mill_no,null);
        List<HashMap<String,Object>> ltif= inspectionFrequencyDao.getFrequencyInfoByProjectNo(project_no);
        Map<String,HashMap<String,Object>> maps=new HashMap<String,HashMap<String,Object>>();
        HashMap<String,Object> insmap=new HashMap<String,Object>();
        Date now=new Date();
        if(ltif.size()>0){
             insmap=ltif.get(0);

            for(int i=0;i<lt.size();i++){
                InspectionTimeRecord timeRecord=lt.get(i);
                float freq=(float)insmap.get(timeRecord.getInspection_item());
                //检验频率 秒
                float freqSec=freq*60*60;
                //间隔秒
                long interval = (now.getTime() - timeRecord.getInspction_time().getTime())/1000;

                boolean needInspectNow=false;
                if(interval>=freqSec){
                    //间隔已大于检验频率，需要检验
                    needInspectNow=true;
                }
                HashMap<String,Object> m=new HashMap<String,Object>();
                m.put("lastInspectionTime",timeRecord.getInspction_time());
                m.put("needInspectNow",needInspectNow);
                maps.put(timeRecord.getInspection_item(),m);
            }
        }



        String mmp= JSONArray.toJSONString(maps);
        return mmp;
    }


//    //设置检测项检测时间
//    @RequestMapping("/setInspectionTimeMap")
//    @ResponseBody
//    public String setInspectionTimeMap(@RequestParam(value = "project_no",required = false)String project_no,@RequestParam(value = "mill_no",required = false)String mill_no,@RequestParam(value = "new_inspectionTimeMap",required = false)Map<String,Object> new_inspectionTimeMap, HttpServletRequest request){
//
//        int change=0;
//        //更新增量new_inspectionTimeMap
//        Iterator iter = new_inspectionTimeMap.entrySet().iterator();
//        while (iter.hasNext()) {
//            Map.Entry entry = (Map.Entry) iter.next();
//            Object key = entry.getKey();
//            Object val = entry.getValue();
//
//        }
//
//
//
//        Map<String,Object> result=new HashMap<String,Object>();
//        if(change>0) {
//            result.put("success", true);
//            result.put("message","insepctionTimeMapSession 设置成功");
//        }else{
//            result.put("success", false);
//            result.put("message","insepctionTimeMapSession 设置失败");
//        }
//
//        String mmp= JSONArray.toJSONString(result);
//        return mmp;
//    }


}
