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
            $('#hlRawMaterialStandard2FbeProDialog').dialog({
                onClose:function () {
                    clearFormLabel();
                }
            });
            $('.mini-buttonedit .mini-buttonedit-input').css('width','150px');
            // hlLanguage("../i18n/");
        });
        function addRawMaterialStandard2Fbe(){
            $('#hlcancelBtn').attr('operationtype','add');
            $('#hlRawMaterialStandard2FbeDialog').dialog('open').dialog('setTitle','新增');
            $('#RawMaterialStandard2FbeId').text('');
            clearFormLabel();
            url="/rawMaterialACOperation/saveRawMaterialStandard2Fbe.action";
        }
        function delRawMaterialStandard2Fbe() {
            var row = $('#RawMaterialStandard2FbeDatagrids').datagrid('getSelections');
            if(row.length>0){
                var idArr=[];
                for (var i=0;i<row.length;i++){
                    idArr.push(row[i].id);
                }
                var idArrs=idArr.join(',');
                $.messager.confirm('系统提示',"您确定要删除这<font color=red>"+idArr.length+ "</font>条数据吗？",function (r) {
                    if(r){
                        $.post("/rawMaterialACOperation/delRawMaterialStandard2Fbe.action",{"hlparam":idArrs},function (data) {
                            if(data.success){
                                $("#RawMaterialStandard2FbeDatagrids").datagrid("reload");
                            }
                            hlAlertFour(data.message);
                        },"json");
                    }
                });
            }else{
                hlAlertOne();
            }
        }
        function editRawMaterialStandard2Fbe(){
            $('#hlcancelBtn').attr('operationtype','edit');
            var row = $('#RawMaterialStandard2FbeDatagrids').datagrid('getSelected');
            if(row){
                $('#hlRawMaterialStandard2FbeDialog').dialog('open').dialog('setTitle','修改');
                $('#RawMaterialStandard2FbeForm').form('load',row);
                $("#RawMaterialStandard2FbeId").text(row.id);
                url="/rawMaterialACOperation/saveRawMaterialStandard2Fbe.action?id="+row.id;
            }else{
                hlAlertTwo();
            }
        }
        function searchRawMaterialStandard2Fbe() {
            $('#RawMaterialStandard2FbeDatagrids').datagrid('load',{
                'raw_material_testing_acceptance_criteria_no': $('#raw_material_testing_acceptance_criteria_no').val()
            });
        }
        function RawMaterialStandard2FbeFormSubmit() {
            $('#RawMaterialStandard2FbeForm').form('submit',{
                url:url,
                onSubmit:function () {
                    setParams();
                },
                success: function(result){
                    var result = eval('('+result+')');
                    $('#hlRawMaterialStandard2FbeDialog').dialog('close');
                    if (result.success){
                        $('#RawMaterialStandard2FbeDatagrids').datagrid('reload');
                    }
                    hlAlertFour(result.message);
                },
                error:function () {
                    //clearFormLabel();
                    hlAlertThree();
                }
            });

        }
        function RawMaterialStandard2FbeCancelSubmit() {
            $('#hlRawMaterialStandard2FbeDialog').dialog('close');
        }
        function setParams() {
            setParamsMax($("input[name='density_max']"));
            setParamsMin($("input[name='density_min']"));
            setParamsMax($("input[name='particle_size_32um_max']"));
            setParamsMin($("input[name='particle_size_32um_min']"));
            setParamsMax($("input[name='particle_size_150um_max']"));
            setParamsMin($("input[name='particle_size_150um_min']"));
            setParamsMax($("input[name='dsc_tgi_max']"));
            setParamsMin($("input[name='dsc_tgi_min']"));
            setParamsMax($("input[name='dsc_tgf_max']"));
            setParamsMin($("input[name='dsc_tgf_min']"));
            setParamsMax($("input[name='dsc_delta_h_max']"));
            setParamsMin($("input[name='dsc_delta_h_min']"));
            setParamsMax($("input[name='gel_time_lt_20s_max']"));
            setParamsMin($("input[name='gel_time_lt_20s_min']"));
            setParamsMax($("input[name='gel_time_gt_20s_max']"));
            setParamsMin($("input[name='gel_time_gt_20s_min']"));
            setParamsMax($("input[name='volatile_max']"));
            setParamsMin($("input[name='volatile_min']"));
            setParamsMax($("input[name='foaming_cross_sectional_max']"));
            setParamsMin($("input[name='foaming_cross_sectional_min']"));
            setParamsMax($("input[name='foaming_interfacial_max']"));
            setParamsMin($("input[name='foaming_interfacial_min']"));
            setParamsMax($("input[name='hot_water_max']"));
            setParamsMin($("input[name='hot_water_min']"));
            setParamsMax($("input[name='cd_65_24h_max']"));
            setParamsMin($("input[name='cd_65_24h_min']"));
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
            $('#RawMaterialStandard2FbeForm').form('clear');
            $('.hl-label').text('');
        }
    </script>





