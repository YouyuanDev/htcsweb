package com.htcsweb.controller;


import com.alibaba.fastjson.JSONObject;
import com.htcsweb.dao.OdBlastProcessDao;
import com.htcsweb.dao.PipeBasicInfoDao;
import com.htcsweb.entity.PipeBasicInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/pipeinfo")
public class PipeBasicInfoController {
    @Autowired
    private PipeBasicInfoDao pipeBasicInfoDao;

    @RequestMapping("/getPipeNumber")
    @ResponseBody
    public String getPipeNumber(){
        List<PipeBasicInfo>list=pipeBasicInfoDao.getPipeNumber();
        String map= JSONObject.toJSONString(list);
        return map;
    }
}
