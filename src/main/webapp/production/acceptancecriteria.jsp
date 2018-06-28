<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
    String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>螺纹检验标准</title>
    <link rel="stylesheet" type="text/css" href="../easyui/themes/bootstrap/easyui.css">
    <link rel="stylesheet" type="text/css" href="../easyui/themes/icon.css">
    <link href="../miniui/multiupload/multiupload.css" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" href="../css/common.css"/>
    <script src="../easyui/jquery.min.js" type="text/javascript"></script>
    <script src="../js/common.js" type="text/javascript"></script>
    <script src="../miniui/boot.js" type="text/javascript"></script>
    <script  src="../js/lrscroll.js" type="text/javascript"></script>
    <script src="../js/jquery.i18n.properties-1.0.9.js" type="text/javascript"></script>
    <script src="../js/language.js" type="text/javascript"></script>
    <script type="text/javascript">
        var url;
        var staticItem=[];
        var addOrEdit=true;
        $(function () {
            $('#addEditDialog').css('top','30px');
            $('#addEditDialog').dialog({
                onClose:function () {
                    clearFormLabel();
                }
            });
            $('.mini-buttonedit .mini-buttonedit-input').css('width','150px');
        });
        function addFunction(){
            $('#hlcancelBtn').attr('operationtype','add');
            $('#addEditDialog').dialog('open').dialog('setTitle','新增');
            $('#serialNumber').text('');
            clearFormLabel();
            $("#acceptance_criteria_no").textbox('setValue',"AC"+new Date().getTime());
            url="/ACOperation/saveAC.action";
        }
        function delFunction() {
            var row = $('#contentDatagrids').datagrid('getSelections');
            if(row.length>0){
                var idArr=[];
                for (var i=0;i<row.length;i++){
                    idArr.push(row[i].id);
                }
                var idArrs=idArr.join(',');
                $.messager.confirm('系统提示',"您确定要删除这<font color=red>"+idArr.length+ "</font>条数据吗？",function (r) {
                    if(r){
                        $.post("/ACOperation/delAC.action",{hlparam:idArrs},function (data) {
                            if(data.success){
                                $("#contentDatagrids").datagrid("reload");
                            }
                            hlAlertFour(data.message);
                        },"json");
                    }
                });
            }else{
                hlAlertOne();
            }
        }
        function editFunction(){
            $('#hlcancelBtn').attr('operationtype','edit');
            var row = $('#contentDatagrids').datagrid('getSelected');
            if(row){
                $('#addEditDialog').dialog('open').dialog('setTitle','修改');
                row.last_update_time=getDate1(row.last_update_time);
                $('#addEditForm').form('load',row);
                $("#serialNumber").text(row.id);
                url="/ACOperation/saveAC.action?id="+row.id;
                loadDynamicByAcceptanceNo(row.acceptance_criteria_no);
            }else{
                hlAlertTwo();
            }
        }
        function Search() {
            $('#contentDatagrids').datagrid('load',{
                'acceptance_criteria_no': $('#acceptancecriteriano').val(),
                'external_coating_type': $('#externalSelect').val(),
                'internal_coating_type': $('#internalSelect').val()
            });
        }
        function  loadDynamicByAcceptanceNo(acceptance_criteria_no) {
            //dg加载测量项数据
            $('#dg').datagrid('load',{
                'acceptance_criteria_no':acceptance_criteria_no,
            });
        }
        function addEditFormSubmit() {
            $('#addEditForm').form('submit',{
                url:url,
                onSubmit:function () {
                    if($("#acceptance_criteria_no").val()==undefined||$("#acceptance_criteria_no").val()==""){
                        hlAlertFour("未生成接收标准编号");
                        return false;
                    }
                },
                success: function(result){
                    var result = eval('('+result+')');
                    $('#addEditDialog').dialog('close');
                    if (result.success){
                        $('#contentDatagrids').datagrid('reload');
                    }
                    hlAlertFour(result.message);
                },
                error:function () {
                    hlAlertThree();
                }
            });
        }
        function CancelSubmit() {
            $('#addEditDialog').dialog('close');
        }

        function  clearFormLabel(){
            $('#addEditForm').form('clear');
            $('.hl-label').text('');
        }
        //测量项
        var editIndex = undefined;
        function endEditing(){
            if (editIndex == undefined){return true}
            if ($('#dg').datagrid('validateRow', editIndex)){
                var ed = $('#dg').datagrid('getEditor', {index:editIndex,field:'item_frequency'});
                var text = $(ed.target).combobox('getText');
                $('#dg').datagrid('getRows')[editIndex]['item_frequency_name'] = text;
                var ed1 = $('#dg').datagrid('getEditor', {index:editIndex,field:'process_code'});
                var text1 = $(ed1.target).combobox('getText');
                $('#dg').datagrid('getRows')[editIndex]['process_name'] = text1;
                var ed2 = $('#dg').datagrid('getEditor', {index:editIndex,field:'control_type'});
                var text2 = $(ed2.target).combobox('getText');
                $('#dg').datagrid('getRows')[editIndex]['control_type_name'] = text2;

                $('#dg').datagrid('endEdit', editIndex);

                //alert("开始验证");
                editIndex = undefined;
                return true;
            } else {
                return false;
            }
        }
        function onClickRow(index){
            if(editIndex!=undefined){
                $('#dg').datagrid('selectRow', editIndex);
                return;
            }
            if (editIndex != index){
                if (endEditing()){
                    $('#dg').datagrid('selectRow', index)
                        .datagrid('beginEdit', index);
                    editIndex = index;
                } else {
                    $('#dg').datagrid('selectRow', editIndex);
                }
            }
        }
        function onClickCell(index,field) {
            alert(field);
            var ed = $('#dg').datagrid('getEditor', {index:editIndex,field:field});
            if(ed){
                var text = $(ed.target).textbox('getText');
                alert(text);
            }
        }
        function append(){
            if (endEditing()){
                $('#dg').datagrid('appendRow',{item_code:"IT"+new Date().getTime(),decimal_num:'0',max_value:'0',min_value:'0',default_value:'0',status:'P'});
                editIndex = $('#dg').datagrid('getRows').length-1;
                $('#dg').datagrid('selectRow', editIndex)
                    .datagrid('beginEdit', editIndex);
            }
        }
        function removeit(){
            if (editIndex == undefined){return}
            $('#dg').datagrid('cancelEdit', editIndex)
                .datagrid('deleteRow', editIndex);
            editIndex = undefined;
        }
        function accept(){
            if (endEditing()){
                alert("保存");
                $('#dg').datagrid('acceptChanges');
                var row = $('#dg').datagrid('getSelected');

                alert(row.item_code+":"+row.item_name+":"+row.item_name_en+":"+row.unit_name+":"+row.unit_name_en+":"+row.item_frequency+":"+row.process_code);
            }
        }
        function reject(){
            $('#dg').datagrid('rejectChanges');
            editIndex = undefined;
        }
        function submitItemInfo() {

        }
    </script>
