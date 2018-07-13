package com.htcsweb.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.htcsweb.dao.ContractInfoDao;
import com.htcsweb.dao.DynamicMeasurementItemDao;
import com.htcsweb.dao.InspectionTimeRecordDao;
import com.htcsweb.dao.PipeBasicInfoDao;
import com.htcsweb.entity.*;
import com.htcsweb.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/DynamicItemOperation")
public class DynamicMeasurementItemController {

    @Autowired
    DynamicMeasurementItemDao dynamicMeasurementItemDao;

    @Autowired
    InspectionTimeRecordDao inspectionTimeRecordDao;

    @Autowired
    PipeBasicInfoDao pipeBasicInfoDao;

    @Autowired
    ContractInfoDao contractInfoDao;


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


    private String InitStencil_content(String processCode,String pipeNo,String stencilContent){

        //设置外防或内防喷标
        if(processCode!=null&&(processCode.equals("od_stencil")||processCode.equals("id_stencil"))){
            List<HashMap<String,Object>> pipelist= pipeBasicInfoDao.getPipeInfoByNo(pipeNo);
            if(pipelist.size()>0){
                float od=(float)pipelist.get(0).get("od");
                float wt=(float)pipelist.get(0).get("wt");
                String grade=(String)pipelist.get(0).get("grade");
                String contract_no=(String)pipelist.get(0).get("contract_no");
                String coating_standard=(String)pipelist.get(0).get("coating_standard");
                String client_spec=(String)pipelist.get(0).get("client_spec");
                String project_name=(String)pipelist.get(0).get("project_name");
                float p_length=(float)pipelist.get(0).get("p_length");
                float halflength=p_length*0.5f;
                String heat_no=(String)pipelist.get(0).get("heat_no");
                String pipe_making_lot_no=(String)pipelist.get(0).get("pipe_making_lot_no");
                float kg=(float)pipelist.get(0).get("weight")*1000;
                Date od_coating_date=(Date)pipelist.get(0).get("od_coating_date");
                String od_coating_dateString="";
                if(od_coating_date!=null) {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    od_coating_dateString = formatter.format(od_coating_date);
                }
                Date id_coating_date=(Date)pipelist.get(0).get("id_coating_date");
                String id_coating_dateString="";
                if(id_coating_date!=null) {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    id_coating_dateString = formatter.format(id_coating_date);
                }

            //替换
            stencilContent = stencilContent.replace("[OD]", String.valueOf(od));
            stencilContent = stencilContent.replace("[WT]", String.valueOf(wt));
            stencilContent = stencilContent.replace("[GRADE]", grade);
            stencilContent = stencilContent.replace("[CONTRACTNO]", contract_no);
            stencilContent = stencilContent.replace("[COATINGSPEC]", coating_standard);
            stencilContent = stencilContent.replace("[CLIENTSPEC]", client_spec);
            stencilContent = stencilContent.replace("[PROJECTNAME]", project_name);
            stencilContent = stencilContent.replace("[PIPENO]", pipeNo);
            stencilContent = stencilContent.replace("[PIPELENGTH]", String.valueOf(p_length));
            stencilContent = stencilContent.replace("[HALFLENGTH]", String.valueOf(halflength));
            stencilContent = stencilContent.replace("[HEATNO]",heat_no);
            stencilContent = stencilContent.replace("[BATCHNO]",pipe_making_lot_no);
            stencilContent = stencilContent.replace("[WEIGHT]",String.valueOf(kg));
            if(processCode.equals("od_stencil"))
                stencilContent = stencilContent.replace("[COATINGDATE]",od_coating_dateString);
            if(processCode.equals("id_stencil"))
                stencilContent = stencilContent.replace("[COATINGDATE]",id_coating_dateString);


            }

        }
        return stencilContent;
    }

    //获得动态检测项列表及检测值，根据管号，工序号，表单编号
    @RequestMapping("/getDynamicItemByPipeNoProcessCodeHeaderCode")
    @ResponseBody
    public String getDynamicItemByPipeNoProcessCodeHeaderCode(HttpServletRequest request) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        JSONObject json=new JSONObject();
        Map<String,Object> resultmaps=new HashMap<String,Object>();
        String pipe_no=request.getParameter("pipe_no");
        String process_code=request.getParameter("process_code");
        String mill_no=request.getParameter("mill_no");
        String inspection_process_record_header_code=request.getParameter("inspection_process_record_header_code");

        System.out.println("pipe_no="+pipe_no);
        System.out.println("process_code="+process_code);
        System.out.println("inspection_process_record_header_code="+inspection_process_record_header_code);

