<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
    String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>检验标准</title>
    <link rel="stylesheet" type="text/css" href="../easyui/themes/bootstrap/easyui.css">
    <link rel="stylesheet" type="text/css" href="../easyui/themes/icon.css">
    <link href="../miniui/multiupload/multiupload.css" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" href="../css/common.css"/>
    <script src="../easyui/jquery.min.js" type="text/javascript"></script>
    <script src="../js/common.js" type="text/javascript"></script>
    <script src="../miniui/boot.js" type="text/javascript"></script>
    <script src="../miniui/fileupload/swfupload/swfupload.js" type="text/javascript"></script>
    <script src="../miniui/multiupload/multiupload.js" type="text/javascript"></script>
    <script  src="../js/lrscroll.js" type="text/javascript"></script>
    <script src="../js/jquery.i18n.properties-1.0.9.js" type="text/javascript"></script>
    <script src="../js/language.js" type="text/javascript"></script>
    <script type="text/javascript" src="../easyui/jquery.easyui.min.js"></script>
    <script src="../js/datagrid-scrollview.js" type="text/javascript"></script>
    <style type="text/css">
        .prev-table {
            border-collapse: collapse;
            margin: 0 auto;
            text-align: center;
            margin-bottom:15px;
        }

        .prev-table td,
        .prev-table th {
            border: 1px solid #cad9ea;
            color: #666;
            height: 30px;
        }

        .prev-table thead th {
            width: 100px;
        }

        .prev-table tr:nth-child(odd) {
            background: #fff;
        }

        .prev-table tr:nth-child(even) {
            background: #F5FAFA;
        }
        .descinfo{
            float: left;
            width:50%;
        }
        .loginfo{
            width:50%;
            float: left;
            max-height:800px;
            text-align: left;
            overflow-y: scroll;
        }
    </style>
    <script type="text/javascript">
        var url;
        var staticItem=[];
        var addOrEdit=true;
        $(function () {
            getRawMaterialType();
            $('#addEditDialog').css('top','5px');
            $('#addEditDialog').dialog({
                onClose:function () {
                    clearFormLabel();
                }
            });
            $('.mini-buttonedit .mini-buttonedit-input').css('width','150px');
        });
        function addFunction(){
            $('#dynamicItem').hide();
            $('#hlcancelBtn').attr('operationtype','add');
            $('#addEditDialog').dialog('open').dialog('setTitle','新增');
            $('#serialNumber').text('');
            clearFormLabel();
            $("#acceptance_criteria_no").textbox('setValue',"AC"+new Date().getTime());
            url="/ACOperation/saveAC.action";
        }
        function delFunction() {
            var row = $('#contentDatagrids').datagrid('getSelections');
            if(row.length>0){
                var idArr=[];
                for (var i=0;i<row.length;i++){
                    idArr.push(row[i].id);
                }
                var idArrs=idArr.join(',');
                $.messager.confirm('系统提示',"您确定要删除这<font color=red>"+idArr.length+ "</font>条标准吗？",function (r) {
                    if(r){
                        $.post("/ACOperation/delAC.action",{hlparam:idArrs,forcedel:"0"},function (data) {
                            if(data.success){
                                $("#contentDatagrids").datagrid("reload");
                                hlAlertFour(data.message);
                            }
                            else if(data.flag){//存在关联数据
                                //再次确认
                                $.messager.confirm('系统提示',data.message+"，确定强制删除这<font color=red>"+idArr.length+ "</font>条标准吗？",function (r) {
                                    if(r){
                                        $.post("/ACOperation/delAC.action",{hlparam:idArrs,forcedel:"1"},function (ret) {
                                            if(ret.success){
                                                $("#contentDatagrids").datagrid("reload");
                                            }
                                            hlAlertFour(ret.message);
                                        },"json");
                                    }
                                });
                                //结束
                            }
                            else{
                                hlAlertFour(data.message);
                            }

                        },"json");
                    }
                });
            }else{
                hlAlertOne();
            }
        }
        function editFunction(){
            $('#dynamicItem').show();
            $('#hlcancelBtn').attr('operationtype','edit');
            var row = $('#contentDatagrids').datagrid('getSelected');
            if(row){
                $('#addEditDialog').dialog('open').dialog('setTitle','修改');
                row.last_update_time=getDate1(row.last_update_time);
                $('#addEditForm').form('load',row);
                $("#serialNumber").text(row.id);
                url="/ACOperation/saveAC.action?id="+row.id;
                loadDynamicItemInfo(row.acceptance_criteria_no);
            }else{
                hlAlertTwo();
            }
        }
        function previewFunction(){
            var row = $('#contentDatagrids').datagrid('getSelected');
            if(row){
                executePreview(row.acceptance_criteria_no);
            }else{
                hlAlertFour('请选择要预览的行!');
            }
        }
        function executePreview(acceptance_criteria_no) {
            $.ajax({
                url:"/DynamicItemOperation/getDynamicItemWithProcessInfoByACNo.action?acceptance_criteria_no="+acceptance_criteria_no,
                dataType:'json',
                data:{
                    acceptance_criteria_no:acceptance_criteria_no,
                    page:1,
                    rows:500
                },
                success:function (data) {
                    if(data&&data.rows.length>0){
                        var arr=data.rows;
                        var map={},dest=[];
                        for(var i = 0; i < arr.length; i++){
                            var ai = arr[i];
                            if(!map[ai.process_name]){
                                dest.push({
                                    process_name: ai.process_name,
                                    data: [ai]
                                });
                                map[ai.process_name] = ai;
                            }else{
                                for(var j = 0; j < dest.length; j++){
                                    var dj = dest[j];
                                    if(dj.process_name == ai.process_name){
                                        dj.data.push(ai);
                                        break;
                                    }
                                }
                            }
                        }
                        $('#previewContainer').empty();
                        for (var i=0;i<dest.length;i++){
                            $('#previewContainer').append(makePreviewInfo(dest[i].process_name,dest[i].data));
                        }
                    }
                    $('#previewContainer').dialog('open');

                },error:function () {
                    hlAlertFour("预览失败!");
                }
            });
        }
        function makePreviewInfo(title,data){
            var tempalate="";
            if(data.length>0){
                tempalate='<table class="prev-table">\n' +
                    '          <caption><h2>'+title+'</h2></caption>\n' +
                    '          <thead>\n' +
                    '            <tr>\n' +
                    '                <th>测量项名称</th>\n' +
                    '                <th>测量项名称英文</th>\n' +
                    '                <th>测量单位</th>\n' +
                    '                <th>测量单位英文</th>\n' +
                    '                <th>测量频率</th>\n' +
                    '                <th>最大值</th>\n' +
                    '                <th>最小值</th>\n' +
                    '                <th>默认值</th>\n' +
                    '                <th>选项</th>\n' +
                    '                <th>是否是特殊项目</th>\n' +
                    '            </tr>\n' +
                    '          </thead>\n'+
                    '          <tbody>\n';
                for (var i=0;i<data.length;i++){
                    var unit_name=data[i].unit_name==undefined?"":data[i].unit_name;
                    var unit_name_en=data[i].unit_name_en==undefined?"":data[i].unit_name_en;
                    var options=data[i].options==undefined?"":data[i].options;
                    var is_special_item=data[i].is_special_item==0?"否":"是";
                    var item_frequency=data[i].item_frequency;
                    if(item_frequency!=undefined){
                        if(item_frequency==1000){
                            item_frequency="每班开机时检验一次";
                        }else if(item_frequency==2000){
                            item_frequency="每班开工时检验一次";
                        }else if (item_frequency==3000) {
                            item_frequency="每班开机时检验一次";
                        }else if(item_frequency>0){
                            item_frequency = "每" + items[i].item_frequency + "小时一次";
                        }else {
                            item_frequency = "每根";
                        }
                    }
                    tempalate+='<tr><td>'+data[i].item_name+'</td><td>'+data[i].item_name_en+'</td><td>'+unit_name+'</td><td>'+unit_name_en+'</td>';
                    tempalate+='<td>'+item_frequency+'</td><td>'+data[i].max_value+'</td><td>'+data[i].min_value+'</td><td>'+data[i].default_value+'</td>';
                    tempalate+='<td>'+options+'</td><td>'+is_special_item+'</td></tr>';
                }
                tempalate+='</tbody></table>';
            }
            return tempalate;
        }
        function Search() {
            $('#contentDatagrids').datagrid('load',{
                'acceptance_criteria_no': $('#acceptancecriteriano').val(),
                'acceptance_criteria_name': $('#acceptancecriterianame').val(),
                'external_coating_type': $('#externalSelect').val(),
                'internal_coating_type': $('#internalSelect').val()
            });
        }
        // function  loadDynamicByAcceptanceNo(acceptance_criteria_no) {
        //     //dg加载测量项数据
        //     $('#dg').datagrid('load',{
        //         'acceptance_criteria_no':acceptance_criteria_no,
        //     });
        // }
        function addEditFormSubmit() {
            var temp=$('#hlcancelBtn').attr('operationtype');
            if(editIndex!=undefined&&temp=="edit"){
                hlAlertFour("请先保存测量项!");
                return false;
            }
            $('#addEditForm').form('submit',{
                url:url,
                onSubmit:function () {
                    if($("#acceptance_criteria_no").val()==undefined||$("#acceptance_criteria_no").val()==""){
                        hlAlertFour("未生成接收标准编号");
                        return false;
                    }
                },
                success: function(result){
                    var result = eval('('+result+')');
                    $('#addEditDialog').dialog('close');
                    if (result.success){
                        $('#contentDatagrids').datagrid('reload');
                    }
                    hlAlertFour(result.message);
                },
                error:function () {
                    hlAlertThree();
                }
            });
        }
        function CancelSubmit() {
            var temp=$('#hlcancelBtn').attr('operationtype');
            if(editIndex!=undefined&&temp=="edit"){
                hlAlertFour("请先保存测量项!");
                return false;
            }
            $('#addEditDialog').dialog('close');
        }

        function  clearFormLabel(){
            $('#addEditForm').form('clear');
            $('.hl-label').text('');
        }
        //测量项
        var editIndex = undefined;
        function endEditing(){
            if (editIndex == undefined){return true;}
            if ($('#dg').datagrid('validateRow', editIndex)){
                var ed = $('#dg').datagrid('getEditor', {index:editIndex,field:'item_frequency'});
                var text = $(ed.target).combobox('getText');
                $('#dg').datagrid('getRows')[editIndex]['item_frequency_name'] = text;
                var ed1 = $('#dg').datagrid('getEditor', {index:editIndex,field:'process_code'});
                var text1 = $(ed1.target).combobox('getText');
                $('#dg').datagrid('getRows')[editIndex]['process_name'] = text1;
                var ed2 = $('#dg').datagrid('getEditor', {index:editIndex,field:'control_type'});
                var text2 = $(ed2.target).combobox('getText');
                $('#dg').datagrid('getRows')[editIndex]['control_type_name'] = text2;

                $('#dg').datagrid('endEdit', editIndex);

                //alert("开始验证");
                editIndex = undefined;
                return true;
            } else {
                return false;
            }
        }
        function onClickRow(index){

            //alert(dgTop);
            // var $obj0=$('#dynamicItem').find('.datagrid-view');
            // alert("scrollTop="+$obj0.scrollTop()+",top="+$obj0.top+",offsetTop="+$obj0.offset().top);
            // var $obj=$('#dynamicItem').find('.datagrid-view').find('.datagrid-body');
            // alert("scrollTop="+$obj.scrollTop()+",top="+$obj.top+",offsetTop="+$obj.offset().top);
            //var clickScrollTop=0;
            if(editIndex!=undefined){
                $('#dg').datagrid('selectRow', editIndex);
                return;
            }
            if (editIndex != index){
                if (endEditing()){
                    $('#dg').datagrid('scrollTo',index);
                    $('#dg').datagrid('selectRow', index).datagrid('beginEdit', index);
                    editIndex = index;

                    //设置options和default_value的点击事件
                    setTextAreaEvent("options");
                    setTextAreaEvent("default_value");
                    var row = $('#dg').datagrid('getSelected');
                    if(row&&row.is_special_item!='0'){
                        disableEditor('process_code',editIndex,"combobox");
                        disableEditor('control_type',editIndex,"combobox");
                        disableEditor('item_name',editIndex,"textbox");
                        disableEditor('item_name_en',editIndex,"textbox");
                        //涂层总厚度 需要设置unit_name unit_name_en
                        //disableEditor('unit_name',editIndex,"textbox");
                        //disableEditor('unit_name_en',editIndex,"textbox");
                        disableEditor('options',editIndex,"textbox");
                        //涂层总厚度 需要设置item_frequency
                        //disableEditor('item_frequency',editIndex,"combobox");
                        //涂层总厚度 需要设置max_value min_value
                        //disableEditor('max_value',editIndex,"numberbox");
                        //disableEditor('min_value',editIndex,"numberbox");
                        //disableEditor('decimal_num',editIndex,"numberbox");
                        //disableEditor('need_verify',editIndex,"checkbox");
                    }
                    var $obj=$('#dynamicItem').find('.datagrid-view').find('.datagrid-body');
                    var dgTop=editIndex*25-(editIndex%8)*25;
                    $obj.scrollTop(dgTop);

                    //$obj.animate({"scrollTop":dgTop},1);
                } else {
                    $('#dg').datagrid('selectRow', editIndex);
                }
            }
        }
         var g_textarea_field=undefined;

        function disableEditor(field,editIndex,type) {
            var cellEdit = $('#dg').datagrid('getEditor', {index:editIndex,field:field});
            var $input = cellEdit.target;
            if(type=="combobox"){
                $input.combobox('disable');
            }else if(type=="numberbox"){
                $input.numberbox('disable');
            }else if(type=="textbox"){
                $input.textbox('disable');
            }else if(type=="checkbox"){
                $input[0].disabled=true;
            }
        }




        function append(){
            if(editIndex!=undefined){
                hlAlertFour("请先保存当前修改项!");
                return;
            }

            if (endEditing()){
                $('#dg').datagrid('insertRow',{index:0,row:{id:0,item_code:"IT-XXXX",decimal_num:'0',max_value:'0',min_value:'0',default_value:'0',status:'P',is_special_item:'0'}});
                editIndex = 0;
                $('#dg').datagrid('selectRow', editIndex)
                    .datagrid('beginEdit', editIndex);
                // editIndex = $('#dg').datagrid('getRows').length-1;
                // $('#dg').datagrid('selectRow', editIndex)
                //     .datagrid('beginEdit', editIndex);
                setTextAreaEvent("options");
                setTextAreaEvent("default_value");
                $("#dg").siblings('div').find('.datagrid-body').animate({scrollTop:0},100);
            }
        }

        //设置Editor的点击事件
        function setTextAreaEvent(field) {
            var ed = $('#dg').datagrid('getEditor', {index:editIndex,field:field});
            $(ed.target).textbox('textbox').bind('click', function() {
                var text=$(ed.target).textbox('getValue');
                if(text!=undefined)
                    $('#tempTextarea').val(text);
                $('#w').window('open');
                $('#winTitle').attr('name',field.replace(/_/g,''));
                //hlLanguage("../i18n/");

                g_textarea_field=field;
            });
        }
        function removeit(){
            var row = $('#dg').datagrid('getSelected');
            if(row){
                if (editIndex == undefined){return;}
                delItem();
            }
        }
        //接收事件
        function accept(){

            if (endEditing()){
                var row = $('#dg').datagrid('getSelected');
                if(row){
                    submitItemInfo(row);
                }
                else{
                    hlAlertFour("请选中要修改的项!");
                }
            }
        }
        //撤销事件
        function reject(){
            $('#dg').datagrid('rejectChanges');
            editIndex = undefined;
        }
        //保存测量项事件
        function submitItemInfo(row) {
            var acceptance_criteria_no=$('#acceptance_criteria_no').val();
            if(row&&acceptance_criteria_no!=undefined&&acceptance_criteria_no!=""){
                $.ajax({
                    url:'/DynamicItemOperation/saveDynamicItem.action',
                    dataType:'json',
                    data:{
                         id:row.id,
                         acceptance_criteria_no:acceptance_criteria_no,
                         item_code:row.item_code,
                         item_name:row.item_name,
                         item_name_en:row.item_name_en,
                         unit_name:row.unit_name,
                         unit_name_en:row.unit_name_en,
                         item_frequency:row.item_frequency,
                         process_code:row.process_code,
                         decimal_num:row.decimal_num,
                         need_verify:row.need_verify,
                         control_type:row.control_type,
                         options:row.options,
                         max_value:row.max_value,
                         min_value:row.min_value,
                         default_value:row.default_value,
                        is_special_item:row.is_special_item
                    },
                    success:function (data) {
                        //var result = eval('('+data+')');
                        if (data.success){
                            $('#dg').datagrid('acceptChanges');
                            loadDynamicItemInfo(data.acceptance_criteria_no);
                        }
                        hlAlertFour(data.message);
                    },error:function () {
                        hlAlertFour("保存失败!");
                    }
                });
            }else{
                hlAlertFour("保存失败!");
            }

        }
        //删除测量项事件
        function delItem() {
            var row = $('#dg').datagrid('getSelected');
            if(row){
                var is_special_item=row.is_special_item;
                if(is_special_item==undefined){
                    hlAlertFour("删除失败!");
                    return;
                }
                if(is_special_item!=undefined&&parseInt(is_special_item)==1){
                    hlAlertFour("特殊项不能删除!");
                    return;
                }
                var idArrs=row.id+",";
                $.messager.confirm('系统提示',"您确定要删除这条数据吗？",function (r) {
                    if(r){
                        $.post("/DynamicItemOperation/delDynamicItem.action",{hlparam:idArrs},function (data) {
                            //var result = eval('('+data+')');
                            if(data.success){
                                $('#dg').datagrid('cancelEdit', editIndex)
                                    .datagrid('deleteRow', editIndex);
                                editIndex = undefined;
                            }
                            hlAlertFour(data.message);
                        },"json");
                    }
                });
            }
            else{
                hlAlertFour("请选中要修改的项!");
            }
        }
        //保存TextArea事件
        function saveTextArea() {
             if(g_textarea_field!=undefined){
                 var val=$('#tempTextarea').val();
                 if(g_textarea_field=="options"){
                     //转换全角分号
                     val=changeSemicolon(val);
                     //去除逗号
                     val=changeComma(val);
                     val=removeComma(val);
                     val=removeEnter(val);
                 }

                 var ed = $('#dg').datagrid('getEditor', {index:editIndex,field:g_textarea_field});
                 $(ed.target).textbox('setValue',val)
             }
             $('#w').window('close');
        }
        //根据接收编号加载事件
        function loadDynamicItemInfo(acceptance_criteria_no) {
            $('#dg').datagrid({
                url:"/DynamicItemOperation/getDynamicItemWithProcessInfoByACNo.action?acceptance_criteria_no="+acceptance_criteria_no
            });
            $("#dg").datagrid('reload');
        }
        //导入事件
        function importItem(){
            //测量项导入来源
            var src_acceptance_criteria_no=$("input[name='acceptance_criteria_no_search']").val();
            //测量项导入去处
            var des_acceptance_criteria_no=$('#acceptance_criteria_no').val();
            if(src_acceptance_criteria_no==undefined||src_acceptance_criteria_no==""){
                hlAlertFour("没有找到源接收标准编号!");return false;
            }
            if(des_acceptance_criteria_no==undefined||des_acceptance_criteria_no==""){
                hlAlertFour("请选择要导入的目标接收标准编号!");return false;
            }
            //alert(src_acceptance_criteria_no+":"+des_acceptance_criteria_no);
            $.ajax({
                url:'/DynamicItemOperation/importDynamicItem.action',
                dataType:'json',
                data:{
                    src_acceptance_criteria_no:src_acceptance_criteria_no,
                    des_acceptance_criteria_no:des_acceptance_criteria_no
                },
                success:function (data) {
                    //alert(data.success);
                    //var result = eval('('+data+')');
                    if (data.success){
                        loadDynamicItemInfo(data.des_acceptance_criteria_no);
                    }
                    hlAlertFour(data.message);
                },error:function () {

                }
            });
        }
        //获取原材料的类型
        function getRawMaterialType() {
            $.ajax({
                url:'/CoatingPowderOperation/getAllCoatingPowderType.action',
                dataType:'json',
                success:function (data) {
                    if (data){
                       for(var i=0;i<data.length;i++){
                           $('#rawMaterialType').append('<option value="'+data[i].powder_type+'">'+data[i].powder_type+'</option>');
                       }
                    }
                },error:function () {

                }
            });
        }
        //根据原材料类型获取原材料型号
        function getAllCoatingPowderInfoByPowderType() {
            var powderType=$('#rawMaterialType').val();
            $.ajax({
                url:'/CoatingPowderOperation/getAllCoatingPowderInfoByPowderType.action',
                dataType:'json',
                data:{powderType:powderType},
                success:function (data) {
                    var str="";
                    if (data){
                        for(var i=0;i<data.length;i++){
                              str+=(data[i].coating_powder_name)+";";
                        }
                        $('#tempTextarea').val(str);
                    }
                },error:function () {

                }
            });
        }

    </script>
