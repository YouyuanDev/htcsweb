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
        var g_result_output;
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
            LoadProcess_input_output();

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
            if(g_result_output==undefined){
                return;
            }
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

                         var language=getCookie("userLanguage");
                         for(var i=0;i<data.length;i++){
                             var map=data[i];
                             for(var key in map){
                                 if(key.indexOf('header')==-1){
                                     getTemplate(key,map[key],language,map[key+"_header"]);
                                 }
                             }
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
        function getTemplate(process_code,array,language,header_dict){

            var template="";
            template += '<table  title="" class="dataintable" style="width:100%;height:auto;"><thead><tr><th class="i18n1" name="'+process_code+'" colspan="6">' + process_code + '</th></tr></thead><tbody>';
            template += '<tr>';
            var j=1;
            var remark="",result="",result_name="";

            for(var i=0;i<array.length;i++){
                if(j > 3) {
                    template+= '</tr><tr>';
                    j = 1;
                }
                var dict=header_dict;
                remark=dict.remark;
                result=dict.result;
                for(var t=0;t<g_result_output.length;t++){
                    if(g_result_output[t].process_code==process_code){
                        for(var w=0;w<g_result_output[t].output.length;w++){
                            if(g_result_output[t].output[w].result==result){
                                if(language=="en"){
                                    result_name=g_result_output[t].output[w].result_name_en;
                                }else{
                                    result_name=g_result_output[t].output[w].result_name;
                                }
                            }
                        }
                    }
                }

                var max_value=array[i].max_value;
                var min_value=array[i].min_value;
                var item_value=array[i].item_value==undefined?"":array[i].item_value;
                var range="";var flag=false;
                if(max_value!=undefined&&min_value!=undefined){
                    if(array[i].need_verify!=undefined&&array[i].need_verify=="1"){
                        range="("+min_value+"~"+max_value+")";
                        if(item_value!=undefined&&!isNaN(item_value)){
                            if(parseFloat(max_value)<parseFloat(item_value))
                                flag=true;
                            if(parseFloat(min_value)>parseFloat(item_value))
                                flag=true;
                        }
                    }
                }
                if(language&&language=="en"){
                    var unit=(array[i].unit_name_en==undefined||array[i].unit_name_en=="")?"":' ('+array[i].unit_name_en+') ';
                    template+= ' <td  style="width:120px;vertical-align: middle;text-align: center;">' +array[i].item_name_en+unit+range+'</td>';
                }else{
                    var unit=(array[i].unit_name==undefined||array[i].unit_name_en=="")?"":' ('+array[i].unit_name+') ';
                    template +=' <td  style="width:120px;vertical-align: middle;text-align: center;">' +array[i].item_name+unit+range+'</td>';
                }
                if(flag){
                    template += '<td style="width:280px;color:red;vertical-align: middle;text-align: center;">'+item_value+'</td>';
                }else{
                    template += '<td style="width:280px;color:#878787;vertical-align: middle;text-align: center;">'+item_value+'</td>';
                }


                j++;
            }
            template+='</tr>';
            template+='<tr>';
            template +=' <td class="i18n1" name="result" style="width:120px;vertical-align: middle;text-align: center;">结果</td>';
            template +=' <td colspan="5" style="width:120px;vertical-align: middle;text-align: center;">' +result_name+ '</td>';
            template+='</tr>';
            template+='<tr>';
            template += ' <td class="i18n1" name="remark" style="width:120px;vertical-align: middle;text-align: center;">备注</td>';
            template += ' <td colspan="5" style="color:#878787;vertical-align: middle;text-align: left;">' + remark + '</td>';
            template+='</tr></tbody></table>';
            $('#pipeRecord-container').append(template);
        }

        //初始化工序的input和output
        function LoadProcess_input_output(){
            //获得本工序的input status
            $.ajax({
                url:'../data/process_input_output.json',
                data:{},
                dataType:'json',
                success:function (data) {
                    if(data!=null&&data!=""){
                        g_result_output=data;
                        // for(var i=0;i<data.length;i++){
                        //     if($('#process_code').val()!=undefined&&data[i].process_code==$('#process_code').val()){
                        //         //初始化input statuslist
                        //         //g_statuslist=data[i].input;
                        //
                        //         $('#inputStatusList').val(data[i].input);
                        //
                        //         //初始化result list
                        //         var options = [];
                        //         var language=getCookie("userLanguage");
                        //
                        //         //用于后台的pipe status 设置规则
                        //
                        //         var arr={};
                        //         arr["output"]=data[i].output;
                        //         $('#outputJson').val(JSON.stringify(arr));
                        //
                        //         $.each(data[i].output, function(i,val){
                        //             //这里的"text","id"和html中对应
                        //             //alert(val);
                        //             if(language&&language=="en"){
                        //                 options.push({ "text": val.result_name_en, "value": val.result });
                        //             }else{
                        //                 options.push({ "text": val.result_name, "value": val.result });
                        //             }
                        //         })
                        //         $("#result").combobox("loadData", options);
                        //         break;
                        //     }
                        // }
                        //alert(statuslist);
                    }
                },
                error:function () {
                    hlAlertThree();
                }
            });


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
                <th field="rebevel_mark" align="center" width="100" class="i18n1" name="rebevelmark">倒棱标志</th>
                <th field="odsampling_mark" align="center" width="100" class="i18n1" name="odsamplingmark">外防取样标志</th>
                <th field="idsampling_mark" align="center" width="100" class="i18n1" name="idsamplingmark">内防取样标志</th>

                <th field="od_dsc_sample_mark" align="center" width="100" class="i18n1" name="oddscsamplemark">外防DSC取样标志</th>
                <th field="od_pe_sample_mark" align="center" width="100" class="i18n1" name="odpesamplemark">外防PE取样标志</th>
                <th field="id_glass_sample_mark" align="center" width="100" class="i18n1" name="idglasssamplemark">内防玻璃片取样标志</th>



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
					        panelHeight:'200'"/></td>
                    <%--<td></td>--%>
                </tr>
                <tr>

                    <td class="i18n1" name="rebevelmark" width="16%">倒棱标志</td>
                    <td colspan="1" width="33%">
                        <%--<input class="easyui-textbox" type="text" name="rebevel_mark" value=""/>  --%>
                            <select class="easyui-combobox" data-options="editable:false" name="rebevel_mark" style="width:200px;">
                                <option value="0">0</option>
                                <option value="1">1</option>
                            </select>
                    </td>

                    <td class="i18n1" name="odsamplingmark" width="16%">外防取样标志</td>
                    <td colspan="1" width="33%">
                        <%--<input class="easyui-textbox" type="text" name="odsampling_mark" value=""/> --%>
                            <select class="easyui-combobox" data-options="editable:false" name="odsampling_mark" style="width:200px;">
                                <option value="">无</option>
                                <option value="0">0</option>
                                <option value="1">1</option>
                            </select>
                    </td>


                </tr>
                <tr>

                    <td class="i18n1" name="oddscsamplemark" width="16%">外防DSC取样标志</td>
                    <td colspan="1" width="33%">

                        <select class="easyui-combobox" data-options="editable:false" name="od_dsc_sample_mark" style="width:200px;">
                            <option value="0">0</option>
                            <option value="1">1</option>
                        </select>
                    </td>
                    <td class="i18n1" name="odpesamplemark" width="16%">外防PE取样标志</td>
                    <td colspan="1" width="33%">
                        <select class="easyui-combobox" data-options="editable:false" name="od_pe_sample_mark" style="width:200px;">
                            <option value="0">0</option>
                            <option value="1">1</option>
                        </select>
                    </td>

                </tr>
                <tr>

                    <td class="i18n1" name="idsamplingmark" width="16%">内防取样标志</td>
                    <td colspan="1" width="33%">

                            <select class="easyui-combobox" data-options="editable:false" name="idsampling_mark" style="width:200px;">
                                <option value="0">0</option>
                                <option value="1">1</option>
                            </select>
                           </td>
                    <td class="i18n1" name="idglasssamplemark" width="16%">内防玻璃片取样标志</td>
                    <td colspan="1" width="33%">

                        <select class="easyui-combobox" data-options="editable:false" name="id_glass_sample_mark" style="width:200px;">
                            <option value="0">0</option>
                            <option value="1">1</option>
                        </select>
                    </td>


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
