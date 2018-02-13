<%--
  Created by IntelliJ IDEA.
  User: kurt
  Date: 2/12/18
  Time: 5:17 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>项目管理</title>
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


        function formatterdate(value,row,index){
            var date = new Date(value);
            //return date.toLocaleString();
            var y = date.getFullYear();
            var m = date.getMonth()+1;
            var d = date.getDate();
            return y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d);

        }


        // 日期格式为 2/20/2017 12:00:00 PM
        function myformatter2(date){
            return date.toLocaleString();
        }
        // 日期格式为 2/20/2017 12:00:00 PM
        function myparser2(s) {
            if (!s) return new Date();
            return new Date(Date.parse(s));
        }




        function string2date(str){
            return new Date(Date.parse(str.replace(/-/g,  "/")));
        }

        // 日期格式为 2018-2-20
        function myformatter(date){
            var y = date.getFullYear();
            var m = date.getMonth()+1;
            var d = date.getDate();
            return y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d);
        }
        // 日期格式为 2018-2-20
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


        function searchProject() {
            $('#projectDatagrids').datagrid('load',{
                'project_no': $('#projectno').val(),
                'project_name': $('#projectname').val(),
                'client_name': $('#clientname').val(),
                'begin_time': $('#begintime').val(),
                'end_time': $('#endtime').val()
            });
        }
        $(function () {

            $('#hlProjectDialog').dialog({
                onClose:function () {
                    var type=$('#hlcancelBtn').attr('operationtype');
                    if(type=="add"){
                        var fileslist=$('#fileslist');
                        var $dialog=$('#hlProjectDialog');
                        hlAlertSix("../UploadFile/delUploadPicture.action",fileslist,$dialog,grid);
                    }else{

                        $('#hl-gallery-con').empty();
                        $('#fileslist').val('');
                        clearFormLabel();
                    }
                }
            });
            $('.mini-buttonedit .mini-buttonedit-input').css('width','150px');
            // hlLanguage("../i18n/");
        });


        function addProject(){
            $('#hlcancelBtn').attr('operationtype','add');
            $('#hlProjectDialog').dialog('open').dialog('setTitle','新增项目');
            $('#fileslist').val('');
            $('#projectForm').form('clear');
            clearMultiUpload(grid);
            url="/ProjectOperation/saveProject.action";
        }
        function editProject() {
            $('#hlcancelBtn').attr('operationtype','edit');
            var row = $('#projectDatagrids').datagrid('getSelected');
            if(row){
                 $('#hlProjectDialog').dialog('open').dialog('setTitle','修改');
                  // $('#project_no').textbox('setValue',row.project_no);
                  // $('#project_name').textbox('setValue',row.project_name);
                  // $('#client_name').textbox('setValue',row.client_name);
                  // $('#client_spec').textbox('setValue',row.client_spec);
                  // $('#coating_standard').textbox('setValue',row.coating_standard);
                  // $('#mps').textbox('setValue',row.mps);
                  // $('#itp').textbox('setValue',row.itp);
                  // $('#project_time').datebox('setValue',row.project_time);
                  // $('#projectid').text(row.id);

                // $('#project_no').text(row.project_no);
                // $('#project_name').text(row.project_name);
                // $('#client_name').text(row.client_name);
                // $('#client_spec').text(row.client_spec);
                // $('#coating_standard').text(row.coating_standard);
                // $('#mps').text(row.mps);
                // $('#itp').text(row.itp);
                // $('#project_time').text(getDate(row.project_time));
                // $('#projectForm').form('load',row);

                var date;
                var strdate="";
                if(row.project_time!=null&&row.project_time!=""){
                    date = new Date(row.project_time);
                    strdate =myformatter(date);
                }


                $('#projectForm').form('load',{
                    'projectid':row.id,
                    'project_no':row.project_no,
                    'project_name':row.project_name,
                    'client_name':row.client_name,
                    'client_spec':row.client_spec,
                    'coating_standard':row.coating_standard,
                    'mps':row.mps,
                    'itp':row.itp,
                    'project_time':strdate

                });

                var files=row.upload_files;
                if(files!=null&&files!=""){
                    var fiList=files.split(';');
                    createPictureModel(fiList);
                }
                url="/ProjectOperation/saveProject.action?id="+row.id;

            }else{
                hlAlertTwo();
            }
        }
        function delProject() {
            var row = $('#projectDatagrids').datagrid('getSelections');
            if(row.length>0){
                var idArr=[];
                for (var i=0;i<row.length;i++){
                    idArr.push(row[i].id);
                }
                var idArrs=idArr.join(',');
                hlAlertFive("/ProjectOperation/delProject.action",idArrs,idArr.length);
                // $.messager.confirm('提示','您确定要删除<font>')
            }else{
                hlAlertOne();
            }
        }






        //文件上传失败操作
        function onUploadError() {
            alert("上传失败!");
        }
        //文件上传成功操作
        function onUploadSuccess(e) {
            var data=eval("("+e.serverData+")");
            var fileListstr=editFilesList(0,data.fileUrl);
            var fList=fileListstr.split(';');
            alert("success");
            //createPictureModel(fList);
        }
        function editFilesList(type,fileUrl) {
            var $obj=$('#fileslist');
            if(type==0){
                var filesList=$('#fileslist').val();
                $obj.val(filesList+fileUrl+";");
            }else{
                $obj.val($obj.val().replace(fileUrl+";",''));
            }
            return $obj.val();
        }

        //取消保存
        function ProjectFormCancelSubmit() {
            $('#hlProjectDialog').dialog('close');


        }


        //增加或保存项目信息
        function ProjectFormSubmit() {

            $('#projectForm').form('submit',{
                url:url,
                onSubmit:function () {
                    //表单验证

                    setParams($("input[name='project_name']"));
                    setParams($("input[name='client_name']"));
                    setParams($("input[name='client_spec']"));
                    setParams($("input[name='coating_standard']"));
                    setParams($("input[name='mps']"));
                    setParams($("input[name='itp']"));

                    if($("input[name='project_time']").val()==""){

                        hlAlertFour("请输入项目开始日期");
                        return false;
                    }


                    //return $('#projectForm').form('enableValidation').form('validate');
                },
                success: function(result){
                    var result = eval('('+result+')');
                    if (result.success){
                        $('#hlProjectDialog').dialog('close');
                        $('#projectDatagrids').datagrid('reload');
                        clearFormLabel();
                        //$('#hl-gallery-con').empty();
                       // $('#projectDatagrids').datagrid('clearSelections');
                    } else {
                        //$.messager.alert('提示',data.msg,'info');
                        hlAlertFour("操作失败!");
                    }
                },
                error:function () {
                    hlAlertThree();
                }
            });
            clearMultiUpload(grid);
        }
        function  setParams($obj) {
            if($obj.val()==null||$obj.val()=="")
                $obj.val('');
        }

        function  clearFormLabel() {
            $('#projectForm').form('clear');

        }


        // $.extend($.fn.validatebox.defaults.rules, {
        //     myvalidate : {
        //         validator : function(value, param) {
        //             var projectno = $("#project_no").val().trim();
        //             console.log(projectno);
        //             alert(projectno)
        //             var haha = " ";
        //             $.ajax({
        //                 type : 'post',
        //                 async : false,
        //                 url : '/ProjectOperation/checkProjectNoAvailable.action',
        //                 data : {
        //                     "project_no" : projectno
        //                 },
        //                 success : function(data) {
        //                     haha = data;
        //                 }
        //             });
        //             console.log(haha);
        //             return haha.indexOf("true");
        //         },
        //         message : '项目名已经被占用'
        //     }
        // });





    </script>



