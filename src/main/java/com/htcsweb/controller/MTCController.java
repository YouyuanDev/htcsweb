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

                Workbook wb=null;
                WritableWorkbook wwb=null;
                WritableSheet wsheet=null;
                String newexcelName= GenerateExcelToPDFUtil.FillExcelTemplate(templateFullName,null);

                try {
                    File newxlsfile = new File(newexcelName);
                    wb = Workbook.getWorkbook(newxlsfile);
                    wwb = Workbook.createWorkbook(newxlsfile, wb);
                    wsheet = wwb.getSheet(0);


                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                    List<HashMap<String,Object>>getMTCCoatinDurationInfo=projectInfoDao.getMTCCoatinDurationInfo(project_no);
                    List<HashMap<String,Object>>MTCBasicInfo=projectInfoDao.getMTCBasicInfo(project_no);
                    List<HashMap<String,Object>>MTCOnlineInspectionInfo=projectInfoDao.getMTCOnlineInspectionInfo(project_no);
                    List<HashMap<String,Object>>MTCLabInfo=projectInfoDao.getMTCLabInfo(project_no);

                    //总的记录条数

                    int totalCount=0,porcessedCount=0;
                    totalCount+=MTCBasicInfo.size();
                    totalCount+=MTCOnlineInspectionInfo.size();
                    totalCount+=MTCLabInfo.size();

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
                                    porcessedCount+=1;
                                    SetProgress(totalCount,porcessedCount,session);
                                }
                                if(b>0)
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
//                                if(b>0)
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
                                    porcessedCount+=1;
                                    SetProgress(totalCount,porcessedCount,session);
                                }
                                if(b>0)
                                    wsheet.insertRow(start);
                            }
                        }


                    }



                    wsheet.removeColumn(14);
                    wsheet.removeColumn(15);
                    wsheet.removeColumn(16);

                    session.setAttribute("mtcProgress", String.valueOf("100"));


                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    wwb.write();
                    wwb.close();//关闭
                    wb.close();
                    System.out.println("finally");
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

private void SetProgress(int totalCount,int processed,HttpSession session ){
    //把用户数据保存在session域对象中
    float percent=0;
    if(totalCount!=0)
        percent=processed*100/totalCount;
    System.out.println("percent="+percent);
    session.setAttribute("mtcProgress", String.valueOf(percent));
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
