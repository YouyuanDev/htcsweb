package com.htcsweb.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.htcsweb.dao.*;
import com.htcsweb.entity.*;
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
@RequestMapping("/APPRequestTransfer")
public class APPRequestTransferController {

    @Autowired
    private AcceptanceCriteriaDao acceptanceCriteriaDao;

    @Autowired
    private PipeBasicInfoDao pipeBasicInfoDao;

    @Autowired
    private InspectionTimeRecordDao inspectionTimeRecordDao;

    @Autowired
    private CoatingPowderInfoDao coatingPowderInfoDao;


    @Autowired
    ContractInfoDao contractInfoDao;

    @Autowired
    private InspectionProcessRecordHeaderDao inspectionProcessRecordHeaderDao;

    @Autowired
    private InspectionProcessRecordItemDao inspectionProcessRecordItemDao;


    //用于APP请求重定向
    @RequestMapping(value = "/getCoatingInfoByPipeNo",produces = "text/plain;charset=utf-8")
    @ResponseBody
    public String getCoatingInfoByPipeNo(@RequestParam(value = "pipe_no",required = false)String pipe_no, HttpServletRequest request,HttpServletResponse response){


        //List<HashMap<String,Object>> list=coatingStripDao.getCoatingStripInfoByLike(pipe_no,operator_no,project_no,contract_no,beginTime,endTime,mill_no,start,Integer.parseInt(rows));
        //int count=coatingStripDao.getCountCoatingStripInfoByLike(pipe_no,operator_no,project_no,contract_no,beginTime,endTime,mill_no);

        Map<String,Object> maps=new HashMap<String,Object>();
        List<HashMap<String,Object>>list=pipeBasicInfoDao.getPipeInfoByNo(pipe_no);

        if(list.size()>0){
            //可跳转的url表
            HashMap<String,Object> urloptions=new HashMap<String,Object>();
            String status=(String)list.get(0).get("status");
            String external_coating=(String)list.get(0).get("external_coating");
            String internal_coating=(String)list.get(0).get("internal_coating");
            String rebevel_mark=(String)list.get(0).get("rebevel_mark");
            String odsampling_mark=(String)list.get(0).get("odsampling_mark");
            String idsampling_mark=(String)list.get(0).get("idsampling_mark");
            String oddscsampling_mark=(String)list.get(0).get("od_dsc_sample_mark");
            String odpesample_mark=(String)list.get(0).get("od_pe_sample_mark");
            String idglasssample_mark=(String)list.get(0).get("id_glass_sample_mark");

            maps.put("success",true);
            maps.put("pipeinfo",list.get(0));
            maps.put("message","存在钢管"+pipe_no+"的信息");
            //根据status，计算可跳转的状态
            //外防
            HashMap<String,Object> functionMap=(HashMap<String,Object>)request.getSession().getAttribute("userfunctionMap");
            if(functionMap!=null) {

                if (status.equals("bare1")) {
                    //urloptions.put("odblastprocess", "od/odblast");
                    urloptions.put("od_blast", "process/generalprocess");
                    urloptions.put("barepipemovement", "storage/barepipemovement");
                    urloptions.put("instoragetransfer", "storage/instoragetransfer");
                    //urloptions.put("productStockout", "storage/productionstockout");
                } else if (status.equals("od1")) {
                    //urloptions.put("odblastinspectionprocess", "od/odblastinspection");
                    urloptions.put("od_blast_inspection", "process/generalprocess");
                }
                else if (status.equals("od2")) {
                    urloptions.put("od_coating", "process/generalprocess");
//                    if (external_coating.equals("2FBE")) {
//                        urloptions.put("odcoatingprocess", "od/odcoating2FBE");
//                    } else if (external_coating.equals("3LPE")) {
//                        urloptions.put("odcoating3lpeprocess", "od/odcoating3LPE");
//                    }
                }
                else if (status.equals("od3")) {
                    urloptions.put("od_coating_inspection", "process/generalprocess");
//                    if (external_coating.equals("2FBE")) {
//                        urloptions.put("odcoatinginspectionprocess", "od/odcoatinginspection2FBE");
//                    } else if (external_coating.equals("3LPE")) {
//                        urloptions.put("odcoating3lpeinspectionprocess", "od/odcoatinginspection3LPE");
//                    }
                }
                else if (status.equals("od4")) {
                    urloptions.put("od_stencil", "process/generalprocess");
                    //urloptions.put("odstencilprocess", "od/odstencil");
                }
                else if (status.equals("od5")) {
                    urloptions.put("od_final_inspection", "process/generalprocess");
                    //urloptions.put("odfinalinspectionprocess", "od/odfinalinspection");
                }
                else if (status.equals("od6")) {
                    urloptions.put("productstockin", "storage/stockin");
                } else if (status.equals("odstockin")) {
                    urloptions.put("id_blast", "process/generalprocess");
                    //urloptions.put("idblastprocess", "id/idblast");
                    urloptions.put("instoragetransfer", "storage/instoragetransfer");

                    //不需要倒棱
                    if (rebevel_mark == null || !rebevel_mark.equals("1")) {
                        //urloptions.put("productStockout", "storage/productionstockout");
                    }

                }
                //内防
                else if (status.equals("bare2")) {
                    //urloptions.put("idblastprocess", "id/idblast");
                    urloptions.put("id_blast", "process/generalprocess");
                    urloptions.put("barepipemovement", "storage/barepipemovement");
                    urloptions.put("instoragetransfer", "storage/instoragetransfer");
                    //urloptions.put("productStockout", "storage/productionstockout");
                } else if (status.equals("id1")) {
                    //urloptions.put("idblastinspectionprocess", "id/idblastinspection");
                    urloptions.put("id_blast_inspection", "process/generalprocess");
                }
                else if (status.equals("id2")) {
                    //urloptions.put("idcoatingprocess", "id/idcoating");
                    urloptions.put("id_coating", "process/generalprocess");

                }
                else if (status.equals("id3")) {
                    //urloptions.put("idcoatinginspectionprocess", "id/idcoatinginspection");
                    urloptions.put("id_coating_inspection", "process/generalprocess");
                }
                else if (status.equals("id4")) {
                    //urloptions.put("idstencilprocess", "id/idstencil");
                    urloptions.put("id_stencil", "process/generalprocess");
                }
                else if (status.equals("id5")) {
                    //urloptions.put("idfinalinspectionprocess", "id/idfinalinspection");
                    urloptions.put("id_final_inspection", "process/generalprocess");
                }
                else if (status.equals("id6")) {
                    urloptions.put("productstockin", "storage/stockin");
                } else if (status.equals("idstockin")) {
                    urloptions.put("instoragetransfer", "storage/instoragetransfer");

                    //不需要倒棱
                    if (rebevel_mark == null || !rebevel_mark.equals("1")) {
                        //urloptions.put("productStockout", "storage/productionstockout");
                    }
                }

                //修补
                else if (status.equals("coatingrepair1") || status.equals("coatingrepair2")) {
                    //urloptions.put("coatingrepair", "addition/coatingrepair");
                    urloptions.put("coating_repair", "process/generalprocess");
                }

                //扒皮
                else if (status.equals("coatingstrip1")) {
                    //urloptions.put("coatingstrip", "addition/coatingstrip");
                    urloptions.put("coating_strip", "process/generalprocess");
                }

                //修磨或切割
                else if (status.equals("onhold")) {
                    //urloptions.put("barepipegrindingProcess", "addition/barepipegrinding");
                    urloptions.put("grinding_cutoff", "process/generalprocess");
                }

                if (rebevel_mark != null && rebevel_mark.equals("1")){
                    //需倒棱
                    //urloptions.put("pipeRebevelProcess", "addition/piperebevel");
                    urloptions.put("coating_rebevel", "process/generalprocess");
                }
                if(odsampling_mark!=null&&odsampling_mark.equals("0")){
                    //需外防取样
                    //urloptions.put("pipeSamplingProcess", "addition/pipesampling");
                    urloptions.put("coating_sampling", "process/generalprocess");
                }
                if(odsampling_mark!=null&&odsampling_mark.equals("1")){
                    //可以做外防实验  odsampling_mark为1时代表取样完毕，其他实验不需要切割取样
//                    if (external_coating.equals("2FBE")) {
//                        //urloptions.put("labtesting2fbe", "labtesting/labtesting2fbe");
//                        urloptions.put("generalprocess", "process/generalprocess");
//                    } else if (external_coating.equals("3LPE")) {
//                        //urloptions.put("labtesting3lpe", "labtesting/labtesting3lpe");
//                        urloptions.put("generalprocess", "process/generalprocess");
//                    }
                    urloptions.put("lab_testing_od_regular", "process/generalprocess");
                }
                if(oddscsampling_mark!=null&&oddscsampling_mark.equals("1")){
                    urloptions.put("lab_testing_dsc", "process/generalprocess");
                }
                if(odpesample_mark!=null&&odpesample_mark.equals("1")){
                    urloptions.put("lab_testing_pe", "process/generalprocess");
                }


                if(idsampling_mark!=null&&idsampling_mark.equals("1")){
                    //可以做内防实验
//                    if (internal_coating.equals("EPOXY")) {
//                        //urloptions.put("labtestingepoxy", "labtesting/labtestingepoxy");
//                        urloptions.put("generalprocess", "process/generalprocess");
//                    }
                    urloptions.put("lab_testing_id_regular", "process/generalprocess");

                }
                if(idglasssample_mark!=null&&idglasssample_mark.equals("1")){

                    urloptions.put("lab_testing_glass", "process/generalprocess");

                }

                //添加原材料实验链接
//                urloptions.put("rawmaterialtesting2fbe", "labtesting/rawmaterialtesting2fbe");
//                urloptions.put("rawmaterialtesting3lpe", "labtesting/rawmaterialtesting3lpe");
//                urloptions.put("rawmaterialtestingliquidepoxy", "labtesting/rawmaterialtestingliquidepoxy");

                //权限过滤url
                List<String> removeList=new ArrayList<>();
                Iterator iter = urloptions.entrySet().iterator();
                while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                String key = (String)entry.getKey();
                String value=(String)entry.getValue();

//                String pageName=value.substring(value.lastIndexOf('/')+1);
//                System.out.println("pageName="+pageName);

                //Object val = entry.getValue();
                    //判断是否有权限
                    if(functionMap!=null){
                        if(!functionMap.containsKey(key)) {
                            System.out.println("APP 不存在存在页面"+key+"的权限");
                            removeList.add(key);
                        }


//                        if(!functionMap.containsKey(pageName)&&!pageName.equals("stockin")) {
//                            System.out.println("APP 不存在存在页面"+pageName+"的权限");
//                            removeList.add(pageName);
//
//                        }
//                        //stockin包含 odstockin  idstockin  有其一权限即可
//                        else if(pageName.equals("stockin")&&!functionMap.containsKey("odstockin")&&!functionMap.containsKey("idstockin")){
//                            System.out.println("APP 不存在存在页面"+pageName+"的权限");
//                            removeList.add(pageName);
//                            continue;
//                        }

                    }
                }
                //清楚没有的权限key
                for(int i=0;i<removeList.size();i++){
                    urloptions.remove(removeList.get(i));
                }
                removeList.clear();

            }
            maps.put("urloptions", urloptions);

        }else{
            maps.put("success",false);
            maps.put("message","不存钢管"+pipe_no+"的信息");
        }


