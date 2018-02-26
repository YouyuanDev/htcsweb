package com.htcsweb.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.htcsweb.dao.IdBlastInspectionProcessDao;
import com.htcsweb.dao.PipeBasicInfoDao;
import com.htcsweb.entity.IdBlastInspectionProcess;
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
@RequestMapping("/IdBlastInspectionOperation")
public class IdBlastInspectionProcessController {

    @Autowired
    private PipeBasicInfoDao pipeBasicInfoDao;
    @Autowired
    private IdBlastInspectionProcessDao idBlastInspectionProcessDao;

    @RequestMapping(value = "/getIdBlastInspectionByLike")
    @ResponseBody
    public String getIdBlastInspectionByLike(@RequestParam(value = "pipe_no",required = false)String pipe_no, @RequestParam(value = "operator_no",required = false)String operator_no, @RequestParam(value = "begin_time",required = false)String begin_time, @RequestParam(value = "end_time",required = false)String end_time, HttpServletRequest request){
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
        List<HashMap<String,Object>>list=idBlastInspectionProcessDao.getAllByLike(pipe_no,operator_no,beginTime,endTime,start,Integer.parseInt(rows));
        int count=idBlastInspectionProcessDao.getCountAllByLike(pipe_no,operator_no,beginTime,endTime);
        Map<String,Object> maps=new HashMap<String,Object>();
        maps.put("total",count);
        maps.put("rows",list);
        String mmp= JSONArray.toJSONString(maps);
        //System.out.print("mmp:"+mmp);
        return mmp;
    }



    //保存外打砂检验数据
    @RequestMapping("/saveIdBlastInspectionProcess")
    @ResponseBody
    public String saveIdBlastInspectionProcess(IdBlastInspectionProcess idBlastInspectionProcess, HttpServletRequest request, HttpServletResponse response){
        JSONObject json=new JSONObject();
        try{
            String odbptime= request.getParameter("idbptime");
            int resTotal=0;
            //System.out.println("odbptime="+odbptime);

            if(odbptime!=null&&odbptime!=""){
                SimpleDateFormat simFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date new_odbptime = simFormat.parse(odbptime);
                idBlastInspectionProcess.setOperation_time(new_odbptime);
            }else{
                idBlastInspectionProcess.setOperation_time(new Date());
            }
            String pipeno=idBlastInspectionProcess.getPipe_no();
            if(idBlastInspectionProcess.getId()==0){
                //添加
                resTotal=idBlastInspectionProcessDao.addIdBlastInProcess(idBlastInspectionProcess);
            }else{
                //修改！
                System.out.println("ID"+idBlastInspectionProcess.getId());
                resTotal=idBlastInspectionProcessDao.updateIdBlastInProcess(idBlastInspectionProcess);
            }
            if(resTotal>0){
                //更新管子的状态
                List<PipeBasicInfo> list=pipeBasicInfoDao.getPipeNumber(pipeno);
                if(list.size()>0){
                    PipeBasicInfo p=list.get(0);
                    if(p.getStatus().equals("id1")) {
                        //验证钢管状态为内打砂完成
                        if(idBlastInspectionProcess.getResult().equals("1")) {//当打砂检验合格时才更新钢管状态
                            p.setStatus("id2");
                            int statusRes = pipeBasicInfoDao.updatePipeBasicInfo(p);
                        }else if(idBlastInspectionProcess.getResult().equals("0")) {
                            p.setStatus("bare2");
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


    @RequestMapping("/delIdBlastInspectionProcess")
    public String delIdBlastInspectionProcess(@RequestParam(value = "hlparam")String hlparam,HttpServletResponse response)throws Exception{
        String[]idArr=hlparam.split(",");
        int resTotal=0;
        resTotal=idBlastInspectionProcessDao.delIdBlastInspectionProcess(idArr);
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
