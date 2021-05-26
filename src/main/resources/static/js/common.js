/*	--------------------------------拦截器跳转登录------------------------------------*/
function  jumptologinPage(xhr, status) {

    //拦截器实现超时跳转到登录页面
    // 通过xhr取得响应头
    var REDIRECT = xhr.getResponseHeader("REDIRECT");
    //如果响应头中包含 REDIRECT 则说明是拦截器返回的需要重定向的请求
    if (REDIRECT == "REDIRECT") {
        var win = window;
        while (win != win.top) {
            win = win.top;
        }
        showMessage("请先登录","提示",0.5);
        /*   hideMessage(2);*/
        setTimeout(hideMessage,1500,0.5);
        win.location.href = xhr.getResponseHeader("CONTEXTPATH");
    }
}



/*    $("#model_message").hide(3000,null);*/

/*==========================消息提示框==============================*/
function showMessage(msg,type,sec) {

    $("#model_message").show(sec*1000,null);
    $(".msgtype").text(type);
    $(".msgcontent").text(msg);
}
function hideMessage(sec) {
    $("#model_message").hide(sec*1000,null);
}

/*请先登录*/
function loginfirst(xhr,status){

    setTimeout(jumptologinPage,2000,xhr,status);
}



