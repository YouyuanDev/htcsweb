package com.htcsweb.util;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.application.Application;
import jxl.Cell;
import jxl.Range;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.*;
import jxl.write.biff.RowsExceededException;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.channels.FileChannel;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;


public class GenerateExcelToPDFUtil {

    private static String  newExcelFileName=null;

    public static void main(String[] args) {
            String excelTemplateFullName = "/Users/kurt/Documents/od_blast_record_template.xls";
            //String newexcelfile=GenerateExcelToPDFUtil.FillExcelTemplate(excelTemplateFullName,null);
            //GenerateExcelToPDFUtil.ExcelToPDFRecord(newexcelfile,"/Users/kurt/Documents/testPDF.pdf","/Users/kurt/Documents/image002.jpg");
        GenerateExcelToPDFUtil.PDFAutoMation(excelTemplateFullName,null,"/Users/kurt/Documents/testPDF.pdf","/Users/kurt/Documents/image002.jpg","/Users/kurt/Documents/simhei.ttf","");
    }

    //PDF生成方法入口
    public static void PDFAutoMation(String excelTemplateFullName,ArrayList<Label> dataList,String pdfFullName,String imagePath,String fontPath,String basePath) {

        String newexcelfile=GenerateExcelToPDFUtil.FillExcelTemplate(excelTemplateFullName,dataList);
        GenerateExcelToPDFUtil.ExcelToPDFRecord(newexcelfile,pdfFullName,imagePath,fontPath,basePath);

    }


    //合并行的静态函数
    private static PdfPCell mergeRow(String str,Font font,int i) {

        //创建单元格对象，将内容及字体传入
        PdfPCell cell=new PdfPCell(new Paragraph(str,font));
        //设置单元格内容居中
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        //将该单元格所在列包括该单元格在内的i行单元格合并为一个单元格
        cell.setRowspan(i);

        return cell;
    }

    //合并列的静态函数
    private static PdfPCell mergeCol(String str,Font font,int i) {

        PdfPCell cell=new PdfPCell(new Paragraph(str,font));
        cell.setMinimumHeight(25);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        //将该单元格所在行包括该单元格在内的i列单元格合并为一个单元格
        cell.setColspan(i);

        return cell;
    }

    //获取指定内容与字体的单元格
    private static PdfPCell getPDFCell(String string, Font font,int textHorizontalAlign)
    {
        //创建单元格对象，将内容与字体放入段落中作为单元格内容
        PdfPCell cell=new PdfPCell(new Paragraph(string,font));

        cell.setHorizontalAlignment(textHorizontalAlign);
        //cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        //设置最小单元格高度
        cell.setMinimumHeight(25);
        return cell;
    }



    /* 使用文件通道的方式复制文件
     */
    public static void fileChannelCopy(String srcDirName,String destDirName){
        FileInputStream fi = null;
        FileOutputStream fo = null;
        FileChannel in = null;
        FileChannel out = null;

        try {
            fi = new FileInputStream(new File(srcDirName));
            fo = new FileOutputStream( new File(destDirName));
            in = fi.getChannel();//得到对应的文件通道
            out = fo.getChannel();//得到对应的文件通道
            /*
             *       public abstract long transferTo(long position, long count,
                                         WritableByteChannel target)throws IOException;
             *          position - 文件中的位置，从此位置开始传输；必须为非负数
             *          count - 要传输的最大字节数；必须为非负数
             *          target - 目标通道
             *          返回：
                        实际已传输的字节数，可能为零
             */
            in.transferTo(0, in.size(), out);//连接两个通道，并且从in通道读取，然后写入out通道中
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }finally{
            try {
                fi.close();
                in.close();
                fo.close();
                out.close();
            } catch (IOException e) {

                e.printStackTrace();
            }
        }

    }





    //根据模版名字，将数据填入相应excel模版中
    private static String FillExcelTemplate(String excelTemplateFullName,ArrayList<Label> dataList) {
        //tempXlsFileName=String.valueOf(System.currentTimeMillis());
        //excelTemplateFullName = "/Users/kurt/Documents/pipe_coating_surface_inspection_record_template.xls";
        //复制模版
        //String newExcelFileName=excelTemplateFullName.substring(0,excelTemplateFullName.lastIndexOf('.'))+ String.valueOf(System.currentTimeMillis()+".xls");
        newExcelFileName=excelTemplateFullName.substring(0,excelTemplateFullName.lastIndexOf('.'))+ String.valueOf(System.currentTimeMillis())+".xls";
        fileChannelCopy(excelTemplateFullName,newExcelFileName);
        try {
            Workbook wb = Workbook.getWorkbook(new File(newExcelFileName));
            WritableWorkbook wwb = Workbook.createWorkbook(new File(newExcelFileName), wb);
            WritableSheet wsheet  = wwb.getSheet(0);
            try {

                //创建sheet对象

                //把datalist中的数据填到相应位置，由相关业务controller设置datalist数据
                for(int i=0;dataList!=null&&i<dataList.size();i++){
                       Label label_data=(Label) dataList.get(i);
                        wsheet.addCell(label_data);
                }

                wwb.write();//把表格信息写入文件
            } catch (Exception e) {
                System.out.println("Exception:"+e.getMessage());
            } finally {
                wwb.close();//关闭
                wb.close();
            }

        } catch (Exception e) {
            System.out.println("Exception:"+e.getMessage());
        } finally {
            System.out.println("表格生成！");
            return newExcelFileName;
        }

    }


