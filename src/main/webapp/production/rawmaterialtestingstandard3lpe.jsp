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
            $('#hlRawMaterialStandard3LpeProDialog').dialog({
                onClose:function () {
                    clearFormLabel();
                }
            });
            $('.mini-buttonedit .mini-buttonedit-input').css('width','150px');
            // hlLanguage("../i18n/");
        });
        function addRawMaterialStandard3Lpe(){
            $('#hlcancelBtn').attr('operationtype','add');
            $('#hlRawMaterialStandard3LpeDialog').dialog('open').dialog('setTitle','新增');
            $('#RawMaterialStandard3LpeId').text('');
            clearFormLabel();
            url="/rawMaterialACOperation/saveRawMaterialStandard3Lpe.action";
        }
        function delRawMaterialStandard3Lpe() {
            var row = $('#RawMaterialStandard3LpeDatagrids').datagrid('getSelections');
            if(row.length>0){
                var idArr=[];
                for (var i=0;i<row.length;i++){
                    idArr.push(row[i].id);
                }
                var idArrs=idArr.join(',');
                $.messager.confirm('系统提示',"您确定要删除这<font color=red>"+idArr.length+ "</font>条数据吗？",function (r) {
                    if(r){
                        $.post("/rawMaterialACOperation/delRawMaterialStandard3Lpe.action",{"hlparam":idArrs},function (data) {
                            if(data.success){
                                $("#RawMaterialStandard3LpeDatagrids").datagrid("reload");
                            }
                            hlAlertFour(data.message);
                        },"json");
                    }
                });
            }else{
                hlAlertOne();
            }
        }
        function editRawMaterialStandard3Lpe(){
            $('#hlcancelBtn').attr('operationtype','edit');
            var row = $('#RawMaterialStandard3LpeDatagrids').datagrid('getSelected');
            if(row){
                $('#hlRawMaterialStandard3LpeDialog').dialog('open').dialog('setTitle','修改');
                $('#RawMaterialStandard3LpeForm').form('load',row);
                $("#RawMaterialStandard3LpeId").text(row.id);
                var lasttime=formatterdate(row.last_update_time);
                $("#lastupdatetime").text(lasttime);
                url="/rawMaterialACOperation/saveRawMaterialStandard3Lpe.action?id="+row.id;
            }else{
                hlAlertTwo();
            }
        }
        function searchRawMaterialStandard3Lpe() {
            $('#RawMaterialStandard3LpeDatagrids').datagrid('load',{
                'raw_material_testing_acceptance_criteria_no': $('#raw_material_testing_acceptance_criteria_no').val()
            });
        }
        function RawMaterialStandard3LpeFormSubmit() {
            $('#RawMaterialStandard3LpeForm').form('submit',{
                url:url,
                onSubmit:function () {
                    if($("input[name='raw_material_testing_acceptance_criteria_no']").val()==""){

                        hlAlertFour("请输入3LPE原材料接收标准编号");
                        return false;
                    }
                    setParams();
                },
                success: function(result){
                    var result = eval('('+result+')');
                    $('#hlRawMaterialStandard3LpeDialog').dialog('close');
                    if (result.success){
                        $('#RawMaterialStandard3LpeDatagrids').datagrid('reload');
                    }
                    hlAlertFour(result.message);
                },
                error:function () {
                    //clearFormLabel();
                    hlAlertThree();
                }
            });

        }
        function RawMaterialStandard3LpeCancelSubmit() {
            $('#hlRawMaterialStandard3LpeDialog').dialog('close');
        }
        function setParams() {
            setParamsMax($("input[name='epoxy_cure_time_max']"));
            setParamsMin($("input[name='epoxy_cure_time_min']"));
            setParamsMax($("input[name='epoxy_gel_time_max']"));
            setParamsMin($("input[name='epoxy_gel_time_min']"));
            setParamsMax($("input[name='epoxy_moisture_content_max']"));
            setParamsMin($("input[name='epoxy_moisture_content_min']"));
            setParamsMax($("input[name='epoxy_particle_size_150um_max']"));
            setParamsMax($("input[name='epoxy_particle_size_250um_max']"));
            setParamsMin($("input[name='epoxy_particle_size_150um_min']"));
            setParamsMin($("input[name='epoxy_particle_size_250um_min']"));
            setParamsMax($("input[name='adhesion_flow_rate_max']"));
            setParamsMin($("input[name='adhesion_flow_rate_min']"));
            setParamsMax($("input[name='polyethylene_flow_rate_max']"));
            setParamsMin($("input[name='polyethylene_flow_rate_min']"));
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
            $('#RawMaterialStandard3LpeForm').form('clear');
            $('.hl-label').text('');
        }
    </script>





</head>

