<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
    String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>外防腐标准</title>
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
                $('#hlOdAcceptanceDialog').dialog({
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
            clearFormLabel();
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
                            }
                            hlAlertFour(data.message);
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
            if(row){
                clearFormLabel();
                $('#hlOdAcceptanceDialog').dialog('open').dialog('setTitle','修改');
                $('#odAcceptanceForm').form('load',row);
                $("#odacceptanceId").text(row.id);
                var lasttime=formatterdate(row.last_update_time);
                $("#lastupdatetime").text(lasttime);
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
                onSubmit:function (){
                    if($("input[name='coating_acceptance_criteria_no']").val()==""){

                        hlAlertFour("请输入外防接收标准编号");
                        return false;
                    }

                    if($("input[name='stencil_content']").val()==""){
                        var str="[PIPENO] [HALFLENGTH]\n"
                                +"BAOSTEEL API SPEC 5L 05 17 [OD]mm*[WT]mm [GRADE] PSL2 HFW TESTED 18.3 APA GROUP\n"
                                +"Order No.: [CONTRACTNO] Pipe No.: [PIPENO] Length: [PIPELENGTH]m Weight: [WEIGHT]Kg Heat No.:[HEATNO] Batch No.:[BATCHNO]\n"
                                +"Linepipe Spec: [CLIENTSPEC] Coating Spec: [COATINGSPEC]\n"
                                +"Coating Date: [COATINGDATE] Nominal Coating Thickness: 600 um [PROJECTNAME] Hilong";
                        $("input[name='stencil_content']").val(str);
                        //alert(str);
                    }

                    setParams();
                },
                success: function(result){
                    var result = eval('('+result+')');
                    $('#hlOdAcceptanceDialog').dialog('close');
                    if (result.success){
                        $('#odAcceptanceDatagrids').datagrid('reload');
                    }
                    hlAlertFour(result.message);
                },
                error:function () {
                    //clearFormLabel();
                    hlAlertThree();
                }
            });

        }
        function odAcceptanceCancelSubmit() {
            $('#hlOdAcceptanceDialog').dialog('close');
        }
        function setParams() {

            setParamsMax($("input[name='salt_contamination_before_blast_max']"));
            setParamsMin($("input[name='salt_contamination_before_blast_min']"));
            setParamsMax($("input[name='preheat_temp_max']"));
            setParamsMin($("input[name='preheat_temp_min']"));
            setParamsMax($("input[name='relative_humidity_max']"));
            setParamsMin($("input[name='relative_humidity_min']"));
            setParamsMax($("input[name='temp_above_dew_point_max']"));
            setParamsMin($("input[name='temp_above_dew_point_min']"));
            setParamsMax($("input[name='blast_finish_sa25_max']"));
            setParamsMin($("input[name='blast_finish_sa25_min']"));
            setParamsMax($("input[name='surface_dust_rating_max']"));
            setParamsMin($("input[name='surface_dust_rating_min']"));
            setParamsMax($("input[name='profile_max']"));
            setParamsMin($("input[name='profile_min']"));
            setParamsMax($("input[name='pipe_temp_after_blast_max']"));
            setParamsMin($("input[name='pipe_temp_after_blast_min']"));
            setParamsMax($("input[name='salt_contamination_after_blasting_max']"));
            setParamsMin($("input[name='salt_contamination_after_blasting_min']"));
            setParamsMax($("input[name='application_temp_max']"));
            setParamsMin($("input[name='application_temp_min']"));
            setParamsMax($("input[name='base_2fbe_coat_thickness_max']"));
            setParamsMin($("input[name='base_2fbe_coat_thickness_min']"));
            setParamsMax($("input[name='top_2fbe_coat_thickness_max']"));
            setParamsMin($("input[name='top_2fbe_coat_thickness_min']"));
            setParamsMax($("input[name='total_2fbe_coat_thickness_max']"));
            setParamsMin($("input[name='total_2fbe_coat_thickness_min']"));
            setParamsMax($("input[name='top_3lpe_coat_thickness_max']"));
            setParamsMin($("input[name='top_3lpe_coat_thickness_min']"));
            setParamsMax($("input[name='middle_3lpe_coat_thickness_max']"));
            setParamsMin($("input[name='middle_3lpe_coat_thickness_min']"));
            setParamsMax($("input[name='base_3lpe_coat_thickness_max']"));
            setParamsMin($("input[name='base_3lpe_coat_thickness_min']"));
            setParamsMax($("input[name='total_3lpe_coat_thickness_max']"));
            setParamsMin($("input[name='total_3lpe_coat_thickness_min']"));
            setParamsMax($("input[name='repair_max']"));
            setParamsMin($("input[name='repair_min']"));
            setParamsMax($("input[name='holiday_tester_voltage_max']"));
            setParamsMin($("input[name='holiday_tester_voltage_min']"));
            setParamsMax($("input[name='cutback_max']"));
            setParamsMin($("input[name='cutback_min']"));

            setParamsMax($("input[name='epoxy_cutback_max']"));
            setParamsMin($("input[name='epoxy_cutback_min']"));
            setParamsMax($("input[name='magnetism_max']"));
            setParamsMin($("input[name='magnetism_min']"));
            setParamsMax($("input[name='coating_bevel_angle_max']"));
            setParamsMin($("input[name='coating_bevel_angle_min']"));
            setParamsMax($("input[name='holiday_max']"));
            setParamsMin($("input[name='holiday_min']"));
            setParamsMax($("input[name='strip_temp_max']"));
            setParamsMin($("input[name='strip_temp_min']"));
            setParamsMax($("input[name='abrasive_conductivity_max']"));
            setParamsMin($("input[name='abrasive_conductivity_min']"));
            setParamsMax($("input[name='rinse_water_conductivity_max']"));
            setParamsMin($("input[name='rinse_water_conductivity_min']"));
            setParamsMax($("input[name='adhesion_rating_max']"));
            setParamsMin($("input[name='adhesion_rating_min']"));
            setParamsMax($("input[name='peel_strength_20_max']"));
            setParamsMin($("input[name='peel_strength_20_min']"));
            setParamsMax($("input[name='peel_strength_65_max']"));
            setParamsMin($("input[name='peel_strength_65_min']"));
        }
        function  setParamsMax($obj) {
            if($obj.val()==null||$obj.val()=="")
                $obj.val(9999);
        }
        function  setParamsMin($obj) {
            if($obj.val()==null||$obj.val()=="")
                $obj.val(-9999);
        }
        function  clearFormLabel(){
            $('#odAcceptanceForm').form('clear');
            $('.hl-label').text('');
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
                   <th field="profile_max" align="center" width="100" hidden="true" class="i18n1" name="odprofilemax">外锚纹深度最大值</th>
                   <th field="profile_min" align="center" width="100" hidden="true" class="i18n1" name="odprofilemin">外锚纹深度最小值</th>
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

                   <th field="epoxy_cutback_max" align="center" width="150" hidden="true" class="i18n1" name="epoxycutbackmax">粉末长度最大值</th>
                   <th field="epoxy_cutback_min" align="center" width="150" hidden="true" class="i18n1" name="epoxycutbackmin">粉末长度最小值</th>
                   <th field="magnetism_max" align="center" width="150" hidden="true" class="i18n1" name="magnetismmax">剩磁最大值</th>
                   <th field="magnetism_min" align="center" width="150" hidden="true" class="i18n1" name="magnetismin">剩磁最小值</th>
                   <th field="coating_bevel_angle_max" align="center" width="150" hidden="true" class="i18n1" name="coatingbevelanglemax">涂层倒角最大值 </th>
                   <th field="coating_bevel_angle_min" align="center" width="150" hidden="true" class="i18n1" name="coatingbevelanglemin">涂层倒角最小值 </th>
                   <th field="holiday_max" align="center" width="150" hidden="true" class="i18n1" name="holidaymax">漏点最大值 </th>
                   <th field="holiday_min" align="center" width="150" hidden="true" class="i18n1" name="holidaymin">漏点最小值 </th>

                   <th field="strip_temp_max" align="center" width="150" hidden="true" class="i18n1" name="striptempmax">扒皮温度最大值 </th>
                   <th field="strip_temp_min" align="center" width="150" hidden="true" class="i18n1" name="striptempmin">扒皮温度最小值 </th>

                   <th field="stencil_content" align="center" width="150" hidden="true" class="i18n1" name="stencilcontent">喷标内容 </th>
                   <th field="abrasive_conductivity_max" align="center" width="150" hidden="true" class="i18n1" name="abrasiveconductivitymax">磨料电导率最大值 </th>
                   <th field="abrasive_conductivity_min" align="center" width="150" hidden="true" class="i18n1" name="abrasiveconductivitymin">磨料电导率最小值 </th>
                   <th field="rinse_water_conductivity_max" align="center" width="150" hidden="true" class="i18n1" name="rinsewaterconductivitymax">冲洗水电导率最大值 </th>
                   <th field="rinse_water_conductivity_min" align="center" width="150" hidden="true" class="i18n1" name="rinsewaterconductivitymin">冲洗水电导率最小值 </th>
                   <th field="adhesion_rating_max" align="center" width="150" hidden="true" class="i18n1" name="adhesionratingmax">附着力等级最大值 </th>
                   <th field="adhesion_rating_min" align="center" width="150" hidden="true" class="i18n1" name="adhesionratingmin">附着力等级最小值 </th>

                   <th field="peel_strength_20_max" align="center" width="150" hidden="true" class="i18n1" name="peelstrength20max"> </th>
                   <th field="peel_strength_20_min" align="center" width="150" hidden="true" class="i18n1" name="peelstrength20min"> </th>
                   <th field="peel_strength_65_max" align="center" width="150" hidden="true" class="i18n1" name="peelstrength65max"> </th>
                   <th field="peel_strength_65_min" align="center" width="150" hidden="true" class="i18n1" name="peelstrength65min"> </th>

                   <th field="last_update_time" align="center" width="150" class="i18n1" name="lastupdatetime" data-options="formatter:formatterdate">最后更新时间</th>

               </tr>
             </thead>
         </table>
    </div>
</fieldset>

<!--工具栏-->
<div id="hlOdAcceptanceTb" style="padding:10px;">
    <span class="i18n1" name="coatingacceptancecriteriano">涂层判定标准编号</span>:
    <input id="coating_acceptance_criteria_no" name="coatingacceptancecriteriano" style="line-height:22px;border:1px solid #ccc">
    <a href="#" class="easyui-linkbutton" plain="true" data-options="iconCls:'icon-search'" onclick="searchOdAcceptance()">Search</a>
    <div style="float:right">
     <a href="#" id="addObpLinkBtn" class="easyui-linkbutton i18n1" name="add" data-options="iconCls:'icon-add',plain:true" onclick="addOdAcceptance()">添加</a>
     <a href="#" id="editObpLinkBtn" class="easyui-linkbutton i18n1" name="edit" data-options="iconCls:'icon-edit',plain:true" onclick="editOdAcceptance()">修改</a>
     <a href="#" id="deltObpLinkBtn" class="easyui-linkbutton i18n1" name="delete" data-options="iconCls:'icon-remove',plain:true" onclick="delOdAcceptance()">删除</a>
    </div>
</div>

<!--添加、修改框-->
<div id="hlOdAcceptanceDialog" class="easyui-dialog" data-options="title:'添加',modal:true"  closed="true" buttons="#dlg-buttons" style="display: none;padding:5px;width:950px;max-height:500px;overflow-y: auto;">
   <form id="odAcceptanceForm" method="post">
       <fieldset style="width:900px;border:solid 1px #aaa;margin-top:8px;position:relative;">
           <legend>外防接收标准信息</legend>
           <table class="ht-table" width="100%" border="0">
               <tr>
                   <td class="i18n1" name="id">流水号</td>
                   <td  >
                       <label id="odacceptanceId" class="hl-label"></label>
                       <%--<input id="odacceptanceId" class="easyui-textbox" readonly="true" type="text" value="" name="odacceptanceId">--%>
                       <%--<input id="odacceptanceId" class="easyui-textbox" readonly="true" type="text" value="" name="odacceptanceId"> --%>
                   </td>
                   <td></td>
                   <td class="i18n1" name="coatingacceptancecriteriano">涂层判定标准编号</td>
                   <td ><input class="easyui-textbox"  type="text" name="coating_acceptance_criteria_no" value=""/></td>
                   <td></td>

               </tr>
               <tr>
                   <td class="i18n1" colspan="6" name="odblastcontrolparameter">外打砂控制参数</td>

               </tr>

               <tr>
                   <td class="i18n1" name="rinsewaterconductivitymax">冲洗水电导率最大值</td>
                   <td  ><input class="easyui-numberbox" data-options="precision:2"  type="text" name="rinse_water_conductivity_max" value=""/></td>
                   <td></td>
                   <td class="i18n1" name="rinsewaterconductivitymin">冲洗水电导率最小值</td>
                   <td  ><input class="easyui-numberbox" data-options="precision:2"  type="text" name="rinse_water_conductivity_min" value=""/></td>
                   <td></td>
               </tr>
               <tr>
                   <td class="i18n1" name="abrasiveconductivitymax">磨料电导率最大值</td>
                   <td  ><input class="easyui-numberbox" data-options="precision:2"  type="text" name="abrasive_conductivity_max" value=""/></td>
                   <td></td>
                   <td class="i18n1" name="abrasiveconductivitymin">磨料电导率最小值</td>
                   <td  ><input class="easyui-numberbox" data-options="precision:2"  type="text" name="abrasive_conductivity_min" value=""/></td>
                   <td></td>
               </tr>

               <tr>
                   <td class="i18n1" name="saltcontaminationbeforeblastmax">打砂前盐度最大值</td>
                   <td  ><input class="easyui-numberbox" data-options="precision:2"  type="text" name="salt_contamination_before_blast_max" value=""/></td>
                   <td></td>
                   <td class="i18n1" name="saltcontaminationbeforeblastmin">打砂前盐度最小值</td>
                   <td  ><input class="easyui-numberbox" data-options="precision:2"  type="text" name="salt_contamination_before_blast_min" value=""/></td>
                   <td></td>
               </tr>
               <tr>
                   <td class="i18n1" name="preheattempmax">预热钢管温度最大值</td>
                   <td  ><input class="easyui-numberbox" data-options="precision:2"  type="text" name="preheat_temp_max" value=""/></td>
                   <td></td>
                   <td class="i18n1" name="preheattempmin">预热钢管温度最小值</td>
                   <td  ><input class="easyui-numberbox" data-options="precision:2"  type="text" name="preheat_temp_min" value=""/></td>
                   <td></td>
               </tr>
               <tr>
                   <td class="i18n1" name="relativehumiditymax">相对湿度最大值</td>
                   <td  ><input class="easyui-numberbox" data-options="precision:2"  type="text" name="relative_humidity_max" value=""/></td>
                   <td></td>
                   <td class="i18n1" name="relativehumiditymin">相对湿度最小值</td>
                   <td  ><input class="easyui-numberbox" data-options="precision:2"  type="text" name="relative_humidity_min" value=""/></td>
                   <td></td>
               </tr>
               <tr>
                   <td class="i18n1" name="tempabovedewpointmax">大于露点最大值</td>
                   <td ><input class="easyui-numberbox" data-options="precision:2"  type="text" name="temp_above_dew_point_max" value=""/></td>
                   <td></td>
                   <td class="i18n1" name="tempabovedewpointmin">大于露点最小值</td>
                   <td ><input class="easyui-numberbox" data-options="precision:2"  type="text" name="temp_above_dew_point_min" value=""/></td>
                   <td></td>
               </tr>
               <tr>
                   <td class="i18n1" name="pipetempafterblastmax">打砂后钢管温度最大值</td>
                   <td ><input class="easyui-numberbox" data-options="precision:2"  type="text" name="pipe_temp_after_blast_max" value=""/></td>
                   <td></td>
                   <td class="i18n1" name="pipetempafterblastmin">打砂后钢管温度最小值</td>
                   <td ><input class="easyui-numberbox" data-options="precision:2"  type="text" name="pipe_temp_after_blast_min" value=""/></td>
                   <td></td>
               </tr>
               <tr>
                   <td class="i18n1" name="blastfinishsa25max">打砂后清洁度Sa2.5最大值</td>
                   <td ><input class="easyui-numberbox" data-options="precision:2"  type="text" name="blast_finish_sa25_max" value=""/></td>
                   <td></td>
                   <td class="i18n1" name="blastfinishsa25min">打砂后清洁度Sa2.5最小值</td>
                   <td ><input class="easyui-numberbox" data-options="precision:2"  type="text" name="blast_finish_sa25_min" value=""/></td>
                   <td></td>
               </tr>
               <tr>
                   <td class="i18n1" name="surfacedustratingmax">灰尘度最大值</td>
                   <td ><input class="easyui-numberbox" data-options="precision:0"  type="text" name="surface_dust_rating_max" value=""/></td>
                   <td></td>
                   <td class="i18n1" name="surfacedustratingmin">灰尘度最小值</td>
                   <td ><input class="easyui-numberbox" data-options="precision:0"  type="text" name="surface_dust_rating_min" value=""/></td>
                   <td></td>
               </tr>
               <tr>
                   <td class="i18n1" name="odprofilemax">外锚纹深度最大值</td>
                   <td ><input class="easyui-numberbox" data-options="precision:2"  type="text" name="profile_max" value=""/></td>
                   <td></td>
                   <td class="i18n1" name="odprofilemin">外锚纹深度最小值</td>
                   <td ><input class="easyui-numberbox" data-options="precision:2"  type="text" name="profile_min" value=""/></td>
                   <td></td>
               </tr>

               <tr>
                   <td class="i18n1" name="saltcontaminationafterblastingmax">打砂后盐度最大值</td>
                   <td ><input class="easyui-numberbox" data-options="precision:2"  type="text" name="salt_contamination_after_blasting_max" value=""/></td>
                   <td></td>
                   <td class="i18n1" name="saltcontaminationafterblastingmin">打砂后盐度最小值</td>
                   <td ><input class="easyui-numberbox" data-options="precision:2"  type="text" name="salt_contamination_after_blasting_min" value=""/></td>
                   <td></td>
               </tr>
               <tr>
                   <td class="i18n1" colspan="6" name="odcoatingcontrolparameter">外喷涂控制参数</td>

               </tr>
               <tr>
                   <td class="i18n1" name="applicationtempmax">中频温度最大值</td>
                   <td ><input class="easyui-numberbox" data-options="precision:2"  type="text" name="application_temp_max" value=""/></td>
                   <td></td>
                   <td class="i18n1" name="applicationtempmin">中频温度最小值</td>
                   <td ><input class="easyui-numberbox" data-options="precision:2"  type="text" name="application_temp_min" value=""/></td>
                   <td></td>
               </tr>
               <tr>
                   <td class="i18n1" colspan="6" name="2fbeodcoatingcontrolparameter">2FBE外涂检验控制参数</td>
               </tr>

               <tr>
                   <td class="i18n1" name="base2fbecoatthicknessmax">底层2fbe最大厚度</td>
                   <td ><input class="easyui-numberbox" data-options="precision:2"  type="text" name="base_2fbe_coat_thickness_max" value=""/></td>
                   <td></td>
                   <td class="i18n1" name="base2fbecoatthicknessmin">底层2fbe最小厚度</td>
                   <td ><input class="easyui-numberbox" data-options="precision:2"  type="text" name="base_2fbe_coat_thickness_min" value=""/></td>
                   <td></td>
               </tr>
               <tr>
                   <td class="i18n1" name="top2fbecoatthicknessmax">顶层2fbe最大厚度</td>
                   <td ><input class="easyui-numberbox" data-options="precision:2"  type="text" name="top_2fbe_coat_thickness_max" value=""/></td>
                   <td></td>
                   <td class="i18n1" name="top2fbecoatthicknessmin">顶层2fbe最小厚度</td>
                   <td ><input class="easyui-numberbox" data-options="precision:2"  type="text" name="top_2fbe_coat_thickness_min" value=""/></td>
                   <td></td>
               </tr>
               <tr>
                   <td class="i18n1" name="total2fbecoatthicknessmax">2fbe总厚度最大值</td>
                   <td ><input class="easyui-numberbox" data-options="precision:2"  type="text" name="total_2fbe_coat_thickness_max" value=""/></td>
                   <td></td>
                   <td class="i18n1" name="total2fbecoatthicknessmin">2fbe总厚度最小值</td>
                   <td ><input class="easyui-numberbox" data-options="precision:2"  type="text" name="total_2fbe_coat_thickness_min" value=""/></td>
                   <td></td>
               </tr>

               <tr>
                   <td class="i18n1" colspan="6" name="3lpeodcoatingcontrolparameter">3LPE外涂检验控制参数</td>
               </tr>

               <tr>
                   <td class="i18n1" name="top3lpecoatthicknessmax">3lpe顶层最大值</td>
                   <td ><input class="easyui-numberbox" data-options="precision:2"  type="text" name="top_3lpe_coat_thickness_max" value=""/></td><td>

               </td>
                   <td class="i18n1" name="top3lpecoatthicknessmin">3lpe顶层最小值</td>
                   <td ><input class="easyui-numberbox" data-options="precision:2"  type="text" name="top_3lpe_coat_thickness_min" value=""/></td><td>

               </td>
               </tr>
               <tr>
                   <td class="i18n1" name="middle3lpecoatthicknessmax">3lpe中间层最大值</td>
                   <td ><input class="easyui-numberbox" data-options="precision:2"  type="text" name="middle_3lpe_coat_thickness_max" value=""/></td>
                   <td></td>
                   <td class="i18n1" name="middle3lpecoatthicknessmin">3lpe中间层最小值</td>
                   <td ><input class="easyui-numberbox" data-options="precision:2"  type="text" name="middle_3lpe_coat_thickness_min" value=""/></td>
                   <td></td>
               </tr>
               <tr>
                   <td class="i18n1" name="base3lpecoatthicknessmax">3lpe底层最大值</td>
                   <td ><input class="easyui-numberbox" data-options="precision:2"  type="text" name="base_3lpe_coat_thickness_max" value=""/></td>
                   <td></td>
                   <td class="i18n1" name="base3lpecoatthicknessmin">3lpe底层最小值</td>
                   <td ><input class="easyui-numberbox" data-options="precision:2"  type="text" name="base_3lpe_coat_thickness_min" value=""/></td>
                   <td></td>
               </tr>
               <tr>
                   <td class="i18n1" name="total3lpecoatthicknessmax">3lpe总厚度最大值</td>
                   <td ><input class="easyui-numberbox" data-options="precision:2"  type="text" name="total_3lpe_coat_thickness_max" value=""/></td>
                   <td></td>
                   <td class="i18n1" name="total3lpecoatthicknessmin">3lpe总厚度最小值</td>
                   <td ><input class="easyui-numberbox" data-options="precision:2"  type="text" name="total_3lpe_coat_thickness_min" value=""/></td>
                   <td></td>
               </tr>
               <tr>
                   <td class="i18n1" colspan="6" name="odcoatinggeneralcontrolparameter">外涂检验通用控制参数</td>
               </tr>
               <tr>
                   <td class="i18n1" name="repairmax">允许修补量最大值</td>
                   <td ><input class="easyui-numberbox" data-options="precision:0"  type="text" name="repair_max" value=""/></td>
                   <td></td>
                   <td class="i18n1" name="repairmin">允许修补量最小值</td>
                   <td ><input class="easyui-numberbox" data-options="precision:0"  type="text" name="repair_min" value=""/></td>
                   <td></td>
               </tr>
               <tr>
                   <td class="i18n1" name="holidaytestervoltagemax">检漏仪电压最大值</td>
                   <td ><input class="easyui-numberbox" data-options="precision:2"  type="text" name="holiday_tester_voltage_max" value=""/></td>
                   <td></td>
                   <td class="i18n1" name="holidaytestervoltagemin">检漏仪电压最小值</td>
                   <td ><input class="easyui-numberbox" data-options="precision:2"  type="text" name="holiday_tester_voltage_min" value=""/></td>
                   <td></td>
               </tr>
               <tr>
                   <td class="i18n1" name="adhesionratingmax">附着力等级最大值</td>
                   <td ><input class="easyui-numberbox" data-options="precision:0"  type="text" name="adhesion_rating_max" value=""/></td>
                   <td></td>
                   <td class="i18n1" name="adhesionratingmin">附着力等级最小值</td>
                   <td ><input class="easyui-numberbox" data-options="precision:0"  type="text" name="adhesion_rating_min" value=""/></td>
                   <td></td>
               </tr>

               <tr>
                   <td class="i18n1" colspan="6" name="odfinalcontrolparameter">外防终检控制参数</td>
               </tr>

               <tr>
                   <td class="i18n1" name="cutbackmax">预留段最大值</td>
                   <td ><input class="easyui-numberbox" data-options="precision:2"  type="text" name="cutback_max" value=""/></td>
                   <td></td>
                   <td class="i18n1" name="cutbackmin">预留段最小值</td>
                   <td ><input class="easyui-numberbox" data-options="precision:2"  type="text" name="cutback_min" value=""/></td>
                   <td></td>
               </tr>
               <tr>
                   <td class="i18n1" name="epoxycutbackmax">粉末长度最大值</td>
                   <td ><input class="easyui-numberbox" data-options="precision:2"  type="text" name="epoxy_cutback_max" value=""/></td>
                   <td></td>
                   <td class="i18n1" name="epoxycutbackmin">粉末长度最小值</td>
                   <td ><input class="easyui-numberbox" data-options="precision:2"  type="text" name="epoxy_cutback_min" value=""/></td>
                   <td></td>
               </tr>
               <tr>
                   <td class="i18n1" name="magnetismmax">剩磁最大值</td>
                   <td ><input class="easyui-numberbox" data-options="precision:2"  type="text" name="magnetism_max" value=""/></td>
                   <td></td>
                   <td class="i18n1" name="magnetismmin">剩磁最小值</td>
                   <td ><input class="easyui-numberbox" data-options="precision:2"  type="text" name="magnetism_min" value=""/></td>
                   <td></td>
               </tr>
               <tr>
                   <td class="i18n1" name="coatingbevelanglemax">涂层倒角最大值</td>
                   <td ><input class="easyui-numberbox" data-options="precision:2"  type="text" name="coating_bevel_angle_max" value=""/></td>
                   <td></td>
                   <td class="i18n1" name="coatingbevelanglemin">涂层倒角最小值</td>
                   <td ><input class="easyui-numberbox" data-options="precision:2"  type="text" name="coating_bevel_angle_min" value=""/></td>
                   <td></td>
               </tr>
               <tr>
                   <td class="i18n1" name="holidaymax">漏点最大值</td>
                   <td ><input class="easyui-numberbox" data-options="precision:0"  type="text" name="holiday_max" value=""/></td>
                   <td></td>
                   <td class="i18n1" name="holidaymin">漏点最小值</td>
                   <td ><input class="easyui-numberbox" data-options="precision:0"  type="text" name="holiday_min" value=""/></td>
                   <td></td>
               </tr>
               <tr>
                   <td class="i18n1" name="striptempmax">扒皮温度最大值(℃)</td>
                   <td ><input class="easyui-numberbox" data-options="precision:0"  type="text" name="strip_temp_max" value=""/></td>
                   <td></td>
                   <td class="i18n1" name="striptempmin">扒皮温度最小值(℃)</td>
                   <td ><input class="easyui-numberbox" data-options="precision:0"  type="text" name="strip_temp_min" value=""/></td>
                   <td></td>
               </tr>
               <tr>
                   <td class="i18n1" name="peelstrength20max"></td>
                   <td ><input class="easyui-numberbox" data-options="precision:0"  type="text" name="peel_strength_20_max" value=""/></td>
                   <td></td>
                   <td class="i18n1" name="peelstrength20max"></td>
                   <td ><input class="easyui-numberbox" data-options="precision:0"  type="text" name="peel_strength_20_min" value=""/></td>
                   <td></td>
               </tr>
               <tr>
                   <td class="i18n1" name="peelstrength65max"></td>
                   <td ><input class="easyui-numberbox" data-options="precision:0"  type="text" name="peel_strength_65_max" value=""/></td>
                   <td></td>
                   <td class="i18n1" name="peelstrength65max"></td>
                   <td ><input class="easyui-numberbox" data-options="precision:0"  type="text" name="peel_strength_65_min" value=""/></td>
                   <td></td>
               </tr>

               <tr>
                   <td class="i18n1" name="stencilcontent">喷标内容</td>
                   <td ><input class="easyui-textbox" type="text" data-options="multiline:true" name="stencil_content" value="" style="width:300px;height:80px"/>
                   </td>
                   <td></td>
                   <td class="i18n1" name="lastupdatetime">最后更新时间</td>
                   <td  >
                       <label class="hl-label" id="lastupdatetime" type="text" name="last_update_time" value="" data-options="formatter:myformatter2,parser:myparser2"/>
                   </td>
                   <td></td>
               </tr>

           </table>
       </fieldset>
   </form>


</div>
<div id="dlg-buttons" align="center" style="width:900px;">
    <a href="#" class="easyui-linkbutton" iconCls="icon-save" onclick="odAcceptanceFormSubmit()">Save</a>
    <a href="#" class="easyui-linkbutton" id="hlcancelBtn" operationtype="add" iconCls="icon-cancel" onclick="odAcceptanceCancelSubmit()">Cancel</a>
</div>


<script type="text/javascript" src="../easyui/jquery.easyui.min.js"></script>
</body>
</html>
<script type="text/javascript">
    mini.parse();
    var grid= mini.get("multiupload1");

    hlLanguage("../i18n/");
</script>