</head>

<body>
<fieldset class="b3" style="padding:10px;margin:10px;">
    <legend> <h3><b style="color: orange" >|&nbsp;</b><span class="i18n1" name="datadisplay">数据展示</span></h3></legend>
    <div  style="margin-top:5px;">
        <table class="easyui-datagrid" id="RawMaterialStandard2FbeDatagrids" url="/rawMaterialACOperation/getRawMaterialStandard2FbeByLike.action" striped="true" loadMsg="正在加载中。。。" textField="text" pageSize="20" fitColumns="true" pagination="true" toolbar="#hlRawMaterialStandard2FbeTb">
            <thead>
            <tr>
                <th data-options="field:'ck',checkbox:true"></th>
                <th field="id" align="center" width="100" class="i18n1" name="id">流水号</th>
                <th field="raw_material_testing_acceptance_criteria_no" align="center" width="150" class="i18n1" name="rawmaterialtestingacceptancecriteriano">原材料接收标准编号</th>
                <th field="density_max" align="center" width="150" class="i18n1" name="densitymax">密度最大值</th>
                <th field="density_min" align="center" width="150" class="i18n1" name="densitymin">密度最小值</th>
                <th field="particle_size_32um_max" align="center" width="150" class="i18n1" name="particlesize32ummax">颗粒度32um 最大值</th>
                <th field="particle_size_32um_min" align="center" width="150" class="i18n1" name="particlesize32ummin">颗粒度32um 最小值</th>
                <th field="particle_size_150um_max" align="center" width="150" class="i18n1" name="particlesize150ummax">颗粒度150um 最大值</th>
                <th field="particle_size_150um_min" align="center" width="150" class="i18n1" name="particlesize150ummin">颗粒度150um 最小值</th>
                <th field="dsc_tgi_max" align="center" width="150" class="i18n1" name="dsctgimax">dsc tgi最大值</th>
                <th field="dsc_tgi_min" align="center" width="150" class="i18n1" name="dsctgimin">dsc tgi最小值</th>
                <th field="dsc_tgf_max" align="center" hidden="true" width="50" class="i18n1" name="dsctgfmax">dsc tgf最大值</th>
                <th field="dsc_tgf_min" align="center" hidden="true" width="100" class="i18n1" name="dsctgfmin">dsc tgf最小值</th>
                <th field="dsc_delta_h_max" align="center" hidden="true" width="100" class="i18n1" name="dscdeltahmax">dsc delta H最大值</th>
                <th field="dsc_delta_h_min" align="center" hidden="true" width="120" class="i18n1" name="dscdeltahmin">dsc delta H最小值</th>
                <th field="gel_time_lt_20s_max" align="center" width="100" hidden="true" class="i18n1" name="geltimelt20smax">胶化时间  小于20秒  最大值</th>
                <th field="gel_time_lt_20s_min" align="center" width="100" hidden="true" class="i18n1" name="geltimelt20smin">胶化时间  小于20秒  最小值</th>
                <th field="gel_time_gt_20s_max" align="center" width="100" hidden="true" class="i18n1" name="geltimegt20smax">胶化时间  大于20秒  最大值</th>
                <th field="gel_time_gt_20s_min" align="center" width="100" hidden="true" class="i18n1" name="geltimegt20smin">胶化时间  大于20秒  最小值</th>
                <th field="volatile_max" align="center" width="100" hidden="true" class="i18n1" name="volatilemax">挥发度最大值</th>
                <th field="volatile_min" align="center" width="100" hidden="true" class="i18n1" name="volatilemin">挥发度最小值</th>
                <th field="foaming_cross_sectional_max" align="center" width="100" hidden="true" class="i18n1" name="foamingcrosssectionalmax">孔隙率 断面最大值</th>
                <th field="foaming_cross_sectional_min" align="center" width="100" hidden="true" class="i18n1" name="foamingcrosssectionalmin">孔隙率 断面最小值</th>
                <th field="foaming_interfacial_max" align="center" width="100" hidden="true" class="i18n1" name="foaminginterfacialmax">孔隙率 表面最大值</th>
                <th field="foaming_interfacial_min" align="center" width="100" hidden="true" class="i18n1" name="foaminginterfacialmin">孔隙率 表面最小值</th>
                <th field="hot_water_max" align="center" width="100" hidden="true" class="i18n1" name="hotwatermax">水煮 98度 24小时最大值</th>
                <th field="hot_water_min" align="center" width="100" hidden="true" class="i18n1" name="hotwatermin">水煮 98度 24小时最小值</th>
                <th field="cd_65_24h_max" align="center" width="100" hidden="true" class="i18n1" name="cd6524hmax">阴极剥离 65度 24小时 最大值</th>
                <th field="cd_65_24h_min" align="center" width="100" hidden="true" class="i18n1" name="cd6524hmin">阴极剥离 65度 24小时 最小值</th>
            </tr>
            </thead>
        </table>
    </div>
