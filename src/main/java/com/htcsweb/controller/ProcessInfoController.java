package com.htcsweb.controller;


import com.alibaba.fastjson.JSONObject;
import com.htcsweb.dao.ProcessInfoDao;
import com.htcsweb.entity.ProcessInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/ProcessOperation")
public class ProcessInfoController {


    @Autowired
    private ProcessInfoDao processInfoDao;


    //用于搜索的分厂信息下拉框
    @RequestMapping("/getAllProcess")
    @ResponseBody
    public String getAllProcess(HttpServletRequest request){
        List<ProcessInfo> list=processInfoDao.getAllProcessInfo();

        String map= JSONObject.toJSONString(list);
        System.out.println("="+map);
        return map;
    }


    //用于搜索的分厂信息下拉框 有All选项
    @RequestMapping("/getAllProcessWithAllOption")
    @ResponseBody
    public String getAllProcessWithAllOption(HttpServletRequest request){
        List<ProcessInfo> list=processInfoDao.getAllProcessInfo();

        ProcessInfo p=new ProcessInfo();
        p.setId(0);
        p.setProcess_code("");
        p.setProcess_name("All");
        p.setProcess_name_en("All");
        list.add(0,p);

        String map= JSONObject.toJSONString(list);
        System.out.println("="+map);
        return map;
    }




}
