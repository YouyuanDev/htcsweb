package com.htcsweb.controller;


import R.YS;
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
import com.sun.corba.se.impl.oa.toa.TOA;
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
import java.math.BigDecimal;
import java.text.NumberFormat;
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
    /**
     * 绘制不合格分析饼状图
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "getStatisticsExcel", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getStatisticsExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String basePath = request.getSession().getServletContext().getRealPath("/") + "";
        if (basePath.lastIndexOf('/') == -1) {
            basePath = basePath.replace('\\', '/');
        }

        List<String> delSetPath = new ArrayList<String>();//定义删除pdf集合，用于生成zip后删除所有的临时文件
        if (UploadFileController.isServerTomcat) {
            basePath = basePath.substring(0, basePath.lastIndexOf('/'));
            basePath = basePath.substring(0, basePath.lastIndexOf('/'));
        }
        //是否成功标识
        boolean success = false;
        String zipName = "";
        String message = "";
        String project_no = request.getParameter("project_no");
        String project_name = request.getParameter("project_name");
        String excelFullName = basePath + "/upload/pdf/" + (project_no + "_MTC_" + UUID.randomUUID().toString() + ".xls");
        File file0 = new File(excelFullName);
        if (!file0.exists()) {
            file0.createNewFile();
        }
        View m_view = new View();
        List<String> finalexcelList = new ArrayList<>();
        HttpSession session = request.getSession();
        if (project_no != null && !project_no.equals("")) {
            int startLine = 2;
            try {
                //先清理.zip垃圾文件
                FileRenameUtil.cleanTrashFiles(basePath);
                //创建两个Sheet
                m_view.setNumSheets(2);
                m_view.setSheetName(0, "coating_reject");
                m_view.setSheetName(1, "coating_thickness");
                // select the first sheet
                m_view.setSheet(0);
                //获取外防涂层不合格信息
                List<HashMap<String, Object>> odCoatingRejectData = getODCoatingRejectData(project_no);
                //获取内防涂层不合格信息
                List<HashMap<String, Object>> idCoatingRejectData = getIDCoatingRejectData(project_no);
                session.setAttribute("statisticExcelProgress", String.valueOf(0));
                int totalCount = odCoatingRejectData.size();
                totalCount += +idCoatingRejectData.size();
                totalCount += +getODTotalCoatingThicknessData(project_no).size();
                totalCount += +getIDDryFilmThicknessData(project_no).size();
                //float percent=0;
                int porcessedCount = 0;
                m_view.getLock();
                //setTextAsValue参数为 行 列 数值
                m_view.setTextAsValue(0, 0, "外防");
                m_view.setTextAsValue(0, 2, "内防");
                m_view.setTextAsValue(0, 4, "项目名称");
                m_view.setTextAsValue(0, 5, project_name);
                m_view.setTextAsValue(1, 0, "外涂层不合格原因");
                m_view.setTextAsValue(1, 1, "不合格支数");
                m_view.setTextAsValue(1, 2, "内涂层不合格原因");
                m_view.setTextAsValue(1, 3, "不合格支数");
                session.setAttribute("statisticExcelProgress", String.valueOf(0));
                int endLine = odCoatingRejectData.size() > idCoatingRejectData.size() ? odCoatingRejectData.size() : idCoatingRejectData.size();
                endLine = endLine + 2;
                for (int i = 0; i < odCoatingRejectData.size(); i++) {
                    String reject_reason = String.valueOf(odCoatingRejectData.get(i).get("reject_reason"));
                    String total_count = String.valueOf(odCoatingRejectData.get(i).get("total_count"));
                    m_view.setTextAsValue(i + 2, 0, reject_reason);
                    m_view.setNumber(i + 2, 1, Double.parseDouble(total_count));
                    porcessedCount += 1;
                    SetProgress(totalCount, porcessedCount, session);
                }
                for (int i = 0; i < idCoatingRejectData.size(); i++) {
                    String reject_reason = String.valueOf(idCoatingRejectData.get(i).get("reject_reason"));
                    String total_count = String.valueOf(idCoatingRejectData.get(i).get("total_count"));
                    m_view.setTextAsValue(i + 2, 2, reject_reason);
                    m_view.setNumber(i + 2, 3, Double.parseDouble(total_count));
                    porcessedCount += 1;
                    SetProgress(totalCount, porcessedCount, session);
                }
                /*--------------外防-------------*/
                // 绘图区坐标addChart（左上列x，左上行y，右下列x，右下行y）
                ChartShape odChart = m_view.addChart(0, endLine + 5, 10, endLine + 40);
                // 图表格式, 其他格式参照demo
                odChart.setChartType(ChartShape.TypePie);
                // 添加一个系列
                odChart.addSeries();
                // 饼图数据源,饼图需要的具体数字，参数为开始单元格结束单元格
                String dataStr = "coating_reject!$B$3:$B$" + (2 + odCoatingRejectData.size());
                System.out.println("--------------------dataStr=" + dataStr);
                odChart.setSeriesYValueFormula(0, dataStr);
                // 数据对应的说明.
                String odNameStr = "coating_reject!$A$3:$A$" + (2 + odCoatingRejectData.size());
                System.out.println("--------------------odNameStr=" + odNameStr);
                odChart.setCategoryFormula(odNameStr);
                //设置总数
                String odSumStr = "SUM(B3:B" + (2 + odCoatingRejectData.size()) + ")";
                System.out.println("--------------------odSumStr=" + odSumStr);
                m_view.setTextAsValue(endLine + 1, 0, "外防不合格总数");
                m_view.setFormula(endLine + 1, 1, odSumStr);
                odChart.setTitle("外防涂层不合格原因分布图");
                m_view.setColWidth(1, 18 * 256);
                // 设置图纸样式, 参照各种Format类
                ChartFormat odCf = odChart.getChartFormat();
                odChart.setChartFormat(odCf);
                // 设置绘图区颜色
                odCf = odChart.getPlotFormat();
                odChart.setPlotFormat(odCf);
                // 展示图饼文字描述
                odCf = odChart.getSeriesFormat(0);
                odCf.setDataLabelPosition(ChartFormat.DataLabelPositionOutside);
                odCf.setDataLabelType(ChartFormat.DataLabelPercentageMask);
                odChart.setSeriesFormat(0, odCf);
                // 设置图饼分块颜色
                odChart.setVaryColors(true);
                odCf = odChart.getDataLabelFormat(0, 0);
