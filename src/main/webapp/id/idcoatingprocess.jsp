<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
    String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>内涂岗位</title>
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
        var basePath ="<%=basePath%>"+"/upload/pictures/";
        $(function () {
            //删除上传的图片
            $(document).on('click','.content-del',function () {
                delUploadPicture($(this));
            });
            $('#hlIdCoatingProDialog').dialog({
                onClose:function () {
                    var type=$('#hlcancelBtn').attr('operationtype');

                    if(type=="add"){
                        var $imglist=$('#fileslist');
                        var $dialog=$('#hlIdCoatingProDialog');
                        hlAlertSix("../UploadFile/delUploadPicture.action",$imglist,$dialog,grid);
                    }
                    clearFormLabel();
                }
            });
            $('.mini-buttonedit .mini-buttonedit-input').css('width','150px');
            // hlLanguage("../i18n/");
        });
        function addIdCoatingPro(){
            $('#hlcancelBtn').attr('operationtype','add');
            $('#hlIdCoatingProDialog').dialog('open').dialog('setTitle','新增');
            $('#fileslist').val('');
            $('#idCoatingProForm').form('clear');
            $('#idcoatproid').text('');
            clearMultiUpload(grid);
            url="/IdCoatOperation/saveIdCoatingProcess.action";
        }
        function delIdCoatingPro() {
            var row = $('#idCoatingProDatagrids').datagrid('getSelections');
            if(row.length>0){
                var idArr=[];
                for (var i=0;i<row.length;i++){
                    idArr.push(row[i].id);
                }
                var idArrs=idArr.join(',');
                $.messager.confirm('系统提示',"您确定要删除这<font color=red>"+idArr.length+ "</font>条数据吗？",function (r) {
                    if(r){
                        $.post("/IdCoatOperation/delIdCoatingProcess.action",{"hlparam":idArrs},function (data) {
                            if(data.success){
                                $("#idCoatingProDatagrids").datagrid("reload");
                            }
                            hlAlertFour(data.message);
                        },"json");
                    }
                });
            }else{
                hlAlertOne();
            }
        }
        function editIdCoatingPro() {
            $('#hlcancelBtn').attr('operationtype','edit');
            var row = $('#idCoatingProDatagrids').datagrid('getSelected');
            if(row){
                $('#hlIdCoatingProDialog').dialog('open').dialog('setTitle','修改');
                // $('#project_name').text(row.project_name);$('#contract_no').text(row.contract_no);
                // $('#pipe_no').text(row.pipe_no);$('#status_name').text(row.status_name);
                // $('#od').text(row.od);$('#wt').text(row.wt);
                // $('#p_length').text(row.p_length);$('#weight').text(row.weight);
                // $('#grade').text(row.grade);$('#heat_no').text(row.heat_no);
                loadPipeBaiscInfo(row);
                //$('#idCoatingProForm').form('load',row);
                $('#idCoatingProForm').form('load', {
                    'mill_no': row.mill_no,
                    'coating_speed': row.coating_speed,
                    'base_used': row.base_used,
                    'base_batch': row.base_batch,
                    'curing_agent_used': row.curing_agent_used,
                    'curing_agent_batch': row.curing_agent_batch,
                    'curing_temp': row.curing_temp,
                    'operation_time':getDate1(row.operation_time),
                    'curing_start_time':getDate1(row.curing_start_time),
                    'curing_finish_time':getDate1(row.curing_finish_time),
                    'upload_files':row.upload_files,
                    'result':row.result,
                    'remark':row.remark
                    // 'curing_start_time': getDate1(row.curing_start_time),
                    // 'curing_finish_time':getDate1(row.curing_finish_time)
                });
                //$('#idcoatprotime').datetimebox('setValue',getDate1(row.operation_time));
                // $('#operation_time').datetimebox('setValue',getDate1(row.operation_time));
                // $('#curing_start_time').datetimebox('setValue',getDate1(row.curing_start_time));
                // $('#curing_finish_time').datetimebox('setValue',getDate1(row.curing_finish_time));
                 $("#idcoatproid").textbox("setValue", row.id);
                look1.setText(row.pipe_no);
                look1.setValue(row.pipe_no);
                look2.setText(row.operator_no);
                look2.setValue(row.operator_no);
                var odpictures=row.upload_files;
                if(odpictures!=null&&odpictures!=""){
                    var imgList=odpictures.split(';');
                    createPictureModel(basePath,imgList);
                }
                url="/IdCoatOperation/saveIdCoatingProcess.action?id="+row.id;

            }else{
                hlAlertTwo();
            }
        }
        function searchIdCoatingPro() {
            $('#idCoatingProDatagrids').datagrid('load',{
                'pipe_no': $('#pipeno').val(),
                'operator_no': $('#operatorno').val(),
                'begin_time': $('#begintime').val(),
                'end_time': $('#endtime').val(),
                'mill_no': $('#millno').val()
            });
        }
        function idCoatingProFormSubmit() {
            $('#idCoatingProForm').form('submit',{
                url:url,
                onSubmit:function () {
                    //表单验证
                    setParams($("input[name='coating_speed']"));
                    setParams($("input[name='curing_temp']"));
                    if($("input[name='pipe_no']").val()==""){

                        hlAlertFour("请选择钢管管号");
                        return false;
                    }
                    if($("input[name='operator_no']").val()==""){

                        hlAlertFour("请选择操作工工号");
                        return false;
                    }
                    if($("input[name='mill_no']").val()==""){

                        hlAlertFour("请输入分厂信息");
                        return false;
                    }
                    if($("input[name='operation_time']").val()==""){
                        hlAlertFour("请输入操作时间");
                        return false;
                    }
                    if($("input[name='curing_start_time']").val()==""){
                        hlAlertFour("请输入固化开始时间");
                        return false;
                    }
                    if($("input[name='curing_finish_time']").val()==""){
                        hlAlertFour("请输入固化结束时间");
                        return false;
                    }
                    if($("input[name='result']").val()==""){
                        hlAlertFour("请输入结论!");
                        return false;
                    }
                },
                success: function(result){
                    var result = eval('('+result+')');
                    if (result.success){
                        $('#hlIdCoatingProDialog').dialog('close');
                        $('#idCoatingProDatagrids').datagrid('reload');
                        clearFormLabel();
                    }
                    hlAlertFour(result.message);
                },
                error:function () {
                    hlAlertThree();
                }
            });
            clearMultiUpload(grid);
        }
        function idCoatingProCancelSubmit() {
            $('#hlIdCoatingProDialog').dialog('close');
        }

        //图片上传失败操作
        function onUploadError() {
            alert("上传失败!");
        }
        //图片上传成功操作
        function onUploadSuccess(e) {
            var data=eval("("+e.serverData+")");
            var imgListstr=editFilesList(0,data.imgUrl);
            var imgList=imgListstr.split(';');
            createPictureModel(basePath,imgList);
        }

        //清理form表单
        function  clearFormLabel() {
            $('#idCoatingProForm').form('clear');
            $('.hl-label').text(''); $('#hl-gallery-con').empty();
        }
    </script>

