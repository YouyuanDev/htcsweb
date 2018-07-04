package com.htcsweb.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.htcsweb.dao.DynamicMeasurementItemDao;
import com.htcsweb.dao.InspectionTimeRecordDao;
import com.htcsweb.entity.AcceptanceCriteria;
import com.htcsweb.entity.CoatingRepair;
import com.htcsweb.entity.DynamicMeasurementItem;
import com.htcsweb.entity.InspectionTimeRecord;
import com.htcsweb.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
@RequestMapping("/DynamicItemOperation")
public class DynamicMeasurementItemController {

    @Autowired
    DynamicMeasurementItemDao dynamicMeasurementItemDao;

    @Autowired
    InspectionTimeRecordDao inspectionTimeRecordDao;


    //获得动态检测项列表，根据ACNo
    @RequestMapping("/getDynamicItemByACNo")
    @ResponseBody
    public String getDynamicItemByACNo(HttpServletRequest request){
        JSONObject json=new JSONObject();

        String acceptance_criteria_no=request.getParameter("acceptance_criteria_no");
        List<DynamicMeasurementItem> list=dynamicMeasurementItemDao.getDynamicMeasurementItemByAcceptanceCriteriaNo(acceptance_criteria_no);

        String mmp= JSONArray.toJSONString(list);
        return mmp;
    }



    //获得动态检测项列表，根据ACNo
    @RequestMapping("/getDynamicItemWithProcessInfoByACNo")
    @ResponseBody
    public String getDynamicItemWithProcessInfoByACNo(HttpServletRequest request){
        JSONObject json=new JSONObject();

        String acceptance_criteria_no=request.getParameter("acceptance_criteria_no");
        List<HashMap<String,Object>> list=dynamicMeasurementItemDao.getDynamicMeasurementItemWithProcessInfoByAcceptanceCriteriaNo(acceptance_criteria_no);

        String mmp= JSONArray.toJSONString(list);
        return mmp;
    }




    //获得动态检测项列表，根据管号，工序号
    @RequestMapping("/getDynamicItemByPipeNoProcessCode")
    @ResponseBody
    public String getDynamicItemByPipeNoProcessCode(HttpServletRequest request){
        JSONObject json=new JSONObject();

        String pipe_no=request.getParameter("pipe_no");
        String process_code=request.getParameter("process_code");

        System.out.println("pipe_no="+pipe_no);
        System.out.println("process_code="+process_code);

        List<DynamicMeasurementItem> list=dynamicMeasurementItemDao.getDynamicItemByPipeNoProcessCode(pipe_no,process_code);

        String mmp= JSONArray.toJSONString(list);
        System.out.println(mmp);
        return mmp;
    }


