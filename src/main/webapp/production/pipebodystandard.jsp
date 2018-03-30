<%--
  Created by IntelliJ IDEA.
  User: kurt
  Date: 3/30/18
  Time: 10:09 AM
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
    <title>钢管本体标准</title>
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
        $(function () {
            $('#hlpipeAcceptanceDialog').dialog({
                onClose:function () {
                    clearFormLabel();
                }
            });
            $('.mini-buttonedit .mini-buttonedit-input').css('width','150px');
            // hlLanguage("../i18n/");
        });
        function addPipeAcceptance(){
            $('#hlcancelBtn').attr('operationtype','add');
            $('#hlpipeAcceptanceDialog').dialog('open').dialog('setTitle','新增');
            $('#pipeacceptanceId').text('');
            clearFormLabel();
            url="/AcceptanceCriteriaOperation/saveAllPipeBodyAcceptanceCriteria.action";
        }
        function delPipeAcceptance() {
            var row = $('#pipeAcceptanceDatagrids').datagrid('getSelections');
            if(row.length>0){
                var idArr=[];
                for (var i=0;i<row.length;i++){
                    idArr.push(row[i].id);
                }
                var idArrs=idArr.join(',');
                $.messager.confirm('系统提示',"您确定要删除这<font color=red>"+idArr.length+ "</font>条数据吗？",function (r) {
                    if(r){
                        $.post("/AcceptanceCriteriaOperation/delAllPipeBodyAcceptanceCriteria.action",{hlparam:idArrs},function (data) {
                            if(data.success){
                                $("#pipeAcceptanceDatagrids").datagrid("reload");
                            }
                            hlAlertFour(data.message);
                        },"json");
                    }
                });
            }else{
                hlAlertOne();
            }
        }
        function editPipeAcceptance(){
            $('#hlcancelBtn').attr('operationtype','edit');
            var row = $('#pipeAcceptanceDatagrids').datagrid('getSelected');
            if(row){
                $('#hlpipeAcceptanceDialog').dialog('open').dialog('setTitle','修改');
                $('#pipeAcceptanceForm').form('load',row);
                $("#pipeacceptanceId").text(row.id);
                var lasttime=formatterdate(row.last_update_time);
                $("#lastupdatetime").text(lasttime);

                url="/AcceptanceCriteriaOperation/saveAllPipeBodyAcceptanceCriteria.action?id="+row.id;
            }else{
                hlAlertTwo();
            }
        }
        function searchPipeAcceptance() {
            $('#pipeAcceptanceDatagrids').datagrid('load',{
                'pipe_body_acceptance_criteria_no': $('#pipe_body_acceptance_criteria_no').val()
            });
        }
        function pipeAcceptanceFormSubmit() {
            $('#pipeAcceptanceForm').form('submit',{
                url:url,
                onSubmit:function () {

                    if($("input[name='pipe_body_acceptance_criteria_no']").val()==""){

                        hlAlertFour("请输入钢管管体接收标准编号");
                        return false;
                    }


                    setParams();
                },
                success: function(result){
                    var result = eval('('+result+')');
                    $('#hlpipeAcceptanceDialog').dialog('close');
                    if (result.success){
                        $('#pipeAcceptanceDatagrids').datagrid('reload');
                    }
                    hlAlertFour(result.message);
                },
                error:function () {
                    //clearFormLabel();
                    hlAlertThree();
                }
            });

        }
        function pipeAcceptanceCancelSubmit() {
            $('#hlpipeAcceptanceDialog').dialog('close');
        }
        function setParams() {

            setParamsMax($("input[name='pipe_length_max']"));
            setParamsMin($("input[name='pipe_length_min']"));
            setParamsMax($("input[name='pipe_thickness_tolerance_max']"));
            setParamsMin($("input[name='pipe_thickness_tolerance_min']"));
            setParamsMax($("input[name='pipe_bevel_angle_max']"));
            setParamsMin($("input[name='pipe_bevel_angle_min']"));
            setParamsMax($("input[name='pipe_rootface_max']"));
            setParamsMin($("input[name='pipe_rootface_min']"));
            setParamsMax($("input[name='pipe_squareness_max']"));
            setParamsMin($("input[name='pipe_squareness_min']"));
            setParamsMax($("input[name='pipe_end_ovality_factor_max']"));
            setParamsMin($("input[name='pipe_end_ovality_factor_min']"));
            setParamsMax($("input[name='pipe_straightness_tolerance_max']"));
            setParamsMin($("input[name='pipe_straightness_tolerance_min']"));

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
            $('#pipeAcceptanceForm').form('clear');
            $('.hl-label').text('');
        }
    </script>





</head>

