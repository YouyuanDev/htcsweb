package com.htcsweb.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.htcsweb.dao.PipeBasicInfoDao;
import com.htcsweb.dao.PipeStatusDao;
import com.htcsweb.entity.PipeBasicInfo;
import com.htcsweb.entity.PipeStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestParam;
import com.htcsweb.util.ResponseUtil;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.htcsweb.util.ComboxItem;

@Controller
@RequestMapping("/pipeinfo")
public class PipeBasicInfoController {

    @Autowired
    private PipeBasicInfoDao pipeBasicInfoDao;

    @Autowired
    private PipeStatusDao pipeStatusDao;


    @RequestMapping("/getPipeNumber")
    @ResponseBody
    public String getPipeNumber(HttpServletRequest request){
        String pipe_no=request.getParameter("pipe_no");
        List<PipeBasicInfo>list=pipeBasicInfoDao.getPipeNumber(pipe_no);
        String map= JSONObject.toJSONString(list);
        return map;
    }


    @RequestMapping("/getAllPipeStatus")
    @ResponseBody
    public String getAllPipeStatus(HttpServletRequest request){
        List<PipeStatus>list=pipeStatusDao.getAllPipeStatus();
        List<ComboxItem> colist=new ArrayList<ComboxItem>();
        for(int i=0;i<list.size();i++){
            ComboxItem citem= new ComboxItem();
            PipeStatus ps=((PipeStatus)list.get(i));
            citem.id=ps.getStatus_code();
            citem.text= ps.getStatus_code()+"("+ps.getStatus_name()+")";
            colist.add(citem);
        }

        String map= JSONObject.toJSONString(colist);
        System.out.println("========="+map);
        return map;
    }


    //模糊查询Pipe信息列表
    @RequestMapping(value = "/getPipeInfoByLike")
    @ResponseBody
    public String getPipeInfoByLike(@RequestParam(value = "project_no",required = false)String project_no, @RequestParam(value = "contract_no",required = false)String contract_no,@RequestParam(value = "pipe_no",required = false)String pipe_no, HttpServletRequest request){
        String page= request.getParameter("page");
        String rows= request.getParameter("rows");
        if(page==null){
            page="1";
        }
        if(rows==null){
            rows="20";
        }

        int start=(Integer.parseInt(page)-1)*Integer.parseInt(rows);
        List<HashMap<String,Object>>list=pipeBasicInfoDao.getAllByLike(project_no,contract_no,pipe_no,start,Integer.parseInt(rows));
        int count=pipeBasicInfoDao.getCount();

        Map<String,Object> maps=new HashMap<String,Object>();
        maps.put("total",count);
        maps.put("rows",list);
        String mmp= JSONArray.toJSONString(maps);
        return mmp;
    }
    //根据钢管编号异步查询钢管信息
    @RequestMapping(value ="/getPipeInfoByNo")
    public String getPipeInfoByNo(HttpServletResponse response,HttpServletRequest request){
        String pipeno=request.getParameter("pipe_no");
        System.out.println("编号："+pipeno);
        List<HashMap<String,Object>>list=pipeBasicInfoDao.getPipeInfoByNo(pipeno);
        System.out.println("数据"+list.size());
        String mmp="";
        JSONObject object=new JSONObject();

        //ResponseUtil.write(response, object);
        if(list!=null){
//            Map<String,Object> maps=new HashMap<String,Object>();
//            maps.put("pipeinfo",list);
            mmp= JSONArray.toJSONString(list);
        }

        System.out.println(mmp);
        return null;
    }

    //增加或修改Pipe信息
    @RequestMapping(value = "/savePipe")
    @ResponseBody
    public String savePipe(PipeBasicInfo pipeBasicInfo, HttpServletResponse response){
        JSONObject json=new JSONObject();
        try{
            int resTotal=0;


            if(pipeBasicInfo.getId()==0){
                //添加
                resTotal=pipeBasicInfoDao.addPipeBasicInfo(pipeBasicInfo);
            }else{
                //修改！
                resTotal=pipeBasicInfoDao.updatePipeBasicInfo(pipeBasicInfo);
            }
            if(resTotal>0){
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

    //删除pipebasicinfo信息
    @RequestMapping("/delPipe")
    public String delPipe(@RequestParam(value = "hlparam")String hlparam,HttpServletResponse response)throws Exception{
        String[]idArr=hlparam.split(",");
        int resTotal=0;
        resTotal=pipeBasicInfoDao.delPipeBasicInfo(idArr);
        JSONObject json=new JSONObject();
        if(resTotal>0){
            System.out.print("删除成功");
            json.put("success",true);
        }else{
            System.out.print("删除失败");
            json.put("success",false);
        }
        ResponseUtil.write(response,json);
        return null;
    }


}
