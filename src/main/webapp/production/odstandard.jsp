<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
    String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>外喷砂信息</title>
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
        $(function () {
                $('#hlOdBlastProDialog').dialog({
                    onClose:function () {
                        clearFormLabel();
                    }
                });
               $('.mini-buttonedit .mini-buttonedit-input').css('width','150px');
               // hlLanguage("../i18n/");
        });
        function addOdAcceptance(){
            $('#hlcancelBtn').attr('operationtype','add');
            $('#hlOdAcceptanceDialog').dialog('open').dialog('setTitle','新增');
            $('#odacceptanceId').text('');
            clearFormLabel(); $(':input').val('1');
            url="/AcceptanceCriteriaOperation/saveAllODAcceptanceCriteria.action";
        }
        function delOdAcceptance() {
            var row = $('#odAcceptanceDatagrids').datagrid('getSelections');
            if(row.length>0){
                var idArr=[];
                for (var i=0;i<row.length;i++){
                    idArr.push(row[i].id);
                }
                var idArrs=idArr.join(',');
                $.messager.confirm('系统提示',"您确定要删除这<font color=red>"+idArr.length+ "</font>条数据吗？",function (r) {
                    if(r){
                        $.post("/AcceptanceCriteriaOperation/delAllODAcceptanceCriteria.action",{"hlparam":idArrs},function (data) {
                            if(data.success){
                                $("#odAcceptanceDatagrids").datagrid("reload");
                            }else{
                                hlAlertFour("操作失败!");
                            }
                        },"json");
                    }
                });
            }else{
                hlAlertOne();
            }
        }
        function editOdAcceptance(){
            $('#hlcancelBtn').attr('operationtype','edit');
            var row = $('#odAcceptanceDatagrids').datagrid('getSelected');
            $(':input').val(1);
            if(row){
                $('#hlOdAcceptanceDialog').dialog('open').dialog('setTitle','修改');
                $("#odacceptanceId").textbox("setValue", row.id);
                url="/AcceptanceCriteriaOperation/saveAllODAcceptanceCriteria.action?id="+row.id;
            }else{
                hlAlertTwo();
            }
        }
        function searchOdAcceptance() {
            $('#odAcceptanceDatagrids').datagrid('load',{
                'coating_acceptance_criteria_no': $('#coating_acceptance_criteria_no').val()
            });
        }
        function odAcceptanceFormSubmit() {
            $('#odAcceptanceForm').form('submit',{
                url:url,
                onSubmit:function () {
                    //表单验证
                    // setParams($("input[name='alkaline_dwell_time']"));
                    // setParams($("input[name='alkaline_concentration']"));
                    // setParams($("input[name='acid_wash_time']"));
                    // setParams($("input[name='acid_concentration']"));
                    // setParams($("input[name='salt_contamination_before_blasting']"));
                    // setParams($("input[name='blast_line_speed']"));
                    // setParams($("input[name='conductivity']"));
                    // setParams($("input[name='preheat_temp']"));
                    // if($("input[name='odbptime']").val()==""){
                    //     hlAlertFour("请输入操作时间");
                    //     return false;
                    // }
                    alert("neirong"+$("input[name='coating_acceptance_criteria_no']").val());
                },
                success: function(result){
                    var result = eval('('+result+')');
                    $('#hlOdAcceptanceDialog').dialog('close');
                    if (result.success){
                        $('#odAcceptanceDatagrids').datagrid('reload');
                    } else {
                         hlAlertFour("操作失败!");
                    }
                },
                error:function () {
                    //clearFormLabel();
                    hlAlertThree();
                }
            });
            //clearMultiUpload(grid);

        }
        function odAcceptanceCancelSubmit() {
            $('#hlOdAcceptanceDialog').dialog('close');
        }

        function  setParams($obj) {
            if($obj.val()==null||$obj.val()=="")
                $obj.val(0);
        }
        function  clearFormLabel(){
            $('#odAcceptanceForm').form('clear');
            $('.hl-label').text('');
            //$('#hl-gallery-con').empty();
            //combox1.setValue("");
        }
    </script>





</head>