<body>
<fieldset class="b3" style="padding:10px;margin:10px;">
    <legend> <h3><b style="color: orange" >|&nbsp;</b><span class="i18n1" name="datadisplay">数据展示</span></h3></legend>
    <div  style="margin-top:5px;">
        <table class="easyui-datagrid" id="RawMaterialStandard3LpeDatagrids" url="/rawMaterialACOperation/getRawMaterialStandard3LpeByLike.action" striped="true" loadMsg="正在加载中。。。" textField="text" pageSize="20" fitColumns="true" pagination="true" toolbar="#hlRawMaterialStandard3LpeTb">
            <thead>
            <tr>
                <th data-options="field:'ck',checkbox:true"></th>
                <th field="id" align="center" width="100" class="i18n1" name="id">流水号</th>
                <th field="raw_material_testing_acceptance_criteria_no" align="center" width="150" class="i18n1" name="rawmaterialtestingacceptancecriteriano">原材料接收标准编号</th>
                <th field="epoxy_cure_time_max" align="center" width="150" class="i18n1" name="epoxycuretimemax">环氧树脂固化时间最大值</th>
                <th field="epoxy_cure_time_min" align="center" width="150" class="i18n1" name="epoxycuretimemin">环氧树脂固化时间最小值</th>
                <th field="epoxy_gel_time_max" align="center" width="150" class="i18n1" name="epoxygeltimemax">环氧树脂胶化时间最大值</th>
                <th field="epoxy_gel_time_min" align="center" width="150" class="i18n1" name="epoxygeltimemin">环氧树脂胶化时间最小值</th>
                <th field="epoxy_moisture_content_max" align="center" width="150" class="i18n1" name="epoxymoisturecontentmax">环氧树脂水分含量最大值</th>
                <th field="epoxy_moisture_content_min" align="center" width="150" class="i18n1" name="epoxymoisturecontentmin">环氧树脂水分含量最小值</th>
                <th field="epoxy_particle_size_150um_max" align="center" width="150" class="i18n1" name="epoxyparticlesize150ummax">环氧树脂颗粒度大小最大值150um</th>
                <th field="epoxy_particle_size_150um_min" align="center" width="150" class="i18n1" name="epoxyparticlesize150ummin">环氧树脂颗粒度大小最小值150um</th>
                <th field="epoxy_particle_size_250um_max" align="center" width="150" class="i18n1" name="epoxyparticlesize250ummax">环氧树脂颗粒度大小最大值150um</th>
                <th field="epoxy_particle_size_250um_min" align="center" width="150" class="i18n1" name="epoxyparticlesize250ummin">环氧树脂颗粒度大小最小值150um</th>
                <th field="adhesion_flow_rate_max" align="center" hidden="true" width="50" class="i18n1" name="adhesionflowratemax">附着层流速最大值</th>
                <th field="adhesion_flow_rate_min" align="center" hidden="true" width="100" class="i18n1" name="adhesionflowratemin">附着层流速最小值</th>
                <th field="polyethylene_flow_rate_max" align="center" hidden="true" width="100" class="i18n1" name="polyethyleneflowratemax">聚乙烯流速最大值</th>
                <th field="polyethylene_flow_rate_min" align="center" hidden="true" width="120" class="i18n1" name="polyethyleneflowratemin">聚乙烯流速最小值</th>

            </tr>
            </thead>
        </table>
    </div>
</fieldset>

<!--工具栏-->
<div id="hlRawMaterialStandard3LpeTb" style="padding:10px;">
    <span class="i18n1" name="coatingacceptancecriteriano">涂层判定标准编号</span>:
    <input id="raw_material_testing_acceptance_criteria_no" name="rawmaterialtestingacceptancecriteriano" style="line-height:22px;border:1px solid #ccc">
    <a href="#" class="easyui-linkbutton" plain="true" data-options="iconCls:'icon-search'" onclick="searchRawMaterialStandard3Lpe()">Search</a>
    <div style="float:right">
        <a href="#" id="addObpLinkBtn" class="easyui-linkbutton i18n1" name="add" data-options="iconCls:'icon-add',plain:true" onclick="addRawMaterialStandard3Lpe()">添加</a>
        <a href="#" id="editObpLinkBtn" class="easyui-linkbutton i18n1" name="edit" data-options="iconCls:'icon-edit',plain:true" onclick="editRawMaterialStandard3Lpe()">修改</a>
        <a href="#" id="deltObpLinkBtn" class="easyui-linkbutton i18n1" name="delete" data-options="iconCls:'icon-remove',plain:true" onclick="delRawMaterialStandard3Lpe()">删除</a>
    </div>
</div>

