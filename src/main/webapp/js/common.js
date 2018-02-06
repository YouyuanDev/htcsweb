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
    return year+"年"+month+"月"+day+""+" "+hour+":"+minute+":"+second;
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