<body>
<fieldset class="b3" style="padding:10px;margin:10px;">
    <legend> <h3><b style="color: orange" >|&nbsp;</b><span class="i18n1" name="datadisplay">数据展示</span></h3></legend>
    <div  style="margin-top:5px;">
         <table class="easyui-datagrid" id="odAcceptanceDatagrids" url="/AcceptanceCriteriaOperation/getAllODAcceptanceCriteriaByLike.action" striped="true" loadMsg="正在加载中。。。" textField="text" pageSize="20" fitColumns="true" pagination="true" toolbar="#hlOdAcceptanceTb">
             <thead>
               <tr>
                   <th data-options="field:'ck',checkbox:true"></th>
                   <th field="id" align="center" width="100" class="i18n1" name="id">流水号</th>
                   <th field="coating_acceptance_criteria_no" align="center" width="150" class="i18n1" name="coatingacceptancecriteriano">涂层判定标准编号</th>
                   <th field="salt_contamination_before_blast_max" align="center" width="150" class="i18n1" name="saltcontaminationbeforeblastmax">打砂前盐度最大值</th>
                   <th field="salt_contamination_before_blast_min" align="center" width="150" class="i18n1" name="saltcontaminationbeforeblastmin">打砂前盐度最小值</th>
                   <th field="preheat_temp_max" align="center" width="150" class="i18n1" name="preheattempmax">预热钢管温度最大值</th>
                   <th field="preheat_temp_min" align="center" width="150" class="i18n1" name="preheattempmin">预热钢管温度最小值</th>
                   <th field="relative_humidity_max" align="center" width="150" class="i18n1" name="relativehumiditymax">相对湿度最大值</th>
                   <th field="relative_humidity_min" align="center" width="150" class="i18n1" name="relativehumiditymin">相对湿度最小值</th>
                   <th field="temp_above_dew_point_max" align="center" width="150" class="i18n1" name="tempabovedewpointmax">大于露点最大值</th>
                   <th field="temp_above_dew_point_min" align="center" width="150" class="i18n1" name="tempabovedewpointmin">大于露点最小值</th>
                   <th field="blast_finish_sa25_max" align="center" hidden="true" width="50" class="i18n1" name="blastfinishsa25max">打砂后清洁度Sa2.5最大值</th>
                   <th field="blast_finish_sa25_min" align="center" hidden="true" width="100" class="i18n1" name="blastfinishsa25min">打砂后清洁度Sa2.5最小值</th>
                   <th field="surface_dust_rating_max" align="center" hidden="true" width="100" class="i18n1" name="surfacedustratingmax">灰尘度最大值</th>
                   <th field="surface_dust_rating_min" align="center" hidden="true" width="120" class="i18n1" name="surfacedustratingmin">灰尘度最小值</th>
                   <th field="od_profile_max" align="center" width="100" hidden="true" class="i18n1" name="odprofilemax">外锚纹深度最大值</th>
                   <th field="od_profile_min" align="center" width="100" hidden="true" class="i18n1" name="odprofilemin">外锚纹深度最小值</th>
                   <th field="pipe_temp_after_blast_max" width="100" hidden="true" align="center"  class="i18n1" name="pipetempafterblastmax">打砂后钢管温度最大值</th>
                   <th field="pipe_temp_after_blast_min" width="100" hidden="true" align="center"  class="i18n1" name="pipetempafterblastmin">打砂后钢管温度最小值</th>
                   <th field="salt_contamination_after_blasting_max" hidden="true" width="100" align="center"  class="i18n1" name="saltcontaminationafterblastingmax">打砂后盐度最大值 </th>
                   <th field="salt_contamination_after_blasting_min" hidden="true" align="center" width="100" class="i18n1" name="saltcontaminationafterblastingmin">打砂后盐度最小值 </th>
                   <th field="application_temp_max" align="center" hidden="true" width="120" class="i18n1" name="applicationtempmax">中频温度最大值</th>
                   <th field="application_temp_min" align="center" hidden="true" width="100" class="i18n1" name="applicationtempmin">中频温度最小值</th>
                   <th field="base_2fbe_coat_thickness_max" hidden="true" align="center" width="100" class="i18n1" name="base2fbecoatthicknessmax">底层2fbe最大厚度</th>
                   <th field="base_2fbe_coat_thickness_min" hidden="true" align="center" width="100" class="i18n1" name="base2fbecoatthicknessmin">底层2fbe最小厚度</th>

                   <th field="top_2fbe_coat_thickness_max" align="center" width="150" hidden="true" class="i18n1" name="top2fbecoatthicknessmax">顶层2fbe最大厚度</th>
                   <th field="top_2fbe_coat_thickness_min" align="center" width="150" hidden="true" class="i18n1" name="top2fbecoatthicknessmin">顶层2fbe最小厚度</th>
                   <th field="total_2fbe_coat_thickness_max" align="center" width="150" hidden="true" class="i18n1" name="total2fbecoatthicknessmax">2fbe总厚度最大值</th>
                   <th field="total_2fbe_coat_thickness_min" align="center" width="150" hidden="true" class="i18n1" name="total2fbecoatthicknessmin">2fbe总厚度最小值</th>
                   <th field="top_3lpe_coat_thickness_max" align="center" width="150" hidden="true" class="i18n1" name="top3lpecoatthicknessmax">3lpe顶层最大值</th>
                   <th field="top_3lpe_coat_thickness_min" align="center" width="150" hidden="true" class="i18n1" name="top3lpecoatthicknessmin">3lpe顶层最小值</th>
                   <th field="middle_3lpe_coat_thickness_max" align="center" width="150" hidden="true" class="i18n1" name="middle3lpecoatthicknessmax">3lpe中间层最大值</th>
                   <th field="middle_3lpe_coat_thickness_min" align="center" width="150" hidden="true" class="i18n1" name="middle3lpecoatthicknessmin">3lpe中间层最小值</th>
                   <th field="base_3lpe_coat_thickness_max" align="center" width="150" hidden="true" class="i18n1" name="base3lpecoatthicknessmax">3lpe底层最大值</th>
                   <th field="base_3lpe_coat_thickness_min" align="center" width="150" hidden="true" class="i18n1" name="base3lpecoatthicknessmin">3lpe底层最小值</th>
                   <th field="total_3lpe_coat_thickness_max" align="center" width="150" hidden="true" class="i18n1" name="total3lpecoatthicknessmax">3lpe总厚度最大值</th>
                   <th field="total_3lpe_coat_thickness_min" align="center" width="150" hidden="true" class="i18n1" name="total3lpecoatthicknessmin">3lpe总厚度最小值</th>
                   <th field="repair_max" align="center" width="150" hidden="true" class="i18n1" name="repairmax">允许修补量最大值</th>
                   <th field="repair_min" align="center" width="150" hidden="true" class="i18n1" name="repairmin">允许修补量最小值</th>
                   <th field="holiday_tester_voltage_max" align="center" width="150" hidden="true" class="i18n1" name="holidaytestervoltagemax">检漏仪电压最大值</th>
                   <th field="holiday_tester_voltage_min" align="center" width="150" hidden="true" class="i18n1" name="holidaytestervoltagemin">检漏仪电压最小值</th>
                   <th field="cutback_max" align="center" width="150" hidden="true" class="i18n1" name="cutbackmax">预留段最大值</th>
                   <th field="cutback_min" align="center" width="150" hidden="true" class="i18n1" name="cutbackmin">预留段最小值</th>
               </tr>
             </thead>
         </table>
    </div>