</head>

<body>
<fieldset class="b3" style="padding:10px;margin:10px;">
    <legend> <h3><b style="color: orange" >|&nbsp;</b><span class="i18n1" name="datadisplay">数据展示</span></h3></legend>
    <div  style="margin-top:5px;">
        <table class="easyui-datagrid" id="idCoatingProDatagrids" url="/IdCoatOperation/getIdCoatingByLike.action" striped="true" loadMsg="正在加载中。。。" textField="text" pageSize="20" fitColumns="true" pagination="true" toolbar="#hlIdBlastProTb">
            <thead>
            <tr>
                <th data-options="field:'ck',checkbox:true"></th>
                <th field="id" align="center" width="100" class="i18n1" name="id">流水号</th>
                <th field="mill_no" align="center" width="150" class="i18n1" name="millno">分厂</th>
                <th field="project_name" align="center" width="120" class="i18n1" name="projectname">项目名称</th>
                <th field="contract_no" align="center" width="120" class="i18n1" name="contractno">合同编号</th>
                <th field="pipe_no" align="center" width="120" class="i18n1" name="pipeno">钢管编号</th>
                <th field="grade" align="center" width="110" class="i18n1" name="grade">钢种</th>
                <th field="status_name" align="center" width="110" class="i18n1" name="statusname">状态</th>
                <th field="od" align="center" width="50" class="i18n1" name="od">外径</th>
                <th field="wt" align="center" width="50" class="i18n1" name="wt">壁厚</th>
                <th field="p_length" align="center" width="50" class="i18n1" name="p_length">长度</th>
                <th field="weight" align="center" width="50" class="i18n1" name="weight">重量</th>
                <th field="heat_no" align="center" hidden="true" width="50" class="i18n1" name="heat_no">炉号</th>
                <th field="operator_no" align="center" width="100" class="i18n1" name="operatorno">操作工编号</th>

                <th field="coating_speed" align="center" width="80" class="i18n1" name="coatingspeed">喷涂速度</th>
                <th field="base_used" align="center" width="100" class="i18n1" name="baseused">底层型号</th>
                <th field="base_batch" align="center" width="100" hidden="true" class="i18n1" name="basebatch">底层批号</th>
                <th field="curing_agent_used" align="center" width="100" hidden="true" class="i18n1" name="curingagentused">固化剂型号</th>
                <th field="curing_agent_batch" width="100" align="center" hidden="true" class="i18n1" name="curingagentbatch">固化剂批号</th>
                <th field="curing_temp" width="100" align="center" hidden="true" class="i18n1" name="curingtemp">固化房温度</th>
                <th field="curing_start_time" align="center" width="120" class="i18n1" name="curingstarttime" data-options="formatter:formatterdate">固化开始时间</th>
                <th field="curing_finish_time" align="center" width="150" class="i18n1" name="curingfinishtime" data-options="formatter:formatterdate">固化结束时间</th>

                <th field="remark" align="center" width="150" class="i18n1" name="remark">备注</th>
                <th field="result" align="center" width="150" class="i18n1" name="result">结论</th>
                <th field="operation_time" align="center" width="150" class="i18n1" name="operationtime" data-options="formatter:formatterdate">操作时间</th>
            </tr>
            </thead>
        </table>

    </div>
