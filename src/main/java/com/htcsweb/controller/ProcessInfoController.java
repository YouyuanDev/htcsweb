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
    /**
     * 获取分厂信息(下拉框使用),不带ALL选项
     * @param request
     * @return
     */
    @RequestMapping("/getAllProcess")
    @ResponseBody
    public String getAllProcess(HttpServletRequest request){
        List<ProcessInfo> list=processInfoDao.getAllProcessInfo();
        String map= JSONObject.toJSONString(list);
        return map;
    }
    /**
     * 获取分厂信息(下拉框使用),带ALL选项
     * @param request
     * @return
     */
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
        return map;
    }

}