</fieldset>

<!--工具栏-->
<div id="hlOdAcceptanceTb" style="padding:10px;">
    <span class="i18n1" name="coatingacceptancecriteriano">涂层判定标准编号</span>:
    <input id="coating_acceptance_criteria_no" name="coating_acceptance_criteria_no" style="line-height:22px;border:1px solid #ccc">
    <a href="#" class="easyui-linkbutton" plain="true" data-options="iconCls:'icon-search'" onclick="searchOdAcceptance()">Search</a>
    <div style="float:right">
     <a href="#" id="addObpLinkBtn" class="easyui-linkbutton i18n1" name="add" data-options="iconCls:'icon-add',plain:true" onclick="addOdAcceptance()">添加</a>
     <a href="#" id="editObpLinkBtn" class="easyui-linkbutton i18n1" name="edit" data-options="iconCls:'icon-edit',plain:true" onclick="editOdAcceptance()">修改</a>
     <a href="#" id="deltObpLinkBtn" class="easyui-linkbutton i18n1" name="delete" data-options="iconCls:'icon-remove',plain:true" onclick="delOdAcceptance()">删除</a>
    </div>
</div>

<!--添加、修改框-->
<div id="hlOdAcceptanceDialog" class="easyui-dialog" data-options="title:'添加',modal:true"  closed="true" buttons="#dlg-buttons" style="display: none;padding:5px;width:950px;height:auto;">
   <form id="odAcceptanceForm" method="post">
       <fieldset style="width:900px;border:solid 1px #aaa;margin-top:8px;position:relative;">
           <legend>接收标准信息</legend>
           <table class="ht-table" width="100%" border="0">
               <tr>
                   <td class="i18n1" name="id">流水号</td>
                   <td colspan="2"><input id="odacceptanceId" class="easyui-textbox" readonly="true" type="text" value="" name="odacceptanceId"> </td>
                   <td class="i18n1" name="coatingacceptancecriteriano">涂层判定标准编号</td>
                   <td colspan="2"><input class="easyui-textbox"  type="text" name="coating_acceptance_criteria_no" value=""/></td>

               </tr>
               <tr>
                   <td class="i18n1" name="saltcontaminationbeforeblastmax">打砂前盐度最大值</td>
                   <td colspan="2"><input class="easyui-numberbox" data-options="min:0,precision:2"  type="text" name="salt_contamination_before_blast_max" value=""/></td>
                   <td class="i18n1" name="saltcontaminationbeforeblastmin">打砂前盐度最小值</td>
                   <td colspan="2"><input class="easyui-numberbox" data-options="min:0,precision:2"  type="text" name="salt_contamination_before_blast_min" value=""/></td>
               </tr>
               <tr>
                   <td class="i18n1" name="preheattempmax">预热钢管温度最大值</td>
                   <td colspan="2"><input class="easyui-numberbox" data-options="min:0,precision:2"  type="text" name="preheat_temp_max" value=""/></td>
                   <td class="i18n1" name="preheattempmax">预热钢管温度最小值</td>
                   <td colspan="2"><input class="easyui-numberbox" data-options="min:0,precision:2"  type="text" name="preheat_temp_min" value=""/></td>
               </tr>
               <tr>
                   <td class="i18n1" name="relativehumiditymax">相对湿度最大值</td>
                   <td colspan="2"><input class="easyui-numberbox" data-options="min:0,precision:2"  type="text" name="relative_humidity_max" value=""/></td>
                   <td class="i18n1" name="relativehumiditymax">相对湿度最小值</td>
                   <td colspan="2"><input class="easyui-numberbox" data-options="min:0,precision:2"  type="text" name="relative_humidity_min" value=""/></td>
               </tr>
               <tr>
                   <td class="i18n1" name="tempabovedewpointmax">大于露点最大值</td>
                   <td colspan="2"><input class="easyui-numberbox" data-options="min:0,precision:2"  type="text" name="temp_above_dew_point_max" value=""/></td>
                   <td class="i18n1" name="tempabovedewpointmin">大于露点最小值</td>
                   <td colspan="2"><input class="easyui-numberbox" data-options="min:0,precision:2"  type="text" name="temp_above_dew_point_min" value=""/></td>
               </tr>
               <tr>
                   <td class="i18n1" name="blastfinishsa25max">打砂后清洁度Sa2.5最大值</td>
                   <td colspan="2"><input class="easyui-numberbox" data-options="min:0,precision:2"  type="text" name="blast_finish_sa25_max" value=""/></td>
                   <td class="i18n1" name="blastfinishsa25min">打砂后清洁度Sa2.5最小值</td>
                   <td colspan="2"><input class="easyui-numberbox" data-options="min:0,precision:2"  type="text" name="blast_finish_sa25_min" value=""/></td>
               </tr>
               <tr>
                   <td class="i18n1" name="surfacedustratingmax">灰尘度最大值</td>
                   <td colspan="2"><input class="easyui-numberbox" data-options="min:0,precision:2"  type="text" name="surface_dust_rating_max" value=""/></td>
                   <td class="i18n1" name="surfacedustratingmax">灰尘度最小值</td>
                   <td colspan="2"><input class="easyui-numberbox" data-options="min:0,precision:2"  type="text" name="surface_dust_rating_min" value=""/></td>
               </tr>
               <tr>
                   <td class="i18n1" name="odprofilemax">外锚纹深度最大值</td>
                   <td colspan="2"><input class="easyui-numberbox" data-options="min:0,precision:2"  type="text" name="od_profile_max" value=""/></td>
                   <td class="i18n1" name="odprofilemax">外锚纹深度最小值</td>
                   <td colspan="2"><input class="easyui-numberbox" data-options="min:0,precision:2"  type="text" name="od_profile_min" value=""/></td>
               </tr>
               <tr>
                   <td class="i18n1" name="pipetempafterblastmax">打砂后钢管温度最大值</td>
                   <td colspan="2"><input class="easyui-numberbox" data-options="min:0,precision:2"  type="text" name="pipe_temp_after_blast_max" value=""/></td>
                   <td class="i18n1" name="pipetempafterblastmin">打砂后钢管温度最小值</td>
                   <td colspan="2"><input class="easyui-numberbox" data-options="min:0,precision:2"  type="text" name="pipe_temp_after_blast_min" value=""/></td>
               </tr>
               <tr>
                   <td class="i18n1" name="saltcontaminationafterblastingmax">打砂后盐度最大值</td>
                   <td colspan="2"><input class="easyui-numberbox" data-options="min:0,precision:2"  type="text" name="salt_contamination_after_blasting_max" value=""/></td>
                   <td class="i18n1" name="saltcontaminationafterblastingmin">打砂后盐度最小值</td>
                   <td colspan="2"><input class="easyui-numberbox" data-options="min:0,precision:2"  type="text" name="salt_contamination_after_blasting_min" value=""/></td>
               </tr><tr>
               <td class="i18n1" name="applicationtempmax">中频温度最大值</td>
               <td colspan="2"><input class="easyui-numberbox" data-options="min:0,precision:2"  type="text" name="application_temp_max" value=""/></td>
               <td class="i18n1" name="applicationtempmin">中频温度最小值</td>
               <td colspan="2"><input class="easyui-numberbox" data-options="min:0,precision:2"  type="text" name="application_temp_min" value=""/></td>
           </tr>
               <tr>
                   <td class="i18n1" name="base2fbecoatthicknessmax">底层2fbe最大厚度</td>
                   <td colspan="2"><input class="easyui-numberbox" data-options="min:0,precision:2"  type="text" name="base_2fbe_coat_thickness_max" value=""/></td>
                   <td class="i18n1" name="base2fbecoatthicknessmin">底层2fbe最小厚度</td>
                   <td colspan="2"><input class="easyui-numberbox" data-options="min:0,precision:2"  type="text" name="base_2fbe_coat_thickness_min" value=""/></td>
               </tr><tr>
               <td class="i18n1" name="top2fbecoatthicknessmax">顶层2fbe最大厚度</td>
               <td colspan="2"><input class="easyui-numberbox" data-options="min:0,precision:2"  type="text" name="top_2fbe_coat_thickness_max" value=""/></td>
               <td class="i18n1" name="top2fbecoatthicknessmin">顶层2fbe最小厚度</td>
               <td colspan="2"><input class="easyui-numberbox" data-options="min:0,precision:2"  type="text" name="top_2fbe_coat_thickness_min" value=""/></td>
           </tr>
               <tr>
                   <td class="i18n1" name="total2fbecoatthicknessmax">2fbe总厚度最大值</td>
                   <td colspan="2"><input class="easyui-numberbox" data-options="min:0,precision:2"  type="text" name="total_2fbe_coat_thickness_max" value=""/></td>
                   <td class="i18n1" name="total2fbecoatthicknessmin">2fbe总厚度最小值</td>
                   <td colspan="2"><input class="easyui-numberbox" data-options="min:0,precision:2"  type="text" name="total_2fbe_coat_thickness_min" value=""/></td>
               </tr>
               <tr>
                   <td class="i18n1" name="top3lpecoatthicknessmax">3lpe顶层最大值</td>
                   <td colspan="2"><input class="easyui-numberbox" data-options="min:0,precision:2"  type="text" name="top_3lpe_coat_thickness_max" value=""/></td>
                   <td class="i18n1" name="top3lpecoatthicknessmin">3lpe顶层最小值</td>
                   <td colspan="2"><input class="easyui-numberbox" data-options="min:0,precision:2"  type="text" name="top_3lpe_coat_thickness_min" value=""/></td>
               </tr>
               <tr>
                   <td class="i18n1" name="middle3lpecoatthicknessmax">3lpe中间层最大值</td>
                   <td colspan="2"><input class="easyui-numberbox" data-options="min:0,precision:2"  type="text" name="middle_3lpe_coat_thickness_max" value=""/></td>
                   <td class="i18n1" name="middle3lpecoatthicknessmin">3lpe中间层最小值</td>
                   <td colspan="2"><input class="easyui-numberbox" data-options="min:0,precision:2"  type="text" name="middle_3lpe_coat_thickness_min" value=""/></td>
               </tr>
               <tr>
                   <td class="i18n1" name="base3lpecoatthicknessmax">3lpe底层最大值</td>
                   <td colspan="2"><input class="easyui-numberbox" data-options="min:0,precision:2"  type="text" name="base_3lpe_coat_thickness_max" value=""/></td>
                   <td class="i18n1" name="base3lpecoatthicknessmin">3lpe底层最小值</td>
                   <td colspan="2"><input class="easyui-numberbox" data-options="min:0,precision:2"  type="text" name="base_3lpe_coat_thickness_min" value=""/></td>
               </tr>
               <tr>
                   <td class="i18n1" name="total3lpecoatthicknessmax">3lpe总厚度最大值</td>
                   <td colspan="2"><input class="easyui-numberbox" data-options="min:0,precision:2"  type="text" name="total_3lpe_coat_thickness_max" value=""/></td>
                   <td class="i18n1" name="total3lpecoatthicknessmin">3lpe总厚度最小值</td>
                   <td colspan="2"><input class="easyui-numberbox" data-options="min:0,precision:2"  type="text" name="total_3lpe_coat_thickness_min" value=""/></td>
               </tr>
               <tr>
                   <td class="i18n1" name="repairmax">允许修补量最大值</td>
                   <td colspan="2"><input class="easyui-numberbox" data-options="min:0,precision:2"  type="text" name="repair_max" value=""/></td>
                   <td class="i18n1" name="repairmin">允许修补量最小值</td>
                   <td colspan="2"><input class="easyui-numberbox" data-options="min:0,precision:2"  type="text" name="repair_min" value=""/></td>
               </tr>
               <tr>
                   <td class="i18n1" name="holidaytestervoltagemax">检漏仪电压最大值</td>
                   <td colspan="2"><input class="easyui-numberbox" data-options="min:0,precision:2"  type="text" name="holiday_tester_voltage_max" value=""/></td>
                   <td class="i18n1" name="holidaytestervoltagemin">检漏仪电压最小值</td>
                   <td colspan="2"><input class="easyui-numberbox" data-options="min:0,precision:2"  type="text" name="holiday_tester_voltage_min" value=""/></td>
               </tr>
               <tr>
                   <td class="i18n1" name="cutbackmax">预留段最大值</td>
                   <td colspan="2"><input class="easyui-numberbox" data-options="min:0,precision:2"  type="text" name="cutback_min" value=""/></td>
                   <td class="i18n1" name="cutbackmin">预留段最小值</td>
                   <td colspan="2"><input class="easyui-numberbox" data-options="min:0,precision:2"  type="text" name="cutback_min" value=""/></td>
               </tr>

           </table>
       </fieldset>
   </form>


