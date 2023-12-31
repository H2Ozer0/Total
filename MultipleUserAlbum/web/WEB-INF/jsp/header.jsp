<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <style>

        .bg-blue {
            background-color: #666699;
        }
    </style>
</head>
<body>
<div class="layui-nav st-header bg-blue">
    <div class="left" style="padding: 10px;margin-left: 60px;">
        <a href="/home" class="horizentol">
            <img width="30px" height="30px" src="${pageContext.request.contextPath}/getAvatar?username=${myInfo.username}">
            <div class="st-white-font" style="padding: 10px 5px">电子相册</div>
        </a>
    </div>

    <li class="layui-nav-item right" lay-unselect="" style="margin-right: 60px;display:${sessionScope.isLogin?"none":"block"}">
        <a href="/login_page" style="">
            <div class="st-white-font">登录 | 注册</div>
        </a>
        <!--TODO:iconfont-->
    </li>
    <li class="layui-nav-item right" lay-unselect="" style="display:${sessionScope.isLogin?"block":"none"};margin-right: 20px">
        <a href="${pageContext.request.contextPath}/userhome"><%--<i class="layui-icon layui-icon-username"style="color: white"></i>--%>
         <div class="st-white-font">当前账户:${sessionScope.myInfo.username}</div>
        </a>
        <dl class="layui-nav-child">
            <dd><a href="${pageContext.request.contextPath}/me/albums">管理相册</a></dd>
            <dd><a href="${pageContext.request.contextPath}/me/friendship">查看好友</a></dd>
            <dd><a href="${pageContext.request.contextPath}/albums/upload_photo">上传照片</a></dd>
            <dd><a href="${pageContext.request.contextPath}/me/favourite">查看收藏</a></dd>
            <dd><a href="${pageContext.request.contextPath}/login_page">退出</a></dd>

        </dl>
    </li>


</div>
<script>
    layui.use('element', function(){
        var element = layui.element; //导航的hover效果、二级菜单等功能，需要依赖element模块

        //监听导航点击
        element.on('nav(demo)', function(elem){
            //console.log(elem)
            layer.msg(elem.text());
        });
        element.render();
    });

    $("#logout").click(function () {
        //TODO 这里有有关session的bug
        //如果当前是/me或者/admin或者/sendMessage，则跳转回主页，否则刷新
        //没有吊用
        // var path = window.location.pathname;
        // var ii = ['/me','/admin','/sendMessage'];
        // ii.forEach(function (value) {
        //     if(path.indexOf(value) === 0 ){
        //         window.location.href = "/home";
        //         return;
        //     }
        // });
        $.ajax({
            url:"http://localhost:8080/logout",
            type:"get",
            dataType:"json",
            success:function () {
                window.location.reload();
            },
            error:function () {
                window.location.href = "/login_page";
            }
        })
    });

</script>
</body>
</html>