        String mmp= JSONArray.toJSONString(maps);


//
//        try{
//            ResponseUtil.write(response, mmp);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//
//
        //System.out.print("mmp:"+mmp);
        return mmp;
    }




    //根据钢管编号查找内外防腐标准、检验频率、钢管信息、光管检验频率、pending数据、实验标准  APP使用  stencil_content 做完动态替换  并且把检验频率也一起返回
    @RequestMapping(value = "/getAllProcessInfoByPipeNo",produces = "text/plain;charset=utf-8")
    @ResponseBody
    public String getAllProcessInfoByPipeNo(HttpServletRequest request){
        //APPRequestTransfer/getAllProcessInfoByPipeNo.action?pipe_no=1524540&&mill_no=mill_2
        String pipe_no=request.getParameter("pipe_no");

        String process_code=request.getParameter("process_code");

        Map<String,Object> resultMaps=new HashMap<String,Object>();//最终返回Map

        //返回用户session数据
        HttpSession session = request.getSession();
        //把用户数据保存在session域对象中
        String employeeno=(String)session.getAttribute("userSession");
        String mill_no=(String)session.getAttribute("millno");

        if(mill_no==null){
            mill_no="mill_2";
        }


        if(employeeno!=null&&mill_no!=null) {
            resultMaps.put("employeeno",employeeno);
            resultMaps.put("millno",mill_no);
        } else{

            resultMaps.put("success",false);
            resultMaps.put("message","session已过期，请重新登录");
            String map= JSONObject.toJSONString(resultMaps);
            return map;
        }


        if(pipe_no!=null&&!pipe_no.equals("")){
            //钢管信息导出
            List<HashMap<String,Object>> pipelist= pipeBasicInfoDao.getPipeInfoByNo(pipe_no);
            if(pipelist.size()>0){
                resultMaps.put("pipeinfo",pipelist.get(0));
            }else{
                resultMaps.put("pipeinfo","");
            }
            if(process_code!=null&&!process_code.equals("")){
                //标准导出
                //ODCoatingAcceptanceCriteria odcriteria=odcoatingacceptancecriteriaDao.getODAcceptanceCriteriaByPipeNo(pipe_no);
                List<HashMap<String,Object>> aclist=acceptanceCriteriaDao.getAcceptanceCriteriaByPipeNoProcessCode(pipe_no,process_code);
                List<InspectionTimeRecord> inspTimeRecordList=inspectionTimeRecordDao.getRecordByPipeNoMillNo(pipe_no,mill_no,null);

                if(process_code.equals("od_stencil")) {//色环颜色初始化一下
                    for(int i=0;i<aclist.size();i++) {
                        HashMap<String, Object> mp=aclist.get(i);
                        if (mp != null && mp.get("item_code") != null &&mp.get("item_code").equals("center_line_color") ) {
                            ContractInfo contract=contractInfoDao.getContractInfoByPipeNo(pipe_no);
                            if(contract!=null){
                                mp.put("default_value",contract.getCenter_line_color());
                                aclist.set(i,mp);
                            }
                        }else if(mp != null && mp.get("item_code") != null && mp.get("item_code").equals("pipe_end_color")){
                            ContractInfo contract=contractInfoDao.getContractInfoByPipeNo(pipe_no);
                            if(contract!=null){
                                mp.put("default_value",contract.getPipe_end_color());
                                aclist.set(i,mp);
                            }
                        }
                    }

                }




                //数据导出
                InspectionProcessRecordHeader header= inspectionProcessRecordHeaderDao.getRecentRecordByPipeNo(process_code,pipe_no);
                if(header!=null){
                    //除了实验工序，其他的工序只调出pending状态的表单
                    if(header.getResult().equals("10")&&!header.getProcess_code().contains("lab")||header.getProcess_code().contains("lab")){
                        //是待定状态
                        resultMaps.put("record_header",header);
                        List<InspectionProcessRecordItem> itemList=inspectionProcessRecordItemDao.getInspectionProcessRecordItemByInspectionProcessRecordHeaderCode(header.getInspection_process_record_header_code());
                        resultMaps.put("record_items",itemList);
                    }
                }

                if(pipelist.size()>0&&aclist!=null){
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



                    String stencil_content_name="";
                    String coatint_date="";
                    if(process_code.equals("od_stencil")){
                        stencil_content_name="od_stencil_content";
                        coatint_date=od_coating_dateString;
                    }
                    else if(process_code.equals("id_stencil")){
                        stencil_content_name="id_stencil_content";
                        coatint_date=id_coating_dateString;
                    }
                    for(int i=0;i<aclist.size();i++){
                        //根据频率设置是否必填
                        String need_inspection_mark="1";
                        HashMap<String,Object> map=aclist.get(i);
                        float freq = Float.parseFloat((String)aclist.get(i).get("item_frequency"));
                        for(int j=0;j<inspTimeRecordList.size();j++){
                            InspectionTimeRecord timeRecord=inspTimeRecordList.get(i);

                            if(timeRecord.getInspection_item().equals(aclist.get(i).get("item_code"))){
                                //找到检验记录了
                                //检验频率 秒
                                float freqSec=freq*60*60;
                                //lastInspectionTime=timeRecord.getInspction_time().toString();
                                //间隔秒
                                Date now=new Date();
                                long interval = (now.getTime() - timeRecord.getInspction_time().getTime())/1000;

                                if(interval<freqSec){
                                    //间隔小于检验频率，不需要检验
                                    //检验频率导出到动态检测项
                                    need_inspection_mark="0";
                                }

                                break;
                            }

                        }

                        map.put("need_inspection_mark",need_inspection_mark);
                        aclist.set(i,map);


                        if(aclist.get(i).get("item_code").equals(stencil_content_name)){
                            String stencil_content=(String)aclist.get(i).get("default_value");
                            if(stencil_content!=null){
                                stencil_content = stencil_content.replace("[OD]", String.valueOf(od));
                                stencil_content = stencil_content.replace("[WT]", String.valueOf(wt));
                                stencil_content = stencil_content.replace("[GRADE]", grade);
                                stencil_content = stencil_content.replace("[CONTRACTNO]", contract_no);
                                stencil_content = stencil_content.replace("[COATINGSPEC]", coating_standard);
                                stencil_content = stencil_content.replace("[CLIENTSPEC]", client_spec);
                                stencil_content = stencil_content.replace("[PROJECTNAME]", project_name);
                                stencil_content = stencil_content.replace("[PIPENO]", pipe_no);
                                stencil_content = stencil_content.replace("[PIPELENGTH]", String.valueOf(p_length));
                                stencil_content = stencil_content.replace("[HALFLENGTH]", String.valueOf(halflength));
                                stencil_content = stencil_content.replace("[HEATNO]",heat_no);
                                stencil_content = stencil_content.replace("[BATCHNO]",pipe_making_lot_no);
                                stencil_content = stencil_content.replace("[WEIGHT]",String.valueOf(kg));
                                stencil_content = stencil_content.replace("[COATINGDATE]",coatint_date);
                                aclist.get(i).put("default_value",stencil_content);
                            }
                        }

                    }

                    //替换
                    resultMaps.put("criteria",aclist);
                }
            }

            resultMaps.put("success",true);
            resultMaps.put("message","成功");
            String map= JSONObject.toJSONString(resultMaps);
            return map;
        }else{
            resultMaps.put("success",false);
            resultMaps.put("message","失败");
            String map= JSONObject.toJSONString(resultMaps);
            return map;
        }
    }

    //APP根据项目编号获取试验信息
    @RequestMapping(value = "/getLabTestingInfoByProjectNo")
    @ResponseBody
    public String getLabTestingInfoByProjectNo(HttpServletRequest request) {
        String project_no=request.getParameter("project_no");
        String page= request.getParameter("page");
        String rows= request.getParameter("rows");
        if(page==null){
            page="1";
        }
        if(rows==null){
            rows="20";
        }
        int start=(Integer.parseInt(page)-1)*Integer.parseInt(rows);
        List<HashMap<String,Object>>list=inspectionProcessRecordHeaderDao.getLabTestingInfoByProjectNo(project_no,start,Integer.parseInt(rows));

        for(int i=0; list!=null&&i<list.size();i++){
            String testing_type=(String)list.get(i).get("testing_type");
            if(testing_type!=null){
                String pipeno=(String)list.get(i).get("pipe_no");
                //System.out.println("pipeno="+pipeno);
                //System.out.println("testing_type="+testing_type);
                    if(testing_type.equals("is_sample")){
                        InspectionProcessRecordHeader header =inspectionProcessRecordHeaderDao.getRecentRecordByPipeNo("lab_testing_od_regular",pipeno);
                        if(header!=null){
                            list.get(i).put("operation_time",header.getOperation_time());
                            //System.out.println("pipeno="+pipeno+"is_sample");
                            list.get(i).put("testing_result",header.getResult());
                        }
                    }
                    else if(testing_type.equals("is_dsc_sample")){
                        InspectionProcessRecordHeader header =inspectionProcessRecordHeaderDao.getRecentRecordByPipeNo("lab_testing_dsc",pipeno);
                        if(header!=null){
                            //System.out.println("pipeno="+pipeno+"is_dsc_sample");
                            list.get(i).put("operation_time",header.getOperation_time());
                            list.get(i).put("testing_result",header.getResult());
                        }
                    }
                    else if(testing_type.equals("is_pe_sample")){
                        InspectionProcessRecordHeader header =inspectionProcessRecordHeaderDao.getRecentRecordByPipeNo("lab_testing_pe",pipeno);
                        if(header!=null){
                            //System.out.println("pipeno="+pipeno+"is_pe_sample");
                            list.get(i).put("operation_time",header.getOperation_time());
                            list.get(i).put("testing_result",header.getResult());
                        }
                    }
                    else if(testing_type.equals("is_steel_sample")){
                        InspectionProcessRecordHeader header =inspectionProcessRecordHeaderDao.getRecentRecordByPipeNo("lab_testing_id_regular",pipeno);
                        if(header!=null){
                            list.get(i).put("testing_result",header.getResult());
                            list.get(i).put("operation_time",header.getOperation_time());
                        }
                    }
                    else if(testing_type.equals("is_glass_sample")){
                        InspectionProcessRecordHeader header =inspectionProcessRecordHeaderDao.getRecentRecordByPipeNo("lab_testing_glass",pipeno);
                        if(header!=null){
                            list.get(i).put("testing_result",header.getResult());
                            list.get(i).put("operation_time",header.getOperation_time());
                        }
                    }

            }
        }

        Map<String, Object> maps = new HashMap<String, Object>();
        if (list!=null&&list.size()>0) {
            //是待定状态
            maps.put("success",true);
            maps.put("record",list);
        } else {
            maps.put("success", false);
        }
        String mmp= JSONArray.toJSONString(maps);
        return mmp;
    }

    //APP根据项目编号获取第一个根管子的信息
    @RequestMapping(value = "/getFirstPipeNoByProjectNo")
    @ResponseBody
    public String getFirstPipeNoByProjectNo(HttpServletRequest request) {
        String project_no=request.getParameter("project_no");
        String external_coating= request.getParameter("external_coating");
        String internal_coating= request.getParameter("internal_coating");
        System.out.println("project_no="+project_no);
        System.out.println("external_coating="+external_coating);
        System.out.println("internal_coating="+internal_coating);

        PipeBasicInfo p=pipeBasicInfoDao.getFirstPipeNoByProjectNo(project_no,external_coating,internal_coating);
        Map<String, Object> maps = new HashMap<String, Object>();
        if(p!=null)
        {
            System.out.println("p="+p.getPipe_no());
            maps.put("success",true);
            maps.put("record",p);
        }
        else{
            maps.put("success",false);
            maps.put("message","无符合的管号");
        }


        String mmp= JSONArray.toJSONString(maps);
        return mmp;
    }