<body>
<fieldset class="b3" style="padding:10px;margin:10px;">
    <legend> <h3><b style="color: orange" >|&nbsp;</b><span class="i18n1" name="datadisplay">数据展示</span></h3></legend>
    <div  style="margin-top:5px;">
        <table class="easyui-datagrid" id="pipeAcceptanceDatagrids" url="/AcceptanceCriteriaOperation/getAllPipeBodyAcceptanceCriteriaByLike.action" striped="true" loadMsg="正在加载中。。。" textField="text" pageSize="20" fitColumns="true" pagination="true" toolbar="#hlPipeAcceptanceTb">
            <thead>
            <tr>
                <th data-options="field:'ck',checkbox:true"></th>
                <th field="id" align="center" width="100" class="i18n1" name="id">流水号</th>
                <th field="pipe_body_acceptance_criteria_no" align="center" width="150" class="i18n1" name="pipebodyacceptancecriteriano">钢管管体接收标准编号</th>


                <th field="pipe_length_max" align="center" width="100" hidden="true" class="i18n1" name="pipelengthmax">钢管长度最大值 m</th>
                <th field="pipe_length_min" align="center" width="100"  class="i18n1" name="pipelengthmin">钢管长度最小值 m</th>
                <th field="pipe_thickness_tolerance_max" align="center" width="100"   class="i18n1" name="pipethicknesstolerancemax">钢管壁厚公差最大值 </th>
                <th field="pipe_thickness_tolerance_min" align="center" width="100"  class="i18n1" name="pipethicknesstolerancemin">钢管壁厚公差最小值 </th>

                <th field="pipe_bevel_angle_max" align="center" hidden="false" width="100" class="i18n1" name="pipebevelanglemax">钢管坡口角度最大值 度</th>
                <th field="pipe_bevel_angle_min" align="center" hidden="true" width="120" class="i18n1" name="pipebevelanglemin">钢管坡口角度最小值 度</th>
                <th field="pipe_rootface_max" align="center" width="100"  class="i18n1" name="piperootfacemax">钝边最大值 mm</th>
                <th field="pipe_rootface_min" align="center" width="100"  class="i18n1" name="piperootfacemin">钝边最小值 mm</th>
                <th field="pipe_squareness_max" align="center" width="100"  class="i18n1" name="pipesquarenessmax">切斜最大值 mm</th>
                <th field="pipe_squareness_min" align="center" width="100" hidden="true" class="i18n1" name="pipesquarenessmin">切斜最小值 mm</th>
                <th field="pipe_end_ovality_factor_max" align="center" width="100" hidden="false" class="i18n1" name="pipeendovalityfactormax">管端椭圆度公差系数最大值 </th>
                <th field="pipe_end_ovality_factor_min" align="center" width="100" hidden="true" class="i18n1" name="pipeendovalityfactormin">管端椭圆度公差系数最小值 </th>
                <th field="pipe_straightness_tolerance_max" align="center" width="100" hidden="false" class="i18n1" name="pipestraightnesstolerancemax">直度公差最大值</th>
                <th field="pipe_straightness_tolerance_min" align="center" width="100" hidden="true" class="i18n1" name="pipestraightnesstolerancemin">直度公差最小值</th>
                <th field="last_update_time" align="center" width="150" class="i18n1" name="lastupdatetime" data-options="formatter:formatterdate">最后更新时间</th>

            </tr>
            </thead>
        </table>
    </div>
</fieldset>

<!--工具栏-->
<div id="hlPipeAcceptanceTb" style="padding:10px;">
    <span class="i18n1" name="pipebodyacceptancecriteriano">钢管管体判定标准编号</span>:
    <input id="pipe_body_acceptance_criteria_no" name="pipebodyacceptancecriteriano" style="line-height:22px;border:1px solid #ccc">
    <a href="#" class="easyui-linkbutton" plain="true" data-options="iconCls:'icon-search'" onclick="searchPipeAcceptance()">Search</a>
    <div style="float:right">
        <a href="#" id="addObpLinkBtn" class="easyui-linkbutton i18n1" name="add" data-options="iconCls:'icon-add',plain:true" onclick="addPipeAcceptance()">添加</a>
        <a href="#" id="editObpLinkBtn" class="easyui-linkbutton i18n1" name="edit" data-options="iconCls:'icon-edit',plain:true" onclick="editPipeAcceptance()">修改</a>
        <a href="#" id="deltObpLinkBtn" class="easyui-linkbutton i18n1" name="delete" data-options="iconCls:'icon-remove',plain:true" onclick="delPipeAcceptance()">删除</a>
    </div>
</div>

