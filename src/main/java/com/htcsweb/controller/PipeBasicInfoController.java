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
    private OdBlastProcessDao odBlastProcessDao;
    @Autowired
    private OdBlastInspectionProcessDao odBlastInspectionProcessDao;
    @Autowired
    private OdCoatingProcessDao odCoatingProcessDao;
    @Autowired
    private OdCoatingInspectionProcessDao odCoatingInspectionProcessDao;
    @Autowired
    private OdCoating3LpeProcessDao odCoating3LpeProcessDao;
    @Autowired
    private OdCoating3LpeInspectionProcessDao odCoating3LpeInspectionProcessDao;
    @Autowired
    private OdStencilProcessDao odStencilProcessDao;
    @Autowired
    private OdFinalInspectionProcessDao odFinalInspectionProcessDao;
    @Autowired
    private IdBlastProcessDao idBlastProcessDao;
    @Autowired
     private  IdBlastInspectionProcessDao idBlastInspectionProcessDao;
    @Autowired
    private IdCoatingProcessDao idCoatingProcessDao;
    @Autowired
    private IdCoatingInspectionProcessDao idCoatingInspectionProcessDao;
    @Autowired
    private IdStencilProcessDao idStencilProcessDao;
    @Autowired
    private IdFinalInspectionProcessDao idFinalInspectionProcessDao;

    @Autowired
    private CoatingStripDao coatingStripDao;
    @Autowired
    private CoatingRepairDao coatingRepairDao;
    @Autowired
    private BarePipeGrindingCutoffRecordDao barePipeGrindingCutoffRecordDao;
    @Autowired
    private PipeRebevelRecordDao pipeRebevelRecordDao;
    //给app搜索使用，无需登录
//    @RequestMapping("/getPipeNumber")
//    @ResponseBody
//    public String getPipeNumber(HttpServletRequest request){
//        String pipe_no=request.getParameter("pipe_no");
//        List<PipeBasicInfo>list=pipeBasicInfoDao.getPipeNumber(pipe_no);
//        String map= JSONObject.toJSONString(list);
//        return map;
//    }


    //给app搜索使用，无需登录
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
            String pipestatus=request.getParameter("pipestatus");
            String[]idArr={};
            if(pipestatus!=null){
                idArr=pipestatus.split(",");
            }
            List<PipeBasicInfo>list=pipeBasicInfoDao.getPipeNumbers(pipe_no,external_coatingtype,idArr);
            map= JSONObject.toJSONString(list);
        }catch (Exception e){
            e.printStackTrace();
        }
        return map;
    }


    //查询2FBE未做实验的样管信息 包括DSC实验
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

    //查询3LPE实验的样管信息 包括DSC实验、PE实验
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

    //查询3LPE未做实验的样管信息 包括玻璃片实验、常规实验
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






    //查询2FBE实验样管信息 不包括DSC实验
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


    //查询2FBE dsc实验管号，未做实验的
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





    //查询3LPE常规实验样管信息
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

    //查询3LPE DSC实验样管信息
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

            System.out.println("testing_id id="+testing_id);
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

    //查询3LPE PE实验样管信息
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





    //查询Liquid Epoxy内防实验样管信息  常规实验
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

    //查询Liquid Epoxy内防实验样管信息  玻璃片实验
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




    //查询样管信息
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

    //查询需要倒棱的管号信息
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

    //用于搜索的pipe状态下拉框，带All 选项
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
        //System.out.println("========="+map);
        return map;
    }



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
        //System.out.println("========="+map);
        return map;
    }


    //模糊查询Pipe信息列表
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
    //根据钢管编号异步查询钢管信息
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
    