</head>

<body>
<fieldset class="b3" style="padding:10px;margin:10px;">
    <legend> <h3><b style="color: orange" >|&nbsp;</b><span class="i18n1" name="datadisplay">数据展示</span></h3></legend>
    <div  style="margin-top:5px;">
        <table class="easyui-datagrid" id="contentDatagrids" url="/ACOperation/getACByLike.action" striped="true" loadMsg="正在加载中。。。" textField="text" pageSize="20" fitColumns="true" pagination="true" toolbar="#toolsTab">
            <thead>
            <tr>
                <th data-options="field:'ck',checkbox:true"></th>
                <th field="id" align="center" width="100" class="i18n1" name="id"></th>
                <th field="acceptance_criteria_no" align="center" width="100" class="i18n1" name="acceptancecriteriano"></th>
                <th field="acceptance_criteria_name" align="center" width="100" class="i18n1" name="acceptancecriterianame"></th>
                <th field="external_coating_type" align="center" width="100" class="i18n1" name="externalcoatingtype"></th>
                <th field="internal_coating_type" align="center" width="100" class="i18n1" name="internalcoatingtype"></th>
                <th field="remark" align="center" width="100" class="i18n1" name="remark"></th>
                <th field="last_update_time" align="center" width="100" class="i18n1" name="lastupdatetime" data-options="formatter:formatterdate"></th>
            </tr>
            </thead>
        </table>
    </div>