</fieldset>

<!--工具栏-->
<div id="hlIdBlastProTb" style="padding:10px;">
    <span class="i18n1" name="millno">分厂编号</span>:
    <input id="millno" class="easyui-combobox" type="text" name="millno"  data-options=
            "url:'/millInfo/getAllMillsWithComboboxSelectAll.action',
					        method:'get',
					        valueField:'id',
					        width: 150,
					        editable:false,
					        textField:'text',
					        panelHeight:'auto'"/>
    <span class="i18n1" name="pipeno">钢管编号</span>:
    <input id="pipeno" name="pipeno" style="line-height:22px;border:1px solid #ccc">
    <span class="i18n1" name="operatorno">操作工编号</span>:
    <input id="operatorno" name="operatorno" style="line-height:22px;border:1px solid #ccc">
    <span class="i18n1" name="begintime">开始时间</span>:
    <input id="begintime" name="begintime" type="text" class="easyui-datebox" data-options="formatter:myformatter,parser:myparser">
    <span class="i18n1" name="endtime">结束时间</span>:
    <input id="endtime" name="endtime" type="text" class="easyui-datebox" data-options="formatter:myformatter,parser:myparser">
    <a href="#" class="easyui-linkbutton" plain="true" data-options="iconCls:'icon-search'" onclick="searchIdCoatingPro()">Search</a>
    <div style="float:right">
        <a href="#" id="addObpLinkBtn" class="easyui-linkbutton i18n1" name="add" data-options="iconCls:'icon-add',plain:true" onclick="addIdCoatingPro()">添加</a>
        <a href="#" id="editObpLinkBtn" class="easyui-linkbutton i18n1" name="edit" data-options="iconCls:'icon-edit',plain:true" onclick="editIdCoatingPro()">修改</a>
        <a href="#" id="deltObpLinkBtn" class="easyui-linkbutton i18n1" name="delete" data-options="iconCls:'icon-remove',plain:true" onclick="delIdCoatingPro()">删除</a>
    </div>
</div>

