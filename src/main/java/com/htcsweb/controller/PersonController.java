package com.htcsweb.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.htcsweb.dao.MillInfoDao;
import com.htcsweb.dao.PersonDao;
import com.htcsweb.dao.RoleDao;
import com.htcsweb.entity.MillInfo;
import com.htcsweb.entity.Person;
import com.htcsweb.entity.PipeBasicInfo;
import com.htcsweb.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private MillInfoDao millInfoDao;


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


    //供app程序使用，无需权限，需验证登录情况
    @RequestMapping(value ="/getPersonByEmployeeNo",produces = "text/plain;charset=utf-8")
    @ResponseBody
    public String getPersonByEmployeeNo(HttpServletRequest request){
        HttpSession session = request.getSession();
        //把用户数据保存在session域对象中
        String employee_no=(String)session.getAttribute("userSession");
        String millno=(String)session.getAttribute("millno");
        if(millno==null)
            millno="";
        String millname="";
        //翻译mill的名字
        List<MillInfo> milllist=millInfoDao.getAllMillInfo();
        for(int j=0;j<milllist.size();j++){
            MillInfo mill=milllist.get(j);
            if(mill.getMill_no().equals(millno)){
                millname=mill.getMill_name();
                break;
            }
        }
        Map<String,Object> maps=new HashMap<String,Object>();
        if(employee_no==null){

            maps.put("success",false);
            maps.put("msg","请先登录");
            String mmp= JSONArray.toJSONString(maps);
            return mmp;
        }

        List<Person> personList=personDao.getPersonByEmployeeNo(employee_no);
        String mmp="";
        if(personList!=null&&personList.size()>0) {
            //角色信息翻译
            Person person=personList.get(0);
            String roles=person.getRole_no_list();
            List<HashMap<String,Object>> rolist=roleDao.getAllRoleByLike(null,null);
            for(int i=0;i<rolist.size();i++){
                HashMap<String,Object> rolemap=rolist.get(i);
                String roleno=(String)rolemap.get("role_no");
                String rolename=(String)rolemap.get("role_name");
                roles=roles.replace(roleno,rolename);
            }
            person.setRole_no_list(roles);
            maps.put("success",true);
            maps.put("msg","获取成功");
            maps.put("millname",millname);
            maps.put("data",person);
            mmp= JSONArray.toJSONString(maps);
        }
        return mmp;
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
        int start=(Integer.parseInt(page)-1)*Integer.parseInt(rows);
        List<HashMap<String,Object>> list=personDao.getAllByLike(employee_no,pname,start,Integer.parseInt(rows));
        int count=personDao.getCountAllByLike(employee_no,pname);
        Map<String,Object> maps=new HashMap<String,Object>();
        maps.put("total",count);
        maps.put("rows",list);
        String mmp= JSONArray.toJSONString(maps);
        //System.out.print("mmp:"+mmp);
        return mmp;

    }

    //保存Person
    //增加或修改Pipe信息
    @RequestMapping(value = "/savePerson")
    @ResponseBody
    public String savePerson(Person person, HttpServletRequest request,HttpServletResponse response){
        //System.out.println(request.getParameter("employee_no")+"getVal");
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
                json.put("message","修改成功!");
            }else{
                json.put("success",false);
                json.put("message","修改失败!");
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
