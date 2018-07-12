package com.htcsweb.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.htcsweb.dao.ProjectInfoDao;
import com.htcsweb.util.FileRenameUtil;
import com.htcsweb.util.GenerateExcelToPDFUtil;
import com.htcsweb.util.MergePDF;
import com.htcsweb.util.ResponseUtil;
import jxl.Cell;
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
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/MTCOperation")
public class MTCController {

     @Autowired
     private ProjectInfoDao projectInfoDao;

    @RequestMapping(value="getMTCRecord",produces="application/json;charset=UTF-8")
    @ResponseBody
    public  String getMTCRecord(HttpServletRequest request, HttpServletResponse response){
        String basePath=request.getSession().getServletContext().getRealPath("/");
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

        System.out.println("getMTCRecord project_no="+project_no);
        long startTime = System.currentTimeMillis();//获取开始时间
        if(project_no!=null&&!project_no.equals("")){
            try{

                //先清理.zip垃圾文件
                FileRenameUtil.cleanTrashFiles(basePath);


                HttpSession session = request.getSession();
                session.setAttribute("mtcProgress", String.valueOf(0));

                String pdfFullName=basePath+"/upload/pdf/"+(project_no+"_MTC_"+ UUID.randomUUID().toString()+".pdf");
//                File file0=new File(pdfFullName);
//                if(!file0.exists()){
//                    file0.createNewFile();
//                }
                System.out.println("pdfFullName"+pdfFullName);
                String templateFullName=request.getSession().getServletContext().getRealPath("/")
                        +"template/mtc_template.xls";
                if(templateFullName.lastIndexOf('/')==-1){
                    templateFullName=templateFullName.replace('\\','/');
                }
                String logoImageFullName=basePath + "/template/img/image002.jpg";
                String fontPath=basePath+"/font/simhei.ttf";
                String copyrightPath=basePath+"/font/simsun.ttc,0";
                //String newPdfName= GenerateExcelToPDFUtil.PDFAutoMation(templateFullName,null,pdfFullName,logoImageFullName,fontPath,copyrightPath);



                String newexcelName= GenerateExcelToPDFUtil.FillExcelTemplate(templateFullName,null);

                try {
                    File newxlsfile = new File(newexcelName);
                    Workbook wb = Workbook.getWorkbook(newxlsfile);
                    WritableWorkbook wwb = Workbook.createWorkbook(newxlsfile, wb);
                    WritableSheet wsheet = wwb.getSheet(0);


                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                    List<HashMap<String,Object>>getMTCCoatinDurationInfo=projectInfoDao.getMTCCoatinDurationInfo(project_no);
                    List<HashMap<String,Object>>MTCBasicInfo=projectInfoDao.getMTCBasicInfo(project_no);
                    List<HashMap<String,Object>>MTCOnlineInspectionInfo=projectInfoDao.getMTCOnlineInspectionInfo(project_no);
                    List<HashMap<String,Object>>MTCLabInfo=projectInfoDao.getMTCLabInfo(project_no);

                    List<Object> data=new ArrayList<>();
                    data.add(MTCBasicInfo);
                    data.add(MTCOnlineInspectionInfo);
                    data.add(MTCOnlineInspectionInfo);
                    data.add(MTCLabInfo);

                    String project_name=" ",product_name=" ",standard=" ",coating_duration=" ";
                    if(MTCBasicInfo!=null&&MTCBasicInfo.size()>0){
                         project_name=String.valueOf(MTCBasicInfo.get(0).get("project_name"));
                         standard=String.valueOf(MTCBasicInfo.get(0).get("client_spec"));
                         for (int i=0;i<MTCBasicInfo.size();i++){
                             String external_coating=(MTCBasicInfo.get(i).get("external_coating")==null?"":String.valueOf(MTCBasicInfo.get(i).get("external_coating")));
                             String internal_coating=(MTCBasicInfo.get(i).get("internal_coating")==null?"":String.valueOf(MTCBasicInfo.get(i).get("internal_coating")));
                             product_name+=(external_coating+":"+internal_coating+" ");
                         }
                    }
                    if(getMTCCoatinDurationInfo!=null&&getMTCCoatinDurationInfo.size()>0){
                         String cotaing_begin_time=null,cotaing_end_time=null;
                         Object begin_time=getMTCCoatinDurationInfo.get(0).get("min_time");
                         Object end_time=getMTCCoatinDurationInfo.get(0).get("max_time");
                         if(begin_time!=null){
                             cotaing_begin_time=sdf.format(begin_time);
                         }
                         if(end_time!=null){
                             cotaing_end_time=sdf.format(end_time);
                         }
                        coating_duration=cotaing_begin_time+" ~ "+cotaing_end_time;
                    }
                    //写表头数据
                    Label label_projectname = new Label(5, 4, project_name);
                    label_projectname.setCellFormat(wsheet.getCell(5,4).getCellFormat());
                    wsheet.addCell(label_projectname);
                    Label label_productname = new Label(5, 5, product_name);
                    label_productname.setCellFormat(wsheet.getCell(5,5).getCellFormat());
                    wsheet.addCell(label_productname);
                    Label label_standard = new Label(11, 4, standard);
                    label_standard.setCellFormat(wsheet.getCell(11,4).getCellFormat());
                    wsheet.addCell(label_standard);
                    Label label_coatingtime = new Label(11, 5, coating_duration);
                    label_coatingtime.setCellFormat(wsheet.getCell(11,5).getCellFormat());
                    wsheet.addCell(label_coatingtime);






                    for(int item_i=1;item_i<=data.size();item_i++){
                        int start=findStart(wsheet,"#START"+item_i);
                        List<HashMap<String,Object>> list=(List<HashMap<String,Object>>)data.get(item_i-1);
                        if(list==null)continue;


                        WritableCellFormat wcf= new WritableCellFormat(wsheet.getCell(2,start).getCellFormat());
                        wcf.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);

                        //写钢管型号数据
                        if(item_i==1&&list.size()>0){
                            for (int b=list.size()-1;b>=0;b--){
                                String total_length=list.get(b).get("total_length")==null?" ":String.valueOf(list.get(b).get("total_length"));
                                String total_weight=list.get(b).get("total_weight")==null?" ":String.valueOf(list.get(b).get("total_weight"));
                                String [] content={String.valueOf(list.get(b).get("od_wt")),
                                        String.valueOf(list.get(b).get("total_count"))+"pcs",
                                        total_length,
                                        total_weight};
                                for(int ii=0;ii<4;ii++){
                                    Label label_A = new Label(ii*3+2, start, content[ii],wcf);
                                    wsheet.addCell(label_A);
                                    wsheet.mergeCells(ii*3+2,start,ii*3+4,start);
                                }
                                if(b<(list.size()-1))
                                    wsheet.insertRow(start);
                            }
                        }else if(item_i==2&&list.size()>0){
                            //写原材料数据
//                            for (int b=0;b<list.size();b++){
//                                String total_length=list.get(b).get("total_length")==null?" ":String.valueOf(list.get(b).get("total_length"));
//                                String total_weight=list.get(b).get("total_weight")==null?" ":String.valueOf(list.get(b).get("total_weight"));
//                                String [] content={String.valueOf(list.get(b).get("od_wt")),
//                                        String.valueOf(list.get(b).get("total_count"))+"pcs",
//                                        total_length,
//                                        total_weight};
//                                for(int ii=0;ii<4;ii++){
//                                    Label label_A = new Label(ii*3+2, start, content[ii],wcf);
//                                    wsheet.addCell(label_A);
//                                    wsheet.mergeCells(ii*3+2,start,ii*3+4,start);
//                                }
//                                if(b<(list.size()-1))
//                                    wsheet.insertRow(start);
//                            }
                        }else if((item_i==3||item_i==4)&&list.size()>0) {
                            //写在线检测项数据
                            //写实验室数据
                            for (int b=list.size()-1;b>=0;b--){

                                String item_name=list.get(b).get("item_name")==null?" ":String.valueOf(list.get(b).get("item_name"));
                                String unit_name_en=list.get(b).get("unit_name_en")==null?" ":String.valueOf(list.get(b).get("unit_name_en"));
                                if(!unit_name_en.equals("")){
                                    item_name=(item_name+"("+unit_name_en+")");
                                }
                                String item_standard=" ",item_record=" ";
                                String item_standard_max=list.get(b).get("max_value")==null?" ":String.valueOf(list.get(b).get("max_value"));
                                String item_standard_min=list.get(b).get("min_value")==null?" ":String.valueOf(list.get(b).get("min_value"));
                                item_standard=(item_standard_min+" - "+item_standard_max);
                                String item_record_max=list.get(b).get("max_record_value")==null?" ":String.valueOf(list.get(b).get("max_record_value"));
                                String item_record_min=list.get(b).get("min_record_value")==null?" ":String.valueOf(list.get(b).get("min_record_value"));
                                item_record=(item_record_min+" - "+item_record_max);
                                String [] content={String.valueOf(b+1),item_name,
                                        item_standard,
                                        item_record,
                                        "合格 Acceptable"};
                                int []rowi={2,3,5,8,12};
                                for(int ii=0;ii<5;ii++){
                                    Label label_A = new Label(rowi[ii], start, content[ii],wcf);
                                    wsheet.addCell(label_A);
                                    if(rowi[ii]!=12)
                                        wsheet.mergeCells(rowi[ii],start,rowi[ii]+ii,start);
                                    else
                                        wsheet.mergeCells(rowi[ii],start,rowi[ii]+1,start);
                                }
                                if(b<(list.size()-1))
                                    wsheet.insertRow(start);
                            }
                        }

                    }



                    wwb.write();
                    wwb.close();//关闭
                    wb.close();


                }catch (Exception e){

                }finally {

                }




                List<String>finalpdfList=new ArrayList<>();

                finalpdfList.add(newexcelName);


                if(finalpdfList.size()==0){
                    success=false;
                    message="没有发运记录";
                }else{
                    success=true;
                    message="存在MTC记录";
                }

                zipName="/upload/pdf/"+ResponseUtil.downLoadPdf(finalpdfList,request,response);


                //获取项目的所有生产信息


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
//                String logoImageFullName=basePath + "/template/img/image002.jpg";
//                String fontPath=basePath+"/font/simhei.ttf";
//                String copyrightPath=basePath+"/font/simsun.ttc,0";
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
//                session.setAttribute("mtcProgress", String.valueOf("0"));
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
//                    session.setAttribute("mtcProgress", String.valueOf(percent));
//                    System.out.println("mtcProgress：" + percent);
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
//                session.setAttribute("mtcProgress", String.valueOf("100"));
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
//                zipName="/upload/pdf/"+ResponseUtil.downLoadPdf(finalpdfList,request,response);
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


private int findStart(WritableSheet wsheet,String symbol){
    for (int i = 0; i < wsheet.getRows(); i++) {
        Cell cell=wsheet.getCell(16, i);
        if(cell.getContents().equals(symbol)){
            return i;
        }
    }

    return 0;
}



    //获取质保书excel生成进度
    @RequestMapping(value="getMTCProgress",produces="application/json;charset=UTF-8")
    @ResponseBody
    public  String getMTCProgress(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json=new JSONObject();
        try{

            HttpSession session = request.getSession();
            String mtcProgress=(String)session.getAttribute("mtcProgress");
            if(mtcProgress==null||mtcProgress.equals(""))
                mtcProgress="0";
            //跳转到用户主页
            json.put("success",true);
            //System.out.println("ggggggete getAttribute pdfProgress：" + pdfProgress);    //输出程序运行时间
            json.put("mtcProgress",mtcProgress);
            ResponseUtil.write(response,json);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