//    private Map<String,HashMap<String,Object>> getInspectionFrequency(String pipe_no,String mill_no,String process_code){
//        ///////得到本次检验频率
//
//        List<InspectionTimeRecord> lt=inspectionTimeRecordDao.getRecordByPipeNoMillNo(pipe_no,mill_no,null);
//        //List<HashMap<String,Object>> ltif= inspectionFrequencyDao.getFrequencyInfoByPipeNo(pipe_no);
//
//        List<HashMap<String,Object>> ltif= inspectionFrequencyDao.getFrequencyInfoByPipeNo(pipe_no);
//
//
//        Map<String,HashMap<String,Object>> maps=new HashMap<String,HashMap<String,Object>>();
//        Date now=new Date();
//
//        if(ltif.size()>0){
//            HashMap<String,Object> insmap=new HashMap<String,Object>();
//            insmap=ltif.get(0);
//
//            Iterator iter = insmap.entrySet().iterator();		//获取key和value的set
//            while (iter.hasNext()) {//迭代inspectionFreq
//                Map.Entry entry = (Map.Entry) iter.next();		//把hashmap转成Iterator再迭代到entry
//                String key = (String)entry.getKey();		//从entry获取key
//                if(key.equals("id")||key.equals("inspection_frequency_no"))
//                    continue;
//                //System.out.println("key="+key);
//                float freq = (float)entry.getValue();	//从entry获取value
//                HashMap<String,Object> m=new HashMap<String,Object>();
//                boolean needInspectNow=true;
//                String lastInspectionTime="";
//                for(int i=0;i<lt.size();i++){
//                    InspectionTimeRecord timeRecord=lt.get(i);
//                    if(timeRecord.getInspection_item().equals(key)){
//                        //找到检验记录了
//                        //检验频率 秒
//                        float freqSec=freq*60*60;
//                        lastInspectionTime=timeRecord.getInspction_time().toString();
//                        //间隔秒
//                        long interval = (now.getTime() - timeRecord.getInspction_time().getTime())/1000;
//
//                        if(interval<freqSec){
//                            //间隔小于检验频率，不需要检验
//                            needInspectNow=false;
//                        }
//                        break;
//                    }
//                }
//
//                m.put("lastInspectionTime",lastInspectionTime);
//                m.put("needInspectNow",needInspectNow);
//                m.put("InspectionItem",key);
//                maps.put(key,m);
//            }
//
//        }
//        return maps;
//    }

    //根据项目编号获取原材料实验标准
