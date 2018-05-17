<%--
  Created by IntelliJ IDEA.
  User: kurt
  Date: 3/26/18
  Time: 1:49 PM
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
    <script src="../js/jquery.form.js" type="text/javascript"></script>
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
            min-height:30px;
            border-width: 2px;
            border-style: solid;
            display: none;
        }
    </style>
    <script type="text/javascript">
        $(function() {
            //先获取所有的项目的编号和名称
            $.ajax({
                url: "/ProjectOperation/getProjectNoAndName.action",
                dataType: 'json',
                async: false,
                success: function (data) {
                    var dataJson = data[0];
                    if (dataJson != null && dataJson.length > 0) {
                        $.each(dataJson, function (index, element) {
                            var template = '<option value="' + element.project_no + '">' + element.project_name + '</option>';
                            $('#cc').append(template);
                        });
                    }
                },
                error: function () {
                    alert("获取项目信息时出错!");
                }
            });


            $('.btnReport').click(function () {
                var selectValue = $('#cc').val();
                var selectText = $('#cc').find('option:selected').text();
                var begin_time = $('#begintime').val();
                var end_time = $('#endtime').val();
                if (begin_time == null || begin_time.length == "") {
                    $.messager.alert('Warning', '请输入开始时间!');
                    return false;
                }
                if (end_time == null || end_time.length == "") {
                    $.messager.alert('Warning', '请输入结束时间!');
                    return false;
                }
                var begin_date = new Date(begin_time);
                var end_date = new Date(end_time);
                if (!(end_date - begin_date >= 0)) {
                    $.messager.alert('Warning', '开始时间必须小于结束时间!');
                    return false;
                }
                //ajaxLoading();
                var form = $("<form>");//定义一个form表单
                form.attr("style", "display:none");
                form.attr("target", "");
                form.attr("method", "post");//请求类型
                form.attr("action","/InspectionRecordPDFOperation/getRecordReportPDF.action");//请求地址
                $("body").append(form);//将表单放置在web中
                var input1 = $("<input type='hidden' name='project_no' value='" + selectValue + "'/>");
                form.append(input1);
                var input2 = $("<input type='hidden' name='beginTime' value='" + begin_time + "'/>");
                form.append(input2);
                var input3 = $("<input type='hidden' name='endTime' value='" + end_time + "'/>");
                form.append(input3);
                var input4 = $("<input type='hidden' name='project_name' value='" + selectText + "'/>");
                form.append(input4);
                // form.submit();
                var options={
                    type:'POST',
                    url:'/InspectionRecordPDFOperation/getRecordReportPDF.action',
                    dataType:'json',
                    beforeSubmit:function () {
                        ajaxLoading();
                    },
                    success:function (data) {
                        $('#p').css('display','none');
                        window.location.href="<%=bPath%>"+data;
                        ajaxLoadEnd();
                    },error:function () {
                        $('#p').css('display','none');
                        ajaxLoadEnd();
                    }
                };
                //form.submit(function (e) {
                form.ajaxSubmit(options);
                   return false;
                });

                //表单提交
                // $.ajax({
                //     url:"/InspectionRecordPDFOperation/getRecordReportPDF.action",
                //     data:{selectValue:selectValue,beginTime:begin_time,endTime:end_time},
                //     dataType:'json',
                //     async:false,
                //     beforeSend:function(){
                //         ajaxLoading();
                //     },
                //     success:function(data){
                //         if(data!=null){
                //              if(data=="success"){
                //                  $.messager.alert('Warning','生成成功!');
                //              }else{
                //                  $.messager.alert('Warning','生成失败!');
                //              }
                //         }else{
                //             $.messager.alert('Warning','生成失败!');
                //         }
                //         ajaxLoadEnd();
                //     },
                //     error:function(){
                //         ajaxLoadEnd();
                //         $.messager.alert('Warning','生成失败!');
                //     },
                //     complete:function(){
                //         ajaxLoadEnd();
                //     }
            //});
        });
        function ajaxLoading() {
            $("<div class=\"datagrid-mask\"></div>").css({
                display: "block",
                width: "100%",
                height: $(window).height()
            }).appendTo("body");
            $("<div class=\"datagrid-mask-msg\"></div>").html("<a style='display: block'>正在处理，请稍候。。。</a>").appendTo("body").css({
                display: "block",
                left: ($(document.body).outerWidth(true) - 190) / 2,
                top: ($(window).height() - 45) / 2
            });
            $('#p').css({
                display: "block",
                left: ($(document.body).outerWidth(true)-600) / 2,
                top: ($(window).height()) / 2,
                'z-index':10000
            })

            showCheckProgress();
        }
        function ajaxLoadEnd() {
            $(".datagrid-mask").remove();
            $(".datagrid-mask-msg").remove();
        }
        // function downloadPdf(pathList) {
        //     var form=$("<form>");//定义一个form表单
        //     form.attr("style","display:none");
        //     form.attr("target","");
        //     form.attr("method","post");//请求类型
        //     form.attr("action","/InspectionRecordPDFOperation/downloadPDF.action");//请求地址
        //     $("body").append(form);//将表单放置在web中
        //     var input1=$("<input>");
        //     input1.attr("type","hidden");
        //     input1.attr("name","pathList");
        //     input1.attr("value",pathList);
        //     form.append(input1);
        //     form.submit();//表单提交
        // }



        //展示进度条
        var timerId;
        function showCheckProgress(){

            //想要修改进度条的颜色去css文件中去修改
            // $('#p').progressbar({
            //     value : 0,          //设置进度条值 默认0
            //     text : '{value}%'  //设置进度条百分比模板 默认 {value}%
            //     //在value改变的时候触发
            //     /*onChange : function (newValue, oldValue) {
            //         console.log('新:' + newValue + ',旧:' + oldValue);
            //     },  */
            // });
            $('#p').progressbar('setValue',0);
            timerId = window.setInterval(getCheckProgress,400);
        }


        //通过session得到进度
        //通过post请求得到进度
        function getCheckProgress(){
            var progressUrl = '/InspectionRecordPDFOperation/getPDFProgress.action';
            //使用JQuery从后台获取JSON格式的数据
            $.ajax({
                type:"post",//请求方式
                url:progressUrl,//发送请求地址
                timeout:3000,//超时时间：30秒
                dataType:"json",//设置返回数据的格式
                //请求成功后的回调函数 data为json格式
                success:function(data){
                    if(data.pdfProgress>=100){
                        $('#p').progressbar('setValue',100);
                        window.clearInterval(timerId);
                        $('#dg').datagrid('load');
                        $('#importBtn').css('display','inline-block');
                        $('#showProgress').css('display','none');

                    }
                    $('#p').progressbar('setValue',data.pdfProgress);
                },
                //请求出错的处理
                error:function(){
                    window.clearInterval(timerId);
                    $('#p').css('display','none');
                }
            });
        }



    </script>