<!--添加、修改框-->
<div id="hlRawMaterialStandard3LpeDialog" class="easyui-dialog" data-options="title:'添加',modal:true"  closed="true" buttons="#dlg-buttons" style="display: none;padding:5px;width:950px;max-height:500px;overflow-y: auto;">
    <form id="RawMaterialStandard3LpeForm" method="post">
        <fieldset style="width:900px;border:solid 1px #aaa;margin-top:8px;position:relative;">
            <legend>3LPE原材料接收标准信息</legend>
            <table class="ht-table" width="100%" border="0">
                <tr>
                    <td class="i18n1" name="id">流水号</td>
                    <td >
                        <label id="RawMaterialStandard3LpeId" class="hl-label"></label>
                    </td>
                    <td></td>
                    <td class="i18n1" name="rawmaterialtestingacceptancecriteriano">原材料接收标准编号</td>
                    <td ><input class="easyui-textbox"  type="text" name="raw_material_testing_acceptance_criteria_no" value=""/></td>
                    <td></td>
                <tr>
                    <td class="i18n1" name="epoxycuretimemax">环氧树脂固化时间最大值</td>
                    <td ><input class="easyui-numberbox" data-options="precision:2"  type="text" name="epoxy_cure_time_max" value=""/></td>
                    <td></td>
                    <td class="i18n1" name="epoxycuretimemin">环氧树脂固化时间最小值</td>
                    <td ><input class="easyui-numberbox" data-options="precision:2"  type="text" name="epoxy_cure_time_min" value=""/></td>
                    <td></td>
                </tr>
                <tr>
                    <td class="i18n1" name="epoxygeltimemax">环氧树脂胶化时间最大值</td>
                    <td ><input class="easyui-numberbox" data-options="precision:2"  type="text" name="epoxy_gel_time_max" value=""/></td>
                    <td></td>
                    <td class="i18n1" name="epoxygeltimemin">环氧树脂胶化时间最小值</td>
                    <td ><input class="easyui-numberbox" data-options="precision:2"  type="text" name="epoxy_gel_time_min" value=""/></td>
                    <td></td>
                </tr>
                <tr>
                    <td class="i18n1" name="epoxymoisturecontentmax">环氧树脂水分含量最大值</td>
                    <td ><input class="easyui-numberbox" data-options="precision:2"  type="text" name="epoxy_moisture_content_max" value=""/></td>
                    <td></td>
                    <td class="i18n1" name="epoxymoisturecontentmin">环氧树脂水分含量最小值</td>
                    <td ><input class="easyui-numberbox" data-options="precision:2"  type="text" name="epoxy_moisture_content_min" value=""/></td>
                    <td></td>
                </tr>
                <tr>
                    <td class="i18n1" name="epoxyparticlesize150ummax">环氧树脂颗粒度大小150um最大值</td>
                    <td ><input class="easyui-numberbox" data-options="precision:2"  type="text" name="epoxy_particle_size_150um_max" value=""/></td>
                    <td></td>
                    <td class="i18n1" name="epoxyparticlesize150ummin">环氧树脂颗粒度大小150um最小值</td>
                    <td ><input class="easyui-numberbox" data-options="precision:2"  type="text" name="epoxy_particle_size_150um_min" value=""/></td>
                    <td></td>
                </tr>
                <tr>
                    <td class="i18n1" name="epoxyparticlesize250ummax">环氧树脂颗粒度大小250um最大值</td>
                    <td ><input class="easyui-numberbox" data-options="precision:2"  type="text" name="epoxy_particle_size_250um_max" value=""/></td>
                    <td></td>
                    <td class="i18n1" name="epoxyparticlesize250ummin">环氧树脂颗粒度大小250um最小值</td>
                    <td ><input class="easyui-numberbox" data-options="precision:2"  type="text" name="epoxy_particle_size_250um_min" value=""/></td>
                    <td></td>
                </tr>
                <tr>
                    <td class="i18n1" name="adhesionflowratemax">附着层流速最大值</td>
                    <td ><input class="easyui-numberbox" data-options="precision:2"  type="text" name="adhesion_flow_rate_max" value=""/></td>
                    <td></td>
                    <td class="i18n1" name="adhesionflowratemin">附着层流速最小值</td>
                    <td ><input class="easyui-numberbox" data-options="precision:2"  type="text" name="adhesion_flow_rate_min" value=""/></td>
                    <td></td>
                </tr>
                <tr>
                    <td class="i18n1" name="polyethyleneflowratemax">聚乙烯流速最大值</td>
                    <td ><input class="easyui-numberbox" data-options="precision:2"  type="text" name="polyethylene_flow_rate_max" value=""/></td>
                    <td></td>
                    <td class="i18n1" name="polyethyleneflowratemin">聚乙烯流速最大值</td>
                    <td ><input class="easyui-numberbox" data-options="precision:2"  type="text" name="polyethylene_flow_rate_min" value=""/></td>
                    <td></td>
                </tr>
                <tr>
                    <td class="i18n1" name="lastupdatetime">最后更新时间</td>
                    <td >
                        <label class="hl-label" id="lastupdatetime" type="text" name="last_update_time" value="" data-options="formatter:myformatter2,parser:myparser2"/>
                    </td>
                    <td></td>

                </tr>
            </table>
        </fieldset>
    </form>


</div>
<div id="dlg-buttons" align="center" style="width:900px;">
    <a href="#" class="easyui-linkbutton" iconCls="icon-save" onclick="RawMaterialStandard3LpeFormSubmit()">Save</a>
    <a href="#" class="easyui-linkbutton" id="hlcancelBtn" operationtype="add" iconCls="icon-cancel" onclick="RawMaterialStandard3LpeCancelSubmit()">Cancel</a>
</div>


<script type="text/javascript" src="../easyui/jquery.easyui.min.js"></script>
</body>
</html>
<script type="text/javascript">
    mini.parse();
    var grid= mini.get("multiupload1");

    hlLanguage("../i18n/");
</script>