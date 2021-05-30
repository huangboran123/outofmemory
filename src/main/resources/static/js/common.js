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

/*建立websocket连接*/
function openSocket() {
    if(typeof(WebSocket) == "undefined") {
        console.log("您的浏览器不支持WebSocket");
    }else{
        console.log("您的浏览器支持WebSocket");
        //实现化WebSocket对象，指定要连接的服务器地址与端口  建立连接
        var socketUrl="http://localhost:8080//chat/"+userId;
        socketUrl=socketUrl.replace("https","ws").replace("http","ws");
        console.log(socketUrl);
        if(socket!=null){
            socket.close();
            socket=null;
        }
        socket = new WebSocket(socketUrl);
        //打开事件
        socket.onopen = function() {
            console.log("websocket已打开");
            socket.send("这是来自客户端的消息");
        };
        //获取消息事件
        socket.onmessage = function(msg) {
            console.log(msg.data);
            //发现消息进入   开始处理前端触发逻辑
        };
        //关闭事件
        socket.onclose = function() {
            console.log("websocket已关闭");
        };
        //错误事件
        socket.onerror = function() {
            console.log("websocket发生了错误");
        }
    }
}