//                odCf.setForeColor((new Color(0, 0, 255)).getRGB());
                odCf.setFontColor((new Color(0, 0, 0)).getRGB());
                odChart.setDataLabelFormat(0, 0, odCf);
                // 设置标题字体格式
                odCf = odChart.getTitleFormat();
                odCf.setFontBold(true);
                odCf.setFontSize(20 * 20);
                odChart.setTitleFormat(odCf);
                // 图表刻印位置
                odChart.setLegendPosition(ChartFormat.LegendPlacementBottom);
                // 图表刻印样式, 取消图饼边框显示
                odCf = odChart.getLegendFormat();
                odCf.setLineNone();
                odCf.setFontSizeInPoints(13);
                odChart.setLegendFormat(odCf);
                /*--------------内防-------------*/
                ChartShape idChart = m_view.addChart(12, endLine + 5, 22, endLine + 40);
                // 图表格式, 其他格式参照demo
                idChart.setChartType(ChartShape.TypePie);
                // 添加一个系列
                idChart.addSeries();
                // 饼图数据源,饼图需要的具体数字，参数为开始单元格结束单元格
                String idDataStr = "coating_reject!$D$3:$D$" + (2 + idCoatingRejectData.size());
                System.out.println("--------------------idDataStr=" + idDataStr);
                idChart.setSeriesYValueFormula(0, idDataStr);
                // 数据对应的说明.
                String idNameStr = "coating_reject!$C$3:$C$" + (2 + idCoatingRejectData.size());
                System.out.println("--------------------idNameStr=" + idNameStr);
                idChart.setCategoryFormula(idNameStr);
                //设置总数
                String idSumStr = "SUM(D3:D" + (2 + idCoatingRejectData.size()) + ")";
                System.out.println("--------------------idSumStr=" + idSumStr);
                m_view.setTextAsValue(endLine + 1, 2, "内防不合格总数");
                m_view.setFormula(endLine + 1, 3, idSumStr);
                idChart.setTitle("内防涂层不合格原因分布图");
                // 设置图纸样式, 参照各种Format类
                ChartFormat idCf = idChart.getChartFormat();
                idChart.setChartFormat(idCf);
                // 设置绘图区颜色
                idCf = idChart.getPlotFormat();
                idChart.setPlotFormat(idCf);
                // 展示图饼文字描述
                idCf = idChart.getSeriesFormat(0);
                idCf.setDataLabelPosition(ChartFormat.DataLabelPositionOutside);
                idCf.setDataLabelType(ChartFormat.LegendPlacementBottom);
                idChart.setSeriesFormat(0, idCf);
                // 设置图饼分块颜色
                idChart.setVaryColors(true);
                idCf = idChart.getDataLabelFormat(0, 0);
