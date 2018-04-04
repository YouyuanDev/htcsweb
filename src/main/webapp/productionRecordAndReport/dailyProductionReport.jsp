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



    <script type="text/javascript">
        var url;
        var basePath ="<%=basePath%>"+"/upload/pictures/";
        $(function () {
            //删除上传的图片

            $('#hldailyProRptDialog').dialog({
                onClose:function () {
                    var type=$('#hlcancelBtn').attr('operationtype');
                    if(type=="add"){
                        var $imglist=$('#fileslist');
                        var $dialog=$('#hldailyProRptDialog');
                        hlAlertSix("../UploadFile/delUploadPicture.action",$imglist,$dialog,grid);
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

            url="/OdOperation/saveOdBlastProcess.action";
            //$("input[name='alkaline_dwell_time']").siblings().css("background-color","#F9A6A6");
        }
        function delOdBlastPro() {
            var row = $('#dailyProRptDatagrids').datagrid('getSelections');
            if(row.length>0){
                var idArr=[];
                for (var i=0;i<row.length;i++){
                    idArr.push(row[i].id);
                }
                var idArrs=idArr.join(',');
                $.messager.confirm('系统提示',"您确定要删除这<font color=red>"+idArr.length+ "</font>条数据吗？",function (r) {
                    if(r){
                        $.post("/OdOperation/delOdBlastProcess.action",{hlparam:idArrs},function (data) {
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

                $('#dprid').text(row.id);
                $('#dailyProRptForm').form('load',row);
                $('#operation-time').datetimebox('setValue',getDate1(row.operation_time));


                url="/OdOperation/saveOdBlastProcess.action?id="+row.id;
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
                    setParams($("input[name='alkaline_dwell_time']"));
                    setParams($("input[name='alkaline_concentration']"));

                    if($("input[name='pipe_no']").val()==""){

                        hlAlertFour("请选择钢管管号");
                        return false;
                    }


                },
                success: function(result){
                    clearFormLabel();
                    var result = eval('('+result+')');
                    $('#hldailyProRptDialog').dialog('close');
                    if (result.success){
                        $('#dailyProRptDatagrids').datagrid('reload');
                    }
                    hlAlertFour(result.message);

                },
                error:function () {
                    //clearFormLabel();
                    hlAlertThree();
                }
            });
            clearMultiUpload(grid);

        }
        function dailyProductionReportCancelSubmit() {
            $('#hldailyProRptDialog').dialog('close');
        }


        function  clearFormLabel(){
            $('#dailyProRptForm').form('clear');
            $('.hl-label').text('');
            $('#hl-gallery-con').empty();
            combox1.setValue("");
            $(":input").each(function () {
                $(this).siblings().css("background-color","#FFFFFF");
            });
        }
    </script>





</head>

<body>
<fieldset class="b3" style="padding:10px;margin:10px;">
    <legend> <h3><b style="color: orange" >|&nbsp;</b><span class="i18n1" name="datadisplay">数据展示</span></h3></legend>
    <div  style="margin-top:5px;">
        <table class="easyui-datagrid" id="dailyProRptDatagrids" url="/OdOperation/getNewOdBlastByLike.action" striped="true" loadMsg="正在加载中。。。" textField="text" pageSize="20" fitColumns="true" pagination="true" toolbar="#hlOdBlastProTb">
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
<div id="hlOdBlastProTb" style="padding:10px;">
    <span class="i18n1" name="millno">分厂编号</span>:
    <input id="millno" class="easyui-combobox" type="text" name="millno"  data-options=
            "url:'/millInfo/getAllMillsWithComboboxSelectAll.action',
					        method:'get',
					        valueField:'id',
					        width: 150,
					        editable:false,
					        textField:'text',
					        panelHeight:'auto'"/>
    <span class="i18n1" name="pipeno">钢管编号</span>:
    <input id="pipeno" name="pipeno" style="line-height:22px;border:1px solid #ccc">
    <span class="i18n1" name="operatorno">操作工编号</span>:
    <input id="operatorno" name="operatorno" style="line-height:22px;border:1px solid #ccc">
    <span class="i18n1" name="begintime">开始时间</span>:
    <input id="begintime" name="begintime" type="text" class="easyui-datebox" data-options="formatter:myformatter,parser:myparser">
    <span class="i18n1" name="endtime">结束时间</span>:
    <input id="endtime" name="endtime" type="text" class="easyui-datebox" data-options="formatter:myformatter,parser:myparser">
    <a href="#" class="easyui-linkbutton" plain="true" data-options="iconCls:'icon-search'" onclick="searchDailyProRpt()">Search</a>
    <div style="float:right">
        <a href="#" id="addObpLinkBtn" class="easyui-linkbutton i18n1" name="add" data-options="iconCls:'icon-add',plain:true" onclick="addDailyProRpt()">添加</a>
        <a href="#" id="editObpLinkBtn" class="easyui-linkbutton i18n1" name="edit" data-options="iconCls:'icon-edit',plain:true" onclick="editDailyProRpt()">修改</a>
        <a href="#" id="deltObpLinkBtn" class="easyui-linkbutton i18n1" name="delete" data-options="iconCls:'icon-remove',plain:true" onclick="delDailyProRpt()">删除</a>
    </div>
</div>

<!--添加、修改框-->
<div id="hldailyProRptDialog" class="easyui-dialog" data-options="title:'添加',modal:true"  closed="true" buttons="#dlg-buttons" style="display: none;padding:5px;width:950px;height:auto;">
    <form id="dailyProRptForm" method="post">

        <fieldset style="width:900px;border:solid 1px #aaa;margin-top:8px;position:relative;">
            <legend class="i18n1" name="odproductioninfo">当日生产统计</legend>

            <table class="ht-table">
                <tr>
                    <td class="i18n1" name="id" width="20%">流水号</td>
                    <td colspan="1" width="30%"><label class="hl-label" id="dprid"></label></td>
                    <td></td>
                    <td class="i18n1" name="operationtime" width="20%">生产日期</td>
                    <td colspan="1" width="30%">
                        <input class="easyui-datetimebox" id="production_date" type="text" name="production_date" value="" data-options="formatter:myformatter2,parser:myparser2"/>

                    </td>
                    <td></td>
                </tr>
            </table>

            <table class="ht-table">
                <tr>
                    <td width="16%" class="i18n1" name="projectno">项目编号</td>
                    <td><label class="hl-label" name="project_no"></label></td>
                    <td></td>
                    <td width="16%" class="i18n1" name="projectname">项目名称</td>
                    <td><label class="hl-label" name="project_name"></label></td>
                    <td></td>
                </tr>
                <tr>
                    <td width="16%" class="i18n1" name="odwt">外径*壁厚</td>
                    <td><input class="easyui-textbox" type="text" name="od_wt" value=""/></td>
                    <td></td>
                    <td width="16%" class="i18n1" name="odcoatingtype">外涂种类</td>
                    <td><input class="easyui-textbox" type="text" name="od_coating_type" value=""/></td>
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
                    <td width="16%" class="i18n1" name="odbarepipeonholdcount">光管待处理数量</td>
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

            </table>
            <input type="hidden" id="fileslist" name="upload_files" value=""/>


        </fieldset>
    </form>


</div>
<div id="dlg-buttons" align="center" style="width:900px;">
    <a href="#" class="easyui-linkbutton" iconCls="icon-save" onclick="dailyProductionReportFormSubmit()">Save</a>
    <a href="#" class="easyui-linkbutton" id="hlcancelBtn" operationtype="add" iconCls="icon-cancel" onclick="dailyProductionReportCancelSubmit()">Cancel</a>
</div>
<div id="gridPanel1" class="mini-panel" title="header" iconCls="icon-add" style="width:450px;height:250px;"
     showToolbar="true" showCloseButton="true" showHeader="false" bodyStyle="padding:0" borderStyle="border:0"
>
    <div property="toolbar" id="searchBar1" style="padding:5px;padding-left:8px;text-align:center;display: none">
        <div style="float:left;padding-bottom:2px;">
            <span class="i18n1" name="pipeno">钢管编号</span><span>:</span>
            <input id="keyText1" class="mini-textbox" style="width:110px;" onenter="onSearchClick(1)"/>
            <a class="mini-button" onclick="onSearchClick(1)">查找</a>
            <a class="mini-button" onclick="onClearClick(1)" name="clear">清除</a>
        </div>
        <div style="float:right;padding-bottom:2px;">
            <a class="mini-button" onclick="onCloseClick(1)" name="close">关闭</a>
        </div>
        <div style="clear:both;"></div>
    </div>
    <div id="datagrid1" class="mini-datagrid" style="width:100%;height:100%;"
         borderStyle="border:0" showPageSize="false" showPageIndex="false"
         url="/pipeinfo/getPipeNumbers.action">
        <div property="columns">
            <div type="checkcolumn" ></div>
            <div field="pipe_no" width="80" headerAlign="center" allowSort="true" class="i18n1" name="pipeno">钢管编号</div>
            <div field="contract_no" width="80" headerAlign="center" allowSort="true" class="i18n1" name="contractno">合同编号</div>
            <div field="status" width="40" headerAlign="center" allowSort="true" class="i18n1" name="status">状态</div>
            <div field="od" width="40" headerAlign="center" allowSort="true" class="i18n1" name="od">外径</div>
            <div field="wt" width="40" headerAlign="center" allowSort="true" class="i18n1" name="wt">壁厚</div>
            <div field="p_length" width="40" headerAlign="center" allowSort="true" class="i18n1" name="p_length">长度</div>
            <div field="weight" width="40" headerAlign="center" allowSort="true" class="i18n1" name="weight">重量</div>
        </div>
    </div>
</div>
<div id="gridPanel2" class="mini-panel" title="header" iconCls="icon-add" style="width:480px;height:250px;"
     showToolbar="true" showCloseButton="true" showHeader="false" bodyStyle="padding:0" borderStyle="border:0"
>
    <div property="toolbar" id="searchBar2" style="padding:5px;text-align:center;display:none;">
        <div style="float:left;padding-bottom:2px;">
            <span class="i18n1" name="operatorno">操作工编号</span><span>:</span>
            <input id="keyText3" class="mini-textbox" style="width:110px;" onenter="onSearchClick(2)"/>
            <span class="i18n1" name="operatorname">姓名</span><span>:</span>
            <input id="keyText4" class="mini-textbox" style="width:110px;" onenter="onSearchClick(2)"/>
            <a class="mini-button" onclick="onSearchClick(2)" name="search">查找</a>
            <a class="mini-button" onclick="onClearClick(2)" name="clear">清除</a>
            <a class="mini-button" onclick="onCloseClick(2)" name="close">关闭</a>
        </div>
        <div style="clear:both;"></div>
    </div>
    <div id="datagrid2" class="mini-datagrid" style="width:100%;height:100%;"
         borderStyle="border:0" showPageSize="false" showPageIndex="false"
         url="/person/getPersonNoByName.action">
        <div property="columns">
            <div type="checkcolumn" ></div>
            <div field="employee_no" width="60" headerAlign="center" allowSort="true" class="i18n1" name="operatorno">操作工编号</div>
            <div field="pname" width="60" headerAlign="center" allowSort="true" class="i18n1" name="operatorname">姓名</div>
        </div>
    </div>
</div>
<script type="text/javascript" src="../easyui/jquery.easyui.min.js"></script>
</body>
</html>
<script type="text/javascript">
    mini.parse();
    var keyText1=mini.get('keyText1');
    var keyText4 = mini.get("keyText4");
    var keyText3=mini.get("keyText3");
    var grid1=mini.get("datagrid1");
    var grid2=mini.get("datagrid2");






    hlLanguage("../i18n/");
</script>