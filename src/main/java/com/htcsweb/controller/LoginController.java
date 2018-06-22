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
import com.htcsweb.util.APICloudPushService;
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

    //存放登录用户的session
    private Map<String,HttpSession> UserSessionMap=new HashMap<String,HttpSession>();

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
        String millno= request.getParameter("mill_no");
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
                session.setAttribute("millno", millno);
                //设置权限
                HashMap<String,Object> functionMap=new HashMap<String,Object>();
                //这里读取数据库设置所有权限

                String role_no_list=null;
                if(employee_no!=null) {
                    List<Person> lt=personDao.getPersonByEmployeeNo(employee_no);
                    if(lt.size()>0) {
                        Person person=lt.get(0);
                        role_no_list=person.getRole_no_list();
                        if(role_no_list!=null&&!role_no_list.equals("")){
                            role_no_list=role_no_list.replace(',',';');
                            String[] roles= role_no_list.split(";");
                            for(int i=0;i<roles.length;i++){
                                List<Role> rolelt=roleDao.getRoleByRoleNo(roles[i]);
                                //System.out.println("role ="+roles[i]);
                                if(rolelt.size()>0) {
                                    Role role=rolelt.get(0);
                                    String functionlist = role.getFunction_no_list();
                                    if(functionlist!=null&&!functionlist.equals("")){
                                        functionlist=functionlist.replace(',',';');
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
                session.setAttribute("userfunctionMap", functionMap);
                //functionMap.put("index","1");




                //查找是否存在其他用户登录该session
                HttpSession oldusersession=UserSessionMap.get(employee_no);
                String msg="";
                if(oldusersession!=null&&oldusersession.getId()!=session.getId()){
                    msg="（已踢出其他客户端）";
                    System.out.println(msg);
                    UserSessionMap.remove(employee_no);
                    try{
                        oldusersession.invalidate();
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }

                // 保存新用户session到公用UserSessionMap
                UserSessionMap.put(employee_no,session);

                //跳转到用户主页
                json.put("success",true);
                json.put("msg","登录成功"+msg);
                json.put("roles",role_no_list);
                //System.out.println("登录验证 success");
//                String basePath = request.getSession().getServletContext().getRealPath("/");
//                System.out.println("登录验证 basePath="+basePath);
//                APICloudPushService.SendPushNotification(basePath,"title标题","内容内容内容内容","2","0","all","");
////
            }else{
                json.put("success",false);
                json.put("msg","用户名或密码错误");
                //System.out.println("fail");
            }
            ResponseUtil.write(response,json);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }




    //登录验证
    @RequestMapping("/Logout")
    @ResponseBody
    public String Logout(HttpServletRequest request,HttpServletResponse response){
        JSONObject json=new JSONObject();
        try{

                HttpSession session = request.getSession();
                //把用户数据保存在session域对象中
                //session.removeAttribute("userSession");
                //session.removeAttribute("userfunctionMap");
                UserSessionMap.remove(session.getAttribute("userSession"));
                session.invalidate();
                //跳转到登录页面
                json.put("success",true);
                json.put("msg","登出成功");

            ResponseUtil.write(response,json);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    //返回自己session，给APP使用
    @RequestMapping("/getMySession")
    @ResponseBody
    public String getMySession(HttpServletRequest request,HttpServletResponse response){
        JSONObject json=new JSONObject();
        try{

            HttpSession session = request.getSession();
            //把用户数据保存在session域对象中
            String employeeno=(String)session.getAttribute("userSession");
            String millno=(String)session.getAttribute("millno");

            if(employeeno!=null&&millno!=null) {
                json.put("success",true);
                json.put("employeeno", employeeno);
                json.put("millno", millno);
                json.put("msg","获取employeeno,millno成功");
            }else if(employeeno!=null&&millno==null){
                json.put("success",false);
                json.put("millno", "");
                json.put("employeeno", employeeno);
                json.put("msg","不存在millno，请先登录");
            }else{
                json.put("success",false);
                json.put("millno", "");
                json.put("employeeno", "");
                json.put("msg","不存在session，请先登录");
            }

            ResponseUtil.write(response,json);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


}
