package com.htcsweb.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.htcsweb.dao.PipeBasicInfoDao;
import com.htcsweb.util.FileRenameUtil;
import com.htcsweb.util.GenerateExcelToPDFUtil;
import com.htcsweb.util.MergePDF;
import com.htcsweb.util.ResponseUtil;
import com.jxcell.ChartFormat;
import com.jxcell.ChartShape;
import com.jxcell.RangeRef;
import com.jxcell.View;
import com.jxcell.designer.Designer;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.write.*;
import jxl.write.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

@Controller
@RequestMapping("/StatisticsOperation")
public class StatisticsController {


    @Autowired
    private PipeBasicInfoDao pipeBasicInfoDao;


//    @RequestMapping(value="getStatisticsExcel",produces="application/json;charset=UTF-8")
//    @ResponseBody
//    public  String getStatisticsExcel(HttpServletRequest request, HttpServletResponse response)throws Exception{
//        String basePath=request.getSession().getServletContext().getRealPath("/");
//
//        if(basePath.lastIndexOf('/')==-1){
//            basePath=basePath.replace('\\','/');
//        }
//        String webPath=basePath;//web路径
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//
//        List<String> delSetPath=new ArrayList<String>();//定义删除pdf集合，用于生成zip后删除所有的临时文件
//        if(UploadFileController.isServerTomcat) {
//            basePath = basePath.substring(0, basePath.lastIndexOf('/'));
//            basePath = basePath.substring(0, basePath.lastIndexOf('/'));
//        }
//
//        //是否成功标识
//        boolean success=false;
//        String zipName="";
//        String message="";
//        String project_no=request.getParameter("project_no");
//        String project_name=request.getParameter("project_name");
//        long startTime = System.currentTimeMillis();//获取开始时间
//        if(project_no!=null&&!project_no.equals("")){
//            Workbook wb=null;
//            WritableWorkbook wwb=null;
//            WritableSheet wsheet=null;
//            List<String>finalexcelList=new ArrayList<>();
//            String newexcelName="";
//            int startLine=1;
//            try{
//                //先清理.zip垃圾文件
//                FileRenameUtil.cleanTrashFiles(basePath);
//                HttpSession session = request.getSession();
//                session.setAttribute("statisticExcelProgress", String.valueOf(0));
//                String templateFullName=request.getSession().getServletContext().getRealPath("/")
//                        +"template/statistics.xls";
//                if(templateFullName.lastIndexOf('/')==-1){
//                    templateFullName=templateFullName.replace('\\','/');
//                }
//
//                newexcelName= GenerateExcelToPDFUtil.FillExcelTemplate(templateFullName,null);
//                File newxlsfile = new File(newexcelName);
//                wb = Workbook.getWorkbook(newxlsfile);
//                wwb = Workbook.createWorkbook(newxlsfile, wb);
//                wsheet = wwb.getSheet(0);
//                //获取外防涂层不合格信息
//                List<HashMap<String,Object>> odCoatingRejectData=getODCoatingRejectData(project_no);
//                //获取内防涂层不合格信息
//                List<HashMap<String,Object>> idCoatingRejectData=getIDCoatingRejectData(project_no);
//
//                WritableCellFormat wcf=null;
//                WritableFont wf=null;
//                try{
//                    wf = new WritableFont(WritableFont.createFont("Arial"), 9);
//                    wcf= new WritableCellFormat(wf);
//                    wcf.setAlignment(Alignment.CENTRE);
//                    wcf.setBackground(Colour.WHITE);
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//                startLine=odCoatingRejectData.size()>idCoatingRejectData.size()?odCoatingRejectData.size():idCoatingRejectData.size();
//                int totalCount=odCoatingRejectData.size()+idCoatingRejectData.size();
//                session.setAttribute("statisticExcelProgress", String.valueOf("0"));
//                float percent=0;
//                wsheet.addCell(new Label(5, 0,project_name, wcf));
//                for (int i=0;i<odCoatingRejectData.size();i++){
//                    wsheet.addCell(new Label(0, i+2, String.valueOf(odCoatingRejectData.get(i).get("reject_reason")), wcf));
//                    wsheet.addCell(new Label(1, i+2, String.valueOf(odCoatingRejectData.get(i).get("total_count")), wcf));
//                    WritableCellFormat wcfFormat= new WritableCellFormat(wsheet.getCell(0,i+2).getCellFormat());
//                    WritableCellFormat wcfFormat1= new WritableCellFormat(wsheet.getCell(1,i+2).getCellFormat());
//                    wcfFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
//                    wcfFormat1.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
//                    if(totalCount!=0)
//                        percent=(i+1)*100/totalCount;
//                    session.setAttribute("statisticExcelProgress", String.valueOf(percent));
//                    System.out.println("statisticExcelProgress：" + percent);
//                    System.out.println("i：" + i);
//                    System.out.println("totalCount：" + totalCount);
//                }
//                for (int i=0;i<idCoatingRejectData.size();i++){
//                    wsheet.addCell(new Label(2, i+2, String.valueOf(idCoatingRejectData.get(i).get("reject_reason")), wcf));
//                    wsheet.addCell(new Label(3, i+2, String.valueOf(idCoatingRejectData.get(i).get("total_count")), wcf));
//                    WritableCellFormat wcfFormat= new WritableCellFormat(wsheet.getCell(2,i+2).getCellFormat());
//                    WritableCellFormat wcfFormat1= new WritableCellFormat(wsheet.getCell(3,i+2).getCellFormat());
//                    wcfFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
//                    wcfFormat1.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
//                    if(totalCount!=0)
//                        percent=(i+1)*100/totalCount;
//                    session.setAttribute("statisticExcelProgress", String.valueOf(percent));
//                    System.out.println("statisticExcelProgress：" + percent);
//                    System.out.println("i：" + i);
//                    System.out.println("totalCount：" + totalCount);
//                }
//                System.out.println("startLine="+startLine);
//                System.out.println("newexcelName======"+newexcelName);
//                DrawPieChart(odCoatingRejectData,idCoatingRejectData,newexcelName,startLine);
//                session.setAttribute("statisticExcelProgress", String.valueOf("100"));
//                if(totalCount==0){
//                    success=false;
//                    message="没有不合格记录";
//                }else{
//                    success=true;
//                    message="存在不合格记录"+String.valueOf(totalCount)+"条";
//                }
//
//            }catch (Exception e){
//                e.printStackTrace();
//            }finally {
//                wwb.write();
//                wwb.close();//关闭
//                wb.close();
//                finalexcelList.add(newexcelName);
//                delSetPath.add(newexcelName);
//                zipName="/upload/pdf/"+ ResponseUtil.downLoadPdf(finalexcelList,request,response);
//                for (int j=0;j<delSetPath.size();j++){
//                    if(delSetPath.get(j)!=null){
//                        File file=new File(delSetPath.get(j));
//                        if(file.exists()){
//                            file.delete();
//                        }
//                    }
//                }
//            }
//
//        }
//        Map<String,Object> maps=new HashMap<String,Object>();
//        maps.put("success",success);
//        maps.put("zipName",zipName);
//        maps.put("message",message);
//        String mmp= JSONArray.toJSONString(maps);
//        return mmp;
//    }
    //生成带饼图的Excel(内外防统一)
@RequestMapping(value="getStatisticsExcel",produces="application/json;charset=UTF-8")
@ResponseBody
public  String getStatisticsExcel(HttpServletRequest request, HttpServletResponse response)throws Exception{
    String basePath=request.getSession().getServletContext().getRealPath("/")+"";
    if(basePath.lastIndexOf('/')==-1){
        basePath=basePath.replace('\\','/');
    }

    List<String> delSetPath=new ArrayList<String>();//定义删除pdf集合，用于生成zip后删除所有的临时文件
    if(UploadFileController.isServerTomcat) {
        basePath = basePath.substring(0, basePath.lastIndexOf('/'));
        basePath = basePath.substring(0, basePath.lastIndexOf('/'));
    }
    //是否成功标识
    boolean success=false;
    String zipName="";
    String message="";
    String project_no=request.getParameter("project_no");
    String project_name=request.getParameter("project_name");
    String excelFullName=basePath+"/upload/pdf/"+(project_no+"_MTC_"+ UUID.randomUUID().toString()+".xls");
    File file0=new File(excelFullName);
    if(!file0.exists()){
        file0.createNewFile();
    }
    View m_view = new View();
    List<String>finalexcelList=new ArrayList<>();
    if(project_no!=null&&!project_no.equals("")){
        int startLine=1;
        try{
            //先清理.zip垃圾文件
            FileRenameUtil.cleanTrashFiles(basePath);
            HttpSession session = request.getSession();
            session.setAttribute("statisticExcelProgress", String.valueOf(0));
            //获取外防涂层不合格信息
            List<HashMap<String,Object>> odCoatingRejectData=getODCoatingRejectData(project_no);
            //获取内防涂层不合格信息
            List<HashMap<String,Object>> idCoatingRejectData=getIDCoatingRejectData(project_no);
            startLine=odCoatingRejectData.size()>idCoatingRejectData.size()?odCoatingRejectData.size():idCoatingRejectData.size();
            int totalCount=odCoatingRejectData.size()+idCoatingRejectData.size()+startLine;

            RangeRef newRange = null;
            m_view.getLock();
            HashMap<String,String>hs=new HashMap<>();
            // 行 列 数值
            m_view.setTextAsValue(0, 0, "外防");
            m_view.setTextAsValue(0, 2, "内防");
            m_view.setTextAsValue(0, 4, "项目名称");
            m_view.setTextAsValue(0, 5, project_name);
            m_view.setTextAsValue(1, 0, "涂层不合格原因");
            m_view.setTextAsValue(1, 1, "不合格支数");
            m_view.setTextAsValue(1, 2, "涂层不合格原因");
            m_view.setTextAsValue(1, 3, "不合格支数");
            startLine=startLine+2;
            float percent=0;
            int processCount=0;
            for (int i=0;i<odCoatingRejectData.size();i++){
                String reject_reason= String.valueOf(odCoatingRejectData.get(i).get("reject_reason"));
                String total_count=String.valueOf(odCoatingRejectData.get(i).get("total_count"));
                m_view.setTextAsValue(i+2,0,reject_reason);
                m_view.setTextAsValue(i+2,1,  total_count);
                if(hs.containsKey(reject_reason)){
                    String result=String.valueOf(hs.get(reject_reason));
                    if(total_count!=null&&!"".equals(total_count)&&result!=null&&!"".equals(result)){
                        int count=(Integer.parseInt(result)+Integer.parseInt(total_count));
                        hs.put(reject_reason,String.valueOf(count));
                    }
                }else{
                    hs.put(reject_reason,total_count);
                }
                if(totalCount!=0)
                        percent=(processCount+1)*100/totalCount;
                    session.setAttribute("statisticExcelProgress", String.valueOf(percent));
            }
            for (int i=0;i<idCoatingRejectData.size();i++){
                String reject_reason= String.valueOf(idCoatingRejectData.get(i).get("reject_reason"));
                String total_count=String.valueOf(idCoatingRejectData.get(i).get("total_count"));
                m_view.setTextAsValue( i+2,2, reject_reason);
                m_view.setTextAsValue( i+2,3, total_count);
                if(hs.containsKey(reject_reason)){
                    String result=String.valueOf(hs.get(reject_reason));
                    if(total_count!=null&&!"".equals(total_count)&&result!=null&&!"".equals(result)){
                        int count=(Integer.parseInt(result)+Integer.parseInt(total_count));
                        hs.put(reject_reason,String.valueOf(count));
                    }
                }else{
                    hs.put(reject_reason,total_count);
                }
                if(totalCount!=0)
                    percent=(processCount+1)*100/totalCount;
                session.setAttribute("statisticExcelProgress", String.valueOf(percent));
            }
            Iterator iter = hs.entrySet().iterator();
            startLine=startLine+2;
            int index=1;
            m_view.setTextAsValue(startLine+index, 1, "名称");
            m_view.setTextAsValue(startLine+index, 2, "数值");
            while (iter.hasNext()) {
                Map.Entry entry=(Map.Entry)iter.next();
                String reject_reason=String.valueOf(entry.getKey());
                String total_count=String.valueOf(entry.getValue());
                // 分项
                m_view.setTextAsValue(startLine+index, 1, reject_reason);
                // 数据
                m_view.setNumber(startLine+index, 2, Double.parseDouble(total_count));
                index++;
                if(totalCount!=0)
                    percent=(processCount+1)*100/totalCount;
                session.setAttribute("statisticExcelProgress", String.valueOf(percent));
            }
            m_view.setTextAsValue(startLine+index, 1, "总计量");
            // 设置公式, 可参照./test/formula/FormulaSample.java
            String sumStr="SUM(C"+(startLine+1)+":C"+(startLine+index+1)+")";
            System.out.println("--------------------sumStr="+sumStr);
            m_view.setFormula(startLine+index, 2, sumStr);
            // 选中单元格区域
            //m_view.setSelection("C7:D7");
            // 编辑复制 向右复制
            ///m_view.editCopyRight();

            /** 1.1 绘制饼图, 可参照./test/chart/ChartFormatDemo.java */
            // 绘图区坐标addChart（左上列x，左上行y，右下列x，右下行y）
            ChartShape chart = m_view.addChart(0, startLine+index+9, 10, startLine+index+40);
            // 图表格式, 其他格式参照demo
            chart.setChartType(ChartShape.TypePie);

            // 添加一个系列
            chart.addSeries();
            // 饼图数据源,饼图需要的具体数字，不包含总数，参数为 开始单元格结束单元格
            String dataStr="Sheet1!$C$"+(startLine+2)+":$C$"+(startLine+index);
            System.out.println("--------------------dataStr="+dataStr);
            chart.setSeriesYValueFormula(0, dataStr);
            // 数据对应的说明.如：货车  12辆，这里是货车
            String nameStr="Sheet1!$B$"+(startLine+2)+":$B$"+(startLine+index);
            System.out.println("--------------------nameStr="+nameStr);
            chart.setCategoryFormula(nameStr);

            // 图表名称
            chart.setTitle("钢管不合格原因分布图");

            /** 1.2 设置样式, 可参照./test/chart/ChartFormatTest.java */
            // 设置列宽行高
            m_view.setColWidth(1, 18 * 256);
            // 设置图纸样式, 参照各种Format类
            ChartFormat cf = chart.getChartFormat();
//            cf.setPattern(com.jxcell.CellFormat.PatternSolid);
//            cf.setPatternFG(m_view.getPaletteEntry(31).getRGB());
            chart.setChartFormat(cf);
            // 设置绘图区颜色
            cf = chart.getPlotFormat();
//            cf.setForeColor((new Color(0, 255, 0)).getRGB());
            chart.setPlotFormat(cf);

            // 展示图饼文字描述
            cf = chart.getSeriesFormat(0);
            cf.setDataLabelPosition(ChartFormat.DataLabelPositionOutside);
//            cf.setDataLabelPosition(ChartFormat.DataLabelPositionAuto);
//            cf.setDataLabelType(ChartFormat.DataLabelValue);
            cf.setDataLabelType(ChartFormat.DataLabelCategoryAndPercent);
            chart.setSeriesFormat(0, cf);

            // 设置图饼分块颜色
            chart.setVaryColors(true);

            cf = chart.getDataLabelFormat(0, 0);
            cf.setForeColor((new Color(0, 0, 255)).getRGB());
            cf.setFontColor((new Color(255, 0, 0)).getRGB());
            chart.setDataLabelFormat(0, 0, cf);

            // 设置标题字体格式
            cf = chart.getTitleFormat();
            cf.setFontBold(true);
            cf.setFontSize(20 * 20);
            chart.setTitleFormat(cf);
            // 图表刻印位置
            chart.setLegendPosition(ChartFormat.LegendPlacementBottom);
            // 图表刻印样式, 取消图饼边框显示
            cf = chart.getLegendFormat();
            cf.setLineNone();
            cf.setFontSizeInPoints(13);
            chart.setLegendFormat(cf);

            /** 2.0 导出功能 */
            // excel写出路径
            m_view.write(excelFullName);
            System.out.println("end");
            Designer.newDesigner(m_view);
            //System.out.println("startLine="+startLine);
            System.out.println("newexcelName======");
            //DrawPieChart(odCoatingRejectData,idCoatingRejectData,excelFullName,startLine);
            session.setAttribute("statisticExcelProgress", String.valueOf(100));
            if(totalCount==0){
                success=false;
                message="没有不合格记录";
            }else{
                success=true;
                message="存在不合格记录"+String.valueOf(totalCount)+"条";
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            m_view.releaseLock();
            finalexcelList.add(excelFullName);
            delSetPath.add(excelFullName);
            zipName="/upload/pdf/"+ ResponseUtil.downLoadPdf(finalexcelList,request,response);
            for (int j=0;j<delSetPath.size();j++){
                if(delSetPath.get(j)!=null){
                    File file=new File(delSetPath.get(j));
                    if(file.exists()){
                        file.delete();
                    }
                }
            }
        }

    }
    Map<String,Object> maps=new HashMap<String,Object>();
    maps.put("success",success);
    maps.put("zipName",zipName);
    maps.put("message",message);
    String mmp= JSONArray.toJSONString(maps);
    return mmp;
}
    //生成带饼图的Excel(内外防分开)
    @RequestMapping(value="getStatisticsExcel1",produces="application/json;charset=UTF-8")
    @ResponseBody
    public  String getStatisticsExcel1(HttpServletRequest request, HttpServletResponse response)throws Exception{
        String basePath=request.getSession().getServletContext().getRealPath("/")+"";
        if(basePath.lastIndexOf('/')==-1){
            basePath=basePath.replace('\\','/');
        }

        List<String> delSetPath=new ArrayList<String>();//定义删除pdf集合，用于生成zip后删除所有的临时文件
        if(UploadFileController.isServerTomcat) {
            basePath = basePath.substring(0, basePath.lastIndexOf('/'));
            basePath = basePath.substring(0, basePath.lastIndexOf('/'));
        }
        //是否成功标识
        boolean success=false;
        String zipName="";
        String message="";
        String project_no=request.getParameter("project_no");
        String project_name=request.getParameter("project_name");
        String excelFullName=basePath+"/upload/pdf/"+(project_no+"_MTC_"+ UUID.randomUUID().toString()+".xls");
        File file0=new File(excelFullName);
        if(!file0.exists()){
            file0.createNewFile();
        }
        View m_view = new View();
        List<String>finalexcelList=new ArrayList<>();
        if(project_no!=null&&!project_no.equals("")){
            int startLine=1;
            try{
                //先清理.zip垃圾文件
                FileRenameUtil.cleanTrashFiles(basePath);
                HttpSession session = request.getSession();
                session.setAttribute("statisticExcelProgress", String.valueOf(0));
                //获取外防涂层不合格信息
                List<HashMap<String,Object>> odCoatingRejectData=getODCoatingRejectData(project_no);
                //获取内防涂层不合格信息
                List<HashMap<String,Object>> idCoatingRejectData=getIDCoatingRejectData(project_no);
                startLine=odCoatingRejectData.size()>idCoatingRejectData.size()?odCoatingRejectData.size():idCoatingRejectData.size();
                int totalCount=odCoatingRejectData.size()+idCoatingRejectData.size();
                RangeRef newRange = null;
                m_view.getLock();
                //HashMap<String,String>hs=new HashMap<>();
                // 行 列 数值
                m_view.setTextAsValue(0, 0, "外防");
                m_view.setTextAsValue(0, 2, "内防");
                m_view.setTextAsValue(0, 4, "项目名称");
                m_view.setTextAsValue(0, 5, project_name);
                m_view.setTextAsValue(1, 0, "涂层不合格原因");
                m_view.setTextAsValue(1, 1, "不合格支数");
                m_view.setTextAsValue(1, 2, "涂层不合格原因");
                m_view.setTextAsValue(1, 3, "不合格支数");
                startLine=startLine+2;
                int g_od_start=3,g_od_end=odCoatingRejectData.size()+3,g_id_start=3,g_id_end=idCoatingRejectData.size()+2;
                for (int i=0;i<odCoatingRejectData.size();i++){
                    String reject_reason= String.valueOf(odCoatingRejectData.get(i).get("reject_reason"));
                    String total_count=String.valueOf(odCoatingRejectData.get(i).get("total_count"));
                    m_view.setTextAsValue(i+2,0,reject_reason);
                    m_view.setNumber(i+2, 1, Double.parseDouble(total_count));
                }
                for (int i=0;i<idCoatingRejectData.size();i++){
                    String reject_reason= String.valueOf(idCoatingRejectData.get(i).get("reject_reason"));
                    String total_count=String.valueOf(idCoatingRejectData.get(i).get("total_count"));
                    m_view.setTextAsValue( i+2,2, reject_reason);
                    m_view.setNumber(i+2, 2, Double.parseDouble(total_count));
                }
                startLine=startLine+2;
                // 设置公式, 可参照./test/formula/FormulaSample.java
                //String sumStr="SUM(C"+(startLine+1)+":C"+(startLine+1)+")";
                //System.out.println("--------------------sumStr="+sumStr);
                //m_view.setFormula(startLine, 2, sumStr);
                // 选中单元格区域
                //m_view.setSelection("C7:D7");
                // 编辑复制 向右复制
                ///m_view.editCopyRight();



                //------------------外防

                /** 1.1 绘制饼图, 可参照./test/chart/ChartFormatDemo.java */
                // 绘图区坐标addChart（左上列x，左上行y，右下列x，右下行y）
                ChartShape chart = m_view.addChart(0, startLine+9, 10, startLine+40);
                // 图表格式, 其他格式参照demo
                chart.setChartType(ChartShape.TypePie);

                // 添加一个系列
                chart.addSeries();
                // 饼图数据源,饼图需要的具体数字，不包含总数，参数为 开始单元格结束单元格
                String dataStr="Sheet1!$B$"+(g_od_start)+":$B$"+g_od_end;
                System.out.println("--------------------dataStr="+dataStr);
                chart.setSeriesYValueFormula(0, dataStr);
                // 数据对应的说明.如：货车  12辆，这里是货车
                String nameStr="Sheet1!$A$"+(g_od_start)+":$A$"+g_od_end;
                System.out.println("--------------------nameStr="+nameStr);
                chart.setCategoryFormula(nameStr);
                // 图表名称
                chart.setTitle("涂层不合格原因分布图");


                //------------------内防
                /** 1.1 绘制饼图, 可参照./test/chart/ChartFormatDemo.java */
                // 绘图区坐标addChart（左上列x，左上行y，右下列x，右下行y）
                ChartShape chart1 = m_view.addChart(0, startLine+50, 10, startLine+40*2);
                // 图表格式, 其他格式参照demo
                chart.setChartType(ChartShape.TypePie);

                // 添加一个系列
                chart1.addSeries();
                // 饼图数据源,饼图需要的具体数字，不包含总数，参数为 开始单元格结束单元格
                String dataStr1="Sheet1!$D$"+(g_id_start)+":$D$"+g_id_end;
                System.out.println("--------------------dataStr1="+dataStr1);
                chart.setSeriesYValueFormula(0, dataStr);
                // 数据对应的说明.如：货车  12辆，这里是货车
                String nameStr1="Sheet1!$C$"+(g_id_start)+":$C$"+g_id_end;
                System.out.println("--------------------nameStr1="+nameStr1);
                chart1.setCategoryFormula(nameStr);
                // 图表名称
                chart1.setTitle("钢管不合格原因分布图");



                /** 1.2 设置样式, 可参照./test/chart/ChartFormatTest.java */
                // 设置列宽行高
                m_view.setColWidth(1, 18 * 256);
                // 设置图纸样式, 参照各种Format类
                ChartFormat cf = chart.getChartFormat();
//            cf.setPattern(com.jxcell.CellFormat.PatternSolid);
//            cf.setPatternFG(m_view.getPaletteEntry(31).getRGB());
                chart.setChartFormat(cf);
                // 设置绘图区颜色
                cf = chart.getPlotFormat();
//            cf.setForeColor((new Color(0, 255, 0)).getRGB());
                chart.setPlotFormat(cf);

                // 展示图饼文字描述
                cf = chart.getSeriesFormat(0);
                cf.setDataLabelPosition(ChartFormat.DataLabelPositionOutside);
//            cf.setDataLabelPosition(ChartFormat.DataLabelPositionAuto);
//            cf.setDataLabelType(ChartFormat.DataLabelValue);
                cf.setDataLabelType(ChartFormat.DataLabelCategoryAndPercent);
                chart.setSeriesFormat(0, cf);

                // 设置图饼分块颜色
                chart.setVaryColors(true);

                cf = chart.getDataLabelFormat(0, 0);
                cf.setForeColor((new Color(0, 0, 255)).getRGB());
                cf.setFontColor((new Color(255, 0, 0)).getRGB());
                chart.setDataLabelFormat(0, 0, cf);

                // 设置标题字体格式
                cf = chart.getTitleFormat();
                cf.setFontBold(true);
                cf.setFontSize(20 * 20);
                chart.setTitleFormat(cf);
                // 图表刻印位置
                chart.setLegendPosition(ChartFormat.LegendPlacementBottom);
                // 图表刻印样式, 取消图饼边框显示
                cf = chart.getLegendFormat();
                cf.setLineNone();
                cf.setFontSizeInPoints(13);
                chart.setLegendFormat(cf);



                /** 2.0 导出功能 */
                // excel写出路径
                m_view.write(excelFullName);
                System.out.println("end");
                Designer.newDesigner(m_view);
                //System.out.println("startLine="+startLine);
                System.out.println("newexcelName======");
                //DrawPieChart(odCoatingRejectData,idCoatingRejectData,excelFullName,startLine);
                session.setAttribute("statisticExcelProgress", String.valueOf("100"));
                if(totalCount==0){
                    success=false;
                    message="没有不合格记录";
                }else{
                    success=true;
                    message="存在不合格记录"+String.valueOf(totalCount)+"条";
                }

            }catch (Exception e){
                e.printStackTrace();
            }finally {
                m_view.releaseLock();
                finalexcelList.add(excelFullName);
                delSetPath.add(excelFullName);
                zipName="/upload/pdf/"+ ResponseUtil.downLoadPdf(finalexcelList,request,response);
                for (int j=0;j<delSetPath.size();j++){
                    if(delSetPath.get(j)!=null){
                        File file=new File(delSetPath.get(j));
                        if(file.exists()){
                            file.delete();
                        }
                    }
                }
            }

        }
        Map<String,Object> maps=new HashMap<String,Object>();
        maps.put("success",success);
        maps.put("zipName",zipName);
        maps.put("message",message);
        String mmp= JSONArray.toJSONString(maps);
        return mmp;
    }
    //绘制饼图(内外防不分开情况)
    public  void DrawPieChart(List<HashMap<String,Object>> OdList,List<HashMap<String,Object>> IdList,String path,int startLine) {
        View m_view = new View();
        RangeRef newRange = null;
        try {
            /** 1.0 数据准备, 可参照./test/TextFormattingTest.java */
            m_view.getLock();
            // 标题 (行，列，值)；
            HashMap<String,String>hs=new HashMap<>();
            for (int i=0;i<OdList.size();i++){
                String reject_reason= String.valueOf(OdList.get(i).get("reject_reason"));
                String total_count=String.valueOf(OdList.get(i).get("total_count"));
                if(hs.containsKey(reject_reason)){
                    String result=String.valueOf(hs.get(reject_reason));
                    if(total_count!=null&&!"".equals(total_count)&&result!=null&&!"".equals(result)){
                        int count=(Integer.parseInt(result)+Integer.parseInt(total_count));
                        hs.put(reject_reason,String.valueOf(count));
                    }
                }else{
                    hs.put(reject_reason,total_count);
                }
            }
            for (int i=0;i<IdList.size();i++){
                String reject_reason= String.valueOf(IdList.get(i).get("reject_reason"));
                String total_count=String.valueOf(IdList.get(i).get("total_count"));
                if(hs.containsKey(reject_reason)){
                    String result=String.valueOf(hs.get(reject_reason));
                    if(total_count!=null&&!"".equals(total_count)&&result!=null&&!"".equals(result)){
                        int count=(Integer.parseInt(result)+Integer.parseInt(total_count));
                        hs.put(reject_reason,String.valueOf(count));
                    }
                }else{
                    hs.put(reject_reason,total_count);
                }
            }
            Iterator iter = hs.entrySet().iterator();
            startLine+=2;
            int index=1;
            m_view.setTextAsValue(startLine+index, 1, "名称");
            m_view.setTextAsValue(startLine+index, 2, "数值");
            while (iter.hasNext()) {
                Map.Entry entry=(Map.Entry)iter.next();
                String reject_reason=String.valueOf(entry.getKey());
                String total_count=String.valueOf(entry.getValue());
                // 分项
                m_view.setTextAsValue(startLine+index, 1, reject_reason);
                // 数据
                m_view.setNumber(startLine+index, 2, Double.parseDouble(total_count));
                index++;
            }
            m_view.setTextAsValue(startLine+index, 1, "总计量");
            // 设置公式, 可参照./test/formula/FormulaSample.java
            String sumStr="SUM(C"+(startLine+1)+":C"+(startLine+index+1)+")";
            m_view.setFormula(startLine+index, 2, sumStr);
            // 选中单元格区域
            //m_view.setSelection("C7:D7");
            // 编辑复制 向右复制
            ///m_view.editCopyRight();

            /** 1.1 绘制饼图, 可参照./test/chart/ChartFormatDemo.java */
            // 绘图区坐标addChart（左上列x，左上行y，右下列x，右下行y）
            ChartShape chart = m_view.addChart(0, startLine+9, 10, startLine+40);
            // 图表格式, 其他格式参照demo
            chart.setChartType(ChartShape.TypePie);

            // 添加一个系列
            chart.addSeries();
            // 饼图数据源,饼图需要的具体数字，不包含总数，参数为 开始单元格结束单元格
            String dataStr="coating_reject!$C$"+(startLine+1)+":$C$"+(startLine+index-1);
            chart.setSeriesYValueFormula(0, dataStr);
            // 数据对应的说明.如：货车  12辆，这里是货车
            String nameStr="coating_reject!$B$"+startLine+1+":$B$6"+(startLine+index-1);
            chart.setCategoryFormula(nameStr);

            // 图表名称
            chart.setTitle("服务状态分布图");

            /** 1.2 设置样式, 可参照./test/chart/ChartFormatTest.java */
            // 设置列宽行高
            m_view.setColWidth(1, 18 * 256);
            // 设置图纸样式, 参照各种Format类
            ChartFormat cf = chart.getChartFormat();
//            cf.setPattern(com.jxcell.CellFormat.PatternSolid);
//            cf.setPatternFG(m_view.getPaletteEntry(31).getRGB());
            chart.setChartFormat(cf);
            // 设置绘图区颜色
            cf = chart.getPlotFormat();
//            cf.setForeColor((new Color(0, 255, 0)).getRGB());
            chart.setPlotFormat(cf);

            // 展示图饼文字描述
            cf = chart.getSeriesFormat(0);
            cf.setDataLabelPosition(ChartFormat.DataLabelPositionOutside);
//            cf.setDataLabelPosition(ChartFormat.DataLabelPositionAuto);
//            cf.setDataLabelType(ChartFormat.DataLabelValue);
            cf.setDataLabelType(ChartFormat.DataLabelCategoryAndPercent);
            chart.setSeriesFormat(0, cf);

            // 设置图饼分块颜色
            chart.setVaryColors(true);

            cf = chart.getDataLabelFormat(0, 0);
            cf.setForeColor((new Color(0, 0, 255)).getRGB());
            cf.setFontColor((new Color(255, 0, 0)).getRGB());
            chart.setDataLabelFormat(0, 0, cf);

            // 设置标题字体格式
            cf = chart.getTitleFormat();
            cf.setFontBold(true);
            cf.setFontSize(20 * 20);
            chart.setTitleFormat(cf);
            // 图表刻印位置
            chart.setLegendPosition(ChartFormat.LegendPlacementBottom);
            // 图表刻印样式, 取消图饼边框显示
            cf = chart.getLegendFormat();
            cf.setLineNone();
            cf.setFontSizeInPoints(13);
            chart.setLegendFormat(cf);

            /** 2.0 导出功能 */
            // excel写出路径
            m_view.write(path);
            System.out.println("end");
            Designer.newDesigner(m_view);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            m_view.releaseLock();
        }
    }

    //获取PDF生成进度
    @RequestMapping(value="getStatisticExcelProgress",produces="application/json;charset=UTF-8")
    @ResponseBody
    public  String getStatisticExcelProgress(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json=new JSONObject();
        try{

            HttpSession session = request.getSession();
            String statisticExcelProgress=(String)session.getAttribute("statisticExcelProgress");
            if(statisticExcelProgress==null||statisticExcelProgress.equals(""))
                statisticExcelProgress="0";
            //跳转到用户主页
            json.put("success",true);
            json.put("statisticExcelProgress",statisticExcelProgress);
            ResponseUtil.write(response,json);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    //绘制线状图
    public  void DrawLinearGraph(String path)
    {
        String xlsFullName=path+"upload/pdf/out.xls";
        System.out.println("xlsFullName="+xlsFullName);
        try{
            File file0=new File(xlsFullName);
            if(!file0.exists()){
                file0.createNewFile();
            }
        }catch (Exception e){
             System.out.println(e.getMessage());
        }

        View m_view = new View();
        RangeRef newRange = null;
        try {
            m_view.getLock();
            //标题 setTextAsValue(行，列，值)；
            m_view.setTextAsValue(1,2,"Jan");
            m_view.setTextAsValue(1,3,"Feb");
            m_view.setTextAsValue(1,4,"Mar");
            m_view.setTextAsValue(1,5,"Apr");
            //分项
            m_view.setTextAsValue(2,1,"香蕉");
            m_view.setTextAsValue(3,1,"大鸭梨");
            m_view.setTextAsValue(4,1,"芒果");
            m_view.setTextAsValue(5,1,"水果1");
            m_view.setTextAsValue(6,1,"水果2");
            m_view.setTextAsValue(7,1,"共计");
            //
            m_view.setTextAsValue(1,6,"time");
            m_view.setNumber(2,6,1);
            m_view.setNumber(3,6,2);
            m_view.setNumber(4,6,3);
            m_view.setNumber(5,6,4);
            m_view.setNumber(6,6,5);
            m_view.setNumber(7,6,6);

            //数据区域随机赋值
            for(int col = 2; col <= 5; col++)
                for(int row = 2; row <= 7; row++)
                    m_view.setFormula(row, col, "rand()");//rand()为excle随机函数
            //设置公式
            m_view.setFormula(7, 2, "SUM(C3:C7)");
            //选中单元格区域
            m_view.setSelection("C8:F8");
            //编辑复制 向右复制
            m_view.editCopyRight();

            //绘图区坐标addChart（左上列x，左上行y，右下列x，右下行y）
            ChartShape chart = m_view.addChart(0, 9.1, 7, 20.4);
            //图标形式
            chart.setChartType(ChartShape.TypeLine);
            /*
            TypeBar:横向柱状图
            TypePie:饼状图
            TypeLine:线状图
            TypeArea:面积图
            TypeDoughnut:圈图
            TypeScatter:线点图
            TypeBubble:没怎么看懂，就是一个灰图，不过查阅资料，貌似是泡状图
            */

            //设置连接区域
            chart.setLinkRange("Sheet1!$C$2", false);

            //添加一个系列
            chart.addSeries();
            //系列名字
            chart.setSeriesName(0, "Sheet1!$C$2");
            //系列值
            chart.setSeriesYValueFormula(0, "Sheet1!$C$3:$C$7");
            //系列分类
            chart.setCategoryFormula("Sheet1!$B$3:$B$7");

            chart.addSeries();
            chart.setSeriesName(1, "Sheet1!$D$2");
            chart.setSeriesYValueFormula(1, "Sheet1!$D$3:$D$7");

            chart.addSeries();
            chart.setSeriesName(2, "Sheet1!$E$2");
            chart.setSeriesYValueFormula(2, "Sheet1!$E$3:$E$7");

            chart.addSeries();
            chart.setSeriesName(3, "Sheet1!$F$2");
            chart.setSeriesYValueFormula(3, "Sheet1!$F$3:$F$7");

//            chart.getChart().validateData();

            //设置横坐标标题
            chart.setAxisTitle(ChartShape.XAxis, 0, "横坐标标题");
            //设置纵坐标标题
            chart.setAxisTitle(ChartShape.YAxis, 0, "纵坐标标题");

            //设置图表样式
            ChartFormat cf = chart.getChartFormat();
            //设置背景色
            cf.setPattern((short)1);
            cf.setPatternFG(Color.LIGHT_GRAY.getRGB());
            chart.setChartFormat(cf);
            //设置绘图区颜色
            cf = chart.getPlotFormat();
            cf.setPattern((short)1);
            cf.setPatternFG(new Color(204, 255, 255).getRGB());
            chart.setPlotFormat(cf);

            //设置横坐标文字大小
            cf = chart.getAxisFormat(ChartShape.XAxis, 0);
            cf.setFontSizeInPoints(8.5);
            chart.setAxisFormat(ChartShape.XAxis, 0, cf);

            //设置纵坐标文字大小
            cf = chart.getAxisFormat(ChartShape.YAxis, 0);
            cf.setFontSizeInPoints(8.5);
            chart.setAxisFormat(ChartShape.YAxis, 0, cf);

            //设置图标内标线样式
            cf = chart.getSeriesFormat(0);//地0个
            cf.setLineStyle((short)1);
            cf.setLineWeight(3*20);
            cf.setLineColor((new Color(0, 0, 128)).getRGB());
            cf.setMarkerAuto(false);
            cf.setMarkerStyle((short)0);
            chart.setSeriesFormat(0, cf);

            cf = chart.getSeriesFormat(1);
            cf.setLineStyle((short)1);
            cf.setLineWeight(3*20);
            cf.setLineColor((new Color(255, 0, 255)).getRGB());
            cf.setMarkerAuto(false);
            cf.setMarkerStyle((short)0);
            chart.setSeriesFormat(1, cf);

            cf = chart.getSeriesFormat(2);
            cf.setLineStyle((short)1);
            cf.setLineWeight(3*20);
            cf.setLineColor((new Color(255, 255, 0)).getRGB());
            cf.setMarkerAuto(false);
            cf.setMarkerStyle((short)0);
            chart.setSeriesFormat(2, cf);

            cf = chart.getSeriesFormat(3);
            cf.setLineStyle((short)1);
            cf.setLineWeight(3*20);
            cf.setLineColor((new Color(0, 255, 255)).getRGB());
            cf.setMarkerAuto(false);
            cf.setMarkerStyle((short)0);
            chart.setSeriesFormat(3, cf);

            //主格网
            cf = chart.getMajorGridFormat(ChartShape.YAxis, 0);
            cf.setLineStyle((short)2);
            cf.setLineColor((new Color(255, 0, 0)).getRGB());
            cf.setLineAuto();
            chart.setMajorGridFormat(ChartShape.YAxis, 0, cf);

            //图利位置
            chart.setLegendPosition(ChartFormat.LegendPlacementRight);

            //图利样式
            cf = chart.getLegendFormat();
            cf.setFontBold(true);
            cf.setFontSizeInPoints(8);
            chart.setLegendFormat(cf);
            //excel写出路径
            m_view.write(xlsFullName);
            //m_view.write("\\Library/apache-tomcat-8.5.30/webapps/ROOT/upload/pdf/out.xls");
            System.out.println("end");
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        finally
        {
            m_view.releaseLock();
        }
    }

    //绘制柱状图
    public  void DrawChart(String path,int startLine) {
        View m_view = new View();
        RangeRef newRange = null;
        try {
            /** 1.0 数据准备, 可参照./test/TextFormattingTest.java */
            m_view.getLock();
            // 标题 (行，列，值)；
            m_view.setTextAsValue(startLine+1, 1, "名称");
            m_view.setTextAsValue(startLine+1, 2, "数值");
            // 分项
            m_view.setTextAsValue(startLine+2, 1, "就诊量");
            m_view.setTextAsValue(startLine+3, 1, "取消量");
            m_view.setTextAsValue(startLine+4, 1, "爽约量");
            m_view.setTextAsValue(startLine+5, 1, "确认预约（待就诊）");
            m_view.setTextAsValue(startLine+6, 1, "总计量");
            // 数据
            m_view.setNumber(startLine+2, 2, 2182);
            m_view.setNumber(startLine+3, 2, 887);
            m_view.setNumber(startLine+4, 2, 191);
            m_view.setNumber(startLine+5, 2, 142);
            // 序号
            m_view.setTextAsValue(startLine+1, 3, "序号");
            m_view.setNumber(startLine+2, 3, 1);
            m_view.setNumber(startLine+3, 3, 2);
            m_view.setNumber(startLine+4, 3, 3);
            m_view.setNumber(startLine+5, 3, 4);

            // 设置公式, 可参照./test/formula/FormulaSample.java
            m_view.setFormula(startLine+6, 2, "SUM(C3:C6)");
            // 选中单元格区域
            m_view.setSelection("C7:D7");
            // 编辑复制 向右复制
            m_view.editCopyRight();

            /** 1.1 绘制饼图, 可参照./test/chart/ChartFormatDemo.java */
            // 绘图区坐标addChart（左上列x，左上行y，右下列x，右下行y）
            ChartShape chart = m_view.addChart(0, startLine+9, 10, startLine+40);
            // 图表格式, 其他格式参照demo
            chart.setChartType(ChartShape.TypePie);

            // 添加一个系列
            chart.addSeries();
            // 饼图数据源,饼图需要的具体数字，不包含总数，参数为 开始单元格结束单元格
            chart.setSeriesYValueFormula(0, "Sheet1!$C$3:$C$6");
            // 数据对应的说明.如：货车  12辆，这里是货车
            chart.setCategoryFormula("Sheet1!$B$3:$B$6");

            // 图表名称
            chart.setTitle("服务状态分布图");

            /** 1.2 设置样式, 可参照./test/chart/ChartFormatTest.java */
            // 设置列宽行高
            m_view.setColWidth(1, 18 * 256);
            // 设置图纸样式, 参照各种Format类
            ChartFormat cf = chart.getChartFormat();
//            cf.setPattern(com.jxcell.CellFormat.PatternSolid);
//            cf.setPatternFG(m_view.getPaletteEntry(31).getRGB());
            chart.setChartFormat(cf);
            // 设置绘图区颜色
            cf = chart.getPlotFormat();
//            cf.setForeColor((new Color(0, 255, 0)).getRGB());
            chart.setPlotFormat(cf);

            // 展示图饼文字描述
            cf = chart.getSeriesFormat(0);
            cf.setDataLabelPosition(ChartFormat.DataLabelPositionOutside);
//            cf.setDataLabelPosition(ChartFormat.DataLabelPositionAuto);
//            cf.setDataLabelType(ChartFormat.DataLabelValue);
            cf.setDataLabelType(ChartFormat.DataLabelCategoryAndPercent);
            chart.setSeriesFormat(0, cf);

            // 设置图饼分块颜色
            chart.setVaryColors(true);

            cf = chart.getDataLabelFormat(0, 0);
            cf.setForeColor((new Color(0, 0, 255)).getRGB());
            cf.setFontColor((new Color(255, 0, 0)).getRGB());
            chart.setDataLabelFormat(0, 0, cf);

            // 设置标题字体格式
            cf = chart.getTitleFormat();
            cf.setFontBold(true);
            cf.setFontSize(20 * 20);
            chart.setTitleFormat(cf);
            // 图表刻印位置
            chart.setLegendPosition(ChartFormat.LegendPlacementBottom);
            // 图表刻印样式, 取消图饼边框显示
            cf = chart.getLegendFormat();
            cf.setLineNone();
            cf.setFontSizeInPoints(13);
            chart.setLegendFormat(cf);

            /** 2.0 导出功能 */
            // excel写出路径
            m_view.write(path);
            System.out.println("end");
            Designer.newDesigner(m_view);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            m_view.releaseLock();
        }
    }



    //获取外防涂层不合格原因记录
    private List<HashMap<String,Object>> getODCoatingRejectData(String project_no){
            List<HashMap<String,Object>> list=pipeBasicInfoDao.getODCoatingRejectData(project_no);
            return list;
    }

    //获取内防涂层不合格原因记录
    private List<HashMap<String,Object>> getIDCoatingRejectData(String project_no){
        List<HashMap<String,Object>> list=pipeBasicInfoDao.getIDCoatingRejectData(project_no);
        return list;
    }

    //获取外防涂层总厚度记录
    private List<HashMap<String,Object>> getODTotalCoatingThicknessData(String project_no){
        List<HashMap<String,Object>> list=pipeBasicInfoDao.getODTotalCoatingThicknessData(project_no);
        return list;
    }

    //获取内防涂层干膜厚度记录
    private List<HashMap<String,Object>> getIDDryFilmThicknessData(String project_no){
        List<HashMap<String,Object>> list=pipeBasicInfoDao.getIDDryFilmThicknessData(project_no);
        return list;
    }
    public  int[] calculateArray(int arr[]) {
        int min = arr[0];
        int max = arr[0];
        int avg = 0;
        for (int i = 1; i < arr.length; i++) {
            if (max < arr[i]) {
                max = arr[i];
            }
            if (min > arr[i]) {
                min = arr[i];
            }
            avg += arr[i];
        }
        arr = new int[] { max, min, avg / arr.length };
        return arr;
    }
}
