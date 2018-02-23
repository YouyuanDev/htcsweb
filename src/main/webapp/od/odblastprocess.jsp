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

        function myformatter(date){
            var y = date.getFullYear();
            var m = date.getMonth()+1;
            var d = date.getDate();
            return y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d);
        }


        function formatterdate(value,row,index){
           return getDate1(value);
        }

        function myparsedate(s){
            if (!s) return new Date();
            return new Date(Date.parse(s));
        }




        function myparser(s){
            if (!s) return new Date();
            var ss = (s.split('-'));
            var y = parseInt(ss[0],10);
            var m = parseInt(ss[1],10);
            var d = parseInt(ss[2],10);
            if (!isNaN(y) && !isNaN(m) && !isNaN(d)){
                return new Date(y,m-1,d);
            } else {
                return new Date();
            }
        }

        // 日期格式为 2/20/2017 12:00:00 PM
        function myformatter2(date){
            return getDate1(date);
        }
        // 日期格式为 2/20/2017 12:00:00 PM
        function myparser2(s) {
            if (!s) return new Date();
            return new Date(Date.parse(s));
        }


        $(function () {
            // $('#odBlastProDatagrids').datagrid({
            //     striped:true,
            //     loadMsg:'正在加载中。。。',
            //     pagination:true,
            //     textField:'text',
            //     rownumbers:true,
            //     pageList:[10,20,30,50,100],
            //     pageSize:20,
            //     fitColumns:true,
            //     url:'/OdOperation/getOdBlastByLike.action',
            //     toolbar:'#hlOdBlastProTb',
            //     columns:[[
            //         { field: '',checkbox:true},
            //         { title: arg1,field: 'id',width:100},
            //         { title: arg2,field: 'pipe_no',width:100},
            //         { title: arg3,field : 'operation_time', formatter:function(value,row,index){
            //                 var operation_time = new Date(value);
            //                  return operation_time.toLocaleString();
            //              } ,width:200},
            //         { title: arg4,field: 'operator_no',width:100},
            //         { title: '外观缺陷',field: 'surface_condition',width:100},
            //         { title: '打砂前盐度',field: 'salt_contamination_before_blasting',width:100},
            //         { title: '碱洗时间',field: 'alkaline_dwell_time',width:100},
            //         { title: '碱浓度',field: 'alkaline_concentration',width: 100},
            //         { title: '传导性',field: 'conductivity',width:120},
            //         { title: '酸洗时间',field: 'acid_wash_time',width:120},
            //         { title: '酸浓度',field: 'acid_concentration',width:120},
            //         { title: '打砂传送速度',field: 'blast_line_speed',width:120},
            //         { title: '预热温度',field: 'preheat_temp',width:120},
            //         { title: '备注',field: 'remark',width:120}
            //     ]]
            // });

                    //删除上传的图片
                $(document).on('click','.content-del',function () {
                     delUploadPicture($(this));
                });
                $('#hlOdBlastProDialog').dialog({
                    onClose:function () {
                        var type=$('#hlcancelBtn').attr('operationtype');
                        if(type=="add"){
                            var $imglist=$('#fileslist');
                            var $dialog=$('#hlOdBlastProDialog');
                            hlAlertSix("../UploadFile/delUploadPicture.action",$imglist,$dialog,grid);
                        }else{
                            //$('#hlOdBlastProDialog').dialog('close');
                            $('#hl-gallery-con').empty();
                            //$('#fileslist').val('');
                            clearFormLabel();
                        }
                    }
                });
               $('.mini-buttonedit .mini-buttonedit-input').css('width','150px');
               // hlLanguage("../i18n/");
        });
        function addOdBlastPro(){
            $('#hlcancelBtn').attr('operationtype','add');
            $('#hlOdBlastProDialog').dialog('open').dialog('setTitle','新增');
            $('#fileslist').val('');
            $('#odBlastProForm').form('clear');
            $('#odbpid').text('');$('#odbptime').text('');
            combox1.setValue("");
            clearMultiUpload(grid);
            url="/OdOperation/saveOdBlastProcess.action";
        }
        function delOdBlastPro() {
            var row = $('#odBlastProDatagrids').datagrid('getSelections');
            if(row.length>0){
                var idArr=[];
                for (var i=0;i<row.length;i++){
                    idArr.push(row[i].id);
                }
                var idArrs=idArr.join(',');
                hlAlertFive("/OdOperation/delOdBlastProcess.action",idArrs,idArr.length);
                // $.messager.confirm('提示','您确定要删除<font>')
            }else{
                hlAlertOne();
            }
        }
        function editOdBlastPro() {
            $('#hlcancelBtn').attr('operationtype','edit');
            var row = $('#odBlastProDatagrids').datagrid('getSelected');
            if(row){
                $('#hlOdBlastProDialog').dialog('open').dialog('setTitle','修改');
                $('#project_name').text(row.project_name);$('#contract_no').text(row.contract_no);
                $('#pipe_no').text(row.pipe_no);$('#status_name').text(row.status_name);
                $('#od').text(row.od);$('#wt').text(row.wt);
                $('#p_length').text(row.p_length);$('#weight').text(row.weight);
                $('#odbpid').text(row.id);
                $('#grade').text(row.grade);$('#heat_no').text(row.heat_no);
                $('#odBlastProForm').form('load',row);

                $('#operation-time').datetimebox('setValue',getDate1(row.operation_time));

                look1.setText(row.pipe_no);
                look1.setValue(row.pipe_no);
                look2.setText(row.operator_no);
                look2.setValue(row.operator_no);
                combox1.setValue(row.surface_condition);
                var odpictures=row.upload_files;
                if(odpictures!=null&&odpictures!=""){
                     var imgList=odpictures.split(';');
                    createPictureModel(imgList);
                }
                url="/OdOperation/saveOdBlastProcess.action?id="+row.id;

            }else{
                hlAlertTwo();
            }
        }
        function searchOdBlastPro() {
            $('#odBlastProDatagrids').datagrid('load',{
                'pipe_no': $('#pipeno').val(),
                'operator_no': $('#operatorno').val(),
                'begin_time': $('#begintime').val(),
                'end_time': $('#endtime').val()
            });
        }
        function odBlastProFormSubmit() {
            $('#odBlastProForm').form('submit',{
                url:url,
                onSubmit:function () {
                    //表单验证
                    //碱洗时间
                    setParams($("input[name='alkaline_dwell_time']"));
                    setParams($("input[name='alkaline_concentration']"));
                    setParams($("input[name='acid_wash_time']"));
                    setParams($("input[name='acid_concentration']"));
                    setParams($("input[name='salt_contamination_before_blasting']"));
                    setParams($("input[name='blast_line_speed']"));
                    setParams($("input[name='conductivity']"));
                    setParams($("input[name='preheat_temp']"));


                    if($("input[name='odbptime']").val()==""){

                        hlAlertFour("请输入操作时间");
                        return false;
                    }



                },
                success: function(result){
                    var result = eval('('+result+')');
                    if (result.success){
                        $('#hlOdBlastProDialog').dialog('close');
                        $('#odBlastProDatagrids').datagrid('reload');
                        clearFormLabel();
                        $('#hl-gallery-con').empty();
                    } else {
                         hlAlertFour("操作失败!");
                    }
                },
                error:function () {
                    hlAlertThree();
                }
            });
            clearMultiUpload(grid);
        }
        function odBlastProCancelSubmit() {
            $('#hlOdBlastProDialog').dialog('close');
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
            createPictureModel(imgList);
        }
        function editFilesList(type,imgUrl) {
            var $obj=$('#fileslist');
            if(type==0){
                var filesList=$('#fileslist').val();
                $obj.val(filesList+imgUrl+";");
            }else{
                $obj.val($obj.val().replace(imgUrl+";",''));
            }
            return $obj.val();
        }
        //删除选择的图片
        function delUploadPicture($obj) {
            var imgUrl=$obj.siblings('dt').find('img').attr('src');
            var imgName=imgUrl.substr(imgUrl.lastIndexOf('/')+1);
            $.ajax({
                url:'../UploadFile/delUploadPicture.action',
                dataType:'json',
                data:{"imgList":imgName+";"},
                success:function (data) {
                    if(data.success){
                        var imgList=editFilesList(2,imgName);
                        $(this).parent('.content-dl').remove();
                    }else{
                        hlAlertFour("移除失败!");
                    }
                },
                error:function () {
                    hlAlertThree();
                }
            });
        }
        //创建图片展示模型(参数是图片集合)
        function  createPictureModel(imgList) {
            var basePath ="<%=basePath%>"+"/upload/pictures/";
            if($('#hl-gallery').length>0){
                $('#content_list').empty();
                for(var i=0;i<imgList.length-1;i++){
                    $('#content_list').append(getCalleryChildren(basePath+imgList[i]));
                }
            }else{
                $('#hl-gallery-con').append(getGalleryCon());
                for(var i=0;i<imgList.length-1;i++){
                    $('#content_list').append(getCalleryChildren(basePath+imgList[i]));
                }
            }
        }
        function  setParams($obj) {
            if($obj.val()==null||$obj.val()=="")
                $obj.val(0);
        }
        function  clearFormLabel() {
            $('#odBlastProForm').form('clear');$('#odbpid').text('');$('#odbptime').text('');
            $('#project_name').text('');$('#contract_no').text('');
            $('#pipe_no').text('');$('#status_name').text('');
            $('#od').text('');$('#wt').text('');
            $('#p_length').text('');$('#weight').text('');
            $('#odbpid').text('');$('#odbptime').text('');
            $('#grade').text('');$('#heat_no').text('');combox1.setValue("");
        }
    </script>





