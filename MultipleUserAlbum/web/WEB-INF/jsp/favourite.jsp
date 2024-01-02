<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>

<head>
    <title>${myInfo.username} -相册收藏夹</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/layui/css/layui.css" type="text/css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/st-style.css" type="text/css"/>
    <script src="https://libs.baidu.com/jquery/2.1.4/jquery.min.js"></script>
    <script src="${pageContext.request.contextPath}/static/layui/layui.all.js"></script>
    <style>
        .layui-table-view .layui-table {
            margin-top: 20px; /* 调整行间距的大小，可以根据需要进行调整 */
        }

        .layui-table-cell {
            height: 60px; /* 调整每行的高度，可以根据需要进行调整 */
            line-height: 40px; /* 使文本垂直居中，可以根据需要进行调整 */
        }
        .custom-btn {
            background-color: #336699; /* 自定义颜色的背景色 */
            color: white; /* 文字颜色 */
        }
        .custom-btn2 {
            background-color: #990033; /* 自定义颜色的背景色 */
            color: white; /* 文字颜色 */
        }
    </style>
</head>
<body class="bg-gray">
<jsp:include page="header.jsp"></jsp:include>
<div class="st-main horizentol" style="margin-top: 15px">
    <jsp:include page="my_left_bar.jsp"></jsp:include>
    <div class="personal-content">
        <table class="layui-hide" id="favoritesTable" lay-filter="favorites_table"></table>
    </div>

    <script type="text/html" id="favoritesBarDemo">
        <a class="layui-btn custom-btn layui-btn-xs" lay-event="check">查看</a>
        <a class="layui-btn custom-btn layui-btn-xs" lay-event="remove">取消收藏</a>
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
                            var url = '${pageContext.request.contextPath}/getCover?url='+encodeURIComponent(d.albumID);
                            console.log(url)
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
                        layer.confirm('确定取消收藏吗?', {icon: 3, title:'提示',offset:'250px'}, function(index){
                            $.ajax({
                                url: "${pageContext.request.contextPath}/favorites/delete",
                                type: "post",
                                data: {"AID":albumId},
                                dataType: "json",
                                success: function (result) {
                                    console.log(result.status);
                                    console.log(result.data);
                                    //如果删除成功
                                    if (result.status == 0) {
                                        layer.msg('取消收藏成功!', {icon: 6,offset:250});
                                        window.location.reload();
                                    } else {
                                        window.location.reload();
                                    }
                                },
                                error: function () {
                                    alert("取消收藏发生异常");
                                }
                            });
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
