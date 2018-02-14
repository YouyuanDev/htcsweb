package com.htcsweb.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.htcsweb.dao.ContractInfoDao;
import com.htcsweb.dao.ProjectInfoDao;
import com.htcsweb.entity.ContractInfo;
import com.htcsweb.entity.OdBlastProcess;
import com.htcsweb.entity.ProjectInfo;
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
@RequestMapping("/ContractOperation")
public class ContractController {

    @Autowired
    private ContractInfoDao contractInfoDao;


    //模糊查询contract信息列表
    @RequestMapping(value = "/getContractInfoByLike")
    @ResponseBody
    public String getContractInfoByLike(@RequestParam(value = "project_no",required = false)String project_no, @RequestParam(value = "project_name",required = false)String project_name,@RequestParam(value = "contract_no",required = false)String contract_no, HttpServletRequest request){
        String page= request.getParameter("page");
        String rows= request.getParameter("rows");
        if(page==null){
            page="1";
        }
        if(rows==null){
            rows="20";
        }

        int start=(Integer.parseInt(page)-1)*Integer.parseInt(rows);
        List<HashMap<String,Object>>list=contractInfoDao.getAllByLike(project_no,project_name,contract_no,start,Integer.parseInt(rows));
        int count=contractInfoDao.getCount();

        Map<String,Object> maps=new HashMap<String,Object>();
        maps.put("total",count);
        maps.put("rows",list);
        String mmp= JSONArray.toJSONString(maps);
        System.out.print("mmp:"+mmp);
        return mmp;
    }


    //增加或修改contract信息
    @RequestMapping(value = "/saveContract")
    @ResponseBody
    public String saveContract(ContractInfo contractInfo, HttpServletResponse response){
        System.out.print("saveContract");

        JSONObject json=new JSONObject();
        try{
            int resTotal=0;


            if(contractInfo.getId()==0){
                //添加
                resTotal=contractInfoDao.addContractInfo(contractInfo);

            }else{
                //修改！

                resTotal=contractInfoDao.updateContractInfo(contractInfo);
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

    //删除contract信息
    @RequestMapping("/delContract")
    public String delContract(@RequestParam(value = "hlparam")String hlparam,HttpServletResponse response)throws Exception{
        String[]idArr=hlparam.split(",");
        int resTotal=0;
        resTotal=contractInfoDao.delContractInfo(idArr);
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
