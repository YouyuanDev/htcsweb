<%--
  Created by IntelliJ IDEA.
  User: kurt
  Date: 2/15/18
  Time: 11:51 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>钢管管理</title>
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

        function searchPipe() {

            $('#pipeDatagrids').datagrid('load',{
                'project_no': $('#projectno').val(),
                'contract_no': $('#contractno').val(),
                'pipe_no':$('#pipeno').val(),
                'status':$('#pstatus').val()
            });
        }
        $(function () {

            $('#hlPipeDialog').dialog({
                onClose:function () {
                    var type=$('#hlcancelBtn').attr('operationtype');
                    if(type=="add"){

                    }else{

                        clearFormLabel();
                    }
                }
            });
            $('.mini-buttonedit .mini-buttonedit-input').css('width','150px');

        });


        function addPipe(){
            $('#hlcancelBtn').attr('operationtype','add');
            $('#hlPipeDialog').dialog('open').dialog('setTitle','新增钢管');
            $('#pipeForm').form('clear');
            url="/pipeinfo/savePipe.action";
        }
        function editPipe() {
            $('#hlcancelBtn').attr('operationtype','edit');
            var row = $('#pipeDatagrids').datagrid('getSelected');
            if(row) {
                $('#hlPipeDialog').dialog('open').dialog('setTitle', '修改');
                $('#pipeForm').form('load', row);
                //通过以上方法无法将pipeid初始化，所以再调用以下方法赋值
                 $('#pipeForm').form('load',{
                     'pipeid':row.id
                 });
                if(row.contract_no!=null)
                    look1.setText(row.contract_no);
                // $('#contractForm').form('load',{
                //     'contractid':row.id,
                //     'project_no':row.project_no,
                //     'project_name':row.project_name,
                //     'contract_no':row.contract_no,
                //     'od':row.od,
                //     'wt':row.wt,
                //     'external_coating':row.external_coating,
                //     'internal_coating':row.internal_coating,
                //     'grade':row.grade,
                //     'total_order_length':row.total_order_length,
                //     'total_order_weight':row.total_order_weight,
                //     'weight_per_meter':row.weight_per_meter,
                //     'pipe_length':row.pipe_length
                //
                // });


                url="/pipeinfo/savePipe.action?id="+row.id;

            }else{
                hlAlertTwo();
            }
        }
        function delPipe() {
            var row = $('#pipeDatagrids').datagrid('getSelections');
            if(row.length>0){
                var idArr=[];
                for (var i=0;i<row.length;i++){
                    idArr.push(row[i].id);
                }
                var idArrs=idArr.join(',');

                $.messager.confirm('系统提示',"您确定要删除这<font color=red>"+idArr.length+ "</font>条数据吗？",function (r) {
                    if(r){
                        $.post(
                            "/pipeinfo/delPipe.action",
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
                hlAlertOne();
            }
        }
        function GenQRCode(){
            var row = $('#pipeDatagrids').datagrid('getSelections');
            if(row.length>0){
                var idArr=[];
                for (var i=0;i<row.length;i++){
                    idArr.push(row[i].pipe_no);
                }
                var idArrs=idArr.join(',');

                $.messager.confirm('系统提示',"您确定要生成这<font color=red>"+idArr.length+ "</font>条QR码吗？",function (r) {
                    if(r){
                        // $.get(
                        //     "/QrCodeOperation/genQRCode.action",
                        //     {"hlparam":idArrs});
                        var form=$("<form>");//定义一个form表单
                        form.attr("style","display:none");
                        form.attr("target","");
                        form.attr("method","post");//请求类型
                        form.attr("action","/QrCodeOperation/genQRCode.action");//请求地址
                        $("body").append(form);//将表单放置在web中
                        var input1=$("<input>");
                        input1.attr("type","hidden");
                        input1.attr("name","hlparam");
                        input1.attr("value",idArrs);
                        form.append(input1);
                        form.submit();//表单提交

                    }
                });

            }else{
                $.messager.alert('Warning','请选择要生成QR码的钢管!');
            }

        }


        //取消保存
        function PipeFormCancelSubmit() {
            $('#hlPipeDialog').dialog('close');


        }


        //增加或保存钢管信息
        function PipeFormSubmit() {

            $('#pipeForm').form('submit',{
                url:url,
                onSubmit:function () {
                    //表单验证

                    if($("input[name='contract_no']").val()==""){

                        hlAlertFour("请输入合同编号");
                        return false;
                    }
                    else if($("input[name='grade']").val()==""){

                        hlAlertFour("请输入钢种");
                        return false;
                    }
                    else if($("input[name='heat_no']").val()==""){

                        hlAlertFour("请输入炉号");
                        return false;
                    }
                    else if($("input[name='pipe_making_lot_no']").val()==""){

                        hlAlertFour("请输入制管批号");
                        return false;
                    }

                    else if($("input[name='od']").val()==""){
                        hlAlertFour("请输入外径");
                        return false;
                    }
                    else if($("input[name='wt']").val()==""){
                        hlAlertFour("请输入壁厚");
                        return false;
                    }
                    else if($("input[name='p_length']").val()==""){
                        hlAlertFour("请输入长度");
                        return false;

                    }
                    else if($("input[name='weight']").val()==""){
                        hlAlertFour("请输入重量");
                        return false;

                    }
                    else if($("input[name='status']").val()==""){

                        hlAlertFour("请输入状态");
                        return false;
                    }


                    //return $('#pipeForm').form('enableValidation').form('validate');
                },
                success: function(result){
                    var result = eval('('+result+')');
                    if (result.success){
                        $('#hlPipeDialog').dialog('close');
                        $('#pipeDatagrids').datagrid('reload');
                        clearFormLabel();
                        //$('#hl-gallery-con').empty();
                        // $('#pipeDatagrids').datagrid('clearSelections');
                    }
                    hlAlertFour(result.message);
                },
                error:function () {
                    hlAlertThree();
                }
            });

        }

        function  clearFormLabel() {
            $('#pipeForm').form('clear');

        }

    </script>

</head>
<body>
<fieldset class="b3" style="padding:10px;margin:10px;">
    <legend> <h3><b style="color: orange" >|&nbsp;</b><span class="i18n1" name="datadisplay">数据展示</span></h3></legend>
    <div  style="margin-top:5px;">
        <table class="easyui-datagrid" id="pipeDatagrids" url="/pipeinfo/getPipeInfoByLike.action" striped="true" loadMsg="正在加载中。。。" textField="text" pageSize="20" fitColumns="true" pagination="true" toolbar="#hlpipeTb">
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
    <input id="pstatus" class="easyui-combobox" type="text" name="pstatus"  data-options=
            "url:'/pipeinfo/getAllPipeStatusWithComboboxSelectAll.action',
					        method:'get',
					        valueField:'id',
					        width: 200,
					        editable:false,
					        textField:'text',
					        panelHeight:'auto'"/>

    <a href="#" class="easyui-linkbutton" plain="true" data-options="iconCls:'icon-search'" onclick="searchPipe()">Search</a>
    <div style="float:right">
        <a href="#" id="genQRLinkBtn" class="easyui-linkbutton i18n1" name="genQR"  onclick="GenQRCode()">生成QRCode</a>

        <a href="#" id="addPipeLinkBtn" class="easyui-linkbutton i18n1" name="add" data-options="iconCls:'icon-add',plain:true" onclick="addPipe()">添加</a>
        <a href="#" id="editPipeLinkBtn" class="easyui-linkbutton i18n1" name="edit" data-options="iconCls:'icon-edit',plain:true" onclick="editPipe()">修改</a>
        <a href="#" id="deltPipeLinkBtn" class="easyui-linkbutton i18n1" name="delete" data-options="iconCls:'icon-remove',plain:true" onclick="delPipe()">删除</a>
    </div>
</div>

<!--添加、修改框-->
<div id="hlPipeDialog" class="easyui-dialog" data-options="title:'添加',modal:true"  closed="true" buttons="#dlg-buttons" style="display: none;padding:5px;width:950px;height:auto;">
    <form id="pipeForm" method="post">
        <fieldset style="width:900px;border:solid 1px #aaa;margin-top:8px;position:relative;">
            <legend class="i18n1" name="pipeinfo">钢管信息</legend>
            <table class="ht-table" width="100%" border="0">
                <tr>
                    <td class="i18n1" name="id">流水号</td>
                    <td width="33%"><input class="easyui-textbox" type="text" name="pipeid" readonly="true" value=""/></td>
                    <td class="i18n1" name="pipeno" width="16%">钢管编号</td>
                    <td width="33%"><input class="easyui-validatebox" type="text" name="pipe_no" value=""/></td>

                </tr>
                <tr>
                    <td class="i18n1" name="projectno" width="16%">项目编号</td>
                    <td width="33%"><input class="easyui-validatebox" type="text" name="project_no" readonly="true"  value=""/></td>

                    <td class="i18n1" name="projectname" width="16%">项目名称</td>
                    <td width="33%"><input class="easyui-validatebox" type="text" name="project_name"  readonly="true" value=""/></td>

                </tr>

                <tr>
                    <td class="i18n1" name="contractno" width="16%">合同编号</td>
                    <td colspan="1" width="33%">
                        <input id="lookup1" name="contract_no" class="mini-lookup" style="text-align:center;width:180px;"
                               textField="contract_no" valueField="id" popupWidth="auto"
                               popup="#gridPanel1" grid="#datagrid1" multiSelect="false"/>
                    </td>
                    <td class="i18n1" name="grade" width="16%">钢种</td>
                    <td  width="33%"><input class="easyui-validatebox" type="text" name="grade" value=""/></td>

                </tr>
                <tr>
                    <td class="i18n1" name="heatno" width="16%">炉号</td>
                    <td   width="33%"><input class="easyui-textbox"  type="text" name="heat_no" value=""/></td>

                    <td class="i18n1" name="pipemakinglotno" width="16%">制管批号</td>
                    <td  width="33%"><input class="easyui-textbox" type="text" name="pipe_making_lot_no" value=""/></td>

                </tr>
                <tr>
                    <td class="i18n1" name="od" width="16%">外径</td>
                    <td   width="33%"><input class="easyui-numberbox"  data-options="min:0,precision:2" type="text" name="od" value=""/></td>

                    <td class="i18n1" name="wt" width="16%">壁厚</td>
                    <td  width="33%"><input class="easyui-numberbox" data-options="min:0,precision:2" type="text" name="wt" value=""/></td>

                </tr>
                <tr>
                    <td class="i18n1" name="p_length" width="16%">管长</td>
                    <td   width="33%"><input class="easyui-numberbox" data-options="min:0,precision:2" type="text" name="p_length" value=""/></td>

                    <td class="i18n1" name="weight" width="16%">重量</td>
                    <td   width="33%"><input class="easyui-numberbox" data-options="min:0,precision:2" type="text" name="weight" value=""/></td>

                </tr>
                <tr>
                    <td class="i18n1" name="storagestack" width="16%">垛位号</td>
                    <td   width="33%">
                        <select class="easyui-combobox" data-options="editable:false" name="storage_stack" style="width:200px;">
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
                        <select class="easyui-combobox" data-options="editable:false" name="stack_level" style="width:200px;">
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
                        </select>
                    </td>

                </tr>
                <tr>
                    <td class="i18n1" name="status" width="16%">状态</td>
                    <td   width="33%"><input id="status" class="easyui-combobox" type="text" name="status"  data-options=
                            "url:'/pipeinfo/getAllPipeStatus.action',
					        method:'get',
					        valueField:'id',
					        editable:false,
					        textField:'text',
					        panelHeight:'auto'"/></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>

                </tr>

            </table>

        </fieldset>

    </form>


</div>
<div id="dlg-buttons" align="center" style="width:900px;">
    <a href="#" class="easyui-linkbutton" iconCls="icon-save" onclick="PipeFormSubmit()">Save</a>
    <a href="#" class="easyui-linkbutton" id="hlcancelBtn" operationtype="add" iconCls="icon-cancel" onclick="PipeFormCancelSubmit()">Cancel</a>
</div>

<div id="gridPanel1" class="mini-panel" title="header" iconCls="icon-add" style="width:500px;height:280px;"
     showToolbar="true" showCloseButton="true" showHeader="false" bodyStyle="padding:0" borderStyle="border:0"
>
    <div property="toolbar" id="searchBar1" style="padding:5px;padding-left:8px;text-align:center;display: none">
        <div style="float:left;padding-bottom:2px;">
            <span class="i18n1" name="contractno">合同编号</span><span>:</span>
            <input id="keyText_contract_no" class="mini-textbox" style="width:110px;" onenter="onSearchClick(1)"/>

            <a class="mini-button" onclick="onSearchClick(1)">查找</a>
            <a class="mini-button" onclick="onClearClick(1)" name="clear">清除</a>
        </div>
        <div style="float:right;padding-bottom:2px;">
            <a class="mini-button" onclick="onCloseClick(1)" name="close">关闭</a>
        </div>
        <div style="clear:both;"></div>
    </div>
    <div id="datagrid1" class="mini-datagrid" style="width:100%;height:100%;"
         borderStyle="border:0" showPageSize="false" showPageIndex="false"
         url="/ContractOperation/getContractInfoByContractNo.action">
        <div property="columns">
            <div type="checkcolumn" width="20"></div>

            <div field="contract_no" width="80" headerAlign="center" allowSort="true" class="i18n1" name="contractno">合同编号</div>
            <div field="project_no" width="40" headerAlign="center" allowSort="true" class="i18n1" name="projectno">项目编号</div>
            <div field="grade" width="40" headerAlign="center" allowSort="true" class="i18n1" name="grade">钢种</div>
            <div field="od" width="40" headerAlign="center" allowSort="true" class="i18n1" name="od">外径</div>
            <div field="wt" width="40" headerAlign="center" allowSort="true" class="i18n1" name="wt">壁厚</div>
            <div field="total_order_length" width="40" headerAlign="center" allowSort="true" class="i18n1" name="total_order_length">合同总长度</div>


        </div>
    </div>

</div>


<script type="text/javascript" src="../easyui/jquery.easyui.min.js"></script>
</body>
</html>
<script type="text/javascript">
    mini.parse();
    var grid1=mini.get("datagrid1");
    grid1.load();

    var keyText1=mini.get('keyText_contract_no');
    var look1=mini.get('lookup1');



    function onSearchClick(type) {
        if(type==1)
        {
            grid1.load({
                contract_no:keyText1.value,
            });
        }

    }
    function onCloseClick(type) {
        if(type==1)
            look1.hidePopup();

    }
    function onClearClick(type) {
        if(type==1)
            look1.deselectAll();
    }
    look1.on('valuechanged',function () {
        var rows = grid1.getSelected();
        $("input[name='contract_no']").val(rows.contract_no);
        $("input[name='project_no']").val('');
        $("input[name='project_name']").val('');
        $.ajax({
            url:'../ProjectOperation/getProjectInfoByContract.action',
            data:{'contract_no':rows.contract_no},
            dataType:'json',
            success:function (data) {
                $.each(data,function (index,element) {
                    $("input[name='project_no']").val(element.project_no);
                    $("input[name='project_name']").val(element.project_name);
                });

            },
            error:function () {
                hlAlertThree();
            }
        });
    });

    look1.on("showpopup",function(e){
        $('.mini-shadow').css('z-index','99999');
        $('.mini-popup').css('z-index','100000');
        $('.mini-panel').css('z-index','100000');
        $('#searchBar1').css('display','block');
        //$('.mini-buttonedit .mini-buttonedit-input').css('width','150px');
    });

    hlLanguage("../i18n/");
</script>
