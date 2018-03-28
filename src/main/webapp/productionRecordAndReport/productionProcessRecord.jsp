<%--
  Created by IntelliJ IDEA.
  User: kurt
  Date: 3/26/18
  Time: 1:49 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" type="text/css" href="../easyui/themes/bootstrap/easyui.css">
    <link rel="stylesheet" type="text/css" href="../easyui/themes/icon.css">
    <link href="../miniui/multiupload/multiupload.css" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" href="../css/common.css"/>
    <script src="../easyui/jquery.min.js" type="text/javascript"></script>
    <script src="../js/common.js" type="text/javascript"></script>
    <script src="../miniui/boot.js" type="text/javascript"></script>
    <script src="../miniui/fileupload/swfupload/swfupload.js" type="text/javascript"></script>
    <script src="../miniui/multiupload/multiupload.js" type="text/javascript"></script>
    <script  src="../js/lrscroll.js" type="text/javascript"></script>
    <script src="../js/jquery.i18n.properties-1.0.9.js" type="text/javascript"></script>
    <script src="../js/language.js" type="text/javascript"></script>

    <style type="text/css">
        .datagrid-mask {
            position: absolute;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            opacity: 0.5;
            filter: alpha(opacity=50);
            background-color:#000000;
            display: none;
        }

        .datagrid-mask-msg {
            position: absolute;
            top: 50%;
            margin-top: -20px;
            padding: 12px 5px 10px 30px;
            width: auto;
            height: 16px;
            border-width: 2px;
            border-style: solid;
            display: none;
        }
    </style>
    <script type="text/javascript">
        $(function() {
            $('.btnReport').click(function () {
                var selectValue=$('#cc').select();
                $.ajax({
                    type:"post",
                    url:"",
                    data:{},
                    async:false,
                    beforeSend:function(){
                        ajaxLoading();
                    },
                    success:function(){
                        ajaxLoadEnd();
                    },
                    error:function(){
                        ajaxLoadEnd();
                    },
                    complete:function(){
                        ajaxLoadEnd();
                    }
                });
            });
        });

        function ajaxLoading() {
            $("<div class=\"datagrid-mask\"></div>").css({
                display: "block",
                width: "100%",
                height: $(window).height()
            }).appendTo("body");
            $("<div class=\"datagrid-mask-msg\"></div>").html("正在处理，请稍候。。。").appendTo("body").css({
                display: "block",
                left: ($(document.body).outerWidth(true) - 190) / 2,
                top: ($(window).height() - 45) / 2
            });
        }

        function ajaxLoadEnd() {
            $(".datagrid-mask").remove();
            $(".datagrid-mask-msg").remove();
        }
    </script>
</head>
<body>
<div style="padding:10px">
    <select id="cc" class="easyui-combobox" data-options="editable:false" name="result" style="width:200px;">
        <option value="0">外喷砂工序</option>
        <option value="1">外喷砂检验工序</option>
        <option value="2">外涂工序(2FBE)</option>
        <option value="3">外涂检验工序(2FBE)</option>
        <option value="4">外涂工序(3LPE)</option>
        <option value="5">外涂检验工序(3LPE)</option>
        <option value="6">外喷标工序</option>
        <option value="7">外涂层终检工序</option>
        <option value="8">内喷砂工序</option>
        <option value="9">内喷砂检验工序</option>
        <option value="10">内涂工序</option>
        <option value="11">内涂检验工序</option>
        <option value="12">内喷标工序</option>
        <option value="13">内涂层终检工序</option>
    </select>
    <span class="i18n1" name="begintime">开始时间</span>:
    <input id="begintime" name="begintime" type="text" class="easyui-datebox" data-options="formatter:myformatter,parser:myparser">
    <span class="i18n1" name="endtime">结束时间</span>:
    <input id="endtime" name="endtime" type="text" class="easyui-datebox" data-options="formatter:myformatter,parser:myparser">
    <button class="btnReport">开始生成</button>

</div>
<script type="text/javascript" src="../easyui/jquery.easyui.min.js"></script>
</body>
</html>
