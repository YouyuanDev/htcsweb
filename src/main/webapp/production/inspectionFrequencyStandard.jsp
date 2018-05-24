<%--
  Created by IntelliJ IDEA.
  User: kurt
  Date: 5/23/18
  Time: 3:42 PM
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
    <title>检验频率</title>
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
        $(function () {
            $('#hlInspectionFrequencyDialog').dialog({
                onClose:function () {
                    clearFormLabel();
                }
            });
            $('.mini-buttonedit .mini-buttonedit-input').css('width','150px');
            // hlLanguage("../i18n/");
        });
        function addInspectionFrequency(){
            $('#hlcancelBtn').attr('operationtype','add');
            $('#hlInspectionFrequencyDialog').dialog('open').dialog('setTitle','新增');
            $('#InspectionFrequencyId').text('');
            clearFormLabel();
            url="/InspectionFrequencyOperation/saveInspectionFrequency.action";
        }
        function delInspectionFrequency() {
            var row = $('#InspectionFrequencyDatagrids').datagrid('getSelections');
            if(row.length>0){
                var idArr=[];
                for (var i=0;i<row.length;i++){
                    idArr.push(row[i].id);
                }
                var idArrs=idArr.join(',');
                $.messager.confirm('系统提示',"您确定要删除这<font color=red>"+idArr.length+ "</font>条数据吗？",function (r) {
                    if(r){
                        $.post("/InspectionFrequencyOperation/delInspectionFrequency.action",{hlparam:idArrs},function (data) {
                            if(data.success){
                                $("#InspectionFrequencyDatagrids").datagrid("reload");
                            }
                            hlAlertFour(data.message);
                        },"json");
                    }
                });
            }else{
                hlAlertOne();
            }
        }
        function editInspectionFrequency(){
            $('#hlcancelBtn').attr('operationtype','edit');
            var row = $('#InspectionFrequencyDatagrids').datagrid('getSelected');
            if(row){
                $('#hlInspectionFrequencyDialog').dialog('open').dialog('setTitle','修改');
                $('#InspectionFrequencyForm').form('load',row);
                $("#InspectionFrequencyId").text(row.id);
                var lasttime=formatterdate(row.last_update_time);
                $("#lastupdatetime").text(lasttime);
                url="/InspectionFrequencyOperation/saveInspectionFrequency.action?id="+row.id;
            }else{
                hlAlertTwo();
            }
        }
        function searchInspectionFrequency() {
            $('#InspectionFrequencyDatagrids').datagrid('load',{
                'inspection_frequency_no': $('#search_inspectionfrequencyno').val()
            });
        }
        function InspectionFrequencyFormSubmit() {
            $('#InspectionFrequencyForm').form('submit',{
                url:url,
                onSubmit:function () {

                    if($("input[name='inspection_frequency_no']").val()==""){

                        hlAlertFour("请输入检验频率标准编号");
                        return false;
                    }



                    setParams();
                },
                success: function(result){
                    var result = eval('('+result+')');
                    $('#hlInspectionFrequencyDialog').dialog('close');
                    if (result.success){
                        $('#InspectionFrequencyDatagrids').datagrid('reload');
                    }
                    hlAlertFour(result.message);
                },
                error:function () {
                    //clearFormLabel();
                    hlAlertThree();
                }
            });

        }
        function InspectionFrequencyCancelSubmit() {
            $('#hlInspectionFrequencyDialog').dialog('close');
        }
        function setParams() {
            setParamsInit($("input[name='od_room_temp_freq']"));
            setParamsInit($("input[name='od_relative_humidity_freq']"));
            setParamsInit($("input[name='od_salt_contamination_before_blast_freq']"));

            setParamsInit($("input[name='od_salt_contamination_after_blasting_freq']"));
            setParamsInit($("input[name='od_preheat_temp_freq']"));
            setParamsInit($("input[name='od_blast_finish_sa25_freq']"));
            setParamsInit($("input[name='od_surface_dust_rating_freq']"));
            setParamsInit($("input[name='od_profile_freq']"));
            setParamsInit($("input[name='od_pipe_temp_after_blast_freq']"));

            setParamsInit($("input[name='od_application_temp_freq']"));
            setParamsInit($("input[name='od_base_2fbe_coat_thickness_freq']"));
            setParamsInit($("input[name='od_top_2fbe_coat_thickness_freq']"));
            setParamsInit($("input[name='od_total_2fbe_coat_thickness_freq']"));
            setParamsInit($("input[name='od_top_3lpe_coat_thickness_freq']"));

            setParamsInit($("input[name='od_middle_3lpe_coat_thickness_freq']"));
            setParamsInit($("input[name='od_base_3lpe_coat_thickness_freq']"));
            setParamsInit($("input[name='od_total_3lpe_coat_thickness_freq']"));
            setParamsInit($("input[name='od_cutback_freq']"));
            setParamsInit($("input[name='od_epoxy_cutback_freq']"));

            setParamsInit($("input[name='od_magnetism_freq']"));
            setParamsInit($("input[name='od_coating_bevel_angle_freq']"));
            setParamsInit($("input[name='od_holiday_freq']"));
            setParamsInit($("input[name='od_abrasive_conductivity_freq']"));
            setParamsInit($("input[name='od_rinse_water_conductivity_freq']"));

            setParamsInit($("input[name='od_adhesion_rating_freq']"));
            setParamsInit($("input[name='id_room_temp_freq']"));
            setParamsInit($("input[name='id_relative_humidity_freq']"));
            setParamsInit($("input[name='id_blast_finish_sa25_freq']"));
            setParamsInit($("input[name='id_profile_freq']"));

            setParamsInit($("input[name='id_dry_film_thickness_freq']"));
            setParamsInit($("input[name='id_cutback_freq']"));
            setParamsInit($("input[name='id_residual_magnetism_freq']"));
            setParamsInit($("input[name='id_wet_film_thickness_freq']"));
            setParamsInit($("input[name='id_salt_contamination_before_blast_freq']"));

            setParamsInit($("input[name='id_salt_contamination_after_blasting_freq']"));
            setParamsInit($("input[name='id_surface_dust_rating_freq']"));
            setParamsInit($("input[name='id_holiday_freq']"));
            setParamsInit($("input[name='id_roughness_freq']"));
            setParamsInit($("input[name='id_pipe_temp_freq']"));
            setParamsInit($("input[name='id_abrasive_conductivity_freq']"));

        }
        function  setParamsInit($obj) {
            if($obj.val()==null||$obj.val()=="")
                $obj.val(0);
        }

        function  clearFormLabel(){
            $('#InspectionFrequencyForm').form('clear');
            $('.hl-label').text('');
        }
    </script>





</head>