<!--添加、修改框-->
<div id="hlIdCoatingProDialog" class="easyui-dialog" data-options="title:'添加',modal:true"  closed="true" buttons="#dlg-buttons" style="display: none;padding:5px;width:950px;height:auto;">
    <form id="idCoatingProForm" method="post">
        <fieldset style="width:900px;border:solid 1px #aaa;margin-top:8px;position:relative;">
            <legend>钢管信息</legend>
            <table class="ht-table" width="100%" border="0">
                <tr>
                    <td class="i18n1" name="projectname" width="16%">项目名称</td>
                    <td colspan="2" width="33%"><label class="hl-label" id="project_name"></label></td>

                    <td class="i18n1" name="contractno" width="16%">合同编号</td>
                    <td colspan="7" width="33%"><label class="hl-label" id="contract_no"></label></td>

                </tr>

                <tr>
                    <td class="i18n1" name="pipeno" width="16%">钢管编号</td>
                    <td colspan="2" width="33%">
                        <input id="lookup1" name="pipe_no" class="mini-lookup" style="text-align:center;width:180px;"
                               textField="pipe_no" valueField="id" popupWidth="auto"
                               popup="#gridPanel1" grid="#datagrid1" multiSelect="false"/>
                    </td>
                    <td class="i18n1" name="statusname" width="16%">钢管状态</td>
                    <td colspan="7" width="33%"><label class="hl-label" id="status_name"></label></td>
                </tr>
            </table>

            <table width="100%" border="0" align="center">
                <tr>
                    <td align="center" class="i18n1" name="grade">钢种</td>
                    <td align="center"><label class="hl-label" id="grade"></label></td>
                    <td align="center" class="i18n1" name="od">外径</td>
                    <td align="center"><label class="hl-label" id="od"></label></td>
                    <td align="center" class="i18n1" name="wt">壁厚</td>
                    <td align="center"><label class="hl-label" id="wt"></label></td>
                    <td align="center" class="i18n1" name="p_length">长度</td>
                    <td align="center"><label class="hl-label" id="p_length"></label></td>
                    <td align="center" class="i18n1" name="weight">重量</td>
                    <td align="center"><label class="hl-label" id="weight"></label></td>
                    <td align="center" class="i18n1" name="heatno">炉号</td>
                    <td align="center"><label class="hl-label" id="heat_no"></label></td>
                </tr>
            </table>
        </fieldset>


        <fieldset style="width:900px;border:solid 1px #aaa;margin-top:8px;position:relative;">
            <legend>内喷涂生产信息</legend>

            <table class="ht-table">
                <tr>
                    <td class="i18n1" name="id" width="20%">流水号</td>
                    <%--<td colspan="5"><label class="hl-label" id="odcoatproid"></label></td>--%>
                    <td colspan="1" width="30%"><input id="idcoatproid" class="easyui-textbox" readonly="true" type="text" value="" name="idcoatproid"> </td>
                    <td class="i18n1" name="millno" width="20%">分厂</td>
                    <td colspan="1" width="30%">
                        <input id="mill_no" class="easyui-combobox" type="text" name="mill_no"  data-options=
                                "url:'/millInfo/getAllMills.action',
					        method:'get',
					        valueField:'id',
					        width: 185,
					        editable:false,
					        textField:'text',
					        panelHeight:'auto'"/>
                    </td>

                </tr>
                <tr>
                    <td class="i18n1" name="operatorno" width="20%">操作工编号</td>
                    <td colspan="1" width="30%">
                        <input id="lookup2" name="operator_no" class="mini-lookup" style="text-align:center;width:180px;"
                               textField="employee_no" valueField="id" popupWidth="auto"
                               popup="#gridPanel2" grid="#datagrid2" multiSelect="false"
                        />
                    </td>
                    <td class="i18n1" name="operationtime" width="20%">操作时间</td>
                    <td colspan="1" width="30%">
                        <input class="easyui-datetimebox" type="text" name="operation_time" value="" data-options="formatter:myformatter2,parser:myparser2"/>
                    </td>

                </tr>
            </table>

            <table class="ht-table">
                <tr>
                    <td class="i18n1" name="coatingspeed">喷涂速度</td>
                    <td colspan="5"><input class="easyui-numberbox" data-options="min:-99,precision:2" type="text" name="coating_speed" value=""/></td>
                </tr>

                <tr>
                    <td class="i18n1" name="baseused">底层涂料</td>
                    <%--<td><input class="easyui-textbox"  type="text" name="base_used" value=""/></td>--%>
                    <td><input id="base_used" class="easyui-combobox" type="text" name="base_used"  data-options=
                            "url:'/APPRequestTransfer/getAllCoatingPowderInfo.action',
					        method:'get',
					        valueField:'text',
					        width: 185,
					        editable:false,
					        textField:'text',
					        panelHeight:'auto'"/></td>
                    <td></td>
                    <td class="i18n1" name="basebatch">底层批号</td>
                    <td><input class="easyui-textbox"  type="text" name="base_batch" value=""/></td>
                    <td></td>
                </tr>

                <tr>
                    <td width="16%" class="i18n1" name="curingagentused">固化剂型号</td>
                    <%--<td><input class="easyui-textbox"  type="text" name="curing_agent_used" value=""/></td>--%>
                    <td><input id="curing_agent_used" class="easyui-combobox" type="text" name="curing_agent_used"  data-options=
                            "url:'/APPRequestTransfer/getAllCoatingPowderInfo.action',
					        method:'get',
					        valueField:'text',
					        width: 185,
					        editable:false,
					        textField:'text',
					        panelHeight:'auto'"/></td>
                    <td></td>
                    <td width="16%" class="i18n1" name="curingagentbatch">固化剂批号</td>
                    <td><input class="easyui-textbox"  type="text" name="curing_agent_batch" value=""/></td>
                    <td></td>
                </tr>
                <tr>
                    <td width="16%" class="i18n1" name="curingstarttime">固化开始时间</td>
                    <td>
                        <input class="easyui-datetimebox" id="curing_start_time" type="text" name="curing_start_time" value="" data-options="formatter:myformatter2,parser:myparser2"/>
                    </td>
                    <td></td>
                    <td width="16%" class="i18n1" name="curingfinishtime">固化结束时间</td>
                    <td><input class="easyui-datetimebox" id="curing_finish_time" type="text" name="curing_finish_time" value="" data-options="formatter:myformatter2,parser:myparser2"/>
                    </td>
                    <td></td>

                </tr>
                <tr>
                    <td width="16%" class="i18n1" name="curingtemp">固化房温度</td>
                    <td colspan="5"><input class="easyui-numberbox" data-options="min:-99,precision:2" type="text" name="curing_temp" value=""/></td>
                </tr>
                <tr>
                    <td width="16%" class="i18n1" name="result">结论</td>
                    <td><select id="cc" class="easyui-combobox" data-options="editable:false" name="result" style="width:200px;">
                        <option value="0">不合格,进入内防喷砂工序</option>
                        <option value="1">合格,进入内涂敷检验工序</option>
                        <option value="10">待定</option>
                    </select></td>
                    <td></td>
                    <td width="16%" class="i18n1" name="remark">备注</td>
                    <td><input class="easyui-textbox" type="text" value="" name="remark" data-options="multiline:true" style="height:60px"/></td>
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
    <a href="#" class="easyui-linkbutton" iconCls="icon-save" onclick="idCoatingProFormSubmit()">Save</a>
    <a href="#" class="easyui-linkbutton" id="hlcancelBtn" operationtype="add" iconCls="icon-cancel" onclick="idCoatingProCancelSubmit()">Cancel</a>
