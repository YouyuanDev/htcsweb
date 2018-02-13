//------公共函数
//弹出框函数
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
function hlAlertSix(url,$imglist,$dialog,$obj) {
    var hlparam=$imglist.val().trim();
    var imgarr=[];
    if(hlparam!=""){
        imgarr=hlparam.split(';');
    }
    if(imgarr.length>0){
        $.messager.confirm('系统提示',"取消上传，图片会自动删除!",function (r) {
            if(r){
                $.ajax({
                    url:url,
                    dataType:'json',
                    data:{"imgList":hlparam},
                    success:function (data) {
                    },
                    error:function () {
                        hlAlertThree();
                    }
                });
                clearMultiUpload($obj);
                $imglist.val('');
                $('#hl-gallery-con').empty();
                // $dialog.dialog('close');
            }
        });
    }else{
        clearMultiUpload($obj);
        $imglist.val('');
        $('#hl-gallery-con').empty();
        // $dialog.dialog('close');
    }
}
function  clearMultiUpload($grid) {
    var rows = $grid.getData();
    for (var i = 0, l = rows.length; i < l; i++) {
        $grid.uploader.cancelUpload(rows[i].fileId);
        $grid.customSettings.queue.remove(rows[i].fileId);
    }
    $grid.clearData();
}
//时间转化函数
function getDate(str){
    var oDate = new Date(str);
    year=oDate.getFullYear();
    month = oDate.getMonth()+1;
    month<10?"0"+month:month;
    day = oDate.getDate();
    day<10?"0"+day:day;
    hour=oDate.getHours();
    minute=oDate.getMinutes();
    second=oDate.getSeconds();
    return year+"年"+month+"月"+day+"日"+" "+hour+":"+minute+":"+second;
}
function getGalleryCon() {
    var str='<div id="hl-gallery">'+
        '<span class="prev"><</span>'+
        '<div id="content">' +
        '<div id="content_list">'+
        '</div>'+
    '</div>'+
    '<span class="next">></span>'+
    '</div>';
    return str;
}
function getCalleryChildren(imgUrl) {
    var str='<dl class="content-dl">' +
        '<dt><img src="'+imgUrl+'"/></dt>' +
        '<a class="content-del">X</a>' +
        '</dl>';
    return str;
}