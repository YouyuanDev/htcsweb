<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>信息</title>
    <link rel="stylesheet" type="text/css" href="../easyui/themes/bootstrap/easyui.css">


    <link rel="stylesheet" type="text/css" href="../easyui/themes/icon.css">

    <script type="text/javascript" src="../easyui/jquery.min.js"></script>
    <script src="../js/jquery.i18n.properties-1.0.9.js" type="text/javascript"></script>
    <script src="../js/language.js" type="text/javascript"></script>
    <script src="../js/common.js" type="text/javascript"></script>
    <style type="text/css" >
        .ht-table,.ht-table td{border-collapse:collapse;border:1px solid #F0F0F0;}
        .ht-table{width:100%;margin-bottom:10px;}
        .ht-table tr td:nth-child(1){width:180px;padding:5px;}
        .ht-table tr td:nth-child(2){width:280px;padding:2px;}
        .ht-table tr td:nth-child(3){width:140px;padding:5px;}
        .ht-table tr td input{width:280px;height:35px;}
        .hltr{border-bottom:2px solid #1f1f1f ;}
        .b3{border-style:inset;border-width:thin;}
    </style>


    <script type="text/javascript">

        $(function () {

                    $('#odBlastProDatagrids').datagrid({
                        striped:true,
                        loadMsg:'正在加载中。。。',
                        pagination:true,
                        textField:'text',
                        rownumbers:true,
                        pageList:[10,20,30,50,100],
                        pageSize:20,
                        fitColumns:true,
                        url:'/OdOperation/getOdBlastByLike.action',
                        toolbar:'#hlOdBlastProTb',
                        columns:[[
                            { field: '',checkbox:true},
                            { title: '流水号',field: 'id',width:100},
                            { title: '钢管编号',field: 'pipe_no',width:100},
                            { title: '操作时间',field : 'operation_time', formatter:function(value,row,index){
                                    var operation_time = new Date(value);
                                     return operation_time.toLocaleString();
                                 } ,width:200},
                            { title: '操作工编号',field: 'operator_no',width:100},
                            { title: '外观缺陷',field: 'surface_condition',width:100},
                            { title: '打砂前盐度',field: 'salt_contamination_before_blasting',width:100},
                            { title: '碱洗时间',field: 'alkaline_dwell_time',width:100},
                            { title: '碱浓度',field: 'alkaline_concentration',width: 100},
                            { title: '传导性',field: 'conductivity',width:120},
                            { title: '酸洗时间',field: 'acid_wash_time',width:120},
                            { title: '酸浓度',field: 'acid_concentration',width:120},
                            { title: '打砂传送速度',field: 'blast_line_speed',width:120},
                            { title: '预热温度',field: 'preheat_temp',width:120},
                            { title: '备注',field: 'remark',width:120}
                        ]]
                    });

        });
        function addOdBlastPro(){
            $('#hlOdBlastProDialog').dialog('open').dialog('setTitle','新增');
            $('#odBlastProForm').form('clear');$('#odbpid').text('');$('#odbptime').text('');
            url="/OdOperation/saveOdBlastProcess.action";
        }
        function delOdBlastPro() {
            var row = $('#odBlastProDatagrids').datagrid('getSelections');
            if(row.length>0){
                var idArr=[];
                for (var i=0;i<row.length;i++){
                    idArr.push(row[i].id);
                }
                var idArrs=idArr.join(',');
                hlAlertFive("/OdOperation/delOdBlastProcess.action",idArrs,idArr.length);
                // $.messager.confirm('提示','您确定要删除<font>')
            }else{
                hlAlertOne();
            }
        }
        function editOdBlastPro() {
            var row = $('#odBlastProDatagrids').datagrid('getSelected');
            if(row){
                $('#hlOdBlastProDialog').dialog('open').dialog('setTitle','修改');
                var odbpid=row.id;
                var odbptime=getDate(row.operation_time);
                $('#odbpid').text(odbpid);$('#odbptime').text(odbptime);
                $('#odBlastProForm').form('load',row);
                url="/OdOperation/saveOdBlastProcess.action?id="+row.id;
            }else{
                hlAlertTwo();
            }
        }
        function searchOdBlastPro() {
            $('#odBlastProDatagrids').datagrid('load',{
                'pipe_no': $('#pipeno').val(),
                'operator_no': $('#operatorno').val(),
                'begin_time': $('#begintime').val(),
                'end_time': $('#endtime').val()
            });
        }
        function odBlastProFormSubmit() {
            $('#odBlastProForm').form('submit',{
                url:url,
                success: function(result){
                    var result = eval('('+result+')');
                    if (result.success){
                        $('#hlOdBlastProDialog').dialog('close');
                        $('#odBlastProDatagrids').datagrid('reload');    // reload the user data
                    } else {
                        hlAlertFour("操作失败!");
                    }
                },
                error:function () {
                    hlAlertThree();
                }

            });
        }
    </script>





</head>

<body>


<fieldset class="b3" style="padding:10px;margin:10px;">
    <legend> <h3><b style="color: orange" >|&nbsp;</b>数据展示</h3></legend>
    <div id="odBlastProDatagrids" style=" margin-top: 210px;"></div>
</fieldset>

<!--工具栏-->
<div id="hlOdBlastProTb" style="padding:10px;">
    <span>钢管编号:</span>
    <input id="pipeno" name="pipeno" style="line-height:26px;border:1px solid #ccc">
    <span>操作工编号:</span>
    <input id="operatorno" name="operatorno" style="line-height:26px;border:1px solid #ccc">
    <span>开始时间:</span>
    <input id="begintime" name="begintime" type="text" class="easyui-datebox">
    <span>结束时间:</span>
    <input id="endtime" name="endtime" type="text" class="easyui-datebox">
    <a href="#" class="easyui-linkbutton" plain="true" data-options="iconCls:'icon-search'" onclick="searchOdBlastPro()">Search</a>
    <div style="float:right">
     <a href="#" id="addObpLinkBtn" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="addOdBlastPro()">Add</a>
     <a href="#" id="editObpLinkBtn" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true" onclick="editOdBlastPro()">Edit</a>
     <a href="#" id="deltObpLinkBtn" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="delOdBlastPro()">Delete</a>
    </div>
</div>

<!--添加、修改框-->
<div id="hlOdBlastProDialog" class="easyui-dialog" data-options="title:'添加',modal:true"  closed="true" buttons="#dlg-buttons" style="display: none;padding:5px;width:600px;height:auto;">
   <form id="odBlastProForm" method="post">
       <table class="ht-table">
           <tr>
               <td>流水号</td>
               <td><label id="odbpid"></label></td>
               <td></td>
           </tr>
           <tr>
               <td>钢管编号</td>
               <td><input class="easyui-validatebox" type="text" name="pipe_no"/></td>
               <td></td>
           </tr>
           <tr>
               <td>操作工编号</td>
               <td><input class="easyui-validatebox" type="text" name="operator_no"/></td>
               <td></td>
           </tr>
           <tr>
               <td>操作时间</td>
               <td><label id="odbptime"></label></td>
               <td></td>
           </tr>
       </table>
<hr>
       <table class="ht-table">
           <tr>
                   <td>碱浓度</td>
                   <td><input class="easyui-validatebox" type="text" name="alkaline_concentration"/></td>
           </tr>
           <tr>
                   <td>碱洗时间(h)</td>
                   <td><input class="easyui-validatebox" type="text" name="alkaline_dwell_time"/></td>
                   <td>10~20</td>
           </tr>

           <tr>
               <td>酸洗时间</td>
               <td><input class="easyui-validatebox" type="text" name="acid_wash_time"/></td>
           </tr>
           <tr>
               <td>酸浓度</td>
               <td><input class="easyui-validatebox" type="text" name="acid_concentration"/></td>
           </tr>

       </table>
       <hr>
       <table class="ht-table">
           <tr>
               <td>外观缺陷</td>
               <td><input class="easyui-validatebox" type="text" name="surface_condition"/></td>
           </tr>
           <tr>
               <td>打砂前盐度</td>
               <td><input class="easyui-validatebox" type="text" name="salt_contamination_before_blasting"/></td>
               <td>80~100</td>
           </tr>
           <tr>
               <td>打砂传送速度(m/s)</td>
               <td><input class="easyui-validatebox" type="text" name="blast_line_speed"/></td>
           </tr>
           <tr>
               <td>传导性</td>
               <td><input class="easyui-validatebox" type="text" name="conductivity"/></td>
           </tr>

           <tr>
               <td>预热温度(℃)</td>
               <td><input class="easyui-validatebox" type="text" name="preheat_temp"/></td>
           </tr>
           <tr>
               <td>备注</td>
               <td><input class="easyui-textbox" type="text" name="remark" data-options="multiline:true" style="height:60px"/></td>
           </tr>
       </table>
   </form>
</div>
<div id="dlg-buttons">
    <a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="odBlastProFormSubmit()">Ok</a>
    <a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#hlOdBlastProDialog').dialog('close')">Cancel</a>
</div>
<script type="text/javascript" src="../easyui/jquery.easyui.min.js" charset="UTF-8"></script>
</body>
</html>
