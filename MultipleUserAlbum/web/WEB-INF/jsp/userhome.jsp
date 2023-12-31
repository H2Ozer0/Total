<%--
  Created by IntelliJ IDEA.
  User: 52491
  Date: 2019/12/10
  Time: 21:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>${userInfo.username}的主页</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/layui/css/layui.css" type="text/css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/st-style.css" type="text/css"/>
    <script src="https://libs.baidu.com/jquery/2.1.4/jquery.min.js"></script>
    <script src="${pageContext.request.contextPath}/static/layui/layui.all.js"></script>
    <style>
        .layui-tab-item {
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh; /* 使容器充满整个视口高度 */
        }

        #myInfoContent {
            display: flex;
            flex-direction: column;
            align-items: center;
        }
    </style>
    <style>
        .st-main {
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh; /* 使容器充满整个视口高度 */
        }
    </style>

</head>
<body class="bg-gray">

<jsp:include page="header.jsp"></jsp:include>

<div class="st-banner" style="height: 150px"></div>

<div style="background: white">
    <div class="home-information-box">
        <div class="information-headimg-box">
            <img src="/getAvatar?id=${myInfo.userId}" width="150px" height="150px"/>
        </div>
    </div>

    <div class="text-center" style="margin-top: 80px">
        <div id="user_name" style="font-size: 24px">${myInfo.username}</div>
        <div id="user_id" style="font-size: 14px;color: #bbbbbb">id:${myInfo.userId}</div>

    </div>
</div>

<div class="layui-tab layui-tab-brief" style="" lay-filter="docDemoTabBrief">
    <ul class="layui-tab-title" style="text-align: center;background:white">
        <li class="layui-this" style="width: 200px" id="myInfoTab">个人信息</li>
    </ul>
    <div class="st-main">
        <div class="layui-tab-content">
            <%--编辑个人信息--%>
            <div class="layui-tab-item layui-show" id="myInfoContent">
                <form class="layui-form" id="myInfoForm">
                    <div class="layui-form-item">
                        <label class="layui-form-label">用户ID</label>
                        <div class="layui-input-inline">
                            <input type="text" name="userId" id="userId" value="${myInfo.userId}" class="layui-input" disabled>
                        </div>
                    </div>

                    <div class="layui-form-item">
                        <label class="layui-form-label">用户名</label>
                        <div class="layui-input-inline">
                            <input type="text" name="username" id="username" value="${myInfo.username}" class="layui-input" disabled>
                            <button type="button" class="layui-btn layui-btn-sm" onclick="modifyInfo('username')">修改</button>
                            <button class="layui-btn layui-btn-sm" lay-submit lay-filter="saveUsername">保存</button>
                        </div>
                    </div>

                    <div class="layui-form-item">
                        <label class="layui-form-label">邮箱</label>
                        <div class="layui-input-inline">
                            <input type="text" name="email" id="email" value="${myInfo.email}" class="layui-input" disabled>
                            <button type="button" class="layui-btn layui-btn-sm" onclick="modifyInfo('email')">修改</button>
                            <button class="layui-btn layui-btn-sm" lay-submit lay-filter="saveEmail">保存</button>
                        </div>
                    </div>

                    <div class="layui-form-item">
                        <label class="layui-form-label">个人描述</label>
                        <div class="layui-input-inline">
                            <input type="text" name="description" id="description" value="${myInfo.description}" class="layui-input" disabled>
                            <button type="button" class="layui-btn layui-btn-sm" onclick="modifyInfo('description')">修改</button>
                            <button class="layui-btn layui-btn-sm" lay-submit lay-filter="saveDescription">保存</button>
                        </div>
                    </div>

                    <!-- 添加每个信息独立的保存按钮 -->
                    <!-- 例如：<button class="layui-btn layui-btn-sm" lay-submit lay-filter="saveEmail">保存</button> -->

                </form>
            </div>
        </div>
    </div>
</div>
<script>
    layui.use(['form', 'layer', 'element'], function () {
        var form = layui.form;
        var layer = layui.layer;
        var element = layui.element;
        // 处理信息修改的函数
        window.modifyInfo = function (fieldName) {
            // 启用输入框以便编辑
            $('#' + fieldName).removeAttr('disabled');
            // 将焦点设置到输入框
            $('#' + fieldName).focus();
            // 显示保存按钮
            $('#saveBtn-' + fieldName).show();
        };
        // 提交表单数据到服务器的通用函数
        function submitForm(fieldValue, url) {
            // 发送 AJAX 请求到服务器
            $.ajax({
                url: url,
                type: 'POST',
                data: { fieldValue: fieldValue },
                success: function (response) {
                    // 处理来自服务器的成功响应
                    console.log(response);
                    // 在前端展示返回的信息
                    layer.alert(response.msg);
                },
                error: function (error) {
                    // 处理来自服务器的错误响应
                    console.error(error);
                }
            });
        }

        // 提交用户名表单数据到服务器
        form.on('submit(saveUsername)', function (data) {
            var fieldValue = data.field.username;
            var url = '${pageContext.request.contextPath}/me/updateUsername';
            submitForm(fieldValue, url);
            return false;
        });

        // 提交邮箱表单数据到服务器
        form.on('submit(saveEmail)', function (data) {
            var fieldValue = data.field.email;
            var url = '${pageContext.request.contextPath}/me/updateEmail';
            submitForm(fieldValue, url);
            return false;
        });

        // 提交个人描述表单数据到服务器
        form.on('submit(saveDescription)', function (data) {
            var fieldValue = data.field.description;
            var url = '${pageContext.request.contextPath}/me/updateDescription';
            submitForm(fieldValue, url);
            return false;
        });
    });
</script>




</body>
</html>
