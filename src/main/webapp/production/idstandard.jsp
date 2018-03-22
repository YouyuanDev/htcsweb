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
    <script  src="../miniui/js/miniui.js" type="text/javascript"></script>
    <script src="../miniui/fileupload/swfupload/swfupload.js" type="text/javascript"></script>
    <script src="../miniui/multiupload/multiupload.js" type="text/javascript"></script>
    <script  src="../js/lrscroll.js" type="text/javascript"></script>
    <script src="../js/jquery.i18n.properties-1.0.9.js" type="text/javascript"></script>
    <script src="../js/language.js" type="text/javascript"></script>



    <script type="text/javascript">
        var url;
        $(function () {
            $('#hlIdBlastProDialog').dialog({
                onClose:function () {
                    clearFormLabel();
                }
            });
            $('.mini-buttonedit .mini-buttonedit-input').css('width','150px');
            // hlLanguage("../i18n/");
        });
        function addIdAcceptance(){
            $('#hlcancelBtn').attr('operationtype','add');
            $('#hlIdAcceptanceDialog').dialog('open').dialog('setTitle','新增');
            $('#idacceptanceId').text('');
            clearFormLabel();
            url="/AcceptanceCriteriaOperation/saveAllIDAcceptanceCriteria.action";
        }
        function delIdAcceptance() {
            var row = $('#idAcceptanceDatagrids').datagrid('getSelections');
            if(row.length>0){
                var idArr=[];
                for (var i=0;i<row.length;i++){
                    idArr.push(row[i].id);
                }
                var idArrs=idArr.join(',');
                $.messager.confirm('系统提示',"您确定要删除这<font color=red>"+idArr.length+ "</font>条数据吗？",function (r) {
                    if(r){
                        $.post("/AcceptanceCriteriaOperation/delAllIDAcceptanceCriteria.action",{"hlparam":idArrs},function (data) {
                            if(data.success){
                                $("#idAcceptanceDatagrids").datagrid("reload");
                            }
                            hlAlertFour(data.message);
                        },"json");
                    }
                });
            }else{
                hlAlertOne();
            }
        }
        function editIdAcceptance(){
            $('#hlcancelBtn').attr('operationtype','edit');
            var row = $('#idAcceptanceDatagrids').datagrid('getSelected');
            if(row){
                $('#hlIdAcceptanceDialog').dialog('open').dialog('setTitle','修改');
                $('#idAcceptanceForm').form('load',row);
                $("#idacceptanceId").text(row.id);
                url="/AcceptanceCriteriaOperation/saveAllIDAcceptanceCriteria.action?id="+row.id;
            }else{
                hlAlertTwo();
            }
        }
        function searchIdAcceptance() {
            $('#idAcceptanceDatagrids').datagrid('load',{
                'coating_acceptance_criteria_no': $('#coating_acceptance_criteria_no').val()
            });
        }
        function idAcceptanceFormSubmit() {
            $('#idAcceptanceForm').form('submit',{
                url:url,
                onSubmit:function () {
                    setParams();
                },
                success: function(result){
                    var result = eval('('+result+')');
                    $('#hlIdAcceptanceDialog').dialog('close');
                    if (result.success){
                        $('#idAcceptanceDatagrids').datagrid('reload');
                    }
                    hlAlertFour(result.message);
                },
                error:function () {
                    //clearFormLabel();
                    hlAlertThree();
                }
            });

        }
        function idAcceptanceCancelSubmit() {
            $('#hlIdAcceptanceDialog').dialog('close');
        }
        function setParams() {
            setParamsMax($("input[name='temp_above_dew_point_max']"));
            setParamsMin($("input[name='temp_above_dew_point_min']"));
            setParamsMax($("input[name='blast_finish_sa25_max']"));
            setParamsMin($("input[name='blast_finish_sa25_min']"));
            setParamsMax($("input[name='relative_humidity_max']"));
            setParamsMin($("input[name='relative_humidity_min']"));
            setParamsMax($("input[name='id_profile_max']"));
            setParamsMin($("input[name='id_profile_min']"));
            setParamsMax($("input[name='dry_film_thickness_max']"));
            setParamsMin($("input[name='dry_film_thickness_min']"));
            setParamsMax($("input[name='cutback_max']"));
            setParamsMin($("input[name='cutback_min']"));
            setParamsMax($("input[name='residual_magnetism_max']"));
            setParamsMin($("input[name='residual_magnetism_min']"));
            setParamsMax($("input[name='wet_film_thickness_max']"));
            setParamsMin($("input[name='wet_film_thickness_min']"));
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
            $('#idAcceptanceForm').form('clear');
            $('.hl-label').text('');
        }
    </script>





</head>