    private static void setNoBorder(PdfPCell cell){
        cell.disableBorderSide(1); // 隐藏单元格周边的上边框
        cell.disableBorderSide(2);// 隐藏单元格周边的下边框
        cell.disableBorderSide(4); // 隐藏单元格周边的左边框
        cell.disableBorderSide(8);// 隐藏单元格周边的右框
    }


    private static void setOutsideBorderWidth(PdfPCell cell,int i,int j){
        if(i==3){//设置head bottom的边框宽度
            cell.setBorderWidthBottom(2);
        }
        if(i==20){//设置foot Bottom的边框宽度
            cell.setBorderWidthBottom(2);
        }
        if(j==0&&(i>=4&&i<=20)){//设置左侧外边框
            cell.setBorderWidthLeft(2);
        }
        if(i>=4&&i<=20){//设置右侧外边框
            if(j==12||(cell.getColspan()+j-1)==12)
                cell.setBorderWidthRight(2);
        }
    }

    //根据excel名称，转成PDF
    private static boolean ExcelToPDFRecord(String excelFullName,String pdfFullName,String imagePath,String fontPath,String basePath){
        File pdf=new File(pdfFullName);
        try{
            if(!pdf.exists()){
                pdf.createNewFile();
            }
        }catch (Exception e){

        }
        float excelTableTotalWidth=800;
        //excelFullName="/Users/kurt/Documents/pipe_coating_surface_inspection_record_template.xls";
        File excelfile = new File(excelFullName);
        StringBuffer sb = new StringBuffer();

        //imagePath = "/Users/kurt/Documents/image002.jpg";

        pdfFullName=pdfFullName.substring(0,pdfFullName.lastIndexOf('.'))+ String.valueOf(System.currentTimeMillis()+".pdf");

        //写入pdf
        //pdfFullName="/Users/kurt/Documents/testPDF.pdf";
        File pdffile = new File(pdfFullName);
        try {
            //BufferedImage logoimg = ImageIO.read(new File(imagePath));
            Image image = Image.getInstance(imagePath);
            //image.setWidthPercentage(0.6f);
            Document document = new Document(new RectangleReadOnly(PageSize.A4.getHeight(),PageSize.A4.getWidth()),0,0,0,0);
           // System.out.println("PageSize.A4.getWidth()="+PageSize.A4.getWidth());//595
            //System.out.println("PageSize.A4.getHeight()="+PageSize.A4.getHeight());//842

            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfFullName));
            PdfPTable table=null;
        //字体设置
        /*
         * 由于itext不支持中文，所以需要进行字体的设置，我这里让itext调用windows系统的中文字体，
         * 找到文件后，打开属性，将文件名及所在路径作为字体名即可。
         */
        //创建BaseFont对象，指明字体，编码方式,是否嵌入
            BaseFont bf=BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, false);
        //BaseFont bf=BaseFont.createFont("/Users/kurt/Documents/simhei.ttf", BaseFont.IDENTITY_H, false);
        //创建Font对象，将基础字体对象，字体大小，字体风格
        Font font=new Font(bf,10,Font.NORMAL);
        Font headfont1=new Font(bf,14,Font.BOLD);
        Font headfont2=new Font(bf,10,Font.BOLD);
        int rowNum = 0;
        int colNum = 0;

            Workbook book = Workbook.getWorkbook(excelfile);

            try{
                Sheet sheet = book.getSheet(0);
                int columnCount=sheet.getColumns();
                table=new PdfPTable(columnCount);

                //下面是找出表格中的空行和空列
                List<Integer> nullCol = new ArrayList<>();
                List<Integer> nullRow = new ArrayList<>();
                for(int j=0;j<sheet.getColumns();j++){
                    int nullColNum = 0;
                    for(int i=0;i<sheet.getRows();i++){
                        Cell cell=sheet.getCell(j, i);
                        String str = cell.getContents();
                        if(str == null || "".equals(str)){
                            nullColNum ++ ;
                        }
                    }
                    if(nullColNum == sheet.getRows()){
                        nullCol.add(j);
                        columnCount--;
                    }
                }

                for(int i=0;i<sheet.getRows();i++){
                    int nullRowNum = 0;
                    for(int j=0;j<sheet.getColumns();j++){
                        Cell cell=sheet.getCell(j, i);
                        String str = cell.getContents();
                        if(str == null || "".equals(str)){
                            nullRowNum ++ ;
                        }
                    }
                    if(nullRowNum == sheet.getColumns()){
                        nullRow.add(i);
                    }
                }
                //table=new PdfPTable(columnCount);
                Range[] ranges = sheet.getMergedCells();

                PdfPCell cell1=new PdfPCell();
                for(int i=0;i<sheet.getRows();i++){
                    if(nullRow.contains(i)){    //如果这一行是空行，这跳过这一行
                        //continue;
                    }
                    for(int j=0;j<sheet.getColumns();j++){
                        if(nullCol.contains(j)){    //如果这一列是空列，则跳过这一列
                           // continue;
                        }

                        //设置logo
                        if(i==0&&j==0){
                            table.addCell(image);
                        }
                        if(i==3&&j==1){//image设置span
                            table.getRow(0).getCells()[0].setRowspan(3);
                            table.getRow(0).getCells()[0].setColspan(3);
                            setNoBorder(table.getRow(0).getCells()[0]);
                        }



                        boolean flag = true;
                        Cell cell=sheet.getCell(j, i);
                        String str = cell.getContents();
                        for(Range range : ranges){    //合并的单元格判断和处理
                            if(j >= range.getTopLeft().getColumn() && j <= range.getBottomRight().getColumn()
                                    && i >= range.getTopLeft().getRow() && i <= range.getBottomRight().getRow()){
                                if(str == null || "".equals(str)){
                                    flag = false;
                                    break;
                                }
                                rowNum = range.getBottomRight().getRow() - range.getTopLeft().getRow()+1;
                                colNum = range.getBottomRight().getColumn() - range.getTopLeft().getColumn()+1;
                                if(rowNum > colNum){
                                    if(i==1||i==3&&j!=10)//头部公司中文名称字体及报告名称
                                        cell1 = mergeRow(str, headfont1, rowNum);
                                    else if(i==2)//设置公司英文名称
                                        cell1 = mergeRow(str, headfont2, rowNum);
                                    else//设置表格其他单元
                                        cell1 = mergeRow(str, font, rowNum);
                                    cell1.setColspan(colNum);
                                }else {
                                    if(i==1||i==3&&j!=10)//设置公司中文名称字体及报告名称
                                        cell1 = mergeCol(str, headfont1, colNum);
                                    else if(i==2)//设置公司英文名称
                                        cell1 = mergeCol(str, headfont2, colNum);
                                    else//设置表格其他单元
                                        cell1 = mergeCol(str, font, colNum);
                                    cell1.setRowspan(rowNum);
                                }

                                if(i<=3) {//头部文字排版
                                    cell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                                }
                                if(i<=3||i>=21){//头部及尾部 取消边框
                                    setNoBorder(cell1);
                                }


                                setOutsideBorderWidth(cell1,i,j);


                                table.addCell(cell1);

                                //System.out.println(num1 + "  " + num2);
                                flag = false;
                                break;
                            }
                        }
                        if(flag){
                            int horizontalAlign=Element.ALIGN_CENTER;
                            if(i<=3) {//头部两行文字
                                horizontalAlign=Element.ALIGN_RIGHT;
                                cell1 = getPDFCell(str, headfont1, horizontalAlign);
                            }else {
                                cell1 = getPDFCell(str, font, horizontalAlign);
                            }
                            if(i<=3||i>=21){//头部及尾部 取消边框
                                setNoBorder(cell1);
                            }
                            setOutsideBorderWidth(cell1,i,j);
                            table.addCell(cell1);
                        }
                    }
                }
                //设置表格总宽度
                table.setTotalWidth(excelTableTotalWidth);
                table.setLockedWidth(true);

                //System.out.println(sb);
            }finally{
                if(book != null){
                    book.close();
                    document.open();
                    document.add(table);
                    document.close();
                    writer.close();
                    System.out.println("PDF生成！");
                    //删除临时生成的.xls文件
                    if(newExcelFileName!=null){
                        File file=new File(newExcelFileName);
                        if(file.exists()){
                            file.delete();
                        }
                    }

                    return true;
                }
            }

        } catch (BiffException e) {
            System.err.println(e+"");

        } catch (IOException e) {
            System.err.println(e+"文件读取错误");
        }catch (Exception e) {
            System.err.println(e+"错误");
        }

        return true;
    }
}