</head>
<body>
<fieldset class="b3" style="padding:10px;margin:10px;">
    <legend> <h3><b style="color: orange" >|&nbsp;</b><span class="i18n1" name="datadisplay">数据展示</span></h3></legend>
    <div  style="margin-top:5px;">
        <table class="easyui-datagrid" id="projectDatagrids" url="/ProjectOperation/getProjectInfoByLike.action" striped="true" loadMsg="正在加载中。。。" textField="text" pageSize="20" fitColumns="true" pagination="true" toolbar="#hlprojectTb">
            <thead>
            <tr>
                <th data-options="field:'ck',checkbox:true"></th>
                <th field="id" align="center" width="100" class="i18n1" name="id">流水号</th>
                <th field="project_no" align="center" width="100" class="i18n1" name="projectno">项目编号</th>
                <th field="project_name" align="center" width="100" class="i18n1" name="projectname">项目名称</th>
                <th field="client_name" align="center" width="100" class="i18n1" name="clientname">客户名称</th>
                <th field="client_spec" align="center" width="100" class="i18n1" name="clientspec">客户技术规格书</th>
                <th field="coating_standard" align="center" width="100" class="i18n1" name="coatingstandard">涂层标准</th>
                <th field="mps" align="center" width="100" class="i18n1" name="mps">MPS</th>
                <th field="itp" align="center" width="100" class="i18n1" name="itp">ITP</th>
                <th field="project_time" align="center" width="100" class="i18n1" name="projecttime" data-options="formatter:formatterdate">项目开始时间</th>
            </tr>
            </thead>
        </table>

    </div>
</fieldset>

