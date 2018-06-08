package com.htcsweb.util;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;



public class APICloudPushService {

    //HLCoatingTrace APP 来自于APICloud
    static String appid = "A6054425848203";
    static String appkey = "1A5DC49C-78B8-1489-9538-29F567194F15";
    static String APIURL = "https://p.apicloud.com/api/push/message";

    public static void main(String[] args) throws IOException {

        SendPushNotification("","10:59titile","内容内容内容内容","1","0","all","");
//        try {
//            HttpClient client = new HttpClient();
//
//            // post请求
//            PostMethod post = new UTF8PostMethod(APIURL);
//
//            // 提交参数
//            NameValuePair title = new NameValuePair("title","推送没121221");                 // 消息标题
//            NameValuePair content = new NameValuePair("content","推送了哇11221212");             // 消息内容
//            NameValuePair type = new NameValuePair("type","2");                     // 消息类型，1:消息 2:通知
//            NameValuePair platform = new NameValuePair("platform","0");             // 0：全部平台，1：ios, 2：android
//            // 推送组，推送用户(没有可不写)
//            NameValuePair groupName = new NameValuePair("groupName","admin");     // 推送组名，多个组用英文逗号隔开.默认:全部组
//            //NameValuePair userIds = new NameValuePair("userIds","id名称");         // 推送用户id, 多个用户用英文逗号分隔
//
//            post.setRequestBody(new NameValuePair[]{title, content, type, platform/*, groupName, userIds*/});
//
//            HttpMethod method = post;
//
//            // 生成规则
//            String    key = testInvokeScriptMethod();
//
//            // 设置请求头部信息
//            method.setRequestHeader("X-APICloud-AppId", appid);
//            method.setRequestHeader("X-APICloud-AppKey", key);
//
//            // 执行方法
//            client.executeMethod(method);
//
//            // 打印服务器返回的状态
//            System.out.println(method.getStatusLine());
//
//            // 打印结果页面
//            String response = new String(method.getResponseBodyAsString().getBytes("8859_1"));
//
//            // 打印返回的信息
//            System.out.println(response);
//
//            // 释放连接
//            method.releaseConnection();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }



    public static String SendPushNotification(String sha1bathPath,String str_title,String str_content,String str_type,String str_platform,String str_groupName,String str_userIds){

        try{
            HttpClient client = new HttpClient();

            // post请求
            PostMethod post = new UTF8PostMethod(APIURL);

            // 提交参数
            NameValuePair title = new NameValuePair("title",str_title);                 // 消息标题
            NameValuePair content = new NameValuePair("content",str_content);             // 消息内容
            NameValuePair type = new NameValuePair("type",str_type);                     // 消息类型，1:消息 2:通知
            NameValuePair platform = new NameValuePair("platform",str_platform);             // 0：全部平台，1：ios, 2：android
            // 推送组，推送用户(没有可不写)
            NameValuePair groupName = new NameValuePair("groupName",str_groupName);     // 推送组名，多个组用英文逗号隔开.默认:全部组
            NameValuePair userIds = new NameValuePair("userIds",str_userIds);         // 推送用户id, 多个用户用英文逗号分隔

            post.setRequestBody(new NameValuePair[]{title, content, type, platform, groupName, userIds});
            HttpMethod method = post;
            // 生成规则
            String    key = testInvokeScriptMethod(sha1bathPath);
            // 设置请求头部信息
            method.setRequestHeader("X-APICloud-AppId", appid);
            method.setRequestHeader("X-APICloud-AppKey", key);
            // 执行方法
            client.executeMethod(method);
            // 打印服务器返回的状态
            System.out.println(method.getStatusLine());
            // 打印结果页面
            String response = new String(method.getResponseBodyAsString().getBytes("8859_1"));
            // 打印返回的信息
            System.out.println(response);
            // 释放连接
            method.releaseConnection();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }




    /**
     * Java中调用脚本语言的方法，通过JDK平台给script的方法中的形参赋值
     *
     * @param
     * @return String
     * */
    private static String testInvokeScriptMethod(String bathPath) throws Exception {
        // 获取时间戳
        long now = new Date().getTime();

        String key = appid + "UZ" + appkey + "UZ" + now;

        ScriptEngineManager sem = new ScriptEngineManager();
        ScriptEngine engine = sem.getEngineByName("js");

        // sha1.js 路径 (可根据自己的需求改为项目存放的相对路径)

        String sha1 = "/Users/kurt/Documents/workspace/htcsweb/src/main/webapp/js/sha1.js";

        if(bathPath!=null&&!bathPath.equals("")){
            sha1=bathPath+"/js/sha1.js";
        }
        System.out.println("sha1="+sha1);
        // 调用js文件
        FileReader fr = new FileReader(sha1);
        engine.eval(fr); // 指定脚本

        Invocable inv = (Invocable) engine;
        // 调用js函数方法(SHA1)
        String res = (String) inv.invokeFunction ("SHA1", key);
        //System.out.println("sha1="+res);
        return res + "." + now;
    }

    public static class UTF8PostMethod extends PostMethod{
        public UTF8PostMethod(String url){
            super(url);
        }
        @Override
        public String getRequestCharSet() {
            return "UTF-8";
        }
    }
}
