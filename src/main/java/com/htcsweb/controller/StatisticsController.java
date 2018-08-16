package com.htcsweb.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.htcsweb.util.FileRenameUtil;
import com.htcsweb.util.GenerateExcelToPDFUtil;
import com.htcsweb.util.MergePDF;
import com.htcsweb.util.ResponseUtil;
import jxl.format.Alignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
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
    @RequestMapping(value="getStatisticsExcel",produces="application/json;charset=UTF-8")
    @ResponseBody
    public  String getShipmentRecordPDF(HttpServletRequest request, HttpServletResponse response){
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
        String begin_time=request.getParameter("beginTime");
        String end_time=request.getParameter("endTime");
        Date beginTime=null;
        Date endTime=null;
        try{
            if(begin_time!=null&&begin_time!=""){
                beginTime=sdf.parse(begin_time);
                System.out.println(beginTime.toString());
            }
            if(end_time!=null&&end_time!=""){
                endTime=sdf.parse(end_time);
                System.out.println(endTime.toString());
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        long startTime = System.currentTimeMillis();//获取开始时间
        if(project_no!=null&&!project_no.equals("")){
            try{

                //先清理.zip垃圾文件
                FileRenameUtil.cleanTrashFiles(basePath);
                HttpSession session = request.getSession();
                session.setAttribute("statisticExcelProgress", String.valueOf(0));
                String xlsFullName=basePath+"/upload/pdf/"+(project_no+"_statistic_"+ UUID.randomUUID().toString()+".xls");
                File file0=new File(xlsFullName);
                if(!file0.exists()){
                    file0.createNewFile();
                }
                System.out.println("xlsFullName"+xlsFullName);
                String templateFullName=request.getSession().getServletContext().getRealPath("/")
                        +"template/shipment_template.xls";
                if(templateFullName.lastIndexOf('/')==-1){
                    templateFullName=templateFullName.replace('\\','/');
                }

//                //获取项目的所有shipment信息
//                List<HashMap<String,Object>> list=shipmentInfoDao.getShipmentByProjectNo(project_no,beginTime,endTime);
//
//                int PAGESIZE=28;
//
//                int totalCount=list.size();
//                List<String>pdfList=new ArrayList<>();
//
//                ArrayList<Label> datalist=new ArrayList<Label>();
//                int index=1,row=0;
//                WritableCellFormat wcf=null;
//                WritableFont wf=null;
//                // String path=this.getClass().getClassLoader().getResource("../../").getPath();
//
//                String logoImageFullName=webPath + "/template/img/image002.jpg";
//                String fontPath=webPath+"/font/simhei.ttf";
//                String copyrightPath=webPath+"/font/simsun.ttc,0";
//                try{
//                    wf = new WritableFont(WritableFont.createFont("Arial"), 9);
//                    wcf= new WritableCellFormat(wf);
//                    wcf.setAlignment(Alignment.CENTRE);
//                    wcf.setBackground(jxl.format.Colour.RED);
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//                String last_shipment_no="";
//                float total_length=0;
//                float total_weight=0;
//                int   total_count=0;
//                session.setAttribute("statisticExcelProgress", String.valueOf("0"));
//                for(int i=0;i<list.size();i++){
//                    System.out.println("pipe_no"+list.get(i).get("pipe_no"));
//                    String shipment_no=String.valueOf(list.get(i).get("shipment_no"));
//                    if(!shipment_no.equals(last_shipment_no)&&last_shipment_no!=("")){
//                        total_count=0;
//                        total_length=0;
//                        total_weight=0;
//                    }
//                    //判断是否需要换页
//                    if(index%PAGESIZE==0||!shipment_no.equals(last_shipment_no)&&last_shipment_no!=("")){
//                        index=1;
//                        System.out.println("另起一页 last_shipment_no="+last_shipment_no+"shipment_no=+"+shipment_no+"pipe_No="+String.valueOf(list.get(i).get("pipe_no")) );
//                        row=0;//另起一页，初始化参数
//                        String newPdfName= GenerateExcelToPDFUtil.PDFAutoMation(templateFullName,datalist,pdfFullName,logoImageFullName,fontPath,copyrightPath);
//                        datalist.clear();
//                        if(newPdfName!=null){
//                            pdfList.add(newPdfName);
//                            delSetPath.add(newPdfName);
//                        }
//                    }
//                    last_shipment_no=shipment_no;
//
//                    if(index==1){
//                        datalist.add(new Label(3, 4, String.valueOf(list.get(i).get("project_name")), wcf));
//                        StringBuilder odwtstr = new StringBuilder();
//                        odwtstr.append("Φ");
//                        odwtstr.append(String.valueOf(list.get(i).get("od")));
//                        odwtstr.append("*");
//                        odwtstr.append(String.valueOf(list.get(i).get("wt")));
//                        odwtstr.append("mm");
//                        datalist.add(new Label(8, 4, odwtstr.toString(), wcf));
//                        datalist.add(new Label(12, 4, String.valueOf(list.get(i).get("shipment_date")), wcf));
//                        StringBuilder coating = new StringBuilder();
//                        coating.append(String.valueOf(list.get(i).get("external_coating")));
//                        coating.append(" ");
//                        coating.append(String.valueOf(list.get(i).get("internal_coating")));
//                        datalist.add(new Label(8, 5, coating.toString(), wcf));
//                        datalist.add(new Label(3, 5, shipment_no, wcf));
//                        datalist.add(new Label(12, 5, String.valueOf(list.get(i).get("vehicle_plate_no")), wcf));
//                        datalist.add(new Label(12, 22,"©2018 TopInspector", wcf));
//                    }
//
//                    int side=0;
//                    if(index>PAGESIZE/2){
//                        side=1;
//                    }
//                    datalist.add(new Label(0+7*side, row+7, String.valueOf(index), wcf));
//                    datalist.add(new Label(1+7*side, row+7, String.valueOf(list.get(i).get("pipe_no")), wcf));
//                    datalist.add(new Label(2+7*side, row+7, String.valueOf(list.get(i).get("p_length")), wcf));
//                    datalist.add(new Label(3+7*side, row+7, String.valueOf(list.get(i).get("weight")), wcf));
//                    datalist.add(new Label(4+7*side, row+7, String.valueOf(list.get(i).get("heat_no")), wcf));
//                    datalist.add(new Label(5+7*side, row+7, String.valueOf(list.get(i).get("pipe_making_lot_no")), wcf));
//                    datalist.add(new Label(6+7*side, row+7,String.valueOf(list.get(i).get("remark")), wcf));
//                    total_count+=1;
//                    total_length+=(float)list.get(i).get("p_length");
//                    total_weight+=(float)list.get(i).get("weight");
//
//                    datalist.add(new Label(4, 21,String.valueOf(total_count), wcf));
//                    datalist.add(new Label(8, 21,String.valueOf(total_length), wcf));
//                    datalist.add(new Label(12, 21,String.valueOf(total_weight), wcf));
//
//
//                    index+=1;
//                    row+=1;
//
//
//
//                    //把用户数据保存在session域对象中
//                    float percent=0;
//                    if(totalCount!=0)
//                        percent=(i+1)*100/totalCount;
//                    session.setAttribute("shipmentpdfProgress", String.valueOf(percent));
//                    System.out.println("shipmentpdfProgress：" + percent);
//                    System.out.println("i：" + i);
//                    System.out.println("totalCount：" + totalCount);
//                }
//                if(datalist.size()>0){
//
//                    String newPdfName= GenerateExcelToPDFUtil.PDFAutoMation(templateFullName,datalist,pdfFullName,logoImageFullName,fontPath,copyrightPath);
//                    datalist.clear();
//                    if(newPdfName!=null){
//                        pdfList.add(newPdfName);
//                        delSetPath.add(newPdfName);
//                    }
//                }
//
//
//                session.setAttribute("shipmentpdfProgress", String.valueOf("100"));
//
//
//                List<String>finalpdfList=new ArrayList<>();
//                if(pdfList.size()>0){
//                    MergePDF.MergePDFs(pdfList,pdfFullName);
//                    pdfList.clear();
//                    finalpdfList.add(pdfFullName);
//                    delSetPath.add(pdfFullName);
//                }
//                if(finalpdfList.size()==0){
//                    success=false;
//                    message="没有发运记录";
//                }else{
//                    success=true;
//                    message="存在成品发运记录"+String.valueOf(list.size())+"条";
//                }
//
//                zipName="/upload/pdf/"+ ResponseUtil.downLoadPdf(finalpdfList,request,response);
//                //定时删除临时文件
//                for (int j=0;j<delSetPath.size();j++){
//                    if(delSetPath.get(j)!=null){
//                        File file=new File(delSetPath.get(j));
//                        if(file.exists()){
//                            file.delete();
//                        }
//                    }
//                }
            }catch (Exception e){
                e.printStackTrace();
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
            String shipmentpdfProgress=(String)session.getAttribute("statisticExcelProgress");
            if(shipmentpdfProgress==null||shipmentpdfProgress.equals(""))
                shipmentpdfProgress="0";
            //跳转到用户主页
            json.put("success",true);
            //System.out.println("ggggggete getAttribute pdfProgress：" + pdfProgress);    //输出程序运行时间
            json.put("statisticExcelProgress",shipmentpdfProgress);
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

}
