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

    private PersonDao personDao;

    @RequestMapping("/commitLogin")
    @ResponseBody
    public String commitLogin(String employee_no,String ppassword, HttpServletResponse response){
        JSONObject json=new JSONObject();
        try{
            int resTotal=0;
            System.out.println("employee_no="+employee_no);

            resTotal= personDao.confirmPersonByEmployeeNoPassword(employee_no,ppassword);

            if(resTotal>0){
                json.put("success",true);
                System.out.println("success");
            }else{
                json.put("success",false);
                System.out.println("fail");
            }
            ResponseUtil.write(response,json);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


}
