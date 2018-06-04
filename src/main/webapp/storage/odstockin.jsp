<%--
  Created by IntelliJ IDEA.
  User: kurt
  Date: 2/26/18
  Time: 5:47 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>外防成品入库</title>
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
                'pipe_no':$('#pipeno').val()
            });
        }
        $(function () {


            $('.mini-buttonedit .mini-buttonedit-input').css('width','150px');

        });

        function openPipeStockInPage(){
            var row = $('#pipeDatagrids').datagrid('getSelections');
            if(row.length==0){
                $.messager.alert('Warning','请选择要入外防成品库的钢管!');
                return;
            }
            $('#hlOdStockinDialog').dialog('open').dialog('setTitle','修改');
        }

        function ODPipeStockInCancelSubmit(){
            $('#hlOdStockinDialog').dialog('close');
        }

        function ODPipeStockIn() {
            var row = $('#pipeDatagrids').datagrid('getSelections');
            if(row.length>0){
                var idArr=[];
                for (var i=0;i<row.length;i++){
                    idArr.push(row[i].id);
                }
                var idArrs=idArr.join(',');
                var storage_stack=$('#storage_stack').val();
                var stack_level=$('#stack_level').val();
                var level_direction=$('#level_direction').val();
                var level_sequence=$('#level_sequence').val();

                $.messager.confirm('系统提示',"您确定要将这<font color=red>"+idArr.length+ "</font>根外防成品管入库吗？",function (r) {
                    if(r){
                        $.post(
                            "/pipeinfo/odproductstockin.action",
                            {hlparam:idArrs,storage_stack:storage_stack,stack_level:stack_level,level_direction:level_direction,level_sequence:level_sequence},function (data) {
                                if(data.success){
                                    $("#pipeDatagrids").datagrid("reload");
                                    $('#hlOdStockinDialog').dialog('close');
                                }
                                hlAlertFour(data.message);
                            },"json");
                    }
                });

                //hlAlertFive("/pipeinfo/delPipe.action",idArrs,idArr.length);
                // $.messager.confirm('提示','您确定要删除<font>')
            }else{
                $.messager.alert('Warning','请选择要入外防成品库的钢管!');
            }
        }



    </script>

</head>
<body>
<fieldset class="b3" style="padding:10px;margin:10px;">
    <legend> <h3><b style="color: orange" >|&nbsp;</b><span class="i18n1" name="datadisplay">数据展示</span></h3></legend>
    <div  style="margin-top:5px;">
        <table class="easyui-datagrid" id="pipeDatagrids" url="/pipeinfo/getODInspectedPipeInfoByLike.action" striped="true" loadMsg="正在加载中。。。" textField="text" pageSize="20" fitColumns="true" pagination="true" toolbar="#hlpipeTb">
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


    <a href="#" class="easyui-linkbutton" plain="true" data-options="iconCls:'icon-search'" onclick="searchPipe()">Search</a>
    <div style="float:right">
        <a href="#" id="ODPipeStockInLinkBtn" class="easyui-linkbutton i18n1" name="odstockin" data-options="iconCls:'icon-edit',plain:true" onclick="openPipeStockInPage()">外防成品管入库</a>

    </div>

</div>

<div id="hlOdStockinDialog" class="easyui-dialog" data-options="title:'添加',modal:true"  closed="true" buttons="#dlg-buttons" style="display: none;padding:5px;width:950px;height:auto;">
    <form id="odStockinForm" method="post">

        <fieldset style="width:900px;border:solid 1px #aaa;margin-top:8px;position:relative;">
            <legend>入库信息</legend>



            <table class="ht-table">

                <tr>
                    <td class="i18n1" name="storagestack" width="16%">垛位号</td>
                    <td   width="33%">
                        <select id="storage_stack" class="easyui-combobox" data-options="editable:false" name="storage_stack" style="width:200px;">
                            <option value="stack0">光管垛</option>
                            <option value="stack1">1号垛</option>
                            <option value="stack2">2号垛</option>
                            <option value="stack3">3号垛</option>
                            <option value="stack4">4号垛</option>
                            <option value="stack5">5号垛</option>
                            <option value="stack6">6号垛</option>
                            <option value="stack7">7号垛</option>
                            <option value="stack8">8号垛</option>
                            <option value="stack9">9号垛</option>
                            <option value="stack10">10号垛</option>
                            <option value="stack11">11号垛</option>
                            <option value="stack12">12号垛</option>
                            <option value="stack13">13号垛</option>
                            <option value="stack14">14号垛</option>
                            <option value="stack15">15号垛</option>
                            <option value="stack16">16号垛</option>
                            <option value="stack17">17号垛</option>
                            <option value="stack18">18号垛</option>
                            <option value="stack19">19号垛</option>
                            <option value="stack20">20号垛</option>
                        </select>
                    </td>
                    <td class="i18n1" name="stacklevel" width="16%">层号</td>
                    <td   width="33%">
                        <select id="stack_level" class="easyui-combobox" data-options="editable:false" name="stack_level" style="width:200px;">
                            <option value="l1">1层</option>
                            <option value="l2">2层</option>
                            <option value="l3">3层</option>
                            <option value="l4">4层</option>
                            <option value="l5">5层</option>
                            <option value="l6">6层</option>
                            <option value="l7">7层</option>
                            <option value="l8">8层</option>
                            <option value="l9">9层</option>
                            <option value="l10">10层</option>
                            <option value="l11">11层</option>
                            <option value="l12">12层</option>
                            <option value="l13">13层</option>
                            <option value="l14">14层</option>
                            <option value="l15">15层</option>
                            <option value="l16">16层</option>
                            <option value="l17">17层</option>
                            <option value="l18">18层</option>
                            <option value="l19">19层</option>
                            <option value="l20">20层</option>
                        </select>
                    </td>

                </tr>
                <tr>
                    <td class="i18n1" name="leveldirection" width="16%">堆垛起始方向</td>
                    <td   width="33%">
                        <select id="level_direction" class="easyui-combobox" data-options="editable:false" name="level_direction" style="width:200px;">
                            <option value="East">东</option>
                            <option value="South">南</option>
                            <option value="West">西</option>
                            <option value="North">北</option>

                        </select>
                    </td>
                    <td class="i18n1" name="levelsequence" width="16%">序号</td>
                    <td   width="33%">
                        <input id="level_sequence" class="easyui-textbox" type="text" name="level_sequence" value=""/>
                    </td>

                </tr>

            </table>


        </fieldset>
    </form>


</div>
<div id="dlg-buttons" align="center" style="width:900px;">
    <a href="#" class="easyui-linkbutton" iconCls="icon-save" onclick="ODPipeStockIn()">Submit</a>
    <a href="#" class="easyui-linkbutton" id="hlcancelBtn" operationtype="add" iconCls="icon-cancel" onclick="ODPipeStockInCancelSubmit()">Cancel</a>
</div>


<script type="text/javascript" src="../easyui/jquery.easyui.min.js"></script>
</body>
</html>
<script type="text/javascript">
    mini.parse();

    hlLanguage("../i18n/");
</script>
