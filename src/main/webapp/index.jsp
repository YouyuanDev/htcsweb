<%@ page import="java.util.ResourceBundle" %>
<%@ page import="java.util.HashMap" %><%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2017/11/13 0013
  Time: 19:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String bPath =request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>钢管涂层生产控制信息系统 主页</title>

    <link rel="stylesheet" type="text/css" href="easyui/themes/bootstrap/easyui.css">


    <link rel="stylesheet" type="text/css" href="easyui/themes/icon.css">

    <script type="text/javascript" src="easyui/jquery.min.js"></script>
    <script src="js/jquery.i18n.properties-1.0.9.js" type="text/javascript"></script>
    <script src="js/language.js" type="text/javascript"></script>
    <script src="js/common.js" type="text/javascript"></script>
    <script src="../js/jquery.form.js" type="text/javascript"></script>
    <style type="text/css" >
        .ht-table,.ht-table td{border-collapse:collapse;border:1px solid #F0F0F0;}
        .ht-table{width:100%;margin-bottom:10px;}
        .hltr{border-bottom:2px solid #1f1f1f ;}
        .b3{border-style:inset;border-width:thin;}

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
            min-height:30px;
            border-width: 2px;
            border-style: solid;
            display: none;
        }


    </style>



    <script type="text/javascript">
        var url;
        $(function(){

            var uriArr=["odblastprocess","odblastinspectionprocess","odcoatingprocess","odcoatinginspectionprocess",
                "odcoating3lpeprocess","odcoating3lpeinspectionprocess","odstencilprocess","odfinalinspectionprocess",
                "idblastprocess","idblastinspectionprocess","idcoatingprocess","idcoatinginspectionprocess","idstencilprocess","idfinalinspectionprocess",
                "odstockin","idstockin","barepipemovement","productStockout",
                "coatingrepair","coatingstrip",
                "barepipegrindingProcess","pipeSamplingProcess","pipeRebevelProcess",
                "projectManagement","contractManagement","pipeManagement","uploadPipe","twodimensionalcode",
                "odstandard","idstandard","labtestingstandard2fbe","labtestingstandard3lpe","rawmaterialtestingstandard2fbe","rawmaterialtestingstandard3lpe","pipebodystandard",
                "labtesting2fbe","labtesting3lpe","labtestingepoxy","rawmaterialtesting2fbe","rawmaterialtesting3lpe","rawmaterialtestingliquidepoxy",
                "person","role","function",
                "productionProcessRecord","dailyProductionReport"];
            var odArr=uriArr.slice(0,8);
            var idArr=uriArr.slice(8,14);
            var outinArr=uriArr.slice(14,18);
            var repairArr=uriArr.slice(18,20);
            var pipeArr=uriArr.slice(20,23);
            var basicArr=uriArr.slice(23,28);
            var standArr=uriArr.slice(28,35);
            var labArr=uriArr.slice(35,41);
            var accountArr=uriArr.slice(41,44);
            var reportArr=uriArr.slice(44,46);



            var hsMapList="<%=session.getAttribute("userfunctionMap")%>";
            var funArr;
            if(hsMapList!=null&&hsMapList!=""&&hsMapList.length>0){
                 var reg=new RegExp('=1',"g")
                 hsMapList=hsMapList.replace(reg,"");
                 funArr=hsMapList.substring(1,hsMapList.length-1).split(',');
            }

            var tempNameArr=new Array();//得到的是比对uri中新的权限数组
            for(var i=0;i<funArr.length;i++){
                if($.inArray(funArr[i].trim(),uriArr)!=-1){
                    tempNameArr.push(funArr[i].trim());
                }
            }
            var finalNameArr=new Array();
            $.each(uriArr,function (index,element) {
                if($.inArray(element,tempNameArr)!=-1){
                    finalNameArr.push(element);
                }
            });

            if(finalNameArr.length>0){
                var odDiv='<div title=\"外防腐\" class=\"i18n\" name=\"externalcoating\"  style=\"padding:10px;\"><ul id=\"od\">';
                var odDivSon="";
                var idDiv='<div title=\"内防腐\" class=\"i18n\" name=\"intenalcoating\"  style=\"padding:10px;\"><ul id=\"id\">';
                var idDivSon="";
                var outinDiv='<div title=\"出入库\" class=\"i18n\" name=\"storage\"  style=\"padding:10px;\"><ul id=\"hlstoragemanagement\">';
                var outinDivSon="";
                var repairDiv='<div title=\"涂层修补与扒皮\" class=\"i18n\" name=\"coatingrepairandstrip\"  style=\"padding:10px;\"><ul id=\"hlcoatingrepairstrip\">';
                var repairDivSon="";
                var pipeDiv='<div title=\"钢管修磨切割与倒棱\" class=\"i18n\" name=\"grindingcutoffrebevel\"  style=\"padding:10px;\"><ul id=\"hlgrindingcutoffrebevel\">';
                var pipeDivSon="";
                var basicDiv='<div title=\"基础信息管理\" class=\"i18n\" name=\"basicinfomanagement\"  style=\"padding:10px;\"><ul id=\"hlbasicinfomanagement\">';
                var basicDivSon="";
                var standDiv='<div title=\"生产工艺\" class=\"i18n\" name=\"productionprocess\"  style=\"padding:10px;\"><ul id=\"hlprocess\">';
                var standDivSon="";
                var labDiv='<div title=\"实验\" class=\"i18n\" name=\"labtesting\"  style=\"padding:10px;\"><ul id=\"hltest\">';
                var labDivSon="";
                var reportDiv='<div title=\"生产报表\" class=\"i18n\" name=\"productionreport\"  style=\"padding:10px;\"><ul id=\"hlprodcutionreport\">';
                var reportDivSon="";
                var accountDiv='<div title=\"账户管理\" class=\"i18n\" name=\"accountmanagement\"  style=\"padding:10px;\"><ul id=\"hlaccount\">';
                var accountDivSon="";

                var endDiv="</ul></div>";
                //外喷砂

                $.each(finalNameArr,function (index,element) {
                    if($.inArray(element,odArr)!=-1){
                        odDivSon+=MakeMenus(element);
                        return true;
                    }
                    if($.inArray(element,idArr)!=-1){
                        idDivSon+=MakeMenus(element);
                        return true;
                    }
                    if($.inArray(element,outinArr)!=-1){
                        outinDivSon+=MakeMenus(element);
                        return true;
                    }
                    if($.inArray(element,repairArr)!=-1){
                        repairDivSon+=MakeMenus(element);
                        return true;
                    }
                    if($.inArray(element,pipeArr)!=-1){
                        pipeDivSon+=MakeMenus(element);
                        return true;
                    }
                    if($.inArray(element,basicArr)!=-1){
                        basicDivSon+=MakeMenus(element);
                        return true;
                    }
                    if($.inArray(element,standArr)!=-1){
                        standDivSon+=MakeMenus(element);
                        return true;
                    }
                    if($.inArray(element,labArr)!=-1){
                        labDivSon+=MakeMenus(element);
                        return true;
                    }
                    if($.inArray(element,reportArr)!=-1){
                        reportDivSon+=MakeMenus(element);
                        return true;
                    }
                    if($.inArray(element,accountArr)!=-1){
                        accountDivSon+=MakeMenus(element);
                        return true;
                    }
                });
                if(odDivSon!=""&&odDivSon.length>0){
                    odDiv+=odDivSon;
                    odDiv+=endDiv;
                    $('#aa').append(odDiv);
                }
                if(idDivSon!=""&&idDivSon.length>0){
                    idDiv+=idDivSon;
                    idDiv+=endDiv;
                    $('#aa').append(idDiv);
                }
                if(outinDivSon!=""&&outinDivSon.length>0){
                    outinDiv+=outinDivSon;
                    outinDiv+=endDiv;
                    $('#aa').append(outinDiv);
                }
                if(repairDivSon!=""&&repairDivSon.length>0){
                    repairDiv+=repairDivSon;
                    repairDiv+=endDiv;
                    $('#aa').append(repairDiv);
                }
                if(pipeDivSon!=""&&pipeDivSon.length>0){
                    pipeDiv+=pipeDivSon;
                    pipeDiv+=endDiv;
                    $('#aa').append(pipeDiv);
                }
                if(basicDivSon!=""&&basicDivSon.length>0){
                    basicDiv+=basicDivSon;
                    basicDiv+=endDiv;
                    $('#aa').append(basicDiv);
                }
                if(standDivSon!=""&&standDivSon.length>0){
                    standDiv+=standDivSon;
                    standDiv+=endDiv;
                    $('#aa').append(standDiv);
                }
                if(labDivSon!=""&&labDivSon.length>0){
                    labDiv+=labDivSon;
                    labDiv+=endDiv;
                    $('#aa').append(labDiv);
                }
                if(reportDivSon!=""&&reportDivSon.length>0){
                    reportDiv+=reportDivSon;
                    reportDiv+=endDiv;
                    $('#aa').append(reportDiv);
                }
                if(accountDivSon!=""&&accountDivSon.length>0){
                    accountDiv+=accountDivSon;
                    accountDiv+=endDiv;
                    $('#aa').append(accountDiv);
                }
                //内喷砂
            }
            // var odDiv='<div title=\"外防腐\" class=\"i18n\" name=\"externalcoating\"  style=\"padding:10px;\"><ul id=\"od\">';
            // var odDivSon="";
            // odDivSon+=MakeMenus("odblastprocess");
            // if(odDivSon.length>0){
            //     odDiv+=odDivSon;
            //     odDiv+='</ul></div>';
            //     $('#aa').append(odDiv);
            // }else{
            //     odDiv="";
            //}
            hlLanguage("i18n/");
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

                        }else if("二维码打印记录"==xy||"Two-dimensional Code Print Record"==xy){
                            $('#hlTab').tabs('add',{
                                title:node.text,
                                content:"<iframe scrolling='auto' frameborder='0'  src='pipe/twodimensionalcode.jsp' style='width:100%;height:100%;'></iframe>",
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

            //涂层修补与扒皮
            $("#hlcoatingrepairstrip").tree({
                onClick: function (node) {
                    var tab = $('#hlTab').tabs('getTab', node.text);
                    var nodeTxt = node.text;
                    if (tab) {
                        $('#hlTab').tabs('select', node.text);
                    } else {
                        if ("涂层修补" == nodeTxt || "Coating Repair" == nodeTxt) {

                            $('#hlTab').tabs('add', {
                                title: node.text,
                                content: "<iframe scrolling='auto' frameborder='0'  src='coatingrepair/coatingrepair.jsp' style='width:100%;height:100%;'></iframe>",
                                closable: true
                            });
                            hlLanguage();
                        }else if ("涂层扒皮" == nodeTxt || "Coating Strip" == nodeTxt) {

                            $('#hlTab').tabs('add', {
                                title: node.text,
                                content: "<iframe scrolling='auto' frameborder='0'  src='coatingstrip/coatingstrip.jsp' style='width:100%;height:100%;'></iframe>",
                                closable: true
                            });
                            hlLanguage();
                        }


                    }
                }
            });


            //钢管修磨、切割与倒棱
            $("#hlgrindingcutoffrebevel").tree({
                onClick: function (node) {
                    var tab = $('#hlTab').tabs('getTab', node.text);
                    var nodeTxt = node.text;
                    if (tab) {
                        $('#hlTab').tabs('select', node.text);
                    } else {

                        if ("光管修磨切割" == nodeTxt || "Bare Pipe Grinding Cut-off" == nodeTxt) {

                            $('#hlTab').tabs('add', {
                                title: node.text,
                                content: "<iframe scrolling='auto' frameborder='0'  src='grinding/barepipegrindingProcess.jsp' style='width:100%;height:100%;'></iframe>",
                                closable: true
                            });
                            hlLanguage();
                        }

                        else if ("钢管倒棱" == nodeTxt || "Pipe Rebevel" == nodeTxt) {

                            $('#hlTab').tabs('add', {
                                title: node.text,
                                content: "<iframe scrolling='auto' frameborder='0'  src='grinding/pipeRebevelProcess.jsp' style='width:100%;height:100%;'></iframe>",
                                closable: true
                            });
                            hlLanguage();
                        }

                        else if ("钢管外防取样" == nodeTxt || "Pipe Coating Sampling" == nodeTxt) {

                            $('#hlTab').tabs('add', {
                                title: node.text,
                                content: "<iframe scrolling='auto' frameborder='0'  src='sampling/pipeSamplingProcess.jsp' style='width:100%;height:100%;'></iframe>",
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
            $("#hlprocess").tree({
                onClick:function (node) {
                    var tab=$('#hlTab').tabs('getTab',node.text);
                    var nodeTxt=node.text;
                    if(tab){
                        $('#hlTab').tabs('select',node.text);
                    }else{
                        if("外防腐生产标准"==nodeTxt||"Od Standard"==nodeTxt){
                            $('#hlTab').tabs('add',{
                                title:node.text,
                                content:"<iframe scrolling='auto' frameborder='0'  src='production/odstandard.jsp' style='width:100%;height:100%;'></iframe>",
                                closable:true
                            });
                            hlLanguage();
                        }else if("内防腐生产标准"==nodeTxt||"Id Standard"==nodeTxt){
                            $('#hlTab').tabs('add',{
                                title:node.text,
                                content:"<iframe scrolling='auto' frameborder='0'  src='production/idstandard.jsp' style='width:100%;height:100%;'></iframe>",
                                closable:true
                            });
                            hlLanguage();
                        }else if("实验标准(2FBE)"==nodeTxt||"Testing Standard(2FBE)"==nodeTxt){
                            $('#hlTab').tabs('add',{
                                title:node.text,
                                content:"<iframe scrolling='auto' frameborder='0'  src='production/labtestingstandard2fbe.jsp' style='width:100%;height:100%;'></iframe>",
                                closable:true
                            });
                            hlLanguage();
                        }else if("实验标准(3LPE)"==nodeTxt||"Testing Standard(3LPE)"==nodeTxt){
                            $('#hlTab').tabs('add',{
                                title:node.text,
                                content:"<iframe scrolling='auto' frameborder='0'  src='production/labtestingstandard3lpe.jsp' style='width:100%;height:100%;'></iframe>",
                                closable:true
                            });
                            hlLanguage();
                        }else if("原材料标准(2FBE)"==nodeTxt||"Raw Material Standard(2FBE)"==nodeTxt){
                            $('#hlTab').tabs('add',{
                                title:node.text,
                                content:"<iframe scrolling='auto' frameborder='0'  src='production/rawmaterialtestingstandard2fbe.jsp' style='width:100%;height:100%;'></iframe>",
                                closable:true
                            });
                            hlLanguage();
                        }else if("原材料标准(3LPE)"==nodeTxt||"Raw Material Standard(3LPE)"==nodeTxt){
                            $('#hlTab').tabs('add',{
                                title:node.text,
                                content:"<iframe scrolling='auto' frameborder='0'  src='production/rawmaterialtestingstandard3lpe.jsp' style='width:100%;height:100%;'></iframe>",
                                closable:true
                            });
                            hlLanguage();
                        }else if("钢管管体标准"==nodeTxt||"Pipe Body Standard"==nodeTxt){
                            $('#hlTab').tabs('add',{
                                title:node.text,
                                content:"<iframe scrolling='auto' frameborder='0'  src='production/pipebodystandard.jsp' style='width:100%;height:100%;'></iframe>",
                                closable:true
                            });
                            hlLanguage();
                        }
                    }
                }
            });
            //实验
            $("#hltest").tree({
                onClick:function (node) {
                    var tab=$('#hlTab').tabs('getTab',node.text);
                    var nodeTxt=node.text;
                    if(tab){
                        $('#hlTab').tabs('select',node.text);
                    }else{
                        if("外防实验(2FBE)"==nodeTxt||"Od Test(2FBE)"==nodeTxt){
                            $('#hlTab').tabs('add',{
                                title:node.text,
                                content:"<iframe scrolling='auto' frameborder='0'  src='labtesting/labtesting2fbe.jsp' style='width:100%;height:100%;'></iframe>",
                                closable:true
                            });
                            hlLanguage();
                        }else if("外防实验(3LPE)"==nodeTxt||"Od Test(3LPE)"==nodeTxt){
                            $('#hlTab').tabs('add',{
                                title:node.text,
                                content:"<iframe scrolling='auto' frameborder='0'  src='labtesting/labtesting3lpe.jsp' style='width:100%;height:100%;'></iframe>",
                                closable:true
                            });
                            hlLanguage();
                        }else if("内防实验(Liquid Epoxy)"==nodeTxt||"Id Test(Liquid Epoxy)"==nodeTxt){
                            $('#hlTab').tabs('add',{
                                title:node.text,
                                content:"<iframe scrolling='auto' frameborder='0'  src='labtesting/labtestingepoxy.jsp' style='width:100%;height:100%;'></iframe>",
                                closable:true
                            });
                            hlLanguage();
                        }else if("原材料实验(2FBE)"==nodeTxt||"Raw Material Testing Standard(2FBE)"==nodeTxt){
                            $('#hlTab').tabs('add',{
                                title:node.text,
                                content:"<iframe scrolling='auto' frameborder='0'  src='labtesting/rawmaterialtesting2fbe.jsp' style='width:100%;height:100%;'></iframe>",
                                closable:true
                            });
                            hlLanguage();
                        }else if("原材料实验(3LPE)"==nodeTxt||"Raw Material Testing Standard(3LPE)"==nodeTxt){
                            $('#hlTab').tabs('add',{
                                title:node.text,
                                content:"<iframe scrolling='auto' frameborder='0'  src='labtesting/rawmaterialtesting3lpe.jsp' style='width:100%;height:100%;'></iframe>",
                                closable:true
                            });
                            hlLanguage();
                        }else if("原材料实验(Liquid Epoxy)"==nodeTxt||"Raw Material Testing Standard(Liquid Epoxy)"==nodeTxt){
                            $('#hlTab').tabs('add',{
                                title:node.text,
                                content:"<iframe scrolling='auto' frameborder='0'  src='labtesting/rawmaterialtestingliquidepoxy.jsp' style='width:100%;height:100%;'></iframe>",
                                closable:true
                            });
                            hlLanguage();
                        }
                    }
                }
            });


            //生产报表管理
            $("#hlprodcutionreport").tree({
                onClick: function (node) {
                    var tab = $('#hlTab').tabs('getTab', node.text);
                    var nodeTxt = node.text;
                    if (tab) {
                        $('#hlTab').tabs('select', node.text);
                    } else {
                        if ("生产岗位记录管理" == nodeTxt || "Production Process Record Management" == nodeTxt) {

                            $('#hlTab').tabs('add', {
                                title: node.text,
                                content: "<iframe scrolling='auto' frameborder='0'  src='productionRecordAndReport/productionProcessRecord.jsp' style='width:100%;height:100%;'></iframe>",
                                closable: true
                            });
                            hlLanguage();
                        }
                        else if ("生产日报管理" == nodeTxt || "Daily Production Report Management" == nodeTxt) {

                            $('#hlTab').tabs('add', {
                                title: node.text,
                                content: "<iframe scrolling='auto' frameborder='0'  src='productionRecordAndReport/dailyProductionReport.jsp' style='width:100%;height:100%;'></iframe>",
                                closable: true
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
                        else if("角色管理"==nodeTxt||"Role Management"==nodeTxt){

                            $('#hlTab').tabs('add',{
                                title:node.text,
                                content:"<iframe scrolling='auto' frameborder='0'  src='account/role.jsp' style='width:100%;height:100%;'></iframe>",
                                closable:true
                            });
                            hlLanguage();
                        }
                        else if("权限管理"==nodeTxt||"Function Management"==nodeTxt){

                            $('#hlTab').tabs('add',{
                                title:node.text,
                                content:"<iframe scrolling='auto' frameborder='0'  src='account/function.jsp' style='width:100%;height:100%;'></iframe>",
                                closable:true
                            });
                            hlLanguage();
                        }


                    }
                }
            });


            //makeMenu("外喷砂工序",odParams,"odblastprocess");
            $('.btnLogout').click(function () {

                var form = $("<form>");//定义一个form表单
                form.attr("style", "display:none");
                form.attr("target", "");
                form.attr("method", "post");//请求类型
                form.attr("action","/Login/logout.action");//请求地址
                $("body").append(form);//将表单放置在web中

                var options={
                    type:'POST',
                    url:'/Login/Logout.action',
                    dataType:'json',
                    beforeSubmit:function () {
                        ajaxLoading();
                    },
                    success:function (data) {
                        ajaxLoadEnd();
                        //alert(data.msg);
                        if(data.success){
                            window.location.href="<%=bPath%>"+"login/login.jsp";
                        }


                    },error:function () {
                        ajaxLoadEnd();
                    }
                };
                //form.submit(function (e) {
                form.ajaxSubmit(options);
                return false;
            });

            function ajaxLoadEnd() {
                $(".datagrid-mask").remove();
                $(".datagrid-mask-msg").remove();
            }

            function ajaxLoading() {
                $("<div class=\"datagrid-mask\"></div>").css({
                    display: "block",
                    width: "100%",
                    height: $(window).height()
                }).appendTo("body");
                $("<div class=\"datagrid-mask-msg\"></div>").html("正在退出，请稍候。。。").appendTo("body").css({
                    display: "block",
                    left: ($(document.body).outerWidth(true) - 190) / 2,
                    top: ($(window).height() - 45) / 2
                });
            }


        });

        function  MakeMenus(name) {
            var res='<li class=\"i18n1\" name=\"'+name+'\" ></li>';
            return res;
        }




    </script>
</head>
<body class="easyui-layout">
<div data-options="region:'south',split:true" style="height:50px;">
    <div style="text-align: center"><h3>Dr.K Inspection &copy;2018 友元科技 版权所有</h3></div>
</div>
<div data-options="region:'north',split:true">



    <div style="float: right;padding:10px">
        <select id="language">
            <option value="zh-CN">中文</option>
            <option value="en">ENGLISH</option>
        </select>
        <button class="btnLogout">退出</button>
    </div>
</div>



<div data-options="region:'west'" title="导航菜单" class="i18n" name="navigation" style="width:200px;">

    <div id="aa" class="easyui-accordion">
        <%--<div title="外防腐" class="i18n" name="externalcoating"  style="padding:10px;">--%>
            <%--<ul id="od">--%>
                <%--<li class="i18n1" name="odblastprocess" >外喷砂工序</li>--%>
                <%--<li class="i18n1" name="odblastinspection" >外喷砂检验工序</li>--%>
                <%--<li class="i18n1" name="odcoating2fbe" >外涂工序(2FBE)</li>--%>
                <%--<li class="i18n1" name="odcoating3lpe" >外涂工序(3LPE)</li>--%>
                <%--<li class="i18n1" name="odcoating2fbeinspection" >外涂检验工序(2FBE)</li>--%>
                <%--<li class="i18n1" name="odcoating3lpeinspection" >外涂检验工序(3LPE)</li>--%>
                <%--<li class="i18n1" name="odstencilprocess" >外喷标工序</li>--%>
                <%--<li class="i18n1" name="odfinalinspection">外涂层终检工序</li>--%>
            <%--</ul>--%>
        <%--</div>--%>
        <%--<div title="内防腐" class="i18n" name="intenalcoating" style="padding:10px;">--%>
            <%--<ul id="id">--%>
                <%--<li class="i18n1" name="idblastprocess">内喷砂工序</li>--%>
                <%--<li class="i18n1" name="idblastinspection">内喷砂检验工序</li>--%>
                <%--<li class="i18n1" name="idcoating">内涂工序</li>--%>
                <%--<li class="i18n1" name="idcoatinginspection">内涂检验工序</li>--%>
                <%--<li class="i18n1" name="idstencilprocess">内喷标工序</li>--%>
                <%--<li class="i18n1" name="idfinalinspection">内涂层终检工序</li>--%>
            <%--</ul>--%>
        <%--</div>--%>
        <%--<div title="出入库" class="i18n" name="storage" style="padding:10px;">--%>
            <%--<ul id="hlstoragemanagement">--%>
            <%--<li class="i18n1" name="odstockin">外涂成品入库</li>--%>
            <%--<li class="i18n1" name="idstockin">内涂成品入库</li>--%>
            <%--<li class="i18n1" name="baremovement">光管调拨</li>--%>
            <%--<li class="i18n1" name="productstockout">成品出厂</li>--%>
            <%--</ul>--%>

        <%--</div>--%>
        <%--<div title="涂层修补与扒皮" class="i18n" name="coatingrepairandstrip" style="padding:10px;">--%>
            <%--<ul id="hlcoatingrepairstrip">--%>
                <%--<li class="i18n1" name="coatingrepair">涂层修补</li>--%>
                <%--<li class="i18n1" name="coatingstrip">涂层扒皮</li>--%>
            <%--</ul>--%>
        <%--</div>--%>
        <%--<div title="钢管修磨切割与倒棱" class="i18n" name="grindingcutoffrebevel" style="padding:10px;">--%>
            <%--<ul id="hlgrindingcutoffrebevel">--%>
                <%--<li class="i18n1" name="barepipegrindingcutoff">光管修磨切割</li>--%>
                <%--<li class="i18n1" name="pipesampling">钢管外防取样</li>--%>
                <%--<li class="i18n1" name="piperebevel">钢管倒棱</li>--%>
            <%--</ul>--%>
        <%--</div>--%>


        <%--<div title="基础信息管理" class="i18n" name="basicinfomanagement" style="padding:10px;">--%>
            <%--<ul id="hlbasicinfomanagement">--%>
                <%--<li class="i18n1" name="projectmanagement">项目管理</li>--%>
                <%--<li class="i18n1" name="contractmanagement">合同管理</li>--%>
                <%--<li class="i18n1" name="pipemanagement">钢管管理</li>--%>
                <%--<li class="i18n1" name="uploadpipe">钢管录入</li>--%>
                <%--<li class="i18n1" name="twodimensionalcodeprintrecord">二维码打印记录</li>--%>
            <%--</ul>--%>
        <%--</div>--%>
        <%--<div title="生产工艺" class="i18n" name="productionprocess" style="padding:10px;">--%>
            <%--<ul id="hlprocess">--%>
                <%--<li class="i18n1" name="odstandard">外防腐生产标准</li>--%>
                <%--<li class="i18n1" name="instandard">内防腐生产标准</li>--%>
                <%--<li class="i18n1" name="teststandard2fbe">实验标准(2FBE)</li>--%>
                <%--<li class="i18n1" name="teststandard3lpe">实验标准(3LPE)</li>--%>
                <%--<li class="i18n1" name="rawmaterialstandard2fbe">原材料标准(2FBE)</li>--%>
                <%--<li class="i18n1" name="rawmaterialstandard3lpe">原材料标准(3LPE)</li>--%>
            <%--</ul>--%>
        <%--</div>--%>
        <%--<div title="实验" class="i18n" name="labtesting" style="padding:10px;">--%>
            <%--<ul id="hltest">--%>
                <%--<li class="i18n1" name="odtest2fbe">外防实验(2FBE)</li>--%>
                <%--<li class="i18n1" name="odtest3lpe">外防实验(3LPE)</li>--%>
                <%--<li class="i18n1" name="idtestepoxy">内防实验(Liquid Epoxy)</li>--%>
                <%--<li class="i18n1" name="rawmaterialtest2fbe">原材料实验(2FBE)</li>--%>
                <%--<li class="i18n1" name="rawmaterialtest3lpe">原材料实验(3LPE)</li>--%>
                <%--<li class="i18n1" name="rawmaterialtestepoxy">原材料实验(Liquid Epoxy)</li>--%>
            <%--</ul>--%>
        <%--</div>--%>
        <%--<div title="生产报表" class="i18n" name="productionreport" style="padding:10px;">--%>
            <%--ff--%>
        <%--</div>--%>
        <%--<div title="账户管理" class="i18n" name="accountmanagement" style="padding:10px;">--%>
            <%--<ul id="hlaccount">--%>
                <%--<li class="i18n1" name="personmanagement">账户管理</li>--%>
                <%--<li class="i18n1" name="rolemanagement">角色管理</li>--%>
                <%--<li class="i18n1" name="functionmanagement">权限管理</li>--%>

            <%--</ul>--%>
        <%--</div>--%>

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

    hlLanguage("i18n/");
</script>