<body>
<fieldset class="b3" style="padding:10px;margin:10px;">
    <legend> <h3><b style="color: orange" >|&nbsp;</b><span class="i18n1" name="datadisplay">数据展示</span></h3></legend>
    <div  style="margin-top:5px;">
        <table class="easyui-datagrid" id="InspectionFrequencyDatagrids" url="/InspectionFrequencyOperation/getAllInspectionFrequencyByLike.action" striped="true" loadMsg="正在加载中。。。" textField="text" pageSize="20" fitColumns="true" pagination="true" toolbar="#hlInspectionFrequencyTb">
            <thead>
            <tr>
                <th data-options="field:'ck',checkbox:true"></th>

                <th field="id" align="center" width="100" class="i18n1" name="id">流水号</th>
                <th field="inspection_frequency_no" align="center" width="150" class="i18n1" name="inspectionfrequencyno">检验频率标准编号</th>
                <th field="od_room_temp_freq" align="center" width="150" class="i18n1" name="odroomtempfreq">外防室内温度检验频率</th>
                <th field="od_relative_humidity_freq" align="center" width="150" class="i18n1" name="odrelativehumidityfreq">外防相对湿度检验频率</th>

                <th field="od_salt_contamination_before_blast_freq" align="center" width="150" class="i18n1" name="odsaltcontaminationbeforeblastfreq">外防打砂前盐度检验频率</th>
                <th field="od_salt_contamination_after_blasting_freq" align="center" width="150" class="i18n1" name="odsaltcontaminationafterblastingfreq">外防打砂后盐度检验频率</th>

                <th field="od_preheat_temp_freq" align="center" width="150" class="i18n1" name="odpreheattempfreq">外防预热温度检验频率</th>
                <th field="od_blast_finish_sa25_freq" align="center" width="150" class="i18n1" name="odblastfinishsa25freq">外防Sa2.5检验频率</th>
                <th field="od_surface_dust_rating_freq" align="center" width="150" class="i18n1" name="odsurfacedustratingfreq">外防表面灰尘度检验频率</th>

                <th field="od_profile_freq" align="center" width="150" class="i18n1" name="odprofilefreq">外防锚纹深度检验频率</th>
                <th field="od_pipe_temp_after_blast_freq" align="center" width="150" class="i18n1" name="odpipetempafterblastfreq">外防打砂后管体温度检验频率</th>
                <th field="od_application_temp_freq" align="center" width="150" class="i18n1" name="odapplicationtempfreq">外防中频温度检验频率</th>
                <th field="od_base_2fbe_coat_thickness_freq" align="center" width="150" class="i18n1" name="odbase2fbecoatthicknessfreq">外防2FBE底层厚度检验频率</th>

                <th field="od_top_2fbe_coat_thickness_freq" align="center" width="150" class="i18n1" name="odtop2fbecoatthicknessfreq">外防2FBE面层厚度检验频率</th>
                <th field="od_total_2fbe_coat_thickness_freq" align="center" width="150" class="i18n1" name="odtotal2fbecoatthicknessfreq">外防2FBE总厚度检验频率</th>
                <th field="od_top_3lpe_coat_thickness_freq" align="center" width="150" class="i18n1" name="odtop3lpecoatthicknessfreq">外防3LPE面层厚度检验频率</th>
                <th field="od_middle_3lpe_coat_thickness_freq" align="center" width="150" class="i18n1" name="odmiddle3lpecoatthicknessfreq">外防3LPE中间层厚度检验频率</th>

                <th field="od_base_3lpe_coat_thickness_freq" align="center" width="150" class="i18n1" name="odbase3lpecoatthicknessfreq">外防3LPE底层厚度检验频率</th>
                <th field="od_total_3lpe_coat_thickness_freq" align="center" width="150" class="i18n1" name="odtotal3lpecoatthicknessfreq">外防3LPE总厚度检验频率</th>
                <th field="od_cutback_freq" align="center" width="150" class="i18n1" name="odcutbackfreq">外防预留段长度检验频率</th>
                <th field="od_epoxy_cutback_freq" align="center" width="150" class="i18n1" name="odepoxycutbackfreq">外防粉末长度长度检验频率</th>

                <th field="od_magnetism_freq" align="center" width="150" class="i18n1" name="odmagnetismfreq">外防剩磁检验频率</th>
                <th field="od_coating_bevel_angle_freq" align="center" width="150" class="i18n1" name="odcoatingbevelanglefreq">外防涂层倒角检验频率</th>
                <th field="od_holiday_freq" align="center" width="150" class="i18n1" name="odholidayfreq">外防漏点检验频率</th>

                <th field="od_abrasive_conductivity_freq" align="center" width="150" class="i18n1" name="odabrasiveconductivityfreq">外防磨料传导性检验频率</th>

                <th field="od_rinse_water_conductivity_freq" align="center" width="150" class="i18n1" name="odrinsewaterconductivityfreq">外防冲洗水传导性检验频率</th>

                <th field="od_adhesion_rating_freq" align="center" width="150" class="i18n1" name="odadhesionratingfreq">外防附着力等级检验频率</th>
                <th field="id_room_temp_freq" align="center" width="150" class="i18n1" name="idroomtempfreq">内防室内温度测试频率</th>
                <th field="id_relative_humidity_freq" align="center" width="150" class="i18n1" name="idrelativehumidityfreq">内防相对湿度检验频率</th>
                <th field="id_blast_finish_sa25_freq" align="center" width="150" class="i18n1" name="idblastfinishsa25freq">内防清洁度sa2.5检验频率</th>

                <th field="id_profile_freq" align="center" width="150" class="i18n1" name="idprofilefreq">内防内锚纹深度检验频率</th>
                <th field="id_dry_film_thickness_freq" align="center" width="150" class="i18n1" name="iddryfilmthicknessfreq">内防干膜厚度检验频率</th>
                <th field="id_cutback_freq" align="center" width="150" class="i18n1" name="idcutbackfreq">内防预留段检验频率</th>
                <th field="id_residual_magnetism_freq" align="center" width="150" class="i18n1" name="idresidualmagnetismfreq">内防剩磁检验频率</th>


                <th field="id_wet_film_thickness_freq" align="center" width="150" class="i18n1" name="idwetfilmthicknessfreq">内防湿膜厚度检验频率</th>
                <th field="id_salt_contamination_before_blast_freq" align="center" width="150" class="i18n1" name="idsaltcontaminationbeforeblastfreq">内防打砂前盐度检验频率</th>
                <th field="id_salt_contamination_after_blasting_freq" align="center" width="150" class="i18n1" name="idsaltcontaminationafterblastingfreq">内防打砂后盐度检验频率</th>
                <th field="id_surface_dust_rating_freq" align="center" width="150" class="i18n1" name="idsurfacedustratingfreq">内防灰尘度检验频率</th>

                <th field="id_holiday_freq" align="center" width="150" class="i18n1" name="idholidayfreq">内防漏点检验频率</th>
                <th field="id_roughness_freq" align="center" width="150" class="i18n1" name="idroughnessfreq">内防粗糙度检验频率</th>
                <th field="id_pipe_temp_freq" align="center" width="150" class="i18n1" name="idpipetempfreq">内防钢管温度检验频率</th>
                <th field="id_abrasive_conductivity_freq" align="center" width="150" class="i18n1" name="idabrasiveconductivityfreq">内防磨料传导性检验频率</th>


            </tr>
            </thead>
        </table>
    </div>
</fieldset>

