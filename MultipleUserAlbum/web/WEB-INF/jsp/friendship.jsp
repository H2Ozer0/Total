<%--
  Created by IntelliJ IDEA.
  User: Lenovo
  Date: 2024/1/1
  Time: 14:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>好友管理</title>
    <!-- 引入Layui的CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/layui/css/layui.css" type="text/css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/st-style.css" type="text/css"/>
    <script src="https://libs.baidu.com/jquery/2.1.4/jquery.min.js"></script>
    <script src="${pageContext.request.contextPath}/static/layui/layui.all.js"></script>
    <!-- 页面自定义的CSS样式 -->
    <style>
        .layui-input-group {
            float: right;
            width: 30%; /* 调整左侧宽度，根据需要 */
        }
        /* 设置表格容器的样式 */
        .friendship-table-container {
            position: fixed; /* 固定定位 */
            right: 10px; /* 距离页面右边的位置 */
            bottom: 10px; /* 距离页面底部的位置 */
            width: 600px; /* 容器的宽度 */
            height: 300px; /* 容器的高度 */
            overflow: auto; /* 如果内容溢出，显示滚动条 */
            border: 1px solid #e2e2e2; /* 容器边框样式 */
            background-color: #fff; /* 容器背景颜色 */
            box-shadow: 0 0 5px rgba(0, 0, 0, 0.1); /* 容器阴影效果 */
        }
    </style>
</head>
<body class="bg-gray">
<jsp:include page="header.jsp"></jsp:include>
<div class="st-main horizentol" style="margin-top: 15px">
    <jsp:include page="my_left_bar.jsp"></jsp:include>
    <div class="friendship-table-container">
        <!-- 表格元素 -->
        <div class="right-div">
            <div class="layui-input-group">
                <div class="layui-input-split layui-input-prefix">
                    用户昵称或id
                </div>
                <input type="text" placeholder="带任意前置和后置内容" class="layui-input">
                <div class="layui-input-suffix">
                    <button type="button" class="layui-btn layui-bg-orange">添加好友</button>
                </div>
            </div>
        </div>
        <table id="friendshipTable" lay-filter="friendshipTable"></table>
    </div>
    <!-- 表格操作栏模板 -->
    <script type="text/html" id="actionBar">
        <!-- 删除按钮的HTML代码 -->
        <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="delete">删除</a>
    </script>

    <script>
        // 使用Layui的JavaScript模块
        layui.use(['table'], function(){
            var table = layui.table;

            // 渲染表格
            table.render({
                elem: '#friendshipTable', // 指定原始表格元素选择器
                url: '${pageContext.request.contextPath}/me/getMyFriendship', // 数据接口
                page: true, // 开启分页
                cols: [[ // 表头
                    {field: 'friendshipID', title: '好友ID'}, // 列字段
                    {field: 'friendName', title: '好友名'}, // 列字段
                    {title: '操作', align: 'center', toolbar: '#actionBar'} // 列操作按钮
                ]]
            });

            // 监听表格操作栏事件
            table.on('tool(friendshipTable)', function(obj){
                var data = obj.data; // 获取当前行数据
                var layEvent = obj.event; // 获取lay-event属性的值

                // 如果点击了删除按钮
                if(layEvent === 'delete'){
                    // 弹出确认对话框
                    layer.confirm('真的删除好友吗？', function(index){
                        // 发送删除请求到后端
                        // 假设后端接口是DELETE请求
                        $.ajax({
                            url: '${pageContext.request.contextPath}/me/delMyFriendship',//看情况修改
                            type: 'post', // 请求类型
                            success: function(result) {
                                // 检查后端返回的结果是否表示成功
                                if (result.success) {
                                    // 删除成功，移除当前行
                                    obj.del();
                                    // 关闭对话框
                                    layer.close(index);
                                    // 可以显示一个成功消息
                                    layer.msg('删除成功');
                                } else {
                                    // 后端返回错误，显示错误消息
                                    layer.msg('删除失败');
                                }
                            }
                        });
                    });
                }
            });
        });
    </script>

</body>
</html>