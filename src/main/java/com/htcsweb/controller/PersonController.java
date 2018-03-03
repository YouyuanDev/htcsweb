package com.htcsweb.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.htcsweb.dao.PersonDao;
import com.htcsweb.entity.Person;
import com.htcsweb.entity.PipeBasicInfo;
import com.htcsweb.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/person")
public class PersonController {
    @Autowired
    private PersonDao personDao;


    //根据姓名模糊查询用户编号,小页面查询
    @RequestMapping(value = "/getPersonNoByName",produces = "text/plain;charset=utf-8")
    @ResponseBody
    public String getPersonNoByName(HttpServletRequest request){
        String pname=request.getParameter("pname");
        String employee_no=request.getParameter("employeeno");
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

    //搜索
    @RequestMapping("getPersonByLike")
    @ResponseBody
    public String getPersonByLike( @RequestParam(value = "employee_no",required = false)String employee_no,@RequestParam(value = "pname",required = false)String pname,  HttpServletRequest request){
        String page= request.getParameter("page");
        String rows= request.getParameter("rows");
        if(page==null){
            page="1";
        }
        if(rows==null){
            rows="20";
        }
        System.out.print("employee_no:"+employee_no);
        int start=(Integer.parseInt(page)-1)*Integer.parseInt(rows);
        List<HashMap<String,Object>> list=personDao.getAllByLike(employee_no,pname,start,Integer.parseInt(rows));
        int count=personDao.getCount();
        Map<String,Object> maps=new HashMap<String,Object>();
        maps.put("total",count);
        maps.put("rows",list);
        String mmp= JSONArray.toJSONString(maps);
        System.out.print("mmp:"+mmp);
        return mmp;

    }

    //保存Person
    //增加或修改Pipe信息
    @RequestMapping(value = "/savePerson")
    @ResponseBody
    public String savePerson(Person person, HttpServletResponse response){
        System.out.print("savePerson");

        JSONObject json=new JSONObject();
        try{
            int resTotal=0;


            if(person.getId()==0){
                //添加
                resTotal=personDao.addPerson(person);

            }else{
                //修改！

                resTotal=personDao.updatePerson(person);
            }
            if(resTotal>0){
                json.put("success",true);
                json.put("message","保存成功");
            }else{
                json.put("success",false);
                json.put("message","保存失败");
            }

        }catch (Exception e){
            e.printStackTrace();
            json.put("success",false);
            json.put("message",e.getMessage());

        }finally {
            try {
                ResponseUtil.write(response, json);
            }catch  (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    //删除person信息
    @RequestMapping("/delPerson")
    public String delPerson(@RequestParam(value = "hlparam")String hlparam,HttpServletResponse response)throws Exception{
        String[]idArr=hlparam.split(",");
        int resTotal=0;
        resTotal=personDao.delPerson(idArr);
        JSONObject json=new JSONObject();
        StringBuilder sbmessage = new StringBuilder();
        sbmessage.append("总共");
        sbmessage.append(Integer.toString(resTotal));
        sbmessage.append("项人员信息删除成功\n");
        if(resTotal>0){
            //System.out.print("删除成功");
            json.put("success",true);
        }else{
            //System.out.print("删除失败");
            json.put("success",false);
        }
        json.put("message",sbmessage.toString());
        ResponseUtil.write(response,json);
        return null;
    }

}
