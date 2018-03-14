<%--
  Created by IntelliJ IDEA.
  User: kurt
  Date: 3/14/18
  Time: 1:54 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
    String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>钢管倒棱处理</title>
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
            $('#hlPipeRebevelProDialog').dialog({
                onClose:function () {
                    var type=$('#hlcancelBtn').attr('operationtype');
                    if(type=="add"){
                        var $imglist=$('#fileslist');
                        var $dialog=$('#hlPipeRebevelProDialog');
                        hlAlertSix("../UploadFile/delUploadPicture.action",$imglist,$dialog,grid);
                    }
                    clearFormLabel();
                }
            });
            $('.mini-buttonedit .mini-buttonedit-input').css('width','150px');
            // hlLanguage("../i18n/");
        });


        function addPipeRebevelPro(){
            $('#hlcancelBtn').attr('operationtype','add');
            $('#hlPipeRebevelProDialog').dialog('open').dialog('setTitle','新增');
            // $('#fileslist').val('');
            // $('#PipeRebevelProForm').form('clear');
            // $('#idbinpid').text('');
            clearFormLabel();
            //combox1.setValue("");
            clearMultiUpload(grid);
            url="/PipeRebevelOperation/savePipeRebevelProcess.action";
        }
        function delPipeRebevelPro() {
            var row = $('#PipeRebevelProDatagrids').datagrid('getSelections');
            if(row.length>0){
                var idArr=[];
                for (var i=0;i<row.length;i++){
                    idArr.push(row[i].id);
                }
                var idArrs=idArr.join(',');
                $.messager.confirm('系统提示',"您确定要删除这<font color=red>"+idArr.length+ "</font>条数据吗？",function (r) {
                    if(r){
                        $.post(
                            "/PipeRebevelOperation/delPipeRebevelProcess.action",
                            {"hlparam":idArrs},function (data) {
                                if(data.success){
                                    $("#PipeRebevelProDatagrids").datagrid("reload");
                                }
                                hlAlertFour(data.message);
                            },"json");
                    }
                });
            }else{
                hlAlertOne();
            }
        }
        function editPipeRebevelPro() {
            $('#hlcancelBtn').attr('operationtype','edit');
            var row = $('#PipeRebevelProDatagrids').datagrid('getSelected');
            if(row){
                $('#hlPipeRebevelProDialog').dialog('open').dialog('setTitle','修改');
                // $('#project_name').text(row.project_name);$('#contract_no').text(row.contract_no);
                // $('#pipe_no').text(row.pipe_no);
                // $('#status_name').text(row.status_name);
                // $('#od').text(row.od);$('#wt').text(row.wt);
                // $('#p_length').text(row.p_length);$('#weight').text(row.weight);
                // $('#grade').text(row.grade);
                // $('#heat_no').text(row.heat_no);
                loadPipeBaiscInfo(row);
                $('#PipeRebevelProForm').form('load',row);
                $("#operation-time").datetimebox('setValue',getDate1(row.operation_time));
                $("#idpid").textbox("setValue", row.id);


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
                //     url:'/AcceptanceCriteriaOperation/getIDAcceptanceCriteriaByContractNo.action',
                //     dataType:'json',
                //     //data:{'contract_no':row.contract_no},
                //     success:function (data) {
                //         // var $obj1=$("input[name='relative_humidity']");
                //         // var $obj2=$("input[name='dew_point']");
                //         // var $obj3=$("input[name='blast_finish_sa25']");
                //         // var $obj4=$("input[name='profile']");
                //         // var $obj5=$("input[name='pipe_temp']");
                //         // $obj1.siblings().css("background-color","#FFFFFF");
                //         // $obj2.siblings().css("background-color","#FFFFFF");
                //         // $obj3.siblings().css("background-color","#FFFFFF");
                //         // $obj4.siblings().css("background-color","#FFFFFF");
                //         // $obj5.siblings().css("background-color","#FFFFFF");
                //         // if(data!=null){
                //         //     var res1=$obj1.val();
                //         //     var res2=$obj2.val();
                //         //     var res3=$obj3.val();
                //         //     var res4=$obj4.val();
                //         //     var res5=$obj5.val();
                //         //     if(!((res1>=data.relative_humidity_min)&&(res1<=data.relative_humidity_max)))
                //         //         $obj1.siblings().css("background-color","#F9A6A6");
                //         //     if(!((res3>=data.blast_finish_sa25_min)&&(res3<=data.blast_finish_sa25_max)))
                //         //         $obj3.siblings().css("background-color","#F9A6A6");
                //         //     if(!((res4>=data.id_profile_min)&&(res4<=data.id_profile_max)))
                //         //         $obj4.siblings().css("background-color","#F9A6A6");
                //         //     if(!((res5-res2)>=data.temp_above_dew_point_min))
                //         //         $obj5.siblings().css("background-color","#F9A6A6");
                //         //
                //         // }
                //     },error:function () {
                //
                //     }
                // });
                url="/PipeRebevelOperation/savePipeRebevelProcess.action?id="+row.id;


            }else{
                hlAlertTwo();
            }
        }



        function searchPipeRebevelPro() {
            $('#PipeRebevelProDatagrids').datagrid('load',{
                'pipe_no': $('#pipeno').val(),
                'operator_no': $('#operatorno').val(),
                'begin_time': $('#begintime').val(),
                'end_time': $('#endtime').val()
            });
        }
        function PipeRebevelProFormSubmit() {
            $('#PipeRebevelProForm').form('submit',{
                url:url,
                onSubmit:function () {
                    //表单验证


                    if($("input[name='operation-time']").val()==""){

                        hlAlertFour("请输入操作时间");
                        return false;
                    }


                },
                success: function(result){
                    //alert(result);
                    var result = eval('('+result+')');
                    if (result.success){
                        $('#hlPipeRebevelProDialog').dialog('close');
                        $('#PipeRebevelProDatagrids').datagrid('reload');
                        clearFormLabel();
                    }
                    hlAlertFour(result.message);

                },
                error:function () {
                    hlAlertThree();
                }
            });
            clearMultiUpload(grid);
        }
        function PipeRebevelProFormCancelSubmit() {
            $('#hlPipeRebevelProDialog').dialog('close');
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

        function  clearFormLabel() {
            $('#PipeRebevelProForm').form('clear');
            $('.hl-label').text(''); $('#hl-gallery-con').empty();
            //combox1.setValue("");
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
        <table class="easyui-datagrid" id="PipeRebevelProDatagrids" url="/PipeRebevelOperation/getAllByLike.action" striped="true" loadMsg="正在加载中。。。" textField="text" pageSize="20" fitColumns="true" pagination="true" toolbar="#hlPipeRebevelProTb">
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
                <th field="p_length" align="center" width="50" hidden="true" class="i18n1" name="p_length">长度</th>
                <th field="weight" align="center" width="50" hidden="true" class="i18n1" name="weight">重量</th>
                <th field="heat_no" align="center" hidden="true" width="50" class="i18n1" name="heat_no">炉号</th>
                <th field="operator_no" align="center" width="100" class="i18n1" name="operatorno">操作工编号</th>

                <th field="squareness" align="center" width="120" class="i18n1" name="squareness">切斜</th>
                <th field="ovality" align="center" width="100"  class="i18n1" name="ovality">椭圆度</th>
                <th field="bevel" width="100" align="center"  class="i18n1" name="bevel">坡口</th>
                <th field="rootface" align="center" width="120" class="i18n1" name="rootface">钝边</th>

                <th field="remark" align="center" width="150" class="i18n1" name="remark">备注</th>
                <th field="result" align="center" width="150" class="i18n1" name="result">结论</th>
                <th field="operation_time" align="center" width="150" class="i18n1" name="operationtime" data-options="formatter:formatterdate">操作时间</th>
            </tr>
            </thead>
        </table>

    </div>
</fieldset>


<!--工具栏-->
<div id="hlPipeRebevelProTb" style="padding:10px;">
    <span class="i18n1" name="pipeno">钢管编号</span>:
    <input id="pipeno" name="pipeno" style="line-height:26px;border:1px solid #ccc">
    <span class="i18n1" name="operatorno">操作工编号</span>:
    <input id="operatorno" name="operatorno" style="line-height:26px;border:1px solid #ccc">
    <span class="i18n1" name="begintime">开始时间</span>:
    <input id="begintime" name="begintime" type="text" class="easyui-datebox" data-options="formatter:myformatter,parser:myparser">
    <span class="i18n1" name="endtime">结束时间</span>:
    <input id="endtime" name="endtime" type="text" class="easyui-datebox" data-options="formatter:myformatter,parser:myparser">
    <a href="#" class="easyui-linkbutton" plain="true" data-options="iconCls:'icon-search'" onclick="searchPipeRebevelPro()">Search</a>
    <div style="float:right">
        <a href="#" id="addLinkBtn" class="easyui-linkbutton i18n1" name="add" data-options="iconCls:'icon-add',plain:true" onclick="addPipeRebevelPro()">添加</a>
        <a href="#" id="editLinkBtn" class="easyui-linkbutton i18n1" name="edit" data-options="iconCls:'icon-edit',plain:true" onclick="editPipeRebevelPro()">修改</a>
        <a href="#" id="deltLinkBtn" class="easyui-linkbutton i18n1" name="delete" data-options="iconCls:'icon-remove',plain:true" onclick="delPipeRebevelPro()">删除</a>
    </div>
</div>



<!--添加、修改框-->
<div id="hlPipeRebevelProDialog" class="easyui-dialog" data-options="title:'添加',modal:true"  closed="true" buttons="#dlg-buttons" style="display: none;padding:5px;width:950px;height:auto;">
    <form id="PipeRebevelProForm" method="post">
        <fieldset style="width:900px;border:solid 1px #aaa;margin-top:8px;position:relative;">
            <legend>钢管信息</legend>
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
                        <input id="lookup1" name="pipe_no" class="mini-lookup" style="text-align:center;width:180px;"
                               textField="pipe_no" valueField="id" popupWidth="auto"
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
            <legend>光管倒棱信息</legend>

            <table class="ht-table">
                <tr>
                    <td class="i18n1" name="id" width="20%">流水号</td>
                    <td colspan="1" width="30%"><input class="easyui-textbox" type="text" id="idpid" name="idpid" readonly="true" value="0"/></td>
                    <td></td>
                    <td colspan="1" width="30%">

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
                        <input class="easyui-datetimebox" type="text" id="operation-time" name="operation-time" value="" data-options="formatter:myformatter2,parser:myparser2"/>
                    </td>

                </tr>
            </table>

            <table class="ht-table">
                <tr>
                    <td class="i18n1" name="squareness">切斜</td>
                    <td>
                        <select  class="easyui-combobox" data-options="editable:false"  name="squareness" style="width:200px;">
                            <option value="0">Not OK</option>
                            <option value="1">OK</option>
                        </select>
                    </td>
                    <td></td>
                    <td class="i18n1" name="ovality">修磨或切割</td>
                    <td>
                        <select  class="easyui-combobox" data-options="editable:false"  name="ovality" style="width:200px;">
                            <option value="0">Not OK</option>
                            <option value="1">OK</option>
                        </select>

                    </td>
                    <td></td>
                </tr>

                <tr>
                    <td class="i18n1" name="bevel">坡口</td>
                    <td>
                        <select  class="easyui-combobox" data-options="editable:false"  name="bevel" style="width:200px;">
                            <option value="0">Not OK</option>
                            <option value="1">OK</option>
                        </select>
                    </td>
                    <td></td>
                    <td class="i18n1" name="rootface">钝边</td>
                    <td>
                        <select  class="easyui-combobox" data-options="editable:false"  name="rootface" style="width:200px;">
                            <option value="0">Not OK</option>
                            <option value="1">OK</option>
                        </select>

                    </td>
                    <td></td>
                </tr>


                <tr>
                    <td width="16%" class="i18n1" name="result">结论</td>
                    <td><select id="cc" class="easyui-combobox" data-options="editable:false"  name="result" style="width:200px;">
                        <option value="0">不合格,重新倒棱处理</option>
                        <option value="1">合格</option>
                        <option value="2">待定</option>
                    </select></td>
                    <td></td>
                    <td width="16%" class="i18n1" name="remark">备注</td>
                    <td><input class="easyui-textbox" type="text" value="" name="remark" data-options="multiline:true" style="height:60px"/></td>
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
    <a href="#" class="easyui-linkbutton" iconCls="icon-save" onclick="PipeRebevelProFormSubmit()">Save</a>
    <a href="#" class="easyui-linkbutton" id="hlcancelBtn" operationtype="add" iconCls="icon-cancel" onclick="PipeRebevelProFormCancelSubmit()">Cancel</a>
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
            <div field="pipe_no" width="80" headerAlign="center" allowSort="true" class="i18n1" name="pipeno">钢管编号</div>
            <div field="contract_no" width="80" headerAlign="center" allowSort="true" class="i18n1" name="contractno">合同编号</div>
            <div field="status" width="40" headerAlign="center" allowSort="true" class="i18n1" name="status">状态</div>
            <div field="od" width="40" headerAlign="center" allowSort="true" class="i18n1" name="od">外径</div>
            <div field="wt" width="40" headerAlign="center" allowSort="true" class="i18n1" name="wt">壁厚</div>
            <div field="p_length" width="40" headerAlign="center" allowSort="true" class="i18n1" name="p_length">长度</div>
            <div field="weight" width="40" headerAlign="center" allowSort="true" class="i18n1" name="weight">重量</div>
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
    var keyText4 = mini.get("keyText4");
    var keyText3=mini.get("keyText3");
    var grid1=mini.get("datagrid1");
    var grid2=mini.get("datagrid2");
    var look1=mini.get('lookup1');
    var look2= mini.get("lookup2");
    //var combox1=mini.get("combobox1");

    function onSearchClick(type) {
        if(type==1)
        {
            grid1.load({
                pipe_no:keyText1.value,
                pipestatus:'onhold,'
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
    look2.on('valuechanged',function (e) {
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
            pipestatus:'onhold,'
        });
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
    });

    // combox1.on("showpopup",function () {
    //     $('.mini-shadow').css('z-index','99999');
    //     $('.mini-popup').css('z-index','100000');
    //     $('.mini-panel').css('z-index','100000');
    // });
    // function onComboxCloseClick(e) {
    //     var obj = e.sender;
    //     obj.setText("");
    //     obj.setValue("");
    // }

    hlLanguage("../i18n/");
</script>
