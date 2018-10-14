package com.htcsweb.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.htcsweb.dao.*;
import com.htcsweb.entity.*;
import com.htcsweb.util.*;
import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.format.Orientation;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/InspectionRecordPDFOperation")
public class InspectionRecordPDFController {
    DecimalFormat decimalFormat=new DecimalFormat(".00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
    String basePath="";
    String logoImageFullName="";
    String pdfDirPath="";
    String fontPath="",copyrightFontPath="";
    String pdfFullName="";
    WritableFont wf=null;
    WritableCellFormat wcf=null;
    JSONArray processInputOutputArray=new JSONArray();
    public InspectionRecordPDFController(){
        //String path=this.getClass().getClassLoader().getResource("../../").getPath();
        basePath= this.getClass().getClassLoader().getResource("../../").getPath();
        if(basePath.lastIndexOf('/')==-1){
            basePath=basePath.replace('\\','/');
        }
        String webPath=basePath;
        basePath = basePath.substring(0, basePath.lastIndexOf('/'));
        if(UploadFileController.isServerTomcat) {//若果是tomcat需要重新定义upload的入口
            basePath = basePath.substring(0, basePath.lastIndexOf('/'));
        }
        logoImageFullName=webPath + "/template/img/image002.jpg";
        fontPath=webPath+"/font/simhei.ttf";
        copyrightFontPath=webPath+"/font/simsun.ttc,1";
        pdfDirPath=basePath+"/upload/pdf/";
        pdfFullName=basePath + "/upload/pdf/DailyProductionRecord.pdf";
        File pdfDirFile=new File(pdfDirPath);
        if(!pdfDirFile.exists()){
            pdfDirFile.mkdir();
        }
        try{
            wf = new WritableFont(WritableFont.createFont("Arial"), 8);
            wcf= new WritableCellFormat(wf);
            wcf.setAlignment(Alignment.RIGHT);
            wcf.setVerticalAlignment(VerticalAlignment.JUSTIFY);
            wcf.setWrap(true);
            wcf.setBackground(Colour.RED);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Autowired
    private MillInfoDao millInfoDao;

    @Autowired
    private ContractInfoDao contractInfoDao;
    @Autowired
    private ProjectInfoDao projectInfoDao;

    @Autowired
    private InspectionProcessRecordHeaderDao inspectionProcessRecordHeaderDao;
    /**
     * 获取PDF生成进度
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="getPDFProgress",produces="application/json;charset=UTF-8")
    @ResponseBody
    public  String getPDFProgress(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json=new JSONObject();
        try{

            HttpSession session = request.getSession();
            String pdfProgress=(String)session.getAttribute("pdfProgress");
            if(pdfProgress==null||pdfProgress.equals(""))
                pdfProgress="0";
            //跳转到用户主页
            json.put("success",true);
            //System.out.println("ggggggete getAttribute pdfProgress：" + pdfProgress);    //输出程序运行时间
            json.put("pdfProgress",pdfProgress);
            ResponseUtil.write(response,json);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 生成岗位报表(pdf格式)
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="getRecordReportPDF",produces="application/json;charset=UTF-8")
    @ResponseBody
    public  String getRecordReportPDF(HttpServletRequest request, HttpServletResponse response){
        readprocessInputOutPutJson(request);
        String basePath=request.getSession().getServletContext().getRealPath("/");
        if(basePath.lastIndexOf('/')==-1){
            basePath=basePath.replace('\\','/');
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeformat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //定义根据项目编号获取的合同集合
        List<ContractInfo>contractInfoList=null;
        //定义工位的生成pdf的集合
        List<String>stationOdList=new ArrayList<String>();
        List<String>stationIdList=new ArrayList<String>();
        List<String>delSetPath=new ArrayList<String>();//定义删除pdf集合，用于生成zip后删除所有的临时文件
        if(UploadFileController.isServerTomcat) {
            basePath = basePath.substring(0, basePath.lastIndexOf('/'));
            basePath = basePath.substring(0, basePath.lastIndexOf('/'));

        }
        Date start_time=null;
        Date finish_time=null;
        String pdfSetOdDayPath="",pdfSetOdNightPath="",pdfSetIdDayPath="",pdfSetIdNightPath="";
        Date day_begin_time=null,day_end_time=null,night_begin_time=null,night_end_time=null;//定义每天班次的开始结束时间
        //是否成功标识
        String flag="success",zipName="";
        boolean success=false;
        String message="";
        //HashMap<String,Object>map=new HashMap<>();
        //先获取选择的日期区间之间的所有日期
        String project_no=request.getParameter("project_no");
        String project_name=request.getParameter("project_name");
        String beginTimeStr=request.getParameter("beginTime");
        String endTimeStr=request.getParameter("endTime");
        List<String>dayNightPdf=new ArrayList<>();
        String pdfOdPath="",pdfIdPath="";
        //岗位报表pdf命名格式：项目名＋分厂＋日期＋内／外防＋班次+ 规格 + 涂层类型
        long startTime = System.currentTimeMillis();//获取开始时间
        if(project_no!=null&&!project_no.equals("")&&project_name!=null&&!project_name.equals("")&&beginTimeStr!=null&&!beginTimeStr.equals("")&&endTimeStr!=null&&!endTimeStr.equals("")){
            try{
                contractInfoList=getStandardAndCoatingTypeList(project_no);
                //先清理.zip垃圾文件
                FileRenameUtil.cleanTrashFiles(basePath);
                //定义pdf头部所需的信息
                //根据项目编号获取项目信息
                String client_standard=" ";
                List<ProjectInfo>projectInfoList=projectInfoDao.getProjectInfoByProjectNo(project_no);
                if(projectInfoList.size()>0){
                    ProjectInfo projectInfo=projectInfoList.get(0);
                    client_standard=projectInfo.getClient_spec()+" "+projectInfo.getCoating_standard();
                }
                //1.－－－－－－－－获取所有分厂集合
                List<MillInfo>millList=millInfoDao.getAllMillInfo();
                //2.－－－－－－－－时间区间集合
                start_time=sdf.parse(beginTimeStr);
                finish_time=sdf.parse(endTimeStr);
                List<String>listDate= DateTimeUtil.getBetweenDates(start_time,finish_time);

                //4.－－－－－－－－班次集合
                List<String>shiftList=new ArrayList<>();
                shiftList.add("白班");
                shiftList.add("夜班");
                //5.－－－－－－－－获取所有规格集合、涂层类型
                int totalPDFCount=listDate.size()*millList.size()*contractInfoList.size()*shiftList.size();
                int n=0;
                HttpSession session = request.getSession();
                session.setAttribute("pdfProgress", String.valueOf(0));
                String pipe_size="";
                //6.-------开始生成笛卡尔集pdf，并填充pdf
                for (MillInfo millInfo:millList){//分厂
                    for (String recordTime:listDate){//日期
                        for (ContractInfo contractInfo:contractInfoList){//规格和涂层类型
                            for (String shift0:shiftList){
                                pipe_size="Φ"+decimalFormat.format(contractInfo.getOd())+"*"+decimalFormat.format(contractInfo.getWt())+"mm";
                                if(shift0.equals("白班")){
                                    start_time=timeformat.parse(recordTime+" 08:00:00");
                                    finish_time=timeformat.parse(recordTime+" 20:00:00");
                                } else{
                                    start_time=timeformat.parse(recordTime+" 20:00:00");
                                    finish_time=timeformat.parse(DateTimeUtil.getNextDay(recordTime)+" 08:00:00");
                                }
                                //外防
                                pdfOdPath=basePath+"/upload/pdf/"+(project_name+"_"+millInfo.getMill_name()+"_"+recordTime+"_外防_"+shift0+"(Day)_"+pipe_size+"_"+contractInfo.getExternal_coating()+"_"+UUID.randomUUID().toString()+".pdf");
                                System.out.println(pdfOdPath+"------------------------");
                                File file0=new File(pdfOdPath);
                                if(!file0.exists()){
                                    file0.createNewFile();
                                }
                                //开始填充pdf
                                //6.1.1---------外防生成封面PDF
                                newCreateCoverOneOd(request,project_no,project_name,millInfo.getMill_no(),millInfo.getMill_name(), pipe_size,client_standard,contractInfo.getExternal_coating(),null,contractInfo.getOd(),contractInfo.getWt(),shift0,recordTime,start_time,finish_time,stationOdList,delSetPath);
                                newCreateCoverTwo(request,0,project_no,project_name,millInfo.getMill_no(),millInfo.getMill_name(), pipe_size,client_standard,contractInfo.getExternal_coating(),null,contractInfo.getOd(),contractInfo.getWt(),shift0,recordTime,start_time,finish_time,stationOdList,delSetPath);
                                //新报表生成
                                String []od_process_code={"od_blast:钢管外喷砂记录 Pipe OD Blast Record",
                                        "od_blast_inspection:钢管外喷砂检验记录 Pipe OD Blast Inspection Record",
                                        "od_coating:钢管外喷涂记录 Pipe OD Coating Record",
                                        "od_coating_inspection:钢管外喷涂检验记录 Pipe OD Coating Inspection Record",
                                        "od_final_inspection:钢管外防终检记录 Pipe OD Final Inspection Record"};
                                for (int i=0;i<od_process_code.length;i++){
                                    String []str=od_process_code[i].split(":");
                                    PostRecordPdf(request,str[0],str[1],project_no,project_name,millInfo.getMill_no(),millInfo.getMill_name(), pipe_size,client_standard,contractInfo.getExternal_coating(),contractInfo.getOd(),contractInfo.getWt(),shift0,recordTime,start_time,finish_time,stationOdList,delSetPath);
                                }
                                if(stationOdList.size()>0){
                                    MergePDF.MergePDFs(stationOdList,pdfOdPath);
                                    stationOdList.clear();
                                    dayNightPdf.add(pdfOdPath);
                                }
                                //内防
                                pdfIdPath=basePath+"/upload/pdf/"+(project_name+"_"+millInfo.getMill_name()+"_"+recordTime+"_内防_"+shift0+"(Day)_"+pipe_size+"_"+contractInfo.getInternal_coating()+"_"+UUID.randomUUID().toString()+".pdf");
                                File file1=new File(pdfIdPath);
                                //开始填充pdf
                                //6.2.1---------内防生成封面PDF
                                newCreateCoverOneId(request,project_no,project_name,millInfo.getMill_no(),millInfo.getMill_name(), pipe_size,client_standard,contractInfo.getInternal_coating(),contractInfo.getOd(),contractInfo.getWt(),shift0,recordTime,start_time,finish_time,stationIdList,delSetPath);
                                newCreateCoverTwo(request,1,project_no,project_name,millInfo.getMill_no(),millInfo.getMill_name(), pipe_size,client_standard,contractInfo.getExternal_coating(),contractInfo.getInternal_coating(),contractInfo.getOd(),contractInfo.getWt(),shift0,recordTime,start_time,finish_time,stationIdList,delSetPath);
                                //新报表生成
                                String []id_process_code={"id_blast_inspection:钢管内喷砂检验记录 Pipe ID Blast Inspection Record",
                                        "id_coating:钢管内喷涂记录 Pipe ID Coating Record",
                                        "id_coating_inspection:钢管内喷涂检验记录 Pipe ID Coating Inspection Record",
                                        "id_final_inspection:钢管内防终检记录 Pipe ID Final Inspection Record"};
                                for (int i=0;i<id_process_code.length;i++){
                                    String []str=id_process_code[i].split(":");
                                    PostRecordPdf(request,str[0],str[1],project_no,project_name,millInfo.getMill_no(),millInfo.getMill_name(), pipe_size,client_standard,contractInfo.getInternal_coating(),contractInfo.getOd(),contractInfo.getWt(),shift0,recordTime,start_time,finish_time,stationIdList,delSetPath);
                                }
                                if(stationIdList.size()>0){
                                    MergePDF.MergePDFs(stationIdList,pdfIdPath);
                                    stationIdList.clear();
                                    dayNightPdf.add(pdfIdPath);
                                }
                                //计算
                                n+=1;
                                //把用户数据保存在session域对象中
                                float percent=0;
                                if(totalPDFCount!=0)
                                    percent=n*100/totalPDFCount;
                                session.setAttribute("pdfProgress", String.valueOf(percent));
                                System.out.println("percent：" + percent);
                                System.out.println("n：" + n);
                                System.out.println("totalPDFCount：" + totalPDFCount);
                            }
                        }
                    }
                }
                //-------结束生成笛卡尔集pdf
                if(dayNightPdf.size()==0){
                    success=false;
                    message="没有岗位记录";
                }else{
                    success=true;
                    message="存在岗位记录"+String.valueOf(dayNightPdf.size())+"份";
                }
                zipName="/upload/pdf/"+ResponseUtil.downLoadPdf(dayNightPdf,request,response);
                dayNightPdf.clear();
                //定时删除临时文件
                for (int j=0;j<delSetPath.size();j++){
                    if(delSetPath.get(j)!=null){
                        File file=new File(delSetPath.get(j));
                        if(file.exists()){
                            file.delete();
                        }
                    }
                }
                session.setAttribute("pdfProgress",100);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        Map<String,Object> maps=new HashMap<String,Object>();
        maps.put("success",success);
        maps.put("zipName",zipName);
        maps.put("message",message);
        String mmp= JSONArray.toJSONString(maps);
        return mmp;
    }

    /**
     * 下载生成的报表
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="downloadPDF",produces="application/json;charset=UTF-8")
    @ResponseBody
    public String downloadPDF(HttpServletRequest request, HttpServletResponse response){
        String pathArr=request.getParameter("pathList");
        String []pathList=pathArr.split(";");
        List<String>dayNightPdf=new ArrayList<>();
        dayNightPdf=Arrays.asList(pathList);
        ResponseUtil.downLoadPdf(dayNightPdf,request,response);
        return null;
    }
    /**
     * 读取process_input_output.json(各工序对应的结论信息)配置文件中的信息
     * @param request
     */
    private void readprocessInputOutPutJson(HttpServletRequest request){
        try{
            String jsonFile=request.getSession().getServletContext().getRealPath("/")
                    +"data/process_input_output.json";
            if(jsonFile.lastIndexOf('/')==-1){
                jsonFile=jsonFile.replace('\\','/');
            }
            InputStreamReader reader=new InputStreamReader(new FileInputStream(jsonFile),"UTF-8");
            BufferedReader bufferedReader=new BufferedReader(reader);
            String tempStr="",s="";
            while ((s=bufferedReader.readLine())!=null){
                tempStr+=s;
            }
            processInputOutputArray=JSON.parseArray(tempStr);
        }catch (Exception ex){
             ex.printStackTrace();
        }
    }
    /**
     * 根据process_input_output.json配置文件中的信息解析结论
     * @param process_code(工序编码)
     * @param result(结论)
     * @return
     */
    private String getResult(String process_code,String result){
        String resultName="";
        for (int i=0;i<processInputOutputArray.size();i++){
            JSONObject object=processInputOutputArray.getJSONObject(i);
            String process_code_now=object.getString("process_code");
            if(process_code_now.equals(process_code)){
                JSONArray outputArray=JSON.parseArray(object.getString("output"));
                for (int j=0;j<outputArray.size();j++){
                    JSONObject outputObject=outputArray.getJSONObject(j);
                    String result_now=outputObject.getString("result");
                    if(result_now!=null&&result_now.equals(result)){
                        resultName=outputObject.getString("result_name");
                    }
                }
            }
        }
        if(resultName.equals("")){
            resultName=result;
        }
        return resultName;
    }
    /**
     * 获取岗位记录pdf
     * @param request
     * @param process_code(工序编码)
     * @param process_name(工序名称)
     * @param project_no(项目编码)
     * @param project_name(项目名称)
     * @param mill_no(分厂号)
     * @param mill_name(分厂名)
     * @param pipe_size(外径和壁厚的组合)
     * @param standard(客户标准)
     * @param coatingType(外涂类型)
     * @param od(外径)
     * @param wt(壁厚)
     * @param shift(班次)
     * @param title_time(记录日期)
     * @param begin_time(开始日期)
     * @param end_time(结束日期)
     * @param stringList(存放临时pdf路径集合)
     * @param delSetPath(待删除文件集合)
     */
    private void  PostRecordPdf(HttpServletRequest request,String process_code,String process_name,String project_no,String project_name,String mill_no,String mill_name,String pipe_size,String standard,String coatingType,float od,float wt,String shift,String title_time,Date begin_time,Date end_time,List<String>stringList,List<String>delSetPath){
        String templateFullName=request.getSession().getServletContext().getRealPath("/")
                +"template/general_process_record_template.xls";
        if(templateFullName.lastIndexOf('/')==-1){
            templateFullName=templateFullName.replace('\\','/');
        }
        String newPdfName=null;
        try{
            List<HashMap<String,Object>>list=inspectionProcessRecordHeaderDao.getPostRecord(process_code,project_no,mill_no,coatingType ,od,wt,begin_time,end_time);
            ArrayList<Label> datalist=new ArrayList<Label>();
            //如果数据不为空
            if(list.size()>0){
                List<HashMap<String,Object>>list1=new ArrayList<>();
                String unit_name="",last_unit_name="",header_code="",last_header_code="",last_name="",temp_item_name="",temp_item_value="",pipe_no="",last_pipe_no="",measure_item="",last_measure_item="",item_remark="",last_item_remark="",item_result="",last_item_result="";
                int count=0;
                for (int i=0;i<list.size();i++){
                    header_code=list.get(i).get("inspection_process_record_header_code").toString();
                    pipe_no=list.get(i).get("pipe_no").toString();
                    temp_item_name=list.get(i).get("item_name").toString();
                    temp_item_value=list.get(i).get("item_value").toString();
                    if(temp_item_value==null||temp_item_value.equals(""))
                        temp_item_value=" ";
                    item_result=(list.get(i).get("result")==null)?" ":list.get(i).get("result").toString();
                    item_remark=(list.get(i).get("remark")==null)?" ":list.get(i).get("remark").toString();
                    unit_name=(list.get(i).get("unit_name_en")==null)?" ":list.get(i).get("unit_name_en").toString();
                    if(!header_code.equals(last_header_code)&&!last_header_code.equals("")){
                        HashMap<String,Object>hs=new HashMap<>();
                        hs.put("header_code",last_header_code);
                        hs.put("pipe_no",last_pipe_no);
                        hs.put("measure_item",last_measure_item);
                        hs.put("last_name",last_name);
                        hs.put("result",last_item_result);
                        hs.put("remark",last_item_remark);
                        list1.add(hs);
                        last_measure_item="";
                        last_name="";
                        count=0;
                    }
                    last_pipe_no=pipe_no;
                    last_header_code=header_code;
                    count++;
                    last_measure_item+=("["+count+"]"+temp_item_value+"    ");
                    if(unit_name!=null&&!unit_name.equals("")){
                        last_name+=("["+count+"]"+temp_item_name+"("+unit_name+")"+"    ");
                    }else{
                        last_name+=("["+count+"]"+temp_item_name+"    ");
                    }
                    last_unit_name=unit_name;
                    last_item_result=item_result;
                    last_item_remark=item_remark;
                }
                HashMap<String,Object>hs=new HashMap<>();
                hs.put("header_code",last_header_code);
                hs.put("pipe_no",last_pipe_no);
                hs.put("measure_item",last_measure_item);
                hs.put("last_name",last_name);
                hs.put("result",last_item_result);
                hs.put("remark",last_item_remark);
                list1.add(hs);

                int index=1,row=0;
                StringBuilder sb=new StringBuilder();
                String remark="",measure_name=" ";
                int qualifiedTotal=0;
                for (int i=0;i<list1.size();i++){
                       datalist.add(new Label(1, row+8,String.valueOf(list1.get(i).get("pipe_no")), wcf));
                       datalist.add(new Label(2, row+8, String.valueOf(list1.get(i).get("measure_item")).trim(), wcf));
                       datalist.add(new Label(13, row+8,getResult(process_code,String.valueOf(list1.get(i).get("result"))), wcf));
                       measure_name=String.valueOf(list1.get(i).get("last_name"));
                       remark=String.valueOf(list1.get(i).get("remark"));
                       if(!remark.equals("")){
                           sb.append(String.valueOf(list1.get(i).get("pipe_no"))+":"+remark+" ");
                       }
                       if(list1.get(i).get("result")!=null&&String.valueOf(list1.get(i).get("result")).equals("1")){
                           qualifiedTotal++;
                       }
                        index++;
                        row++;
                        //最后一行数据为空问题
                        if(index%13==0){
                            newCreateRecordPdfTitle(datalist,process_name,measure_name,project_name,pipe_size,standard,coatingType,shift,title_time);
                            newCreateRecordPdf(datalist,newPdfName,templateFullName,qualifiedTotal,index,row,sb.toString(),stringList,delSetPath);
                            sb.setLength(0);
                        }
                }
                if(datalist.size()>0){
                    for (int k=index-1;k<12;k++){
                        datalist.add(new Label(1, k+8," ", wcf));
                        datalist.add(new Label(2, k+8, " ", wcf));
                        datalist.add(new Label(13, k+8, " ", wcf));
                    }
                    newCreateRecordPdfTitle(datalist,process_name,measure_name,project_name,pipe_size,standard,coatingType,shift,title_time);
                    newCreateRecordPdf(datalist,newPdfName,templateFullName,qualifiedTotal,index,row,sb.toString(),stringList,delSetPath);
                    sb.setLength(0);
                }
            }else{
                newCreateRecordNullPdf(datalist,process_name," ",project_name,pipe_size,standard,coatingType,shift,title_time,newPdfName,templateFullName,stringList,delSetPath);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * 生成外防封面
     * @param request
     * @param project_no(项目编号)
     * @param project_name(项目名称)
     * @param mill_no(分厂号)
     * @param mill_name(分厂名称)
     * @param pipe_size(外径和壁厚的组合)
     * @param standard(客户标准)
     * @param external_coating(外涂类型)
     * @param internal_coating(内涂类型)
     * @param od(外径)
     * @param wt(壁厚)
     * @param shift(班次)
     * @param title_time(报表日期)
     * @param begin_time(开始日期)
     * @param end_time(结束日期)
     * @param stringList(存放临时pdf路径集合)
     * @param delSetPath(待删除文件集合)
     */
    public void  newCreateCoverOneOd(HttpServletRequest request,String project_no,String project_name,String mill_no,String mill_name,String pipe_size,String standard,String external_coating,String internal_coating,float od,float wt,String shift,String title_time,Date begin_time,Date end_time,List<String>stringList,List<String>delSetPath){
        //获取试验管
        String templateFullName=request.getSession().getServletContext().getRealPath("/")
                +"template/online_production_inspection_record_list_cover_1_od.xls";
        if(templateFullName.lastIndexOf('/')==-1){
            templateFullName=templateFullName.replace('\\','/');
        }
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String newPdfName=null;
        try{
            int odCoatingCount=0,odAcceptedPipeCount=0,odRepairPipeCount=0,odRejectedPipeCount=0,odOnholdPipeCount=0;
            //coatingCount代表防腐数，acceptedPipeCount代表防腐合格数，repairPipeCount代表修补数，rejectedPipeCount代表废管数，onholdPipeCount代表隔离数
            odCoatingCount=inspectionProcessRecordHeaderDao.getODCoatingCount(project_no,mill_no,external_coating,null,od,wt,begin_time,end_time);
            List<HashMap<String,Object>>list1=inspectionProcessRecordHeaderDao.getODCoatingAcceptedInfo(project_no,mill_no,external_coating,null,od,wt,begin_time,end_time,"1");
            if(list1!=null&&list1.size()>0){
                HashMap<String,Object>hs=list1.get(0);
                if(hs!=null){
                    odAcceptedPipeCount=((Long) hs.get("odtotalcount")).intValue();
                }
            }
            odRepairPipeCount=inspectionProcessRecordHeaderDao.getCoatingRepairCount(project_no,mill_no,external_coating,null,"od",od,wt,begin_time,end_time);
            odRejectedPipeCount=inspectionProcessRecordHeaderDao.getODCoatingRejectedPipeCount(project_no,mill_no,external_coating,null,od,wt,begin_time,end_time);
            odOnholdPipeCount=inspectionProcessRecordHeaderDao.getODBarePipeOnholdCount(project_no,mill_no,external_coating,null,od,wt,begin_time,end_time);
            //获取试验管管号和切样长度
            List<HashMap<String,Object>>sampleList=inspectionProcessRecordHeaderDao.getCoverPipeSamplingInfo(project_no,mill_no,od,wt,begin_time,end_time);
            //固化度试验管管号
            List<HashMap<String,Object>>dscList=new ArrayList<>();
            //PE实验管号
            List<HashMap<String,Object>>peList=new ArrayList<>();
            //dscList=inspectionProcessRecordHeaderDao.getCover2FBEDscTestingInfo(project_no,mill_no,od,wt,begin_time,end_time);
            dscList = inspectionProcessRecordHeaderDao.getCover2FBEDscTestingInfo(project_no, mill_no, od, wt, begin_time, end_time);
            peList = inspectionProcessRecordHeaderDao.getCover3LPEPETestingInfo(project_no, mill_no, od, wt, begin_time, end_time);
            List<HashMap<String,String>>labPipenoList=new ArrayList<>();
            int samplePipeCount=0,dscPipeCount=0,pePipeCount=0;
            if(sampleList!=null)
                samplePipeCount=sampleList.size();
            if(dscList!=null)
                dscPipeCount=dscList.size();
            if(peList!=null)
                pePipeCount=peList.size();
            int maxSize=samplePipeCount;
            if(maxSize<dscPipeCount){
                maxSize=dscPipeCount;
            }
            if(maxSize<pePipeCount){
                maxSize=pePipeCount;
            }
            List<CoverOneRecordOD>recordODList=new ArrayList<>();
            for (int i=0;i<maxSize;i++)
            {
                CoverOneRecordOD recordOD=new CoverOneRecordOD();
                if(i<samplePipeCount)
                {
                    if(sampleList.get(i).get("pipe_no")!=null&&!sampleList.get(i).get("pipe_no").toString().equals(""))
                        recordOD.setTestSampleNo(sampleList.get(i).get("pipe_no").toString());
                    else
                        recordOD.setTestSampleNo(" ");
                    if(sampleList.get(i).get("cut_off_length")!=null&&!sampleList.get(i).get("cut_off_length").toString().equals(""))
                        recordOD.setCutLength(sampleList.get(i).get("cut_off_length").toString());
                    else
                        recordOD.setCutLength(" ");
                }else{
                    recordOD.setTestSampleNo(" ");recordOD.setCutLength(" ");
                }
                if(i<dscPipeCount){
                    if(dscList.get(i).get("pipe_no")!=null&&!dscList.get(i).get("pipe_no").toString().equals(""))
                        recordOD.setDscSampleNo(dscList.get(i).get("pipe_no").toString());
                    else
                        recordOD.setDscSampleNo(" ");
                }else{
                    recordOD.setDscSampleNo(" ");
                }
                if(i<pePipeCount){
                    if(peList.get(i).get("pipe_no")!=null&&!peList.get(i).get("pipe_no").toString().equals(""))
                        recordOD.setPeSampleNo(peList.get(i).get("pipe_no").toString());
                    else
                        recordOD.setPeSampleNo(" ");
                }else{
                    recordOD.setPeSampleNo(" ");
                }
                recordODList.add(recordOD);
            }
            ArrayList<Label> datalist=new ArrayList<Label>();
            if(recordODList.size()>0){
                int index=1,row=0;
                for (int i=0;i<recordODList.size();i++){
                    datalist.add(new Label(1,row+10,recordODList.get(i).getTestSampleNo() ,wcf));
                    datalist.add(new Label(4,row+10, recordODList.get(i).getCutLength(),wcf));
                    datalist.add(new Label(8,row+10,recordODList.get(i).getDscSampleNo() ,wcf));
                    datalist.add(new Label(9,row+10,recordODList.get(i).getPeSampleNo() ,wcf));
                    index++;
                    if(index%9==0){
                        createRecordPdfTitle(datalist,3,8,12,4,5,project_name,pipe_size,standard,external_coating,shift,title_time);
                        datalist.add(new Label(11,20,mill_name,wcf));
                        datalist.add(new Label(2,6,String.valueOf(odCoatingCount),wcf));
                        datalist.add(new Label(5,6,String.valueOf(odAcceptedPipeCount),wcf));
                        datalist.add(new Label(7,6,String.valueOf(odRepairPipeCount),wcf));
                        datalist.add(new Label(9,6,String.valueOf(odRejectedPipeCount),wcf));
                        datalist.add(new Label(12,6,String.valueOf(odOnholdPipeCount),wcf));
                        newPdfName=GenerateExcelToPDFUtil.PDFAutoMation(templateFullName,datalist,pdfFullName,logoImageFullName,fontPath,copyrightFontPath);
                        index=1;row=0;
                        datalist.clear();
                        stringList.add(newPdfName);
                        delSetPath.add(newPdfName);
                    }
                }
                if(datalist.size()>0){
                    createRecordPdfTitle(datalist,3,8,12,4,5,project_name,pipe_size,standard,external_coating,shift,title_time);
                    datalist.add(new Label(11,20,mill_name,wcf));
                    datalist.add(new Label(2,6,String.valueOf(odCoatingCount),wcf));
                    datalist.add(new Label(5,6,String.valueOf(odAcceptedPipeCount),wcf));
                    datalist.add(new Label(7,6,String.valueOf(odRepairPipeCount),wcf));
                    datalist.add(new Label(9,6,String.valueOf(odRejectedPipeCount),wcf));
                    datalist.add(new Label(12,6,String.valueOf(odOnholdPipeCount),wcf));
                    newPdfName=GenerateExcelToPDFUtil.PDFAutoMation(templateFullName,datalist,pdfFullName,logoImageFullName,fontPath,copyrightFontPath);
                    index=1;row=0;
                    datalist.clear();
                    stringList.add(newPdfName);
                    delSetPath.add(newPdfName);
                }
            }else{
                createRecordPdfTitle(datalist,3,8,12,4,5,project_name,pipe_size,standard,external_coating,shift,title_time);
                datalist.add(new Label(11,20,mill_name,wcf));
                datalist.add(new Label(2,6,String.valueOf(odCoatingCount),wcf));
                datalist.add(new Label(5,6,String.valueOf(odAcceptedPipeCount),wcf));
                datalist.add(new Label(7,6,String.valueOf(odRepairPipeCount),wcf));
                datalist.add(new Label(9,6,String.valueOf(odRejectedPipeCount),wcf));
                datalist.add(new Label(12,6,String.valueOf(odOnholdPipeCount),wcf));
                datalist.add(new Label(1,10,"今天暂无记录!",wcf));
                newPdfName=GenerateExcelToPDFUtil.PDFAutoMation(templateFullName,datalist,pdfFullName,logoImageFullName,fontPath,copyrightFontPath);
                if(newPdfName!=null){
                    stringList.add(newPdfName);
                    delSetPath.add(newPdfName);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * 生成内防封面
     * @param request
     * @param project_no(项目编号)
     * @param project_name(项目名称)
     * @param mill_no(分厂号)
     * @param mill_name(分厂名称)
     * @param pipe_size(外径和壁厚的组合)
     * @param standard(客户标准)
     * @param internal_coating(内涂类型)
     * @param od(外径)
     * @param wt(壁厚)
     * @param shift(班次)
     * @param title_time(报表日期)
     * @param begin_time(开始日期)
     * @param end_time(结束日期)
     * @param stringList(存放临时pdf路径集合)
     * @param delSetPath(待删除文件集合)
     */
    public void  newCreateCoverOneId(HttpServletRequest request,String project_no,String project_name,String mill_no,String mill_name,String pipe_size,String standard,String internal_coating,float od,float wt, String shift,String title_time,Date begin_time,Date end_time,List<String>stringList,List<String>delSetPath){
        //获取试验管
        String templateFullName=request.getSession().getServletContext().getRealPath("/")
                +"template/online_production_inspection_record_list_cover_1_id.xls";
        if(templateFullName.lastIndexOf('/')==-1){
            templateFullName=templateFullName.replace('\\','/');
        }
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String newPdfName=null;
        try{
            int idCoatingCount=0,idAcceptedPipeCount=0,idRepairPipeCount=0,idRejectedPipeCount=0,idOnholdPipeCount=0;
            //coatingCount代表防腐数，acceptedPipeCount代表防腐合格数，repairPipeCount代表修补数，rejectedPipeCount代表废管数，onholdPipeCount代表隔离数
            idCoatingCount=inspectionProcessRecordHeaderDao.getIDCoatingCount(project_no,mill_no,null,internal_coating,od,wt,begin_time,end_time);
            List<HashMap<String,Object>>list1=inspectionProcessRecordHeaderDao.getIDCoatingAcceptedInfo(project_no,mill_no,null,internal_coating,od,wt,begin_time,end_time,"1");
            if(list1!=null&&list1.size()>0){
                HashMap<String,Object>hs=list1.get(0);
                if(hs!=null){
                    idAcceptedPipeCount=((Long)hs.get("idtotalcount")).intValue();
                }
            }
            idRepairPipeCount=inspectionProcessRecordHeaderDao.getCoatingRepairCount(project_no,mill_no,null,internal_coating,"id",od,wt,begin_time,end_time);
            idRejectedPipeCount=inspectionProcessRecordHeaderDao.getIDCoatingRejectedPipeCount(project_no,mill_no,null,internal_coating,od,wt,begin_time,end_time);
            idOnholdPipeCount=inspectionProcessRecordHeaderDao.getODBarePipeOnholdCount(project_no,mill_no,null,internal_coating,od,wt,begin_time,end_time);
            //获取钢试片管号和玻璃试片管号
            List<HashMap<String,Object>>sampleInfoList=inspectionProcessRecordHeaderDao.getCoverIdSampleInfo(project_no,mill_no,od,wt,begin_time,end_time);
            List<HashMap<String,Object>>glassSampleInfoList=inspectionProcessRecordHeaderDao.getCoverIdGlassSampleInfo(project_no,mill_no,od,wt,begin_time,end_time);
            int maxSize=0,sampleCount=0,glassSampleCount=0;
            if(sampleInfoList!=null)
                sampleCount=sampleInfoList.size();
            if(glassSampleInfoList!=null)
                glassSampleCount=glassSampleInfoList.size();
            maxSize=sampleCount>glassSampleCount?sampleCount:glassSampleCount;
            List<CoverOneRecordID>oneRecordIDList=new ArrayList<>();
            for (int i=0;i<maxSize;i++){
                CoverOneRecordID recordID=new CoverOneRecordID();
                if (i<sampleCount){
                    if(sampleInfoList.get(i).get("pipe_no")!=null&&!sampleInfoList.get(i).get("pipe_no").toString().equals(""))
                        recordID.setSteelPanelNo(sampleInfoList.get(i).get("pipe_no").toString());
                    else
                        recordID.setSteelPanelNo(" ");
                }else{
                    recordID.setSteelPanelNo(" ");
                }
                if(i<glassSampleCount){
                    if(glassSampleInfoList.get(i).get("pipe_no")!=null&&!sampleInfoList.get(i).get("pipe_no").toString().equals(""))
                        recordID.setGlassPanelNo(glassSampleInfoList.get(i).get("pipe_no").toString());
                    else
                        recordID.setGlassPanelNo(" ");
                }else{
                    recordID.setGlassPanelNo(" ");
                }
                oneRecordIDList.add(recordID);
            }
            ArrayList<Label> datalist=new ArrayList<Label>();
            if(oneRecordIDList.size()>0){
                int index=1,row=0,column=0;
                for (int i=0;i<oneRecordIDList.size();i++){
                    if(i%2==0){
                        datalist.add(new Label(column+1,row+10,oneRecordIDList.get(i).getSteelPanelNo() ,wcf));
                        datalist.add(new Label(column+4,row+10, oneRecordIDList.get(i).getGlassPanelNo(),wcf));
                    }else{
                        datalist.add(new Label(column+7,row+10,oneRecordIDList.get(i).getSteelPanelNo() ,wcf));
                        datalist.add(new Label(column+10,row+10, oneRecordIDList.get(i).getGlassPanelNo(),wcf));
                        row++;
                    }
                    index++;
                    if(index%19==0){
                        createRecordPdfTitle(datalist,3,8,12,4,5,project_name,pipe_size,standard,internal_coating,shift,title_time);
                        datalist.add(new Label(11,20,mill_name,wcf));
                        datalist.add(new Label(2,6,String.valueOf(idCoatingCount),wcf));
                        datalist.add(new Label(5,6,String.valueOf(idAcceptedPipeCount),wcf));
                        datalist.add(new Label(7,6,String.valueOf(idRepairPipeCount),wcf));
                        datalist.add(new Label(9,6,String.valueOf(idRejectedPipeCount),wcf));
                        datalist.add(new Label(12,6,String.valueOf(idOnholdPipeCount),wcf));
                        newPdfName=GenerateExcelToPDFUtil.PDFAutoMation(templateFullName,datalist,pdfFullName,logoImageFullName,fontPath,copyrightFontPath);
                        index=1;row=0;
                        datalist.clear();
                        stringList.add(newPdfName);
                        delSetPath.add(newPdfName);
                    }
                }
                if(datalist.size()>0){
                    createRecordPdfTitle(datalist,3,8,12,4,5,project_name,pipe_size,standard,internal_coating,shift,title_time);
                    datalist.add(new Label(11,20,mill_name,wcf));
                    datalist.add(new Label(2,6,String.valueOf(idCoatingCount),wcf));
                    datalist.add(new Label(5,6,String.valueOf(idAcceptedPipeCount),wcf));
                    datalist.add(new Label(7,6,String.valueOf(idRepairPipeCount),wcf));
                    datalist.add(new Label(9,6,String.valueOf(idRejectedPipeCount),wcf));
                    datalist.add(new Label(12,6,String.valueOf(idOnholdPipeCount),wcf));
                    newPdfName=GenerateExcelToPDFUtil.PDFAutoMation(templateFullName,datalist,pdfFullName,logoImageFullName,fontPath,copyrightFontPath);
                    index=1;row=0;
                    datalist.clear();
                    stringList.add(newPdfName);
                    delSetPath.add(newPdfName);
                }
            }else{
                createRecordPdfTitle(datalist,3,8,12,4,5,project_name,pipe_size,standard,internal_coating,shift,title_time);
                datalist.add(new Label(11,20,mill_name,wcf));
                datalist.add(new Label(2,6,String.valueOf(idCoatingCount),wcf));
                datalist.add(new Label(5,6,String.valueOf(idAcceptedPipeCount),wcf));
                datalist.add(new Label(7,6,String.valueOf(idRepairPipeCount),wcf));
                datalist.add(new Label(9,6,String.valueOf(idRejectedPipeCount),wcf));
                datalist.add(new Label(12,6,String.valueOf(idOnholdPipeCount),wcf));
                datalist.add(new Label(1,10,"今天暂无记录!",wcf));
                newPdfName=GenerateExcelToPDFUtil.PDFAutoMation(templateFullName,datalist,pdfFullName,logoImageFullName,fontPath,copyrightFontPath);
                if(newPdfName!=null){
                    stringList.add(newPdfName);
                    delSetPath.add(newPdfName);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * 生成封面2
     * @param request
     * @param project_no(项目编号)
     * @param project_name(项目名称)
     * @param mill_no(分厂号)
     * @param mill_name(分厂名称)
     * @param pipe_size(外径和壁厚的组合)
     * @param standard(客户标准)
     * @param external_coating(外涂类型)
     * @param internal_coating(内涂类型)
     * @param od(外径)
     * @param wt(壁厚)
     * @param shift(班次)
     * @param title_time(报表日期)
     * @param begin_time(开始日期)
     * @param end_time(结束日期)
     * @param stringList(存放临时pdf路径集合)
     * @param delSetPath(待删除文件集合)
     */
    public void  newCreateCoverTwo(HttpServletRequest request,int type,String project_no,String project_name,String mill_no,String mill_name,String pipe_size,String standard,String external_coating,String internal_coating,float od,float wt,String shift,String title_time,Date begin_time,Date end_time,List<String>stringList,List<String>delSetPath){
        String templateFullName=request.getSession().getServletContext().getRealPath("/")
                +"template/online_production_inspection_record_list_cover_2.xls";
        if(templateFullName.lastIndexOf('/')==-1){
            templateFullName=templateFullName.replace('\\','/');
        }
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String newPdfName=null,coatingType=" ";
        List<HashMap<String,Object>>repairList=null;
        List<HashMap<String,Object>>stripList=null;
        List<HashMap<String,Object>>onholdList=null;
        int repairCount=0,stripCount=0,onholdCount=0,maxSize=0;
        try{
            if(type==0){
                //获取外防修补涂层管管号和原因  ?修补前还是修补后
                repairList=inspectionProcessRecordHeaderDao.getCoatingRepairInfo(project_no,mill_no,null,null,"od",od,wt,begin_time,end_time);
                //获取外防涂层管废管管号和原因  OK
                stripList=inspectionProcessRecordHeaderDao.getODCoatingRejectedPipeInfo(project_no,mill_no,external_coating,null,od,wt,begin_time,end_time);
                //获取外防隔离光管管号和原因  OK
                onholdList=inspectionProcessRecordHeaderDao.getODBarePipeOnholdInfo(project_no,mill_no,external_coating,null,od,wt,begin_time,end_time);
                if(external_coating!=null&&!external_coating.equals(""))
                    coatingType=external_coating;
            }else{
                //获取内防修补涂层管管号和原因  ?修补前还是修补后
                repairList=inspectionProcessRecordHeaderDao.getCoatingRepairInfo(project_no,mill_no,null,null,"id",0,0,begin_time,end_time);
                //获取内防涂层管废管管号和原因  OK
                stripList=inspectionProcessRecordHeaderDao.getIDCoatingRejectedPipeInfo(project_no,mill_no,null,internal_coating,od,wt,begin_time,end_time);
                //获取内防隔离光管管号和原因  OK
                onholdList=inspectionProcessRecordHeaderDao.getIDBarePipeOnholdInfo(project_no,mill_no,null,internal_coating,od,wt,begin_time,end_time);
                if(internal_coating!=null&&!internal_coating.equals(""))
                    coatingType=internal_coating;
            }
            if(repairList!=null)
                repairCount=repairList.size();
            if(stripList!=null)
                stripCount=stripList.size();
            if(onholdList!=null)
                onholdCount=onholdList.size();
            maxSize=repairCount>=stripCount?repairCount:stripCount;
            maxSize=maxSize>=onholdCount?maxSize:onholdCount;
            List<CoverTwoRecord>coverRecordList=new ArrayList<>();
            for (int i=0;i<maxSize;i++){
                CoverTwoRecord record=new CoverTwoRecord();
                if(i<repairCount){
                    if(repairList.get(i).get("pipe_no")!=null&&!repairList.get(i).get("pipe_no").equals(""))
                        record.setRepairNo(String.valueOf(repairList.get(i).get("pipe_no")));
                    if(repairList.get(i).get("remark")!=null&&!repairList.get(i).get("remark").equals(""))
                        record.setRepairRemark(String.valueOf(repairList.get(i).get("remark")));
                }else{
                    record.setRepairNo(" ");
                    record.setRepairRemark(" ");
                }
                if(i<stripCount)
                {
                    if(stripList.get(i).get("pipe_no")!=null&&!stripList.get(i).get("pipe_no").toString().equals(""))
                        record.setStripNo(String.valueOf(stripList.get(i).get("pipe_no")));
                    else
                        record.setStripNo(" ");
                    if(stripList.get(i).get("remark")!=null&&!stripList.get(i).get("remark").toString().equals(""))
                        record.setStripRemark(String.valueOf(stripList.get(i).get("remark")));
                    else
                        record.setStripRemark(" ");
                }else{
                    record.setStripNo(" ");
                    record.setStripRemark(" ");
                }
                if(i<onholdCount)
                {
                    if(onholdList.get(i).get("pipe_no")!=null&&!onholdList.get(i).get("pipe_no").toString().equals(""))
                        record.setOnholdNo(String.valueOf(onholdList.get(i).get("pipe_no")));
                    else
                        record.setOnholdNo(" ");
                    if(onholdList.get(i).get("remark")!=null&&!onholdList.get(i).get("remark").toString().equals(""))
                        record.setOnholdRemark(String.valueOf(onholdList.get(i).get("remark")));
                    else
                        record.setOnholdRemark(" ");
                }else{
                    record.setOnholdNo(" ");
                    record.setOnholdRemark(" ");
                }
                coverRecordList.add(record);
            }
            ArrayList<Label> datalist=new ArrayList<Label>();
            if(coverRecordList.size()>0){
                int index=1,row=0,column=0;
                for (int i=0;i<coverRecordList.size();i++){
                    if(i%2==0){
                        datalist.add(new Label(column+1,row+8,coverRecordList.get(i).getRepairNo() ,wcf));
                        datalist.add(new Label(column+2,row+8, coverRecordList.get(i).getRepairRemark(),wcf));
                        datalist.add(new Label(column+3,row+8,coverRecordList.get(i).getStripNo() ,wcf));
                        datalist.add(new Label(column+4,row+8,coverRecordList.get(i).getStripRemark(),wcf));
                        datalist.add(new Label(column+5,row+8,coverRecordList.get(i).getOnholdNo() ,wcf));
                        datalist.add(new Label(column+6,row+8,coverRecordList.get(i).getOnholdRemark() ,wcf));
                    }else{
                        datalist.add(new Label(column+7,row+8,coverRecordList.get(i).getRepairNo() ,wcf));
                        datalist.add(new Label(column+8,row+8, coverRecordList.get(i).getRepairRemark(),wcf));
                        datalist.add(new Label(column+9,row+8,coverRecordList.get(i).getStripNo() ,wcf));
                        datalist.add(new Label(column+10,row+8,coverRecordList.get(i).getStripRemark(),wcf));
                        datalist.add(new Label(column+11,row+8,coverRecordList.get(i).getOnholdNo() ,wcf));
                        datalist.add(new Label(column+12,row+8,coverRecordList.get(i).getOnholdRemark() ,wcf));
                        row++;
                    }
                    index++;
                    if(index%23==0){
                        createRecordPdfTitle(datalist,3,8,12,4,5,project_name,pipe_size,standard,coatingType,shift,title_time);
                        datalist.add(new Label(12,20,mill_name,wcf));
                        newPdfName=GenerateExcelToPDFUtil.PDFAutoMation(templateFullName,datalist,pdfFullName,logoImageFullName,fontPath,copyrightFontPath);
                        index=1;row=0;
                        datalist.clear();
                        stringList.add(newPdfName);
                        delSetPath.add(newPdfName);
                    }
                }
                if(datalist.size()>0){
                    createRecordPdfTitle(datalist,3,8,12,4,5,project_name,pipe_size,standard,coatingType,shift,title_time);
                    datalist.add(new Label(12,20,mill_name,wcf));
                    newPdfName=GenerateExcelToPDFUtil.PDFAutoMation(templateFullName,datalist,pdfFullName,logoImageFullName,fontPath,copyrightFontPath);
                    index=1;row=0;
                    datalist.clear();
                    stringList.add(newPdfName);
                    delSetPath.add(newPdfName);
                }
            }else{
                datalist.add(new Label(12,20,mill_name,wcf));
                createRecordNullPdf(datalist,1,3,8,12,4,5,8,project_name,pipe_size,standard,coatingType,shift,title_time,newPdfName,templateFullName,stringList,delSetPath);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * 填补空白Bug
     * @param datalist(Excel填充数据集合)
     * @param remark(备注)
     * @param wcf(表格类型)
     */
    private void AddLastWhiteSpace(ArrayList<Label> datalist,String remark,WritableCellFormat wcf){
        datalist.add(new Label(2,20,remark,wcf));
    }
    /**
     * 导出外观检验记录PDF
     * @param request
     * @return
     */
    @RequestMapping(value = "/exportBlastRecordToPDF",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String exportBlastRecordToPDF(HttpServletRequest request)  {
        File pdfDirFile=new File(pdfDirPath);
        if(!pdfDirFile.exists()){
            pdfDirFile.mkdir();
        }
        String templateFullName=request.getSession().getServletContext().getRealPath("/")
                +"template/od_blast_record_template.xls";
        if(templateFullName.lastIndexOf('/')==-1){
            templateFullName=templateFullName.replace('\\','/');
        }
        //这里设置datalist参数
        ArrayList<Label> datalist=new ArrayList<Label>();
        //根据报告的内容格式设置
        /*
         * 设置单元格字体，还有很多其他样式，不一一列举
         */
        WritableFont wf = new WritableFont(WritableFont.createFont("Arial"), 10);
        /*
         * 设置单元格样式并添加字体样式
         */
        WritableCellFormat wcf = new WritableCellFormat(wf);
        // 设置内容居中
        try {
            wcf.setAlignment(Alignment.CENTRE);
            // 设置单元格的背景颜色
            wcf.setBackground(jxl.format.Colour.RED);

            /*
             * 在创建单元格的时候使用样式
             */
        }catch (Exception e){

        }
        return "";
    }
    /**
     * 当报表数据为空的时候pdf数据填充,
     * 参数(column1:project_name、standard所在列,column2:pipe_size、coating_type所在列,
     * column3:班次、时间所在列,column4:空记录所在列
     * row1:project_name、pipe_size、时间所在行,row2:standard、coating_type、班次所在行,row3:空记录所在行)
     * @param datalist(填充数据集合)
     * @param process_name(工序名称)
     * @param measure_name(测量项名称)
     * @param title_project_name(项目名称)
     * @param title_pipe_size(外径壁厚组合)
     * @param title_standard(客户标准)
     * @param title_coating_type(涂层类型)
     * @param title_shift(班次)
     * @param title_time(日期)
     * @param PdfName(生成pdf名称)
     * @param templateFullName(模板路径)
     * @param stringList(存放临时pdf路径集合)
     * @param delSetPath(待删除文件集合)
     */
    private void newCreateRecordNullPdf(ArrayList<Label> datalist,String process_name,String measure_name,String title_project_name,String title_pipe_size,String title_standard,String title_coating_type,String title_shift,String title_time,String PdfName,String templateFullName,List<String>stringList,List<String>delSetPath){
        datalist.add(new Label(3, 4,title_project_name, wcf));
        datalist.add(new Label(9, 4,title_pipe_size, wcf));
        datalist.add(new Label(13, 4,title_time, wcf));
        datalist.add(new Label(3, 5,title_standard, wcf));
        datalist.add(new Label(9, 5,title_coating_type, wcf));
        datalist.add(new Label(13, 5,title_shift, wcf));
        datalist.add(new Label(0, 3,process_name, wcf));
        datalist.add(new Label(2, 7,measure_name, wcf));
        for (int i=0;i<12;i++){
            datalist.add(new Label(1, i+8," ", wcf));
            datalist.add(new Label(2, i+8, " ", wcf));
            datalist.add(new Label(13, i+8, " ", wcf));
        }
        datalist.add(new Label(1,8,"今天暂无记录!",wcf));
        datalist.add(new Label(2, 20," ", wcf));
        datalist.add(new Label(13,20," ",wcf));
        datalist.add(new Label(12, 21,"©2018 TopInspector", wcf));
        PdfName=GenerateExcelToPDFUtil.PDFAutoMation(templateFullName,datalist,pdfFullName,logoImageFullName,fontPath,copyrightFontPath);
        if(PdfName!=null){
            stringList.add(PdfName);
            delSetPath.add(PdfName);
        }
    }
    /**
     * 当报表数据为空的时候封面2的pdf数据填充
     * @param datalist(填充数据集合)
     * @param column1(excel列)
     * @param column2(excel列)
     * @param column3(excel列)
     * @param column4(excel列)
     * @param row1(excel行)
     * @param row2(excel行)
     * @param row3(excel行)
     * @param title_project_name(项目名称)
     * @param title_pipe_size(外径壁厚组合)
     * @param title_standard(客户标准)
     * @param title_coating_type(涂层类型)
     * @param title_shift(班次)
     * @param title_time(日期)
     * @param PdfName(生成pdf名称)
     * @param templateFullName(模板路径)
     * @param stringList(存放临时pdf路径集合)
     * @param delSetPath(待删除文件集合)
     */
    private void createRecordNullPdf(ArrayList<Label> datalist,int column1,int column2,int column3,int column4,int row1,int row2,int row3,String title_project_name,String title_pipe_size,String title_standard,String title_coating_type,String title_shift,String title_time,String PdfName,String templateFullName,List<String>stringList,List<String>delSetPath){
        datalist.add(new Label(column2, row1,title_project_name, wcf));
        datalist.add(new Label(column3, row1,title_pipe_size, wcf));
        datalist.add(new Label(column4, row1, title_time, wcf));
        datalist.add(new Label(column2, row2,title_standard, wcf));
        datalist.add(new Label(column3, row2,title_coating_type, wcf));
        datalist.add(new Label(column4, row2,title_shift, wcf));
        datalist.add(new Label(column1,row3,"今天暂无记录!",wcf));
        datalist.add(new Label(11, 21,"©2018 TopInspector", wcf));
        PdfName=GenerateExcelToPDFUtil.PDFAutoMation(templateFullName,datalist,pdfFullName,logoImageFullName,fontPath,copyrightFontPath);
        if(PdfName!=null){
            stringList.add(PdfName);
            delSetPath.add(PdfName);
        }
    }
    /**
     * 封面2的头部数据填充
     * @param datalist(填充数据集合)
     * @param column1(excel列)
     * @param column2(excel列)
     * @param column3(excel列)
     * @param row1(excel行)
     * @param row2(excel行)
     * @param title_project_name(项目名称)
     * @param title_pipe_size(外径壁厚组合)
     * @param title_standard(客户标准)
     * @param title_coating_type(涂层类型)
     * @param title_shift(班次)
     * @param title_time(日期)
     */
    private void createRecordPdfTitle(ArrayList<Label> datalist,int column1,int column2,int column3,int row1,int row2,String title_project_name,String title_pipe_size,String title_standard,String title_coating_type,String title_shift,String title_time){
        datalist.add(new Label(column1, row1,title_project_name, wcf));
        datalist.add(new Label(column2, row1,title_pipe_size, wcf));
        datalist.add(new Label(column1, row2,title_standard, wcf));
        datalist.add(new Label(column2, row2,title_coating_type, wcf));
        datalist.add(new Label(column3, row2,title_shift, wcf));
        datalist.add(new Label(column3, row1,title_time, wcf));
        datalist.add(new Label(11, 21,"©2018 TopInspector", wcf));
    }
    /**
     * 检验记录pdf头部数据填充
     * @param datalist(填充数据集合)
     * @param process_name(工序名称)
     * @param measure_name(测量项名称)
     * @param title_project_name(项目名称)
     * @param title_pipe_size(外径壁厚组合)
     * @param title_standard(客户标准)
     * @param title_coating_type(涂层类型)
     * @param title_shift(班次)
     * @param title_time(日期)
     */
    private void newCreateRecordPdfTitle(ArrayList<Label> datalist,String process_name,String measure_name,String title_project_name,String title_pipe_size,String title_standard,String title_coating_type,String title_shift,String title_time){
        datalist.add(new Label(3, 4,title_project_name, wcf));
        datalist.add(new Label(9, 4,title_pipe_size, wcf));
        datalist.add(new Label(13, 4,title_time, wcf));
        datalist.add(new Label(3, 5,title_standard, wcf));
        datalist.add(new Label(9, 5,title_coating_type, wcf));
        datalist.add(new Label(13, 5,title_shift, wcf));
        datalist.add(new Label(0, 3,process_name, wcf));
        datalist.add(new Label(2, 7,measure_name, wcf));//"\\u00a9"
        datalist.add(new Label(12, 21,"©2018 TopInspector", wcf));
    }
    /**
     * 检验记录pdf生成
     * @param datalist(填充数据集合)
     * @param newPdfName(pdf名称)
     * @param templateFullName(模板路径)
     * @param qualifiedTotal(要重置的合格数)
     * @param index(要重置的遍历标记)
     * @param row(要重置的行位置)
     * @param remark(备注)
     * @param stringList(存放临时pdf路径集合)
     * @param delSetPath(待删除文件集合)
     */
    private void newCreateRecordPdf(ArrayList<Label> datalist,String newPdfName,String templateFullName,int qualifiedTotal,int index,int row,String remark,List<String>stringList,List<String>delSetPath){
        if(remark.equals(""))
            remark=" ";
        datalist.add(new Label(2,20,remark,wcf));
        //添加合格数
        datalist.add(new Label(13,20,String.valueOf(qualifiedTotal),wcf));
        newPdfName=GenerateExcelToPDFUtil.PDFAutoMation(templateFullName,datalist,pdfFullName,logoImageFullName,fontPath,copyrightFontPath);
        datalist.clear();
        qualifiedTotal=0;
        index=1;
        row=0;
        if(newPdfName!=null){
            stringList.add(newPdfName);
            delSetPath.add(newPdfName);
        }
    }
//    private void createRecordPdf(ArrayList<Label> datalist,String newPdfName,String templateFullName,int qualifiedTotal,int x1,int y1,int x2,int y2,int index,int row,StringBuilder sb,List<String>stringList,List<String>delSetPath){
//        datalist.add(new Label(x1,y1,String.valueOf(sb.toString()),wcf));
//        //添加合格数
//        datalist.add(new Label(x2,y2,String.valueOf(qualifiedTotal),wcf));
//        newPdfName=GenerateExcelToPDFUtil.PDFAutoMation(templateFullName,datalist,pdfFullName,logoImageFullName,fontPath,copyrightFontPath);
//        datalist.clear();
//        qualifiedTotal=0;
//        index=1;
//        row=0;
//        sb.setLength(0);
//        if(newPdfName!=null){
//            stringList.add(newPdfName);
//            delSetPath.add(newPdfName);
//        }
//    }
    /**
     * 数组拆分两部分
     * @param strVal
     * @return
     */
    private List<String>getSplitList(String strVal){
        String leftArr=" ",rightArr=" ";
        List<String>listStr=new ArrayList<>();
        if(strVal!=null){
            String []list=strVal.split(",");
            int listLen=list.length;
            int leftLen=1,rightLen=1;
            if(listLen%2!=0){
                leftLen=(listLen+1)/2;
                rightLen=leftLen-1;
            }else{
                leftLen=listLen/2;
                rightLen=leftLen;
            }

            if(leftLen%2!=0){
                for (int i=0;i<leftLen;i++){
                    if(i!=leftLen-1)
                        leftArr+=list[i]+",";
                    else
                        leftArr+=list[i];
                }
                for (int i=leftLen,j=0;i<listLen;i++,j++){
                    if(i!=leftLen-1)
                        rightArr+=list[i]+",";
                    else
                        rightArr+=list[i];

                }
            }else{
                for (int i=0;i<leftLen;i++){
                    leftArr+=list[i]+",";
                }
                for (int i=leftLen,j=0;i<listLen;i++,j++){
                    rightArr+=list[i]+",";
                }
            }
        }
        if(leftArr.endsWith(",")){
            leftArr=leftArr.substring(0,leftArr.length()-1);
        }
        if(rightArr.endsWith(",")){
            rightArr=rightArr.substring(0,rightArr.length()-1);
        }
        listStr.add(leftArr);
        listStr.add(rightArr);
        return  listStr;
    }
    /**
     * 根据项目编号获取合同对应的标准和涂层集合集合
     * @param project_no(项目编号)
     * @return
     */
    private List<ContractInfo> getStandardAndCoatingTypeList(String project_no){
        List<ContractInfo>contractInfoList=null;
        try{
            contractInfoList=contractInfoDao.getAllContractInfoByProjectNo(project_no);
        }catch (Exception e){
            System.out.println("根据项目编号获取合同对应的标准和涂层集合集合出错!");
        }
        return contractInfoList;
    }
    /**
     * 根据项目编号、分厂号、日期获取当班统计信息(APP使用)
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/RequireShiftInfoForAPP")
    @ResponseBody
    public String RequireShiftInfoForAPP(HttpServletRequest request)throws Exception{
        try{
            String project_no=request.getParameter("project_no");
            String mill_no=request.getParameter("mill_no");
            String operation_time=request.getParameter("operation_time");
            float od=0,wt=0;
            SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Map<String,Object> resultMaps=new HashMap<String,Object>();//最终返回Map
            if((project_no!=null&&project_no!="")&&(mill_no!=null&&mill_no!="")&&(operation_time!=null&&operation_time!="")){
                Date day_begin_time=format.parse(operation_time+" 08:00:00");
                Date day_end_time=format.parse(operation_time+" 20:00:00");
                Date night_begin_time=format.parse(operation_time+" 20:00:00");
                Date night_end_time=format.parse(DateTimeUtil.getNextDay(operation_time)+" 08:00:00");
                List<ContractInfo>contractInfoList=getStandardAndCoatingTypeList(project_no);
                int odCoatingCountOfDay=0,odAcceptedPipeCountOfDay=0,odRepairPipeCountOfDay=0,odRejectedPipeCountOfDay=0,odOnholdPipeCountOfDay=0,
                        odCoatingCountOfNight=0,odAcceptedPipeCountOfNight=0,odRepairPipeCountOfNight=0,odRejectedPipeCountOfNight=0,odOnholdPipeCountOfNight=0;
                //coatingCount代表防腐数，acceptedPipeCount代表防腐合格数，repairPipeCount代表修补数，rejectedPipeCount代表废管数，onholdPipeCount代表隔离数
                int idCoatingCountOfDay=0,idAcceptedPipeCountOfDay=0,idRepairPipeCountOfDay=0,idRejectedPipeCountOfDay=0,idOnholdPipeCountOfDay=0,
                        idCoatingCountOfNight=0,idAcceptedPipeCountOfNight=0,idRepairPipeCountOfNight=0,idRejectedPipeCountOfNight=0,idOnholdPipeCountOfNight=0;
                //coatingCount代表防腐数，acceptedPipeCount代表防腐合格数，repairPipeCount代表修补数，rejectedPipeCount代表废管数，onholdPipeCount代表隔离数
                List<HashMap<String,Object>>OdSampleListOfDay=new ArrayList<>();
                List<HashMap<String,Object>>OdDscListOfDay=new ArrayList<>();
                List<HashMap<String,Object>>OdPEListOfDay=new ArrayList<>();
                List<HashMap<String,Object>>IDSampleInfoListOfDay=new ArrayList<>();
                List<HashMap<String,Object>>IDGlassSampleInfoListOfDay=new ArrayList<>();
                List<HashMap<String,Object>>OdSampleListOfNight=new ArrayList<>();
                List<HashMap<String,Object>>OdDscListOfNight=new ArrayList<>();
                List<HashMap<String,Object>>OdPEListOfNight=new ArrayList<>();
                List<HashMap<String,Object>>IDSampleInfoListOfNight=new ArrayList<>();
                List<HashMap<String,Object>>IDGlassSampleInfoListOfNight=new ArrayList<>();
                List<HashMap<String,Object>>ODRepairListOfNight=new ArrayList<>();
                List<HashMap<String,Object>>ODStripListOfNight=new ArrayList<>();
                List<HashMap<String,Object>>ODOnholdListOfNight=new ArrayList<>();
                List<HashMap<String,Object>>ODRepairListOfDay=new ArrayList<>();
                List<HashMap<String,Object>>ODStripListOfDay=new ArrayList<>();
                List<HashMap<String,Object>>ODOnholdListOfDay=new ArrayList<>();
                List<HashMap<String,Object>>IDRepairListOfNight=new ArrayList<>();
                List<HashMap<String,Object>>IDStripListOfNight=new ArrayList<>();
                List<HashMap<String,Object>>IDOnholdListOfNight=new ArrayList<>();
                List<HashMap<String,Object>>IDRepairListOfDay=new ArrayList<>();
                List<HashMap<String,Object>>IDStripListOfDay=new ArrayList<>();
                List<HashMap<String,Object>>IDOnholdListOfDay=new ArrayList<>();
                for (ContractInfo contractInfo:contractInfoList){
                    od=contractInfo.getOd();wt=contractInfo.getWt();
                    //-----------获取白班外防信息(dayshiftodinfo)
                    odCoatingCountOfDay+=inspectionProcessRecordHeaderDao.getODCoatingCount(project_no,mill_no,null,null,od,wt,day_begin_time,day_end_time);
                    //获取外防当班合格数
                    List<HashMap<String,Object>>list1=inspectionProcessRecordHeaderDao.getODCoatingAcceptedInfo(project_no,mill_no,null,null,od,wt,day_begin_time,day_end_time,"1");

                    if(list1!=null&&list1.size()>0){
                        HashMap<String,Object>hs=list1.get(0);
                        if(hs!=null){
                            odAcceptedPipeCountOfDay+=((Long) hs.get("odtotalcount")).intValue();
                        }
                    }
                    odRepairPipeCountOfDay+=inspectionProcessRecordHeaderDao.getCoatingRepairCount(project_no,mill_no,null,null,"od",od,wt,day_begin_time,day_end_time);
                    odRejectedPipeCountOfDay+=inspectionProcessRecordHeaderDao.getODCoatingRejectedPipeCount(project_no,mill_no,null,null,od,wt,day_begin_time,day_end_time);

                    odOnholdPipeCountOfDay+=inspectionProcessRecordHeaderDao.getODBarePipeOnholdCount(project_no,mill_no,null,null,od,wt,day_begin_time,day_end_time);
                    //获取试验管管号和切样长度
                    List<HashMap<String,Object>>tempList0=inspectionProcessRecordHeaderDao.getCoverPipeSamplingInfo(project_no,mill_no,od,wt,day_begin_time,day_end_time);


                    if(tempList0!=null&&tempList0.size()>0){}
                      OdSampleListOfDay.addAll(tempList0);//列表合并
                    //固化度试验管管号
                    List<HashMap<String,Object>>tempList1=inspectionProcessRecordHeaderDao.getCover2FBEDscTestingInfo(project_no,mill_no,od,wt,day_begin_time,day_end_time);


                    if(tempList1!=null&&tempList1.size()>0)
                        OdDscListOfDay.addAll(tempList1);
                    //PE实验管号
                    List<HashMap<String,Object>>tempList3 = inspectionProcessRecordHeaderDao.getCover3LPEPETestingInfo(project_no, mill_no, od, wt, day_begin_time, day_end_time);

                    if(tempList3!=null&&tempList3.size()>0)
                        OdPEListOfDay.addAll(tempList3);
                    //获取外防修补涂层管管号和原因  ?修补前还是修补后
                    List<HashMap<String,Object>> tempList12=inspectionProcessRecordHeaderDao.getCoatingRepairInfo(project_no,mill_no,null,null,"od",od,wt,day_begin_time, day_end_time);


                    if(tempList12!=null&&tempList12.size()>0)
                        ODRepairListOfDay.addAll(tempList12);
                    //获取外防涂层管废管管号和原因  OK
                    List<HashMap<String,Object>>tempList13=inspectionProcessRecordHeaderDao.getODCoatingRejectedPipeInfo(project_no,mill_no,null,null,od,wt,day_begin_time, day_end_time);

                    if(tempList13!=null&&tempList13.size()>0)
                        ODStripListOfDay.addAll(tempList13);
                    //获取外防隔离光管管号和原因  OK
                    List<HashMap<String,Object>>tempList14=inspectionProcessRecordHeaderDao.getODBarePipeOnholdInfo(project_no,mill_no,null,null,od,wt,day_begin_time, day_end_time);


                    if(tempList14!=null&&tempList14.size()>0)
                        ODOnholdListOfDay.addAll(tempList14);



                    //-----------获取白班内防信息(dayshiftidinfo)
                    idCoatingCountOfDay+=inspectionProcessRecordHeaderDao.getIDCoatingCount(project_no,mill_no,null,null,od,wt,day_begin_time, day_end_time);
                    List<HashMap<String,Object>>list2=inspectionProcessRecordHeaderDao.getIDCoatingAcceptedInfo(project_no,mill_no,null,null,od,wt,day_begin_time, day_end_time,"1");

                    if(list2!=null&&list2.size()>0){
                        HashMap<String,Object>hs=list2.get(0);
                        if(hs!=null){
                            idAcceptedPipeCountOfDay+=((Long)hs.get("idtotalcount")).intValue();
                        }
                    }
                    idRepairPipeCountOfDay+=inspectionProcessRecordHeaderDao.getCoatingRepairCount(project_no,mill_no,null,null,"id",od,wt,day_begin_time, day_end_time);
                    idRejectedPipeCountOfDay+=inspectionProcessRecordHeaderDao.getIDCoatingRejectedPipeCount(project_no,mill_no,null,null,od,wt,day_begin_time, day_end_time);

                    idOnholdPipeCountOfDay+=inspectionProcessRecordHeaderDao.getIDBarePipeOnholdCount(project_no,mill_no,null,null,od,wt,day_begin_time, day_end_time);


                    //获取钢试片管号和玻璃试片管号
                    List<HashMap<String,Object>>tempList4=inspectionProcessRecordHeaderDao.getCoverIdSampleInfo(project_no,mill_no,od,wt,day_begin_time, day_end_time);

                    if(tempList4!=null&&tempList4.size()>0)
                        IDSampleInfoListOfDay.addAll(tempList4);
                    List<HashMap<String,Object>>tempList5=inspectionProcessRecordHeaderDao.getCoverIdGlassSampleInfo(project_no,mill_no,od,wt,day_begin_time, day_end_time);

                    if(tempList5!=null&&tempList5.size()>0)
                        IDGlassSampleInfoListOfDay.addAll(tempList5);
                    //获取内防修补涂层管管号和原因  ?修补前还是修补后
                    List<HashMap<String,Object>>tempList15=inspectionProcessRecordHeaderDao.getCoatingRepairInfo(project_no,mill_no,null,null,"id",od,wt,day_begin_time, day_end_time);

                    if(tempList15!=null&&tempList15.size()>0)
                        IDRepairListOfDay.addAll(tempList15);
                    //获取内防涂层管废管管号和原因  OK
                    List<HashMap<String,Object>>tempList16=inspectionProcessRecordHeaderDao.getIDCoatingRejectedPipeInfo(project_no,mill_no,null,null,od,wt,day_begin_time, day_end_time);


                    if(tempList16!=null&&tempList16.size()>0)
                        IDStripListOfDay.addAll(tempList16);
                    //获取内防隔离光管管号和原因  OK
                    List<HashMap<String,Object>>tempList17=inspectionProcessRecordHeaderDao.getIDBarePipeOnholdInfo(project_no,mill_no,null,null,od,wt,day_begin_time, day_end_time);

                    if(tempList17!=null&&tempList17.size()>0)
                        IDOnholdListOfDay.addAll(tempList17);

                    //-----------获取夜班外防信息(nightshiftodinfo)
                    odCoatingCountOfNight+=inspectionProcessRecordHeaderDao.getODCoatingCount(project_no,mill_no,null,null,od,wt,night_begin_time,night_end_time);
                    List<HashMap<String,Object>>list3=inspectionProcessRecordHeaderDao.getODCoatingAcceptedInfo(project_no,mill_no,null,null,od,wt,night_begin_time,night_end_time,"1");

                    if(list3!=null&&list3.size()>0){
                        HashMap<String,Object>hs=list3.get(0);
                        if(hs!=null){
                            odAcceptedPipeCountOfNight+=((Long) hs.get("odtotalcount")).intValue();
                        }
                    }
                    odRepairPipeCountOfNight+=inspectionProcessRecordHeaderDao.getCoatingRepairCount(project_no,mill_no,null,null,"od",od,wt,night_begin_time,night_end_time);
                    odRejectedPipeCountOfNight+=inspectionProcessRecordHeaderDao.getODCoatingRejectedPipeCount(project_no,mill_no,null,null,od,wt,night_begin_time,night_end_time);
                    odOnholdPipeCountOfNight+=inspectionProcessRecordHeaderDao.getODBarePipeOnholdCount(project_no,mill_no,null,null,od,wt,night_begin_time,night_end_time);
                    //获取试验管管号和切样长度
                    List<HashMap<String,Object>>tempList6=inspectionProcessRecordHeaderDao.getCoverPipeSamplingInfo(project_no,mill_no,od,wt,night_begin_time,night_end_time);

                    if(tempList6!=null&&tempList6.size()>0){}
                    OdSampleListOfNight.addAll(tempList6);//列表合并
                    //固化度试验管管号
                    List<HashMap<String,Object>>tempList7=inspectionProcessRecordHeaderDao.getCover2FBEDscTestingInfo(project_no,mill_no,od,wt,night_begin_time,night_end_time);

                    if(tempList7!=null&&tempList7.size()>0)
                        OdDscListOfNight.addAll(tempList7);
                    //PE实验管号
                    List<HashMap<String,Object>>tempList9 = inspectionProcessRecordHeaderDao.getCover3LPEPETestingInfo(project_no, mill_no, od, wt, night_begin_time, night_end_time);

                    if(tempList9!=null&&tempList9.size()>0)
                        OdPEListOfNight.addAll(tempList9);
                    //获取外防修补涂层管管号和原因  ?修补前还是修补后
                    List<HashMap<String,Object>>tempList18=inspectionProcessRecordHeaderDao.getCoatingRepairInfo(project_no,mill_no,null,null,"od",od,wt, night_begin_time, night_end_time);
                    if(tempList18!=null&&tempList18.size()>0)
                        ODRepairListOfNight.addAll(tempList18);
                    //获取外防涂层管废管管号和原因  OK
                    List<HashMap<String,Object>>tempList19=inspectionProcessRecordHeaderDao.getODCoatingRejectedPipeInfo(project_no,mill_no,null,null,od,wt, night_begin_time, night_end_time);


                    if(tempList19!=null&&tempList19.size()>0)
                        ODStripListOfNight.addAll(tempList19);
                    //获取外防隔离光管管号和原因  OK
                    List<HashMap<String,Object>>tempList20=inspectionProcessRecordHeaderDao.getODBarePipeOnholdInfo(project_no,mill_no,null,null,od,wt, night_begin_time, night_end_time);

                    if(tempList20!=null&&tempList20.size()>0)
                        ODOnholdListOfNight.addAll(tempList20);

                    //-----------获取夜班内防信息(nightshiftidinfo)
                    idCoatingCountOfNight+=inspectionProcessRecordHeaderDao.getIDCoatingCount(project_no,mill_no,null,null,od,wt,night_begin_time, night_end_time);
                    List<HashMap<String,Object>>list4=inspectionProcessRecordHeaderDao.getIDCoatingAcceptedInfo(project_no,mill_no,null,null,od,wt,night_begin_time, night_end_time,"1");


                    if(list4!=null&&list4.size()>0){
                        HashMap<String,Object>hs=list4.get(0);
                        if(hs!=null){
                            idAcceptedPipeCountOfNight+=((Long)hs.get("idtotalcount")).intValue();
                        }
                    }
                    idRepairPipeCountOfNight+=inspectionProcessRecordHeaderDao.getCoatingRepairCount(project_no,mill_no,null,null,"id",od,wt,night_begin_time, night_end_time);
                    idRejectedPipeCountOfNight+=inspectionProcessRecordHeaderDao.getIDCoatingRejectedPipeCount(project_no,mill_no,null,null,od,wt,night_begin_time, night_end_time);
                    idOnholdPipeCountOfNight+=inspectionProcessRecordHeaderDao.getIDBarePipeOnholdCount(project_no,mill_no,null,null,od,wt,night_begin_time, night_end_time);

                    //获取钢试片管号和玻璃试片管号
                    List<HashMap<String,Object>>tempList10=inspectionProcessRecordHeaderDao.getCoverIdSampleInfo(project_no,mill_no,od,wt,night_begin_time, night_end_time);

                    if(tempList10!=null&&tempList10.size()>0)
                        IDSampleInfoListOfNight.addAll(tempList10);
                    List<HashMap<String,Object>>tempList11=inspectionProcessRecordHeaderDao.getCoverIdGlassSampleInfo(project_no,mill_no,od,wt,night_begin_time, night_end_time);

                    if(tempList11!=null&&tempList11.size()>0)
                        IDGlassSampleInfoListOfNight.addAll(tempList11);
                    //获取内防修补涂层管管号和原因  ?修补前还是修补后
                    List<HashMap<String,Object>>tempList21=inspectionProcessRecordHeaderDao.getCoatingRepairInfo(project_no,mill_no,null,null,"id",od,wt,night_begin_time, night_end_time);


                    if(tempList21!=null&&tempList21.size()>0)
                        IDRepairListOfNight.addAll(tempList21);
                    //获取内防涂层管废管管号和原因  OK
                    List<HashMap<String,Object>>tempList22=inspectionProcessRecordHeaderDao.getIDCoatingRejectedPipeInfo(project_no,mill_no,null,null,od,wt,night_begin_time, night_end_time);

                    if(tempList22!=null&&tempList22.size()>0)
                        IDStripListOfNight.addAll(tempList22);
                    //获取内防隔离光管管号和原因  OK
                    List<HashMap<String,Object>>tempList23=inspectionProcessRecordHeaderDao.getIDBarePipeOnholdInfo(project_no,mill_no,null,null,od,wt,night_begin_time, night_end_time);

                    if(tempList23!=null&&tempList23.size()>0)
                        IDOnholdListOfNight.addAll(tempList23);
                }
                //将获取到的信息添加到map中
                //白班外防
                resultMaps.put("odCoatingCountOfDay",odCoatingCountOfDay);
                resultMaps.put("odAcceptedPipeCountOfDay",odAcceptedPipeCountOfDay);
                resultMaps.put("odRepairPipeCountOfDay",odRepairPipeCountOfDay);
                resultMaps.put("odRejectedPipeCountOfDay",odRejectedPipeCountOfDay);
                resultMaps.put("odOnholdPipeCountOfDay",odOnholdPipeCountOfDay);
                resultMaps.put("OdSampleListOfDay",OdSampleListOfDay);
                resultMaps.put("OdDscListOfDay",OdDscListOfDay);
                resultMaps.put("OdPEListOfDay",OdPEListOfDay);
                resultMaps.put("ODRepairListOfDay",ODRepairListOfDay);
                resultMaps.put("ODStripListOfDay",ODStripListOfDay);
                resultMaps.put("ODOnholdListOfDay",ODOnholdListOfDay);
                //白班内防
                resultMaps.put("idCoatingCountOfDay",idCoatingCountOfDay);
                resultMaps.put("idAcceptedPipeCountOfDay",idAcceptedPipeCountOfDay);
                resultMaps.put("idRepairPipeCountOfDay",idRepairPipeCountOfDay);
                resultMaps.put("idRejectedPipeCountOfDay",idRejectedPipeCountOfDay);
                resultMaps.put("idOnholdPipeCountOfDay",idOnholdPipeCountOfDay);
                resultMaps.put("IDSampleInfoListOfDay",IDSampleInfoListOfDay);
                resultMaps.put("IDGlassSampleInfoListOfDay",IDGlassSampleInfoListOfDay);
                resultMaps.put("IDRepairListOfDay",IDRepairListOfDay);
                resultMaps.put("IDStripListOfDay",IDStripListOfDay);
                resultMaps.put("IDOnholdListOfDay",IDOnholdListOfDay);
                //夜班外防
                resultMaps.put("odCoatingCountOfNight",odCoatingCountOfNight);
                resultMaps.put("odAcceptedPipeCountOfNight",odAcceptedPipeCountOfNight);
                resultMaps.put("odRepairPipeCountOfNight",odRepairPipeCountOfNight);
                resultMaps.put("odRejectedPipeCountOfNight",odRejectedPipeCountOfNight);
                resultMaps.put("odOnholdPipeCountOfNight",odOnholdPipeCountOfNight);
                resultMaps.put("OdSampleListOfNight",OdSampleListOfNight);
                resultMaps.put("OdDscListOfNight",OdDscListOfNight);
                resultMaps.put("OdPEListOfNight",OdPEListOfNight);
                resultMaps.put("ODRepairListOfNight",ODRepairListOfNight);
                resultMaps.put("ODStripListOfNight",ODStripListOfNight);
                resultMaps.put("ODOnholdListOfNight",ODOnholdListOfNight);
                //夜班内防
                resultMaps.put("idCoatingCountOfNight",idCoatingCountOfNight);
                resultMaps.put("idAcceptedPipeCountOfNight",idAcceptedPipeCountOfNight);
                resultMaps.put("idRepairPipeCountOfNight",idRepairPipeCountOfNight);
                resultMaps.put("idRejectedPipeCountOfNight",idRejectedPipeCountOfNight);
                resultMaps.put("idOnholdPipeCountOfNight",idOnholdPipeCountOfNight);
                resultMaps.put("IDSampleInfoListOfNight",IDSampleInfoListOfNight);
                resultMaps.put("IDGlassSampleInfoListOfNight",IDGlassSampleInfoListOfNight);
                resultMaps.put("IDRepairListOfNight",IDRepairListOfNight);
                resultMaps.put("IDStripListOfNight",IDStripListOfNight);
                resultMaps.put("IDOnholdListOfNight",IDOnholdListOfNight);
                resultMaps.put("success",true);
                String map= JSONObject.toJSONString(resultMaps);
                return map;
            }else{
                return  null;
            }
        }catch (Exception ex){
            ex.printStackTrace();
            return  null;
        }
    }
}