//

    //模糊查询OD ID 光管的Pipe信息列表
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
        //System.out.println("钢管状态："+status);
        int start=(Integer.parseInt(page)-1)*Integer.parseInt(rows);
        List<HashMap<String,Object>>list=pipeBasicInfoDao.getODIDBarePipeInfoByLike(project_no,contract_no,pipe_no,status,start,Integer.parseInt(rows));
        int count=pipeBasicInfoDao.getCountODIDBarePipeInfoByLike(project_no,contract_no,pipe_no,status);

        Map<String,Object> maps=new HashMap<String,Object>();
        maps.put("total",count);
        maps.put("rows",list);
        //System.out.println("结果集："+list.toString());
        //System.out.println("结果个数："+list.toString());
        String mmp= JSONArray.toJSONString(maps);
        return mmp;
    }




    //模糊查询外防成品、可出库的Pipe信息列表
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


    //模糊查询内防成品、可入成品库的Pipe信息列表
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



    //增加或修改Pipe信息
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

    //删除pipebasicinfo信息
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


    //外访成品管入库
    @RequestMapping("/odproductstockin")
    public String odproductstockin(@RequestParam(value = "hlparam")String hlparam,@RequestParam(value = "storage_stack")String storage_stack,@RequestParam(value = "stack_level")String stack_level,@RequestParam(value = "level_direction")String level_direction,@RequestParam(value = "level_sequence")String level_sequence,HttpServletResponse response)throws Exception{
        //System.out.println("-----------"+hlparam);
        String[]idArr=hlparam.split(",");
        int resTotal=0;
        resTotal=pipeBasicInfoDao.odProductStockin(idArr,storage_stack,stack_level,level_direction,level_sequence);
        JSONObject json=new JSONObject();
        StringBuilder sbmessage = new StringBuilder();
        sbmessage.append("总共");
        sbmessage.append(Integer.toString(resTotal));
        sbmessage.append("根钢管外防入库成功\n");
        if(resTotal>0){
            System.out.print("外防入库成功");
            json.put("success",true);
        }else{
            System.out.print("外防入库失败");
            json.put("success",false);
        }
        json.put("message",sbmessage.toString());
        ResponseUtil.write(response,json);
        return null;
    }


    //内访成品管入库
    @RequestMapping("/idproductstockin")
    public String idproductstockin(@RequestParam(value = "hlparam")String hlparam,@RequestParam(value = "storage_stack")String storage_stack,@RequestParam(value = "stack_level")String stack_level,@RequestParam(value = "level_direction")String level_direction,@RequestParam(value = "level_sequence")String level_sequence,HttpServletResponse response)throws Exception{
        String[]idArr=hlparam.split(",");
        int resTotal=0;
        resTotal=pipeBasicInfoDao.idProductStockin(idArr,storage_stack,stack_level,level_direction,level_sequence);
        JSONObject json=new JSONObject();
        StringBuilder sbmessage = new StringBuilder();
        sbmessage.append("总共");
        sbmessage.append(Integer.toString(resTotal));
        sbmessage.append("根钢管内防入库成功\n");
        if(resTotal>0){
           // System.out.print("内防入库成功");
            json.put("success",true);
        }else{
           // System.out.print("内防入库成功");
            json.put("success",false);
        }
        json.put("message",sbmessage.toString());
        ResponseUtil.write(response,json);
        return null;
    }


    //内防光管（必须无外防处理记录）、外防扒皮管转外防光管库
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
            //System.out.println("newidArr=" + newidArr[0]);

            resTotal = pipeBasicInfoDao.SetToODBare(newidArr);
        }
        JSONObject json=new JSONObject();
        sbmessage.insert(0,"根钢管转入外防光管库\n");
        sbmessage.insert(0, Integer.toString(resTotal));
        sbmessage.insert(0,"总共");
        if(resTotal>0){
            //System.out.print("转外防光管库成功");
            json.put("success",true);
        }else{
            //System.out.print("转外防光管库成功");
            json.put("success",false);
        }
        json.put("message",sbmessage.toString());
        ResponseUtil.write(response,json);
        return null;
    }

    //外防光管（必须无内防处理记录）、内防扒皮管转内防光管库
    @RequestMapping("/SetPipeTOIDBare")
    public String SetPipeTOIDBare(@RequestParam(value = "hlparam")String hlparam,HttpServletResponse response)throws Exception{
        String[]idArr=hlparam.split(",");
        ArrayList<String> list = new ArrayList<String>();
        StringBuilder sbmessage = new StringBuilder();
        for(int i=0;i<idArr.length;i++){
            //System.out.println("id="+idArr[i]);
            int res=pipeBasicInfoDao.isPipeIDProcessed(idArr[i]);
            if(res==0){
                //System.out.println("res==0");
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
            //System.out.println("newidArr=" + newidArr[0]);

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







    //模糊查询内防外防成品可出厂的Pipe信息列表，需倒棱的管子不可出厂，光管也可以出库
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
    //成品管出厂
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
            //System.out.print("涂层成品管出厂成功");
            json.put("success",true);
        }else{
            //System.out.print("涂层成品管出厂失败");
            json.put("success",false);
        }
        json.put("message",sbmessage.toString());
        ResponseUtil.write(response,json);
        return null;
    }
    //根据钢管编号查询钢管的防腐记录
    @RequestMapping("/searchPipeRecord")
    @ResponseBody
    public String searchPipeRecord(HttpServletRequest request){
        String mmp="";
        try{
            Map<String,Object> maps=new HashMap<String,Object>();
            String pipe_no=request.getParameter("pipe_no");
            if(pipe_no!=null&&!pipe_no.equals("")){
                //根据钢管编号查询钢管的所有记录
                //1.查询外打砂记录
                OdBlastProcess odBlastProcessRecord=odBlastProcessDao.getRecentRecordByPipeNo(pipe_no);
                //2.查询外打砂检验记录
                OdBlastInspectionProcess odBlastInspectionProcessRecord=odBlastInspectionProcessDao.getRecentRecordByPipeNo(pipe_no);
                //3.查询外涂记录
                OdCoatingProcess odCoatingProcessRecord=odCoatingProcessDao.getRecentRecordByPipeNo(pipe_no);
                OdCoating3LpeProcess odCoating3LpeProcessRecord=odCoating3LpeProcessDao.getRecentRecordByPipeNo(pipe_no);
                //4.查询外涂检验记录
                OdCoatingInspectionProcess odCoatingInspectionProcessRecord=odCoatingInspectionProcessDao.getRecentRecordByPipeNo(pipe_no);
                OdCoating3LpeInspectionProcess odCoating3LpeInspectionProcessRecord=odCoating3LpeInspectionProcessDao.getRecentRecordByPipeNo(pipe_no);
                //5.查询外喷标记录
                OdStencilProcess odStencilProcessRecord=odStencilProcessDao.getRecentRecordByPipeNo(pipe_no);
                //6.查询外涂终检记录
                OdFinalInspectionProcess odFinalInspectionProcessRecord=odFinalInspectionProcessDao.getRecentRecordByPipeNo(pipe_no);
                //7.查询内打砂记录
                IdBlastProcess idBlastProcessRecord=idBlastProcessDao.getRecentRecordByPipeNo(pipe_no);
                //8.查询内打砂检验记录
                IdBlastInspectionProcess idBlastInspectionProcessRecord=idBlastInspectionProcessDao.getRecentRecordByPipeNo(pipe_no);
                //9.查询内涂记录
                IdCoatingProcess idCoatingProcessRecord=idCoatingProcessDao.getRecentRecordByPipeNo(pipe_no);
                //10.查询内涂检验记录
                IdCoatingInspectionProcess idCoatingInspectionProcessRecord=idCoatingInspectionProcessDao.getRecentRecordByPipeNo(pipe_no);
                //11.查询内喷标记录
                IdStencilProcess idStencilProcessRecord=idStencilProcessDao.getRecentRecordByPipeNo(pipe_no);
                //12.查询内涂终检记录
                IdFinalInspectionProcess idFinalInspectionProcess=idFinalInspectionProcessDao.getRecentRecordByPipeNo(pipe_no);
                //13.扒皮记录
                List<CoatingStrip>coatingStrips=coatingStripDao.getRecentRecordByPipeNo(pipe_no);
                //14.修补
                List<CoatingRepair>coatingRepairs=coatingRepairDao.getRecentRecordByPipeNo(pipe_no);
                //15.修磨切割
                List<BarePipeGrindingCutoffRecord>barePipeGrindingCutoffRecords=barePipeGrindingCutoffRecordDao.getRecentRecordByPipeNo(pipe_no);
                //16.倒棱
                List<PipeRebevelRecord>pipeRebevelRecords=pipeRebevelRecordDao.getRecentRecordByPipeNo(pipe_no);
                if(odBlastProcessRecord!=null)
                   maps.put("odBlastProcessRecord",odBlastProcessRecord);
                if(odBlastInspectionProcessRecord!=null)
                    maps.put("odBlastInspectionProcessRecord",odBlastInspectionProcessRecord);
                if(odCoatingProcessRecord!=null)
                    maps.put("odCoatingProcessRecord",odCoatingProcessRecord);
                if(odCoatingInspectionProcessRecord!=null)
                    maps.put("odCoatingInspectionProcessRecord",odCoatingInspectionProcessRecord);
                if(odCoating3LpeProcessRecord!=null)
                    maps.put("odCoating3LpeProcessRecord",odCoating3LpeProcessRecord);
                if(odCoating3LpeInspectionProcessRecord!=null)
                    maps.put("odCoating3LpeInspectionProcessRecord",odCoating3LpeInspectionProcessRecord);
                if(odStencilProcessRecord!=null)
                    maps.put("odStencilProcessRecord",odStencilProcessRecord);
                if(odFinalInspectionProcessRecord!=null)
                    maps.put("odFinalInspectionProcessRecord",odFinalInspectionProcessRecord);
                if(idBlastProcessRecord!=null)
                    maps.put("idBlastProcessRecord",idBlastProcessRecord);
                if(idBlastInspectionProcessRecord!=null)
                    maps.put("idBlastInspectionProcessRecord",idBlastInspectionProcessRecord);
                if(idCoatingProcessRecord!=null)
                    maps.put("idCoatingProcessRecord",idCoatingProcessRecord);
                if(idCoatingInspectionProcessRecord!=null)
                    maps.put("idCoatingInspectionProcessRecord",idCoatingInspectionProcessRecord);
                if(idStencilProcessRecord!=null)
                    maps.put("idStencilProcessRecord",idStencilProcessRecord);
                if(idFinalInspectionProcess!=null)
                    maps.put("idFinalInspectionProcess",idFinalInspectionProcess);
                if(coatingStrips!=null&&coatingStrips.size()>0)
                    maps.put("coatingStrips",coatingStrips);
                if(coatingRepairs!=null&&coatingRepairs.size()>0)
                    maps.put("coatingRepairs",coatingRepairs);
                if(barePipeGrindingCutoffRecords!=null&&barePipeGrindingCutoffRecords.size()>0)
                    maps.put("barePipeGrindingCutoffRecords",barePipeGrindingCutoffRecords);
                if(pipeRebevelRecords!=null&&pipeRebevelRecords.size()>0)
                    maps.put("pipeRebevelRecords",pipeRebevelRecords);
                maps.put("success",true);
                mmp= JSONArray.toJSONString(maps);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return mmp;
    }
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

    //根据管号判断该钢管是否可以出库
    @RequestMapping(value="checkPipeForShipment",produces="application/json;charset=UTF-8")
    @ResponseBody
    public String checkPipeForShipment(HttpServletRequest request) {
        String pipe_no=request.getParameter("pipe_no");
        System.out.println("pipe_no"+pipe_no);

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
