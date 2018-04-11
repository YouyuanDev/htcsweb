<%--
  Created by IntelliJ IDEA.
  User: kurt
  Date: 2/28/18
  Time: 9:41 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
    String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>涂层修补记录</title>
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
        function searchCoatingRepair() {
            $('#repairDatagrids').datagrid('load',{
                'project_no': $('#projectno').val(),
                'contract_no': $('#contractno').val(),
                'pipe_no':$('#pipeno').val(),
                'operator_no': $('#operatorno').val(),
                'begin_time': $('#begintime').val(),
                'end_time': $('#endtime').val(),
                'mill_no': $('#millno').val()
            });
        }
        $(function () {
            $(document).on('click','.content-del',function () {
                delUploadPicture($(this));
            });
            $('#hlCoatingRepairDialog').dialog({
                onClose:function () {
                    var type=$('#hlcancelBtn').attr('operationtype');
                    if(type=="add"){
                        var $imglist=$('#fileslist');
                        var $dialog=$('#hlCoatingRepairDialog');
                        hlAlertSix("../UploadFile/delUploadPicture.action",$imglist,$dialog,grid);
                    }
                    clearFormLabel();
                }
            });
            $('.mini-buttonedit .mini-buttonedit-input').css('width','150px');

        });


        function addCoatingRepair(){
            $('#hlcancelBtn').attr('operationtype','add');
            $('#hlCoatingRepairDialog').dialog('open').dialog('setTitle','新增修补记录');
            clearFormLabel();
            clearMultiUpload(grid);
            url="/coatingRepairOperation/saveCoatingRepair.action";
        }
        function editCoatingRepair() {
            $('#hlcancelBtn').attr('operationtype','edit');
            var row = $('#repairDatagrids').datagrid('getSelected');
            if(row){
                $('#hlCoatingRepairDialog').dialog('open').dialog('setTitle','修改');
                loadPipeBaiscInfo(row);
                $('#coatingRepairForm').form('load',row);
                $('#crid').text(row.id);
                $('#operation-time').datetimebox('setValue',getDate1(row.operation_time));
                $('#inspection-time').datetimebox('setValue',getDate1(row.inspection_time));

                look1.setText(row.pipe_no);
                look1.setValue(row.pipe_no);
                look2.setText(row.operator_no);
                look2.setValue(row.operator_no);
                look3.setText(row.inspector_no);
                look3.setValue(row.inspector_no);
                var odpictures=row.upload_files;
                if(odpictures!=null&&odpictures!=""){
                    var imgList=odpictures.split(';');
                    createPictureModel(basePath,imgList);
                }



                var accurl;
                if(row.odid=="od")
                    accurl='/AcceptanceCriteriaOperation/getODAcceptanceCriteriaByContractNo.action';
                else
                    accurl='/AcceptanceCriteriaOperation/getIDAcceptanceCriteriaByContractNo.action';

                //异步获取标准并匹配
                $.ajax({
                    url:accurl,
                    dataType:'json',
                    data:{'contract_no':row.contract_no},
                    success:function (data) {
                        var $obj1=$("input[name='repair_thickness']");
                        var $obj2=$("input[name='holiday_number']");
                        var $obj3=$("input[name='repair_number']");


                        $obj1.siblings().css("background-color","#FFFFFF");
                        $obj2.siblings().css("background-color","#FFFFFF");
                        $obj3.siblings().css("background-color","#FFFFFF");


                        if(data!=null){
                            var res1=changeComma($obj1.val());
                            var res1_1=res1.split(',');
                            var res2=$obj2.val();
                            var res3=$obj3.val();

                            if(row.odid=="id") {

                                for (var i = 0; i < res1_1.length; i++) {
                                    if (res1_1[i] != "" && res1_1.length > 0) {
                                        if (!((res1_1[i] >= data.dry_film_thickness_min) && (res1_1[i] <= data.dry_film_thickness_max)))
                                            $obj1.siblings().css("background-color", "#F9A6A6");
                                    }
                                }
                            }
                            else if(row.odid=="od"){
                                if(row.coating_type=="2FBE"){

                                    for (var i = 0; i < res1_1.length; i++) {
                                        if (res1_1[i] != "" && res1_1.length > 0) {
                                            if (!((res1_1[i] >= data.total_2fbe_coat_thickness_min) && (res1_1[i] <= data.total_2fbe_coat_thickness_max)))
                                                $obj1.siblings().css("background-color", "#F9A6A6");
                                        }
                                    }
                                }
                                else if(row.coating_type=="3LPE"){

                                    for (var i = 0; i < res1_1.length; i++) {
                                        if (res1_1[i] != "" && res1_1.length > 0) {
                                            if (!((res1_1[i] >= data.total_3lpe_coat_thickness_min) && (res1_1[i] <= data.total_3lpe_coat_thickness_max)))
                                                $obj1.siblings().css("background-color", "#F9A6A6");
                                        }
                                    }
                                }
                            }

                            if(!((res2>=data.holiday_min)&&(res2<=data.holiday_max)))
                                $obj2.siblings().css("background-color","#F9A6A6");
                            if(!((res3>=data.repair_min)&&(res3<=data.repair_max)))
                                $obj3.siblings().css("background-color","#F9A6A6");

                        }
                    },error:function () {

                    }
                });
                url="/coatingRepairOperation/saveCoatingRepair.action?id="+row.id;

            }else{
                hlAlertTwo();
            }
        }
        function delCoatingRepair() {
            var row = $('#repairDatagrids').datagrid('getSelections');
            if(row.length>0){
                var idArr=[];
                for (var i=0;i<row.length;i++){
                    idArr.push(row[i].id);
                }
                var idArrs=idArr.join(',');

                $.messager.confirm('系统提示',"您确定要删除这<font color=red>"+idArr.length+ "</font>条数据吗？",function (r) {
                    if(r){
                        $.post(
                            "/coatingRepairOperation/delCoatingRepair.action",
                            {"hlparam":idArrs},function (data) {
                                if(data.success){
                                    $("#repairDatagrids").datagrid("reload");
                                }
                                hlAlertFour(data.message);
                            },"json");
                    }
                });
            }else{
                hlAlertOne();
            }
        }
        //取消保存
        function CoatingRepairFormCancelSubmit() {
            $('#hlCoatingRepairDialog').dialog('close');
        }

        //增加或保存合同信息
        function CoatingRepairFormSubmit() {
            $('#coatingRepairForm').form('submit',{
                url:url,
                onSubmit:function () {
                    //表单验证
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
                    if($("input[name='operation-time']").val()==""){

                        hlAlertFour("请输入操作时间");
                        return false;
                    }
                    if($("input[name='inspector_no']").val()==""){

                        hlAlertFour("请输入检验员工号");
                        return false;
                    }
                    if($("input[name='instime']").val()==""){

                        hlAlertFour("请输入检验时间");
                        return false;
                    }
                    var arg1=$("input[name='repair_thickness']").val().trim();
                    $("input[name='repair_thickness']").val(changeComma(arg1));

                    setParams($("input[name='repair_number']"));
                    setParams($("input[name='holiday_number']"));

                    //return $('#coatingRepairForm').form('enableValidation').form('validate');
                },
                success: function(result){
                    var result = eval('('+result+')');
                    clearFormLabel();
                    $('#hlCoatingRepairDialog').dialog('close');
                    if (result.success){
                        $('#repairDatagrids').datagrid('reload');
                    }
                    hlAlertFour(result.message);
                },
                error:function () {
                    hlAlertThree();
                }
            });
            clearMultiUpload(grid);
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
        // function editFilesList(type,imgUrl) {
        //     var $obj=$('#fileslist');
        //     if(type==0){
        //         var filesList=$('#fileslist').val();
        //         $obj.val(filesList+imgUrl+";");
        //     }else{
        //         $obj.val($obj.val().replace(imgUrl+";",''));
        //     }
        //     return $obj.val();
        // }
        //删除选择的图片
        // function delUploadPicture($obj) {
        //     var imgUrl=$obj.siblings('dt').find('img').attr('src');
        //     var imgName=imgUrl.substr(imgUrl.lastIndexOf('/')+1);
        //     $.ajax({
        //         url:'../UploadFile/delUploadPicture.action',
        //         dataType:'json',
        //         data:{"imgList":imgName+";"},
        //         success:function (data) {
        //             if(data.success){
        //                 var imgList=editFilesList(2,imgName);
        //                 $(this).parent('.content-dl').remove();
        //             }else{
        //                 hlAlertFour("移除失败!");
        //             }
        //         },
        //         error:function () {
        //             hlAlertThree();
        //         }
        //     });
        // }
        //创建图片展示模型(参数是图片集合)
        <%--function  createPictureModel(imgList) {--%>
            <%--var basePath ="<%=basePath%>"+"/upload/pictures/";--%>
            <%--if($('#hl-gallery').length>0){--%>
                <%--$('#content_list').empty();--%>
                <%--for(var i=0;i<imgList.length-1;i++){--%>
                    <%--$('#content_list').append(getCalleryChildren(basePath+imgList[i]));--%>
                <%--}--%>
            <%--}else{--%>
                <%--$('#hl-gallery-con').append(getGalleryCon());--%>
                <%--for(var i=0;i<imgList.length-1;i++){--%>
                    <%--$('#content_list').append(getCalleryChildren(basePath+imgList[i]));--%>
                <%--}--%>
            <%--}--%>
        <%--}--%>

        <%--function  setParams($obj) {--%>
            <%--if($obj.val()==null||$obj.val()=="")--%>
                <%--$obj.val(0);--%>
        <%--}--%>

        function  clearFormLabel() {
            $('#coatingRepairForm').form('clear');
            $('.hl-label').text('');
            $('#hl-gallery-con').empty();
        }
    </script>
</head>
<body>
<fieldset class="b3" style="padding:10px;margin:10px;">
    <legend> <h3><b style="color: orange" >|&nbsp;</b><span class="i18n1" name="datadisplay">数据展示</span></h3></legend>
    <div  style="margin-top:5px;">
        <table class="easyui-datagrid" id="repairDatagrids" url="/coatingRepairOperation/getCoatingRepairInfoByLike.action" striped="true" loadMsg="正在加载中。。。" textField="text" pageSize="20" fitColumns="true" pagination="true" toolbar="#hlcoatingRepairTb">
            <thead>
            <tr>
                <th data-options="field:'ck',checkbox:true"></th>
                <th field="id" align="center" width="100" class="i18n1" name="id">流水号</th>
                <th field="project_no" align="center" width="120" class="i18n1" name="projectno">项目编号</th>
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

                <th field="coating_type" align="center" width="80" class="i18n1" name="coatingtype">涂层类型</th>
                <th field="odid" align="center" width="100" class="i18n1" name="odid">外/内防</th>
                <th field="repair_size" align="center" width="100" hidden="true" class="i18n1" name="repairsize">修补大小</th>
                <th field="repair_number" align="center" width="100" hidden="true" class="i18n1" name="repairnumber">修补数量</th>
                <th field="holiday_number" width="100" align="center" hidden="true" class="i18n1" name="holidaynumber">漏点数量</th>
                <th field="repair_method" width="100" align="center" hidden="true" class="i18n1" name="repairmethod">修补方法</th>
                <th field="unqualified_reason" align="center" width="120" class="i18n1" name="unqualifiedreason">不合格原因</th>
                <th field="inspector_no" align="center" width="150" class="i18n1" name="inspectorno" >检验员工号</th>

                <th field="inspection_time" align="center" width="150" class="i18n1" name="inspectiontime" data-options="formatter:formatterdate">检验时间</th>
                <th field="surface_condition" align="center" width="150" class="i18n1" name="surfacecondition">表面质量</th>
                <th field="repair_thickness" align="center" width="150" class="i18n1" name="repairthickness">修补厚度</th>
                <th field="holiday_testing" align="center" width="150" class="i18n1" name="holidaytesting">检漏结果</th>
                <th field="adhesion" align="center" width="150" class="i18n1" name="adhesion">附着力</th>
                <th field="result" align="center" width="150" class="i18n1" name="result">结论</th>
                <th field="remark" align="center" width="150" class="i18n1" name="remark">备注</th>

                <th field="operation_time" align="center" width="150" class="i18n1" name="operationtime" data-options="formatter:formatterdate">操作时间</th>
            </tr>
            </thead>
        </table>

    </div>
</fieldset>

<!--工具栏-->
<div id="hlcoatingRepairTb" style="padding:10px;">
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
    <input id="pipeno" name="pipeno"  style="width:100px;line-height:22px;border:1px solid #ccc">
    <span class="i18n1" name="contractno">合同编号</span>:
    <input id="contractno" name="contractno" style="width:100px;line-height:22px;border:1px solid #ccc">
    <span class="i18n1" name="projectno">项目编号</span>:
    <input id="projectno" name="projectno"  style="width:100px;line-height:22px;border:1px solid #ccc"><br><br>
    <span class="i18n1" name="operatorno">操作工编号</span>:
    <input id="operatorno" name="operatorno" style="line-height:22px;border:1px solid #ccc">
    <span class="i18n1" name="begintime">开始时间</span>:
    <input id="begintime" name="begintime" type="text" class="easyui-datebox" data-options="formatter:myformatter,parser:myparser">
    <span class="i18n1" name="endtime">结束时间</span>:
    <input id="endtime" name="endtime" type="text" class="easyui-datebox" data-options="formatter:myformatter,parser:myparser">


    <a href="#" class="easyui-linkbutton" plain="true" data-options="iconCls:'icon-search'" onclick="searchCoatingRepair()">Search</a>
    <div style="float:right">
        <a href="#" id="addCoatingRepairLinkBtn" class="easyui-linkbutton i18n1" name="add" data-options="iconCls:'icon-add',plain:true" onclick="addCoatingRepair()">添加</a>
        <a href="#" id="editCoatingRepairLinkBtn" class="easyui-linkbutton i18n1" name="edit" data-options="iconCls:'icon-edit',plain:true" onclick="editCoatingRepair()">修改</a>
        <a href="#" id="deltCoatingRepairLinkBtn" class="easyui-linkbutton i18n1" name="delete" data-options="iconCls:'icon-remove',plain:true" onclick="delCoatingRepair()">删除</a>
    </div>
</div>

<!--添加、修改框-->
<div id="hlCoatingRepairDialog" class="easyui-dialog" data-options="title:'添加',modal:true"  closed="true" buttons="#dlg-buttons" style="display: none;padding:5px;width:950px;height:auto;">
    <form id="coatingRepairForm" method="post">
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
                    <td class="i18n1" name="statusname" width="16%">状态</td>
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
            <legend>修补及检验信息</legend>

            <table class="ht-table">
                <tr>
                    <td class="i18n1" name="id" colspan="1">流水号</td>
                    <td colspan="3"><label class="hl-label" id="crid"></label></td>
                </tr>
                <tr>
                    <td class="i18n1" name="operatorno" width="20%">操作工编号</td>
                    <td   width="30%">
                        <input id="lookup2" name="operator_no" class="mini-lookup" style="text-align:center;width:185px;"
                               textField="employee_no" valueField="id" popupWidth="auto"
                               popup="#gridPanel2" grid="#datagrid2" multiSelect="false"
                        />
                    </td>
                    <td class="i18n1" name="operationtime" width="20%">操作时间</td>
                    <td  width="30%">
                        <input class="easyui-datetimebox" id="operation-time" type="text" name="operation-time" value="" data-options="formatter:myformatter2,parser:myparser2"/>
                    </td>
                </tr>
            </table>

            <table class="ht-table">
                <tr>
                    <td class="i18n1" name="coatingtype" width="20%">涂层类型</td>
                    <td width="30%">
                        <select id="coating_type" class="easyui-combobox" data-options="editable:false" name="coating_type"   style="width:185px;">
                            <option value="2FBE">2FBE</option>
                            <option value="3LPE">3LPE</option>
                            <option value="Liquid Epoxy">Liquid Epoxy</option>
                            <option value="Other">Other</option>
                        </select>
                    </td>
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
                    <td width="20%" class="i18n1" name="repairsize">修补大小</td>
                    <td width="30%">
                        <input class="easyui-textbox"  type="text" name="repair_size" value=""/>

                    </td>
                    <td class="i18n1" name="odid" width="20%">外/内防</td>
                    <td width="30%">
                        <select id="odid" class="easyui-combobox" data-options="editable:false" name="odid"   style="width:185px;">
                            <option value="od">外涂层</option>
                            <option value="id">内涂层</option>
                        </select>
                    </td>

                </tr>
                <tr>
                    <td width="20%" class="i18n1" name="holidaynumber">漏点数量</td>
                    <td width="30%">
                        <input class="easyui-numberbox" data-options="min:0,precision:0" type="text" name="holiday_number" value=""/>

                    </td>
                    <td width="20%" class="i18n1" name="repairnumber">修补数量</td>
                    <td width="30%">
                        <input class="easyui-numberbox" data-options="min:0,precision:0" type="text" name="repair_number" value=""/>
                    </td>

                </tr>
                <tr>
                    <td width="20%"  class="i18n1" name="unqualifiedreason">不合格原因</td>
                    <td width="30%">
                        <%--<input class="easyui-textbox"  type="text" name="unqualified_reason" value=""/>--%>
                        <select id="unqualified_reason" class="easyui-combobox" data-options="editable:false" name="unqualified_reason"   style="width:185px;">
                            <option value="1">漏点</option>
                            <option value="2">碰伤</option>
                            <option value="3">厚度不合</option>
                            <option value="4">附着力不合</option>
                            <option value="5">有杂质</option>
                        </select>
                    </td>
                    <td width="20%" class="i18n1" name="repairmethod">修补方法</td>
                    <td width="30%">
                        <select id="repair_method" class="easyui-combobox" data-options="editable:false" name="repair_method"   style="width:185px;">
                            <option value="2POXY">2POXY</option>
                            <option value="XXXXX">XXXXX</option>
                        </select>
                    </td>

                </tr>
                <tr>
                    <td width="20%" class="i18n1" name="inspectiontime">检验时间</td>
                    <td width="30%">
                        <input class="easyui-datetimebox" id="inspection-time" type="text" name="instime" value="" data-options="formatter:myformatter2,parser:myparser2"/>

                    </td>
                    <td width="20%"  class="i18n1" name="inspectorno">检验员编号</td>
                    <td width="30%">
                        <input id="lookup3" name="inspector_no" class="mini-lookup" style="text-align:center;width:180px;"
                               textField="employee_no" valueField="id" popupWidth="auto"
                               popup="#gridPanel3" grid="#datagrid3" multiSelect="false"
                        />
                    </td>

                </tr>
                <tr>
                    <td width="20%" class="i18n1" name="repairthickness">修补厚度（,分隔）</td>
                    <td width="30%" >
                        <input class="easyui-textbox"  type="text" name="repair_thickness" value=""/>

                    </td>
                    <td width="20%" class="i18n1" name="surfacecondition1">表面质量</td>
                    <td width="30%">
                        <select id="surface_condition" class="easyui-combobox" data-options="editable:false" name="surface_condition"   style="width:185px;">
                            <option value="OK">OK</option>
                            <option value="Not OK">Not OK</option>
                        </select>
                    </td>

                </tr>
                <tr>
                    <td width="20%" class="i18n1" name="adhesion">附着力</td>
                    <td width="30%">
                        <select id="adhesion" class="easyui-combobox" data-options="editable:false" name="adhesion"   style="width:185px;">
                            <option value="OK">OK</option>
                            <option value="Not OK">Not OK</option>
                        </select>

                    </td>
                    <td width="20%" class="i18n1" name="holidaytesting">漏点检测</td>
                    <td width="30%">
                        <select id="holiday_testing" class="easyui-combobox" data-options="editable:false" name="holiday_testing"   style="width:185px;">
                            <option value="OK">OK</option>
                            <option value="Not OK">Not OK</option>
                        </select>
                    </td>

                </tr>
                <tr>
                    <td width="20%"  class="i18n1" name="result">结论</td>
                    <td width="30%"><select id="cc" class="easyui-combobox" data-options="editable:false" name="result"   style="width:185px;">
                        <option value="0">不合格,重新修补</option>
                        <option value="1">合格</option>
                        <option value="2">不合格,扒皮处理</option>
                        <option value="3">待定</option>
                    </select></td>
                    <td width="20%" class="i18n1" name="remark">备注</td>
                    <td width="30%">
                        <input class="easyui-textbox"  data-options="multiline:true" type="text" name="remark" value=""/>
                    </td>
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
    <a href="#" class="easyui-linkbutton" iconCls="icon-save" onclick="CoatingRepairFormSubmit()">Save</a>
    <a href="#" class="easyui-linkbutton" id="hlcancelBtn" operationtype="add" iconCls="icon-cancel" onclick="CoatingRepairFormCancelSubmit()">Cancel</a>
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
<div id="gridPanel3" class="mini-panel" title="header" iconCls="icon-add" style="width:480px;height:250px;"
     showToolbar="true" showCloseButton="true" showHeader="false" bodyStyle="padding:0" borderStyle="border:0"
>
    <div property="toolbar" id="searchBar3" style="padding:5px;text-align:center;display:none;">
        <div style="float:left;padding-bottom:2px;">
            <span class="i18n1" name="operatorno">操作工编号</span><span>:</span>
            <input id="keyText5" class="mini-textbox" style="width:110px;" onenter="onSearchClick(3)"/>
            <span class="i18n1" name="operatorname">姓名</span><span>:</span>
            <input id="keyText6" class="mini-textbox" style="width:110px;" onenter="onSearchClick(3)"/>
            <a class="mini-button" onclick="onSearchClick(3)" name="search">查找</a>
            <a class="mini-button" onclick="onClearClick(3)" name="clear">清除</a>
            <a class="mini-button" onclick="onCloseClick(3)" name="close">关闭</a>
        </div>
        <div style="clear:both;"></div>
    </div>
    <div id="datagrid3" class="mini-datagrid" style="width:100%;height:100%;"
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
    var keyText5 = mini.get("keyText5");
    var keyText6=mini.get("keyText6");
    var grid1=mini.get("datagrid1");
    var grid2=mini.get("datagrid2");
    var grid3=mini.get("datagrid3");
    var look1=mini.get('lookup1');
    var look2= mini.get("lookup2");
    var look3= mini.get("lookup3");

    function onSearchClick(type) {
        if(type==1)
        {
            grid1.load({
                pipe_no:keyText1.value,
                pipestatus:'odrepair1,idrepair1,odrepair2,idrepair2'
            });
        }else if(type==2){
            grid2.load({
                pname: keyText4.value,
                employeeno:keyText3.value
            });
        }else if(type==3){
            grid3.load({
                pname: keyText6.value,
                employeeno:keyText5.value
            });
        }

    }
    function onCloseClick(type) {
        if(type==1)
            look1.hidePopup();
        else if(type==2){
            look2.hidePopup();
        }else if(type==3){
            look3.hidePopup();
        }
    }
    function onClearClick(type) {
        if(type==1)
            look1.deselectAll();
        else if(type==2){
            look2.deselectAll();
        }else if(type==3){
            look3.deselectAll();
        }
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
    look3.on('valuechanged',function (e){
        var rows = grid3.getSelected();
        $("input[name='inspector_no']").val(rows.employee_no);
    });
    look1.on("showpopup",function(e){
        $('.mini-shadow').css('z-index','99999');
        $('.mini-popup').css('z-index','100000');
        $('.mini-panel').css('z-index','100000');
        $('#searchBar1').css('display','block');
        grid1.load({
            pipe_no:keyText1.value,
            pipestatus:'odrepair1,idrepair1,odrepair2,idrepair2'
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
    look3.on("showpopup",function(e){
        $('.mini-shadow').css('z-index','99999');
        $('.mini-popup').css('z-index','100000');
        $('.mini-panel').css('z-index','100000');
        $('#searchBar3').css('display','block');
        grid3.load({
            pname: keyText6.value,
            employeeno:keyText5.value
        });
    });
    hlLanguage("../i18n/");
</script>