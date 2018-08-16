package com.htcsweb.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.htcsweb.dao.PipeBasicInfoDao;
import com.htcsweb.util.FileRenameUtil;
import com.htcsweb.util.GenerateExcelToPDFUtil;
import com.htcsweb.util.MergePDF;
import com.htcsweb.util.ResponseUtil;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.write.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.text.View;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/StatisticsOperation")
public class StatisticsController {


    @Autowired
    private PipeBasicInfoDao pipeBasicInfoDao;


    @RequestMapping(value="getStatisticsExcel",produces="application/json;charset=UTF-8")
    @ResponseBody
    public  String getStatisticsExcel(HttpServletRequest request, HttpServletResponse response)throws Exception{
        String basePath=request.getSession().getServletContext().getRealPath("/");

        if(basePath.lastIndexOf('/')==-1){
            basePath=basePath.replace('\\','/');
        }
        String webPath=basePath;//web路径
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

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

        long startTime = System.currentTimeMillis();//获取开始时间
        if(project_no!=null&&!project_no.equals("")){
            Workbook wb=null;
            WritableWorkbook wwb=null;
            WritableSheet wsheet=null;
            List<String>finalexcelList=new ArrayList<>();
            String newexcelName="";
            try{
                //先清理.zip垃圾文件
                FileRenameUtil.cleanTrashFiles(basePath);
                HttpSession session = request.getSession();
                session.setAttribute("statisticExcelProgress", String.valueOf(0));
//                File file0=new File(xlsFullName);
//                if(!file0.exists()){
//                    file0.createNewFile();
//                }

                String templateFullName=request.getSession().getServletContext().getRealPath("/")
                        +"template/statistics.xls";
                if(templateFullName.lastIndexOf('/')==-1){
                    templateFullName=templateFullName.replace('\\','/');
                }

                newexcelName= GenerateExcelToPDFUtil.FillExcelTemplate(templateFullName,null);
                System.out.println("newexcelName"+newexcelName);
                File newxlsfile = new File(newexcelName);
                wb = Workbook.getWorkbook(newxlsfile);
                wwb = Workbook.createWorkbook(newxlsfile, wb);
                wsheet = wwb.getSheet(0);
                //获取外防涂层不合格信息
                List<HashMap<String,Object>> odCoatingRejectData=getODCoatingRejectData(project_no);
                //获取内防涂层不合格信息
                List<HashMap<String,Object>> idCoatingRejectData=getIDCoatingRejectData(project_no);

                WritableCellFormat wcf=null;
                WritableFont wf=null;
                try{
                    wf = new WritableFont(WritableFont.createFont("Arial"), 9);
                    wcf= new WritableCellFormat(wf);
                    wcf.setAlignment(Alignment.CENTRE);
                    wcf.setBackground(jxl.format.Colour.RED);
                }catch (Exception e){
                    e.printStackTrace();
                }
                int totalCount=odCoatingRejectData.size()+idCoatingRejectData.size();
                session.setAttribute("statisticExcelProgress", String.valueOf("0"));
                float percent=0;
                for (int i=0;i<odCoatingRejectData.size();i++){
                    wsheet.addCell(new Label(0, i+2, String.valueOf(odCoatingRejectData.get(i).get("reject_reason")), wcf));
                    wsheet.addCell(new Label(1, i+2, String.valueOf(odCoatingRejectData.get(i).get("total_count")), wcf));
                    if(totalCount!=0)
                        percent=(i+1)*100/totalCount;
                    session.setAttribute("statisticExcelProgress", String.valueOf(percent));
                    System.out.println("statisticExcelProgress：" + percent);
                    System.out.println("i：" + i);
                    System.out.println("totalCount：" + totalCount);
                }
                for (int i=0;i<idCoatingRejectData.size();i++){
                    wsheet.addCell(new Label(2, i+2, String.valueOf(odCoatingRejectData.get(i).get("reject_reason")), wcf));
                    wsheet.addCell(new Label(3, i+2, String.valueOf(odCoatingRejectData.get(i).get("total_count")), wcf));
                    if(totalCount!=0)
                        percent=(i+1)*100/totalCount;
                    session.setAttribute("statisticExcelProgress", String.valueOf(percent));
                    System.out.println("statisticExcelProgress：" + percent);
                    System.out.println("i：" + i);
                    System.out.println("totalCount：" + totalCount);
                }
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
                wwb.write();
                wwb.close();//关闭
                wb.close();
                finalexcelList.add(newexcelName);
                zipName="/upload/pdf/"+ ResponseUtil.downLoadPdf(finalexcelList,request,response);
                System.out.println(zipName);

            }

        }
        Map<String,Object> maps=new HashMap<String,Object>();
        maps.put("success",success);
        maps.put("zipName",zipName);
        maps.put("message",message);
        String mmp= JSONArray.toJSONString(maps);
        return mmp;
    }
    //获取PDF生成进度
    @RequestMapping(value="getStatisticExcelProgress",produces="application/json;charset=UTF-8")
    @ResponseBody
    public  String getShipmentpdfProgress(HttpServletRequest request, HttpServletResponse response) {
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

//    public  void CreateExcel()
//    {
//
//        View m_view = new View();
//
//
//        RangeRef newRange = null;
//
//        try {
//            m_view.getLock();
//            //标题 setTextAsValue(行，列，值)；
//            m_view.setTextAsValue(1,2,"Jan");
//            m_view.setTextAsValue(1,3,"Feb");
//            m_view.setTextAsValue(1,4,"Mar");
//            m_view.setTextAsValue(1,5,"Apr");
//            //分项
//            m_view.setTextAsValue(2,1,"香蕉");
//            m_view.setTextAsValue(3,1,"大鸭梨");
//            m_view.setTextAsValue(4,1,"芒果");
//            m_view.setTextAsValue(5,1,"水果1");
//            m_view.setTextAsValue(6,1,"水果2");
//            m_view.setTextAsValue(7,1,"共计");
//            //
//            m_view.setTextAsValue(1,6,"time");
//            m_view.setNumber(2,6,1);
//            m_view.setNumber(3,6,2);
//            m_view.setNumber(4,6,3);
//            m_view.setNumber(5,6,4);
//            m_view.setNumber(6,6,5);
//            m_view.setNumber(7,6,6);
//
//            //数据区域随机赋值
//            for(int col = 2; col <= 5; col++)
//                for(int row = 2; row <= 7; row++)
//                    m_view.setFormula(row, col, "rand()");//rand()为excle随机函数
//            //设置公式
//            m_view.setFormula(7, 2, "SUM(C3:C7)");
//            //选中单元格区域
//            m_view.setSelection("C8:F8");
//            //编辑复制 向右复制
//            m_view.editCopyRight();
//
//            //绘图区坐标addChart（左上列x，左上行y，右下列x，右下行y）
//            ChartShape chart = m_view.addChart(0, 9.1, 7, 20.4);
//            //图标形式
//            chart.setChartType(ChartShape.TypeLine);
//            /*
//            TypeBar:横向柱状图
//            TypePie:饼状图
//            TypeLine:线状图
//            TypeArea:面积图
//            TypeDoughnut:圈图
//            TypeScatter:线点图
//            TypeBubble:没怎么看懂，就是一个灰图，不过查阅资料，貌似是泡状图
//            */
//
//            //设置连接区域
//            chart.setLinkRange("Sheet1!$C$2", false);
//
//            //添加一个系列
//            chart.addSeries();
//            //系列名字
//            chart.setSeriesName(0, "Sheet1!$C$2");
//            //系列值
//            chart.setSeriesYValueFormula(0, "Sheet1!$C$3:$C$7");
//            //系列分类
//            chart.setCategoryFormula("Sheet1!$B$3:$B$7");
//
//            chart.addSeries();
//            chart.setSeriesName(1, "Sheet1!$D$2");
//            chart.setSeriesYValueFormula(1, "Sheet1!$D$3:$D$7");
//
//            chart.addSeries();
//            chart.setSeriesName(2, "Sheet1!$E$2");
//            chart.setSeriesYValueFormula(2, "Sheet1!$E$3:$E$7");
//
//            chart.addSeries();
//            chart.setSeriesName(3, "Sheet1!$F$2");
//            chart.setSeriesYValueFormula(3, "Sheet1!$F$3:$F$7");
//
////            chart.getChart().validateData();
//
//            //设置横坐标标题
//            chart.setAxisTitle(ChartShape.XAxis, 0, "横坐标标题");
//            //设置纵坐标标题
//            chart.setAxisTitle(ChartShape.YAxis, 0, "纵坐标标题");
//
//            //设置图表样式
//            ChartFormat cf = chart.getChartFormat();
//            //设置背景色
//            cf.setPattern((short)1);
//            cf.setPatternFG(Color.LIGHT_GRAY.getRGB());
//            chart.setChartFormat(cf);
//            //设置绘图区颜色
//            cf = chart.getPlotFormat();
//            cf.setPattern((short)1);
//            cf.setPatternFG(new Color(204, 255, 255).getRGB());
//            chart.setPlotFormat(cf);
//
//            //设置横坐标文字大小
//            cf = chart.getAxisFormat(ChartShape.XAxis, 0);
//            cf.setFontSizeInPoints(8.5);
//            chart.setAxisFormat(ChartShape.XAxis, 0, cf);
//
//            //设置纵坐标文字大小
//            cf = chart.getAxisFormat(ChartShape.YAxis, 0);
//            cf.setFontSizeInPoints(8.5);
//            chart.setAxisFormat(ChartShape.YAxis, 0, cf);
//
//            //设置图标内标线样式
//            cf = chart.getSeriesFormat(0);//地0个
//            cf.setLineStyle((short)1);
//            cf.setLineWeight(3*20);
//            cf.setLineColor((new Color(0, 0, 128)).getRGB());
//            cf.setMarkerAuto(false);
//            cf.setMarkerStyle((short)0);
//            chart.setSeriesFormat(0, cf);
//
//            cf = chart.getSeriesFormat(1);
//            cf.setLineStyle((short)1);
//            cf.setLineWeight(3*20);
//            cf.setLineColor((new Color(255, 0, 255)).getRGB());
//            cf.setMarkerAuto(false);
//            cf.setMarkerStyle((short)0);
//            chart.setSeriesFormat(1, cf);
//
//            cf = chart.getSeriesFormat(2);
//            cf.setLineStyle((short)1);
//            cf.setLineWeight(3*20);
//            cf.setLineColor((new Color(255, 255, 0)).getRGB());
//            cf.setMarkerAuto(false);
//            cf.setMarkerStyle((short)0);
//            chart.setSeriesFormat(2, cf);
//
//            cf = chart.getSeriesFormat(3);
//            cf.setLineStyle((short)1);
//            cf.setLineWeight(3*20);
//            cf.setLineColor((new Color(0, 255, 255)).getRGB());
//            cf.setMarkerAuto(false);
//            cf.setMarkerStyle((short)0);
//            chart.setSeriesFormat(3, cf);
//
//            //主格网
//            cf = chart.getMajorGridFormat(ChartShape.YAxis, 0);
//            cf.setLineStyle((short)2);
//            cf.setLineColor((new Color(255, 0, 0)).getRGB());
//            cf.setLineAuto();
//            chart.setMajorGridFormat(ChartShape.YAxis, 0, cf);
//
//            //图利位置
//            chart.setLegendPosition(ChartFormat.LegendPlacementRight);
//
//            //图利样式
//            cf = chart.getLegendFormat();
//            cf.setFontBold(true);
//            cf.setFontSizeInPoints(8);
//            chart.setLegendFormat(cf);
//
//
//            //excel写出路径
//            m_view.write("c:\\out.xls");
//            System.out.println("end");
//        }
//        catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//        finally
//        {
//            m_view.releaseLock();
//        }
//    }

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


}
