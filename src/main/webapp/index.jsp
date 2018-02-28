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
        .hltr{border-bottom:2px solid #1f1f1f ;}
        .b3{border-style:inset;border-width:thin;}
    </style>
    <script type="text/javascript">
        var url;
        $(function(){
            //hlLanguage("i18n/");
            $('#od').tree({
                onClick:function(node){
                    var tab=$('#hlTab').tabs('getTab',node.text);
                    var xy=node.text;
                    if(tab){
                        //切换
                        $('#hlTab').tabs('select',node.text);
                    }else{
                        //添加新的选项卡
                        if("外喷砂工序"==xy||"OD Blast Process"==xy){
                            $('#hlTab').tabs('add',{
                                title:node.text,
                                content:"<iframe scrolling='auto' frameborder='0'  src='od/odblastprocess.jsp' style='width:100%;height:100%;'></iframe>",
                                closable:true
                            });
                            hlLanguage();
                        }
                        else if("外喷砂检验工序"==xy||"OD Blast Inspection"==xy){
                            $('#hlTab').tabs('add',{
                                title:node.text,
                                content:"<iframe scrolling='auto' frameborder='0'  src='od/odblastinspectionprocess.jsp' style='width:100%;height:100%;'></iframe>",
                                closable:true
                            });
                        }else if("外涂工序(2FBE)"==xy||"OD Coating Process(2FBE)"==xy){
                            $('#hlTab').tabs('add',{
                                title:node.text,
                                content:"<iframe scrolling='auto' frameborder='0'  src='od/odcoatingprocess.jsp' style='width:100%;height:100%;'></iframe>",
                                closable:true
                            });
                        }else if("外涂工序(3LPE)"==xy||"OD Coating Process(3LPE)"==xy){
                            $('#hlTab').tabs('add',{
                                title:node.text,
                                content:"<iframe scrolling='auto' frameborder='0'  src='od/odcoating3lpeprocess.jsp' style='width:100%;height:100%;'></iframe>",
                                closable:true
                            });
                        }else if("外涂检验工序(2FBE)"==xy||"OD Coating Inspection(2FBE)"==xy){
                            $('#hlTab').tabs('add',{
                                title:node.text,
                                content:"<iframe scrolling='auto' frameborder='0'  src='od/odcoatinginspectionprocess.jsp' style='width:100%;height:100%;'></iframe>",
                                closable:true
                            });
                        }else if("外涂检验工序(3LPE)"==xy||"OD Coating Inspection(3LPE)"==xy){
                            $('#hlTab').tabs('add',{
                                title:node.text,
                                content:"<iframe scrolling='auto' frameborder='0'  src='od/odcoating3lpeinspectionprocess.jsp' style='width:100%;height:100%;'></iframe>",
                                closable:true
                            });
                        }else if("外喷标工序"==xy||"OD Stencil Process"==xy){
                            $('#hlTab').tabs('add',{
                                title:node.text,
                                content:"<iframe scrolling='auto' frameborder='0'  src='od/odstencilprocess.jsp' style='width:100%;height:100%;'></iframe>",
                                closable:true
                            });
                        }else if("外涂层终检工序"==xy||"OD Final Inspection"==xy){
                            $('#hlTab').tabs('add',{
                                title:node.text,
                                content:"<iframe scrolling='auto' frameborder='0'  src='od/odfinalinspectionprocess.jsp' style='width:100%;height:100%;'></iframe>",
                                closable:true
                            });
                        }
                    }
                }
            });

            //基础信息管理
            $("#hlbasicinfomanagement").tree({
                onClick:function (node) {
                    var tab=$('#hlTab').tabs('getTab',node.text);
                    var xy=node.text;
                    if(tab){
                        $('#hlTab').tabs('select',node.text);
                    }else{
                        if("项目管理"==xy||"Project Management"==xy){


                            $('#hlTab').tabs('add',{
                                title:node.text,
                                content:"<iframe scrolling='auto' frameborder='0'  src='project/projectManagement.jsp' style='width:100%;height:100%;'></iframe>",
                                closable:true
                            });
                            hlLanguage();

                        }
                        else if("合同管理"==xy||"Contract Management"==xy){


                            $('#hlTab').tabs('add',{
                                title:node.text,
                                content:"<iframe scrolling='auto' frameborder='0'  src='project/contractManagement.jsp' style='width:100%;height:100%;'></iframe>",
                                closable:true
                            });
                            hlLanguage();

                        }else if("钢管管理"==xy||"Pipe Management"==xy){


                            $('#hlTab').tabs('add',{
                                title:node.text,
                                content:"<iframe scrolling='auto' frameborder='0'  src='pipe/pipeManagement.jsp' style='width:100%;height:100%;'></iframe>",
                                closable:true
                            });
                            hlLanguage();

                        }else if("钢管录入"==xy||"Upload Pipe"==xy){


                            $('#hlTab').tabs('add',{
                                title:node.text,
                                content:"<iframe scrolling='auto' frameborder='0'  src='pipe/uploadPipe.jsp' style='width:100%;height:100%;'></iframe>",
                                closable:true
                            });
                            hlLanguage();

                        }

                    }
                }
            });

            $('#id').tree({
                onClick:function(node){
                    var tab=$('#hlTab').tabs('getTab',node.text);
                    var xy=node.text;
                    if(tab){
                        //切换
                        $('#hlTab').tabs('select',node.text);
                    }else{
                        //添加新的选项卡
                        if("内喷砂工序"==xy||"ID Blast Process"==xy){
                            $('#hlTab').tabs('add',{
                                title:node.text,
                                content:"<iframe scrolling='auto' frameborder='0'  src='id/idblastprocess.jsp' style='width:100%;height:100%;'></iframe>",
                                closable:true
                            });
                            hlLanguage();
                        }
                        else if("内喷砂检验工序"==xy||"ID Blast Inspection"==xy){
                            $('#hlTab').tabs('add',{
                                title:node.text,
                                content:"<iframe scrolling='auto' frameborder='0'  src='id/idblastinspectionprocess.jsp' style='width:100%;height:100%;'></iframe>",
                                closable:true
                            });
                        }else if("内涂工序"==xy||"ID Coating Process"==xy){
                            $('#hlTab').tabs('add',{
                                title:node.text,
                                content:"<iframe scrolling='auto' frameborder='0'  src='id/idcoatingprocess.jsp' style='width:100%;height:100%;'></iframe>",
                                closable:true
                            });
                        }else if("内涂检验工序"==xy||"ID Coating Inspection"==xy){
                            $('#hlTab').tabs('add',{
                                title:node.text,
                                content:"<iframe scrolling='auto' frameborder='0'  src='id/idcoatinginspectionprocess.jsp' style='width:100%;height:100%;'></iframe>",
                                closable:true
                            });
                        }else if("内喷标工序"==xy||"ID Stencil Process"==xy){
                            $('#hlTab').tabs('add',{
                                title:node.text,
                                content:"<iframe scrolling='auto' frameborder='0'  src='id/idstencilprocess.jsp' style='width:100%;height:100%;'></iframe>",
                                closable:true
                            });
                        }else if("内涂层终检工序"==xy||"ID Final Inspection"==xy){
                            $('#hlTab').tabs('add',{
                                title:node.text,
                                content:"<iframe scrolling='auto' frameborder='0'  src='id/idfinalinspectionprocess.jsp' style='width:100%;height:100%;'></iframe>",
                                closable:true
                            });
                        }
                    }
                }
            });

            //涂层修补
            $("#hlcoatingrepair").tree({
                onClick: function (node) {
                    var tab = $('#hlTab').tabs('getTab', node.text);
                    var nodeTxt = node.text;
                    if (tab) {
                        $('#hlTab').tabs('select', node.text);
                    } else {
                        if ("外涂层修补" == nodeTxt || "OD Repair" == nodeTxt) {

                            $('#hlTab').tabs('add', {
                                title: node.text,
                                content: "<iframe scrolling='auto' frameborder='0'  src='coatingrepair/odrepair.jsp' style='width:100%;height:100%;'></iframe>",
                                closable: true
                            });
                            hlLanguage();
                        }
                        else if ("内涂层修补" == nodeTxt || "ID Repair" == nodeTxt) {

                            $('#hlTab').tabs('add', {
                                title: node.text,
                                content: "<iframe scrolling='auto' frameborder='0'  src='coatingrepair/idrepair.jsp' style='width:100%;height:100%;'></iframe>",
                                closable: true
                            });
                            hlLanguage();
                        }
                    }
                }
            });

            //出入库管理
            $("#hlstoragemanagement").tree({
                onClick:function (node) {
                    var tab=$('#hlTab').tabs('getTab',node.text);
                    var nodeTxt=node.text;
                    if(tab){
                        $('#hlTab').tabs('select',node.text);
                    }else{
                        if("外涂管成品入库"==nodeTxt||"OD Product Stock In"==nodeTxt){

                            $('#hlTab').tabs('add',{
                                title:node.text,
                                content:"<iframe scrolling='auto' frameborder='0'  src='storage/odstockin.jsp' style='width:100%;height:100%;'></iframe>",
                                closable:true
                            });
                            hlLanguage();
                        }
                        else if("内涂管成品入库"==nodeTxt||"ID Product Stock In"==nodeTxt){

                            $('#hlTab').tabs('add',{
                                title:node.text,
                                content:"<iframe scrolling='auto' frameborder='0'  src='storage/idstockin.jsp' style='width:100%;height:100%;'></iframe>",
                                closable:true
                            });
                            hlLanguage();
                        }
                        else if("光管调拨"==nodeTxt||"Bare Pipe Movement"==nodeTxt){

                            $('#hlTab').tabs('add',{
                                title:node.text,
                                content:"<iframe scrolling='auto' frameborder='0'  src='storage/barepipemovement.jsp' style='width:100%;height:100%;'></iframe>",
                                closable:true
                            });
                            hlLanguage();
                        }
                        else if("成品出厂"==nodeTxt||"Coating Product Stock Out"==nodeTxt){

                            $('#hlTab').tabs('add',{
                                title:node.text,
                                content:"<iframe scrolling='auto' frameborder='0'  src='storage/productStockout.jsp' style='width:100%;height:100%;'></iframe>",
                                closable:true
                            });
                            hlLanguage();
                        }
                    }
                }
            });

            //生产工艺
            //账户管理
            $("#hlprocess").tree({
                onClick:function (node) {
                    var tab=$('#hlTab').tabs('getTab',node.text);
                    var nodeTxt=node.text;
                    if(tab){
                        $('#hlTab').tabs('select',node.text);
                    }else{
                        if("外防腐标准"==nodeTxt||"Od Standard"==nodeTxt){
                            $('#hlTab').tabs('add',{
                                title:node.text,
                                content:"<iframe scrolling='auto' frameborder='0'  src='production/odstandard.jsp' style='width:100%;height:100%;'></iframe>",
                                closable:true
                            });
                            hlLanguage();
                        }else if("内防腐标准"==nodeTxt||"Id Standard"==nodeTxt){
                            $('#hlTab').tabs('add',{
                                title:node.text,
                                content:"<iframe scrolling='auto' frameborder='0'  src='production/idstandard.jsp' style='width:100%;height:100%;'></iframe>",
                                closable:true
                            });
                            hlLanguage();
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
                        $('#hlTab').tabs('select',node.text);
                    }else{
                        if("人员管理"==nodeTxt||"Person Management"==nodeTxt){

                            $('#hlTab').tabs('add',{
                                title:node.text,
                                content:"<iframe scrolling='auto' frameborder='0'  src='account/person.jsp' style='width:100%;height:100%;'></iframe>",
                                closable:true
                            });
                            hlLanguage();
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
            <ul id="od">
                <li class="i18n1" name="odblastprocess">外喷砂工序</li>
                <li class="i18n1" name="odblastinspection">外喷砂检验工序</li>
                <li class="i18n1" name="odcoating2fbe">外涂工序(2FBE)</li>
                <li class="i18n1" name="odcoating3lpe">外涂工序(3LPE)</li>
                <li class="i18n1" name="odcoating2fbeinspection">外涂检验工序(2FBE)</li>
                <li class="i18n1" name="odcoating3lpeinspection">外涂检验工序(3LPE)</li>
                <li class="i18n1" name="odstencilprocess">外喷标工序</li>
                <li class="i18n1" name="odfinalinspection">外涂层终检工序</li>
            </ul>
        </div>
        <div title="内防腐" class="i18n" name="intenalcoating" style="padding:10px;">
            <ul id="id">
                <li class="i18n1" name="idblastprocess">内喷砂工序</li>
                <li class="i18n1" name="idblastinspection">内喷砂检验工序</li>
                <li class="i18n1" name="idcoating">内涂工序</li>
                <li class="i18n1" name="idcoatinginspection">内涂检验工序</li>
                <li class="i18n1" name="idstencilprocess">内喷标工序</li>
                <li class="i18n1" name="idfinalinspection">内涂层终检工序</li>
            </ul>
        </div>
        <div title="出入库" class="i18n" name="storage" style="padding:10px;">
            <ul id="hlstoragemanagement">
            <li class="i18n1" name="odstockin">外涂成品入库</li>
            <li class="i18n1" name="idstockin">内涂成品入库</li>
            <li class="i18n1" name="baremovement">光管调拨</li>
            <li class="i18n1" name="productstockout">成品出厂</li>
            </ul>

        </div>
        <div title="涂层修补" class="i18n" name="coatingrepair" style="padding:10px;">
            <ul id="hlcoatingrepair">
                <li class="i18n1" name="odrepair">外防修补</li>
                <li class="i18n1" name="idrepair">内防修补</li>
            </ul>
        </div>
        <div title="基础信息管理" class="i18n" name="basicinfomanagement" style="padding:10px;">
            <ul id="hlbasicinfomanagement">
                <li class="i18n1" name="projectmanagement">项目管理</li>
                <li class="i18n1" name="contractmanagement">合同管理</li>
                <li class="i18n1" name="pipemanagement">钢管管理</li>
                <li class="i18n1" name="uploadpipe">钢管录入</li>
            </ul>
        </div>
        <div title="生产工艺" class="i18n" name="productionprocess" style="padding:10px;">
            <ul id="hlprocess">
                <li class="i18n1" name="odstandard">外防腐标准</li>
                <li class="i18n1" name="instandard">内防腐标准</li>
            </ul>
        </div>
        <div title="实验" class="i18n" name="labtesting" style="padding:10px;">
            ee
        </div>
        <div title="生产报表" class="i18n" name="productionreport" style="padding:10px;">
            ff
        </div>
        <div title="账户管理" class="i18n" name="accountmanagement" style="padding:10px;">
            <ul id="hlaccount">
                <li class="i18n1" name="personmanagement">账户管理</li>

            </ul>
        </div>

    </div>

</div>


<div data-options="region:'center'," style="padding:5px;">
    <div id="hlTab" class="easyui-tabs" data-options="fit:true">
        <div title="首页" class="i18n" name="home" style="padding:5px;display:none;" data-options="iconCls:'icon-lightbulb'">
            <%--<img src="images/44.jpg" style="width:100%;height:100%">--%>
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
<script type="text/javascript">
    hlLanguage();
</script>