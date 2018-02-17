<%--
  Created by IntelliJ IDEA.
  User: kurt
  Date: 2/15/18
  Time: 10:50 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
<input id="fileupload1" class="mini-fileupload" name="Fdata" limitType="*.txt;*.xls;*.xlsx"
       flashUrl="../miniui/fileupload/swfupload/swfupload.swf"
       uploadUrl="/UploadFile/uploadPipeList.action"
       onuploadsuccess="onUploadSuccess"
       onuploaderror="onUploadError" onfileselect="onFileSelect" width="400px"
/>

<br /><br />
<input type="button" value="上传" onclick="startUpload()"/>
<div class="description">
    <h3>Description</h3>
    <p>上传钢管入库清单</p>
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

        alert("上传成功：" + e.serverData);
        var result = eval('('+e.serverData+')');
        if(result.success){
            alert("成功上传钢管数量："+result.totaluploaded+" 根，"+"因不存在合同号无法上传的钢管数量："+result.totalskipped+" 根");
        }

        this.setText("");
    }
    function onUploadError(e) {

    }

    function startUpload() {
        var fileupload = mini.get("fileupload1");

        fileupload.startUpload();
    }


</script>