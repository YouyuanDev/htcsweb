<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>供应商信息</title>
    <link rel="stylesheet" type="text/css" href="../easyui/themes/metro/easyui.css" charset="UTF-8">
    <link rel="stylesheet" type="text/css" href="../easyui/themes/icon.css" charset="UTF-8">
    <script type="text/javascript" src="../easyui/jquery.min.js" charset="UTF-8"></script>
    <script type="text/javascript" src="../easyui/jquery.easyui.min.js" charset="UTF-8"></script>
    <script type="text/javascript" src="../easyui/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
     <script type="text/javascript" src="../js/jquery.form.js"></script>
    <script src="../miniui/boot.js" type="text/javascript"></script>
    <style type="text/css" >
        .b3{border-style:inset;border-width:thin;}
    </style>


    <script type="text/javascript">


    </script>

</head>

<body>
<input id="lookup2" name="look" class="mini-lookup" style="width:200px;" textField="name" valueField="id" popupWidth="auto" popup="#gridPanel" grid="#datagrid1" multiSelect="true" value="5e9d1625-d14e-49f2-b618-efcaadeeca71" text="abc" />

<div id="gridPanel" class="mini-panel" title="header" iconCls="icon-add" style="width:450px;height:250px;" showToolbar="true" showCloseButton="true" showHeader="false" bodyStyle="padding:0" borderStyle="border:0">
    <div property="toolbar" style="padding:5px;padding-left:8px;text-align:center;">
        <div style="float:left;padding-bottom:2px;">
            <span>姓名：</span>
            <input id="keyText" class="mini-textbox" style="width:160px;" onenter="onSearchClick" />
            <a class="mini-button" onclick="onSearchClick">查询</a>
            <a class="mini-button" onclick="onClearClick">清除</a>
        </div>
        <div style="float:right;padding-bottom:2px;">
            <a class="mini-button" onclick="onCloseClick">关闭</a>
        </div>
        <div style="clear:both;"></div>
    </div>
    <div id="datagrid1" class="mini-datagrid" style="width:100%;height:100%;" borderStyle="border:0" showPageSize="false" showPageIndex="false" url="/person/getPersonByLike.action">
        <div property="columns">
            <div type="checkcolumn"></div>
            <div field="loginname" width="120" headerAlign="center" allowSort="true">员工帐号</div>
            <div field="name" width="120" headerAlign="center" allowSort="true">姓名</div>
            <div field="createtime" width="100" headerAlign="center" dateFormat="yyyy-MM-dd" allowSort="true">创建日期</div>
        </div>
    </div>
</div>
<script type="text/javascript">
    mini.parse();

    var grid = mini.get("datagrid1");
    var keyText = mini.get("keyText");

    grid.load();

    function onSearchClick(e) {
        grid.load({
            key: keyText.value
        });
    }

    function onCloseClick(e) {
        var lookup2 = mini.get("lookup2");
        lookup2.hidePopup();
    }

    function onClearClick(e) {
        var lookup2 = mini.get("lookup2");
        lookup2.deselectAll();
    }
</script>
</body>
</html>
