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
    //根据姓名模糊查询用户编号
    @RequestMapping(value = "/getPersonNoByName")
    @ResponseBody
    public String getPersonNoByName(HttpServletRequest request){
        String pname=request.getParameter("key");
        String employee_no=request.getParameter("key1");
        List<Person>list=personDao.getNoByName(pname,employee_no);
        String mmp= JSONArray.toJSONString(list);
        return mmp;
    }
    @RequestMapping("getPersonById")
    public String getPersonById(int id,HttpServletRequest request){
        Person person=personDao.getPersonById(id);
        request.getSession().setAttribute("sa",person);
        return "personedit";
    }
}
