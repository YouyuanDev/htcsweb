<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>信息</title>
    <link rel="stylesheet" type="text/css" href="easyui/themes/metro/easyui.css" charset="UTF-8">
    <link rel="stylesheet" type="text/css" href="easyui/themes/icon.css" charset="UTF-8">
    <script type="text/javascript" src="easyui/jquery.min.js"charset="UTF-8"></script>
    <script type="text/javascript" src="easyui/jquery.easyui.min.js" charset="UTF-8"></script>
    <script type="text/javascript" src="easyui/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>


    <style type="text/css" >
        .b3{border-style:inset;border-width:thin;}
    </style>


    <script type="text/javascript">
        $(function(){

            // $("#datagrids").datagrid({
            //     url: '/selfy.action',
            //     columns: [[
            //         { field: '',checkbox:true},
            //         { title: '用户名',field: 'pname',width:100},
            //         { title: '年龄',field: 'page',width:100},
            //     ]],
            //     striped:true,
            //     loadMsg:"加载中",
            //     pagination:true,
            //     singleSelect:true,
            //     pageList:[1,2,3,4,5,6,7,8,9,10],
            //     pageSize:6,
            //     pageNumber:1
            // });
        })
        var url;
        //增加
        function addPerson(){
            $('#personWindow').window({
                title:'添加账户',
                width:700,
                height:430,
                modal:true,
                draggable:false,
                resizable:false,
                shadow:false
                // content:"<iframe src='odblastprocessadd.jsp' width='100%' height='100%'  frameborder='no' >",
            });
        }
        //修改：
        function edituser(){
            var row = $('#datagrids').datagrid('getSelected');
            if(row){
                $('#dla').window({
                    title:'修改',
                    width:700,
                    height:430,
                    modal:true,
                    draggable:false,
                    resizable:false,
                    content:"<iframe src='/getodblastprocessById.action?id="+row.id+"' width='100%' height='100%'  frameborder='no' >",
                    shadow:false,
                });
                parent.$('#datagrids').datagrid('reload');
            }

        }

        //查所有




    </script>





</head>

<body>


<fieldset class="b3" style="width:100%;height:auto;padding:10px;margin:10px;">
    <legend> <h3><b style="color: orange" >|&nbsp;</b>数据展示</h3></legend>
    <div id="personDatagrids" style=" margin-top: 210px;width:auto;">

    </div>
</fieldset>

<%--<div id="personDialog" class="easyui-dialog" title="添加账户" style="width:300px;height:300px;" data-options="iconCls:'icon-vcard_add,">--%>

<%--</div>--%>
<div id="personWindow" class="easyui-window">
   <form action="#" id="personForm" method="post">
       <div>
           <label for="pname">Name:</label>
           <input class="easyui-validatebox" type="text" name="pname" data-options="required:true" />
       </div>
       <div>
           <label for="page">Age:</label>
           <input class="easyui-validatebox" type="text" name="page" data-options="required:true" />
       </div>
   </form>
</div>
</body>
</html>
