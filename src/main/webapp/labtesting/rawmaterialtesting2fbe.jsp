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
                $('#hlRawMaterialtest2FbeDialog').dialog({
                    onClose:function () {
                        var type=$('#hlcancelBtn').attr('operationtype');
                        if(type=="add"){
                            var $imglist=$('#fileslist');
                            var $dialog=$('#hlRawMaterialtest2FbeDialog');
                            hlAlertSix("../UploadFile/delUploadPicture.action",$imglist,$dialog,grid);
                        }
                        clearFormLabel();
                    }
                });
               $('.mini-buttonedit .mini-buttonedit-input').css('width','150px');
               // hlLanguage("../i18n/");
        });
        function addRawMaterialtest2FbePro(){
            $('#hlcancelBtn').attr('operationtype','add');
            $('#hlRawMaterialtest2FbeDialog').dialog('open').dialog('setTitle','新增');
            clearFormLabel();
            clearMultiUpload(grid);
            url="/RawMaterialTesting2FbeOperation/saveRawMaterialTest2Fbe.action";
            //$("input[name='alkaline_dwell_time']").siblings().css("background-color","#F9A6A6");
        }
        function delRawMaterialtest2FbePro() {
            var row = $('#RawMaterialtest2FbeDatagrids').datagrid('getSelections');
            if(row.length>0){
                var idArr=[];
                for (var i=0;i<row.length;i++){
                    idArr.push(row[i].id);
                }
                var idArrs=idArr.join(',');
                $.messager.confirm('系统提示',"您确定要删除这<font color=red>"+idArr.length+ "</font>条数据吗？",function (r) {
                    if(r){
                        $.post("/RawMaterialTesting2FbeOperation/delRawMaterialTest2Fbe.action",{"hlparam":idArrs},function (data) {
                            if(data.success){
                                $("#RawMaterialtest2FbeDatagrids").datagrid("reload");
                            }
                            hlAlertFour(data.message);
                        },"json");
                    }
                });
            }else{
                hlAlertOne();
            }
        }
        function editRawMaterialtest2FbePro(){
            $('#hlcancelBtn').attr('operationtype','edit');
            var row = $('#RawMaterialtest2FbeDatagrids').datagrid('getSelected');
            if(row){
                $('#hlRawMaterialtest2FbeDialog').dialog('open').dialog('setTitle','修改');
                $('#project_name').text(row.project_name);
                $('#odbpid').text(row.id);
                //$('#RawMaterialtest2FbeForm').form('load',row);
                $('#RawMaterialtest2FbeForm').form('load', {
                    'project_no':row.project_no,
                    'sample_no':row.sample_no,
                    'raw_material':row.raw_material,
                    'batch_no':row.batch_no,
                    'density':row.density,
                    'particle_size_32um':row.particle_size_32um,
                    'particle_size_150um':row.particle_size_150um,
                    'dsc_tgi':row.dsc_tgi,
                    'dsc_tgf':row.dsc_tgf,
                    'dsc_delta_h':row.dsc_delta_h,
                    'gel_time_lt_20s':row.gel_time_lt_20s,
                    'gel_time_gt_20s':row.gel_time_gt_20s,
                    'powder_volatile':row.powder_volatile,
                    'foaming_cross_sectional':row.foaming_cross_sectional,
                    'foaming_interfacial':row.foaming_interfacial,
                    'impact':row.impact,
                    'flexibility':row.flexibility,
                    'hot_water':row.hot_water,
                    'cd_65_24h':row.cd_65_24h,
                    'operator_no':row.operator_no,
                    'operation_time':getDate1(row.operation_time),
                    'upload_files':row.upload_files,
                    'result':row.result,
                    'remark':row.remark

                });

                // $('#operation-time').datetimebox('setValue',getDate1(row.operation_time));
                look1.setText(row.project_no);
                look1.setValue(row.project_no);
                look2.setText(row.operator_no);
                look2.setValue(row.operator_no);
                var odpictures=row.upload_files;
                if(odpictures!=null&&odpictures!=""){
                     var imgList=odpictures.split(';');
                     createPictureModel(basePath,imgList);
                }
                //异步获取标准并匹配
                $.ajax({
                    url:'/rawMaterialACOperation/getRawMaterialStandard2FbeByProjectNo.action',
                    dataType:'json',
                    data:{'project_no':row.project_no},
                    success:function (data) {
                        var $obj1=$("input[name='density']");
                        var $obj2=$("input[name='particle_size_32um']");
                        var $obj3=$("input[name='particle_size_150um']");
                        var $obj4=$("input[name='dsc_tgi']");
                        var $obj5=$("input[name='dsc_tgf']");
                        var $obj6=$("input[name='dsc_delta_h']");
                        var $obj7=$("input[name='gel_time_lt_20s']");
                        var $obj8=$("input[name='gel_time_gt_20s']");
                        var $obj9=$("input[name='powder_volatile']");
                        var $obj10=$("input[name='foaming_cross_sectional']");
                        var $obj11=$("input[name='foaming_interfacial']");
                        // var $obj12=$("input[name='hot_water']");
                        var $obj13=$("input[name='cd_65_24h']");
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
                        $obj11.siblings().css("background-color","#FFFFFF");
                        // $obj12.siblings().css("background-color","#FFFFFF");
                        $obj13.siblings().css("background-color","#FFFFFF");
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
                            var res11=$obj11.val();
                            // var res12=$obj12.val();
                            var res13=$obj13.val();
                            if(!((res1>=data.density_min)&&(res1<=data.density_max)))
                                $obj1.siblings().css("background-color","#F9A6A6");
                            if(!((res2>=data.particle_size_32um_min)&&(res2<=data.particle_size_32um_max)))
                                $obj2.siblings().css("background-color","#F9A6A6");
                            if(!((res3>=data.particle_size_150um_min)&&(res3<=data.particle_size_150um_max)))
                                $obj3.siblings().css("background-color","#F9A6A6");
                            if(!((res4>=data.dsc_tgi_min)&&(res4<=data.dsc_tgi_max)))
                                $obj4.siblings().css("background-color","#F9A6A6");
                            if(!((res5>=data.dsc_tgf_min)&&(res5<=data.dsc_tgf_max)))
                                $obj5.siblings().css("background-color","#F9A6A6");
                            if(!((res6>=data.dsc_delta_h_min)&&(res6<=data.dsc_delta_h_max)))
                                $obj6.siblings().css("background-color","#F9A6A6");
                            if(!((res7>=data.gel_time_lt_20s_min)&&(res7<=data.gel_time_lt_20s_max)))
                                $obj7.siblings().css("background-color","#F9A6A6");
                            if(!((res8>=data.gel_time_gt_20s_min)&&(res8<=data.gel_time_gt_20s_max)))
                                $obj8.siblings().css("background-color","#F9A6A6");
                            if(!((res9>=data.volatile_min)&&(res9<=data.volatile_max)))
                                $obj9.siblings().css("background-color","#F9A6A6");
                            if(!((res10>=data.foaming_cross_sectional_min)&&(res10<=data.foaming_cross_sectional_max)))
                                $obj10.siblings().css("background-color","#F9A6A6");
                            if(!((res11>=data.foaming_interfacial_min)&&(res11<=data.foaming_interfacial_max)))
                                $obj11.siblings().css("background-color","#F9A6A6");
                            // if(!((res12>=data.hot_water_min)&&(res12<=data.hot_water_max)))
                            //     $obj12.siblings().css("background-color","#F9A6A6");
                            if(!((res13>=data.cd_65_24h_min)&&(res13<=data.cd_65_24h_max)))
                                $obj13.siblings().css("background-color","#F9A6A6");
                        }
                    },error:function () {

                    }
                });
                url="/RawMaterialTesting2FbeOperation/saveRawMaterialTest2Fbe.action?id="+row.id;
            }else{
                hlAlertTwo();
            }
        }
        function searchRawMaterialtest2FbePro() {
            $('#RawMaterialtest2FbeDatagrids').datagrid('load',{
                'project_no': $('#projectno').val(),
                'operator_no': $('#operatorno').val(),
                'begin_time': $('#begintime').val(),
                'end_time': $('#endtime').val()
            });
        }
        function RawMaterialtest2FbeFormSubmit() {
            $('#RawMaterialtest2FbeForm').form('submit',{
                url:url,
                onSubmit:function () {
                    //表单验证
                    if($("input[name='project_no']").val()==""){

                        hlAlertFour("请选择项目信息");
                        return false;
                    }
                    if($("input[name='operator_no']").val()==""){

                        hlAlertFour("请选择操作工工号");
                        return false;
                    }
                    if($("input[name='operation_time']").val()==""){
                        hlAlertFour("请输入操作时间");
                        return false;
                    }
                    setParams($("input[name='density']"));
                    setParams($("input[name='particle_size_32um']"));
                    setParams($("input[name='particle_size_150um']"));
                    setParams($("input[name='dsc_tgi']"));
                    setParams($("input[name='dsc_tgf']"));
                    setParams($("input[name='dsc_delta_h']"));
                    setParams($("input[name='gel_time_lt_20s']"));
                    setParams($("input[name='gel_time_gt_20s']"));
                    setParams($("input[name='powder_volatile']"));
                    setParams($("input[name='foaming_cross_sectional']"));
                    setParams($("input[name='foaming_interfacial']"));
                    setParams($("input[name='impact']"));
                    setParams($("input[name='flexibility']"));
                    // setParams($("input[name='hot_water']"));
                    setParams($("input[name='cd_65_24h']"));

                },
                success: function(result){
                    clearFormLabel();
                    var result = eval('('+result+')');
                    $('#hlRawMaterialtest2FbeDialog').dialog('close');
                    if (result.success){
                        $('#RawMaterialtest2FbeDatagrids').datagrid('reload');
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
        function RawMaterialtest2FbeCancelSubmit() {
            $('#hlRawMaterialtest2FbeDialog').dialog('close');
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
            $('#RawMaterialtest2FbeForm').form('clear');
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
         <table class="easyui-datagrid" id="RawMaterialtest2FbeDatagrids" url="/RawMaterialTesting2FbeOperation/getRawMaterialTest2FbeByLike.action" striped="true" loadMsg="正在加载中。。。" textField="text" pageSize="20" fitColumns="true" pagination="true" toolbar="#hlRawMaterialtest2FbeProTb">
             <thead>
               <tr>
                       <th data-options="field:'ck',checkbox:true"></th>
                       <th field="id" align="center" width="100" class="i18n1" name="id">流水号</th>
                       <th field="project_name" align="center" width="120" class="i18n1" name="projectname">项目名称</th>
                      <th field="project_no" align="center" width="120" hidden="true" class="i18n1" name="projectno">项目编号</th>
                       <th field="operator_no" align="center" width="100"  class="i18n1" name="operatorno">操作工编号</th>

                       <th field="sample_no" align="center" width="120" class="i18n1" name="sampleno">试样号</th>
                       <th field="raw_material" align="center" width="100" hidden="true" class="i18n1" name="rawmaterial">原材料</th>
                       <th field="batch_no" align="center" width="100" hidden="true" class="i18n1" name="batchno">批号</th>
                       <th field="density" width="100" align="center" hidden="true" class="i18n1" name="density">密度</th>
                       <th field="particle_size_32um" width="100" align="center" hidden="true" class="i18n1" name="particlesize32um">颗粒度 32um 所占比例</th>
                       <th field="particle_size_150um" width="100" align="center" hidden="true" class="i18n1" name="particlesize150um">颗粒度 150um 所占比例</th>
                       <th field="dsc_tgi" align="center" width="120" class="i18n1" name="dsctgi">dsc tgi 比例</th>
                       <th field="dsc_tgf" align="center" width="120" class="i18n1" name="dsctgf">dsc tgf 比例</th>
                       <th field="dsc_delta_h" align="center" width="120" hidden="true" class="i18n1" name="dscdeltah">dsc delta H 比例</th>
                       <th field="gel_time_lt_20s" align="center" width="120" hidden="true" class="i18n1" name="geltimelt20s">固化度  小于20秒</th>
                       <th field="gel_time_gt_20s" align="center" width="120" hidden="true" class="i18n1" name="geltimegt20s">固化度  大于20秒</th>
                       <th field="powder_volatile" align="center" width="120" hidden="true" class="i18n1" name="powdervolatile">挥发性</th>
                       <th field="foaming_cross_sectional" align="center" hidden="true" width="120" class="i18n1" name="foamingcrosssectional">孔隙率  断面</th>
                       <th field="foaming_interfacial" align="center" hidden="true" width="120" class="i18n1" name="foaminginterfacial">孔隙率  表面</th>
                   <th field="impact" align="center" width="120" hidden="true" class="i18n1" name="impact">冲击</th>
                       <th field="flexibility" align="center" hidden="true" width="120" class="i18n1" name="flexibility">弯曲</th>
                      <th field="hot_water" align="center" hidden="true" width="120" class="i18n1" name="hotwater">水煮</th>
                      <th field="cd_65_24h" align="center" hidden="true" width="120" class="i18n1" name="cd6524h">阴极剥离 65度 24小时</th>

                       <th field="remark" align="center" width="150" class="i18n1" name="remark">备注</th>
                       <th field="result" align="center" width="150" class="i18n1" name="result">结论</th>
                       <th field="operation_time" align="center" width="150" class="i18n1" name="operationtime" data-options="formatter:formatterdate">操作时间</th>
               </tr>
             </thead>
         </table>

    </div>
</fieldset>

<!--工具栏-->
<div id="hlRawMaterialtest2FbeProTb" style="padding:10px;">
    <span class="i18n1" name="projectno">项目编号</span>:
    <input id="projectno" name="projectno" style="line-height:22px;border:1px solid #ccc">
    <span class="i18n1" name="operatorno">操作工编号</span>:
    <input id="operatorno" name="operatorno" style="line-height:22px;border:1px solid #ccc">
    <span class="i18n1" name="begintime">开始时间</span>:
    <input id="begintime" name="begintime" type="text" class="easyui-datebox" data-options="formatter:myformatter,parser:myparser">
    <span class="i18n1" name="endtime">结束时间</span>:
    <input id="endtime" name="endtime" type="text" class="easyui-datebox" data-options="formatter:myformatter,parser:myparser">
    <a href="#" class="easyui-linkbutton" plain="true" data-options="iconCls:'icon-search'" onclick="searchRawMaterialtest2FbePro()">Search</a>
    <div style="float:right">
     <a href="#" id="addObpLinkBtn" class="easyui-linkbutton i18n1" name="add" data-options="iconCls:'icon-add',plain:true" onclick="addRawMaterialtest2FbePro()">添加</a>
     <a href="#" id="editObpLinkBtn" class="easyui-linkbutton i18n1" name="edit" data-options="iconCls:'icon-edit',plain:true" onclick="editRawMaterialtest2FbePro()">修改</a>
     <a href="#" id="deltObpLinkBtn" class="easyui-linkbutton i18n1" name="delete" data-options="iconCls:'icon-remove',plain:true" onclick="delRawMaterialtest2FbePro()">删除</a>
    </div>
</div>

<!--添加、修改框-->
<div id="hlRawMaterialtest2FbeDialog" class="easyui-dialog" data-options="title:'添加',modal:true"  closed="true" buttons="#dlg-buttons" style="display: none;padding:5px;width:950px;height:auto;">
   <form id="RawMaterialtest2FbeForm" method="post">
       <fieldset style="width:900px;border:solid 1px #aaa;margin-top:8px;position:relative;">
           <legend>项目信息</legend>
           <table class="ht-table" width="100%" border="0">
               <tr>
                   <td class="i18n1" name="projectname" width="16%">项目名称</td>
                   <td colspan="2"><label class="hl-label" id="project_name"></label></td>
                   <td class="i18n1" name="projectno" width="16%">项目编号</td>
                   <td colspan="2" width="33%">
                       <input  id="lookup1" name="project_no" class="mini-lookup" style="text-align:center;width:180px;"
                               textField="project_no" valueField="id" popupWidth="auto"
                               popup="#gridPanel1" grid="#datagrid1" multiSelect="false"/>
                   </td>
               </tr>

           </table>
       </fieldset>
       <fieldset style="width:900px;border:solid 1px #aaa;margin-top:8px;position:relative;">
           <legend>原材料实验(2FBE)信息</legend>

       <table class="ht-table">
           <tr>
               <td class="i18n1" name="id" width="20%">流水号</td>
               <td colspan="5"><label class="hl-label" id="odbpid"></label></td>
           </tr>
           <tr>
               <td class="i18n1" name="operatorno" width="20%">操作工编号</td>
               <td colspan="1" width="30%">
                   <input id="lookup2" name="operator_no" class="mini-lookup" style="text-align:center;width:180px;"
                          textField="employee_no" valueField="id" popupWidth="auto"
                          popup="#gridPanel2" grid="#datagrid2" multiSelect="false"
                   />
               </td>
               <td></td>
               <td class="i18n1" name="operationtime">操作时间</td>
               <td>
                   <input class="easyui-datetimebox" type="text" name="operation_time" value="" data-options="formatter:myformatter2,parser:myparser2"/>

               </td>
               <td></td>
           </tr>
       </table>

       <table class="ht-table">
           <tr>
               <td class="i18n1" name="sampleno">试样号</td>
               <td>
                   <input class="easyui-textbox"   type="text" name="sample_no" value=""/>
               </td>
               <td></td>
               <td class="i18n1" name="rawmaterial">原材料</td>
               <td><input class="easyui-textbox hl-errorcolor"  type="text" name="raw_material" value=""/></td>
               <td></td>
           </tr>
           <tr>
               <td class="i18n1" name="batchno">批号</td>
               <td><input class="easyui-textbox"   type="text" name="batch_no" value=""/></td>
               <td></td>
               <td class="i18n1" name="density">密度</td>
               <td><input class="easyui-numberbox" data-options="min:0,precision:2"  type="text" name="density" value=""/></td>
               <td></td>
           </tr>
           <tr>
               <td class="i18n1" name="particlesize32um">颗粒度 32um 所占比例</td>
               <td><input class="easyui-numberbox" data-options="min:0,precision:2" type="text" name="particle_size_32um" value=""/></td>
               <td></td>
               <td width="16%" class="i18n1" name="particlesize150um">颗粒度 150um 所占比例</td>
               <td><input class="easyui-numberbox" data-options="min:0,precision:2" type="text" name="particle_size_150um" value=""/></td>
               <td></td>
           </tr>

           <tr>
               <td width="16%" class="i18n1" name="dsctgi">dsc tgi 比例</td>
               <td><input class="easyui-numberbox" data-options="min:0,precision:2"  type="text" name="dsc_tgi" value=""/></td>
               <td></td>
               <td class="i18n1" name="dsctgf">dsc tgf 比例</td>
               <td><input class="easyui-numberbox" data-options="min:0,precision:2" type="text" name="dsc_tgf" value=""/></td>
               <td></td>
           </tr>
           <tr>
               <td class="i18n1" name="dscdeltah">dsc delta H 比例</td>
               <td><input class="easyui-numberbox" data-options="min:0,precision:2" type="text" name="dsc_delta_h" value=""/></td>
               <td></td>
               <td class="i18n1" name="geltimelt20s">固化度  小于20秒</td>
               <td><input class="easyui-numberbox" data-options="min:0,precision:2" type="text" name="gel_time_lt_20s" value=""/></td>
               <td></td>
           </tr>
           <tr>
               <td class="i18n1" name="geltimegt20s">固化度  大于20秒</td>
               <td><input class="easyui-numberbox" data-options="min:0,precision:2" type="text" name="gel_time_gt_20s" value=""/></td>
               <td></td>
               <td class="i18n1" name="powdervolatile">挥发性</td>
               <td><input class="easyui-numberbox" data-options="min:0,precision:2" type="text" name="powder_volatile" value=""/></td>
               <td></td>
           </tr>
           <tr>
               <td class="i18n1" name="foamingcrosssectional">孔隙率  断面</td>
               <td>
                   <%--<input class="easyui-numberbox" data-options="min:0,precision:0" type="text" name="foaming_cross_sectional" value=""/>--%>
                       <select id="foaming_cross_sectional" class="easyui-combobox" data-options="editable:false" name="foaming_cross_sectional" style="width:200px;">
                           <option value="1">1级</option>
                           <option value="2">2级</option>
                           <option value="3">3级</option>
                           <option value="4">4级</option>
                       </select>
               </td>
               <td></td>
               <td class="i18n1" name="foaminginterfacial">孔隙率  表面</td>
               <td>
                   <%--<input class="easyui-numberbox" data-options="min:0,precision:0" type="text" name="foaming_interfacial" value=""/>--%>
                       <select id="foaming_interfacial" class="easyui-combobox" data-options="editable:false" name="foaming_interfacial" style="width:200px;">
                           <option value="1">1级</option>
                           <option value="2">2级</option>
                           <option value="3">3级</option>
                           <option value="4">4级</option>
                       </select>
               </td>
               <td></td>
           </tr>
           <tr>
               <td class="i18n1" name="impact">冲击</td>
               <td>
                   <select id="impact" class="easyui-combobox" data-options="editable:false" name="impact" style="width:200px;">
                       <option value="OK">合格（无漏点）</option>
                       <option value="Not OK">不合格（有漏点）</option>
                       <option value="Pending">待定</option>
                   </select>
               </td>
               <td></td>
               <td class="i18n1" name="flexibility">弯曲</td>
               <td>
                   <select  class="easyui-combobox" data-options="editable:false" name="flexibility" style="width:200px;">
                       <option value="OK">合格（无裂纹）</option>
                       <option value="Not OK">不合格（有裂纹）</option>
                       <option value="Pending">待定</option>
                   </select>
               </td>
               <td></td>
           </tr>
           <tr>
               <td class="i18n1" name="hotwater">水煮</td>
               <td>
                   <%--<input class="easyui-numberbox" data-options="min:0,precision:0" type="text" name="hot_water" value=""/>--%>
                   <select id="hot_water" class="easyui-combobox" data-options="editable:false" name="hot_water" style="width:200px;">
                       <option value="1">1级</option>
                       <option value="2">2级</option>
                       <option value="3">3级</option>
                       <option value="4">4级</option>
                   </select>
               </td>
               <td></td>
               <td class="i18n1" name="cd6524h">阴极剥离 65度 24小时</td>
               <td><input class="easyui-numberbox" data-options="min:0,precision:2" type="text" name="cd_65_24h" value=""/></td>
               <td></td>
           </tr>
           <tr>
               <td class="i18n1" name="remark">备注</td>
               <td><input class="easyui-textbox" type="text" value="" name="remark" data-options="multiline:true" style="height:60px"/></td>
               <td></td>
               <td  class="i18n1" name="result">结论</td>
               <td><select id="cc" class="easyui-combobox" data-options="editable:false" name="result" style="width:200px;">
                   <option value="0">不合格,复验</option>
                   <option value="1">合格</option>
                   <option value="10">待定</option>
               </select></td>
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
    <a href="#" class="easyui-linkbutton" iconCls="icon-save" onclick="RawMaterialtest2FbeFormSubmit()">Save</a>
    <a href="#" class="easyui-linkbutton" id="hlcancelBtn" operationtype="add" iconCls="icon-cancel" onclick="RawMaterialtest2FbeCancelSubmit()">Cancel</a>
</div>
<div id="gridPanel1" class="mini-panel" title="header" iconCls="icon-add" style="width:450px;height:250px;"
     showToolbar="true" showCloseButton="true" showHeader="false" bodyStyle="padding:0" borderStyle="border:0"
>
    <div property="toolbar" id="searchBar1" style="padding:5px;padding-left:8px;text-align:center;display: none">
        <div style="float:left;padding-bottom:2px;">
            <span class="i18n1" name="projectno">项目编号</span><span>:</span>
            <input id="keyText1" class="mini-textbox" style="width:110px;" onenter="onSearchClick(1)"/>
            <span class="i18n1" name="projectname">项目名称</span><span>:</span>
            <input id="keyText2" class="mini-textbox" style="width:110px;" onenter="onSearchClick(1)"/>
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
         url="/ProjectOperation/getProjectInfoByNoOrName.action">
        <div property="columns">
            <div type="checkcolumn" ></div>
            <div field="project_no" width="80" headerAlign="center" allowSort="true" class="i18n1" name="pipeno">项目编号</div>
            <div field="project_name" width="80" headerAlign="center" allowSort="true" class="i18n1" name="contractno">项目名称</div>
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
    var keyText2=mini.get("keyText2");
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
                project_no:keyText1.value,
                project_name:keyText2.value,
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
        $("input[name='project_no']").val(rows.project_no);
        $("#project_name").text(rows.project_name);
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
            project_no:keyText1.value,
            project_name:keyText2.value,
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