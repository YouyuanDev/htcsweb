<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
    String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>涂层（3LPE）实验信息</title>
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
            $('#hlLabtest3LpeDialog').dialog({
                onClose:function () {
                    var type=$('#hlcancelBtn').attr('operationtype');
                    if(type=="add"){
                        var $imglist=$('#fileslist');
                        var $dialog=$('#hlLabtest3LpeDialog');
                        hlAlertSix("../UploadFile/delUploadPicture.action",$imglist,$dialog,grid);
                    }
                    clearFormLabel();
                }
            });
            $('.mini-buttonedit .mini-buttonedit-input').css('width','150px');
            // hlLanguage("../i18n/");
        });
        function addLabtest3LpePro(){
            $('#hlcancelBtn').attr('operationtype','add');
            $('#hlLabtest3LpeDialog').dialog('open').dialog('setTitle','新增');
            clearFormLabel();
            clearMultiUpload(grid);
            url="/LabTest3LpeOperation/saveLabTest3Lpe.action";
            //$("input[name='alkaline_dwell_time']").siblings().css("background-color","#F9A6A6");
        }
        function delLabtest3LpePro() {
            var row = $('#Labtest3LpeDatagrids').datagrid('getSelections');
            if(row.length>0){
                var idArr=[];
                for (var i=0;i<row.length;i++){
                    idArr.push(row[i].id);
                }
                var idArrs=idArr.join(',');
                $.messager.confirm('系统提示',"您确定要删除这<font color=red>"+idArr.length+ "</font>条数据吗？",function (r) {
                    if(r){
                        $.post("/LabTest3LpeOperation/delLabTest3Lpe.action",{"hlparam":idArrs},function (data) {
                            if(data.success){
                                $("#Labtest3LpeDatagrids").datagrid("reload");
                            }
                            hlAlertFour(data.message);
                        },"json");
                    }
                });
            }else{
                hlAlertOne();
            }
        }
        function editLabtest3LpePro(){
            $('#hlcancelBtn').attr('operationtype','edit');
            var row = $('#Labtest3LpeDatagrids').datagrid('getSelected');
            if(row){
                $('#hlLabtest3LpeDialog').dialog('open').dialog('setTitle','修改');
                loadPipeBaiscInfo(row);
                $('#odbpid').text(row.id);
                //$('#Labtest3LpeForm').form('load',row);
                $('#Labtest3LpeForm').form('load', {
                    'mill_no': row.mill_no,
                    'sample_no': row.sample_no,
                    'coating_date': getDate1(row.coating_date),
                    'resistance_to_cd_20_28d': row.resistance_to_cd_20_28d,
                    'resistance_to_cd_max_28d': row.resistance_to_cd_max_28d,
                    'resistance_to_cd_65_24h': row.resistance_to_cd_65_24h,
                    'impact_resistance_23': row.impact_resistance_23,
                    'impact_resistance_m40': row.impact_resistance_m40,
                    'indentation_hardness_23': row.indentation_hardness_23,
                    'indentation_hardness_70': row.indentation_hardness_70,
                    'elongation_at_break': row.elongation_at_break,
                    'coating_resistivity': row.coating_resistivity,
                    'thermal_degradation': row.thermal_degradation,
                    'operator_no':row.operator_no,
                    'operation_time':getDate1(row.operation_time),
                    'upload_files':row.upload_files,
                    'result':row.result,
                    'remark':row.remark,
                    'dsc':row.dsc,
                    'dsc_pipe_no':row.dsc_pipe_no,
                    'dsc_sample_no':row.dsc_sample_no,
                    'pe_pipe_no':row.pe_pipe_no,
                    'pe_sample_no':row.pe_sample_no
                });

                // $('#coating-date').datetimebox('setValue',getDate1(row.coating_date));
                // $('#operation-time').datetimebox('setValue',getDate1(row.operation_time));
                look1.setText(row.pipe_no);
                look1.setValue(row.pipe_no);
                look2.setText(row.operator_no);
                look2.setValue(row.operator_no);
                var odpictures=row.upload_files;
                if(odpictures!=null&&odpictures!=""){
                    var imgList=odpictures.split(';');
                    createPictureModel(basePath,imgList);
                }
                //异步获取标准并匹配
                $.ajax({
                    url:'/LabTestingAcceptanceCriteriaOperation/getAcceptanceCriteria3LpeByContractNo.action',
                    dataType:'json',
                    data:{'contract_no':row.contract_no},
                    success:function (data) {
                        var $obj1=$("input[name='resistance_to_cd_20_28d']");
                        var $obj2=$("input[name='resistance_to_cd_max_28d']");
                        var $obj3=$("input[name='resistance_to_cd_65_24h']");
                        var $obj4=$("input[name='impact_resistance_23']");
                        var $obj5=$("input[name='impact_resistance_m40']");
                        var $obj6=$("input[name='indentation_hardness_23']");
                        var $obj7=$("input[name='indentation_hardness_70']");
                        var $obj8=$("input[name='elongation_at_break']");
                        var $obj9=$("input[name='coating_resistivity']");
                        var $obj10=$("input[name='thermal_degradation']");
                        $obj1.siblings().css("background-color","#FFFFFF");
                        $obj2.siblings().css("background-color","#FFFFFF");
                        $obj3.siblings().css("background-color","#FFFFFF");
                        $obj4.siblings().css("background-color","#FFFFFF");
                        $obj5.siblings().css("background-color","#FFFFFF");
                        $obj6.siblings().css("background-color","#FFFFFF");
                        $obj7.siblings().css("background-color","#FFFFFF");
                        $obj8.siblings().css("background-color","#FFFFFF");
                        $obj9.siblings().css("background-color","#FFFFFF");
                        $obj10.siblings().css("background-color","#FFFFFF");
                        if(data!=null){
                            var res1=$obj1.val();
                            var res2=$obj2.val();
                            var res3=$obj3.val();
                            var res4=$obj4.val();
                            var res5=$obj5.val();
                            var res6=$obj6.val();
                            var res7=$obj7.val();
                            var res8=$obj8.val();
                            var res9=$obj9.val();
                            var res10=$obj10.val();
                            if(!((res1>=data.resistance_to_cd_20_28d_min)&&(res1<=data.resistance_to_cd_20_28d_max)))
                                $obj1.siblings().css("background-color","#F9A6A6");
                            if(!((res2>=data.resistance_to_cd_max_28d_min)&&(res2<=data.resistance_to_cd_max_28d_max)))
                                $obj2.siblings().css("background-color","#F9A6A6");
                            if(!((res3>=data.resistance_to_cd_65_24h_min)&&(res3<=data.resistance_to_cd_65_24h_max)))
                                $obj3.siblings().css("background-color","#F9A6A6");
                            if(!((res4>=data.impact_resistance_23_min)&&(res4<=data.impact_resistance_23_max)))
                                $obj4.siblings().css("background-color","#F9A6A6");
                            if(!((res5>=data.impact_resistance_m40_min)&&(res5<=data.impact_resistance_m40_max)))
                                $obj5.siblings().css("background-color","#F9A6A6");
                            if(!((res6>=data.indentation_hardness_23_min)&&(res6<=data.indentation_hardness_23_max)))
                                $obj6.siblings().css("background-color","#F9A6A6");
                            if(!((res7>=data.indentation_hardness_70_min)&&(res7<=data.indentation_hardness_70_max)))
                                $obj7.siblings().css("background-color","#F9A6A6");
                            if(!((res8>=data.elongation_at_break_min)&&(res8<=data.elongation_at_break_max)))
                                $obj8.siblings().css("background-color","#F9A6A6");
                            if(!((res9>=data.coating_resistivity_min)&&(res9<=data.coating_resistivity_max)))
                                $obj9.siblings().css("background-color","#F9A6A6");
                            if(!((res10>=data.thermal_degradation_min)&&(res10<=data.thermal_degradation_max)))
                                $obj10.siblings().css("background-color","#F9A6A6");
                        }
                    },error:function () {

                    }
                });
                url="/LabTest3LpeOperation/saveLabTest3Lpe.action?id="+row.id;
            }else{
                hlAlertTwo();
            }
        }
        function searchLabtest3LpePro() {
            $('#Labtest3LpeDatagrids').datagrid('load',{
                'pipe_no': $('#pipeno').val(),
                'operator_no': $('#operatorno').val(),
                'begin_time': $('#begintime').val(),
                'end_time': $('#endtime').val(),
                'mill_no': $('#millno').val()
            });
        }
        function Labtest3LpeFormSubmit() {
            $('#Labtest3LpeForm').form('submit',{
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
                    if($("input[name='operation_time']").val()==""){
                        hlAlertFour("请输入操作时间");return false;
                    }
                    if($("input[name='coating_date']").val()==""){
                        hlAlertFour("请输入涂层时间");return false;
                    }
                    setParams($("input[name='resistance_to_cd_20_28d']"));
                    setParams($("input[name='resistance_to_cd_max_28d']"));
                    setParams($("input[name='resistance_to_cd_65_24h']"));
                    setParams($("input[name='impact_resistance_23']"));
                    setParams($("input[name='impact_resistance_m40']"));
                    setParams($("input[name='indentation_hardness_23']"));
                    setParams($("input[name='indentation_hardness_70']"));
                    setParams($("input[name='elongation_at_break']"));
                    setParams($("input[name='coating_resistivity']"));
                    setParams($("input[name='thermal_degradation']"));

                },
                success: function(result){
                    clearFormLabel();
                    var result = eval('('+result+')');
                    $('#hlLabtest3LpeDialog').dialog('close');
                    if (result.success){
                        $('#Labtest3LpeDatagrids').datagrid('reload');
                    }
                    hlAlertFour(result.message);
                },
                error:function () {
                    //clearFormLabel();
                    hlAlertThree();
                }
            });
            clearMultiUpload(grid);
        }
        function Labtest3LpeCancelSubmit() {
            $('#hlLabtest3LpeDialog').dialog('close');
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

        function  clearFormLabel(){
            $('#Labtest3LpeForm').form('clear');
            $('.hl-label').text('');
            $('#hl-gallery-con').empty();
            $(":input").each(function () {
                $(this).siblings().css("background-color","#FFFFFF");
            });
        }
    </script>





</head>

<body>
<fieldset class="b3" style="padding:10px;margin:10px;">
    <legend> <h3><b style="color: orange" >|&nbsp;</b><span class="i18n1" name="datadisplay">数据展示</span></h3></legend>
    <div  style="margin-top:5px;">
        <table class="easyui-datagrid" id="Labtest3LpeDatagrids" url="/LabTest3LpeOperation/getLabTest3LpeByLike.action" striped="true" loadMsg="正在加载中。。。" textField="text" pageSize="20" fitColumns="true" pagination="true" toolbar="#hlLabtest3LpeProTb">
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

                <th field="sample_no" align="center" width="120" class="i18n1" name="sampleno">试样号</th>
                <th field="coating_date" align="center" width="120" class="i18n1" name="coatingdate" data-options="formatter:formatterdate">涂层时间</th>

                <th field="resistance_to_cd_20_28d" align="center" width="100" hidden="true" class="i18n1" name="resistancetocd2028d">阴极剥离 20度 28天</th>
                <th field="resistance_to_cd_max_28d" align="center" width="100" hidden="true" class="i18n1" name="resistancetocdmax28d">阴极剥离 最高温度 28天</th>
                <th field="resistance_to_cd_65_24h" width="100" align="center" hidden="true" class="i18n1" name="resistancetocd6524h">阴极剥离 65度 24小时</th>
                <th field="impact_resistance_23" width="100" align="center" hidden="true" class="i18n1" name="impactresistance23">冲击 23度</th>
                <th field="impact_resistance_m40" width="100" align="center" hidden="true" class="i18n1" name="impactresistancem40">冲击 -40度</th>
                <th field="indentation_hardness_23" align="center" width="120" class="i18n1" name="indentationhardness23">压痕硬度 23度</th>
                <th field="indentation_hardness_70" align="center" width="120" class="i18n1" name="indentationhardness70">压痕硬度 70度</th>
                <th field="elongation_at_break" align="center" width="120" class="i18n1" name="elongationatbreak">延展率</th>
                <th field="coating_resistivity" align="center" width="120" class="i18n1" name="coatingresistivity">涂层强度</th>
                <th field="thermal_degradation" align="center" width="120" class="i18n1" name="thermaldegradation">热降解</th>
                <th field="dsc" align="center" width="120" class="i18n1" name="dsc"></th>
                <th field="dsc_pipe_no" align="center" width="120" class="i18n1" name="dscpipeno"></th>
                <th field="dsc_sample_no" align="center" width="120" class="i18n1" name="dscsampleno"></th>
                <th field="pe_pipe_no" align="center" width="120" class="i18n1" name="pepipeno"></th>
                <th field="pe_sample_no" align="center" width="120" class="i18n1" name="pesampleno"></th>


                <th field="remark" align="center" width="150" class="i18n1" name="remark">备注</th>
                <th field="result" align="center" width="150" class="i18n1" name="result">结论</th>
                <th field="operation_time" align="center" width="150" class="i18n1" name="operationtime" data-options="formatter:formatterdate">操作时间</th>
            </tr>
            </thead>
        </table>

    </div>
</fieldset>

<!--工具栏-->
<div id="hlLabtest3LpeProTb" style="padding:10px;">
    <span class="i18n1" name="pipeno">钢管编号</span>:
    <input id="pipeno" name="pipeno" style="line-height:22px;border:1px solid #ccc">
    <span class="i18n1" name="operatorno">操作工编号</span>:
    <input id="operatorno" name="operatorno" style="line-height:22px;border:1px solid #ccc">
    <span class="i18n1" name="begintime">开始时间</span>:
    <input id="begintime" name="begintime" type="text" class="easyui-datebox" data-options="formatter:myformatter,parser:myparser">
    <span class="i18n1" name="endtime">结束时间</span>:
    <input id="endtime" name="endtime" type="text" class="easyui-datebox" data-options="formatter:myformatter,parser:myparser">
    <a href="#" class="easyui-linkbutton" plain="true" data-options="iconCls:'icon-search'" onclick="searchLabtest3LpePro()">Search</a>
    <div style="float:right">
        <a href="#" id="addObpLinkBtn" class="easyui-linkbutton i18n1" name="add" data-options="iconCls:'icon-add',plain:true" onclick="addLabtest3LpePro()">添加</a>
        <a href="#" id="editObpLinkBtn" class="easyui-linkbutton i18n1" name="edit" data-options="iconCls:'icon-edit',plain:true" onclick="editLabtest3LpePro()">修改</a>
        <a href="#" id="deltObpLinkBtn" class="easyui-linkbutton i18n1" name="delete" data-options="iconCls:'icon-remove',plain:true" onclick="delLabtest3LpePro()">删除</a>
    </div>
</div>

<!--添加、修改框-->
<div id="hlLabtest3LpeDialog" class="easyui-dialog" data-options="title:'添加',modal:true"  closed="true" buttons="#dlg-buttons" style="display:none;padding:5px;width:950px;max-height:600px;overflow-y: scroll;">
    <form id="Labtest3LpeForm" method="post">
        <fieldset style="width:900px;border:solid 1px #aaa;margin-top:8px;position:relative;">
            <legend class="i18n1" name="pipebasicinfo">钢管信息</legend>
            <table class="ht-table">
                <tr>
                    <td class="i18n1" name="id" width="20%">流水号</td>
                    <td colspan="5" width="30%"><label class="hl-label" id="odbpid"></label></td>
                </tr>
                <tr>
                    <td class="i18n1" name="operatorno" width="20%">操作工编号</td>
                    <td colspan="1" width="30%">
                        <input id="lookup2" name="operator_no" class="mini-lookup" style="text-align:center;width:180px;"
                               textField="employee_no" valueField="id" popupWidth="auto"
                               popup="#gridPanel2" grid="#datagrid2" multiSelect="false"
                        />
                    </td>

                    <td class="i18n1" name="operationtime">操作时间</td>
                    <td>
                        <input class="easyui-datetimebox" type="text" name="operation_time" value="" data-options="formatter:myformatter2,parser:myparser2"/>

                    </td>
                </tr>
            </table>

        </fieldset>
        <fieldset style="width:900px;border:solid 1px #aaa;margin-top:8px;position:relative;">
            <legend>外防实验(3LPE)信息(Dsc取样)</legend>
            <table class="ht-table" width="100%" border="0">
                <tr>
                    <td class="i18n1" name="dscpipeno"></td>
                    <td><input class="easyui-textbox" type="text" name="dsc_pipe_no" value=""/></td>
                    <td></td>
                    <td class="i18n1" name="dscsampleno"></td>
                    <td><input class="easyui-textbox" type="text" name="dsc_sample_no" value=""/></td>
                    <td></td>
                </tr>
                <tr>
                    <td class="i18n1" name="dsc">热特性实验</td>
                    <td><input class="easyui-textbox"  onchange="ceshi()"  type="text" name="dsc" value=""/></td>
                    <td></td>
                </tr>
            </table>
        </fieldset>
        <fieldset style="width:900px;border:solid 1px #aaa;margin-top:8px;position:relative;">
            <legend>外防实验(3LPE)信息(PE取样)</legend>
            <table class="ht-table" width="100%" border="0">
                <tr>
                    <td class="i18n1" name="pepipeno"></td>
                    <td><input class="easyui-textbox" type="text" name="pe_pipe_no" value=""/></td>
                    <td></td>
                    <td class="i18n1" name="pesampleno"></td>
                    <td><input class="easyui-textbox" type="text" name="pe_sample_no" value=""/></td>
                    <td></td>
                </tr>
                <tr>
                    <td class="i18n1" name="indentationhardness23">压痕硬度 23度</td>
                    <td><input class="easyui-numberbox" data-options="min:0,precision:2" type="text" name="indentation_hardness_23" value=""/></td>
                    <td></td>
                    <td class="i18n1" name="indentationhardness70">压痕硬度 70度</td>
                    <td><input class="easyui-numberbox" data-options="min:0,precision:2" type="text" name="indentation_hardness_70" value=""/></td>
                    <td></td>
                </tr>
                <tr>
                    <td class="i18n1" name="elongationatbreak">延展率</td>
                    <td><input class="easyui-numberbox" data-options="min:0,precision:2" type="text" name="elongation_at_break" value=""/></td>
                    <td></td>
                    <td class="i18n1" name="coatingresistivity">涂层强度</td>
                    <td><input class="easyui-numberbox" data-options="min:0,precision:2" type="text" name="coating_resistivity" value=""/></td>
                    <td></td>
                </tr>
            </table>
        </fieldset>
        <fieldset style="width:900px;border:solid 1px #aaa;margin-top:8px;position:relative;">
            <legend>外防实验(3LPE)信息(常规取样)</legend>

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
                        <input  id="lookup1" name="pipe_no" class="mini-lookup" style="text-align:center;width:180px;"
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
            <table class="ht-table">
                <tr>
                    <td class="i18n1" name="sampleno">试样号</td>
                    <td>
                        <input class="easyui-textbox"   type="text" name="sample_no" value=""/>
                    </td>
                    <td></td>
                    <td class="i18n1" name="coatingdate" width="20%">涂层时间</td>
                    <td colspan="1" width="30%">
                        <input class="easyui-datetimebox" type="text" name="coating_date" value="" data-options="formatter:myformatter2,parser:myparser2"/>

                    </td>
                    <td></td>
                </tr>
                <tr>
                    <td class="i18n1" name="resistancetocd2028d">阴极剥离 20度 28天</td>
                    <td><input class="easyui-numberbox hl-errorcolor" data-options="min:0,precision:2" type="text" name="resistance_to_cd_20_28d" value=""/></td>
                    <td></td>
                    <td class="i18n1" name="resistancetocdmax28d">阴极剥离 最高温度 28天</td>
                    <td><input class="easyui-numberbox"  data-options="min:0,precision:2" type="text" name="resistance_to_cd_max_28d" value=""/></td>
                    <td></td>
                </tr>
                <tr>
                    <td class="i18n1" name="resistancetocd6524h">阴极剥离 65度 24小时</td>
                    <td>
                        <input class="easyui-numberbox"  data-options="min:0,precision:2" type="text" name="resistance_to_cd_65_24h" value=""/></td>
                    <td></td>
                    <td class="i18n1" name="thermaldegradation">热降解</td>
                    <td><input class="easyui-numberbox" data-options="min:0,precision:2" type="text" name="thermal_degradation" value=""/></td>
                    <td></td>
                </tr>

                <tr>
                    <td width="16%" class="i18n1" name="impactresistance23">冲击 23度</td>
                    <td>
                        <input class="easyui-numberbox" data-options="min:0,precision:2" type="text" name="impact_resistance_23" value=""/>
                    </td>
                    <td></td>
                    <td width="16%" class="i18n1" name="impactresistancem40">冲击 -40度</td>
                    <td>
                        <input class="easyui-numberbox" data-options="min:0,precision:2" type="text" name="impact_resistance_m40" value=""/>
                    </td>
                    <td></td>
                </tr>


                <tr>
                    <td width="16%" class="i18n1" name="remark">备注</td>
                    <td colspan="5"><input class="easyui-textbox" type="text" value="" name="remark" data-options="multiline:true" style="height:60px"/></td>
                </tr>
                <tr>
                    <td width="16%" class="i18n1" name="result">结论</td>
                    <td><select id="cc" class="easyui-combobox" data-options="editable:false" name="result" style="width:200px;">
                        <option value="0">不合格,复验</option>
                        <option value="1">合格</option>
                        <option value="10">待定</option>
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
    <a href="#" class="easyui-linkbutton" iconCls="icon-save" onclick="Labtest3LpeFormSubmit()">Save</a>
    <a href="#" class="easyui-linkbutton" id="hlcancelBtn" operationtype="add" iconCls="icon-cancel" onclick="Labtest3LpeCancelSubmit()">Cancel</a>
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
         url="/pipeinfo/get3LPESamplePipeNo.action">
        <div property="columns">
            <div type="checkcolumn" ></div>
            <div field="pipe_no" width="80" headerAlign="center" allowSort="true" class="i18n1" name="pipeno">钢管编号</div>
            <div field="contract_no" width="80" headerAlign="center" allowSort="true" class="i18n1" name="contractno">合同编号</div>
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
                pipe_no:keyText1.value
                //pipestatus:'bare1,'
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
            url:'../pipeinfo/get3LPESamplePipeNo.action',
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
            pipestatus:'bare1,'
        });
        //$('.mini-buttonedit .mini-buttonedit-input').css('width','150px');
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
        //$('.mini-buttonedit .mini-buttonedit-input').css('width','150px');
    });
    hlLanguage("../i18n/");
</script>