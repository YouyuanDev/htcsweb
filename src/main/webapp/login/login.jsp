<%--
  Created by IntelliJ IDEA.
  User: kurt
  Date: 2/8/18
  Time: 4:45 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
    <title>登录</title>

    <link rel="stylesheet" type="text/css" href="../easyui/themes/bootstrap/easyui.css">
    <link rel="stylesheet" type="text/css" href="../easyui/themes/bootstrap/window.css">
    <link rel="stylesheet" type="text/css" href="../easyui/themes/icon.css">
    <link rel="stylesheet" href="../css/common.css"/>
    <script type="text/javascript" src="../easyui/jquery.min.js"></script>
    <script type="text/javascript" src="../easyui/jquery.easyui.min.js"></script>
    <script src="../js/common.js" type="text/javascript"></script>
    <script>
        function submitForm() {
            alert("submit")
            $("#frmLogin").form('submit',{
                url: "/Login/commitLogin.action",
                onSubmit:function() {
                    alert("submit1111")
                    return $(this).form('validate');
                },
                success:function(result) {
                    alert("submit2222")
                    var data = eval('(' + result + ')');
                    if (data.success === 1) {
                        window.location.href = "index.jsp";
                    } else {
                        $.messager.alert("提示",data.msg);
                    }
                }
            });
        }
    </script>
</head>
<body >


<%--<form method="POST" id="frmLogin" >--%>
    <%--<div class="login_title">登陆注册页面FORM</div>--%>
    <%--<label class="login">用户名</label>--%>
    <%--<input class="easyui-textbox" name="loginUser" id="loginUser"--%>
           <%--data-options="iconCls:'icon-man',required:true,validType:'email'" />--%>

    <%--<label class="login">密码</label>--%>
    <%--<input class="easyui-textbox" name="loginPass" id="loginPass"--%>
           <%--type="password" data-options="iconCls:'icon-lock',required:true" />--%>

    <%--<a href="javascript:void(0);" onclick="submitForm();" style="width:80px;"--%>
       <%--class="easyui-linkbutton" data-options="iconCls:'icon-ok'" >登陆</a>--%>
<%--</form>--%>

<%--<div style="margin:20px 0;">--%>
    <%--<a href="javascript:void(0)" class="easyui-linkbutton" onclick="$('#w').window('open')">Open</a>--%>
    <%--<a href="javascript:void(0)" class="easyui-linkbutton" onclick="$('#w').window('close')">Close</a>--%>
<%--</div>--%>
<div id="w" class="easyui-window" title="请先登录" data-options="modal:true,closed:false,iconCls:'Lockgo',closable:false,minimizable:false" style="width:400px;padding:20px 70px 20px 70px;">
    <form id="frmLogin" method="post">
    <div style="margin-bottom:10px">
        <input class="easyui-textbox" id="employee_no" style="width:100%;height:30px;padding:12px" data-options="prompt:'员工工号',iconCls:'icon-man',iconWidth:38">
    </div>
    <div style="margin-bottom:20px">
        <input class="easyui-textbox" id="ppassword" type="password" style="width:100%;height:30px;padding:12px" data-options="prompt:'登录密码',iconCls:'icon-lock',iconWidth:38">
    </div>
    <%--<div style="margin-bottom:20px">--%>
        <%--<input class="easyui-textbox" type="text" id="logyzm" style="width:50%;height:30px;padding:12px" data-options="prompt:'验证码'"> <a href="javascript:;" class="showcode" onclick="changeVeryfy()"><img style=" margin:0 0 0 3px ; vertical-align:middle; height:26px;" src="/index.php?s=/Xjadmin/verifyCode"></a>--%>
    <%--</div>--%>
    <div style="margin-bottom:20px">
        <input type="checkbox" checked="checked" id="logrem">
        <span>Remember me</span>
    </div>
    <div>
        <a href="javascript:;" onclick="submitForm();" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" style="padding:5px 0px;width:100%;">
            <span style="font-size:14px;">登录</span>
        </a>
    </div>
    </form>


</div>









</body>
</html>
