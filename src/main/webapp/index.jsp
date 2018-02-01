<%@ page import="java.util.ResourceBundle" %><%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2017/11/13 0013
  Time: 19:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Insert title here</title>
    <!-- 1    jQuery的js包 -->
    <script type="text/javascript" src="easyui/jquery.min.js"></script>

    <!-- 2    css资源 -->
    <link rel="stylesheet" type="text/css" href="easyui/themes/bootstrap/easyui.css">

    <!-- 3    图标资源 -->
    <link rel="stylesheet" type="text/css" href="easyui/themes/icon.css">

    <!-- 4    EasyUI的js包 -->
    <script type="text/javascript" src="easyui/jquery.easyui.min.js"></script>

    <!-- 5    本地语言 -->
    <script type="text/javascript" src="easyui/locale/easyui-lang-zh_CN.js"></script>
    <%--<script type="text/javascript" src="js/jquery.localize.min.js"></script>--%>
    <style type="text/css" >
        .ht-table,.ht-table td{border-collapse:collapse;border:1px solid #F0F0F0;}
        .ht-table{width:100%;margin-bottom:10px;}
        .ht-table tr td:nth-child(1){width:180px;padding:5px;}
        .ht-table tr td:nth-child(2){width:280px;padding:2px;}
        .ht-table tr td:nth-child(3){width:140px;padding:5px;}
        .ht-table tr td input{width:280px;height:35px;}
        .hltr{border-bottom:2px solid #1f1f1f ;}
        .b3{border-style:inset;border-width:thin;}
    </style>
    <script type="text/javascript">
        var url;
        $(function(){
            $('#tt').tree({
                url:'tree_data.json',
                onClick:function(node){

                    var tab=$('#hlTab').tabs('getTab',node.text);
                    var xy=node.text;
                    if(tab){
                        //切换
                        $('#hlTab').tabs('select',node.text);
                    }else{
                        //添加新的选项卡
                        if("外喷砂岗位"==xy){
                            $('#hlTab').tabs('add',{
                                title:node.text,
                                href:'odblastprocess.jsp',
                                closable:true,
                                onLoad:function(){
                                    $('#odBlastProDatagrids').datagrid({
                                        striped:true,
                                        loadMsg:'正在加载中。。。',
                                        pagination:true,
                                        textField:'text',
                                        rownumbers:true,
                                        pageList:[10,20,30,50,100],
                                        pageSize:20,
                                        fitColumns:true,
                                        url:'/OdOperation/getOdBlastByLike.action',
                                        toolbar:'#hlOdBlastProTb',
                                        columns:[[
                                            { field: '',checkbox:true},
                                            { title: '流水号',field: 'id',width:100},
                                            { title: '钢管编号',field: 'pipe_no',width:100},
                                            { title: '操作时间',field : 'operation_time', formatter:function(value,row,index){
                                                    var operation_time = new Date(value);
                                                     return operation_time.toLocaleString();
                                                 } ,width:200},
                                            { title: '操作工编号',field: 'operator_no',width:100},
                                            { title: '外观缺陷',field: 'surface_condition',width:100},
                                            { title: '打砂前盐度',field: 'salt_contamination_before_blasting',width:100},
                                            { title: '碱洗时间',field: 'alkaline_dwell_time',width:100},
                                            { title: '碱浓度',field: 'alkaline_concentration',width: 100},
                                            { title: '传导性',field: 'conductivity',width:120},
                                            { title: '酸洗时间',field: 'acid_wash_time',width:120},
                                            { title: '酸浓度',field: 'acid_concentration',width:120},
                                            { title: '打砂传送速度',field: 'blast_line_speed',width:120},
                                            { title: '预热温度',field: 'preheat_temp',width:120},
                                            { title: '备注',field: 'remark',width:120}
                                        ]]
                                    });
                                }
                            });
                        }
                        else if("外喷砂检验岗位"==xy){
                            $('#hlTab').tabs('add',{
                                title:node.text,
                                href:'main.jsp',
                                closable:true,
                                onLoad:function(){

                                    $('#dgg').datagrid({
                                        url:'datagrid_data.json',
                                        columns:[[
                                            {field:'code',title:'代码',width:100},
                                            {field:'name',title:'名称',width:100},
                                            {field:'price',title:'价格',width:100,align:'right'}
                                        ]],
                                        striped:true,
                                        loadMsg:'正在拼命加载中...',
                                        pagination:true,
                                        singleSelect:true,
                                        rownumbers:true
                                    });
                                }

                            })
                        }
                    }
                }
            });
            //账户管理
            $("#hlaccount").tree({
                onClick:function (node) {
                    var tab=$('#hlTab').tabs('getTab',node.text);
                    var nodeTxt=node.text;
                    if(tab){
                        $('hlTab').tabs('select',node.text);
                    }else{
                        if("账户管理"==nodeTxt){
                            $('#hlTab').tabs('add',{
                                title:nodeTxt,
                                href:'person.jsp',
                                closable:true,
                                onLoad:function () {
                                    $('#personDatagrids').datagrid({
                                        url:'/person/getPersonByPage.action',
                                        striped:true,
                                        loadMsg:'加载中...',
                                        pagination:true,
                                        singleSelect:true,
                                        pageNumber:1,
                                        rownumbers:true,
                                        fitColumns:true,
                                        pageList:[10,20,50,100],
                                        pageSize:10,
                                        toolbar:'#hlOdBlastProTb',
                                        columns:[[
                                            {field:'id',title:'编号',width:100},
                                            {field:'pname',title:'姓名',width:100},
                                            {field:'page',title:'年龄',width:100}
                                        ]]
                                    })
                                }
                            })
                        }
                    }
                }
            });

            addHlFun();editHlFun();deltHlFun();


            //初始化语言
            // var uulanguage = (navigator.language || navigator.browserLanguage).toLowerCase();
            // if (uulanguage.indexOf("en") > -1) {
            //     $("[data-localize]").localize("text", {pathPrefix: "lang", language: "en"});
            // }else if (uulanguage.indexOf("zh") > -1) {
            //     $("[data-localize]").localize("text", {pathPrefix: "lang", language: "zh"});
            // }else{
            //     $("[data-localize]").localize("text", {pathPrefix: "lang", language: "en"});
            // };
            // if (getCookie(name) != "") {
            //     if (getCookie(name) == "zh") {
            //         $("[data-localize]").localize("text", {pathPrefix: "lang", language: "zh"});
            //     }
            //     if (getCookie(name) == "en") {
            //         $("[data-localize]").localize("text", {pathPrefix: "lang", language: "en"});
            //     }
            // }

        });
        //增删改
        //-------添加数据
        function addHlFun() {
            $('#addLinkBtn').click(function () {
                var tabTitle= $('.tabs-selected').text();
                if("账户管理"==tabTitle){
                   addPerson();
                }else{
                    alert(tabTitle);
                }
            });
        }
        //-------修改数据
        function editHlFun() {
            $('#editLinkBtn').click(function () {
                var tabTitle= $('.tabs-selected').text();
            });
        }
        //-------删除数据
        function deltHlFun() {
            $('#deltLinkBtn').click(function () {
                var tabTitle= $('.tabs-selected').text();
            });
        }
        //------外喷砂增删改查
        function addOdBlastPro(){
            $('#hlOdBlastProDialog').dialog('open').dialog('setTitle','新增');
            $('#odBlastProForm').form('clear');
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
            var row = $('#odBlastProDatagrids').datagrid('getSelected');
            if(row){
                $('#hlOdBlastProDialog').dialog('open').dialog('setTitle','修改');
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
                        $('#odBlastProDatagrids').datagrid('reload');    // reload the user data
                    } else {
                        hlAlertFour("操作失败!");
                    }
                },
                error:function () {
                    hlAlertThree();
                }

            });
        }
        //------公共函数
        function  hlAlertOne() {
            $.messager.alert('Warning','请选择要删除的行!');
        }
        function hlAlertTwo() {
            $.messager.alert('Warning','请选择要修改的行!');
        }
        function hlAlertThree() {
            $.messager.alert('Warning','系统繁忙!');
        }
        function hlAlertFour(txt) {
            $.messager.alert('Warning',txt);
        }
        function hlAlertFive(url,hlparam,total) {
            $.messager.confirm('系统提示',"您确定要删除这<font color=red>"+total+ "</font>条数据吗？",function (r) {
                if(r){
                    $.post(url,{"hlparam":hlparam},function (data) {
                        if(data.success){
                            $("#odBlastProDatagrids").datagrid("reload");
                        }else{
                            hlAlertFour("操作失败!");
                        }
                    },"json");
                }
            });
        }

        //语言设置
        var name = "somoveLanguage";
        function  changeLang() {
            var value = $("#ddlSomoveLanguage").children('option:selected').val();
            SetCookie(name,value);
            location.reload();
        }
        function  SetCookie(name,value){
            var Days = 30; //此 cookie 将被保存 30 天
            var exp = new Date();    //new Date("December 31, 9998");
            exp.setTime(exp.getTime() + Days*24*60*60*1000);
            document.cookie = name + "="+ escape (value) + ";expires=" + exp.toGMTString();
        }
        function getCookie(name)//取cookies函数
        {
            var arr = document.cookie.match(new RegExp("(^| )"+name+"=([^;]*)(;|$)"));
            if(arr != null) return unescape(arr[2]); return null;
        }

    </script>
