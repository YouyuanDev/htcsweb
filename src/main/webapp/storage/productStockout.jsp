<%--
  Created by IntelliJ IDEA.
  User: kurt
  Date: 2/26/18
  Time: 10:51 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>涂层成品出厂</title>
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

        function searchPipe() {

            $('#pipeDatagrids').datagrid('load',{
                'project_no': $('#projectno').val(),
                'contract_no': $('#contractno').val(),
                'pipe_no':$('#pipeno').val(),
                'status':$('#status').val()
            });
        }
        $(function () {


            $('.mini-buttonedit .mini-buttonedit-input').css('width','150px');

        });




        function CoatedPipeStockOut() {
            var row = $('#pipeDatagrids').datagrid('getSelections');
            if(row.length>0){
                var idArr=[];
                for (var i=0;i<row.length;i++){
                    idArr.push(row[i].id);
                }
                var idArrs=idArr.join(',');

                $.messager.confirm('系统提示',"您确定要将这<font color=red>"+idArr.length+ "</font>根成品管出厂吗？",function (r) {
                    if(r){
                        $.post(
                            "/pipeinfo/coatingProductStockout.action",
                            {"hlparam":idArrs},function (data) {
                                if(data.success){
                                    $("#pipeDatagrids").datagrid("reload");
                                }
                                hlAlertFour(data.message);
                            },"json");
                    }
                });

                //hlAlertFive("/pipeinfo/delPipe.action",idArrs,idArr.length);
                // $.messager.confirm('提示','您确定要删除<font>')
            }else{
                $.messager.alert('Warning','请选择要出厂的成品涂层管或光管!');
            }
        }



    </script>

</head>
<body>
<fieldset class="b3" style="padding:10px;margin:10px;">
    <legend> <h3><b style="color: orange" >|&nbsp;</b><span class="i18n1" name="datadisplay">数据展示</span></h3></legend>
    <div  style="margin-top:5px;">
        <table class="easyui-datagrid" id="pipeDatagrids" url="/pipeinfo/getCoatedStockinPipeInfoByLike.action" striped="true" loadMsg="正在加载中。。。" textField="text" pageSize="20" fitColumns="true" pagination="true" toolbar="#hlpipeTb">
            <thead>
            <tr>
                <th data-options="field:'ck',checkbox:true"></th>
                <th field="id" align="center" width="100" class="i18n1" name="id">流水号</th>
                <th field="pipe_no" align="center" width="100" class="i18n1" name="pipeno">钢管编号</th>
                <th field="project_no" align="center" width="100" class="i18n1" name="projectno">项目编号</th>
                <th field="project_name" align="center" width="100" class="i18n1" name="projectname">项目名称</th>
                <th field="contract_no" align="center" width="100" class="i18n1" name="contractno">合同编号</th>
                <th field="grade" align="center" width="100" class="i18n1" name="grade">钢种</th>
                <th field="heat_no" align="center" width="100" class="i18n1" name="heatno">炉号</th>
                <th field="pipe_making_lot_no" align="center" width="100" class="i18n1" name="pipemakinglotno">制管批号</th>
                <th field="od" align="center" width="100" class="i18n1" name="od">外径</th>
                <th field="wt" align="center" width="100" class="i18n1" name="wt">壁厚</th>
                <th field="p_length" align="center" width="100" class="i18n1" name="p_length">钢管长度</th>
                <th field="weight" align="center" width="100" class="i18n1" name="weight">钢管米重</th>
                <th field="status" align="center" width="100" class="i18n1" name="status">状态</th>
                <th field="storage_stack" align="center" width="100" class="i18n1" name="storagestack">垛位号</th>
                <th field="stack_level" align="center" width="100" class="i18n1" name="stacklevel">层数</th>
            </tr>
            </thead>
        </table>

    </div>
</fieldset>

<!--工具栏-->
<div id="hlpipeTb" style="padding:10px;">
    <span class="i18n1" name="pipeno">钢管编号</span>:
    <input id="pipeno" name="pipeno" style="width:100px;line-height:22px;border:1px solid #ccc">
    <span class="i18n1" name="contractno">合同编号</span>:
    <input id="contractno" name="contractno" style="width:100px;line-height:22px;border:1px solid #ccc">
    <span class="i18n1" name="projectno">项目编号</span>:
    <input id="projectno" name="projectno"  style="width:100px;line-height:22px;border:1px solid #ccc">
    <span class="i18n1" name="status">状态</span>:
    <select id="status" class="easyui-combobox" data-options="editable:false" name="status" style="width:200px;">
        <option value="odstockin"><span class="i18n1" name="odstockin">外涂成品</span></option>
        <option value="idstockin"><span class="i18n1" name="idstockin">内(外)涂成品</span></option>
    </select>

    <a href="#" class="easyui-linkbutton" plain="true" data-options="iconCls:'icon-search'" onclick="searchPipe()">Search</a>
    <div style="float:right">
        <a href="#" id="CoatingProductStockOutLinkBtn" class="easyui-linkbutton i18n1" name="coatingproductstockout"  onclick="CoatedPipeStockOut()">成品管出厂</a>

    </div>
</div>




<script type="text/javascript" src="../easyui/jquery.easyui.min.js"></script>
</body>
</html>
<script type="text/javascript">
    mini.parse();

    hlLanguage("../i18n/");
</script>