</div>
<div id="dlg-buttons" align="center" style="width:900px;">
    <a href="#" class="easyui-linkbutton" iconCls="icon-save" onclick="odAcceptanceFormSubmit()">Save</a>
    <a href="#" class="easyui-linkbutton" id="hlcancelBtn" operationtype="add" iconCls="icon-cancel" onclick="odAcceptanceCancelSubmit()">Cancel</a>
</div>
<%--<div id="gridPanel1" class="mini-panel" title="header" iconCls="icon-add" style="width:450px;height:250px;"--%>
     <%--showToolbar="true" showCloseButton="true" showHeader="false" bodyStyle="padding:0" borderStyle="border:0"--%>
<%-->--%>
    <%--<div property="toolbar" id="searchBar1" style="padding:5px;padding-left:8px;text-align:center;display: none">--%>
        <%--<div style="float:left;padding-bottom:2px;">--%>
            <%--<span class="i18n1" name="pipeno">钢管编号</span><span>:</span>--%>
            <%--<input id="keyText1" class="mini-textbox" style="width:110px;" onenter="onSearchClick(1)"/>--%>
            <%--<a class="mini-button" onclick="onSearchClick(1)">查找</a>--%>
            <%--<a class="mini-button" onclick="onClearClick(1)" name="clear">清除</a>--%>
        <%--</div>--%>
        <%--<div style="float:right;padding-bottom:2px;">--%>
            <%--<a class="mini-button" onclick="onCloseClick(1)" name="close">关闭</a>--%>
        <%--</div>--%>
        <%--<div style="clear:both;"></div>--%>
    <%--</div>--%>
    <%--<div id="datagrid1" class="mini-datagrid" style="width:100%;height:100%;"--%>
         <%--borderStyle="border:0" showPageSize="false" showPageIndex="false"--%>
         <%--url="/pipeinfo/getPipeNumber.action">--%>
        <%--<div property="columns">--%>
            <%--<div type="checkcolumn" ></div>--%>
            <%--<div field="pipe_no" width="80" headerAlign="center" allowSort="true" class="i18n1" name="pipeno">钢管编号</div>--%>
            <%--<div field="contract_no" width="80" headerAlign="center" allowSort="true" class="i18n1" name="contractno">合同编号</div>--%>
            <%--<div field="od" width="40" headerAlign="center" allowSort="true" class="i18n1" name="od">外径</div>--%>
            <%--<div field="wt" width="40" headerAlign="center" allowSort="true" class="i18n1" name="wt">壁厚</div>--%>
            <%--<div field="p_length" width="40" headerAlign="center" allowSort="true" class="i18n1" name="p_length">长度</div>--%>
            <%--<div field="weight" width="40" headerAlign="center" allowSort="true" class="i18n1" name="weight">重量</div>--%>
        <%--</div>--%>
    <%--</div>--%>
