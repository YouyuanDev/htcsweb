package com.htcsweb.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.htcsweb.dao.PipeBasicInfoDao;
import com.htcsweb.dao.PushEventRuleDao;
import com.htcsweb.dao.RoleDao;
import com.htcsweb.dao.ShipmentInfoDao;
import com.htcsweb.entity.*;
import com.htcsweb.util.*;
import jxl.format.Alignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;


@Controller
@RequestMapping("/ShipmentOperation")
public class ShipmentController {


    @Autowired
    private ShipmentInfoDao shipmentInfoDao;

    @Autowired
    private PipeBasicInfoDao pipeBasicInfoDao;


        //获取所有Shipment列表
        @RequestMapping("getAllShipmentInfoByLike")
        @ResponseBody
        public String getAllShipmentInfoByLike(@Param("project_no")String project_no, @Param("shipment_no")String shipment_no, @Param("pipe_no")String pipe_no, @Param("vehicle_plate_no")String vehicle_plate_no, @Param("begin_time") String begin_time, @Param("end_time") String end_time,HttpServletRequest request){
            String page= request.getParameter("page");
            String rows= request.getParameter("rows");
            if(page==null){
                page="1";
            }
            if(rows==null){
                rows="20";
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
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

            int start=(Integer.parseInt(page)-1)*Integer.parseInt(rows);
            List<HashMap<String,Object>> list=shipmentInfoDao.getAllByLike(project_no,shipment_no,pipe_no,vehicle_plate_no,beginTime,endTime,start,Integer.parseInt(rows));
            int count=shipmentInfoDao.getCountAllByLike(project_no,shipment_no,pipe_no,vehicle_plate_no,beginTime,endTime);
            Map<String,Object> maps=new HashMap<String,Object>();
            maps.put("total",count);
            maps.put("rows",list);
            String mmp= JSONArray.toJSONString(maps);
            System.out.print("mmp:"+mmp);
            return mmp;

        }





        //保存ShipmentInfo
        @RequestMapping(value = "/saveShipmentInfo")
        @ResponseBody
        public String saveShipmentInfo(ShipmentInfo shipmentInfo, HttpServletResponse response){
            System.out.print("saveShipmentInfo");

            JSONObject json=new JSONObject();
            try{
                int resTotal=0;
                List<PipeBasicInfo> pipelist=pipeBasicInfoDao.getPipeNumber(shipmentInfo.getPipe_no());
                String msg="";
                if(shipmentInfo.getId()==0){
                    //添加

                    if(pipelist.size()>0){
                        if(pipelist.get(0).getStatus().equals("out")){
                                //已经出厂
                            msg="，该钢管已出厂！";
                        }
                        else{
                            resTotal=shipmentInfoDao.addShipmentInfo(shipmentInfo);
                        }

                    }
                }else{
                    //修改！

                    resTotal=shipmentInfoDao.updateShipmentInfo(shipmentInfo);
                }
                if(resTotal>0){

                    //更新管子的状态

                    if(pipelist.size()>0){
                        PipeBasicInfo p=pipelist.get(0);
                        if(p.getStatus().equals("bare1")||p.getStatus().equals("bare2")||p.getStatus().equals("odstockin")||p.getStatus().equals("idstockin")) {
                            //验证钢管状态为光管  bare1 bare2 odstockin idstockin
                            p.setStatus("out");
                            p.setLast_accepted_status(p.getStatus());
                                //同时更新钢管基础信息的shipment日期
                            p.setShipment_date(shipmentInfo.getShipment_date());
                            int statusRes = pipeBasicInfoDao.updatePipeBasicInfo(p);
                        }
                    }

                    json.put("success",true);
                    json.put("message","保存成功");
                }else{
                    json.put("success",false);
                    json.put("message","保存失败"+msg);
                }

            }catch (Exception e){
                e.printStackTrace();
                json.put("success",false);
                json.put("message",e.getMessage());

            }finally {
                try {
                    ResponseUtil.write(response, json);
                }catch  (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }


        //删除ShipmentInfo信息
        @RequestMapping("/delShipmentInfo")
        public String delRole(@RequestParam(value = "hlparam")String hlparam,HttpServletResponse response)throws Exception{
            String[]idArr=hlparam.split(",");
            int resTotal=0;
            resTotal=shipmentInfoDao.delShipmentInfo(idArr);
            JSONObject json=new JSONObject();
            StringBuilder sbmessage = new StringBuilder();
            sbmessage.append("总共");
            sbmessage.append(Integer.toString(resTotal));
            sbmessage.append("项发运信息删除成功\n");
            if(resTotal>0){
                //System.out.print("删除成功");
                json.put("success",true);
            }else{
                //System.out.print("删除失败");
                json.put("success",false);
            }
            json.put("message",sbmessage.toString());
            ResponseUtil.write(response,json);
            return null;
        }


    @RequestMapping(value="getShipmentRecordPDF",produces="application/json;charset=UTF-8")
    @ResponseBody
    public  String getShipmentRecordPDF(HttpServletRequest request, HttpServletResponse response){
        String basePath=request.getSession().getServletContext().getRealPath("/");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeformat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //定义根据项目编号获取的合同集合

        List<String>delSetPath=new ArrayList<String>();//定义删除pdf集合，用于生成zip后删除所有的临时文件
        if(UploadFileController.isServerTomcat) {
            basePath = basePath.substring(0, basePath.lastIndexOf('/'));
            basePath = basePath.substring(0, basePath.lastIndexOf('/'));
        }

        //是否成功标识
        String flag="success",zipName="";
        String project_no=request.getParameter("project_no");
        String project_name=request.getParameter("project_name");
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
                session.setAttribute("shipmentpdfProgress", String.valueOf(0));

                String pdfFullName=basePath+"/upload/pdf/"+(project_no+"_shipment_"+System.currentTimeMillis()+".pdf");
//                File file0=new File(pdfFullName);
//                if(!file0.exists()){
//                    file0.createNewFile();
//                }
                System.out.println("pdfFullName"+pdfFullName);
                String templateFullName=request.getSession().getServletContext().getRealPath("/")
                        +"template/shipment_template.xls";

                //获取项目的所有shipment信息
                List<HashMap<String,Object>> list=shipmentInfoDao.getShipmentByProjectNo(project_no,beginTime,endTime);

                int PAGESIZE=30;

                int totalCount=list.size();
                List<String>pdfList=new ArrayList<>();

                ArrayList<Label> datalist=new ArrayList<Label>();
                int index=1,row=0;
                WritableCellFormat wcf=null;
                WritableFont wf=null;
                String path=this.getClass().getClassLoader().getResource("../../").getPath();
                String logoImageFullName=path + "template/img/image002.jpg";
                String fontPath=path+"font/simhei.ttf";
                try{
                    wf = new WritableFont(WritableFont.createFont("Arial"), 9);
                    wcf= new WritableCellFormat(wf);
                    wcf.setAlignment(Alignment.CENTRE);
                    wcf.setBackground(jxl.format.Colour.RED);
                }catch (Exception e){
                    e.printStackTrace();
                }
                String last_shipment_no="";
                for(int i=0;i<list.size();i++){
                    System.out.println("pipe_no"+list.get(i).get("pipe_no"));
                    String shipment_no=String.valueOf(list.get(i).get("shipment_no"));
                    if(index==1){
                        datalist.add(new Label(1, 1, String.valueOf(list.get(i).get("project_no")), wcf));
                        StringBuilder odwtstr = new StringBuilder();
                        odwtstr.append(String.valueOf(list.get(i).get("od")));
                        odwtstr.append("*");
                        odwtstr.append(String.valueOf(list.get(i).get("wt")));
                        odwtstr.append("mm");
                        datalist.add(new Label(1, 2, odwtstr.toString(), wcf));
                        datalist.add(new Label(1, 3, String.valueOf(list.get(i).get("shipment_date")), wcf));
                        StringBuilder coating = new StringBuilder();
                        coating.append(String.valueOf(list.get(i).get("external_coating")));
                        coating.append(" ");
                        coating.append(String.valueOf(list.get(i).get("internal_coating")));
                        datalist.add(new Label(5, 1, coating.toString(), wcf));
                        datalist.add(new Label(5, 2, shipment_no, wcf));
                        datalist.add(new Label(5, 3, String.valueOf(list.get(i).get("vehicle_plate_no")), wcf));
                    }
                    datalist.add(new Label(0, row+6, String.valueOf(index), wcf));
                    datalist.add(new Label(1, row+6, String.valueOf(list.get(i).get("pipe_no")), wcf));
                    datalist.add(new Label(2, row+6, String.valueOf(list.get(i).get("p_length")), wcf));
                    datalist.add(new Label(3, row+6, String.valueOf(list.get(i).get("weight")), wcf));
                    datalist.add(new Label(4, row+6, String.valueOf(list.get(i).get("heat_no")), wcf));
                    datalist.add(new Label(5, row+6, String.valueOf(list.get(i).get("pipe_making_lot_no")), wcf));
                    datalist.add(new Label(6, row+6,String.valueOf(list.get(i).get("remark")), wcf));
                    index+=1;
                    row+=1;
                    if(index%PAGESIZE==0||!shipment_no.equals(last_shipment_no)){
                        index=1;
                        //System.out.println("另起一页" );
                        row=0;//另起一页，初始化参数
                        String newPdfName= GenerateExcelToPDFUtil.PDFAutoMation(templateFullName,datalist,pdfFullName,logoImageFullName,fontPath);
                        datalist.clear();
                        if(newPdfName!=null){
                            pdfList.add(newPdfName);
                            delSetPath.add(newPdfName);
                        }
                    }
                    last_shipment_no=shipment_no;


                    //把用户数据保存在session域对象中
                    float percent=0;
                    if(totalCount!=0)
                        percent=i*100/totalCount;
                    session.setAttribute("shipmentpdfProgress", String.valueOf(percent));
                    System.out.println("shipmentpdfProgress：" + percent);
                    System.out.println("i：" + i);
                    System.out.println("totalCount：" + totalCount);
                }
                if(datalist.size()>0){

                    String newPdfName= GenerateExcelToPDFUtil.PDFAutoMation(templateFullName,datalist,pdfFullName,logoImageFullName,fontPath);
                    datalist.clear();
                    if(newPdfName!=null){
                        pdfList.add(newPdfName);
                        delSetPath.add(newPdfName);
                    }
                }


                List<String>finalpdfList=new ArrayList<>();
                if(pdfList.size()>0){
                    MergePDF.MergePDFs(pdfList,pdfFullName);
                    pdfList.clear();
                    finalpdfList.add(pdfFullName);
                    delSetPath.add(pdfFullName);
                }


                zipName="/upload/pdf/"+ResponseUtil.downLoadPdf(finalpdfList,request,response);
                //定时删除临时文件
                for (int j=0;j<delSetPath.size();j++){
                    if(delSetPath.get(j)!=null){
                        File file=new File(delSetPath.get(j));
                        if(file.exists()){
                            file.delete();
                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return JSONObject.toJSONString(zipName);
    }
}