<!--添加、修改框-->
<div id="hlpipeAcceptanceDialog" class="easyui-dialog" data-options="title:'添加',modal:true"  closed="true" buttons="#dlg-buttons" style="display: none;padding:5px;width:950px;max-height:500px;overflow-y:auto;">
    <form id="pipeAcceptanceForm" method="post">
        <fieldset style="width:900px;border:solid 1px #aaa;margin-top:8px;position:relative;">
            <legend>钢管管体接收标准信息</legend>
            <table class="ht-table" width="100%" border="0">
                <tr>
                    <td class="i18n1" name="id">流水号</td>
                    <td colspan="2">
                        <label id="pipeacceptanceId" class="hl-label"></label>
                    </td>
                    <td class="i18n1" name="pipebodyacceptancecriteriano">钢管管体标准编号</td>
                    <td colspan="2"><input class="easyui-textbox"  type="text" name="pipe_body_acceptance_criteria_no" value=""/></td>

                </tr>
                <tr>
                    <td class="i18n1" name="pipelengthmax">钢管长度最大值 m</td>
                    <td colspan="2"><input class="easyui-numberbox" data-options="precision:2"  type="text" name="pipe_length_max" value=""/></td>
                    <td class="i18n1" name="pipelengthmin">钢管长度最小值 m</td>
                    <td colspan="2"><input class="easyui-numberbox" data-options="precision:2"  type="text" name="pipe_length_min" value=""/></td>
                </tr>
                <tr>
                    <td class="i18n1" name="pipethicknesstolerancemax">钢管壁厚公差最大值</td>
                    <td colspan="2"><input class="easyui-numberbox" data-options="precision:2"  type="text" name="pipe_thickness_tolerance_max" value=""/></td>
                    <td class="i18n1" name="pipethicknesstolerancemin">钢管壁厚公差最小值</td>
                    <td colspan="2"><input class="easyui-numberbox" data-options="precision:2"  type="text" name="pipe_thickness_tolerance_min" value=""/></td>
                </tr>
                <tr>
                    <td class="i18n1" name="pipebevelanglemax">钢管坡口角度最大值 度</td>
                    <td colspan="2"><input class="easyui-numberbox" data-options="precision:2"  type="text" name="pipe_bevel_angle_max" value=""/></td>
                    <td class="i18n1" name="pipebevelanglemin">钢管坡口角度最小值 度</td>
                    <td colspan="2"><input class="easyui-numberbox" data-options="precision:2"  type="text" name="pipe_bevel_angle_min" value=""/></td>
                </tr>
                <tr>
                    <td class="i18n1" name="piperootfacemax">钝边最大值 mm</td>
                    <td colspan="2"><input class="easyui-numberbox" data-options="precision:2"  type="text" name="pipe_rootface_max" value=""/></td>
                    <td class="i18n1" name="piperootfacemin">钝边最小值 mm</td>
                    <td colspan="2"><input class="easyui-numberbox" data-options="precision:2"  type="text" name="pipe_rootface_min" value=""/></td>
                </tr>
                <tr>
                    <td class="i18n1" name="pipesquarenessmax">切斜最大值 mm</td>
                    <td colspan="2"><input class="easyui-numberbox" data-options="precision:2"  type="text" name="pipe_squareness_max" value=""/></td>
                    <td class="i18n1" name="pipesquarenessmin">切斜最小值 mm</td>
                    <td colspan="2"><input class="easyui-numberbox" data-options="precision:2"  type="text" name="pipe_squareness_min" value=""/></td>
                </tr>
                <tr>
                    <td class="i18n1" name="pipeendovalityfactormax">管端椭圆度公差系数最大值</td>
                    <td colspan="2"><input class="easyui-numberbox" data-options="precision:3"  type="text" name="pipe_end_ovality_factor_max" value=""/></td>
                    <td class="i18n1" name="pipeendovalityfactormin">管端椭圆度公差系数最小值</td>
                    <td colspan="2"><input class="easyui-numberbox" data-options="precision:3"  type="text" name="pipe_end_ovality_factor_min" value=""/></td>
                </tr>
                <tr>
                    <td class="i18n1" name="pipestraightnesstolerancemax">直度公差最大值</td>
                    <td colspan="2"><input class="easyui-numberbox" data-options="precision:2"  type="text" name="pipe_straightness_tolerance_max" value=""/></td>
                    <td class="i18n1" name="pipestraightnesstolerancemin">直度公差最小值</td>
                    <td colspan="2"><input class="easyui-numberbox" data-options="precision:2"  type="text" name="pipe_straightness_tolerance_min" value=""/></td>
                </tr>
                <tr>
                    <td class="i18n1" name="lastupdatetime">最后更新时间</td>
                    <td colspan="2">
                        <label class="hl-label" id="lastupdatetime" type="text" name="last_update_time" value="" data-options="formatter:myformatter2,parser:myparser2"/>
                    </td>


                </tr>

            </table>
        </fieldset>
    </form>


</div>
<div id="dlg-buttons" align="center" style="width:900px;">
    <a href="#" class="easyui-linkbutton" iconCls="icon-save" onclick="pipeAcceptanceFormSubmit()">Save</a>
    <a href="#" class="easyui-linkbutton" id="hlcancelBtn" operationtype="add" iconCls="icon-cancel" onclick="pipeAcceptanceCancelSubmit()">Cancel</a>
</div>


<script type="text/javascript" src="../easyui/jquery.easyui.min.js"></script>
</body>
</html>
<script type="text/javascript">
    mini.parse();
    var grid= mini.get("multiupload1");

    hlLanguage("../i18n/");
</script>