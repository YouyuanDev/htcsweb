package com.htcsweb.controller;

import com.alibaba.fastjson.JSONArray;
import com.htcsweb.dao.DefectInfoDao;
import com.htcsweb.entity.DefectInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;


@Controller
@RequestMapping("/DefectOperation")
public class DefectInfoController {

    @Autowired
    DefectInfoDao defectInfoDao;
    /**
     * 得到所有钢管表面的缺陷种类
     * @param request
     * @return
     */
    @RequestMapping(value = "getAllSteelDefectInfo",produces = "text/plain;charset=utf-8")
    @ResponseBody
    public String getAllSteelDefectInfo(HttpServletRequest request){
        List<DefectInfo> list=defectInfoDao.getAllSteelDefectInfo();
        String mmp= JSONArray.toJSONString(list);
        System.out.println(mmp);
        return mmp;
    }
    /**
     * 得到所有涂层的缺陷种类
     * @param request
     * @return
     */
    @RequestMapping(value = "getAllCoatingDefectInfo",produces = "text/plain;charset=utf-8")
    @ResponseBody
    public String getAllCoatingDefectInfo(HttpServletRequest request){
        List<DefectInfo> list=defectInfoDao.getAllCoatingDefectInfo();
        String mmp= JSONArray.toJSONString(list);
        System.out.println(mmp);
        return mmp;
    }

}
