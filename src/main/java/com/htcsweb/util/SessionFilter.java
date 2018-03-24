package com.htcsweb.util;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

/**
 * OncePerRequestFilter
 *      确保一个请求只经过一个filter,而不需要重复执行
 * @author huangdb
 *
 */
public class SessionFilter extends OncePerRequestFilter{

    boolean adminMode=false; //测试时暂时关闭
    String[] notFilterList = new String[] {
            "login.jsp",
            "commitLogin.action",
            "error.jsp",
            "getAllMillsWithComboboxSelectAll",
            "getAllMills",
            "getODAcceptanceCriteriaByContractNo",
            "getPipeNumbers",
            "getPersonNoByName",
            "getPipeInfoByNo",
            "delUploadPicture",
            "uploadPicture",
            "getIDAcceptanceCriteriaByContractNo",
            "getAllODAcceptanceCriteria",
            "getAllIDAcceptanceCriteria",
            "getAllLabTestingAcceptanceCriteria2fbe",
            "getAllLabTestingAcceptanceCriteria3lpe",
            "getAllRawMaterialTestingAcceptanceCriteria2fbe",
            "getAllRawMaterialTestingAcceptanceCriteria3lpe",
            "getProjectInfo",
            "getProjectNameByNo",
            "getContractInfoByContractNo",
            "getAllPipeStatus",
            "getAllPipeStatusWithComboboxSelectAll",
            "getAcceptanceCriteria2FbeByContractNo",
            "getAcceptanceCriteria3LpeByContractNo",
            "getRawMaterialStandard2FbeByProjectNo",
            "getRawMaterialStandard3LpeByProjectNo",
            "getFunctionByLike",
            "getFunctionByNoName",
            "getAllRoleByLike",
            "pipe_coating_surface_inspection_record_template"

    }; // 不过滤的uri

    @Override
    protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if(adminMode){//adminMode
            filterChain.doFilter(request, response);//不执行过滤,继续执行操作
            return;
        }



        System.out.println("====测试Filter功能====拦截用户登陆====");
        String strUri = request.getRequestURI();
        //从uri中取出functioncode 如/index.jsp 为 index
        String reqfunctionCode=strUri.substring(strUri.lastIndexOf('/')+1,strUri.lastIndexOf('.'));
        System.out.println("reqfunctionCode===="+reqfunctionCode);
        //System.out.println("strUri="+strUri);
        if(request.getSession().getAttribute("userSession")==null ){
            //进入后台,必须先登陆
            if(isURIinNotFilterList(strUri)){//请求的URI允许不过滤,包括login等
                //System.out.println("====login 不需要验证====");
                System.out.println("请求登录="+strUri);
                filterChain.doFilter(request, response);//不执行过滤,继续执行操作
                return;
            }else{//uri需要被过滤
                System.out.println("目标URI禁止访问，请先登录"+strUri);
                response.sendRedirect("/login/login.jsp") ;

            }
        }else{//存在登录信息session
            if(isURIinNotFilterList(strUri)){//请求的URI允许不过滤,包括login等
                filterChain.doFilter(request, response);//不执行过滤,继续执行操作
                return;
            }


            System.out.println("存在用户session 可以进入 session="+request.getSession().getAttribute("userSession"));
            System.out.println("检测用户是否存在页面"+reqfunctionCode+"的权限");
            boolean authrized=false;
            //下面开始验证访问权限
            HashMap<String,Object> functionMap=(HashMap<String,Object>)request.getSession().getAttribute("userfunctionMap");
            if(functionMap!=null&&functionMap.containsKey(reqfunctionCode)){
                authrized=true;
            }
            System.out.println("authrized===="+authrized);
            if(!authrized)
                response.sendRedirect("/error.jsp") ;
            else
                filterChain.doFilter(request, response);//不执行过滤,继续执行操作
            //filterChain.doFilter(new MyFilter((HttpServletRequest)request), response);//调用下一个filter
            return ;
        }
    }

    //检查URI是否在免过滤列表中
    private boolean isURIinNotFilterList(String URI){
        if(URI==null)
            return false;


        for(int i=0;i<notFilterList.length;i++){
            if( URI.indexOf(notFilterList[i])!=-1)
                return true;
        }


        return false;
    }




}