<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>${myInfo.username} - 查看相册照片</title>
  <meta charset="UTF-8">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/layui/css/layui.css" type="text/css"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/st-style.css" type="text/css"/>
  <script src="https://libs.baidu.com/jquery/2.1.4/jquery.min.js"></script>
  <script src="${pageContext.request.contextPath}/static/layui/layui.all.js"></script>
  <style>
    /* 新增样式用于居中显示大图 */
    .large-photo-container {
      text-align: center;
      display: flex;
      align-items: center;
      justify-content: center;
    }

    /* 新增样式用于隐藏删除按钮 */
    .delete-button {
      display: none;
    }

    /* 新增样式用于 showing 删除按钮 */
    .photo-item:hover .delete-button {
      display: block;
    }

    /* 新增样式用于定位删除按钮 */
    .delete-button {
      position: absolute;
      top: 0;
      right: 0;
      padding: 5px;
      cursor: pointer;
      background-color: #fff; /* Optional: Adjust the background color as needed */
    }

    /* 新增样式用于调整图片大小和间距 */
    .photo-item {
      width: 22%; /* 设置为 22% 以确保每行四张图，留出一些空隙 */
      margin: 1%; /* 设置为 1% 以在图片之间添加一些间距 */
      position: relative;
    }

    .photo-image {
      width: 100%;
      max-width: 100%;
      height: auto;
    }
  </style>
</head>
<body class="bg-gray">
<jsp:include page="header.jsp"></jsp:include>
<div class="st-main horizentol" style="margin-top: 15px">
  <div class="personal-content">
    <!-- 照片列表容器 -->
    <div id="photoContainer" class="layui-row"></div>
  </div>

  <script>
    layui.use('table', function(){
      var table = layui.table;

      // 通过 Ajax 请求获取照片列表数据
      $.ajax({
        url: '${pageContext.request.contextPath}/albums/getPhotos?albumId=${albumId}',
        type: 'get',
        dataType: 'json',
        success: function(res) {
          // 处理获取的照片数据，展示照片
          renderPhotos(res.data);
        },
        error: function() {
          // 处理错误情况
          layer.msg('获取照片失败', {icon: 5, offset: 250});
        }
      });

      // 函数：渲染照片列表
      function renderPhotos(photos) {
        var photoContainer = $('#photoContainer');

        // 遍历照片数据，生成照片元素并添加到页面中
        $.each(photos, function(index, photo) {
          // 创建照片元素
          var photoElement = $('<div>', {
            class: 'photo-item layui-col-md3 layui-col-xs6',
          });

          var imgElement = $('<img>', {
            src: '${pageContext.request.contextPath}/getphoto?path=' + encodeURIComponent(photo.path).replace(/\\/g, '/'),
            alt: photo.title,
            title: photo.title,
            class: 'photo-image',
          });

          var deleteButton = $('<div>', {
            class: 'delete-button',
            text: '删除',
          });
          var myInfoUserId = '${myInfo.userId}';
          var albumCreatorID = '${albumId}';
          console.log(myInfoUserId);
          console.log(albumCreatorID);
          // 判断是否显示删除按钮
          if (myInfoUserId && myInfoUserId == albumCreatorID) {
            // 点击删除按钮时触发事件
            deleteButton.click(function(event) {
              event.stopPropagation(); // Prevent the click event from propagating to the photoElement
              deletePhoto(photo);
            });
          } else {
            deleteButton.hide(); // 隐藏删除按钮
          }

          // 点击照片时触发事件，显示大图
          photoElement.click(function() {
            showLargePhoto(photo);
          });

          // 将照片元素添加到容器中
          photoElement.append(imgElement, deleteButton);
          photoContainer.append(photoElement);
        });
      }

      // 函数：显示大图
      function showLargePhoto(photo) {
        var url = '${pageContext.request.contextPath}/getphoto?path=' + encodeURIComponent(photo.path).replace(/\\/g, '/');

        // 创建对象
        var img = new Image();
        img.src = url;

        // 判断是否有缓存
        if (img.complete) {
          handleLargePhoto(img.width, img.height);
        } else {
          img.onload = function() {
            handleLargePhoto(img.width, img.height);
          }
        }

        // 处理显示大图的逻辑
        function handleLargePhoto(originalWidth, originalHeight) {
          var maxWidth = 800; // 设置最大宽度
          var maxHeight = 800; // 设置最大高度

          var width, height;

          // 计算缩放比例，保持宽高比例一致
          var widthRatio = originalWidth / maxWidth;
          var heightRatio = originalHeight / maxHeight;
          var maxRatio = Math.max(widthRatio, heightRatio);

          // 计算缩放后的宽高
          width = originalWidth / maxRatio;
          height = originalHeight / maxRatio;

          width = width + 'px';
          height = height + 'px';

          // 页面层
          layer.open({
            title: '照片查看',
            type: 1,
            skin: 'layui-layer-rim', // 加上边框
            area: ['auto', 'auto'], // 自适应内容大小
            offset: 'auto', // 自动居中
            shadeClose: true, // 开启遮罩关闭
            end: function(index, layero) {
              return false;
            },
            content: '<div class="large-photo-container"><img src="' + url + '" /></div>'
          });
        }
      }

      // 函数：删除照片
      function deletePhoto(photo) {
        // 实现删除的逻辑，可以使用 layer.confirm 或其他方式
        layer.confirm('确定删除该照片吗？', {icon: 3, title:'提示'}, function(index){
          // Get the photo ID and path from the current photo
          var photoID = photo.photoID;
          var path = photo.path;

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
                layer.msg('删除成功!', { icon: 6, offset: 250 }, function(){
                  layer.closeAll(); // 关闭所有layer弹窗
                  table.reload('test'); // Reload the table to reflect the changes
                });

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
      }
    });
  </script>
</div>
</body>
</html>