<%--</div>--%>
<%--<div id="gridPanel2" class="mini-panel" title="header" iconCls="icon-add" style="width:450px;height:250px;"--%>
     <%--showToolbar="true" showCloseButton="true" showHeader="false" bodyStyle="padding:0" borderStyle="border:0"--%>
<%-->--%>
    <%--<div property="toolbar" id="searchBar2" style="padding:5px;padding-left:8px;text-align:center;display:none;">--%>
        <%--<div style="float:left;padding-bottom:2px;">--%>
            <%--<span class="i18n1" name="operatorno">操作工编号</span><span>:</span>--%>
            <%--<input id="keyText3" class="mini-textbox" style="width:110px;" onenter="onSearchClick(2)"/>--%>
            <%--<span class="i18n1" name="operatorname">姓名</span><span>:</span>--%>
            <%--<input id="keyText4" class="mini-textbox" style="width:110px;" onenter="onSearchClick(2)"/>--%>
            <%--<a class="mini-button" onclick="onSearchClick(2)" name="search">查找</a>--%>
            <%--<a class="mini-button" onclick="onClearClick(2)" name="clear">清除</a>--%>
        <%--</div>--%>
        <%--<div style="float:right;padding-bottom:2px;">--%>
            <%--<a class="mini-button" onclick="onCloseClick(2)" name="close">关闭</a>--%>
        <%--</div>--%>
        <%--<div style="clear:both;"></div>--%>
    <%--</div>--%>
    <%--<div id="datagrid2" class="mini-datagrid" style="width:100%;height:100%;"--%>
         <%--borderStyle="border:0" showPageSize="false" showPageIndex="false"--%>
         <%--url="/person/getPersonNoByName.action">--%>
        <%--<div property="columns">--%>
            <%--<div type="checkcolumn" ></div>--%>
            <%--<div field="employee_no" width="60" headerAlign="center" allowSort="true" class="i18n1" name="operatorno">操作工编号</div>--%>
            <%--<div field="pname" width="60" headerAlign="center" allowSort="true" class="i18n1" name="operatorname">姓名</div>--%>
        <%--</div>--%>
    <%--</div>--%>
