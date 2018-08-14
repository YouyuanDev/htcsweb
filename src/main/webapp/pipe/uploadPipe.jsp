<%--
  Created by IntelliJ IDEA.
  User: kurt
  Date: 2/15/18
  Time: 10:50 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>上传钢管列表</title>
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
    <link rel="stylesheet" type="text/css" href="../easyui/themes/default/easyui.css">

    <script type="text/javascript">//导入excel

    </script>

        </head>
<body>
<br />
<div align="left">
<h3><span class="i18n1" name="plsselectupladexcelfiles">请选择要上传的excel文件（.xls）</span></h3>
<br />
<input id="fileupload1" class="mini-fileupload" name="Fdata" limitType="*.xls;"
       flashUrl="../miniui/fileupload/swfupload/swfupload.swf"
       uploadUrl="/UploadFile/uploadPipeList.action"
       onuploadsuccess="onUploadSuccess"
       onuploaderror="onUploadError" onfileselect="onFileSelect" width="400px"
/>
<br /><br />
    <select id="entrance" class="easyui-combobox" data-options="editable:false" name="entrance" style="width:200px;">
        <option value="0">录入外防光管库</option>
        <option value="1">录入内防光管库</option>
    </select><br><br>
    <input type="checkbox" id="ck_overwrite" name="ck_overwrite"/><span class="i18n1" name="is_overwrite">是否完全覆盖数据库已有记录？(对数据库已存在的钢管,不会覆盖其状态信息)</span>
    <br /><br />
    <a class="mini-button mini-button-success" width="100px" value="上传" onclick="startUpload()"><span class="i18n1" name="upload">上传</span></a>
<%--<input type="button" class="mini-button mini-button-success" width="80px" value="上传" onclick="startUpload()"/>--%>
</div>
<div class="description">
    <h3><span class="i18n1" name="description">描述</span></h3>
    <p><span class="i18n1" name="uploadpipetext">上传钢管入库清单</span></p>
    <p><a href="../template/upload_pipe_template.xls" >点击下载：钢管清单模版.xls</a></p>

    <p>序号&#9;管捆号&#9;合同号&#9;原合同号&#9;外径&#9;壁厚&#9;炉号&#9;试批号&#9;实际重量&#9;理论重量&#9;总长度&#9;库位</p><br>
    <p>1&#9;1523580&#9;RL36800012&#9;RL36800012&#9;219.1&#9;5.6&#9;17131593&#9;72456&#9;0.546&#9;0.536&#9;18.17&#9;1A02</p><br>
    <p>2&#9;1524530&#9;RL36800012&#9;RL36800012&#9;219.1&#9;5.6&#9;17131593&#9;72457&#9;0.546&#9;0.538&#9;18.24&#9;1A02</p><br>
    <p>3&#9;1524540&#9;RL36800012&#9;RL36800012&#9;219.1&#9;5.6&#9;17131593&#9;72457&#9;0.55&#9;0.538&#9;18.24&#9;1A02</p><br>
    <p>4&#9;1524550&#9;RL36800012&#9;RL36800012&#9;219.1&#9;5.6&#9;17131593&#9;72457&#9;0.551&#9;0.538&#9;18.25&#9;1A02</p><br>
</div>


</body>
</html>
<script type="text/javascript">
    mini.parse();

    //动态设置url
    //    var fileupload = mini.get("fileupload1");
    //    fileupload.setUploadUrl("upload.aspx");

    function onFileSelect(e) {
        //alert("选择文件");
    }
    function onUploadSuccess(e) {

        //alert("上传成功：" + e.serverData);
        var result = eval('('+e.serverData+')');
        if(result.success){
            alert("成功上传钢管数量："+result.totaluploaded+" 根，"+"因不存在合同号无法上传的钢管数量："+result.totalskipped+" 根");
        }

        this.setText("");
    }
    function onUploadError(e) {
        alert("上传错误：" + e.serverData);
    }

    function startUpload() {
        var fileupload = mini.get("fileupload1");
        var checkbox = document.getElementById('ck_overwrite');//
        var entrance = $('#entrance').val()
        //alert(checkbox.checked);//是否被选中
        var overwrite="0";

        if(checkbox.checked){
            //选中了
            //alert("checked")
            overwrite="1";
            //fileupload.setUploadUrl("/UploadFile/uploadPipeList.action?ck_overwrite=" + "1");
        }else{
            //没选中
            //alert("Not checked")
            overwrite="0";
            //fileupload.setUploadUrl("/UploadFile/uploadPipeList.action?ck_overwrite=" + "0");
        }
        if(entrance=='1'){
            //录入内防光管库
            //alert("1111")
            fileupload.setUploadUrl("/UploadFile/uploadPipeList.action?ck_overwrite=" + overwrite+"&&entrance=1");
        }else{
            //录入外防光管库
            //alert("0000");
            fileupload.setUploadUrl("/UploadFile/uploadPipeList.action?ck_overwrite=" + overwrite+"&&entrance=0");
        }




        fileupload.startUpload();
    }

    hlLanguage("../i18n/");
</script>