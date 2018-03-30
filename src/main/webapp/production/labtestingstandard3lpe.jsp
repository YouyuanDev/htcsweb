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
            $('#hlLabStandard3LpeProDialog').dialog({
                onClose:function () {
                    clearFormLabel();
                }
            });
            $('.mini-buttonedit .mini-buttonedit-input').css('width','150px');
            // hlLanguage("../i18n/");
        });
        function addLabStandard3Lpe(){
            $('#hlcancelBtn').attr('operationtype','add');
            $('#hlLabStandard3LpeDialog').dialog('open').dialog('setTitle','新增');
            $('#LabStandard3LpeId').text('');
            clearFormLabel();
            url="/LabTestingAcceptanceCriteriaOperation/saveLabTestingAcceptanceCriteria3lpe.action";
        }
        function delLabStandard3Lpe() {
            var row = $('#LabStandard3LpeDatagrids').datagrid('getSelections');
            if(row.length>0){
                var idArr=[];
                for (var i=0;i<row.length;i++){
                    idArr.push(row[i].id);
                }
                var idArrs=idArr.join(',');
                $.messager.confirm('系统提示',"您确定要删除这<font color=red>"+idArr.length+ "</font>条数据吗？",function (r) {
                    if(r){
                        $.post("/LabTestingAcceptanceCriteriaOperation/delLabTestingAcceptanceCriteria3lpe.action",{"hlparam":idArrs},function (data) {
                            if(data.success){
                                $("#LabStandard3LpeDatagrids").datagrid("reload");
                            }
                            hlAlertFour(data.message);
                        },"json");
                    }
                });
            }else{
                hlAlertOne();
            }
        }
        function editLabStandard3Lpe(){
            $('#hlcancelBtn').attr('operationtype','edit');
            var row = $('#LabStandard3LpeDatagrids').datagrid('getSelected');
            if(row){
                $('#hlLabStandard3LpeDialog').dialog('open').dialog('setTitle','修改');
                $('#LabStandard3LpeForm').form('load',row);
                $("#LabStandard3LpeId").text(row.id);
                var lasttime=formatterdate(row.last_update_time);
                $("#lastupdatetime").text(lasttime);
                url="/LabTestingAcceptanceCriteriaOperation/saveLabTestingAcceptanceCriteria3lpe.action?id="+row.id;
            }else{
                hlAlertTwo();
            }
        }
        function searchLabStandard3Lpe() {
            $('#LabStandard3LpeDatagrids').datagrid('load',{
                'lab_testing_acceptance_criteria_no': $('#lab_testing_acceptance_criteria_no').val()
            });
        }
        function LabStandard3LpeFormSubmit() {
            $('#LabStandard3LpeForm').form('submit',{
                url:url,
                onSubmit:function () {
                    if($("input[name='lab_testing_acceptance_criteria_no']").val()==""){

                        hlAlertFour("请输入3LPE实验标准编号");
                        return false;
                    }
                    setParams();
                },
                success: function(result){
                    var result = eval('('+result+')');
                    $('#hlLabStandard3LpeDialog').dialog('close');
                    if (result.success){
                        $('#LabStandard3LpeDatagrids').datagrid('reload');
                    }
                    hlAlertFour(result.message);
                },
                error:function () {
                    //clearFormLabel();
                    hlAlertThree();
                }
            });
        }
        function LabStandard3LpeCancelSubmit() {
            $('#hlLabStandard3LpeDialog').dialog('close');
        }
        function setParams() {
            setParamsMax($("input[name='resistance_to_cd_20_28d_max']"));
            setParamsMin($("input[name='resistance_to_cd_20_28d_min']"));
            setParamsMax($("input[name='resistance_to_cd_max_28d_max']"));
            setParamsMin($("input[name='resistance_to_cd_max_28d_min']"));
            setParamsMax($("input[name='resistance_to_cd_65_24h_max']"));
            setParamsMin($("input[name='resistance_to_cd_65_24h_min']"));
            setParamsMax($("input[name='impact_resistance_23_max']"));
            setParamsMin($("input[name='impact_resistance_23_min']"));
            setParamsMax($("input[name='impact_resistance_m40_max']"));
            setParamsMin($("input[name='impact_resistance_m40_min']"));
            setParamsMax($("input[name='indentation_hardness_23_max']"));
            setParamsMin($("input[name='indentation_hardness_23_min']"));
            setParamsMax($("input[name='indentation_hardness_70_max']"));
            setParamsMin($("input[name='indentation_hardness_70_min']"));
            setParamsMax($("input[name='elongation_at_break_max']"));
            setParamsMin($("input[name='elongation_at_break_min']"));
            setParamsMax($("input[name='coating_resistivity_max']"));
            setParamsMin($("input[name='coating_resistivity_min']"));
            setParamsMax($("input[name='thermal_degradation_max']"));
            setParamsMin($("input[name='thermal_degradation_min']"));
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
            $('#LabStandard3LpeForm').form('clear');
            $('.hl-label').text('');
        }
    </script>