</div>
<div id="gridPanel1" class="mini-panel" title="header" iconCls="icon-add" style="width:450px;height:250px;"
     showToolbar="true" showCloseButton="true" showHeader="false" bodyStyle="padding:0" borderStyle="border:0"
>
    <div property="toolbar" id="searchBar1" style="padding:5px;padding-left:8px;text-align:center;display: none">
        <div style="float:left;padding-bottom:2px;">
            <span class="i18n1" name="pipeno">钢管编号</span><span>:</span>
            <input id="keyText1" class="mini-textbox" style="width:110px;" onenter="onSearchClick(1)"/>
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
         url="/pipeinfo/getPipeNumbers.action">
        <div property="columns">
            <div type="checkcolumn" ></div>
            <div field="pipe_no" width="80" headerAlign="center" allowSort="true" class="i18n1" name="pipeno">钢管编号</div>
            <div field="contract_no" width="80" headerAlign="center" allowSort="true" class="i18n1" name="contractno">合同编号</div>
            <div field="status" width="40" headerAlign="center" allowSort="true" class="i18n1" name="status">状态</div>
            <div field="od" width="40" headerAlign="center" allowSort="true" class="i18n1" name="od">外径</div>
            <div field="wt" width="40" headerAlign="center" allowSort="true" class="i18n1" name="wt">壁厚</div>
            <div field="p_length" width="40" headerAlign="center" allowSort="true" class="i18n1" name="p_length">长度</div>
            <div field="weight" width="40" headerAlign="center" allowSort="true" class="i18n1" name="weight">重量</div>
        </div>
    </div>
