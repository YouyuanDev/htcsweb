package com.htcsweb.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.htcsweb.dao.*;
import com.htcsweb.entity.*;
import com.htcsweb.util.APICloudPushService;
import com.htcsweb.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/InspectionProcessOperation")
public class InspectionProcess {


    //岗位检验通用表单operation

    @Autowired
    private PipeBasicInfoDao pipeBasicInfoDao;

    @Autowired
    private InspectionProcessRecordHeaderDao inspectionProcessRecordHeaderDao;

    @Autowired
    private ProcessInfoDao processInfoDao;

    @Autowired
    private InspectionProcessRecordItemDao inspectionProcessRecordItemDao;


    @Autowired
    private InspectionTimeRecordDao inspectionTimeRecordDao;

    @Autowired
    private RoleDao roleDao;




    @RequestMapping("/saveProcess")
    @ResponseBody
    public String saveProcess(InspectionProcessRecordHeader inspectionProcessRecordHeader, HttpServletRequest request, HttpServletResponse response){
        JSONObject json=new JSONObject();
        System.out.println("process_code="+inspectionProcessRecordHeader.getProcess_code());
        String dynamicJson=request.getParameter("dynamicJson");
        String outputJson=request.getParameter("outputJson");
        String inputStatusList=request.getParameter("inputStatusList");

        System.out.println("dynamicJson="+dynamicJson);
        System.out.println("outputJson="+outputJson);
        System.out.println("inputStatusList="+inputStatusList);

        JSONObject dynamicMap=null;
        JSONObject outputMap =null;
        JSONArray resultArray=null;
        //用于特殊的检验项 ，比如 是否是is_sample is_dsc_sample is_pe_sample is_glass_sample
        JSONArray additionParamArray=null;

        if(dynamicJson!=null) {
            dynamicMap = JSONObject.parseObject(dynamicJson);
        }
        if(outputJson!=null) {
            outputMap = JSONObject.parseObject(outputJson);
        }
        if(outputMap!=null) {
            resultArray = (JSONArray) outputMap.get("output");
            additionParamArray = (JSONArray) outputMap.get("additionParams");
        }


        String msg="";
        String result_name="";
        String result_name_en="";

        if(inspectionProcessRecordHeader.getInspection_process_record_header_code()==null||inspectionProcessRecordHeader.getInspection_process_record_header_code()==""){
            String uuid = UUID.randomUUID().toString();
            inspectionProcessRecordHeader.setInspection_process_record_header_code("IPRH"+uuid);
        }

        //System.out.println("id="+inspectionProcessRecordHeader.getId());

        try{
            int resTotal=0;
            if(inspectionProcessRecordHeader.getOperation_time()==null){
                inspectionProcessRecordHeader.setOperation_time(new Date());
            }

            String pipeno=inspectionProcessRecordHeader.getPipe_no();
            String mill_no=inspectionProcessRecordHeader.getMill_no();
            String project_no="";


            List<HashMap<String,Object>> list=pipeBasicInfoDao.getPipeInfoByNo(pipeno);
            String p_status="";
            if(list.size()>0) {
                p_status = (String) list.get(0).get("status");
            }

            boolean inputStatusVerified=false;
            if(inputStatusList!=null){
                String [] statusArr=inputStatusList.split(",");
                for(int i=0; i<statusArr.length;i++){
                    inputStatusVerified=statusArr[i].equals(p_status);
                    if(inputStatusVerified)break;
                }
            }


            if(inspectionProcessRecordHeader.getId()==0){
                //添加

                if(inputStatusVerified){
                    InspectionProcessRecordHeader oldrecord=inspectionProcessRecordHeaderDao.getRecentRecordByPipeNo(inspectionProcessRecordHeader.getProcess_code(),pipeno);
                    if(oldrecord!=null&&oldrecord.getResult().equals("10")){
                        //存在一条pending数据，不给予insert处理
                        msg="已存在待定记录,不能新增记录";
                    }else{
                        inspectionProcessRecordHeader.setInspection_process_record_header_code(inspectionProcessRecordHeader.getInspection_process_record_header_code());
                        resTotal=inspectionProcessRecordHeaderDao.addInspectionProcessRecordHeader(inspectionProcessRecordHeader);

                    }
                }
                project_no=(String)list.get(0).get("project_no");
                System.out.println("project_no="+project_no);


            }else{
                //修改！
                resTotal=inspectionProcessRecordHeaderDao.updateInspectionProcessRecordHeader(inspectionProcessRecordHeader);

            }
            if(resTotal>0){

                List<PipeBasicInfo> list1=pipeBasicInfoDao.getPipeNumber(pipeno);
                PipeBasicInfo p=null;
                if(list1.size()>0) {
                    p = list1.get(0);
                }

                //增加或者更新动态检测项
                for (String key:dynamicMap.keySet()) {
                        //更新原有动态检测项
                        InspectionProcessRecordItem recorditem=inspectionProcessRecordItemDao.getInspectionProcessRecordItemByHeaderCodeAndItemCode(inspectionProcessRecordHeader.getInspection_process_record_header_code(),key);
                        if(recorditem!=null) {
                            recorditem.setItem_value((String) dynamicMap.get(key));
                            inspectionProcessRecordItemDao.updateInspectionProcessRecordItem(recorditem);
                        }else{
                            InspectionProcessRecordItem item=new InspectionProcessRecordItem();
                            item.setId(0);
                            item.setInspection_process_record_header_code(inspectionProcessRecordHeader.getInspection_process_record_header_code());
                            item.setItem_code(key);
                            item.setItem_value((String)dynamicMap.get(key));
                            int addtotal=inspectionProcessRecordItemDao.addInspectionProcessRecordItem(item);

                            //更新检验时间
                            //更新增量 inspectionTimeMap
                            if(addtotal>0){
                                List<InspectionTimeRecord> lt=inspectionTimeRecordDao.getRecordByProjectNoMillNo(project_no,mill_no,key);
                                if(lt.size()>0) {
                                    InspectionTimeRecord itr=lt.get(0);
                                    itr.setInspction_time(inspectionProcessRecordHeader.getOperation_time());
                                    inspectionTimeRecordDao.updateInspectionTimeRecord(itr);
                                }else{
                                    InspectionTimeRecord itr=new InspectionTimeRecord();
                                    itr.setProject_no(project_no);
                                    itr.setMill_no(mill_no);
                                    itr.setInspection_item(key);
                                    itr.setInspction_time(inspectionProcessRecordHeader.getOperation_time());
                                    inspectionTimeRecordDao.addInspectionTimeRecord(itr);
                                }
                            }


                        }

                        //特殊检验项的处理
                    for(int i=0;additionParamArray!=null&&i<additionParamArray.size();i++) {
                        JSONObject additionParammap = (JSONObject) additionParamArray.get(i);
                        if (additionParammap != null) {
                            String addition_item_code = (String) additionParammap.get("addition_item_code");
                            String des_pipe_property_name = (String) additionParammap.get("des_pipe_property_name");
                            String set_value = (String) additionParammap.get("set_value");

                            if(key.equals(addition_item_code)){
                                String v=(String)dynamicMap.get(key);
                                if(set_value==null){
                                    //若没有给定的set_value值，则设置为表单输入项的值
                                    set_value=v;
                                }
                                //这里做各种个性化处理
                                if(des_pipe_property_name!=null){
                                    Class c = Class.forName("com.htcsweb.entity.PipeBasicInfo");
                                    Constructor con = c.getConstructor();
                                    Object obj = con.newInstance();
                                    obj=p;
                                    Field newname = c.getDeclaredField(des_pipe_property_name);
                                    newname.setAccessible(true);
                                    newname.set(obj, set_value);
                                    p=(PipeBasicInfo)obj;
                                    int statusRes = pipeBasicInfoDao.updatePipeBasicInfo(p);
                                    System.out.println("设置属性"+des_pipe_property_name+"="+set_value);
                                }
                            }
                        }
                    }


                }


                //更新管子的状态

                if(p!=null){
                    if(inputStatusVerified) {
                        for(int i=0;i<resultArray.size();i++){
                            JSONObject  rmap=(JSONObject)resultArray.get(i);
                            if(rmap!=null){
                                String tmp_result=(String)rmap.get("result");
                                result_name=(String)rmap.get("result_name");
                                result_name_en=(String)rmap.get("result_name_en");
                                String tmp_next_status=(String)rmap.get("next_status");
                                String tmp_last_status=(String)rmap.get("last_status");
                                //找到对应关系
                                if(inspectionProcessRecordHeader.getResult().equals(tmp_result)){
                                    if(tmp_next_status!=null&&!tmp_next_status.equals("last_status")){
                                        p.setStatus(tmp_next_status);
                                    }
                                    else if(tmp_next_status.equals("last_status")){
                                        p.setStatus(p.getLast_accepted_status());
                                    }
                                    if(tmp_last_status!=null)
                                        p.setLast_accepted_status(p.getStatus());
                                    int statusRes = pipeBasicInfoDao.updatePipeBasicInfo(p);
                                    break;
                                }

                            }

                        }

                    }

                }


                //发送事件推送

                 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                 Date nowtime=new Date();
                 String str_title=sdf.format(nowtime.getTime());
                 String str_content="工序："+inspectionProcessRecordHeader.getProcess_code()+",管号："+p.getPipe_no()+", "+result_name+"("+result_name_en+")";

                 SendEvent(inspectionProcessRecordHeader.getProcess_code(),str_title,str_content);

                System.out.println("推送title="+str_title);
                System.out.println("推送str_content="+str_content);


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

    //发送推送消息
    public void SendEvent(String event, String title,String content){
        List<HashMap<String,Object>>  lt=roleDao.getRolesByEvent(event);

        for(int i=0;i<lt.size();i++){
            String role=(String)lt.get(i).get("role_no");
            //发消息
            APICloudPushService.SendPushNotification("",title,content,"1","0",role,"");
        }


    }



    @RequestMapping("/delProcess")
    public String delProcess(HttpServletRequest request, HttpServletResponse response)throws Exception{

        String hlparam=request.getParameter("hlparam");
        System.out.println("hlparam"+hlparam);
        String[] idArr={};
        if(hlparam!=null) {
            idArr = hlparam.split(",");
        }

        //删除表单
        int resTotal=0;
        resTotal=inspectionProcessRecordHeaderDao.delInspectionProcessRecordHeader(idArr);
        JSONObject json=new JSONObject();
        StringBuilder sbmessage = new StringBuilder();
        sbmessage.append("总共");
        sbmessage.append(Integer.toString(resTotal));
        sbmessage.append("项工序检验信息删除成功\n");
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



    ///搜索岗位工序检验信息

    @RequestMapping(value = "/getProcessByLike")
    @ResponseBody
    public String getProcessByLike(@RequestParam(value = "process_code",required = false)String process_code,@RequestParam(value = "pipe_no",required = false)String pipe_no, @RequestParam(value = "operator_no",required = false)String operator_no, @RequestParam(value = "begin_time",required = false)String begin_time, @RequestParam(value = "end_time",required = false)String end_time, @RequestParam(value = "mill_no",required = false)String mill_no, HttpServletRequest request){
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
        List<HashMap<String,Object>>list=inspectionProcessRecordHeaderDao.getAllByLike(process_code,pipe_no,operator_no,beginTime,endTime,mill_no,start,Integer.parseInt(rows));
        int count=inspectionProcessRecordHeaderDao.getCountAllByLike(process_code,pipe_no,operator_no,beginTime,endTime,mill_no);
        Map<String,Object> maps=new HashMap<String,Object>();
        maps.put("total",count);
        maps.put("rows",list);
        String mmp= JSONArray.toJSONString(maps);
        //System.out.print("mmp:"+mmp);
        return mmp;
    }

}
