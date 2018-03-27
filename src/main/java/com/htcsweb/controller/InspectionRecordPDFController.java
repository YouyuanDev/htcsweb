package com.htcsweb.controller;


import com.htcsweb.dao.*;
import com.htcsweb.entity.*;
import com.htcsweb.util.GenerateExcelToPDFUtil;
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

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/InspectionRecordPDFOperation")
public class InspectionRecordPDFController {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
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

    //1.---------------获取外打砂记录PDF
    @RequestMapping("getOdBlastRecord")
    public  String getOdBlastRecord(HttpServletRequest request){
       // String begin_time=request.getParameter("begin_time");
        //String end_time=request.getParameter("end_time");
        String begin_time="2018-01-03";
        String end_time="2018-03-30";
        String templateFullName=request.getSession().getServletContext().getRealPath("/")
                +"template/od_blast_record_template.xls";
        try{
            if(begin_time!=null&&begin_time!=""){
                beginTime=sdf.parse(begin_time);
            }
            if(end_time!=null&&end_time!=""){
                endTime=sdf.parse(end_time);
            }
            List<OdBlastProcess>list=odblastprocessDao.getOdBlastRecord(beginTime,endTime);
            ArrayList<Label> datalist=new ArrayList<Label>();
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
                Label label7 = new Label(7, row+8, String.valueOf(list.get(i).getConductivity()), wcf);
                datalist.add(label7);
                Label label8 = new Label(8, row+8, String.valueOf(list.get(i).getAlkaline_dwell_time()), wcf);
                datalist.add(label8);
                Label label9 = new Label(9, row+8, String.valueOf(list.get(i).getAcid_concentration()), wcf);
                datalist.add(label9);
                Label label10 = new Label(10, row+8, String.valueOf(list.get(i).getAcid_wash_time()), wcf);
                datalist.add(label10);
                Label label11 = new Label(11, row+8, String.valueOf(list.get(i).getAcid_concentration()), wcf);
                datalist.add(label11);
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
                Label label12 = new Label(12, row+8, result, wcf);
                datalist.add(label12);
                if(list.get(i).getRemark()!=null&&!list.get(i).getRemark().equals("")) {
                    sb.append("#" + list.get(i).getPipe_no() + ":" + list.get(i).getRemark() + " ");
                }
                //最后一行数据为空问题
                index++;
                row++;
                if(index%13==0){
                    AddLastWhiteSpace(datalist,sb.toString(),wcf);
                    //添加合格数
                    datalist.add(new Label(12,20,String.valueOf(qualifiedTotal),wcf));
                    //到结束
                    GenerateExcelToPDFUtil.PDFAutoMation(templateFullName,datalist,pdfFullName,logoImageFullName,fontPath,basePath);
                    datalist.clear();
                    index=1;
                    row=0;
                }
            }
            if(datalist.size()>0){
                AddLastWhiteSpace(datalist,sb.toString(),wcf);
                //添加合格数
                datalist.add(new Label(12,20,String.valueOf(qualifiedTotal),wcf));
                GenerateExcelToPDFUtil.PDFAutoMation(templateFullName,datalist,pdfFullName,logoImageFullName,fontPath,basePath);
                datalist.clear();
                index=1;
                row=0;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return  null;
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
                Label label12 = new Label(12, row+8, result, wcf);
                datalist.add(label12);
                if(list.get(i).getRemark()!=null&&!list.get(i).getRemark().equals("")){
                    sb.append("#"+list.get(i).getPipe_no()+":"+list.get(i).getRemark()+" ");
                }
                //最后一行数据为空问题
                index++;
                row++;
                if(index%13==0){
                    AddLastWhiteSpace(datalist,sb.toString(),wcf);
                    //添加合格数
                    datalist.add(new Label(12,20,String.valueOf(qualifiedTotal),wcf));
                    //到结束
                    GenerateExcelToPDFUtil.PDFAutoMation(templateFullName,datalist,pdfFullName,logoImageFullName,fontPath,basePath);
                    datalist.clear();
                    index=1;
                    row=0;
                }
            }
            if(datalist.size()>0){
                AddLastWhiteSpace(datalist,sb.toString(),wcf);
                //添加合格数
                datalist.add(new Label(12,20,String.valueOf(qualifiedTotal),wcf));
                GenerateExcelToPDFUtil.PDFAutoMation(templateFullName,datalist,pdfFullName,logoImageFullName,fontPath,basePath);
                datalist.clear();
                index=1;
                row=0;
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
                Label label1 = new Label(1, row+8, getFormatString(list.get(i).getPipe_no()), wcf);
                datalist.add(label1);
                Label label2 = new Label(2, row+8, String.valueOf(list.get(i).getCoating_line_speed()), wcf);
                datalist.add(label2);
                Label label3 = new Label(3, row+8, getFormatString(list.get(i).getBase_coat_used()), wcf);
                datalist.add(label3);
                Label label4 = new Label(4, row+8, getFormatString(list.get(i).getBase_coat_lot_no()), wcf);
                datalist.add(label4);
                Label label5 = new Label(5, row+8, getFormatString(list.get(i).getTop_coat_used()), wcf);
                datalist.add(label5);
                Label label6 = new Label(6, row+8,getFormatString(list.get(i).getTop_coat_lot_no()), wcf);
                datalist.add(label6);
                Label label7 = new Label(7, row+8,list.get(i).getBase_coat_gun_count()+"", wcf);
                datalist.add(label7);
                Label label8 = new Label(8, row+8, list.get(i).getTop_coat_gun_count()+"", wcf);
                datalist.add(label8);
                Label label9 = new Label(9, row+8, list.get(i).getApplication_temp()+"", wcf);
                datalist.add(label9);
                Label label10 = new Label(10, row+8, list.get(i).getTo_first_touch_duration()+"", wcf);
                datalist.add(label10);
                Label label11= new Label(11, row+8,list.get(i).getTo_quench_duration()+"", wcf);
                datalist.add(label11);
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
                Label label12 = new Label(12, row+8, result, wcf);
                datalist.add(label12);
                if(list.get(i).getRemark()!=null&&!list.get(i).getRemark().equals("")){
                    sb.append("#"+list.get(i).getPipe_no()+":"+list.get(i).getRemark()+" ");
                }
                //最后一行数据为空问题
                index++;
                row++;
                if(index%13==0){
                    AddLastWhiteSpace(datalist,sb.toString(),wcf);
                    //添加合格数
                    datalist.add(new Label(12,20,String.valueOf(qualifiedTotal),wcf));
                    //到结束
                    GenerateExcelToPDFUtil.PDFAutoMation(templateFullName,datalist,pdfFullName,logoImageFullName,fontPath,basePath);
                    datalist.clear();
                    index=1;
                    row=0;
                }
            }
            if(datalist.size()>0){
                AddLastWhiteSpace(datalist,sb.toString(),wcf);
                //添加合格数
                datalist.add(new Label(12,20,String.valueOf(qualifiedTotal),wcf));
                GenerateExcelToPDFUtil.PDFAutoMation(templateFullName,datalist,pdfFullName,logoImageFullName,fontPath,basePath);
                datalist.clear();
                index=1;
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
                String Adhesion=list.get(i).getAdhesion_test();
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
                    }else{
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
                index++;
                row+=2;
                if(index%5==0){
                    datalist.add(new Label(2,19,sb.toString(),wcf));
                    //添加合格数
                    datalist.add(new Label(12,19,String.valueOf(qualifiedTotal),wcf));
                    //到结束
                    GenerateExcelToPDFUtil.PDFAutoMation(templateFullName,datalist,pdfFullName,logoImageFullName,fontPath,basePath);
                    datalist.clear();
                    index=1;
                    row=0;
                }
            }
            if(datalist.size()>0){
                if(sb.toString().equals(""))
                   datalist.add(new Label(2,19," ",wcf));
                else
                    datalist.add(new Label(2,19,sb.toString(),wcf));
                datalist.add(new Label(12,19,String.valueOf(qualifiedTotal),wcf));
                GenerateExcelToPDFUtil.PDFAutoMation(templateFullName,datalist,pdfFullName,logoImageFullName,fontPath,basePath);
                datalist.clear();
                index=1;
                row=0;
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
                Label label1 = new Label(1, row+8, getFormatString(list.get(i).getPipe_no()), wcf);
                datalist.add(label1);
                Label label2 = new Label(2, row+8, String.valueOf(list.get(i).getCoating_line_speed()), wcf);
                datalist.add(label2);
                Label label3 = new Label(3, row+8, getFormatString(list.get(i).getBase_coat_used()), wcf);
                datalist.add(label3);
                Label label4 = new Label(4, row+8, getFormatString(list.get(i).getBase_coat_lot_no()), wcf);
                datalist.add(label4);
                Label label5 = new Label(5, row+8, getFormatString(list.get(i).getMiddle_coat_used()), wcf);
                datalist.add(label5);
                Label label6 = new Label(6, row+8,getFormatString(list.get(i).getMiddle_coat_lot_no()), wcf);
                datalist.add(label6);
                Label label7 = new Label(7, row+8, getFormatString(list.get(i).getTop_coat_used()), wcf);
                datalist.add(label7);
                Label label8 = new Label(8, row+8, getFormatString(list.get(i).getTop_coat_lot_no()), wcf);
                datalist.add(label8);
                Label label9 = new Label(9, row+8, String.valueOf(list.get(i).getBase_coat_gun_count()), wcf);
                datalist.add(label9);
                Label label10 = new Label(10, row+8, String.valueOf(list.get(i).getApplication_temp()), wcf);
                datalist.add(label10);
                Label label11 = new Label(11, row+8,String.valueOf(list.get(i).getTo_first_touch_duration()), wcf);
                datalist.add(label11);
                Label label12 = new Label(12, row+8, String.valueOf(list.get(i).getTo_quench_duration()), wcf);
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
                    else{
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
                    AddLastWhiteSpace(datalist,sb.toString(),wcf);
                    //添加合格数
                    datalist.add(new Label(13,20,String.valueOf(qualifiedTotal),wcf));
                    //到结束
                    GenerateExcelToPDFUtil.PDFAutoMation(templateFullName,datalist,pdfFullName,logoImageFullName,fontPath,basePath);
                    datalist.clear();
                    index=1;
                    row=0;
                }
            }
            if(datalist.size()>0){
                AddLastWhiteSpace(datalist,sb.toString(),wcf);
                //添加合格数
                datalist.add(new Label(13,20,String.valueOf(qualifiedTotal),wcf));
                GenerateExcelToPDFUtil.PDFAutoMation(templateFullName,datalist,pdfFullName,logoImageFullName,fontPath,basePath);
                datalist.clear();
                index=1;
                row=0;
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
                String Adhesion=list.get(i).getAdhesion_test();
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
//                if(list.get(i).getRemark()!=null&&!list.get(i).getRemark().equals("")){
//                    sb.append("#"+list.get(i).getPipe_no()+":"+list.get(i).getRemark()+" ");
//                }
                //最后一行数据为空问题
                index++;
                row+=2;
                if(index%5==0){
                    datalist.add(new Label(2,19,getFormatString(sb.toString()),wcf));
                    //到结束
                    datalist.add(new Label(12,19,String.valueOf(qualifiedTotal),wcf));
                    GenerateExcelToPDFUtil.PDFAutoMation(templateFullName,datalist,pdfFullName,logoImageFullName,fontPath,basePath);
                    datalist.clear();
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
                row=0;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return  null;
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

    public  String getFormatString(String param){
        if(param!=null&&!param.equals("")){
            return  param;
        }else{
            return  " ";
        }
    }
}