</head>

<body>
<fieldset class="b3" style="padding:10px;margin:10px;">
    <legend> <h3><b style="color: orange" >|&nbsp;</b><span class="i18n1" name="datadisplay">数据展示</span></h3></legend>
    <div  style="margin-top:5px;">
        <table class="easyui-datagrid" id="LabStandard3LpeDatagrids" url="/LabTestingAcceptanceCriteriaOperation/getAllODAcceptanceCriteria3LpeByLike.action" striped="true" loadMsg="正在加载中。。。" textField="text" pageSize="20" fitColumns="true" pagination="true" toolbar="#hlLabStandard3LpeTb">
            <thead>
            <tr>
                <th data-options="field:'ck',checkbox:true"></th>
                <th field="id" align="center" width="100" class="i18n1" name="id">流水号</th>
                <th field="lab_testing_acceptance_criteria_no" align="center" width="150" class="i18n1" name="labtestingacceptancecriteriano">实验接收标准编号</th>
                <th field="resistance_to_cd_20_28d_max" align="center" width="150" class="i18n1" name="resistancetocd2028dmax">阴极剥离 20度 28d 最大值</th>
                <th field="resistance_to_cd_20_28d_min" align="center" width="150" class="i18n1" name="resistancetocd2028dmin">阴极剥离 20度 28d 最小值</th>
                <th field="resistance_to_cd_max_28d_max" align="center" width="150" class="i18n1" name="resistancetocdmax28dmax">阴极剥离 最高温度 28d 最大值</th>
                <th field="resistance_to_cd_max_28d_min" align="center" width="150" class="i18n1" name="resistancetocdmax28dmin">阴极剥离 最高温度 28d 最大值</th>
                <th field="resistance_to_cd_65_24h_max" align="center" width="150" class="i18n1" name="resistancetocd6524hmax">阴极剥离 65度 24小时 最大值</th>
                <th field="resistance_to_cd_65_24h_min" align="center" width="150" class="i18n1" name="resistancetocd6524hmin">阴极剥离 65度 24小时 最大值</th>
                <th field="impact_resistance_23_max" align="center" width="150" class="i18n1" name="impactresistance23max">冲击 23度 最大值</th>
                <th field="impact_resistance_23_min" align="center" width="150" class="i18n1" name="impactresistance23min">冲击 23度 最大值</th>
                <th field="impact_resistance_m40_max" align="center" hidden="true" width="50" class="i18n1" name="impactresistancem40max">冲击 -40度 最大值</th>
                <th field="impact_resistance_m40_min" align="center" hidden="true" width="100" class="i18n1" name="impactresistancem40min">冲击 -40度 最大值</th>
                <th field="indentation_hardness_23_max" align="center" hidden="true" width="100" class="i18n1" name="indentationhardness23max">压痕硬度 23度  最大值</th>
                <th field="indentation_hardness_23_min" align="center" hidden="true" width="120" class="i18n1" name="indentationhardness23min">压痕硬度 23度  最大值</th>
                <th field="indentation_hardness_70_max" align="center" width="100" hidden="true" class="i18n1" name="indentationhardness70max">压痕硬度 70度  最大值</th>
                <th field="indentation_hardness_70_min" align="center" width="100" hidden="true" class="i18n1" name="indentationhardness70min">压痕硬度 70度  最大值</th>
                <th field="elongation_at_break_max" align="center" width="100" hidden="true" class="i18n1" name="elongationatbreakmax">延展率 最大值</th>
                <th field="elongation_at_break_min" align="center" width="100" hidden="true" class="i18n1" name="elongationatbreakmin">延展率 最大值</th>
                <th field="coating_resistivity_max" align="center" width="100" hidden="true" class="i18n1" name="coatingresistivitymax">涂层强度 最大值</th>
                <th field="coating_resistivity_min" align="center" width="100" hidden="true" class="i18n1" name="coatingresistivitymin">涂层强度 最大值</th>
                <th field="thermal_degradation_max" align="center" width="100" hidden="true" class="i18n1" name="thermaldegradationmax">热特性最大值</th>
                <th field="thermal_degradation_min" align="center" width="100" hidden="true" class="i18n1" name="thermaldegradationmin">热特性最小值</th>
                <th field="last_update_time" align="center" width="150" class="i18n1" name="lastupdatetime" data-options="formatter:formatterdate">最后更新时间</th>

            </tr>
            </thead>
        </table>
    </div>
