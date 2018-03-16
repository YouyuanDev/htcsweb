package com.htcsweb.util;

import java.io.IOException;

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

    @Override
    protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        System.out.println("====测试Filter功能====拦截用户登陆====");

        String[] notFilter = new String[] { "login.jsp"}; // 不过滤的uri
        String strUri = request.getRequestURI() ;
        System.out.println("strUri="+strUri);
        System.out.println("request.getSession()="+request.getSession().getAttribute("userSession"));
        if(request.getSession().getAttribute("userSession")==null ){
            //进入后台,必须先登陆
            if( strUri.indexOf("login.jsp")==-1&&strUri.indexOf("commitLogin.action")==-1){//点击的不是登陆页面
                response.sendRedirect("/login/login.jsp") ;
            }else{
                System.out.println("可以进入 strUri="+strUri);
                filterChain.doFilter(request, response);//不执行过滤,继续执行操作
                return ;
            }
        }else{
            System.out.println("存在用户session 可以进入 session="+request.getSession().getAttribute("userSession"));
            filterChain.doFilter(request, response);//不执行过滤,继续执行操作
            //filterChain.doFilter(new MyFilter((HttpServletRequest)request), response);//调用下一个filter
            return ;
        }
    }

}