<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>供应商信息</title>
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
    <style type="text/css" >
        .b3{border-style:inset;border-width:thin;}
    </style>


    <script type="text/javascript">



    </script>

</head>

<body>
<table>
    <tr>
        <td style="width:180px;height:30px;">
            <input id="lookup2" name="employee_no"  class="mini-lookup" style="width:180px;"
                   textField="employee_no" valueField="id" popupWidth="auto"
                   popup="#gridPanel" grid="#datagrid1" multiSelect="false"
            />
        </td>
    </tr>
</table>


<div id="gridPanel" class="mini-panel" title="header" iconCls="icon-add" style="width:350px;height:250px;"
     showToolbar="true" showCloseButton="true" showHeader="false" bodyStyle="padding:0" borderStyle="border:0"
>
    <div property="toolbar" style="padding:5px;padding-left:8px;text-align:center;">
        <div style="float:left;padding-bottom:2px;">
            <span>姓名：</span>
            <input id="keyText" class="mini-textbox" style="width:140px;" onenter="onSearchClick"/>
            <a class="mini-button" onclick="onSearchClick">查询</a>
            <a class="mini-button" onclick="onClearClick">清除</a>
        </div>
        <div style="float:right;padding-bottom:2px;">
            <a class="mini-button" onclick="onCloseClick">关闭</a>
        </div>
        <div style="clear:both;"></div>
    </div>
    <div id="datagrid1" class="mini-datagrid" style="width:100%;height:100%;"
         borderStyle="border:0" showPageSize="false" showPageIndex="false"
         url="/person/getPersonNoByName.action">
        <div property="columns">
            <div type="checkcolumn" ></div>
            <div field="pname" width="60" headerAlign="center" allowSort="true">姓名</div>
            <div field="employee_no" width="60" headerAlign="center" allowSort="true">编号</div>
        </div>
    </div>
</div>
<script type="text/javascript">
    mini.parse();

    var grid = mini.get("datagrid1");
    var keyText = mini.get("keyText");
    var grid1=mini.get("datagrid1");
    grid.load();

    var look= mini.get("lookup2");
    function onSearchClick(e) {
        grid1.load({
            key: keyText.value
        });
    }
    function onCloseClick(e) {
        //var lookup2 = mini.get("lookup2");
        look.hidePopup();
    }
    function onClearClick(e) {
        //var lookup2 = mini.get("lookup2");
        look.deselectAll();
    }

    look.on("showpopup",function(e){
        $('.mini-shadow').css('z-index','99999');
        $('.mini-popup').css('z-index','100000');
        $('.mini-panel').css('z-index','100000');
    });
</script>
<script type="text/javascript" src="../easyui/jquery.easyui.min.js"></script>
</body>
</html>
