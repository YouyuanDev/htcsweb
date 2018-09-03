package com.htcsweb.controller;


import com.alibaba.fastjson.JSONArray;
import com.itextpdf.text.pdf.BaseFont;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import jxl.demo.Write;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.rmi.CORBA.Util;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.*;

@Controller
@RequestMapping("/CommonOperation")
public class CommonController {

    /**
     * html转Pdf
     * @param request
     * @return
     */
    @RequestMapping(value = "/htmlToPDF",method = RequestMethod.POST,produces = "pplication/json;charset=UTF-8")
    @ResponseBody
    public String htmlToPDF(HttpServletRequest request){

         String basePath=request.getSession().getServletContext().getRealPath("/");
        if(basePath.lastIndexOf('/')==-1){
            basePath=basePath.replace('\\','/');
        }
         String tmppdfPath = request.getSession().getServletContext().getRealPath("/")+"upload/pdf";
        if(tmppdfPath.lastIndexOf('/')==-1){
            tmppdfPath=tmppdfPath.replace('\\','/');
        }
          //String LOGO_PATH = "file://"+PathUtil.getCurrentPath()+"/logo.png
         File pdfDir=new File(tmppdfPath);
         if(!pdfDir.exists()){
             pdfDir.mkdir();
         }
         try{
             UUID uuid = UUID.randomUUID();
             String outputFile = tmppdfPath + "/" + uuid + ".pdf"; //生成的PDF名
             File pdfFile=new File(outputFile);
             if(!pdfFile.exists()){
                 pdfFile.createNewFile();
             }
             Map<String,Object> data = new HashMap();
             data.put("name","中国");
             Writer out = new StringWriter();
             String content=null;
             try {
                 // 获取模板,并设置编码方式
                 Configuration configuration = new Configuration();
                 configuration.setDirectoryForTemplateLoading(new File(basePath+"template"));//方式一：使用绝对路径设置模版路径
                 Template template = configuration.getTemplate("template.html");
                 template.setEncoding("UTF-8");
                 // 合并数据模型与模板
                 template.process(data, out); //将合并后的数据和模板写入到流中，这里使用的字符流
                 out.flush();
                 content= out.toString();
             } catch (Exception e) {
                 e.printStackTrace();
             } finally {
                 try {
                     out.close();
                 } catch (IOException ex) {
                     ex.printStackTrace();
                 }
             }
             ITextRenderer render = new ITextRenderer();
             try{
                 ITextFontResolver fontResolver = render.getFontResolver();
                 String FONT=basePath+"font/simhei.ttf";
                 System.out.println(FONT);
                 fontResolver.addFont(FONT, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
             }catch (Exception e){

             }finally {
                 // 解析html生成pdf
                 render.setDocumentFromString(content);
//             //解决图片相对路径的问题
//             render.getSharedContext().setBaseURL(LOGO_PATH);
                 render.layout();
                 render.createPDF(new FileOutputStream(outputFile));
             }
         }catch (Exception e){}
         return null;
    }

}