//    @RequestMapping("/getRawMaterialCriteriaByProjecteNo")
//    @ResponseBody
//    public String getRawMaterialCriteriaByProjecteNo(HttpServletRequest request){
//        String project_no=request.getParameter("project_no");
//        Map<String,Object> resultMaps=new HashMap<String,Object>();//最终返回Map
//        //返回用户session数据
//        HttpSession session = request.getSession();
//        //把用户数据保存在session域对象中
//        String employeeno=(String)session.getAttribute("userSession");
//        if(employeeno!=null) {
//            resultMaps.put("employeeno",employeeno);
//        } else{
//            resultMaps.put("success",false);
//            resultMaps.put("message","session已过期，请重新登录");
//            String map= JSONObject.toJSONString(resultMaps);
//            return map;
//        }
//        if(project_no!=null&&project_no!=""){
//            //2FBE 原材料实验标准
//            RawMaterialTestingAcceptanceCriteria2Fbe raw2fbecriteria=rawMaterialTestingAcceptanceCriteria2FbeDao.getRawMaterialStandard2FbeByProjectNo(project_no);
//            if(raw2fbecriteria!=null){
//                resultMaps.put("raw2fbecriteria",raw2fbecriteria);
//            }else{
//                resultMaps.put("raw2fbecriteria","");
//            }
//            //3LPE 原材料实验标准
//            RawMaterialTestingAcceptanceCriteria3Lpe raw3lpecriteria=rawMaterialTestingAcceptanceCriteria3LpeDao.getRawMaterialStandard3LpeByProjectNo(project_no);
//            if(raw3lpecriteria!=null){
//                resultMaps.put("raw3lpecriteria",raw3lpecriteria);
//            }else{
//                resultMaps.put("raw3lpecriteria","");
//            }
//            resultMaps.put("success",true);
//            resultMaps.put("message","成功");
//            String map= JSONObject.toJSONString(resultMaps);
//            return map;
//        }else{
//            return  null;
//        }
//    }


}
