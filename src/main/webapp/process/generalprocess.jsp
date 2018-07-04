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
        var temprow;
        //var g_statuslist=undefined;// input status



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

                $('#mill_no').combobox({
                    onLoadSuccess:function(){
                        var data = $('#mill_no').combobox('getData');
                        $('#mill_no').combobox('select',data[0].value);
                    },
                    onChange: function (newValue, oldValue) {
                        //alert(newValue);
                        if(newValue!=undefined){
                            GenerateInspectionItem(temprow);
                        }

                    }
                });


               $('.mini-buttonedit .mini-buttonedit-input').css('width','150px');
               // hlLanguage("../i18n/");
        });
        function addPro(){
            var pcode=$('#processcode').val();
            if(pcode==undefined||pcode==""){
                hlAlertFour("请先选择工序");
                return;
            }
            $('#hlcancelBtn').attr('operationtype','add');
            $('#hlProDialog').dialog('open').dialog('setTitle','新增');
            //$('#legend-title').text(row.process_name);
            clearFormLabel();
            clearMultiUpload(grid);

            $('#process_code').val(pcode);
            LoadProcess_input_output();
            $('#legend-title').text($('#processcode').combobox('getText'));
            $("#process_name").textbox("setValue", $('#processcode').combobox('getText'));

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
                        $.post("/InspectionProcessOperation/delProcess.action",
                            {hlparam:idArrs},function (data) {
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
                //$('#process_code').val(row.process_code);
                $('#legend-title').text(row.process_name);
                loadPipeBaiscInfo(row);
                $('#odbpid').text(row.id);
                row.operation_time=getDate1(row.operation_time),
                $('#proForm').form('load',row);
                //设置多选框值
                LoadProcess_input_output();

                //$multiselectObj.combobox('setValues',valueArr);
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

                GenerateInspectionItem(row);



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

            $("#dynamicTable :input[dynamic='dynamic'],select[dynamic='dynamic']").each(function(i){
                var dy=$(this).attr("dynamic");
                if(dy!=undefined&&dy=="dynamic"){
                    var item_code=$(this).attr("myname");
                    if(item_code!=undefined) {
                        var controltype = $(this).attr("controltype");
                        if(controltype!=undefined&&controltype=="multiselect"){
                            arr[item_code]=$(this).combobox('getText');
                        }else{
                            arr[item_code]=this.value;
                        }

                    }
                }
            });

            alert(JSON.stringify( arr ));

            $('#dynamicJson').val(JSON.stringify( arr ));



            // $('#dynamicTable').find('input,select').each(function () {
            //     var item_code="",item_value="";
            //     var input=$(this).siblings('input');
            //     if(input!=undefined){
            //         alert(input);
            //         var dy=input.attr('dynamic');
            //         if(dy!=undefined&&dy=="dynamic"){
            //             //得到动态测量编号及值的配对
            //             item_code=input.attr('name');
            //             item_value=$(this).val();
            //             alert(item_code);
            //             alert(item_value);
            //             if(item_code!=undefined){
            //                 arr[item_code]=item_value;
            //             }
            //         }
            //     }
            // });
            // alert(JSON.stringify( arr ));


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
            $('#dynamicTable').empty();
            $(":input").each(function () {
                $(this).siblings().css("background-color","#FFFFFF");
            });
        }
        var multipleElectionArr=[];
        var checkboxArr=[];
        //生成检验项表单
        function GenerateInspectionItem(row){
            multipleElectionArr.length=0;
            checkboxArr.length=0;
            var pipe_no=$("input[name='pipe_no']").val();
            //alert(pipe_no);
            if(pipe_no==undefined||pipe_no=="")return;
            if($('#mill_no').val()==undefined||$('#mill_no').val()==""){
                alert("请选择mill");
                return;
            }

            var process_code=$('#process_code').val();
            if(process_code==undefined||process_code=="")return;
            //根据管号和工序编号得到检验项表单项目
            $.ajax({
                url:"/DynamicItemOperation/getDynamicItemByPipeNoProcessCodeHeaderCode.action",
                dataType:'json',
                data:{
                    pipe_no:pipe_no,
                    mill_no:$('#mill_no').val(),
                    process_code:process_code,
                    inspection_process_record_header_code:$('#inspection_process_record_header_code').val()
                },
                success:function (data) {

                   if(data==undefined||data.record==undefined)return;

                   var div="";
                   for(var i=0;i<data.record.length;i++){
                       if((i==0)||(i%2==0)){
                           div+="<tr>"
                       }
                       div+=getTemplate(data.record[i],data.needInspectionInfo);
                   }
                    div+="</tr>";
                    $('#dynamicTable').empty();
                  $('#dynamicTable').append(div);
                  $.parser.parse("#dynamicTable");
                    if(multipleElectionArr.length>0){
                        $.each(multipleElectionArr, function (n, value) {
                            var idName="#"+value.itemcode;
                            $(idName).combobox('setValues',value.itemvalue);
                        });
                    }
                    if(checkboxArr.length>0){
                        $.each(checkboxArr, function (n, value) {
                            var idName="#"+value.itemcode;
                            if(value.itemvalue=='1'){
                                var idName="#"+value.itemcode;
                                $(idName).attr('checked', true);
                                $("input[name='"+value.itemcode+"']").val(1);
                            }else{
                                $(idName).attr('checked', false);
                                $("input[name='"+value.itemcode+"']").val(0);
                            }
                        });
                    }
                    JudgeMaxAndMIn();
                    replaceStencilcontent(row);
                },error:function () {

                }
            });





        }


        //checkbox 点击事件
        function checkboxChecked(obj){
            var name=$(obj).attr("myname");
            if($(obj).is(":checked")){
                $(obj).prop('checked', true);
                $("input[name='"+name+"']").val(1);
            }else{
                $(obj).prop('checked', false);
                $("input[name='"+name+"']").val(0);
            }
        }
        //根据字段的属性动态生成控件
        function getTemplate(item,frequencyList){
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
            var itemvalue=item.item_value; // 若存在表单，则有该值，否则为undefined

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
            for(var i=0;frequencyList!=undefined&&i<frequencyList.length;i++){
                var InspectionItem=frequencyList[i].InspectionItem;
                var needInspectNow=frequencyList[i].needInspectNow;
                if(InspectionItem!=undefined&&needInspectNow!=undefined&&InspectionItem==itemcode){
                    frequencydiv+="<span style=\"color:red;\">*</span>";
                    frequencydiven+="<span style=\"color:red;\">*</span>";
                }
            }

            if(language&&language=="en"){
                if(unitnameen!=undefined&&unitnameen!=""){
                    unitnameen="("+unitnameen+")";
                }else{
                    unitnameen="";
                }
                div=
                    "<td width=\"25%\" class=\"\">"+itemnameen+unitnameen+" "+minmax+frequencydiven+"</td>" +
                    "<td>";
            }else{
                if(unitname!=undefined&&unitname!=""){
                    unitname="("+unitname+")";
                }else{
                    unitname="";
                }

                div=
                    "<td width=\"25%\" class=\"\">"+itemname+unitname+" "+minmax+frequencydiv+"</td>" +
                    "<td>";
            }

            var controldiv="";
            if(itemvalue!=undefined){
                defaultvalue=itemvalue;
            }

            if(controltype=="singleselect"){//单选
                controldiv="<select controltype=\""+controltype+"\" dynamic=\"dynamic\" id=\""+itemcode+"\"  defaultvalue=\""+defaultvalue+"\"  maxvalue=\""+maxvalue+"\"  minvalue=\""+minvalue+"\" needverify=\""+needverify+"\" class=\"easyui-combobox\" data-options=\"editable:false,panelHeight:'200'\" value=\""+defaultvalue+"\" myname=\""+itemcode+"\" name=\""+itemcode+"\" style=\"width:200px;\" >";
                var optionArr=[];
                optionArr=options.split(';');
                var optiondiv="";
                optiondiv+="<option value=\"\">请选择</option>";
                for (var i=0;i<optionArr.length;i++){
                    var select="";
                    if(itemvalue!=undefined&&itemvalue==optionArr[i]){
                        select="selected=\"selected\"";
                    }
                    // else if(itemvalue==undefined&&i==0){
                    //     select="selected=\"selected\"";
                    // }
                    optiondiv+="<option value=\""+optionArr[i]+"\" "+select+">"+optionArr[i]+"</option>";
                }
                controldiv+=optiondiv;
                controldiv+="</select>";
                $()
            }else if(controltype=="singlenumber"){//单值数字
                controldiv="<input controltype=\""+controltype+"\" dynamic=\"dynamic\" onchange=\"JudgeMaxAndMIn(this)\" class=\"easyui-numberbox\" "+"defaultvalue=\""+defaultvalue+"\"  maxvalue=\""+maxvalue+"\"  minvalue=\""+minvalue+"\" needverify=\""+needverify+"\" data-options=\"min:-99,precision:"+decimalnum+"\" type=\"text\" myname=\""+itemcode+"\" name=\"" + itemcode +"\" value=\""+defaultvalue+"\" style=\"width:200px;\"/>";
            }else if(controltype=="singletext"){//单值文本
                controldiv="<input controltype=\""+controltype+"\"  dynamic=\"dynamic\" class=\"easyui-textbox\" "+"defaultvalue=\""+defaultvalue+"\"  maxvalue=\""+maxvalue+"\"  minvalue=\""+minvalue+"\" needverify=\""+needverify+"\" type=\"text\" name=\"" + itemcode +"\" myname=\"" + itemcode +"\" value=\""+defaultvalue+"\" style=\"width:200px;\"/>";
            }
            else if(controltype=="multinumber"){//多值数字
                controldiv="<input controltype=\""+controltype+"\" dynamic=\"dynamic\" class=\"easyui-textbox\"  "+"defaultvalue=\""+defaultvalue+"\"  maxvalue=\""+maxvalue+"\"  minvalue=\""+minvalue+"\" needverify=\""+needverify+"\" type=\"text\" name=\"" + itemcode +"\" myname=\"" + itemcode +"\"  value=\""+defaultvalue+"\" style=\"width:200px;\"/>";
            }
            else if(controltype=="multitext"){//多值文本

                controldiv="<input controltype=\""+controltype+"\"  dynamic=\"dynamic\" class=\"easyui-textbox\" "+"defaultvalue=\""+defaultvalue+"\"  maxvalue=\""+maxvalue+"\"  minvalue=\""+minvalue+"\" needverify=\""+needverify+"\" type=\"text\" name=\"" + itemcode +"\" myname=\"" + itemcode +"\" value=\""+defaultvalue+"\" style=\"width:200px;\"/>";

            }
            else if(controltype=="multiselect"){//多选
                controldiv="<select id=\""+itemcode+"\" controltype=\""+controltype+"\" dynamic=\"dynamic\"  class=\"easyui-combobox\" data-options=\"editable:false,multiple:true,panelHeight:'200'\" name=\""+itemcode+"\" myname=\"" + itemcode +"\" value=\""+defaultvalue+"\" style=\"width:200px;\">";
                var optionArr=[];
                optionArr=options.split(';');
                var optiondiv="<option value=\"\" >无</option>";
                //var optiondiv="";
                var valueArr=[];
                for (var i=0;i<optionArr.length;i++){
                    if(itemvalue!=undefined){
                        valueArr=itemvalue.split(',');
                        if(valueArr.length>0){
                            multipleElectionArr.push({'itemcode':itemcode,'itemvalue':valueArr});
                        }
                    }
                    optiondiv+='<option value='+optionArr[i]+'>'+optionArr[i]+'</option>';
                }
                controldiv+=optiondiv;
                controldiv+="</select>";

            }else if(controltype=="checkbox"){//复选框
                controldiv="<input controltype=\""+controltype+"\"  type=\"checkbox\" id=\""+itemcode+"\" myname=\"" + itemcode +"\" value=\"1\" checked=\"false\" onchange=\"checkboxChecked(this)\"/>\n" +
                    "<input dynamic=\"dynamic\" type=\"hidden\" myname=\"" + itemcode +"\" name=\""+itemcode+"\" value=\"0\">";
                if(itemvalue!=undefined&&itemvalue!=""){
                    checkboxArr.push({'itemcode':itemcode,'itemvalue':itemvalue});
                }else{
                    checkboxArr.push({'itemcode':itemcode,'itemvalue':defaultvalue});
                }
            }else if(controltype=="textarea"){//多行文本
                controldiv="<input controltype=\""+controltype+"\" id=\""+itemcode+"\" dynamic=\"dynamic\" class=\"easyui-textbox\" type=\"text\" value=\""+defaultvalue+"\" myname=\"" + itemcode +"\" name=\""+itemcode+"\" data-options=\"multiline:true\" style=\"width:300px;height:80px\" />";
            }else if(controltype=="date"){//时间
                if(defaultvalue!=undefined)
                    controldiv="<input class=\"easyui-datetimebox\" dynamic=\"dynamic\" controltype=\""+controltype+"\"   myname=\""+itemcode +"\" name=\""+itemcode+"\" value=\""+defaultvalue+"\" defaultvalue=\""+defaultvalue+"\" editable=\"false\" data-options=\"formatter:myformatter2,parser:myparser2\" style=\"width:200px;\"/>";
                else
                    controldiv="<input class=\"easyui-datetimebox\" dynamic=\"dynamic\" controltype=\""+controltype+"\"   myname=\""+itemcode +"\" name=\""+itemcode+"\"  defaultvalue=\""+defaultvalue+"\" editable=\"false\" data-options=\"formatter:myformatter2,parser:myparser2\" style=\"width:200px;\"/>";
            }
            div+=controldiv;
            var taildiv ="</td>";
            div+=taildiv;
            return div;
        }

        //判断值范围是否合法
        function JudgeMaxAndMIn(){
            //alert($(obj).attr('maxvalue')+":"+$(obj).val());
            $("#dynamicTable :input[dynamic='dynamic'],select[dynamic='dynamic']").each(function(i){
                var $obj=$(this);
                var controltype = $obj.attr("controltype");
                var needverify=$obj.attr("needverify");
                if(needverify!="1"){
                    return;
                }
                $obj.siblings('span').find('input').css("background-color","#FFFFFF");
                setOutofBound(this,controltype);
                var reg = new RegExp("，","g");
                $obj.siblings('span').find('input[type=text]').bind('input propertychange', function() {
                    $(this).css("background-color","#FFFFFF");
                    var maxValue="",minValue="";
                    if(controltype=="singleselect"){
                        maxValue=$(this).parent().siblings('select').attr('maxvalue');
                        minValue=$(this).parent().siblings('select').attr('minvalue');
                    }else{
                        maxValue=$(this).parent().siblings('input').attr('maxvalue');
                        minValue=$(this).parent().siblings('input').attr('minvalue');
                    }
                    var value=$(this).val();
                    value=value.replace(reg,",");
                    var numArr=value.split(',');
                    for(var i=0;i<numArr.length;i++){
                        if(numArr!=undefined&&numArr[i]!=""&&!isNaN(numArr[i])){
                            if(maxValue!=undefined&&maxValue!=""&&!isNaN(maxValue)){
                                if(parseFloat(maxValue)<numArr[i]){
                                    $(this).css("background-color","#F9A6A6");
                                }
                            }
                            if(minValue!=undefined&&minValue!=""&&!isNaN(minValue)){
                                if(parseFloat(minValue)>parseFloat(numArr[i])){
                                    $(this).css("background-color","#F9A6A6");
                                }
                            }
                        }
                    }

                });
            });
        }
        function setOutofBound(obj,controltype) {
            //obj.siblings('input').css("background-color","#FFFFFF");
            var maxValue="",minValue="";
            var numArr=[];
            maxValue=$(obj).attr('maxvalue');
            minValue=$(obj).attr('minvalue');
            var value=$(obj).siblings('span').find('input[type=text]').val();
            var reg = new RegExp("，","g");
            if(value!=undefined){
                value=value.replace(reg,",");
                numArr=value.split(',');
            }
            for(var i=0;i<numArr.length;i++){
                if(numArr!=undefined&&numArr[i]!=""&&!isNaN(numArr[i])){
                    if(maxValue!=undefined&&maxValue!=""&&!isNaN(maxValue)){
                        if(parseFloat(maxValue)<numArr[i]){
                            $(obj).siblings('span').find('input').css("background-color","#F9A6A6");
                        }
                    }
                    if(minValue!=undefined&&minValue!=""&&!isNaN(minValue)){
                        if(parseFloat(minValue)>parseFloat(numArr[i])){
                            $(obj).siblings('span').find('input').css("background-color","#F9A6A6");
                        }
                    }
                }
            }
        }
        //获取喷标内容
        function replaceStencilcontent(row){
            if(row==undefined)
                return;
            //异步获取标准并匹配
            $.ajax({
                url:'/DynamicItemOperation/getOdIdStencilContentModel.action',
                dataType:'json',
                data:{'contract_no':row.contract_no},
                success:function (data) {
                    var od_stencil_content="",id_stencil_content="";
                    if(data!=null) {
                        if(data[0].default_value!=undefined)
                            od_stencil_content=data[0].default_value;
                        if(data[1].default_value!=undefined)
                            id_stencil_content=data[1].default_value;
                        if(od_stencil_content!=undefined&&od_stencil_content!=""){
                            var str=od_stencil_content;
                            str=str.replace(/\[OD\]/g, row.od);
                            str=str.replace(/\[WT\]/g, row.wt);
                            str=str.replace(/\[GRADE\]/g, row.grade);
                            str=str.replace(/\[CONTRACTNO\]/g, row.contract_no);
                            str=str.replace(/\[COATINGSPEC\]/, row.coating_standard);
                            str=str.replace(/\[CLIENTSPEC\]/, row.client_spec);
                            str=str.replace(/\[PROJECTNAME\]/g, row.project_name);
                            str=str.replace(/\[PIPENO\]/g, row.pipe_no);
                            str=str.replace(/\[PIPELENGTH\]/, row.p_length);
                            var halflength=row.p_length*0.5;
                            str=str.replace(/\[HALFLENGTH\]/, halflength);
                            str=str.replace(/\[HEATNO\]/, row.heat_no);
                            str=str.replace(/\[BATCHNO\]/, row.pipe_making_lot_no);
                            var kg=row.weight*1000;
                            str=str.replace(/\[WEIGHT\]/, kg);
                            var coatingdate=getDateWithoutTime(row.od_coating_date)
                            str=str.replace(/\[COATINGDATE\]/, coatingdate);
                            if($("input[name='od_stencil_content']")!=undefined){
                                $("#od_stencil_content").textbox("setValue", str);
                            }

                        }
                        if(id_stencil_content!=undefined&&id_stencil_content!=""){
                            var str=id_stencil_content;
                            str=str.replace(/\[OD\]/, row.od);
                            str=str.replace(/\[WT\]/, row.wt);
                            str=str.replace(/\[PIPENO\]/, row.pipe_no);
                            str=str.replace(/\[PIPELENGTH\]/, row.p_length);
                            str=str.replace(/\[WEIGHT\]/, row.weight);
                            var coatingdate=getDateWithoutTime(row.id_coating_date)
                            str=str.replace(/\[COATINGDATE\]/, coatingdate);
                            if($("#id_stencil_content")!=undefined)
                                $("#id_stencil_content").textbox("setValue", str);
                        }
                    }
                },error:function () {
                    alert("Could not getODAcceptanceCriteriaByContractNo");
                }
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

                   <th field="inspection_process_record_header_code" align="center" width="150" class="i18n1" name="inspectionprocessrecordheadercode">检验表单编号</th>
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
           <legend id="legend-title" class="" name="legend-title"></legend>

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
					        panelHeight:'200'"/>
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
           <tr>
               <td class="i18n1" name="processname" width="20%">工序名称</td>
               <td colspan="1" width="30%">
                   <input id="process_name" class="easyui-textbox" readonly="true" type="text" name="process_name" value=""/>
               </td>
               <td class="i18n1" name="inspectionprocessrecordheadercode" width="20%"></td>
               <td colspan="1" width="30%">
                   <input id="inspection_process_record_header_code" class="easyui-textbox" readonly="true" type="text" name="inspection_process_record_header_code" value=""/>
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

       <table class="ht-table">
           <tr>
               <td class="i18n1" name="result" width="20%">结论</td>
               <td colspan="3"><select id="result" style="width:200px;" class="easyui-combobox" data-options="editable:false" name="result" >

               </select><span style="color:red;">*：本次必填</span>
               </td>
           </tr>
           <tr>
               <td class="i18n1" name="remark" width="20%">备注</td>
               <td colspan="3"><input class="easyui-textbox" type="text" value="" name="remark" data-options="multiline:true" style="height:60px;width:100%;"/></td>
           </tr>
       </table>
           <input type="hidden" id="fileslist" name="upload_files" value=""/>
           <input type="text" id="process_code" name="process_code" value=""/>
           <input type="hidden" id="dynamicJson" name="dynamicJson" value=""/>
           <input type="text" id="outputJson" name="outputJson" value=""/>
           <input type="text" id="inputStatusList" name="inputStatusList" value=""/>
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
    <a href="#" class="easyui-linkbutton i18n1" name="submit" iconCls="icon-save" onclick="proFormSubmit()">Submit</a>
    <a href="#" class="easyui-linkbutton i18n1" name="cancel" id="hlcancelBtn" operationtype="add" iconCls="icon-cancel" onclick="proCancelSubmit()">Cancel</a>
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


    //初始化工序的input和output
    function LoadProcess_input_output(){
        //获得本工序的input status
        $.ajax({
            url:'../data/process_input_output.json',
            data:{},
            dataType:'json',
            success:function (data) {
                if(data!=null&&data!=""){
                    for(var i=0;i<data.length;i++){
                        if($('#process_code').val()!=undefined&&data[i].process_code==$('#process_code').val()){
                            //初始化input statuslist
                            //g_statuslist=data[i].input;

                            $('#inputStatusList').val(data[i].input);

                            //初始化result list
                            var options = [];
                            var language=getCookie("userLanguage");

                            //用于后台的pipe status 设置规则

                            var arr={};
                            arr["output"]=data[i].output;
                            arr["additionParams"]=data[i].additionParams;
                            $('#outputJson').val(JSON.stringify(arr));

                            $.each(data[i].output, function(i,val){
                                //这里的"text","id"和html中对应
                                //alert(val);
                                if(language&&language=="en"){
                                    options.push({ "text": val.result_name_en, "value": val.result });
                                }else{
                                    options.push({ "text": val.result_name, "value": val.result });
                                }
                            })
                            $("#result").combobox("loadData", options);
                            break;
                        }
                    }
                    //alert(statuslist);
                }
            },
            error:function () {
                hlAlertThree();
            }
        });


    }

    function onSearchClick(type) {
        if(type==1)
        {
            if($('#inputStatusList').val()!=undefined){
                grid1.load({
                    pipe_no:keyText1.value,
                    pipestatus:$('#inputStatusList').val()
                });
            }else{
                LoadProcess_input_output();
            }
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
                    GenerateInspectionItem(data[0]);
                    temprow=data[0];
                    //replaceStencilcontent();
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

        if($('#inputStatusList').val()!=undefined){
            grid1.load({
                pipe_no:keyText1.value,
                pipestatus:$('#inputStatusList').val()
            });
        }else{
            LoadProcess_input_output();
        }

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