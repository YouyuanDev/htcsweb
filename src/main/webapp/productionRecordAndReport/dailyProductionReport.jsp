<%--
  Created by IntelliJ IDEA.
  User: kurt
  Date: 4/4/18
  Time: 10:19 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
    String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
    String path = request.getContextPath();
    String bPath =request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>生产日报</title>
    <link rel="stylesheet" type="text/css" href="../easyui/themes/bootstrap/easyui.css">
    <link rel="stylesheet" type="text/css" href="../easyui/themes/icon.css">
    <link href="../miniui/multiupload/multiupload.css" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" href="../css/common.css"/>
    <script src="../easyui/jquery.min.js" type="text/javascript"></script>
    <%--<script src="../js/jquery.i18n.properties-1.0.9.js" type="text/javascript"></script>--%>
    <%--<script src="../js/language.js" type="text/javascript"></script>--%>
    <script src="../js/common.js" type="text/javascript"></script>
    <script src="../miniui/boot.js" type="text/javascript"></script>
    <%--<script  src="../miniui/js/miniui.js" type="text/javascript"></script>--%>
    <script src="../miniui/fileupload/swfupload/swfupload.js" type="text/javascript"></script>
    <script src="../miniui/multiupload/multiupload.js" type="text/javascript"></script>
    <script  src="../js/lrscroll.js" type="text/javascript"></script>
    <script src="../js/jquery.i18n.properties-1.0.9.js" type="text/javascript"></script>
    <script src="../js/language.js" type="text/javascript"></script>

    <script src="../js/jquery.form.js" type="text/javascript"></script>
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
        var url;
        var basePath ="<%=basePath%>"+"/upload/pictures/";
        $(function () {
            //删除上传的图片

            $('#hldailyProRptDialog').dialog({
                onClose:function () {
                    var type=$('#hlcancelBtn').attr('operationtype');
                    if(type=="add"){
                        var $dialog=$('#hldailyProRptDialog');
                    }
                    clearFormLabel();
                }
            });
            $('.mini-buttonedit .mini-buttonedit-input').css('width','150px');
            // hlLanguage("../i18n/");
        });
        function addDailyProRpt(){
            $('#hlcancelBtn').attr('operationtype','add');
            $('#hldailyProRptDialog').dialog('open').dialog('setTitle','新增');
            clearFormLabel();

            url="/DailyProductionReportOperation/saveDailyProductionReport.action";
            //$("input[name='alkaline_dwell_time']").siblings().css("background-color","#F9A6A6");
        }
        function delDailyProRpt() {
            var row = $('#dailyProRptDatagrids').datagrid('getSelections');
            if(row.length>0){
                var idArr=[];
                for (var i=0;i<row.length;i++){
                    idArr.push(row[i].id);
                }
                var idArrs=idArr.join(',');
                $.messager.confirm('系统提示',"您确定要删除这<font color=red>"+idArr.length+ "</font>条数据吗？",function (r) {
                    if(r){
                        $.post("/DailyProductionReportOperation/delDailyProductionReport.action",{hlparam:idArrs},function (data) {
                            if(data.success){
                                $("#dailyProRptDatagrids").datagrid("reload");
                            }
                            hlAlertFour(data.message);
                        },"json");
                    }
                });
            }else{
                hlAlertOne();
            }
        }
        function editDailyProRpt(){
            $('#hlcancelBtn').attr('operationtype','edit');
            var row = $('#dailyProRptDatagrids').datagrid('getSelected');
            if(row){
                $('#hldailyProRptDialog').dialog('open').dialog('setTitle','修改');

                $('#dprid').textbox("setValue",row.id);
                $('#dailyProRptForm').form('load',row);

                $('#production-date').datetimebox('setValue',getDate1(row.production_date));

                url="/DailyProductionReportOperation/saveDailyProductionReport.action?id="+row.id;
            }else{
                hlAlertTwo();
            }
        }
        function searchDailyProRpt() {
            $('#dailyProRptDatagrids').datagrid('load',{
                'project_no': $('#projectno').val(),
                'begin_time': $('#begintime').val(),
                'end_time': $('#endtime').val()
            });
        }
        function dailyProductionReportFormSubmit() {
            $('#dailyProRptForm').form('submit',{
                url:url,
                onSubmit:function () {
                    //表单验证
                    setParams($("input[name='bare_pipe_count']"));
                    setParams($("input[name='bare_pipe_length']"));
                    setParams($("input[name='od_total_coated_count']"));
                    setParams($("input[name='od_total_accepted_count']"));
                    setParams($("input[name='od_aiming_accepted_count']"));
                    setParams($("input[name='od_total_accepted_length']"));
                    setParams($("input[name='od_aiming_total_accepted_length']"));
                    setParams($("input[name='od_repair_pipe_count']"));
                    setParams($("input[name='od_bare_pipe_onhold_count']"));
                    setParams($("input[name='od_bare_pipe_grinded_count']"));
                    setParams($("input[name='od_bare_pipe_cut_count']"));

                    setParams($("input[name='od_coated_pipe_rejected_count']"));
                    setParams($("input[name='od_coated_pipe_strip_count']"));
                    setParams($("input[name='id_total_coated_count']"));
                    setParams($("input[name='id_total_accepted_count']"));
                    setParams($("input[name='id_aiming_accepted_count']"));


                    setParams($("input[name='id_total_accepted_length']"));
                    setParams($("input[name='id_aiming_total_accepted_length']"));
                    setParams($("input[name='id_repair_pipe_count']"));
                    setParams($("input[name='id_bare_pipe_onhold_count']"));
                    setParams($("input[name='id_bare_pipe_grinded_count']"));
                    setParams($("input[name='id_bare_pipe_cut_count']"));


                    setParams($("input[name='id_coated_pipe_rejected_count']"));
                    setParams($("input[name='id_coated_pipe_strip_count']"));
                    //setParams($("input[name='od_test_pipe_no_dayshift']"));
                    setParams($("input[name='od_test_pipe_length_before_cut_dayshift']"));
                    setParams($("input[name='od_test_pipe_cutting_length_dayshift']"));
                    //setParams($("input[name='od_test_pipe_no_nightshift']"));
                    setParams($("input[name='od_test_pipe_length_before_cut_nightshift']"));
                    setParams($("input[name='od_test_pipe_cutting_length_nightshift']"));


                    setParams($("input[name='od_test_pipe_count']"));
                    setParams($("input[name='rebevel_pipe_count']"));
                    setParams($("input[name='pipe_accepted_count_after_rebevel']"));
                    setParams($("input[name='pipe_delivered_count']"));
                    setParams($("input[name='pipe_delivered_length']"));



                    if($("input[name='production-date']").val()==""){

                        hlAlertFour("请选择生产日期");
                        return false;
                    }
                    if($("input[name='project_no']").val()==""){

                        hlAlertFour("请输入项目编号");
                        return false;
                    }

                },
                success: function(result){
                    var result = eval('('+result+')');
                    $('#hldailyProRptDialog').dialog('close');
                    if (result.success){
                        $('#dailyProRptDatagrids').datagrid('reload');
                    }
                    clearFormLabel();
                    hlAlertFour(result.message);
                },
                error:function () {
                    hlAlertThree();
                }
            });


        }
        function dailyProductionReportCancelSubmit() {
            $('#hldailyProRptDialog').dialog('close');
        }
        
        function  addDialyReportLinkBtn() {
            var project_no=$("input[name='projectno']").val();
            var begin_time=$("input[name='begintime']").val();
            var end_time=$("input[name='endtime']").val();
            if(project_no==null||project_no==""){
                alert("请输入项目编号");
                return false;
            }
            if(begin_time==null||begin_time==""){
                alert("请输入开始时间");
                return false;
            }
            if(end_time==null||end_time==""){
                alert("请输入结束时间");
                return false;
            }
            $.ajax({
               url:'/DailyProductionReportOperation/addDialyReportAndCreateExcel.action',
               dataType:'json',
               data:{project_no:project_no,begin_time:begin_time,end_time:end_time},
               success:function (data) {
                   if (data=="success"){
                       $('#dailyProRptDatagrids').datagrid('reload');
                       //hlAlertFour("生成成功!");
                       //开始下载excel
                   }else{
                       hlAlertFour("生成日报时出错!");
                   }

               },
                error:function () {
                    hlAlertFour("生成日报时出错!");
                }
            });
        }

        function  clearFormLabel(){
            $('#dailyProRptForm').form('clear');
            $('.hl-label').text('');
        }
        function downloadDialyReportLinkBtn() {
            var project_no=$("input[name='projectno']").val();
            var begin_time=$("input[name='begintime']").val();
            var end_time=$("input[name='endtime']").val();
            if (begin_time == null || begin_time.length == "") {
                $.messager.alert('Warning', '请输入开始时间!');
                return false;
            }
            if (end_time == null || end_time.length == "") {
                $.messager.alert('Warning', '请输入结束时间!');
                return false;
            }
            var begin_date = new Date(begin_time);
            var end_date = new Date(end_time);
            if (!(end_date - begin_date >= 0)) {
                $.messager.alert('Warning', '开始时间必须小于结束时间!');
                return false;
            }
            var form = $("<form>");//定义一个form表单
            form.attr("style", "display:none");
            form.attr("target", "");
            form.attr("method", "post");//请求类型
            form.attr("action","/DailyProductionReportOperation/getDailyRecordReportExcel.action");//请求地址
            $("body").append(form);//将表单放置在web中
            var input1 = $("<input type='hidden' name='project_no' value='" + project_no + "'/>");
            form.append(input1);
            var input2 = $("<input type='hidden' name='beginTime' value='" + begin_time + "'/>");
            form.append(input2);
            var input3 = $("<input type='hidden' name='endTime' value='" + end_time + "'/>");
            form.append(input3);
            // form.submit();
            var options={
                type:'POST',
                url:'/DailyProductionReportOperation/getDailyRecordReportExcel.action',
                dataType:'json',
                beforeSubmit:function () {
                    ajaxLoading();
                },
                success:function (data) {
                    //alert(data);
                    if(data=="fail"){
                       alert("下载时出现错误!")
                    }else if(data=="nodata"){
                        alert("暂时没有数据!")
                    }
                    else{
                        window.location.href="<%=bPath%>"+data;
                    }
                    ajaxLoadEnd();
                },error:function () {
                    ajaxLoadEnd();
                }
            };
            //form.submit(function (e) {
            form.ajaxSubmit(options);
            return false;
        }
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
<fieldset class="b3" style="padding:10px;margin:10px;">
    <legend> <h3><b style="color: orange" >|&nbsp;</b><span class="i18n1" name="datadisplay">数据展示</span></h3></legend>
    <div  style="margin-top:5px;">
        <table class="easyui-datagrid" id="dailyProRptDatagrids" url="/DailyProductionReportOperation/getDailProductionReportByLike.action" striped="true" loadMsg="正在加载中。。。" textField="text" pageSize="20" fitColumns="true" pagination="true" toolbar="#hldailyRptTb">
            <thead>
            <tr>
                <th data-options="field:'ck',checkbox:true"></th>
                <th field="id" align="center" width="100" class="i18n1" name="id">流水号</th>
                <th field="production_date" align="center" width="150" class="i18n1" name="productiondate" data-options="formatter:formatterdate">生产日期</th>
                <th field="project_no" align="center" width="150" class="i18n1" name="projectno">项目编号</th>
                <th field="project_name" align="center" width="120" class="i18n1" name="projectname">项目名称</th>

                <th field="od_wt" align="center" width="50" class="i18n1" name="odwt">外径*壁厚</th>
                <th field="od_coating_type" align="center" width="50" class="i18n1" name="odcoatingtype">外涂种类</th>
                <th field="bare_pipe_count" align="center" width="50" class="i18n1" name="barepipecount">光管接收数量</th>
                <th field="bare_pipe_length" align="center" width="50" class="i18n1" name="barepipelength">光管接收长度</th>
                <th field="od_total_coated_count" align="center" width="50" class="i18n1" name="odtotalcoatedcount">外防涂敷数量</th>
                <th field="od_total_accepted_count" align="center" width="50" class="i18n1" name="odtotalacceptedcount">外防涂敷合格数量</th>

                <th field="od_aiming_accepted_count" align="center" width="50" class="i18n1" name="odaimingacceptedcount">外防目标涂敷数量</th>
                <th field="od_total_accepted_length" align="center" width="50" class="i18n1" name="odtotalacceptedlength">外防合格涂敷长度</th>
                <th field="od_aiming_total_accepted_length" align="center" width="50" class="i18n1" name="odaimingtotalacceptedlength">外防目标合格涂敷长度</th>
                <th field="od_repair_pipe_count" align="center" width="50" class="i18n1" name="odrepairpipecount">外防修补管数</th>

                <th field="od_bare_pipe_onhold_count" align="center" width="50" class="i18n1" name="odbarepipeonholdcount">光管待处理数量</th>
                <th field="od_bare_pipe_grinded_count" align="center" width="50" class="i18n1" name="odbarepipegrindedcount">外光管修磨数量</th>
                <th field="od_bare_pipe_cut_count" align="center" width="50" class="i18n1" name="odbarepipecutcount">外光管切长数量</th>
                <th field="od_coated_pipe_rejected_count" align="center" width="50" class="i18n1" name="odcoatedpiperejectedcount">外涂层管废管数量</th>

                <th field="od_coated_pipe_strip_count" align="center" width="50" class="i18n1" name="odcoatedpipestripcount">外涂层管扒皮数量</th>
                <th field="id_total_coated_count" align="center" width="50" class="i18n1" name="idtotalcoatedcount">内防涂敷数量</th>
                <th field="id_total_accepted_count" align="center" width="50" class="i18n1" name="idtotalacceptedcount">内防涂敷合格数量</th>
                <th field="id_aiming_accepted_count" align="center" width="50" class="i18n1" name="idaimingacceptedcount">内防目标涂敷数量</th>

                <th field="id_total_accepted_length" align="center" width="50" class="i18n1" name="idtotalacceptedlength">内防合格涂敷米数</th>
                <th field="id_aiming_total_accepted_length" align="center" width="50" class="i18n1" name="idaimingtotalacceptedlength">内防目标合格涂敷米数</th>
                <th field="id_repair_pipe_count" align="center" width="50" class="i18n1" name="idrepairpipecount">内防修补管数</th>
                <th field="id_bare_pipe_onhold_count" align="center" width="50" class="i18n1" name="idbarepipeonholdcount">内光管待处理数量</th>

                <th field="id_bare_pipe_grinded_count" align="center" width="50" class="i18n1" name="idbarepipegrindedcount">内光管修磨数量</th>
                <th field="id_bare_pipe_cut_count" align="center" width="50" class="i18n1" name="idbarepipecutcount">内光管切长数量</th>
                <th field="id_coated_pipe_rejected_count" align="center" width="50" class="i18n1" name="idcoatedpiperejectedcount">内涂层管废管数量</th>
                <th field="id_coated_pipe_strip_count" align="center" width="50" class="i18n1" name="idcoatedpipestripcount">内涂层管扒皮数量</th>

                <th field="od_test_pipe_no_dayshift" align="center" width="50" class="i18n1" name="odtestpipenodayshift">实验管管号白班</th>
                <th field="od_test_pipe_length_before_cut_dayshift" align="center" width="50" class="i18n1" name="odtestpipelengthbeforecutdayshift">实验管原始长度白班</th>
                <th field="od_test_pipe_cutting_length_dayshift" align="center" width="50" class="i18n1" name="odtestpipecuttinglengthdayshift">实验管切除长度白班</th>

                <th field="od_test_pipe_no_nightshift" align="center" width="50" class="i18n1" name="odtestpipenonightshift">实验管管号夜班</th>
                <th field="od_test_pipe_length_before_cut_nightshift" align="center" width="50" class="i18n1" name="odtestpipelengthbeforecutnightshift">实验管原始长度夜班</th>
                <th field="od_test_pipe_cutting_length_nightshift" align="center" width="50" class="i18n1" name="odtestpipecuttinglengthnightshift">实验管切除长度夜班</th>

                <th field="od_test_pipe_count" align="center" width="50" class="i18n1" name="odtestpipecount">实验管数量</th>
                <th field="rebevel_pipe_count" align="center" width="50" class="i18n1" name="rebevelpipecount">需倒棱管数量</th>
                <th field="pipe_accepted_count_after_rebevel" align="center" width="50" class="i18n1" name="pipeacceptedcountafterrebevel">倒棱合格管数量</th>
                <th field="pipe_delivered_count" align="center" width="50" class="i18n1" name="pipedeliveredcount">成品发运数量</th>


                <th field="pipe_delivered_length" align="center" width="50" class="i18n1" name="pipedeliveredlength">成品发运长度</th>

                 </tr>
            </thead>
        </table>

    </div>
