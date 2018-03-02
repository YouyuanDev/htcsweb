package com.htcsweb.controller;


import com.alibaba.fastjson.JSONObject;
import com.htcsweb.dao.LabTestingAcceptanceCriteria2FbeDao;
import com.htcsweb.dao.LabTestingAcceptanceCriteria3LpeDao;
import com.htcsweb.util.ComboxItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.htcsweb.entity.LabTestingAcceptanceCriteria2Fbe;
import com.htcsweb.entity.LabTestingAcceptanceCriteria3Lpe;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/LabTestingAcceptanceCriteriaOperation")
public class LabTestingAcceptanceCriteriaController {

    @Autowired
    LabTestingAcceptanceCriteria2FbeDao labTestingAcceptanceCriteria2FbeDao;

    @Autowired
    LabTestingAcceptanceCriteria3LpeDao labTestingAcceptanceCriteria3LpeDao;


    @RequestMapping("/getAllLabTestingAcceptanceCriteria2fbe")
    @ResponseBody
    public String getAllLabTestingAcceptanceCriteria2fbe(@RequestParam(value = "lab_testing_acceptance_criteria_no",required = false)String lab_testing_acceptance_criteria_no, HttpServletRequest request){
        List<LabTestingAcceptanceCriteria2Fbe> list=labTestingAcceptanceCriteria2FbeDao.getAllLabTestingAcceptanceCriteria2Fbe();
        List<ComboxItem> colist=new ArrayList<ComboxItem>();
        for(int i=0;i<list.size();i++){
            ComboxItem citem= new ComboxItem();
            LabTestingAcceptanceCriteria2Fbe ps=((LabTestingAcceptanceCriteria2Fbe)list.get(i));
            citem.id=ps.getLab_testing_acceptance_criteria_no();
            citem.text=ps.getLab_testing_acceptance_criteria_no();
            colist.add(citem);
        }
        String map= JSONObject.toJSONString(colist);
        return map;
    }



    @RequestMapping("/getAllLabTestingAcceptanceCriteria3lpe")
    @ResponseBody
    public String getAllLabTestingAcceptanceCriteria3lpe(@RequestParam(value = "lab_testing_acceptance_criteria_no",required = false)String coating_acceptance_criteria_no, HttpServletRequest request){
        List<LabTestingAcceptanceCriteria3Lpe> list=labTestingAcceptanceCriteria3LpeDao.getAllLabTestingAcceptanceCriteria3Lpe();
        List<ComboxItem> colist=new ArrayList<ComboxItem>();
        for(int i=0;i<list.size();i++){
            ComboxItem citem= new ComboxItem();
            LabTestingAcceptanceCriteria3Lpe ps=((LabTestingAcceptanceCriteria3Lpe)list.get(i));
            citem.id=ps.getLab_testing_acceptance_criteria_no();
            citem.text=ps.getLab_testing_acceptance_criteria_no();
            colist.add(citem);
        }
        String map= JSONObject.toJSONString(colist);
        return map;

    }
}
