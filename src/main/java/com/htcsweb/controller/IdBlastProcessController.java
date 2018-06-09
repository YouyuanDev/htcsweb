package com.htcsweb.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.htcsweb.dao.IdBlastProcessDao;
import com.htcsweb.dao.InspectionTimeRecordDao;
import com.htcsweb.dao.PipeBasicInfoDao;
import com.htcsweb.entity.IdBlastProcess;
import com.htcsweb.entity.InspectionTimeRecord;
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
@RequestMapping("/IdOperation")
public class IdBlastProcessController {
   
    @Autowired
    private IdBlastProcessDao idBlastProcessDao;

    @Autowired
    private PipeBasicInfoDao pipeBasicInfoDao;

    @Autowired
    private InspectionTimeRecordDao inspectionTimeRecordDao;

    @RequestMapping("/saveIdBlastProcess")
    @ResponseBody
    public String saveIdBlastProcess(IdBlastProcess idBlastProcess, HttpServletRequest request, HttpServletResponse response){
        JSONObject json=new JSONObject();
        try{
            String odbptime= request.getParameter("idbptime");
            int resTotal=0;
            if(odbptime!=null&&odbptime!=""){
                SimpleDateFormat simFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date new_odbptime = simFormat.parse(odbptime);
                idBlastProcess.setOperation_time(new_odbptime);
            }else{
                idBlastProcess.setOperation_time(new Date());
            }
            String pipeno=idBlastProcess.getPipe_no();
            if(idBlastProcess.getId()==0){
                //添加
                resTotal=idBlastProcessDao.addIdBlastProcess(idBlastProcess);
                List<HashMap<String,Object>>list=pipeBasicInfoDao.getPipeInfoByNo(idBlastProcess.getPipe_no());
                String project_no="";
                String mill_no=idBlastProcess.getMill_no();
                if(list.size()>0){
                    project_no=(String)list.get(0).get("project_no");
                }
                System.out.println("project_no="+project_no);

                //更新增量 inspectionTimeMap
                if (idBlastProcess.getAbrasive_conductivity()!=-99) {
                    List<InspectionTimeRecord> lt=inspectionTimeRecordDao.getRecordByProjectNoMillNo(project_no,mill_no,"id_abrasive_conductivity_freq");
                    if(lt.size()>0) {
                        InspectionTimeRecord itr=lt.get(0);
                        itr.setInspction_time(idBlastProcess.getOperation_time());
                        inspectionTimeRecordDao.updateInspectionTimeRecord(itr);
                    }else{
                        InspectionTimeRecord itr=new InspectionTimeRecord();
                        itr.setProject_no(project_no);
                        itr.setMill_no(mill_no);
                        itr.setInspection_item("id_abrasive_conductivity_freq");
                        itr.setInspction_time(idBlastProcess.getOperation_time());
                        inspectionTimeRecordDao.addInspectionTimeRecord(itr);
                    }
                }
                if (idBlastProcess.getSalt_contamination_before_blasting()!=-99) {
                    List<InspectionTimeRecord> lt=inspectionTimeRecordDao.getRecordByProjectNoMillNo(project_no,mill_no,"id_salt_contamination_before_blast_freq");
                    if(lt.size()>0) {
                        InspectionTimeRecord itr=lt.get(0);
                        itr.setInspction_time(idBlastProcess.getOperation_time());
                        inspectionTimeRecordDao.updateInspectionTimeRecord(itr);
                    }else{
                        InspectionTimeRecord itr=new InspectionTimeRecord();
                        itr.setProject_no(project_no);
                        itr.setMill_no(mill_no);
                        itr.setInspection_item("id_salt_contamination_before_blast_freq");
                        itr.setInspction_time(idBlastProcess.getOperation_time());
                        inspectionTimeRecordDao.addInspectionTimeRecord(itr);
                    }
                }


            }else{
                //修改！
                resTotal=idBlastProcessDao.updateIdBlastProcess(idBlastProcess);
            }
            if(resTotal>0){
                //更新管子的状态
                List<PipeBasicInfo> list=pipeBasicInfoDao.getPipeNumber(pipeno);
                if(list.size()>0){
                    PipeBasicInfo p=list.get(0);
                    if(p.getStatus().equals("odstockin")||p.getStatus().equals("bare2")) {
                        //验证钢管状态是否是成品入库或者外防腐终检完成   bare2,odstockin   内喷砂工序    0:bare2     1:id1   10:bare2 or odstockin  3： onhold
                        if(idBlastProcess.getResult().equals("1")) {//当合格时才更新钢管状态
                            p.setStatus("id1");
                            p.setLast_accepted_status(p.getStatus());
                        }
                        else if(idBlastProcess.getResult().equals("3")) {//当需要修磨或切除时，设置为onhold状态
                            p.setStatus("onhold");
                        }
                        int statusRes = pipeBasicInfoDao.updatePipeBasicInfo(p);
                    }

                }
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
    @RequestMapping("/delIdBlastProcess")
    public String delIdBlastProcess(@RequestParam(value = "hlparam")String hlparam,HttpServletResponse response)throws Exception{
        String[]idArr=hlparam.split(",");
        int resTotal=0;
        resTotal=idBlastProcessDao.delIdBlastProcess(idArr);
        JSONObject json=new JSONObject();
        StringBuilder sbmessage = new StringBuilder();
        sbmessage.append("总共");
        sbmessage.append(Integer.toString(resTotal));
        sbmessage.append("项内喷砂信息删除成功\n");
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

    @RequestMapping(value = "/getIdBlastByLike")
    @ResponseBody
    public String getIdBlastByLike(@RequestParam(value = "pipe_no",required = false)String pipe_no, @RequestParam(value = "operator_no",required = false)String operator_no, @RequestParam(value = "begin_time",required = false)String begin_time, @RequestParam(value = "end_time",required = false)String end_time, @RequestParam(value = "mill_no",required = false)String mill_no, HttpServletRequest request){
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
        List<HashMap<String,Object>>list=idBlastProcessDao.getAllByLike(pipe_no,operator_no,beginTime,endTime,mill_no,start,Integer.parseInt(rows));
        int count=idBlastProcessDao.getCountAllByLike(pipe_no,operator_no,beginTime,endTime,mill_no);
        Map<String,Object> maps=new HashMap<String,Object>();
        maps.put("total",count);
        maps.put("rows",list);
        String mmp= JSONArray.toJSONString(maps);
        return mmp;
    }

}
