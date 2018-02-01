<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>信息</title>
    <link rel="stylesheet" type="text/css" href="easyui/themes/metro/easyui.css" charset="UTF-8">
    <link rel="stylesheet" type="text/css" href="easyui/themes/icon.css" charset="UTF-8">
    <script type="text/javascript" src="easyui/jquery.min.js"charset="UTF-8"></script>
    <script type="text/javascript" src="easyui/jquery.easyui.min.js" charset="UTF-8"></script>
    <script type="text/javascript" src="easyui/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>


    <style type="text/css" >
         table,table td{border-collapse:collapse;}
         table{border:1px solid #B0C4DE;}
         table tr td{border:1px solid #B0C4DE;}
        .b3{border-style:inset;border-width:thin;}
    </style>


    <script type="text/javascript">
        var url;
        //增加
        function addOdBlastPro(){
            $('#hlOdBlastProDialog').window('open');
        }
        //修改：
        function edituser(){
            var row = $('#datagrids').datagrid('getSelected');
            if(row){
                $('#dla').window({
                    title:'修改',
                    width:700,
                    height:430,
                    modal:true,
                    draggable:false,
                    resizable:false,
                    content:"<iframe src='/getodblastprocessById.action?id="+row.id+"' width='100%' height='100%'  frameborder='no' >",
                    shadow:false,
                });
                parent.$('#datagrids').datagrid('reload');
            }

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
    <a href="#" id="addObpLinkBtn" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="addOdBlastPro()">Add</a>
    <a href="#" id="editObpLinkBtn" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true" onclick="editOdBlastPro()">Edit</a>
    <a href="#" id="deltObpLinkBtn" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="delOdBlastPro()">Delete</a>
</div>

<!--添加、修改框-->
<div id="hlOdBlastProDialog" class="easyui-dialog" data-options="title:'添加',modal:true"  closed="true" buttons="#dlg-buttons" style="padding:5px;width:600px;height:auto;">
   <form id="odBlastProForm" method="post">
       <table class="ht-table">
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
</body>
</html>
