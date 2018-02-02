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

    <link rel="stylesheet" type="text/css" href="easyui/themes/bootstrap/easyui.css">


    <link rel="stylesheet" type="text/css" href="easyui/themes/icon.css">

    <script type="text/javascript" src="easyui/jquery.min.js"></script>
    <script src="js/jquery.i18n.properties-1.0.9.js" type="text/javascript"></script>
    <script src="js/language.js" type="text/javascript"></script>
    <script src="js/common.js" type="text/javascript"></script>
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
            hlLanguage();
            $('#tt').tree({
                onClick:function(node){
                    var tab=$('#hlTab').tabs('getTab',node.text);
                    var xy=node.text;
                    if(tab){
                        //切换
                        $('#hlTab').tabs('select',node.text);
                    }else{
                        //添加新的选项卡
                        if("外喷砂岗位"==xy||"Od Blast Process"==xy){
                            $('#hlTab').tabs('add',{
                                title:node.text,
                                content:"<iframe scrolling='auto' frameborder='0'  src='od/odblastprocess.jsp' style='width:100%;height:100%;'></iframe>",
                                closable:true
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




        });

    </script>
</head>
<body class="easyui-layout">
<div data-options="region:'south',split:true" style="height:50px;">
    <div style="text-align: center"><h3>@2018 友元科技 版权所有</h3></div>
</div>
<div data-options="region:'north',split:true">
    <div style="float: right;padding:10px">
        <select id="language">
            <option value="zh-CN">中文</option>
            <option value="en">ENGLISH</option>
        </select>
    </div>
</div>
<div data-options="region:'west'" title="导航菜单" class="i18n" name="navigation" style="width:200px;">

    <div id="aa" class="easyui-accordion">
        <div title="外防腐" class="i18n" name="externalcoating"  style="padding:10px;">
            <ul id="tt">
                <li class="i18n1" name="odblastprocess">外喷砂岗位</li>
                <li class="i18n1" name="odblastinspection">外喷砂检验岗位</li>
                <li class="i18n1" name="odcoating">外涂岗位</li>
                <li class="i18n1" name="odcoatinginspection">外涂检验岗位</li>
                <li class="i18n1" name="odstencilprocess">外喷标岗位</li>
                <li class="i18n1" name="odfinalinspection">外涂层终检岗位</li>
            </ul>
        </div>
        <div title="内防腐" class="i18n" name="intenalcoating" style="padding:10px;">
            bb
        </div>
        <div title="出入库" class="i18n" name="storage" style="padding:10px;">
            cc
        </div>
        <div title="项目工艺" class="i18n" name="projectspecification" style="padding:10px;">
            dd
        </div>
        <div title="实验" class="i18n" name="labtesting" style="padding:10px;">
            ee
        </div>
        <div title="生产报表" class="i18n" name="productionreport" style="padding:10px;">
            ff
        </div>
        <div title="账户管理" class="i18n" name="accountmanagement" style="padding:10px;">
            <ul id="hlaccount">
                <li>账户管理</li>
            </ul>
        </div>
    </div>

</div>


<div data-options="region:'center'," style="padding:5px;">
    <div id="hlTab" class="easyui-tabs" data-options="fit:true">
        <div title="首页" class="i18n" name="home" style="padding:5px;display:none;" data-options="iconCls:'icon-lightbulb'">
            <img src="images/44.jpg" style="width:100%;height:100%">
        </div>
    </div>
</div>
<div id="hlTb">
    <a href="#" id="addLinkBtn" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">Add</a>
    <a href="#" id="editLinkBtn" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">Edit</a>
    <a href="#" id="deltLinkBtn" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">Delete</a>
</div>
<script type="text/javascript" src="easyui/jquery.easyui.min.js"></script>
</body>

</html>
