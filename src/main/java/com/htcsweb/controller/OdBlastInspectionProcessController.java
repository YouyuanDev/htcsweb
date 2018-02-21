package com.htcsweb.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.htcsweb.dao.OdBlastInspectionProcessDao;
import com.htcsweb.dao.PipeBasicInfoDao;
import com.htcsweb.entity.OdBlastInspectionProcess;
import com.htcsweb.entity.PipeBasicInfo;

import com.htcsweb.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/OdBlastInspectionOperation")
public class OdBlastInspectionProcessController {

    @Autowired
    private PipeBasicInfoDao pipeBasicInfoDao;
    @Autowired
    private OdBlastInspectionProcessDao odBlastInspectionProcessDao;

    @RequestMapping(value = "/getOdBlastInspectionByLike")
    @ResponseBody
    public String getOdBlastInspectionByLike(@RequestParam(value = "pipe_no",required = false)String pipe_no, @RequestParam(value = "operator_no",required = false)String operator_no, @RequestParam(value = "begin_time",required = false)String begin_time, @RequestParam(value = "end_time",required = false)String end_time, HttpServletRequest request){
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
        List<HashMap<String,Object>>list=odBlastInspectionProcessDao.getAllByLike(pipe_no,operator_no,beginTime,endTime,start,Integer.parseInt(rows));
        int count=odBlastInspectionProcessDao.getCount();
        Map<String,Object> maps=new HashMap<String,Object>();
        maps.put("total",count);
        maps.put("rows",list);
        String mmp= JSONArray.toJSONString(maps);
        System.out.print("mmp:"+mmp);
        return mmp;
    }



    //保存外打砂检验数据
    @RequestMapping("/saveOdBlastInspectionProcess")
    @ResponseBody
    public String saveOdBlastInspectionProcess(OdBlastInspectionProcess odblastinspectionprocess, HttpServletRequest request, HttpServletResponse response){
        JSONObject json=new JSONObject();
        try{
            String odbptime= request.getParameter("odbptime");
            int resTotal=0;
            //System.out.println("odbptime="+odbptime);

            if(odbptime!=null&&odbptime!=""){
                SimpleDateFormat simFormat = new SimpleDateFormat("MM/dd/yyyy,hh:mm:ss");
                Date new_odbptime = simFormat.parse(odbptime);
                odblastinspectionprocess.setOperation_time(new_odbptime);
            }else{
                odblastinspectionprocess.setOperation_time(new Date());
            }
            String pipeno=odblastinspectionprocess.getPipe_no();
            if(odblastinspectionprocess.getId()==0){
                //添加
                resTotal=odBlastInspectionProcessDao.addOdBlastInProcess(odblastinspectionprocess);
            }else{
                //修改！
                System.out.print("操作工编号是:"+odblastinspectionprocess.getOperator_no());
                resTotal=odBlastInspectionProcessDao.updateOdBlastInProcess(odblastinspectionprocess);
            }
            if(resTotal>0){
                //更新管子的状态
                List<PipeBasicInfo> list=pipeBasicInfoDao.getPipeNumber(pipeno);
                if(list.size()>0){
                    PipeBasicInfo p=list.get(0);
                    if(p.getStatus().equals("od1")) {
                        //验证钢管状态为光管
                        if(odblastinspectionprocess.getResult().equals("1")) {//当打砂检验合格时才更新钢管状态
                            p.setStatus("od2");
                            int statusRes = pipeBasicInfoDao.updatePipeBasicInfo(p);
                        }
                        else if(odblastinspectionprocess.getResult().equals("0")){//打砂检验不合格，改变状态为bare1，重新打砂处理
                            p.setStatus("bare1");
                            int statusRes = pipeBasicInfoDao.updatePipeBasicInfo(p);

                        }
                    }

                }
                json.put("success",true);
            }else{
                json.put("success",false);
            }
            ResponseUtil.write(response,json);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    @RequestMapping("/delOdBlastInspectionProcess")
    public String delOdBlastInspectionProcess(@RequestParam(value = "hlparam")String hlparam,HttpServletResponse response)throws Exception{
        String[]idArr=hlparam.split(",");
        int resTotal=0;
        resTotal=odBlastInspectionProcessDao.delOdBlastInspectionProcess(idArr);
        JSONObject json=new JSONObject();
        if(resTotal>0){
            json.put("success",true);
        }else{
            json.put("success",false);
        }
        ResponseUtil.write(response,json);
        return null;
    }




}
