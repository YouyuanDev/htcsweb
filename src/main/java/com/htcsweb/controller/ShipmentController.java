package com.htcsweb.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.htcsweb.dao.PushEventRuleDao;
import com.htcsweb.dao.RoleDao;
import com.htcsweb.dao.ShipmentInfoDao;
import com.htcsweb.entity.PushEventRule;
import com.htcsweb.entity.Role;
import com.htcsweb.entity.ShipmentInfo;
import com.htcsweb.util.ResponseUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/ShipmentOperation")
public class ShipmentController {


        @Autowired
        private ShipmentInfoDao shipmentInfoDao;



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
                if(shipmentInfo.getId()==0){
                    //添加
                    resTotal=shipmentInfoDao.addShipmentInfo(shipmentInfo);

                }else{
                    //修改！

                    resTotal=shipmentInfoDao.updateShipmentInfo(shipmentInfo);
                }
                if(resTotal>0){
                    json.put("success",true);
                    json.put("message","保存成功");
                }else{
                    json.put("success",false);
                    json.put("message","保存失败");
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



}
