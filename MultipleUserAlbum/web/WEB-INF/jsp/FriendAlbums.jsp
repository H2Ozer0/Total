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
  <title>${myInfo.username}好友相册</title>
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
  <div class = "personal-content">
    <table class="layui-hide" id="test" lay-filter="album_table"></table>
  </div>

  <script type="text/html" id="barDemo">
    <a class="layui-btn layui-btn-xs" lay-event="check">查看照片</a>
    <a class="layui-btn layui-btn-xs" lay-event="content">点赞评论</a>

  </script>

  <script>
    layui.use('table', function(){
      var table = layui.table;

      table.render({
        elem: '#test'
        ,url:'${pageContext.request.contextPath}/me/getfriendalbums?friendid='+'${userid}'
        ,cols: [[
          {field:'albumID',  title: '相册ID', sort: true}
          ,{field:'albumName',  title: '相册名',sort:true}
          ,{field:'likesCount',  title: '点赞数',sort:true}
          ,{field:'description',  title: '相册描述'}
          ,{field:'favoritesCount',  title: '收藏数', width:80, sort: true}
          ,{field: 'url', title: '封面',width:120,
            templet: function(d){
              var url = '${pageContext.request.contextPath}/getCover?url='+encodeURIComponent(d.albumName); ;
              return '<div><img  src= "'+url+'" alt="" width="50px" height="50px"></a></div>';
            },width:100,event :'preview'
          }
          ,{fixed: 'right', width:300, align:'center', toolbar: '#barDemo'}
        ]]
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
      table.on('tool(album_table)',function (obj) {
        if(obj.event === 'check'){
          var url = '${pageContext.request.contextPath}/albums/showphotos?albumId=' + obj.data.albumID;
          window.open(url,"_blank");
        }
        else if(obj.event === 'edit'){
          var url = '${pageContext.request.contextPath}/editAlbum?albumId=' + obj.data.albumID;
          window.open(url,"_blank");
        }else if(obj.event === 'del'){
          var albumName = obj.data.albumName;
          var albumId = obj.data.albumID;
          layer.confirm('确定删除该相册「' + albumName +'」吗?该相册下的照片也会一并删除。', {icon: 3, title:'提示'}, function(index){
            $.ajax({
              url: "${pageContext.request.contextPath}/albums/delAlbum",
              type: "post",
              data: {"albumId": albumId},
              dataType: "json",
              success: function (result) {
                //如果删除成功
                if (result.status == 0) {
                  layer.msg('删除成功!', {icon: 6,offset:250});
                  window.location.reload();
                } else {
                  window.location.reload();
                }
              },
              error: function () {
                alert("删除相册发生异常");
              }
            });
            layer.close(index);
          });
        } else if (obj.event === 'preview'){
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
        }else if(obj.event === 'content'){
          var albumName = obj.data.albumName;
          var albumId = obj.data.albumID;


          window.location.href = "${pageContext.request.contextPath}/albums/album_content?albumId=" + albumId;
        }

      })
    });
  </script>
</body>
</html>
