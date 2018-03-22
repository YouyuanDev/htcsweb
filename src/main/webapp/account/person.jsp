<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>人员账户管理</title>
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

    <style type="text/css" >
        .b3{border-style:inset;border-width:thin;}
    </style>


    <script type="text/javascript">
        var url;

        function formatterdate(value,row,index){
            return getDate1(value);
        }

        // 日期格式为 2017-02-20 22:00:00
        function myformatter2(date){
            return getDate1(date);
        }
        // 日期格式为 2017-02-20 22:00:00
        function myparser2(s) {
            if (!s) return new Date();
            return new Date(Date.parse(s));
        }



        $(function () {

            $('#hlPersonDialog').dialog({
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
        function addPerson(){
            $('#hlcancelBtn').attr('operationtype','add');
            $('#hlPersonDialog').dialog('open').dialog('setTitle','新增');
            $('#personForm').form('clear');
            $('#pid').text('');
            $('#pregister_time').text('');
            url="/person/savePerson.action";
            look2.deselectAll();
        }
        function delPerson() {
            var row = $('#personDatagrids').datagrid('getSelections');
            if(row.length>0){
                var idArr=[];
                for (var i=0;i<row.length;i++){
                    idArr.push(row[i].id);
                }
                var idArrs=idArr.join(',');
                $.messager.confirm('系统提示',"您确定要删除这<font color=red>"+idArr.length+ "</font>条数据吗？",function (r) {
                    if(r){
                        $.post(
                            "/person/delPerson.action",
                            {"hlparam":idArrs},function (data) {
                                if(data.success){
                                    $("#personDatagrids").datagrid("reload");
                                }
                                hlAlertFour(data.message);
                            },"json");
                    }
                });
                //hlAlertFive("/person/delPerson.action",idArrs,idArr.length);
                // $.messager.confirm('提示','您确定要删除<font>')
            }else{
                hlAlertOne();
            }
        }
        function editPerson() {
            $('#hlcancelBtn').attr('operationtype','edit');

            var row = $('#personDatagrids').datagrid('getSelected');
            if(row){
                $('#hlPersonDialog').dialog('open').dialog('setTitle','修改');

                $('#personForm').form('load',{
                    'employee_no':row.employee_no,
                    'pname':row.pname,
                    'ppassword':row.ppassword,
                    'pidcard_no':row.pidcard_no,
                    'pmobile':row.pmobile,
                    'page':row.page,
                    'psex':row.psex,
                    'pdepartment':row.pdepartment,
                    'pstatus':row.pstatus,
                    'pid':row.id
                });
                look2.setText(row.role_no_list);
                look2.setValue(row.role_no_list);
                $("#pregister_time").datetimebox('setValue',getDate1(row.pregister_time));

                url="/person/savePerson.action?id="+row.id;

            }else{
                hlAlertTwo();
            }
        }



        function searchPerson() {
            $('#personDatagrids').datagrid('load',{
                'employee_no': $('#employeeno').val(),
                'pname': $('#pname').val()
            });
        }



        function personFormSubmit() {
            $('#personForm').form('submit',{
                url:url,
                onSubmit:function () {
                    //表单验证
                    //碱洗时间

                    if($("input[name='page']").val()==""){

                        hlAlertFour("请输入年龄");
                        return false;
                    }
                    else if($("input[name='pregister_time']").val()==""){

                        hlAlertFour("请输入注册时间");
                        return false;
                    }



                },
                success: function(result){
                    //alert(result);
                    var result = eval('('+result+')');
                    if (result.success){
                        $('#hlPersonDialog').dialog('close');
                        $('#personDatagrids').datagrid('reload');
                        clearFormLabel();
                    }
                    hlAlertFour(result.message);
                },
                error:function () {
                    hlAlertThree();
                }
            });
        }
        function personCancelSubmit() {
            $('#hlPersonDialog').dialog('close');
        }

        function  clearFormLabel() {
            $('#personForm').form('clear');

        }

    </script>





</head>

<body>

<fieldset class="b3" style="padding:10px;margin:10px;">
    <legend> <h3><b style="color: orange" >|&nbsp;</b><span class="i18n1" name="datadisplay">数据展示</span></h3></legend>
    <div  style="margin-top:5px;">
        <table class="easyui-datagrid" id="personDatagrids" url="/person/getPersonByLike.action" striped="true" loadMsg="正在加载中。。。" textField="text" pageSize="20" fitColumns="true" pagination="true" toolbar="#hlpersonTb">
            <thead>
            <tr>
                <th data-options="field:'ck',checkbox:true"></th>
                <th field="id" align="center" width="100" class="i18n1" name="id">流水号</th>
                <th field="employee_no" align="center" width="100" class="i18n1" name="employeeno">员工编号</th>
                <th field="pname" align="center" width="100" class="i18n1" name="pname">姓名</th>
                <th field="ppassword" align="center" width="100" class="i18n1" hidden="true" name="ppassword">密码</th>
                <th field="pidcard_no" align="center" width="100" class="i18n1" name="pidcardno">身份证号</th>
                <th field="pmobile" align="center" width="100" class="i18n1" name="pmobile">手机号</th>
                <th field="page" align="center" width="100" class="i18n1" name="page">年龄</th>
                <th field="psex" align="center" width="100" class="i18n1" name="psex">性别</th>
                <th field="pdepartment" align="center" width="100" class="i18n1" name="pdepartment">部门</th>
                <th field="pregister_time" align="center" width="100" class="i18n1" name="pregistertime" data-options="formatter:formatterdate">注册时间</th>
                <th field="pstatus" align="center" width="100" class="i18n1" name="pstatus">状态</th>
                <th field="role_no_list" align="center" width="100" class="i18n1" name="rolenolist">角色列表</th>
            </tr>
            </thead>
        </table>

    </div>
</fieldset>

<!--工具栏-->
<div id="hlpersonTb" style="padding:10px;">
    <span class="i18n1" name="employeeno">员工编号</span>:
    <input id="employeeno" name="employeeno" style="line-height:22px;border:1px solid #ccc">
    <span class="i18n1" name="pname">姓名</span>:
    <input id="pname" name="pname" style="line-height:22px;border:1px solid #ccc">

    <a href="#" class="easyui-linkbutton" plain="true" data-options="iconCls:'icon-search'" onclick="searchPerson()">Search</a>
    <div style="float:right">
        <a href="#" id="addPersonLinkBtn" class="easyui-linkbutton i18n1" name="add" data-options="iconCls:'icon-add',plain:true" onclick="addPerson()">添加</a>
        <a href="#" id="editPersonLinkBtn" class="easyui-linkbutton i18n1" name="edit" data-options="iconCls:'icon-edit',plain:true" onclick="editPerson()">修改</a>
        <a href="#" id="deltPersonLinkBtn" class="easyui-linkbutton i18n1" name="delete" data-options="iconCls:'icon-remove',plain:true" onclick="delPerson()">删除</a>
    </div>
</div>


<!--添加、修改框-->
<div id="hlPersonDialog" class="easyui-dialog" data-options="title:'添加',modal:true"  closed="true" buttons="#dlg-buttons" style="display: none;padding:5px;width:950px;height:auto;">
    <form id="personForm" method="post">


        <fieldset style="width:900px;border:solid 1px #aaa;margin-top:8px;position:relative;">
            <legend>人员信息</legend>

            <table class="ht-table">
                <tr>
                    <td class="i18n1" name="id">流水号</td>
                    <td colspan="5"><input class="easyui-textbox" type="text" name="pid" readonly="true" value="0"/></td>

                </tr>
                <tr>
                    <td class="i18n1" name="employeeno">员工编号</td>
                    <td colspan="2" >
                        <input class="easyui-textbox" type="text" value="" name="employee_no" />
                    </td>
                    <td class="i18n1" name="ppassword">密码</td>
                    <td colspan="2">
                        <input class="easyui-textbox" id="ppassword" name="ppassword" type="password" style="width:150px;height:22px;padding:12px" data-options="prompt:'登录密码',iconCls:'icon-lock',iconWidth:38">
                    </td>
                </tr>
            </table>

            <table class="ht-table">
                <tr>
                    <td width="16%"  class="i18n1" name="pname">姓名</td>
                    <td><input class="easyui-textbox" type="text" name="pname" value=""/></td>
                    <td></td>
                    <td width="16%"  class="i18n1" name="pdepartment">部门</td>
                    <td><input class="easyui-textbox" type="text" name="pdepartment" value=""/></td>
                    <td></td>

                </tr>

                <tr>
                    <td class="i18n1" name="pidcardno">身份证号</td>
                    <td><input class="easyui-textbox" type="text" name="pidcard_no" value=""/></td>
                    <td></td>
                    <td class="i18n1" name="pmobile">手机号</td>
                    <td><input class="easyui-textbox"  type="text" name="pmobile" value=""/></td>
                    <td></td>

                </tr>
                <tr>
                    <td class="i18n1" name="page">年龄</td>
                    <td><input class="easyui-numberbox" data-options="min:0,precision:0" type="text" name="page" value=""/></td>
                    <td></td>
                    <td class="i18n1" name="psex">性别</td>
                    <td><select class="easyui-combobox" name="psex" data-options="editable:false" style="width:200px;">
                        <option value="M">男</option>
                        <option value="F">女</option>
                    </select></td>
                    <td></td>

                </tr>
                <tr>
                    <td width="16%" class="i18n1" name="pregistertime">注册时间</td>
                    <td><input class="easyui-datetimebox" type="text" id="pregister_time" name="pregister_time" value="" data-options="formatter:myformatter2,parser:myparser2"/>
                    </td>
                    <td></td>
                    <td width="16%" class="i18n1" name="pstatus">状态</td>
                    <td><select id="cc" class="easyui-combobox" name="pstatus" data-options="editable:false" style="width:200px;">
                        <option value="0">在用</option>
                        <option value="1">停用</option>
                    </select></td>
                    <td></td>

                </tr>
                <tr>
                    <td>角色列表</td>
                    <td colspan="5">
                        <input id="lookup2" name="role_no_list" class="mini-lookup hl-mini-input" style="width:400px;"
                               textField="role_no" valueField="role_no" popupWidth="auto"
                               popup="#gridPanel" grid="#datagrid1" multiSelect="true"
                        />

                        <div id="gridPanel" class="mini-panel" title="header" iconCls="icon-add" style="width:450px;height:250px;"
                             showToolbar="true" showCloseButton="true" showHeader="false" bodyStyle="padding:0" borderStyle="border:0"
                        >
                            <div property="toolbar" style="padding:5px;padding-left:8px;text-align:center;">
                                <div style="float:left;padding-bottom:2px;">
                                    <span>角色编号或名称：</span>
                                    <input id="keyText" class="mini-textbox" style="width:160px;" onenter="onSearchClick"/>
                                    <a class="mini-button" onclick="onSearchClick">查询</a>
                                    <a class="mini-button" onclick="onClearClick">清除</a>
                                </div>
                                <div style="float:right;padding-bottom:2px;">
                                    <a class="mini-button" onclick="onCloseClick">关闭</a>
                                </div>
                                <div style="clear:both;"></div>
                            </div>
                            <div id="datagrid1" class="mini-datagrid" style="width:100%;height:100%;"
                                 borderStyle="border:0" showPageSize="false" showPageIndex="false"
                                 url="/Role/getAllRoleByLike.action"
                            >
                                <div property="columns">
                                    <div type="checkcolumn" ></div>
                                    <div field="id" width="120" headerAlign="center" allowSort="true" class="i18n1" name="id">流水号</div>
                                    <div field="role_no" width="120" headerAlign="center" allowSort="true" class="i18n1" name="roleno">角色编号</div>
                                    <div field="role_name" width="120" headerAlign="center" allowSort="true" class="i18n1" name="rolename">角色名称</div>
                                </div>
                            </div>
                        </div>

                    </td>

                </tr>
            </table>

        </fieldset>
    </form>


</div>
<div id="dlg-buttons" align="center" style="width:900px;">
    <a href="#" class="easyui-linkbutton" iconCls="icon-save" onclick="personFormSubmit()">Save</a>
    <a href="#" class="easyui-linkbutton" id="hlcancelBtn" operationtype="add" iconCls="icon-cancel" onclick="personCancelSubmit()">Cancel</a>
</div>


<script type="text/javascript" src="../easyui/jquery.easyui.min.js"></script>
</body>
</html>
<script type="text/javascript">
    mini.parse();
    var grid = mini.get("datagrid1");
    var keyText = mini.get("keyText");
    var look2= mini.get("lookup2");
    //grid.load();
    // look2.on('valuechanged',function () {
    //    var rows = grid.getSelected();
    //    $("input[name='function_no_list']").val(rows.function_no);
    // });

    function onSearchClick(e) {
        grid.load({
            role_no: keyText.value,
            role_name:keyText.value
        });
    }
    function onCloseClick(e) {
        look2.hidePopup();
    }
    function onClearClick(e) {
        look2.deselectAll();
    }
    look2.on("showpopup",function(e){
        $('.mini-shadow').css('z-index','99999');
        $('.mini-popup').css('z-index','100000');
        $('.mini-panel').css('z-index','100000');
        $('#searchBar2').css('display','block');


        grid.load({
            function_no: keyText.value,
            function_name:keyText.value
        });
    });
    $(function () {
        $('.hl-mini-input .mini-buttonedit-border .mini-buttonedit-input').css("width","360px");
    });
    hlLanguage("../i18n/");
</script>