</fieldset>

<!--工具栏-->
<div id="toolsTab" style="padding:10px;">
    <span class="i18n1" name="acceptancecriteriano">接收标准编号</span>:
    <input id="acceptancecriteriano" style="line-height:22px;border:1px solid #ccc">

    <span class="i18n1" name="acceptancecriterianame">接收标准名称</span>:
    <input id="acceptancecriterianame" style="line-height:22px;border:1px solid #ccc">

    <span class="i18n1" name="externalcoatingtype">外防类型</span>:
    <select id="externalSelect" class="easyui-combobox" data-options="editable:false" name="external_coating_type"   style="width:200px;">
        <option value="" selected="selected">ALL</option>
        <option value="2FBE">2FBE</option>
        <option value="3LPE">3LPE</option>
    </select>
    <span class="i18n1" name="internalcoatingtype">内防类型</span>:
    <select id="internalSelect" class="easyui-combobox" data-options="editable:false" name="internal_coating_type"   style="width:200px;">
        <option value="" selected="selected">ALL</option>
        <option value="EPOXY">EPOXY</option>
    </select>
    <a href="#" class="easyui-linkbutton" plain="true" data-options="iconCls:'icon-search'" onclick="Search()">Search</a>
    <div style="float:right">
        <a href="#" id="previewObpLinkBtn" class="easyui-linkbutton i18n1" name="preview" data-options="iconCls:'icon-preview',plain:true" onclick="previewFunction()">预览</a>
        <a href="#" id="addObpLinkBtn" class="easyui-linkbutton i18n1" name="add" data-options="iconCls:'icon-add',plain:true" onclick="addFunction()">添加</a>
        <a href="#" id="editObpLinkBtn" class="easyui-linkbutton i18n1" name="edit" data-options="iconCls:'icon-edit',plain:true" onclick="editFunction()">修改</a>
        <a href="#" id="deltObpLinkBtn" class="easyui-linkbutton i18n1" name="delete" data-options="iconCls:'icon-remove',plain:true" onclick="delFunction()">删除</a>
    </div>
