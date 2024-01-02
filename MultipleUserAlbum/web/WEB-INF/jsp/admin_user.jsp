<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>${myInfo.username} -用户管理</title>
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
    <div class="personal-content">
        <table class="layui-hide" id="userTable" lay-filter="user_table"></table>
    </div>

    <script type="text/html" id="barDemo">
        <a class="layui-btn layui-btn-xs" lay-event="check">查看</a>
        <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del">删除</a>
    </script>

    <script>
        layui.use('table', function(){
            var table = layui.table;

            table.render({
                elem: '#userTable'
                ,url:'${pageContext.request.contextPath}/admin/getAllUsers'
                ,cols: [[
                    {field:'userId', title: '用户ID', sort: true}
                    ,{field:'username', title: '用户名', sort: true}
                    ,{field:'email', title: '邮箱'}
                    ,{field:'description', title: '描述'}
                    ,{fixed: 'right', width:178, align:'center', toolbar: '#barDemo'}
                ]]
                ,page: true
                ,parseData: function(res){
                    return {
                        "code": res.status,
                        "msg": res.msg,
                        "count": res.data.length,
                        "data": res.data
                    };
                }
            });

            table.on('tool(user_table)', function (obj) {
                var userId = obj.data.userId;
                console.log(userId);
                if(obj.event === 'check'){
                    window.open("${pageContext.request.contextPath}/admin/getUserInfo?userId=" + userId);
                } else if(obj.event === 'del'){

                    layer.confirm('确定删除用户吗?', {icon: 3, title: '确认删除'}, function(index) {
                        $.ajax({
                            url: "${pageContext.request.contextPath}/admin/delUser",
                            type: "post",
                            data: {"userId": userId},
                            dataType: "json",
                            success: function (result) {
                                if (result.status == 0) {
                                    layer.msg('删除成功!', {icon: 6, offset: 250});

                                } else {
                                    console.error("删除用户异常", result.msg);
                                }
                            },
                            error: function () {

                                alert("删除用户发生异常");
                            }
                        });

                        layer.close(index);
                    });
                }
            });
        });
    </script>
</body>
</html>
