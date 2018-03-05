<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
    String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>内防腐标准</title>
    <link rel="stylesheet" type="text/css" href="../easyui/themes/bootstrap/easyui.css">
    <link rel="stylesheet" type="text/css" href="../easyui/themes/icon.css">
    <link href="../miniui/multiupload/multiupload.css" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" href="../css/common.css"/>
    <script src="../easyui/jquery.min.js" type="text/javascript"></script>
    <script src="../js/jquery.i18n.properties-1.0.9.js" type="text/javascript"></script>
    <script src="../js/language.js" type="text/javascript"></script>
    <script src="../js/common.js" type="text/javascript"></script>
    <script src="../miniui/boot.js" type="text/javascript"></script>
    <script src="../miniui/js/miniui.js" type="text/javascript"></script>
    <script src="../miniui/fileupload/swfupload/swfupload.js" type="text/javascript"></script>
    <script src="../miniui/multiupload/multiupload.js" type="text/javascript"></script>
    <script src="../js/lrscroll.js" type="text/javascript"></script>
    <script src="../js/jquery.i18n.properties-1.0.9.js" type="text/javascript"></script>
    <script src="../js/language.js" type="text/javascript"></script>



    <script type="text/javascript">
        var url;
        $(function () {
            $('#hlLabStandard2FbeProDialog').dialog({
                onClose:function () {
                    clearFormLabel();
                }
            });
            $('.mini-buttonedit .mini-buttonedit-input').css('width','150px');
            // hlLanguage("../i18n/");
        });
        function addLabStandard2Fbe(){
            $('#hlcancelBtn').attr('operationtype','add');
            $('#hlLabStandard2FbeDialog').dialog('open').dialog('setTitle','新增');
            $('#LabStandard2FbeId').text('');
            clearFormLabel();
            url="/LabTestingAcceptanceCriteriaOperation/saveLabTestingAcceptanceCriteria2Fbe.action";
        }
        function delLabStandard2Fbe() {
            var row = $('#LabStandard2FbeDatagrids').datagrid('getSelections');
            if(row.length>0){
                var idArr=[];
                for (var i=0;i<row.length;i++){
                    idArr.push(row[i].id);
                }
                var idArrs=idArr.join(',');
                $.messager.confirm('系统提示',"您确定要删除这<font color=red>"+idArr.length+ "</font>条数据吗？",function (r) {
                    if(r){
                        $.post("/LabTestingAcceptanceCriteriaOperation/delLabTestingAcceptanceCriteria2fbe.action",{"hlparam":idArrs},function (data) {
                            if(data.success){
                                $("#LabStandard2FbeDatagrids").datagrid("reload");
                            }
                            hlAlertFour(data.message);
                        },"json");
                    }
                });
            }else{
                hlAlertOne();
            }
        }
        function editLabStandard2Fbe(){
            $('#hlcancelBtn').attr('operationtype','edit');
            var row = $('#LabStandard2FbeDatagrids').datagrid('getSelected');
            if(row){
                $('#hlLabStandard2FbeDialog').dialog('open').dialog('setTitle','修改');
                $('#LabStandard2FbeForm').form('load',row);
                $("#LabStandard2FbeId").text(row.id);
                url="/LabTestingAcceptanceCriteriaOperation/saveLabTestingAcceptanceCriteria2Fbe.action?id="+row.id;
            }else{
                hlAlertTwo();
            }
        }
        function searchLabStandard2Fbe() {
            $('#LabStandard2FbeDatagrids').datagrid('load',{
                'lab_testing_acceptance_criteria_no': $('#lab_testing_acceptance_criteria_no').val()
            });
        }
        function LabStandard2FbeFormSubmit() {
            $('#LabStandard2FbeForm').form('submit',{
                url:url,
                onSubmit:function () {
                    setParams();
                },
                success: function(result){
                    var result = eval('('+result+')');
                    $('#hlLabStandard2FbeDialog').dialog('close');
                    if (result.success){
                        $('#LabStandard2FbeDatagrids').datagrid('reload');
                    }
                    hlAlertFour(result.message);
                },
                error:function () {
                    //clearFormLabel();
                    hlAlertThree();
                }
            });

        }
        function LabStandard2FbeCancelSubmit() {
            $('#hlLabStandard2FbeDialog').dialog('close');
        }
        function setParams() {
            setParamsMax($("input[name='interfacial_contamination_max']"));
            setParamsMin($("input[name='interfacial_contamination_min']"));
            setParamsMax($("input[name='foaming_cross_sectional_max']"));
            setParamsMin($("input[name='foaming_cross_sectional_min']"));
            setParamsMax($("input[name='foaming_interfacial_max']"));
            setParamsMin($("input[name='foaming_interfacial_min']"));
            setParamsMax($("input[name='resistance_to_hot_water_98_24h_max']"));
            setParamsMin($("input[name='resistance_to_hot_water_98_24h_min']"));
            setParamsMax($("input[name='resistance_to_hot_water_98_28d_max']"));
            setParamsMin($("input[name='resistance_to_hot_water_98_28d_min']"));
            setParamsMax($("input[name='resistance_to_cd_65_24h_max']"));
            setParamsMin($("input[name='resistance_to_cd_65_24h_min']"));
            setParamsMax($("input[name='resistance_to_cd_22_28d_max']"));
            setParamsMin($("input[name='resistance_to_cd_22_28d_min']"));
            setParamsMax($("input[name='resistance_to_cd_65_28d_max']"));
            setParamsMin($("input[name='resistance_to_cd_65_28d_min']"));
        }
        function  setParamsMax($obj) {
            if($obj.val()==null||$obj.val()=="")
                $obj.val(9999);
        }
        function  setParamsMin($obj) {
            if($obj.val()==null||$obj.val()=="")
                $obj.val(-9999);
        }
        function  clearFormLabel(){
            $('#LabStandard2FbeForm').form('clear');
            $('.hl-label').text('');
        }
    </script>





