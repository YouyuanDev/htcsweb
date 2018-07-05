<%--
  Created by IntelliJ IDEA.
  User: kurt
  Date: 6/15/18
  Time: 2:27 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>涂层原材料</title>
    <link rel="stylesheet" type="text/css" href="../easyui/themes/bootstrap/easyui.css">
    <link rel="stylesheet" type="text/css" href="../easyui/themes/icon.css">
    <link href="../miniui/multiupload/multiupload.css" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" href="../css/common.css"/>
    <script src="../easyui/jquery.min.js" type="text/javascript"></script>
    <%--<script src="../js/jquery.i18n.properties-1.0.9.js" type="text/javascript"></script>--%>
    <%--<script src="../js/language.js" type="text/javascript"></script>--%>
    <script src="../js/common.js" type="text/javascript"></script>
    <script src="../miniui/boot.js" type="text/javascript"></script>
    <script  src="../miniui/js/miniui.js" type="text/javascript"></script>
    <script src="../miniui/fileupload/swfupload/swfupload.js" type="text/javascript"></script>
    <script src="../miniui/multiupload/multiupload.js" type="text/javascript"></script>
    <script  src="../js/lrscroll.js" type="text/javascript"></script>
    <script src="../js/jquery.i18n.properties-1.0.9.js" type="text/javascript"></script>
    <script src="../js/language.js" type="text/javascript"></script>

    <style type="text/css" >
        .b3{border-style:inset;border-width:thin;}
    </style>


    <script type="text/javascript">
        var url;

        $(function () {

            $('#hlCoatingPowderDialog').dialog({
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



        function addCoatingPowder(){
            $('#hlcancelBtn').attr('operationtype','add');
            $('#hlCoatingPowderDialog').dialog('open').dialog('setTitle','新增');
            $('#CoatingPowderForm').form('clear');
            $("input[name='id']").val('0');
            url="/CoatingPowderOperation/saveCoatingPowder.action";
        }
        function delCoatingPowder() {
            var row = $('#CoatingPowderDatagrids').datagrid('getSelections');
            if(row.length>0){
                var idArr=[];
                for (var i=0;i<row.length;i++){
                    idArr.push(row[i].id);
                }
                var idArrs=idArr.join(',');
                $.messager.confirm('系统提示',"您确定要删除这<font color=red>"+idArr.length+ "</font>条数据吗？",function (r) {
                    if(r){
                        $.post(
                            "/CoatingPowderOperation/delCoatingPowder.action",
                            {hlparam:idArrs},function (data) {
                                if(data.success){
                                    $("#CoatingPowderDatagrids").datagrid("reload");
                                }
                                hlAlertFour(data.message);
                            },"json");
                    }
                });
                //hlAlertFive("/coatingpowder/delCoatingPowder.action",idArrs,idArr.length);
                // $.messager.confirm('提示','您确定要删除<font>')
            }else{
                hlAlertOne();
            }
        }
        function editCoatingPowder() {
            $('#hlcancelBtn').attr('operationtype','edit');

            var row = $('#CoatingPowderDatagrids').datagrid('getSelected');
            if(row){
                $('#hlCoatingPowderDialog').dialog('open').dialog('setTitle','修改');


                $('#CoatingPowderForm').form('load',row);

                url="/CoatingPowderOperation/saveCoatingPowder.action?id="+row.id;

            }else{
                hlAlertTwo();
            }
        }



        function searchCoatingPowder() {
            $('#CoatingPowderDatagrids').datagrid('load',{

                'coating_powder_name': $('#coatingpowdername').val(),
                'powder_type': $('#powdertype').val()
            });
        }



        function CoatingPowderFormSubmit() {
            $('#CoatingPowderForm').form('submit',{
                url:url,
                onSubmit:function () {
                    //表单验证
                    //碱洗时间

                    if($("input[name='coating_powder_name']").val()==""){

                        hlAlertFour("请输入原材料名称");
                        return false;
                    }


                    if($("#powder_type").val()==undefined||$("#powder_type").val()==""){

                        hlAlertFour("请选择原材料类型");
                        return false;
                    }

                },
                success: function(result){
                    //alert(result);
                    var result = eval('('+result+')');
                    if (result.success){
                        $('#hlCoatingPowderDialog').dialog('close');
                        $('#CoatingPowderDatagrids').datagrid('reload');
                        clearFormLabel();
                    }
                    hlAlertFour(result.message);
                },
                error:function () {
                    hlAlertThree();
                }
            });
        }
        function CoatingPowderCancelSubmit() {
            $('#hlCoatingPowderDialog').dialog('close');
        }

        function  clearFormLabel() {
            $('#CoatingPowderForm').form('clear');

        }

    </script>





</head>

<body>

<fieldset class="b3" style="padding:10px;margin:10px;">
    <legend> <h3><b style="color: orange" >|&nbsp;</b><span class="i18n1" name="datadisplay">数据展示</span></h3></legend>
    <div  style="margin-top:5px;">
        <table class="easyui-datagrid" id="CoatingPowderDatagrids" url="/CoatingPowderOperation/getCoatingPowderInfoByLike.action" striped="true" loadMsg="正在加载中。。。" textField="text" pageSize="20" fitColumns="true" pagination="true" toolbar="#hlCoatingPowderTb">
            <thead>
            <tr>
                <th data-options="field:'ck',checkbox:true"></th>
                <th field="id" align="center" width="100" class="i18n1" name="id">流水号</th>

                <th field="coating_powder_name" align="center" width="100" class="i18n1" name="coatingpowdername">原材料型号</th>
                <th field="powder_type" align="center" width="100" class="i18n1" name="powdertype">原材料类型</th>

            </tr>
            </thead>
        </table>

    </div>
</fieldset>

<!--工具栏-->
<div id="hlCoatingPowderTb" style="padding:10px;">
    <span class="i18n1" name="coatingpowdername">原材料型号</span>:
    <input id="coatingpowdername" style="line-height:22px;border:1px solid #ccc">
    <span class="i18n1" name="powdertype">原材料类型</span>:
    <input id="powdertype" class="easyui-combobox" type="text" name="powdertype"  data-options=
            "url:'/CoatingPowderOperation/getAllCoatingPowderType.action',
					        method:'post',
					        valueField:'powder_type',
					        width: 140,
					        editable:false,
					        textField:'powder_type',
					        panelHeight:'200'"/>


    <a href="#" class="easyui-linkbutton" plain="true" data-options="iconCls:'icon-search'" onclick="searchCoatingPowder()">Search</a>
    <div style="float:right">
        <a href="#" id="addCoatingPowderLinkBtn" class="easyui-linkbutton i18n1" name="add" data-options="iconCls:'icon-add',plain:true" onclick="addCoatingPowder()">添加</a>
        <a href="#" id="editCoatingPowderLinkBtn" class="easyui-linkbutton i18n1" name="edit" data-options="iconCls:'icon-edit',plain:true" onclick="editCoatingPowder()">修改</a>
        <a href="#" id="deltCoatingPowderLinkBtn" class="easyui-linkbutton i18n1" name="delete" data-options="iconCls:'icon-remove',plain:true" onclick="delCoatingPowder()">删除</a>
    </div>
</div>


<!--添加、修改框-->
<div id="hlCoatingPowderDialog" class="easyui-dialog" data-options="title:'添加',modal:true"  closed="true" buttons="#dlg-buttons" style="display: none;padding:5px;width:950px;height:auto;">
    <form id="CoatingPowderForm" method="post">


        <fieldset style="width:900px;border:solid 1px #aaa;margin-top:8px;position:relative;">
            <legend>原材料信息</legend>

            <table class="ht-table">
                <tr>
                    <td class="i18n1" name="id">流水号</td>
                    <td colspan="1"><input class="easyui-textbox" type="text" name="id" readonly="true" value="0"/></td>
                    <td></td>
                    <td class="i18n1" name="coatingpowdername">原材料型号</td>
                    <td colspan="1">
                        <input class="easyui-textbox" name="coating_powder_name" type="text" style="width:150px;height:22px;padding:12px" >
                    </td>
                    <td></td>
                </tr>
                <tr>
                    <td class="i18n1" name="powdertype">原材料类型</td>
                    <td colspan="1">
                        <select id="powder_type" class="easyui-combobox" data-options="editable:false" name="powder_type"   style="width:185px;">
                            <option value="FBE">FBE</option>
                            <option value="PE">PE</option>
                            <option value="AD">AD</option>
                            <option value="PP">PP</option>
                            <option value="EPOXY">EPOXY</option>
                            <option value="PARTICLE">PARTICLE</option>
                            <option value="CURING">CURING</option>
                        </select>


                    </td>
                    <td></td>

                </tr>
            </table>



        </fieldset>
    </form>


</div>
<div id="dlg-buttons" align="center" style="width:900px;">
    <a href="#" class="easyui-linkbutton" iconCls="icon-save" onclick="CoatingPowderFormSubmit()">Save</a>
    <a href="#" class="easyui-linkbutton" id="hlcancelBtn" operationtype="add" iconCls="icon-cancel" onclick="CoatingPowderCancelSubmit()">Cancel</a>
</div>


<script type="text/javascript" src="../easyui/jquery.easyui.min.js"></script>
</body>
</html>
<script type="text/javascript">
    mini.parse();

    hlLanguage("../i18n/");
</script>