</div>
<!--添加、修改框  max-height:600px;overflow-y:auto;-->
<div id="addEditDialog" class="easyui-dialog" data-options="closable:false,title:'添加',modal:true" closed="true" buttons="#dlg-buttons" style="display: none;padding:5px 0px;width:1200px; height:660px;">
    <form id="addEditForm" method="post" style="width:95%;margin:0 auto;">
        <fieldset style="width:99%;border:solid 1px #aaa;position:relative;">
            <legend><span class="i18n1" name="ACinfo">标准信息</span></legend>
            <div style="width:100%;padding-bottom:5px;">
                <table class="ht-table"  width="100%" border="0">
                    <tr>
                        <td class="i18n1" name="id">流水号</td>
                        <td colspan="1">
                            <label id="serialNumber" class="hl-label"></label>
                        </td>
                        <td></td>


                    </tr>
                    <tr>
                        <td class="i18n1" name="acceptancecriteriano"></td>
                        <td><input class="easyui-textbox" id="acceptance_criteria_no"   type="text" name="acceptance_criteria_no" readonly="true" value=""/></td>
                        <td></td>
                        <td class="i18n1" name="acceptancecriterianame"></td>
                        <td><input class="easyui-textbox" id="acceptance_criteria_name"   type="text" name="acceptance_criteria_name" value=""/></td>
                        <td></td>
                    </tr>


                    <tr>
                        <td class="i18n1" name="externalcoatingtype"></td>
                        <td>
                            <select id="external_coating_type" class="easyui-combobox" data-options="editable:false" name="external_coating_type"   style="width:200px;">
                                <option value="2FBE">2FBE</option>
                                <option value="3LPE">3LPE</option>
                            </select>
                        </td>
                        <td></td>
                        <td class="i18n1" name="internalcoatingtype"></td>
                        <td>
                            <select id="internal_coating_type" class="easyui-combobox" data-options="editable:false" name="internal_coating_type"   style="width:200px;">
                                <option value="EPOXY">EPOXY</option>
                            </select>
                        </td>
                        <td></td>
                    </tr>
                    <tr>
                        <td class="i18n1" name="lastupdatetime"></td>
                        <td><input class="easyui-textbox"   type="text" readonly="true" name="last_update_time" value=""/></td>
                        <td></td>
                        <td class="i18n1" name="remark"></td>
                        <td><input class="easyui-textbox"   type="text" name="remark" value=""/></td>
                        <td></td>
                    </tr>


                </table>
                <div id="dlg-buttons" align="center" style="width:900px;">
                    <a href="#" class="easyui-linkbutton" iconCls="icon-save" onclick="addEditFormSubmit()">Save</a>
                    <a href="#" class="easyui-linkbutton" id="hlcancelBtn" operationtype="add" iconCls="icon-cancel" onclick="CancelSubmit()">Cancel</a>
                </div>
            </div>
        </fieldset>
        <fieldset id="dynamicItem" style="width:99%;height:380px;border:solid 1px #aaa;position:relative;">
            <legend><span class="i18n1" name="inspectioniteminfo">测量项信息</span></legend>
            <table id="dg" class="easyui-datagrid" title="" style="width:100%;height:360px;" data-options="
				iconCls: '',
				singleSelect: true,
				striped:true,
				toolbar:'#tb',
				pagination:true,
				pageList:[10,20,30,50,100],
				pageSize:10,
				rownumbers:true,
                fitColumns:true,
				onClickRow:onClickRow
			">
                <thead>
                <tr>
                    <%--<th class="i18n1" name="acceptancecriteriano" data-options="field:'acceptance_criteria_no',width:80"></th>--%>

                        <th class="i18n1" name="id" hidden="true" data-options="field:'id'"></th>
                        <th class="i18n1" name="processname" data-options="field:'process_name'"></th>
                        <th class="i18n1" name="processcode" data-options="field:'process_code',width:130,formatter:function(value,row){
							return row.process_code;
						},
						editor:{
							type:'combobox',
							width:150,
							options:{
								valueField:'process_code',
								textField:'process_name',
								method:'get',
								url:'/ProcessOperation/getAllProcess.action',
								required:true
							}
						}"></th>

                    <th class="i18n1" name="itemname" data-options="field:'item_name',editor:{type:'textbox',options:{required:true}}"></th>
                    <th class="i18n1" name="itemnameen" data-options="field:'item_name_en',editor:{type:'textbox',options:{required:true}}"></th>
                    <th class="i18n1" name="unitname" data-options="field:'unit_name',editor:'textbox'"></th>
                    <th class="i18n1" name="unitnameen" data-options="field:'unit_name_en',editor:'textbox'"></th>
                    <th class="i18n1" name="itemfrequency" data-options="field:'item_frequency',width:120,formatter:function(value,row){
							return row.item_frequency;
						},
						editor:{
							type:'combobox',
							options:{
								valueField:'item_frequency',
								textField:'item_frequency_name',
								method:'get',
								url:'../data/freq.json',
								required:true
							}
						}"></th>
                        <th class="i18n1" name="controltype" data-options="field:'control_type',width:100,formatter:function(value,row){
							return row.control_type;
						},
						editor:{
							type:'combobox',
							options:{
								valueField:'control_type',
								textField:'control_type_name',
								method:'get',
								url:'../data/control.json',
								required:true
							}
						}"></th>
                    <th class="i18n1" name="decimalnum" data-options="field:'decimal_num',editor:{type:'numberbox'}"></th>
                    <th class="i18n1" name="needverify" data-options="field:'need_verify',editor:{type:'checkbox',options:{on:'1',off:'0'}}"></th>
                        <th class="i18n1" name="maxvalue" data-options="field:'max_value',editor:{type:'numberbox',options:{precision:2}}"></th>
                        <th class="i18n1" name="minvalue" data-options="field:'min_value',editor:{type:'numberbox',options:{precision:2}}"></th>
                        <th class="i18n1" name="defaultvalue" data-options="field:'default_value',width:50,editor:{type:'textbox'}"></th>


                    <th class="i18n1" name="options" data-options="field:'options',editor:'textbox'"></th>
                        <th class="i18n1" name="isspecialitem" data-options="field:'is_special_item'"></th>
                        <th class="i18n1" name="itemcode" data-options="field:'item_code'"></th>
                    <%--<th class="i18n1" name="status" data-options="field:'status',align:'center',editor:{type:'checkbox',options:{on:'P',off:''}}">Status</th>--%>

                </tr>
                </thead>
            </table>
            <div id="tb" style="height:auto">
                <a href="javascript:void(0)" class="easyui-linkbutton i18n1" name="add" data-options="iconCls:'icon-add',plain:true" onclick="append()">Append</a>
                <a href="javascript:void(0)" class="easyui-linkbutton i18n1" name="delete" data-options="iconCls:'icon-remove',plain:true" onclick="removeit()">Remove</a>
                <a href="javascript:void(0)" class="easyui-linkbutton i18n1" name="save" data-options="iconCls:'icon-save',plain:true" onclick="accept()">Accept</a>
                <a href="javascript:void(0)" class="easyui-linkbutton i18n1" name="undo" data-options="iconCls:'icon-undo',plain:true" onclick="reject()">Reject</a>
                <a href="javascript:void(0)" class="easyui-linkbutton i18n1" name="import" data-options="iconCls:'icon-undo',plain:true" onclick="importItem()">Import</a>
                <input  id="lookup1" name="acceptance_criteria_no_search" class="mini-lookup" style="text-align:center;width:180px;"
                        textField="acceptance_criteria_no" valueField="acceptance_criteria_no" popupWidth="auto"
                        popup="#gridPanel1" grid="#datagrid1" multiSelect="false"/>
            </div>
        </fieldset>
    </form>
