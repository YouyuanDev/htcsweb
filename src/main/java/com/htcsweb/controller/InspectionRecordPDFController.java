package com.htcsweb.controller;


import com.htcsweb.util.GenerateExcelToPDFUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/InspectionRecordPDFOperation")
public class InspectionRecordPDFController {


    //导出外观检验记录PDF
    //InspectionRecordPDFOperation/exportBlastRecordToPDF
    @RequestMapping(value = "/exportBlastRecordToPDF",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String exportBlastRecordToPDF(HttpServletRequest request) {

        String basePath = request.getSession().getServletContext().getRealPath("/");
        String templatePath=request.getSession().getServletContext().getRealPath("/")+"template";
        String tmppdfPath = request.getSession().getServletContext().getRealPath("/") + "upload/pdf";

        String templateFullName=templatePath+"/"+"pipe_coating_surface_inspection_record_template.xls";
        String pdfFullName=request.getSession().getServletContext().getRealPath("/") + "upload/pdf/BlastRecord.pdf";
        //这里设置datalist参数


        String newexcelfilename=GenerateExcelToPDFUtil.FillExcelTemplate(templateFullName,null);
        GenerateExcelToPDFUtil.ExcelToPDFRecord(newexcelfilename,pdfFullName);
        return "";
    }

}