</div>
<div id="gridPanel2" class="mini-panel" title="header" iconCls="icon-add" style="width:480px;height:250px;"
     showToolbar="true" showCloseButton="true" showHeader="false" bodyStyle="padding:0" borderStyle="border:0"
>
    <div property="toolbar" id="searchBar2" style="padding:5px;text-align:center;display:none;">
        <div style="float:left;padding-bottom:2px;">
            <span class="i18n1" name="operatorno">操作工编号</span><span>:</span>
            <input id="keyText3" class="mini-textbox" style="width:110px;" onenter="onSearchClick(2)"/>
            <span class="i18n1" name="operatorname">姓名</span><span>:</span>
            <input id="keyText4" class="mini-textbox" style="width:110px;" onenter="onSearchClick(2)"/>
            <a class="mini-button" onclick="onSearchClick(2)" name="search">查找</a>
            <a class="mini-button" onclick="onClearClick(2)" name="clear">清除</a>
            <a class="mini-button" onclick="onCloseClick(2)" name="close">关闭</a>
        </div>
        <div style="clear:both;"></div>
    </div>
    <div id="datagrid2" class="mini-datagrid" style="width:100%;height:100%;"
         borderStyle="border:0" showPageSize="false" showPageIndex="false"
         url="/person/getPersonNoByName.action">
        <div property="columns">
            <div type="checkcolumn" ></div>
            <div field="employee_no" width="60" headerAlign="center" allowSort="true" class="i18n1" name="operatorno">操作工编号</div>
            <div field="pname" width="60" headerAlign="center" allowSort="true" class="i18n1" name="operatorname">姓名</div>
        </div>
    </div>
</div>

<script type="text/javascript" src="../easyui/jquery.easyui.min.js"></script>
</body>
</html>
<script type="text/javascript">
    mini.parse();
    var grid= mini.get("multiupload1");
    var keyText1=mini.get('keyText1');
    var keyText4 = mini.get("keyText4");
    var keyText3=mini.get("keyText3");
    var grid1=mini.get("datagrid1");
    var grid2=mini.get("datagrid2");
    var look1=mini.get('lookup1');
    var look2= mini.get("lookup2");


    function onSearchClick(type) {
        if(type==1)
        {
            grid1.load({
                pipe_no:keyText1.value,
                pipestatus:'id2,'
            });
        }else if(type==2){
            grid2.load({
                pname: keyText4.value,
                employeeno:keyText3.value
            });
        }

    }
    function onCloseClick(type) {
        if(type==1)
            look1.hidePopup();
        else if(type==2)
            look2.hidePopup();
    }
    function onClearClick(type) {
        if(type==1)
            look1.deselectAll();
        else if(type==2)
            look2.deselectAll();
    }
    look1.on('valuechanged',function () {
        var rows = grid1.getSelected();
        $("input[name='pipe_no']").val(rows.pipe_no);
        clearLabelPipeInfo();
        $.ajax({
            url:'../pipeinfo/getPipeInfoByNo.action',
            data:{'pipe_no':rows.pipe_no},
            dataType:'json',
            success:function (data) {
                if(data!=null&&data!=""){
                    addLabelPipeInfo(data);
                }
            },
            error:function () {
                hlAlertThree();
            }
        });
    });
    look2.on('valuechanged',function (e){
        var rows = grid2.getSelected();
        $("input[name='operator_no']").val(rows.employee_no);
    });
    look1.on("showpopup",function(e){
        $('.mini-shadow').css('z-index','99999');
        $('.mini-popup').css('z-index','100000');
        $('.mini-panel').css('z-index','100000');
        $('#searchBar1').css('display','block');
        grid1.load({
            pipe_no:keyText1.value,
            pipestatus:'id2,'
        });
    });
    look2.on("showpopup",function(e){
        $('.mini-shadow').css('z-index','99999');
        $('.mini-popup').css('z-index','100000');
        $('.mini-panel').css('z-index','100000');
        $('#searchBar2').css('display','block');
        grid2.load({
            pname: keyText4.value,
            employeeno:keyText3.value
        });
    });
    hlLanguage("../i18n/");
</script>