</fieldset>

<!--工具栏-->
<div id="hlRawMaterialStandard2FbeTb" style="padding:10px;">
    <span class="i18n1" name="coatingacceptancecriteriano">涂层判定标准编号</span>:
    <input id="raw_material_testing_acceptance_criteria_no" name="raw_material_testing_acceptance_criteria_no" style="line-height:22px;border:1px solid #ccc">
    <a href="#" class="easyui-linkbutton" plain="true" data-options="iconCls:'icon-search'" onclick="searchRawMaterialStandard2Fbe()">Search</a>
    <div style="float:right">
        <a href="#" id="addObpLinkBtn" class="easyui-linkbutton i18n1" name="add" data-options="iconCls:'icon-add',plain:true" onclick="addRawMaterialStandard2Fbe()">添加</a>
        <a href="#" id="editObpLinkBtn" class="easyui-linkbutton i18n1" name="edit" data-options="iconCls:'icon-edit',plain:true" onclick="editRawMaterialStandard2Fbe()">修改</a>
        <a href="#" id="deltObpLinkBtn" class="easyui-linkbutton i18n1" name="delete" data-options="iconCls:'icon-remove',plain:true" onclick="delRawMaterialStandard2Fbe()">删除</a>
    </div>
</div>

<!--添加、修改框-->
<div id="hlRawMaterialStandard2FbeDialog" class="easyui-dialog" data-options="title:'添加',modal:true"  closed="true" buttons="#dlg-buttons" style="display: none;padding:5px;width:950px;height:auto;">
    <form id="RawMaterialStandard2FbeForm" method="post">
        <fieldset style="width:900px;border:solid 1px #aaa;margin-top:8px;position:relative;">
            <legend>接收标准信息</legend>
            <table class="ht-table" width="100%" border="0">
                <tr>
                    <td class="i18n1" name="id">流水号</td>
                    <td colspan="2">
                        <label id="RawMaterialStandard2FbeId" class="hl-label"></label>
                    </td>
                    <td class="i18n1" name="rawmaterialtestingacceptancecriteriano">原材料接收标准编号</td>
                    <td colspan="2"><input class="easyui-textbox"  type="text" name="raw_material_testing_acceptance_criteria_no" value=""/></td>
                </tr>
                <tr>
                    <td class="i18n1" name="densitymax">密度最大值</td>
                    <td colspan="2"><input class="easyui-numberbox" data-options="precision:2"  type="text" name="density_max" value=""/></td>
                    <td class="i18n1" name="densitymin">密度最小值</td>
                    <td colspan="2"><input class="easyui-numberbox" data-options="precision:2"  type="text" name="density_min" value=""/></td>
                </tr>
                <tr>
                    <td class="i18n1" name="particlesize32ummax">颗粒度32um最大值</td>
                    <td colspan="2"><input class="easyui-numberbox" data-options="precision:2"  type="text" name="particle_size_32um_max" value=""/></td>
                    <td class="i18n1" name="particlesize32ummin">颗粒度32um最小值</td>
                    <td colspan="2"><input class="easyui-numberbox" data-options="precision:2"  type="text" name="particle_size_32um_min" value=""/></td>
                </tr>
                <tr>
                    <td class="i18n1" name="particlesize150ummax">颗粒度150um最大值</td>
                    <td colspan="2"><input class="easyui-numberbox" data-options="precision:2"  type="text" name="particle_size_150um_max" value=""/></td>
                    <td class="i18n1" name="particlesize150ummin">颗粒度150um最小值</td>
                    <td colspan="2"><input class="easyui-numberbox" data-options="precision:2"  type="text" name="particle_size_150um_min" value=""/></td>
                </tr>
                <tr>
                    <td class="i18n1" name="dsctgimax">dsc tgi最大值</td>
                    <td colspan="2"><input class="easyui-numberbox" data-options="precision:2"  type="text" name="dsc_tgi_max" value=""/></td>
                    <td class="i18n1" name="dsctgimin">dsc tgi最小值</td>
                    <td colspan="2"><input class="easyui-numberbox" data-options="precision:2"  type="text" name="dsc_tgi_min" value=""/></td>
                </tr>
                <tr>
                    <td class="i18n1" name="dsctgfmax">dsc tgf最大值</td>
                    <td colspan="2"><input class="easyui-numberbox" data-options="precision:2"  type="text" name="dsc_tgf_max" value=""/></td>
                    <td class="i18n1" name="dsctgfmin">dsc tgf最小值</td>
                    <td colspan="2"><input class="easyui-numberbox" data-options="precision:2"  type="text" name="dsc_tgf_min" value=""/></td>
                </tr>
                <tr>
                    <td class="i18n1" name="dscdeltahmax">dsc delta h最大值</td>
                    <td colspan="2"><input class="easyui-numberbox" data-options="precision:2"  type="text" name="dsc_delta_h_max" value=""/></td>
                    <td class="i18n1" name="dscdeltahmin">dsc delta h最大值</td>
                    <td colspan="2"><input class="easyui-numberbox" data-options="precision:2"  type="text" name="dsc_delta_h_min" value=""/></td>
                </tr>
                <tr>
                    <td class="i18n1" name="geltimelt20smax">胶化时间小于20秒最大值</td>
                    <td colspan="2"><input class="easyui-numberbox" data-options="precision:2"  type="text" name="gel_time_lt_20s_max" value=""/></td>
                    <td class="i18n1" name="geltimelt20smin">胶化时间小于20秒最小值</td>
                    <td colspan="2"><input class="easyui-numberbox" data-options="precision:2"  type="text" name="gel_time_lt_20s_min" value=""/></td>
                </tr>
                <tr>
                    <td class="i18n1" name="geltimegt20smax">胶化时间大于20秒最大值</td>
                    <td colspan="2"><input class="easyui-numberbox" data-options="precision:2"  type="text" name="gel_time_gt_20s_max" value=""/></td>
                    <td class="i18n1" name="geltimegt20smin">胶化时间大于20秒最小值</td>
                    <td colspan="2"><input class="easyui-numberbox" data-options="precision:2"  type="text" name="gel_time_gt_20s_min" value=""/></td>
                </tr>
                <tr>
                    <td class="i18n1" name="volatilemax">挥发度最大值</td>
                    <td colspan="2"><input class="easyui-numberbox" data-options="precision:2"  type="text" name="volatile_max" value=""/></td>
                    <td class="i18n1" name="volatilemin">挥发度最小值</td>
                    <td colspan="2"><input class="easyui-numberbox" data-options="precision:2"  type="text" name="volatile_min" value=""/></td>
                </tr>
                <tr>
                    <td class="i18n1" name="foamingcrosssectionalmax">孔隙率 断面最大值</td>
                    <td colspan="2"><input class="easyui-numberbox" data-options="precision:2"  type="text" name="foaming_cross_sectional_max" value=""/></td>
                    <td class="i18n1" name="foamingcrosssectionalmin">孔隙率 断面最小值</td>
                    <td colspan="2"><input class="easyui-numberbox" data-options="precision:2"  type="text" name="foaming_cross_sectional_min" value=""/></td>
                </tr>
                <tr>
                    <td class="i18n1" name="foaminginterfacialmax">孔隙率 表面最大值</td>
                    <td colspan="2"><input class="easyui-numberbox" data-options="precision:2"  type="text" name="foaming_interfacial_max" value=""/></td>
                    <td class="i18n1" name="foaminginterfacialmin">孔隙率 表面最小值</td>
                    <td colspan="2"><input class="easyui-numberbox" data-options="precision:2"  type="text" name="foaming_interfacial_min" value=""/></td>
                </tr>
                <tr>
                    <td class="i18n1" name="hotwatermax">水煮98度24小时最大值</td>
                    <td colspan="2"><input class="easyui-numberbox" data-options="precision:0"  type="text" name="hot_water_max" value=""/></td>
                    <td class="i18n1" name="hotwatermin">水煮98度24小时最小值</td>
                    <td colspan="2"><input class="easyui-numberbox" data-options="precision:0"  type="text" name="hot_water_min" value=""/></td>
                </tr>
                <tr>
                    <td class="i18n1" name="cd6524hmax">阴极剥离65度24小时最大值</td>
                    <td colspan="2"><input class="easyui-numberbox" data-options="precision:2"  type="text" name="cd_65_24h_max" value=""/></td>
                    <td class="i18n1" name="cd6524hmin">阴极剥离65度24小时最小值</td>
                    <td colspan="2"><input class="easyui-numberbox" data-options="precision:2"  type="text" name="cd_65_24h_min" value=""/></td>
                </tr>
            </table>
        </fieldset>
    </form>


</div>
<div id="dlg-buttons" align="center" style="width:900px;">
    <a href="#" class="easyui-linkbutton" iconCls="icon-save" onclick="RawMaterialStandard2FbeFormSubmit()">Save</a>
    <a href="#" class="easyui-linkbutton" id="hlcancelBtn" operationtype="add" iconCls="icon-cancel" onclick="RawMaterialStandard2FbeCancelSubmit()">Cancel</a>
</div>


<script type="text/javascript" src="../easyui/jquery.easyui.min.js"></script>
</body>
</html>
<script type="text/javascript">
    mini.parse();
    var grid= mini.get("multiupload1");

    hlLanguage("../i18n/");
</script>