</head>

<body>
<fieldset class="b3" style="padding:10px;margin:10px;">
    <legend> <h3><b style="color: orange" >|&nbsp;</b><span class="i18n1" name="datadisplay">数据展示</span></h3></legend>
    <div  style="margin-top:5px;">
         <table class="easyui-datagrid" id="odBlastProDatagrids" url="/OdOperation/getNewOdBlastByLike.action" striped="true" loadMsg="正在加载中。。。" textField="text" pageSize="20" fitColumns="true" pagination="true" toolbar="#hlOdBlastProTb">
             <thead>
               <tr>
                       <th data-options="field:'ck',checkbox:true"></th>
                       <th field="id" align="center" width="100" class="i18n1" name="id">流水号</th>
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
                       <th field="surface_condition" align="center" width="120" class="i18n1" name="surfacecondition">外观缺陷</th>
                       <th field="salt_contamination_before_blasting" align="center" width="120" class="i18n1" name="saltcontaminationbeforeblasting">打砂前盐度</th>
                       <th field="alkaline_dwell_time" align="center" width="100" hidden="true" class="i18n1" name="alkalinedwelltime">碱洗时间</th>
                       <th field="alkaline_concentration" align="center" width="100" hidden="true" class="i18n1" name="alkalineconcentration">碱浓度</th>
                       <th field="conductivity" width="100" align="center" hidden="true" class="i18n1" name="conductivity">传导性</th>
                       <th field="acid_wash_time" width="100" align="center" hidden="true" class="i18n1" name="acidwashtime">酸洗时间</th>
                       <th field="acid_concentration" width="100" align="center" hidden="true" class="i18n1" name="acidconcentration">酸浓度</th>
                       <th field="blast_line_speed" align="center" width="120" class="i18n1" name="blastlinespeed">打砂传送速度</th>
                       <th field="preheat_temp" align="center" width="120" class="i18n1" name="preheattemp">预热温度</th>
                       <th field="remark" align="center" width="150" class="i18n1" name="remark">备注</th>
                       <th field="result" align="center" width="150" class="i18n1" name="result">结论</th>
                       <th field="operation_time" align="center" width="150" class="i18n1" name="operationtime" data-options="formatter:formatterdate">操作时间</th>
               </tr>
             </thead>
         </table>

    </div>
