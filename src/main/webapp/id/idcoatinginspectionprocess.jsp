<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
    String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>内涂检验岗位</title>
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
            $('#hlIdCoatingInProDialog').dialog({
                onClose:function () {
                    var type=$('#hlcancelBtn').attr('operationtype');

                    if(type=="add"){
                        var $imglist=$('#fileslist');
                        var $dialog=$('#hlIdCoatingInProDialog');
                        hlAlertSix("../UploadFile/delUploadPicture.action",$imglist,$dialog,grid);
                    }
                    clearFormLabel();
                }
            });
            $('.mini-buttonedit .mini-buttonedit-input').css('width','150px');
            // hlLanguage("../i18n/");
        });
        function addIdCoatingInPro(){
            $('#hlcancelBtn').attr('operationtype','add');
            $('#hlIdCoatingInProDialog').dialog('open').dialog('setTitle','新增');
            // $('#fileslist').val('');
            // $('#idCoatingInProForm').form('clear');
            // $('#idcoatInproid').text('');
            clearFormLabel();
            combox1.setValue("");
            clearMultiUpload(grid);
            url="/IdCoatInOperation/saveIdCoatingInProcess.action";
        }
        function delIdCoatingInPro() {
            var row = $('#idCoatingInProDatagrids').datagrid('getSelections');
            if(row.length>0){
                var idArr=[];
                for (var i=0;i<row.length;i++){
                    idArr.push(row[i].id);
                }
                var idArrs=idArr.join(',');
                $.messager.confirm('系统提示',"您确定要删除这<font color=red>"+idArr.length+ "</font>条数据吗？",function (r) {
                    if(r){
                        $.post("/IdCoatInOperation/delIdCoatingInProcess.action",{"hlparam":idArrs},function (data) {
                            if(data.success){
                                $("#idCoatingInProDatagrids").datagrid("reload");
                            }
                            hlAlertFour(data.message);
                        },"json");
                    }
                });
            }else{
                hlAlertOne();
            }
        }
        function editIdCoatingInPro() {
            $('#hlcancelBtn').attr('operationtype','edit');
            var row = $('#idCoatingInProDatagrids').datagrid('getSelected');
            if(row){
                $('#hlIdCoatingInProDialog').dialog('open').dialog('setTitle','修改');
                // $('#project_name').text(row.project_name);$('#contract_no').text(row.contract_no);
                // $('#pipe_no').text(row.pipe_no);$('#status_name').text(row.status_name);
                // $('#od').text(row.od);$('#wt').text(row.wt);
                // $('#p_length').text(row.p_length);$('#weight').text(row.weight);
                // $('#grade').text(row.grade);$('#heat_no').text(row.heat_no);
                loadPipeBaiscInfo(row);
                $('#idCoatingInProForm').form('load',row);
                $('#idcoatInprotime').datetimebox('setValue',getDate1(row.operation_time));
                $("#idcoatInproid").textbox("setValue", row.id);
                var sample=row.is_sample;
                if(sample=="1"){
                    $('#is-sample').prop('checked', true);
                    $("input[name='is_sample']").val(1);
                }else{
                    $('#is-sample').prop('checked', false);
                    $("input[name='is_sample']").val(0);
                }
                look1.setText(row.pipe_no);
                look1.setValue(row.pipe_no);
                look2.setText(row.operator_no);
                look2.setValue(row.operator_no);
                combox1.setValue(row.surface_condition);
                var odpictures=row.upload_files;
                if(odpictures!=null&&odpictures!=""){
                    var imgList=odpictures.split(';');
                    createPictureModel(basePath,imgList);
                }
                //异步获取标准并匹配
                $.ajax({
                    url:'/AcceptanceCriteriaOperation/getIDAcceptanceCriteriaByContractNo.action',
                    dataType:'json',
                    data:{'contract_no':row.contract_no},
                    success:function (data) {
                        var $obj1=$("input[name='dry_film_thickness_max']");
                        var $obj2=$("input[name='dry_film_thickness_min']");
                        var $obj3=$("input[name='cutback']");
                        var $obj4=$("input[name='magnetism']");
                        $obj1.siblings().css("background-color","#FFFFFF");
                        $obj2.siblings().css("background-color","#FFFFFF");
                        $obj3.siblings().css("background-color","#FFFFFF");
                        $obj4.siblings().css("background-color","#FFFFFF");
                        if(data!=null){
                            var res1=$obj1.val();
                            var res2=$obj2.val();
                            var res3=$obj3.val();
                            var res4=$obj4.val();
                            if(!((res1>=data.dry_film_thickness_min)&&(res1<=data.dry_film_thickness_max)))
                                $obj1.siblings().css("background-color","#F9A6A6");
                            if(!((res2>=data.dry_film_thickness_min)&&(res2<=data.dry_film_thickness_max)))
                                $obj2.siblings().css("background-color","#F9A6A6");
                            if(!((res3>=data.cutback_min)&&(res3<=data.cutback_max)))
                                $obj3.siblings().css("background-color","#F9A6A6");
                            if(!((res4>=data.residual_magnetism_min)&&(res4<=data.residual_magnetism_max)))
                                $obj4.siblings().css("background-color","#F9A6A6");
                        }
                    },error:function () {

                    }
                });
                url="/IdCoatInOperation/saveIdCoatingInProcess.action?id="+row.id;

            }else{
                hlAlertTwo();
            }
        }
        function searchIdCoatingInPro() {
            $('#idCoatingInProDatagrids').datagrid('load',{
                'pipe_no': $('#pipeno').val(),
                'operator_no': $('#operatorno').val(),
                'begin_time': $('#begintime').val(),
                'end_time': $('#endtime').val(),
                'mill_no': $('#millno').val()
            });
        }
        function idCoatingInProFormSubmit() {
            $('#idCoatingInProForm').form('submit',{
                url:url,
                onSubmit:function () {
                    //表单验证
                    setParams($("input[name='dry_film_thickness_max']"));
                    setParams($("input[name='dry_film_thickness_min']"));
                    setParams($("input[name='cutback']"));
                    setParams($("input[name='holiday_tester_volts']"));
                    setParams($("input[name='holiday_test_results']"));
                    setParams($("input[name='magnetism']"));
                    setParams($("input[name='internal_repairs']"));
                    if($("input[name='idcoatInprotime']").val()==""){
                        hlAlertFour("请输入操作时间");
                        return false;
                    }
                },
                success: function(result){
                    var result = eval('('+result+')');
                    if (result.success){
                        $('#hlIdCoatingInProDialog').dialog('close');
                        $('#idCoatingInProDatagrids').datagrid('reload');
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
        function idCoatingInProCancelSubmit() {
            $('#hlIdCoatingInProDialog').dialog('close');
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
        <%--function editFilesList(type,imgUrl) {--%>
            <%--var $obj=$('#fileslist');--%>
            <%--if(type==0){--%>
                <%--var filesList=$('#fileslist').val();--%>
                <%--$obj.val(filesList+imgUrl+";");--%>
            <%--}else{--%>
                <%--$obj.val($obj.val().replace(imgUrl+";",''));--%>
            <%--}--%>
            <%--return $obj.val();--%>
        <%--}--%>
        <%--//删除选择的图片--%>
        <%--function delUploadPicture($obj) {--%>
            <%--var imgUrl=$obj.siblings('dt').find('img').attr('src');--%>
            <%--var imgName=imgUrl.substr(imgUrl.lastIndexOf('/')+1);--%>
            <%--$.ajax({--%>
                <%--url:'../UploadFile/delUploadPicture.action',--%>
                <%--dataType:'json',--%>
                <%--data:{"imgList":imgName+";"},--%>
                <%--success:function (data) {--%>
                    <%--if(data.success){--%>
                        <%--var imgList=editFilesList(2,imgName);--%>
                        <%--$(this).parent('.content-dl').remove();--%>
                    <%--}else{--%>
                        <%--hlAlertFour("移除失败!");--%>
                    <%--}--%>
                <%--},--%>
                <%--error:function () {--%>
                    <%--hlAlertThree();--%>
                <%--}--%>
            <%--});--%>
        <%--}--%>
        <%--//创建图片展示模型(参数是图片集合)--%>
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
        //清理form表单
        function  clearFormLabel() {
            $('#idCoatingInProForm').form('clear');
            $('.hl-label').text(''); $('#hl-gallery-con').empty();
        }
    </script>
</head>

<body>
<fieldset class="b3" style="padding:10px;margin:10px;">
    <legend> <h3><b style="color: orange" >|&nbsp;</b><span class="i18n1" name="datadisplay">数据展示</span></h3></legend>
    <div  style="margin-top:5px;">
        <table class="easyui-datagrid" id="idCoatingInProDatagrids" url="/IdCoatInOperation/getIdCoatingInByLike.action" striped="true" loadMsg="正在加载中。。。" textField="text" pageSize="20" fitColumns="true" pagination="true" toolbar="#hlIdCoatingInProTb">
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
                <th field="is_sample" align="center" width="80" class="i18n1" name="issample">取样管</th>
                <th field="dry_film_thickness_max" align="center" width="80" class="i18n1" name="dryfilmthicknessmax">最大干膜厚度</th>
                <th field="dry_film_thickness_min" align="center" width="100" class="i18n1" name="dryfilmthicknessmin">最小干膜厚度</th>
                <th field="cutback" align="center" width="100" hidden="true" class="i18n1" name="cutback">预留端长度</th>
                <th field="holiday_tester_volts" width="100" align="center" hidden="true" class="i18n1" name="holidaytestervolts">电火花检测电压</th>
                <th field="holiday_test_results" align="center" width="100" hidden="true" class="i18n1" name="holidaytestresults">电火花检验结果</th>
                <th field="surface_condition" align="center" width="150" class="i18n1" name="surfacecondition1">表面质量</th>
                <th field="bevel_check" align="center" width="80" class="i18n1" name="bevelcheck">坡口质量</th>
                <th field="magnetism" width="100" align="center"  class="i18n1" name="magnetism">剩磁</th>
                <th field="internal_repairs" width="100" align="center"  class="i18n1" name="internalrepairs">内涂层修补数</th>

                <th field="remark" align="center" width="150" class="i18n1" name="remark">备注</th>
                <th field="result" align="center" width="150" class="i18n1" name="result">结论</th>
                <th field="operation_time" align="center" width="150" class="i18n1" name="operationtime" data-options="formatter:formatterdate">操作时间</th>
            </tr>
            </thead>
        </table>

    </div>
</fieldset>

<!--工具栏-->
<div id="hlIdCoatingInProTb" style="padding:10px;">
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
    <a href="#" class="easyui-linkbutton" plain="true" data-options="iconCls:'icon-search'" onclick="searchIdCoatingInPro()">Search</a>
    <div style="float:right">
        <a href="#" id="addObpLinkBtn" class="easyui-linkbutton i18n1" name="add" data-options="iconCls:'icon-add',plain:true" onclick="addIdCoatingInPro()">添加</a>
        <a href="#" id="editObpLinkBtn" class="easyui-linkbutton i18n1" name="edit" data-options="iconCls:'icon-edit',plain:true" onclick="editIdCoatingInPro()">修改</a>
        <a href="#" id="deltObpLinkBtn" class="easyui-linkbutton i18n1" name="delete" data-options="iconCls:'icon-remove',plain:true" onclick="delIdCoatingInPro()">删除</a>
    </div>
</div>

<!--添加、修改框-->
<div id="hlIdCoatingInProDialog" class="easyui-dialog" data-options="title:'添加',modal:true"  closed="true" buttons="#dlg-buttons" style="display: none;padding:5px;width:950px;height:auto;">
    <form id="idCoatingInProForm" method="post">
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
            <legend>内涂检验信息</legend>

            <table class="ht-table">
                <tr>
                    <td class="i18n1" name="id" width="20%">流水号</td>
                    <%--<td colspan="5"><label class="hl-label" id="odcoatproid"></label></td>--%>
                    <td colspan="1" width="30%"><input id="idcoatInproid" class="easyui-textbox" readonly="true" type="text" value="" name="idcoatInproid"> </td>
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
                        <input class="easyui-datetimebox" id="idcoatInprotime" type="text" name="idcoatInprotime" value="" data-options="formatter:myformatter2,parser:myparser2"/>
                    </td>

                </tr>
            </table>

            <table class="ht-table">
                <tr>
                    <td class="i18n1" name="dryfilmthicknessmax">最大干膜厚度</td>
                    <td><input class="easyui-numberbox" data-options="min:0,precision:2" type="text" name="dry_film_thickness_max" value=""/></td>
                    <td>10~20</td>
                    <td class="i18n1" name="dryfilmthicknessmin">最小干膜厚度</td>
                    <td><input class="easyui-numberbox"  data-options="min:0,precision:2" type="text" name="dry_film_thickness_min" value=""/></td>
                    <td></td>
                </tr>
                <tr>
                    <td width="16%" class="i18n1" name="holidaytestervolts">电火花检测电压</td>
                    <td><input class="easyui-numberbox" data-options="min:0,precision:2" type="text" name="holiday_tester_volts" value=""/></td>
                    <td></td>
                    <td width="16%" class="i18n1" name="holidaytestresults">电火花检测结果</td>
                    <td><input class="easyui-numberbox" data-options="min:0,precision:0"  type="text" name="holiday_test_results" value=""/></td>
                    <td></td>
                </tr>
                <tr>
                    <td width="16%" class="i18n1" name="cutback">预留端长度</td>
                    <td><input class="easyui-numberbox" data-options="min:0,precision:2" type="text" name="cutback" value=""/></td>
                    <td></td>
                    <td width="16%" class="i18n1" name="bevelcheck">坡口质量</td>
                    <td><input class="easyui-textbox"  type="text" name="bevel_check" value=""/></td>
                    <td></td>

                </tr>

                <tr>
                    <td class="i18n1" name="magnetism">剩磁</td>
                    <td><input class="easyui-numberbox" data-options="min:0,precision:2" type="text" name="magnetism" value=""/></td>
                    <td></td>
                    <td class="i18n1" name="internalrepairs">内涂层修补数</td>
                    <td><input class="easyui-numberbox" data-options="min:0,precision:0" type="text" name="internal_repairs" value=""/></td>
                    <td></td>
                </tr>
                <tr>
                    <td width="16%" class="i18n1" name="surfacecondition1">表面质量</td>
                    <td colspan="2">
                        <div id="combobox1" class="mini-combobox" style="width:185px;"  popupWidth="185" textField="text" valueField="text"
                             url="../data/surfacequality.txt" name="surface_condition" multiSelect="true"  showClose="true" oncloseclick="onComboxCloseClick" >
                            <div property="columns">
                                <div header="缺陷类型" field="text"></div>
                            </div>
                        </div>
                    </td>

                    <td width="16%" class="i18n1" name="issample">取样管</td>
                    <td colspan="2">
                        <input type="checkbox" id="is-sample" value="0" checked="false" onchange="selectIsSample()"/>
                        <input type="hidden" name="is_sample" value="0">
                    </td>
                </tr>
                <tr>
                    <td width="16%" class="i18n1" name="result">结论</td>
                    <td><select id="cc" class="easyui-combobox" data-options="editable:false" name="result" style="width:200px;">
                        <option value="0">不合格,进入待修补工序</option>
                        <option value="1">合格,进入内喷标工序</option>
                        <option value="2">不合格,进入待扒皮工序</option>
                        <option value="3">待定</option>
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
    <a href="#" class="easyui-linkbutton" iconCls="icon-save" onclick="idCoatingInProFormSubmit()">Save</a>
    <a href="#" class="easyui-linkbutton" id="hlcancelBtn" operationtype="add" iconCls="icon-cancel" onclick="idCoatingInProCancelSubmit()">Cancel</a>
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
    var combox1=mini.get("combobox1");


    function onSearchClick(type) {
        if(type==1)
        {
            grid1.load({
                pipe_no:keyText1.value,
                pipestatus:'id3,'
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
            pipestatus:'id3,'
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
    combox1.on("showpopup",function () {
        $('.mini-shadow').css('z-index','99999');
        $('.mini-popup').css('z-index','100000');
        $('.mini-panel').css('z-index','100000');
    });
    function onComboxCloseClick(e) {
        var obj = e.sender;
        obj.setText("");
        obj.setValue("");
    }
    hlLanguage("../i18n/");
</script>