</head>

<body>
<fieldset class="b3" style="padding:10px;margin:10px;">
    <legend> <h3><b style="color: orange" >|&nbsp;</b><span class="i18n1" name="datadisplay">数据展示</span></h3></legend>
    <div  style="margin-top:5px;">
        <table class="easyui-datagrid" id="LabStandard2FbeDatagrids" url="/LabTestingAcceptanceCriteriaOperation/getAllODAcceptanceCriteria2FbeByLike.action" striped="true" loadMsg="正在加载中。。。" textField="text" pageSize="20" fitColumns="true" pagination="true" toolbar="#hlLabStandard2FbeTb">
            <thead>
            <tr>
                <th data-options="field:'ck',checkbox:true"></th>
                <th field="id" align="center" width="100" class="i18n1" name="id">流水号</th>
                <th field="raw_material_testing_acceptance_criteria_no" align="center" width="150" class="i18n1" name="raw_material_testing_acceptance_criteria_no">实验接收标准编号</th>
                <th field="density_max" align="center" width="150" class="i18n1" name="interfacialcontaminationmax">表面污染率最大值</th>
                <th field="density_max" align="center" width="150" class="i18n1" name="interfacialcontaminationmin">表面污染率最小值</th>
                <th field="particle_size_32um_max" align="center" width="150" class="i18n1" name="foamingcrosssectionalmax">孔隙率实验 截面 最大值</th>
                <th field="particle_size_32um_max" align="center" width="150" class="i18n1" name="foamingcrosssectionalmin">孔隙率实验 截面 最小值</th>
                <th field="particle_size_150um_max" align="center" width="150" class="i18n1" name="foaminginterfacialmax">孔隙率实验 表面 最大值</th>
                <th field="particle_size_150um_max" align="center" width="150" class="i18n1" name="foaminginterfacialmin">孔隙率实验 表面 最小值</th>
                <th field="dsc_tgi_max" align="center" width="150" class="i18n1" name="resistancetohotwater9824hmax">水煮实验 98度 24小时 最大值</th>
                <th field="dsc_tgi_max" align="center" width="150" class="i18n1" name="resistancetohotwater9824hmin">水煮实验 98度 24小时 最小值</th>
                <th field="dsc_tgf_max" align="center" hidden="true" width="50" class="i18n1" name="resistancetohotwater9828dmax">水煮实验 98度 28天 最大值</th>
                <th field="dsc_tgf_max" align="center" hidden="true" width="100" class="i18n1" name="resistancetohotwater9828dmin">水煮实验 98度 28天 最小值</th>
                <th field="dsc_delta_h_max" align="center" hidden="true" width="100" class="i18n1" name="resistancetocd6524hmax">水煮实验 65度 24小时 最大值</th>
                <th field="dsc_delta_h_max" align="center" hidden="true" width="120" class="i18n1" name="resistancetocd6524hmin">水煮实验 65度 24小时 最小值</th>
                <th field="gel_time_lt_20s_max" align="center" width="100" hidden="true" class="i18n1" name="resistancetocd2228dmax">水煮实验 22.5度 28天 最大值</th>
                <th field="gel_time_lt_20s_max" align="center" width="100" hidden="true" class="i18n1" name="resistancetocd2228dmin">水煮实验 22.5度 28天 最小值</th>
                <th field="gel_time_gt_20s_max" align="center" width="100" hidden="true" class="i18n1" name="resistancetocd6528dmax">水煮实验 65度 28天 最大值</th>
                <th field="gel_time_gt_20s_max" align="center" width="100" hidden="true" class="i18n1" name="resistancetocd6528dmin">水煮实验 65度 28天 最小值</th>
                <th field="volatile_max" align="center" width="100" hidden="true" class="i18n1" name="resistancetocd6528dmax">水煮实验 65度 28天 最大值</th>
                <th field="volatile_max" align="center" width="100" hidden="true" class="i18n1" name="resistancetocd6528dmin">水煮实验 65度 28天 最小值</th>
                <th field="foaming_cross_sectional_max" align="center" width="100" hidden="true" class="i18n1" name="resistancetocd6528dmax">水煮实验 65度 28天 最大值</th>
                <th field="foaming_cross_sectional_max" align="center" width="100" hidden="true" class="i18n1" name="resistancetocd6528dmin">水煮实验 65度 28天 最小值</th>
                <th field="foaming_interfacial_max" align="center" width="100" hidden="true" class="i18n1" name="resistancetocd6528dmax">水煮实验 65度 28天 最大值</th>
                <th field="foaming_interfacial_max" align="center" width="100" hidden="true" class="i18n1" name="resistancetocd6528dmin">水煮实验 65度 28天 最小值</th>
                <th field="hot_water_max" align="center" width="100" hidden="true" class="i18n1" name="resistancetocd6528dmax">水煮实验 65度 28天 最大值</th>
                <th field="hot_water_max" align="center" width="100" hidden="true" class="i18n1" name="resistancetocd6528dmin">水煮实验 65度 28天 最小值</th>
                <th field="cd_65_24h_max" align="center" width="100" hidden="true" class="i18n1" name="resistancetocd6528dmax">水煮实验 65度 28天 最大值</th>
                <th field="cd_65_24h_max" align="center" width="100" hidden="true" class="i18n1" name="resistancetocd6528dmin">水煮实验 65度 28天 最小值</th>

            </tr>
            </thead>
        </table>
    </div>
