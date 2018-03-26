package com.htcsweb.controller;


import com.htcsweb.util.GenerateExcelToPDFUtil;
import jxl.format.Alignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/InspectionRecordPDFOperation")
public class InspectionRecordPDFController {


    //导出外观检验记录PDF
    //InspectionRecordPDFOperation/exportBlastRecordToPDF
    @RequestMapping(value = "/exportBlastRecordToPDF",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String exportBlastRecordToPDF(HttpServletRequest request)  {
        String pdfDirPath=request.getSession().getServletContext().getRealPath("/") + "upload/pdf/";
        File pdfDirFile=new File(pdfDirPath);
        if(!pdfDirFile.exists()){
            pdfDirFile.mkdir();
        }
        String templateFullName=request.getSession().getServletContext().getRealPath("/")
                +"template/pipe_coating_surface_inspection_record_template.xls";
        String pdfFullName=request.getSession().getServletContext().getRealPath("/") + "upload/pdf/BlastRecord.pdf";
        String logoImageFullName=request.getSession().getServletContext().getRealPath("/") + "template/img/image002.jpg";

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
        String fontPath=request.getSession().getServletContext().getRealPath("/")+"font/simhei.ttf";
        // 设置内容居中
        try {
            wcf.setAlignment(Alignment.CENTRE);
            // 设置单元格的背景颜色
            wcf.setBackground(jxl.format.Colour.RED);

            /*
             * 在创建单元格的时候使用样式
             */
            UserTemp temp=new UserTemp();
            temp.arg1="123"; temp.arg2="123"; temp.arg3="123"; temp.arg4="123"; temp.arg5="123"; temp.arg6="123";
            temp.arg7="123"; temp.arg8="123"; temp.arg9="123"; temp.arg10="123"; temp.arg11="123"; temp.arg12="123";
            List<UserTemp>list=new ArrayList<>();
            for (int i=0;i<20;i++){
                list.add(temp);
            }
            int index=1,row=0;
            for (int i=0;i<list.size();i++){
                System.out.println(list.size()+":"+i+"ci循环");
                Label label1 = new Label(1, row+8, list.get(i).arg1, wcf);
                datalist.add(label1);
                Label label2 = new Label(2, row+8, list.get(i).arg2, wcf);
                datalist.add(label2);
                Label label3 = new Label(3, row+8, list.get(i).arg3, wcf);
                datalist.add(label3);
                Label label4 = new Label(4, row+8, list.get(i).arg4, wcf);
                datalist.add(label4);
                Label label5 = new Label(5, row+8, list.get(i).arg5, wcf);
                datalist.add(label5);
                Label label6 = new Label(6, row+8, list.get(i).arg6, wcf);
                datalist.add(label6);
                Label label7 = new Label(7, row+8, list.get(i).arg7, wcf);
                datalist.add(label7);
                Label label8 = new Label(8, row+8, list.get(i).arg8, wcf);
                datalist.add(label8);
                Label label9 = new Label(9, row+8, list.get(i).arg9, wcf);
                datalist.add(label9);
                Label label10 = new Label(10, row+8, list.get(i).arg10, wcf);
                datalist.add(label10);
                Label label11 = new Label(11, row+8, list.get(i).arg11, wcf);
                datalist.add(label11);
                Label label12 = new Label(12, row+8, list.get(i).arg12, wcf);
                datalist.add(label12);
                index++;
                row++;
                if(index%13==0){
                    //到结束
                    GenerateExcelToPDFUtil.PDFAutoMation(templateFullName,datalist,pdfFullName,logoImageFullName,fontPath);
                    datalist.clear();
                    System.out.println("转换执行一次");
                    index=1;
                    row=0;
                }
            }
            if(datalist.size()>0){
                GenerateExcelToPDFUtil.PDFAutoMation(templateFullName,datalist,pdfFullName,logoImageFullName,fontPath);
                datalist.clear();
                index=1;
                row=0;
                System.out.println("转换执行一次");
            }
            System.out.println("最终判断"+datalist.size());

        }catch (Exception e){
            System.out.println("Exception ="+e.getMessage());
        }


        return "";
    }
    public  class  UserTemp{
        private String arg1;
        private String arg2;
        private String arg3;
        private String arg4;
        private String arg5;
        private String arg6;
        private String arg7;
        private String arg8;
        private String arg9;
        private String arg10;
        private String arg11;
        private String arg12;

        public String getArg1() {
            return arg1;
        }

        public void setArg1(String arg1) {
            this.arg1 = arg1;
        }

        public String getArg2() {
            return arg2;
        }

        public void setArg2(String arg2) {
            this.arg2 = arg2;
        }

        public String getArg3() {
            return arg3;
        }

        public void setArg3(String arg3) {
            this.arg3 = arg3;
        }

        public String getArg4() {
            return arg4;
        }

        public void setArg4(String arg4) {
            this.arg4 = arg4;
        }

        public String getArg5() {
            return arg5;
        }

        public void setArg5(String arg5) {
            this.arg5 = arg5;
        }

        public String getArg6() {
            return arg6;
        }

        public void setArg6(String arg6) {
            this.arg6 = arg6;
        }

        public String getArg7() {
            return arg7;
        }

        public void setArg7(String arg7) {
            this.arg7 = arg7;
        }

        public String getArg8() {
            return arg8;
        }

        public void setArg8(String arg8) {
            this.arg8 = arg8;
        }

        public String getArg9() {
            return arg9;
        }

        public void setArg9(String arg9) {
            this.arg9 = arg9;
        }

        public String getArg10() {
            return arg10;
        }

        public void setArg10(String arg10) {
            this.arg10 = arg10;
        }

        public String getArg11() {
            return arg11;
        }

        public void setArg11(String arg11) {
            this.arg11 = arg11;
        }

        public String getArg12() {
            return arg12;
        }

        public void setArg12(String arg12) {
            this.arg12 = arg12;
        }
    }
}
