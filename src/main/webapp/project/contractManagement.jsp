<%--
  Created by IntelliJ IDEA.
  User: kurt
  Date: 2/14/18
  Time: 10:27 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>


<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>合同管理</title>
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


        // 日期格式为 2/20/2017 12:00:00 PM
        function myformatter2(date){
            return date.toLocaleString();
        }
        // 日期格式为 2/20/2017 12:00:00 PM
        function myparser2(s) {
            if (!s) return new Date();
            return new Date(Date.parse(s));
        }


        function searchContract() {

            $('#contractDatagrids').datagrid('load',{
                'project_no': $('#projectno').val(),
                'project_name': $('#projectname').val(),
                'contract_no': $('#contractno').val()
            });
        }
        $(function () {

            $('#hlContractDialog').dialog({
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


        function addContract(){
            $('#hlcancelBtn').attr('operationtype','add');
            $('#hlContractDialog').dialog('open').dialog('setTitle','新增合同');
            $('#contractForm').form('clear');
            url="/ContractOperation/saveContract.action";
        }
        function editContract() {
            $('#hlcancelBtn').attr('operationtype','edit');
            var row = $('#contractDatagrids').datagrid('getSelected');
            if(row){
                $('#hlContractDialog').dialog('open').dialog('setTitle','修改');
                $('#contractForm').form('load',row);
                look1.setText(row.project_no);
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


                url="/ContractOperation/saveContract.action?id="+row.id;

            }else{
                hlAlertTwo();
            }
        }
        function delContract() {
            var row = $('#contractDatagrids').datagrid('getSelections');
            if(row.length>0){
                var idArr=[];
                for (var i=0;i<row.length;i++){
                    idArr.push(row[i].id);
                }
                var idArrs=idArr.join(',');

                hlAlertFive("/ContractOperation/delContract.action",idArrs,idArr.length);
                // $.messager.confirm('提示','您确定要删除<font>')
            }else{
                hlAlertOne();
            }
        }


        //取消保存
        function ContractFormCancelSubmit() {
            $('#hlContractDialog').dialog('close');


        }

        //增加或保存合同信息
        function ContractFormSubmit() {

            $('#contractForm').form('submit',{
                url:url,
                onSubmit:function () {
                    //表单验证

                    setParams($("input[name='external_coating']"));
                    setParams($("input[name='internal_coating']"));
                    setParams($("input[name='grade']"));


                    if($("input[name='contract_no']").val()==""){

                        hlAlertFour("请输入合同编号");
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
                    else if($("input[name='total_order_length']").val()==""){
                        hlAlertFour("请输入合同总长度");
                        return false;

                    }
                    else if($("input[name='total_order_weight']").val()==""){
                        hlAlertFour("请输入合同总重量");
                        return false;

                    }


                    //return $('#contractForm').form('enableValidation').form('validate');
                },
                success: function(result){
                    var result = eval('('+result+')');
                    if (result.success){
                        $('#hlContractDialog').dialog('close');
                        $('#contractDatagrids').datagrid('reload');
                        clearFormLabel();
                        //$('#hl-gallery-con').empty();
                        // $('#contractDatagrids').datagrid('clearSelections');
                    } else {
                        //$.messager.alert('提示',data.msg,'info');
                        hlAlertFour("操作失败!");
                    }
                },
                error:function () {
                    hlAlertThree();
                }
            });

        }


        function  setParams($obj) {
            if($obj.val()==null||$obj.val()=="")
                $obj.val('');
        }

        function  clearFormLabel() {
            $('#contractForm').form('clear');

        }


    </script>
</head>
<body>
<fieldset class="b3" style="padding:10px;margin:10px;">
    <legend> <h3><b style="color: orange" >|&nbsp;</b><span class="i18n1" name="datadisplay">数据展示</span></h3></legend>
    <div  style="margin-top:5px;">
        <table class="easyui-datagrid" id="contractDatagrids" url="/ContractOperation/getContractInfoByLike.action" striped="true" loadMsg="正在加载中。。。" textField="text" pageSize="20" fitColumns="true" pagination="true" toolbar="#hlcontractTb">
            <thead>
            <tr>
                <th data-options="field:'ck',checkbox:true"></th>
                <th field="id" align="center" width="100" class="i18n1" name="id">流水号</th>
                <th field="project_no" align="center" width="100" class="i18n1" name="projectno">项目编号</th>
                <th field="project_name" align="center" width="100" class="i18n1" name="projectname">项目名称</th>
                <th field="contract_no" align="center" width="100" class="i18n1" name="contractno">合同编号</th>
                <th field="od" align="center" width="100" class="i18n1" name="od">外径</th>
                <th field="wt" align="center" width="100" class="i18n1" name="wt">壁厚</th>
                <th field="external_coating" align="center" width="100" class="i18n1" name="externalcoating">外涂层</th>
                <th field="internal_coating" align="center" width="100" class="i18n1" name="internalcoating">内涂层</th>
                <th field="grade" align="center" width="100" class="i18n1" name="grade">钢种</th>
                <th field="total_order_length" align="center" width="100" class="i18n1" name="totalorderlength">合同总长度</th>
                <th field="total_order_weight" align="center" width="100" class="i18n1" name="totalorderweight">合同总重量</th>
                <th field="weight_per_meter" align="center" width="100" class="i18n1" name="weightpermeter">钢管米重</th>
                <th field="pipe_length" align="center" width="100" class="i18n1" name="pipelength">钢管长度</th>

                <th field="remark" align="center" width="100" class="i18n1" name="remark">备注</th>
            </tr>
            </thead>
        </table>

    </div>
</fieldset>

<!--工具栏-->
<div id="hlcontractTb" style="padding:10px;">
    <span class="i18n1" name="contractno">合同编号</span>:
    <input id="contractno" name="contractno" style="width:100px;line-height:22px;border:1px solid #ccc">
    <span class="i18n1" name="projectno">项目编号</span>:
    <input id="projectno" name="projectno"  style="width:100px;line-height:22px;border:1px solid #ccc">
    <span class="i18n1" name="projectname">项目名称</span>:
    <input id="projectname" name="projectname" style="width:100px;line-height:22px;border:1px solid #ccc">



    <a href="#" class="easyui-linkbutton" plain="true" data-options="iconCls:'icon-search'" onclick="searchContract()">Search</a>
    <div style="float:right">
        <a href="#" id="addContractLinkBtn" class="easyui-linkbutton i18n1" name="add" data-options="iconCls:'icon-add',plain:true" onclick="addContract()">添加</a>
        <a href="#" id="editContractLinkBtn" class="easyui-linkbutton i18n1" name="edit" data-options="iconCls:'icon-edit',plain:true" onclick="editContract()">修改</a>
        <a href="#" id="deltContractLinkBtn" class="easyui-linkbutton i18n1" name="delete" data-options="iconCls:'icon-remove',plain:true" onclick="delContract()">删除</a>
    </div>
</div>

<!--添加、修改框-->
<div id="hlContractDialog" class="easyui-dialog" data-options="title:'添加',modal:true"  closed="true" buttons="#dlg-buttons" style="display: none;padding:5px;width:950px;height:auto;">
    <form id="contractForm" method="post">
        <fieldset style="width:900px;border:solid 1px #aaa;margin-top:8px;position:relative;">
            <legend class="i18n1" name="contractinfo">合同信息</legend>
            <table class="ht-table" width="100%" border="0">
                <tr>
                    <td class="i18n1" name="id">流水号</td>
                    <td colspan="5"><input class="easyui-textbox" type="text" name="contractid" readonly="true" value=""/></td>

                </tr>
                <tr>
                    <td class="i18n1" name="projectno" width="16%">项目编号</td>
                    <%--<td width="33%"><input class="easyui-validatebox" type="text" name="project_no"   value=""/></td>--%>

                    <td colspan="1" width="33%">
                        <input id="lookup1" name="project_no" class="mini-lookup" style="text-align:center;width:180px;"
                               textField="project_no" valueField="id" popupWidth="auto"
                               popup="#gridPanel1" grid="#datagrid1" multiSelect="false"/>
                    </td>



                    <td class="i18n1" name="projectname" width="16%">项目名称</td>
                    <td width="33%"><input class="easyui-validatebox" type="text" name="project_name"  readonly="true" value=""/></td>


                </tr>

                <tr>
                    <td class="i18n1" name="contractno" width="16%">合同编号</td>
                    <td   width="33%"><input class="easyui-validatebox" type="text" name="contract_no" value=""/></td>
                    <td class="i18n1" name="grade" width="16%">钢种</td>
                    <td  width="33%"><input class="easyui-validatebox" type="text" name="grade" value=""/></td>

                </tr>

                <tr>
                    <td class="i18n1" name="od" width="16%">外径</td>
                    <td   width="33%"><input class="easyui-numberbox"  data-options="min:0,precision:2" type="text" name="od" value=""/></td>

                    <td class="i18n1" name="wt" width="16%">壁厚</td>
                    <td  width="33%"><input class="easyui-numberbox" data-options="min:0,precision:2" type="text" name="wt" value=""/></td>

                </tr>
                <tr>
                    <td class="i18n1" name="externalcoating" width="16%">外涂层</td>
                    <td   width="33%"><input class="easyui-validatebox" type="text" name="external_coating" value=""/></td>
                    <td class="i18n1" name="internalcoating" width="16%">内涂层</td>
                    <td   width="33%"><input class="easyui-validatebox" type="text" name="internal_coating" value="" /></td>
                </tr>
                <tr>
                    <td class="i18n1" name="weightpermeter" width="16%">米重</td>
                    <td   width="33%"><input class="easyui-numberbox" data-options="min:0,precision:2" type="text" name="weight_per_meter" value=""/></td>

                    <td class="i18n1" name="pipelength" width="16%">管长</td>
                    <td   width="33%"><input class="easyui-numberbox" data-options="min:0,precision:2" type="text" name="pipe_length" value=""/></td>

                </tr>
                <tr>
                    <td class="i18n1" name="totalorderlength" width="16%">合同总长度</td>
                    <td   width="33%"><input class="easyui-numberbox" data-options="min:0,precision:3" type="text" name="total_order_length" value=""/></td>

                    <td class="i18n1" name="totalorderweight" width="16%">合同总重量</td>
                    <td   width="33%"><input class="easyui-numberbox" data-options="min:0,precision:3" type="text" name="total_order_weight" value=""/></td>

                </tr>

                <tr>
                    <td class="i18n1" name="remark" width="16%">Remark</td>
                    <td   width="33%"><input class="easyui-textbox" data-options="min:0,precision:3" type="text" name="remark" value=""/></td>


                </tr>


            </table>


        </fieldset>


    </form>


</div>
<div id="dlg-buttons" align="center" style="width:900px;">
    <a href="#" class="easyui-linkbutton" iconCls="icon-save" onclick="ContractFormSubmit()">Save</a>
    <a href="#" class="easyui-linkbutton" id="hlcancelBtn" operationtype="add" iconCls="icon-cancel" onclick="ContractFormCancelSubmit()">Cancel</a>
</div>



<div id="gridPanel1" class="mini-panel" title="header" iconCls="icon-add" style="width:500px;height:280px;"
     showToolbar="true" showCloseButton="true" showHeader="false" bodyStyle="padding:0" borderStyle="border:0"
>
    <div property="toolbar" id="searchBar1" style="padding:5px;padding-left:8px;text-align:center;display: none">
        <div style="float:left;padding-bottom:2px;">
            <span class="i18n1" name="projectno">项目编号</span><span>:</span>
            <input id="keyText_project_no" class="mini-textbox" style="width:110px;" onenter="onSearchClick(1)"/>
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
         url="/ProjectOperation/getProjectInfo.action">
        <div property="columns">
            <div type="checkcolumn" width="20"></div>
            <div field="project_no" width="40" headerAlign="center" allowSort="true" class="i18n1" name="projectno">项目编号</div>
            <div field="project_name" width="80" headerAlign="center" allowSort="true" class="i18n1" name="projectname">项目名称</div>
            <div field="client_name" width="40" headerAlign="center" allowSort="true" class="i18n1" name="clientname">客户名称</div>
            <div field="project_time" width="40" headerAlign="center" allowSort="true" class="i18n1" name="projecttime" dateFormat="yyyy-MM-dd">项目时间</div>

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

    var keyText1=mini.get('keyText_project_no');
    var look1=mini.get('lookup1');

    function onSearchClick(type) {
        if(type==1)
        {
            grid1.load({
                project_no:keyText1.value,
            });
        }
        // else if(type==2){
        //     grid2.load({
        //         pname: keyText4.value,
        //         employeeno:keyText3.value
        //     });
        // }

    }
    function onCloseClick(type) {
        if(type==1)
            look1.hidePopup();
        // else if(type==2)
        //     look2.hidePopup();
    }
    function onClearClick(type) {
        if(type==1)
            look1.deselectAll();
        // else if(type==2)
        //     look2.deselectAll();
    }
    look1.on('valuechanged',function () {
        var rows = grid1.getSelected();
        $("input[name='project_no']").val(rows.project_no);
    });
    // look2.on('valuechanged',function (e) {
    //     var rows = grid2.getSelected();
    //     $("input[name='operator_no']").val(rows.employee_no);
    // });
    look1.on("showpopup",function(e){
        $('.mini-shadow').css('z-index','99999');
        $('.mini-popup').css('z-index','100000');
        $('.mini-panel').css('z-index','100000');
        $('#searchBar1').css('display','block');
        //$('.mini-buttonedit .mini-buttonedit-input').css('width','150px');
    });

    hlLanguage("../i18n/");
</script>