</fieldset>

<!--工具栏-->
<div id="hldailyRptTb" style="padding:10px;">

    <span class="i18n1" name="projectno">项目编号</span>:
    <input id="projectno" name="projectno" style="line-height:22px;border:1px solid #ccc">
    <span class="i18n1" name="begintime">开始时间</span>:
    <input id="begintime" name="begintime" type="text" class="easyui-datebox" data-options="formatter:myformatter,parser:myparser">
    <span class="i18n1" name="endtime">结束时间</span>:
    <input id="endtime" name="endtime" type="text" class="easyui-datebox" data-options="formatter:myformatter,parser:myparser">
    <a href="#" class="easyui-linkbutton" plain="true" data-options="iconCls:'icon-search'" onclick="searchDailyProRpt()">Search</a>
    <a href="#" id="addDialyReportLinkBtn" class="easyui-linkbutton i18n1" name="addDialyReport" onclick="addDialyReportLinkBtn()">生成报表</a>
    <a href="#" id="downloadDialyReportLinkBtn" class="easyui-linkbutton i18n1" name="downloadDialyReport" onclick="downloadDialyReportLinkBtn()">下载报表</a>
    <div style="float:right">

        <a href="#" id="addObpLinkBtn" class="easyui-linkbutton i18n1" name="add" data-options="iconCls:'icon-add',plain:true" onclick="addDailyProRpt()">添加</a>
        <a href="#" id="editObpLinkBtn" class="easyui-linkbutton i18n1" name="edit" data-options="iconCls:'icon-edit',plain:true" onclick="editDailyProRpt()">修改</a>
        <a href="#" id="deltObpLinkBtn" class="easyui-linkbutton i18n1" name="delete" data-options="iconCls:'icon-remove',plain:true" onclick="delDailyProRpt()">删除</a>
    </div>