<body>
<fieldset class="b3" style="padding:10px;margin:10px;">
    <legend> <h3><b style="color: orange" >|&nbsp;</b><span class="i18n1" name="datadisplay">数据展示</span></h3></legend>
    <div  style="margin-top:5px;">
        <table class="easyui-datagrid" id="idAcceptanceDatagrids" url="/AcceptanceCriteriaOperation/getAllIDAcceptanceCriteriaByLike.action" striped="true" loadMsg="正在加载中。。。" textField="text" pageSize="20" fitColumns="true" pagination="true" toolbar="#hlIdAcceptanceTb">
            <thead>
            <tr>
                <th data-options="field:'ck',checkbox:true"></th>
                <th field="id" align="center" width="100" class="i18n1" name="id">流水号</th>
                <th field="coating_acceptance_criteria_no" align="center" width="150" class="i18n1" name="coatingacceptancecriteriano">涂层判定标准编号</th>
                <th field="temp_above_dew_point_max" align="center" width="150" class="i18n1" name="tempabovedewpointmax">高于露点最大值</th>
                <th field="temp_above_dew_point_min" align="center" width="150" class="i18n1" name="tempabovedewpointmin">高于露点最小值</th>
                <th field="blast_finish_sa25_max" align="center" width="150" class="i18n1" name="blastfinishsa25max">清洁度sa2.5最大值</th>
                <th field="blast_finish_sa25_min" align="center" width="150" class="i18n1" name="blastfinishsa25min">清洁度sa2.5最小值</th>
                <th field="relative_humidity_max" align="center" width="150" class="i18n1" name="relativehumiditymax">相对湿度最大值</th>
                <th field="relative_humidity_min" align="center" width="150" class="i18n1" name="relativehumiditymin">相对湿度最小值</th>
                <th field="id_profile_max" align="center" width="150" class="i18n1" name="idprofilemax">内锚纹深度最大值</th>
                <th field="id_profile_min" align="center" width="150" class="i18n1" name="idprofilemin">内锚纹深度最小值</th>
                <th field="dry_film_thickness_max" align="center" hidden="true" width="50" class="i18n1" name="dryfilmthicknessmax">干膜厚度最大值</th>
                <th field="dry_film_thickness_min" align="center" hidden="true" width="100" class="i18n1" name="dryfilmthicknessmin">干膜厚度最小值</th>
                <th field="cutback_max" align="center" hidden="true" width="100" class="i18n1" name="cutbackmax">预留段最大值</th>
                <th field="cutback_min" align="center" hidden="true" width="120" class="i18n1" name="cutbackmin">预留段最小值</th>
                <th field="residual_magnetism_max" align="center" width="100" hidden="true" class="i18n1" name="residualmagnetismmax">剩磁最大值</th>
                <th field="residual_magnetism_min" align="center" width="100" hidden="true" class="i18n1" name="residualmagnetismmin">剩磁最小值</th>
                <th field="wet_film_thickness_max" align="center" width="100" hidden="true" class="i18n1" name="wetfilmthicknessmax">湿膜厚度最大值μm</th>
                <th field="wet_film_thickness_min" align="center" width="100" hidden="true" class="i18n1" name="wetfilmthicknessmin">湿膜厚度最小值μm</th>
            </tr>
            </thead>
        </table>
    </div>
</fieldset>

<!--工具栏-->
<div id="hlIdAcceptanceTb" style="padding:10px;">
    <span class="i18n1" name="coatingacceptancecriteriano">涂层判定标准编号</span>:
    <input id="coating_acceptance_criteria_no" name="coating_acceptance_criteria_no" style="line-height:22px;border:1px solid #ccc">
    <a href="#" class="easyui-linkbutton" plain="true" data-options="iconCls:'icon-search'" onclick="searchIdAcceptance()">Search</a>
    <div style="float:right">
        <a href="#" id="addObpLinkBtn" class="easyui-linkbutton i18n1" name="add" data-options="iconCls:'icon-add',plain:true" onclick="addIdAcceptance()">添加</a>
        <a href="#" id="editObpLinkBtn" class="easyui-linkbutton i18n1" name="edit" data-options="iconCls:'icon-edit',plain:true" onclick="editIdAcceptance()">修改</a>
        <a href="#" id="deltObpLinkBtn" class="easyui-linkbutton i18n1" name="delete" data-options="iconCls:'icon-remove',plain:true" onclick="delIdAcceptance()">删除</a>
    </div>
</div>

