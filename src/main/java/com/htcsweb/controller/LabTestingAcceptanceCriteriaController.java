package com.htcsweb.controller;


import com.alibaba.fastjson.JSONObject;
import com.htcsweb.entity.ODCoatingAcceptanceCriteria;
import com.htcsweb.util.ComboxItem;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/LabTestingAcceptanceCriteriaOperation")
public class LabTestingAcceptanceCriteriaController {




    @RequestMapping("/getAllLabTestingAcceptanceCriteria2fbe")
    @ResponseBody
    public String getAllLabTestingAcceptanceCriteria2fbe(@RequestParam(value = "coating_acceptance_criteria_no",required = false)String coating_acceptance_criteria_no, HttpServletRequest request){
//        List<ODCoatingAcceptanceCriteria> list=odcoatingacceptancecriteriaDao.getAllODAcceptanceCriteria();
//        List<ComboxItem> colist=new ArrayList<ComboxItem>();
//        for(int i=0;i<list.size();i++){
//            ComboxItem citem= new ComboxItem();
//            ODCoatingAcceptanceCriteria ps=((ODCoatingAcceptanceCriteria)list.get(i));
//            citem.id=ps.getCoating_acceptance_criteria_no();
//            citem.text=ps.getCoating_acceptance_criteria_no();
//            colist.add(citem);
//        }
//        String map= JSONObject.toJSONString(colist);
        //return map;
        return "";
    }



    @RequestMapping("/getAllLabTestingAcceptanceCriteria3lpe")
    @ResponseBody
    public String getAllLabTestingAcceptanceCriteria3lpe(@RequestParam(value = "coating_acceptance_criteria_no",required = false)String coating_acceptance_criteria_no, HttpServletRequest request){
//        List<ODCoatingAcceptanceCriteria> list=odcoatingacceptancecriteriaDao.getAllODAcceptanceCriteria();
//        List<ComboxItem> colist=new ArrayList<ComboxItem>();
//        for(int i=0;i<list.size();i++){
//            ComboxItem citem= new ComboxItem();
//            ODCoatingAcceptanceCriteria ps=((ODCoatingAcceptanceCriteria)list.get(i));
//            citem.id=ps.getCoating_acceptance_criteria_no();
//            citem.text=ps.getCoating_acceptance_criteria_no();
//            colist.add(citem);
//        }
//        String map= JSONObject.toJSONString(colist);
        //return map;
        return "";
    }
}
