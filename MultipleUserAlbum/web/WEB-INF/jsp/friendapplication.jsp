<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>好友申请列表</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/layui/css/layui.css" type="text/css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/st-style.css" type="text/css"/>
    <script src="https://libs.baidu.com/jquery/2.1.4/jquery.min.js"></script>
    <script src="${pageContext.request.contextPath}/static/layui/layui.all.js"></script>

</head>
<body class="bg-gray">
<jsp:include page="header.jsp"></jsp:include>
<div class="st-main horizentol" style="margin-top: 15px">
    <jsp:include page="my_left_bar.jsp"></jsp:include>
<div class="layui-container" style="margin-top: 20px;">
    <table id="friendshipTable" lay-filter="friendshipTable"></table>
</div>

<script src="${pageContext.request.contextPath}/static/layui/layui.all.js"></script>
<script>
    layui.use('table', function () {
        var table = layui.table;
        var layer = layui.layer;

        // 渲染表格
        table.render({
            elem: '#friendshipTable',
            url: '${pageContext.request.contextPath}/me/get_application', // 接口地址
            method: 'post',
            page: true,
            cols: [
                [
                    {field: 'userID2', title: '好友申请者ID'},
                    {title: '操作', toolbar: '#actionBar'},
                ]
            ]
            , parseData: function (res) { //res 即为原始返回的数据
                return {
                    "code": res.status, //解析接口状态
                    "msg": res.msg, //解析提示文本
                    "count": res.data.length, //解析数据长度
                    "data": res.data //解析数据列表
                };
            }
        });

        // 监听表格工具条
        table.on('tool(friendshipTable)', function (obj) {
            var data = obj.data;
            var layEvent = obj.event;

            // 如果点击了接受按钮
            if (layEvent === 'accept') {
                layer.msg('已接受好友申请，执行接受逻辑');
                // 这里执行接受好友申请的逻辑，可以发起后端请求，处理相应的业务
                // 例如：$.ajax({url: '接口地址', type: 'post', data: {参数}, success: function(result){}});
            }
            // 如果点击了拒绝按钮
            else if (layEvent === 'reject') {
                layer.msg('已拒绝好友申请，执行拒绝逻辑');
                // 这里执行拒绝好友申请的逻辑，可以发起后端请求，处理相应的业务
                // 例如：$.ajax({url: '接口地址', type: 'post', data: {参数}, success: function(result){}});
            }
        });
    });
</script>

<script type="text/html" id="actionBar">
    <a class="layui-btn layui-btn-xs" lay-event="accept">接受</a>
    <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="reject">拒绝</a>
</script>

</body>
</html>
