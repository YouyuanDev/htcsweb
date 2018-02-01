<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>供应商信息</title>
    <link rel="stylesheet" type="text/css" href="easyui/themes/metro/easyui.css" charset="UTF-8">
    <link rel="stylesheet" type="text/css" href="easyui/themes/icon.css" charset="UTF-8">
    <script type="text/javascript" src="easyui/jquery.min.js"charset="UTF-8"></script>
    <script type="text/javascript" src="easyui/jquery.easyui.min.js" charset="UTF-8"></script>
    <script type="text/javascript" src="easyui/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>


    <style type="text/css" >
        .b3{border-style:inset;border-width:thin;}
    </style>


    <script type="text/javascript">
        var url;
        //增加
        function newUser(){
            $('#dlg').window({
                title:'增加',
                width:700,
                height:430,
                modal:true,
                draggable:false,
                resizable:false,
                shadow:false,
                content:"<iframe src='supplieradd.jsp' width='100%' height='100%'  frameborder='no' >",
            });
        }
        //修改：
        function edituser(){
            var row = $('#datagrid').datagrid('getSelected');
            if(row){
                $('#dla').window({
                    title:'修改',
                    width:700,
                    height:430,
                    modal:true,
                    draggable:false,
                    resizable:false,
                    content:"<iframe src='/getSupplierById.action?supplier_id="+row.supplier_id+"' width='100%' height='100%'  frameborder='no' >",
                    shadow:false,
                });
                parent.$('#datagrid').datagrid('reload');
            }

//            alert("xiuuu")
//            var row = $('#datagrid').datagrid('getSelected');
//            if (row){
//                $("#dla").dialog('open').dialog('setTitle','修改');
//                $("#fa").form('load',row.supplier_id);
//                //val() - 设置或返回表单字段的值
//                $("#supplier_code").val(row.supplier_code);
//                $("#supplierame").val(row.supplier_name);
//           //按照 经验 经验查询


            //
        }

       //查所有
        $(function(){
            $("#datagrid").datagrid({
                url: '/selfy.action',
                columns: [[
                    { field: '',checkbox:true},
                    { title: 'id',field: 'supplier_id',width:100},
                    { title: '供应商',field: 'supplier_code',width:100},
                    { title: '供应商名字',field :'supplier_name',width:100},
                    { title: '供应商地址',field: 'forwar_address',width:100},
                    { title: '联系手机  ',field: 'contact_phone',width:100},
                    { title: '结算方式',field: 'clearing_form',width:100},
                    { title: '是否有效',field: 'is_validity',width:100},
                    { title: '银行',field: 'bank',width: 100},
                    { title: '说明',field: 'instructions',width:120}
                ]],
                striped:true,
                loadMsg:"加载中",
                pagination:true,
                singleSelect:true,
                pageList:[1,2,3,4,5,6,7,8,9,10],
                pageSize:6,
                pageNumber:1
            });
        })

        /天极

    </script>





</head>

<body>

    <div>
    <a id="btna"  class="easyui-linkbutton" data-options="iconCls:'icon-add'" onclick="newUser()">新增</a>
    <a id="btnb"  class="easyui-linkbutton" data-options="iconCls:'icon-edit'" onclick="edituser()">编辑</a>
    <a id="btnc"  class="easyui-linkbutton" data-options="iconCls:'icon-delete'" onclick="getnull()">关闭</a>
    </div>
    <br/>
    <fieldset class="b3" style="width: 965px">
        <legend>
            <h3><b style="color: orange;margin-top: 210px;width: 1065px" >|&nbsp;</b>查询条件</h3>
        </legend>
        <b style="margin-left: 30px;padding-top: 5px;">运输商：</b><input  id="xc" name="depa" >
        <b style="margin-left: 40px;">供应商手机：</b><input  id="xd" name="depb">
        <b style="margin-left: 40px;">是否有效：</b>
        <select id="xa"  style="width: 120px;margin-top: -10px" >
            <option value="1">全部</option>
            <option value="2">有效</option>
            <option value="3">无效</option>
        </select>
        <input type="hidden" id="sd">
        <a onclick="adds()" class="easyui-linkbutton" style="background: #FABE00;width: 60px" >查询</a>
    </fieldset>
    <br>
<fieldset class="b3" style="width: 965px">
    <legend> <h3><b style="color: orange" >|&nbsp;</b>供应商数据</h3></legend>
    <div id="datagrid" style=" margin-top: 210px;width: 950px"></div>
</fieldset>
    <div id="dlg" ></div>
    <div id="dla" ></div>
</body>
</html>