</fieldset>

<!--工具栏-->
<div id="hlLabStandard3LpeTb" style="padding:10px;">
    <span class="i18n1" name="coatingacceptancecriteriano">涂层判定标准编号</span>:
    <input id="lab_testing_acceptance_criteria_no" name="labtestingacceptancecriteriano" style="line-height:22px;border:1px solid #ccc">
    <a href="#" class="easyui-linkbutton" plain="true" data-options="iconCls:'icon-search'" onclick="searchLabStandard3Lpe()">Search</a>
    <div style="float:right">
        <a href="#" id="addObpLinkBtn" class="easyui-linkbutton i18n1" name="add" data-options="iconCls:'icon-add',plain:true" onclick="addLabStandard3Lpe()">添加</a>
        <a href="#" id="editObpLinkBtn" class="easyui-linkbutton i18n1" name="edit" data-options="iconCls:'icon-edit',plain:true" onclick="editLabStandard3Lpe()">修改</a>
        <a href="#" id="deltObpLinkBtn" class="easyui-linkbutton i18n1" name="delete" data-options="iconCls:'icon-remove',plain:true" onclick="delLabStandard3Lpe()">删除</a>
    </div>
</div>

<!--添加、修改框-->
<div id="hlLabStandard3LpeDialog" class="easyui-dialog" data-options="title:'添加',modal:true"  closed="true" buttons="#dlg-buttons" style="display: none;padding:5px;width:950px;height:auto;">
    <form id="LabStandard3LpeForm" method="post">
        <fieldset style="width:900px;border:solid 1px #aaa;margin-top:8px;position:relative;">
            <legend>3LPE实验接收标准信息</legend>
            <table class="ht-table" width="100%" border="0">
                <tr>
                    <td class="i18n1" name="id">流水号</td>
                    <td >
                        <label id="LabStandard3LpeId" class="hl-label"></label>
                    </td>
                    <td></td>
                    <td class="i18n1" name="coatingacceptancecriteriano">涂层判定标准编号</td>
                    <td ><input class="easyui-textbox"  type="text" name="lab_testing_acceptance_criteria_no" value=""/></td>
                    <td></td>
                </tr>
                <tr>
                    <td class="i18n1" name="resistancetocd2028dmax">阴极剥离 20度 28d 最大值</td>
                    <td ><input class="easyui-numberbox" data-options="precision:2"  type="text" name="resistance_to_cd_20_28d_max" value=""/></td>
                    <td></td>
                    <td class="i18n1" name="resistancetocd2028dmin">阴极剥离 20度 28d 最小值</td>
                    <td ><input class="easyui-numberbox" data-options="precision:2"  type="text" name="resistance_to_cd_20_28d_min" value=""/></td>
                    <td></td>
                </tr>
                <tr>
                    <td class="i18n1" name="resistancetocdmax28dmax">阴极剥离 最高温度 28d 最大值</td>
                    <td ><input class="easyui-numberbox" data-options="precision:2"  type="text" name="resistance_to_cd_max_28d_max" value=""/></td>
                    <td></td>
                    <td class="i18n1" name="resistancetocdmax28dmin">阴极剥离 最高温度 28d 最小值</td>
                    <td ><input class="easyui-numberbox" data-options="precision:2"  type="text" name="resistance_to_cd_max_28d_min" value=""/></td>
                    <td></td>
                </tr>
                <tr>
                    <td class="i18n1" name="resistancetocd6524hmax">阴极剥离 65度 24小时 最大值</td>
                    <td ><input class="easyui-numberbox" data-options="precision:2"  type="text" name="resistance_to_cd_65_24h_max" value=""/></td>
                    <td></td>
                    <td class="i18n1" name="resistancetocd6524hmin">阴极剥离 65度 24小时 最小值</td>
                    <td ><input class="easyui-numberbox" data-options="precision:2"  type="text" name="resistance_to_cd_65_24h_min" value=""/></td>
                    <td></td>
                </tr>
                <tr>
                    <td class="i18n1" name="impactresistance23max">冲击 23度 最大值</td>
                    <td ><input class="easyui-numberbox" data-options="precision:2"  type="text" name="impact_resistance_23_max" value=""/></td>
                    <td></td>
                    <td class="i18n1" name="impactresistance23min">冲击 23度 最小值</td>
                    <td ><input class="easyui-numberbox" data-options="precision:2"  type="text" name="impact_resistance_23_min" value=""/></td>
                    <td></td>
                </tr>
                <tr>
                    <td class="i18n1" name="impactresistancem40max">冲击 -40度 最大值</td>
                    <td ><input class="easyui-numberbox" data-options="precision:2"  type="text" name="impact_resistance_m40_max" value=""/></td>
                    <td></td>
                    <td class="i18n1" name="impactresistancem40min">冲击 -40度 最小值</td>
                    <td ><input class="easyui-numberbox" data-options="precision:2"  type="text" name="impact_resistance_m40_min" value=""/></td>
                    <td></td>
                </tr>
                <tr>
                    <td class="i18n1" name="indentationhardness23max">压痕硬度 23度  最大值</td>
                    <td ><input class="easyui-numberbox" data-options="precision:2"  type="text" name="indentation_hardness_23_max" value=""/></td>
                    <td></td>
                    <td class="i18n1" name="indentationhardness23min">压痕硬度 23度  最小值</td>
                    <td ><input class="easyui-numberbox" data-options="precision:2"  type="text" name="indentation_hardness_23_min" value=""/></td>
                    <td></td>
                </tr>
                <tr>
                    <td class="i18n1" name="indentationhardness70max">压痕硬度 70度  最大值</td>
                    <td ><input class="easyui-numberbox" data-options="precision:2"  type="text" name="indentation_hardness_70_max" value=""/></td>
                    <td></td>
                    <td class="i18n1" name="indentationhardness70min">压痕硬度 70度  最小值</td>
                    <td ><input class="easyui-numberbox" data-options="precision:2"  type="text" name="indentation_hardness_70_min" value=""/></td>
                    <td></td>
                </tr>
                <tr>
                    <td class="i18n1" name="elongationatbreakmax">延展率 最大值</td>
                    <td ><input class="easyui-numberbox" data-options="precision:2"  type="text" name="elongation_at_break_max" value=""/></td>
                    <td></td>
                    <td class="i18n1" name="elongationatbreakmin">延展率 最小值</td>
                    <td ><input class="easyui-numberbox" data-options="precision:2"  type="text" name="elongation_at_break_min" value=""/></td>
                    <td></td>
                </tr>
                <tr>
                    <td class="i18n1" name="coatingresistivitymax">涂层强度 最大值</td>
                    <td ><input class="easyui-numberbox" data-options="precision:2"  type="text" name="coating_resistivity_max" value=""/></td>
                    <td></td>
                    <td class="i18n1" name="coatingresistivitymin">涂层强度 最小值</td>
                    <td ><input class="easyui-numberbox" data-options="precision:2"  type="text" name="coating_resistivity_min" value=""/></td>
                    <td></td>
                </tr>
                <tr>
                    <td class="i18n1" name="thermaldegradationmax">热特性最大值</td>
                    <td ><input class="easyui-numberbox" data-options="precision:2"  type="text" name="thermal_degradation_max" value=""/></td>
                    <td></td>
                    <td class="i18n1" name="thermaldegradationmin">热特性最小值</td>
                    <td ><input class="easyui-numberbox" data-options="precision:2"  type="text" name="thermal_degradation_min" value=""/></td>
                    <td></td>
                </tr>

                <tr>
                    <td class="i18n1" name="lastupdatetime">最后更新时间</td>
                    <td>
                        <label class="hl-label" id="lastupdatetime" type="text" name="last_update_time" value="" data-options="formatter:myformatter2,parser:myparser2"/>
                    </td>


                </tr>
            </table>
        </fieldset>
    </form>


</div>
<div id="dlg-buttons" align="center" style="width:900px;">
    <a href="#" class="easyui-linkbutton" iconCls="icon-save" onclick="LabStandard3LpeFormSubmit()">Save</a>
    <a href="#" class="easyui-linkbutton" id="hlcancelBtn" operationtype="add" iconCls="icon-cancel" onclick="LabStandard3LpeCancelSubmit()">Cancel</a>
</div>


<script type="text/javascript" src="../easyui/jquery.easyui.min.js"></script>
</body>
</html>
<script type="text/javascript">
    mini.parse();
    var grid= mini.get("multiupload1");

    hlLanguage("../i18n/");
</script>