package com.htcsweb.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipOutputStream;

public class ResponseUtil {
    public static void write(HttpServletResponse response,Object object)throws Exception{
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out=response.getWriter();
        out.println(object);
        out.flush();
        out.close();
    }

    public static void writeQRCodeZipFile(String hlparam,HttpServletRequest request, HttpServletResponse response)throws Exception{

        if(hlparam==null||hlparam.equals(""))
            return;

        String[]pipeNoArr=hlparam.split(",");

        ResponseUtil.writeQRCodeZipFile(pipeNoArr,request,response);

    }


    public static void writeQRCodeZipFile(String[] pipeNoArr, HttpServletRequest request, HttpServletResponse response)throws Exception{

        String tmpfolderPath=request.getSession().getServletContext().getRealPath("/tmp");
        String logoDirectory = request.getSession().getServletContext().getRealPath("/images");
        StringBuilder sb = new StringBuilder();
        sb.append(logoDirectory);
        sb.append("/");
        sb.append("logo1.jpg");
        //System.out.println("logoDirectory="+sb.toString());
        String logofullname=sb.toString();
        //下载文件列表初始化
        ArrayList<FileBean> fileList = new ArrayList<FileBean>();
        for(int i=0;i<pipeNoArr.length;i++){
            StringBuilder sb2 = new StringBuilder();
            sb2.append("qrcode_");
            sb2.append(pipeNoArr[i]);
            String pictureName=sb2.toString();
            StringBuilder sbbot = new StringBuilder();
            sbbot.append("P/N:");
            sbbot.append(pipeNoArr[i]);
            String bottomDes=sbbot.toString();
            String QRContent=pipeNoArr[i];
            QRCodeUtil.GenerateQRCode(QRContent,logofullname,pictureName,bottomDes,tmpfolderPath);
            //保存该文件
            FileBean file = new FileBean();
            file.setFileName(pictureName+".jpg");
            //System.out.println("tmpfolderPath="+tmpfolderPath);
            file.setFilePath(tmpfolderPath);
            fileList.add(file);
        }
        //以上代码可以正常生成所有单个QR码图片

        //以下代码下载zip文件

        //设置压缩包的名字
        //解决不同浏览器压缩包名字含有中文时乱码的问题
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");//可以方便地修改日期格式
        String timenow = dateFormat.format( now );
        StringBuilder sbzip = new StringBuilder();
        sbzip.append("downloadQR_");
        sbzip.append(String.valueOf(fileList.size()));
        sbzip.append("items_");
        sbzip.append(timenow);
        sbzip.append(".zip");
        String zipName = sbzip.toString();
        response.setContentType("APPLICATION/OCTET-STREAM");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(zipName, "UTF-8"));
        //设置压缩流：直接写入response，实现边压缩边下载

        //循环将文件写入压缩流
        ZipOutputStream out = new ZipOutputStream(response.getOutputStream());
        try {
            for(Iterator<FileBean> it = fileList.iterator(); it.hasNext();){
                FileBean file = it.next();
                //System.out.println("zip="+file.getFilePath()+"/"+file.getFileName());
                ZipUtils.doCompress(file.getFilePath()+"/"+file.getFileName(), out);
                response.flushBuffer();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());

        }finally{
            out.close();
        }

    }
    //新增将pdf打包下载
//    public  static  void downLoadPdf(List<String>pdfListPath,HttpServletRequest request, HttpServletResponse response){
//        List<FileBean> fileList=new ArrayList<>();
//
//        for (int i=0;i<pdfListPath.size();i++){
//            FileBean bean=new FileBean();
//            bean.setFileId(i);
//            //.setFileName(pdfListPath.get(i).substring(pdfListPath.get(i).lastIndexOf("/")+1));
//            bean.setFilePath(pdfListPath.get(i));
//            fileList.add(bean);
//        }
//        try{
//            Date now = new Date();
//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");//可以方便地修改日期格式
//            String timenow = dateFormat.format( now );
//            StringBuilder sbzip = new StringBuilder();
//            sbzip.append("downloadPDF_");
//            sbzip.append(String.valueOf(pdfListPath.size()));
//            sbzip.append("items_");
//            sbzip.append(timenow);
//            sbzip.append(".zip");
//            String zipName = sbzip.toString();
//            response.setContentType("APPLICATION/OCTET-STREAM");
//            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(zipName, "UTF-8"));
//            //设置压缩流：直接写入response，实现边压缩边下载
//
//            //循环将文件写入压缩流
//            ZipOutputStream out = new ZipOutputStream(response.getOutputStream());
//            try {
//                for(Iterator<FileBean> it = fileList.iterator(); it.hasNext();){
//                    FileBean file = it.next();
//                    //System.out.println("zip="+file.getFilePath()+"/"+file.getFileName());
//                    ZipUtils.doCompress(file.getFilePath(), out);
//                    response.flushBuffer();
//                }
//            } catch (Exception e) {
//                  e.printStackTrace();
//                  System.out.println(e.getMessage());
//
//            }finally{
//                out.close();
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//            System.out.println(e.getMessage());
//        }
//    }
    //
    public  static  String downLoadPdf(List<String>pdfListPath,HttpServletRequest request, HttpServletResponse response){
        List<FileBean> fileList=new ArrayList<>();

        for (int i=0;i<pdfListPath.size();i++){
            FileBean bean=new FileBean();
            bean.setFileId(i);
            //.setFileName(pdfListPath.get(i).substring(pdfListPath.get(i).lastIndexOf("/")+1));
            bean.setFilePath(pdfListPath.get(i));
            fileList.add(bean);
        }
       String zipName="";
        try{
            Date now = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");//可以方便地修改日期格式
            String timenow = dateFormat.format( now );
            StringBuilder sbzip = new StringBuilder();
            sbzip.append("downloadPDF_");
            sbzip.append(String.valueOf(pdfListPath.size()));
            sbzip.append("items_");
            sbzip.append(timenow);
            sbzip.append(".zip");
            zipName = sbzip.toString();
            String basePath=request.getSession().getServletContext().getRealPath("/")+"upload/pdf/";
            File filezip=new File(basePath+zipName);
            //response.setContentType("APPLICATION/OCTET-STREAM");
            //response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(zipName, "UTF-8"));
            //设置压缩流：直接写入response，实现边压缩边下载

            //循环将文件写入压缩流
            FileOutputStream fos=new FileOutputStream(filezip);
            ZipOutputStream out = new ZipOutputStream(fos);
            try {
                for(Iterator<FileBean> it = fileList.iterator(); it.hasNext();){
                    FileBean file = it.next();
                    //System.out.println("zip="+file.getFilePath()+"/"+file.getFileName());
                    ZipUtils.doCompress(file.getFilePath(), out);
                    response.flushBuffer();
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e.getMessage());

            }finally{
                out.close();
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return zipName;
    }

}
