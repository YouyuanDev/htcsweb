package com.htcsweb.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.htcsweb.entity.RawMaterialTestingAcceptanceCriteria2Fbe;
import com.htcsweb.dao.RawMaterialTestingAcceptanceCriteria2FbeDao;
import com.htcsweb.entity.RawMaterialTestingAcceptanceCriteria3Lpe;
import com.htcsweb.dao.RawMaterialTestingAcceptanceCriteria3LpeDao;
import com.htcsweb.util.ComboxItem;
import com.htcsweb.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;


@Controller
@RequestMapping("/rawMaterialACOperation")
public class RawMaterialTestingAcceptanceCriteriaController {

    @Autowired
    RawMaterialTestingAcceptanceCriteria2FbeDao rawMaterialTestingAcceptanceCriteria2FbeDao;

    @Autowired
    RawMaterialTestingAcceptanceCriteria3LpeDao rawMaterialTestingAcceptanceCriteria3LpeDao;


    @RequestMapping("/getAllRawMaterialTestingAcceptanceCriteria2fbe")
    @ResponseBody
    public String getAllRawMaterialTestingAcceptanceCriteria2fbe(HttpServletRequest request){

        List<RawMaterialTestingAcceptanceCriteria2Fbe> list=rawMaterialTestingAcceptanceCriteria2FbeDao.getAllRawMaterialTestingAcceptanceCriteria2Fbe();
        List<ComboxItem> colist=new ArrayList<ComboxItem>();
        for(int i=0;i<list.size();i++){
            ComboxItem citem= new ComboxItem();
            RawMaterialTestingAcceptanceCriteria2Fbe ps=((RawMaterialTestingAcceptanceCriteria2Fbe)list.get(i));
            citem.id=ps.getRaw_material_testing_acceptance_criteria_no();
            citem.text=ps.getRaw_material_testing_acceptance_criteria_no();
            colist.add(citem);
        }
        String map= JSONObject.toJSONString(colist);
        return map;
    }



    @RequestMapping("/getAllRawMaterialTestingAcceptanceCriteria3lpe")
    @ResponseBody
    public String getAllRawMaterialTestingAcceptanceCriteria3lpe(HttpServletRequest request){
        List<RawMaterialTestingAcceptanceCriteria3Lpe> list=rawMaterialTestingAcceptanceCriteria3LpeDao.getAllRawMaterialTestingAcceptanceCriteria3Lpe();
        List<ComboxItem> colist=new ArrayList<ComboxItem>();
        for(int i=0;i<list.size();i++){
            ComboxItem citem= new ComboxItem();
            RawMaterialTestingAcceptanceCriteria3Lpe ps=((RawMaterialTestingAcceptanceCriteria3Lpe)list.get(i));
            citem.id=ps.getRaw_material_testing_acceptance_criteria_no();
            citem.text=ps.getRaw_material_testing_acceptance_criteria_no();
            colist.add(citem);
        }
        String map= JSONObject.toJSONString(colist);
        System.out.println("3map="+map);
        return map;
    }

}