        List<HashMap<String, Object>> itemlist=new ArrayList<>();

        if(pipe_no!=null&&!pipe_no.equals("")&&process_code!=null&&!process_code.equals("")) {
            if (inspection_process_record_header_code == null || inspection_process_record_header_code.equals("")) {

                itemlist=dynamicMeasurementItemDao.getDynamicItemByPipeNoProcessCodeReturnMap(pipe_no, process_code);

                if(process_code.equals("od_stencil")) {//色环颜色初始化一下
                    String newStencilvalue = "";
                    for(int i=0;i<itemlist.size();i++) {
                        HashMap<String, Object> mp=itemlist.get(i);
                        if (mp != null && mp.get("item_code") != null &&mp.get("item_code").equals("center_line_color") ) {
                            ContractInfo contract=contractInfoDao.getContractInfoByPipeNo(pipe_no);
                            if(contract!=null){
                                mp.put("default_value",contract.getCenter_line_color());
                                itemlist.set(i,mp);
                            }

                        }else if(mp != null && mp.get("item_code") != null && mp.get("item_code").equals("pipe_end_color")){
                            ContractInfo contract=contractInfoDao.getContractInfoByPipeNo(pipe_no);
                            if(contract!=null){
                                mp.put("default_value",contract.getPipe_end_color());
                                itemlist.set(i,mp);
                            }
                        }
                    }

                }



//                List<DynamicMeasurementItem> tmplist = dynamicMeasurementItemDao.getDynamicItemByPipeNoProcessCode(pipe_no, process_code);
//                //mmp= JSONArray.toJSONString(list);
//
//                    for(int i=0;i<tmplist.size();i++){
//                        DynamicMeasurementItem item=tmplist.get(i);
//                            Field[] field = item.getClass().getDeclaredFields();
//                            HashMap <String,Object> map=new HashMap <String,Object>();
//                            for (int j = 0; j < field.length; j++) {     //遍历所有属性
//                                String name = field[j].getName();
//                                name = name.substring(0, 1).toUpperCase() + name.substring(1); //将属性的首字符大写，方便构造get，set方法
//                                Method m = null;
//                                m = item.getClass().getMethod("get" + name);
//
//                                String type = field[j].getGenericType().toString();    //获取属性的类型
//
//                                if (type.equals("class java.lang.String")) {
//                                    String value = (String) m.invoke(item);    //调用getter方法获取属性值
//                                    if (value != null) {
//                                        System.out.println("attribute value:" + value);
//                                    }
//
//                                    //替换喷标内容
//                                    String newStencilvalue="";
//                                    if(name!=null&&value!=null&&name.equals("item_code")&&(value.equals("od_stencil_content")||value.equals("id_stencil_content"))) {
//                                        newStencilvalue = InitStencil_content(process_code, pipe_no, item.getDefault_value());
//                                        System.out.println("stencil_content="+newStencilvalue);
//                                    }
//
//                                    if(!newStencilvalue.equals("")) {
//                                        map.put("item_value",newStencilvalue);
//                                    }
//                                    map.put(name,value);
//                                }
//                                else if(type.equals("class java.lang.Integer")) {
//                                    Integer value = (Integer) m.invoke(item);
//                                    if (value != null)
//                                    {
//                                        System.out.println("attribute value:" + value);
//                                    }
//                                    map.put(name,value);
//                                }
//                            }
//                        list.add(map);
//
//                        }



            } else {
                itemlist = dynamicMeasurementItemDao.getDynamicItemByPipeNoProcessCodeHeaderCode(pipe_no, process_code, inspection_process_record_header_code);
                //mmp= JSONArray.toJSONString(list);

            }


            //初始化喷标模版
            //替换喷标内容
            if(process_code.equals("od_stencil")||process_code.equals("id_stencil")) {
                String newStencilvalue = "";
                for(int i=0;i<itemlist.size();i++) {
                    HashMap<String, Object> mp=itemlist.get(i);
                    if (mp != null && mp.get("item_code") != null &&(mp.get("item_code").equals("od_stencil_content")  || mp.get("item_code").equals("id_stencil_content"))) {
                        newStencilvalue = InitStencil_content(process_code, pipe_no, mp.get("default_value").toString());
                        System.out.println("stencil_content=" + newStencilvalue);
                        mp.put("item_value",newStencilvalue);
                        itemlist.set(i,mp);
                    }
                }

            }
            resultmaps.put("record", itemlist);

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
