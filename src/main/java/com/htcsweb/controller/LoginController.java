package com.htcsweb.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.htcsweb.dao.OdBlastProcessDao;
import com.htcsweb.dao.PersonDao;
import com.htcsweb.entity.OdBlastProcess;
import com.htcsweb.entity.Person;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/Login")
public class LoginController {

    @Autowired
    private PersonDao personDao;

    @RequestMapping("/commitLogin")
    @ResponseBody


    //登录验证
    public String commitLogin(HttpServletRequest request,HttpServletResponse response){
        JSONObject json=new JSONObject();
        String employee_no= request.getParameter("employee_no");
        String ppassword= request.getParameter("ppassword");
        try{
            int resTotal=0;
            System.out.println("employee_no="+employee_no);
            //System.out.println("ppassword="+ppassword);
            //if(personDao!=null)
            resTotal= personDao.confirmPersonByEmployeeNoPassword(employee_no,ppassword);
            if(resTotal>0){
                json.put("success",true);
                System.out.println("success");
            }else{
                json.put("success",false);
                json.put("msg","用户名或密码错误");
                System.out.println("fail");
            }
            ResponseUtil.write(response,json);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


}