</div>

<!--添加、修改框-->
<div id="hldailyProRptDialog" class="easyui-dialog" data-options="title:'添加',modal:true"  closed="true" buttons="#dlg-buttons" style="display: none;padding:5px;width:950px;max-height:500px;overflow-y:auto;">
    <form id="dailyProRptForm" method="post">

        <fieldset style="width:900px;border:solid 1px #aaa;margin-top:8px;position:relative;">
            <legend class="i18n1" name="odproductioninfo">当日生产统计</legend>

            <table class="ht-table">
                <tr>
                    <td class="i18n1" name="id" width="20%">流水号</td>
                    <td colspan="1" width="30%">
                        <%--<label class="hl-label" id="dprid"></label>--%>
                        <input class="easyui-textbox" type="text" id="dprid" name="dprid" readonly="true" value="0"/>
                    </td>
                    <td></td>
                    <td class="i18n1" name="productiondate" width="20%">生产日期</td>
                    <td colspan="1" width="30%">
                        <input class="easyui-datetimebox" id="production-date" type="text" readonly="true" name="production-date" value="" data-options="formatter:myformatter2,parser:myparser2"/>

                    </td>
                    <td></td>
                </tr>
            </table>

            <table class="ht-table">
                <tr>
                    <td width="16%" class="i18n1" name="projectno">项目编号</td>
                    <td><input class="easyui-textbox" type="text" name="project_no" readonly="true" value=""/></td>
                    <td></td>
                    <td width="16%" class="i18n1" name="projectname">项目名称</td>
                    <td><input class="easyui-textbox" readonly="true" type="text" name="project_name" value=""/></td>
                    <td></td>
                </tr>
                <tr>
                    <td width="16%" class="i18n1" name="odwt">外径*壁厚</td>
                    <td><input class="easyui-textbox" type="text" readonly="true" name="od_wt" value=""/></td>
                    <td></td>
                    <td width="16%" class="i18n1" name="odcoatingtype">外涂种类</td>
                    <td><input class="easyui-textbox" type="text" name="od_coating_type" readonly="true" value=""/></td>
                    <td></td>
                </tr>
                <tr>
                    <td width="16%" class="i18n1" name="barepipecount">光管接收数量</td>
                    <td><input class="easyui-numberbox" data-options="min:0,precision:0" type="text" name="bare_pipe_count" value=""/></td>
                    <td></td>
                    <td width="16%" class="i18n1" name="barepipelength">光管接收长度</td>
                    <td><input class="easyui-numberbox" data-options="min:0,precision:2" type="text" name="bare_pipe_length" value=""/></td>
                    <td></td>
                </tr>
                <tr>
                    <td width="16%" class="i18n1" name="odtotalcoatedcount">外防涂敷数量</td>
                    <td><input class="easyui-numberbox" data-options="min:0,precision:0" type="text" name="od_total_coated_count" value=""/></td>
                    <td></td>
                    <td width="16%" class="i18n1" name="odtotalacceptedcount">外防涂敷合格数量</td>
                    <td><input class="easyui-numberbox" data-options="min:0,precision:0" type="text" name="od_total_accepted_count" value=""/></td>
                    <td></td>
                </tr>
                <tr>

                    <td width="16%" class="i18n1" name="odaimingacceptedcount">外防目标涂敷数量</td>
                    <td><input class="easyui-numberbox" data-options="min:0,precision:0" type="text" name="od_aiming_accepted_count" value=""/></td>
                    <td></td>
                    <td width="16%" class="i18n1" name="odtotalacceptedlength">外防合格涂敷长度</td>
                    <td><input class="easyui-numberbox" data-options="min:0,precision:2" type="text" name="od_total_accepted_length" value=""/></td>
                    <td></td>
                </tr>
                <tr>

                    <td width="16%" class="i18n1" name="odaimingtotalacceptedlength">外防目标合格涂敷长度</td>
                    <td><input class="easyui-numberbox" data-options="min:0,precision:2" type="text" name="od_aiming_total_accepted_length" value=""/></td>
                    <td></td>
                    <td width="16%" class="i18n1" name="odrepairpipecount">外防修补管数</td>
                    <td><input class="easyui-numberbox" data-options="min:0,precision:0" type="text" name="od_repair_pipe_count" value=""/></td>
                    <td></td>
                </tr>

                <tr>
                    <td width="16%" class="i18n1" name="odbarepipeonholdcount">外光管待处理数量</td>
                    <td><input class="easyui-numberbox" data-options="min:0,precision:0" type="text" name="od_bare_pipe_onhold_count" value=""/></td>
                    <td></td>
                    <td width="16%" class="i18n1" name="odbarepipegrindedcount">外光管修磨数量</td>
                    <td><input class="easyui-numberbox" data-options="min:0,precision:0" type="text" name="od_bare_pipe_grinded_count" value=""/></td>
                    <td></td>
                </tr>
                <tr>
                    <td width="16%" class="i18n1" name="odbarepipecutcount">外光管切长数量</td>
                    <td><input class="easyui-numberbox" data-options="min:0,precision:0" type="text" name="od_bare_pipe_cut_count" value=""/></td>
                    <td></td>
                    <td width="16%" class="i18n1" name="odcoatedpiperejectedcount">外涂层管废管数量</td>
                    <td><input class="easyui-numberbox" data-options="min:0,precision:0" type="text" name="od_coated_pipe_rejected_count" value=""/></td>
                    <td></td>
                </tr>

                <tr>
                    <td width="16%" class="i18n1" name="odcoatedpipestripcount">外涂层管扒皮数量</td>
                    <td><input class="easyui-numberbox" data-options="min:0,precision:0" type="text" name="od_coated_pipe_strip_count" value=""/></td>
                    <td></td>
                    <td width="16%" class="i18n1" name="idtotalcoatedcount">内防涂敷数量</td>
                    <td><input class="easyui-numberbox" data-options="min:0,precision:0" type="text" name="id_total_coated_count" value=""/></td>
                    <td></td>
                </tr>

                <tr>
                    <td width="16%" class="i18n1" name="idtotalacceptedcount">内防涂敷合格数量</td>
                    <td><input class="easyui-numberbox" data-options="min:0,precision:0" type="text" name="id_total_accepted_count" value=""/></td>
                    <td></td>
                    <td width="16%" class="i18n1" name="idaimingacceptedcount">内防目标涂敷数量</td>
                    <td><input class="easyui-numberbox" data-options="min:0,precision:0" type="text" name="id_aiming_accepted_count" value=""/></td>
                    <td></td>
                </tr>

                <tr>
                    <td width="16%" class="i18n1" name="idtotalacceptedlength">内防合格涂敷米数</td>
                    <td><input class="easyui-numberbox" data-options="min:0,precision:2" type="text" name="id_total_accepted_length" value=""/></td>
                    <td></td>
                    <td width="16%" class="i18n1" name="idaimingtotalacceptedlength">内防目标合格涂敷米数</td>
                    <td><input class="easyui-numberbox" data-options="min:0,precision:2" type="text" name="id_aiming_total_accepted_length" value=""/></td>
                    <td></td>
                </tr>

                <tr>
                    <td width="16%" class="i18n1" name="idrepairpipecount">内防修补管数</td>
                    <td><input class="easyui-numberbox" data-options="min:0,precision:0" type="text" name="id_repair_pipe_count" value=""/></td>
                    <td></td>
                    <td width="16%" class="i18n1" name="idbarepipeonholdcount">内光管待处理数量</td>
                    <td><input class="easyui-numberbox" data-options="min:0,precision:0" type="text" name="id_bare_pipe_onhold_count" value=""/></td>
                    <td></td>
                </tr>

                <tr>
                    <td width="16%" class="i18n1" name="idbarepipegrindedcount">内光管修磨数量</td>
                    <td><input class="easyui-numberbox" data-options="min:0,precision:0" type="text" name="id_bare_pipe_grinded_count" value=""/></td>
                    <td></td>
                    <td width="16%" class="i18n1" name="idbarepipecutcount">内光管切长数量</td>
                    <td><input class="easyui-numberbox" data-options="min:0,precision:0" type="text" name="id_bare_pipe_cut_count" value=""/></td>
                    <td></td>
                </tr>

                <tr>
                    <td width="16%" class="i18n1" name="idcoatedpiperejectedcount">内涂层管废管数量</td>
                    <td><input class="easyui-numberbox" data-options="min:0,precision:0" type="text" name="id_coated_pipe_rejected_count" value=""/></td>
                    <td></td>
                    <td width="16%" class="i18n1" name="idcoatedpipestripcount">内涂层管扒皮数量</td>
                    <td><input class="easyui-numberbox" data-options="min:0,precision:0" type="text" name="id_coated_pipe_strip_count" value=""/></td>
                    <td></td>
                </tr>

                <tr>
                    <td width="16%" class="i18n1" name="odtestpipenodayshift">实验管管号白班</td>
                    <td><input class="easyui-textbox" type="text" name="od_test_pipe_no_dayshift" value=""/></td>
                    <td></td>
                    <td width="16%" class="i18n1" name="odtestpipelengthbeforecutdayshift">实验管原始长度白班</td>
                    <td><input class="easyui-numberbox" data-options="min:0,precision:2" type="text" name="od_test_pipe_length_before_cut_dayshift" value=""/></td>
                    <td></td>
                </tr>

                <tr>
                    <td width="16%" class="i18n1" name="odtestpipecuttinglengthdayshift">实验管切除长度白班</td>
                    <td><input class="easyui-numberbox" data-options="min:0,precision:2" type="text" name="od_test_pipe_cutting_length_dayshift" value=""/></td>
                    <td></td>
                    <td width="16%" class="i18n1" name="odtestpipenonightshift">实验管管号夜班</td>
                    <td><input class="easyui-textbox" type="text" name="od_test_pipe_no_nightshift" value=""/></td>
                    <td></td>
                </tr>

                <tr>
                    <td width="16%" class="i18n1" name="odtestpipelengthbeforecutnightshift">实验管原始长度夜班</td>
                    <td><input class="easyui-numberbox" data-options="min:0,precision:2" type="text" name="od_test_pipe_length_before_cut_nightshift" value=""/></td>
                    <td></td>
                    <td width="16%" class="i18n1" name="odtestpipecuttinglengthnightshift">实验管切除长度夜班</td>
                    <td><input class="easyui-numberbox" data-options="min:0,precision:2" type="text" name="od_test_pipe_cutting_length_nightshift" value=""/></td>
                    <td></td>
                </tr>

                <tr>
                    <td width="16%" class="i18n1" name="odtestpipecount">实验管数量</td>
                    <td><input class="easyui-numberbox" data-options="min:0,precision:0" type="text" name="od_test_pipe_count" value=""/></td>
                    <td></td>
                    <td width="16%" class="i18n1" name="rebevelpipecount">需倒棱管数量</td>
                    <td><input class="easyui-numberbox" data-options="min:0,precision:0" type="text" name="rebevel_pipe_count" value=""/></td>
                    <td></td>
                </tr>

                <tr>

                    <td width="16%" class="i18n1" name="pipeacceptedcountafterrebevel">倒棱合格管数量</td>
                    <td><input class="easyui-numberbox" data-options="min:0,precision:0" type="text" name="pipe_accepted_count_after_rebevel" value=""/></td>
                    <td></td>
                    <td width="16%" class="i18n1" name="pipedeliveredcount">成品发运数量</td>
                    <td><input class="easyui-numberbox" data-options="min:0,precision:0" type="text" name="pipe_delivered_count" value=""/></td>
                    <td></td>
                </tr>
                <tr>
                    <td width="16%" class="i18n1" name="pipedeliveredlength">成品发运长度</td>
                    <td><input class="easyui-numberbox" data-options="min:0,precision:2" type="text" name="pipe_delivered_length" value=""/></td>
                    <td></td>

                </tr>

            </table>
            <input type="hidden" id="fileslist" name="upload_files" value=""/>


        </fieldset>
    </form>


</div>
<div id="dlg-buttons" align="center" style="width:900px;">
    <a href="#" class="easyui-linkbutton" iconCls="icon-save" onclick="dailyProductionReportFormSubmit()">Save</a>
    <a href="#" class="easyui-linkbutton" id="hlcancelBtn" operationtype="add" iconCls="icon-cancel" onclick="dailyProductionReportCancelSubmit()">Cancel</a>
</div>


<script type="text/javascript" src="../easyui/jquery.easyui.min.js"></script>
</body>
</html>
<script type="text/javascript">
    mini.parse();

    hlLanguage("../i18n/");
</script>