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

    <style type="text/css" >
        .b3{border-style:inset;border-width:thin;}
    </style>


    <script type="text/javascript">


        $(function() {

        });
        function  uploadPic() {
            $('#supplierForm').ajaxSubmit({
                type:'post',
                url:'',
                beforeSubmit:function () {
                    
                },
                success:function () {

                },
                error:function () {
                    
                }
            });
        }
    </script>





</head>

<body>
    <form id="supplierForm" method="post">
        <input type="file" class="easyui-textbox" onchange="uploadPic()">
    </form>


</body>
</html>