</fieldset>

<!--工具栏-->
<div id="hlOdBlastProTb" style="padding:10px;">
    <span class="i18n1" name="pipeno">钢管编号</span>:
    <input id="pipeno" name="pipeno" style="line-height:22px;border:1px solid #ccc">
    <span class="i18n1" name="operatorno">操作工编号</span>:
    <input id="operatorno" name="operatorno" style="line-height:22px;border:1px solid #ccc">
    <span class="i18n1" name="begintime">开始时间</span>:
    <input id="begintime" name="begintime" type="text" class="easyui-datebox" data-options="formatter:myformatter,parser:myparser">
    <span class="i18n1" name="endtime">结束时间</span>:
    <input id="endtime" name="endtime" type="text" class="easyui-datebox" data-options="formatter:myformatter,parser:myparser">
    <a href="#" class="easyui-linkbutton" plain="true" data-options="iconCls:'icon-search'" onclick="searchOdBlastPro()">Search</a>
    <div style="float:right">
     <a href="#" id="addObpLinkBtn" class="easyui-linkbutton i18n1" name="add" data-options="iconCls:'icon-add',plain:true" onclick="addOdBlastPro()">添加</a>
     <a href="#" id="editObpLinkBtn" class="easyui-linkbutton i18n1" name="edit" data-options="iconCls:'icon-edit',plain:true" onclick="editOdBlastPro()">修改</a>
     <a href="#" id="deltObpLinkBtn" class="easyui-linkbutton i18n1" name="delete" data-options="iconCls:'icon-remove',plain:true" onclick="delOdBlastPro()">删除</a>
    </div>