<!--工具栏-->
<div id="hlprojectTb" style="padding:10px;">
    <span class="i18n1" name="projectno">项目编号</span>:
    <input id="projectno" name="projectno"  style="width:100px;line-height:22px;border:1px solid #ccc">
    <span class="i18n1" name="projectname">项目名称</span>:
    <input id="projectname" name="projectname" style="width:100px;line-height:22px;border:1px solid #ccc">
    <span class="i18n1" name="clientname">客户名称</span>:
    <input id="clientname" name="clientname" style="width:100px;line-height:22px;border:1px solid #ccc">

    <span class="i18n1" name="begintime">开始时间</span>:
    <input id="begintime" name="begintime" type="text" class="easyui-datebox" data-options="formatter:myformatter,parser:myparser">
    <span class="i18n1" name="endtime">结束时间</span>:
    <input id="endtime" name="endtime" type="text" class="easyui-datebox" data-options="formatter:myformatter,parser:myparser">
    <a href="#" class="easyui-linkbutton" plain="true" data-options="iconCls:'icon-search'" onclick="searchProject()">Search</a>
    <div style="float:right">
        <a href="#" id="addProjectLinkBtn" class="easyui-linkbutton i18n1" name="add" data-options="iconCls:'icon-add',plain:true" onclick="addProject()">添加</a>
        <a href="#" id="editProjectLinkBtn" class="easyui-linkbutton i18n1" name="edit" data-options="iconCls:'icon-edit',plain:true" onclick="editProject()">修改</a>
        <a href="#" id="deltProjectLinkBtn" class="easyui-linkbutton i18n1" name="delete" data-options="iconCls:'icon-remove',plain:true" onclick="delProject()">删除</a>
    </div>
</div>


<!--添加、修改框-->
<div id="hlProjectDialog" class="easyui-dialog" data-options="title:'添加',modal:true"  closed="true" buttons="#dlg-buttons" style="display: none;padding:5px;width:950px;height:auto;">
    <form id="projectForm" method="post">
        <fieldset style="width:900px;border:solid 1px #aaa;margin-top:8px;position:relative;">
            <legend class="i18n1" name="projectinfo">项目信息</legend>
            <table class="ht-table" width="100%" border="0">
                <tr>
                    <td class="i18n1" name="id">流水号</td>
                    <td colspan="5"><input class="easyui-textbox" type="text" name="projectid" readonly="true" value="0"/></td>

                </tr>
                <tr>
                    <td class="i18n1" name="projectno" width="16%">项目编号</td>
                    <td width="33%"><input class="easyui-validatebox" type="text" name="project_no"  value=""/></td>

                    <td class="i18n1" name="projectname" width="16%">项目名称</td>
                    <td width="33%"><input class="easyui-validatebox" type="text" name="project_name"   value=""/></td>


                </tr>

                <tr>
                    <td class="i18n1" name="clientname" width="16%">客户名称</td>
                    <td   width="33%"><input class="easyui-validatebox" type="text" name="client_name" value=""/></td>

                    <td class="i18n1" name="clientspec" width="16%">客户技术规格书</td>
                    <td  width="33%"><input class="easyui-validatebox" type="text" name="client_spec" value=""/></td>

                </tr>
                <tr>
                    <td class="i18n1" name="coatingstandard" width="16%">涂层标准</td>
                    <td   width="33%"><input class="easyui-validatebox" type="text" name="coating_standard" value=""/></td>
                    <td class="i18n1" name="projecttime" width="16%">项目时间</td>
                    <td   width="33%"><input class="easyui-datebox" type="text" name="project_time" value="" data-options="formatter:myformatter2,parser:myparser2"/></td>
                </tr>
                <tr>
                    <td class="i18n1" name="mps" width="16%">MPS</td>
                    <td   width="33%"><input class="easyui-validatebox" type="text" name="mps" value=""/></td>

                    <td class="i18n1" name="itp" width="16%">ITP</td>
                    <td   width="33%"><input class="easyui-validatebox" type="text" name="itp" value=""/></td>

                </tr>
            </table>

            <input type="hidden" id="fileslist" name="upload_files" value=""/>
            <div id="hl-gallery-con" style="width:100%;">

            </div>
            <div id="multiupload1" class="uc-multiupload" style="width:100%; max-height:200px"
                 flashurl="../miniui/fileupload/swfupload/swfupload.swf"
                 uploadurl="../UploadFile/uploadFile.action" _autoUpload="false" _limittype="*.txt;*.pdf;*.doc;*.docx;*.xls;*.xlsx"
                 onuploaderror="onUploadError" onuploadsuccess="onUploadSuccess">
            </div>
        </fieldset>


    </form>


</div>
<div id="dlg-buttons" align="center" style="width:900px;">
    <a href="#" class="easyui-linkbutton" iconCls="icon-save" onclick="ProjectFormSubmit()">Save</a>
    <a href="#" class="easyui-linkbutton" id="hlcancelBtn" operationtype="add" iconCls="icon-cancel" onclick="ProjectFormCancelSubmit()">Cancel</a>
</div>



<script type="text/javascript" src="../easyui/jquery.easyui.min.js"></script>
</body>
</html>
<script type="text/javascript">
    mini.parse();
    var grid= mini.get("multiupload1");
    hlLanguage("../i18n/");
</script>