</head>
<body>
<div style="padding:10px">
    <select id="cc" class="easyui-combobox" data-options="editable:false" name="result" style="width:200px;">
        <%--<option value="-1" selected="selected">所有工序</option>--%>
        <%--<option value="0">外喷砂工序</option>--%>
        <%--<option value="1">外喷砂检验工序</option>--%>
        <%--<option value="2">外涂工序(2FBE)</option>--%>
        <%--<option value="3">外涂检验工序(2FBE)</option>--%>
        <%--<option value="4">外涂工序(3LPE)</option>--%>
        <%--<option value="5">外涂检验工序(3LPE)</option>--%>
        <%--<option value="6">外喷标工序</option>--%>
        <%--<option value="7">外涂层终检工序</option>--%>
        <%--<option value="8">内喷砂工序</option>--%>
        <%--<option value="9">内喷砂检验工序</option>--%>
        <%--<option value="10">内涂工序</option>--%>
        <%--<option value="11">内涂检验工序</option>--%>
        <%--<option value="12">内喷标工序</option>--%>
        <%--<option value="13">内涂层终检工序</option>--%>
    </select>
    <span class="i18n1" name="begintime">开始时间</span>:
    <input id="begintime" name="begintime" type="text" class="easyui-datebox" data-options="formatter:myformatter,parser:myparser">
    <span class="i18n1" name="endtime">结束时间</span>:
    <input id="endtime" name="endtime" type="text" class="easyui-datebox" data-options="formatter:myformatter,parser:myparser">
    <button class="btnReport">开始生成</button>
<br><br>
    <div id="p" class="easyui-progressbar" data-options="value:0" style="width:600px;display:none"></div>
</div>





<script type="text/javascript" src="../easyui/jquery.easyui.min.js"></script>
</body>
</html>