</fieldset>

<!--工具栏-->
<div id="hlLabStandard2FbeTb" style="padding:10px;">
    <span class="i18n1" name="coatingacceptancecriteriano">涂层判定标准编号</span>:
    <input id="lab_testing_acceptance_criteria_no" name="lab_testing_acceptance_criteria_no" style="line-height:22px;border:1px solid #ccc">
    <a href="#" class="easyui-linkbutton" plain="true" data-options="iconCls:'icon-search'" onclick="searchLabStandard2Fbe()">Search</a>
    <div style="float:right">
        <a href="#" id="addObpLinkBtn" class="easyui-linkbutton i18n1" name="add" data-options="iconCls:'icon-add',plain:true" onclick="addLabStandard2Fbe()">添加</a>
        <a href="#" id="editObpLinkBtn" class="easyui-linkbutton i18n1" name="edit" data-options="iconCls:'icon-edit',plain:true" onclick="editLabStandard2Fbe()">修改</a>
        <a href="#" id="deltObpLinkBtn" class="easyui-linkbutton i18n1" name="delete" data-options="iconCls:'icon-remove',plain:true" onclick="delLabStandard2Fbe()">删除</a>
    </div>
</div>

<!--添加、修改框-->
<div id="hlLabStandard2FbeDialog" class="easyui-dialog" data-options="title:'添加',modal:true"  closed="true" buttons="#dlg-buttons" style="display: none;padding:5px;width:950px;height:auto;">
    <form id="LabStandard2FbeForm" method="post">
        <fieldset style="width:900px;border:solid 1px #aaa;margin-top:8px;position:relative;">
            <legend>接收标准信息</legend>
            <table class="ht-table" width="100%" border="0">
                <tr>
                    <td class="i18n1" name="id">流水号</td>
                    <td colspan="2">
                        <label id="LabStandard2FbeId" class="hl-label"></label>
                    </td>
                    <td class="i18n1" name="coatingacceptancecriteriano">涂层判定标准编号</td>
                    <td colspan="2"><input class="easyui-textbox"  type="text" name="lab_testing_acceptance_criteria_no" value=""/></td>
                </tr>
                <tr>
                    <td class="i18n1" name="interfacialcontaminationmax">表面污染率最大值</td>
                    <td colspan="2"><input class="easyui-numberbox" data-options="precision:2"  type="text" name="interfacial_contamination_max" value=""/></td>
                    <td class="i18n1" name="interfacialcontaminationmin">表面污染率最小值</td>
                    <td colspan="2"><input class="easyui-numberbox" data-options="precision:2"  type="text" name="interfacial_contamination_min" value=""/></td>
                </tr>
                <tr>
                    <td class="i18n1" name="foamingcrosssectionalmax">孔隙率实验 截面 最大值</td>
                    <td colspan="2"><input class="easyui-numberbox" data-options="precision:0"  type="text" name="foaming_cross_sectional_max" value=""/></td>
                    <td class="i18n1" name="foamingcrosssectionalmin">孔隙率实验 截面 最小值</td>
                    <td colspan="2"><input class="easyui-numberbox" data-options="precision:0"  type="text" name="foaming_cross_sectional_min" value=""/></td>
                </tr>
                <tr>
                    <td class="i18n1" name="foaminginterfacialmax">孔隙率实验 表面 最大值</td>
                    <td colspan="2"><input class="easyui-numberbox" data-options="precision:0"  type="text" name="foaming_interfacial_max" value=""/></td>
                    <td class="i18n1" name="foaminginterfacialmin">孔隙率实验 表面 最小值</td>
                    <td colspan="2"><input class="easyui-numberbox" data-options="precision:0"  type="text" name="foaming_interfacial_min" value=""/></td>
                </tr>
                <tr>
                    <td class="i18n1" name="resistancetohotwater9824hmax">水煮实验 98度 24小时 最大值</td>
                    <td colspan="2"><input class="easyui-numberbox" data-options="precision:0"  type="text" name="resistance_to_hot_water_98_24h_max" value=""/></td>
                    <td class="i18n1" name="resistancetohotwater9824hmin">水煮实验 98度 24小时 最小值</td>
                    <td colspan="2"><input class="easyui-numberbox" data-options="precision:0"  type="text" name="resistance_to_hot_water_98_24h_min" value=""/></td>
                </tr>
                <tr>
                    <td class="i18n1" name="resistancetohotwater9828dmax">水煮实验 98度 28天 最大值</td>
                    <td colspan="2"><input class="easyui-numberbox" data-options="precision:0"  type="text" name="resistance_to_hot_water_98_28d_max" value=""/></td>
                    <td class="i18n1" name="resistancetohotwater9828dmin">水煮实验 98度 28天 最小值</td>
                    <td colspan="2"><input class="easyui-numberbox" data-options="precision:0"  type="text" name="resistance_to_hot_water_98_28d_min" value=""/></td>
                </tr>
                <tr>
                    <td class="i18n1" name="resistancetocd6524hmax">水煮实验 65度 24小时 最大值</td>
                    <td colspan="2"><input class="easyui-numberbox" data-options="precision:2"  type="text" name="resistance_to_cd_65_24h_max" value=""/></td>
                    <td class="i18n1" name="resistancetocd6524hmin">水煮实验 65度 24小时 最小值</td>
                    <td colspan="2"><input class="easyui-numberbox" data-options="precision:2"  type="text" name="resistance_to_cd_65_24h_min" value=""/></td>
                </tr>
                <tr>
                    <td class="i18n1" name="resistancetocd2228dmax">水煮实验 22.5度 28天 最大值</td>
                    <td colspan="2"><input class="easyui-numberbox" data-options="precision:2"  type="text" name="resistance_to_cd_22_28d_max" value=""/></td>
                    <td class="i18n1" name="resistancetocd2228dmin">水煮实验 22.5度 28天 最小值</td>
                    <td colspan="2"><input class="easyui-numberbox" data-options="precision:2"  type="text" name="resistance_to_cd_22_28d_min" value=""/></td>
                </tr>
                <tr>
                    <td class="i18n1" name="resistancetocd6528dmax">水煮实验 65度 28天 最大值</td>
                    <td colspan="2"><input class="easyui-numberbox" data-options="precision:2"  type="text" name="resistance_to_cd_65_28d_max" value=""/></td>
                    <td class="i18n1" name="resistancetocd6528dmin">水煮实验 65度 28天 最小值</td>
                    <td colspan="2"><input class="easyui-numberbox" data-options="precision:2"  type="text" name="resistance_to_cd_65_28d_min" value=""/></td>
                </tr>
            </table>
        </fieldset>
    </form>


</div>
<div id="dlg-buttons" align="center" style="width:900px;">
    <a href="#" class="easyui-linkbutton" iconCls="icon-save" onclick="LabStandard2FbeFormSubmit()">Save</a>
    <a href="#" class="easyui-linkbutton" id="hlcancelBtn" operationtype="add" iconCls="icon-cancel" onclick="LabStandard2FbeCancelSubmit()">Cancel</a>
</div>


<script type="text/javascript" src="../easyui/jquery.easyui.min.js"></script>
</body>
</html>
<script type="text/javascript">
    mini.parse();
    var grid= mini.get("multiupload1");

    hlLanguage("../i18n/");
</script>