</head>

<body>
<fieldset class="b3" style="padding:10px;margin:10px;">
    <legend> <h3><b style="color: orange" >|&nbsp;</b><span class="i18n1" name="datadisplay">数据展示</span></h3></legend>
    <div  style="margin-top:5px;">
        <table class="easyui-datagrid" id="contentDatagrids" url="/ACOperation/getACByLike.action" striped="true" loadMsg="正在加载中。。。" textField="text" pageSize="20" fitColumns="true" pagination="true" toolbar="#toolsTab">
            <thead>
            <tr>
                <th data-options="field:'ck',checkbox:true"></th>
                <th field="id" align="center" width="100" class="i18n1" name="id"></th>
                <th field="acceptance_criteria_no" align="center" width="100" class="i18n1" name="acceptancecriteriano"></th>
                <th field="external_coating_type" align="center" width="100" class="i18n1" name="externalcoatingtype"></th>
                <th field="internal_coating_type" align="center" width="100" class="i18n1" name="internalcoatingtype"></th>
                <th field="remark" align="center" width="100" class="i18n1" name="remark"></th>
                <th field="last_update_time" align="center" width="100" class="i18n1" name="lastupdatetime" data-options="formatter:formatterdate"></th>
            </tr>
            </thead>
        </table>
    </div>
</fieldset>

