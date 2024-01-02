<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>${myInfo.username} -查看相册照片</title>
  <meta charset="UTF-8">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/layui/css/layui.css" type="text/css"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/st-style.css" type="text/css"/>
  <script src="https://libs.baidu.com/jquery/2.1.4/jquery.min.js"></script>
  <script src="${pageContext.request.contextPath}/static/layui/layui.all.js"></script>
</head>
<body class="bg-gray">
<jsp:include page="header.jsp"></jsp:include>
<div class="st-main horizentol" style="margin-top: 15px">
  <div class="personal-content">
    <table class="layui-hide" id="test" lay-filter="photo_table"></table>
  </div>

  <script type="text/html" id="barDemo">
    <a class="layui-btn layui-btn-xs" lay-event="preview">查看照片</a>
    <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="delete">删除</a>
    <!-- 其他操作按钮... -->
  </script>

  <script>
    layui.use('table', function(){
      var table = layui.table;

      table.render({
        elem: '#test'
        ,url:'${pageContext.request.contextPath}/albums/getPhotos?albumId=${albumId}'
        ,cols: [[
          {field:'photoID',  title: '照片ID', sort: true}
          ,{field:'title',  title: '名称', sort:true}
          ,{field: 'url', title: '预览',
            templet: function(d){
              var url = '${pageContext.request.contextPath}/getphoto?path='+encodeURIComponent(d.path).replace(/\\/g, '/');
              return '<div><img  src= "'+url+'" alt="" width="50px" height="50px"></a></div>';
            },width:80,event :'preview'
          }
          ,{fixed: 'right', width:260, align:'center', toolbar: '#barDemo'}
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
      table.on('tool(photo_table)',function (obj) {
        if(obj.event === 'preview'){
          // 实现预览的逻辑，可以使用 layer.open 或其他方式
          var url = "${pageContext.request.contextPath}/getphoto?path=" +  obj.data.path.replace(/\\/g, '/');
          var width = 800;
          var height = 800;

          // 创建对象
          var img = new Image();
          img.src = url;

          // 判断是否有缓存
          if(img.complete){
            width = img.width>width?width:img.width;
            height = img.height>height?height:img.height + 42;
          } else {
            img.onload = function(){
              width = img.width>width?width:img.width;
              height = img.height>height?height:img.height + 42;
            }
          }

          width = width + 'px';
          height = height + 'px';

          // 页面层
          layer.open({
            title:'照片查看',
            type: 1,
            skin: 'layui-layer-rim', // 加上边框
            area: [width, height], // 宽高
            shadeClose: true, // 开启遮罩关闭
            end: function (index, layero) {
              return false;
            },
            content: '<div style="text-align:center"><img src="'+url+'" /></div>'
          });
        } else if(obj.event === 'delete'){
          // 实现删除的逻辑，可以使用 layer.confirm 或其他方式
          layer.confirm('确定删除该照片吗？', {icon: 3, title:'提示'}, function(index){
            // Get the photo ID and path from the current row
            var photoID = obj.data.photoID;
            var path = obj.data.path;

            // Ajax request to delete the photo
            $.ajax({
              url: "${pageContext.request.contextPath}/photos/deletephoto",
              type: "post",
              data: {
                "photoId": photoID,
                "path": path
              },
              dataType: "json",
              success: function(result) {
                // Handle the result after the deletion
                if (result.status == 0) {
                  layer.msg('删除成功!', {icon: 6, offset: 250});
                  table.reload('test'); // Reload the table to reflect the changes
                } else {
                  layer.msg('删除失败: ' + result.msg, {icon: 5, offset: 250});
                }
              },
              error: function() {

                layer.msg('删除照片发生异常', {icon: 5, offset: 250});
              }
            });

            layer.close(index);
          });
        }else if (obj.event === 'preview'){
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
      })
    });
  </script>


</div>
</body>
</html>
