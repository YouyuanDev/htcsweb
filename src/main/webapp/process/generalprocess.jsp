<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
    String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>工序信息</title>
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
                $('#hlProDialog').dialog({
                    onClose:function () {
                        var type=$('#hlcancelBtn').attr('operationtype');
                        if(type=="add"){
                            var $imglist=$('#fileslist');
                            var $dialog=$('#hlProDialog');
                            hlAlertSix("../UploadFile/delUploadPicture.action",$imglist,$dialog,grid);
                        }
                        clearFormLabel();
                    }
                });
               $('.mini-buttonedit .mini-buttonedit-input').css('width','150px');
               // hlLanguage("../i18n/");
        });
        function addPro(){
            $('#hlcancelBtn').attr('operationtype','add');
            $('#hlProDialog').dialog('open').dialog('setTitle','新增');
            clearFormLabel();
            clearMultiUpload(grid);
            url="/InspectionProcessOperation/saveProcess.action";
            //$("input[name='alkaline_dwell_time']").siblings().css("background-color","#F9A6A6");
        }
        function delPro() {
            var row = $('#proDatagrids').datagrid('getSelections');
            if(row.length>0){
                var idArr=[];
                for (var i=0;i<row.length;i++){
                    idArr.push(row[i].id);
                }
                var idArrs=idArr.join(',');
                $.messager.confirm('系统提示',"您确定要删除这<font color=red>"+idArr.length+ "</font>条数据吗？",function (r) {
                    if(r){
                        $.post("/InspectionProcessOperation/delProcess.action",{"hlparam":idArrs},function (data) {
                            if(data.success){
                                $("#proDatagrids").datagrid("reload");
                            }
                            hlAlertFour(data.message);
                        },"json");
                    }
                });
            }else{
                hlAlertOne();
            }
        }
        function editPro(){
            $('#hlcancelBtn').attr('operationtype','edit');
            var row = $('#proDatagrids').datagrid('getSelected');
            if(row){
                $('#hlProDialog').dialog('open').dialog('setTitle','修改');
                loadPipeBaiscInfo(row);
                $('#odbpid').text(row.id);
                row.operation_time=getDate1(row.operation_time),
                $('#proForm').form('load',row);
                // $('#proForm').form('load', {
                //     'mill_no': row.mill_no,
                //     'surface_condition': row.surface_condition,
                //     'salt_contamination_before_blasting': row.salt_contamination_before_blasting,
                //     'alkaline_dwell_time': row.alkaline_dwell_time,
                //     'alkaline_concentration': row.alkaline_concentration,
                //     'abrasive_conductivity': row.abrasive_conductivity,
                //     'acid_wash_time': row.acid_wash_time,
                //     'acid_concentration': row.acid_concentration,
                //     'blast_line_speed': row.blast_line_speed,
                //     'preheat_temp': row.preheat_temp,
                //     'marking': row.marking,
                //     'rinse_water_conductivity': row.rinse_water_conductivity,
                //     'operation_time':getDate1(row.operation_time),
                //     'upload_files':row.upload_files,
                //     'result':row.result,
                //     'remark':row.remark
                // });

                //$('#operation-time').datetimebox('setValue',getDate1(row.operation_time));
                look1.setText(row.pipe_no);
                look1.setValue(row.pipe_no);
                look2.setText(row.operator_no);
                look2.setValue(row.operator_no);


                //combox1.setValue(row.surface_condition);
                var odpictures=row.upload_files;
                if(odpictures!=null&&odpictures!=""){
                     var imgList=odpictures.split(';');
                     createPictureModel(basePath,imgList);
                }
                //异步获取标准并匹配
                // $.ajax({
                //     url:'/AcceptanceCriteriaOperation/getODAcceptanceCriteriaByContractNo.action',
                //     dataType:'json',
                //     data:{'contract_no':row.contract_no},
                //     success:function (data) {
                //         var $obj=$("input[name='salt_contamination_before_blasting']");
                //         $obj.siblings().css("background-color","#FFFFFF");
                //         var $obj1=$("input[name='preheat_temp']");
                //         $obj1.siblings().css("background-color","#FFFFFF");
                //         var $obj2=$("input[name='abrasive_conductivity']");
                //         $obj2.siblings().css("background-color","#FFFFFF");
                //         var $obj3=$("input[name='rinse_water_conductivity']");
                //         $obj3.siblings().css("background-color","#FFFFFF");
                //
                //
                //         if(data!=null){
                //             var salt=$obj.val();
                //             var res1=$obj1.val();
                //             var res2=$obj2.val();
                //             var res3=$obj3.val();
                //             if(!((salt>=data.salt_contamination_before_blast_min)&&(salt<=data.salt_contamination_before_blast_max)))
                //                 $obj.siblings().css("background-color","#F9A6A6");
                //             if(!((res1>=data.preheat_temp_min)&&(res1<=data.preheat_temp_max)))
                //                 $obj1.siblings().css("background-color","#F9A6A6");
                //             if(!((res2>=data.abrasive_conductivity_min)&&(res2<=data.abrasive_conductivity_max)))
                //                 $obj2.siblings().css("background-color","#F9A6A6");
                //             if(!((res3>=data.rinse_water_conductivity_min)&&(res3<=data.rinse_water_conductivity_max)))
                //                 $obj3.siblings().css("background-color","#F9A6A6");
                //
                //         }
                //     },error:function () {
                //
                //     }
                // });
                url="/InspectionProcessOperation/saveProcess.action?id="+row.id;
            }else{
                hlAlertTwo();
            }
        }
        function searchPro() {
            $('#proDatagrids').datagrid('load',{
                'pipe_no': $('#pipeno').val(),
                'process_code': $('#processcode').val(),
                'operator_no': $('#operatorno').val(),
                'begin_time': $('#begintime').val(),
                'end_time': $('#endtime').val(),
                'mill_no': $('#millno').val()
            });
        }
        function proFormSubmit() {
            var arr={};
            $('#dynamicTable').find('input,select').each(function () {
                var item_code="",item_value="";
                var input=$(this).siblings('input');
                if(input!=undefined){
                    item_code=input.attr('name');
                    item_value=$(this).val();
                    if(item_code!=undefined){
                        arr[item_code]=item_value;
                    }
                }
            });
            alert(arr);

            return;
            $('#proForm').form('submit',{
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
                    if($("input[name='opeartion_time']").val()==""){

                        hlAlertFour("请输入操作时间");
                        return false;
                    }
                    if($("input[name='result']").val()==""){
                        hlAlertFour("请输入结论!");
                        return false;
                    }

                    // setParams($("input[name='preheat_temp']"));
                },
                success: function(result){
                    clearFormLabel();
                    var result = eval('('+result+')');

                    if (result.success){
                        $('#hlProDialog').dialog('close');
                        $('#proDatagrids').datagrid('reload');
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
        function proCancelSubmit() {
            $('#hlProDialog').dialog('close');
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
            $('#proForm').form('clear');
            $('.hl-label').text('');
            $('#hl-gallery-con').empty();
            $(":input").each(function () {
                $(this).siblings().css("background-color","#FFFFFF");
            });
        }

        //生成检验项表单
        function GenerateInspectionItem(){
            if(look1==undefined)return;
            var pipe_no=look1.getValue();
            if(pipe_no==undefined)return;

            var process_code=$('#processcode').val();
            if(process_code==undefined||process_code=="")return;
            //根据管号和工序编号得到检验项表单项目
            $.ajax({
                url:"/DynamicItemOperation/getDynamicItemByPipeNoProcessCode.action",
                dataType:'json',
                data:{
                    pipe_no:pipe_no,
                    process_code:process_code
                },
                success:function (data) {

                    var div="";
                   for(var i=0;i<data.length;i++){
                       if((i==0)||(i%2==0)){
                           div+="<tr>"
                       }
                       div+=getTemplate(data[i]);
                   }
                    div+="</tr>";
                    $('#dynamicTable').empty();
                  $('#dynamicTable').append(div);
                  $.parser.parse("#dynamicTable");
                    JudgeMaxAndMIn();
                },error:function () {

                }
            });





        }


        //checkbox 点击事件
        function checkboxChecked(obj){
            var name=obj.prop("name");
            alert(obj.prop("name"));
            if(obj.is(":checked")){
                obj.prop('checked', true);
                $("input[name='"+name+"']").val(1);
            }else{
                obj.prop('checked', false);
                $("input[name='"+name+"']").val(0);
            }
        }


        //验证
        function verification(obj){
            alert("verification");
            obj.siblings().css("background-color","#FF0000");
        }


        //根据字段的属性动态生成控件
        function getTemplate(item){
            var controltype=item.control_type;//控件类型
            var itemcode=item.item_code;//控件编号
            var itemname=item.item_name; //中文名
            var itemnameen=item.item_name_en;//英文名
            var unitname=item.unit_name; //单位名称
            var unitnameen=item.unit_name_en; //单位名称英文名
            var itemfrequency=item.item_frequency; //检测频率
            var processcode=item.process_code; //工序编号
            var decimalnum=item.decimal_num; //小树位数
            var needverify=item.need_verify; //是否验证
            var options=item.options; //选项
            var maxvalue=item.max_value; //最大值
            var minvalue=item.min_value; //最小值
            var defaultvalue=item.default_value; //默认值

            var div="";
            var language=getCookie("userLanguage");

            var minmax=minvalue+"~"+maxvalue;
            var frequencydiv="";
            var frequencydiven="";
            if(itemfrequency!=undefined&&itemfrequency==0){
                frequencydiv="每根检测";
                frequencydiven=" Inspect Every Pipe";
            }else if(itemfrequency!=undefined&&itemfrequency!=0){
                frequencydiv=" 每"+itemfrequency+"小时检测一次";
                frequencydiven=" Inspect Every"+itemfrequency+"hour(s)";
            }


            if(language&&language=="en"){
                if(unitnameen!=undefined&&unitnameen!=""){
                    unitnameen="("+unitnameen+")";
                }
                div=
                    "<td width=\"25%\" class=\"\">"+itemnameen+unitnameen+" "+minmax+frequencydiven+"</td>" +
                    "<td>";
            }else{
                if(unitname!=undefined&&unitname!=""){
                    unitname="("+unitname+")";
                }

                div=
                    "<td width=\"25%\" class=\"\">"+itemname+unitname+" "+minmax+frequencydiv+"</td>" +
                    "<td>";
            }

            var controldiv="";

            if(controltype=="singleselect"){//单选
                controldiv="<select id=\""+itemcode+"\"  defaultvalue=\""+defaultvalue+"\"  maxvalue=\""+maxvalue+"\"  minvalue=\""+minvalue+"\" needverify=\""+needverify+"\" class=\"easyui-combobox\" data-options=\"editable:false\" name=\""+itemcode+"\" style=\"width:200px;\" >";
                var optionArr=[];
                optionArr=options.split(';');
                var optiondiv="";
                for (var i=0;i<optionArr.length;i++){
                    var select="";
                    if(i==0){
                        select="selected=\"selected\"";
                    }
                    optiondiv+="<option value=\""+optionArr[i]+"\" "+select+">"+optionArr[i]+"</option>";
                }
                controldiv+=optiondiv;
                controldiv+="</select>";
            }else if(controltype=="singlenumber"){//单值数字
                controldiv="<input onchange=\"JudgeMaxAndMIn(this)\" class=\"easyui-numberbox\" "+"defaultvalue=\""+defaultvalue+"\"  maxvalue=\""+maxvalue+"\"  minvalue=\""+minvalue+"\" needverify=\""+needverify+"\""+"data-options=\"min:-99,precision:"+decimalnum+"\" type=\"text\" name=\"" + itemcode +"\" value=\""+defaultvalue+"\" style=\"width:200px;\"/>";
            }else if(controltype=="singletext"){//单值文本
                controldiv="<input class=\"easyui-textbox\" "+"defaultvalue=\""+defaultvalue+"\"  maxvalue=\""+maxvalue+"\"  minvalue=\""+minvalue+"\" needverify=\""+needverify+"\""+" type=\"text\" name=\"" + itemcode +"\" value=\""+defaultvalue+"\" style=\"width:200px;\"/>";
            }
            else if(controltype=="multinumber"){//多值数字
                controldiv="<input class=\"easyui-textbox\"  "+"defaultvalue=\""+defaultvalue+"\"  maxvalue=\""+maxvalue+"\"  minvalue=\""+minvalue+"\" needverify=\""+needverify+"\""+" type=\"text\" name=\"" + itemcode +"\" value=\""+defaultvalue+"\" style=\"width:200px;\"/>";
            }
            else if(controltype=="multitext"){//多值文本

                controldiv="<input class=\"easyui-textbox\" "+"defaultvalue=\""+defaultvalue+"\"  maxvalue=\""+maxvalue+"\"  minvalue=\""+minvalue+"\" needverify=\""+needverify+"\""+" type=\"text\" name=\"" + itemcode +"\" value=\""+defaultvalue+"\" style=\"width:200px;\"/>";
            }
            else if(controltype=="multiselect"){//多选
                controldiv="<select id=\""+itemcode+"\" class=\"easyui-combobox\" data-options=\"editable:false,multiple:true,multiline:false\" name=\""+itemcode+"\" style=\"width:200px;\">";
                var optionArr=[];
                optionArr=options.split(';');
                var optiondiv="<option value=\"\" ></option>";
                for (var i=0;i<optionArr.length;i++){
                    optiondiv+="<option value=\""+optionArr[i]+"\" >"+optionArr[i]+"</option>";
                }
                controldiv+=optiondiv;
                controldiv+="</select>";

            }else if(controltype=="checkbox"){//复选框
                controldiv="<input type=\"checkbox\" id=\""+itemcode+"\" value=\"0\" checked=\"false\" onchange=\"checkboxChecked(this)\"/>\n" +
                    "<input type=\"hidden\" name=\""+itemcode+"\" value=\"0\">";

            }else if(controltype=="textarea"){//多行文本
                controldiv="<input class=\"easyui-textbox\" type=\"text\" value=\""+defaultvalue+"\" name=\""+itemcode+"\" data-options=\"multiline:true\" style=\"width:300px;height:80px\" />";
            }
            div+=controldiv;
            var taildiv ="</td>";
            div+=taildiv;
            return div;
        }


        function JudgeMaxAndMIn(){
            //alert($(obj).attr('maxvalue')+":"+$(obj).val());
            $("input[type=text]").each(function(i){
                $(this).bind('input propertychange', function() {
                    var max=$(this).parent().siblings('input').attr('maxvalue');
                    var min=$(this).parent().siblings('input').attr('minvalue');
                    var value=$(this).val();
                    if(value!=undefined){
                        if(max!=undefined&&max!=""&&!isNaN(max)){
                            if(parseFloat(max)<parseFloat(value)){
                                $(this).css("background-color","#F9A6A6");
                            }else{
                                $(this).css("background-color","#FFFFFF");
                            }
                        }
                        if(min!=undefined&&min!=""&&!isNaN(min)){
                            if(parseFloat(min)>parseFloat(value)){
                                $(this).css("background-color","#F9A6A6");
                            }else{
                                $(this).css("background-color","#FFFFFF");
                            }
                        }
                    }
                });
            });

        }



    </script>





</head>

<body>
<fieldset class="b3" style="padding:10px;margin:10px;">
    <legend> <h3><b style="color: orange" >|&nbsp;</b><span class="i18n1" name="datadisplay">数据展示</span></h3></legend>
    <div  style="margin-top:5px;">
         <table class="easyui-datagrid" id="proDatagrids" url="/InspectionProcessOperation/getProcessByLike.action" striped="true" loadMsg="正在加载中。。。" textField="text" pageSize="20" fitColumns="true" pagination="true" toolbar="#hlProTb">
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

                       <%--<th field="surface_condition" align="center" width="120" class="i18n1" name="surfacecondition">外观缺陷</th>--%>
                       <%--<th field="salt_contamination_before_blasting" align="center" width="120" class="i18n1" name="saltcontaminationbeforeblasting">打砂前盐度</th>--%>
                       <%--<th field="alkaline_dwell_time" align="center" width="100" hidden="true" class="i18n1" name="alkalinedwelltime">碱洗时间</th>--%>
                       <%--<th field="alkaline_concentration" align="center" width="100" hidden="true" class="i18n1" name="alkalineconcentration">碱浓度</th>--%>
                       <%--<th field="rinse_water_conductivity" width="100" align="center" hidden="true" class="i18n1" name="rinsewaterconductivity">冲洗水传导性</th>--%>

                       <%--<th field="abrasive_conductivity" width="100" align="center" hidden="true" class="i18n1" name="abrasiveconductivity">磨料电导率</th>--%>
                       <%--<th field="acid_wash_time" width="100" align="center" hidden="true" class="i18n1" name="acidwashtime">酸洗时间</th>--%>
                       <%--<th field="acid_concentration" width="100" align="center" hidden="true" class="i18n1" name="acidconcentration">酸浓度</th>--%>
                       <%--<th field="blast_line_speed" align="center" width="120" hidden="true" class="i18n1" name="blastlinespeed">打砂传送速度</th>--%>
                       <%--<th field="preheat_temp" align="center" width="120" hidden="true" class="i18n1" name="preheattemp">预热温度</th>--%>
                       <%--<th field="marking" align="center" width="120" hidden="true" class="i18n1" name="marking">管体标识是否清晰</th>--%>
                   <th field="process_name" align="center" width="150" class="i18n1" name="processname">工序名称</th>
                   <th field="process_name_en" align="center" width="150" class="i18n1" name="processnameen">工序英文</th>
                   <th field="remark" align="center" width="150" class="i18n1" name="remark">备注</th>
                       <th field="result" align="center" width="150" class="i18n1" name="result">结论</th>
                       <th field="operation_time" align="center" width="150" class="i18n1" name="operationtime" data-options="formatter:formatterdate">操作时间</th>
               </tr>
             </thead>
         </table>

    </div>
</fieldset>

<!--工具栏-->
<div id="hlProTb" style="padding:10px;">

    <span class="i18n1" name="process">工序</span>:
    <input id="processcode" class="easyui-combobox" type="text" name="processcode"  data-options=
            "url:'/ProcessOperation/getAllProcessWithAllOption.action',
					        method:'post',
					        valueField:'process_code',
					        width: 140,
					        editable:false,
					        textField:'process_name',
					        panelHeight:'200'"/>

    <span class="i18n1" name="millno">分厂编号</span>:
    <input id="millno" class="easyui-combobox" type="text" name="millno"  data-options=
            "url:'/millInfo/getAllMillsWithComboboxSelectAll.action',
					        method:'post',
					        valueField:'id',
					        width: 140,
					        editable:false,
					        textField:'text',
					        panelHeight:'200'"/>
    <span class="i18n1" name="pipeno">钢管编号</span>:
    <input id="pipeno" name="pipeno" style="line-height:22px;border:1px solid #ccc">
    <span class="i18n1" name="operatorno">操作工编号</span>:
    <input id="operatorno" name="operatorno" style="line-height:22px;border:1px solid #ccc">
    <span class="i18n1" name="begintime">开始时间</span>:
    <input id="begintime" name="begintime" type="text" class="easyui-datebox" data-options="formatter:myformatter,parser:myparser">
    <span class="i18n1" name="endtime">结束时间</span>:
    <input id="endtime" name="endtime" type="text" class="easyui-datebox" data-options="formatter:myformatter,parser:myparser">
    <a href="#" class="easyui-linkbutton" plain="true" data-options="iconCls:'icon-search'" onclick="searchPro()">Search</a>
    <div style="float:right">
     <a href="#" id="addObpLinkBtn" class="easyui-linkbutton i18n1" name="add" data-options="iconCls:'icon-add',plain:true" onclick="addPro()">添加</a>
     <a href="#" id="editObpLinkBtn" class="easyui-linkbutton i18n1" name="edit" data-options="iconCls:'icon-edit',plain:true" onclick="editPro()">修改</a>
     <a href="#" id="deltObpLinkBtn" class="easyui-linkbutton i18n1" name="delete" data-options="iconCls:'icon-remove',plain:true" onclick="delPro()">删除</a>
    </div>
</div>

<!--添加、修改框-->
<div id="hlProDialog" class="easyui-dialog" data-options="title:'添加',modal:true"  closed="true" buttons="#dlg-buttons" style="display: none;padding:5px;width:950px;height:auto;">
   <form id="proForm" method="post">
       <fieldset style="width:900px;border:solid 1px #aaa;margin-top:8px;position:relative;">
           <legend class="i18n1" name="pipebasicinfo">钢管信息</legend>
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
                              textField="pipe_no" valueField="pipe_no" popupWidth="auto"
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
           <legend class="i18n1" name="odproductioninfo">外喷砂生产信息</legend>

       <table class="ht-table">
           <tr>
               <td class="i18n1" name="id" width="20%">流水号</td>
               <td colspan="1" width="30%"><label class="hl-label" id="odbpid"></label></td>
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

       <table class="dynamic-table" id="dynamicTable">

           <%--以下检测项自动生成--%>
           <%--<tr>--%>
               <%--<td width="16%" class="i18n1" name="marking">管体标识是否清晰</td>--%>
               <%--<td>--%>
                   <%--<select id="mk" class="easyui-combobox" data-options="editable:false" name="marking" style="width:200px;">--%>
                       <%--<option value="0" selected="selected">清晰</option>--%>
                       <%--<option value="1">不清晰</option>--%>
                   <%--</select>--%>
                   <%--<input class="easyui-numberbox" data-options="min:-99,precision:1" type="text" name="preheat_temp" value=""/>--%>
               <%--</td>--%>
               <%--<td></td>--%>
               <%--<td width="16%"  class="i18n1" name="surfacecondition">外观缺陷</td>--%>
               <%--<td>--%>

                   <%--&lt;%&ndash;<input class="easyui-validatebox" type="text" name="surface_condition" value=""/>&ndash;%&gt;--%>
                   <%--<div id="combobox1" class="mini-combobox" style="width:185px;"  popupWidth="185" textField="defect_name" valueField="defect_name"--%>
                        <%--url="/DefectOperation/getAllSteelDefectInfo.action" name="surface_condition" multiSelect="true"  showClose="true" oncloseclick="onComboxCloseClick" >--%>
                       <%--<div property="columns">--%>
                           <%--<div header="缺陷类型" field="defect_name"></div>--%>
                       <%--</div>--%>
                   <%--</div>--%>

               <%--</td>--%>
               <%--<td></td>--%>
           <%--</tr>--%>
           <%--<tr>--%>
               <%--<td width="16%" class="i18n1" name="preheattemp">预热温度</td>--%>
               <%--<td><input class="easyui-numberbox" data-options="min:-99,precision:1" type="text" name="preheat_temp" value=""/></td>--%>
               <%--<td></td>--%>
               <%--<td width="16%" class="i18n1" name="saltcontaminationbeforeblasting">打砂前盐度</td>--%>
               <%--<td><input class="easyui-numberbox" data-options="min:-99,precision:2" type="text" name="salt_contamination_before_blasting" value=""/></td>--%>
               <%--<td></td>--%>
           <%--</tr>--%>
           <%--<tr>--%>
               <%--<td class="i18n1" name="alkalinedwelltime">碱洗时间</td>--%>
               <%--<td><input class="easyui-numberbox hl-errorcolor" data-options="min:-99,precision:0" type="text" name="alkaline_dwell_time" value=""/></td>--%>
               <%--<td></td>--%>
               <%--<td class="i18n1" name="alkalineconcentration">碱浓度</td>--%>
               <%--<td><input class="easyui-numberbox"  data-options="min:-99,precision:2" type="text" name="alkaline_concentration" value=""/></td>--%>
               <%--<td></td>--%>
           <%--</tr>--%>

           <%--<tr>--%>
               <%--<td class="i18n1" name="acidwashtime">酸洗时间</td>--%>
               <%--<td><input class="easyui-numberbox" data-options="min:-99,precision:0" type="text" name="acid_wash_time" value=""/></td>--%>
               <%--<td></td>--%>
               <%--<td class="i18n1" name="acidconcentration">酸浓度</td>--%>
               <%--<td><input class="easyui-numberbox" data-options="min:-99,precision:2" type="text" name="acid_concentration" value=""/></td>--%>
               <%--<td></td>--%>
           <%--</tr>--%>


           <%--<tr>--%>
               <%--<td width="16%" class="i18n1" name="rinsewaterconductivity">冲洗水电导率</td>--%>
               <%--<td><input class="easyui-numberbox" data-options="min:-99,precision:2" type="text" name="rinse_water_conductivity" value=""/></td>--%>
               <%--<td></td>--%>
               <%--<td width="16%" class="i18n1" name="abrasiveconductivity">磨料电导率</td>--%>
               <%--<td><input class="easyui-numberbox" data-options="min:-99,precision:2" type="text" name="abrasive_conductivity" value=""/></td>--%>
               <%--<td></td>--%>
           <%--</tr>--%>
           <%--<tr>--%>
               <%--<td width="16%" class="i18n1" name="blastlinespeed">打砂传送速度</td>--%>
               <%--<td><input class="easyui-numberbox" data-options="min:-99,precision:2" type="text" name="blast_line_speed" value=""/></td>--%>
               <%--<td></td>--%>
               <%--<td></td>--%>
               <%--<td></td>--%>
               <%--<td></td>--%>
           <%--</tr>--%>
               <%--以上检测项自动生成--%>
       </table>

       <table class="dynamic-table">
           <tr>
               <td class="i18n1" name="result">结论</td>
               <td><select style="width:100%;" id="cc" class="easyui-combobox" data-options="editable:false" name="result">
                   <option value="0">不合格,重新打砂处理</option>
                   <option value="1">合格,进入外喷砂检验工序</option>
                   <option value="10">待定</option>
                   <option value="3">隔离，进入修磨或切割工序</option>
               </select></td>
               <td class="i18n1" name="remark">备注</td>
               <td><input class="easyui-textbox" type="text" value="" name="remark" data-options="multiline:true" style="height:60px;width:100%;"/></td>
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
    <a href="#" class="easyui-linkbutton" iconCls="icon-save" onclick="proFormSubmit()">Save</a>
    <a href="#" class="easyui-linkbutton" id="hlcancelBtn" operationtype="add" iconCls="icon-cancel" onclick="proCancelSubmit()">Cancel</a>
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
            <div field="pipe_no" width="80" headerAlign="center" allowSort="true" class="i18n1" name="pipeno">钢管编号Pipe No.</div>
            <div field="contract_no" width="80" headerAlign="center" allowSort="true" class="i18n1" name="contractno">合同编号Contract No.</div>
            <div field="status" width="40" headerAlign="center" allowSort="true" class="i18n1" name="status">状态 Status</div>
            <div field="od" width="40" headerAlign="center" allowSort="true" class="i18n1" name="od">外径 OD</div>
            <div field="wt" width="40" headerAlign="center" allowSort="true" class="i18n1" name="wt">壁厚 WT</div>
            <div field="p_length" width="40" headerAlign="center" allowSort="true" class="i18n1" name="p_length">长度 Length</div>
            <div field="weight" width="40" headerAlign="center" allowSort="true" class="i18n1" name="weight">重量 Weight</div>
        </div>
    </div>
</div>
<div id="gridPanel2" class="mini-panel" title="header" iconCls="icon-add" style="width:480px;height:250px;"
     showToolbar="true" showCloseButton="true" showHeader="false" bodyStyle="padding:0" borderStyle="border:0"
>
    <div property="toolbar" id="searchBar2" style="padding:5px;text-align:center;display:none;">
        <div style="float:left;padding-bottom:2px;">
            <span class="i18n1" name="operatorno">操作工编号 Operator No.</span><span>:</span>
            <input id="keyText3" class="mini-textbox" style="width:110px;" onenter="onSearchClick(2)"/>
            <span class="i18n1" name="operatorname">姓名 Name</span><span>:</span>
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
                pipestatus:'bare1,'
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
                    GenerateInspectionItem();
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
    // combox1.on("showpopup",function () {
    //     $('.mini-shadow').css('z-index','99999');
    //     $('.mini-popup').css('z-index','100000');
    //     $('.mini-panel').css('z-index','100000');
    // });
    function onComboxCloseClick(e) {
        var obj = e.sender;
        obj.setText("");
        obj.setValue("");
    }
    hlLanguage("../i18n/");
</script>