<!--工具栏-->
<div id="toolsTab" style="padding:10px;">
    <span class="i18n1" name="acceptancecriteriano">接收标准编号</span>:
    <input id="acceptancecriteriano" style="line-height:22px;border:1px solid #ccc">
    <span class="i18n1" name="externalcoatingtype">外防类型</span>:
    <select id="externalSelect" class="easyui-combobox" data-options="editable:false" name="external_coating_type"   style="width:200px;">
        <option value="" selected="selected">ALL</option>
        <option value="2FBE">2FBE</option>
        <option value="3LPE">3LPE</option>
    </select>
    <span class="i18n1" name="internalcoatingtype">内防类型</span>:
    <select id="internalSelect" class="easyui-combobox" data-options="editable:false" name="internal_coating_type"   style="width:200px;">
        <option value="" selected="selected">ALL</option>
        <option value="EPOXY">EPOXY</option>
    </select>
    <a href="#" class="easyui-linkbutton" plain="true" data-options="iconCls:'icon-search'" onclick="Search()">Search</a>
    <div style="float:right">
        <a href="#" id="addObpLinkBtn" class="easyui-linkbutton i18n1" name="add" data-options="iconCls:'icon-add',plain:true" onclick="addFunction()">添加</a>
        <a href="#" id="editObpLinkBtn" class="easyui-linkbutton i18n1" name="edit" data-options="iconCls:'icon-edit',plain:true" onclick="editFunction()">修改</a>
        <a href="#" id="deltObpLinkBtn" class="easyui-linkbutton i18n1" name="delete" data-options="iconCls:'icon-remove',plain:true" onclick="delFunction()">删除</a>
    </div>