</div>

<div id="w" class="easyui-window" title="修改" data-options="modal:true,collapsible:false,minimizable:false,maximizable:false,closed:true" style="width:500px;height:300px;padding:10px;text-align: center;display:none;">
    <div style="width:100%;height:auto;text-align:left">
      <span id="winTitle"  name=""></span>
    </div>
    <div style="width:100%;height:auto;padding:5px 0px;">
        <label class="i18n1" name="exportrawmaterial">导出原材料</label>
        <select style="width:70%;margin-left:4px;" id="rawMaterialType" onchange="getAllCoatingPowderInfoByPowderType()">
            <option value="" selected="selected">请选择</option>
        </select>
    </div>
    <div style="width:100%;height:auto;">
        <textarea id="tempTextarea" rows="" cols="" style="width:95%;height:170px;margin:0 auto;"></textarea>
    </div>
     <div style="width:100%;padding-top:10px;">
         <a  href="#" class="easyui-linkbutton"  iconCls="icon-save" onclick="saveTextArea()">Save</a>
     </div>
</div>
<div id="gridPanel1" class="mini-panel" title="header" iconCls="icon-add" style="width:450px;height:250px;"
     showToolbar="true" showCloseButton="true" showHeader="false" bodyStyle="padding:0" borderStyle="border:0"
