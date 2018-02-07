<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
    String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>信息</title>
    <link rel="stylesheet" type="text/css" href="../easyui/themes/bootstrap/easyui.css">
    <link rel="stylesheet" type="text/css" href="../easyui/themes/icon.css">
    <link href="../miniui/multiupload/multiupload.css" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" href="../css/common.css"/>
    <script type="text/javascript" src="../easyui/jquery.min.js"></script>
    <script src="../js/jquery.i18n.properties-1.0.9.js" type="text/javascript"></script>
    <script src="../js/language.js" type="text/javascript"></script>
    <script src="../js/common.js" type="text/javascript"></script>
    <script src="../miniui/boot.js" type="text/javascript"></script>
    <script type="text/javascript" src="../miniui/js/miniui.js"></script>
    <script src="../miniui/fileupload/swfupload/swfupload.js" type="text/javascript"></script>
    <script src="../miniui/multiupload/multiupload.js" type="text/javascript"></script>
    <script type="text/javascript" src="../js/lrscroll.js"></script>
    <script src="../js/jquery.i18n.properties-1.0.9.js" type="text/javascript"></script>
    <script src="../js/language.js" type="text/javascript"></script>
    <script type="text/javascript">

        $(function () {
                    //删除上传的图片
                $(document).on('click','.content-del',function () {
                     delUploadPicture($(this));
                });

        });
        function addOdBlastPro(){
            $('#hlOdBlastProDialog').dialog('open').dialog('setTitle','新增');
            $('#fileslist').val('');
            $('#odBlastProForm').form('clear');$('#odbpid').text('');$('#odbptime').text('');
            clearMultiUpload();
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
            clearMultiUpload();
            var row = $('#odBlastProDatagrids').datagrid('getSelected');
            if(row){
                $('#hlOdBlastProDialog').dialog('open').dialog('setTitle','修改');
                var odbpid=row.id;
                var odbptime=getDate(row.operation_time);
                $('#odbpid').text(odbpid);$('#odbptime').text(odbptime);
                $('#odBlastProForm').form('load',row);
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
                success: function(result){
                    var result = eval('('+result+')');
                    if (result.success){
                        $('#hlOdBlastProDialog').dialog('close');
                        $('#odBlastProDatagrids').datagrid('reload');
                        $('#hl-gallery-con').empty();
                    } else {
                        hlAlertFour("操作失败!");
                    }
                },
                error:function () {
                    hlAlertThree();
                }
            });
        }
        function odBlastProCancelSubmit() {
            var $imglist=$('#fileslist');
            var $dialog=$('#hlOdBlastProDialog');
            hlAlertSix("../UploadFile/delUploadPicture.action",$imglist,$dialog,grid);
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
        //清理图片选择
        function  clearMultiUpload() {
            var rows = grid.getData();
            for (var i = 0, l = rows.length; i < l; i++) {
                grid.uploader.cancelUpload(rows[i].fileId);
                grid.customSettings.queue.remove(rows[i].fileId);
            }
            grid.clearData();
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
    </script>





</head>

<body>
<fieldset class="b3" style="padding:10px;margin:10px;">
    <legend> <h3><b style="color: orange" >|&nbsp;</b><span class="i18n1" name="datadisplay">数据展示</span></h3></legend>
    <div  style="margin-top:5px;">
         <table class="easyui-datagrid" id="odBlastProDatagrids" url="/OdOperation/getOdBlastByLike.action" pagination="true" toolbar="#hlOdBlastProTb">
             <thead>
               <tr>
                       <th data-options="field:'ck',checkbox:true"></th>
                       <th field="id" width="100" class="i18n1" name="id">流水号</th>
                       <th field="pipe_no" width="100" class="i18n1" name="pipeno">钢管编号</th>
                       <th field="operation_time" width="100" class="i18n1" name="operationtime">操作时间</th>
                       <th field="operator_no" width="100" class="i18n1" name="operatorno">操作工编号</th>
                       <th field="surface_condition" width="100" class="i18n1" name="surfacecondition">外观缺陷</th>
                       <th field="salt_contamination_before_blasting" width="100" class="i18n1" name="saltcontaminationbeforeblasting">打砂前盐度</th>
                       <th field="alkaline_dwell_time" width="100" class="i18n1" name="alkalinedwelltime">碱洗时间</th>
                       <th field="alkaline_concentration" width="100" class="i18n1" name="alkalineconcentration">碱浓度</th>
                       <th field="conductivity" width="100" class="i18n1" name="conductivity">传导性</th>
                       <th field="acid_wash_time" width="100" class="i18n1" name="acidwashtime">酸洗时间</th>
                       <th field="acid_concentration" width="100" class="i18n1" name="acidconcentration">酸浓度</th>
                       <th field="blast_line_speed" width="100" class="i18n1" name="blastlinespeed">打砂传送速度</th>
                       <th field="preheat_temp" width="100" class="i18n1" name="preheattemp">预热温度</th>
                       <th field="remark" width="100" class="i18n1" name="remark">备注</th>
               </tr>
             </thead>
         </table>

    </div>
</fieldset>

<!--工具栏-->
<div id="hlOdBlastProTb" style="padding:10px;">
    <span class="i18n1" name="pipeno">钢管编号</span>:
    <input id="pipeno" name="pipeno" style="line-height:26px;border:1px solid #ccc">
    <span class="i18n1" name="operatorno">操作工编号</span>:
    <input id="operatorno" name="operatorno" style="line-height:26px;border:1px solid #ccc">
    <span class="i18n1" name="begintime">开始时间</span>:
    <input id="begintime" name="begintime" type="text" class="easyui-datebox">
    <span class="i18n1" name="endtime">结束时间</span>:
    <input id="endtime" name="endtime" type="text" class="easyui-datebox">
    <a href="#" class="easyui-linkbutton" plain="true" data-options="iconCls:'icon-search'" onclick="searchOdBlastPro()">Search</a>
    <div style="float:right">
     <a href="#" id="addObpLinkBtn" class="easyui-linkbutton i18n1" name="add" data-options="iconCls:'icon-add',plain:true" onclick="addOdBlastPro()">添加</a>
     <a href="#" id="editObpLinkBtn" class="easyui-linkbutton i18n1" name="edit" data-options="iconCls:'icon-edit',plain:true" onclick="editOdBlastPro()">修改</a>
     <a href="#" id="deltObpLinkBtn" class="easyui-linkbutton i18n1" name="delete" data-options="iconCls:'icon-remove',plain:true" onclick="delOdBlastPro()">删除</a>
    </div>
</div>

<!--添加、修改框-->
<div id="hlOdBlastProDialog" class="easyui-dialog" data-options="title:'添加',modal:true"  closed="true" buttons="#dlg-buttons" style="display: none;padding:5px;width:800px;height:auto;">
   <form id="odBlastProForm" method="post">
       <table class="ht-table">
           <tr>
               <td>流水号</td>
               <td><label id="odbpid"></label></td>
               <td></td>
               <td>钢管编号</td>
               <td><input class="easyui-validatebox" type="text" name="pipe_no"/></td>
               <td></td>
           </tr>
           <tr>
               <td>操作工编号</td>
               <td><input class="easyui-validatebox" type="text" name="operator_no"/></td>
               <td></td>
               <td>操作时间</td>
               <td><label id="odbptime"></label></td>
               <td></td>
           </tr>
       </table>
<hr>
       <table class="ht-table">
           <tr>
               <td>碱洗时间(秒)</td>
               <td><input class="easyui-validatebox" type="text" name="alkaline_dwell_time"/></td>
               <td>10~20</td>
               <td>碱浓度</td>
               <td><input class="easyui-validatebox" type="text" name="alkaline_concentration"/></td>
               <td></td>

           </tr>

           <tr>
               <td>酸洗时间</td>
               <td><input class="easyui-validatebox" type="text" name="acid_wash_time"/></td>
               <td></td>
               <td>酸浓度</td>
               <td><input class="easyui-validatebox" type="text" name="acid_concentration"/></td>
               <td></td>
           </tr>
       </table>
       <hr>
       <table class="ht-table">
           <tr>
               <td>外观缺陷</td>
               <td><input class="easyui-validatebox" type="text" name="surface_condition"/></td>
               <td></td>
               <td>打砂前盐度</td>
               <td><input class="easyui-validatebox" type="text" name="salt_contamination_before_blasting"/></td>
               <td><=25mg/㎡</td>
           </tr>

           <tr>
               <td>打砂传送速度(m/s)</td>
               <td><input class="easyui-validatebox" type="text" name="blast_line_speed"/></td>
               <td></td>
               <td>传导性</td>
               <td><input class="easyui-validatebox" type="text" name="conductivity"/></td>
               <td></td>
           </tr>
           <tr>
               <td>预热温度(℃)</td>
               <td><input class="easyui-validatebox" type="text" name="preheat_temp"/></td>
               <td></td>
               <td>备注</td>
               <td><input class="easyui-textbox" type="text" name="remark" data-options="multiline:true" style="height:60px"/></td>
               <td></td>
           </tr>

       </table>
       <input type="hidden" id="fileslist" name="upload_files" value="">
   </form>
    <div id="multiupload1" class="uc-multiupload" style="width:100%; max-height:200px"
         flashurl="../miniui/fileupload/swfupload/swfupload.swf"
         uploadurl="../UploadFile/uploadPicture.action" _autoUpload="false" _limittype="*jpg"
         onuploaderror="onUploadError" onuploadsuccess="onUploadSuccess"
    >
    </div>
    <div id="hl-gallery-con">

    </div>
</div>
<div id="dlg-buttons">
    <a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="odBlastProFormSubmit()">Ok</a>
    <a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="odBlastProCancelSubmit()">Cancel</a>
</div>
<script type="text/javascript" src="../easyui/jquery.easyui.min.js" charset="UTF-8"></script>
</body>
</html>
<script type="text/javascript">
    mini.parse();
    var grid= mini.get("multiupload1");
    hlLanguage("../i18n/");
</script>