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

    <style type="text/css" >
        .b3{border-style:inset;border-width:thin;}
    </style>


    <script type="text/javascript">
        var url;

        function formatterdate(value,row,index){
            var date = new Date(value);
            //var y = date.getFullYear();
            //var m = date.getMonth()+1;
            //var d = date.getDate();

            //return y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d);
            //alert("ss"+date.toLocaleString())
            return date.toLocaleString();
        }

        // 日期格式为 2/20/2017 12:00:00 PM
        function myformatter2(date){
            return date.toLocaleString();
        }
        // 日期格式为 2/20/2017 12:00:00 PM
        function myparser2(s) {
            if (!s) return new Date();
            return new Date(Date.parse(s));
        }



        $(function () {


            // $('#hlPersonDialog').dialog({
            //     onClose:function () {
            //         var type=$('#hlcancelBtn').attr('operationtype');
            //         if(type=="add"){
            //             var $imglist=$('#fileslist');
            //             var $dialog=$('#hlPersonDialog');
            //             hlAlertSix("../UploadFile/delUploadPicture.action",$imglist,$dialog,grid);
            //         }else{
            //             //$('#hlOdBlastProDialog').dialog('close');
            //             $('#hl-gallery-con').empty();
            //             //$('#fileslist').val('');
            //             clearFormLabel();
            //         }
            //     }
            // });
            // $('.mini-buttonedit .mini-buttonedit-input').css('width','150px');
            // hlLanguage("../i18n/");
        });



        function searchPerson() {
            $('#personDatagrids').datagrid('load',{
                'employee_no': $('#employeeno').val(),
                'pname': $('#pname').val()
            });
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
            <legend>钢管信息</legend>
            <table class="ht-table" width="100%" border="0">
                <tr>
                    <td class="i18n1" name="projectname" width="16%">项目名称</td>
                    <td colspan="2" width="33%"><label id="project_name"></label></td>

                    <td class="i18n1" name="contractno" width="16%">合同编号</td>
                    <td colspan="7" width="33%"><label id="contract_no"></label></td>

                </tr>

                <tr>
                    <td class="i18n1" name="pipeno" width="16%">钢管编号</td>
                    <td colspan="2" width="33%">
                        <input id="lookup1" name="pipe_no" class="mini-lookup" style="text-align:center;width:180px;"
                               textField="pipe_no" valueField="id" popupWidth="auto"
                               popup="#gridPanel1" grid="#datagrid1" multiSelect="false"/>
                    </td>
                    <td class="i18n1" name="statusname" width="16%">状态</td>
                    <td colspan="7" width="33%"><label id="status_name"></label></td>
                </tr>
            </table>

            <table width="100%" border="0" align="center">
                <tr>
                    <td align="center" class="i18n1" name="grade">钢种</td>
                    <td align="center"><label id="grade"></label></td>
                    <td align="center" class="i18n1" name="od">外径</td>
                    <td align="center"><label id="od"></label></td>
                    <td align="center" class="i18n1" name="wt">壁厚</td>
                    <td align="center"><label id="wt"></label></td>
                    <td align="center" class="i18n1" name="p_length">长度</td>
                    <td align="center"><label id="p_length"></label></td>
                    <td align="center" class="i18n1" name="weight">重量</td>
                    <td align="center"><label id="weight"></label></td>
                    <td align="center" class="i18n1" name="heatno">炉号</td>
                    <td align="center"><label id="heat_no"></label></td>
                </tr>
            </table>
        </fieldset>


        <fieldset style="width:900px;border:solid 1px #aaa;margin-top:8px;position:relative;">
            <legend>外喷砂生产信息</legend>

            <table class="ht-table">
                <tr>
                    <td class="i18n1" name="id">流水号</td>
                    <td colspan="5"><label id="odbpid"></label></td>

                </tr>
                <tr>
                    <td class="i18n1" name="operatorno">操作工编号</td>
                    <td colspan="2" >
                        <input id="lookup2" name="operator_no" class="mini-lookup" style="text-align:center;width:180px;"
                               textField="employee_no" valueField="id" popupWidth="auto"
                               popup="#gridPanel2" grid="#datagrid2" multiSelect="false"
                        />
                    </td>
                    <td class="i18n1" name="operationtime">操作时间</td>
                    <td colspan="2">
                        <input class="easyui-datebox" type="text" name="odbptime" value="" data-options="formatter:myformatter2,parser:myparser2"/>

                    </td>

                </tr>
            </table>

            <table class="ht-table">
                <tr>
                    <td class="i18n1" name="alkalinedwelltime">碱洗时间</td>
                    <td><input class="easyui-numberbox" data-options="min:0,precision:0" type="text" name="alkaline_dwell_time" value=""/></td>
                    <td>10~20</td>
                    <td class="i18n1" name="alkalineconcentration">碱浓度</td>
                    <td><input class="easyui-numberbox"  data-options="min:0,precision:2" type="text" name="alkaline_concentration" value=""/></td>
                    <td></td>
                </tr>

                <tr>
                    <td class="i18n1" name="acidwashtime">酸洗时间</td>
                    <td><input class="easyui-numberbox" data-options="min:0,precision:0" type="text" name="acid_wash_time" value=""/></td>
                    <td></td>
                    <td class="i18n1" name="acidconcentration">酸浓度</td>
                    <td><input class="easyui-numberbox" data-options="min:0,precision:2" type="text" name="acid_concentration" value=""/></td>
                    <td></td>
                </tr>
                <tr>
                    <td width="16%"  class="i18n1" name="surfacecondition">外观缺陷</td>
                    <td><input class="easyui-validatebox" type="text" name="surface_condition" value=""/></td>
                    <td></td>
                    <td width="16%" class="i18n1" name="saltcontaminationbeforeblasting">打砂前盐度</td>
                    <td><input class="easyui-numberbox" data-options="min:0,precision:2" type="text" name="salt_contamination_before_blasting" value=""/></td>
                    <td><=25</td>
                </tr>

                <tr>
                    <td width="16%" class="i18n1" name="blastlinespeed">打砂传送速度</td>
                    <td><input class="easyui-numberbox" data-options="min:0,precision:2" type="text" name="blast_line_speed" value=""/></td>
                    <td></td>
                    <td width="16%" class="i18n1" name="conductivity">传导性</td>
                    <td><input class="easyui-numberbox" data-options="min:0,precision:2" type="text" name="conductivity" value=""/></td>
                    <td></td>
                </tr>
                <tr>
                    <td width="16%" class="i18n1" name="preheattemp">预热温度</td>
                    <td><input class="easyui-numberbox" data-options="min:0,precision:1" type="text" name="preheat_temp" value=""/></td>
                    <td></td>
                    <td width="16%" class="i18n1" name="remark">备注</td>
                    <td><input class="easyui-textbox" type="text" value="" name="remark" data-options="multiline:true" style="height:60px"/></td>
                    <td></td>
                </tr>
                <tr>
                    <td width="16%" class="i18n1" name="result">结论</td>
                    <td><select id="cc" class="easyui-combobox" name="result" style="width:200px;">
                        <option value="0">不合格</option>
                        <option value="1">合格</option>
                        <option value="2">待定</option>
                    </select></td>
                    <td></td>
                    <td ></td>
                    <td></td>
                    <td></td>
                </tr>





            </table>
            <input type="hidden" id="fileslist" name="upload_files" value=""/>
            <div id="hl-gallery-con" style="width:100%;">

            </div>
            <div id="multiupload1" class="uc-multiupload" style="width:100%; max-height:200px"
                 flashurl="../miniui/fileupload/swfupload/swfupload.swf"
                 uploadurl="../UploadFile/uploadPicture.action" _autoUpload="false" _limittype="*.jpg;*.png;*.jpeg;*.bmp"
                 onuploaderror="onUploadError" onuploadsuccess="onUploadSuccess">
            </div>
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

    hlLanguage("../i18n/");
</script>