>
    <div property="toolbar" id="searchBar1" style="padding:5px;padding-left:8px;text-align:center;display: none">
        <div style="float:left;padding-bottom:2px;">
            <span class="i18n1" name="acceptancecriteriano"></span><span>:</span>
            <input id="keyText1" class="mini-textbox" style="width:110px;" onenter="onSearchClick(1)"/>
            <span class="i18n1" name="acceptancecriterianame"></span><span>:</span>
            <input id="keyText2" class="mini-textbox" style="width:110px;" onenter="onSearchClick(1)"/>
            <a class="mini-button" onclick="onSearchClick(1)">查找</a>
        </div>
        <div style="float:right;padding-bottom:2px;">
            <a class="mini-button" onclick="onClearClick(1)" name="clear">清除</a>
            <a class="mini-button" onclick="onCloseClick(1)" name="close">关闭</a>
        </div>

        <%--<div style="clear:both;"></div>--%>
    </div>
    <div id="datagrid1" class="mini-datagrid" style="width:100%;height:100%;"
         borderStyle="border:0" showPageSize="false" showPageIndex="false"
         url="/ACOperation/getACs.action">
        <div property="columns">
            <div type="checkcolumn" ></div>
            <div field="acceptance_criteria_no" width="80" headerAlign="center" allowSort="true" class="i18n1" name="acceptancecriteriano">接收标准编号</div>
            <div field="acceptance_criteria_name" width="80" headerAlign="center" allowSort="true" class="i18n1" name="acceptancecriterianame">标准名称</div>
            <div field="external_coating_type" width="40" headerAlign="center" allowSort="true" class="i18n1" name="externalcoatingtype">外涂类型</div>
            <div field="internal_coating_type" width="40" headerAlign="center" allowSort="true" class="i18n1" name="internalcoatingtype">内涂类型</div>
            <div field="remark" width="40" headerAlign="center" allowSort="true" class="i18n1" name="remark">备注</div>
        </div>
    </div>
</div>
<div id="previewContainer"  class="easyui-dialog" closed="true" style="width:1200px;height:600px"
     data-options="title:'接收标准',toolbar:'',modal:true">
</div>
<script type="text/javascript" src="../easyui/jquery.easyui.min.js"></script>

</body>
</html>
<script type="text/javascript">
    mini.parse();
    var keyText1=mini.get('keyText1');
    var keyText2=mini.get('keyText2');
    var grid1=mini.get("datagrid1");
    var look1=mini.get('lookup1');
    function onSearchClick(type) {
        if(type==1)
        {
            grid1.load({
                acceptance_criteria_no:keyText1.value,
                acceptance_criteria_name:keyText2.value
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
        $("input[name='acceptance_criteria_no_search']").val(rows.acceptance_criteria_no);
    });
    look1.on("showpopup",function(e){
        $('.mini-shadow').css('z-index','99999');
        $('.mini-popup').css('z-index','100000');
        $('.mini-panel').css('z-index','100000');
        $('#searchBar1').css('display','block');
        grid1.load({
            acceptance_criteria_no:keyText1.value,
            acceptance_criteria_name:keyText2.value
        });
    });
    hlLanguage("../i18n/");
</script>