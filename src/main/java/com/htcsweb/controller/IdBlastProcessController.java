package com.htcsweb.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.htcsweb.dao.IdBlastProcessDao;
import com.htcsweb.dao.PipeBasicInfoDao;
import com.htcsweb.entity.IdBlastProcess;
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
                        //验证钢管状态是否是成品入库或者外防腐终检完成
                        if(idBlastProcess.getResult().equals("1")) {//当合格时才更新钢管状态
                            p.setStatus("id1");
                            int statusRes = pipeBasicInfoDao.updatePipeBasicInfo(p);
                        }
                        else if(idBlastProcess.getResult().equals("3")) {//当需要修磨或切除时，设置为onhold状态
                            p.setStatus("onhold");
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
