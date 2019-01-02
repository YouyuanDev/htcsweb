package com.htcsweb.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.htcsweb.dao.*;
import com.htcsweb.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestParam;
import com.htcsweb.util.ResponseUtil;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.htcsweb.util.ComboxItem;


@Controller
@RequestMapping("/pipeinfo")
public class PipeBasicInfoController {

    @Autowired
    private PipeBasicInfoDao pipeBasicInfoDao;

    @Autowired
    private PipeStatusDao pipeStatusDao;

    @Autowired
    DynamicMeasurementItemDao dynamicMeasurementItemDao;
    @Autowired
    private InspectionProcessRecordHeaderDao headerDao;
    @Autowired
    InspectionTimeRecordDao inspectionTimeRecordDao;
    /**
     * 根据钢管编号分页模糊查询钢管信息,无需登录(app使用)
     * @param request
     * @return
     */
    @RequestMapping(value = "/searchPipe", produces = "text/plain;charset=utf-8")
    @ResponseBody
    public String searchPipe(HttpServletRequest request){
        String pipe_no=request.getParameter("pipe_no");
        String page= request.getParameter("page");
        String rows= request.getParameter("rows");
        if(page==null){
            page="1";
        }
        if(rows==null){
            rows="20";
        }
        int start=(Integer.parseInt(page)-1)*Integer.parseInt(rows);
        List<HashMap<String,Object>>list=pipeBasicInfoDao.searchPipe(pipe_no,start,Integer.parseInt(rows));
        int count=pipeBasicInfoDao.searchPipeCount(pipe_no);
        Map<String,Object> maps=new HashMap<String,Object>();
        maps.put("total",count);
        maps.put("rows",list);
        String mmp= JSONArray.toJSONString(maps);
        return mmp;

    }
    @RequestMapping("/getPipeNumbers")
    @ResponseBody
    public String getPipeNumbers(HttpServletRequest request){
        String map="";
        try{
            String pipe_no=request.getParameter("pipe_no");
            String external_coatingtype=request.getParameter("external_coatingtype");
            String process_code=request.getParameter("processcode");
            String pipestatus=request.getParameter("pipestatus");
            String[]statusArr=null;
            if(pipestatus!=null&&!pipestatus.equals("")){
                if(pipestatus.equals("lab_testing_od_regular")||pipestatus.equals("lab_testing_dsc")||pipestatus.equals("lab_testing_pe")||pipestatus.equals("lab_testing_glass")||pipestatus.equals("lab_testing_id_regular")||pipestatus.equals("coating_sampling")||pipestatus.equals("coating_rebevel")){
                    pipestatus="";
                }else{
                    statusArr=pipestatus.split(",");
                }
            }
            //判断是否是实验界面
            String odsampling_mark="",od_dsc_sample_mark="",od_pe_sample_mark="",idsampling_mark="",id_glass_sample_mark="",rebevel_mark="";
            if(process_code!=null&&!process_code.equals("")){
                if(process_code.equals("lab_testing_od_regular")){
                    odsampling_mark="1";
                }else if(process_code.equals("lab_testing_dsc")){
                    od_dsc_sample_mark="1";
                }else if(process_code.equals("lab_testing_pe")){
                    od_pe_sample_mark="1";
                }else if(process_code.equals("lab_testing_id_regular")){
                    idsampling_mark="1";
                }else if(process_code.equals("lab_testing_glass")){
                    id_glass_sample_mark="1";
                }else if(process_code.equals("coating_sampling")){
                    odsampling_mark="0";
                }else if(process_code.equals("coating_rebevel")){
                    rebevel_mark="1";
                }
            }
            List<PipeBasicInfo>list=pipeBasicInfoDao.getPipeNumbers(pipe_no,external_coatingtype,statusArr,odsampling_mark,od_dsc_sample_mark,od_pe_sample_mark,idsampling_mark,id_glass_sample_mark,rebevel_mark);
            map= JSONObject.toJSONString(list);
        }catch (Exception e){
            e.printStackTrace();
        }
        return map;
    }
    /**
     * 查询2FBE未做实验的样管信息 包括DSC实验
     * @param request
     * @return
     */
    @RequestMapping("/getAll2FBESamplePipe")
    @ResponseBody
    public String getAll2FBESamplePipe(HttpServletRequest request){
        String map="";
        try{
            String project_no=request.getParameter("project_no");
            List<PipeBasicInfo>list=pipeBasicInfoDao.getAll2FBESamplePipe(project_no);
            map= JSONObject.toJSONString(list);
        }catch (Exception e){
        }
        return map;
    }
    /**
     * 查询3LPE实验的样管信息 包括DSC实验、PE实验
     * @param request
     * @return
     */
    @RequestMapping("/getAll3LPESamplePipe")
    @ResponseBody
    public String getAll3LPESamplePipe(HttpServletRequest request){
        String map="";
        try{
            String project_no=request.getParameter("project_no");
            List<PipeBasicInfo>list=pipeBasicInfoDao.getAll3LPESamplePipe(project_no);
            map= JSONObject.toJSONString(list);
        }catch (Exception e){
        }
        return map;
    }
    /**
     * 查询3LPE未做实验的样管信息 包括玻璃片实验、常规实验
     * @param request
     * @return
     */
    @RequestMapping("/getAllEpoxySamplePipe")
    @ResponseBody
    public String getAllEpoxySamplePipe(HttpServletRequest request){
        String map="";
        try{
            String project_no=request.getParameter("project_no");
            List<PipeBasicInfo>list=pipeBasicInfoDao.getAllEpoxySamplePipe(project_no);
            map= JSONObject.toJSONString(list);
        }catch (Exception e){
        }
        return map;
    }
    /**
     * 查询2FBE实验样管信息 不包括DSC实验
     * @param request
     * @return
     */
    @RequestMapping("/get2FBESamplePipeNo")
    @ResponseBody
    public String get2FBESamplePipeNo(HttpServletRequest request){
        String map="";
        try{
            String pipe_no=request.getParameter("pipe_no");
            String coating_date=request.getParameter("coating_date");
            String testing_id=request.getParameter("testing_id");
            int id=-1;
            if(testing_id!=null&&testing_id!="") {
                id=Integer.parseInt(testing_id);
            }
            Date beginTime=null;
            Date endTime=null;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if(coating_date!=null&&coating_date!=""){
                beginTime=sdf.parse(coating_date);
                System.out.println(beginTime.toString());
            }
            if(coating_date!=null&&coating_date!=""){
                endTime=sdf.parse(coating_date);
                System.out.println(endTime.toString());
            }
            List<PipeBasicInfo>list=pipeBasicInfoDao.get2FBESamplePipeNo(pipe_no,beginTime,endTime,id);
            map= JSONObject.toJSONString(list);
        }catch (Exception e){
        }
        return map;
    }
    /**
     * 查询2FBE dsc实验管号，未做实验的
     * @param request
     * @return
     */
    @RequestMapping("/get2FBEDSCSamplePipeNo")
    @ResponseBody
    public String getDSCSamplePipeNo(HttpServletRequest request){
        String map="";
        try{
            String pipe_no=request.getParameter("pipe_no");
            String coating_date=request.getParameter("coating_date");
            String testing_id=request.getParameter("testing_id");
            int id=-1;
            if(testing_id!=null&&testing_id!="") {
                id=Integer.parseInt(testing_id);
            }
            Date beginTime=null;
            Date endTime=null;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if(coating_date!=null&&coating_date!=""){
                beginTime=sdf.parse(coating_date);
                System.out.println(beginTime.toString());
            }
            if(coating_date!=null&&coating_date!=""){
                endTime=sdf.parse(coating_date);
                System.out.println(endTime.toString());
            }
            List<PipeBasicInfo>list=pipeBasicInfoDao.get2FBEDSCSamplePipeNo(pipe_no,beginTime,endTime,id);
            map= JSONObject.toJSONString(list);
        }catch (Exception e){
        }
        return map;
    }
    /**
     * 查询3LPE常规实验样管信息
     * @param request
     * @return
     */
    @RequestMapping("/get3LPESamplePipeNo")
    @ResponseBody
    public String get3LPESamplePipeNo(HttpServletRequest request){
        String map="";
        try{
            String pipe_no=request.getParameter("pipe_no");
            String coating_date=request.getParameter("coating_date");
            String testing_id=request.getParameter("testing_id");
            int id=-1;
            if(testing_id!=null&&testing_id!="") {
                id=Integer.parseInt(testing_id);
            }
            Date beginTime=null;
            Date endTime=null;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if(coating_date!=null&&coating_date!=""){
                beginTime=sdf.parse(coating_date);
                System.out.println(beginTime.toString());
            }
            if(coating_date!=null&&coating_date!=""){
                endTime=sdf.parse(coating_date);
                System.out.println(endTime.toString());
            }
            List<PipeBasicInfo>list=pipeBasicInfoDao.get3LPESamplePipeNo(pipe_no,beginTime,endTime,id);
            map= JSONObject.toJSONString(list);
        }catch (Exception e){
        }
        return map;
    }
    /**
     * 查询3LPE DSC实验样管信息
     * @param request
     * @return
     */
    @RequestMapping("/get3LPEDSCSamplePipeNo")
    @ResponseBody
    public String get3LPEDSCSamplePipeNo(HttpServletRequest request){
        String map="";
        try{
            String pipe_no=request.getParameter("pipe_no");
            String coating_date=request.getParameter("coating_date");
            String testing_id=request.getParameter("testing_id");
            int id=-1;
            if(testing_id!=null&&testing_id!="") {
                id=Integer.parseInt(testing_id);
            }
            Date beginTime=null;
            Date endTime=null;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if(coating_date!=null&&coating_date!=""){
                beginTime=sdf.parse(coating_date);
                System.out.println(beginTime.toString());
            }
            if(coating_date!=null&&coating_date!=""){
                endTime=sdf.parse(coating_date);
                System.out.println(endTime.toString());
            }
            List<PipeBasicInfo>list=pipeBasicInfoDao.get3LPEDSCSamplePipeNo(pipe_no,beginTime,endTime,id);
            map= JSONObject.toJSONString(list);
        }catch (Exception e){
        }
        return map;
    }
    /**
     * 查询3LPE PE实验样管信息
     * @param request
     * @return
     */
    @RequestMapping("/get3LPEPESamplePipeNo")
    @ResponseBody
    public String get3LPEPESamplePipeNo(HttpServletRequest request){
        String map="";
        try{
            String pipe_no=request.getParameter("pipe_no");
            String coating_date=request.getParameter("coating_date");
            String testing_id=request.getParameter("testing_id");
            int id=-1;
            if(testing_id!=null&&testing_id!="") {
                id=Integer.parseInt(testing_id);
            }
            Date beginTime=null;
            Date endTime=null;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if(coating_date!=null&&coating_date!=""){
                beginTime=sdf.parse(coating_date);
                System.out.println(beginTime.toString());
            }
            if(coating_date!=null&&coating_date!=""){
                endTime=sdf.parse(coating_date);
                System.out.println(endTime.toString());
            }
            List<PipeBasicInfo>list=pipeBasicInfoDao.get3LPEPESamplePipeNo(pipe_no,beginTime,endTime,id);
            map= JSONObject.toJSONString(list);
        }catch (Exception e){
        }
        return map;
    }
    /**
     * 查询Liquid Epoxy内防实验样管信息  常规实验
     * @param request
     * @return
     */
    @RequestMapping("/getLiquidEpoxySamplePipeNo")
    @ResponseBody
    public String getLiquidEpoxySamplePipeNo(HttpServletRequest request){
        String map="";
        try{
            String pipe_no=request.getParameter("pipe_no");
            String coating_date=request.getParameter("coating_date");
            String testing_id=request.getParameter("testing_id");
            int id=-1;
            if(testing_id!=null&&testing_id!="") {
                id=Integer.parseInt(testing_id);
            }
            Date beginTime=null;
            Date endTime=null;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if(coating_date!=null&&coating_date!=""){
                beginTime=sdf.parse(coating_date);
                System.out.println(beginTime.toString());
            }
            if(coating_date!=null&&coating_date!=""){
                endTime=sdf.parse(coating_date);
                System.out.println(endTime.toString());
            }
            List<PipeBasicInfo>list=pipeBasicInfoDao.getLiquidEpoxySamplePipeNo(pipe_no,beginTime,endTime,id);
            map= JSONObject.toJSONString(list);
        }catch (Exception e){
        }
        return map;
    }
    /**
     * 查询Liquid Epoxy内防实验样管信息  玻璃片实验
     * @param request
     * @return
     */
    @RequestMapping("/getLiquidEpoxyGlassSamplePipeNo")
    @ResponseBody
    public String getLiquidEpoxyGlassSamplePipeNo(HttpServletRequest request){
        String map="";
        try{
            String pipe_no=request.getParameter("pipe_no");
            String coating_date=request.getParameter("coating_date");
            String testing_id=request.getParameter("testing_id");
            int id=-1;
            if(testing_id!=null&&testing_id!="") {
                id=Integer.parseInt(testing_id);
            }
            Date beginTime=null;
            Date endTime=null;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if(coating_date!=null&&coating_date!=""){
                beginTime=sdf.parse(coating_date);
                System.out.println(beginTime.toString());
            }
            if(coating_date!=null&&coating_date!=""){
                endTime=sdf.parse(coating_date);
                System.out.println(endTime.toString());
            }
            List<PipeBasicInfo>list=pipeBasicInfoDao.getLiquidEpoxyGlassSamplePipeNo(pipe_no,beginTime,endTime,id);
            map= JSONObject.toJSONString(list);
        }catch (Exception e){
        }
        return map;
    }
    /**
     * 查询样管信息
     * @param request
     * @return
     */
    @RequestMapping("/getODSamplePipeNumbers")
    @ResponseBody
    public String getODSamplePipeNumbers(HttpServletRequest request){
        String map="";
        try{
            String pipe_no=request.getParameter("pipe_no");
            List<PipeBasicInfo>list=pipeBasicInfoDao.getODSamplePipeNumbers(pipe_no);
            map= JSONObject.toJSONString(list);
        }catch (Exception e){
        }
        return map;
    }
    /**
     * 查询需要倒棱的管号信息
     * @param request
     * @return
     */
    @RequestMapping("/getNeedRebevelPipeNumbers")
    @ResponseBody
    public String getNeedRebevelPipeNumbers(HttpServletRequest request){
        String map="";
        try{
            String pipe_no=request.getParameter("pipe_no");
            List<PipeBasicInfo>list=pipeBasicInfoDao.getNeedRebevelPipeNumbers(pipe_no);
            map= JSONObject.toJSONString(list);
        }catch (Exception e){
        }
        return map;
    }
    /**
     * 获取钢管所有状态(下拉框),带All 选项
     * @param request
     * @return
     */
    @RequestMapping("/getAllPipeStatusWithComboboxSelectAll")
    @ResponseBody
    public String getAllPipeStatusWithComboboxSelectAll(HttpServletRequest request){
        List<PipeStatus>list=pipeStatusDao.getAllPipeStatus();
        List<ComboxItem> colist=new ArrayList<ComboxItem>();
        ComboxItem itemall= new ComboxItem();
        itemall.id="";
        itemall.text="All（所有状态）";
        colist.add(itemall);
        for(int i=0;i<list.size();i++){
            ComboxItem citem= new ComboxItem();
            PipeStatus ps=((PipeStatus)list.get(i));
            citem.id=ps.getStatus_code();
            citem.text= ps.getStatus_code()+"("+ps.getStatus_name()+")";
            colist.add(citem);
        }
        String map= JSONObject.toJSONString(colist);
        return map;
    }
    /**
     * 获取钢管所有状态(下拉框),不带All 选项
     * @param request
     * @return
     */
    @RequestMapping("/getAllPipeStatus")
    @ResponseBody
    public String getAllPipeStatus(HttpServletRequest request){
        List<PipeStatus>list=pipeStatusDao.getAllPipeStatus();
        List<ComboxItem> colist=new ArrayList<ComboxItem>();
        for(int i=0;i<list.size();i++){
            ComboxItem citem= new ComboxItem();
            PipeStatus ps=((PipeStatus)list.get(i));
            citem.id=ps.getStatus_code();
            citem.text= ps.getStatus_code()+"("+ps.getStatus_name()+")";
            colist.add(citem);
        }
        String map= JSONObject.toJSONString(colist);
        return map;
    }
    /**
     * 分页模糊查询钢管信息
     * @param project_no(项目编号)
     * @param contract_no(合同编号)
     * @param pipe_no(钢管编号)
     * @param status(钢管状态)
     * @param request
     * @return
     */
    @RequestMapping(value = "/getPipeInfoByLike")
    @ResponseBody
    public String getPipeInfoByLike(@RequestParam(value = "project_no",required = false)String project_no, @RequestParam(value = "contract_no",required = false)String contract_no,@RequestParam(value = "pipe_no",required = false)String pipe_no, @RequestParam(value = "status",required = false)String status,HttpServletRequest request){
        String page= request.getParameter("page");
        String rows= request.getParameter("rows");
        if(page==null){
            page="1";
        }
        if(rows==null){
            rows="20";
        }
        int start=(Integer.parseInt(page)-1)*Integer.parseInt(rows);
        List<HashMap<String,Object>>list=pipeBasicInfoDao.getAllByLike(project_no,contract_no,pipe_no,status,start,Integer.parseInt(rows));
        int count=pipeBasicInfoDao.getCountAllByLike(project_no,contract_no,pipe_no,status);
        Map<String,Object> maps=new HashMap<String,Object>();
        maps.put("total",count);
        maps.put("rows",list);
        String mmp= JSONArray.toJSONString(maps);
        return mmp;
    }
    /**
     * 根据钢管编号异步查询钢管信息
     * @param response
     * @param request
     * @return
     */
    @RequestMapping(value ="/getPipeInfoByNo")
    public String getPipeInfoByNo(HttpServletResponse response,HttpServletRequest request){
        String pipeno=request.getParameter("pipe_no");
        List<HashMap<String,Object>>list=pipeBasicInfoDao.getPipeInfoByNo(pipeno);
        String mmp= JSON.toJSONString(list);
        try{
            ResponseUtil.write(response, mmp);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 判断钢管是否能入成品库
     * @param response
     * @param request
     * @return
     */
    @RequestMapping(value ="/checkForProductStockIn")
    @ResponseBody
    public String checkForProductStockIn(HttpServletResponse response,HttpServletRequest request){
        String pipeno=request.getParameter("pipe_no");
        List<HashMap<String,Object>>list=pipeBasicInfoDao.getPipeInfoByNo(pipeno);
        JSONObject jsonObject=new JSONObject();
        if(list!=null&&list.size()>0){
            HashMap<String,Object>hs=list.get(0);
            String status=String.valueOf(hs.get("status"));
            if(status.equals("od6")||status.equals("id6")){
                jsonObject.put("success",true);
                jsonObject.put("record",hs);
                jsonObject.put("message","可以入库!");
            }else{
                jsonObject.put("success",false);
                jsonObject.put("message","当前状态"+status+",不能入库!");
            }
        }else{
            jsonObject.put("success",false);
            jsonObject.put("message","未查到钢管信息!");
        }
        String mmp= JSON.toJSONString(jsonObject);
        return mmp;
    }
    /**
     * 分页模糊查询外防、内防光管的钢管信息
     * @param project_no(项目编号)
     * @param contract_no(合同编号)
     * @param pipe_no(钢管编号)
     * @param status(钢管状态)
     * @param request
     * @return
     */
    @RequestMapping(value ="/getODIDBarePipeInfoByLike")
    @ResponseBody
    public String getODIDBarePipeInfoByLike(@RequestParam(value = "project_no",required = false)String project_no, @RequestParam(value = "contract_no",required = false)String contract_no,@RequestParam(value = "pipe_no",required = false)String pipe_no, @RequestParam(value = "status",required = false)String status,HttpServletRequest request){
        String page= request.getParameter("page");
        String rows= request.getParameter("rows");
        if(page==null){
            page="1";
        }
        if(rows==null){
            rows="20";
        }
        if(status==null||status.equals("")){
            status="bare1";
        }
        int start=(Integer.parseInt(page)-1)*Integer.parseInt(rows);
        List<HashMap<String,Object>>list=pipeBasicInfoDao.getODIDBarePipeInfoByLike(project_no,contract_no,pipe_no,status,start,Integer.parseInt(rows));
        int count=pipeBasicInfoDao.getCountODIDBarePipeInfoByLike(project_no,contract_no,pipe_no,status);
        Map<String,Object> maps=new HashMap<String,Object>();
        maps.put("total",count);
        maps.put("rows",list);
        String mmp= JSONArray.toJSONString(maps);
        return mmp;
    }
    /**
     * 分页模糊查询外防成品、可出库的钢管信息
     * @param project_no(项目编号)
     * @param contract_no(合同编号)
     * @param pipe_no(钢管编号)
     * @param request
     * @return
     */
    @RequestMapping(value ="/getODInspectedPipeInfoByLike")
    @ResponseBody
    public String getODInspectedPipeInfoByLike(@RequestParam(value = "project_no",required = false)String project_no, @RequestParam(value = "contract_no",required = false)String contract_no,@RequestParam(value = "pipe_no",required = false)String pipe_no, HttpServletRequest request){
        String page= request.getParameter("page");
        String rows= request.getParameter("rows");
        if(page==null){
            page="1";
        }
        if(rows==null){
            rows="20";
        }
        int start=(Integer.parseInt(page)-1)*Integer.parseInt(rows);
        List<HashMap<String,Object>>list=pipeBasicInfoDao.getODInspectedPipeInfoByLike(project_no,contract_no,pipe_no,start,Integer.parseInt(rows));
        int count=pipeBasicInfoDao.getCountODInspectedPipeInfoByLike(project_no,contract_no,pipe_no);
        Map<String,Object> maps=new HashMap<String,Object>();
        maps.put("total",count);
        maps.put("rows",list);
        String mmp= JSONArray.toJSONString(maps);
        return mmp;
    }
    /**
     * 分页模糊查询内防成品、可入成品库的钢管信息
     * @param project_no(项目编号)
     * @param contract_no(合同编号)
     * @param pipe_no(钢管编号)
     * @param request
     * @return
     */
    @RequestMapping(value ="/getIDInspectedPipeInfoByLike")
    @ResponseBody
    public String getIDInspectedPipeInfoByLike(@RequestParam(value = "project_no",required = false)String project_no, @RequestParam(value = "contract_no",required = false)String contract_no,@RequestParam(value = "pipe_no",required = false)String pipe_no, HttpServletRequest request){
        String page= request.getParameter("page");
        String rows= request.getParameter("rows");
        if(page==null){
            page="1";
        }
        if(rows==null){
            rows="20";
        }
        int start=(Integer.parseInt(page)-1)*Integer.parseInt(rows);
        List<HashMap<String,Object>>list=pipeBasicInfoDao.getIDInspectedPipeInfoByLike(project_no,contract_no,pipe_no,start,Integer.parseInt(rows));
        int count=pipeBasicInfoDao.getCountIDInspectedPipeInfoByLike(project_no,contract_no,pipe_no);
        Map<String,Object> maps=new HashMap<String,Object>();
        maps.put("total",count);
        maps.put("rows",list);
        String mmp= JSONArray.toJSONString(maps);
        return mmp;
    }
    /**
     * 添加或修改钢管基础信息
     * @param pipeBasicInfo(钢管基础信息)
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/savePipe")
    @ResponseBody
    public String savePipe(PipeBasicInfo pipeBasicInfo,HttpServletRequest request, HttpServletResponse response){
        JSONObject json=new JSONObject();
        try{
            int resTotal=0;
            String shipment_date=request.getParameter("shipmentDate");
            String od_coating_date=request.getParameter("odcoatingDate");
            String id_coating_date=request.getParameter("idcoatingDate");
            SimpleDateFormat simFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if(shipment_date!=null&&shipment_date!=""){
                Date new_odbptime = simFormat.parse(shipment_date);
                pipeBasicInfo.setShipment_date(new_odbptime);
            }
            if(od_coating_date!=null&&od_coating_date!=""){
                Date new_od_coating_date = simFormat.parse(od_coating_date);
                pipeBasicInfo.setOd_coating_date(new_od_coating_date);
            }
            if(id_coating_date!=null&&id_coating_date!=""){
                Date new_id_coating_date= simFormat.parse(id_coating_date);
                pipeBasicInfo.setId_coating_date(new_id_coating_date);
            }
            //备份一下上一个生产工序状态,用于修补管及隔离管处理后状态跳转
            String status=pipeBasicInfo.getStatus();
            if(!status.equals("odrepair1")&&!status.equals("odrepair2")&&!status.equals("idrepair1")&&!status.equals("idrepair2")&&!status.equals("onhold")){
                pipeBasicInfo.setLast_accepted_status(status);
            }
            if(pipeBasicInfo.getId()==0){
                //添加
                resTotal=pipeBasicInfoDao.addPipeBasicInfo(pipeBasicInfo);
            }else{
                //修改！
                resTotal=pipeBasicInfoDao.updatePipeBasicInfo(pipeBasicInfo);
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
    /**
     * 删除钢管基础信息
     * @param hlparam(钢管id集合,","分割)
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/delPipe")
    public String delPipe(@RequestParam(value = "hlparam")String hlparam,HttpServletResponse response)throws Exception{
        String[]idArr=hlparam.split(",");
        int resTotal=0;
        resTotal=pipeBasicInfoDao.delPipeBasicInfo(idArr);
        JSONObject json=new JSONObject();
        StringBuilder sbmessage = new StringBuilder();
        sbmessage.append("总共");
        sbmessage.append(Integer.toString(resTotal));
        sbmessage.append("根钢删除成功\n");
        if(resTotal>0){
            json.put("success",true);
        }else{
            json.put("success",false);
        }
        json.put("message",sbmessage.toString());
        ResponseUtil.write(response,json);
        return null;
    }
    /**
     * 成品管入库
     * @param hlparam(成品管id集合,","分割)
     * @param storage_stack(垛位号)
     * @param stack_level(垛位层号)
     * @param level_direction(层方向)
     * @param level_sequence(层序号)
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/productstockin")
    public String productstockin(@RequestParam(value = "hlparam")String hlparam,@RequestParam(value = "storage_stack")String storage_stack,@RequestParam(value = "stack_level")String stack_level,@RequestParam(value = "level_direction")String level_direction,@RequestParam(value = "level_sequence")String level_sequence,HttpServletResponse response)throws Exception{
        JSONObject json=new JSONObject();
//        System.out.println("hlparam="+hlparam);
//        System.out.println("storage_stack="+storage_stack);
//        System.out.println("stack_level="+stack_level);
//        System.out.println("level_direction="+level_direction);
//        System.out.println("level_sequence="+level_sequence);
        int resTotal=0;
        if(hlparam!=null&&!hlparam.equals("")&&level_sequence!=null&&!level_sequence.equals("")){
            String[]idArr=hlparam.split(",");
            int start_level_sequence=Integer.parseInt(level_sequence);
            //System.out.println("start_level_sequence="+start_level_sequence);
            int count=0;
            for(int i=0;i<idArr.length;i++){
                if(idArr[i]!=null&&!idArr[i].equals("")){
                    //System.out.println("11111111="+i);
                    int id=Integer.parseInt(idArr[i]);
                    List<HashMap<String,Object>>list=pipeBasicInfoDao.getPipeInfoById(id);
                    if(list!=null&&list.size()>0){
                        HashMap<String,Object>hs=list.get(0);
                        String status=String.valueOf(hs.get("status"));
                        if(status!=null){
                            if(status.equals("od6")){
                                resTotal=pipeBasicInfoDao.odProductStockin(id,storage_stack,stack_level,level_direction,String.valueOf(start_level_sequence));
                                if(resTotal>0){
                                    start_level_sequence++;
                                    count++;
                                }
                            }else if(status.equals("id6")){
                                resTotal=pipeBasicInfoDao.idProductStockin(id,storage_stack,stack_level,level_direction,String.valueOf(start_level_sequence));
                                if(resTotal>0){
                                    start_level_sequence++;
                                    count++;
                                }
                            }
                        }
                    }
                }
            }
            StringBuilder sbmessage = new StringBuilder();
            sbmessage.append("总共");
            sbmessage.append(String.valueOf(count));
            sbmessage.append("根钢管入库成功\n");
            if(count>0){
                json.put("success",true);
            }else{
                json.put("success",false);
            }
            json.put("message",sbmessage.toString());
        }else{
            json.put("success",false);
            json.put("message","入库失败!");
        }
        ResponseUtil.write(response,json);
        return null;
    }
    /**
     * 外防成品管入库
     * @param hlparam(外防成品管id集合,","分割)
     * @param storage_stack(垛位号)
     * @param stack_level(垛位层号)
     * @param level_direction(层方向)
     * @param level_sequence(层序号)
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/odproductstockin")
    public String odproductstockin(@RequestParam(value = "hlparam")String hlparam,@RequestParam(value = "storage_stack")String storage_stack,@RequestParam(value = "stack_level")String stack_level,@RequestParam(value = "level_direction")String level_direction,@RequestParam(value = "level_sequence")String level_sequence,HttpServletResponse response)throws Exception{
        String[]idArr=hlparam.split(",");
        JSONObject json=new JSONObject();
        int resTotal=0;
        if(level_sequence!=null&&!"".equals(level_sequence)){
            int start_level_sequence=Integer.parseInt(level_sequence);
            int count=0;
            for(int i=0;i<idArr.length;i++){
                if(idArr[i]!=null&&!"".equals(idArr[i])){
                    int id=Integer.parseInt(idArr[i]);
                    resTotal=pipeBasicInfoDao.odProductStockin(id,storage_stack,stack_level,level_direction,String.valueOf(start_level_sequence));
                    if(resTotal>0){
                        start_level_sequence++;
                        count++;
                    }
                }
            }
            StringBuilder sbmessage = new StringBuilder();
            sbmessage.append("总共");
            sbmessage.append(String.valueOf(count));
            sbmessage.append("根钢管外防入库成功\n");
            if(count>0){
                json.put("success",true);
            }else{
                json.put("success",false);
            }
            json.put("message",sbmessage.toString());
        }else{
            json.put("success",false);
            json.put("message","入库失败!");
        }
        ResponseUtil.write(response,json);
        return null;
    }
    /**
     * 内防成品管入库
     * @param hlparam(内防成品管id集合,","分割)
     * @param storage_stack(垛位号)
     * @param stack_level(垛位层号)
     * @param level_direction(层方向)
     * @param level_sequence(层序号)
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/idproductstockin")
    public String idproductstockin(@RequestParam(value = "hlparam")String hlparam,@RequestParam(value = "storage_stack")String storage_stack,@RequestParam(value = "stack_level")String stack_level,@RequestParam(value = "level_direction")String level_direction,@RequestParam(value = "level_sequence")String level_sequence,HttpServletResponse response)throws Exception{
        String[]idArr=hlparam.split(",");
        int resTotal=0;
        JSONObject json=new JSONObject();
        if(level_sequence!=null&&!"".equals(level_sequence)){
            int start_level_sequence=Integer.parseInt(level_sequence);
            int count=0;
            for(int i=0;i<idArr.length;i++){
                if(idArr[i]!=null&&!"".equals(idArr[i])){
                    int id=Integer.parseInt(idArr[i]);
                    resTotal=pipeBasicInfoDao.idProductStockin(id,storage_stack,stack_level,level_direction,String.valueOf(start_level_sequence));
                    if(resTotal>0){
                        start_level_sequence++;
                        count++;
                    }
                }
            }
            StringBuilder sbmessage = new StringBuilder();
            sbmessage.append("总共");
            sbmessage.append(String.valueOf(count));
            sbmessage.append("根钢管内防入库成功\n");
            if(count>0){
                json.put("success",true);
            }else{
                json.put("success",false);
            }
            json.put("message",sbmessage.toString());
        }else{
            json.put("success",false);
            json.put("message","入库失败!");
        }
        ResponseUtil.write(response,json);
        return null;
    }
    /**
     * 内防光管（必须无外防处理记录）、外防扒皮管转外防光管库
     * @param hlparam(内防光管、外防扒皮管id集合,","分割)
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/SetPipeTOODBare")
    public String SetPipeTOODBare(@RequestParam(value = "hlparam")String hlparam,HttpServletResponse response)throws Exception{
        String[]idArr=hlparam.split(",");
        ArrayList<String> list = new ArrayList<String>();
        StringBuilder sbmessage = new StringBuilder();
        for(int i=0;i<idArr.length;i++){

            int res=pipeBasicInfoDao.isPipeODProcessed(idArr[i]);
            if(res==0){
                list.add(idArr[i]);
            }else{
                //需要剔除的钢管id
                sbmessage.append("钢管:");
                sbmessage.append(idArr[i]);
                sbmessage.append("已存在外防生产信息，无法转外防光管 ");
            }
        }
        int resTotal=0;
        if(list.size()>0) {
            String[] array = new String[list.size()];
            String[] newidArr = list.toArray(array);
            resTotal = pipeBasicInfoDao.SetToODBare(newidArr);
        }
        JSONObject json=new JSONObject();
        sbmessage.insert(0,"根钢管转入外防光管库\n");
        sbmessage.insert(0, Integer.toString(resTotal));
        sbmessage.insert(0,"总共");
        if(resTotal>0){
            json.put("success",true);
        }else{
            json.put("success",false);
        }
        json.put("message",sbmessage.toString());
        ResponseUtil.write(response,json);
        return null;
    }
    /**
     * 外防光管（必须无内防处理记录）、内防扒皮管转内防光管库
     * @param hlparam(外防光管、内防扒皮管id集合,","分割)
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/SetPipeTOIDBare")
    public String SetPipeTOIDBare(@RequestParam(value = "hlparam")String hlparam,HttpServletResponse response)throws Exception{
        String[]idArr=hlparam.split(",");
        ArrayList<String> list = new ArrayList<String>();
        StringBuilder sbmessage = new StringBuilder();
        for(int i=0;i<idArr.length;i++){
            int res=pipeBasicInfoDao.isPipeIDProcessed(idArr[i]);
            if(res==0){
                list.add(idArr[i]);
            }else{
                //需要剔除的钢管id
                sbmessage.append("钢管:");
                sbmessage.append(idArr[i]);
                sbmessage.append("已存在内防生产信息，无法转内防光管\n ");
            }
        }
        int resTotal=0;
        if(list.size()>0) {
            String[] array = new String[list.size()];
            String[] newidArr = list.toArray(array);
            resTotal = pipeBasicInfoDao.SetToIDBare(newidArr);
        }
        JSONObject json=new JSONObject();
        sbmessage.insert(0,"根钢管转入内防光管库\n");
        sbmessage.insert(0, Integer.toString(resTotal));
        sbmessage.insert(0,"总共");
        if(resTotal>0){
            System.out.print("转内防光管库成功");
            json.put("success",true);
        }else{
            System.out.print("转内防光管库失败");
            json.put("success",false);
        }
        json.put("message",sbmessage.toString());
        ResponseUtil.write(response,json);
        return null;
    }
    /**
     * 分页模糊查询内防外防成品可出厂的钢管信息(需倒棱的管子不可出厂，光管也可以出库)
     * @param project_no(项目编号)
     * @param contract_no(合同编号)
     * @param pipe_no(钢管编号)
     * @param status(钢管状态)
     * @param request
     * @return
     */
    @RequestMapping(value ="/getCoatedStockinPipeInfoByLike")
    @ResponseBody
    public String getCoatedStockinPipeInfoByLike(@RequestParam(value = "project_no",required = false)String project_no, @RequestParam(value = "contract_no",required = false)String contract_no,@RequestParam(value = "pipe_no",required = false)String pipe_no,@RequestParam(value = "status",required = false)String status, HttpServletRequest request){
        String page= request.getParameter("page");
        String rows= request.getParameter("rows");
        if(page==null){
            page="1";
        }
        if(rows==null){
            rows="20";
        }
        if(status==null||status.equals("")){
            status="odstockin";
        }
        int start=(Integer.parseInt(page)-1)*Integer.parseInt(rows);
        List<HashMap<String,Object>>list=pipeBasicInfoDao.getCoatedStockinPipeInfoByLike(project_no,contract_no,pipe_no,status,start,Integer.parseInt(rows));
        int count=pipeBasicInfoDao.getCountCoatedStockinPipeInfoByLike(project_no,contract_no,pipe_no,status);
        Map<String,Object> maps=new HashMap<String,Object>();
        maps.put("total",count);
        maps.put("rows",list);
        String mmp= JSONArray.toJSONString(maps);
        return mmp;
    }
    /**
     * 成品管出厂
     * @param hlparam(成品管id集合,","分割)
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/coatingProductStockout")
    public String coatingProductStockout(@RequestParam(value = "hlparam")String hlparam,HttpServletResponse response)throws Exception{
        String[]idArr=hlparam.split(",");
        int resTotal=0;
        resTotal=pipeBasicInfoDao.coatingProductStockout(idArr);
        StringBuilder sbmessage = new StringBuilder();
        JSONObject json=new JSONObject();
        sbmessage.insert(0,"根涂层成品管出厂成功\n");
        sbmessage.insert(0, Integer.toString(resTotal));
        sbmessage.insert(0,"总共");
        if(resTotal>0){
            json.put("success",true);
        }else{
            json.put("success",false);
        }
        json.put("message",sbmessage.toString());
        ResponseUtil.write(response,json);
        return null;
    }
    /**
     * 根据钢管编号查询钢管的防腐记录
     * @param request
     * @return
     */
    @RequestMapping(value = "/searchPipeRecord",produces = "text/plain;charset=utf-8")
    @ResponseBody
    public String searchPipeRecord(HttpServletRequest request){
        String mmp="";
        try{
            String pipe_no=request.getParameter("pipe_no");
            List<Map<String,Object>> result=new ArrayList<>();
            if(pipe_no!=null&&!pipe_no.equals("")){
                String [] processList={"od_blast","od_blast_inspection","od_coating","od_coating_inspection","od_stencil",
                        "od_final_inspection","id_blast","id_blast_inspection","id_coating","id_coating_inspection",
                "id_stencil","id_final_inspection","coating_strip","coating_repair","grinding_cutoff","coating_rebevel"};
                //根据钢管编号查询钢管的所有记录
                for(int i=0;i<processList.length;i++){
                    InspectionProcessRecordHeader header=headerDao.getRecentRecordByPipeNo(processList[i],pipe_no);
                    Map<String,Object> maps=new HashMap<String,Object>();
                    if(header!=null){
                        List<HashMap<String, Object>> itemlist = dynamicMeasurementItemDao.getDynamicItemByPipeNoProcessCodeHeaderCode(pipe_no, processList[i], header.getInspection_process_record_header_code());
                        if(itemlist!=null&&itemlist.size()>0){
                            maps.put(processList[i],itemlist);
                            maps.put(processList[i]+"_header",header);
                            result.add(maps);
                        }
                    }
                }
                mmp= JSONArray.toJSONString(result);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return mmp;
    }
    /**
     * 钢管转运
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/doInStoageTransfer")
    public String doInStoageTransfer(HttpServletRequest request,HttpServletResponse response)throws Exception{

        String hlparam=request.getParameter("hlparam");
        String storage_stack=request.getParameter("storage_stack");
        String stack_level=request.getParameter("stack_level");
        String level_direction=request.getParameter("level_direction");
        String level_sequence=request.getParameter("level_sequence");
        String[]idArr=hlparam.split(",");
        int resTotal=0;
        resTotal=pipeBasicInfoDao.InStoageTransfer(idArr,storage_stack,stack_level,level_direction,level_sequence);
        JSONObject json=new JSONObject();
        StringBuilder sbmessage = new StringBuilder();
        sbmessage.append("总共");
        sbmessage.append(Integer.toString(resTotal));
        sbmessage.append("根钢管转运成功\n");
        if(resTotal>0){
            System.out.print("转运成功");
            json.put("success",true);
        }else{
            System.out.print("转运失败");
            json.put("success",false);
        }
        json.put("message",sbmessage.toString());
        ResponseUtil.write(response,json);
        return null;
    }
    /**
     * 根据管号判断该钢管是否可以出库
     * @param request
     * @return
     */
    @RequestMapping(value="checkPipeForShipment",produces="application/json;charset=UTF-8")
    @ResponseBody
    public String checkPipeForShipment(HttpServletRequest request) {
        String pipe_no=request.getParameter("pipe_no");
        List<HashMap<String,Object>>list=pipeBasicInfoDao.checkPipeForShipment(pipe_no);
        Map<String, Object> maps = new HashMap<String, Object>();
        if (list!=null&&list.size()>0) {
            //是待定状态
            maps.put("success",true);
            maps.put("record",list);
            maps.put("message","");
        } else {
            maps.put("success", false);
            StringBuilder sbmessage = new StringBuilder();
            sbmessage.append("钢管");
            sbmessage.append(pipe_no);
            sbmessage.append("不能出库");
            maps.put("message",sbmessage.toString());
        }
        String mmp= JSONArray.toJSONString(maps);
        return mmp;
    }

}
