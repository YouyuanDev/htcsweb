package com.htcsweb.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.htcsweb.dao.*;
import com.htcsweb.entity.*;
import com.htcsweb.util.*;
import jxl.format.Alignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import org.apache.ibatis.jdbc.Null;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import java.awt.geom.FlatteningPathIterator;
import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/InspectionRecordPDFOperation")
public class InspectionRecordPDFController {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat timeformat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date beginTime=new Date();
    Date endTime=new Date();
    String basePath="";
    String logoImageFullName="";
    String pdfDirPath="";
    String fontPath="";
    String pdfFullName="";
    WritableFont wf=null;
    WritableCellFormat wcf=null;
    //定义临时文件名
    String tempDayName=null,tempNightName=null;
    //定义工位的生成pdf的集合
    List<String>stationOdDayList=new ArrayList<String>();
    List<String>stationOdNightList=new ArrayList<String>();
    List<String>stationIdDayList=new ArrayList<String>();
    List<String>stationIdNightList=new ArrayList<String>();
    List<String>delSetPath=new ArrayList<String>();//定义删除pdf集合，用于生成zip后删除所有的临时文件
    //String logoImageFullName=this.getClass().getClassLoader().getResource("").getPath() + "template/img/image002.jpg";
    //String pdfDirPath=this.getClass().getClassLoader().getResource("").getPath()+"upload/pdf/";
    //String fontPath=this.getClass().getClassLoader().getResource("").getPath()+"font/simhei.ttf";
    //String pdfFullName=this.getClass().getClassLoader().getResource("").getPath() + "upload/pdf/BlastRecord.pdf";
    public InspectionRecordPDFController(){
        basePath= this.getClass().getClassLoader().getResource("../../").getPath();
        logoImageFullName=basePath + "template/img/image002.jpg";
        pdfDirPath=basePath+"upload/pdf/";
        fontPath=basePath+"font/simhei.ttf";
        pdfFullName=basePath + "upload/pdf/BlastRecord.pdf";
        File pdfDirFile=new File(pdfDirPath);
        if(!pdfDirFile.exists()){
            pdfDirFile.mkdir();
        }
        try{
            wf = new WritableFont(WritableFont.createFont("Arial"), 9);
            wcf= new WritableCellFormat(wf);
            wcf.setAlignment(Alignment.CENTRE);
            wcf.setBackground(jxl.format.Colour.RED);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Autowired
    private OdBlastProcessDao odblastprocessDao;
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
    private OdFinalInspectionProcessDao odFinalInspectionProcessDao;
    @Autowired
    private IdBlastInspectionProcessDao idBlastInspectionProcessDao;
    @Autowired
    private IdCoatingProcessDao idCoatingProcessDao;
    @Autowired
    private  IdCoatingInspectionProcessDao idCoatingInspectionProcessDao;
    @Autowired
    private  IdFinalInspectionProcessDao idFinalInspectionProcessDao;
    @RequestMapping(value="getRecordReportPDF",produces="application/json;charset=UTF-8")
    @ResponseBody
    public  String getRecordReportPDF(HttpServletRequest request, HttpServletResponse response){
        String basePath=request.getSession().getServletContext().getRealPath("/");
        Date start_time=null;
        Date finish_time=null;
        List<String>listDate=new ArrayList<>();//时间区间集合
        //List<String>listPdfSetPath=new ArrayList<>();//pdf白班夜班集合
        String pdfSetOdDayPath="",pdfSetOdNightPath="",pdfSetIdDayPath="",pdfSetIdNightPath="";
        Date day_begin_time=null,day_end_time=null,night_begin_time=null,night_end_time=null;//定义每天班次的开始结束时间
        //是否成功标识
        String flag="success",zipName="";
        //HashMap<String,Object>map=new HashMap<>();
        //先获取选择的日期区间之间的所有日期
        String project_no=request.getParameter("project_no");
        String project_name=request.getParameter("project_name");
        if(project_name==null||project_name.equals("")){
            project_name=getFormatString("");
        }
        String beginTimeStr=request.getParameter("beginTime");
        String endTimeStr=request.getParameter("endTime");
        List<String>dayNightPdf=new ArrayList<>();
        //先清理.zip垃圾文件
        try{
            File fileZip=new File(basePath+"upload/pdf/");
            if(fileZip.exists()&&fileZip.isDirectory()){
                String zipList[]=fileZip.list();
                for (String zippath:zipList){
                    File file=new File(basePath+"/upload/pdf/"+zippath);
                    if(file.isFile()&&file.getName().endsWith(".zip")){
                        file.delete();
                    }
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        if(project_no!=null&&!project_no.equals("")){
            try{
                if(beginTimeStr==null||beginTimeStr.equals("")||endTimeStr==null||endTimeStr.equals("")){
                    beginTimeStr=sdf.format(new Date());
                    endTimeStr=sdf.format(new Date());
                }
                start_time=sdf.parse(beginTimeStr);
                finish_time=sdf.parse(endTimeStr);
                listDate= DateTimeUtil.getBetweenDates(start_time,finish_time);
                //遍历时间区间，生成白班、夜班集合PDF文件
                for (int i=0;i<listDate.size();i++){
                    pdfSetOdDayPath=basePath+"upload/pdf/"+(project_name+"_"+listDate.get(i)+"_外防白班(Day).pdf");
                    pdfSetOdNightPath=basePath+"upload/pdf/"+(project_name+"_"+listDate.get(i)+"_外防夜班(Night).pdf");
                    pdfSetIdDayPath=basePath+"upload/pdf/"+(project_name+"_"+listDate.get(i)+"_内防白班(Day).pdf");
                    pdfSetIdNightPath=basePath+"upload/pdf/"+(project_name+"_"+listDate.get(i)+"_内防夜班(Night).pdf");
                    File fileodday=new File(pdfSetOdDayPath);
                    File fileodnight=new File(pdfSetOdNightPath);
                    File fileidday=new File(pdfSetIdDayPath);
                    File fileidnight=new File(pdfSetIdNightPath);
                    if(!fileodday.exists()){
                        fileodday.createNewFile();
                    }
                    if(!fileodnight.exists()){
                        fileodnight.createNewFile();
                    }
                    if(!fileidday.exists()){
                        fileidday.createNewFile();
                    }
                    if(!fileidnight.exists()){
                        fileidnight.createNewFile();
                    }
                }
                //按天生成白班和夜班的pdf,然后融合到白班的pdf和夜班的pdf中
                for (int i=0;i<listDate.size();i++){
                    pdfSetOdDayPath=basePath+"upload/pdf/"+(project_name+"_"+listDate.get(i)+"_外防白班(Day).pdf");
                    pdfSetOdNightPath=basePath+"upload/pdf/"+(project_name+"_"+listDate.get(i)+"_外防夜班(Night).pdf");
                    pdfSetIdDayPath=basePath+"upload/pdf/"+(project_name+"_"+listDate.get(i)+"_内防白班(Day).pdf");
                    pdfSetIdNightPath=basePath+"upload/pdf/"+(project_name+"_"+listDate.get(i)+"_内防夜班(Night).pdf");
                    day_begin_time=timeformat.parse(listDate.get(i)+" 08:00:00");
                    day_end_time=timeformat.parse(listDate.get(i)+" 20:00:00");
                    night_begin_time=timeformat.parse(listDate.get(i)+" 20:00:00");
                    night_end_time=timeformat.parse(DateTimeUtil.getNextDay(listDate.get(i))+" 08:00:00");

                    //-------------------外防腐---------------------

                    //1.生成当天的外打砂工位的模板
                    OdBlastRecord(request,project_no,0,day_begin_time,day_end_time);//生成白班报表PDF
                    OdBlastRecord(request,project_no,1,night_begin_time,night_end_time);//生成夜班报表PDF
                    //2.生成当天的外打砂检验工位的模板
                    OdBlastInspectionRecord(request,project_no,0,day_begin_time,day_end_time);
                    OdBlastInspectionRecord(request,project_no,1,night_begin_time,night_end_time);
                    //3.生成当天的外涂(2FBE)工位的模板
                    OdCoat2FBERecord(request,project_no,0,day_begin_time,day_end_time);
                    OdCoat2FBERecord(request,project_no,1,night_begin_time,night_end_time);
                    //4.生成当天的外涂检验(2FBE)工位的模板
                     OdCoat2FBEInspectionRecord(request,project_no,0,day_begin_time,day_end_time);
                    OdCoat2FBEInspectionRecord(request,project_no,1,night_begin_time,night_end_time);
                    //5.生成当天的外涂(3LPE)工位的模板
                    OdCoat3LPERecord(request,project_no,0,day_begin_time,day_end_time);
                    OdCoat3LPERecord(request,project_no,1,night_begin_time,night_end_time);
                    //6.生成当天的外涂检验(3LPE)工位的模板
                    OdCoat3LPEInspectionRecord(request,project_no,0,day_begin_time,day_end_time);
                    OdCoat3LPEInspectionRecord(request,project_no,1,night_begin_time,night_end_time);
                    //7.生成当天的外防终检工位的模板
                    OdCoatFinalInspectionRecord(request,project_no,0,day_begin_time,day_end_time);
                    OdCoatFinalInspectionRecord(request,project_no,1,night_begin_time,night_end_time);

                    //-------------------内防腐---------------------

                    //8.内打砂检验记录PDF
                    IdBlastInspectionRecord(request,project_no,0,day_begin_time,day_end_time);
                    IdBlastInspectionRecord(request,project_no,1,night_begin_time,night_end_time);
                    //9.内涂记录PDF
                    IdCoatRecord(request,project_no,0,day_begin_time,day_end_time);
                    IdCoatRecord(request,project_no,1,night_begin_time,night_end_time);
                    //10.内涂检验记录PDF
                    IdCoatInspectionRecord(request,project_no,0,day_begin_time,day_end_time);
                    IdCoatInspectionRecord(request,project_no,1,night_begin_time,night_end_time);
                    //11.内涂终验记录PDF
                    IdFinalInspectionRecord(request,project_no,0,day_begin_time,day_end_time);
                    IdFinalInspectionRecord(request,project_no,1,night_begin_time,night_end_time);
                    //最后分别融合外访和内防的白班集合和夜班集合
                    if(stationOdDayList.size()>0){
                        MergePDF.MergePDFs(stationOdDayList,pdfSetOdDayPath);
                    }
                    if(stationOdNightList.size()>0){
                        MergePDF.MergePDFs(stationOdNightList,pdfSetOdNightPath);
                    }
                    if(stationIdDayList.size()>0){
                        MergePDF.MergePDFs(stationIdDayList,pdfSetIdDayPath);
                    }
                    if(stationIdNightList.size()>0){
                        MergePDF.MergePDFs(stationIdNightList,pdfSetIdNightPath);
                    }
                    dayNightPdf.add(pdfSetOdDayPath);
                    dayNightPdf.add(pdfSetOdNightPath);
                    dayNightPdf.add(pdfSetIdDayPath);
                    dayNightPdf.add(pdfSetIdNightPath);
                    delSetPath.add(pdfSetOdDayPath);
                    delSetPath.add(pdfSetOdNightPath);
                    delSetPath.add(pdfSetIdDayPath);
                    delSetPath.add(pdfSetIdNightPath);
                    //清空stationDayList和stationNightList
                    stationOdDayList.clear();
                    stationOdNightList.clear();
                    stationIdDayList.clear();
                    stationIdNightList.clear();
                }
                zipName="upload/pdf/"+ResponseUtil.downLoadPdf(dayNightPdf,request,response);
                //定时删除临时文件
                for (int j=0;j<delSetPath.size();j++){
                    if(delSetPath.get(j)!=null){
                        File file=new File(delSetPath.get(j));
                        if(file.exists()){
                            file.delete();
                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return JSONObject.toJSONString(zipName);
    }

    @RequestMapping(value="downloadPDF",produces="application/json;charset=UTF-8")
    @ResponseBody
    public String downloadPDF(HttpServletRequest request, HttpServletResponse response){
        String pathArr=request.getParameter("pathList");
        System.out.println(pathArr+"0-------------");
        String []pathList=pathArr.split(";");
        List<String>dayNightPdf=new ArrayList<>();
        dayNightPdf=Arrays.asList(pathList);
        ResponseUtil.downLoadPdf(dayNightPdf,request,response);
        return null;
    }
    //1.---------------获取外打砂记录PDF
    public  void OdBlastRecord(HttpServletRequest request,String project_no,int dayOrNight,Date begin_time,Date end_time){
        String templateFullName=request.getSession().getServletContext().getRealPath("/")
                +"template/od_blast_record_template.xls";
        String newPdfName=null;
        try{
            List<HashMap<String,Object>>list=odblastprocessDao.getOdBlastRecord(project_no,begin_time,end_time);
            ArrayList<Label> datalist=new ArrayList<Label>();
            if(list.size()>0){
                int index=1,row=0;
                StringBuilder sb=new StringBuilder();
                String result="";
                int qualifiedTotal=0;
                boolean isHaveTitle=true;
                String title_project_name=" ",title_pipe_size=" ",title_standard=" ",title_coating_type=" ";
                for (int i=0;i<list.size();i++){
                    if(isHaveTitle){
                        //添加模板头部信息
                        title_project_name=String.valueOf(list.get(i).get("project_name"));
                        title_pipe_size=("Φ"+String.valueOf(list.get(i).get("od"))+"*"+String.valueOf(list.get(i).get("wt"))+"mm");
                        title_standard=(String.valueOf(list.get(i).get("client_spec"))+" "+String.valueOf(list.get(i).get("coating_standard")));
                        title_coating_type=getFormatString(String.valueOf(list.get(i).get("external_coating")));
                        isHaveTitle=false;
                    }
                    datalist.add(new Label(1, row+8, list.get(i).get("pipe_no").toString(), wcf));
                    String isClear="是";
                    String marking=String.valueOf(list.get(i).get("marking"));
                    if(marking!=null){
                        if(!marking.equals("0")){
                            isClear="否";
                        }
                    }else {
                        isClear=" ";
                    }
                    datalist.add(new Label(2, row+8, isClear, wcf));
                    datalist.add(new Label(3, row+8, String.valueOf(list.get(i).get("surface_condition")), wcf));
                    datalist.add(new Label(4, row+8, String.valueOf(list.get(i).get("salt_contamination_before_blasting")), wcf));
                    datalist.add(new Label(5, row+8, String.valueOf(list.get(i).get("preheat_temp")), wcf));
                    datalist.add(new Label(6, row+8, String.valueOf(list.get(i).get("blast_line_speed")), wcf));
                    datalist.add(new Label(7, row+8, String.valueOf(list.get(i).get("rinse_water_conductivity")), wcf));
                    datalist.add(new Label(8, row+8, String.valueOf(list.get(i).get("abrasive_conductivity")), wcf));
                    datalist.add(new Label(9, row+8, String.valueOf(list.get(i).get("alkaline_dwell_time")), wcf));
                    datalist.add(new Label(10, row+8, String.valueOf(list.get(i).get("acid_concentration")), wcf));
                    datalist.add(new Label(11, row+8, String.valueOf(list.get(i).get("acid_wash_time")), wcf));
                    datalist.add(new Label(12, row+8, String.valueOf(list.get(i).get("acid_concentration")), wcf));
                    result=String.valueOf(list.get(i).get("result"));
                    if(result!=null){
                        if(result.equals("0")){
                            result="不合格";
                        }else if(result.equals("1")){
                            result="合格";
                            qualifiedTotal++;
                        }else if(result.equals("2")){
                            result="待定";
                        }
                        else if(result.equals("3")){
                            result="表面缺陷";
                        }else{
                            result=" ";
                        }
                    }else{
                        result=" ";
                    }
                    datalist.add(new Label(13, row+8, result, wcf));
                    if(list.get(i).get("remark")!=null&&!list.get(i).get("remark").equals("")) {
                        sb.append("#" + list.get(i).get("pipe_no") + ":" + list.get(i).get("remark") + " ");
                    }
                    //最后一行数据为空问题
                    index++;
                    row++;
                    if(index%13==0){
                        createRecordPdfTitle(datalist,3,9,13,4,5,title_project_name,title_pipe_size,title_standard,title_coating_type,dayOrNight,begin_time);
                        createRecordPdf(datalist,newPdfName,templateFullName,qualifiedTotal,2,20,13,20,index,row,sb,dayOrNight,stationOdDayList,stationOdNightList);
                    }
                }
                if(datalist.size()>0){
                     createRecordPdfTitle(datalist,3,9,13,4,5,title_project_name,title_pipe_size,title_standard,title_coating_type,dayOrNight,begin_time);
                     createRecordPdf(datalist,newPdfName,templateFullName,qualifiedTotal,2,20,13,20,index,row,sb,dayOrNight,stationOdDayList,stationOdNightList);
                }
            }else{
                createRecordNullPdf(datalist,3,9,13,1,4,5,8,newPdfName,templateFullName,dayOrNight,stationOdDayList,stationOdNightList);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //2.---------------获取外打砂检验记录PDF
    public  void OdBlastInspectionRecord(HttpServletRequest request,String project_no,int dayOrNight,Date begin_time,Date end_time){
        String templateFullName=request.getSession().getServletContext().getRealPath("/")
                +"template/od_blast_inspection_record_template.xls";
        String newPdfName=null;
        try{
            List<HashMap<String,Object>>list=odBlastInspectionProcessDao.getOdBlastInspectionRecord(project_no,begin_time,end_time);
            //System.out.println("外打砂检验"+timeformat.format(begin_time)+":"+timeformat.format(end_time)+"的数据个数是:"+list.size());
            ArrayList<Label> datalist=new ArrayList<Label>();
            int index=1,row=0;
            StringBuilder sb=new StringBuilder();
            String result="";
            int qualifiedTotal=0;
            boolean isHaveTitle=true;
            String title_project_name=" ",title_pipe_size=" ",title_standard=" ",title_coating_type=" ";
            for (int i=0;i<list.size();i++){
                if(isHaveTitle){
                    //添加模板头部信息
                    title_project_name=String.valueOf(list.get(i).get("project_name"));
                    title_pipe_size=("Φ"+String.valueOf(list.get(i).get("od"))+"*"+String.valueOf(list.get(i).get("wt"))+"mm");
                    title_standard=(String.valueOf(list.get(i).get("client_spec"))+" "+String.valueOf(list.get(i).get("coating_standard")));
                    title_coating_type=getFormatString(String.valueOf(list.get(i).get("external_coating")));
                    isHaveTitle=false;
                }
                datalist.add(new Label(1, row+8, String.valueOf(list.get(i).get("pipe_no")), wcf));
                datalist.add(new Label(2, row+8, String.valueOf(list.get(i).get("air_temp")), wcf));
                datalist.add(new Label(3, row+8, String.valueOf(list.get(i).get("relative_humidity")), wcf));
                datalist.add(new Label(4, row+8, String.valueOf(list.get(i).get("dew_point")), wcf));
                datalist.add(new Label(5, row+8, String.valueOf(list.get(i).get("pipe_temp")), wcf));
                datalist.add(new Label(6, row+8,String.valueOf(list.get(i).get("surface_condition")), wcf));
                datalist.add(new Label(7, row+8, String.valueOf(list.get(i).get("blast_finish_sa25")), wcf));
                datalist.add(new Label(8, row+8, String.valueOf(list.get(i).get("surface_dust_rating")), wcf));
                datalist.add(new Label(9, row+8, String.valueOf(list.get(i).get("profile")), wcf));
                datalist.add(new Label(10, row+8, String.valueOf(list.get(i).get("salt_contamination_after_blasting")), wcf));
                String isOil=String.valueOf(list.get(i).get("oil_water_in_air_compressor"));
                String oil=" ";
                if(isOil!=null){
                    if(isOil.equals("1")){
                        oil="是";
                    }else{
                        oil="否";
                    }
                }
                datalist.add(new Label(11, row+8,oil, wcf));
                datalist.add(new Label(12,row+8,String.valueOf(list.get(i).get("elapsed_time")),wcf));
                result=String.valueOf(list.get(i).get("result"));
                if(result!=null){
                    if(result.equals("0")){
                        result="不合格";
                    }else if(result.equals("1")){
                        result="合格";
                        qualifiedTotal++;
                    }else if(result.equals("2")){
                        result="待定";
                    }
                    else if(result.equals("3")){
                        result="表面缺陷";
                    }else{
                        result=" ";
                    }
                }else{
                    result=" ";
                }
                datalist.add(new Label(13, row+8, result, wcf));
                if(String.valueOf(list.get(i).get("remark"))!=null&&!String.valueOf(list.get(i).get("remark")).equals("")){
                    sb.append("#"+list.get(i).get("pipe_no")+":"+list.get(i).get("remark")+" ");
                }
                //最后一行数据为空问题
                index++;
                row++;
                if(index%13==0){
                    createRecordPdfTitle(datalist,3,8,12,4,5,title_project_name,title_pipe_size,title_standard,title_coating_type,dayOrNight,begin_time);
                    createRecordPdf(datalist,newPdfName,templateFullName,qualifiedTotal,2,20,13,20,index,row,sb,dayOrNight,stationOdDayList,stationOdNightList);
                }
            }
            if(datalist.size()>0){
                createRecordPdfTitle(datalist,3,8,12,4,5,title_project_name,title_pipe_size,title_standard,title_coating_type,dayOrNight,begin_time);
                createRecordPdf(datalist,newPdfName,templateFullName,qualifiedTotal,2,20,13,20,index,row,sb,dayOrNight,stationOdDayList,stationOdNightList);
            }else{
                createRecordNullPdf(datalist,3,8,12,1,4,5,8,newPdfName,templateFullName,dayOrNight,stationOdDayList,stationOdNightList);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //3.---------------获取外涂(2FBE)记录PDF
    public  void OdCoat2FBERecord(HttpServletRequest request,String project_no,int dayOrNight,Date begin_time,Date end_time){
        String templateFullName=request.getSession().getServletContext().getRealPath("/")
                +"template/od_coating_2fbe_record_template.xls";
        String newPdfName=null;
        try{
            List<HashMap<String,Object>>list=odCoatingProcessDao.getOd2FBECoatRecord(project_no,begin_time,end_time);
            ArrayList<Label> datalist=new ArrayList<Label>();
            if(list.size()>0){
                int index=1,row=0;
                StringBuilder sb=new StringBuilder();
                String result="";
                int qualifiedTotal=0;
                boolean isHaveTitle=true;
                String title_project_name=" ",title_pipe_size=" ",title_standard=" ",title_coating_type=" ";
                for (int i=0;i<list.size();i++){
                    if(isHaveTitle){
                        //添加模板头部信息
                        title_project_name=String.valueOf(list.get(i).get("project_name"));
                        title_pipe_size=("Φ"+String.valueOf(list.get(i).get("od"))+"*"+String.valueOf(list.get(i).get("wt"))+"mm");
                        title_standard=(String.valueOf(list.get(i).get("client_spec"))+" "+String.valueOf(list.get(i).get("coating_standard")));
                        title_coating_type=getFormatString(String.valueOf(list.get(i).get("external_coating")));
                        isHaveTitle=false;
                    }
                    datalist.add(new Label(1, row+9, String.valueOf(list.get(i).get("pipe_no")), wcf));
                    datalist.add(new Label(2, row+9, String.valueOf(list.get(i).get("coating_line_speed")), wcf));
                    datalist.add(new Label(3, row+9, String.valueOf(list.get(i).get("base_coat_used")), wcf));
                    datalist.add(new Label(4, row+9, String.valueOf(list.get(i).get("base_coat_lot_no")), wcf));
                    datalist.add(new Label(5, row+9, String.valueOf(list.get(i).get("top_coat_used")), wcf));
                    datalist.add(new Label(6, row+9,String.valueOf(list.get(i).get("top_coat_lot_no")), wcf));
                    datalist.add(new Label(7, row+9,String.valueOf(list.get(i).get("base_coat_gun_count")), wcf));
                    datalist.add(new Label(8, row+9, String.valueOf(list.get(i).get("top_coat_gun_count")), wcf));
                    datalist.add(new Label(9, row+9, String.valueOf(list.get(i).get("air_pressure")), wcf));
                    datalist.add(new Label(10, row+9, String.valueOf(list.get(i).get("coating_voltage")), wcf));
                    datalist.add(new Label(11, row+9,String.valueOf(list.get(i).get("gun_distance")), wcf));
                    datalist.add(new Label(2, row+10,String.valueOf(list.get(i).get("spray_speed")), wcf));
                    datalist.add(new Label(3, row+10,String.valueOf(list.get(i).get("application_temp")), wcf));
                    datalist.add(new Label(4, row+10,String.valueOf(list.get(i).get("application_voltage")), wcf));
                    String dateStr=timeformat.format(list.get(i).get("operation_time"));
                    Label label15= new Label(5, row+10,dateStr, wcf);
                    datalist.add(label15);
                    datalist.add(new Label(6, row+10,String.valueOf(list.get(i).get("to_first_touch_duration")), wcf));
                    datalist.add(new Label(7, row+10,String.valueOf(list.get(i).get("to_quench_duration")), wcf));


                    result=String.valueOf(list.get(i).get("result"));
                    if(result!=null){
                        if(result.equals("0")){
                            result="不合格";
                        }else if(result.equals("1")){
                            result="合格";
                            qualifiedTotal++;
                        }else if(result.equals("2")){
                            result="待定";
                        }else{
                            result=" ";
                        }
                    }else{
                        result=" ";
                    }
                    datalist.add(new Label(12, row+9, result, wcf));
                    if(String.valueOf(list.get(i).get("remark"))!=null&&!String.valueOf(list.get(i).get("remark")).equals("")){
                        sb.append("#"+list.get(i).get("pipe_no")+":"+list.get(i).get("remark")+" ");
                    }
                    //最后一行数据为空问题
                    index+=2;
                    row+=2;
                    if(index%11==0){
                        createRecordPdfTitle(datalist,3,8,12,4,5,title_project_name,title_pipe_size,title_standard,title_coating_type,dayOrNight,begin_time);
                        createRecordPdf(datalist,newPdfName,templateFullName,qualifiedTotal,2,19,12,19,index,row,sb,dayOrNight,stationOdDayList,stationOdNightList);
                    }
                }
                if(datalist.size()>0){
                    createRecordPdfTitle(datalist,3,8,12,4,5,title_project_name,title_pipe_size,title_standard,title_coating_type,dayOrNight,begin_time);

                    createRecordPdf(datalist,newPdfName,templateFullName,qualifiedTotal,2,19,12,19,index,row,sb,dayOrNight,stationOdDayList,stationOdNightList);
                }
            }else{
                createRecordNullPdf(datalist,3,8,12,1,4,5,9,newPdfName,templateFullName,dayOrNight,stationOdDayList,stationOdNightList);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //4.---------------获取外涂(2FBE)检验记录PDF
    public  void OdCoat2FBEInspectionRecord(HttpServletRequest request,String project_no,int dayOrNight,Date begin_time,Date end_time){
        String templateFullName=request.getSession().getServletContext().getRealPath("/")
                +"template/od_coating_2fbe_inspection_record_template.xls";
        String newPdfName=null;
        try{
            List<HashMap<String,Object>>list=odCoatingInspectionProcessDao.getOd2FBECoatInspectionRecord(project_no,begin_time,end_time);
            ArrayList<Label> datalist=new ArrayList<Label>();
            if(list.size()>0){
                int index=1,row=0;
                StringBuilder sb=new StringBuilder();
                String result="";
                int qualifiedTotal=0;
                boolean isHaveTitle=true;
                String title_project_name=" ",title_pipe_size=" ",title_standard=" ",title_coating_type=" ";
                for (int i=0;i<list.size();i++){
                    if(isHaveTitle){
                        //添加模板头部信息
                        title_project_name=String.valueOf(list.get(i).get("project_name"));
                        title_pipe_size=("Φ"+String.valueOf(list.get(i).get("od"))+"*"+String.valueOf(list.get(i).get("wt"))+"mm");
                        title_standard=(String.valueOf(list.get(i).get("client_spec"))+" "+String.valueOf(list.get(i).get("coating_standard")));
                        title_coating_type=getFormatString(String.valueOf(list.get(i).get("external_coating")));
                        isHaveTitle=false;
                    }
                    datalist.add(new Label(1, row+9, String.valueOf(list.get(i).get("pipe_no")), wcf));
                    datalist.add(new Label(2, row+9, String.valueOf(list.get(i).get("holidays")), wcf));
                    datalist.add(new Label(3, row+9, String.valueOf(list.get(i).get("holiday_tester_volts")), wcf));
                    datalist.add(new Label(4, row+9, String.valueOf(list.get(i).get("repairs")), wcf));
                    String bevel=String.valueOf(list.get(i).get("bevel"));
                    String label5txt="未检测";
                    if(bevel!=null){
                        if(bevel.equals("1")){
                            label5txt="合格";
                        }else if(bevel.equals("2")){
                            label5txt="不合格";
                        }
                    }
                    datalist.add(new Label(5, row+9,label5txt, wcf));
                    String Adhesion=String.valueOf(list.get(i).get("adhesion_rating"));
                    String label6txt="未检测";
                    if(Adhesion!=null){
                        if(Adhesion.equals("1")){
                            label6txt="合格";
                        }else if(Adhesion.equals("2")){
                            label6txt="不合格";
                        }
                    }
                    datalist.add(new Label(6, row+9,label6txt, wcf));
                    String isSample=String.valueOf(list.get(i).get("is_sample"));
                    String label7Txt=" ";
                    if(isSample!=null){
                        if(isSample.equals("0")){
                            label7Txt="否";
                        }else if(isSample.equals("1")){
                            label7Txt="是";
                        }
                    }
                    datalist.add(new Label(7, row+9,label7Txt, wcf));
                    datalist.add(new Label(8, row+9, getFormatString(list.get(i).get("surface_condition").toString()), wcf));
                    datalist.add(new Label(2, row+10,String.valueOf(list.get(i).get("base_coat_thickness_list")), wcf));
                    datalist.add(new Label(5, row+10, String.valueOf(list.get(i).get("top_coat_thickness_list")), wcf));
                    datalist.add(new Label(8, row+10,String.valueOf(list.get(i).get("total_coating_thickness_list")), wcf));
                    result=String.valueOf(list.get(i).get("result"));
                    if(result!=null){
                        if(result.equals("0")){
                            result="不合格,进入待修补工序";
                        }else if(result.equals("1")){
                            result="合格";
                            qualifiedTotal++;
                        }else if(result.equals("2")){
                            result="不合格,进入待扒皮工序";
                        }
                        else if(result.equals("3")){
                            result="待定";
                        }else{
                            result=" ";
                        }
                    }else{
                        result=" ";
                    }
                    datalist.add(new Label(12, row+10, result, wcf));
                    if(String.valueOf(list.get(i).get("remark"))!=null&&!String.valueOf(list.get(i).get("remark")).equals("")){
                        sb.append("#"+list.get(i).get("pipe_no")+":"+list.get(i).get("remark")+" ");
                    }
                    //最后一行数据为空问题
                    index+=2;
                    row+=2;
                    if(index%11==0){
                        createRecordPdfTitle(datalist,3,8,12,4,5,title_project_name,title_pipe_size,title_standard,title_coating_type,dayOrNight,begin_time);
                        createRecordPdf(datalist,newPdfName,templateFullName,qualifiedTotal,2,19,12,19,index,row,sb,dayOrNight,stationOdDayList,stationOdNightList);
                    }
                }
                if(datalist.size()>0){
                    createRecordPdfTitle(datalist,3,8,12,4,5,title_project_name,title_pipe_size,title_standard,title_coating_type,dayOrNight,begin_time);

                    createRecordPdf(datalist,newPdfName,templateFullName,qualifiedTotal,2,19,12,19,index,row,sb,dayOrNight,stationOdDayList,stationOdNightList);
                }
            }else{
                createRecordNullPdf(datalist,3,8,12,1,4,5,9,newPdfName,templateFullName,dayOrNight,stationOdDayList,stationOdNightList);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //5.---------------获取外涂(3LPE)记录PDF
    public  void OdCoat3LPERecord(HttpServletRequest request,String project_no,int dayOrNight,Date begin_time,Date end_time){
        String templateFullName=request.getSession().getServletContext().getRealPath("/")
                +"template/od_coating_3lpe_record_template.xls";
        //SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String newPdfName=null;
        try{
            List<HashMap<String,Object>>list=odCoating3LpeProcessDao.getOd3LPECoatRecord(project_no,begin_time,end_time);
            ArrayList<Label> datalist=new ArrayList<Label>();
            if(list.size()>0){
                int index=1,row=0;
                StringBuilder sb=new StringBuilder();
                String result="";
                int qualifiedTotal=0;
                boolean isHaveTitle=true;
                String title_project_name=" ",title_pipe_size=" ",title_standard=" ",title_coating_type=" ";
                for (int i=0;i<list.size();i++){
                    if(isHaveTitle){
                        //添加模板头部信息
                        title_project_name=String.valueOf(list.get(i).get("project_name"));
                        title_pipe_size=("Φ"+String.valueOf(list.get(i).get("od"))+"*"+String.valueOf(list.get(i).get("wt"))+"mm");
                        title_standard=(String.valueOf(list.get(i).get("client_spec"))+" "+String.valueOf(list.get(i).get("coating_standard")));
                        title_coating_type=getFormatString(String.valueOf(list.get(i).get("external_coating").toString()));
                        isHaveTitle=false;
                    }
                    datalist.add(new Label(1, row+9, String.valueOf(list.get(i).get("pipe_no")), wcf));
                    datalist.add(new Label(2, row+9, String.valueOf(list.get(i).get("coating_line_speed")), wcf));
                    datalist.add(new Label(3, row+9, String.valueOf(list.get(i).get("base_coat_used")), wcf));
                    datalist.add(new Label(4, row+9, String.valueOf(list.get(i).get("base_coat_lot_no")), wcf));
                    datalist.add(new Label(5, row+9, String.valueOf(list.get(i).get("middle_coat_used")), wcf));
                    datalist.add(new Label(6, row+9,String.valueOf(list.get(i).get("middle_coat_lot_no")), wcf));
                    datalist.add(new Label(7, row+9, String.valueOf(list.get(i).get("top_coat_used")), wcf));
                    datalist.add(new Label(8, row+9, String.valueOf(list.get(i).get("top_coat_lot_no")), wcf));
                    datalist.add(new Label(9, row+9, String.valueOf(list.get(i).get("air_pressure")), wcf));
                    datalist.add(new Label(10, row+9, String.valueOf(list.get(i).get("coating_voltage")), wcf));
                    datalist.add(new Label(11, row+9,String.valueOf(list.get(i).get("gun_distance")), wcf));
                    datalist.add(new Label(2, row+10, String.valueOf(list.get(i).get("spray_speed")), wcf));
                    datalist.add(new Label(3, row+10, String.valueOf(list.get(i).get("base_coat_gun_count")), wcf));
                    datalist.add(new Label(4, row+10,String.valueOf(list.get(i).get("application_temp")), wcf));
                    datalist.add(new Label(5, row+10, String.valueOf(list.get(i).get("application_voltage")), wcf));
                    String dateSter=timeformat.format(list.get(i).get("operation_time"));
                    datalist.add(new Label(6, row+10, dateSter, wcf));
                    datalist.add(new Label(7, row+10, String.valueOf(list.get(i).get("to_first_touch_duration")), wcf));
                    datalist.add(new Label(8, row+10, String.valueOf(list.get(i).get("to_quench_duration")), wcf));

                    result=String.valueOf(list.get(i).get("result"));
                    if(result!=null){
                        if(result.equals("0")){
                            result="不合格";
                        }else if(result.equals("1")){
                            result="合格";
                            qualifiedTotal++;
                        }else if(result.equals("2")){
                            result="待定";
                        }
                        else{
                            result=" ";
                        }
                    }else{
                        result=" ";
                    }
                    Label label19 = new Label(12, row+10, result, wcf);
                    datalist.add(label19);
                    if(String.valueOf(list.get(i).get("remark"))!=null&&!String.valueOf(list.get(i).get("remark")).equals("")){
                        sb.append("#"+list.get(i).get("pipe_no")+":"+list.get(i).get("remark")+" ");
                    }
                    //最后一行数据为空问题
                    index+=2;
                    row+=2;
                    if(index%11==0){
                        createRecordPdfTitle(datalist,3,8,12,4,5,title_project_name,title_pipe_size,title_standard,title_coating_type,dayOrNight,begin_time);

                        createRecordPdf(datalist,newPdfName,templateFullName,qualifiedTotal,2,19,12,19,index,row,sb,dayOrNight,stationOdDayList,stationOdNightList);
                    }
                }
                if(datalist.size()>0){
                    createRecordPdfTitle(datalist,3,8,12,4,5,title_project_name,title_pipe_size,title_standard,title_coating_type,dayOrNight,begin_time);

                    createRecordPdf(datalist,newPdfName,templateFullName,qualifiedTotal,2,19,12,19,index,row,sb,dayOrNight,stationOdDayList,stationOdNightList);
                }
            }else{
                createRecordNullPdf(datalist,3,8,12,1,4,5,9,newPdfName,templateFullName,dayOrNight,stationOdDayList,stationOdNightList);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //6.---------------获取外涂(3LPE)检验记录PDF
    public  void OdCoat3LPEInspectionRecord(HttpServletRequest request,String project_no,int dayOrNight,Date begin_time,Date end_time){
        String templateFullName=request.getSession().getServletContext().getRealPath("/")
                +"template/od_coating_3lpe_inspection_record_template.xls";
        String newPdfName=null;
        try{
            List<HashMap<String,Object>>list=odCoating3LpeInspectionProcessDao.getOd3LPECoatInspectionRecord(project_no,begin_time,end_time);
            ArrayList<Label> datalist=new ArrayList<Label>();
            if(list.size()>0){
                int index=1,row=0;
                StringBuilder sb=new StringBuilder();
                String result="";
                int qualifiedTotal=0;
                boolean isHaveTitle=true;
                String title_project_name=" ",title_pipe_size=" ",title_standard=" ",title_coating_type=" ";
                for (int i=0;i<list.size();i++){
                    if(isHaveTitle){
                        //添加模板头部信息
                        title_project_name=String.valueOf(list.get(i).get("project_name"));
                        title_pipe_size=("Φ"+String.valueOf(list.get(i).get("od"))+"*"+String.valueOf(list.get(i).get("wt"))+"mm");
                        title_standard=(String.valueOf(list.get(i).get("client_spec"))+" "+String.valueOf(list.get(i).get("coating_standard")));
                        title_coating_type=getFormatString(String.valueOf(list.get(i).get("external_coating")));
                        isHaveTitle=false;
                    }
                    Label label1 = new Label(1, row+9, String.valueOf(list.get(i).get("pipe_no")), wcf);
                    datalist.add(label1);
                    Label label2 = new Label(2, row+9, String.valueOf(list.get(i).get("holidays")), wcf);
                    datalist.add(label2);
                    Label label3 = new Label(3, row+9, String.valueOf(list.get(i).get("holiday_tester_volts")), wcf);
                    datalist.add(label3);
                    Label label4 = new Label(4, row+9, String.valueOf(list.get(i).get("repairs")), wcf);
                    datalist.add(label4);
                    String bevel=String.valueOf(list.get(i).get("bevel"));
                    String label5txt="未检测";
                    if(bevel!=null){
                        if(bevel.equals("1")){
                            label5txt="合格";
                        }else if(bevel.equals("2")){
                            label5txt="不合格";
                        }
                    }
                    Label label5 = new Label(5, row+9, label5txt, wcf);
                    datalist.add(label5);
                    String Adhesion=String.valueOf(list.get(i).get("adhesion_rating"));
                    String label6txt="未检测";
                    if(Adhesion!=null){
                        if(Adhesion.equals("1")){
                            label6txt="合格";
                        }else if(Adhesion.equals("2")){
                            label6txt="不合格";
                        }
                    }
                    Label label6 = new Label(6, row+9, label6txt, wcf);
                    datalist.add(label6);
                    String isSample=String.valueOf(list.get(i).get("is_sample"));
                    String label7Txt=" ";
                    if(isSample!=null){
                        if(isSample.equals("0")){
                            label7Txt="否";
                        }else{
                            label7Txt="是";
                        }

                    }
                    Label label7 = new Label(7, row+9,label7Txt, wcf);
                    datalist.add(label7);
                    Label label8 = new Label(8, row+9,getFormatString(String.valueOf(list.get(i).get("surface_condition"))), wcf);
                    datalist.add(label8);
                    Label label9 = new Label(2, row+10, String.valueOf(list.get(i).get("base_coat_thickness_list")), wcf);
                    datalist.add(label9);
                    Label label10 = new Label(4, row+10, String.valueOf(list.get(i).get("middle_coat_thickness_list")), wcf);
                    datalist.add(label10);
                    Label label11 = new Label(6, row+10, String.valueOf(list.get(i).get("top_coat_thickness_list")), wcf);
                    datalist.add(label11);
                    Label label12 = new Label(8, row+10,String.valueOf(list.get(i).get("total_coating_thickness_list")), wcf);
                    datalist.add(label12);
                    result=String.valueOf(list.get(i).get("result"));
                    if(result!=null){
                        if(result.equals("0")){
                            result="不合格";
                        }else if(result.equals("1")){
                            result="合格";
                            qualifiedTotal++;
                        }else if(result.equals("2")){
                            result="不合格";
                        }
                        else if(result.equals("3")){
                            result="待定";
                        }else{
                            result=" ";
                        }
                    }else{
                        result=" ";
                    }
                    Label label13= new Label(12, row+10, result, wcf);
                    datalist.add(label13);
                    if(String.valueOf(list.get(i).get("remark"))!=null&&!String.valueOf(list.get(i).get("remark")).equals("")){
                        sb.append("#"+list.get(i).get("pipe_no")+":"+list.get(i).get("remark")+" ");
                    }
                    //最后一行数据为空问题
                    index++;
                    row+=2;
                    if(index%5==0){
                        createRecordPdfTitle(datalist,3,8,12,4,5,title_project_name,title_pipe_size,title_standard,title_coating_type,dayOrNight,begin_time);

                        createRecordPdf(datalist,newPdfName,templateFullName,qualifiedTotal,2,19,12,19,index,row,sb,dayOrNight,stationOdDayList,stationOdNightList);
                    }
                }
                if(datalist.size()>0){
                    createRecordPdfTitle(datalist,3,8,12,4,5,title_project_name,title_pipe_size,title_standard,title_coating_type,dayOrNight,begin_time);

                    createRecordPdf(datalist,newPdfName,templateFullName,qualifiedTotal,2,19,12,19,index,row,sb,dayOrNight,stationOdDayList,stationOdNightList);
                }
            }else{
                createRecordNullPdf(datalist,3,8,12,1,4,5,9,newPdfName,templateFullName,dayOrNight,stationOdDayList,stationOdNightList);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //7.---------------获取外涂终检记录PDF
    public  void OdCoatFinalInspectionRecord(HttpServletRequest request,String project_no,int dayOrNight,Date begin_time,Date end_time){
        String templateFullName=request.getSession().getServletContext().getRealPath("/")
                +"template/od_final_inspection_record_template.xls";
        String newPdfName=null;
        List<String>splitList=new ArrayList<>();
        try{
            List<HashMap<String,Object>>list=odFinalInspectionProcessDao.getOdFianlInspectionRecord(project_no,begin_time,end_time);
            ArrayList<Label> datalist=new ArrayList<Label>();
            if(list.size()>0){
                int index=1,row=0;
                StringBuilder sb=new StringBuilder();
                String result="",cutbackStr=null,cutback1=" ",cutback2=" ",stencilStr=" ";;
                int qualifiedTotal=0;
                boolean isHaveTitle=true;
                String title_project_name=" ",title_pipe_size=" ",title_standard=" ",title_coating_type=" ";
                for (int i=0;i<list.size();i++){
                    if(isHaveTitle){
                        //添加模板头部信息
                        title_project_name=String.valueOf(list.get(i).get("project_name"));
                        title_pipe_size=("Φ"+String.valueOf(list.get(i).get("od"))+"*"+String.valueOf(list.get(i).get("wt"))+"mm");
                        title_standard=(String.valueOf(list.get(i).get("client_spec"))+" "+String.valueOf(list.get(i).get("coating_standard")));
                        title_coating_type=getFormatString(String.valueOf(list.get(i).get("external_coating")));
                        isHaveTitle=false;
                    }
                    datalist.add(new Label(1, row+9, String.valueOf(list.get(i).get("pipe_no")), wcf));
                    String stencilRes=String.valueOf(list.get(i).get("stencil_verification"));
                    if(stencilRes!=null&&!stencilRes.equals("")){
                        if(stencilRes.equals("0")){
                            stencilStr="未检测";
                        }else if(stencilRes.equals("1")){
                            stencilStr="合格";
                        }else if(stencilRes.equals("2")){
                            stencilStr="不合格";
                        }
                    }
                    datalist.add(new Label(2, row+9, stencilStr, wcf));
                    cutbackStr=String.valueOf(list.get(i).get("cutback_length"));
                    if(cutbackStr!=null&&!cutbackStr.equals("")){
                        String[]arr=cutbackStr.split(",");
                        if(arr.length>0){
                            cutback1=arr[0];
                            if(arr.length>1)
                                cutback2=arr[1];
                        }
                    }
                    //预留端 两个值
                    datalist.add(new Label(3, row+9, cutback1, wcf));
                    datalist.add(new Label(3, row+10, cutback2, wcf));
                    //预留端 外观两个值
                    String cutback= String.valueOf(list.get(i).get("cutback_surface"));
                    if(cutback!=null&&!cutback.equals("")){
                        if(cutback.equals("0")){
                            cutback="合格";
                        }else if(cutback.equals("1")){
                            cutback="不合格";
                        }else{
                            cutback=" ";
                        }
                    }else{
                        cutback=" ";
                    }
                    datalist.add(new Label(4, row+9,cutback, wcf));
                    datalist.add(new Label(4, row+10, cutback, wcf));
                    //剩磁 四个值 一端两个
                    splitList=getSplitList(String.valueOf(list.get(i).get("magnetism_list")));
                    datalist.add(new Label(5, row+9, splitList.get(0), wcf));
                    datalist.add(new Label(5, row+10,splitList.get(1), wcf));
                    //平均剩磁
                    datalist.add(new Label(6, row+9, getAvgOfMagnetism(splitList.get(0)), wcf));
                    datalist.add(new Label(6, row+10,getAvgOfMagnetism(splitList.get(1)), wcf));
                    //涂层倒角
                    splitList=getSplitList(String.valueOf(list.get(i).get("coating_bevel_angle_list")));
                    datalist.add(new Label(7, row+9, splitList.get(0), wcf));
                    datalist.add(new Label(7, row+10,splitList.get(1), wcf));
                    //粉末长度
                    splitList=getSplitList(String.valueOf(list.get(i).get("epoxy_cutback_list")));
                    datalist.add(new Label(8, row+9, splitList.get(0), wcf));
                    datalist.add(new Label(8, row+10,splitList.get(1), wcf));

                    datalist.add(new Label(11, row+9,sdf.format(sdf.parse(String.valueOf(list.get(i).get("operation_time")))), wcf));

                    String result1=String.valueOf(list.get(i).get("result"));
                    String labeltxt=" ";
                    if(result1!=null&&!result1.equals("")){
                        if(result1.equals("1")){
                            qualifiedTotal++;
                            labeltxt="合格";
                        }else if(result1.equals("2")){
                            labeltxt="待定";
                        }else if(result1.equals("0")){
                            labeltxt="不合格";
                        }
                    }
                    datalist.add(new Label(12, row+9, labeltxt, wcf));
                    if(String.valueOf(list.get(i).get("remark"))!=null&&!String.valueOf(list.get(i).get("remark")).equals("")){
                        sb.append("#"+list.get(i).get("pipe_no")+":"+list.get(i).get("remark")+" ");
                    }
                    //最后一行数据为空问题
                    index++;
                    row+=2;
                    if(index%5==0){
                        createRecordPdfTitle(datalist,3,8,12,4,5,title_project_name,title_pipe_size,title_standard,title_coating_type,dayOrNight,begin_time);

                        createRecordPdf(datalist,newPdfName,templateFullName,qualifiedTotal,2,19,12,19,index,row,sb,dayOrNight,stationOdDayList,stationOdNightList);
                    }
                }
                if(datalist.size()>0){
                    createRecordPdfTitle(datalist,3,8,12,4,5,title_project_name,title_pipe_size,title_standard,title_coating_type,dayOrNight,begin_time);

                    createRecordPdf(datalist,newPdfName,templateFullName,qualifiedTotal,2,19,12,19,index,row,sb,dayOrNight,stationOdDayList,stationOdNightList);
                }
            }else{
                createRecordNullPdf(datalist,3,8,12,1,4,5,9,newPdfName,templateFullName,dayOrNight,stationOdDayList,stationOdNightList);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    //8.---------------内打砂检验记录PDF
    public void  IdBlastInspectionRecord(HttpServletRequest request,String project_no,int dayOrNight,Date begin_time,Date end_time){
        String templateFullName=request.getSession().getServletContext().getRealPath("/")
                +"template/id_blast_inspection_record_template.xls";
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String newPdfName=null;
        try{
            List<HashMap<String,Object>>list=idBlastInspectionProcessDao.getIdBlastInspectionRecord(project_no,begin_time,end_time);
            ArrayList<Label> datalist=new ArrayList<Label>();
            if(list.size()>0){
                int index=1,row=0;
                StringBuilder sb=new StringBuilder();
                String result="";
                int qualifiedTotal=0;
                boolean isHaveTitle=true;
                String title_project_name=" ",title_pipe_size=" ",title_standard=" ",title_coating_type=" ";
                for (int i=0;i<list.size();i++){
                    if(isHaveTitle){
                        //添加模板头部信息
                        title_project_name=String.valueOf(list.get(i).get("project_name"));
                        title_pipe_size=("Φ"+String.valueOf(list.get(i).get("od"))+"*"+String.valueOf(list.get(i).get("wt"))+"mm");
                        title_standard=(String.valueOf(list.get(i).get("client_spec"))+" "+String.valueOf(list.get(i).get("coating_standard")));
                        title_coating_type=getFormatString(String.valueOf(list.get(i).get("internal_coating")));
                        isHaveTitle=false;
                    }
                    datalist.add(new Label(1, row+8, String.valueOf(list.get(i).get("pipe_no")), wcf));
                    datalist.add(new Label(2, row+8, String.valueOf(list.get(i).get("air_temp")), wcf));
                    datalist.add(new Label(3, row+8, String.valueOf(list.get(i).get("relative_humidity")), wcf));
                    datalist.add(new Label(4, row+8, String.valueOf(list.get(i).get("dew_point")), wcf));
                    datalist.add(new Label(5, row+8, String.valueOf(list.get(i).get("pipe_temp")), wcf));
                    datalist.add(new Label(6, row+8, getFormatString(String.valueOf(list.get(i).get("surface_condition"))) , wcf));
                    datalist.add(new Label(7, row+8, getFormatString(String.valueOf(list.get(i).get("blast_finish_sa25"))), wcf));
                    datalist.add(new Label(8, row+8, String.valueOf(list.get(i).get("surface_dust_rating"))+"级", wcf));
                    datalist.add(new Label(9, row+8, String.valueOf(list.get(i).get("profile")), wcf));
                    datalist.add(new Label(10, row+8, String.valueOf(list.get(i).get("salt_contamination_after_blasting")), wcf));
                    datalist.add(new Label(11, row+8,String.valueOf(list.get(i).get("elapsed_time")), wcf));

                    result=String.valueOf(list.get(i).get("result"));
                    if(result!=null){
                        if(result.equals("0")){
                            result="不合格";
                        }else if(result.equals("1")){
                            result="合格";
                            qualifiedTotal++;
                        }else if(result.equals("2")){
                            result="待定";
                        }
                        else{
                            result="其他";
                        }
                    }else{
                        result=" ";
                    }
                    Label label19 = new Label(12, row+8, result, wcf);
                    datalist.add(label19);
                    if(String.valueOf(list.get(i).get("remark"))!=null&&!String.valueOf(list.get(i).get("remark")).equals("")){
                        sb.append("#"+list.get(i).get("pipe_no")+":"+list.get(i).get("remark")+" ");
                    }
                    //最后一行数据为空问题
                    index++;
                    row++;
                    if(index%13==0){
                        createRecordPdfTitle(datalist,3,8,12,4,5,title_project_name,title_pipe_size,title_standard,title_coating_type,dayOrNight,begin_time);

                        createRecordPdf(datalist,newPdfName,templateFullName,qualifiedTotal,2,20,12,20,index,row,sb,dayOrNight,stationIdDayList,stationIdNightList);
                    }
                }
                if(datalist.size()>0){
                    createRecordPdfTitle(datalist,3,8,12,4,5,title_project_name,title_pipe_size,title_standard,title_coating_type,dayOrNight,begin_time);

                    createRecordPdf(datalist,newPdfName,templateFullName,qualifiedTotal,2,20,12,20,index,row,sb,dayOrNight,stationIdDayList,stationIdNightList);
                }
            }else{
                createRecordNullPdf(datalist,3,8,12,1,4,5,8,newPdfName,templateFullName,dayOrNight,stationIdDayList,stationIdNightList);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //9.---------------内涂记录PDF
    public void  IdCoatRecord(HttpServletRequest request,String project_no,int dayOrNight,Date begin_time,Date end_time){
        String templateFullName=request.getSession().getServletContext().getRealPath("/")
                +"template/id_coating_epoxy_record_template.xls";
        //SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String newPdfName=null;
        try{
            List<HashMap<String,Object>>list=idCoatingProcessDao.getIdCoatRecord(project_no,begin_time,end_time);
            ArrayList<Label> datalist=new ArrayList<Label>();
            if(list.size()>0){
                int index=1,row=0;
                StringBuilder sb=new StringBuilder();
                String result="";
                int qualifiedTotal=0;
                boolean isHaveTitle=true;
                String title_project_name=" ",title_pipe_size=" ",title_standard=" ",title_coating_type=" ";
                for (int i=0;i<list.size();i++){
                    if(isHaveTitle){
                        //添加模板头部信息
                        title_project_name=String.valueOf(list.get(i).get("project_name"));
                        title_pipe_size=("Φ"+String.valueOf(list.get(i).get("od"))+"*"+String.valueOf(list.get(i).get("wt"))+"mm");
                        title_standard=(String.valueOf(list.get(i).get("client_spec"))+" "+String.valueOf(list.get(i).get("coating_standard")));
                        title_coating_type=getFormatString(String.valueOf(list.get(i).get("internal_coating")));
                        isHaveTitle=false;
                    }
                    datalist.add(new Label(1, row+8, String.valueOf(list.get(i).get("pipe_no")), wcf));
                    datalist.add(new Label(2, row+8, String.valueOf(list.get(i).get("coating_speed")), wcf));
                    datalist.add(new Label(3, row+8, String.valueOf(list.get(i).get("base_used")), wcf));
                    datalist.add(new Label(4, row+8, String.valueOf(list.get(i).get("base_batch")), wcf));
                    datalist.add(new Label(5, row+8, String.valueOf(list.get(i).get("curing_agent_used")), wcf));
                    datalist.add(new Label(6, row+8,String.valueOf(list.get(i).get("curing_agent_batch")), wcf));
                    datalist.add(new Label(7, row+8, String.valueOf(list.get(i).get("curing_temp")), wcf));
                    datalist.add(new Label(8, row+8, String.valueOf(list.get(i).get("curing_start_time")), wcf));
                    datalist.add(new Label(9, row+8, String.valueOf(list.get(i).get("curing_finish_time")), wcf));
                    datalist.add(new Label(10, row+8,timeformat.format(timeformat.parse(String.valueOf(list.get(i).get("operation_time")))), wcf));

                    result=String.valueOf(list.get(i).get("result"));
                    if(result!=null){
                        if(result.equals("0")){
                            result="不合格";
                        }else if(result.equals("1")){
                            result="合格";
                            qualifiedTotal++;
                        }else if(result.equals("2")){
                            result="待定";
                        }
                        else{
                            result=" ";
                        }
                    }else{
                        result=" ";
                    }
                    Label label19 = new Label(12, row+8, result, wcf);
                    datalist.add(label19);
                    if(String.valueOf(list.get(i).get("remark"))!=null&&!String.valueOf(list.get(i).get("remark")).equals("")){
                        sb.append("#"+list.get(i).get("pipe_no")+":"+list.get(i).get("remark")+" ");
                    }
                    //最后一行数据为空问题
                    index++;
                    row++;
                    if(index%13==0){
                        createRecordPdfTitle(datalist,3,8,12,4,5,title_project_name,title_pipe_size,title_standard,title_coating_type,dayOrNight,begin_time);

                        createRecordPdf(datalist,newPdfName,templateFullName,qualifiedTotal,2,20,12,20,index,row,sb,dayOrNight,stationIdDayList,stationIdNightList);
                    }
                }
                if(datalist.size()>0){
                    createRecordPdfTitle(datalist,3,8,12,4,5,title_project_name,title_pipe_size,title_standard,title_coating_type,dayOrNight,begin_time);

                    createRecordPdf(datalist,newPdfName,templateFullName,qualifiedTotal,2,20,12,20,index,row,sb,dayOrNight,stationIdDayList,stationIdNightList);
                }
            }else{
                createRecordNullPdf(datalist,3,8,12,1,4,5,8,newPdfName,templateFullName,dayOrNight,stationIdDayList,stationIdNightList);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //10.---------------内涂检验记录PDF
    public void  IdCoatInspectionRecord(HttpServletRequest request,String project_no,int dayOrNight,Date begin_time,Date end_time){
        String templateFullName=request.getSession().getServletContext().getRealPath("/")
                +"template/id_coating_epoxy_inspection_record_template.xls";
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String newPdfName=null;
        try{
            List<HashMap<String,Object>>list=idCoatingInspectionProcessDao.getIdCoatInspectionRecord(project_no,begin_time,end_time);
            ArrayList<Label> datalist=new ArrayList<Label>();
            if(list.size()>0){
                int index=1,row=0;
                StringBuilder sb=new StringBuilder();
                String result="";
                int qualifiedTotal=0;
                boolean isHaveTitle=true;
                String title_project_name=" ",title_pipe_size=" ",title_standard=" ",title_coating_type=" ";
                for (int i=0;i<list.size();i++){
                    if(isHaveTitle){
                        //添加模板头部信息
                        title_project_name=String.valueOf(list.get(i).get("project_name"));
                        title_pipe_size=("Φ"+String.valueOf(list.get(i).get("od"))+"*"+String.valueOf(list.get(i).get("wt"))+"mm");
                        title_standard=(String.valueOf(list.get(i).get("client_spec"))+" "+String.valueOf(list.get(i).get("coating_standard")));
                        title_coating_type=getFormatString(String.valueOf(list.get(i).get("internal_coating")));
                        isHaveTitle=false;
                    }
                    datalist.add(new Label(1, row+8, String.valueOf(list.get(i).get("pipe_no")), wcf));
                    String isSample=String.valueOf(list.get(i).get("is_sample"));
                    if(isSample!=null&&!isSample.equals("")){
                        if(isSample.equals("0")){
                            isSample="否";
                        }else if(isSample.equals("1")){
                            isSample="是";
                        }else{
                            isSample=" ";
                        }
                    }else{
                        isSample=" ";
                    }
                    datalist.add(new Label(2, row+8,isSample, wcf));
                    datalist.add(new Label(3, row+8, String.valueOf(list.get(i).get("wet_film_thickness_list")), wcf));
                    result=String.valueOf(list.get(i).get("result"));
                    if(result!=null){
                        if(result.equals("0")){
                            result="不合格";
                        }else if(result.equals("1")){
                            result="合格";
                            qualifiedTotal++;
                        }else if(result.equals("2")){
                            result="待定";
                        }
                        else{
                            result=" ";
                        }
                    }else{
                        result=" ";
                    }
                    Label label19 = new Label(12, row+8, result, wcf);
                    datalist.add(label19);
                    if(String.valueOf(list.get(i).get("remark"))!=null&&!String.valueOf(list.get(i).get("remark")).equals("")){
                        sb.append("#"+list.get(i).get("pipe_no")+":"+list.get(i).get("remark")+" ");
                    }
                    //最后一行数据为空问题
                    index++;
                    row++;
                    if(index%13==0){
                        createRecordPdfTitle(datalist,3,8,12,4,5,title_project_name,title_pipe_size,title_standard,title_coating_type,dayOrNight,begin_time);

                        createRecordPdf(datalist,newPdfName,templateFullName,qualifiedTotal,2,20,12,20,index,row,sb,dayOrNight,stationIdDayList,stationIdNightList);
                    }
                }
                if(datalist.size()>0){
                    createRecordPdfTitle(datalist,3,8,12,4,5,title_project_name,title_pipe_size,title_standard,title_coating_type,dayOrNight,begin_time);

                    createRecordPdf(datalist,newPdfName,templateFullName,qualifiedTotal,2,20,12,20,index,row,sb,dayOrNight,stationIdDayList,stationIdNightList);
                }
            }else{
                createRecordNullPdf(datalist,3,8,12,1,4,5,8,newPdfName,templateFullName,dayOrNight,stationIdDayList,stationIdNightList);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //11.---------------内涂终验记录PDF
    public void  IdFinalInspectionRecord(HttpServletRequest request,String project_no,int dayOrNight,Date begin_time,Date end_time){
        String templateFullName=request.getSession().getServletContext().getRealPath("/")
                +"template/id_coating_final_inspection_record_template.xls";
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String newPdfName=null;
        try{
            List<HashMap<String,Object>>list=idFinalInspectionProcessDao.getIdCoatFinalInspectionRecord(project_no,begin_time,end_time);
            ArrayList<Label> datalist=new ArrayList<Label>();
            if(list.size()>0){
                int index=1,row=0;
                StringBuilder sb=new StringBuilder();
                String result="",stencilRes="",stencilStr="",bevelRes="",bevelStr="";
                int qualifiedTotal=0;
                boolean isHaveTitle=true;
                String title_project_name=" ",title_pipe_size=" ",title_standard=" ",title_coating_type=" ";
                for (int i=0;i<list.size();i++){
                    if(isHaveTitle){
                        //添加模板头部信息
                        title_project_name=String.valueOf(list.get(i).get("project_name"));
                        title_pipe_size=("Φ"+String.valueOf(list.get(i).get("od"))+"*"+String.valueOf(list.get(i).get("wt"))+"mm");
                        title_standard=(String.valueOf(list.get(i).get("client_spec"))+" "+String.valueOf(list.get(i).get("coating_standard")));
                        title_coating_type=getFormatString(String.valueOf(list.get(i).get("internal_coating")));
                        isHaveTitle=false;
                    }
                    datalist.add(new Label(1, row+9, String.valueOf(list.get(i).get("pipe_no")), wcf));
                    datalist.add(new Label(2, row+9, String.valueOf(list.get(i).get("holidays")), wcf));
                    datalist.add(new Label(3, row+9, String.valueOf(list.get(i).get("holiday_tester_volts")), wcf));
                    datalist.add(new Label(4, row+9, String.valueOf(list.get(i).get("internal_repairs")), wcf));
                    bevelRes=String.valueOf(list.get(i).get("bevel_check"));
                    if(bevelRes!=null&&!bevelRes.equals("")){
                        if(bevelRes.equals("0")){
                            bevelStr="未检测";
                        }else if(bevelRes.equals("1")){
                            bevelStr="合格";
                        }else if(bevelRes.equals("2")){
                            bevelStr="不合格";
                        }
                    }
                    datalist.add(new Label(5, row+9, bevelStr, wcf));
                    datalist.add(new Label(6, row+9,String.valueOf(list.get(i).get("magnetism_list")), wcf));
                    stencilRes=String.valueOf(list.get(i).get("stencil_verification"));
                    if(stencilRes!=null&&!stencilRes.equals("")){
                        if(stencilRes.equals("0")){
                            stencilStr="未检测";
                        }else if(stencilRes.equals("1")){
                            stencilStr="合格";
                        }else if(stencilRes.equals("2")){
                            stencilStr="不合格";
                        }
                    }
                    datalist.add(new Label(7, row+9, stencilStr, wcf));
                    datalist.add(new Label(8, row+9,getFormatString(String.valueOf(list.get(i).get("surface_condition"))), wcf));

                    datalist.add(new Label(2, row+10, String.valueOf(list.get(i).get("dry_film_thickness_list")), wcf));
                    datalist.add(new Label(5, row+10, String.valueOf(list.get(i).get("cutback_length")), wcf));
                    datalist.add(new Label(8, row+10, String.valueOf(list.get(i).get("roughness_list")), wcf));

                    result=String.valueOf(list.get(i).get("result"));
                    if(result!=null){
                        if(result.equals("0")){
                            result="不合格";
                        }else if(result.equals("1")){
                            result="合格";
                            qualifiedTotal++;
                        }else if(result.equals("7")){
                            result="待定";
                        }
                        else{
                            result="其他";
                        }
                    }else{
                        result=" ";
                    }
                    Label label19 = new Label(12, row+10, result, wcf);
                    datalist.add(label19);
                    if(String.valueOf(list.get(i).get("remark"))!=null&&!String.valueOf(list.get(i).get("remark")).equals("")){
                        sb.append("#"+list.get(i).get("pipe_no")+":"+list.get(i).get("remark")+" ");
                    }
                    //最后一行数据为空问题
                    index+=2;
                    row+=2;
                    if(index%11==0){
                        createRecordPdfTitle(datalist,3,8,12,4,5,title_project_name,title_pipe_size,title_standard,title_coating_type,dayOrNight,begin_time);
                        createRecordPdf(datalist,newPdfName,templateFullName,qualifiedTotal,2,19,12,19,index,row,sb,dayOrNight,stationIdDayList,stationIdNightList);
                    }
                }
                if(datalist.size()>0){
                    createRecordPdfTitle(datalist,3,8,12,4,5,title_project_name,title_pipe_size,title_standard,title_coating_type,dayOrNight,begin_time);
                    createRecordPdf(datalist,newPdfName,templateFullName,qualifiedTotal,2,19,12,19,index,row,sb,dayOrNight,stationIdDayList,stationIdNightList);
                }
            }else {
                 createRecordNullPdf(datalist,3,8,12,1,4,5,9,newPdfName,templateFullName,dayOrNight,stationIdDayList,stationIdNightList);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //填补空白Bug
    private void AddLastWhiteSpace(ArrayList<Label> datalist,String remark,WritableCellFormat wcf){
        datalist.add(new Label(2,20,remark,wcf));
    }

    //导出外观检验记录PDF
    //InspectionRecordPDFOperation/exportBlastRecordToPDF
    @RequestMapping(value = "/exportBlastRecordToPDF",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String exportBlastRecordToPDF(HttpServletRequest request)  {
        //String pdfDirPath=request.getSession().getServletContext().getRealPath("/") + "upload/pdf/";
        File pdfDirFile=new File(pdfDirPath);
        if(!pdfDirFile.exists()){
            pdfDirFile.mkdir();
        }
        String templateFullName=request.getSession().getServletContext().getRealPath("/")
                +"template/od_blast_record_template.xls";
        //String pdfFullName=request.getSession().getServletContext().getRealPath("/") + "upload/pdf/BlastRecord.pdf";
       // String logoImageFullName=request.getSession().getServletContext().getRealPath("/") + "template/img/image002.jpg";

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

    //生成数据为空的pdf,参数(column1:project_name、standard所在列,column2:pipe_size、coating_type所在列,column3:班次、时间所在列,column4:空记录所在列
    //row1:project_name、pipe_size、时间所在行,row2:standard、coating_type、班次所在行,row3:空记录所在行)
    private void createRecordNullPdf(ArrayList<Label> datalist,int column1,int column2,int column3,int column4,int row1,int row2,int row3,String newPdfName,String templateFullName,int dayOrNight,List<String>stationDayList,List<String>stationNightList){
        datalist.add(new Label(column1, row1," ", wcf));
        datalist.add(new Label(column2, row1," ", wcf));
        datalist.add(new Label(column1, row2," ", wcf));
        datalist.add(new Label(column2, row2," ", wcf));
        datalist.add(new Label(column3, row2," ", wcf));
        datalist.add(new Label(column3, row1, " ", wcf));
        datalist.add(new Label(column4,row3,"今天暂无记录!",wcf));
        newPdfName=GenerateExcelToPDFUtil.PDFAutoMation(templateFullName,datalist,pdfFullName,logoImageFullName,fontPath,basePath);
        if(newPdfName!=null){
            if(dayOrNight==0){
                stationDayList.add(newPdfName);
            }else{
                stationNightList.add(newPdfName);
            }
            delSetPath.add(newPdfName);
        }
    }

    //公共函数生成PDF头部信息,参数(column1:project_name、standard所在列,column2:pipe_size、coating_type所在列,column3:班次、时间所在列,
    // row1:project_name、pipe_size、时间所在行,row2:standard、coating_type、班次所在行)
    private void createRecordPdfTitle(ArrayList<Label> datalist,int column1,int column2,int column3,int row1,int row2,String title_project_name,String title_pipe_size,String title_standard,String title_coating_type,int dayOrNight,Date begin_time){
            datalist.add(new Label(column1, row1,title_project_name, wcf));
            datalist.add(new Label(column2, row1,title_pipe_size, wcf));
            datalist.add(new Label(column1, row2,title_standard, wcf));
            datalist.add(new Label(column2, row2,title_coating_type, wcf));
            if(dayOrNight==0){
                datalist.add(new Label(column3, row2,"白班(Day)", wcf));
                datalist.add(new Label(column3, row1,sdf.format(begin_time), wcf));
            }else{
                datalist.add(new Label(column3, row2, "夜班(Night)", wcf));
                datalist.add(new Label(column3, row1,sdf.format(begin_time), wcf));
            }
    }

    //公共生成pdf的函数，参数列表(dataList:表格数据集合,newPdfName:pdf名字,templateFullName:pdf模板名字,x1为备注所在列 y1为备注所在行
    //x2为合格数所在列 y2为合格数所在行，qualifiedTotal:合格数,index:循环索引,row:行数,sb:备注内容,dayOrNight:班次判定))
    private void createRecordPdf(ArrayList<Label> datalist,String newPdfName,String templateFullName,int qualifiedTotal,int x1,int y1,int x2,int y2,int index,int row,StringBuilder sb,int dayOrNight,List<String>stationDayList,List<String>stationNightList){
        datalist.add(new Label(x1,y1,String.valueOf(sb.toString()),wcf));
        //添加合格数
        datalist.add(new Label(x2,y2,String.valueOf(qualifiedTotal),wcf));
        newPdfName=GenerateExcelToPDFUtil.PDFAutoMation(templateFullName,datalist,pdfFullName,logoImageFullName,fontPath,basePath);
        datalist.clear();
        qualifiedTotal=0;
        index=1;
        row=0;
        sb.setLength(0);
        if(newPdfName!=null){
            if(dayOrNight==0){
                stationDayList.add(newPdfName);
                //stationIdDayList.add(newPdfName);
            }else{
                stationNightList.add(newPdfName);
                //stationIdNightList.add(newPdfName);
            }
            delSetPath.add(newPdfName);
        }
    }

    //格式化为空的字符串
    public  String getFormatString(String param){
        if(param!=null&&!param.equals("")){
            return  param;
        }else{
            return  " ";
        }
    }


    //数组拆分两部分
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
    //获取剩磁平均值
    public String getAvgOfMagnetism(String strVal){
        String returnVal=" ";
        if(strVal.trim().length()>0){
            String valArr[]=strVal.split(",");
            Float totalNumber=0f;
            for (int i=0;i<valArr.length;i++){
                 totalNumber+=Float.parseFloat(valArr[i]);
            }
            returnVal=String.valueOf(new BigDecimal(totalNumber/valArr.length).setScale(0, BigDecimal.ROUND_HALF_UP));
        }
        return  returnVal;
    }
}
