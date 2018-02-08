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
    @RequestMapping(value = "/getPersonByLike")
    @ResponseBody
    public String getPersonByPage(HttpServletRequest request){
        String page=request.getParameter("page");
        String rows=request.getParameter("rows");
        System.out.println("page="+page);
        System.out.println("rows="+rows);
        if(page==null||page==""){
            page="1";
        }
        if(rows==null||rows==""){
            rows="10";
        }
        String pname=request.getParameter("key");
        System.out.println("pname="+pname);
        int start=(Integer.parseInt(page)-1)*Integer.parseInt(rows);
        List<Person>list=personDao.getAllByLike(pname,start,Integer.parseInt(rows));
        int count=personDao.getCount();
        Map<String,Object>map=new HashMap<String, Object>();
        map.put("total",count);
        map.put("rows",list);
        String mmp= JSONArray.toJSONString(map);
        return mmp;
    }
    @RequestMapping("getPersonById")
    public String getPersonById(int id,HttpServletRequest request){
        Person person=personDao.getPersonById(id);
        request.getSession().setAttribute("sa",person);
        return "personedit";
    }
}
