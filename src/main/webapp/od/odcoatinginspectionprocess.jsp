<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
    String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>外涂检验岗位</title>
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



    </script>

</head>

<body>
<table>
    <tr>
        <td style="width:180px;height:30px;">
            <input id="lookup1" name="pipe_no" class="mini-lookup" style="text-align:center;width:180px;"
                   textField="pipe_no" valueField="pipe_no" popupWidth="auto"
                   popup="#gridPanel1" grid="#datagrid1" multiSelect="false"
            />
        </td>
    </tr>
</table>


<div id="gridPanel1" class="mini-panel" title="header" iconCls="icon-add" style="width:450px;height:250px;"
     showToolbar="true" showCloseButton="true" showHeader="false" bodyStyle="padding:0" borderStyle="border:0"
>
    <div property="toolbar" style="padding:5px;padding-left:8px;text-align:center;">
        <div style="float:left;padding-bottom:2px;">
            <a class="mini-button" onclick="onSearchClick(1)">查询</a>
            <a class="mini-button" onclick="onClearClick(1)">清除</a>
        </div>
        <div style="float:right;padding-bottom:2px;">
            <a class="mini-button" onclick="onCloseClick(1)">关闭</a>
        </div>
        <div style="clear:both;"></div>
    </div>
    <div id="datagrid1" class="mini-datagrid" style="width:100%;height:100%;"
         borderStyle="border:0" showPageSize="false" showPageIndex="false"
         url="/pipeinfo/getPipeNumber.action">
        <div property="columns">
            <div type="checkcolumn" ></div>
            <div field="pipe_no" width="60" headerAlign="center" allowSort="true">钢管编号</div>
        </div>
    </div>
</div>
<script type="text/javascript">
    mini.parse();


    var grid1=mini.get("datagrid1");
    grid1.load();

    var look1= mini.get("lookup1");
    function onSearchClick() {
        grid1.load();
    }
    function onCloseClick(e) {
        //var lookup2 = mini.get("lookup2");
        look1.hidePopup();
    }
    function onClearClick(e) {
        //var lookup2 = mini.get("lookup2");
        look1.deselectAll();
    }

    look1.on("showpopup",function(e){
        $('.mini-shadow').css('z-index','99999');
        $('.mini-popup').css('z-index','100000');
        $('.mini-panel').css('z-index','100000');
    });
</script>
<script type="text/javascript" src="../easyui/jquery.easyui.min.js"></script>
</body>
</html>
