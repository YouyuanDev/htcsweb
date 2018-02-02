package com.htcsweb.controller;

import com.alibaba.fastjson.JSONArray;
import com.htcsweb.dao.PersonDao;
import com.htcsweb.entity.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/person")
public class PersonController {
    @Autowired
    private PersonDao personDao;

    @RequestMapping("addPerson")
    public String addPerson(Person person){
        personDao.addPerson(person);
        return "account/person";
    }
    @ResponseBody
    @RequestMapping("getPerson")
    public Map<String,Object>getPerson(){
        List<Person>personList=personDao.getPerson();
        Map<String,Object>map=new HashMap<String, Object>();
        map.put("rows",personList);
        return map;
    }
    @RequestMapping("editPerson")
    public  String editPerson(Person person){
        personDao.updatePerson(person);
        return "account/person";
    }
    @RequestMapping(value = "/getPersonByPage")
    @ResponseBody
    public String getPersonByPage(@RequestParam(value="page",required=false) String page, @RequestParam(value="rows",required=false) String rows, HttpServletRequest request){
//        String page=request.getParameter("page");
//        String rows=request.getParameter("rows");
        String mmp="";
        if(page==null||rows==null){
           System.out.println("page="+page+":rows="+rows);
        }else{
            int start=(Integer.parseInt(page)-1)*Integer.parseInt(rows);
            List<Person>list=personDao.feny(start,Integer.parseInt(rows));
            int count=personDao.getCount();
            Map<String,Object>map=new HashMap<String, Object>();
            map.put("total",count);
            map.put("rows",list);
            mmp= JSONArray.toJSONString(map);
        }

        return mmp;
    }
    @RequestMapping("getPersonById")
    public String getPersonById(int id,HttpServletRequest request){
        Person person=personDao.getPersonById(id);
        request.getSession().setAttribute("sa",person);
        return "personedit";
    }
}
