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



    //添加ShipmentInfo APP使用
    @RequestMapping(value = "/APPAddShipmentInfo")
    @ResponseBody
    public String APPAddShipmentInfo(HttpServletRequest request,HttpServletResponse response){
        System.out.print("APPAddShipmentInfo");
        String shipment_no=request.getParameter("shipment_no");
        String vehicle_plate_no=request.getParameter("vehicle_plate_no");
        String pipeno_arr=request.getParameter("pipeno_arr");
        String operator_no=null;
        if(request.getSession().getAttribute("userSession")!=null) {
            operator_no=(String) request.getSession().getAttribute("userSession");
        }


        JSONObject json=new JSONObject();


        try{
            int SuccessCount=0,failCount=0;
            int resTotal=0;
            if(pipeno_arr==null||pipeno_arr.equals("")||shipment_no==null){
                //
                json.put("success",false);
                json.put("message","出库失败，无管号");
                ResponseUtil.write(response, json);
                return "";
            }

            String[]pipenoArr=pipeno_arr.split(",");
            String msg="";
            for(int i=0;i<pipenoArr.length;i++){
                List<PipeBasicInfo> pipelist=pipeBasicInfoDao.getPipeNumber(pipenoArr[i]);


                //添加
                ShipmentInfo shipmentInfo=new ShipmentInfo();
                if(pipelist.size()>0){
                    if(pipelist.get(0).getStatus().equals("out")){
                        //已经出厂
                        msg+="，钢管"+pipenoArr[i]+"已出厂！";
                    }
                    else{

                        shipmentInfo.setId(0);
                        shipmentInfo.setPipe_no(pipenoArr[i]);
                        shipmentInfo.setOperator_no(operator_no);
                        shipmentInfo.setRemark("APP 出库");
                        shipmentInfo.setVehicle_plate_no(vehicle_plate_no);
                        shipmentInfo.setShipment_no(shipment_no);
                        shipmentInfo.setShipment_date(new Date());
                        resTotal=shipmentInfoDao.addShipmentInfo(shipmentInfo);
                        SuccessCount+=resTotal;
                    }

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

                }else{
                    //没有出库成功
                    msg+="，钢管"+pipenoArr[i]+"出库失败！";
                    failCount+=1;
                }
            }

            json.put("success",true);
            json.put("message","总共出库"+SuccessCount+"根钢管,出库失败"+failCount+"根钢管"+msg);

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
        if(basePath.lastIndexOf('/')==-1){
            basePath=basePath.replace('\\','/');
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        List<String>delSetPath=new ArrayList<String>();//定义删除pdf集合，用于生成zip后删除所有的临时文件
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
                session.setAttribute("shipmentpdfProgress", String.valueOf(0));

                String pdfFullName=basePath+"/upload/pdf/"+(project_no+"_shipment_"+UUID.randomUUID().toString()+".pdf");
//                File file0=new File(pdfFullName);
//                if(!file0.exists()){
//                    file0.createNewFile();
//                }
                System.out.println("pdfFullName"+pdfFullName);
                String templateFullName=request.getSession().getServletContext().getRealPath("/")
                        +"template/shipment_template.xls";
                if(templateFullName.lastIndexOf('/')==-1){
                    templateFullName=templateFullName.replace('\\','/');
                }

                //获取项目的所有shipment信息
                List<HashMap<String,Object>> list=shipmentInfoDao.getShipmentByProjectNo(project_no,beginTime,endTime);

                int PAGESIZE=28;

                int totalCount=list.size();
                List<String>pdfList=new ArrayList<>();

                ArrayList<Label> datalist=new ArrayList<Label>();
                int index=1,row=0;
                WritableCellFormat wcf=null;
                WritableFont wf=null;
               // String path=this.getClass().getClassLoader().getResource("../../").getPath();

                String logoImageFullName=basePath + "/template/img/image002.jpg";
                String fontPath=basePath+"/font/simhei.ttf";
                String copyrightPath=basePath+"/font/simsun.ttc,0";
                try{
                    wf = new WritableFont(WritableFont.createFont("Arial"), 9);
                    wcf= new WritableCellFormat(wf);
                    wcf.setAlignment(Alignment.CENTRE);
                    wcf.setBackground(jxl.format.Colour.RED);
                }catch (Exception e){
                    e.printStackTrace();
                }
                String last_shipment_no="";
                float total_length=0;
                float total_weight=0;
                int   total_count=0;
                session.setAttribute("shipmentpdfProgress", String.valueOf("0"));
                for(int i=0;i<list.size();i++){
                    System.out.println("pipe_no"+list.get(i).get("pipe_no"));
                    String shipment_no=String.valueOf(list.get(i).get("shipment_no"));
                    if(!shipment_no.equals(last_shipment_no)&&last_shipment_no!=("")){
                        total_count=0;
                        total_length=0;
                        total_weight=0;
                    }
                    //判断是否需要换页
                    if(index%PAGESIZE==0||!shipment_no.equals(last_shipment_no)&&last_shipment_no!=("")){
                        index=1;
                        System.out.println("另起一页 last_shipment_no="+last_shipment_no+"shipment_no=+"+shipment_no+"pipe_No="+String.valueOf(list.get(i).get("pipe_no")) );
                        row=0;//另起一页，初始化参数
                        String newPdfName= GenerateExcelToPDFUtil.PDFAutoMation(templateFullName,datalist,pdfFullName,logoImageFullName,fontPath,copyrightPath);
                        datalist.clear();
                        if(newPdfName!=null){
                            pdfList.add(newPdfName);
                            delSetPath.add(newPdfName);
                        }
                    }
                    last_shipment_no=shipment_no;

                    if(index==1){
                        datalist.add(new Label(3, 4, String.valueOf(list.get(i).get("project_name")), wcf));
                        StringBuilder odwtstr = new StringBuilder();
                        odwtstr.append("Φ");
                        odwtstr.append(String.valueOf(list.get(i).get("od")));
                        odwtstr.append("*");
                        odwtstr.append(String.valueOf(list.get(i).get("wt")));
                        odwtstr.append("mm");
                        datalist.add(new Label(8, 4, odwtstr.toString(), wcf));
                        datalist.add(new Label(12, 4, String.valueOf(list.get(i).get("shipment_date")), wcf));
                        StringBuilder coating = new StringBuilder();
                        coating.append(String.valueOf(list.get(i).get("external_coating")));
                        coating.append(" ");
                        coating.append(String.valueOf(list.get(i).get("internal_coating")));
                        datalist.add(new Label(8, 5, coating.toString(), wcf));
                        datalist.add(new Label(3, 5, shipment_no, wcf));
                        datalist.add(new Label(12, 5, String.valueOf(list.get(i).get("vehicle_plate_no")), wcf));
                        datalist.add(new Label(12, 22,"©2018 TopInspector", wcf));
                    }

                    int side=0;
                    if(index>PAGESIZE/2){
                        side=1;
                    }
                    datalist.add(new Label(0+7*side, row+7, String.valueOf(index), wcf));
                    datalist.add(new Label(1+7*side, row+7, String.valueOf(list.get(i).get("pipe_no")), wcf));
                    datalist.add(new Label(2+7*side, row+7, String.valueOf(list.get(i).get("p_length")), wcf));
                    datalist.add(new Label(3+7*side, row+7, String.valueOf(list.get(i).get("weight")), wcf));
                    datalist.add(new Label(4+7*side, row+7, String.valueOf(list.get(i).get("heat_no")), wcf));
                    datalist.add(new Label(5+7*side, row+7, String.valueOf(list.get(i).get("pipe_making_lot_no")), wcf));
                    datalist.add(new Label(6+7*side, row+7,String.valueOf(list.get(i).get("remark")), wcf));
                    total_count+=1;
                    total_length+=(float)list.get(i).get("p_length");
                    total_weight+=(float)list.get(i).get("weight");

                    datalist.add(new Label(4, 21,String.valueOf(total_count), wcf));
                    datalist.add(new Label(8, 21,String.valueOf(total_length), wcf));
                    datalist.add(new Label(12, 21,String.valueOf(total_weight), wcf));


                    index+=1;
                    row+=1;



                    //把用户数据保存在session域对象中
                    float percent=0;
                    if(totalCount!=0)
                        percent=(i+1)*100/totalCount;
                    session.setAttribute("shipmentpdfProgress", String.valueOf(percent));
                    System.out.println("shipmentpdfProgress：" + percent);
                    System.out.println("i：" + i);
                    System.out.println("totalCount：" + totalCount);
                }
                if(datalist.size()>0){

                    String newPdfName= GenerateExcelToPDFUtil.PDFAutoMation(templateFullName,datalist,pdfFullName,logoImageFullName,fontPath,copyrightPath);
                    datalist.clear();
                    if(newPdfName!=null){
                        pdfList.add(newPdfName);
                        delSetPath.add(newPdfName);
                    }
                }


                session.setAttribute("shipmentpdfProgress", String.valueOf("100"));


                List<String>finalpdfList=new ArrayList<>();
                if(pdfList.size()>0){
                    MergePDF.MergePDFs(pdfList,pdfFullName);
                    pdfList.clear();
                    finalpdfList.add(pdfFullName);
                    delSetPath.add(pdfFullName);
                }
                if(finalpdfList.size()==0){
                    success=false;
                    message="没有发运记录";
                }else{
                    success=true;
                    message="存在成品发运记录"+String.valueOf(list.size())+"条";
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


        Map<String,Object> maps=new HashMap<String,Object>();
        maps.put("success",success);
        maps.put("zipName",zipName);
        maps.put("message",message);
        String mmp= JSONArray.toJSONString(maps);

        return mmp;
    }

    //获取PDF生成进度
    @RequestMapping(value="getShipmentpdfProgress",produces="application/json;charset=UTF-8")
    @ResponseBody
    public  String getShipmentpdfProgress(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json=new JSONObject();
        try{

            HttpSession session = request.getSession();
            String shipmentpdfProgress=(String)session.getAttribute("shipmentpdfProgress");
            if(shipmentpdfProgress==null||shipmentpdfProgress.equals(""))
                shipmentpdfProgress="0";
            //跳转到用户主页
            json.put("success",true);
            //System.out.println("ggggggete getAttribute pdfProgress：" + pdfProgress);    //输出程序运行时间
            json.put("shipmentpdfProgress",shipmentpdfProgress);
            ResponseUtil.write(response,json);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }



    //根据项目编号获得发运信息
    @RequestMapping(value="getAllShipmentInfoByProjectNo",produces="application/json;charset=UTF-8")
    @ResponseBody
    public String getAllShipmentInfoByProjectNo(HttpServletRequest request) {
        String project_no=request.getParameter("project_no");
        String page= request.getParameter("page");
        String rows= request.getParameter("rows");
        if(page==null){
            page="1";
        }
        if(rows==null){
            rows="20";
        }
        int start=(Integer.parseInt(page)-1)*Integer.parseInt(rows);
        List<HashMap<String,Object>>list=shipmentInfoDao.getAllShipmentInfoByProjectNo(project_no,start,Integer.parseInt(rows));
        Map<String, Object> maps = new HashMap<String, Object>();
        if (list!=null&&list.size()>0) {
            //是待定状态
            maps.put("success",true);
            maps.put("record",list);
        } else {
            maps.put("success", false);
        }
        String mmp= JSONArray.toJSONString(maps);
        return mmp;
    }




    //根据发运单编号获得发运信息
    @RequestMapping(value="getShipmentInfoByShipmentNo",produces="application/json;charset=UTF-8")
    @ResponseBody
    public String getShipmentInfoByShipmentNo(HttpServletRequest request) {
        String shipment_no=request.getParameter("shipment_no");

        List<HashMap<String,Object>>list=shipmentInfoDao.getShipmentInfoByShipmentNo(shipment_no);
        Map<String, Object> maps = new HashMap<String, Object>();
        if (list!=null&&list.size()>0) {
            maps.put("success",true);
            maps.put("record",list);
            maps.put("message","");
        } else {
            maps.put("success", false);
            maps.put("message","不存在发运单编号"+shipment_no);
        }
        String mmp= JSONArray.toJSONString(maps);
        return mmp;
    }






}