<!--工具栏-->
<div id="hlInspectionFrequencyTb" style="padding:10px;">
    <span class="i18n1" name="inspectionfrequencyno">检验频率标准编号</span>:
    <input id="search_inspectionfrequencyno" name="search_inspectionfrequencyno" style="line-height:22px;border:1px solid #ccc">
    <a href="#" class="easyui-linkbutton" plain="true" data-options="iconCls:'icon-search'" onclick="searchInspectionFrequency()">Search</a>
    <div style="float:right">
        <a href="#" id="addObpLinkBtn" class="easyui-linkbutton i18n1" name="add" data-options="iconCls:'icon-add',plain:true" onclick="addInspectionFrequency()">添加</a>
        <a href="#" id="editObpLinkBtn" class="easyui-linkbutton i18n1" name="edit" data-options="iconCls:'icon-edit',plain:true" onclick="editInspectionFrequency()">修改</a>
        <a href="#" id="deltObpLinkBtn" class="easyui-linkbutton i18n1" name="delete" data-options="iconCls:'icon-remove',plain:true" onclick="delInspectionFrequency()">删除</a>
    </div>
</div>

<!--添加、修改框-->
<div id="hlInspectionFrequencyDialog" class="easyui-dialog" data-options="title:'添加',modal:true"  closed="true" buttons="#dlg-buttons" style="display: none;padding:5px;width:950px;max-height:500px;overflow-y:auto;">
    <form id="InspectionFrequencyForm" method="post">
        <fieldset style="width:900px;border:solid 1px #aaa;margin-top:8px;position:relative;">
            <legend>涂层检验频率标准信息</legend>
            <table class="ht-table"  width="100%" border="0">
                <tr>
                    <td class="i18n1" name="id">流水号</td>
                    <td colspan="1">
                        <label id="InspectionFrequencyId" class="hl-label"></label>
                    </td>
                    <td></td>
                    <td class="i18n1" name="inspectionfrequencyno">检验频率标准编号</td>
                    <td colspan="1"><input class="easyui-textbox"  type="text" name="inspection_frequency_no" value=""/></td>
                    <td></td>
                </tr>

                <tr>
                    <td class="i18n1" colspan="6" name="odblastcontrolparameter">外打砂控制参数</td>

                </tr>

                <tr>
                    <td class="i18n1" name="odrinsewaterconductivityfreq">冲洗水电导率检验频率</td>
                    <td  ><select class="easyui-combobox" data-options="editable:false" name="od_rinse_water_conductivity_freq" style="width:200px;">
                        <option value="0">每0小时检验1次（每根）</option>
                        <option value="1">每1小时检验一次</option>
                        <option value="2">每2小时检验一次</option>
                        <option value="3">每3小时检验一次</option>
                        <option value="4">每4小时检验一次</option>
                        <option value="5">每5小时检验一次</option>
                        <option value="6">每6小时检验一次</option>
                        <option value="7">每7小时检验一次</option>
                        <option value="8">每8小时检验一次</option>
                        <option value="9">每9小时检验一次</option>
                        <option value="10">每10小时检验一次</option>
                        <option value="11">每11小时检验一次</option>
                        <option value="12">每12小时检验一次（每班）</option>
                        <option value="24">每24小时检验一次（每天）</option>
                    </select></td>
                    <td></td>
                    <td class="i18n1" name="odabrasiveconductivityfreq">磨料电导率检验频率</td>
                    <td><select class="easyui-combobox" data-options="editable:false" name="od_abrasive_conductivity_freq" style="width:200px;">
                        <option value="0">每0小时检验1次（每根）</option>
                        <option value="1">每1小时检验一次</option>
                        <option value="2">每2小时检验一次</option>
                        <option value="3">每3小时检验一次</option>
                        <option value="4">每4小时检验一次</option>
                        <option value="5">每5小时检验一次</option>
                        <option value="6">每6小时检验一次</option>
                        <option value="7">每7小时检验一次</option>
                        <option value="8">每8小时检验一次</option>
                        <option value="9">每9小时检验一次</option>
                        <option value="10">每10小时检验一次</option>
                        <option value="11">每11小时检验一次</option>
                        <option value="12">每12小时检验一次（每班）</option>
                        <option value="24">每24小时检验一次（每天）</option>
                    </select></td>
                    <td></td>
                </tr>


                <tr>
                    <td class="i18n1" name="odsaltcontaminationbeforeblastfreq">打砂前盐度检验频率</td>
                    <td ><select class="easyui-combobox" data-options="editable:false" name="od_salt_contamination_before_blast_freq" style="width:200px;">
                        <option value="0">每0小时检验1次（每根）</option>
                        <option value="1">每1小时检验一次</option>
                        <option value="2">每2小时检验一次</option>
                        <option value="3">每3小时检验一次</option>
                        <option value="4">每4小时检验一次</option>
                        <option value="5">每5小时检验一次</option>
                        <option value="6">每6小时检验一次</option>
                        <option value="7">每7小时检验一次</option>
                        <option value="8">每8小时检验一次</option>
                        <option value="9">每9小时检验一次</option>
                        <option value="10">每10小时检验一次</option>
                        <option value="11">每11小时检验一次</option>
                        <option value="12">每12小时检验一次（每班）</option>
                        <option value="24">每24小时检验一次（每天）</option>
                    </select></td>
                    <td></td>
                    <td class="i18n1" name="odpreheattempfreq">预热钢管温度检验频率</td>
                    <td ><select class="easyui-combobox" data-options="editable:false" name="od_preheat_temp_freq" style="width:200px;">
                        <option value="0">每0小时检验1次（每根）</option>
                        <option value="1">每1小时检验一次</option>
                        <option value="2">每2小时检验一次</option>
                        <option value="3">每3小时检验一次</option>
                        <option value="4">每4小时检验一次</option>
                        <option value="5">每5小时检验一次</option>
                        <option value="6">每6小时检验一次</option>
                        <option value="7">每7小时检验一次</option>
                        <option value="8">每8小时检验一次</option>
                        <option value="9">每9小时检验一次</option>
                        <option value="10">每10小时检验一次</option>
                        <option value="11">每11小时检验一次</option>
                        <option value="12">每12小时检验一次（每班）</option>
                        <option value="24">每24小时检验一次（每天）</option>
                    </select></td>
                    <td></td>
                </tr>

                <tr>
                    <td class="i18n1" name="odrelativehumidityfreq">相对湿度检验频率</td>
                    <td  ><select class="easyui-combobox" data-options="editable:false" name="od_relative_humidity_freq" style="width:200px;">
                        <option value="0">每0小时检验1次（每根）</option>
                        <option value="1">每1小时检验一次</option>
                        <option value="2">每2小时检验一次</option>
                        <option value="3">每3小时检验一次</option>
                        <option value="4">每4小时检验一次</option>
                        <option value="5">每5小时检验一次</option>
                        <option value="6">每6小时检验一次</option>
                        <option value="7">每7小时检验一次</option>
                        <option value="8">每8小时检验一次</option>
                        <option value="9">每9小时检验一次</option>
                        <option value="10">每10小时检验一次</option>
                        <option value="11">每11小时检验一次</option>
                        <option value="12">每12小时检验一次（每班）</option>
                        <option value="24">每24小时检验一次（每天）</option>
                    </select></td>
                    <td></td>
                    <td class="i18n1" name="odblastfinishsa25freq">打砂后清洁度Sa2.5检验频率</td>
                    <td ><select class="easyui-combobox" data-options="editable:false" name="od_blast_finish_sa25_freq" style="width:200px;">
                        <option value="0">每0小时检验1次（每根）</option>
                        <option value="1">每1小时检验一次</option>
                        <option value="2">每2小时检验一次</option>
                        <option value="3">每3小时检验一次</option>
                        <option value="4">每4小时检验一次</option>
                        <option value="5">每5小时检验一次</option>
                        <option value="6">每6小时检验一次</option>
                        <option value="7">每7小时检验一次</option>
                        <option value="8">每8小时检验一次</option>
                        <option value="9">每9小时检验一次</option>
                        <option value="10">每10小时检验一次</option>
                        <option value="11">每11小时检验一次</option>
                        <option value="12">每12小时检验一次（每班）</option>
                        <option value="24">每24小时检验一次（每天）</option>
                    </select></td>
                    <td></td>
                </tr>

                <tr>
                    <td class="i18n1" name="odsurfacedustratingfreq">外防灰尘度检验频率</td>
                    <td ><select class="easyui-combobox" data-options="editable:false" name="od_surface_dust_rating_freq" style="width:200px;">
                        <option value="0">每0小时检验1次（每根）</option>
                        <option value="1">每1小时检验一次</option>
                        <option value="2">每2小时检验一次</option>
                        <option value="3">每3小时检验一次</option>
                        <option value="4">每4小时检验一次</option>
                        <option value="5">每5小时检验一次</option>
                        <option value="6">每6小时检验一次</option>
                        <option value="7">每7小时检验一次</option>
                        <option value="8">每8小时检验一次</option>
                        <option value="9">每9小时检验一次</option>
                        <option value="10">每10小时检验一次</option>
                        <option value="11">每11小时检验一次</option>
                        <option value="12">每12小时检验一次（每班）</option>
                        <option value="24">每24小时检验一次（每天）</option>
                    </select></td>
                    <td></td>

                </tr>


                <tr>
                    <td class="i18n1" name="odprofilefreq">外防外锚纹深度检验频率</td>
                    <td ><select class="easyui-combobox" data-options="editable:false" name="od_profile_freq" style="width:200px;">
                        <option value="0">每0小时检验1次（每根）</option>
                        <option value="1">每1小时检验一次</option>
                        <option value="2">每2小时检验一次</option>
                        <option value="3">每3小时检验一次</option>
                        <option value="4">每4小时检验一次</option>
                        <option value="5">每5小时检验一次</option>
                        <option value="6">每6小时检验一次</option>
                        <option value="7">每7小时检验一次</option>
                        <option value="8">每8小时检验一次</option>
                        <option value="9">每9小时检验一次</option>
                        <option value="10">每10小时检验一次</option>
                        <option value="11">每11小时检验一次</option>
                        <option value="12">每12小时检验一次（每班）</option>
                        <option value="24">每24小时检验一次（每天）</option>
                    </select></td>
                    <td></td>

                </tr>

                <tr>
                    <td class="i18n1" name="odsaltcontaminationafterblastingfreq">打砂后盐度最大值</td>
                    <td ><select class="easyui-combobox" data-options="editable:false" name="od_salt_contamination_after_blasting_freq" style="width:200px;">
                        <option value="0">每0小时检验1次（每根）</option>
                        <option value="1">每1小时检验一次</option>
                        <option value="2">每2小时检验一次</option>
                        <option value="3">每3小时检验一次</option>
                        <option value="4">每4小时检验一次</option>
                        <option value="5">每5小时检验一次</option>
                        <option value="6">每6小时检验一次</option>
                        <option value="7">每7小时检验一次</option>
                        <option value="8">每8小时检验一次</option>
                        <option value="9">每9小时检验一次</option>
                        <option value="10">每10小时检验一次</option>
                        <option value="11">每11小时检验一次</option>
                        <option value="12">每12小时检验一次（每班）</option>
                        <option value="24">每24小时检验一次（每天）</option>
                    </select></td>
                    <td></td>
                    <td class="i18n1" name="odpipetempafterblastfreq">打砂后钢管温度检验频率</td>
                    <td ><select class="easyui-combobox" data-options="editable:false" name="od_pipe_temp_after_blast_freq" style="width:200px;">
                        <option value="0">每0小时检验1次（每根）</option>
                        <option value="1">每1小时检验一次</option>
                        <option value="2">每2小时检验一次</option>
                        <option value="3">每3小时检验一次</option>
                        <option value="4">每4小时检验一次</option>
                        <option value="5">每5小时检验一次</option>
                        <option value="6">每6小时检验一次</option>
                        <option value="7">每7小时检验一次</option>
                        <option value="8">每8小时检验一次</option>
                        <option value="9">每9小时检验一次</option>
                        <option value="10">每10小时检验一次</option>
                        <option value="11">每11小时检验一次</option>
                        <option value="12">每12小时检验一次（每班）</option>
                        <option value="24">每24小时检验一次（每天）</option>
                    </select></td>
                    <td></td>
                </tr>
                <tr>
                    <td class="i18n1" colspan="6" name="odcoatingcontrolparameter">外喷涂控制参数</td>

                </tr>
                <tr>
                    <td class="i18n1" name="odapplicationtempfreq">中频温度检验频率</td>
                    <td ><select class="easyui-combobox" data-options="editable:false" name="od_application_temp_freq" style="width:200px;">
                        <option value="0">每0小时检验1次（每根）</option>
                        <option value="1">每1小时检验一次</option>
                        <option value="2">每2小时检验一次</option>
                        <option value="3">每3小时检验一次</option>
                        <option value="4">每4小时检验一次</option>
                        <option value="5">每5小时检验一次</option>
                        <option value="6">每6小时检验一次</option>
                        <option value="7">每7小时检验一次</option>
                        <option value="8">每8小时检验一次</option>
                        <option value="9">每9小时检验一次</option>
                        <option value="10">每10小时检验一次</option>
                        <option value="11">每11小时检验一次</option>
                        <option value="12">每12小时检验一次（每班）</option>
                        <option value="24">每24小时检验一次（每天）</option>
                    </select></td>
                    <td></td>

                </tr>
                <tr>
                    <td class="i18n1" colspan="6" name="2fbeodcoatingcontrolparameter">2FBE外涂检验控制参数</td>
                </tr>

                <tr>
                    <td class="i18n1" name="odbase2fbecoatthicknessfreq">底层2fbe 厚度检验频率</td>
                    <td ><select class="easyui-combobox" data-options="editable:false" name="od_base_2fbe_coat_thickness_freq" style="width:200px;">
                        <option value="0">每0小时检验1次（每根）</option>
                        <option value="1">每1小时检验一次</option>
                        <option value="2">每2小时检验一次</option>
                        <option value="3">每3小时检验一次</option>
                        <option value="4">每4小时检验一次</option>
                        <option value="5">每5小时检验一次</option>
                        <option value="6">每6小时检验一次</option>
                        <option value="7">每7小时检验一次</option>
                        <option value="8">每8小时检验一次</option>
                        <option value="9">每9小时检验一次</option>
                        <option value="10">每10小时检验一次</option>
                        <option value="11">每11小时检验一次</option>
                        <option value="12">每12小时检验一次（每班）</option>
                        <option value="24">每24小时检验一次（每天）</option>
                    </select></td>
                    <td></td>

                    <td class="i18n1" name="odtop2fbecoatthicknessfreq">面层2fbe 厚度检验频率</td>
                    <td ><select class="easyui-combobox" data-options="editable:false" name="od_top_2fbe_coat_thickness_freq" style="width:200px;">
                        <option value="0">每0小时检验1次（每根）</option>
                        <option value="1">每1小时检验一次</option>
                        <option value="2">每2小时检验一次</option>
                        <option value="3">每3小时检验一次</option>
                        <option value="4">每4小时检验一次</option>
                        <option value="5">每5小时检验一次</option>
                        <option value="6">每6小时检验一次</option>
                        <option value="7">每7小时检验一次</option>
                        <option value="8">每8小时检验一次</option>
                        <option value="9">每9小时检验一次</option>
                        <option value="10">每10小时检验一次</option>
                        <option value="11">每11小时检验一次</option>
                        <option value="12">每12小时检验一次（每班）</option>
                        <option value="24">每24小时检验一次（每天）</option>
                    </select></td>
                    <td></td>
                </tr>
                <tr>
                    <td class="i18n1" name="odtotal2fbecoatthicknessfreq">2fbe总厚度检验频率</td>
                    <td ><select class="easyui-combobox" data-options="editable:false" name="od_total_2fbe_coat_thickness_freq" style="width:200px;">
                        <option value="0">每0小时检验1次（每根）</option>
                        <option value="1">每1小时检验一次</option>
                        <option value="2">每2小时检验一次</option>
                        <option value="3">每3小时检验一次</option>
                        <option value="4">每4小时检验一次</option>
                        <option value="5">每5小时检验一次</option>
                        <option value="6">每6小时检验一次</option>
                        <option value="7">每7小时检验一次</option>
                        <option value="8">每8小时检验一次</option>
                        <option value="9">每9小时检验一次</option>
                        <option value="10">每10小时检验一次</option>
                        <option value="11">每11小时检验一次</option>
                        <option value="12">每12小时检验一次（每班）</option>
                        <option value="24">每24小时检验一次（每天）</option>
                    </select></td>
                    <td></td>

                </tr>


                <tr>
                    <td class="i18n1" colspan="6" name="3lpeodcoatingcontrolparameter">3LPE外涂检验控制参数</td>
                </tr>

                <tr>
                    <td class="i18n1" name="odtop3lpecoatthicknessfreq">3lpe面层厚度检验频率</td>
                    <td ><select class="easyui-combobox" data-options="editable:false" name="od_top_3lpe_coat_thickness_freq" style="width:200px;">
                        <option value="0">每0小时检验1次（每根）</option>
                        <option value="1">每1小时检验一次</option>
                        <option value="2">每2小时检验一次</option>
                        <option value="3">每3小时检验一次</option>
                        <option value="4">每4小时检验一次</option>
                        <option value="5">每5小时检验一次</option>
                        <option value="6">每6小时检验一次</option>
                        <option value="7">每7小时检验一次</option>
                        <option value="8">每8小时检验一次</option>
                        <option value="9">每9小时检验一次</option>
                        <option value="10">每10小时检验一次</option>
                        <option value="11">每11小时检验一次</option>
                        <option value="12">每12小时检验一次（每班）</option>
                        <option value="24">每24小时检验一次（每天）</option>
                    </select></td>
                    <td></td>

                    <td class="i18n1" name="odmiddle3lpecoatthicknessfreq">3lpe中间层厚度检验频率</td>
                    <td ><select class="easyui-combobox" data-options="editable:false" name="od_middle_3lpe_coat_thickness_freq" style="width:200px;">
                        <option value="0">每0小时检验1次（每根）</option>
                        <option value="1">每1小时检验一次</option>
                        <option value="2">每2小时检验一次</option>
                        <option value="3">每3小时检验一次</option>
                        <option value="4">每4小时检验一次</option>
                        <option value="5">每5小时检验一次</option>
                        <option value="6">每6小时检验一次</option>
                        <option value="7">每7小时检验一次</option>
                        <option value="8">每8小时检验一次</option>
                        <option value="9">每9小时检验一次</option>
                        <option value="10">每10小时检验一次</option>
                        <option value="11">每11小时检验一次</option>
                        <option value="12">每12小时检验一次（每班）</option>
                        <option value="24">每24小时检验一次（每天）</option>
                    </select></td>
                    <td></td>
                </tr>

                <tr>

                    <td class="i18n1" name="odbase3lpecoatthicknessfreq">3lpe底层厚度检验频率</td>
                    <td ><select class="easyui-combobox" data-options="editable:false" name="od_base_3lpe_coat_thickness_freq" style="width:200px;">
                        <option value="0">每0小时检验1次（每根）</option>
                        <option value="1">每1小时检验一次</option>
                        <option value="2">每2小时检验一次</option>
                        <option value="3">每3小时检验一次</option>
                        <option value="4">每4小时检验一次</option>
                        <option value="5">每5小时检验一次</option>
                        <option value="6">每6小时检验一次</option>
                        <option value="7">每7小时检验一次</option>
                        <option value="8">每8小时检验一次</option>
                        <option value="9">每9小时检验一次</option>
                        <option value="10">每10小时检验一次</option>
                        <option value="11">每11小时检验一次</option>
                        <option value="12">每12小时检验一次（每班）</option>
                        <option value="24">每24小时检验一次（每天）</option>
                    </select></td>
                    <td></td>

                    <td class="i18n1" name="odtotal3lpecoatthicknessfreq">3lpe总厚度检验频率</td>
                    <td ><select class="easyui-combobox" data-options="editable:false" name="od_total_3lpe_coat_thickness_freq" style="width:200px;">
                        <option value="0">每0小时检验1次（每根）</option>
                        <option value="1">每1小时检验一次</option>
                        <option value="2">每2小时检验一次</option>
                        <option value="3">每3小时检验一次</option>
                        <option value="4">每4小时检验一次</option>
                        <option value="5">每5小时检验一次</option>
                        <option value="6">每6小时检验一次</option>
                        <option value="7">每7小时检验一次</option>
                        <option value="8">每8小时检验一次</option>
                        <option value="9">每9小时检验一次</option>
                        <option value="10">每10小时检验一次</option>
                        <option value="11">每11小时检验一次</option>
                        <option value="12">每12小时检验一次（每班）</option>
                        <option value="24">每24小时检验一次（每天）</option>
                    </select></td>
                    <td></td>
                </tr>



                <tr>

                    <td class="i18n1" name="odholidayfreq">漏点检验频率</td>
                    <td ><select class="easyui-combobox" data-options="editable:false" name="od_holiday_freq" style="width:200px;">
                        <option value="0">每0小时检验1次（每根）</option>
                        <option value="1">每1小时检验一次</option>
                        <option value="2">每2小时检验一次</option>
                        <option value="3">每3小时检验一次</option>
                        <option value="4">每4小时检验一次</option>
                        <option value="5">每5小时检验一次</option>
                        <option value="6">每6小时检验一次</option>
                        <option value="7">每7小时检验一次</option>
                        <option value="8">每8小时检验一次</option>
                        <option value="9">每9小时检验一次</option>
                        <option value="10">每10小时检验一次</option>
                        <option value="11">每11小时检验一次</option>
                        <option value="12">每12小时检验一次（每班）</option>
                        <option value="24">每24小时检验一次（每天）</option>
                    </select></td>
                    <td></td>
                </tr>
                <tr>

                    <td class="i18n1" name="odadhesionratingfreq">外防附着力等级检验频率</td>
                    <td ><select class="easyui-combobox" data-options="editable:false" name="od_adhesion_rating_freq" style="width:200px;">
                        <option value="0">每0小时检验1次（每根）</option>
                        <option value="1">每1小时检验一次</option>
                        <option value="2">每2小时检验一次</option>
                        <option value="3">每3小时检验一次</option>
                        <option value="4">每4小时检验一次</option>
                        <option value="5">每5小时检验一次</option>
                        <option value="6">每6小时检验一次</option>
                        <option value="7">每7小时检验一次</option>
                        <option value="8">每8小时检验一次</option>
                        <option value="9">每9小时检验一次</option>
                        <option value="10">每10小时检验一次</option>
                        <option value="11">每11小时检验一次</option>
                        <option value="12">每12小时检验一次（每班）</option>
                        <option value="24">每24小时检验一次（每天）</option>
                    </select></td>
                    <td></td>
                </tr>

                <tr>
                    <td class="i18n1" colspan="6" name="odfinalcontrolparameter">外防终检控制参数</td>
                </tr>

                <tr>

                    <td class="i18n1" name="odcutbackfreq">预留段检验频率</td>
                    <td ><select class="easyui-combobox" data-options="editable:false" name="od_cutback_freq" style="width:200px;">
                        <option value="0">每0小时检验1次（每根）</option>
                        <option value="1">每1小时检验一次</option>
                        <option value="2">每2小时检验一次</option>
                        <option value="3">每3小时检验一次</option>
                        <option value="4">每4小时检验一次</option>
                        <option value="5">每5小时检验一次</option>
                        <option value="6">每6小时检验一次</option>
                        <option value="7">每7小时检验一次</option>
                        <option value="8">每8小时检验一次</option>
                        <option value="9">每9小时检验一次</option>
                        <option value="10">每10小时检验一次</option>
                        <option value="11">每11小时检验一次</option>
                        <option value="12">每12小时检验一次（每班）</option>
                        <option value="24">每24小时检验一次（每天）</option>
                    </select></td>
                    <td></td>

                    <td class="i18n1" name="odepoxycutbackfreq">粉末长度检验频率</td>
                    <td ><select class="easyui-combobox" data-options="editable:false" name="od_epoxy_cutback_freq" style="width:200px;">
                        <option value="0">每0小时检验1次（每根）</option>
                        <option value="1">每1小时检验一次</option>
                        <option value="2">每2小时检验一次</option>
                        <option value="3">每3小时检验一次</option>
                        <option value="4">每4小时检验一次</option>
                        <option value="5">每5小时检验一次</option>
                        <option value="6">每6小时检验一次</option>
                        <option value="7">每7小时检验一次</option>
                        <option value="8">每8小时检验一次</option>
                        <option value="9">每9小时检验一次</option>
                        <option value="10">每10小时检验一次</option>
                        <option value="11">每11小时检验一次</option>
                        <option value="12">每12小时检验一次（每班）</option>
                        <option value="24">每24小时检验一次（每天）</option>
                    </select></td>
                    <td></td>

                </tr>

                <tr>

                    <td class="i18n1" name="odmagnetismfreq">外防剩磁检验频率</td>
                    <td ><select class="easyui-combobox" data-options="editable:false" name="od_magnetism_freq" style="width:200px;">
                        <option value="0">每0小时检验1次（每根）</option>
                        <option value="1">每1小时检验一次</option>
                        <option value="2">每2小时检验一次</option>
                        <option value="3">每3小时检验一次</option>
                        <option value="4">每4小时检验一次</option>
                        <option value="5">每5小时检验一次</option>
                        <option value="6">每6小时检验一次</option>
                        <option value="7">每7小时检验一次</option>
                        <option value="8">每8小时检验一次</option>
                        <option value="9">每9小时检验一次</option>
                        <option value="10">每10小时检验一次</option>
                        <option value="11">每11小时检验一次</option>
                        <option value="12">每12小时检验一次（每班）</option>
                        <option value="24">每24小时检验一次（每天）</option>
                    </select></td>
                    <td></td>

                    <td class="i18n1" name="odcoatingbevelanglefreq">涂层倒角检验频率</td>
                    <td ><select class="easyui-combobox" data-options="editable:false" name="od_coating_bevel_angle_freq" style="width:200px;">
                        <option value="0">每0小时检验1次（每根）</option>
                        <option value="1">每1小时检验一次</option>
                        <option value="2">每2小时检验一次</option>
                        <option value="3">每3小时检验一次</option>
                        <option value="4">每4小时检验一次</option>
                        <option value="5">每5小时检验一次</option>
                        <option value="6">每6小时检验一次</option>
                        <option value="7">每7小时检验一次</option>
                        <option value="8">每8小时检验一次</option>
                        <option value="9">每9小时检验一次</option>
                        <option value="10">每10小时检验一次</option>
                        <option value="11">每11小时检验一次</option>
                        <option value="12">每12小时检验一次（每班）</option>
                        <option value="24">每24小时检验一次（每天）</option>
                    </select></td>
                    <td></td>


                </tr>

                <tr>
                    <td class="i18n1" colspan="6" name="idblastcontrolparameter">内打砂控制参数</td>

                </tr>
                <tr>
                    <td class="i18n1" name="idabrasiveconductivityfreq">内防磨料传导性检验频率</td>
                    <td ><select class="easyui-combobox" data-options="editable:false" name="id_abrasive_conductivity_freq" style="width:200px;">
                        <option value="0">每0小时检验1次（每根）</option>
                        <option value="1">每1小时检验一次</option>
                        <option value="2">每2小时检验一次</option>
                        <option value="3">每3小时检验一次</option>
                        <option value="4">每4小时检验一次</option>
                        <option value="5">每5小时检验一次</option>
                        <option value="6">每6小时检验一次</option>
                        <option value="7">每7小时检验一次</option>
                        <option value="8">每8小时检验一次</option>
                        <option value="9">每9小时检验一次</option>
                        <option value="10">每10小时检验一次</option>
                        <option value="11">每11小时检验一次</option>
                        <option value="12">每12小时检验一次（每班）</option>
                        <option value="24">每24小时检验一次（每天）</option>
                    </select></td>
                    <td></td>
                    <td class="i18n1" name="idsaltcontaminationbeforeblastfreq">内防打砂前盐度检验频率</td>
                    <td ><select class="easyui-combobox" data-options="editable:false" name="id_salt_contamination_before_blast_freq" style="width:200px;">
                        <option value="0">每0小时检验1次（每根）</option>
                        <option value="1">每1小时检验一次</option>
                        <option value="2">每2小时检验一次</option>
                        <option value="3">每3小时检验一次</option>
                        <option value="4">每4小时检验一次</option>
                        <option value="5">每5小时检验一次</option>
                        <option value="6">每6小时检验一次</option>
                        <option value="7">每7小时检验一次</option>
                        <option value="8">每8小时检验一次</option>
                        <option value="9">每9小时检验一次</option>
                        <option value="10">每10小时检验一次</option>
                        <option value="11">每11小时检验一次</option>
                        <option value="12">每12小时检验一次（每班）</option>
                        <option value="24">每24小时检验一次（每天）</option>
                    </select></td>
                    <td></td>

                </tr>

                <tr>
                    <td class="i18n1" name="idrelativehumidityfreq">内防相对湿度检验频率</td>
                    <td ><select class="easyui-combobox" data-options="editable:false" name="id_relative_humidity_freq" style="width:200px;">
                        <option value="0">每0小时检验1次（每根）</option>
                        <option value="1">每1小时检验一次</option>
                        <option value="2">每2小时检验一次</option>
                        <option value="3">每3小时检验一次</option>
                        <option value="4">每4小时检验一次</option>
                        <option value="5">每5小时检验一次</option>
                        <option value="6">每6小时检验一次</option>
                        <option value="7">每7小时检验一次</option>
                        <option value="8">每8小时检验一次</option>
                        <option value="9">每9小时检验一次</option>
                        <option value="10">每10小时检验一次</option>
                        <option value="11">每11小时检验一次</option>
                        <option value="12">每12小时检验一次（每班）</option>
                        <option value="24">每24小时检验一次（每天）</option>
                    </select></td>
                    <td></td>
                    <td class="i18n1" name="idpipetempfreq">内防钢管温度检验频率</td>
                    <td ><select class="easyui-combobox" data-options="editable:false" name="id_pipe_temp_freq" style="width:200px;">
                        <option value="0">每0小时检验1次（每根）</option>
                        <option value="1">每1小时检验一次</option>
                        <option value="2">每2小时检验一次</option>
                        <option value="3">每3小时检验一次</option>
                        <option value="4">每4小时检验一次</option>
                        <option value="5">每5小时检验一次</option>
                        <option value="6">每6小时检验一次</option>
                        <option value="7">每7小时检验一次</option>
                        <option value="8">每8小时检验一次</option>
                        <option value="9">每9小时检验一次</option>
                        <option value="10">每10小时检验一次</option>
                        <option value="11">每11小时检验一次</option>
                        <option value="12">每12小时检验一次（每班）</option>
                        <option value="24">每24小时检验一次（每天）</option>
                    </select></td>
                    <td></td>

                </tr>

                <tr>
                    <td class="i18n1" name="idroomtempfreq">内防室内温度测试频率</td>
                    <td ><select class="easyui-combobox" data-options="editable:false" name="id_room_temp_freq" style="width:200px;">
                        <option value="0">每0小时检验1次（每根）</option>
                        <option value="1">每1小时检验一次</option>
                        <option value="2">每2小时检验一次</option>
                        <option value="3">每3小时检验一次</option>
                        <option value="4">每4小时检验一次</option>
                        <option value="5">每5小时检验一次</option>
                        <option value="6">每6小时检验一次</option>
                        <option value="7">每7小时检验一次</option>
                        <option value="8">每8小时检验一次</option>
                        <option value="9">每9小时检验一次</option>
                        <option value="10">每10小时检验一次</option>
                        <option value="11">每11小时检验一次</option>
                        <option value="12">每12小时检验一次（每班）</option>
                        <option value="24">每24小时检验一次（每天）</option>
                    </select></td>
                    <td></td>
                    <td class="i18n1" name="idblastfinishsa25freq">内防清洁度sa2.5检验频率</td>
                    <td ><select class="easyui-combobox" data-options="editable:false" name="id_blast_finish_sa25_freq" style="width:200px;">
                        <option value="0">每0小时检验1次（每根）</option>
                        <option value="1">每1小时检验一次</option>
                        <option value="2">每2小时检验一次</option>
                        <option value="3">每3小时检验一次</option>
                        <option value="4">每4小时检验一次</option>
                        <option value="5">每5小时检验一次</option>
                        <option value="6">每6小时检验一次</option>
                        <option value="7">每7小时检验一次</option>
                        <option value="8">每8小时检验一次</option>
                        <option value="9">每9小时检验一次</option>
                        <option value="10">每10小时检验一次</option>
                        <option value="11">每11小时检验一次</option>
                        <option value="12">每12小时检验一次（每班）</option>
                        <option value="24">每24小时检验一次（每天）</option>
                    </select></td>
                    <td></td>

                </tr>

                <tr>
                    <td class="i18n1" name="idsurfacedustratingfreq">内防灰尘度检验频率</td>
                    <td ><select class="easyui-combobox" data-options="editable:false" name="id_surface_dust_rating_freq" style="width:200px;">
                        <option value="0">每0小时检验1次（每根）</option>
                        <option value="1">每1小时检验一次</option>
                        <option value="2">每2小时检验一次</option>
                        <option value="3">每3小时检验一次</option>
                        <option value="4">每4小时检验一次</option>
                        <option value="5">每5小时检验一次</option>
                        <option value="6">每6小时检验一次</option>
                        <option value="7">每7小时检验一次</option>
                        <option value="8">每8小时检验一次</option>
                        <option value="9">每9小时检验一次</option>
                        <option value="10">每10小时检验一次</option>
                        <option value="11">每11小时检验一次</option>
                        <option value="12">每12小时检验一次（每班）</option>
                        <option value="24">每24小时检验一次（每天）</option>
                    </select></td>
                    <td></td>
                    <td class="i18n1" name="idsaltcontaminationafterblastingfreq">内防打砂后盐度检验频率</td>
                    <td ><select class="easyui-combobox" data-options="editable:false" name="id_salt_contamination_after_blasting_freq" style="width:200px;">
                        <option value="0">每0小时检验1次（每根）</option>
                        <option value="1">每1小时检验一次</option>
                        <option value="2">每2小时检验一次</option>
                        <option value="3">每3小时检验一次</option>
                        <option value="4">每4小时检验一次</option>
                        <option value="5">每5小时检验一次</option>
                        <option value="6">每6小时检验一次</option>
                        <option value="7">每7小时检验一次</option>
                        <option value="8">每8小时检验一次</option>
                        <option value="9">每9小时检验一次</option>
                        <option value="10">每10小时检验一次</option>
                        <option value="11">每11小时检验一次</option>
                        <option value="12">每12小时检验一次（每班）</option>
                        <option value="24">每24小时检验一次（每天）</option>
                    </select></td>
                    <td></td>
                </tr>

                <tr>
                    <td class="i18n1" name="idprofilefreq">内防内锚纹深度检验频率</td>
                    <td ><select class="easyui-combobox" data-options="editable:false" name="id_profile_freq" style="width:200px;">
                        <option value="0">每0小时检验1次（每根）</option>
                        <option value="1">每1小时检验一次</option>
                        <option value="2">每2小时检验一次</option>
                        <option value="3">每3小时检验一次</option>
                        <option value="4">每4小时检验一次</option>
                        <option value="5">每5小时检验一次</option>
                        <option value="6">每6小时检验一次</option>
                        <option value="7">每7小时检验一次</option>
                        <option value="8">每8小时检验一次</option>
                        <option value="9">每9小时检验一次</option>
                        <option value="10">每10小时检验一次</option>
                        <option value="11">每11小时检验一次</option>
                        <option value="12">每12小时检验一次（每班）</option>
                        <option value="24">每24小时检验一次（每天）</option>
                    </select></td>
                    <td></td>

                </tr>
                <tr>
                    <td class="i18n1" colspan="6" name="idcoatingcontrolparameter">内喷涂控制参数</td>

                </tr>
                <tr>
                    <td class="i18n1" name="idwetfilmthicknessfreq">内防湿膜厚度检验频率</td>
                    <td ><select class="easyui-combobox" data-options="editable:false" name="id_wet_film_thickness_freq" style="width:200px;">
                        <option value="0">每0小时检验1次（每根）</option>
                        <option value="1">每1小时检验一次</option>
                        <option value="2">每2小时检验一次</option>
                        <option value="3">每3小时检验一次</option>
                        <option value="4">每4小时检验一次</option>
                        <option value="5">每5小时检验一次</option>
                        <option value="6">每6小时检验一次</option>
                        <option value="7">每7小时检验一次</option>
                        <option value="8">每8小时检验一次</option>
                        <option value="9">每9小时检验一次</option>
                        <option value="10">每10小时检验一次</option>
                        <option value="11">每11小时检验一次</option>
                        <option value="12">每12小时检验一次（每班）</option>
                        <option value="24">每24小时检验一次（每天）</option>
                    </select></td>
                    <td></td>

                </tr>

                <tr>
                    <td class="i18n1" colspan="6" name="idfinalcontrolparameter">内防终检控制参数</td>
                </tr>
                <tr>
                    <td class="i18n1" name="iddryfilmthicknessfreq">内防干膜厚度检验频率</td>
                    <td ><select class="easyui-combobox" data-options="editable:false" name="id_dry_film_thickness_freq" style="width:200px;">
                        <option value="0">每0小时检验1次（每根）</option>
                        <option value="1">每1小时检验一次</option>
                        <option value="2">每2小时检验一次</option>
                        <option value="3">每3小时检验一次</option>
                        <option value="4">每4小时检验一次</option>
                        <option value="5">每5小时检验一次</option>
                        <option value="6">每6小时检验一次</option>
                        <option value="7">每7小时检验一次</option>
                        <option value="8">每8小时检验一次</option>
                        <option value="9">每9小时检验一次</option>
                        <option value="10">每10小时检验一次</option>
                        <option value="11">每11小时检验一次</option>
                        <option value="12">每12小时检验一次（每班）</option>
                        <option value="24">每24小时检验一次（每天）</option>
                    </select></td>
                    <td></td>
                    <td class="i18n1" name="idroughnessfreq">内防粗糙度检验频率</td>
                    <td ><select class="easyui-combobox" data-options="editable:false" name="id_roughness_freq" style="width:200px;">
                        <option value="0">每0小时检验1次（每根）</option>
                        <option value="1">每1小时检验一次</option>
                        <option value="2">每2小时检验一次</option>
                        <option value="3">每3小时检验一次</option>
                        <option value="4">每4小时检验一次</option>
                        <option value="5">每5小时检验一次</option>
                        <option value="6">每6小时检验一次</option>
                        <option value="7">每7小时检验一次</option>
                        <option value="8">每8小时检验一次</option>
                        <option value="9">每9小时检验一次</option>
                        <option value="10">每10小时检验一次</option>
                        <option value="11">每11小时检验一次</option>
                        <option value="12">每12小时检验一次（每班）</option>
                        <option value="24">每24小时检验一次（每天）</option>
                    </select></td>
                    <td></td>
                </tr>

                <tr>
                    <td class="i18n1" name="idcutbackfreq">内防预留段检验频率</td>
                    <td ><select class="easyui-combobox" data-options="editable:false" name="id_cutback_freq" style="width:200px;">
                        <option value="0">每0小时检验1次（每根）</option>
                        <option value="1">每1小时检验一次</option>
                        <option value="2">每2小时检验一次</option>
                        <option value="3">每3小时检验一次</option>
                        <option value="4">每4小时检验一次</option>
                        <option value="5">每5小时检验一次</option>
                        <option value="6">每6小时检验一次</option>
                        <option value="7">每7小时检验一次</option>
                        <option value="8">每8小时检验一次</option>
                        <option value="9">每9小时检验一次</option>
                        <option value="10">每10小时检验一次</option>
                        <option value="11">每11小时检验一次</option>
                        <option value="12">每12小时检验一次（每班）</option>
                        <option value="24">每24小时检验一次（每天）</option>
                    </select></td>
                    <td></td>
                    <td class="i18n1" name="idresidualmagnetismfreq">内防剩磁检验频率</td>
                    <td ><select class="easyui-combobox" data-options="editable:false" name="id_residual_magnetism_freq" style="width:200px;">
                        <option value="0">每0小时检验1次（每根）</option>
                        <option value="1">每1小时检验一次</option>
                        <option value="2">每2小时检验一次</option>
                        <option value="3">每3小时检验一次</option>
                        <option value="4">每4小时检验一次</option>
                        <option value="5">每5小时检验一次</option>
                        <option value="6">每6小时检验一次</option>
                        <option value="7">每7小时检验一次</option>
                        <option value="8">每8小时检验一次</option>
                        <option value="9">每9小时检验一次</option>
                        <option value="10">每10小时检验一次</option>
                        <option value="11">每11小时检验一次</option>
                        <option value="12">每12小时检验一次（每班）</option>
                        <option value="24">每24小时检验一次（每天）</option>
                    </select></td>
                    <td></td>
                </tr>


                <tr>
                    <td class="i18n1" name="idholidayfreq">内防漏点检验频率</td>
                    <td ><select class="easyui-combobox" data-options="editable:false" name="id_holiday_freq" style="width:200px;">
                        <option value="0">每0小时检验1次（每根）</option>
                        <option value="1">每1小时检验一次</option>
                        <option value="2">每2小时检验一次</option>
                        <option value="3">每3小时检验一次</option>
                        <option value="4">每4小时检验一次</option>
                        <option value="5">每5小时检验一次</option>
                        <option value="6">每6小时检验一次</option>
                        <option value="7">每7小时检验一次</option>
                        <option value="8">每8小时检验一次</option>
                        <option value="9">每9小时检验一次</option>
                        <option value="10">每10小时检验一次</option>
                        <option value="11">每11小时检验一次</option>
                        <option value="12">每12小时检验一次（每班）</option>
                        <option value="24">每24小时检验一次（每天）</option>
                    </select></td>
                    <td></td>

                </tr>

            </table>
        </fieldset>
    </form>


</div>
<div id="dlg-buttons" align="center" style="width:900px;">
    <a href="#" class="easyui-linkbutton" iconCls="icon-save" onclick="InspectionFrequencyFormSubmit()">Save</a>
    <a href="#" class="easyui-linkbutton" id="hlcancelBtn" operationtype="add" iconCls="icon-cancel" onclick="InspectionFrequencyCancelSubmit()">Cancel</a>
</div>


<script type="text/javascript" src="../easyui/jquery.easyui.min.js"></script>
</body>
</html>
<script type="text/javascript">
    mini.parse();
    var grid= mini.get("multiupload1");

    hlLanguage("../i18n/");
</script>