<!--添加、修改框-->
<div id="hlIdAcceptanceDialog" class="easyui-dialog" data-options="title:'添加',modal:true"  closed="true" buttons="#dlg-buttons" style="display: none;padding:5px;width:950px;height:auto;">
    <form id="idAcceptanceForm" method="post">
        <fieldset style="width:900px;border:solid 1px #aaa;margin-top:8px;position:relative;">
            <legend>接收标准信息</legend>
            <table class="ht-table" width="100%" border="0">
                <tr>
                    <td class="i18n1" name="id">流水号</td>
                    <td colspan="2">
                        <label id="idacceptanceId" class="hl-label"></label>
                    </td>
                    <td class="i18n1" name="coatingacceptancecriteriano">涂层判定标准编号</td>
                    <td colspan="2"><input class="easyui-textbox"  type="text" name="coating_acceptance_criteria_no" value=""/></td>

                </tr>
                <tr>
                    <td class="i18n1" name="tempabovedewpointmax">高于露点最大值</td>
                    <td colspan="2"><input class="easyui-numberbox" data-options="precision:2"  type="text" name="temp_above_dew_point_max" value=""/></td>
                    <td class="i18n1" name="tempabovedewpointmin">高于露点最小值</td>
                    <td colspan="2"><input class="easyui-numberbox" data-options="precision:2"  type="text" name="temp_above_dew_point_min" value=""/></td>
                </tr>
                <tr>
                    <td class="i18n1" name="blastfinishsa25max">清洁度sa2.5最大值</td>
                    <td colspan="2"><input class="easyui-numberbox" data-options="precision:2"  type="text" name="blast_finish_sa25_max" value=""/></td>
                    <td class="i18n1" name="blastfinishsa25min">清洁度sa2.5最小值</td>
                    <td colspan="2"><input class="easyui-numberbox" data-options="precision:2"  type="text" name="blast_finish_sa25_min" value=""/></td>
                </tr>
                <tr>
                    <td class="i18n1" name="relativehumiditymax">相对湿度最大值</td>
                    <td colspan="2"><input class="easyui-numberbox" data-options="precision:2"  type="text" name="relative_humidity_max" value=""/></td>
                    <td class="i18n1" name="relativehumiditymin">相对湿度最小值</td>
                    <td colspan="2"><input class="easyui-numberbox" data-options="precision:2"  type="text" name="relative_humidity_min" value=""/></td>
                </tr>
                <tr>
                    <td class="i18n1" name="idprofilemax">内锚纹深度最大值</td>
                    <td colspan="2"><input class="easyui-numberbox" data-options="precision:2"  type="text" name="id_profile_max" value=""/></td>
                    <td class="i18n1" name="idprofilemin">内锚纹深度最小值</td>
                    <td colspan="2"><input class="easyui-numberbox" data-options="precision:2"  type="text" name="id_profile_min" value=""/></td>
                </tr>
                <tr>
                    <td class="i18n1" name="dryfilmthicknessmax">干膜厚度最大值</td>
                    <td colspan="2"><input class="easyui-numberbox" data-options="precision:2"  type="text" name="dry_film_thickness_max" value=""/></td>
                    <td class="i18n1" name="dryfilmthicknessmin">干膜厚度最小值</td>
                    <td colspan="2"><input class="easyui-numberbox" data-options="precision:2"  type="text" name="dry_film_thickness_min" value=""/></td>
                </tr>
                <tr>
                    <td class="i18n1" name="cutbackmax">预留段最大值</td>
                    <td colspan="2"><input class="easyui-numberbox" data-options="precision:2"  type="text" name="cutback_max" value=""/></td>
                    <td class="i18n1" name="cutbackmin">预留段最小值</td>
                    <td colspan="2"><input class="easyui-numberbox" data-options="precision:2"  type="text" name="cutback_min" value=""/></td>
                </tr>
                <tr>
                    <td class="i18n1" name="residualmagnetismmax">剩磁最大值</td>
                    <td colspan="2"><input class="easyui-numberbox" data-options="precision:2"  type="text" name="residual_magnetism_max" value=""/></td>
                    <td class="i18n1" name="residualmagnetismmin">剩磁最小值</td>
                    <td colspan="2"><input class="easyui-numberbox" data-options="precision:2"  type="text" name="residual_magnetism_min" value=""/></td>
                </tr>
                <tr>
                    <td class="i18n1" name="wetfilmthicknessmax">剩磁最大值</td>
                    <td colspan="2"><input class="easyui-numberbox" data-options="precision:2"  type="text" name="wet_film_thickness_max" value=""/></td>
                    <td class="i18n1" name="wetfilmthicknessmin">剩磁最小值</td>
                    <td colspan="2"><input class="easyui-numberbox" data-options="precision:2"  type="text" name="wet_film_thickness_min" value=""/></td>
                </tr>
            </table>
        </fieldset>
    </form>


</div>
<div id="dlg-buttons" align="center" style="width:900px;">
    <a href="#" class="easyui-linkbutton" iconCls="icon-save" onclick="idAcceptanceFormSubmit()">Save</a>
    <a href="#" class="easyui-linkbutton" id="hlcancelBtn" operationtype="add" iconCls="icon-cancel" onclick="idAcceptanceCancelSubmit()">Cancel</a>
</div>


<script type="text/javascript" src="../easyui/jquery.easyui.min.js"></script>
</body>
</html>
<script type="text/javascript">
    mini.parse();
    var grid= mini.get("multiupload1");

    hlLanguage("../i18n/");
</script>