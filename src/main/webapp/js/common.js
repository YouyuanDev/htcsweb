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
function hlAlertSix(url,$imglist,$dialog,$obj) {
    var hlparam=$imglist.val().trim();
    var imgarr=[];
    if(hlparam!=""){
        imgarr=hlparam.split(';');
    }
    if((imgarr.length-1)>0){
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
//表单验证函数
//---验证是否为空
function hlValidateNull($obj) {
  if($obj.val().trim()==""){
     return false;
  }
}

//清理选择文件框
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
//时间转化函数
function getDate1(str){
    var oDate = new Date(str);
    y=oDate.getFullYear();
    m = oDate.getMonth()+1;
    d = oDate.getDate();
    h=oDate.getHours();
    mins=oDate.getMinutes();
    s=oDate.getSeconds();
    return  y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d)+' '+(h<10?('0'+h):h)+':'+(mins<10?('0'+mins):mins)+':'+(s<10?('0'+s):s);
}
//时间格式化
function formatterdate(value,row,index){
    return getDate1(value);
}
function myformatter(date){
    var y = date.getFullYear();
    var m = date.getMonth()+1;
    var d = date.getDate();
    return y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d);
}
function myparsedate(s){
    if (!s) return new Date();
    return new Date(Date.parse(s));
}
function myparser(s){
    if (!s) return new Date();
    var ss = (s.split('-'));
    var y = parseInt(ss[0],10);
    var m = parseInt(ss[1],10);
    var d = parseInt(ss[2],10);
    if (!isNaN(y) && !isNaN(m) && !isNaN(d)){
        return new Date(y,m-1,d);
    } else {
        return new Date();
    }
}

// 日期格式为 2/20/2017 12:00:00 PM
function myformatter2(date){
    return getDate1(date);
}
// 日期格式为 2/20/2017 12:00:00 PM
function myparser2(s){
    if (!s) return new Date();
    return new Date(Date.parse(s));
}
//加载钢管信息
function loadPipeBaiscInfo(row) {
    $('#project_name').text(row.project_name);$('#contract_no').text(row.contract_no);
    $('#pipe_no').text(row.pipe_no);$('#status_name').text(row.status_name);
    $('#od').text(row.od);$('#wt').text(row.wt);
    $('#p_length').text(row.p_length);$('#weight').text(row.weight);
    $('#grade').text(row.grade);$('#heat_no').text(row.heat_no);
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
//钢管编号异步获取钢管信息清理数据
function  clearLabelPipeInfo() {
    $('#project_name').text('');
    $('#contract_no').text('');
    $('#status_name').text('');
    $('#grade').text('');
    $('#od').text('');
    $('#wt').text('');
    $('#p_length').text('');
    $('#weight').text('');
    $('#heat_no').text('');
}
//钢管编号异步获取钢管信息添加数据
function addLabelPipeInfo(data) {
    $.each(data,function (index,element) {
        $('#project_name').text(element.project_name);
        $('#contract_no').text(element.contract_no);
        $('#status_name').text(element.status_name);
        $('#grade').text(element.grade);
        $('#od').text(element.od);
        $('#wt').text(element.wt);
        $('#p_length').text(element.p_length);
        $('#weight').text(element.weight);
        $('#heat_no').text(element.heat_no);
    });
}