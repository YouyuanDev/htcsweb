package com.htcsweb.controller;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


import com.htcsweb.entity.CoatingAcceptanceCriteria;
import com.htcsweb.util.ComboxItem;
import com.htcsweb.dao.CoatingAcceptanceCriteriaDao;
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
import java.util.*;

@Controller
@RequestMapping("/AcceptanceCriteriaOperation")
public class AcceptanceCriteriaController {


    @Autowired
    private CoatingAcceptanceCriteriaDao coatingacceptancecriteriaDao;

    @RequestMapping("/getAllAcceptanceCriteria")
    @ResponseBody
    public String getAllAcceptanceCriteria(HttpServletRequest request){
        List<CoatingAcceptanceCriteria>list=coatingacceptancecriteriaDao.getAllAcceptanceCriteria();
        List<ComboxItem> colist=new ArrayList<ComboxItem>();
        for(int i=0;i<list.size();i++){
            ComboxItem citem= new ComboxItem();
            CoatingAcceptanceCriteria ps=((CoatingAcceptanceCriteria)list.get(i));
            citem.id=ps.getCoating_acceptance_criteria_no();
            citem.text=ps.getCoating_acceptance_criteria_no();

            colist.add(citem);
        }

        String map= JSONObject.toJSONString(colist);
        System.out.println("========="+map);
        return map;
    }

}