</div>

<!--添加、修改框-->
<div id="hlOdBlastProDialog" class="easyui-dialog" data-options="title:'添加',modal:true"  closed="true" buttons="#dlg-buttons" style="display: none;padding:5px;width:950px;height:auto;">
   <form id="odBlastProForm" method="post">
       <fieldset style="width:900px;border:solid 1px #aaa;margin-top:8px;position:relative;">
           <legend>钢管信息</legend>
           <table class="ht-table" width="100%" border="0">
               <tr>
                   <td class="i18n1" name="projectname" width="16%">项目名称</td>
                   <td colspan="2" width="33%"><label id="project_name"></label></td>

                   <td class="i18n1" name="contractno" width="16%">合同编号</td>
                   <td colspan="7" width="33%"><label id="contract_no"></label></td>

               </tr>

               <tr>
                   <td class="i18n1" name="pipeno" width="16%">钢管编号</td>
                   <td colspan="2" width="33%">
                       <input id="lookup1" name="pipe_no" class="mini-lookup" style="text-align:center;width:180px;"
                              textField="pipe_no" valueField="id" popupWidth="auto"
                              popup="#gridPanel1" grid="#datagrid1" multiSelect="false"/>
                   </td>
                   <td class="i18n1" name="statusname" width="16%">状态</td>
                   <td colspan="7" width="33%"><label id="status_name"></label></td>
               </tr>
           </table>

           <table width="100%" border="0" align="center">
               <tr>
                   <td align="center" class="i18n1" name="grade">钢种</td>
                   <td align="center"><label id="grade"></label></td>
                   <td align="center" class="i18n1" name="od">外径</td>
                   <td align="center"><label id="od"></label></td>
                   <td align="center" class="i18n1" name="wt">壁厚</td>
                   <td align="center"><label id="wt"></label></td>
                   <td align="center" class="i18n1" name="p_length">长度</td>
                   <td align="center"><label id="p_length"></label></td>
                   <td align="center" class="i18n1" name="weight">重量</td>
                   <td align="center"><label id="weight"></label></td>
                   <td align="center" class="i18n1" name="heatno">炉号</td>
                   <td align="center"><label id="heat_no"></label></td>
               </tr>
           </table>
       </fieldset>


       <fieldset style="width:900px;border:solid 1px #aaa;margin-top:8px;position:relative;">
           <legend>外喷砂生产信息</legend>

       <table class="ht-table">
           <tr>
               <td class="i18n1" name="id">流水号</td>
               <td colspan="5"><label id="odbpid"></label></td>

           </tr>
           <tr>
               <td class="i18n1" name="operatorno">操作工编号</td>
               <td colspan="2" >
                   <input id="lookup2" name="operator_no" class="mini-lookup" style="text-align:center;width:180px;"
                          textField="employee_no" valueField="id" popupWidth="auto"
                          popup="#gridPanel2" grid="#datagrid2" multiSelect="false"
                   />
               </td>
               <td class="i18n1" name="operationtime">操作时间</td>
               <td colspan="2">
                   <input class="easyui-datetimebox" id="operation-time" type="text" name="odbptime" value="" data-options="formatter:myformatter2,parser:myparser2"/>

               </td>

           </tr>
       </table>

       <table class="ht-table">
           <tr>
               <td class="i18n1" name="alkalinedwelltime">碱洗时间</td>
               <td><input class="easyui-numberbox" data-options="min:0,precision:0" type="text" name="alkaline_dwell_time" value=""/></td>
               <td>10~20</td>
               <td class="i18n1" name="alkalineconcentration">碱浓度</td>
               <td><input class="easyui-numberbox"  data-options="min:0,precision:2" type="text" name="alkaline_concentration" value=""/></td>
               <td></td>
           </tr>

           <tr>
               <td class="i18n1" name="acidwashtime">酸洗时间</td>
               <td><input class="easyui-numberbox" data-options="min:0,precision:0" type="text" name="acid_wash_time" value=""/></td>
               <td></td>
               <td class="i18n1" name="acidconcentration">酸浓度</td>
               <td><input class="easyui-numberbox" data-options="min:0,precision:2" type="text" name="acid_concentration" value=""/></td>
               <td></td>
           </tr>
           <tr>
               <td width="16%"  class="i18n1" name="surfacecondition">外观缺陷</td>
               <td>

                   <%--<input class="easyui-validatebox" type="text" name="surface_condition" value=""/>--%>
                   <div id="combobox1" class="mini-combobox" style="width:185px;"  popupWidth="185" textField="text" valueField="text"
                            url="../data/defect.txt" name="surface_condition" multiSelect="true"  showClose="true" oncloseclick="onComboxCloseClick" >
                           <div property="columns">
                               <div header="缺陷类型" field="text"></div>
                           </div>
                   </div>

               </td>
               <td></td>
               <td width="16%" class="i18n1" name="saltcontaminationbeforeblasting">打砂前盐度</td>
               <td><input class="easyui-numberbox" data-options="min:0,precision:2" type="text" name="salt_contamination_before_blasting" value=""/></td>
               <td><=25</td>
           </tr>

           <tr>
               <td width="16%" class="i18n1" name="blastlinespeed">打砂传送速度</td>
               <td><input class="easyui-numberbox" data-options="min:0,precision:2" type="text" name="blast_line_speed" value=""/></td>
               <td></td>
               <td width="16%" class="i18n1" name="conductivity">传导性</td>
               <td><input class="easyui-numberbox" data-options="min:0,precision:2" type="text" name="conductivity" value=""/></td>
               <td></td>
           </tr>
           <tr>
               <td width="16%" class="i18n1" name="preheattemp">预热温度</td>
               <td><input class="easyui-numberbox" data-options="min:0,precision:1" type="text" name="preheat_temp" value=""/></td>
               <td></td>
               <td width="16%" class="i18n1" name="remark">备注</td>
               <td><input class="easyui-textbox" type="text" value="" name="remark" data-options="multiline:true" style="height:60px"/></td>
               <td></td>
           </tr>
           <tr>
               <td width="16%" class="i18n1" name="result">结论</td>
               <td><select id="cc" class="easyui-combobox" name="result" style="width:200px;">
                   <option value="0">不合格,重新打砂处理</option>
                   <option value="1">合格,进入外喷砂检验工序</option>
                   <option value="2">待定</option>
               </select></td>
               <td></td>
               <td ></td>
               <td></td>
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
    <a href="#" class="easyui-linkbutton" iconCls="icon-save" onclick="odBlastProFormSubmit()">Save</a>
    <a href="#" class="easyui-linkbutton" id="hlcancelBtn" operationtype="add" iconCls="icon-cancel" onclick="odBlastProCancelSubmit()">Cancel</a>
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
         url="/pipeinfo/getPipeNumber.action">
        <div property="columns">
            <div type="checkcolumn" ></div>
            <div field="pipe_no" width="80" headerAlign="center" allowSort="true" class="i18n1" name="pipeno">钢管编号</div>
            <div field="contract_no" width="80" headerAlign="center" allowSort="true" class="i18n1" name="contractno">合同编号</div>
            <div field="od" width="40" headerAlign="center" allowSort="true" class="i18n1" name="od">外径</div>
            <div field="wt" width="40" headerAlign="center" allowSort="true" class="i18n1" name="wt">壁厚</div>
            <div field="p_length" width="40" headerAlign="center" allowSort="true" class="i18n1" name="p_length">长度</div>
            <div field="weight" width="40" headerAlign="center" allowSort="true" class="i18n1" name="weight">重量</div>
        </div>
    </div>
