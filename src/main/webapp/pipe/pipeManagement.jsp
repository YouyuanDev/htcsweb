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
                    $('#od_coating_date').text(getDate1(row.od_coating_date));
                if(row.id_coating_date!=undefined)
                    $('#id_coating_date').text(getDate1(row.id_coating_date));
                //通过以上方法无法将pipeid初始化，所以再调用以下方法赋值
                 $('#pipeForm').form('load',{
                     'pipeid':row.id
                 });
                if(row.contract_no!=null)
                    look1.setText(row.contract_no);
                // $('#contractForm').form('load',{
                //     'contractid':row.id,
                //     'project_no':row.project_no,
                //     'project_name':row.project_name,
                //     'contract_no':row.contract_no,
                //     'od':row.od,
                //     'wt':row.wt,
                //     'external_coating':row.external_coating,
                //     'internal_coating':row.internal_coating,
                //     'grade':row.grade,
                //     'total_order_length':row.total_order_length,
                //     'total_order_weight':row.total_order_weight,
                //     'weight_per_meter':row.weight_per_meter,
                //     'pipe_length':row.pipe_length
                //
                // });


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
             var row = $('#pipeDatagrids').datagrid('getSelected');
             if(row) {
                 var pipe_no=row.pipe_no;
                 $.ajax({
                     url:'/pipeinfo/searchPipeRecord.action',
                     dataType:'json',
                     data:{pipe_no:pipe_no},
                     success:function (data) {
                         //外打砂
                         if(data.odBlastProcessRecord!=undefined&&data.odBlastProcessRecord!=null){
                              var obj=data.odBlastProcessRecord;
                              var dic=[];
                              dic["操作工工号"]=obj.operator_no;dic["操作时间"]=obj.operation_time;
                              dic["分厂号"]=obj.mill_no;
                              dic["外观缺陷"]=obj.surface_condition;dic["打砂前盐度"]=obj.salt_contamination_before_blasting;
                              dic["碱洗所用时间"]=obj.alkaline_dwell_time;dic["碱浓度"]=obj.alkaline_concentration;
                              dic["酸洗所用时间"]=obj.acid_wash_time;dic["酸浓度"]=obj.alkaline_concentration;
                              dic["磨料传导性"]=obj.abrasive_conductivity;dic["打砂传送速度"]=obj.blast_line_speed;
                              dic["预热温度"]=obj.preheat_temp;dic["冲洗水电导率"]=obj.rinse_water_conductivity;
                              dic["管体标识是否清晰"]=obj.marking;dic["备注"]=obj.remark;
                              dic["结论"]=obj.result;
                             alert(recordTemplate("外涂记录(2FBE)",dic));
                              $('#pipeRecordDialog').append(recordTemplate("外打砂记录",dic));
                         }
                         //外打砂检验
                         if(data.odBlastInspectionProcessRecord!=undefined&&data.odBlastInspectionProcessRecord!=null){
                             var obj=data.odBlastInspectionProcessRecord;
                             var dic=[];
                             dic["操作工工号"]=obj.operator_no;dic["操作时间"]=obj.operation_time;
                             dic["分厂号"]=obj.mill_no;
                             dic["环境温度"]=obj.air_temp;dic["相对湿度"]=obj.relative_humidity;
                             dic["露点温度"]=obj.dew_point;dic["表面清洁度"]=obj.blast_finish_sa25;
                             dic["锚纹深度"]=obj.profile;dic["灰尘度"]=obj.surface_dust_rating;
                             dic["钢管表面温度"]=obj.pipe_temp;dic["打砂后盐度"]=obj.salt_contamination_after_blasting;
                             dic["表面瑕疵"]=obj.surface_condition;dic["压缩空气是否存在油水"]=obj.oil_water_in_air_compressor;
                             dic["涂敷前等待时间"]=obj.elapsed_time;dic["备注"]=obj.remark;
                             dic["结论"]=obj.result;

                             $('#pipeRecordDialog').append(recordTemplate("外打砂检验记录",dic));
                         }
                         //外涂2fbe
                         if(data.odCoatingProcessRecord!=undefined&&data.odCoatingProcessRecord!=null){
                             var obj=data.odCoatingProcessRecord;
                             var dic=[];
                             dic["操作工工号"]=obj.operator_no;dic["操作时间"]=obj.operation_time;
                             dic["分厂号"]=obj.mill_no;
                             dic["涂敷线速度"]=obj.coating_line_speed;dic["底层粉末型号"]=obj.base_coat_used;
                             dic["底层粉末批号"]=obj.base_coat_lot_no;dic["面层粉末型号"]=obj.top_coat_used;
                             dic["面层粉末批号"]=obj.top_coat_lot_no;dic["底层粉末喷枪数"]=obj.base_coat_gun_count;
                             dic["面层粉末喷枪数"]=obj.top_coat_gun_count;dic["中频温度"]=obj.application_temp;
                             dic["表面瑕疵"]=obj.to_first_touch_duration;dic["到达水淋处所需时间"]=obj.to_quench_duration;
                             dic["到达首次接触点所需时间"]=obj.elapsed_time;dic["供气压力"]=obj.air_pressure;
                             dic["喷涂电压"]=obj.coating_voltage;dic["喷枪距离"]=obj.gun_distance;
                             dic["粉量"]=obj.spray_speed;dic["中频电压"]=obj.application_voltage;
                             dic["备注"]=obj.remark;dic["结论"]=obj.result;

                             $('#pipeRecordDialog').append(recordTemplate("外涂记录(2FBE)",dic));
                         }
                         //外涂检验2fbe
                         if(data.odCoatingInspectionProcessRecord!=undefined&&data.odCoatingInspectionProcessRecord!=null){
                             var obj=data.odCoatingInspectionProcessRecord;
                             var dic=[];
                             dic["操作工工号"]=obj.operator_no;dic["操作时间"]=obj.operation_time;
                             dic["分厂号"]=obj.mill_no;
                             dic["底层涂层厚度"]=obj.base_coat_thickness_list;dic["面层涂层厚度"]=obj.top_coat_thickness_list;
                             dic["涂层总厚度"]=obj.total_coating_thickness_list;dic["漏点数量"]=obj.holidays;
                             dic["电火花检测电压"]=obj.holiday_tester_volts;dic["修补点数"]=obj.repairs;
                             dic["坡口检测"]=obj.bevel;dic["表面质量"]=obj.surface_condition;
                             dic["是否是取样管"]=obj.is_sample;dic["附着力等级"]=obj.adhesion_rating;
                             dic["是否是DSC取样管  "]=obj.is_dsc_sample;dic["备注"]=obj.remark;
                             dic["结论"]=obj.result;
                             $('#pipeRecordDialog').append(recordTemplate("外涂检验记录(2FBE)",dic));
                         }
                         //外涂3lpe
                         if(data.odCoating3LpeProcessRecord!=undefined&&data.odCoating3LpeProcessRecord!=null){
                             var obj=data.odCoating3LpeProcessRecord;
                             var dic=[];
                             dic["操作工工号"]=obj.operator_no;dic["操作时间"]=obj.operation_time;
                             dic["分厂号"]=obj.mill_no;
                             dic["涂敷线速度"]=obj.coating_line_speed;dic["底层粉末型号"]=obj.base_coat_used;
                             dic["底层粉末批号"]=obj.base_coat_lot_no;dic["中间层粉末型号"]=obj.middle_coat_used;
                             dic["中间粉末批号"]=obj.middle_coat_lot_no;dic["面层粉末型号"]=obj.top_coat_used;
                             dic["面层粉末批号"]=obj.top_coat_lot_no;dic["底层粉末喷枪数"]=obj.base_coat_gun_count;
                              dic["中频温度"]=obj.application_temp; dic["供气压力"]=obj.air_pressure;
                             dic["表面瑕疵"]=obj.to_first_touch_duration;dic["到达水淋处所需时间"]=obj.to_quench_duration;
                             dic["喷涂电压"]=obj.coating_voltage;dic["喷枪距离"]=obj.gun_distance;
                             dic["粉量"]=obj.spray_speed;dic["中频电压"]=obj.application_voltage;
                             dic["备注"]=obj.remark;dic["结论"]=obj.result;
                             $('#pipeRecordDialog').append(recordTemplate("外涂记录(3LPE)",dic));
                         }
                         //外涂检验3lpe
                         if(data.odCoating3LpeInspectionProcessRecord!=undefined&&data.odCoating3LpeInspectionProcessRecord!=null){
                             var obj=data.odCoating3LpeInspectionProcessRecord;
                             var dic=[];
                             dic["操作工工号"]=obj.operator_no;dic["操作时间"]=obj.operation_time;
                             dic["分厂号"]=obj.mill_no;
                             dic["底层涂层厚度"]=obj.base_coat_thickness_list;dic["面层涂层厚度"]=obj.top_coat_thickness_list;
                             dic["中间层总厚度"]=obj.middle_coat_thickness_list;
                             dic["涂层总厚度"]=obj.total_coating_thickness_list;dic["漏点数量"]=obj.holidays;
                             dic["电火花检测电压"]=obj.holiday_tester_volts;dic["修补点数"]=obj.repairs;
                             dic["坡口检测"]=obj.bevel;dic["表面质量"]=obj.surface_condition;
                             dic["是否是取样管"]=obj.is_sample;dic["附着力等级"]=obj.adhesion_rating;
                             dic["是否是DSC取样管  "]=obj.is_dsc_sample;dic["是否为PE取样管"]=obj.is_pe_sample;
                             dic["备注"]=obj.remark;
                             dic["结论"]=obj.result;
                             $('#pipeRecordDialog').append(recordTemplate("外涂检验记录(3LPE)",dic));
                         }
                         //外喷标
                         if(data.odStencilProcessRecord!=undefined&&data.odStencilProcessRecord!=null){
                             var obj=data.odStencilProcessRecord;
                             var dic=[];
                             dic["操作工工号"]=obj.operator_no;dic["操作时间"]=obj.operation_time;
                             dic["分厂号"]=obj.mill_no;
                             dic["喷标内容"]=obj.stencil_content;dic["中心色环"]=obj.center_line_color;
                             dic["管端色环"]=obj.pipe_end_color;dic["备注"]=obj.remark;
                             dic["结论"]=obj.result;
                             $('#pipeRecordDialog').append(recordTemplate("外喷标记录",dic));
                         }
                         //外防终检
                         if(data.odFinalInspectionProcessRecord!=undefined&&data.odFinalInspectionProcessRecord!=null){
                             var obj=data.odFinalInspectionProcessRecord;
                             var dic=[];
                             dic["操作工工号"]=obj.operator_no;dic["操作时间"]=obj.operation_time;
                             dic["分厂号"]=obj.mill_no;
                             dic["外涂层质检结果"]=obj.inspection_result;dic["预留端长度"]=obj.cutback_length;
                             dic["外喷标检验"]=obj.stencil_verification;dic["预留端表面"]=obj.cutback_surface;
                             dic["剩磁测量值"]=obj.magnetism_list;dic["涂层倒角"]=obj.coating_bevel_angle_list;
                             dic["粉末长度  "]=obj.epoxy_cutback_list;dic["备注"]=obj.remark;
                             dic["结论"]=obj.result;
                             $('#pipeRecordDialog').append(recordTemplate("外防终检记录",dic));
                         }
                         //内打砂
                         if(data.idBlastProcessRecord!=undefined&&data.idBlastProcessRecord!=null){
                             var obj=data.idBlastProcessRecord;
                             var dic=[];
                             dic["操作工工号"]=obj.operator_no;dic["操作时间"]=obj.operation_time;
                             dic["分厂号"]=obj.mill_no;
                             dic["原内壁标签管号"]=obj.original_pipe_no;dic["新内壁标签管号"]=obj.new_pipe_no;
                             dic["完成标签更新"]=obj.pipe_no_update;dic["打砂前盐度"]=obj.salt_contamination_before_blasting;
                             dic["内表面外观"]=obj.internal_surface_condition;dic["外涂层表面"]=obj.external_coating_condition;
                             dic["管体标识是否清晰"]=obj.marking;dic["备注"]=obj.abrasive_conductivity;
                             dic["备注"]=obj.remark;dic["磨料传导性"]=obj.result;
                             $('#pipeRecordDialog').append(recordTemplate("内打砂记录",dic));
                         }
                         //内打砂检验
                         if(data.idBlastInspectionProcessRecord!=undefined&&data.idBlastInspectionProcessRecord!=null){
                             var obj=data.idBlastInspectionProcessRecord;
                             var dic=[];
                             dic["操作工工号"]=obj.operator_no;dic["操作时间"]=obj.operation_time;
                             dic["分厂号"]=obj.mill_no;
                             dic["环境温度"]=obj.air_temp;dic["相对湿度"]=obj.relative_humidity;
                             dic["露点温度"]=obj.dew_point;dic["表面清洁度"]=obj.blast_finish_sa25;
                             dic["内打砂时间"]=obj.blast_time;
                             dic["锚纹深度"]=obj.profile;dic["灰尘度"]=obj.surface_dust_rating;
                             dic["钢管表面温度"]=obj.pipe_temp;dic["打砂后盐度"]=obj.salt_contamination_after_blasting;
                             dic["表面质量"]=obj.surface_condition;
                             dic["涂敷前等待时间"]=obj.elapsed_time;dic["备注"]=obj.remark;
                             dic["结论"]=obj.result;
                             $('#pipeRecordDialog').append(recordTemplate("内打砂检验记录",dic));
                         }
                         //内涂
                         if(data.idCoatingProcessRecord!=undefined&&data.idCoatingProcessRecord!=null){
                             var obj=data.idCoatingProcessRecord;
                             var dic=[];
                             dic["操作工工号"]=obj.operator_no;dic["操作时间"]=obj.operation_time;
                             dic["分厂号"]=obj.mill_no;
                             dic["喷涂速度"]=obj.coating_speed;dic["底层涂料"]=obj.base_used;
                             dic["底层批号"]=obj.base_batch;dic["固化剂型号"]=obj.curing_agent_used;
                             dic["固化剂批号"]=obj.curing_agent_batch;dic["固化开始时间"]=obj.curing_start_time;
                             dic["固化结束时间  "]=obj.curing_finish_time;dic["固化房温度  "]=obj.curing_temp;
                             dic["备注"]=obj.remark; dic["结论"]=obj.result;
                             $('#pipeRecordDialog').append(recordTemplate("内涂记录",dic));
                         }
                         //内涂检验
                         if(data.idCoatingInspectionProcessRecord!=undefined&&data.idCoatingInspectionProcessRecord!=null){
                             var obj=data.idCoatingInspectionProcessRecord;
                             var dic=[];
                             dic["操作工工号"]=obj.operator_no;dic["操作时间"]=obj.operation_time;
                             dic["分厂号"]=obj.mill_no;
                             dic["是否为铁片样管"]=obj.is_sample;dic["湿膜厚度"]=obj.wet_film_thickness_list;
                             dic["是否为玻璃片样管"]=obj.is_glass_sample;
                             dic["备注"]=obj.remark;
                             dic["结论"]=obj.result;
                             $('#pipeRecordDialog').append(recordTemplate("内涂检验记录",dic));
                         }
                         //内喷标
                         if(data.idStencilProcessRecord!=undefined&&data.idStencilProcessRecord!=null){
                             var obj=data.idStencilProcessRecord;
                             var dic=[];
                             dic["操作工工号"]=obj.operator_no;dic["操作时间"]=obj.operation_time;
                             dic["分厂号"]=obj.mill_no;
                             dic["喷标内容"]=obj.stencil_content; dic["备注"]=obj.remark;
                             dic["结论"]=obj.result;
                             $('#pipeRecordDialog').append(recordTemplate("内喷标记录",dic));
                         }
                         //内防终检
                         if(data.idFinalInspectionProcess!=undefined&&data.idFinalInspectionProcess!=null){
                             var obj=data.idFinalInspectionProcess;
                             var dic=[];
                             dic["操作工工号"]=obj.operator_no;dic["操作时间"]=obj.operation_time;
                             dic["分厂号"]=obj.mill_no;
                             dic["外涂层质检结果"]=obj.od_inspection_result;dic["终检结果"]=obj.final_inspection_result;
                             dic["干膜厚度"]=obj.dry_film_thickness_list;dic["预留端长度"]=obj.cutback_length;
                             dic["内喷标检验"]=obj.stencil_verification;dic["粗糙度"]=obj.roughness_list;
                             dic["电火花检漏电压"]=obj.holiday_tester_volts;dic["漏点数量"]=obj.holidays;
                             dic["表面质量"]=obj.surface_condition;dic["坡口质量"]=obj.bevel_check;
                             dic["剩磁"]=obj.magnetism_list;dic["内涂层修补数"]=obj.internal_repairs;
                             dic["备注"]=obj.remark;dic["结论"]=obj.result;
                             $('#pipeRecordDialog').append(recordTemplate("内防终检记录",dic));
                         }
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
        function recordTemplate(recordName, dic) {
            var template = "";
            var i = 1;
            template += '<table  title="" style="width:100%;height:auto;"><thead><tr><th colspan="3">' + recordName + '</th></tr></thead><tbody>';
            template += '<tr>';
                for(var key in dic) {
                        template += '<td>'+ key +'</td>';
                        template += ' <td>'+dic[key] +'</td>';
                }
            template+='</tr></tbody></table>';
            return template;
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
					        panelHeight:'auto'"/>

    <a href="#" class="easyui-linkbutton" plain="true" data-options="iconCls:'icon-search'" onclick="searchPipe()">Search</a>
    <div style="float:right">
        <a href="#" id="pipeRecordBtn" class="easyui-linkbutton i18n1" name="searchPipeRecord"  onclick="SearchPipeRecord()">钢管流程信息</a>
        <a href="#" id="genQRLinkBtn" class="easyui-linkbutton i18n1" name="genQR"  onclick="GenQRCode()">生成QRCode</a>

        <a href="#" id="addPipeLinkBtn" class="easyui-linkbutton i18n1" name="add" data-options="iconCls:'icon-add',plain:true" onclick="addPipe()">添加</a>
        <a href="#" id="editPipeLinkBtn" class="easyui-linkbutton i18n1" name="edit" data-options="iconCls:'icon-edit',plain:true" onclick="editPipe()">修改</a>
        <a href="#" id="deltPipeLinkBtn" class="easyui-linkbutton i18n1" name="delete" data-options="iconCls:'icon-remove',plain:true" onclick="delPipe()">删除</a>
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
                    <td   width="33%"><label class="hl-label" id="od_coating_date"></label>
                    </td>

                    <td class="i18n1" name="idcoatingdate" width="16%">内涂日期</td>
                    <td   width="33%"><label class="hl-label" id="id_coating_date"></label>
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
    <div id="pipeRecordDialog" class="easyui-dialog" title="钢管流程信息" closed="true" data-options="iconCls:'icon-save'" style="width:800px;height:400px;padding:10px;word-break: break-all; word-wrap:break-word;overflow-y: scroll;">
        <%--<table  title="" style="width:100%;height:auto;">--%>
            <%--<thead>--%>
            <%--<tr>--%>
                <%--<th colspan="3">1222</th>--%>
            <%--</tr>--%>
            <%--</thead>--%>
            <%--<tbody>--%>
              <%--<tr>--%>
                  <%--<td>外防</td>--%>
                  <%--<td>123</td>--%>
                  <%--<td>内防</td>--%>
                  <%--<td>123</td>--%>
                  <%--<td>内防</td>--%>
                  <%--<td>123</td>--%>
              <%--</tr>--%>
            <%--</tbody>--%>
        <%--</table>--%>
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

    hlLanguage("../i18n/");
</script>
