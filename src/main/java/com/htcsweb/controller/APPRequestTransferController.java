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
    private ODCoatingAcceptanceCriteriaDao odcoatingacceptancecriteriaDao;
    @Autowired
    private IDCoatingAcceptanceCriteriaDao idcoatingacceptancecriteriaDao;

    @Autowired
    private PipeBasicInfoDao pipeBasicInfoDao;

    @Autowired
    private InspectionTimeRecordDao inspectionTimeRecordDao;

    @Autowired
    private InspectionFrequencyDao inspectionFrequencyDao;

    @Autowired
    private PipeBodyAcceptanceCriteriaDao pipeBodyAcceptanceCriteriaDao;

    @Autowired
    private CoatingPowderInfoDao coatingPowderInfoDao;

    @Autowired
    LabTestingAcceptanceCriteria2FbeDao labTestingAcceptanceCriteria2FbeDao;

    @Autowired
    LabTestingAcceptanceCriteria3LpeDao labTestingAcceptanceCriteria3LpeDao;

    @Autowired
    RawMaterialTestingAcceptanceCriteria2FbeDao rawMaterialTestingAcceptanceCriteria2FbeDao;

    @Autowired
    RawMaterialTestingAcceptanceCriteria3LpeDao rawMaterialTestingAcceptanceCriteria3LpeDao;



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
                    urloptions.put("odblastprocess", "od/odblast");
                    urloptions.put("barepipemovement", "storage/barepipemovement");
                    urloptions.put("instoragetransfer", "storage/instoragetransfer");
                    //urloptions.put("productStockout", "storage/productionstockout");
                } else if (status.equals("od1")) {
                    urloptions.put("odblastinspectionprocess", "od/odblastinspection");
                } else if (status.equals("od2")) {
                    if (external_coating.equals("2FBE")) {
                        urloptions.put("odcoatingprocess", "od/odcoating2FBE");
                    } else if (external_coating.equals("3LPE")) {
                        urloptions.put("odcoating3lpeprocess", "od/odcoating3LPE");
                    }
                } else if (status.equals("od3")) {
                    if (external_coating.equals("2FBE")) {
                        urloptions.put("odcoatinginspectionprocess", "od/odcoatinginspection2FBE");
                    } else if (external_coating.equals("3LPE")) {
                        urloptions.put("odcoating3lpeinspectionprocess", "od/odcoatinginspection3LPE");
                    }
                } else if (status.equals("od4")) {
                    urloptions.put("odstencilprocess", "od/odstencil");
                } else if (status.equals("od5")) {
                    urloptions.put("odfinalinspectionprocess", "od/odfinalinspection");
                } else if (status.equals("od6")) {
                    urloptions.put("stockin", "storage/stockin");
                } else if (status.equals("odstockin")) {
                    urloptions.put("idblastprocess", "id/idblast");
                    urloptions.put("instoragetransfer", "storage/instoragetransfer");

                    //不需要倒棱
                    if (rebevel_mark == null || !rebevel_mark.equals("1")) {
                        //urloptions.put("productStockout", "storage/productionstockout");
                    }

                }
                //内防
                else if (status.equals("bare2")) {
                    urloptions.put("idblastprocess", "id/idblast");
                    urloptions.put("barepipemovement", "storage/barepipemovement");
                    urloptions.put("instoragetransfer", "storage/instoragetransfer");
                    //urloptions.put("productStockout", "storage/productionstockout");
                } else if (status.equals("id1")) {
                    urloptions.put("idblastinspectionprocess", "id/idblastinspection");
                } else if (status.equals("id2")) {
                    urloptions.put("idcoatingprocess", "id/idcoating");
                } else if (status.equals("id3")) {
                    urloptions.put("idcoatinginspectionprocess", "id/idcoatinginspection");
                } else if (status.equals("id4")) {
                    urloptions.put("idstencilprocess", "id/idstencil");
                } else if (status.equals("id5")) {
                    urloptions.put("idfinalinspectionprocess", "id/idfinalinspection");
                } else if (status.equals("id6")) {
                    urloptions.put("stockin", "storage/stockin");
                } else if (status.equals("idstockin")) {
                    urloptions.put("instoragetransfer", "storage/instoragetransfer");

                    //不需要倒棱
                    if (rebevel_mark == null || !rebevel_mark.equals("1")) {
                        //urloptions.put("productStockout", "storage/productionstockout");
                    }
                }

                //修补
                else if (status.equals("odrepair1") || status.equals("odrepair2") || status.equals("idrepair1") || status.equals("idrepair2")) {
                    urloptions.put("coatingrepair", "addition/coatingrepair");
                }

                //扒皮
                else if (status.equals("odstrip1") || status.equals("idstrip1")) {
                    urloptions.put("coatingstrip", "addition/coatingstrip");
                }

                //修磨或切割
                else if (status.equals("onhold")) {
                    urloptions.put("barepipegrindingProcess", "addition/barepipegrinding");
                }

                if (rebevel_mark != null && rebevel_mark.equals("1")){
                    //需倒棱
                    urloptions.put("pipeRebevelProcess", "addition/piperebevel");
                }
                if(odsampling_mark!=null&&odsampling_mark.equals("0")){
                    //需外防取样
                    urloptions.put("pipeSamplingProcess", "addition/pipesampling");
                }
                if(odsampling_mark!=null&&odsampling_mark.equals("1")||oddscsampling_mark!=null&&oddscsampling_mark.equals("1")||odpesample_mark!=null&&odpesample_mark.equals("1")){
                    //可以做外防实验  odsampling_mark为1时代表取样完毕，其他实验不需要切割取样
                    if (external_coating.equals("2FBE")) {
                        urloptions.put("labtesting2fbe", "labtesting/labtesting2fbe");
                    } else if (external_coating.equals("3LPE")) {
                        urloptions.put("labtesting3lpe", "labtesting/labtesting3lpe");
                    }
                }
                if(idsampling_mark!=null&&idsampling_mark.equals("1")||idglasssample_mark!=null&&idglasssample_mark.equals("1")){
                    //可以做内防实验
                    if (internal_coating.equals("EPOXY")) {
                        urloptions.put("labtestingepoxy", "labtesting/labtestingepoxy");
                    }
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
                //Object val = entry.getValue();
                    //判断是否有权限
                    if(functionMap!=null){

                        if(!functionMap.containsKey(key)&&!key.equals("stockin")) {
                            System.out.println("APP 不存在存在页面"+key+"的权限");
                            removeList.add(key);

                        }
                        //stockin包含 odstockin  idstockin  有其一权限即可
                        else if(key.equals("stockin")&&!functionMap.containsKey("odstockin")&&!functionMap.containsKey("idstockin")){
                            System.out.println("APP 不存在存在页面"+key+"的权限");
                            removeList.add(key);
                            continue;
                        }

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
    @RequestMapping("/getAllProcessInfoByPipeNo")
    @ResponseBody
    public String getAllProcessInfoByPipeNo(HttpServletRequest request){
        //AcceptanceCriteriaOperation/getODAcceptanceCriteriaByPipeNo.action?pipe_no=1524540&&mill_no=mill_2
        String pipe_no=request.getParameter("pipe_no");
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


        if(pipe_no!=null&&pipe_no!=""){
            //钢管信息导出
            List<HashMap<String,Object>> pipelist= pipeBasicInfoDao.getPipeInfoByNo(pipe_no);
            if(pipelist.size()>0){
                resultMaps.put("pipeinfo",pipelist.get(0));
            }else{
                resultMaps.put("pipeinfo","");
            }

            //外防标准导出
            ODCoatingAcceptanceCriteria odcriteria=odcoatingacceptancecriteriaDao.getODAcceptanceCriteriaByPipeNo(pipe_no);
            if(pipelist.size()>0&&odcriteria!=null){
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

                //替换
                String stencil_content=(String)odcriteria.getStencil_content();
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
                stencil_content = stencil_content.replace("[COATINGDATE]",od_coating_dateString);
                odcriteria.setStencil_content(stencil_content);

                resultMaps.put("odcriteria",odcriteria);
            }else{
                resultMaps.put("odcriteria","");
            }

            //检验频率导出
            Map<String,HashMap<String,Object>> maps=getInspectionFrequency(pipe_no,mill_no);
            resultMaps.put("inspectfreq",maps);

            //内防标准导出
            IDCoatingAcceptanceCriteria idcriteria=idcoatingacceptancecriteriaDao.getIDAcceptanceCriteriaByPipeNo(pipe_no);
            if(pipelist.size()>0&&idcriteria!=null){
                float od=(float)pipelist.get(0).get("od");
                float wt=(float)pipelist.get(0).get("wt");
                float p_length=(float)pipelist.get(0).get("p_length");
                String pipe_making_lot_no=(String)pipelist.get(0).get("pipe_making_lot_no");
                float kg=(float)pipelist.get(0).get("weight")*1000;
                Date id_coating_date=(Date)pipelist.get(0).get("id_coating_date");
                String id_coating_dateString="";
                if(id_coating_date!=null) {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    id_coating_dateString = formatter.format(id_coating_date);
                }

                //替换
                String stencil_content=(String)idcriteria.getStencil_content();
                stencil_content = stencil_content.replace("[OD]", String.valueOf(od));
                stencil_content = stencil_content.replace("[WT]", String.valueOf(wt));
                stencil_content = stencil_content.replace("[PIPENO]", pipe_no);
                stencil_content = stencil_content.replace("[PIPELENGTH]", String.valueOf(p_length));
                stencil_content = stencil_content.replace("[BATCHNO]",pipe_making_lot_no);
                stencil_content = stencil_content.replace("[WEIGHT]",String.valueOf(kg));
                stencil_content = stencil_content.replace("[COATINGDATE]",id_coating_dateString);
                idcriteria.setStencil_content(stencil_content);
                resultMaps.put("idcriteria",idcriteria);
            }else{
                resultMaps.put("idcriteria","");
            }

            //光管接收标准
            PipeBodyAcceptanceCriteria pbcriteria=pipeBodyAcceptanceCriteriaDao.getPipeBodyAcceptanceCriteriaByPipeNo(pipe_no);
            if(pbcriteria!=null){
                resultMaps.put("pbcriteria",pbcriteria);
            }else{
                resultMaps.put("pbcriteria","");
            }

            //2FBE实验标准
            LabTestingAcceptanceCriteria2Fbe lab2fbecriteria=labTestingAcceptanceCriteria2FbeDao.getLabTestCriteria2FbeByPipeNo(pipe_no);
            if(lab2fbecriteria!=null){
                resultMaps.put("lab2fbecriteria",lab2fbecriteria);
            }else{
                resultMaps.put("lab2fbecriteria","");
            }

            //3LPE实验标准
            LabTestingAcceptanceCriteria3Lpe lab3lpecriteria=labTestingAcceptanceCriteria3LpeDao.getLabTestCriteria3LpeByPipeNo(pipe_no);
            if(lab3lpecriteria!=null){
                resultMaps.put("lab3lpecriteria",lab3lpecriteria);
            }else{
                resultMaps.put("lab3lpecriteria","");
            }

            //2FBE 原材料实验标准
            RawMaterialTestingAcceptanceCriteria2Fbe raw2fbecriteria=rawMaterialTestingAcceptanceCriteria2FbeDao.getRawMaterialStandard2FbeByPipeNo(pipe_no);
            if(raw2fbecriteria!=null){
                resultMaps.put("raw2fbecriteria",raw2fbecriteria);
            }else{
                resultMaps.put("raw2fbecriteria","");
            }

            //3LPE 原材料实验标准
            RawMaterialTestingAcceptanceCriteria3Lpe raw3lpecriteria=rawMaterialTestingAcceptanceCriteria3LpeDao.getRawMaterialStandard3LpeByPipeNo(pipe_no);
            if(raw3lpecriteria!=null){
                resultMaps.put("raw3lpecriteria",raw3lpecriteria);
            }else{
                resultMaps.put("raw3lpecriteria","");
            }





            resultMaps.put("success",true);
            resultMaps.put("message","成功");
            String map= JSONObject.toJSONString(resultMaps);
            return map;
        }else{
            return  null;
        }
    }



    private Map<String,HashMap<String,Object>> getInspectionFrequency(String pipe_no,String mill_no){
        ///////得到本次检验频率

        List<InspectionTimeRecord> lt=inspectionTimeRecordDao.getRecordByPipeNoMillNo(pipe_no,mill_no,null);
        List<HashMap<String,Object>> ltif= inspectionFrequencyDao.getFrequencyInfoByPipeNo(pipe_no);
        Map<String,HashMap<String,Object>> maps=new HashMap<String,HashMap<String,Object>>();
        Date now=new Date();

        if(ltif.size()>0){
            HashMap<String,Object> insmap=new HashMap<String,Object>();
            insmap=ltif.get(0);

            Iterator iter = insmap.entrySet().iterator();		//获取key和value的set
            while (iter.hasNext()) {//迭代inspectionFreq
                Map.Entry entry = (Map.Entry) iter.next();		//把hashmap转成Iterator再迭代到entry
                String key = (String)entry.getKey();		//从entry获取key
                if(key.equals("id")||key.equals("inspection_frequency_no"))
                    continue;
                //System.out.println("key="+key);
                float freq = (float)entry.getValue();	//从entry获取value
                HashMap<String,Object> m=new HashMap<String,Object>();
                boolean needInspectNow=true;
                String lastInspectionTime="";
                for(int i=0;i<lt.size();i++){
                    InspectionTimeRecord timeRecord=lt.get(i);
                    if(timeRecord.getInspection_item().equals(key)){
                        //找到检验记录了
                        //检验频率 秒
                        float freqSec=freq*60*60;
                        lastInspectionTime=timeRecord.getInspction_time().toString();
                        //间隔秒
                        long interval = (now.getTime() - timeRecord.getInspction_time().getTime())/1000;

                        if(interval<freqSec){
                            //间隔小于检验频率，不需要检验
                            needInspectNow=false;
                        }
                        break;
                    }
                }

                m.put("lastInspectionTime",lastInspectionTime);
                m.put("needInspectNow",needInspectNow);
                m.put("InspectionItem",key);
                maps.put(key,m);
            }

        }
        return maps;
    }




}