</head>

<body class="easyui-layout">
<div data-options="region:'south',split:true" style="height:50px;">
    <div style="text-align: center"><h3>@2013 Wu All RightsReserved</h3></div>
</div>
<div data-options="region:'north',split:true">
    <div style="float: right;padding:10px">
        <select id="ddlSomoveLanguage" onchange="changeLang();">
            <option value="zh">中文</option>
            <option value="en">ENGLISH</option>
        </select>
    </div>
</div>
<div data-options="region:'west',title:'导航菜单'" style="width:200px;">

    <div id="aa" class="easyui-accordion">
        <div title="外防腐" data-localize="nav.odanti" style="padding:10px;">
            <ul id="tt"></ul>
        </div>
        <div title="内防腐" data-localize="nav.inanti" style="padding:10px;">
            bb
        </div>
        <div title="出入库" data-localize="nav.outstrage" style="padding:10px;">
            cc
        </div>
        <div title="项目工艺" data-localize="nav.protech" style="padding:10px;">
            dd
        </div>
        <div title="实验" data-localize="nav.experiment" style="padding:10px;">
            ee
        </div>
        <div title="生产报表" data-localize="nav.report" style="padding:10px;">
            ff
        </div>
        <div title="账户管理" data-localize="nav.account" style="padding:10px;">
            <ul id="hlaccount">
                <li>账户管理</li>
            </ul>
        </div>
    </div>

</div>


<div data-options="region:'center'," style="padding:5px;">
    <div id="hlTab" class="easyui-tabs" data-options="fit:true">
        <div title="首页" style="padding:5px;display:none;" data-options="iconCls:'icon-lightbulb'">
            <img src="images/44.jpg" style="width:100%;height:100%">
        </div>
    </div>
</div>
<div id="hlTb">
    <a href="#" id="addLinkBtn" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">Add</a>
    <a href="#" id="editLinkBtn" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">Edit</a>
    <a href="#" id="deltLinkBtn" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">Delete</a>
</div>

</body>

</html>