</div>
<div id="gridPanel2" class="mini-panel" title="header" iconCls="icon-add" style="width:450px;height:250px;"
     showToolbar="true" showCloseButton="true" showHeader="false" bodyStyle="padding:0" borderStyle="border:0"
>
    <div property="toolbar" id="searchBar2" style="padding:5px;padding-left:8px;text-align:center;display:none;">
        <div style="float:left;padding-bottom:2px;">
            <span class="i18n1" name="operatorno">操作工编号</span><span>:</span>
            <input id="keyText3" class="mini-textbox" style="width:110px;" onenter="onSearchClick(2)"/>
            <span class="i18n1" name="operatorname">姓名</span><span>:</span>
            <input id="keyText4" class="mini-textbox" style="width:110px;" onenter="onSearchClick(2)"/>
            <a class="mini-button" onclick="onSearchClick(2)" name="search">查找</a>
            <a class="mini-button" onclick="onClearClick(2)" name="clear">清除</a>
        </div>
        <div style="float:right;padding-bottom:2px;">
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
    var combox1=mini.get("combobox1");
    grid1.load();
    grid2.load();

    function onSearchClick(type) {
        if(type==1)
        {
            grid1.load({
                pipe_no:keyText1.value
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
        //$('.mini-buttonedit .mini-buttonedit-input').css('width','150px');
    });
    look2.on("showpopup",function(e){
        $('.mini-shadow').css('z-index','99999');
        $('.mini-popup').css('z-index','100000');
        $('.mini-panel').css('z-index','100000');
        $('#searchBar2').css('display','block');
        //$('.mini-buttonedit .mini-buttonedit-input').css('width','150px');
    });
    combox1.on("showpopup",function () {
        $('.mini-shadow').css('z-index','99999');
        $('.mini-popup').css('z-index','100000');
        $('.mini-panel').css('z-index','100000');
    });
    function onComboxCloseClick(e) {
        var obj = e.sender;
        obj.setText("");
        obj.setValue("");
    }
    hlLanguage("../i18n/");
</script>