<%--</div>--%>
<script type="text/javascript" src="../easyui/jquery.easyui.min.js"></script>
</body>
</html>
<script type="text/javascript">
    mini.parse();
    var grid= mini.get("multiupload1");
    // var keyText1=mini.get('keyText1');
    // var keyText4 = mini.get("keyText4");
    // var keyText3=mini.get("keyText3");
    // var grid1=mini.get("datagrid1");
    // var grid2=mini.get("datagrid2");
    // var look1=mini.get('lookup1');
    // var look2= mini.get("lookup2");
    // var combox1=mini.get("combobox1");
    // grid1.load();
    // grid2.load();

    // function onSearchClick(type) {
    //     if(type==1)
    //     {
    //         grid1.load({
    //             pipe_no:keyText1.value
    //         });
    //     }else if(type==2){
    //         grid2.load({
    //             pname: keyText4.value,
    //             employeeno:keyText3.value
    //         });
    //     }
    //
    // }
    // function onCloseClick(type) {
    //     if(type==1)
    //        look1.hidePopup();
    //     else if(type==2)
    //         look2.hidePopup();
    // }
    // function onClearClick(type) {
    //     if(type==1)
    //         look1.deselectAll();
    //     else if(type==2)
    //         look2.deselectAll();
    // }
    // look1.on('valuechanged',function () {
    //     var rows = grid1.getSelected();
    //     $("input[name='pipe_no']").val(rows.pipe_no);
    //     clearLabelPipeInfo();
    //     $.ajax({
    //         url:'../pipeinfo/getPipeInfoByNo.action',
    //         data:{'pipe_no':rows.pipe_no},
    //         dataType:'json',
    //         success:function (data) {
    //             if(data!=null&&data!=""){
    //                 addLabelPipeInfo(data);
    //             }
    //         },
    //         error:function () {
    //             hlAlertThree();
    //         }
    //     });
    // });
    // look2.on('valuechanged',function (e){
    //     var rows = grid2.getSelected();
    //     $("input[name='operator_no']").val(rows.employee_no);
    // });
    // look1.on("showpopup",function(e){
    //     $('.mini-shadow').css('z-index','99999');
    //     $('.mini-popup').css('z-index','100000');
    //     $('.mini-panel').css('z-index','100000');
    //     $('#searchBar1').css('display','block');
    //     //$('.mini-buttonedit .mini-buttonedit-input').css('width','150px');
    // });
    // look2.on("showpopup",function(e){
    //     $('.mini-shadow').css('z-index','99999');
    //     $('.mini-popup').css('z-index','100000');
    //     $('.mini-panel').css('z-index','100000');
    //     $('#searchBar2').css('display','block');
    //     //$('.mini-buttonedit .mini-buttonedit-input').css('width','150px');
    // });
    // combox1.on("showpopup",function () {
    //     $('.mini-shadow').css('z-index','99999');
    //     $('.mini-popup').css('z-index','100000');
    //     $('.mini-panel').css('z-index','100000');
    // });
    // function onComboxCloseClick(e) {
    //     var obj = e.sender;
    //     obj.setText("");
    //     obj.setValue("");
    // }
    hlLanguage("../i18n/");
</script>