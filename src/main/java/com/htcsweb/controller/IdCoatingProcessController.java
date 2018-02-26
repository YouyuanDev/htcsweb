package com.htcsweb.controller;



import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.htcsweb.dao.IdCoatingProcessDao;
import com.htcsweb.dao.PipeBasicInfoDao;
import com.htcsweb.entity.IdCoatingProcess;
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
@RequestMapping("/IdCoatOperation")
public class IdCoatingProcessController {
    @Autowired
    private IdCoatingProcessDao idCoatingProcessDao;
    @Autowired
    private PipeBasicInfoDao pipeBasicInfoDao;

    //查询
    @RequestMapping(value = "/getIdCoatingByLike")
    @ResponseBody
    public String getIdCoatingByLike(@RequestParam(value = "pipe_no",required = false)String pipe_no, @RequestParam(value = "operator_no",required = false)String operator_no, @RequestParam(value = "begin_time",required = false)String begin_time, @RequestParam(value = "end_time",required = false)String end_time, HttpServletRequest request){
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
        List<HashMap<String,Object>> list=idCoatingProcessDao.getAllByLike(pipe_no,operator_no,beginTime,endTime,start,Integer.parseInt(rows));
        int count=idCoatingProcessDao.getCountAllByLike(pipe_no,operator_no,beginTime,endTime);
        Map<String,Object> maps=new HashMap<String,Object>();
        maps.put("total",count);
        maps.put("rows",list);
        String mmp= JSONArray.toJSONString(maps);
        return mmp;
    }

    //添加、修改
    @RequestMapping("/saveIdCoatingProcess")
    @ResponseBody
    public String saveIdCoatingProcess(IdCoatingProcess idCoatingProcess, HttpServletRequest request, HttpServletResponse response){
        JSONObject json=new JSONObject();
        try{
            String odcoatprotime= request.getParameter("idcoatprotime");
            String curing_starttime= request.getParameter("curing_starttime");
            String curing_finishtime= request.getParameter("curing_finishtime");
            SimpleDateFormat simFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            int resTotal=0;
            if(odcoatprotime!=null&&odcoatprotime!=""){
                Date new_odbptime = simFormat.parse(odcoatprotime);
                idCoatingProcess.setOperation_time(new_odbptime);
            }else{
                idCoatingProcess.setOperation_time(new Date());
            }
            if(odcoatprotime!=null&&odcoatprotime!=""){
                Date curing_start_time=simFormat.parse(curing_starttime);
                idCoatingProcess.setCuring_start_time(curing_start_time);
            }else{
                idCoatingProcess.setCuring_start_time(new Date());
            }
            if(odcoatprotime!=null&&odcoatprotime!=""){
                Date curing_finish_time=simFormat.parse(curing_finishtime);
                idCoatingProcess.setCuring_finish_time(curing_finish_time);
            }else{
                idCoatingProcess.setCuring_finish_time(new Date());
            }

            String pipeno=idCoatingProcess.getPipe_no();
            if(idCoatingProcess.getId()==0){
                //添加
                resTotal=idCoatingProcessDao.addIdCoatingProcess(idCoatingProcess);
            }else{
                //修改！
                resTotal=idCoatingProcessDao.updateIdCoatingProcess(idCoatingProcess);
            }
            if(resTotal>0){
                //更新管子的状态
                List<PipeBasicInfo> list=pipeBasicInfoDao.getPipeNumber(pipeno);
                if(list.size()>0){
                    PipeBasicInfo p=list.get(0);
                    if(p.getStatus().equals("id2")) {
                        //验证钢管状态为光管
                        if(idCoatingProcess.getResult().equals("1")) {//当合格时才更新钢管状态
                            p.setStatus("id3");
                            int statusRes = pipeBasicInfoDao.updatePipeBasicInfo(p);
                        }else if(idCoatingProcess.getResult().equals("0")){
                            p.setStatus("idrepair1");
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
    //删除
    @RequestMapping("/delIdCoatingProcess")
    public String delIdCoatingProcess(@RequestParam(value = "hlparam")String hlparam,HttpServletResponse response)throws Exception{
        String[]idArr=hlparam.split(",");
        int resTotal=0;
        resTotal=idCoatingProcessDao.delIdCoatingProcess(idArr);
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
