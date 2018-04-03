package com.htcsweb.controller;


import com.htcsweb.dao.*;
import com.htcsweb.entity.*;
import com.htcsweb.util.FileBean;
import com.htcsweb.util.GenerateExcelToPDFUtil;
import com.htcsweb.util.MergePDF;
import com.htcsweb.util.ResponseUtil;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

//    @RequestMapping(value="getRecordReportPDF",produces="application/json;charset=UTF-8")
//    @ResponseBody
//    public  String getRecordReportPDF(HttpServletRequest request, HttpServletResponse response){
//        String flag="fail";
//        String pdfPath=request.getSession().getServletContext().getRealPath("/")+"upload/pdf/";
//        String dayOutputPDFFullName=pdfPath+"daymerge.pdf";
//        String nightOutputPDFFullName=pdfPath+"nightmerge.pdf";
//        try{
//            //判断合成文件是否存在
//            File fileDay=new File(dayOutputPDFFullName);
//            File fileNight=new File(nightOutputPDFFullName);
//            if(!fileDay.exists()){
//                fileDay.createNewFile();
//            }
//            if(!fileNight.exists()){
//                fileNight.createNewFile();
//            }
//            SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
//            //SimpleDateFormat timeformat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            String selectValStr=request.getParameter("selectValue");
//            String beginTimeStr=request.getParameter("beginTime");
//            String endTimeStr=request.getParameter("endTime");
//            Date start_time=null;
//            Date finish_time=null;
//            if(selectValStr!=null&&!selectValStr.equals("")){
//                int selectVal=Integer.parseInt(selectValStr);
//                if(beginTimeStr==null||beginTimeStr.equals("")||endTimeStr==null||endTimeStr.equals("")){
//                    beginTimeStr=format.format(new Date());
//                    endTimeStr=format.format(new Date());
//                }
//                start_time=format.parse(beginTimeStr);
//                finish_time=format.parse(endTimeStr);
//                //时间集合为包含前面不包含后面
//                List<String>listDate=getBetweenDates(start_time,finish_time);
//                Date day_begin_time=null,day_end_time=null,night_begin_time=null,night_end_time=null;
//                List<String> dayPdfNamePathList=new ArrayList<>();
//                List<String> nightPdfNamePathList=new ArrayList<>();
//                List<String>dayAndNightList=new ArrayList<>();
//                String dayTempName=null,nightTempName=null;
//                for (int i=0;i<listDate.size();i++){
//                    day_begin_time=timeformat.parse(listDate.get(i)+" 08:00:00");
//                    day_end_time=timeformat.parse(listDate.get(i)+" 20:00:00");
//                    night_begin_time=timeformat.parse(listDate.get(i)+" 20:00:00");
//                    night_end_time=timeformat.parse(getNextDay(listDate.get(i))+" 08:00:00");
//                    switch (selectVal) {
//                        case 0://外喷砂工序
//                            dayTempName=getOdBlastRecord(request,0,day_begin_time,day_end_time);//生成白班报表PDF
//                            if(dayTempName!=null){
//                                dayPdfNamePathList.add(dayTempName);
//                                dayAndNightList.add(dayTempName);
//                            }
//                            nightTempName=getOdBlastRecord(request,1,night_begin_time,night_end_time);//生成夜班报表PDF
//                            if(nightTempName!=null){
//                                nightPdfNamePathList.add(nightTempName);
//                                dayAndNightList.add(nightTempName);
//                            }
//                            flag="success";
//                            break;
//                        case 1://外喷砂检验工序
//
//                            break;
//                        case 2://外涂工序(2FBE)
//
//                            break;
//                        case 3://外涂检验工序(2FBE)
//
//                            break;
//                        case 4://外涂工序(3LPE)
//
//                            break;
//                        case 5://外涂检验工序(3LPE)
//
//                            break;
//                        case 6://外涂层终检工序
//
//                            break;
//                        case 7://内喷砂工序
//
//                            break;
//                        case 8://内喷砂检验工序
//
//                            break;
//                        case 9://内涂工序
//
//                            break;
//                    }
//                }
//                //然后根据白班 夜班的集合，Merge Pdf,最后打包下载
//                MergePDF.MergePDFs(dayPdfNamePathList,dayOutputPDFFullName);
//                MergePDF.MergePDFs(nightPdfNamePathList,nightOutputPDFFullName);
//                List<String>dayNightPdf=new ArrayList<>();
//                dayNightPdf.add(dayOutputPDFFullName);
//                dayNightPdf.add(nightOutputPDFFullName);
//                ResponseUtil.downLoadPdf(dayNightPdf,request,response);
//            }else{
//                //没有获取到数据，生成失败
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return  flag;
//    }

    @RequestMapping(value="getRecordReportPDF",produces="application/json;charset=UTF-8")
    @ResponseBody
    public  String getRecordReportPDF(HttpServletRequest request, HttpServletResponse response){
        String basePath=request.getSession().getServletContext().getRealPath("/");
        Date start_time=null;
        Date finish_time=null;
        List<String>delSetPath=new ArrayList<>();//定义删除pdf集合，用于生成zip后删除所有的临时文件
        List<String>listDate=new ArrayList<>();//时间区间集合
        List<String>listPdfSetPath=new ArrayList<>();//pdf白班夜班集合
        String pdfSetDayPath="",pdfSetNightPath="";
        Date day_begin_time=null,day_end_time=null,night_begin_time=null,night_end_time=null;//定义每天班次的开始结束时间
        //是否成功标识
        String flag="fail";
        //先获取选择的日期区间之间的所有日期
        String project_name=request.getParameter("project_name");
        if(project_name==null||project_name.equals("")){
            project_name="";
        }
        String beginTimeStr=request.getParameter("beginTime");
        String endTimeStr=request.getParameter("endTime");
        //定义临时文件名
        String tempDayName=null,tempNightName=null;
        //定义工位的生成pdf的集合
        List<String>stationDayList=new ArrayList<>();
        List<String>stationNightList=new ArrayList<>();
        try{
            if(beginTimeStr==null||beginTimeStr.equals("")||endTimeStr==null||endTimeStr.equals("")){
                beginTimeStr=sdf.format(new Date());
                endTimeStr=sdf.format(new Date());
            }
            start_time=sdf.parse(beginTimeStr);
            finish_time=sdf.parse(endTimeStr);
            listDate=getBetweenDates(start_time,finish_time);
            //遍历时间区间，生成白班、夜班集合PDF文件
            for (int i=0;i<listDate.size();i++){
                pdfSetDayPath=basePath+"upload/pdf/"+(project_name+"_"+listDate.get(i)+"_白班(Day).pdf");
                pdfSetNightPath=basePath+"upload/pdf/"+(project_name+"_"+listDate.get(i)+"_夜班(Night).pdf");
                System.out.println(pdfSetDayPath);
                System.out.println(pdfSetNightPath);
                File fileday=new File(pdfSetDayPath);
                File filenight=new File(pdfSetNightPath);

                if(!fileday.exists()){
                     fileday.createNewFile();
                }
                if(!filenight.exists()){
                     filenight.createNewFile();
                }
                listPdfSetPath.add(pdfSetDayPath);
                listPdfSetPath.add(pdfSetNightPath);
            }
            //按天生成白班和夜班的pdf,然后融合到白班的pdf和夜班的pdf中
            for (int i=0;i<listDate.size();i++){
                day_begin_time=timeformat.parse(listDate.get(i)+" 08:00:00");
                day_end_time=timeformat.parse(listDate.get(i)+" 20:00:00");
                night_begin_time=timeformat.parse(listDate.get(i)+" 20:00:00");
                night_end_time=timeformat.parse(getNextDay(listDate.get(i))+" 08:00:00");
                //1.生成当天的外打砂工位的模板
                tempDayName=getOdBlastRecord(request,0,day_begin_time,day_end_time);//生成白班报表PDF
                if(tempDayName!=null){
                    stationDayList.add(tempDayName);
                    delSetPath.add(tempDayName);
                }
                tempNightName=getOdBlastRecord(request,1,night_begin_time,night_end_time);//生成夜班报表PDF
                if(tempNightName!=null){
                    stationNightList.add(tempNightName);
                    delSetPath.add(tempNightName);
                }
                //.......

                //最后分别融合白班集合和夜班集合
                MergePDF.MergePDFs(stationDayList,pdfSetDayPath);
                MergePDF.MergePDFs(stationNightList,pdfSetNightPath);
            }
            //定时删除临时文件
            for (int j=0;j<delSetPath.size();j++){
                File file=new File(delSetPath.get(j));
                if(file.exists()){
                    file.delete();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }


    //1.---------------获取外打砂记录PDF
    // 返回生成的pdf名称
    public  String getOdBlastRecord(HttpServletRequest request,int dayOrNight,Date begin_time,Date end_time){

        String templateFullName=request.getSession().getServletContext().getRealPath("/")
                +"template/od_blast_record_template.xls";
        String newPdfName=null;
        try{
            System.out.println(String.valueOf(begin_time==null)+":"+String.valueOf(end_time==null));
            System.out.println(timeformat.format(begin_time)+":"+timeformat.format(end_time));
            List<OdBlastProcess>list=odblastprocessDao.getOdBlastRecord(begin_time,end_time);
            if(list.size()>0){
                ArrayList<Label> datalist=new ArrayList<Label>();
                //添加模板头部信息
                if(dayOrNight==0){
                    datalist.add(new Label(13, 5,"白班(Day)", wcf));
                }else{
                    datalist.add(new Label(13, 5, "夜班(Night)", wcf));
                }
                int index=1,row=0;
                StringBuilder sb=new StringBuilder();
                String result="";
                int qualifiedTotal=0;
                for (int i=0;i<list.size();i++){
                    Label label1 = new Label(1, row+8, list.get(i).getPipe_no(), wcf);
                    datalist.add(label1);
                    String isClear="是";
                    String marking=list.get(i).getMarking();
                    if(marking!=null){
                        if(!marking.equals("0")){
                            isClear="否";
                        }
                    }else {
                        isClear=" ";
                    }
                    Label label2 = new Label(2, row+8, isClear, wcf);
                    datalist.add(label2);
                    Label label3 = new Label(3, row+8, getFormatString(list.get(i).getSurface_condition()), wcf);
                    datalist.add(label3);
                    Label label4 = new Label(4, row+8, String.valueOf(list.get(i).getSalt_contamination_before_blasting()), wcf);
                    datalist.add(label4);
                    Label label5 = new Label(5, row+8, String.valueOf(list.get(i).getPreheat_temp()), wcf);
                    datalist.add(label5);
                    Label label6 = new Label(6, row+8, String.valueOf(list.get(i).getBlast_line_speed()), wcf);
                    datalist.add(label6);
                    Label label7 = new Label(7, row+8, String.valueOf(list.get(i).getRinse_water_conductivity()), wcf);
                    datalist.add(label7);
                    Label label8 = new Label(8, row+8, String.valueOf(list.get(i).getAbrasive_conductivity()), wcf);
                    datalist.add(label8);
                    Label label9 = new Label(9, row+8, String.valueOf(list.get(i).getAlkaline_dwell_time()), wcf);
                    datalist.add(label9);
                    Label label10 = new Label(10, row+8, String.valueOf(list.get(i).getAcid_concentration()), wcf);
                    datalist.add(label10);
                    Label label11 = new Label(11, row+8, String.valueOf(list.get(i).getAcid_wash_time()), wcf);
                    datalist.add(label11);
                    Label label12= new Label(12, row+8, String.valueOf(list.get(i).getAcid_concentration()), wcf);
                    datalist.add(label12);
                    result=list.get(i).getResult();
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
                    Label label13 = new Label(13, row+8, result, wcf);
                    datalist.add(label13);
                    if(list.get(i).getRemark()!=null&&!list.get(i).getRemark().equals("")) {
                        sb.append("#" + list.get(i).getPipe_no() + ":" + list.get(i).getRemark() + " ");
                    }
                    //最后一行数据为空问题
                    index++;
                    row++;
                    if(index%13==0){
                        AddLastWhiteSpace(datalist,getFormatString(sb.toString()),wcf);
                        //添加合格数
                        datalist.add(new Label(13,20,String.valueOf(qualifiedTotal),wcf));
                        //到结束
                         newPdfName=GenerateExcelToPDFUtil.PDFAutoMation(templateFullName,datalist,pdfFullName,logoImageFullName,fontPath,basePath);
                        datalist.clear();
                        qualifiedTotal=0;
                        index=1;
                        row=0;sb.setLength(0);
                    }
                }
                if(datalist.size()>0){
                    AddLastWhiteSpace(datalist,getFormatString(sb.toString()),wcf);
                    //添加合格数
                    datalist.add(new Label(13,20,String.valueOf(qualifiedTotal),wcf));
                    newPdfName=GenerateExcelToPDFUtil.PDFAutoMation(templateFullName,datalist,pdfFullName,logoImageFullName,fontPath,basePath);
                    datalist.clear();
                    qualifiedTotal=0;
                    index=1;
                    row=0;sb.setLength(0);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
         return newPdfName;
    }

    //2.---------------获取外打砂检验记录PDF
    @RequestMapping("getOdBlastInspectionRecord")
    public  String getOdBlastInspectionRecord(HttpServletRequest request){
        String begin_time="2018-01-03";
        String end_time="2018-03-30";
        String templateFullName=request.getSession().getServletContext().getRealPath("/")
                +"template/od_blast_inspection_record_template.xls";
        try{
            if(begin_time!=null&&begin_time!=""){
                beginTime=sdf.parse(begin_time);
            }
            if(end_time!=null&&end_time!=""){
                endTime=sdf.parse(end_time);
            }
            List<OdBlastInspectionProcess>list=odBlastInspectionProcessDao.getOdBlastInspectionRecord(beginTime,endTime);
            ArrayList<Label> datalist=new ArrayList<Label>();
            int index=1,row=0;
            StringBuilder sb=new StringBuilder();
            String result="";
            int qualifiedTotal=0;
            for (int i=0;i<list.size();i++){
                Label label1 = new Label(1, row+8, list.get(i).getPipe_no(), wcf);
                datalist.add(label1);
                Label label2 = new Label(2, row+8, String.valueOf(list.get(i).getAir_temp()), wcf);
                datalist.add(label2);
                Label label3 = new Label(3, row+8, String.valueOf(list.get(i).getRelative_humidity()), wcf);
                datalist.add(label3);
                Label label4 = new Label(4, row+8, String.valueOf(list.get(i).getDew_point()), wcf);
                datalist.add(label4);
                Label label5 = new Label(5, row+8, String.valueOf(list.get(i).getPipe_temp()), wcf);
                datalist.add(label5);
                Label label6 = new Label(6, row+8,getFormatString(list.get(i).getSurface_condition()), wcf);
                datalist.add(label6);
                Label label7 = new Label(7, row+8, String.valueOf(list.get(i).getBlast_finish_sa25()), wcf);
                datalist.add(label7);
                Label label8 = new Label(8, row+8, String.valueOf(list.get(i).getSurface_dust_rating()), wcf);
                datalist.add(label8);
                Label label9 = new Label(9, row+8, String.valueOf(list.get(i).getProfile()), wcf);
                datalist.add(label9);
                Label label10 = new Label(10, row+8, String.valueOf(list.get(i).getSalt_contamination_after_blasting()), wcf);
                datalist.add(label10);
                String isOil=list.get(i).getOil_water_in_air_compressor();
                String oil=" ";
                if(isOil!=null){
                    if(isOil.equals("1")){
                        oil="是";
                    }else{
                        oil="否";
                    }
                }
                Label label11 = new Label(11, row+8,oil, wcf);
                datalist.add(label11);
                Label label12=new Label(12,row+8,String.valueOf(list.get(i).getElapsed_time()),wcf);
                datalist.add(label12);
                result=list.get(i).getResult();
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
                Label label13 = new Label(13, row+8, result, wcf);
                datalist.add(label13);
                if(list.get(i).getRemark()!=null&&!list.get(i).getRemark().equals("")){
                    sb.append("#"+list.get(i).getPipe_no()+":"+list.get(i).getRemark()+" ");
                }
                //最后一行数据为空问题
                index++;
                row++;
                if(index%13==0){
                    datalist.add(new Label(2,20,getFormatString(sb.toString()),wcf));
                    //添加合格数
                    datalist.add(new Label(13,20,String.valueOf(qualifiedTotal),wcf));
                    //到结束
                    GenerateExcelToPDFUtil.PDFAutoMation(templateFullName,datalist,pdfFullName,logoImageFullName,fontPath,basePath);
                    datalist.clear();
                    qualifiedTotal=0;
                    index=1;
                    row=0;sb.setLength(0);
                }
            }
            if(datalist.size()>0){
                datalist.add(new Label(2,20,getFormatString(sb.toString()),wcf));
                //添加合格数
                datalist.add(new Label(13,20,String.valueOf(qualifiedTotal),wcf));
                GenerateExcelToPDFUtil.PDFAutoMation(templateFullName,datalist,pdfFullName,logoImageFullName,fontPath,basePath);
                datalist.clear();
                qualifiedTotal=0;
                index=1;
                row=0;sb.setLength(0);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return  null;
    }

    //3.---------------获取外涂(2FBE)记录PDF
    @RequestMapping("getOdCoat2FBERecord")
    public  String getOdCoat2FBERecord(HttpServletRequest request){
        String begin_time="2018-01-03";
        String end_time="2018-03-30";
        String templateFullName=request.getSession().getServletContext().getRealPath("/")
                +"template/od_coating_2fbe_record_template.xls";
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            if(begin_time!=null&&begin_time!=""){
                beginTime=sdf.parse(begin_time);
            }
            if(end_time!=null&&end_time!=""){
                endTime=sdf.parse(end_time);
            }
            List<OdCoatingProcess>list=odCoatingProcessDao.getOd2FBECoatRecord(beginTime,endTime);
            ArrayList<Label> datalist=new ArrayList<Label>();
            int index=1,row=0;
            StringBuilder sb=new StringBuilder();
            String result="";
            int qualifiedTotal=0;
            for (int i=0;i<list.size();i++){
                Label label1 = new Label(1, row+9, getFormatString(list.get(i).getPipe_no()), wcf);
                datalist.add(label1);
                Label label2 = new Label(2, row+9, String.valueOf(list.get(i).getCoating_line_speed()), wcf);
                datalist.add(label2);
                Label label3 = new Label(3, row+9, getFormatString(list.get(i).getBase_coat_used()), wcf);
                datalist.add(label3);
                Label label4 = new Label(4, row+9, getFormatString(list.get(i).getBase_coat_lot_no()), wcf);
                datalist.add(label4);
                Label label5 = new Label(5, row+9, getFormatString(list.get(i).getTop_coat_used()), wcf);
                datalist.add(label5);
                Label label6 = new Label(6, row+9,getFormatString(list.get(i).getTop_coat_lot_no()), wcf);
                datalist.add(label6);
                Label label7 = new Label(7, row+9,String.valueOf(list.get(i).getBase_coat_gun_count()), wcf);
                datalist.add(label7);
                Label label8 = new Label(8, row+9, String.valueOf(list.get(i).getTop_coat_gun_count()), wcf);
                datalist.add(label8);
                Label label9 = new Label(9, row+9, String.valueOf(list.get(i).getAir_pressure()), wcf);
                datalist.add(label9);
                Label label10 = new Label(10, row+9, String.valueOf(list.get(i).getCoating_voltage()), wcf);
                datalist.add(label10);
                Label label11= new Label(11, row+9,String.valueOf(list.get(i).getGun_distance()), wcf);
                datalist.add(label11);

                Label label12= new Label(2, row+10,String.valueOf(list.get(i).getSpray_speed()), wcf);
                datalist.add(label12);
                Label label13= new Label(3, row+10,String.valueOf(list.get(i).getApplication_temp()), wcf);
                datalist.add(label13);
                Label label14= new Label(4, row+10,String.valueOf(list.get(i).getApplication_voltage()), wcf);
                datalist.add(label14);

                String dateStr=format.format(list.get(i).getOperation_time());
                Label label15= new Label(5, row+10,dateStr, wcf);
                datalist.add(label15);

                Label label16= new Label(6, row+10,String.valueOf(list.get(i).getTo_first_touch_duration()), wcf);
                datalist.add(label16);
                Label label17= new Label(7, row+10,String.valueOf(list.get(i).getTo_quench_duration()), wcf);
                datalist.add(label17);


                result=list.get(i).getResult();
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
                Label label18 = new Label(12, row+9, result, wcf);
                datalist.add(label18);
                if(list.get(i).getRemark()!=null&&!list.get(i).getRemark().equals("")){
                    sb.append("#"+list.get(i).getPipe_no()+":"+list.get(i).getRemark()+" ");
                }
                //最后一行数据为空问题
                index+=2;
                row+=2;
                if(index%11==0){
                    datalist.add(new Label(2,19,getFormatString(sb.toString()),wcf));
                    //添加合格数
                    datalist.add(new Label(12,19,String.valueOf(qualifiedTotal),wcf));
                    //到结束
                    GenerateExcelToPDFUtil.PDFAutoMation(templateFullName,datalist,pdfFullName,logoImageFullName,fontPath,basePath);
                    datalist.clear();
                    qualifiedTotal=0;
                    index=1;
                    row=0;sb.setLength(0);
                }
            }
            if(datalist.size()>0){
                datalist.add(new Label(2,19,getFormatString(sb.toString()),wcf));
                //添加合格数
                datalist.add(new Label(12,19,String.valueOf(qualifiedTotal),wcf));
                GenerateExcelToPDFUtil.PDFAutoMation(templateFullName,datalist,pdfFullName,logoImageFullName,fontPath,basePath);
                datalist.clear();
                index=1;sb.setLength(0);
                row=0;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return  null;
    }

    //4.---------------获取外涂(2FBE)检验记录PDF
    @RequestMapping("getOdCoat2FBEInspectionRecord")
    public  String getOdCoat2FBEInspectionRecord(HttpServletRequest request){
        String begin_time="2018-01-03";
        String end_time="2018-03-30";
        String templateFullName=request.getSession().getServletContext().getRealPath("/")
                +"template/od_coating_2fbe_inspection_record_template.xls";
        try{
            if(begin_time!=null&&begin_time!=""){
                beginTime=sdf.parse(begin_time);
            }
            if(end_time!=null&&end_time!=""){
                endTime=sdf.parse(end_time);
            }
            List<OdCoatingInspectionProcess>list=odCoatingInspectionProcessDao.getOd2FBECoatInspectionRecord(beginTime,endTime);
            ArrayList<Label> datalist=new ArrayList<Label>();
            int index=1,row=0;
            StringBuilder sb=new StringBuilder();
            String result="";
            int qualifiedTotal=0;
            for (int i=0;i<list.size();i++){
                Label label1 = new Label(1, row+9, getFormatString(list.get(i).getPipe_no()), wcf);
                datalist.add(label1);
                Label label2 = new Label(2, row+9, String.valueOf(list.get(i).getHolidays()), wcf);
                datalist.add(label2);
                Label label3 = new Label(3, row+9, String.valueOf(list.get(i).getHoliday_tester_volts()), wcf);
                datalist.add(label3);
                Label label4 = new Label(4, row+9, String.valueOf(list.get(i).getRepairs()), wcf);
                datalist.add(label4);
                String bevel=list.get(i).getBevel();
                String label5txt="未检测";
                if(bevel!=null){
                    if(bevel.equals("1")){
                        label5txt="合格";
                    }else if(bevel.equals("2")){
                        label5txt="不合格";
                    }
                }
                Label label5 = new Label(5, row+9,label5txt, wcf);
                datalist.add(label5);
                String Adhesion=String.valueOf(list.get(i).getAdhesion_rating());
                String label6txt="未检测";
                if(Adhesion!=null){
                    if(Adhesion.equals("1")){
                        label6txt="合格";
                    }else if(Adhesion.equals("2")){
                        label6txt="不合格";
                    }
                }
                Label label6 = new Label(6, row+9,label6txt, wcf);
                datalist.add(label6);
                String isSample=list.get(i).getIs_sample();
                String label7Txt=" ";
                if(isSample!=null){
                    if(isSample.equals("0")){
                        label7Txt="否";
                    }else if(isSample.equals("1")){
                        label7Txt="是";
                    }
                }
                Label label7 = new Label(7, row+9,label7Txt, wcf);
                datalist.add(label7);

                Label label8 = new Label(8, row+9, getFormatString(list.get(i).getSurface_condition()), wcf);
                datalist.add(label8);
                Label label9 = new Label(2, row+10,getFormatString(list.get(i).getBase_coat_thickness_list()), wcf);
                datalist.add(label9);
                Label label10 = new Label(5, row+10, getFormatString(list.get(i).getTop_coat_thickness_list()), wcf);
                datalist.add(label10);

                Label label11 = new Label(8, row+10,getFormatString(list.get(i).getTotal_coating_thickness_list()), wcf);
                datalist.add(label11);
                result=list.get(i).getResult();
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
                Label label12 = new Label(12, row+10, result, wcf);
                datalist.add(label12);
                if(list.get(i).getRemark()!=null&&!list.get(i).getRemark().equals("")){
                    sb.append("#"+list.get(i).getPipe_no()+":"+list.get(i).getRemark()+" ");
                }
                //最后一行数据为空问题
                index+=2;
                row+=2;
                if(index%11==0){
                    datalist.add(new Label(2,19,getFormatString(sb.toString()),wcf));
                    //添加合格数
                    datalist.add(new Label(12,19,String.valueOf(qualifiedTotal),wcf));
                    //到结束
                    GenerateExcelToPDFUtil.PDFAutoMation(templateFullName,datalist,pdfFullName,logoImageFullName,fontPath,basePath);
                    datalist.clear();
                    qualifiedTotal=0;
                    index=1;
                    row=0;sb.setLength(0);
                }
            }
            if(datalist.size()>0){
                datalist.add(new Label(2,19,getFormatString(sb.toString()),wcf));
                datalist.add(new Label(12,19,String.valueOf(qualifiedTotal),wcf));
                GenerateExcelToPDFUtil.PDFAutoMation(templateFullName,datalist,pdfFullName,logoImageFullName,fontPath,basePath);
                datalist.clear();
                qualifiedTotal=0;
                index=1;
                row=0;sb.setLength(0);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return  null;
    }

    //5.---------------获取外涂(3LPE)记录PDF
    @RequestMapping("getOdCoat3LPERecord")
    public  String getOdCoat3LPERecord(HttpServletRequest request){
        String begin_time="2018-01-03";
        String end_time="2018-03-30";
        String templateFullName=request.getSession().getServletContext().getRealPath("/")
                +"template/od_coating_3lpe_record_template.xls";
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            if(begin_time!=null&&begin_time!=""){
                beginTime=sdf.parse(begin_time);
            }
            if(end_time!=null&&end_time!=""){
                endTime=sdf.parse(end_time);
            }
            List<OdCoating3LpeProcess>list=odCoating3LpeProcessDao.getOd3LPECoatRecord(beginTime,endTime);
            ArrayList<Label> datalist=new ArrayList<Label>();
            int index=1,row=0;
            StringBuilder sb=new StringBuilder();
            String result="";
            int qualifiedTotal=0;
            for (int i=0;i<list.size();i++){
                Label label1 = new Label(1, row+9, getFormatString(list.get(i).getPipe_no()), wcf);
                datalist.add(label1);
                Label label2 = new Label(2, row+9, String.valueOf(list.get(i).getCoating_line_speed()), wcf);
                datalist.add(label2);
                Label label3 = new Label(3, row+9, getFormatString(list.get(i).getBase_coat_used()), wcf);
                datalist.add(label3);
                Label label4 = new Label(4, row+9, getFormatString(list.get(i).getBase_coat_lot_no()), wcf);
                datalist.add(label4);
                Label label5 = new Label(5, row+9, getFormatString(list.get(i).getMiddle_coat_used()), wcf);
                datalist.add(label5);
                Label label6 = new Label(6, row+9,getFormatString(list.get(i).getMiddle_coat_lot_no()), wcf);
                datalist.add(label6);
                Label label7 = new Label(7, row+9, getFormatString(list.get(i).getTop_coat_used()), wcf);
                datalist.add(label7);
                Label label8 = new Label(8, row+9, getFormatString(list.get(i).getTop_coat_lot_no()), wcf);
                datalist.add(label8);
                Label label9 = new Label(9, row+9, String.valueOf(list.get(i).getAir_pressure()), wcf);
                datalist.add(label9);
                Label label10 = new Label(10, row+9, String.valueOf(list.get(i).getCoating_voltage()), wcf);
                datalist.add(label10);
                Label label11 = new Label(11, row+9,String.valueOf(list.get(i).getGun_distance()), wcf);
                datalist.add(label11);

                Label label12 = new Label(2, row+10, String.valueOf(list.get(i).getSpray_speed()), wcf);
                datalist.add(label12);
                Label label13 = new Label(3, row+10, String.valueOf(list.get(i).getBase_coat_gun_count()), wcf);
                datalist.add(label13);
                Label label14= new Label(4, row+10,String.valueOf(list.get(i).getApplication_temp()), wcf);
                datalist.add(label14);
                Label label15 = new Label(5, row+10, String.valueOf(list.get(i).getApplication_voltage()), wcf);
                datalist.add(label15);
                String dateSter=format.format(list.get(i).getOperation_time());
                Label label16 = new Label(6, row+10, dateSter, wcf);
                datalist.add(label16);
                Label label17 = new Label(7, row+10, String.valueOf(list.get(i).getTo_first_touch_duration()), wcf);
                datalist.add(label17);
                Label label18 = new Label(8, row+10, String.valueOf(list.get(i).getTo_quench_duration()), wcf);
                datalist.add(label18);

                result=list.get(i).getResult();
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
                if(list.get(i).getRemark()!=null&&!list.get(i).getRemark().equals("")){
                    sb.append("#"+list.get(i).getPipe_no()+":"+list.get(i).getRemark()+" ");
                }
                //最后一行数据为空问题
                index+=2;
                row+=2;
                if(index%11==0){
                    datalist.add(new Label(2,19,getFormatString(sb.toString()),wcf));
                    //添加合格数
                    datalist.add(new Label(12,19,String.valueOf(qualifiedTotal),wcf));
                    //到结束
                    GenerateExcelToPDFUtil.PDFAutoMation(templateFullName,datalist,pdfFullName,logoImageFullName,fontPath,basePath);
                    datalist.clear();
                    qualifiedTotal=0;
                    index=1;
                    row=0;
                    sb.setLength(0);
                }
            }
            if(datalist.size()>0){
                datalist.add(new Label(2,19,String.valueOf(sb.toString()),wcf));
                //添加合格数
                datalist.add(new Label(12,19,String.valueOf(qualifiedTotal),wcf));
                GenerateExcelToPDFUtil.PDFAutoMation(templateFullName,datalist,pdfFullName,logoImageFullName,fontPath,basePath);
                datalist.clear();
                qualifiedTotal=0;
                index=1;
                row=0;
                sb.setLength(0);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return  null;
    }

    //6.---------------获取外涂(3LPE)检验记录PDF
    @RequestMapping("getOdCoat3LPEInspectionRecord")
    public  String getOdCoat3LPEInspectionRecord(HttpServletRequest request){
        String begin_time="2018-01-03";
        String end_time="2018-03-30";
        String templateFullName=request.getSession().getServletContext().getRealPath("/")
                +"template/od_coating_3lpe_inspection_record_template.xls";

        try{
            if(begin_time!=null&&begin_time!=""){
                beginTime=sdf.parse(begin_time);
            }
            if(end_time!=null&&end_time!=""){
                endTime=sdf.parse(end_time);
            }
            List<OdCoating3LpeInspectionProcess>list=odCoating3LpeInspectionProcessDao.getOd3LPECoatInspectionRecord(beginTime,endTime);
            ArrayList<Label> datalist=new ArrayList<Label>();
            int index=1,row=0;
            StringBuilder sb=new StringBuilder();
            String result="";
            int qualifiedTotal=0;
            for (int i=0;i<list.size();i++){
                Label label1 = new Label(1, row+9, getFormatString(list.get(i).getPipe_no()), wcf);
                datalist.add(label1);
                Label label2 = new Label(2, row+9, String.valueOf(list.get(i).getHolidays()), wcf);
                datalist.add(label2);
                Label label3 = new Label(3, row+9, String.valueOf(list.get(i).getHoliday_tester_volts()), wcf);
                datalist.add(label3);
                Label label4 = new Label(4, row+9, String.valueOf(list.get(i).getRepairs()), wcf);
                datalist.add(label4);
                String bevel=list.get(i).getBevel();
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
                String Adhesion=String.valueOf(list.get(i).getAdhesion_rating());
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
                String isSample=list.get(i).getIs_sample();
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
                Label label8 = new Label(8, row+9, getFormatString(list.get(i).getSurface_condition()), wcf);
                datalist.add(label8);

                Label label9 = new Label(2, row+10, String.valueOf(list.get(i).getBase_coat_thickness_list()), wcf);
                datalist.add(label9);
                Label label10 = new Label(4, row+10, String.valueOf(list.get(i).getMiddle_coat_thickness_list()), wcf);
                datalist.add(label10);
                Label label11 = new Label(6, row+10, String.valueOf(list.get(i).getTop_coat_thickness_list()), wcf);
                datalist.add(label11);
                Label label12 = new Label(8, row+10,String.valueOf(list.get(i).getTotal_coating_thickness_list()), wcf);
                datalist.add(label12);
                result=list.get(i).getResult();
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
                if(list.get(i).getRemark()!=null&&!list.get(i).getRemark().equals("")){
                    sb.append("#"+list.get(i).getPipe_no()+":"+list.get(i).getRemark()+" ");
                }
                //最后一行数据为空问题
                index++;
                row+=2;
                if(index%5==0){
                    datalist.add(new Label(2,19,getFormatString(sb.toString()),wcf));
                    //到结束
                    datalist.add(new Label(12,19,String.valueOf(qualifiedTotal),wcf));
                    GenerateExcelToPDFUtil.PDFAutoMation(templateFullName,datalist,pdfFullName,logoImageFullName,fontPath,basePath);
                    datalist.clear();
                    sb.setLength(0);
                    index=1;
                    row=0;
                }
            }
            if(datalist.size()>0){
                datalist.add(new Label(2,19,getFormatString(sb.toString()),wcf));
                datalist.add(new Label(12,19,String.valueOf(qualifiedTotal),wcf));
                GenerateExcelToPDFUtil.PDFAutoMation(templateFullName,datalist,pdfFullName,logoImageFullName,fontPath,basePath);
                datalist.clear();
                index=1;
                row=0;sb.setLength(0);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return  null;
    }

    //7.---------------获取外涂终检记录PDF
    @RequestMapping("getOdCoatFinalInspectionRecord")
//    public  String getOdCoatFinalInspectionRecord(HttpServletRequest request){
//        String begin_time="2018-01-03";
//        String end_time="2018-03-30";
//        String templateFullName=request.getSession().getServletContext().getRealPath("/")
//                +"template/od_final_inspection_record_template.xls";
//        try{
//            if(begin_time!=null&&begin_time!=""){
//                beginTime=sdf.parse(begin_time);
//            }
//            if(end_time!=null&&end_time!=""){
//                endTime=sdf.parse(end_time);
//            }
//            List<OdFinalInspectionProcess>list=odFinalInspectionProcessDao.getOdFianlInspectionRecord(beginTime,endTime);
//            ArrayList<Label> datalist=new ArrayList<Label>();
//            int index=1,row=0;
//            StringBuilder sb=new StringBuilder();
//            String result="";
//            int qualifiedTotal=0;
//            for (int i=0;i<list.size();i++){
//                Label label1 = new Label(1, row+9, getFormatString(list.get(i).getPipe_no()), wcf);
//                datalist.add(label1);
//                String stencilRes=list.get(i).getStencil_verification();
//                String stencilStr=" ";
//                if(stencilRes!=null&&!stencilStr.equals("")){
//                    if(stencilRes.equals("0")){
//                        stencilStr="未检测";
//                    }else if(stencilRes.equals("1")){
//                        stencilStr="合格";
//                    }else if(stencilRes.equals("2")){
//                        stencilStr="不合格";
//                    }
//                }
//                String cutbackStr=list.get(i).getCutback_length();
//                String cutback1=" ",cutback2=" ";
//                if(cutbackStr!=null&&!cutbackStr.equals("")){
//                    String[]arr=cutbackStr.split(",");
//                    if(arr.length>0){
//                        cutback1=arr[0];
//                        if(arr.length>1)
//                           cutback2=arr[1];
//                    }
//                }
//                Label label2 = new Label(2, row+9, String.valueOf(list.get(i).getStencil_verification()), wcf);
//                datalist.add(label2);
//                Label label3 = new Label(3, row+9, String.valueOf(list.get(i).getHoliday_tester_volts()), wcf);
//                datalist.add(label3);
//                Label label4 = new Label(4, row+9, String.valueOf(list.get(i).getRepairs()), wcf);
//                datalist.add(label4);
//                String bevel=list.get(i).getBevel();
//                String label5txt="未检测";
//                if(bevel!=null){
//                    if(bevel.equals("1")){
//                        label5txt="合格";
//                    }else if(bevel.equals("2")){
//                        label5txt="不合格";
//                    }
//                }
//                Label label5 = new Label(5, row+9, label5txt, wcf);
//                datalist.add(label5);
//                String Adhesion=list.get(i).getAdhesion_test();
//                String label6txt="未检测";
//                if(Adhesion!=null){
//                    if(Adhesion.equals("1")){
//                        label6txt="合格";
//                    }else if(Adhesion.equals("2")){
//                        label6txt="不合格";
//                    }
//                }
//                Label label6 = new Label(6, row+9, label6txt, wcf);
//                datalist.add(label6);
//                String isSample=list.get(i).getIs_sample();
//                String label7Txt=" ";
//                if(isSample!=null){
//                    if(isSample.equals("0")){
//                        label7Txt="否";
//                    }else{
//                        label7Txt="是";
//                    }
//
//                }
//                Label label7 = new Label(7, row+9,label7Txt, wcf);
//                datalist.add(label7);
//                Label label8 = new Label(8, row+9, getFormatString(list.get(i).getSurface_condition()), wcf);
//                datalist.add(label8);
//
//                Label label9 = new Label(2, row+10, String.valueOf(list.get(i).getBase_coat_thickness_list()), wcf);
//                datalist.add(label9);
//                Label label10 = new Label(4, row+10, String.valueOf(list.get(i).getMiddle_coat_thickness_list()), wcf);
//                datalist.add(label10);
//                Label label11 = new Label(6, row+10, String.valueOf(list.get(i).getTop_coat_thickness_list()), wcf);
//                datalist.add(label11);
//                Label label12 = new Label(8, row+10,String.valueOf(list.get(i).getTotal_coating_thickness_list()), wcf);
//                datalist.add(label12);
//                result=list.get(i).getResult();
//                if(result!=null){
//                    if(result.equals("0")){
//                        result="不合格";
//                    }else if(result.equals("1")){
//                        result="合格";
//                        qualifiedTotal++;
//                    }else if(result.equals("2")){
//                        result="不合格";
//                    }
//                    else if(result.equals("3")){
//                        result="待定";
//                    }else{
//                        result=" ";
//                    }
//                }else{
//                    result=" ";
//                }
//                Label label13= new Label(12, row+10, result, wcf);
//                datalist.add(label13);
//                if(list.get(i).getRemark()!=null&&!list.get(i).getRemark().equals("")){
//                    sb.append("#"+list.get(i).getPipe_no()+":"+list.get(i).getRemark()+" ");
//                }
//                //最后一行数据为空问题
//                index++;
//                row+=2;
//                if(index%5==0){
//                    datalist.add(new Label(2,19,getFormatString(sb.toString()),wcf));
//                    //到结束
//                    datalist.add(new Label(12,19,String.valueOf(qualifiedTotal),wcf));
//                    GenerateExcelToPDFUtil.PDFAutoMation(templateFullName,datalist,pdfFullName,logoImageFullName,fontPath,basePath);
//                    datalist.clear();
//                    index=1;
//                    row=0;
//                    sb.setLength(0);
//                }
//            }
//            if(datalist.size()>0){
//                datalist.add(new Label(2,19,getFormatString(sb.toString()),wcf));
//                datalist.add(new Label(12,19,String.valueOf(qualifiedTotal),wcf));
//                GenerateExcelToPDFUtil.PDFAutoMation(templateFullName,datalist,pdfFullName,logoImageFullName,fontPath,basePath);
//                datalist.clear();
//                index=1;
//                row=0; sb.setLength(0);
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return  null;
//    }

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

    public  String getFormatString(String param){
        if(param!=null&&!param.equals("")){
            return  param;
        }else{
            return  " ";
        }
    }

    //获取两个日期之间的所有字符串日期
    private  List<String> getBetweenDates(Date start, Date end) {
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        List<String> result = new ArrayList<String>();
        result.add(format.format(start));
        Calendar tempStart = Calendar.getInstance();
        tempStart.setTime(start);
        tempStart.add(Calendar.DAY_OF_YEAR, 1);
        Calendar tempEnd = Calendar.getInstance();
        tempEnd.setTime(end);
        while (tempStart.before(tempEnd)) {
            result.add(format.format(tempStart.getTime()));
            tempStart.add(Calendar.DAY_OF_YEAR, 1);
        }
        return result;
    }
    //获取传过来日期的下一天的日期字符串
    private  String getNextDay(String day){
        String returnday=null;
        try{
            Date dayTime=sdf.parse(day);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dayTime);
            calendar.add(Calendar.DAY_OF_MONTH,1);
            returnday=sdf.format(calendar.getTime());
        }catch (Exception e){
            e.printStackTrace();
        }
        return returnday;
    }
}
