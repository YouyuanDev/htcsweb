<%--
  Created by IntelliJ IDEA.
  User: kurt
  Date: 2/15/18
  Time: 11:51 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>钢管管理</title>
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
    <style type="text/css">
        table.dataintable {
            margin-top:15px;
            border-collapse:collapse;
            border:1px solid #aaa;
            width:100%;
        }
        table.dataintable th {
            vertical-align:baseline;
            padding:5px 15px 5px 6px;
            background-color:#3F3F3F;
            border:1px solid #3F3F3F;
            text-align:center;
            color:#fff;
        }
        table.dataintable td {
            vertical-align:text-top;
            padding:6px 15px 6px 6px;
            border:1px solid #aaa;
        }
        table.dataintable tr:nth-child(odd) {
            background-color:#F5F5F5;
        }
        table.dataintable tr:nth-child(even) {
            background-color:#fff;
        }
    </style>
    <script type="text/javascript">
        var url;

        function searchPipe() {
            $('#pipeDatagrids').datagrid('load',{
                'project_no': $('#projectno').val(),
                'contract_no': $('#contractno').val(),
                'pipe_no':$('#pipeno').val(),
                'status':$('#pstatus').val()
            });

        }
        $(function () {

            $('#hlPipeDialog').dialog({
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


        function addPipe(){
            $('#hlcancelBtn').attr('operationtype','add');
            $('#hlPipeDialog').dialog('open').dialog('setTitle','新增钢管');
            $('#pipeForm').form('clear');
            url="/pipeinfo/savePipe.action";
        }
        function editPipe() {
            $('#hlcancelBtn').attr('operationtype','edit');
            var row = $('#pipeDatagrids').datagrid('getSelected');
            $('#od_coating_date').text('');
            $('#id_coating_date').text('');
            if(row) {
                $('#hlPipeDialog').dialog('open').dialog('setTitle', '修改');
                $('#pipeForm').form('load', row);
                if(row.shipment_date!=undefined)
                   $('#shipmentDate').datetimebox('setValue',getDate1(row.shipment_date));
                if(row.od_coating_date!=undefined)
                    $('#odcoatingdate').datetimebox('setValue',getDate1(row.od_coating_date));
                    // $('#od_coating_date').text(getDate1(row.od_coating_date));
                if(row.id_coating_date!=undefined)
                    $('#idcoatingdate').datetimebox('setValue',getDate1(row.id_coating_date));
                    // $('#id_coating_date').text(getDate1(row.id_coating_date));
                //通过以上方法无法将pipeid初始化，所以再调用以下方法赋值
                 $('#pipeForm').form('load',{
                     'pipeid':row.id
                 });
                if(row.contract_no!=null)
                    look1.setText(row.contract_no);
                url="/pipeinfo/savePipe.action?id="+row.id;

            }else{
                hlAlertTwo();
            }
        }
        function delPipe() {
            var row = $('#pipeDatagrids').datagrid('getSelections');
            if(row.length>0){
                var idArr=[];
                for (var i=0;i<row.length;i++){
                    idArr.push(row[i].id);
                }
                var idArrs=idArr.join(',');

                $.messager.confirm('系统提示',"您确定要删除这<font color=red>"+idArr.length+ "</font>条数据吗？",function (r) {
                    if(r){
                        $.post(
                            "/pipeinfo/delPipe.action",
                            {"hlparam":idArrs},function (data) {
                                if(data.success){
                                    $("#pipeDatagrids").datagrid("reload");
                                }
                                hlAlertFour(data.message);
                            },"json");
                    }
                });

                //hlAlertFive("/pipeinfo/delPipe.action",idArrs,idArr.length);
                // $.messager.confirm('提示','您确定要删除<font>')
            }else{
                hlAlertOne();
            }
        }
        function GenQRCode(){
            var row = $('#pipeDatagrids').datagrid('getSelections');
            if(row.length>0){
                var idArr=[];
                for (var i=0;i<row.length;i++){
                    idArr.push(row[i].pipe_no);
                }
                var idArrs=idArr.join(',');

                $.messager.confirm('系统提示',"您确定要生成这<font color=red>"+idArr.length+ "</font>条QR码吗？",function (r) {
                    if(r){
                        // $.get(
                        //     "/QrCodeOperation/genQRCode.action",
                        //     {"hlparam":idArrs});
                        var form=$("<form>");//定义一个form表单
                        form.attr("style","display:none");
                        form.attr("target","");
                        form.attr("method","post");//请求类型
                        form.attr("action","/QrCodeOperation/genQRCode.action");//请求地址
                        $("body").append(form);//将表单放置在web中
                        var input1=$("<input>");
                        input1.attr("type","hidden");
                        input1.attr("name","hlparam");
                        input1.attr("value",idArrs);
                        form.append(input1);
                        form.submit();//表单提交

                    }
                });

            }else{
                $.messager.alert('Warning','请选择要生成QR码的钢管!');
            }

        }


        //取消保存
        function PipeFormCancelSubmit() {
            $('#hlPipeDialog').dialog('close');


        }


        //增加或保存钢管信息
        function PipeFormSubmit() {

            $('#pipeForm').form('submit',{
                url:url,
                onSubmit:function () {
                    //表单验证

                    if($("input[name='contract_no']").val()==""){

                        hlAlertFour("请输入合同编号");
                        return false;
                    }
                    else if($("input[name='grade']").val()==""){

                        hlAlertFour("请输入钢种");
                        return false;
                    }
                    else if($("input[name='heat_no']").val()==""){

                        hlAlertFour("请输入炉号");
                        return false;
                    }
                    else if($("input[name='pipe_making_lot_no']").val()==""){

                        hlAlertFour("请输入制管批号");
                        return false;
                    }

                    else if($("input[name='od']").val()==""){
                        hlAlertFour("请输入外径");
                        return false;
                    }
                    else if($("input[name='wt']").val()==""){
                        hlAlertFour("请输入壁厚");
                        return false;
                    }
                    else if($("input[name='p_length']").val()==""){
                        hlAlertFour("请输入长度");
                        return false;

                    }
                    else if($("input[name='weight']").val()==""){
                        hlAlertFour("请输入重量");
                        return false;

                    }
                    else if($("input[name='status']").val()==""){

                        hlAlertFour("请输入状态");
                        return false;
                    }


                    //return $('#pipeForm').form('enableValidation').form('validate');
                },
                success: function(result){
                    var result = eval('('+result+')');
                    if (result.success){
                        $('#hlPipeDialog').dialog('close');
                        $('#pipeDatagrids').datagrid('reload');
                        clearFormLabel();
                        //$('#hl-gallery-con').empty();
                        // $('#pipeDatagrids').datagrid('clearSelections');
                    }
                    hlAlertFour(result.message);
                },
                error:function () {
                    hlAlertThree();
                }
            });

        }

        function  clearFormLabel() {
            $('#pipeForm').form('clear');

        }
         function SearchPipeRecord() {
             $('#pipeRecord-container').empty();
             var row = $('#pipeDatagrids').datagrid('getSelected');
             if(row) {
                 var pipe_no=row.pipe_no;
                 $.ajax({
                     url:'/pipeinfo/searchPipeRecord.action',
                     dataType:'json',
                     data:{pipe_no:pipe_no},
                     success:function (data) {
                         //alert(toString.call(data.success)+":"+data.success);
                         if(!data.success){
                             $.messager.alert('Warning', data.message);
                             return;
                         }
                         //外打砂
                         if(data.odBlastProcessRecord!=undefined&&data.odBlastProcessRecord!=null){
                              var obj=data.odBlastProcessRecord;
                              var result="";
                              if(obj.result=="0")
                                  result="不合格,重新打砂处理";
                              else if(obj.result=="1")
                                  result="合格,进入外喷砂检验工序";
                              else if(obj.result=="2")
                                  result="待定";
                              else if(obj.result=="3")
                                  result="隔离,进入修磨或切割工序";
                              var marking="";
                              if(obj.marking=="0")
                                  marking="清晰";
                              else if(obj.marking=="1")
                                  marking="不清晰";
                              var dic={
                                  "operatorno":obj.operator_no,"operationtime":getDate1(obj.operation_time),"millno":obj.mill_no,
                                   "surfacecondition":obj.surface_condition,"saltcontaminationbeforeblasting":obj.salt_contamination_before_blasting,"alkalinedwelltime":obj.alkaline_dwell_time,
                                  "alkalineconcentration":obj.alkaline_concentration, "acidwashtime":obj.acid_wash_time,"acidconcentration":obj.acid_concentration,
                                  "abrasiveconductivity":obj.abrasive_conductivity,"blastlinespeed":obj.blast_line_speed, "preheattemp":obj.preheat_temp,
                                  "rinsewaterconductivity":obj.rinse_water_conductivity, "marking":marking,"result":result,
                                  "remark":obj.remark
                              };
                              $('#pipeRecord-container').append(recordTemplate("odblastprocess","外打砂记录",dic));
                         }
                         //外打砂检验
                         if(data.odBlastInspectionProcessRecord!=undefined&&data.odBlastInspectionProcessRecord!=null){
                             var obj=data.odBlastInspectionProcessRecord;
                             var result="";
                             if(obj.result=="0")
                                 result="不合格,重新打砂处理";
                             else if(obj.result=="1")
                                 result="合格,进入外涂敷工序";
                             else if(obj.result=="2")
                                 result="待定";
                             else if(obj.result=="3")
                                 result="隔离,进入修磨或切割工序";
                             var oilwaterinaircompressor="";
                             if(obj.oil_water_in_air_compressor=="0")
                                 oilwaterinaircompressor="否";
                             else if(obj.oil_water_in_air_compressor=="1")
                                 oilwaterinaircompressor="是";
                             var dic= {
                                 "operatorno":obj.operator_no,"operationtime":getDate1(obj.operation_time),"millno":obj.mill_no,
                                 "airtemp": obj.air_temp, "relativehumidity":obj.relative_humidity, "dewpoint":obj.dew_point,
                                 "blastfinishsa25":obj.blast_finish_sa25, "profile": obj.profile, "surfacedustrating":obj.surface_dust_rating,
                                 "pipetemp":obj.pipe_temp, "saltcontaminationafterblasting":obj.salt_contamination_after_blasting,"surfacecondition":obj.surface_condition,
                                 "oilwaterinaircompressor":oilwaterinaircompressor, "elapsedtime": obj.elapsed_time,"result":result,
                                 "remark":obj.remark
                             };
                             $('#pipeRecord-container').append(recordTemplate("odblastinspectionprocess","外打砂检验记录",dic));
                         }
                         //外涂2fbe
                         if(data.odCoatingProcessRecord!=undefined&&data.odCoatingProcessRecord!=null){
                             var obj=data.odCoatingProcessRecord;
                             var result="";
                             if(obj.result=="0")
                                 result="不合格,进入外喷砂工序";
                             else if(obj.result=="1")
                                 result="合格,进入外涂敷检验工序";
                             else if(obj.result=="2")
                                 result="待定";
                             var dic={
                                 "operatorno":obj.operator_no,"operationtime":getDate1(obj.operation_time),"millno":obj.mill_no,
                               "coatinglinespeed":obj.coating_line_speed,"basecoatused":obj.base_coat_used, "basecoatlotno":obj.base_coat_lot_no,
                                 "topcoatused":obj.top_coat_used, "topcoatlotno":obj.top_coat_lot_no,"basecoatguncount":obj.base_coat_gun_count,
                              "topcoatguncount":obj.top_coat_gun_count,"applicationtemp":obj.application_temp, "tofirsttouchduration":obj.to_first_touch_duration,
                                 "toquenchduration":obj.to_quench_duration, "elapsedtime":obj.elapsed_time,"airpressure":obj.air_pressure,
                              "coatingvoltage":obj.coating_voltage,"gundistance":obj.gun_distance, "sprayspeed":obj.spray_speed,
                                 "applicationvoltage":obj.application_voltage, "result":result,"":"",
                                 "remark":obj.remark};
                             $('#pipeRecord-container').append(recordTemplate("odcoatingprocess","外涂记录(2FBE)",dic));
                         }
                         //外涂检验2fbe
                         if(data.odCoatingInspectionProcessRecord!=undefined&&data.odCoatingInspectionProcessRecord!=null){
                             var obj=data.odCoatingInspectionProcessRecord;
                             var result="";
                             if(obj.result=="0")
                                 result="不合格,进入待修补工序";
                             else if(obj.result=="1")
                                 result="合格,进入外喷标工序";
                             else if(obj.result=="2")
                                 result="不合格,进入待扒皮工序";
                             else if(obj.result=="3")
                                 result="待定";
                             else if(obj.result=="4")
                                 result="隔离,进入修磨或切割工序";
                             var dic={
                                 "operatorno":obj.operator_no,"operationtime":getDate1(obj.operation_time),"millno":obj.mill_no,
                              "basecoatthicknesslist":obj.base_coat_thickness_list,"topcoatthicknesslist":obj.top_coat_thickness_list, "totalcoatingthicknesslist":obj.total_coating_thickness_list,
                                 "holidays":obj.holidays, "holidaytestervolts":obj.holiday_tester_volts,"repairs":obj.repairs,
                             "bevel":obj.bevel,"surfacecondition":obj.surface_condition, "issample":obj.is_sample,
                                 "adhesionrating":obj.adhesion_rating, "isdscsample":obj.is_dsc_sample, "result":result,
                                 "remark":obj.remark};
                             $('#pipeRecord-container').append(recordTemplate("odcoatinginspectionprocess","外涂检验记录(2FBE)",dic));
                         }
                         //外涂3lpe
                         if(data.odCoating3LpeProcessRecord!=undefined&&data.odCoating3LpeProcessRecord!=null){
                             var obj=data.odCoating3LpeProcessRecord;
                             var result="";
                             if(obj.result=="0")
                                 result="不合格,进入外防喷砂工序";
                             else if(obj.result=="1")
                                 result="合格,进入外涂敷检验工序";
                             else if(obj.result=="2")
                                 result="待定";
                             var dic={
                                 "operatorno":obj.operator_no,"operationtime":getDate1(obj.operation_time),"millno":obj.mill_no,
                              "coatinglinespeed":obj.coating_line_speed,"basecoatused":obj.base_coat_used, "basecoatlotno":obj.base_coat_lot_no,
                                 "middlecoatused":obj.middle_coat_used, "middlecoatlotno":obj.middle_coat_lot_no,"topcoatused":obj.top_coat_used,
                             "topcoatlotno":obj.top_coat_lot_no,"basecoatguncount":obj.base_coat_gun_count, "applicationtemp":obj.application_temp,
                                 "airpressure":obj.air_pressure, "tofirsttouchduration":obj.to_first_touch_duration,"toquenchduration":obj.to_quench_duration,
                             "coatingvoltage":obj.coating_voltage,"gundistance":obj.gun_distance, "sprayspeed":obj.spray_speed,
                                 "applicationvoltage":obj.application_voltage,"result":result,"":"",
                                 "remark":obj.remark};
                             $('#pipeRecord-container').append(recordTemplate("odcoating3lpeprocess","外涂记录(3LPE)",dic));
                         }
                         //外涂检验3lpe
                         if(data.odCoating3LpeInspectionProcessRecord!=undefined&&data.odCoating3LpeInspectionProcessRecord!=null){
                             var obj=data.odCoating3LpeInspectionProcessRecord;
                             var result="";
                             if(obj.result=="0")
                                 result="不合格,进入待修补工序";
                             else if(obj.result=="1")
                                 result="合格,进入外喷标工序";
                             else if(obj.result=="2")
                                 result="不合格,进入待扒皮工序";
                             else if(obj.result=="3")
                                 result="待定";
                             else if(obj.result=="4")
                                 result="隔离,进入修磨或切割工序";
                             var bevel="";
                             if(obj.bevel=="0")
                                 bevel="未检测";
                             else if(obj.bevel=="1")
                                 bevel="合格";
                             else if(obj.bevel=="2")
                                 bevel="不合格";
                             var issample="";
                             if(obj.is_sample=="0")
                                 issample="否";
                             else if(obj.is_sample=="1")
                                 issample="是";
                             var isdscsample="";
                             if(obj.is_dsc_sample=="0")
                                 isdscsample="否";
                             else if(obj.is_dsc_sample=="1")
                                 isdscsample="是";
                             var ispesample="";
                             if(obj.is_pe_sample=="0")
                                 ispesample="否";
                             else if(obj.is_pe_sample=="1")
                                 ispesample="是";
                             var dic={
                                 "operatorno":obj.operator_no,"operationtime":getDate1(obj.operation_time),"millno":obj.mill_no,
                             "basecoatthicknesslist":obj.base_coat_thickness_list,"topcoatthicknesslist":obj.top_coat_thickness_list, "middlecoatthicknesslist":obj.middle_coat_thickness_list,
                             "totalcoatingthicknesslist":obj.total_coating_thickness_list,"holidays":obj.holidays, "holidaytestervolts":obj.holiday_tester_volts,
                                 "repairs":obj.repairs, "bevel":bevel,"surfacecondition":obj.surface_condition,
                             "issample":issample,"adhesionrating":obj.adhesion_rating, "isdscsample":isdscsample,
                                 "ispesample":ispesample, "result":result,"":"",
                                 "remark":obj.remark};
                             $('#pipeRecord-container').append(recordTemplate("odcoating3lpeinspectionprocess","外涂检验记录(3LPE)",dic));
                         }
                         //外喷标
                         if(data.odStencilProcessRecord!=undefined&&data.odStencilProcessRecord!=null){
                             var obj=data.odStencilProcessRecord;
                             var result="";
                             if(obj.result=="0")
                                 result="不合格,重新喷标";
                             else if(obj.result=="1")
                                 result="合格,进入外防终检工序";
                             else if(obj.result=="2")
                                 result="待定";
                             var dic={
                                 "operatorno":obj.operator_no,"operationtime":getDate1(obj.operation_time),"millno":obj.mill_no,
                                 "centerlinecolor":obj.center_line_color, "pipeendcolor":obj.pipe_end_color,"result":result,
                               "stencilcontent":obj.stencil_content,
                                 "remark":obj.remark};
                             $('#pipeRecord-container').append(recordTemplate("odstencilprocess","外喷标记录",dic));
                         }
                         //外防终检
                         if(data.odFinalInspectionProcessRecord!=undefined&&data.odFinalInspectionProcessRecord!=null){
                             var obj=data.odFinalInspectionProcessRecord;
                             var result="";
                             if(obj.result=="0")
                                 result="不合格,进入外防待修补工序";
                             else if(obj.result=="1")
                                 result="合格,进入外防成品入库工序";
                             else if(obj.result=="2")
                                 result="不合格,进入外防待扒皮工序";
                             else if(obj.result=="3")
                                 result="待定";
                             else if(obj.result=="4")
                                 result="不合格,进入外喷标工序";
                             else if(obj.result=="5")
                                 result="隔离，进入修磨或切割工序";
                             var inspectionresult="";
                             if(obj.inspection_result=="0")
                                 inspectionresult="不合格";
                             else if(obj.inspection_result=="1")
                                 inspectionresult="合格";
                             else if(obj.inspection_result=="2")
                                 inspectionresult="待定";
                             var stencilverification="";
                             if(obj.stencil_verification=="0")
                                 stencilverification="未检测";
                             else if(obj.stencil_verification=="1")
                                 stencilverification="合格";
                             else if(obj.stencil_verification=="2")
                                 stencilverification="待定";
                             var cutbacksurface="";
                             if(obj.cutback_surface=="0")
                                 cutbacksurface="合格";
                             else if(obj.cutback_surface=="1")
                                 cutbacksurface="不合格";
                             var dic={
                                 "operatorno":obj.operator_no,"operationtime":getDate1(obj.operation_time),"millno":obj.mill_no,
                                 "inspectionresult":inspectionresult,"cutbacklength":obj.cutback_length, "stencilverification":stencilverification,
                                 "cutbacksurface":cutbacksurface, "magnetismlist":obj.magnetism_list,"coatingbevelanglelist":obj.coating_bevel_angle_list,
                                 "epoxycutbacklist":obj.epoxy_cutback_list, "result":result,"":"",
                                 "remark":obj.remark};
                             $('#pipeRecord-container').append(recordTemplate("odfinalinspectionprocess","外防终检记录",dic));
                         }
                         //内打砂
                         if(data.idBlastProcessRecord!=undefined&&data.idBlastProcessRecord!=null){
                             var obj=data.idBlastProcessRecord;
                             var result="";
                             if(obj.result=="0")
                                 result="不合格,重新喷砂处理";
                             else if(obj.result=="1")
                                 result="合格,进入内喷砂检验工序";
                             else if(obj.result=="2")
                                 result="待定";
                             else if(obj.result=="3")
                                 result="隔离,进入修磨或切割工序";
                             var pipenoupdate="";
                             if(obj.pipe_no_update=="0")
                                 pipenoupdate="是";
                             else if(obj.pipe_no_update=="1")
                                 pipenoupdate="否";
                             var marking="";
                             if(obj.marking=="0")
                                 marking="清晰";
                             else if(obj.marking=="1")
                                 marking="不清晰";

                             var dic={
                                 "operatorno":obj.operator_no,"operationtime":getDate1(obj.operation_time),"millno":obj.mill_no,
                                 "originalpipeno":obj.original_pipe_no,"newpipeno":obj.new_pipe_no,"pipenoupdate":pipenoupdate,
                                 "saltcontaminationbeforeblasting":obj.salt_contamination_before_blasting,"internalsurfacecondition":obj.internal_surface_condition,"externalcoatingcondition":obj.external_coating_condition,
                             "marking":marking,"abrasiveconductivity":obj.abrasive_conductivity,"result":result,
                                 "remark":obj.remark};
                             $('#pipeRecord-container').append(recordTemplate("idblastprocess","内打砂记录",dic));
                         }
                         //内打砂检验
                         if(data.idBlastInspectionProcessRecord!=undefined&&data.idBlastInspectionProcessRecord!=null){
                             var obj=data.idBlastInspectionProcessRecord;
                             var result="";
                             if(obj.result=="0")
                                 result="不合格,重新喷砂处理";
                             else if(obj.result=="1")
                                 result="合格,进入内涂敷工序";
                             else if(obj.result=="2")
                                 result="待定";
                             else if(obj.result=="3")
                                 result="隔离,进入修磨或切割工序";
                             var dic={
                                 "operatorno":obj.operator_no,"operationtime":getDate1(obj.operation_time),"millno":obj.mill_no,
                                 "airtemp":obj.air_temp,"relativehumidity":obj.relative_humidity, "dewpoint":obj.dew_point,
                                 "blastfinishsa25":obj.blast_finish_sa25, "blasttime":obj.blast_time, "profile":obj.profile,
                                 "surfacedustrating":obj.surface_dust_rating, "pipetemp":obj.pipe_temp,"saltcontaminationafterblasting":obj.salt_contamination_after_blasting,
                                 "surfacecondition":obj.surface_condition, "elapsedtime":obj.elapsed_time, "result":result,
                                 "remark":obj.remark};
                             $('#pipeRecord-container').append(recordTemplate("idblastinspectionprocess","内打砂检验记录",dic));
                         }
                         //内涂
                         if(data.idCoatingProcessRecord!=undefined&&data.idCoatingProcessRecord!=null){
                             var obj=data.idCoatingProcessRecord;
                             var result="";
                             if(obj.result=="0")
                                 result="不合格,进入内防喷砂工序";
                             else if(obj.result=="1")
                                 result="合格,进入内涂敷检验工序";
                             else if(obj.result=="2")
                                 result="待定";
                             var dic={
                                 "operatorno":obj.operator_no,"operationtime":getDate1(obj.operation_time),"millno":obj.mill_no,
                                 "coatingspeed":obj.coating_speed,"baseused":obj.base_used, "basebatch":obj.base_batch,
                                 "curingagentused":obj.curing_agent_used, "curingagentbatch":obj.curing_agent_batch,"curingstarttime":getDate1(obj.curing_start_time),
                             "curingfinishtime":getDate1(obj.curing_finish_time),"curingtemp":obj.curing_temp,"result":result,
                                 "remark":obj.remark};
                             $('#pipeRecord-container').append(recordTemplate("idcoatingprocess","内涂记录",dic));
                         }
                         //内涂检验
                         if(data.idCoatingInspectionProcessRecord!=undefined&&data.idCoatingInspectionProcessRecord!=null){
                             var obj=data.idCoatingInspectionProcessRecord;
                             var result="";
                             if(obj.result=="0")
                                 result="不合格,进入待修补工序";
                             else if(obj.result=="1")
                                 result="合格,进入内喷标工序";
                             else if(obj.result=="2")
                                 result="合格,进入待扒皮工序";
                             else if(obj.result=="3")
                                 result="待定";
                             else if(obj.result=="4")
                                 result="隔离,进入修磨或切割工序";
                             var issample="";
                             if(obj.is_sample=="0")
                                 issample="否";
                             else if(obj.is_sample=="1")
                                 issample="是";
                             var isglasssample="";
                             if(obj.is_glass_sample=="0")
                                 isglasssample="否";
                             else if(obj.is_glass_sample=="1")
                                 isglasssample="是";
                             var dic={
                                 "operatorno":obj.operator_no,"operationtime":getDate1(obj.operation_time),"millno":obj.mill_no,
                             "issample":issample,"wetfilmthicknesslist":obj.wet_film_thickness_list, "isglasssample":isglasssample,
                             "result":result, "":""," ":"",
                                 "remark":obj.remark};
                             $('#pipeRecord-container').append(recordTemplate("idcoatinginspectionprocess","内涂检验记录",dic));
                         }
                         //内喷标
                         if(data.idStencilProcessRecord!=undefined&&data.idStencilProcessRecord!=null){
                             var obj=data.idStencilProcessRecord;
                             var result="";
                             if(obj.result=="0")
                                 result="不合格,重新喷标";
                             else if(obj.result=="1")
                                 result="合格,进入内防终检工序";
                             else if(obj.result=="2")
                                 result="待定";
                             var dic={
                                 "operatorno":obj.operator_no,"operationtime":getDate1(obj.operation_time),"millno":obj.mill_no,
                                 "stencilcontent":obj.stencil_content, "result":result,"":"",
                                 "remark":obj.remark};
                             $('#pipeRecord-container').append(recordTemplate("idstencilprocess","内喷标记录",dic));
                         }
                         //内防终检
                         if(data.idFinalInspectionProcess!=undefined&&data.idFinalInspectionProcess!=null){
                             var obj=data.idFinalInspectionProcess;
                             var result="";
                             if(obj.result=="0")
                                 result="不合格,进入内防涂层待修补工序";
                             else if(obj.result=="1")
                                 result="合格,进入内防成品入库工序";
                             else if(obj.result=="2")
                                 result="不合格,进入外防待扒皮工序";
                             else if(obj.result=="3")
                                 result="不合格,进入内防待扒皮工序";
                             else if(obj.result=="4")
                                 result="不合格,进入内喷标工序";
                             else if(obj.result=="5")
                                 result="待定";
                             else if(obj.result=="6")
                                 result="隔离,进入修磨或切割工序";
                             var odinspectionresult="";
                             if(obj.od_inspection_result=="0")
                                 odinspectionresult="不合格";
                             else if(obj.od_inspection_result=="1")
                                 odinspectionresult="合格";
                             else if(obj.od_inspection_result=="2")
                                 odinspectionresult="待定";
                             var idstencilverification="";
                             if(obj.stencil_verification=="0")
                                 idstencilverification="未检测";
                             else if(obj.stencil_verification=="1")
                                 idstencilverification="合格";
                             else if(obj.stencil_verification=="2")
                                 idstencilverification="不合格";
                             var bevelcheck="";
                             if(obj.bevel_check=="0")
                                 bevelcheck="未检测";
                             else if(obj.bevel_check=="1")
                                 bevelcheck="合格";
                             else if(obj.bevel_check=="2")
                                 bevelcheck="不合格";
                             var dic={
                                 "operatorno":obj.operator_no,"operationtime":getDate1(obj.operation_time),"millno":obj.mill_no,
                             "odinspectionresult":odinspectionresult,"dryfilmthicknesslist":obj.dry_film_thickness_list,"cutbacklength":obj.cutback_length,
                                 "stencilverification":idstencilverification,"roughnesslist":obj.roughness_list,"holidaytestervolts":obj.holiday_tester_volts,
                                 "holidays":obj.holidays, "surfacecondition":obj.surface_condition, "bevelcheck":bevelcheck,
                                 "magnetismlist":obj.magnetism_list,"internalrepairs":obj.internal_repairs, "result":result,
                                 "remark":obj.remark};
                             $('#pipeRecord-container').append(recordTemplate("idfinalinspectionprocess","内防终检记录",dic));
                         }
                         //扒皮
                          if(data.coatingStrips!=undefined&&data.coatingStrips!=null&&data.coatingStrips.length>0){
                              var dicField=["operatorno","operationtime","millno","striptemperature","odid","result","remark"];
                              var template = "";
                              template += '<table  title="" class="dataintable" style="width:100%;height:auto;"><thead><tr>';
                              template += '<th class="i18n1" name="coatingstrip" colspan="7">扒皮记录</th>';
                              template +='</tr></thead><tbody><tr>';
                              for(var i=0;i<dicField.length;i++){
                                  template += '<td style="text-align:center;" class="i18n1" name='+dicField[i]+'>'+ dicField[i] + '</td>';
                              }
                              template +='</tr>';
                              var odid="",result="";
                              for(var i=0;i<data.coatingStrips.length;i++){
                                  odid="";result="";
                                  var obj=data.coatingStrips[i];
                                  template +='<tr>';
                                  template +='<td>'+obj.operator_no+'</td>';
                                  template +='<td>'+getDate1(obj.operation_time)+'</td>';
                                  template +='<td>'+obj.mill_no+'</td>';
                                  template +='<td>'+obj.strip_temperature+'</td>';
                                  if(obj.odid=="od")
                                      odid="外防";
                                  else if(obj.odid=="id")
                                      odid="内防";
                                  template +='<td>'+odid+'</td>';
                                  if(obj.result=="0")
                                      result="不合格,重新扒皮";
                                  else if(obj.result=="1")
                                      result="合格,转为光管";
                                  else if(obj.result=="2")
                                      result="待定";
                                  template +='<td>'+result+'</td>';
                                  template +='<td>'+obj.remark+'</td>';
                                  template +='</tr>';
                              }
                              template+='</tbody></table>';
                              $('#pipeRecord-container').append(template);
                          }
                          //修补
                          if(data.coatingRepairs!=undefined&&data.coatingRepairs!=null&&data.coatingRepairs.length>0){
                              var dicField=["operatorno","operationtime","millno","coatingtype","odid","repairsize",
                                  "repairnumber","holidaynumber","repairmethod","unqualifiedreason","inspectorno","inspectiontime",
                                  "surfacecondition","repairthickness","holidaytesting","adhesion",
                                  "result","remark"];
                              var template = "";
                              template += '<table  title="" class="dataintable" style="width:100%;height:auto;"><thead><tr>';
                              template += '<th class="i18n1" name="coatingrepair" colspan="18">修补记录</th>';

                              template +='</tr></thead><tbody><tr>';
                              for(var i=0;i<dicField.length;i++){
                                  template += '<td style="text-align: center;" class="i18n1" name='+dicField[i]+'>'+ dicField[i] + '</td>';
                              }
                              template +='</tr>';
                              var odid="",result="",unqualifiedreason="";
                              for(var i=0;i<data.coatingRepairs.length;i++){
                                  odid="";result="",unqualifiedreason="";
                                  var obj=data.coatingRepairs[i];
                                  template +='<tr>';
                                  template +='<td>'+obj.operator_no+'</td>';
                                  template +='<td>'+getDate1(obj.operation_time)+'</td>';
                                  template +='<td>'+obj.mill_no+'</td>';
                                  template +='<td>'+obj.coating_type+'</td>';
                                  if(obj.odid=="od")
                                      odid="外防";
                                  else if(obj.odid=="id")
                                      odid="内防";
                                  template +='<td>'+odid+'</td>';
                                  template +='<td>'+obj.repair_size+'</td>';
                                  template +='<td>'+obj.repair_number+'</td>';
                                  template +='<td>'+obj.holiday_number+'</td>';
                                  template +='<td>'+obj.repair_method+'</td>';
                                  if(obj.unqualified_reason=="1")
                                      unqualifiedreason="漏点";
                                  else if(obj.unqualified_reason=="2")
                                      unqualifiedreason="碰伤";
                                  else if(obj.unqualified_reason=="3")
                                      unqualifiedreason="厚度不合";
                                  else if(obj.unqualified_reason=="4")
                                      unqualifiedreason="附着力不合";
                                  else if(obj.unqualified_reason=="5")
                                      unqualifiedreason="有杂质";
                                  template +='<td>'+unqualifiedreason+'</td>';
                                  template +='<td>'+obj.inspector_no+'</td>';
                                  template +='<td>'+getDate1(obj.inspection_time)+'</td>';
                                  template +='<td>'+obj.surface_condition+'</td>';
                                  template +='<td>'+obj.repair_thickness+'</td>';
                                  template +='<td>'+obj.holiday_testing+'</td>';
                                  template +='<td>'+obj.adhesion+'</td>';
                                  if(obj.result=="0")
                                      result="不合格,重新修补";
                                  else if(obj.result=="1")
                                      result="修补完成,检验合格";
                                  else if(obj.result=="2")
                                      result="修补完成,待检验";
                                  else if(obj.result=="3")
                                      result="不合格,外防扒皮处理";
                                  else if(obj.result=="4")
                                      result="不合格,内防扒皮处理";
                                  else if(obj.result=="5")
                                      result="待定";
                                  template +='<td>'+result+'</td>';
                                  template +='<td>'+obj.remark+'</td>';
                                  template +='</tr>';
                              }
                              template+='</tbody></table>';
                              $('#pipeRecord-container').append(template);
                         }
                         //修磨切割
                         if(data.barePipeGrindingCutoffRecords!=undefined&&data.barePipeGrindingCutoffRecords!=null&&data.barePipeGrindingCutoffRecords.length>0){
                             var dicField=["operatorno","operationtime","millno","odid",
                                  "grinding_cutoff","remainingwallthicknesslist","cutofflength","originalpipelength","pipelengthaftercut",
                                 "result","remark"];
                             var template = "";
                             template += '<table  title="" class="dataintable" style="width:100%;height:auto;"><thead><tr>';
                             template += '<th class="i18n1" name="barepipegrindingProcess" colspan="11">修磨切割记录</th>';
                             template +='</tr></thead><tbody><tr>';
                             for(var i=0;i<dicField.length;i++){
                                 template += '<td style="text-align: center;" class="i18n1" name='+dicField[i]+'>'+ dicField[i] + '</td>';
                             }
                             template +='</tr>';
                             var odid="",result="",grindingcutoff="";
                             for(var i=0;i<data.barePipeGrindingCutoffRecords.length;i++){
                                 odid="";result="",grindingcutoff="";
                                 var obj=data.barePipeGrindingCutoffRecords[i];
                                 template +='<tr>';
                                 template +='<td>'+obj.operator_no+'</td>';
                                 template +='<td>'+getDate1(obj.operation_time)+'</td>';
                                 template +='<td>'+obj.mill_no+'</td>';
                                 if(obj.odid=="OD")
                                     odid="外表面";
                                 else if(obj.odid=="ID")
                                     odid="内表面";
                                 template +='<td>'+odid+'</td>';
                                 if(obj.grinding_cutoff="G")
                                     grindingcutoff="修磨";
                                 else if(obj.grinding_cutoff="C")
                                     grindingcutoff="切割";
                                 else if(obj.grinding_cutoff="GC")
                                     grindingcutoff="修磨并切割";
                                 template +='<td>'+grindingcutoff+'</td>';
                                 template +='<td>'+obj.remaining_wall_thickness_list+'</td>';
                                 template +='<td>'+obj.cut_off_length+'</td>';
                                 template +='<td>'+obj.original_pipe_length+'</td>';
                                 template +='<td>'+obj.pipe_length_after_cut+'</td>';
                                 if(obj.result=="0")
                                     result="不合格,重新修磨或切割处理";
                                 else if(obj.result=="1")
                                     result="合格,返回上一工序";
                                 else if(obj.result=="2")
                                     result="待定";
                                 template +='<td>'+result+'</td>';
                                 template +='<td>'+obj.remark+'</td>';
                                 template +='</tr>';
                             }
                             template+='</tbody></table>';
                             $('#pipeRecord-container').append(template);
                         }
                         //倒棱
                         if(data.pipeRebevelRecords!=undefined&&data.pipeRebevelRecords!=null&&data.pipeRebevelRecords.length>0){
                             var dicField=["operatorno","operationtime","squareness","ovality","bevelangle","rootface","result","remark"];
                             var template = "";
                             template += '<table  title="" class="dataintable" style="width:100%;height:auto;"><thead><tr>';
                             template += '<th class="i18n1" name="pipeRebevelProcess" colspan="8">倒棱记录</th>';
                             template +='</tr></thead><tbody><tr>';
                             for(var i=0;i<dicField.length;i++){
                                 template += '<td style="text-align: center;" class="i18n1" name='+dicField[i]+'>'+ dicField[i] + '</td>';
                             }
                             template +='</tr>';
                             var odid="",result="";
                             for(var i=0;i<data.pipeRebevelRecords.length;i++){
                                 odid="";result="";
                                 var obj=data.pipeRebevelRecords[i];
                                 template +='<tr>';
                                 template +='<td>'+obj.operator_no+'</td>';
                                 template +='<td>'+getDate1(obj.operation_time)+'</td>';
                                 template +='<td>'+obj.squareness+'</td>';
                                 template +='<td>'+obj.ovality+'</td>';
                                 template +='<td>'+obj.bevel_angle+'</td>';
                                 template +='<td>'+obj.rootface+'</td>';
                                 if(obj.result=="0")
                                     result="不合格,重新倒棱处理";
                                 else if(obj.result=="1")
                                     result="合格";
                                 else if(obj.result=="2")
                                     result="待定";
                                 template +='<td>'+result+'</td>';
                                 template +='<td>'+obj.remark+'</td>';
                                 template +='</tr>';
                             }
                             template+='</tbody></table>';
                             $('#pipeRecord-container').append(template);
                         }
                         hlLanguage("../i18n/");
                         $('#pipeRecordDialog').dialog('open');
                     },
                     error:function () {
                         $.messager.alert('Warning','系统繁忙,请稍后查看!');
                     }
                 });

             }else{
                 $.messager.alert('Warning','请选择要查看的钢管编号!');
             }
         }
        function recordTemplate(recordEnName,recordName, dict) {
            var template = "";
            var i = 1;
            template += '<table  title="" class="dataintable" style="width:100%;height:auto;"><thead><tr><th class="i18n1" name="'+recordEnName+'" colspan="6">' + recordName + '</th></tr></thead><tbody>';
            template += '<tr>';
            for(var key in dict) {
                if(i > 3) {
                    template += '</tr><tr>';
                    i = 1;
                }
                if(key.trim().length>0){
                    if(key=="remark"||(key=="stencilcontent"&&recordName=="外喷标记录")){
                        template += ' <td class="i18n1" name='+key+' style="width:120px;vertical-align: middle;text-align: center;">' + key + '</td>';
                        template += ' <td colspan="5" style="color:#878787;vertical-align: middle;text-align: center;">' + dict[key] + '</td>';
                        i=3;
                    }else{
                        template += ' <td class="i18n1" name='+key+' style="width:120px;vertical-align: middle;text-align: center;">' + key + '</td>';
                        template += ' <td style="width:280px;color:#878787;vertical-align: middle;text-align: center;">' + dict[key] + '</td>';
                    }

                }else{
                    template += ' <td style="width:120px;vertical-align: middle;text-align: center;"></td>';
                    template += ' <td style="width:280px;color:#878787;vertical-align: middle;text-align: center;"></td>';
                }
                i++;
            }
            template+='</tr></tbody></table>';
            return template;
        }
        function recordTemplate1(recordName,dicField,dicValue) {
            var template = "";
            template += '<table  title="" class="dataintable" style="width:100%;height:auto;"><thead><tr>';
            //</tr></thead><tbody>';
            for(var key in dicField){
                template += '<th class="i18n1" name='+dicField[key]+'>'+ dicField[key] + '</th>';
            }
            template +='</tr></thead><tbody>';
            var i=1;
            for(var key in dicValue) {
                if(i%dicField.length==0){
                    template += '<tr>';
                }
                template += ' <td>'+dicValue[key]+'</td>';
                if(i%dicField.length==0){
                    template += '</tr>';i=1;
                }
                i++;
            }
            template+='</tbody></table>';
            return template;
        }
        function closePipeRecordDialog() {
            $('#pipeRecordDialog').dialog('close');
        }
    </script>

</head>
<body>
<fieldset class="b3" style="padding:10px;margin:10px;">
    <legend> <h3><b style="color: orange" >|&nbsp;</b><span class="i18n1" name="datadisplay">数据展示</span></h3></legend>
    <div  style="margin-top:5px;">
        <table class="easyui-datagrid" id="pipeDatagrids" url="/pipeinfo/getPipeInfoByLike.action" striped="true" loadMsg="正在加载中。。。" textField="text" pageSize="20" fitColumns="true" pagination="true" toolbar="#hlpipeTb">
            <thead>
            <tr>
                <th data-options="field:'ck',checkbox:true"></th>
                <th field="id" align="center" width="100" class="i18n1" name="id">流水号</th>
                <th field="pipe_no" align="center" width="100" class="i18n1" name="pipeno">钢管编号</th>
                <th field="project_no" align="center" width="100" class="i18n1" name="projectno">项目编号</th>
                <th field="project_name" align="center" width="100" class="i18n1" name="projectname">项目名称</th>
                <th field="contract_no" align="center" width="100" class="i18n1" name="contractno">合同编号</th>
                <th field="grade" align="center" width="100" class="i18n1" name="grade">钢种</th>
                <th field="heat_no" align="center" width="100" class="i18n1" name="heatno">炉号</th>
                <th field="pipe_making_lot_no" align="center" width="100" class="i18n1" name="pipemakinglotno">制管批号</th>
                <th field="od" align="center" width="100" class="i18n1" name="od">外径</th>
                <th field="wt" align="center" width="100" class="i18n1" name="wt">壁厚</th>
                <th field="p_length" align="center" width="100" class="i18n1" name="p_length">钢管长度</th>
                <th field="weight" align="center" width="100" class="i18n1" name="weight">钢管米重</th>
                <th field="status" align="center" width="100" class="i18n1" name="status">状态</th>
                <th field="storage_stack" align="center" width="100" class="i18n1" name="storagestack">垛位号</th>
                <th field="stack_level" align="center" width="100" class="i18n1" name="stacklevel">层数</th>
                <th field="level_direction" align="center" width="100" class="i18n1" name="leveldirection">堆垛方向</th>
                <th field="level_sequence" align="center" width="100" class="i18n1" name="levelsequence">序号</th>
                <th field="od_coating_date" align="center" width="100" class="i18n1" name="odcoatingdate" data-options="formatter:formatterdate">外涂日期</th>
                <th field="id_coating_date" align="center" width="100" class="i18n1" name="idcoatingdate" data-options="formatter:formatterdate">内涂日期</th>
                <th field="shipment_date" align="center" width="100" class="i18n1" name="shipmentdate" data-options="formatter:formatterdate">出厂日期</th>

            </tr>
            </thead>
        </table>

    </div>
</fieldset>

<!--工具栏-->
<div id="hlpipeTb" style="padding:10px;">
    <span class="i18n1" name="pipeno">钢管编号</span>:
    <input id="pipeno" name="pipeno" style="width:100px;line-height:22px;border:1px solid #ccc">
    <span class="i18n1" name="contractno">合同编号</span>:
    <input id="contractno" name="contractno" style="width:100px;line-height:22px;border:1px solid #ccc">
    <span class="i18n1" name="projectno">项目编号</span>:
    <input id="projectno" name="projectno"  style="width:100px;line-height:22px;border:1px solid #ccc">
    <input id="pstatus" class="easyui-combobox" type="text" name="pstatus"  data-options=
            "url:'/pipeinfo/getAllPipeStatusWithComboboxSelectAll.action',
					        method:'get',
					        valueField:'id',
					        width: 200,
					        editable:false,
					        textField:'text',
					        panelHeight:200"/>

    <a href="#" class="easyui-linkbutton" plain="true" data-options="iconCls:'icon-search'" onclick="searchPipe()">Search</a>
    <div style="float:right">
        <a href="#" id="pipeRecordBtn" class="easyui-linkbutton i18n2" name="searchPipeRecord"  onclick="SearchPipeRecord()">钢管流程信息</a>
        <a href="#" id="genQRLinkBtn" class="easyui-linkbutton i18n2" name="genQR"  onclick="GenQRCode()">生成QRCode</a>

        <a href="#" id="addPipeLinkBtn" class="easyui-linkbutton i18n2" name="add" data-options="iconCls:'icon-add',plain:true" onclick="addPipe()">添加</a>
        <a href="#" id="editPipeLinkBtn" class="easyui-linkbutton i18n2" name="edit" data-options="iconCls:'icon-edit',plain:true" onclick="editPipe()">修改</a>
        <a href="#" id="deltPipeLinkBtn" class="easyui-linkbutton i18n2" name="delete" data-options="iconCls:'icon-remove',plain:true" onclick="delPipe()">删除</a>
    </div>
</div>

<!--添加、修改框-->
<div id="hlPipeDialog" class="easyui-dialog" data-options="title:'添加',modal:true"  closed="true" buttons="#dlg-buttons" style="display: none;padding:5px;width:950px;height:auto;">
    <form id="pipeForm" method="post">
        <fieldset style="width:900px;border:solid 1px #aaa;margin-top:8px;position:relative;">
            <legend class="i18n1" name="pipeinfo">钢管信息</legend>
            <table class="ht-table" width="100%" border="0">
                <tr>
                    <td class="i18n1" name="id">流水号</td>
                    <td width="33%"><input class="easyui-textbox" type="text" name="pipeid" readonly="true" value=""/></td>
                    <td class="i18n1" name="pipeno" width="16%">钢管编号</td>
                    <td width="33%"><input class="easyui-validatebox" type="text" name="pipe_no" value=""/></td>

                </tr>
                <tr>
                    <td class="i18n1" name="projectno" width="16%">项目编号</td>
                    <td width="33%"><input class="easyui-validatebox" type="text" name="project_no" readonly="true"  value=""/></td>

                    <td class="i18n1" name="projectname" width="16%">项目名称</td>
                    <td width="33%"><input class="easyui-validatebox" type="text" name="project_name"  readonly="true" value=""/></td>

                </tr>

                <tr>
                    <td class="i18n1" name="contractno" width="16%">合同编号</td>
                    <td colspan="1" width="33%">
                        <input id="lookup1" name="contract_no" class="mini-lookup" style="text-align:center;width:180px;"
                               textField="contract_no" valueField="id" popupWidth="auto"
                               popup="#gridPanel1" grid="#datagrid1" multiSelect="false"/>
                    </td>
                    <td class="i18n1" name="grade" width="16%">钢种</td>
                    <td  width="33%"><input class="easyui-validatebox" type="text" name="grade" value=""/></td>

                </tr>
                <tr>
                    <td class="i18n1" name="heatno" width="16%">炉号</td>
                    <td   width="33%"><input class="easyui-textbox"  type="text" name="heat_no" value=""/></td>

                    <td class="i18n1" name="pipemakinglotno" width="16%">制管批号</td>
                    <td  width="33%"><input class="easyui-textbox" type="text" name="pipe_making_lot_no" value=""/></td>

                </tr>
                <tr>
                    <td class="i18n1" name="od" width="16%">外径</td>
                    <td   width="33%"><input class="easyui-numberbox"  data-options="min:0,precision:2" type="text" name="od" value=""/></td>

                    <td class="i18n1" name="wt" width="16%">壁厚</td>
                    <td  width="33%"><input class="easyui-numberbox" data-options="min:0,precision:2" type="text" name="wt" value=""/></td>

                </tr>
                <tr>
                    <td class="i18n1" name="p_length" width="16%">管长</td>
                    <td   width="33%"><input class="easyui-numberbox" data-options="min:0,precision:2" type="text" name="p_length" value=""/></td>

                    <td class="i18n1" name="weight" width="16%">重量</td>
                    <td   width="33%"><input class="easyui-numberbox" data-options="min:0,precision:2" type="text" name="weight" value=""/></td>

                </tr>
                <tr>
                    <td class="i18n1" name="storagestack" width="16%">垛位号</td>
                    <td   width="33%">
                        <select class="easyui-combobox" data-options="editable:false" name="storage_stack" style="width:200px;">
                            <option value="stack0">光管垛</option>
                            <option value="stack1">1号垛</option>
                            <option value="stack2">2号垛</option>
                            <option value="stack3">3号垛</option>
                            <option value="stack4">4号垛</option>
                            <option value="stack5">5号垛</option>
                            <option value="stack6">6号垛</option>
                            <option value="stack7">7号垛</option>
                            <option value="stack8">8号垛</option>
                            <option value="stack9">9号垛</option>
                            <option value="stack10">10号垛</option>
                            <option value="stack11">11号垛</option>
                            <option value="stack12">12号垛</option>
                            <option value="stack13">13号垛</option>
                            <option value="stack14">14号垛</option>
                            <option value="stack15">15号垛</option>
                            <option value="stack16">16号垛</option>
                            <option value="stack17">17号垛</option>
                            <option value="stack18">18号垛</option>
                            <option value="stack19">19号垛</option>
                            <option value="stack20">20号垛</option>
                        </select>
                    </td>
                    <td class="i18n1" name="stacklevel" width="16%">层号</td>
                    <td   width="33%">
                        <select class="easyui-combobox" data-options="editable:false" name="stack_level" style="width:200px;">
                            <option value="l1">1层</option>
                            <option value="l2">2层</option>
                            <option value="l3">3层</option>
                            <option value="l4">4层</option>
                            <option value="l5">5层</option>
                            <option value="l6">6层</option>
                            <option value="l7">7层</option>
                            <option value="l8">8层</option>
                            <option value="l9">9层</option>
                            <option value="l10">10层</option>
                            <option value="l11">11层</option>
                            <option value="l12">12层</option>
                            <option value="l13">13层</option>
                            <option value="l14">14层</option>
                            <option value="l15">15层</option>
                            <option value="l16">16层</option>
                            <option value="l17">17层</option>
                            <option value="l18">18层</option>
                            <option value="l19">19层</option>
                            <option value="l20">20层</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td class="i18n1" name="leveldirection" width="16%">堆垛起始方向</td>
                    <td   width="33%">
                        <select id="level_direction" class="easyui-combobox" data-options="editable:false" name="level_direction" style="width:200px;">
                            <option value="East">东</option>
                            <option value="South">南</option>
                            <option value="West">西</option>
                            <option value="North">北</option>

                        </select>
                    </td>
                    <td class="i18n1" name="levelsequence" width="16%">序号</td>
                    <td   width="33%">
                        <input id="level_sequence" class="easyui-textbox" type="text" name="level_sequence" value=""/>
                    </td>

                </tr>




                <tr>
                    <td class="i18n1" name="odcoatingdate" width="16%">外涂日期</td>
                    <td   width="33%">
                        <%--<label class="hl-label" id="od_coating_date"></label>--%>
                        <input class="easyui-datetimebox" id="odcoatingdate" type="text" name="odcoatingDate" value="" data-options="formatter:myformatter2,parser:myparser2"/>
                    </td>

                    <td class="i18n1" name="idcoatingdate" width="16%">内涂日期</td>
                    <td   width="33%">
                        <%--<label class="hl-label" id="id_coating_date"></label>--%>
                        <input class="easyui-datetimebox" id="idcoatingdate" type="text" name="idcoatingDate" value="" data-options="formatter:myformatter2,parser:myparser2"/>

                    </td>

                </tr>
                </tr>
                <tr>
                    <%--<td class="i18n1" name="shipmentdate" width="16%">内涂日期</td>--%>
                    <%--<td   width="33%"><label class="hl-label" id="shipment_date"></label>--%>
                    <%--</td>--%>
                    <td class="i18n1" name="shipmentdate" width="16%">出厂时间</td>
                    <td colspan="1" width="33%">
                        <input class="easyui-datetimebox" id="shipmentDate" type="text" name="shipmentDate" value="" data-options="formatter:myformatter2,parser:myparser2"/>
                    </td>
                        <%--<td></td>--%>
                    <td class="i18n1" name="status" width="16%">状态</td>
                    <td   width="33%"><input id="status" class="easyui-combobox" type="text" name="status"  data-options=
                            "url:'/pipeinfo/getAllPipeStatus.action',
					        method:'get',
					        valueField:'id',
					        editable:false,
					        textField:'text',
					        panelHeight:'auto'"/></td>
                    <%--<td></td>--%>
                </tr>

            </table>

        </fieldset>

    </form>


</div>
<div id="dlg-buttons" align="center" style="width:100%;">
    <a href="#" class="easyui-linkbutton" iconCls="icon-save" onclick="PipeFormSubmit()">Save</a>
    <a href="#" class="easyui-linkbutton" id="hlcancelBtn" operationtype="add" iconCls="icon-cancel" onclick="PipeFormCancelSubmit()">Cancel</a>
</div>

<div id="gridPanel1" class="mini-panel" title="header" iconCls="icon-add" style="width:500px;height:280px;"
     showToolbar="true" showCloseButton="true" showHeader="false" bodyStyle="padding:0" borderStyle="border:0"
>
    <div property="toolbar" id="searchBar1" style="padding:5px;padding-left:8px;text-align:center;display: none">
        <div style="float:left;padding-bottom:2px;">
            <span class="i18n1" name="contractno">合同编号</span><span>:</span>
            <input id="keyText_contract_no" class="mini-textbox" style="width:110px;" onenter="onSearchClick(1)"/>

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
         url="/ContractOperation/getContractInfoByContractNo.action">
        <div property="columns">
            <div type="checkcolumn" width="20"></div>

            <div field="contract_no" width="80" headerAlign="center" allowSort="true" class="i18n1" name="contractno">合同编号</div>
            <div field="project_no" width="40" headerAlign="center" allowSort="true" class="i18n1" name="projectno">项目编号</div>
            <div field="grade" width="40" headerAlign="center" allowSort="true" class="i18n1" name="grade">钢种</div>
            <div field="od" width="40" headerAlign="center" allowSort="true" class="i18n1" name="od">外径</div>
            <div field="wt" width="40" headerAlign="center" allowSort="true" class="i18n1" name="wt">壁厚</div>
            <div field="total_order_length" width="40" headerAlign="center" allowSort="true" class="i18n1" name="total_order_length">合同总长度</div>


        </div>
    </div>
    <div id="pipeRecordDialog" buttons="#dlg-buttons1" class="easyui-dialog" title="钢管流程信息" closed="true" data-options="iconCls:'icon-save'" style="width:1200px;height:600px;top:25px;padding:10px;word-break: break-all; word-wrap:break-word;overflow-y: scroll;">
        <div id="pipeRecord-container" style="width:100%;height:100%;overflow-y: scroll">

        </div>

    </div>
    <div id="dlg-buttons1" align="center" style="width:100%;">
        <a href="#" class="easyui-linkbutton i18n2" name="close" iconCls="icon-cancel" onclick="closePipeRecordDialog()">关闭</a>
    </div>
</div>


<script type="text/javascript" src="../easyui/jquery.easyui.min.js"></script>
</body>
</html>
<script type="text/javascript">
    mini.parse();
    var grid1=mini.get("datagrid1");
    grid1.load();

    var keyText1=mini.get('keyText_contract_no');
    var look1=mini.get('lookup1');



    function onSearchClick(type) {
        if(type==1)
        {
            grid1.load({
                contract_no:keyText1.value,
            });
        }

    }
    function onCloseClick(type) {
        if(type==1)
            look1.hidePopup();

    }
    function onClearClick(type) {
        if(type==1)
            look1.deselectAll();
    }
    look1.on('valuechanged',function () {
        var rows = grid1.getSelected();
        $("input[name='contract_no']").val(rows.contract_no);
        $("input[name='project_no']").val('');
        $("input[name='project_name']").val('');
        $.ajax({
            url:'../ProjectOperation/getProjectInfoByContract.action',
            data:{'contract_no':rows.contract_no},
            dataType:'json',
            success:function (data) {
                $.each(data,function (index,element) {
                    $("input[name='project_no']").val(element.project_no);
                    $("input[name='project_name']").val(element.project_name);
                });

            },
            error:function () {
                hlAlertThree();
            }
        });
    });

    look1.on("showpopup",function(e){
        $('.mini-shadow').css('z-index','99999');
        $('.mini-popup').css('z-index','100000');
        $('.mini-panel').css('z-index','100000');
        $('#searchBar1').css('display','block');
        //$('.mini-buttonedit .mini-buttonedit-input').css('width','150px');
    });
    $(function () {
        hlLanguage("../i18n/");
    });
    //hlLanguage("../i18n/");
</script>