    //获得动态检测项列表及检测值，根据管号，工序号，表单编号
    @RequestMapping("/getDynamicItemByPipeNoProcessCodeHeaderCode")
    @ResponseBody
    public String getDynamicItemByPipeNoProcessCodeHeaderCode(HttpServletRequest request){
        JSONObject json=new JSONObject();
        Map<String,Object> resultmaps=new HashMap<String,Object>();
        String pipe_no=request.getParameter("pipe_no");
        String process_code=request.getParameter("process_code");
        String mill_no=request.getParameter("mill_no");
        String inspection_process_record_header_code=request.getParameter("inspection_process_record_header_code");

        System.out.println("pipe_no="+pipe_no);
        System.out.println("process_code="+process_code);
        System.out.println("inspection_process_record_header_code="+inspection_process_record_header_code);

        if(pipe_no!=null&&!pipe_no.equals("")&&process_code!=null&&!process_code.equals("")) {
            if (inspection_process_record_header_code == null || inspection_process_record_header_code.equals("")) {
                List<DynamicMeasurementItem> list = dynamicMeasurementItemDao.getDynamicItemByPipeNoProcessCode(pipe_no, process_code);
                //mmp= JSONArray.toJSONString(list);
                resultmaps.put("record", list);
            } else {
                List<HashMap<String, Object>> list = dynamicMeasurementItemDao.getDynamicItemByPipeNoProcessCodeHeaderCode(pipe_no, process_code, inspection_process_record_header_code);
                //mmp= JSONArray.toJSONString(list);
                resultmaps.put("record", list);
            }

            //获取检测频率记录
            if(mill_no!=null&&!mill_no.equals("")) {
                List<InspectionTimeRecord> recordlist = inspectionTimeRecordDao.getRecordByPipeNoMillNo(pipe_no, mill_no, null);

                List<DynamicMeasurementItem> dynamiclist = dynamicMeasurementItemDao.getDynamicItemByPipeNoProcessCode(pipe_no, process_code);
                List<HashMap<String,Object>> list=new ArrayList<HashMap<String,Object>>();
                Date now = new Date();
                for (int i = 0; i < dynamiclist.size(); i++) {
                    DynamicMeasurementItem item = dynamiclist.get(i);
                    boolean needInspectNow = true;
                    String lastInspectionTime = "";
                    HashMap<String, Object> m = new HashMap<String, Object>();
                    for (int j = 0; j < recordlist.size(); j++) {
                        InspectionTimeRecord timeRecord = recordlist.get(j);
                        if (item.getItem_code().equals(timeRecord.getInspection_item())) {
                            //找到检验记录了
                            //检验频率 秒
                            float freq = Float.parseFloat(item.getItem_frequency());
                            float freqSec = freq * 60 * 60;
                            lastInspectionTime = timeRecord.getInspction_time().toString();
                            //间隔秒
                            long interval = (now.getTime() - timeRecord.getInspction_time().getTime()) / 1000;
                            if (interval < freqSec) {
                                //间隔小于检验频率，不需要检验
                                needInspectNow = false;
                            }
                            break;
                        }
                    }
                    m.put("lastInspectionTime", lastInspectionTime);
                    m.put("needInspectNow", needInspectNow);
                    m.put("InspectionItem", item.getItem_code());
                    list.add(m);
                }
                resultmaps.put("needInspectionInfo",list);
            }
        }



        String mmp= JSONArray.toJSONString(resultmaps);

        System.out.println(mmp);
        return mmp;
    }






    @RequestMapping("/importDynamicItem")
    @ResponseBody
    public String importDynamicItem(HttpServletRequest request) {
        JSONObject json = new JSONObject();
        String src_acceptance_criteria_no=request.getParameter("src_acceptance_criteria_no");
        String des_acceptance_criteria_no=request.getParameter("des_acceptance_criteria_no");
        List<DynamicMeasurementItem> list=dynamicMeasurementItemDao.getDynamicMeasurementItemByAcceptanceCriteriaNo(src_acceptance_criteria_no);
        System.out.println("importDynamicItem src_acceptance_criteria_no="+src_acceptance_criteria_no);
        System.out.println("importDynamicItem des_acceptance_criteria_no="+des_acceptance_criteria_no);
        int totalcount=0;
        for(int i=0;i<list.size();i++) {
            DynamicMeasurementItem item=list.get(i);
            //过滤掉特殊检测项
            System.out.println("getIs_special_item="+item.getIs_special_item());
            if(item.getIs_special_item()!=null&&item.getIs_special_item().equals("1"))
                continue;

            //初始化自己的参数
            item.setId(0);
            item.setAcceptance_criteria_no(des_acceptance_criteria_no);
            String uuid = UUID.randomUUID().toString();
            item.setItem_code("IT"+uuid);
            int count=dynamicMeasurementItemDao.addDynamicMeasurementItem(item);
            totalcount+=count;
        }
        if(totalcount>0) {
            json.put("success", true);
            json.put("message", "成功导入检测项"+String.valueOf(totalcount)+"项");
            json.put("des_acceptance_criteria_no", des_acceptance_criteria_no);
        }
        else{
            json.put("success", false);
            json.put("message", "没有检测项可导入");
        }

        String mmp= JSONArray.toJSONString(json);
        return mmp;

    }