//                idCf.setForeColor((new Color(0, 0, 255)).getRGB());
                idCf.setFontColor((new Color(0, 0, 0)).getRGB());
                idChart.setDataLabelFormat(0, 0, idCf);
                // 设置标题字体格式
                idCf = idChart.getTitleFormat();
                idCf.setFontBold(true);
                idCf.setFontSize(20 * 20);
                idChart.setTitleFormat(idCf);
                // 图表刻印位置
                idChart.setLegendPosition(ChartFormat.LegendPlacementBottom);
                // 图表刻印样式, 取消图饼边框显示
                idCf = idChart.getLegendFormat();
                idCf.setLineNone();
                idCf.setFontSizeInPoints(13);
                idChart.setLegendFormat(idCf);
                if (totalCount == 0) {
                    success = false;
                    message = "没有记录";
                } else {
                    success = true;
                    message = "存在记录";
                }
                DrawingThicknessTableHistogram(m_view, project_no, project_name, totalCount, porcessedCount, session);
                //m_view.write("/Users/pengtian/Desktop/out1.xls");
                m_view.write(excelFullName);
                session.setAttribute("statisticExcelProgress", String.valueOf(100));
                //Designer.newDesigner(m_view);
            } catch (Exception e) {
                e.printStackTrace();
                session.setAttribute("statisticExcelProgress", String.valueOf(100));
            } finally {
                m_view.releaseLock();
                finalexcelList.add(excelFullName);
                delSetPath.add(excelFullName);
                zipName = "/upload/pdf/" + ResponseUtil.downLoadPdf(finalexcelList, request, response);
                for (int j = 0; j < delSetPath.size(); j++) {
                    if (delSetPath.get(j) != null) {
                        File file = new File(delSetPath.get(j));
                        if (file.exists()) {
                            file.delete();
                        }
                    }
                }
            }
        }
        Map<String, Object> maps = new HashMap<String, Object>();
        maps.put("success", success);
        maps.put("zipName", zipName);
        maps.put("message", message);
        String mmp = JSONArray.toJSONString(maps);
        return mmp;
    }
    /**
     * 绘制厚度列表柱状图
     * @param m_view(jxcell中视图类)
     * @param project_no(项目编号)
     * @param project_name(项目名称)
     * @param totalCount(总进度)
     * @param porcessedCount(当前进度)
     * @param session(pdf进度session)
     */
    public void DrawingThicknessTableHistogram(View m_view, String project_no, String project_name, int totalCount, int porcessedCount, HttpSession session) {
        try {
            m_view.setSheet(1);
            List<HashMap<String, Object>> thinckness_odlist = getODTotalCoatingThicknessData(project_no);
            List<HashMap<String, Object>> thinckness_idlist = getIDDryFilmThicknessData(project_no);
            ArrayList odThinknessList = new ArrayList();
            ArrayList idThinknessList = new ArrayList();
            int thinckness_startLine = 2;
            /*------外防-------*/
            for (int i = 0; i < thinckness_odlist.size(); i++) {
                String val = String.valueOf(thinckness_odlist.get(i).get("total_coating_thickness"));
                if (val != null) {
                    String[] arr = val.split(",");
                    for (int j = 0; j < arr.length; j++) {
                        if (arr[j] != null && isNumber(arr[j])) {
                            odThinknessList.add(arr[j]);
                        }
                    }
                }
                porcessedCount += 1;
                SetProgress(totalCount, porcessedCount, session);
            }
            float odAvg = getAvg(odThinknessList);
            float[] odMinMax = calculateArray(odThinknessList);
            System.out.println("odMin=" + odMinMax[0]);
            System.out.println("odMax=" + odMinMax[1]);
            HashMap<String, String> odIntervalList = new LinkedHashMap<>();
            int odMultiple = (int) Math.ceil((odMinMax[1] - odMinMax[0]) / 100) * 10;
            System.out.println(odMultiple);
            if (odMultiple != 0) {
                for (int i = 0; i < 10; i++) {
                    odIntervalList.put(getTransferCeil(odMultiple * i) + "-" + getTransferCeil(odMultiple * (i + 1)), String.valueOf(getCount(odThinknessList, getTransferCeil(odMultiple * i), getTransferCeil(odMultiple * (i + 1)))));
                }
            } else {
                odIntervalList.put(getTransferCeil(odMinMax[0]) + "-" + getTransferCeil(odMinMax[1]), String.valueOf(getCount(odThinknessList, getTransferCeil(odMinMax[0]), getTransferCeil(odMinMax[1]))));
            }
            Iterator odIter = odIntervalList.entrySet().iterator();
            int odIndex = 0;
            while (odIter.hasNext()) {
                Map.Entry entry = (Map.Entry) odIter.next();
                System.out.println(entry.getKey() + "::od::" + entry.getValue());
                m_view.setTextAsValue(thinckness_startLine + odIndex, 0, String.valueOf(entry.getKey()));
                m_view.setTextAsValue(thinckness_startLine + odIndex, 1, String.valueOf(entry.getValue()));
                odIndex++;
            }
            /*------内防-------*/
            for (int i = 0; i < thinckness_idlist.size(); i++) {
                String val = String.valueOf(thinckness_idlist.get(i).get("total_coating_thickness"));
                if (val != null) {
                    String[] arr = val.split(",");
                    for (int j = 0; j < arr.length; j++) {
                        if (arr[j] != null && isNumber(arr[j])) {
                            idThinknessList.add(arr[j]);
                        }
                    }
                }
                porcessedCount += 1;
                SetProgress(totalCount, porcessedCount, session);
            }
            float idAvg = getAvg(idThinknessList);
            float[] idMinMax = calculateArray(idThinknessList);
            System.out.println("idMin=" + idMinMax[0]);
            System.out.println("idMax=" + idMinMax[1]);
            HashMap<String, String> idIntervalList = new LinkedHashMap<>();
            float idMultiple = (idMinMax[1] - idMinMax[0]) / 10;
            System.out.println("idMultiple=" + idMultiple);
            if (idMultiple != 0) {
                for (int i = 0; i < 10; i++) {
                    idIntervalList.put(getTransferCeil(idMultiple * i) + "-" + getTransferCeil(idMultiple * (i + 1)), String.valueOf(getCount(idThinknessList, getTransferCeil(idMultiple * i), getTransferCeil(idMultiple * (i + 1)))));
                }
            } else {
                idIntervalList.put(getTransferCeil(idMinMax[0]) + "-" + getTransferCeil(idMinMax[1]), String.valueOf(getCount(idThinknessList, getTransferCeil(idMinMax[0]), getTransferCeil(idMinMax[1]))));
            }
            Iterator idIter = idIntervalList.entrySet().iterator();
            int idIndex = 0;
            while (idIter.hasNext()) {
                Map.Entry entry = (Map.Entry) idIter.next();
                System.out.println(entry.getKey() + "::id::" + entry.getValue());
                m_view.setTextAsValue(thinckness_startLine + idIndex, 2, String.valueOf(entry.getKey()));
                m_view.setTextAsValue(thinckness_startLine + idIndex, 3, String.valueOf(entry.getValue()));
                idIndex++;
            }
            int endLine = odIntervalList.size() > idIntervalList.size() ? odIntervalList.size() : idIntervalList.size();

            m_view.getLock();
            m_view.setTextAsValue(0, 0, "外防");
            m_view.setTextAsValue(0, 2, "内防");
            m_view.setTextAsValue(0, 4, "项目名称");
            m_view.setTextAsValue(0, 5, project_name);
            m_view.setTextAsValue(1, 0, "外防厚度区间列表");
            m_view.setTextAsValue(1, 1, "支数");
            m_view.setTextAsValue(1, 2, "内防厚度区间列表");
            m_view.setTextAsValue(1, 3, "支数");
            m_view.setTextAsValue(endLine + 2, 0, "外防厚度平均数");
            m_view.setTextAsValue(endLine + 2, 1, String.valueOf(odAvg));
            m_view.setTextAsValue(endLine + 2, 2, "内防厚度平均数");
            m_view.setTextAsValue(endLine + 2, 3, String.valueOf(idAvg));
            /*--------外防---------*/
            //绘图区坐标addChart（左上列x，左上行y，右下列x，右下行y）
            ChartShape odChart = m_view.addChart(0, endLine + 5, 10, endLine + 20.4);
            //图标形式
            odChart.setChartType(ChartShape.TypeColumn);
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
            //chart.setLinkRange("Sheet1!$C$2", false);
            //添加一个系列
            odChart.addSeries();
            //系列名字
            odChart.setSeriesName(0, "coating_thickness!$B$2");
            //系列值
            odChart.setSeriesYValueFormula(0, "coating_thickness!$B$3:$B$" + (thinckness_startLine + odIntervalList.size()));
            //系列分类
            odChart.setCategoryFormula("coating_thickness!$A$3:$A$" + (thinckness_startLine + odIntervalList.size()));
            System.out.println("coating_thickness!$B$3:$B$" + (thinckness_startLine + odIntervalList.size()));
            System.out.println("coating_thickness!$A$3:$A$" + (thinckness_startLine + odIntervalList.size()));
            //设置横坐标标题
            odChart.setAxisTitle(ChartShape.XAxis, 0, "外涂层总厚度区间(单位:μm)");
            //设置纵坐标标题
            odChart.setAxisTitle(ChartShape.YAxis, 0, "支数");

            //设置图表样式
            ChartFormat odCf = odChart.getChartFormat();
            //设置背景色
            odCf.setPattern((short) 1);
            odCf.setPatternFG(Color.LIGHT_GRAY.getRGB());
            odChart.setChartFormat(odCf);
            //设置绘图区颜色
            odCf = odChart.getPlotFormat();
            odCf.setPattern((short) 1);
            odCf.setPatternFG(new Color(204, 255, 255).getRGB());
            odChart.setPlotFormat(odCf);

            //设置横坐标文字大小
            odCf = odChart.getAxisFormat(ChartShape.XAxis, 0);
            odCf.setFontSizeInPoints(8.5);
            odChart.setAxisFormat(ChartShape.XAxis, 0, odCf);

            //设置纵坐标文字大小
            odCf = odChart.getAxisFormat(ChartShape.YAxis, 0);
            odCf.setFontSizeInPoints(8.5);
            odChart.setAxisFormat(ChartShape.YAxis, 0, odCf);

            //设置图标内标线样式
            odCf = odChart.getSeriesFormat(0);//地0个
            odCf.setLineStyle((short) 1);
            odCf.setLineWeight(3 * 20);
            odCf.setLineColor((new Color(0, 0, 128)).getRGB());
            odCf.setMarkerAuto(false);
            odCf.setMarkerStyle((short) 0);
            odChart.setSeriesFormat(0, odCf);

            //主格网
            odCf = odChart.getMajorGridFormat(ChartShape.YAxis, 0);
            odCf.setLineStyle((short) 2);
            odCf.setLineColor((new Color(255, 0, 0)).getRGB());
            odCf.setLineAuto();
            odChart.setMajorGridFormat(ChartShape.YAxis, 0, odCf);

            //图利位置
            odChart.setLegendPosition(ChartFormat.LegendPlacementRight);

            //图利样式
            odCf = odChart.getLegendFormat();
            odCf.setFontBold(true);
            odCf.setFontSizeInPoints(8);
            odChart.setLegendFormat(odCf);


            /*--------内防---------*/
            //绘图区坐标addChart（左上列x，左上行y，右下列x，右下行y）
            ChartShape idChart = m_view.addChart(12, endLine + 5, 22, endLine + 20.4);
            //图标形式
            idChart.setChartType(ChartShape.TypeColumn);
            //添加一个系列
            idChart.addSeries();
            //系列名字
            idChart.setSeriesName(0, "coating_thickness!$B$2");
            //系列值
            idChart.setSeriesYValueFormula(0, "coating_thickness!$D$3:$D$" + (thinckness_startLine + odIntervalList.size()));
            //系列分类
            idChart.setCategoryFormula("coating_thickness!$C$3:$C$" + (thinckness_startLine + odIntervalList.size()));
            System.out.println("coating_thickness!$D$3:$D$" + (thinckness_startLine + odIntervalList.size()));
            System.out.println("coating_thickness!$C$3:$C$" + (thinckness_startLine + odIntervalList.size()));
            //设置横坐标标题
            idChart.setAxisTitle(ChartShape.XAxis, 0, "内涂层总厚度区间(单位:μm)");
            //设置纵坐标标题
            idChart.setAxisTitle(ChartShape.YAxis, 0, "支数");

            //设置图表样式
            ChartFormat idCf = idChart.getChartFormat();
            //设置背景色
            idCf.setPattern((short) 1);
            idCf.setPatternFG(Color.LIGHT_GRAY.getRGB());
            idChart.setChartFormat(idCf);
            //设置绘图区颜色
            idCf = idChart.getPlotFormat();
            idCf.setPattern((short) 1);
            idCf.setPatternFG(new Color(204, 255, 255).getRGB());
            idChart.setPlotFormat(idCf);

            //设置横坐标文字大小
            idCf = idChart.getAxisFormat(ChartShape.XAxis, 0);
            idCf.setFontSizeInPoints(8.5);
            idChart.setAxisFormat(ChartShape.XAxis, 0, idCf);

            //设置纵坐标文字大小
            idCf = idChart.getAxisFormat(ChartShape.YAxis, 0);
            idCf.setFontSizeInPoints(8.5);
            idChart.setAxisFormat(ChartShape.YAxis, 0, idCf);

            //设置图标内标线样式
            idCf = idChart.getSeriesFormat(0);//地0个
            idCf.setLineStyle((short) 1);
            idCf.setLineWeight(3 * 20);
            idCf.setLineColor((new Color(0, 0, 128)).getRGB());
            idCf.setMarkerAuto(false);
            idCf.setMarkerStyle((short) 0);
            idChart.setSeriesFormat(0, idCf);

            //主格网
            idCf = idChart.getMajorGridFormat(ChartShape.YAxis, 0);
            idCf.setLineStyle((short) 2);
            idCf.setLineColor((new Color(255, 0, 0)).getRGB());
            idCf.setLineAuto();
            idChart.setMajorGridFormat(ChartShape.YAxis, 0, idCf);

            //图利位置
            idChart.setLegendPosition(ChartFormat.LegendPlacementRight);

            //图利样式
            idCf = idChart.getLegendFormat();
            idCf.setFontBold(true);
            idCf.setFontSizeInPoints(8);
            idChart.setLegendFormat(idCf);
            //excel写出路径
            //m_view.write("/Users/pengtian/Desktop/out1.xls");
            System.out.println("end");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    /**
     * 获取PDF生成进度
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "getStatisticExcelProgress", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getStatisticExcelProgress(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        try {

            HttpSession session = request.getSession();
            String statisticExcelProgress = (String) session.getAttribute("statisticExcelProgress");
            System.out.println("statisticExcelProgress===========" + statisticExcelProgress);
            if (statisticExcelProgress == null || statisticExcelProgress.equals(""))
                statisticExcelProgress = "0";
            //跳转到用户主页
            json.put("success", true);
            json.put("statisticExcelProgress", statisticExcelProgress);
            ResponseUtil.write(response, json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 获取外防涂层不合格原因记录
     * @param project_no(项目编号)
     * @return
     */
    private List<HashMap<String, Object>> getODCoatingRejectData(String project_no) {
        List<HashMap<String, Object>> list = pipeBasicInfoDao.getODCoatingRejectData(project_no);
        return list;
    }
    /**
     * 获取内防涂层不合格原因记录
     * @param project_no(项目编号)
     * @return
     */
    private List<HashMap<String, Object>> getIDCoatingRejectData(String project_no) {
        List<HashMap<String, Object>> list = pipeBasicInfoDao.getIDCoatingRejectData(project_no);
        return list;
    }
    /**
     * 获取外防涂层总厚度记录
     * @param project_no(项目编号)
     * @return
     */
    private List<HashMap<String, Object>> getODTotalCoatingThicknessData(String project_no) {
        List<HashMap<String, Object>> list = pipeBasicInfoDao.getODTotalCoatingThicknessData(project_no);
        return list;
    }
    /**
     * 获取内防涂层干膜厚度记录
     * @param project_no(项目编号)
     * @return
     */
    private List<HashMap<String, Object>> getIDDryFilmThicknessData(String project_no) {
        List<HashMap<String, Object>> list = pipeBasicInfoDao.getIDDryFilmThicknessData(project_no);
        return list;
    }
    /**
     * 判断是否是数值
     * @param str(字符串)
     * @return
     */
    public static boolean isNumber(String str) {
        String reg = "^[0-9]+(.[0-9]+)?$";
        return str.matches(reg);
    }
    /**
     * 获取数组中的最大最小值
     * @param arr(数组)
     * @return
     */
    public static float[] calculateArray(ArrayList arr) {
        float[] arrFloat = new float[]{0, 0};
        if (arr.size() > 0) {
            arrFloat[0] = Float.parseFloat(String.valueOf(Collections.min(arr)));
            arrFloat[1] = Float.parseFloat(String.valueOf(Collections.max(arr)));
        }
        return arrFloat;
    }
    /**
     * 求满足区间之间数值的个数
     * @param list(数组)
     * @param start(起始位置)
     * @param end(结束位置)
     * @return
     */
    public static int getCount(ArrayList list, int start, int end) {
        int count = 0;
        start = getTransferCeil(start);
        end = getTransferCeil(end);
        for (int i = 0; i < list.size(); i++) {
            float val = Float.parseFloat(String.valueOf(list.get(i)));
            if (i != list.size() - 1) {
                if (val >= start && val < end) {
                    count++;
                }
            } else {
                if (val >= start && val <= end) {
                    count++;
                }
            }
        }
        return count;
    }
    /**
     * 向下取整
     * @param val(值)
     * @return
     */
    public static int getTransferFloor(float val) {
        return (int) Math.floor(val);
    }
    /**
     * 向上取整
     * @param val(值)
     * @return
     */
    public static int getTransferCeil(float val) {
        return (int) Math.ceil(val);
    }
    /**
     * 保留4位小数
     * @param val(值)
     * @return
     */
    public static float getTransferFloat(float val) {
        BigDecimal b = new BigDecimal(val);
        float f1 = b.setScale(4, BigDecimal.ROUND_HALF_UP).floatValue();
        return f1;
    }
    /**
     * 计算数组的平均值
     * @param list(数组)
     * @return
     */
    public static float getAvg(ArrayList list) {
        float avg = 0, total = 0;
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                total = total + Float.parseFloat(String.valueOf(list.get(i)));
            }
            avg = getTransferFloat(total / list.size());
        }
        return avg;
    }
    /**
     * 设置pdf生成进度
     * @param totalCount(总进度)
     * @param processed（当前进度）
     * @param session（session）
     */
    private void SetProgress(int totalCount, int processed, HttpSession session) {
        //把用户数据保存在session域对象中
        float percent = 0;
        if (totalCount != 0)
            percent = processed * 100 / totalCount;
        System.out.println("percent=" + percent);
        session.setAttribute("statisticExcelProgress", String.valueOf(percent));
    }
}
