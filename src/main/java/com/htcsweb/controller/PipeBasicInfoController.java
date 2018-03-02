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
    @RequestMapping("/getPipeNumbers")
    @ResponseBody
    public String getPipeNumbers(HttpServletRequest request){
        String map="";
        try{
            String pipe_no=request.getParameter("pipe_no");
            String pipestatus=request.getParameter("pipestatus");
            String[]idArr={};
            if(pipestatus!=null){
                idArr=pipestatus.split(",");
            }
            List<PipeBasicInfo>list=pipeBasicInfoDao.getPipeNumbers(pipe_no,idArr);
            map= JSONObject.toJSONString(list);
        }catch (Exception e){
        }
        return map;
    }
    @RequestMapping("/getPipeNumberBySample")
    @ResponseBody
    public String getPipeNumberBySample(HttpServletRequest request){
        String map="";
        try{
            String pipe_no=request.getParameter("pipe_no");
            List<PipeBasicInfo>list=pipeBasicInfoDao.getPipeNumberBySample(pipe_no);
            map= JSONObject.toJSONString(list);
        }catch (Exception e){
        }
        return map;
    }


    //用于搜索的pipe状态下拉框，带All 选项
    @RequestMapping("/getAllPipeStatusWithComboboxSelectAll")
    @ResponseBody
    public String getAllPipeStatusWithComboboxSelectAll(HttpServletRequest request){
        List<PipeStatus>list=pipeStatusDao.getAllPipeStatus();
        List<ComboxItem> colist=new ArrayList<ComboxItem>();
        ComboxItem itemall= new ComboxItem();
        itemall.id="";
        itemall.text="All（所有状态）";
        colist.add(itemall);

        for(int i=0;i<list.size();i++){
            ComboxItem citem= new ComboxItem();
            PipeStatus ps=((PipeStatus)list.get(i));
            citem.id=ps.getStatus_code();
            citem.text= ps.getStatus_code()+"("+ps.getStatus_name()+")";
            colist.add(citem);
        }

        String map= JSONObject.toJSONString(colist);
        //System.out.println("========="+map);
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
        //System.out.println("========="+map);
        return map;
    }


    //模糊查询Pipe信息列表
    @RequestMapping(value = "/getPipeInfoByLike")
    @ResponseBody
    public String getPipeInfoByLike(@RequestParam(value = "project_no",required = false)String project_no, @RequestParam(value = "contract_no",required = false)String contract_no,@RequestParam(value = "pipe_no",required = false)String pipe_no, @RequestParam(value = "status",required = false)String status,HttpServletRequest request){
        String page= request.getParameter("page");
        String rows= request.getParameter("rows");
        if(page==null){
            page="1";
        }
        if(rows==null){
            rows="20";
        }

        int start=(Integer.parseInt(page)-1)*Integer.parseInt(rows);
        List<HashMap<String,Object>>list=pipeBasicInfoDao.getAllByLike(project_no,contract_no,pipe_no,status,start,Integer.parseInt(rows));
        int count=pipeBasicInfoDao.getCountAllByLike(project_no,contract_no,pipe_no,status);

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
        List<HashMap<String,Object>>list=pipeBasicInfoDao.getPipeInfoByNo(pipeno);
        String mmp= JSON.toJSONString(list);
        try{
            ResponseUtil.write(response, mmp);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

//

    //模糊查询OD ID 光管的Pipe信息列表
    @RequestMapping(value ="/getODIDBarePipeInfoByLike")
    @ResponseBody
    public String getODIDBarePipeInfoByLike(@RequestParam(value = "project_no",required = false)String project_no, @RequestParam(value = "contract_no",required = false)String contract_no,@RequestParam(value = "pipe_no",required = false)String pipe_no, @RequestParam(value = "status",required = false)String status,HttpServletRequest request){
        String page= request.getParameter("page");
        String rows= request.getParameter("rows");
        if(page==null){
            page="1";
        }
        if(rows==null){
            rows="20";
        }
        if(status==null||status.equals("")){
            status="bare1";
        }
        //System.out.println("钢管状态："+status);
        int start=(Integer.parseInt(page)-1)*Integer.parseInt(rows);
        List<HashMap<String,Object>>list=pipeBasicInfoDao.getODIDBarePipeInfoByLike(project_no,contract_no,pipe_no,status,start,Integer.parseInt(rows));
        int count=pipeBasicInfoDao.getCountODIDBarePipeInfoByLike(project_no,contract_no,pipe_no,status);

        Map<String,Object> maps=new HashMap<String,Object>();
        maps.put("total",count);
        maps.put("rows",list);
        //System.out.println("结果集："+list.toString());
        //System.out.println("结果个数："+list.toString());
        String mmp= JSONArray.toJSONString(maps);
        return mmp;
    }




    //模糊查询外防成品、可出库的Pipe信息列表
    @RequestMapping(value ="/getODInspectedPipeInfoByLike")
    @ResponseBody
    public String getODInspectedPipeInfoByLike(@RequestParam(value = "project_no",required = false)String project_no, @RequestParam(value = "contract_no",required = false)String contract_no,@RequestParam(value = "pipe_no",required = false)String pipe_no, HttpServletRequest request){
        String page= request.getParameter("page");
        String rows= request.getParameter("rows");
        if(page==null){
            page="1";
        }
        if(rows==null){
            rows="20";
        }

        int start=(Integer.parseInt(page)-1)*Integer.parseInt(rows);
        List<HashMap<String,Object>>list=pipeBasicInfoDao.getODInspectedPipeInfoByLike(project_no,contract_no,pipe_no,start,Integer.parseInt(rows));
        int count=pipeBasicInfoDao.getCountODInspectedPipeInfoByLike(project_no,contract_no,pipe_no);

        Map<String,Object> maps=new HashMap<String,Object>();
        maps.put("total",count);
        maps.put("rows",list);
        String mmp= JSONArray.toJSONString(maps);
        return mmp;
    }


    //模糊查询内防成品、可入成品库的Pipe信息列表
    @RequestMapping(value ="/getIDInspectedPipeInfoByLike")
    @ResponseBody
    public String getIDInspectedPipeInfoByLike(@RequestParam(value = "project_no",required = false)String project_no, @RequestParam(value = "contract_no",required = false)String contract_no,@RequestParam(value = "pipe_no",required = false)String pipe_no, HttpServletRequest request){
        String page= request.getParameter("page");
        String rows= request.getParameter("rows");
        if(page==null){
            page="1";
        }
        if(rows==null){
            rows="20";
        }

        int start=(Integer.parseInt(page)-1)*Integer.parseInt(rows);
        List<HashMap<String,Object>>list=pipeBasicInfoDao.getIDInspectedPipeInfoByLike(project_no,contract_no,pipe_no,start,Integer.parseInt(rows));
        int count=pipeBasicInfoDao.getCountIDInspectedPipeInfoByLike(project_no,contract_no,pipe_no);

        Map<String,Object> maps=new HashMap<String,Object>();
        maps.put("total",count);
        maps.put("rows",list);
        String mmp= JSONArray.toJSONString(maps);
        return mmp;
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
            //System.out.print("删除成功");
            json.put("success",true);
        }else{
            //System.out.print("删除失败");
            json.put("success",false);
        }
        ResponseUtil.write(response,json);
        return null;
    }


    //外访成品管入库
    @RequestMapping("/odproductstockin")
    public String odproductstockin(@RequestParam(value = "hlparam")String hlparam,HttpServletResponse response)throws Exception{
        String[]idArr=hlparam.split(",");
        int resTotal=0;
        resTotal=pipeBasicInfoDao.odProductStockin(idArr);
        JSONObject json=new JSONObject();
        if(resTotal>0){
            System.out.print("外防入库成功");
            json.put("success",true);
        }else{
            System.out.print("外防入库失败");
            json.put("success",false);
        }
        ResponseUtil.write(response,json);
        return null;
    }


    //内访成品管入库
    @RequestMapping("/idproductstockin")
    public String idproductstockin(@RequestParam(value = "hlparam")String hlparam,HttpServletResponse response)throws Exception{
        String[]idArr=hlparam.split(",");
        int resTotal=0;
        resTotal=pipeBasicInfoDao.idProductStockin(idArr);
        JSONObject json=new JSONObject();
        if(resTotal>0){
           // System.out.print("内防入库成功");
            json.put("success",true);
        }else{
           // System.out.print("内防入库成功");
            json.put("success",false);
        }
        ResponseUtil.write(response,json);
        return null;
    }


    //内防光管（必须无外防处理记录）、外防扒皮管转外防光管库
    @RequestMapping("/SetPipeTOODBare")
    public String SetPipeTOODBare(@RequestParam(value = "hlparam")String hlparam,HttpServletResponse response)throws Exception{
        String[]idArr=hlparam.split(",");

        ArrayList<String> list = new ArrayList<String>();
        StringBuilder sbmessage = new StringBuilder();
        for(int i=0;i<idArr.length;i++){

            int res=pipeBasicInfoDao.isPipeODProcessed(idArr[i]);
            if(res==0){
                list.add(idArr[i]);
            }else{
                //需要剔除的钢管id
                sbmessage.append("钢管:");
                sbmessage.append(idArr[i]);
                sbmessage.append("已存在外防生产信息，无法转外防光管 ");
            }
        }
        int resTotal=0;
        if(list.size()>0) {
            String[] array = new String[list.size()];
            String[] newidArr = list.toArray(array);
            //System.out.println("newidArr=" + newidArr[0]);

            resTotal = pipeBasicInfoDao.SetToODBare(newidArr);
        }
        JSONObject json=new JSONObject();
        json.put("message",sbmessage.toString());
        if(resTotal>0){
            //System.out.print("转外防光管库成功");
            json.put("success",true);
        }else{
            //System.out.print("转外防光管库成功");
            json.put("success",false);
        }
        ResponseUtil.write(response,json);
        return null;
    }

    //外防光管（必须无内防处理记录）、内防扒皮管转内防光管库
    @RequestMapping("/SetPipeTOIDBare")
    public String SetPipeTOIDBare(@RequestParam(value = "hlparam")String hlparam,HttpServletResponse response)throws Exception{
        String[]idArr=hlparam.split(",");
        ArrayList<String> list = new ArrayList<String>();
        StringBuilder sbmessage = new StringBuilder();
        for(int i=0;i<idArr.length;i++){
            //System.out.println("id="+idArr[i]);
            int res=pipeBasicInfoDao.isPipeIDProcessed(idArr[i]);
            if(res==0){
                //System.out.println("res==0");
                list.add(idArr[i]);
            }else{
                //需要剔除的钢管id
                sbmessage.append("钢管:");
                sbmessage.append(idArr[i]);
                sbmessage.append("已存在内防生产信息，无法转内防光管 ");
            }
        }
        int resTotal=0;
        if(list.size()>0) {
            String[] array = new String[list.size()];
            String[] newidArr = list.toArray(array);
            //System.out.println("newidArr=" + newidArr[0]);

            resTotal = pipeBasicInfoDao.SetToIDBare(newidArr);
        }
        JSONObject json=new JSONObject();
        json.put("message",sbmessage.toString());
        if(resTotal>0){
            System.out.print("转内防光管库成功");
            json.put("success",true);
        }else{
            System.out.print("转内防光管库失败");
            json.put("success",false);

        }
        ResponseUtil.write(response,json);
        return null;
    }







    //模糊查询内防外防成品可出厂的Pipe信息列表
    @RequestMapping(value ="/getCoatedStockinPipeInfoByLike")
    @ResponseBody
    public String getCoatedStockinPipeInfoByLike(@RequestParam(value = "project_no",required = false)String project_no, @RequestParam(value = "contract_no",required = false)String contract_no,@RequestParam(value = "pipe_no",required = false)String pipe_no,@RequestParam(value = "status",required = false)String status, HttpServletRequest request){
        String page= request.getParameter("page");
        String rows= request.getParameter("rows");
        if(page==null){
            page="1";
        }
        if(rows==null){
            rows="20";
        }
        if(status==null||status.equals("")){
            status="odstockin";
        }

        int start=(Integer.parseInt(page)-1)*Integer.parseInt(rows);
        List<HashMap<String,Object>>list=pipeBasicInfoDao.getCoatedStockinPipeInfoByLike(project_no,contract_no,pipe_no,status,start,Integer.parseInt(rows));
        int count=pipeBasicInfoDao.getCountCoatedStockinPipeInfoByLike(project_no,contract_no,pipe_no,status);

        Map<String,Object> maps=new HashMap<String,Object>();
        maps.put("total",count);
        maps.put("rows",list);
        String mmp= JSONArray.toJSONString(maps);
        return mmp;
    }
    //成品管出厂
    @RequestMapping("/coatingProductStockout")
    public String coatingProductStockout(@RequestParam(value = "hlparam")String hlparam,HttpServletResponse response)throws Exception{
        String[]idArr=hlparam.split(",");
        int resTotal=0;
        resTotal=pipeBasicInfoDao.coatingProductStockout(idArr);
        JSONObject json=new JSONObject();
        if(resTotal>0){
            //System.out.print("涂层成品管出厂成功");
            json.put("success",true);
        }else{
            //System.out.print("涂层成品管出厂失败");
            json.put("success",false);
        }
        ResponseUtil.write(response,json);
        return null;
    }

}
