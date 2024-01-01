<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>${myInfo.username} - 创建相册</title>
  <meta charset="UTF-8">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/layui/css/layui.css" type="text/css"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/st-style.css" type="text/css"/>
  <script src="https://libs.baidu.com/jquery/2.1.4/jquery.min.js"></script>
  <script src="${pageContext.request.contextPath}/static/layui/layui.all.js"></script>
</head>
<body class="bg-gray">
<%@ include file="header.jsp" %>
<div class="st-main horizentol" style="margin-top: 15px">
  <%@ include file="my_left_bar.jsp" %>
  <div class="personal-content">
    <form class="layui-form" id="createAlbumForm" lay-filter="createAlbumForm">
      <div class="layui-form-item">
        <label class="layui-form-label">相册名称</label>
        <div class="layui-input-inline">
          <input type="text" name="albumName" id="albumName" class="layui-input" required>
        </div>
      </div>

      <div class="layui-form-item">
        <label class="layui-form-label">相册封面</label>
        <div class="layui-input-inline">
          <button type="button" class="layui-btn" id="uploadCover">上传封面</button>
          <div class="layui-upload-list">
            <img class="layui-upload-img" id="coverImg" style="width: 100px; height: 100px;">
            <p id="coverText"></p>
          </div>
          <input type="hidden" name="coverPath" id="coverPath">
        </div>
      </div>

      <div class="layui-form-item">
        <label class="layui-form-label">上传照片</label>
        <div class="layui-input-inline">
          <button type="button" class="layui-btn" id="uploadPhotos">上传照片</button>
          <div class="layui-upload-list" id="photosList"></div>
          <input type="hidden" name="photoPaths" id="photoPaths">
        </div>
      </div>

      <div class="layui-form-item">
        <div class="layui-input-block">
          <button class="layui-btn" lay-submit lay-filter="createAlbum">创建相册</button>
        </div>
      </div>
    </form>
  </div>
</div>

<script>
  layui.use(['form', 'upload', 'layer'], function () {
    var form = layui.form;
    var upload = layui.upload;
    var layer = layui.layer;

    // 创建相册表单提交
    form.on('submit(createAlbum)', function (data) {
      // 执行相册创建的 AJAX 请求
      $.ajax({
        url: '${pageContext.request.contextPath}/me/createAlbum',
        type: 'POST',
        data: data.field,
        success: function (response) {
          // 处理来自服务器的成功响应
          console.log(response);
          // 在前端展示返回的信息
          layer.alert(response.msg, {icon: response.status === 0 ? 1 : 2}, function () {
            if (response.status === 0) {
              // 创建成功，跳转到相册列表页
              window.location.href = '${pageContext.request.contextPath}/me/albums';
            }
          });
        },
        error: function (error) {
          // 处理来自服务器的错误响应
          console.error(error);
        }
      });

      return false; // 阻止表单跳转
    });

    // 相册封面上传
    upload.render({
      elem: '#uploadCover',
      url: '${pageContext.request.contextPath}/me/uploadCover',
      accept: 'images',
      done: function (res) {
        console.log(res);
        // 处理上传成功的回调
        $('#coverImg').attr('src', res.data.src);
        $('#coverPath').val(res.data.path);
        $('#coverText').html(''); // 清空提示
        layer.msg('封面上传成功');
      },
      error: function (index, upload) {
        console.error(index, upload);
        // 处理上传失败的回调
        $('#coverText').html('<span style="color: #FF5722;">上传失败</span>');
        layer.msg('封面上传失败');
      }
    });

    // 照片上传
    upload.render({
      elem: '#uploadPhotos',
      url: '${pageContext.request.contextPath}/me/uploadPhotos',
      accept: 'images',
      multiple: true,
      number: 10, // 限制上传的照片数量
      done: function (res) {
        console.log(res);
        // 处理上传成功的回调
        var imgHtml = '<img class="layui-upload-img" src="' + res.data.src + '" style="width: 100px; height: 100px;">';
        $('#photosList').append(imgHtml);
        var photoPaths = $('#photoPaths').val() || '';
        photoPaths += res.data.path + ';';
        $('#photoPaths').val(photoPaths);
        layer.msg('照片上传成功');
      },
      error: function (index, upload) {
        console.error(index, upload);
        // 处理上传失败的回调
        layer.msg('照片上传失败');
      }
    });
  });
</script>
</body>
</html>
