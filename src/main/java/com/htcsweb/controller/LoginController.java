package com.htcsweb.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.htcsweb.dao.FunctionDao;
import com.htcsweb.dao.OdBlastProcessDao;
import com.htcsweb.dao.PersonDao;
import com.htcsweb.dao.RoleDao;
import com.htcsweb.entity.Function;
import com.htcsweb.entity.OdBlastProcess;
import com.htcsweb.entity.Person;
import com.htcsweb.entity.Role;
import com.htcsweb.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


//import org.apache.shiro.crypto.hash.Md5Hash;
//import org.apache.shiro.crypto.hash.SimpleHash;


@Controller
@RequestMapping("/Login")
public class LoginController {

    @Autowired
    private PersonDao personDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private FunctionDao functionDao;


//    public static String md5(String pass){
//        String saltSource = "blog";
//        String hashAlgorithmName = "MD5";
//        Object salt = new Md5Hash(saltSource);
//        int hashIterations = 1024;
//        Object result = new SimpleHash(hashAlgorithmName, pass, salt, hashIterations);
//        String password = result.toString();
//        return password;
//    }

    //登录验证
    @RequestMapping("/commitLogin")
    @ResponseBody
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
                HttpSession session = request.getSession();
                //把用户数据保存在session域对象中
                session.setAttribute("userSession", employee_no);

                //设置权限
                HashMap<String,Object> functionMap=new HashMap<String,Object>();
                //这里读取数据库设置所有权限
                if(employee_no!=null) {
                    List<Person> lt=personDao.getPersonByEmployeeNo(employee_no);
                    if(lt.size()>0) {
                        Person person=lt.get(0);
                        String role_no_list=person.getRole_no_list();
                        if(role_no_list!=null&&!role_no_list.equals("")){
                            String[] roles= role_no_list.split(";");
                            for(int i=0;i<roles.length;i++){
                                List<Role> rolelt=roleDao.getRoleByRoleNo(roles[i]);
                                System.out.println("role ="+roles[i]);
                                if(rolelt.size()>0) {
                                    Role role=rolelt.get(0);
                                    String functionlist = role.getFunction_no_list();
                                    if(functionlist!=null&&!functionlist.equals("")){
                                        String[] func_no_s=functionlist.split(";");
                                        for(int j=0;j<func_no_s.length;j++) {
                                            List<Function> funlst=functionDao.getFunctionByFunctionNo(func_no_s[j]);
                                            if(funlst.size()>0){
                                                //得到function
                                                Function f=funlst.get(0);
                                                String function_no=f.getFunction_no();
                                                String uri=f.getUri();
                                                functionMap.put(function_no,"1");
                                                functionMap.put(uri,"1");
                                                System.out.println("functionMap put="+function_no);
                                                System.out.println("uri put="+uri);
                                            }


                                        }
                                    }
                                }
                            }

                        }


                    }
                }

                //functionMap.put("index","1");
                session.setAttribute("userfunctionMap", functionMap);

                //跳转到用户主页
                json.put("success",true);
                System.out.println("登录验证 success");
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
