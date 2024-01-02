<%--
    Created by IntelliJ IDEA.
    User: 52491
    Date: 2019/12/29
    Time: 15:48
    To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>${myInfo.username} -相册收藏夹</title>
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
        <table class="layui-hide" id="favoritesTable" lay-filter="favorites_table"></table>
    </div>

    <script type="text/html" id="favoritesBarDemo">
        <a class="layui-btn layui-btn-xs" lay-event="check">查看</a>
        <a class="layui-btn layui-btn-xs" lay-event="remove">移除</a>
    </script>

    <script>
        layui.use('table', function(){
            var table = layui.table;

            table.render({
                elem: '#favoritesTable'
                ,url:'${pageContext.request.contextPath}/me/getMyFavorites'
                ,cols: [[
                    {field:'albumID',  title: '相册ID', sort: true}
                    ,{field:'albumName',  title: '相册名',sort:true, templet: '<div><a href="javascript:;" lay-event="viewAlbum">{{d.albumName}}</a></div>'}
                    ,{field:'likesCount',  title: '点赞数',sort:true}
                    ,{field:'description',  title: '相册描述'}
                    ,{field:'favoritesCount',  title: '收藏数', width:80, sort: true}
                    ,{field: 'url', title: '封面',
                        templet: function(d){
                            var url = '${pageContext.request.contextPath}/getCover?url='+encodeURIComponent(d.albumName);
                            return '<div><img  src= "'+url+'" alt="" width="50px" height="50px"></a></div>';
                        },width:80,event :'preview'
                    }
                    ,{fixed: 'right', width:178, align:'center', toolbar: '#favoritesBarDemo'}
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

            table.on('tool(favorites_table)',function (obj) {
                table.on('tool(favorites_table)', function (obj) {
                    if (obj.event === 'check') {
                        var url = '${pageContext.request.contextPath}/albums/album_content?albumId=' + obj.data.albumID;
                        window.open(url, "_blank");
                    } else if (obj.event === 'remove') {
                        var albumName = obj.data.albumName;
                        var albumId = obj.data.albumID;
                        layer.confirm('确定移除该相册「' + albumName + '」吗？', { icon: 3, title: '提示' }, function (index) {
                            // 在此添加移除相册的逻辑
                            layer.close(index);
                        });
                    } else if (obj.event === 'viewAlbum') {
                        var url = '${pageContext.request.contextPath}/albums/album_content?albumId=' + obj.data.albumID;
                        window.open(url, "_blank");
                    }
                });

            });
        });
    </script>
</div>
</body>
</html>
