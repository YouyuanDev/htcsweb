package com.htcsweb.controller;



import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.htcsweb.dao.OdCoatingInspectionProcessDao;
import com.htcsweb.dao.PipeBasicInfoDao;
import com.htcsweb.entity.OdCoatingInspectionProcess;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/OdCoatInOperation")
public class OdCoatingInspectionProcessController {
    @Autowired
    private OdCoatingInspectionProcessDao odCoatingInspectionProcessDao;
    @Autowired
    private PipeBasicInfoDao pipeBasicInfoDao;

    //查询
    @RequestMapping(value = "/getOdCoatingInByLike")
    @ResponseBody
    public String getOdCoatingInByLike(@RequestParam(value = "pipe_no",required = false)String pipe_no, @RequestParam(value = "operator_no",required = false)String operator_no, @RequestParam(value = "begin_time",required = false)String begin_time, @RequestParam(value = "end_time",required = false)String end_time,@RequestParam(value = "mill_no",required = false)String mill_no, HttpServletRequest request){
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
        List<HashMap<String,Object>> list=odCoatingInspectionProcessDao.getAllByLike(pipe_no,operator_no,beginTime,endTime,mill_no,start,Integer.parseInt(rows));
        int count=odCoatingInspectionProcessDao.getCountAllByLike(pipe_no,operator_no,beginTime,endTime,mill_no);
        Map<String,Object> maps=new HashMap<String,Object>();
        maps.put("total",count);
        maps.put("rows",list);
        String mmp= JSONArray.toJSONString(maps);
        return mmp;
    }

    //添加、修改
    @RequestMapping("/saveOdCoatingInProcess")
    @ResponseBody
    public String saveOdCoatingInProcess(OdCoatingInspectionProcess odCoatingInspectionProcess, HttpServletRequest request, HttpServletResponse response){
        JSONObject json=new JSONObject();
        try{
            String odcoatInprotime= request.getParameter("odcoatInprotime");
            int resTotal=0;
            if(odCoatingInspectionProcess.getOperation_time()==null){
                SimpleDateFormat simFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date new_odbptime = simFormat.parse(odcoatInprotime);
                odCoatingInspectionProcess.setOperation_time(new_odbptime);
            }
            String pipeno=odCoatingInspectionProcess.getPipe_no();
            if(odCoatingInspectionProcess.getId()==0){
                //添加
                resTotal=odCoatingInspectionProcessDao.addOdCoatingInProcess(odCoatingInspectionProcess);
            }else{
                //修改！
                //System.out.println("sample="+odCoatingInspectionProcess.getIs_sample());
                resTotal=odCoatingInspectionProcessDao.updateOdCoatingInProcess(odCoatingInspectionProcess);
            }
            if(resTotal>0){
                //更新管子的状态
                List<PipeBasicInfo> list=pipeBasicInfoDao.getPipeNumber(pipeno);
                if(list.size()>0){
                    PipeBasicInfo p=list.get(0);
                    if(p.getStatus().equals("od3")) {
                        //验证钢管状态为光管
                        if(odCoatingInspectionProcess.getResult().equals("1")) {//当合格时才更新钢管状态
                            p.setStatus("od4");
                            int statusRes = pipeBasicInfoDao.updatePipeBasicInfo(p);
                        }else if(odCoatingInspectionProcess.getResult().equals("0")){
                            p.setStatus("odrepair1");
                            int statusRes = pipeBasicInfoDao.updatePipeBasicInfo(p);
                        }else if(odCoatingInspectionProcess.getResult().equals("2")){
                            p.setStatus("odstrip1");
                            int statusRes = pipeBasicInfoDao.updatePipeBasicInfo(p);
                        }
                    }
                }
                json.put("success",true);
                json.put("message","保存成功");
            }else{
                json.put("success",false);
                json.put("message","保存失败");
            }
            ResponseUtil.write(response,json);
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
    //删除
    @RequestMapping("/delOdCoatingInProcess")
    public String delOdCoatingInProcess(@RequestParam(value = "hlparam")String hlparam,HttpServletResponse response)throws Exception{
        String[]idArr=hlparam.split(",");
        int resTotal=0;
        resTotal=odCoatingInspectionProcessDao.delOdCoatingInProcess(idArr);
        JSONObject json=new JSONObject();
        if(resTotal>0){
            json.put("success",true);
            json.put("message","删除成功");
        }else{
            json.put("success",false);
            json.put("message","删除失败");
        }
        ResponseUtil.write(response,json);
        return null;
    }
}
