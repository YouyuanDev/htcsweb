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
import javax.servlet.http.HttpSession;
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

//        System.out.println("dynamicJson="+dynamicJson);
//        System.out.println("outputJson="+outputJson);
//        System.out.println("inputStatusList="+inputStatusList);

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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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

            boolean inputStatusVerified=false;
            List<HashMap<String,Object>> list=pipeBasicInfoDao.getPipeInfoByNo(pipeno);
            String p_status="";
            if(list.size()>0) {
                p_status = (String) list.get(0).get("status");
                if(inputStatusList!=null){
                    if(inputStatusList.equals("")){
                        inputStatusVerified=true;
                    }
                    else if(inputStatusList.equals("lab_testing_od_regular")){
                        if( list.get(0).get("odsampling_mark").equals("1"))
                            inputStatusVerified=true;
                    }else if(inputStatusList.equals("lab_testing_dsc")){
                        if( list.get(0).get("od_dsc_sample_mark").equals("1"))
                            inputStatusVerified=true;
                    }else if(inputStatusList.equals("lab_testing_pe")){
                        if( list.get(0).get("od_pe_sample_mark").equals("1"))
                            inputStatusVerified=true;
                    }else if(inputStatusList.equals("lab_testing_glass")){
                        if( list.get(0).get("id_glass_sample_mark").equals("1"))
                            inputStatusVerified=true;
                    }else if(inputStatusList.equals("lab_testing_id_regular")){
                        if( list.get(0).get("idsampling_mark").equals("1"))
                            inputStatusVerified=true;
                    }else if(inputStatusList.equals("coating_sampling")){
                        if( list.get(0).get("odsampling_mark").equals("0"))
                            inputStatusVerified=true;
                    }else if(inputStatusList.equals("coating_rebevel")){
                        if( list.get(0).get("rebevel_mark").equals("1"))
                            inputStatusVerified=true;
                    }
                    else{
                        String [] statusArr=inputStatusList.split(",");
                        for(int i=0; i<statusArr.length;i++){
                            inputStatusVerified=statusArr[i].equals(p_status);
                            if(inputStatusVerified)break;
                        }
                    }
                }
            }
            //判断mill_no 是否正确，保证管子所有工序只能在一个工厂内完成

            InspectionProcessRecordHeader lastrecord=inspectionProcessRecordHeaderDao.getLastInspectionRecord(pipeno);
            if(lastrecord!=null&&!lastrecord.getMill_no().equals(inspectionProcessRecordHeader.getMill_no())){
                //mill_no不同，不可以保存数据
                msg="该管号上一工序所处分厂为"+lastrecord.getMill_no()+"，本次检验分厂为"+inspectionProcessRecordHeader.getMill_no()+"，请保持同一分厂";
            }else{
                //可以保存
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
                            System.out.println("resTotal="+resTotal);
                        }
                    }
                    project_no=(String)list.get(0).get("project_no");
                    System.out.println("project_no="+project_no);
                }else{
                    //修改！
                    resTotal=inspectionProcessRecordHeaderDao.updateInspectionProcessRecordHeader(inspectionProcessRecordHeader);
                }
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


                        if(inspectionProcessRecordHeader.getResult().equals("1")){
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
                                            String type = newname.getGenericType().toString();    //获取属性的类型
                                            System.out.println("字段="+des_pipe_property_name+"=");
                                            System.out.println("字段类型="+type+"=");
                                            System.out.println("value="+set_value+"=");
                                            newname.setAccessible(true);

                                            if (type.contains("String")) {
                                                newname.set(obj, set_value);
                                            }
                                            else if(type.contains("int")) {
                                                if(set_value!=null){
                                                    newname.set(obj, Integer.parseInt(set_value));
                                                }

                                            }
                                            else if(type.contains("float")) {
                                                if(set_value!=null){
                                                    newname.set(obj, Float.parseFloat(set_value));
                                                }
                                            }
                                            p=(PipeBasicInfo)obj;
                                            int statusRes = pipeBasicInfoDao.updatePipeBasicInfo(p);
                                            System.out.println("设置属性"+des_pipe_property_name+"="+set_value);
                                        }
                                    }
                                }
                            }
                        }

                }


                //各类工序合格后的处理逻辑
                if(inspectionProcessRecordHeader.getResult().equals("1")){

                    if(inspectionProcessRecordHeader.getProcess_code().equals("od_blast_inspection")){
                        //APP使用，外喷砂检验合格后的管子，自动添加odcoating记录
                        HttpSession session = request.getSession();
                        //把用户数据保存在session域对象中
                        String app_mill_no=(String) session.getAttribute("millno");
                        //app_mill_no="mill_2";
                        if (app_mill_no != null) {
                            //此处为APP应用
                            List<HashMap<String, Object>> lt = pipeBasicInfoDao.getPipeInfoByNo(pipeno);
                            if (lt.size() > 0) {
                                InspectionProcessRecordHeader header=new InspectionProcessRecordHeader();
                                header.setId(0);
                                header.setPipe_no(pipeno);
                                header.setMill_no(mill_no);
                                header.setOperation_time(new Date());
                                header.setOperator_no(inspectionProcessRecordHeader.getOperator_no());
                                header.setResult("10");
                                header.setRemark("INIT");
                                header.setUpload_files("");
                                header.setProcess_code("od_coating");
                                header.setInspection_process_record_header_code("IPRH"+UUID.randomUUID().toString());
                                inspectionProcessRecordHeaderDao.addInspectionProcessRecordHeader(header);
                            }
                        }
                    }
                    else  if((inspectionProcessRecordHeader.getProcess_code().equals("od_coating")||inspectionProcessRecordHeader.getProcess_code().equals("id_coating"))){

                            //更新涂敷前等待时间(当process_code为od_coating，id_coating时执行)
                            //更新钢管涂层时间
                            //String header_code=inspectionProcessRecordHeader.getInspection_process_record_header_code();
                            String coating_type="",blast_type="";
                            if(inspectionProcessRecordHeader.getProcess_code().equals("od_coating")){
                                coating_type="od_elapsed_time";
                                blast_type="od_blast_inspection";
                                p.setOd_coating_date(new Date());
                            }
                            else{
                                coating_type="id_elapsed_time";
                                blast_type="id_blast_inspection";
                                p.setId_coating_date(new Date());
                            }
                            int statusRes = pipeBasicInfoDao.updatePipeBasicInfo(p);


                            int headerId=inspectionProcessRecordHeader.getId();
                            List<HashMap<String,Object>>blastlist=inspectionProcessRecordHeaderDao.getBlastInfoByCoatingInfo(pipeno,headerId,blast_type);
                            if(blastlist!=null&&blastlist.size()>0){
                                HashMap<String,Object>hs=blastlist.get(0);
                                int odBlastId=Integer.parseInt(String.valueOf(hs.get("id")));
                                long begin_time=sdf.parse(String.valueOf(hs.get("coatingtime"))).getTime();
                                long end_time=sdf.parse(String.valueOf(hs.get("blasttime"))).getTime();
                                float minute=((begin_time-end_time)/(1000));
                                minute=minute/60;
                                minute=(float)(Math.round(minute*100))/100;
                                //开始更新涂敷等待时间
                                String inspection_process_record_header_code=String.valueOf(hs.get("inspection_process_record_header_code"));
                                inspectionProcessRecordItemDao.updateElapsedTime(inspection_process_record_header_code,coating_type,String.valueOf(minute));
                            }
                        }
                    else if(inspectionProcessRecordHeader.getProcess_code().equals("coating_sampling")){
                        //设置外防取样标志
                        p.setOdsampling_mark("1");
                        p.setRebevel_mark("1");
                        pipeBasicInfoDao.updatePipeBasicInfo(p);
                    }
                    else if(inspectionProcessRecordHeader.getProcess_code().equals("grinding_cutoff")){
                        //钢管切割设置倒棱标识位
                        InspectionProcessRecordItem recorditem=inspectionProcessRecordItemDao.getInspectionProcessRecordItemByHeaderCodeAndItemCode(inspectionProcessRecordHeader.getInspection_process_record_header_code(),"grinding_cutoff");
                        if(recorditem!=null&&recorditem.getItem_value().contains("CutOff")){
                            p.setRebevel_mark("1");
                            pipeBasicInfoDao.updatePipeBasicInfo(p);
                        }
                    }
                    else if(inspectionProcessRecordHeader.getProcess_code().equals("coating_rebevel")){
                        //清空置外防取样标志
                        p.setRebevel_mark("0");
                        pipeBasicInfoDao.updatePipeBasicInfo(p);
                    }
                    else if(inspectionProcessRecordHeader.getProcess_code().equals("coating_strip")){
                        //清空外涂层涂层日期
                        //判断外防扒皮还是内防扒皮
                        InspectionProcessRecordItem recorditem=inspectionProcessRecordItemDao.getInspectionProcessRecordItemByHeaderCodeAndItemCode(inspectionProcessRecordHeader.getInspection_process_record_header_code(),"strip_odid");
                        if(recorditem!=null&&recorditem.getItem_value().equals("OD")){
                             p.setOd_coating_date(null);
                             p.setId_coating_date(null);
                        }else if(recorditem!=null&&recorditem.getItem_value().equals("ID")){
                            p.setId_coating_date(null);
                        }
                        pipeBasicInfoDao.updatePipeBasicInfo(p);
                    }
                }

                //for循环结束
                //更新管子的状态
                if(p!=null){

                        for(int i=0;i<resultArray.size();i++){
                            JSONObject  rmap=(JSONObject)resultArray.get(i);
                            if(rmap!=null){
                                //需要更新result_name，用于推送信息内容组成
                                String tmp_result=(String)rmap.get("result");
                                result_name=(String)rmap.get("result_name");
                                result_name_en=(String)rmap.get("result_name_en");
                                String tmp_next_status=(String)rmap.get("next_status");
                                String tmp_last_status=(String)rmap.get("last_status");
                                //找到对应关系
                                if(inspectionProcessRecordHeader.getResult().equals(tmp_result)){
                                    if(inputStatusVerified) {
                                        if (tmp_next_status != null) {
                                            if (!tmp_next_status.equals("last_status")) {
                                                p.setStatus(tmp_next_status);
                                            } else if (tmp_next_status.equals("last_status")) {
                                                p.setStatus(p.getLast_accepted_status());
                                            }
                                        }
                                        if (tmp_last_status != null)
                                            p.setLast_accepted_status(p.getStatus());
                                        int statusRes = pipeBasicInfoDao.updatePipeBasicInfo(p);
                                    }

                                    break;
                                }

                            }

                        }



                }


                //发送事件推送,仅非合格且非待定时发送
                if(!inspectionProcessRecordHeader.getResult().equals("1")&&!inspectionProcessRecordHeader.getResult().equals("10")){
                    Date nowtime=new Date();
                    //String str_title=sdf.format(nowtime.getTime())+" "+inspectionProcessRecordHeader.getProcess_code()+" "+p.getPipe_no()+" "+result_name;//+"("+result_name_en+")";
                    String str_title=inspectionProcessRecordHeader.getProcess_code()+" "+p.getPipe_no()+" "+result_name;//+"("+result_name_en+")";

                    String str_content="工序："+inspectionProcessRecordHeader.getProcess_code()+",管号："+p.getPipe_no()+", "+result_name+"("+result_name_en+")";
                    String basePath = request.getSession().getServletContext().getRealPath("/");
                    if(basePath.lastIndexOf('/')==-1){
                        basePath=basePath.replace('\\','/');
                    }

                    PushServiceThread threadDemo01 = new PushServiceThread(basePath,inspectionProcessRecordHeader.getProcess_code(),str_title,str_content);
                    threadDemo01.setName("PushServiceThread1");
                    threadDemo01.start();
                    System.out.println(Thread.currentThread().toString());
                    System.out.println("推送title="+str_title);
                    System.out.println("推送str_content="+str_content);
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


    public class PushServiceThread extends Thread{
        private String basePath="";
        private String event="";
        private String title="";
        private String content="";

        public PushServiceThread(String _basePath,String _event, String _title,String _content){
            //编写子类的构造方法，可缺省
            basePath=_basePath;
            event=_event;
            title=_title;
            content=_content;
        }
        public void run(){
            //编写自己的线程代码
            System.out.println(Thread.currentThread().getName());
            List<HashMap<String,Object>>  lt=roleDao.getRolesByEvent(event);

            for(int i=0;i<lt.size();i++){
                String role=(String)lt.get(i).get("role_no");
                //发消息
                APICloudPushService.SendPushNotification(basePath,title,content,"1","0",role,"");
            }
            System.out.println(Thread.currentThread().getName()+" finished");
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



    //得到钢管后10根工位记录管号，并且记录为待定状态10
    @RequestMapping(value = "/getNextTenPipesBeforePipeNo")
    @ResponseBody
    public String getNextTenPipesBeforePipeNo(@RequestParam(value = "pipe_no",required = false)String pipe_no, HttpServletRequest request) {
        HttpSession session = request.getSession();
        //OdCoatOperation/getLastAcceptedRecordBeforePipeNo.action?pipe_no=121212
        //把用户数据保存在session域对象中
        String mill_no = (String) session.getAttribute("millno");
        String process_code=(String) session.getAttribute("process_code");
        Map<String,Object> maps=new HashMap<String,Object>();
        if(mill_no!=null&&pipe_no!=null&&process_code!=null){
            List<HashMap<String,Object>> list=inspectionProcessRecordHeaderDao.getNextTenPipesBeforePipeNo(pipe_no,mill_no,process_code);
            if(list.size()>0){
                //是合格状态
                maps.put("success",true);
                maps.put("data",list);
            }else{
                maps.put("success",false);
            }

        }else{
            maps.put("success",false);
        }
        String mmp= JSONArray.toJSONString(maps);
        return mmp;

    }

}
