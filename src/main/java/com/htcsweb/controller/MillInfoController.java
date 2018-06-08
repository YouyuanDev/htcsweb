package com.htcsweb.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.htcsweb.dao.MillInfoDao;
import com.htcsweb.entity.MillInfo;
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
@RequestMapping("/millInfo")
public class MillInfoController {

    @Autowired
    private MillInfoDao millInfoDao;


    //用于搜索的分厂信息下拉框，带All 选项
    @RequestMapping("/getAllMillsWithComboboxSelectAll")
    @ResponseBody
    public String getAllMillsWithComboboxSelectAll(HttpServletRequest request){
        List<MillInfo>list=millInfoDao.getAllMillInfo();
        List<ComboxItem> colist=new ArrayList<ComboxItem>();
        ComboxItem itemall= new ComboxItem();
        itemall.id="";
        itemall.text="All（所有分厂）";
        colist.add(itemall);

        for(int i=0;i<list.size();i++){
            ComboxItem citem= new ComboxItem();
            MillInfo mill=((MillInfo)list.get(i));
            citem.id=mill.getMill_no();
            citem.text= mill.getMill_no()+"("+mill.getMill_name()+")";
            colist.add(citem);
        }

        String map= JSONObject.toJSONString(colist);
        System.out.println("="+map);
        return map;
    }


    //用于搜索的分厂信息下拉框
    @RequestMapping("/getAllMills")
    @ResponseBody
    public String getAllMills(HttpServletRequest request){
        List<MillInfo>list=millInfoDao.getAllMillInfo();
        List<ComboxItem> colist=new ArrayList<ComboxItem>();

        for(int i=0;i<list.size();i++){
            ComboxItem citem= new ComboxItem();
            MillInfo mill=((MillInfo)list.get(i));
            citem.id=mill.getMill_no();
            citem.text= mill.getMill_no()+"("+mill.getMill_name()+")";
            colist.add(citem);
        }

        String map= JSONObject.toJSONString(colist);
        System.out.println("="+map);
        return map;
    }

    //返回mill的所有信息, APP使用
    @RequestMapping("/getAllMillsInfo")
    @ResponseBody
    public String getAllMillsInfo(HttpServletRequest request){
        List<MillInfo>list=millInfoDao.getAllMillInfo();
        Map<String,Object> maps=new HashMap<String,Object>();
        maps.put("success",true);
        maps.put("millinfo",list);
        maps.put("message","返回成功");
        String map= JSONObject.toJSONString(maps);
        System.out.println("="+map);
        return map;
    }



}