</div>
<!--添加、修改框-->
<div id="addEditDialog" class="easyui-dialog" data-options="title:'添加',modal:true" closed="true" buttons="#dlg-buttons" style="display: none;padding:5px 0px;width:1150px;max-height:500px;overflow-y:auto;">
    <form id="addEditForm" method="post">
        <fieldset style="width:99%;border:solid 1px #aaa;position:relative;">
            <legend>标准信息</legend>
            <div style="width:100%;padding-bottom:5px;">
                <table class="ht-table"  width="100%" border="0">
                    <tr>
                        <td class="i18n1" name="id">流水号</td>
                        <td colspan="1">
                            <label id="serialNumber" class="hl-label"></label>
                        </td>
                        <td></td>
                        <td class="i18n1" name="acceptancecriteriano"></td>
                        <td><input class="easyui-textbox" id="acceptance_criteria_no"   type="text" name="acceptance_criteria_no" readonly="true" value=""/></td>
                        <td></td>

                    </tr>
                    <tr>
                        <td class="i18n1" name="externalcoatingtype"></td>
                        <td>
                            <select id="external_coating_type" class="easyui-combobox" data-options="editable:false" name="external_coating_type"   style="width:200px;">
                                <option value="2FBE">2FBE</option>
                                <option value="3LPE">3LPE</option>
                            </select>
                        </td>
                        <td></td>
                        <td class="i18n1" name="internalcoatingtype"></td>
                        <td>
                            <select id="internal_coating_type" class="easyui-combobox" data-options="editable:false" name="internal_coating_type"   style="width:200px;">
                                <option value="EPOXY">EPOXY</option>
                            </select>
                        </td>
                        <td></td>
                    </tr>
                    <tr>
                        <td class="i18n1" name="lastupdatetime"></td>
                        <td><input class="easyui-textbox"   type="text" readonly="true" name="last_update_time" value=""/></td>
                        <td></td>
                        <td class="i18n1" name="remark"></td>
                        <td><input class="easyui-textbox"   type="text" name="remark" value=""/></td>
                        <td></td>
                    </tr>


                </table>
                <div id="dlg-buttons" align="center" style="width:900px;">
                    <a href="#" class="easyui-linkbutton" iconCls="icon-save" onclick="addEditFormSubmit()">Save</a>
                    <a href="#" class="easyui-linkbutton" id="hlcancelBtn" operationtype="add" iconCls="icon-cancel" onclick="CancelSubmit()">Cancel</a>
                </div>
            </div>
        </fieldset>
        <fieldset style="width:99%;border:solid 1px #aaa;position:relative;">
            <legend>测量项信息</legend>
            <table id="dg" class="easyui-datagrid" title="" style="width:100%;height:auto" data-options="
				iconCls: '',
				singleSelect: true,
				toolbar: '#tb',
				onClickRow: onClickRow,
				onClickCell:onClickCell
			">
                <thead>
                <tr>
                    <%--<th class="i18n1" name="acceptancecriteriano" data-options="field:'acceptance_criteria_no',width:80"></th>--%>
                    <th class="i18n1" name="itemcode" data-options="field:'item_code'"></th>
                    <th class="i18n1" name="itemname" data-options="field:'item_name',editor:'textbox',required:true"></th>
                    <th class="i18n1" name="itemnameen" data-options="field:'item_name_en',editor:'textbox',required:true"></th>
                    <th class="i18n1" name="unitname" data-options="field:'unit_name',editor:'textbox'"></th>
                    <th class="i18n1" name="unitnameen" data-options="field:'unit_name_en',editor:'textbox'"></th>
                    <th class="i18n1" name="itemfrequency" data-options="field:'item_frequency',width:150,formatter:function(value,row){
							return row.item_frequency_name;
						},
						editor:{
							type:'combobox',
							options:{
								valueField:'item_frequency',
								textField:'item_frequency_name',
								method:'get',
								url:'../data/freq.json',
								required:true
							}
						}"></th>
                    <th class="i18n1" name="processcode" data-options="field:'process_code',width:150,formatter:function(value,row){
							return row.process_name;
						},
						editor:{
							type:'combobox',
							width:150,
							options:{
								valueField:'process_code',
								textField:'process_name',
								method:'get',
								url:'/ProcessOperation/getAllProcess.action',
								required:true
							}
						}"></th>
                    <th class="i18n1" name="decimalnum" data-options="field:'decimal_num',editor:{type:'numberbox'}"></th>
                    <th class="i18n1" name="needverify" data-options="field:'need_verify',editor:{type:'checkbox',options:{on:'1',off:'0'}}"></th>
                        <th class="i18n1" name="maxvalue" data-options="field:'max_value',editor:{type:'numberbox',options:{precision:2}}"></th>
                        <th class="i18n1" name="minvalue" data-options="field:'min_value',editor:{type:'numberbox',options:{precision:2}}"></th>
                        <th class="i18n1" name="defaultvalue" data-options="field:'default_value',width:150,editor:{type:'textbox'}"></th>

                        <th class="i18n1" name="controltype" data-options="field:'control_type',width:100,formatter:function(value,row){
							return row.control_type_name;
						},
						editor:{
							type:'combobox',
							options:{
								valueField:'control_type',
								textField:'control_type_name',
								method:'get',
								url:'../data/control.json',
								required:true
							}
						}"></th>
                    <th class="i18n1" name="options" data-options="field:'options',editor:'textbox',onLoadSuccess:function(row,data){
                        var ed = $('#dg').datagrid('getEditor', {index:editIndex,field:'options'});
                        var text = $(ed.target).bind('click',function(){
                           alert(123);
                        });
                    }"></th>
                    <th class="i18n1" name="status" data-options="field:'status',align:'center',editor:{type:'checkbox',options:{on:'P',off:''}}">Status</th>
                </tr>
                </thead>
            </table>
            <div id="tb" style="height:auto">
                <a href="javascript:void(0)" class="easyui-linkbutton i18n1" name="add" data-options="iconCls:'icon-add',plain:true" onclick="append()">Append</a>
                <a href="javascript:void(0)" class="easyui-linkbutton i18n1" name="delete" data-options="iconCls:'icon-remove',plain:true" onclick="removeit()">Remove</a>
                <a href="javascript:void(0)" class="easyui-linkbutton i18n1" name="save" data-options="iconCls:'icon-save',plain:true" onclick="accept()">Accept</a>
                <a href="javascript:void(0)" class="easyui-linkbutton i18n1" name="undo" data-options="iconCls:'icon-undo',plain:true" onclick="reject()">Reject</a>
            </div>
        </fieldset>
    </form>
</div>
<script type="text/javascript" src="../easyui/jquery.easyui.min.js"></script>
</body>
</html>
<script type="text/javascript">
    mini.parse();
    hlLanguage("../i18n/");
</script>