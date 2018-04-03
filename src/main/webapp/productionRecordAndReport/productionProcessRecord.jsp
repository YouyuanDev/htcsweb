<%--
  Created by IntelliJ IDEA.
  User: kurt
  Date: 3/26/18
  Time: 1:49 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" type="text/css" href="../easyui/themes/bootstrap/easyui.css">
    <link rel="stylesheet" type="text/css" href="../easyui/themes/icon.css">
    <link href="../miniui/multiupload/multiupload.css" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" href="../css/common.css"/>
    <script src="../easyui/jquery.min.js" type="text/javascript"></script>
    <script src="../js/common.js" type="text/javascript"></script>
    <script src="../miniui/boot.js" type="text/javascript"></script>
    <script src="../miniui/fileupload/swfupload/swfupload.js" type="text/javascript"></script>
    <script src="../miniui/multiupload/multiupload.js" type="text/javascript"></script>
    <script  src="../js/lrscroll.js" type="text/javascript"></script>
    <script src="../js/jquery.i18n.properties-1.0.9.js" type="text/javascript"></script>
    <script src="../js/language.js" type="text/javascript"></script>

    <style type="text/css">
        .datagrid-mask {
            position: absolute;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            opacity: 0.5;
            filter: alpha(opacity=50);
            background-color:#000000;
            display: none;
        }

        .datagrid-mask-msg {
            position: absolute;
            top: 50%;
            margin-top: -20px;
            padding: 12px 5px 10px 30px;
            width: auto;
            height: 16px;
            border-width: 2px;
            border-style: solid;
            display: none;
        }
    </style>
    <script type="text/javascript">
        $(function() {
            $('.btnReport').click(function () {
                var selectValue=$('#cc').val();
                var begin_time=$('#begintime').val();
                var end_time=$('#endtime').val();
                if(begin_time==null||begin_time.length==""){
                    $.messager.alert('Warning','请输入开始时间!');
                    return false;
                }
                if(end_time==null||end_time.length==""){
                    $.messager.alert('Warning','请输入结束时间!');
                    return false;
                }
                var begin_date=new Date(begin_time);
                var end_date=new Date(end_time);
                if(!(end_date-begin_date>=0)){
                    $.messager.alert('Warning','开始时间必须小于结束时间!');
                    return false;
                }
                var form=$("<form>");//定义一个form表单
                form.attr("style","display:none");
                form.attr("target","");
                form.attr("method","post");//请求类型
                form.attr("action","/InspectionRecordPDFOperation/getRecordReportPDF.action");//请求地址
                $("body").append(form);//将表单放置在web中
                var input1=$("<input type='hidden' name='selectValue' value='"+selectValue+"'/>");
                form.append(input1);
                var input2=$("<input type='hidden' name='beginTime' value='"+begin_time+"'/>");
                form.append(input2);
                var input3=$("<input type='hidden' name='endTime' value='"+end_time+"'/>");
                form.append(input3);
                form.submit();//表单提交


                // $.ajax({
                //     url:"/InspectionRecordPDFOperation/getRecordReportPDF.action",
                //     data:{selectValue:selectValue,beginTime:begin_time,endTime:end_time},
                //     dataType:'json',
                //     async:false,
                //     beforeSend:function(){
                //         ajaxLoading();
                //     },
                //     success:function(data){
                //         if(data!=null){
                //              if(data=="success"){
                //                  $.messager.alert('Warning','生成成功!');
                //              }else{
                //                  $.messager.alert('Warning','生成失败!');
                //              }
                //         }else{
                //             $.messager.alert('Warning','生成失败!');
                //         }
                //         ajaxLoadEnd();
                //     },
                //     error:function(){
                //         ajaxLoadEnd();
                //         $.messager.alert('Warning','生成失败!');
                //     },
                //     complete:function(){
                //         ajaxLoadEnd();
                //     }
                // });
            });
        });

        function ajaxLoading() {
            $("<div class=\"datagrid-mask\"></div>").css({
                display: "block",
                width: "100%",
                height: $(window).height()
            }).appendTo("body");
            $("<div class=\"datagrid-mask-msg\"></div>").html("正在处理，请稍候。。。").appendTo("body").css({
                display: "block",
                left: ($(document.body).outerWidth(true) - 190) / 2,
                top: ($(window).height() - 45) / 2
            });
        }

        function ajaxLoadEnd() {
            $(".datagrid-mask").remove();
            $(".datagrid-mask-msg").remove();
        }
    </script>
</head>
<body>
<div style="padding:10px">
    <select id="cc" class="easyui-combobox" data-options="editable:false" name="result" style="width:200px;">
        <option value="-1" selected="selected">所有工序</option>
        <option value="0">外喷砂工序</option>
        <option value="1">外喷砂检验工序</option>
        <option value="2">外涂工序(2FBE)</option>
        <option value="3">外涂检验工序(2FBE)</option>
        <option value="4">外涂工序(3LPE)</option>
        <option value="5">外涂检验工序(3LPE)</option>
        <option value="6">外喷标工序</option>
        <option value="7">外涂层终检工序</option>
        <option value="8">内喷砂工序</option>
        <option value="9">内喷砂检验工序</option>
        <option value="10">内涂工序</option>
        <option value="11">内涂检验工序</option>
        <option value="12">内喷标工序</option>
        <option value="13">内涂层终检工序</option>
    </select>
    <span class="i18n1" name="begintime">开始时间</span>:
    <input id="begintime" name="begintime" type="text" class="easyui-datebox" data-options="formatter:myformatter,parser:myparser">
    <span class="i18n1" name="endtime">结束时间</span>:
    <input id="endtime" name="endtime" type="text" class="easyui-datebox" data-options="formatter:myformatter,parser:myparser">
    <button class="btnReport">开始生成</button>

</div>
<script type="text/javascript" src="../easyui/jquery.easyui.min.js"></script>
</body>
</html>