            //添加、修改
    @RequestMapping("/saveDynamicItem")
    @ResponseBody
    public String saveDynamicItem(DynamicMeasurementItem dynamicMeasurementItem, HttpServletRequest request, HttpServletResponse response){
        JSONObject json=new JSONObject();
        try{
            int resTotal=0;
            if(dynamicMeasurementItem.getId()==0){
                //添加
                String uuid = UUID.randomUUID().toString();
                dynamicMeasurementItem.setItem_code("IT"+uuid);
                resTotal=dynamicMeasurementItemDao.addDynamicMeasurementItem(dynamicMeasurementItem);
            }else{
                //修改！
                resTotal=dynamicMeasurementItemDao.updateDynamicMeasurementItem(dynamicMeasurementItem);

            }
            if(resTotal>0){
                json.put("success",true);
                json.put("message","保存成功");
                json.put("acceptance_criteria_no",dynamicMeasurementItem.getAcceptance_criteria_no());

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
    @RequestMapping("/delDynamicItem")
    public String delDynamicItem(@RequestParam(value = "hlparam")String hlparam, HttpServletResponse response)throws Exception{
        String[]idArr=hlparam.split(",");
        int resTotal=0;
        resTotal=dynamicMeasurementItemDao.delDynamicMeasurementItem(idArr);
        JSONObject json=new JSONObject();
        StringBuilder sbmessage = new StringBuilder();
        sbmessage.append("总共");
        sbmessage.append(Integer.toString(resTotal));
        sbmessage.append("项检验项删除成功\n");
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

        //DynamicItemOperation/getAllInspectionTimeMapByPipeNoMillNo.action?pipe_no=1524540&mill_no=mill_1
        String pipe_no=request.getParameter("pipe_no");
        String mill_no=request.getParameter("mill_no");
        String process_code=request.getParameter("process_code");

        List<HashMap<String,Object>> list=new ArrayList<HashMap<String,Object>>();

        if(pipe_no!=null&&!pipe_no.equals("")&&mill_no!=null&&!mill_no.equals("")) {
            List<InspectionTimeRecord> recordlist = inspectionTimeRecordDao.getRecordByPipeNoMillNo(pipe_no, mill_no, null);
            //List<HashMap<String,Object>> ltif= inspectionFrequencyDao.getFrequencyInfoByPipeNo(pipe_no);

            List<DynamicMeasurementItem> dynamiclist = dynamicMeasurementItemDao.getDynamicItemByPipeNoProcessCode(pipe_no, process_code);


            Date now = new Date();

            //System.out.println("11111111111ltif.size()"+ltif.size());
            //System.out.println("222222lt.size()"+lt.size());//这里出错了 可能没记录

            for (int i = 0; i < dynamiclist.size(); i++) {
                DynamicMeasurementItem item = dynamiclist.get(i);
                boolean needInspectNow = true;
                String lastInspectionTime = "";
                HashMap<String, Object> m = new HashMap<String, Object>();
                for (int j = 0; j < recordlist.size(); j++) {
                    InspectionTimeRecord timeRecord = recordlist.get(j);
                    if (item.getItem_code().equals(timeRecord.getInspection_item())) {
                        //找到检验记录了
                        //检验频率 秒
                        float freq = Float.parseFloat(item.getItem_frequency());
                        float freqSec = freq * 60 * 60;
                        lastInspectionTime = timeRecord.getInspction_time().toString();
                        //间隔秒
                        long interval = (now.getTime() - timeRecord.getInspction_time().getTime()) / 1000;
                        if (interval < freqSec) {
                            //间隔小于检验频率，不需要检验
                            needInspectNow = false;
                        }
                        break;
                    }
                }
                m.put("lastInspectionTime", lastInspectionTime);
                m.put("needInspectNow", needInspectNow);
                m.put("InspectionItem", item.getItem_code());
                list.add(m);
            }
        }
        String mmp= JSONArray.toJSONString(list);
        //System.out.println(mmp);
        return mmp;
    }
    //获取内外喷标内容模板
    @RequestMapping("/getOdIdStencilContentModel")
    @ResponseBody
    public String getOdIdStencilContentModel(HttpServletRequest request){
        String contract_no=request.getParameter("contract_no");
        if(contract_no!=null&&contract_no!=""){
            List<HashMap<String,Object>> stencilContentModel=dynamicMeasurementItemDao.getOdIdStencilContentModel(contract_no);
            String map= JSONObject.toJSONString(stencilContentModel);
            return map;
        }else{
            return  null;
        }
    }
}
