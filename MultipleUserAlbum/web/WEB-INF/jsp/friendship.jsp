<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>好友管理</title>
    <!-- 引入Layui的CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/layui/css/layui.css" type="text/css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/st-style.css" type="text/css"/>
    <script src="https://libs.baidu.com/jquery/2.1.4/jquery.min.js"></script>
    <script src="${pageContext.request.contextPath}/static/layui/layui.all.js"></script>
</head>
<body class="bg-gray">
<jsp:include page="header.jsp"></jsp:include>
<div class="st-main horizentol" style="margin-top: 15px">
    <jsp:include page="my_left_bar.jsp"></jsp:include>
    <!-- 外层div，居中展示表格 -->
    <div style="width: 80%; margin: 0 auto;">
        <div class="friendship-table-container">
            <!-- 表格元素 -->

            <div class="right-div">
                <div class="layui-input-group">
                    <div class="layui-input-split layui-input-prefix">
                        请输入用户id，以添加好友
                    </div>
                    <input type="text" placeholder="带任意前置和后置内容" class="layui-input" id="friendInput">

                    <div class="layui-input-suffix">
                        <button type="button" class="layui-btn layui-bg-orange" id="addFriendBtn">添加好友</button>
                    </div>
                </div>
            </div>

            <table id="friendshipTable" lay-filter="friendshipTable"></table>
        </div>
        <!-- 表格操作栏模板 -->
        <script type="text/html" id="actionBar">
            <!-- 删除按钮的HTML代码 -->

            <a class="layui-btn layui-btn-xs" lay-event="albums">公开相册列表</a>
            <a class="layui-btn layui-btn-xs" lay-event="infromation">好友信息</a>
            <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="delete">删除</a>
        </script>

        <script>
            layui.use(['table', 'layer'], function(){
                var table = layui.table;
                var layer = layui.layer;
                // 监听添加好友按钮点击事件
                $('#addFriendBtn').on('click', function () {
                    var friendInputValue = $('#friendInput').val();

                    // 在这里可以根据 friendInputValue 发起请求，向后端提交数据
                    // 例如使用 $.ajax 或其他方式发送请求到后端，进行添加好友的操作
                    // 注意：这里只是个示例，实际操作需要根据后端接口的具体情况进行修改

                    // 示例：假设后端的添加好友接口为 "/addFriend"，并且接收一个参数 friendInputValue
                    $.ajax({
                        url: "${pageContext.request.contextPath}/me/addFriend",
                        type: "post",
                        data: {"friendInputValue": friendInputValue},
                        dataType: "json",
                        success: function (result) {
                            // 根据后端返回的结果进行相应的处理
                            if (result.status == 0) {
                                layer.msg('已经发送请求!', {icon: 6, offset: 250});
                            } else {
                                layer.msg('请求发送失败!', {icon: 5, offset: 250});
                            }
                        },
                        error: function () {
                            alert("添加好友发生异常");
                        }
                    });
                });
                // 渲染表格
                table.render({
                    elem: '#friendshipTable',
                    url: '${pageContext.request.contextPath}/me/getMyFriendship',
                    page: true,
                    cols: [
                        [
                            {field: 'userId', title: '好友ID',sort:true},
                            {field: 'url', title: '头像',
                            templet: function(d){
                                var url='${pageContext.request.contextPath}/getAvatar?username='+encodeURIComponent(d.username);
                                return '<div><img  src= "'+url+'" alt="" width="50px" height="50px"></a></div>';
                            },width:80,event :'preview'
                        },
                            {field: 'username', title: '好友名',sort:true},
                            {field: 'description', title: '好友描述',sort:true},
                            {title: '操作', align: 'center', toolbar: '#actionBar',sort:true,width:240,},

                        ]
                    ]
                    ,page: true
                    ,parseData: function(res){ //res 即为原始返回的数据
                        return {
                            "code": res.status, //解析接口状态
                            "msg": res.msg, //解析提示文本
                            "count": res.data.length, //解析数据长度
                            "data": res.data //解析数据列表
                        };
                    }
                });

                // 监听表格操作栏事件
                table.on('tool(friendshipTable)', function(obj){
                    var data = obj.data;
                    var layEvent = obj.event;

                    // 如果点击了删除按钮
                    if(layEvent === 'delete'){
                        var friendId = obj.data.userId;

                        // 发起删除好友请求
                        $.ajax({
                            url: "${pageContext.request.contextPath}/me/delfriendship",
                            type: "post",
                            data: {"friendid": friendId},
                            dataType: "json",
                            success: function (result) {
                                // 根据后端返回的结果进行相应的处理
                                if (result.status == 0) {
                                    // 如果删除成功
                                    layer.msg('删除成功!', {icon: 6, offset: 250});
                                    // 可以进行一些其他操作，例如刷新页面或重新加载数据
                                } else {
                                    layer.msg('删除失败!', {icon: 5, offset: 250});
                                }
                            },
                            error: function () {
                                alert("删除好友发生异常");
                            }
                        });
                    }
                    else if(obj=='preview')
                    {
                        var url = "${pageContext.request.contextPath}/getCover?url=" + obj.data.albumID;
                        var width = 800;
                        var height = 800;
                        // 创建对象
                        var img = new Image();
                        // 改变图片的src
                        img.src = url;
                        // 判断是否有缓存
                        if(img.complete){
                            // 打印
                            width = img.width>width?width:img.width;
                            height = img.height>height?height:img.height + 42;
                        }else{
                            // 加载完成执行
                            img.onload = function(){
                                width = img.width>width?width:img.width;
                                height = img.height>height?height:img.height + 42;
                            }
                        }
                        width = width + 'px';
                        height = height + 'px';
                        //页面层
                        layer.open({
                            title:'封面预览',
                            type: 1,
                            skin: 'layui-layer-rim', //加上边框
                            area: [width, height], //宽高
                            shadeClose: true, //开启遮罩关闭
                            end: function (index, layero) {
                                return false;
                            },
                            content: '<div style="text-align:center"><img src="'+url+'" /></div>'
                        });
                    }
                });


            });
        </script>
    </div>
</div>
</body>
</html>
