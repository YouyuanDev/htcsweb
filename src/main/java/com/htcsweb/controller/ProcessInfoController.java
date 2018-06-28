package com.htcsweb.controller;


import com.alibaba.fastjson.JSONObject;
import com.htcsweb.dao.ProcessInfoDao;
import com.htcsweb.entity.ProcessInfo;
import com.htcsweb.util.ComboxItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
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
        List<ComboxItem> colist=new ArrayList<ComboxItem>();

        for(int i=0;i<list.size();i++){
            ComboxItem citem= new ComboxItem();
            ProcessInfo process=((ProcessInfo)list.get(i));
            citem.id=process.getProcess_code();
            citem.text=process.getProcess_name();
            colist.add(citem);
        }

        String map= JSONObject.toJSONString(colist);
        System.out.println("="+map);
        return map;
    }

}
